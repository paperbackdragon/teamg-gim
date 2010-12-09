package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The Worker class takes a socket and data
 */
public class Worker implements Runnable {

	private CommandBuffer<Command> commandBuffer;
	private Socket socket = null;
	private BufferedReader in;
	private PrintWriter out;
	private long lastCommunication;

	public Worker(Socket socket) {
		this.socket = socket;
		this.commandBuffer = new CommandBuffer<Command>();
		this.lastCommunication = System.currentTimeMillis();
	}

	private Command processCommand(Command cmd) {
		Command response;

		updateLastCommunication();

		switch (cmd.getCommandAsEnum()) {
		case PING:
			response = ping(cmd);
			break;

		case SERVER:
			response = server(cmd);
			break;

		case AUTH:
			response = auth(cmd);
			break;

		default:
			response = new Command("ERROR", "SERVER_ERROR", "A server error occured.");
		}

		return response;

	}

	/**
	 * Update the time which the Worker last received a communication from the
	 * client.
	 */
	private void updateLastCommunication() {
		this.lastCommunication = System.currentTimeMillis();
	}

	/**
	 * Get the time in seconds since the last communication with the client took
	 * place
	 * 
	 * @return the time in seconds
	 */
	public int getLastCommunicationTimeDifference() {
		return (int) ((System.currentTimeMillis() - this.lastCommunication) / 1000);
	}

	/**
	 * Do nothing, just respond with an OKAY
	 * 
	 * @param cmd
	 *            the PING command and any data it contains
	 * @return an OKAY command
	 */
	private Command ping(Command cmd) {
		return new Command("OKAY", null, null);
	}

	private Command server(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	private Command auth(Command cmd) {
		// TODO Auto-generated method stub
		return null;
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

		// Create a thread to print out responses
		Thread responseWriter = new Thread(new ResponseWriter(out, commandBuffer));
		responseWriter.start();

		// Deal with the commands
		while (true) {
			// Load a command for the buffer
			Command cmd = commandBuffer.getCommand();

			// Do something with the commands in the buffer
			Command rsp = processCommand(cmd);

			// Send out the response
			commandBuffer.putResponse(rsp);
		}

	}

}
