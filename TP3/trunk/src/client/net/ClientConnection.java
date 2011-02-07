package client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

import client.Model;
import client.User;

import util.Command;
import util.Buffer;

public class ClientConnection implements NetworkingOut, Runnable {

	private Socket serverConnection;
	private BufferedReader bufferedreader;
	private PrintWriter printwriter;

	private NetworkReader reader;
	private NetworkWriter writer;
	private Buffer<Command> buffer;

	private Thread readerthread;
	private Thread writerthread;

	private ServerConnection gui;
	private Thread thread;

	private Model model = Model.getInstance();

	boolean connected = false;

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
		gui.setServer(this);
		connect();
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	private void readandwrite() {
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
			printwriter = new PrintWriter(serverConnection.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("aye... what the hell do we do if this happens :|.");

			bufferedreader = null;
			printwriter = null;
		}

		// get the reader and writer, and buffer on the go... ;x
		this.buffer = new Buffer<Command>();
		this.reader = new NetworkReader(bufferedreader, gui);
		this.writer = new NetworkWriter(printwriter, buffer);

		this.readerthread = new Thread(reader);
		this.writerthread = new Thread(writer);

		readerthread.start();
		writerthread.start();

		this.thread = new Thread(this);
		thread.start();

	}

	public Boolean connect() {
		try {
			SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			serverConnection = (SSLSocket) sslFactory.createSocket("rooster.dyndns.info", 4444);

			// Pick all AES algorithms of 128 bits key size
			String patternString = "AES.*128";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher;
			boolean matchFound;

			String supportedSuites[] = ((SSLSocket) serverConnection).getSupportedCipherSuites();
			String suitePickOrder[] = new String[supportedSuites.length];

			int j = 0, k = supportedSuites.length - 1;
			for (int i = 0; i < supportedSuites.length; i++) {
				// Determine if pattern exists in input
				matcher = pattern.matcher(supportedSuites[i]);
				matchFound = matcher.find();
				if (matchFound)
					suitePickOrder[j++] = supportedSuites[i];
				else
					suitePickOrder[k--] = supportedSuites[i];
			}

			((SSLSocket) serverConnection).setEnabledCipherSuites(suitePickOrder);

			readandwrite();
			
			this.connected = true;
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to server");
			// tell the GUI
		} catch (IOException e) {
			serverConnection = null;
			this.connected = false;
			return false;
		}
		return true;
	}

	/**
	 * Run the thread.
	 */
	public void run() {
		// heart beat
		// NO IDEA IF THIS IS SENSIBLE, OR EVEN THE WAY TO DO IT

		while (connected) {
			try {
				Thread.currentThread();
				Thread.sleep(12000);
				ping();
			} catch (InterruptedException e) {
				break;
			}
		}

	}

	@Override
	public void ping() {
		buffer.putCommand(new Command("PING"));
	}

	public void authenticate(String emailaddress, char[] password) {
		// :AUTH { LOGIN | REGISTER }: <email address> <password>;
		if (connected == false && connect() != true) {
			JOptionPane.showMessageDialog(null, "Server is down.");
			return;
		}

		buffer.putCommand(new Command("AUTH", "LOGIN", Command.encode(emailaddress) + " "
				+ Command.encode(new String(password))));

		User self = new User(emailaddress);
		this.model.addUser(self);
		this.model.setSelf(self);

	}

	@Override
	public void quit() {
		// :QUIT:;
		buffer.putCommand(new Command("QUIT"));
	}

	@Override
	public void register(String emailaddress, char[] password) {
		// :AUTH { LOGIN | REGISTER }: <email address> <password>;
		buffer.putCommand(new Command("AUTH", "REGISTER", Command.encode(emailaddress) + " "
				+ Command.encode(new String(password))));
	}

	@Override
	public void friendlist() {
		// :FRIENDLIST:;
		buffer.putCommand(new Command("FRIENDLIST"));
	}

	@Override
	public void message(String roomid, String message) {
		// :MESSAGE: <roomid> <message>;
		buffer.putCommand(new Command("MESSAGE", null, Command.encode(roomid) + " " + Command.encode(message)));
	}

	@Override
	public void logout() {
		// :LOGOUT:;
		buffer.putCommand(new Command("LOGOUT"));
	}

	@Override
	public void serverTime() {
		// :SERVERSTATUS { USERS | TIME | UPTIME }:;
		buffer.putCommand(new Command("SERVERSTATUS", "TIME"));
	}

	@Override
	public void serverUptime() {
		// :SERVERSTATUS { USERS | TIME | UPTIME }:;
		buffer.putCommand(new Command("SERVERSTATUS", "UPTIME"));
	}

	@Override
	public void servernumberofusers() {
		// :SERVERSTATUS { USERS | TIME | UPTIME }:;
		buffer.putCommand(new Command("SERVERSTATUS", "USERS"));
	}

	@Override
	public void serverstatus() {
		// :SERVERSTATUS { USERS | TIME | UPTIME }:;
		buffer.putCommand(new Command("SERVERSTATUS"));
	}

