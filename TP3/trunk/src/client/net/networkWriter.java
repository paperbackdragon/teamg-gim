package client.net;

import java.io.PrintWriter;
import util.CommandBuffer;

/**
 * Reads commands (formatted appropriately) to send to the server off the
 * CommandBuffer and sends to the server
 */

public class networkWriter implements Runnable {

	private CommandBuffer buffer;
	private PrintWriter writer;

	public networkWriter(PrintWriter writer, CommandBuffer buffer) {
		
		this.buffer = buffer;
		this. writer = writer;

	}

	@Override
	public void run() {
		
		
		

	}

}
