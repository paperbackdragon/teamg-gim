package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import server.util.CommandBuffer;

public class Worker implements Runnable {

	private HashMap<Integer, Worker> workers;
	private CommandBuffer<Command> commandBuffer;
	private Socket socket = null;
	private BufferedReader in;
	private PrintWriter out;

	public Worker(Socket socket, HashMap<Integer, Worker> workers) {
		this.workers = workers;
		this.socket = socket;
		this.commandBuffer = new CommandBuffer<Command>();
	}

	@Override
	public void run() {
		
		// Get input and output buffers from the socket
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("We're fscked.");
			System.exit(-1);
		}

		// Create a thread to read in and handle commands
		Thread cmdReader = new Thread(new CommandReader(in, commandBuffer));
		cmdReader.start();
		
		while(true) {
			out.println(commandBuffer.getResponse().toString());
		}

	}

}
