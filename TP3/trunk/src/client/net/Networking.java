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
