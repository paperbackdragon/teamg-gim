package client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import util.CommandBuffer;

public class ClientConnection implements NetworkingOut {

	private Socket serverConnection;
	private BufferedReader bufferedreader;
	private PrintWriter printwriter;
	
	private networkReader reader;
	private networkWriter writer;
	private CommandBuffer buffer;
	private networkController controller;
	
	private Thread readerthread;
	private Thread writerthread;
	private Thread controllerthread;
	private ServerConnection gui;

	/**
	 * Creates a connection to the server. Allows the GUI to perform action on server. 
	 * 
	 * @param incoming
	 *            the class that methods are called from when a command is
	 *            received from the server
	 */
	public ClientConnection(ServerConnection gui) {
			this.gui = gui;
		
			// initiate connection to server
			try {
				serverConnection = new Socket("127.0.0.1", 4444);
			} catch (UnknownHostException e) {
				System.out.println("Could not connect to server");
				// tell the GUI
				
			} catch (IOException e) {
				System.out.println("uhm... handle this somehow... what is it anyway");
			}
		
			
			// make a buffered reader and print writer
			try {
				bufferedreader = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
				printwriter = new PrintWriter(serverConnection.getOutputStream(), true);
			} catch (IOException e) {
				System.out.println("aye... what the hell do we do if this happens :|.");
			}
		
			// get the reader and writer, and buffer on the go... ;x
			this.buffer = new CommandBuffer();
			this.reader = new networkReader(bufferedreader, buffer);
			this.writer = new networkWriter(printwriter, buffer);
			this.controller = new networkController(buffer, gui);
			
			this.readerthread = new Thread(reader);
			this.writerthread = new Thread(writer);
			this.controllerthread = new Thread(controller);
			
	}

	@Override
	public void ping() {
		// TODO Auto-generated method stub

	}

	@Override
	public void authenticate(String emailaddress, char[] password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(String emailaddress, char[] password) {
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
