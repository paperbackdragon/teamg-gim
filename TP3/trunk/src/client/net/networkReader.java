package client.net;

import java.io.BufferedReader;
import java.io.IOException;

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
			
			if (cmd.getArumentsAsString().equalsIgnoreCase("USERS")) {
				gui.usercount(cmd.getData());
				
			}
			
			else if (cmd.getArumentsAsString().equalsIgnoreCase("TIME")) {
				gui.servertime(cmd.getData());
			}
			
			else if (cmd.getArumentsAsString().equalsIgnoreCase("UPTIME")) {
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
			
			if (cmd.getArumentsAsString().equalsIgnoreCase("LOGGEDIN")) {
				gui.authorised();
			}
			else if (cmd.getArumentsAsString().equalsIgnoreCase("UNAUTHORIZED")) {
				gui.unauthorised();
			}
			
		}
		
		// post login
		
		
		
		
		
		
		
	}

}
	

