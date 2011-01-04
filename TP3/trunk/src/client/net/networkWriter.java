package client.net;

import java.io.PrintWriter;

import util.Command;
import util.CommandBuffer;
import util.Command.COMMANDS;

/**
 * Reads commands (formatted appropriately) to send to the server off the
 * CommandBuffer and sends to the server
 */

public class networkWriter implements Runnable {

	private CommandBuffer<Command> buffer;
	private PrintWriter writer;

	public networkWriter(PrintWriter writer, CommandBuffer<Command> buffer) {
		this.buffer = buffer;
		this.writer = writer;
	}

	@Override
	public void run() {

		// Send out any responses in the buffer
		while (true) {

			Command currentCommand = buffer.getCommand();
			writer.println(currentCommand);
			
			// Check if we just killed the connection
			if (currentCommand.getCommandAsEnum() == COMMANDS.QUIT)
				break;

			// System.out.println(response.toString());

		}

		System.out.println("ResponseWriter killed.");
		
	}

}
