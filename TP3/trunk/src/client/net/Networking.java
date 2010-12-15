package client.net;

/**
 * This will list all of the functions which the UI code expects the networking code to implement
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
	 *      example usage: serverstatus("TIME USERS")*/
	void serverstatus(String command);
	
	
	// post-login commands
	
	

}
