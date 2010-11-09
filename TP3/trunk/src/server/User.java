package server;

import java.util.HashMap;

public class User {
	
	public enum Status {OFFLINE, ONLINE, BUSY, AWAY, APPEAROFFLINE}
	
	private String id = null;
	private String passwordHash = null;
	private Status status = Status.OFFLINE;
	private String nickname = null;
	private String personalMessage;
	private HashMap<String, User> friendList = new HashMap<String, User>();
	private HashMap<String, User> inFreindList = new HashMap<String, User>();
	
}
