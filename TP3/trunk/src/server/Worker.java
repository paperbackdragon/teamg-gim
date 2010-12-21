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
 * 
 */
public class Worker implements Runnable {

	private CommandBuffer<Command> commandBuffer;
	private CommandBuffer<Command> responseBuffer;
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
		this.responseBuffer = new CommandBuffer<Command>();
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
	 * Put a command in the buffer
	 * 
	 * @param cmd
	 *            The command to place in the buffer
	 */
	public void putCommand(Command cmd) {
		this.commandBuffer.putCommand(cmd);
	}

	/**
	 * Put a response in the buffer
	 * 
	 * @param cmd
	 *            The command to put in the response buffer
	 */
	public void putResponse(Command cmd) {
		this.responseBuffer.putCommand(cmd);
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
			response = ping();
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
			response = friendlist(cmd);
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
		// shouldn't happen. If it does I've messed up somewhere.
		default:
			response = new Command("ERROR", "SERVER_ERROR", Command
					.encode("A server error occured. Please submit a bug report to cyblob@gmail.com."));
		}

		return response;

	}

	/**
	 * <p>
	 * Empty the buffer and kill the worker threads.
	 * </p>
	 */
	private void kill() {
		this.responseBuffer.putCommand(new Command("KILL"));

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
	private Command ping() {
		return new Command("PONG");
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

			if (user != null && user.getPasswordHash().equalsIgnoreCase(dataParts[1])) {

				// Make sure the user ins't already logged in
				if (user.getWorker() != null) {
					user.getWorker().putResponse(new Command("ERROR", "LOGGED_IN_FROM_OTHER_LOCATION"));
					user.getWorker().kill();
				}

				// They provided accurate details, log them in
				this.loggedInUser = user;
				this.loggedInUser.setOnline(true);
				this.loggedInUser.setWorker(this);

				// Tell them that they're logged in
				responseBuffer.putCommand(new Command("AUTH", "LOGGEDIN"));

				// Add any commands they received while offline
				synchronized (this.loggedInUser.getQueue()) {
					while (!loggedInUser.getQueue().isEmpty())
						responseBuffer.putCommand(loggedInUser.getQueue().remove());
				}

				// We did everything already D:
				return null;

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

	/**
	 * <p>
	 * <b>:FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
	 * [target];</b>
	 * </p>
	 * 
	 * <p>
	 * The FRIEND command controls all friend list data.
	 * </p>
	 * 
	 * <p>
	 * <b>ADD</b><br>
	 * The ADD arguments specifies that the client wishes to add the target user
	 * to their friend list. The server should then send a request to the target
	 * user asking for permission to access their data.
	 * </p>
	 * 
	 * <p>
	 * <b>BLOCK</b><br>
	 * The BLOCK argument places the target user into a list of blocked users
	 * who cannot access any data about the user.
	 * </p>
	 * 
	 * <p>
	 * <b>UNBLOCK</b><br>
	 * The UNBLOCK argument removes the target from the current users blocked
	 * list.
	 * </p>
	 * 
	 * <p>
	 * <b>ACCEPT</b><br>
	 * The ACCEPT arguments specifies that the user is responding to a previous
	 * friend request, where the target is the user who sent the request. If the
	 * user accepts then the target user should be given permissions to use the
	 * users data.
	 * </p>
	 * 
	 * <p>
	 * <b>DELETE</b><br>
	 * The DELETE argument specifies that the target user should be deleted from
	 * the users friend list, however the target user will still have access to
	 * the users details.
	 * </p>
	 * 
	 * <p>
	 * <b>DECLINE</b> The DECLINE argument specifies that the user does not wish
	 * the target user to be to able to access their data.
	 * </p>
	 * 
	 * @param cmd
	 * @return
	 */
	private Command friend(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		// Too many arguments
		if (cmd.getArguments().length > 1)
			return new Command("ERROR", "TOO_MANY_ARGUMENTS");

		User user = data.getUser(cmd.getDecodedData());

		// Can't find the user
		if (user == null)
			return new Command("ERROR", "USER_DOES_NOT_EXIST");

		String arg = cmd.getArgumentsAsString();
		if (arg.equalsIgnoreCase("ADD")) {
			/**
			 * Add the user
			 */

			// Make sure they're not alread a friend
			if (this.loggedInUser.isFriendsWith(user))
				return new Command("ERROR", "ALREADY_IN_FRIENDLIST", Command.encode(user.getId()));

			user.sendFriendRequest(this.loggedInUser);

			return this.okay;

		} else if (arg.equalsIgnoreCase("BLOCK")) {
			/**
			 * Block the user
			 */

			// You can't block yourself, that would be silly
			if (this.loggedInUser == user)
				return new Command("ERROR", "CANNOT_BLOCK_YOURSELF");

			this.loggedInUser.block(user);

			return this.okay;

		} else if (arg.equalsIgnoreCase("UNBLOCK")) {
			/**
			 * Unblock the user
			 */

			user.unblock(user);

			return this.okay;

		} else if (arg.equalsIgnoreCase("ACCEPT")) {
			/**
			 * Accept the users friend request
			 */

			if (!user.sentFriendRequestTo(this.loggedInUser))
				return new Command("ERROR", "CANNOT_ACCEPT_REQUEST", "This user did not send you a friend request.");

			this.loggedInUser.addFriend(user);
			user.addFriend(this.loggedInUser);
			user.removeFriendRequest(this.loggedInUser);

			return this.okay;

		} else if (arg.equalsIgnoreCase("DECLINE")) {
			/**
			 * Decline the users friend request
			 */
			user.removeFriendRequest(this.loggedInUser);

			return this.okay;

		} else if (arg.equalsIgnoreCase("DELETE")) {
			/**
			 * Delete the user from our friend list
			 */

			if (!this.loggedInUser.isFriendsWith(user))
				return new Command("ERROR", "NOT_IN_FRIENDLIST");

			this.loggedInUser.removeFriend(user);
			return this.okay;

		}

		// If we made it this far in the code then something isn't right
		return new Command("ERROR", "INVALID_ARGUMENT", arg);

	}

	/**
	 * <p>
	 * <b>:MESSAGE: <roomid> <message>;</b>
	 * </p>
	 * 
	 * <p>
	 * The MESSAGE command specifies a room and a message which should be
	 * delivered to the room.
	 * </p>
	 * 
	 * @param cmd
	 *            The MESSAGE received
	 * @return An OKAY or ERROR COMMAND
	 */
	private Command message(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		String[] data = cmd.splitAndDecodeData(" ");

		// Make sure all the data we need is there
		if (data.length < 2)
			return new Command("ERROR", "MISSING_DATA", "The MESSAGE commange requires a room id and message.");

		// Turn the data into a usable format
		int roomId = Integer.valueOf(data[0]);
		Room room = this.data.getRoom(roomId);

		if (room == null)
			return new Command("ERROR", "ROOM_DOES_NOT_EXIST");

		if (room.messageAll(this.loggedInUser, data[1]))
			return this.okay;

		return new Command("ERROR", "COULD_NOT_SEND_MESSAGE");

	}

	/**
	 * <p>
	 * <b>:ROOM [ CREATE {PUBLIC} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
	 * <user>};</b>
	 * </p>
	 * 
	 * <p>
	 * The ROOM command deals with all room related requests.
	 * </p>
	 * 
	 * <p>
	 * <b>CREATE</b><br>
	 * The CREATE arguments specifies that the client wishes to create a new
	 * chat room for use. If the PUBLIC argument is also supplied then anyone
	 * can join the room without an invite.
	 * 
	 * <b>INVITE</b><br>
	 * The INVITE argument sends an invite to join the room to the specified
	 * user. They are then allowed to join the room at any point.
	 * 
	 * <b>JOIN</b><br>
	 * The JOIN command specifies that the user wishes to join the specified
	 * room.
	 * 
	 * <b>LEAVE</b><br>
	 * The LEAVE argument specifies that the user wishes to leave the specified
	 * room.
	 * 
	 * <b>USERS</b><br>
	 * The USERS argument request a list of the users in the room from the
	 * server.
	 * 
	 * <b>TYPE</b><br>
	 * The TYPE argument can be used to check if the ROOM is either a PERSONAL
	 * or GROUP chat.
	 * </p>
	 * 
	 * @param cmd
	 *            The ROOM command received
	 * @return A bunch of responses depending on the data and arguments
	 *         received. See above for details.
	 */
	private Command room(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		if (cmd.getArguments().length > 2)
			return new Command("ERROR", "INVALID_NUMBER_OF_ARGUMENTS",
					"The ROOM command can accept no more than 2 arguments.");

		String arg = cmd.getArguments()[0];
		if (arg.equalsIgnoreCase("CREATE")) {

			// Do they want a public room?
			boolean group = false;
			if (cmd.getArguments().length == 2 && cmd.getArguments()[1].equalsIgnoreCase("GROUP"))
				group = true;

			User invite = null;
			if (group == false) {
				invite = data.getUser(cmd.getDecodedData());

				if (invite == null)
					return new Command("ERROR", "INVALID_USER_SUPPLIED");

				if (!invite.isOnline())
					return new Command("ERROR", "USER_OFFLINE");
			}

			// Create the new room
			Room room = new Room(this.loggedInUser, group);
			data.addRoom(room);

			if (group == false)
				room.invite(invite, this.loggedInUser);

			return new Command("ROOM", "CREATED", room.getId() + "");

		} else {

			String[] data = cmd.splitAndDecodeData(" ");

			if (arg.equalsIgnoreCase("INVITE")) {

				// Make sure all the data we need is there
				if (data.length < 2)
					return new Command("ERROR", "MISSING_DATA", "The INVITE argument requires a room id and a user id");

				// Turn the data into a usable format
				int roomId = Integer.valueOf(data[0]);
				String userId = data[1];

				// Make sure we have permissions to invite people to the room
				if (!this.loggedInUser.inRoom(roomId))
					return new Command("ERROR", "NOT_IN_ROOM",
							"You cannot invite someone into a room which you are no already in.");

				Room room = this.data.getRoom(roomId);
				User user = this.data.getUser(userId);

				if (!user.isFriendsWith(this.loggedInUser))
					return new Command("ERROR", "NOT_IN_FRIEND_LIST");

				// The user doesn't exist
				if (user == null)
					return new Command("ERROR", "USER_DOES_NOT_EXIST", userId);

				// The user they're trying to invite is offline
				if (!user.isOnline())
					return new Command("ERROR", "USER_IS_OFFLINE", Command.encode(user.getId()));

				// Make sure the room isn't full.
				if (!room.isGroup() && (room.getUsers().size() > 1 || room.getInvitiedUsers().size() > 1))
					return new Command("ERROR", "ROOM_FULL", room.getId() + "");

				room.invite(user, this.loggedInUser);

				return this.okay;

			} else if (arg.equalsIgnoreCase("JOIN")) {

				// Make sure all the data we need is there
				if (data.length < 1)
					return new Command("ERROR", "MISSING_DATA", "The JOIN argument requires a room id.");

				// Turn the data into a usable format
				int roomId = Integer.valueOf(data[0]);
				Room room = this.data.getRoom(roomId);

				if (room == null)
					return new Command("ERROR", "ROOM_DOES_NOT_EXIST");

				if (room.join(this.loggedInUser))
					this.loggedInUser.addRoom(room);
				else
					return new Command("ERROR", "COULD_NOT_JOIN_ROOM");

				return this.okay;

			} else if (arg.equalsIgnoreCase("LEAVE")) {

				// Make sure all the data we need is there
				if (data.length < 1)
					return new Command("ERROR", "MISSING_DATA", "The LEAVE argument requires a room id.");

				// Turn the data into a usable format
				int roomId = Integer.valueOf(data[0]);
				Room room = this.data.getRoom(roomId);

				if (room == null)
					return new Command("ERROR", "ROOM_DOES_NOT_EXIST");

				if (room.leave(this.loggedInUser))
					this.loggedInUser.removeRoom(room);
				else
					return new Command("ERROR", "COULD_NOT_LEAVE_ROOM");

				return this.okay;

			} else if (arg.equalsIgnoreCase("USERS")) {

				// Make sure all the data we need is there
				if (data.length < 1)
					return new Command("ERROR", "MISSING_DATA", "The USERS argument requires a room id.");

				// Turn the data into a usable format
				int roomId = Integer.valueOf(data[0]);
				Room room = this.data.getRoom(roomId);

				if (room == null)
					return new Command("ERROR", "ROOM_DOES_NOT_EXIST");

				String users = "";
				for (User user : room.getUsers().values())
					users += Command.encode(user.getId()) + " ";

				return new Command("ROOM", "USERS", users);

			} else if (arg.equalsIgnoreCase("TYPE")) {

				// Make sure all the data we need is there
				if (data.length < 1)
					return new Command("ERROR", "MISSING_DATA", "The TYPE argument requires a room id.");

				// Turn the data into a usable format
				int roomId = Integer.valueOf(data[0]);
				Room room = this.data.getRoom(roomId);

				if (room == null)
					return new Command("ERROR", "ROOM_DOES_NOT_EXIST");

				return new Command("ROOM", room.getType());

			}

		}

		return new Command("ERROR", "INVALID_ARGUMENT");
	}

	/**
	 * :FRIENDLIST:;
	 * 
	 * The FRIENDLIST command requests a list of users in the users friend list.
	 * 
	 * @param cmd
	 *            The FRIENDLIST command
	 * @return The friend list of the current user
	 */
	private Command friendlist(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		String online = "ONLINE ";
		String offline = "OFFLINE ";
		String blocked = "BLOCKED";

		// Sort the online and offline users
		for (User user : this.loggedInUser.getFriendList()) {
			if (user.isOnline())
				online += Command.encode(user.getId()) + " ";
			else
				offline += Command.encode(user.getId()) + " ";
		}

		// Get all of the blocked users
		for (User user : this.loggedInUser.getBlockedUsers())
			blocked += Command.encode(user.getId()) + " ";

		return new Command("FRIENDLIST", null, online + offline + blocked);

	}

	/**
	 * <p>
	 * <b>:GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
	 * {[user],[user]};</b>
	 * </p>
	 * 
	 * <p>
	 * The GET command requests a set of attributes for each user in a comma
	 * separated list of users. The server should respond with an INFO command
	 * and the appropriate data.
	 * </p>
	 * 
	 * @param cmd
	 *            The GET command received by the server
	 * @return An INFO command with the data asked for
	 */
	private Command get(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		// Split the IDs
		String[] users = cmd.splitAndDecodeData(" ");
		String response = "";

		// Make sure they provided at least one ID
		if (users.length == 0)
			return new Command("ERROR", "INSUFFICENT_DATA_TO_COMPLETE_REQUEST");

		for (String id : users) {

			// Make sure the user id they want information about is valid
			if (!User.validID(id))
				return new Command("ERROR", "INVALID_USER_ID");

			// Make sure that the user exists
			User user = data.getUser(id);
			if (user == null)
				return new Command("ERROR", "USER_NOT_FOUND", Command.encode(id));

			// Stop them from getting access about people who aren't in their
			// freindlist
			if (!this.loggedInUser.isFriendsWith(user) && this.loggedInUser.inRoomWith(user)
					&& this.loggedInUser != user)
				return new Command("ERROR", "NOT_AUTHORIZED");

			// Get the data they asked for
			response += Command.encode(user.getId()) + "\n";
			String[] args = cmd.getArguments();
			for (int i = 0; i < args.length; i++) {

				if (args[i].equalsIgnoreCase("NICKNAME")) {
					response += Command.encode(user.getNickname());

				} else if (args[i].equalsIgnoreCase("STATUS")) {
					response += Command.encode(user.getStatus().toString());

				} else if (args[i].equalsIgnoreCase("PERSONAL_MESSAGE")) {
					response += Command.encode(user.getPersonalMessage());

				} else if (args[i].equalsIgnoreCase("DISPLAY_PIC")) {
					response += Command.encode(user.getDisplayPic());

				} else {
					return new Command("ERROR", "INVALID_ARGUMENT");
				}

				response += "\n";
			}

		}

		// Remove extra newline added above
		response = response.substring(0, response.length() - 1);

		return new Command("INFO", null, response);
	}

	/**
	 * <p>
	 * <b>:SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]:
	 * [value];</b>
	 * </p>
	 * 
	 * <p>
	 * The SET commands allows various user attributes to be set by the client.
	 * The exact attribute depends on the augment given however only 1 attribute
	 * can be set at a time. In the case of DISPLAY_PIC, the image is Base 64
	 * encoded.
	 * </p>
	 * 
	 * @param cmd
	 *            The SET command received
	 * @return Assuming all went well, an OKAY command. Otherwise, an ERROR will
	 *         be returned
	 */
	private Command set(Command cmd) {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		if (cmd.getArguments().length != 1)
			return new Command("ERROR", "INVALID_NUMBER_OF_ARGUMENTS", "The SET command only accept 1 argument.");

		// TODO: Check that there are no constrains for the values each of these
		// can take
		String arg = cmd.getArguments()[0];
		if (arg.equalsIgnoreCase("NICKNAME")) {
			this.loggedInUser.setNickname(cmd.getDecodedData());

		} else if (arg.equalsIgnoreCase("STATUS")) {
			if (!this.loggedInUser.setStatus(cmd.getDecodedData()))
				return new Command("ERROR", "INVALID_STATUS", cmd.getDecodedData());

		} else if (arg.equalsIgnoreCase("PERSONAL_MESSAGE")) {
			this.loggedInUser.setPersonalMessage(cmd.getDecodedData());

		} else if (arg.equalsIgnoreCase("DISPLAY_PIC")) {
			this.loggedInUser.setDisplayPic(cmd.getDecodedData());

		} else {
			return new Command("ERROR", "INVALID_ARGUMENT", arg);
		}

		return this.okay;
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
	 * 
	 *         TODO: Leave any rooms they're currently in
	 */
	private Command logout() {

		if (this.loggedInUser == null)
			return new Command("ERROR", "UNAUTHORIZED");

		this.loggedInUser.logout();
		this.loggedInUser.setWorker(null);
		this.loggedInUser = null;

		return this.okay;
	}

	/**
	 * RUN FOREST! RUN!
	 * 
	 * ...but seriously. This creates the threads that the input and output to
	 * the socket run in. It then reads commands off the command buffer, and
	 * processes them, putting responses back into the response buffer.
	 */
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
		this.responseWriter = new ResponseWriter(out, responseBuffer);
		responseWriterThread = new Thread(this.responseWriter);
		responseWriterThread.start();

		// Deal with the commands
		while (socket.isConnected()) {
			Command cmd = commandBuffer.getCommand();
			Command rsp = processCommand(cmd);

			if (rsp != null) {
				this.responseBuffer.putCommand(rsp);
			}

			if (Thread.currentThread().isInterrupted())
				break;

		}

		System.out.println("Worker finished.");

	}
}
