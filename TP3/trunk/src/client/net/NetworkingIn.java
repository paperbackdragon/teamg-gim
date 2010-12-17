package client.net;

import java.util.ArrayList;

public interface NetworkingIn {

	// prelogin

	// :OKAY:;

	/**
	 * The last command was send successfully (this is sent where there is no
	 * other response)
	 */
	void okay();

	// :SERVERSTATUS { USERS | TIME | UPTIME }:;

/*	*//**
	 * The server has sent information about the global status of the server
	 * 
	 * @param status
	 *            The string with global information
	 *//*
	void status(String status);*/

	/**
	 * The server has sent information about the server's time
	 * 
	 * @param servertime
	 *            The server's time
	 */
	void servertime(String servertime);

	/**
	 * The server has sent the number of users connected to the server
	 * 
	 * @param usercount
	 *            The amount of user's connected
	 */
	void usercount(String usercount);

	/**
	 * The server has sent information about its uptime
	 * 
	 * @param uptime
	 *            The uptime of the server
	 */
	void uptime(String uptime);

	// :SERVERSTATUS ends

	/**
	 * The server has indicated that it is about to force the client to disconnect.
	 * The server will close the connection after this command is sent.
	 * 
	 * @param message
	 *            The reason why the server wants the client to disconnect
	 *            gracefully.
	 */
	void kill(String message);

	// :BROADCAST: <message>;

	/**
	 * The server has sent a global message to all connected user. This is
	 * likely to contain critical information and should be clearly displayed to
	 * the user.
	 * 
	 * @param message
	 *            The message from the server.
	 */
	void broadcast(String message);

	// :AUTH [ LOGGEDIN | UNAUTHORIZED ]:;

	// NOT SURE ABOUT THESE TWO... WILL TALK TO JAMES

	/** The server indicated the user is authorised */
	void authorised();

	/** The server indicated the user is unauthorised */
	void unauthorised();
	
	/** The server has indicated that the user has successfully registered */
	void registered();

	// end :AUTH

	// postlogin

	// :MESSAGE: <roomid> <sender> <message>;

	/**
	 * The server has indicated that the user has received a message
	 * 
	 * @param roomid
	 *            The id of the room the message was sent to
	 * @param sender
	 *            The person the message was received from
	 * @param message
	 *            The message the sender sent
	 */
	void message(String roomid, String sender, String message);

	// need james to clarify on these:

	// :ROOM [ CREATED | JOINED | LEFT | INVITED | USERS ]: <roomid> {<user>};

	/**
	 * The server has notified that the room the user requested to be made was
	 * created successfully
	 * 
	 * @param roomid
	 *            the roomid they now have permission to join
	 */
	void created(String roomid);

	/**
	 * The server has notified that a person has joined a group chat that the
	 * user is in
	 * 
	 * @param user
	 *            The user that joined
	 */
	void joined(String user, String roomid);

	/**
	 * The server has notified that a person has left one of the group chats
	 * that the user is in
	 * 
	 * @param user
	 *            The user that left
	 * @param roomid
	 *            The id of the room the user left
	 * */
	void left(String user, String roomid);

	/**
	 * The server has notified that the user has been invited to join a room
	 * 
	 * @param user
	 *            the user that sent the invite
	 * @param roomid
	 *            the id of the room the person was invited to
	 */
	void invited(String user, String roomid);

	/**
	 * The server has sent a list of users in a group chat
	 * 
	 * @param users
	 *            the arraylist of users
	 * @param roomid
	 *            the id of the room the users are in
	 */
	void users(ArrayList<String> users, String roomid);
	
	/** The server has reported that a room is a personal chat
	 * @roomid
	 * 		The room that is personal*/
	void personal(String roomid);
	
	/** The server has reported that a room is a group chat
	 * @roomid
	 * 		The room that is a group chat*/
	void group(String roomid);

	// end :ROOM

	// :p feel free to tell me if you don't think this is a sensible way of
	// dealing with this:

	// :FRIENDLIST: ONLINE <user>{,<user>} OFFLINE <user>{,<user>} BLOCKED

	/**
	 * The server has sent the online status of all the users on the buddy list
	 * 
	 * @param onlinelist
	 *            An arraylist of the users online on the buddy list
	 * @param offlinelist
	 *            An arraylist of the users offline on the buddy list
	 * @param blockedlist
	 *            An arraylist of the users who are blocked on the buddy list
	 * */

	void friendlist(String[] onlinelist,
			String[] offlinelist, String[] blockedlist);

	// :FRIENDREQUEST: <user> <nickname>;

