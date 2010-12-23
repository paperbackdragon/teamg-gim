package client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import util.Command;
import util.CommandBuffer;

public class ClientConnection implements NetworkingOut, Runnable {

	private Socket serverConnection;
	private BufferedReader bufferedreader;
	private PrintWriter printwriter;

	private networkReader reader;
	private networkWriter writer;
	private CommandBuffer<String> buffer;

	private Thread readerthread;
	private Thread writerthread;
	private Thread controllerthread;
	private ServerConnection gui;
	private Thread thread;

	/**
	 * Creates a connection to the server. Allows the GUI to perform action on
	 * server.
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
			System.out
					.println("uhm... handle this somehow... what is it anyway");
		}

		// make a buffered reader and print writer
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					serverConnection.getInputStream()));
			printwriter = new PrintWriter(serverConnection.getOutputStream(),
					true);
		} catch (IOException e) {
			System.out
					.println("aye... what the hell do we do if this happens :|.");
		}

		// get the reader and writer, and buffer on the go... ;x
		this.buffer = new CommandBuffer<String>();
		this.reader = new networkReader(bufferedreader, gui);
		this.writer = new networkWriter(printwriter, buffer);

		this.readerthread = new Thread(reader);
		this.writerthread = new Thread(writer);

		readerthread.start();
		writerthread.start();

		this.thread = new Thread(this);
		thread.start();
	}

	public void run() {
		// heart beat
		// NO IDEA IF THIS IS SENSIBLE, OR EVEN THE WAY TO DO IT

		while (true) {
			try {
				Thread.currentThread();
				Thread.sleep(12000);
				ping();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Oh noes!");
			}
		}

	}

	@Override
	public void ping() {
		buffer.putCommand(":PING:;");
	}

	@Override
	public void authenticate(String emailaddress, char[] password) {
		// TODO Auto-generated method stub
		// :AUTH { LOGIN | REGISTER }: <email address> <password>;

		buffer.putCommand(":AUTH LOGIN: " + Command.encode(emailaddress) + " "
				+ Command.encode(new String(password)) + ";");
	}

	@Override
	public void quit() {
		// :QUIT:;
		buffer.putCommand(":QUIT:;");
	}

	@Override
	public void register(String emailaddress, char[] password) {

		// :AUTH { LOGIN | REGISTER }: <email address> <password>;

		buffer.putCommand(":AUTH REGISTER: " + Command.encode(emailaddress)
				+ " " + Command.encode(new String(password)) + ";");

	}

	@Override
	public void friendlist() {
		// :FRIENDLIST:;
		buffer.putCommand(":GET FRIENDLIST:;");

	}

	@Override
	public void message(String roomid, String message) {
		// :MESSAGE: <roomid> <message>;
		buffer.putCommand(":MESSAGE: " + Command.encode(roomid) + " "
				+ Command.encode(message) + ";");

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
		buffer.putCommand(":SET DISPLAY_PIC: " + Command.encode(displayPicture)
				+ ";");
	}

	@Override
	public void setNickname(String nickname) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(":SET NICKNAME: " + Command.encode(nickname) + ";");

	}

	@Override
	public void setPersonalMessage(String personalmessage) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(":SET PERSONAL_MESSAGE: "
				+ Command.encode(personalmessage) + ";");

	}

	@Override
	public void setStatus(String status) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(":SET STATUS: " + Command.encode(status) + ";");

	}

	@Override
	public void getDisplayPicture(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{ <user>};
		String[] data = userList.split(" ");
		String userListString = "";

		for (int i = 0; i < data.length; i++) {
			Command.encode(data[i]);
			userListString += data[i];
			
			if (i < data.length) userListString += " ";
		}
		buffer.putCommand(":GET DISPLAY_PIC: " + userListString + ";");
	}

	@Override
	public void getNickname(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{ <user>};

		String[] data = userList.split(" ");
		String userListString = "";

		for (int i = 0; i < data.length; i++) {
			Command.encode(data[i]);
			userListString += data[i];
			
			if (i < data.length) userListString += " ";
		}
		buffer.putCommand(":GET NICKNAME: " + userListString + ";");

	}

	@Override
	public void getPersonalMessage(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{ <user>};
		String[] data = userList.split(" ");
		String userListString = "";

		for (int i = 0; i < data.length; i++) {
			Command.encode(data[i]);
			userListString += data[i];
			
			if (i < data.length) userListString += " ";
		}

		buffer.putCommand(":GET PERSONAL_MESSAGE: " + userListString + ";");

	}

	@Override
	public void getStatus(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{ <user>};
		String[] data = userList.split(" ");
		String userListString = "";

		for (int i = 0; i < data.length; i++) {
			Command.encode(data[i]);
			userListString += data[i];
			
			if (i < data.length) userListString += " ";
		}

		buffer.putCommand(":GET STATUS: " + userListString + ";");
	}

	@Override
	public void invite(String roomid, String user) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
		// <user>};
		buffer.putCommand(":ROOM INVITE: " + Command.encode(roomid) + " "
				+ Command.encode(user) + ";");
	}

	@Override
	public void join(String roomid) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
		// <user>};
		buffer.putCommand(":ROOM JOIN: " + Command.encode(roomid) + ";");
	}

	@Override
	public void leave(String roomid) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
		// <user>};
		buffer.putCommand(":ROOM LEAVE: " + Command.encode(roomid) + ";");
	}

	@Override
	public void roomusers(String roomid) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
		// <user>};
		buffer.putCommand(":ROOM USERS: " + Command.encode(roomid) + ";");
	}

	@Override
	public void accept(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(":FRIEND ACCEPT: " + Command.encode(friend) + ";");
	}

	@Override
	public void add(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(":FRIEND ADD: " + Command.encode(friend) + ";");
	}

	@Override
	public void block(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(":FRIEND BLOCK: " + Command.encode(friend) + ";");
	}

	@Override
	public void decline(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(":FRIEND DECLINE: " + Command.encode(friend) + ";");
	}

	@Override
	public void delete(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(":FRIEND DELETE: " + Command.encode(friend) + ";");
	}

	@Override
	public void unblock(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(":FRIEND UNBLOCK: " + Command.encode(friend) + ";");
	}

	@Override
	public void getAttributes(ArrayList<String> attributes,
			ArrayList<String> users) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{,<user>};

		String attributesstring = "";
		for (int i = 0; i < attributes.size(); i++) {
			attributesstring += attributes.get(i);

			if (i < attributes.size()) {
				attributesstring += " ";
			}
		}

		String usersstring = "";
		for (int j = 0; j < users.size(); j++) {
			usersstring += Command.encode(users.get(j));

			if (j < users.size()) {
				usersstring += " ";
			}

		}

		buffer
				.putCommand(":GET: " + attributesstring + ":" + " " + usersstring
						+ ";");

	}

	@Override
	public void createGroupChat() {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS | TYPE ]:
		// {<roomid> |
		// <user>};

		buffer.putCommand(":ROOM: CREATE GROUP:;");
	}

	@Override
	public void createSingleChat(String user) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS | TYPE ]:
		// {<roomid> |
		// <user>};
		buffer.putCommand(":ROOM: CREATE: " + Command.encode(user) + ";");
	}

	@Override
	public void type(String roomid) {
		buffer.putCommand(":ROOM TYPE: " + Command.encode(roomid) + ";");
	}

}
