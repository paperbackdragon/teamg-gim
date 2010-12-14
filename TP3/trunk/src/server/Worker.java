package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import server.User.Status;
import util.Command;
import util.CommandBuffer;
import util.CommandReader;
import util.ResponseWriter;

/**
 * The Worker class takes a socket and data
 * 
 * TODO: Encode all of the data generated
 */
public class Worker implements Runnable {

	private CommandBuffer<Command> commandBuffer;
	private Socket socket = null;
	private BufferedReader in;
	private PrintWriter out;

	private Data data = Data.getInstance();
	private long lastCommunication;
	private User loggedInUser;

	private Command okay = new Command("OKAY");

	private ResponseWriter responseWriter;
	private CommandReader commandReader;
	private Thread responseWriterThread;
	private Thread commandReaderThread;

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

		case SERVERSTATUS:
			response = serverstatus(cmd);
			break;

		case AUTH:
			response = auth(cmd);
			break;

		case QUIT:
			response = quit();
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
			response = logout();
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
			response = new Command("ERROR", "SERVER_ERROR", Command.encode("A server error occured."));
			break;
		}

		return response;

	}

	/**
	 * <p>
	 * Empty the buffer and kill the worker threads.
	 * </p>
	 */
	private void kill() {
		this.commandBuffer.putResponse(new Command("KILL"));

		try {
			responseWriterThread.join();
		} catch (InterruptedException e1) {
		}

		try {
			socket.close();
		} catch (IOException e) {
		}

		try {
			commandReaderThread.join();
		} catch (InterruptedException e) {
		}

		Thread.currentThread().interrupt();
	}

	/**
	 * <p>
	 * <b>:PING:;</b>
	 * </p>
	 * 
	 * <p>
	 * The command is used as a keep-alive command. Its primary use it to
	 * indicate that the client is still alive even if no other communication
	 * has been received from the client.
	 * </p>
	 * 
	 * @param cmd
	 *            the PING Command
	 * @return an OKAY command
	 */
	private Command ping(Command cmd) {
		return this.okay;
	}

	/**
	 * <p>
	 * <b>:SERVERSTATUS { USERS | TIME | UPTIME }:;</b>
	 * </p>
	 * 
	 * <p>
	 * The SERVERSTATUS command returns information about the server including
	 * the number of users, the local system time and server up-time. In that
	 * case that an argument is provided then the server should return a
	 * SERVERSTATUS as defined in the client section. If more than one argument
	 * is provided then the server should return each value on a new line in the
	 * order which the arguments were received (reading from left to right).
	 * </P>
	 * 
	 * <p>
	 * The arguments for this command are as follows:
	 * <ul>
	 * <li>USERS - The total number of users and number of online users</li>
	 * <li>TIME - The current local time of the server</li>
	 * <li>UPTIME - The up-time of the server instance</li>
	 * </ul>
	 * </p>
	 * 
	 * @param cmd
	 *            A command containing the status command
	 * @return The serverstatus command with the data requested, as define above
	 */
	private Command serverstatus(Command cmd) {

		// TODO: Make these return the correct things...
		String time = Command.encode(new Date().toString());
		String users = Command.encode("OMG LOL");
		String uptime = Command.encode("OMG UPTIME");

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
	 * <p>
	 * <b>:AUTH { LOGIN | REGISTER }: [email address] [password];</b>
	 * </p>
	 * 
	 * <p>
	 * The AUTH commands deals with all aspects of user authorisation and
	 * permissions. The AUTH command alone should should return the users
	 * current authorisation state, either LOGGEDIN or UNAUTHORIZED.
	 * </p>
	 * 
	 * <p>
	 * <b>LOGIN</b><br>
	 * If the details are valid then the server should respond with an :AUTH
	 * LOGGEDIN: command. It should set the user’s state (but not status) to
	 * ONLINE. In the event that an error occurs the server should generate one
	 * of the following ERROR statuses:
	 * <ul>
	 * <li>USER_DOES_NOT_EXIST The email address has not been registered
	 * <li>LOGIN_DETAILS_INCORRECT The password was incorrect MISSING_ARGUMENTS
	 * There were too few arguments.
	 * </ul>
	 * </p>
	 * <p>
	 * If the user is already logged in then the server should send a
	 * LOGGED_IN_FROM_OTHER_LOCATION error and KILL command to the already
	 * connected user.
	 * </p>
	 * 
	 * <p>
	 * <b>REGISTER</b><br>
	 * The register argument allows for new users to be registered by providing
	 * a valid email address and password. If the new account is registered
	 * correctly then the server should respond with AUTH REGISTERED however it
	 * should not log the user in. If the registration is unsuccessful the
	 * server should return one of the following ERROR messages: INVALID_EMAIL
	 * The email address was invalid EMAIL_ALREADY_IN_USE The email address has
	 * already by registered PASSWORD_TOO_SHORT The password is too short
	 * MISSING_ARGUMENTS There were too few arguments
	 * 
	 * @param cmd
	 *            The login Command issued
	 * @return The appropriate response command
	 * 
	 */
	private Command auth(Command cmd) {

		// Should give 0) email address 1) password hash
		String[] dataParts = cmd.splitAndDecodeData(" ");
		
		// TODO: decode the data

		// No arguments, give them their current login status
		if (cmd.getArguments().length == 0) {

			if (this.loggedInUser == null)
				return new Command("AUTH", "UNAUTHORIZED");
			else
				return new Command("AUTH", "LOGGEDIN", this.loggedInUser.getNickname());

			// Attempt to log the user in
		} else if (cmd.getArguments()[0].equalsIgnoreCase("LOGIN")) {

			// Make sure that they're not already logged in
			if (this.loggedInUser != null)
				return new Command("ERROR", "ALREADY_LOGGEDIN");

			// Not enough data to continue
			if (dataParts.length < 2)
				return new Command("ERROR", "MISSING_DATA");

			// Check the user details
			User user = data.getUser(dataParts[0]);
			System.out.println(dataParts[0]);
			if (user != null && user.getPasswordHash().equalsIgnoreCase(dataParts[1])) {

				// They provided accurate details, log them in
				this.loggedInUser = user;
				this.loggedInUser.setOnline(true);
				return new Command("AUTH", "LOGGEDIN");

				// Something wasn't correct
			} else {

				// The user doesn't exist
				if (user == null) {
					return new Command("ERROR", "USER_DOES_NOT_EXIST");
				} else {
					// The details were wrong
					return new Command("ERROR", "LOGIN_DETAILS_INCORRECT");
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
			return new Command("AUTH", "REGISTERED");

		}

		// The arguments given didn't make sense for this command
		return new Command("ERROR", "INVALID_ARGUMENT");
	}

	/**
	 * <p>
	 * <b>:QUIT:;</b>
	 * </p>
	 * 
	 * <p>
	 * The QUIT command tells the server that the users wishes to log out (if
	 * applicable) and disconnect from the server. Once the quit command has
	 * been received the users’ state should be changed to OFFLINE and the the
	 * connection broken.
	 * </p>
	 * 
	 * @return an OKAY command, although this should never reach the user
	 */
	public Command quit() {
		logout();
		kill();
		return this.okay;
	}

	private Command friend(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		// TODO: The friend command
		return null;
	}

	private Command message(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		// TODO: The message command
		return null;
	}

	private Command room(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		// TODO: The room command
		return null;
	}

	private Command frindlist(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		// TODO: The friendlist command
		return null;
	}

	private Command get(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		// Split the IDs
		String[] users = cmd.splitAndDecodeData(",");
		String response = "";

		// Make sure they provided at least one ID
		if (users.length == 0)
			return new Command("ERROR", "INSUFFICENT_DATA_TO_COMPLETE_REQUEST");

		for (String id : users) {
			if (!User.validID(id))
				return new Command("ERROR", "INVALID_USER_ID");

			User user = data.getUser(id);
			if (user == null)
				return new Command("ERROR", "USER_NOT_FOUND");

			if (!this.loggedInUser.friendListContains(user) && this.loggedInUser != user)
				return new Command("ERROR", "NOT_AUTHORIZED");

			String[] args = cmd.getArguments();
			for (int i = 0; i < args.length; i++) {

				if (args[i].equalsIgnoreCase("NICKNAME"))
					response += user.getNickname();
				else if (args[i].equalsIgnoreCase("STATUS"))
					response += user.getStatus().toString();
				else if (args[i].equalsIgnoreCase("PERSONAL_MESSAGE"))
					response += user.getPersonalMessage();
				else if (args[i].equalsIgnoreCase("DISPLAY_PIC"))
					response += user.getDisplayPic();
				else
					return new Command("ERROR", "INVALID_ARGUMENT");

				response += "\n";

			}

		}

		// Remove extra newline
		response = response.substring(0, response.length() - 2);

		return new Command("INFO", null, response);
	}

	private Command set(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		// TODO: The set command
		return null;
	}

	/**
	 * <p>
	 * <b>:LOGOUT:;</b>
	 * </p>
	 * 
	 * <p>
	 * The LOGOUT command specifies that the client wishes to logout but not
	 * drop the connection to the server.
	 * </p>
	 * 
	 * @return If successful it should return an OKAY. Upon an error such as the
	 *         user not being logged in, it should return an ERROR.
	 */
	private Command logout() {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		loggedInUser.setOnline(false);
		loggedInUser.setStatus(Status.OFFLINE);
		this.loggedInUser = null;

		return this.okay;
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
		this.commandReader = new CommandReader(in, commandBuffer);
		commandReaderThread = new Thread(this.commandReader);
		commandReaderThread.start();

		// Create a thread to print out responses
		this.responseWriter = new ResponseWriter(out, commandBuffer);
		responseWriterThread = new Thread(this.responseWriter);
		responseWriterThread.start();

		try {

			// Deal with the commands
			while (socket.isConnected()) {
				Command cmd = commandBuffer.getCommand();
				Command rsp = processCommand(cmd);
				if (rsp != null) {
					commandBuffer.putResponse(rsp);
				}
			}

		} catch (InterruptedException e) {
			System.out.println("Worker has finished.");
		}

	}
}
