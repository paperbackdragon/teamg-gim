package server;

import java.io.PrintWriter;

import util.Command;
import util.Buffer;
import util.Command.COMMANDS;

/**
 * The response writer reads commands off a CommandBuffer and sends them across
 * the network.
 * 
 */
public class ResponseWriter implements Runnable {

	private PrintWriter out;
	private Buffer<Command> responseBuffer;

	public ResponseWriter(PrintWriter out, Buffer<Command> cmdBuffer) {
		this.out = out;
		this.responseBuffer = cmdBuffer;
	}

	public void run() {

		// Send out any responses in the buffer
		while (true) {
			Command response = responseBuffer.getCommand();
			out.println(response.toString());

			System.out.println(">>" + response.toString());

			// Check we didn't just kill the connection
			if (response.getCommandAsEnum() == COMMANDS.KILL)
				break;
		}

	}

}
