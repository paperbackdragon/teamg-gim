package client.ui;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import client.net.ClientConnection;
import client.net.ServerConnection;

public class ClientModel {

	private ServerConnection inLink = new ServerConnection();
	private ClientConnection outLink = new ClientConnection(inLink);

	private LinkedList<String[]> newRoomList = new LinkedList<String[]>();
	private LinkedList<String> invitationsList = new LinkedList<String>();
	private LinkedList<Boolean> typeList = new LinkedList<Boolean>();
	
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();

	// buddy list
	private String[] onlinefriends;
	private String[] offlinefriends;
	private String[] blockedfriends;
	
	// options
	
	private String personalMessage;
	private String status;
	private String nickname;
	private String displayPicture;
	private String username;
	private String latestperson;
	
	public void setOwnUserName(String username) {
		this.username = username;	
	}
	
	public String getOwnUserName() {
		return username;
	}
	
	public void setOwnPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}
	
	public String getOwnPersonalMessage() {
		return personalMessage;
	}
	
	public void setOwnStatus(String status) {
		this.status = status;
	}
	
	public String getOwnStatus() {
		return status;
	}
	
	public String getOwnNickname() {
		return nickname;
	}
	
	public void setOwnNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Do nothing, just here to beat default constructor
	 */
	public ClientModel() {
	}

	/**
	 * Apparently doing it this way makes constructing it thread-safe...
	 */
	private static class SingeltonHolder {
		public static final ClientModel INSTANCE = new ClientModel();
	}

	/**
	 * Return the current instance of the data class.
	 * 
	 * @return the current instance
	 */
	public static ClientModel getInstance() {
		return SingeltonHolder.INSTANCE;
	}

	// ACCESSORS

	// public LinkedList<String[]> getRoomList() {
	// return newRoomList;
	// }

	public String[] getNextRoom() {
		// Gordon: Critical section?
		return newRoomList.pop();
	}

	public void addNextRoom(String[] userlist) {
		// Gordon: Critical section?
		newRoomList.add(userlist);
	}

	public Boolean getNextType() {
		return typeList.pop();
	}

	/**
	 * Add a user to the list of global users
	 * 
	 * @param user
	 *            The ID of the user to add
	 */
	public void addUser(String user) {
		this.users.put(user, new User(user));
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

	public void addInvitation(String from) {
		// Gordon: Critical section?
		invitationsList.add(from);
	}

	public String getNextInvitation() {
		// Gordon: Critical section?
		return invitationsList.pop();
	}

	public String[] getOnlinefriends() {
		return onlinefriends;
	}

	public void setOnlinefriends(String[] onlinefriends) {
		this.onlinefriends = onlinefriends;
	}

	public String[] getOfflinefriends() {
		return offlinefriends;
	}

	public void setOfflinefriends(String[] offlinefriends) {
		this.offlinefriends = offlinefriends;
	}

	public String[] getBlockedfriends() {
		return blockedfriends;
	}

	public void setBlockedfriends(String[] blockedfriends) {
		this.blockedfriends = blockedfriends;
	}

	// MESSAGES TO SERVER

	// Connection stuff

	public void authenticate(String email, char[] pwd) {
		outLink.authenticate(email, pwd);
	}

	public void register(String email, char[] pwd) {
		outLink.register(email, pwd);
	}

	public void message(String roomid, String message) {
		outLink.message(roomid, message);
	}

	public void logout() {
		outLink.logout();

		// maybe should not be here...
		// outLink.setConnected(false);
	}

	public void quit() {
		outLink.logout();
		// TODO (heather): clean up instead of this:
		System.exit(0);
	}

	// End: connection stuff

	// ROOM stuff

	public void createRoom(Boolean group, String[] contacts) {
		newRoomList.add(contacts);
		typeList.add(group);

		if (group)
			outLink.createGroupChat();
		else
			outLink.createSingleChat(contacts[0]);
	}

	public void invite(String roomid, String user) {
		outLink.invite(roomid, user);
	}

	public void join(String roomid) {
		outLink.join(roomid);
	}

	public void leave(String roomid) {
		outLink.leave(roomid);
	}

	public void users(String roomid) {
		outLink.roomusers(roomid);
	}

	public void type(String roomid) {
		outLink.type(roomid);
	}

	// end: ROOM stuff

	// Friends list stuff

	public void addfriend(String user) {
		outLink.add(user);
	}

	public void blockfriend(String user) {
		outLink.block(user);
	}

	public void unblockfriend(String user) {
		outLink.unblock(user);
	}

	public void removefriend(String user) {
		outLink.delete(user);
	}

	// end: Friends list stuff

	// GET stuff (when something has been updated)

	public void getFriendList() {
		outLink.friendlist();
	}

	public void getStatus(String user) {
		outLink.getStatus(user);
	}

	public void getPersonalMessage(String user) {
		outLink.getPersonalMessage(user);
	}

	public void getNickName(String user) {
		outLink.getNickname(user);
	}

	public void getDisplayPicture(String user) {
		outLink.getDisplayPicture(user);
	}

	public void setStatus(String status) {
		outLink.setStatus(status);

	}
	
	public void setPersonalMessage(String personalmessage) {
		outLink.setPersonalMessage(personalmessage);
	}
	
	public void setNickname(String nickname) {
		outLink.setNickname(nickname);
	}

	public void setConnected(boolean b) {
		outLink.setConnected(false);
	}
	
	public void endNetworkWriter() {
		outLink.endNetworkWriter();
	}

	public String getLatestPerson() {
		return latestperson;
		// TODO Auto-generated method stub
	}
	
	public void setLatestPerson(String latest) {
		//SET FOR SIGN IN / OUT / MESSAGE ALERTS
		this.latestperson = latest;
	}

	public void acceptRequest(String user) {
		outLink.accept(user);
		
	}

}