package client.ui;

import java.util.ArrayList;
import java.util.LinkedList;

import client.net.ClientConnection;
import client.net.ServerConnection;

public class ClientModel {
	private ServerConnection inLink;
	private ClientConnection outLink;
	private LinkedList<String[]> newRoomList;
	private LinkedList<String> invitationsList;
	private LinkedList<Boolean> typeList;

	// buddy list
	private String[] onlinefriends;
	private String[] offlinefriends;
	private String[] blockedfriends;

	private ArrayList<User> users;

	//private Object lock;

	// CONSTRUCTOR
	public ClientModel() {
		inLink = new ServerConnection();
		outLink = new ClientConnection(inLink);

		newRoomList = new LinkedList<String[]>();
		typeList = new LinkedList<Boolean>();

		// Gordon: if you don't think this is a sensible way to do it, we can
		// all discuss this :P
		invitationsList = new LinkedList<String>();
		users = new ArrayList<User>();
		//lock = new Object();
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
	
	public void addUser(String user) {
		users.add(new User(user));
	}
	
	public User getUser(String user) {
		if (!users.isEmpty()) {
			for (int i = 0; i < users.size(); i ++) {
				if (users.get(i).getEmail().equals(user)) {
					return users.get(i);
				}
			}
			return null;
		}
		return null;
		
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
		//outLink.setConnected(false);
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

	public void setContactList() {
		outLink.friendlist();
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

	public void setConnected(boolean b) {
		outLink.setConnected(false);
	}

}