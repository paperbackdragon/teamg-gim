package client.ui;

import java.util.LinkedList;

import client.net.ClientConnection;
import client.net.ServerConnection;

public class ClientModel {
	private ClientConnection outLink;
	private ServerConnection inLink;
	private LinkedList<String[]> newRoomList;// = new LinkedList<String[]>();
	
	//CONSTRUCTOR
	public ClientModel() {
		inLink = new ServerConnection();
		outLink = new ClientConnection(inLink);
		
		newRoomList = new LinkedList<String[]>();
	}
	
	//ACCESSORS
	public LinkedList<String[]> getRoomList() {
		return newRoomList;
	}
	
	//MESSAGES TO SERVER
	
	// Connection stuff
	
	public void authenticate(String email, char[] pwd) {
		outLink.authenticate(email, pwd);
	}
	
	public void register(String email, char[] pwd) {
		outLink.register(email, pwd);
	}
	
	public void logout() {
		outLink.logout();
	}
	
	public void quit() {
		outLink.logout();
		// TODO clean up instead of this:
		System.exit(0);
	}
	
	//End: connection stuff
	
	
	// ROOM stuff
	
	public void createRoom(Boolean group, String[] contacts) {
		newRoomList.add(contacts);
		if(group)
			outLink.createGroupChat();
		else
			outLink.createSingleChat(null);// TODO make this not null
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
	
	//end: Friends list stuff
}