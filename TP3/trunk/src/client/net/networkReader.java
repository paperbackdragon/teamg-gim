package client.net;

import java.io.BufferedReader;

import util.CommandBuffer;

public class networkReader implements Runnable {
	
	
	private CommandBuffer buffer;
	private BufferedReader reader;

	/** Reads commands off the network and performs calls the necessary method */
	public networkReader(BufferedReader reader, CommandBuffer buffer) {
		
		this.buffer = buffer;
		this.reader = reader;
		
	}

	@Override
	public void run() {

		
		
	}

}