	@Override
	public void setDisplayPicture(String displayPicture) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(new Command("SET", "DISPLAY_PIC", Command.encode(displayPicture)));
	}

	@Override
	public void setNickname(String nickname) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(new Command("SET", "NICKNAME", Command.encode(nickname)));
	}

	@Override
	public void setPersonalMessage(String personalmessage) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(new Command("SET", "PERSONAL_MESSAGE", Command.encode(personalmessage)));
	}

	@Override
	public void setStatus(String status) {
		// :SET [ NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC ]: <value>;
		buffer.putCommand(new Command("SET", "STATUS", Command.encode(status)));
	}

	@Override
	public void getDisplayPicture(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{ <user>};

		// Encode the names
		String userListString = "";
		for (String user : userList.split(" "))
			userListString += Command.encode(user) + " ";

		buffer.putCommand(new Command("GET", "DISPLAY_PIC", userListString));
	}

	@Override
	public void getNickname(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{ <user>};

		// Encode the names
		String userListString = "";
		for (String user : userList.split(" "))
			userListString += Command.encode(user) + " ";

		buffer.putCommand(new Command("GET", "NICKNAME", userListString));
	}

	@Override
	public void getPersonalMessage(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{ <user>};

		// Encode the names
		String userListString = "";
		for (String user : userList.split(" "))
			userListString += Command.encode(user) + " ";

		buffer.putCommand(new Command("GET", "PERSONAL_MESSAGE", userListString));
	}

	@Override
	public void getStatus(String userList) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{ <user>};

		// Encode the names
		String userListString = "";
		for (String user : userList.split(" "))
			userListString += Command.encode(user) + " ";

		buffer.putCommand(new Command("GET", "STATUS", userListString));
	}

	@Override
	public void invite(String roomid, String user) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
		// <user>};
		buffer.putCommand(new Command("ROOM", "INVITE", Command.encode(roomid) + " " + Command.encode(user)));
	}

	@Override
	public void join(String roomid) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
		// <user>};
		buffer.putCommand(new Command("ROOM", "JOIN", Command.encode(roomid)));
	}

	@Override
	public void leave(String roomid) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
		// <user>};
		buffer.putCommand(new Command("ROOM", "LEAVE", Command.encode(roomid)));
	}

	@Override
	public void roomusers(String roomid) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS ]: {<roomid> |
		// <user>};
		buffer.putCommand(new Command("ROOM", "USERS", Command.encode(roomid)));
	}

	@Override
	public void accept(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(new Command("FRIEND", "ACCEPT", Command.encode(friend)));
	}

	@Override
	public void add(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(new Command("FRIEND", "ADD", Command.encode(friend)));
	}

	@Override
	public void block(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(new Command("FRIEND", "BLOCK", Command.encode(friend)));
	}

	@Override
	public void decline(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(new Command("FRIEND", "DECLINE", Command.encode(friend)));
	}

	@Override
	public void delete(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(new Command("FRIEND", "DELETE", Command.encode(friend)));
	}

	@Override
	public void unblock(String friend) {
		// :FRIEND [ ADD | BLOCK | UNBLOCK | ACCEPT | DECLINE | DELETE ]:
		// <target>;
		buffer.putCommand(new Command("FRIEND", "UNBLOCK", Command.encode(friend)));
	}

	@Override
	public void getAttributes(ArrayList<String> attributes, ArrayList<String> users) {
		// :GET { NICKNAME| STATUS | PERSONAL_MESSAGE | DISPLAY_PIC }:
		// <user>{,<user>};

		String attributesstring = "";
		for (String attribute : attributes)
			attributesstring += attribute + " ";

		String userList = "";
		for (String user : users)
			userList += Command.encode(user) + " ";

		buffer.putCommand(new Command("GET", attributesstring, userList));
	}

	@Override
	public void createGroupChat() {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS | TYPE ]:
		// {<roomid> |
		// <user>};

		buffer.putCommand(new Command("ROOM", "CREATE GROUP"));
	}

	@Override
	public void createSingleChat(User user) {
		// :ROOM [ CREATE {GROUP} | INVITE | JOIN | LEAVE | USERS | TYPE ]:
		// {<roomid> |
		// <user>};

		buffer.putCommand(new Command("ROOM", "CREATE", Command.encode(user.getEmail())));
	}

	@Override
	public void type(String roomid) {
		buffer.putCommand(new Command("ROOM", "TYPE", Command.encode(roomid)));
	}

	public void endNetworkWriter() {
		try {
			serverConnection.close();
		} catch (IOException e) {
		}
	}

	public void updateAll() {
		String userList = "";

		for (User user : model.getFriendList().getFriendList())
			userList += user.getEmail() + " ";

		userList += model.getSelf().getEmail();

		buffer.putCommand(new Command("GET", "NICKNAME STATUS PERSONAL_MESSAGE DISPLAY_PIC", userList));
	}

}
