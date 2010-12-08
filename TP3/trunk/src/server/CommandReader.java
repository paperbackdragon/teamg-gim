package server;

import java.io.BufferedReader;
import java.io.IOException;

import server.util.CommandBuffer;

public class CommandReader implements Runnable {

	private BufferedReader in;
	private CommandBuffer<Command> commandBuffer;

	/**
	 * The constructor, 'cos constructing is cool.
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

	/**
	 * Read commands from the input stream and put them into the command buffer
	 */
	@Override
	public void run() {
		String command = null, args = null, data = "", line = "";
		String[] parts, cmdParts, dataParts;

		while (true) {

			try {
				line = in.readLine();
			} catch (IOException e) {
				// TODO: Cleanup, the client just disconnected.
			}

			if (command == null) {
				parts = line.split(":");

				if (parts.length == 3) {

					cmdParts = parts[1].split(" ", 2);
					if (cmdParts.length == 2) {
						command = cmdParts[0];
						args = cmdParts[1];
					} else if (cmdParts.length == 1) {
						command = cmdParts[0];
					}

					line = parts[2];

				}

			}

			if (command != null) {
				dataParts = line.split(";", 2);

				// No ;, not the end of a command
				if (dataParts.length == 0) {
					data += line;
				} else if (dataParts.length >= 2) {
					// End of a command
					data += dataParts[0];

					commandBuffer.putCommand(new Command(command, args, data));
					System.out.println(new Command(command, args, data));

					command = null;
					args = null;
					data = "";
				}
			}

		}
	}
}
