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
	 * @param command
	 * 		if command is null, the server will return the
	 * 		number of users online, the time, and the uptime of the server
	 * 
	 * 		if command is one or more of the following list of tokens, each followed by a space
	 * 		: "USERS, TIME, UPTIME "
	 * 		then the server will return information about each token, followed by a new line sequentially
	 * 
	 *      example usage: serverstatus("TIME USERS")
	 *      
	 *      if any arguments are invalid, the server will return an error */
	void serverstatus(String command);
	
	
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
	
	
	
	
	// post-login commands
	
	

}
