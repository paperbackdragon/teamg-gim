package client.net;

import java.util.ArrayList;

import util.CommandBuffer;

public class ClientConnection implements NetworkingOut {

	private networkReader reader;
	private networkWriter writer;
	private CommandBuffer buffer;

	/**
	 * Creates a connection to the server
	 * 
	 * @param incoming
	 *            the class that methods are called from when a command is
	 *            received from the server
	 */
	public ClientConnection(ServerConnection incoming) {
		
			// initiate connection to server
		
			
		
			// make a buffered reader
			// make a print writer
		
			this.buffer = new CommandBuffer();
			this.reader = new networkReader(null, buffer);
			this.writer = new networkWriter(null, buffer);
	}

	@Override
	public void ping() {
		// TODO Auto-generated method stub

	}

	@Override
	public void authenticate(String emailaddress, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(String emailaddress, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void friendlist() {
		// TODO Auto-generated method stub

	}

	@Override
	public void groupmessage(String roomid, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void serverTime() {
		// TODO Auto-generated method stub

	}

	@Override
	public void serverUptime() {
		// TODO Auto-generated method stub

	}

	@Override
	public void servernumberofusers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void serverstatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisplayPicture(String displayPicture) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNickname(String nickname) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPersonalMessage(String personalmessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatus(String status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createGroupChat(Boolean publicchat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getDisplayPicture(String userList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getNickname(String userList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPersonalMessage(String userList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getStatus(String userList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void invite(String roomid, String user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void join(String roomid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leave(String roomid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void roomusers(String roomid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void accept(String friend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(String friend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void block(String friend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decline(String friend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String friend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unblock(String friend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAttributes(ArrayList<String> attributes,
			ArrayList<String> users) {
		// TODO Auto-generated method stub

	}

}
