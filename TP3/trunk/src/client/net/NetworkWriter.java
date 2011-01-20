package client.net;

import java.io.PrintWriter;

import util.Command;
import util.Buffer;
import util.Command.COMMANDS;

/**
 * Reads commands (formatted appropriately) to send to the server off the
 * CommandBuffer and sends to the server
 */

public class NetworkWriter implements Runnable {

	private Buffer<Command> buffer;
	private PrintWriter writer;

	public NetworkWriter(PrintWriter writer, Buffer<Command> buffer) {
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
