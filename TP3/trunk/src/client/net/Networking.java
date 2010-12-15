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
	
	/** used to implement the "keep-alive" command. Sends a ping to the server to 
	 * check if the client is still "alive" */
	void ping();
	
	/** Returns global information about the server 
	 * the server will return:
	 *  the number of users online
	 *  the time,
	 *  the uptime of the server */
	void serverstatus();
	
	/** returns the number of users logged in */
	void servernumberofusers();
	
	/** returns the server's time */
	void serverTime();
	
	/** returns the uptime of the server */
	void serverUptime();
	
	
	
	/** attempts a log in 
	 * @param emailaddress
	 * 		a valid e-mail address
	 * @param password
	 * 		the password corresponding to the e-mail address
	 * 
	 *  The server will return the user's authorisation state: logged in, or unauthorised */
	void authenticate(String emailaddress, String password);
	
	
	/** attempts to register an account
		 * @param emailaddress
	 * 		the desired e-mail address
	 * @param password
	 * 		the desired password
	 * 
	 *  If the registration is unsuccessful, the server will respond with an error message indicating why */
	void register(String emailaddress, String password);
	
	/** will log the user out from the server. The connection will be terminated from the server. */
	void quit();
	
	
	
	
	
	
	// post-login commands
	
	
	// split up 
	void set(String attribute, String value);
	
	void get(String attribute, String userlist);
	
	//endcomment
	
	void friendlist();
	
	// split up
	
	void room(String action, String id);
	
	// endcomment
	
	void groupmessage(String roomid, String message);
	
	
	// split up
	void friend(String command, String target);
	
	// endcomment
	
	
	void logout();
	
	
	
	

}
