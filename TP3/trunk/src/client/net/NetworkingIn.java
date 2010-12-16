package client.net;

import java.util.ArrayList;

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
	
	// need james to clarify on these:
	
	// :ROOM [ CREATED | JOINED | LEFT | INVITED | USERS ]: <roomid> {<user>};
	
	void created();
	
	void joined();
	
	void left();
	
	void invited();
	
	void users();
	
	// end :ROOM
	
	// :p feel free to tell me if you don't think this is a sensible way of dealing with this: 
	
	// :FRIENDLIST: ONLINE <user>{,<user>} OFFLINE <user>{,<user>} BLOCKED
	
	/** The server has sent the online status of all the users on the buddy list
	 * @param onlinelist
	 * 		An arraylist of the users online on the buddy list
	 * @param offlinelist
	 * 		An arraylist of the users offline on the buddy list
	 * @param blockedlist
	 * 		An arraylist of the users who are blocked on the buddy list
	 *  */
	
	void friendlist(ArrayList<String> onlinelist, ArrayList<String> offlinelist, ArrayList<String> blockedlist);
	
	// :FRIENDREQUEST: <user> <nickname>;
	
	/** the server has notified that the user has received a friend request
	 * @param user
	 * 		The user that has sent a friend request
	 * @param nickname
	 * 		The user's nickname at the time of sending the friend request */
	void friendrequest(String user, String nickname);
	
	// :UPDATE [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <user>;
	
	/** The server has notified that a user has changed their nickname
	 * @param user
	 * 		The user that changed their nickname */
	void notifyNickname(String user);
	
	/** The server has notified that a user has changed their status 
	 * @param user
	 * 		The user that has changed their status*/
	void notifyStatus(String user);
	
	/** The server has notified that a user has changed their personal message
	 * @param user
	 * 		The user that has changed their personal message*/
	void notifyPersonalMessage(String user);
	
	/** The server has notified that a user has changed their display picture
	 * @param user
	 * 		The user that has changed their display picture*/
	void notifyDisplayPicture(String user);
	
	// end :UPDATE
	
	
	// :INFO { NICKNAME | STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }: <user> <data> {<user data>};
	// ^^ I will parse this and invoke these methods appropriately ... (if we're all agreed this is fine :P)
	
	/** The server has given the client information about a user's nickname
	 * @param user 
	 * 		The user that the server is giving nickname information about
	 * @param nickname
	 * 		The user's nickname
	 *  */
	void updateNickname(String user, String nickname);
	
	/** The server has given the client information about a user's status
	 * @param user
	 * 		The user that the server is giving status information about
	 * @param status
	 * 		The user's status */
	void updateStatus(String user, String status);
	
	/** The server has given the client information about a user's personal messages 
	 * @param user
	 * 		The user that the server is giving personal information about
	 * @param status
	 * 		The user's personal message */
	void updatePersonalMessage(String user, String personalmessage);
	
	/** The server has given the client information about a user's display picture 
	 * @param user
	 * 		The user that the server is giving display picture information about
	 * @param displayPicture
	 * 		The user's display picture */
	void updateDisplayPicture(String user, String displayPicture);
	
	// end :INFO
	
	// errors
	
	

}
