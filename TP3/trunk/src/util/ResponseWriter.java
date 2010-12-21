package util;

import java.io.PrintWriter;

import util.Command.COMMANDS;

/**
 * The response writer reads commands off a CommandBuffer and sends them across
 * the network.
 * 
 */
public class ResponseWriter implements Runnable {

	private PrintWriter out;
	private CommandBuffer<Command> responseBuffer;

	public ResponseWriter(PrintWriter out, CommandBuffer<Command> cmdBuffer) {
		this.out = out;
		this.responseBuffer = cmdBuffer;
	}

	public void run() {

		// Send out any responses in the buffer
		while (true) {
			Command response = responseBuffer.getCommand();
			out.println(response.toString());

			System.out.println(response.toString());

			// Check we didn't just kill the connection
			if (response.getCommandAsEnum() == COMMANDS.KILL)
				break;
		}

		System.out.println("ResponseWriter killed.");
	}

}
