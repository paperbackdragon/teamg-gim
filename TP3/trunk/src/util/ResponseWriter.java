package util;

import java.io.PrintWriter;

import util.Command.COMMANDS;

/**
 * The response writer reads commands off a CommandBuffer and sends them across
 * the network.
 * 
 * TODO: Encode any unicode characters
 */
public class ResponseWriter implements Runnable {

	private PrintWriter out;
	private CommandBuffer<Command> cmdBuffer;

	public ResponseWriter(PrintWriter out, CommandBuffer<Command> cmdBuffer) {
		this.out = out;
		this.cmdBuffer = cmdBuffer;
	}

	public void run() {
		try {
			
			// Send out any responses in the buffer
			while (true) {
				Command response= cmdBuffer.getResponse();
				out.println(response.toString());

				// Check we didn't just kill the connection
				if (response.getCommandAsEnum() == COMMANDS.KILL)
					break;
			}

		} catch (InterruptedException e) {
		}
		
		System.out.println("ResponseWriter killed.");
	}

}
