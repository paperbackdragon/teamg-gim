package client.net;

/**
 * This will list all of the functions which the UI code expects the networking code to implement, as part of the
 * networking protocol. Although the server's response is out of the scope of this aspect, it will be indicated in
 * the documentation descriptively how the server will respond, as is considered helpful.
 * 
 * @authors James McMinn, Gordon Martin
 *
 */

public interface Networking {
	
	// pre-login commands
	
	// :PING:;
	
	/** used to implement the "keep-alive" command. Sends a ping to the server to 
	 * check if the client is still "alive" */
	void ping();
	
	
	// :SERVERSTATUS { USERS | TIME | UPTIME }:;
	
	/** 
	 * sends a request to the server to return (as a line):
	 *  the number of users online
	 *  the time,
	 *  the uptime of the server */
	void serverstatus();
	
	/** sends a request to the server to the number of users logged in */
	void servernumberofusers();
	
	/** sends a request to the server to return the server's time */
	void serverTime();
	
	/** sends a request to the server to the server to return the uptime of the server */
	void serverUptime();
	
	// :SERVERSTATUS ends
	
	// :AUTH { LOGIN | REGISTER }: <email address> <password>;
	
	/** sends a request to the server to log in 
	 * @param emailaddress
	 * 		a valid e-mail address
	 * @param password
	 * 		the password corresponding to the e-mail address
	 * 
	 *  The server will return the user's authorisation state: logged in, or unauthorised */
	void authenticate(String emailaddress, String password);
	
	
	/** sends a request to the server to register an account
		 * @param emailaddress
	 * 		the desired e-mail address
	 * @param password
	 * 		the desired password
	 * 
	 *  If the registration is unsuccessful, the server will respond with an error message indicating why */
	void register(String emailaddress, String password);
	
	// :AUTH ends
	
	// :QUIT:;
	
	/** sends a request to the server to log the user out. 
	 * If handled successfully, the connection will be terminated from the server. */
	void quit();
	
	// post-login commands
	
	
	// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
	
	/** Sends a request to the server to set the user's nickname
	 * @param nickname 
	 * 		The desired nickname*/
	void setNickname(String nickname);
	
	/** Sends a request to the server to set the user's availability
	 * @param status 
	 * 		The desired status*/
	void setStatus(String status);
	
	
	/** Sends a request to the server to set the user's personal message
	 * @param personalmessage 
	 * 		The desired personal message*/
	void setPersonalMessage(String personalmessage);
	
	// figure this one out later... Does it take a string?
	
	/** Sends a request to the server to set the user's display picture
	 * @param x [TO BE IMPLIMENTED] */
	void setDisplayPicture(String displayPicture);
	
	// :SET ends
	
	// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }: <user>{,<user>};
	
	/** Sends a request to the server to return the nicknames of a list of users
	 * @param userlist
	 * 		  A list of one or more users, separated by commas */
	void getNickname(String userList);
	
	/** Sends a request to the server to return the statuses of a list of users
	 * @param userlist
	 * 		  A list of one or more users, separated by commas */
	void getStatus(String userList);
	
	
	/** Sends a request to the server to return the personal messages of a list of users
	 * @param userlist
	 * 		  A list of one or more users, separated by commas */
	void getPersonalMessage(String userList);
	
	/** Sends a request to the server to return the display pictures of a list of users
	 * @param userlist
	 * 		  A list of one or more users, separated by commas */
	void getDisplayPicture(String userList);
	
	
	// :GET ends
	
	// :FRIENDLIST:;
	
	/** Sends a request to the server to return the user's friend list*/
	void friendlist();
	
   // :ROOM [ CREATE {PUBLIC} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> | <user>};
	
	/** Sends a request to the server to create a new group chat 
	 * @param publicchat
	 * 		if true, then any user has permission to join
	 * 		if false, only those invited to the chat has permission to join */
	void createGroupChat(Boolean publicchat);
	
	
	/** sends a request to the server to invite a user to a group chat
	 * @param roomid 
	 * 		the id of the room to give the user permission to join the group chat of
	 * @param user
	 * 		the user to give permission to join the group chat */
	void invite(String roomid, String user);
	
	
	/** send a request to the server to join a group chat 
	 * @param roomid
	 * 		the id of the room to join*/
	void join(String roomid);
	
	
	/** sends a request to the server to leave a group chat */
	void leave(String roomid);
	
	/** sends a request to the server to return a list of users currently in a group chat */
	void roomusers(String roomid);
	
	// :ROOM ends
	
	// :MESSAGE: <roomid> <message>;
	
	/** sends a request to the server to send a message to a group chat
	 * @param roomid
	 * 		the ID of the room
	 * @param message
	 * 		the message to send to the room */
	void groupmessage(String roomid, String message);
	
	
	// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]: <target>;
	
	
	/** sends a request to the server to add a friend
	 * @param friend
	 * 		The e-mail address of the user to add */
	void addfriend(String friend);
	
	/** sends a request to the server to block a friend
	 * @param friend
	 * 		The e-mail address of the user to block */
	void blockfriend(String friend);
	
	/** sends a request to the server to unblock a friend
	 * @param friend
	 * 		The e-mail address of the user to unblock */
	void unblockfriend(String friend);
	
	/** sends a request to the server to accept the friend request of a user
	 * @param friend
	 * 		The e-mail address of the user to accept the request from */
	void acceptfriend(String friend);
	
	/** sends a request to the server to decline the friend request of a user
	 * @param friend
	 * 		The e-mail address of the user to decline the request from */
	void declinefriend(String friend);
	
	/** sends a request to the server to delete a friend
	 * @param friend
	 * 		The e-mail address of the user to delete */
	void deletefriend(String friend);
	
	// end :FRIEND
	
	// :LOGOUT:;
	
	/** Send a request to the server to log out, but retain a connection to the server */
	void logout();
	
}
