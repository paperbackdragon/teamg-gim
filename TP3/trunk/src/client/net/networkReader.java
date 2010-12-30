package client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import util.Command;
import util.CommandBuffer;

public class networkReader implements Runnable {

	private BufferedReader reader;
	private ServerConnection gui;

	/** Reads commands off the network and performs calls the necessary method */
	public networkReader(BufferedReader reader, ServerConnection gui) {

		this.gui = gui;
		this.reader = reader;

	}

	@Override
	public void run() {
		String command = null, args = null, data = "", line = "";
		String[] parts, cmdParts, dataParts;

		while (true) {

			try {
				line = reader.readLine();
			} catch (IOException e) {
				break;
			}

			if (line == null) {
				System.out.println("Client closed connection.");
				// commandBuffer.putCommand(new Command("QUIT"));
				break;
			}

			if (command == null) {
				parts = line.split(":");

				// parts[0]: Junk data before the first :, we can throw it away
				// parts[1]: The command name and its arguments
				// parts[2]: The data that goes with the command
				if (parts.length == 3) {

					cmdParts = parts[1].split(" ", 2);
					if (cmdParts.length == 2) {
						// There is a command and it has arguments
						command = cmdParts[0];
						args = cmdParts[1];
					} else if (cmdParts.length == 1) {
						// Just the command, no arguments
						command = cmdParts[0];
					}

					// Strip the command and its arguments off of the line, the
					// rest is data
					line = parts[2];

				}

			}

			// Don't proceed if we didn't find a command in the last line
			if (command != null) {
				// Look for the end of a command
				dataParts = line.split(";", 2);

				// No ;, not the end of a command
				if (dataParts.length == 1) {
					data += "\n" + line;
				} else if (dataParts.length >= 2) {
					// End of a command
					data += "\n" + dataParts[0];

					// Create the command and out in in the buffer
					Command cmd = new Command(command, args, data.trim());

					performCommand(cmd);

					// commandBuffer.putCommand(cmd);

					// Remove or comment out if not debugging
					System.out.println(cmd);

					// Reset variables for the next command
					command = null;
					args = null;
					data = "";
				}
			}

		}

		// TODO: Clean up
		System.out.println("CommandReader stopped.");

	}

	private void performCommand(Command cmd) {

		if (cmd.getCommand().equalsIgnoreCase("OKAY")) {
			gui.okay();
		}

		else if (cmd.getCommand().equalsIgnoreCase("SERVERSTATUS")) {

			if (cmd.getArgumentsAsString().equalsIgnoreCase("USERS")) {
				gui.usercount(Command.decode(cmd.getData()));

			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("TIME")) {
				gui.servertime(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("UPTIME")) {
				gui.servertime(Command.decode(cmd.getData()));
			}

		}

		else if (cmd.getCommand().equalsIgnoreCase("KILL")) {
			gui.kill(Command.decode(cmd.getData()));
		}

		else if (cmd.getCommand().equalsIgnoreCase("BROADCAST")) {
			gui.broadcast(Command.decode(cmd.getData()));

		}

		else if (cmd.getCommand().equalsIgnoreCase("AUTH")) {

			if (cmd.getArgumentsAsString().equalsIgnoreCase("LOGGEDIN")) {
				gui.authorised();
			} else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"UNAUTHORIZED")) {
				gui.unauthorised();
			} else if (cmd.getArgumentsAsString()
					.equalsIgnoreCase("REGISTERED")) {
				gui.registered();
			}
		}

		// post login

		else if (cmd.getCommand().equalsIgnoreCase("MESSAGE")) {
			System.out.println("received a message");

			String[] parts = cmd.splitAndDecodeData(" ");
			gui.message(parts[0], parts[1], parts[2]);
		}

