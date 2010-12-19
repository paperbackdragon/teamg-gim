package util;

import java.io.BufferedReader;
import java.io.IOException;


/**
 * The command reader reads in commands from a BufferedReader and put them into
 * a CommandBuffer
 */
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
				break;
			}

			if (line == null) {
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
					
					// Create the command and put it in the buffer
					Command cmd = new Command(command, args, data.trim());
					commandBuffer.putCommand(cmd);

					// Remove or comment out if not debugging
					System.out.println(cmd);

					// Reset variables for the next command
					command = null;
					args = null;
					data = "";
				}
			}

		}
		
		// Cleaup
		commandBuffer.putCommand(new Command("QUIT"));
		System.out.println("CommandReader stopped.");
		
	}
}
