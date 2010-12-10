package util;

import java.io.PrintWriter;

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

	@Override
	public void run() {
		// Send out any responses in the buffer
		while (true) {
			out.println(cmdBuffer.getResponse().toString());
		}

	}

}
