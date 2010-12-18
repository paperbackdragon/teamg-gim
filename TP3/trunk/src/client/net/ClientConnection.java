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
			this.reader = new networkReader(bufferedreader, gui);
			this.writer = new networkWriter(printwriter, buffer);
			
			this.readerthread = new Thread(reader);
			this.writerthread = new Thread(writer);
			
	}

	@Override
	public void ping() {
		buffer.putCommand(":PING:;");
	}

	@Override
	public void authenticate(String emailaddress, char[] password) {
		// TODO Auto-generated method stub
		// :AUTH { LOGIN | REGISTER }: <email address> <password>;
		
		 buffer.putCommand(":AUTH LOGIN: "+ emailaddress+ " " + password + ";" );

	}

	@Override
	public void quit() {
		// :QUIT:;
		buffer.putCommand(":QUIT:;");
	}

	@Override
	public void register(String emailaddress, char[] password) {
		
		// :AUTH { LOGIN | REGISTER }: <email address> <password>;
		
		buffer.putCommand(":AUTH REGISTER: " + emailaddress + " " + password + ";");

	}

	@Override
	public void friendlist() {
		// :FRIENDLIST:;
		buffer.putCommand(":FRIENDLIST:;");

	}

	@Override
	public void message(String roomid, String message) {
		// :MESSAGE: <roomid> <message>;
		buffer.putCommand(":MESSAGE: " + roomid + " " + message + ";");
		

	}

	@Override
	public void logout() {
		// :LOGOUT:;
		buffer.putCommand(":LOGOUT:;");
	}

	@Override
	public void serverTime() {
		// :SERVERSTATUS { USERS | TIME | UPTIME }:;
		buffer.putCommand(":SERVERSTATUS TIME:;");
	}

	@Override
	public void serverUptime() {
		// :SERVERSTATUS { USERS | TIME | UPTIME }:;
		buffer.putCommand(":SERVERSTATUS UPTIME:;");

	}

	@Override
	public void servernumberofusers() {
		// :SERVERSTATUS { USERS | TIME | UPTIME }:;
		buffer.putCommand(":SERVERSTATUS USERS:;");
	}

	@Override
	public void serverstatus() {
		// :SERVERSTATUS { USERS | TIME | UPTIME }:;
		buffer.putCommand(":SERVERSTATUS:;");
	}

	@Override
	public void setDisplayPicture(String displayPicture) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(":SET DISPLAY_PIC: " + displayPicture + ";");
	}

	@Override
	public void setNickname(String nickname) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(":SET NICKNAME: " + nickname + ";");

	}

	@Override
	public void setPersonalMessage(String personalmessage) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(":SET PERSONAL_MESSAGE: " + personalmessage + ";");

	}

	@Override
	public void setStatus(String status) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(":SET STATUS: " + status + ";");

	}

	@Override
	public void getDisplayPicture(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{,<user>};
		buffer.putCommand(":GET DISPLAY_PIC: " + userList + ";");

	}

	@Override
	public void getNickname(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{,<user>};
		buffer.putCommand(":GET NICKNAME: " + userList + ";");

	}

	@Override
	public void getPersonalMessage(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{,<user>};
		buffer.putCommand(":GET PERSONAL_MESSAGE: " + userList + ";");

	}

	@Override
	public void getStatus(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{,<user>};
		buffer.putCommand(":GET STATUS: " + userList + ";");
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

	@Override
	public void createGroupChat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createSingleChat(String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void type(String roomid) {
		// TODO Auto-generated method stub
		
	}

}