		else if (cmd.getCommand().equalsIgnoreCase("ROOM")) {

			if (cmd.getArgumentsAsString().equalsIgnoreCase("CREATED")) {
				gui.created(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("JOINED")) {
				String data = cmd.getData();
				String[] parts = data.split(" ");
				gui.joined(Command.decode(parts[1]), Command.decode(parts[0]));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("LEFT")) {
				String data = cmd.getData();
				String[] parts = data.split(" ");
				gui.left(Command.decode(parts[1]), Command.decode(parts[0]));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("INVITED")) {
				String data = cmd.getData();
				String[] parts = data.split(" ");
				gui.invited(Command.decode(parts[1]), Command.decode(parts[0]));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("USERS")) {
				String data = cmd.getData();

				String[] parts = data.split(" ");

				String roomid = Command.decode(parts[0]);

				String[] users = parts[1].split("\n");

				for (int i = 0; i < users.length; i++) {
					users[i] = Command.decode(users[i]);
				}

				gui.users(users, roomid);
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("PERSONAL")) {
				gui.personal(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("GROUP")) {
				gui.group(Command.decode(cmd.getData()));
			}

		}

		else if (cmd.getCommand().equalsIgnoreCase("FRIENDLIST")) {

			String data = cmd.getData();

			// fix case issues later

			String online = data.split("OFFLINE")[0];
			online = online.replaceFirst("ONLINE ", "");
			String[] onlinelist = online.split(" ");

			System.out.println("ONLINE: ");
			for (int i = 0; i < onlinelist.length; i++) {
				onlinelist[i] = Command.decode(onlinelist[i]);
				// System.out.println(onlinelist[i]);
			}

			String offline = data.split("OFFLINE")[1].split("BLOCKED")[0];
			offline = offline.replaceFirst(" ", "");
			String[] offlinelist = offline.split(" ");

			System.out.println("OFFLINE: ");
			for (int j = 0; j < offlinelist.length; j++) {
				offlinelist[j] = Command.decode(offlinelist[j]);
				// System.out.println(offlinelist[j]);
			}

			String blocked = data.split("OFFLINE")[1].split("BLOCKED")[0];
			blocked = blocked.replaceFirst(" ", "");
			String[] blockedlist = blocked.split(" ");

			System.out.println("BLOCKED: ");
			for (int k = 0; k < blockedlist.length; k++) {
				blockedlist[k] = Command.decode(blockedlist[k]);
				// System.out.println(blockedlist[k]);
			}

			gui.friendlist(onlinelist, offlinelist, blockedlist);
		}

		else if (cmd.getCommand().equalsIgnoreCase("FRIENDREQUEST")) {
			String data = cmd.getData();
			String[] parts = data.split(" ");

			gui.friendrequest(Command.decode(parts[0]), Command
					.decode(parts[1]));
		}

		else if (cmd.getCommand().equalsIgnoreCase("UPDATE")) {

			if (cmd.getArgumentsAsString().equalsIgnoreCase("FRIENDLIST")) {
				gui.notifyFriendsList();
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("NICKNAME")) {
				gui.notifyNickname(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("STATUS")) {
				gui.notifyStatus(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"PERSONAL_MESSAGE")) {
				gui.notifyPersonalMessage(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase("DISPLAY_PIC")) {
				gui.notifyDisplayPicture(Command.decode(cmd.getData()));
			}

		}

		else if (cmd.getCommand().equalsIgnoreCase("INFO")) {

			String[] arguments = cmd.getArguments();
			int argumentcount = arguments.length;

			String data = cmd.getData();
			String[] parts = data.split("\n");

			int i = 0;
			int count = 0;

			String currentusername = parts[0];
			while (i < parts.length) {

				if (count != 0) {
					// note to self: lol.. eh... do this better. this probably won't work for
					// multiple madness
					count--;

					if (arguments[count].equalsIgnoreCase("NICKNAME")) {

						gui.updateNickname(currentusername,Command.decode(parts[i])
								);

					}

					else if (arguments[count].equalsIgnoreCase("STATUS")) {
						gui.updateStatus(currentusername, Command.decode(parts[i])
								);
						
						System.out.println("got where i wanted");

					}

					else if (arguments[count]
							.equalsIgnoreCase("PERSONAL_MESSAGE")) {
						gui.updatePersonalMessage(currentusername, Command.decode(parts[i])
								);

					}

					else if (arguments[count].equalsIgnoreCase("DISPLAY_PIC")) {
						gui.updateDisplayPicture(currentusername,Command.decode(parts[i])
								);
					}

				}

				else { // it's a username
					currentusername = Command.decode(parts[i]);
				}

				i++;

				if (count == (argumentcount + 1)) {
					count = 0;
				} else {
					count++;
				}

			}

		}

		// ERRORS

		else if (cmd.getCommand().equals("ERROR")) {

			if (cmd.getArgumentsAsString().equalsIgnoreCase("UNAUTHORISED")) {
				gui.unauthorisedError(Command.decode(cmd.getData()));

			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"INVALID_EMAIL")) {
				gui.invalidEmailError(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"EMAIL_ALREADY_IN_USE")) {
				gui.emailInuseError(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"PASSWORD_TOO_SHORT")) {
				gui.passwordTooShortError(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"MISSING_ARGUMENTS")) {
				gui.missingArgumentsError(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"TOO_MANY_ARGUMENTS")) {
				gui.tooManyArgumentsError(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"INVALID_ARGUMENT")) {
				gui.invalidArgumentError(Command.decode(cmd.getData()));

			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"LOGGED_IN_FROM_OTHER_LOCATION")) {
				gui.loggedInFromAnotherLocationError(Command.decode(cmd
						.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"USER_DOES_NOT_EXIST")) {
				gui.userDoesNotExistError(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"LOGIN_DETAILS_INCORRECT")) {
				gui.logInDetailsIncorrectError(Command.decode(cmd.getData()));
			}

			else if (cmd.getArgumentsAsString().equalsIgnoreCase(
					"INVALID_USER_SUPPLIED")) {
				gui.invalidUserError(Command.decode(cmd.getData()));
			}
			
			else if (cmd.getArgumentsAsString().equalsIgnoreCase("USER_OFFLINE")) {
				gui.userOfflineError(Command.decode(cmd.getData()));
			}

		}

	}

}
