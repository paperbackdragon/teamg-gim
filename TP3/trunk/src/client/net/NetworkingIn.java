package client.net;

public interface NetworkingIn {
	
	// prelogin
	
	/** The last command was send successfully */
	
	// :OKAY:;
	
	void okay();
	
	// :SERVERSTATUS { USERS | TIME | UPTIME }:;
	
	void status(String status);
	
	void servertime(String servertime);
	
	void usercount(String usercount);
	
	void uptime(String uptime);
	
	// :SERVERSTATUS ends
	
	void kill(String message);
	
	// :BROADCAST: <message>;
	
	/** */
	void broadcast();
	
	
	// :AUTH [ LOGGEDIN | UNAUTHORIZED ]:;
	
	// NOT SURE ABOUT THESE TWO... WILL TALK TO JAMES
	
	void authorised();
	
	void unauthorised();
	
	
	
	
	// postlogin
	
	

}
