package client.ui;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import client.net.ClientConnection;

public class ClientModel {

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
		try {
			SingeltonHolder.INSTANCE.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SingeltonHolder.INSTANCE;
	}

	private ClientConnection outLink;

	private LinkedList<String[]> newRoomList = new LinkedList<String[]>();
	private LinkedList<String> invitationsList = new LinkedList<String>();
	private LinkedList<Boolean> typeList = new LinkedList<Boolean>();

	private String path = null;
	
	// buddy list
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
	private String[] onlinefriends;
	private String[] offlinefriends;
	private String[] blockedfriends;
	
	// options
	private String personalMessage;
	private String status;
	private String nickname;
	private String username;
	private String latestperson;

	/**
	 * Do nothing, just here to beat default constructor
	 */
	public ClientModel() {
		System.out.println("Creating model.");
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
	public void addUser(String user) {
		this.users.put(user, new User(user));
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

	public String[] getBlockedfriends() {
		return blockedfriends;
	}

	// ACCESSORS

	// public LinkedList<String[]> getRoomList() {
	// return newRoomList;
	// }

	public void getDisplayPicture(String user) {
		outLink.getDisplayPicture(user);
	}

	public void getFriendList() {
		outLink.friendlist();
	}

	public String getLatestPerson() {
		return latestperson;
		// TODO Auto-generated method stub
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

	public void getNickName(String user) {
		outLink.getNickname(user);
	}

	public String[] getOfflinefriends() {
		return offlinefriends;
	}

	public String[] getOnlinefriends() {
		return onlinefriends;
	}

	public String getOwnNickname() {
		return nickname;
	}

	public String getOwnPersonalMessage() {
		return personalMessage;
	}

	public String getOwnStatus() {
		return status;
	}

	public String getOwnUserName() {
		return username;
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
				path = ClientModel.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			} catch (URISyntaxException e) {
			}
			path = path.substring(0, path.lastIndexOf("/") + 1);
		}
		return this.path;
	}

	public void getPersonalMessage(String user) {
		outLink.getPersonalMessage(user);
	}

	public void getStatus(String user) {
		outLink.getStatus(user);
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

	public void setBlockedfriends(String[] blockedfriends) {
		this.blockedfriends = blockedfriends;
	}

	public void setConnected(boolean b) {
		outLink.setConnected(false);
	}

	public void setLatestPerson(String latest) {
		// SET FOR SIGN IN / OUT / MESSAGE ALERTS
		this.latestperson = latest;
	}

	// end: Friends list stuff

	// GET stuff (when something has been updated)

	public void setNickname(String nickname) {
		outLink.setNickname(nickname);
	}

	public void setOfflinefriends(String[] offlinefriends) {
		this.offlinefriends = offlinefriends;
	}

	public void setOnlinefriends(String[] onlinefriends) {
		this.onlinefriends = onlinefriends;
	}

	public void setOutLink(ClientConnection o) {
		this.outLink = o;
	}

	public void setOwnNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setOwnPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}

	public void setOwnStatus(String status) {
		this.status = status;
	}

	public void setOwnUserName(String username) {
		this.username = username;
	}

	public void setPersonalMessage(String personalmessage) {
		outLink.setPersonalMessage(personalmessage);
	}

	public void setStatus(String status) {
		outLink.setStatus(status);

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