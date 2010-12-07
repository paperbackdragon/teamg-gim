package server;

import java.io.BufferedReader;
import java.io.IOException;

import server.util.CommandBuffer;

public class CommandReader implements Runnable {

	private BufferedReader in;
	private CommandBuffer<Command> commandBuffer;

	/**
	 * The constructor
	 * 
	 * @param in
	 *            the input to read the commands from
	 * @param commandBuffer
	 *            the buffer to put the commands into
	 */
	public CommandReader(BufferedReader in, CommandBuffer<Command> commandBuffer) {
		this.in = in;
		this.commandBuffer = commandBuffer;
	}

	@Override
	public void run() {
		String command = null, args = null, data = "", line = "";
		int firstColon, secondColon, cmdStop, semiColon;

		while (true) {
			firstColon = -1;
			secondColon = -1;
			cmdStop = -1;
			
			try {
				line = in.readLine();
			} catch (IOException e) {
				// TODO: Cleanup, the client just disconnected.
			}

			semiColon = line.indexOf(';');
			
			if (command == null) {
				firstColon = line.indexOf(':');
				cmdStop = line.indexOf(' ', firstColon + 1);
				secondColon = line.indexOf(':', firstColon + 1);
				
				// Invalid command structure, missing a colon
				if(firstColon == -1 || secondColon == -1)
					continue;
				
				// There aren't any arguments in this command
				if(cmdStop == -1)
					cmdStop = secondColon;
				
				// The colons are in sensible places
				if(cmdStop <= secondColon && cmdStop >= firstColon) {
					command = line.substring(firstColon + 1, cmdStop);
					args = line.substring(cmdStop, secondColon);
				} else {
					// I'm not even sure how this could happen
					command = "NULL";
				}
				line = line.substring(secondColon + 1);
			}
			
			data += line + "\n";
			
			if (semiColon != -1) {
				// Strip the semi-colon
				data = data.substring(0, data.length() - 2);
				
				runCommand(new Command(command, args, data));

				// Reset everything
				command = null;
				args = null;
				data = "";
			}

		}
	}

	public boolean runCommand(Command cmd) {
		commandBuffer.putResponse(cmd);
		return true;
	}

}
