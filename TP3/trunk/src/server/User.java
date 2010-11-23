package server;

import java.util.HashMap;

public class User {

	public enum Status {
		OFFLINE, ONLINE, BUSY, AWAY, APPEAROFFLINE
	}

	private String id = null;
	private String passwordHash = null;
	private Status status = Status.OFFLINE;
	private String nickname = null;
	private String personalMessage = "";
	private HashMap<String, User> friendList = new HashMap<String, User>();
	private HashMap<String, User> inFreindList = new HashMap<String, User>();
	private HashMap<String, User> blockedUsers = new HashMap<String, User>();

	public User(String id, String passwordHash, Status status, String nickname, String personalMessage,
			HashMap<String, User> friendList, HashMap<String, User> inFreindList, HashMap<String, User> blockedUsers) {
		super();
		this.id = id;
		this.passwordHash = passwordHash;
		this.status = status;
		this.nickname = nickname;
		this.personalMessage = personalMessage;
		this.friendList = friendList;
		this.inFreindList = inFreindList;
		this.blockedUsers = blockedUsers;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPersonalMessage() {
		return personalMessage;
	}

	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}

	public HashMap<String, User> getFriendList() {
		return friendList;
	}

	public void setFriendList(HashMap<String, User> friendList) {
		this.friendList = friendList;
	}

	public HashMap<String, User> getInFreindList() {
		return inFreindList;
	}

	public void setInFreindList(HashMap<String, User> inFreindList) {
		this.inFreindList = inFreindList;
	}

	public HashMap<String, User> getBlockedUsers() {
		return blockedUsers;
	}

	public void setBlockedUsers(HashMap<String, User> blockedUsers) {
		this.blockedUsers = blockedUsers;
	}

}
