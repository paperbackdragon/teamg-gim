package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import util.Command;
import util.CommandBuffer;
import util.CommandReader;
import util.ResponseWriter;

/**
 * The Worker class takes a socket and data
 */
public class Worker implements Runnable {

	private CommandBuffer<Command> commandBuffer;
	private Socket socket = null;
	private BufferedReader in;
	private PrintWriter out;

	private Data data = Data.getInstance();
	private long lastCommunication;
	private User loggedInUser;

	// Okay command gets used a lot, lets not be wasteful and just have one of
	// them per worker
	private Command okay = new Command("OKAY", null, null);

	public Worker(Socket socket) {
		this.socket = socket;
		this.commandBuffer = new CommandBuffer<Command>();
		this.lastCommunication = System.currentTimeMillis();
		this.loggedInUser = null;
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

		case SERVERSTATUS:
			response = serverstatus(cmd);
			break;

		// Below this point, generate an error as the server should not receive
		// these commands
		case OKAY:
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

	private Command serverstatus(Command cmd) {

		String time = new Date().toString();
		String users = "OMG LOL";
		String uptime = "OMG UPTIME";

		String data = "";
		String[] args = cmd.getArguments();

		if (cmd.getArguments().length == 0)
			return new Command("SERVERSTATUS", null, time + "\n" + users + "\n" + uptime);

		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("TIME"))
				data += time;
			else if (args[i].equalsIgnoreCase("USERS"))
				data += users;
			else if (args[i].equalsIgnoreCase("UPTIME"))
				data += uptime;
			else
				return new Command("ERROR", "INVALID_ARGUMENT", null);

			if (i < (args.length - 1))
				data += "\n";

		}

		return new Command("SERVERSTATUS", null, data);

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
		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED", null);

		this.loggedInUser = null;

		return this.okay;
	}

	private Command friend(Command cmd) {
		// TODO: The friend command
		return null;
	}

	private Command message(Command cmd) {
		// TODO: The message command
		return null;
	}

	private Command room(Command cmd) {
		// TODO: The room command
		return null;
	}

	private Command frindlist(Command cmd) {
		// TODO: The friendlist command
		return null;
	}

	private Command get(Command cmd) {
		// TODO: The get command
		return null;
	}

	private Command set(Command cmd) {
		// TODO: The set command
		return null;
	}

	private Command quit(Command cmd) {
		// TODO: The quit command
		return null;
	}

	/**
	 * Do nothing, just respond with an OKAY. We'll me getting lots of these.
	 * 
	 * @param cmd
	 *            the PING Command
	 * @return an OKAY command
	 */
	private Command ping(Command cmd) {
		return this.okay;
	}

	private Command server(Command cmd) {
		// TODO: The server command
		return null;
	}

	/**
	 * The AUTH commands deals with all aspects of user authorisation and
	 * permissions. The AUTH command alone should should return the users
	 * current authorisation state, either LOGGEDIN or UNAUTHORIZED.
	 * 
	 * LOGIN
	 * 
	 * If the details are valid then the server should respond with an :AUTH
	 * OKAY: command. It should set the user’s state (but not status) to ONLINE.
	 * If the user is already logged in then the server should send a
	 * LOGGED_IN_FROM_OTHER_LOCATION error and KILL command to the already
	 * connected user.
	 * 
	 * REGISTER
	 * 
	 * The register argument allows for new users to be registered by providing
	 * a valid email address and password. If the new account is registered
	 * correctly then the server should respond with AUTH REGISTERED however it
	 * should not log the user in.
	 * 
	 * @param cmd
	 *            The login Command issued
	 * @return The appropriate response command
	 * 
	 */
	private Command auth(Command cmd) {

		// TODO: Check that the user is not already logged in

		// Should give 0) email address 1) password hash
		String[] dataParts = cmd.getData().split(" ");

		// No arguments, give them their current login status
		if (cmd.getArguments().length == 0) {

			if (this.loggedInUser == null)
				return new Command("AUTH", "UNAUTHORIZED", null);
			else
				return new Command("AUTH", "LOGGEDIN", this.loggedInUser.getNickname());

			// Attempt to log the user in
		} else if (cmd.getArguments()[0].equalsIgnoreCase("LOGIN")) {

			// Make sure that they're not already logged in
			if (this.loggedInUser != null)
				return new Command("ERROR", "ALREADY_LOGGEDIN", null);

			// Not enough data to continue
			if (dataParts.length < 2)
				return new Command("ERROR", "MISSING_DATA", null);

			// Check the user details
			User user = data.getUser(dataParts[0]);
			if (user != null && user.getPasswordHash().equalsIgnoreCase(dataParts[1])) {

				// They provided accurate details, log them in
				this.loggedInUser = user;
				this.loggedInUser.setStatus(User.Status.ONLINE);
				return new Command("AUTH", "LOGGEDIN", null);

				// Something wasn't correct
			} else {

				// The user doesn't exist
				if (user == null) {
					return new Command("ERROR", "USER_DOES_NOT_EXIST", null);
				} else {
					// The details were wrong
					return new Command("ERROR", "LOGIN_DETAILS_INCORRECT", null);
				}

			}

			// Attempt to register a new account with the given details
		} else if (cmd.getArguments()[0].equalsIgnoreCase("REGISTER")) {

			// TODO: Check password length

			// Not enough data to continue
			if (dataParts.length < 2)
				return new Command("ERROR", "MISSING_DATA", "Not enough data to complete the request");

			// Make sure that they're not already logged in
			if (this.loggedInUser != null)
				return new Command("ERROR", "ALREADY_LOGGEDIN",
						"You cannot register a new account while already logged in.");

			// The email address isn't valid
			if (!User.validID(dataParts[1]))
				return new Command("ERROR", "INVALID_EMAIL", "Invalid email address.");

			// The email address is already registered
			if (data.getUser(dataParts[0]) != null)
				return new Command("ERROR", "EMAIL_ALREADY_IN_USE",
						"The email address you are attempting to register is already in use.");

			// If we made it this far everything should be okay! :D
			data.addUser(new User(dataParts[0], dataParts[1]));
			return new Command("AUTH", "REGISTERED", null);

		}

		// The arguments given didn't make sense for this command
		return new Command("ERROR", "INVALID_ARGUMENT", null);
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
			Command cmd = commandBuffer.getCommand();
			Command rsp = processCommand(cmd);
			commandBuffer.putResponse(rsp);
		}

	}

}