	/**
	 * the server has notified that the user has received a friend request
	 * 
	 * @param user
	 *            The user that has sent a friend request
	 * @param nickname
	 *            The user's nickname at the time of sending the friend request
	 */
	void friendrequest(String user, String nickname);

	// :UPDATE [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <user>;

	/**
	 * The server has notified that a user has changed their nickname
	 * 
	 * @param user
	 *            The user that changed their nickname
	 */
	void notifyNickname(String user);

	/**
	 * The server has notified that a user has changed their status
	 * 
	 * @param user
	 *            The user that has changed their status
	 */
	void notifyStatus(String user);

	/**
	 * The server has notified that a user has changed their personal message
	 * 
	 * @param user
	 *            The user that has changed their personal message
	 */
	void notifyPersonalMessage(String user);

	/**
	 * The server has notified that a user has changed their display picture
	 * 
	 * @param user
	 *            The user that has changed their display picture
	 */
	void notifyDisplayPicture(String user);

	// end :UPDATE

	// :INFO { NICKNAME | STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }: <user>
	// <data> {<user data>};
	// ^^ I will parse this and invoke these methods appropriately ... (if we're
	// all agreed this is fine :P)

	/**
	 * The server has given the client information about a user's nickname
	 * 
	 * @param user
	 *            The user that the server is giving nickname information about
	 * @param nickname
	 *            The user's nickname
	 * */
	void updateNickname(String user, String nickname);

	/**
	 * The server has given the client information about a user's status
	 * 
	 * @param user
	 *            The user that the server is giving status information about
	 * @param status
	 *            The user's status
	 */
	void updateStatus(String user, String status);

	/**
	 * The server has given the client information about a user's personal
	 * messages
	 * 
	 * @param user
	 *            The user that the server is giving personal information about
	 * @param status
	 *            The user's personal message
	 */
	void updatePersonalMessage(String user, String personalmessage);

	/**
	 * The server has given the client information about a user's display
	 * picture
	 * 
	 * @param user
	 *            The user that the server is giving display picture information
	 *            about
	 * @param displayPicture
	 *            The user's display picture
	 */
	void updateDisplayPicture(String user, String displayPicture);

	// end :INFO

	// errors

	/*
	 * :ERROR [ UNAUTHORISED | INVALID_EMAIL | EMAIL_ALREADY_IN_USE |
	 * PASSWORD_TOO_SHORT | MISSING_ARGUMENTS | TOO_MANY_ARGUMENTS |
	 * INVALID_ARGUMENT | LOGGED_IN_FROM_OTHER_LOCATION | USER_DOES_NOT_EXIST |
	 * LOGIN_DETAILS_INCORRECT ]: <message>;
	 */

	/**
	 * Server has thrown an 'unauthorised' error. This is thrown if a user is
	 * not logged in.
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void unauthorisedError(String message);

	/**
	 * Server has thrown an 'invalid email' error. This is thrown when the sent
	 * e-mail address is invalid.
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void invalidEmailError(String message);

	/**
	 * Server has thrown an 'E-mail in use error.' This is sent when the user
	 * tries to register using an e-mail address that is already in use in the
	 * server
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void emailInuseError(String message);

	/**
	 * Server has thrown a 'password too short' error. This is thrown when the
	 * user tries to register with a password that is too short
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void passwordTooShortError(String message);

	/**
	 * Server has thrown a 'missing argument error.' This is thrown when a
	 * command is sent to the server and is missing and expected argument.
	 * 
	 * @param message
	 *            message sent the the error. Gives details.
	 */
	void missingArgumentsError(String message);

	/**
	 * Server has thrown a 'too many arguments' error. This is thrown when a
	 * command is sent with one or more unexpected arguments.
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void tooManyArgumentsError(String message);

	/**
	 * Server has thrown an 'invalid argument' error. This is thrown when an
	 * argument sent to the server is invalid.
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void invalidArgumentError(String message);

	/**
	 * Server has thrown a 'logged in from another location' error. This occurs
	 * when the user has attempted to log in in two locations.
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void loggedInFromAnotherLocationError(String message);

	/**
	 * Server has thrown an 'user does not exist' error. This is thrown when the
	 * uer attempts to log in using an username that does not exist.
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void userDoesNotExistError(String message);

	/**
	 * Server has thrown a 'login details incorrect' error. This is thrown when
	 * the user has provided incorrect log in details.
	 * 
	 * @param message
	 *            message sent with the error. Gives details.
	 */
	void logInDetailsIncorrectError(String message);
	
	// /** The logout was successful */
	//void logoutSuccessful();

}
