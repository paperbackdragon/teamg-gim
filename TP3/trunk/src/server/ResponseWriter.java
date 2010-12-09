package server;

import java.io.PrintWriter;


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
		while(true) {
			out.println(cmdBuffer.getResponse().toString());
		}

	}

}
