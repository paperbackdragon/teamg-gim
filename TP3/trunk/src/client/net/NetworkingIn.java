package client.net;

public interface NetworkingIn {
	
	// prelogin
	
	// :OKAY:;
	
	/** The last command was send successfully (this is sent where there is no other response) */
	void okay();
	
	// :SERVERSTATUS { USERS | TIME | UPTIME }:;
	
	/** The server has sent information about the global status of the server
	 * @param status
	 * 		The string with global information */
	void status(String status);
	
	/** The server has sent information about the server's time 
	 * @param servertime
	 * 		The server's time*/
	void servertime(String servertime);
	
	/** The server has sent the number of users connected to the server
	 * @param usercount
	 * 		The amount of user's connected */
	void usercount(String usercount);
	
	/** The server has sent information about its uptime
	 * @param uptime
	 * 		The uptime of the server */
	void uptime(String uptime);
	
	// :SERVERSTATUS ends
	
	/** The server has indicated that it wants to force the client to disconnect gracefully.
	 * @param message
	 * 		The reason why the server wants the client to disconnect gracefully. */
	void kill(String message);
	
	// :BROADCAST: <message>;
	
	/** The server has sent a global message to all connected user. This is likely to contain critical information
	 * and should be clearly displayed to the user.
	 * @param message
	 * 		The message from the server.  */
	void broadcast(String message);
	
	
	// :AUTH [ LOGGEDIN | UNAUTHORIZED ]:;
	
	// NOT SURE ABOUT THESE TWO... WILL TALK TO JAMES
	
	/** The server indicated the user is authorised */
	void authorised();
	
	/** The server indicated the user is unauthorised */
	void unauthorised();
	
	// end :AUTH
	
	// postlogin
	
	//:MESSAGE: <roomid> <sender> <message>;
	
	/** The server has indicated that the user has received a message
	 * @param roomid
	 * 		The id of the room the message was sent to
	 * @param sender
	 * 		The person the message was received from
	 * @param message
	 * 		The message the sender sent */
	void message(String roomid, String sender, String message);
	
	// :ROOM [ CREATED | JOINED | LEFT | INVITED | USERS ]: <roomid> {<user>};
	
	void created();
	
	void joined();
	
	void left();
	
	void invited();
	
	void users();
	
	// errors
	

}
