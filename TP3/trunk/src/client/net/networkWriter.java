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

	private CommandBuffer buffer;
	private PrintWriter writer;

	public networkWriter(PrintWriter writer, CommandBuffer buffer) {

		this.buffer = buffer;
		this.writer = writer;

	}

	@Override
	public void run() {

		// Send out any responses in the buffer
		while (true) {

			String currentcommand = (String) buffer.getCommand();
			
			

			// check if connection was lost
			if (currentcommand.equals("STOP")) {
				break;
			}
			
			writer.println(currentcommand);
			
			// Check if we just killed the connection
			if (currentcommand.equals(":QUIT:;")) {
				break;
			}

			// System.out.println(response.toString());

			
		}

		System.out.println("ResponseWriter killed.");
	}

}
