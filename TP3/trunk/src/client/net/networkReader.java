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
				//commandBuffer.putCommand(new Command("QUIT"));
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
					data += line;
				} else if (dataParts.length >= 2) {
					// End of a command
					data += dataParts[0];
					
					// Create the command and out in in the buffer
					Command cmd = new Command(command, args, data.trim());
					
					performCommand(cmd);
					
					//commandBuffer.putCommand(cmd);

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
		
		if (cmd.getCommand().equals("OKAY")) {
			gui.okay();
		}
		
		
		else if (cmd.getCommand().equalsIgnoreCase("SERVERSTATUS")) {
			
			if (cmd.getArgumentsAsString().equalsIgnoreCase("USERS")) {
				gui.usercount(cmd.getData());
				
			}
			
			else if (cmd.getArgumentsAsString().equalsIgnoreCase("TIME")) {
				gui.servertime(cmd.getData());
			}
			
			else if (cmd.getArgumentsAsString().equalsIgnoreCase("UPTIME")) {
				gui.servertime(cmd.getData());
			}
			
		}
		

		else if (cmd.getCommand().equals("KILL")) {
			gui.kill(cmd.getData());
		}
		
		else if (cmd.getCommand().equals("BROADCAST")) {
			gui.broadcast(cmd.getData());
			
		}
		
		else if (cmd.getCommand().equals("AUTH")) {
			
			if (cmd.getArgumentsAsString().equalsIgnoreCase("LOGGEDIN")) {
				gui.authorised();
			}
			else if (cmd.getArgumentsAsString().equalsIgnoreCase("UNAUTHORIZED")) {
				gui.unauthorised();
			}
			
		}
		
		// post login
		
		else if (cmd.getCommand().equals("MESSAGE")) {
			String data = cmd.getData();
			String[] parts = data.split(" ");
			
			String roomid = parts[0];
			String sender = parts[1];
			
			String message = "";
			for (int i = 2; i < parts.length; i++ ) {
				message += parts[i] + " ";
			}
			gui.message(roomid, sender, message);
		}
		
		else if (cmd.getCommand().equals("ROOM")) {
			
			if (cmd.getArgumentsAsString().equalsIgnoreCase("CREATED")) {
				gui.created(cmd.getData());
			}
			
			else if (cmd.getArgumentsAsString().equalsIgnoreCase("JOINED")) {
				String data = cmd.getData();
				String[] parts = data.split(" ");
				gui.joined(parts[1], parts[0]);
			}
			
			else if (cmd.getArgumentsAsString().equalsIgnoreCase("LEFT")) {
				String data = cmd.getData();
				String[] parts = data.split(" ");
				gui.left(parts[1], parts[0]);
			}
			
			else if (cmd.getArgumentsAsString().equalsIgnoreCase("INVITED")) {
				String data = cmd.getData();
				String[] parts = data.split(" ");
				gui.invited(parts[1], parts[0]);
			}
			
			else if (cmd.getArgumentsAsString().equalsIgnoreCase("USERS")) {
				String data = cmd.getData();
				String[] parts = data.split(" ");
				
				String roomid = parts[0];
				
				ArrayList<String> users = new ArrayList<String>();
				for (int i = 1; i < parts.length; i++ ) {
					users.add(parts[i]);
				}
				gui.users(users, roomid);
			}
			
			else if (cmd.getCommand().equalsIgnoreCase("FRIENDLIST")) {
				
				String data = cmd.getData();
				
				// fix case issues later
				
				String online = data.split("OFFLINE")[0];
				online = online.replace("ONLINE","");
				String[] onlinelist = online.split(" ");
				
				String offline = data.split("OFFLINE")[1].split("BLOCKED")[0];
				String[] offlinelist = offline.split(" ");
				 
				String blocked = data.split("BLOCKED")[1];
				String[] blockedlist = blocked.split(" ");
				
				gui.friendlist(onlinelist, offlinelist, blockedlist);
			}
			
			else if (cmd.getCommand().equalsIgnoreCase("FRIENDREQUEST")) {
				String data = cmd.getData();
				String[] parts = data.split(" ");
				
				gui.friendrequest(parts[0], parts[1]);	
			}
			
			else if (cmd.getCommand().equalsIgnoreCase("UPDATE")) {
				
				if (cmd.getArgumentsAsString().equalsIgnoreCase("NICKNAME")) {
					gui.notifyNickname(cmd.getData());
				}
				
				if (cmd.getArgumentsAsString().equalsIgnoreCase("STATUS")) {
					gui.notifyStatus(cmd.getData());
				}
				
				if (cmd.getArgumentsAsString().equalsIgnoreCase("PERSONAL_MESSAGE")) {
					gui.notifyPersonalMessage(cmd.getData());
				}
				
				if (cmd.getArgumentsAsString().equalsIgnoreCase("DISPLAY_PIC")) {
					gui.notifyDisplayPicture(cmd.getData());
				}
				
			}
			
			else if (cmd.getCommand().equalsIgnoreCase("INFO")) {
				
				
				
				
				
			}
			
			
			// ERRORS
			
			else if (cmd.getCommand().equals("ERROR")) {
				
				
				
			}
			
			
			
		}
		
		
		
	}

}
	

