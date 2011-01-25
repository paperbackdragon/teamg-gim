package client;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import client.net.ClientConnection;

public class Model {

	private ClientConnection outLink;
	private boolean initilised = false;

	private LinkedList<String[]> newRoomList = new LinkedList<String[]>();
	private LinkedList<String> invitationsList = new LinkedList<String>();
	private LinkedList<Boolean> typeList = new LinkedList<Boolean>();

	// The file path to the current location
	private String path = null;

	/*
	 * The ACTUAL friend list. The users list contains details of users who are
	 * not necessarily in this users friend list
	 */
	private FriendList friendlist = new FriendList();
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
	private User loggedInUser;
	
	private String latestperson;

	/**
	 * Apparently doing it this way makes constructing it thread-safe...
	 */
	private static class SingeltonHolder {
		public static final Model INSTANCE = new Model();
	}

	/**
	 * Return the current instance of the class.
	 * 
	 * @return the current instance
	 */
	public static Model getInstance() {
		return SingeltonHolder.INSTANCE;
	}

	/**
	 * Do nothing, just here to beat default constructor
	 */
	public Model() {
	}

	public void setInitilised(boolean i) {
		this.initilised = i;
	}
	
	public boolean isInitilised() {
		return this.initilised;
	}
	
	public void acceptRequest(String user) {
		outLink.accept(user);
	}

	public void addfriend(String user) {
		outLink.add(user);
	}

	public void addInvitation(String from) {
		// Gordon: Critical section?
		invitationsList.add(from);
	}

	public void addNextRoom(String[] userlist) {
		// Gordon: Critical section?
		newRoomList.add(userlist);
	}

	/**
	 * Add a user to the list of global users
	 * 
	 * @param user
	 *            The ID of the user to add
	 */
	public void addUser(User user) {
		if(user.getEmail().length() > 0)
			this.users.put(user.getEmail(), user);
	}

	public void authenticate(String email, char[] pwd) {
		outLink.authenticate(email, pwd);
	}

	public void blockfriend(String user) {
		outLink.block(user);
	}

	public void createRoom(Boolean group, String[] contacts) {
		newRoomList.add(contacts);
		typeList.add(group);

		if (group)
			outLink.createGroupChat();
		else
			outLink.createSingleChat(contacts[0]);
	}

	public void endNetworkWriter() {
		outLink.endNetworkWriter();
	}

	public Collection<User> getBlockedfriends() {
		return friendlist.getBlockedUsers();
	}

	public void getDisplayPicture(String user) {
		outLink.getDisplayPicture(user);
	}

	public FriendList getFriendList() {
		return this.friendlist;
	}

	public String getLatestPerson() {
		return latestperson;
	}

	public String getNextInvitation() {
		// Gordon: Critical section?
		return invitationsList.pop();
	}

	public String[] getNextRoom() {
		// Gordon: Critical section?
		return newRoomList.pop();
	}

	public Boolean getNextType() {
		return typeList.pop();
	}

	public Collection<User> getOfflinefriends() {
		return this.friendlist.getOfflineUsers();
	}

	public Collection<User> getOnlinefriends() {
		return this.friendlist.getOnlineUsers();
	}

	public User getSelf() {
		return this.loggedInUser;
	}

	// MESSAGES TO SERVER

	// Connection stuff

	/**
	 * Get the path to the current directory
	 * 
	 * @return The path, ending with a /
	 */
	public String getPath() {
		if (this.path == null) {
			try {
				path = Model.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			} catch (URISyntaxException e) {
			}
			path = path.substring(0, path.lastIndexOf("/") + 1);
		}
		return this.path;
	}

	public ClientConnection getOutLink() {
		return this.outLink;
	}

	/**
	 * Get a user from the list of users
	 * 
	 * @param user
	 *            The ID of the user
	 * @return The User object or null of no user was found
	 */
	public User getUser(String user) {
		return this.users.get(user);
	}

	public void invite(String roomid, String user) {
		outLink.invite(roomid, user);
	}

	// End: connection stuff

	// ROOM stuff

	public void join(String roomid) {
		outLink.join(roomid);
	}

	public void leave(String roomid) {
		outLink.leave(roomid);
	}

	public void logout() {
		outLink.logout();

		// maybe should not be here...
		// outLink.setConnected(false);
	}

	public void message(String roomid, String message) {
		outLink.message(roomid, message);
	}

	public void quit() {
		outLink.logout();
		
		// TODO (heather): clean up instead of this:
		System.exit(0);
	}

	public void register(String email, char[] pwd) {
		outLink.register(email, pwd);
	}

	// end: ROOM stuff

	// Friends list stuff

	public void removefriend(String user) {
		outLink.delete(user);
	}

	public void setConnected(boolean b) {
		outLink.setConnected(false);
	}

	public void setLatestPerson(String latest) {
		// SET FOR SIGN IN / OUT / MESSAGE ALERTS
		this.latestperson = latest;
	}

	public void setOutLink(ClientConnection o) {
		this.outLink = o;
	}
	
	public void setSelf(User self) {
		this.loggedInUser = self;
	}

	public void type(String roomid) {
		outLink.type(roomid);
	}

	public void unblockfriend(String user) {
		outLink.unblock(user);
	}

	public void users(String roomid) {
		outLink.roomusers(roomid);
	}

}