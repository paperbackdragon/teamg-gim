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
	 * Process the given command and get a response.
	 * 
	 * @param cmd
	 *            the command to process
	 * @return the response to the command
	 */
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

		case QUIT:
			response = quit(cmd);
			break;

		case SET:
			response = set(cmd);
			break;

		case GET:
			response = get(cmd);
			break;

		case FRIENDLIST:
			response = frindlist(cmd);
			break;

		case ROOM:
			response = room(cmd);
			break;

		case MESSAGE:
			response = message(cmd);
			break;

		case FRIEND:
			response = friend(cmd);
			break;

		case LOGOUT:
			response = logout(cmd);
			break;

		// Below this point, generate an error as the server should not receive
		// these commands
		case OKAY:
		case SERVERSTATUS:
		case KILL:
		case BROADCAST:
		case FREINDREQUEST:
		case UPDATE:
		case INFO:
		case ERROR:
			response = new Command("ERROR", "COMMAND_RECOGNISED_BUT_NOT_VALID",
					"The command was recognised by the server however the server should not recieve this command.");
			break;

		// Somehow the command was recognised but not processed above. This
		// shouldn't happen.
		default:
			response = new Command("ERROR", "SERVER_ERROR", "A server error occured.");
			break;
		}

		return response;

	}

	/**
	 * The LOGOUT command specifies that the client wishes to logout but not
	 * drop the connection to the server.
	 * 
	 * @param cmd
	 *            the logout command
	 * @return If successful it should return an OKAY. Upon an error such as the
	 *         user not being logged in, it should return an ERROR.
	 */
	private Command logout(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	private Command friend(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	private Command message(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	private Command room(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	private Command frindlist(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	private Command get(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	private Command set(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	private Command quit(Command cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Do nothing, just respond with an OKAY.
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
