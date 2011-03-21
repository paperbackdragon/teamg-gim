package client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import client.net.ClientConnection;

public class Model {

	private ClientConnection outLink;
	private boolean initilised = false;

	private LinkedList<LinkedList<User>> newRoomList = new LinkedList<LinkedList<User>>();
	private LinkedList<User> invitationsList = new LinkedList<User>();
	private LinkedList<Boolean> typeList = new LinkedList<Boolean>();
	
	private Options options;

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
	 * Doing it this way makes constructing it thread-safe...
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
	
	public void reset() {
		newRoomList = new LinkedList<LinkedList<User>>();
		invitationsList = new LinkedList<User>();
		typeList = new LinkedList<Boolean>();
		friendlist = new FriendList();
		users = new ConcurrentHashMap<String, User>();
		loggedInUser = null;
		latestperson = null;
	}

	public void setInitilised(boolean i) {
		this.initilised = i;
		synchronized (this) {
			notifyAll();
		}
	}

	public boolean isInitilised() {
		return this.initilised;
	}

	/**
	 * Wait for the FriendList to be initilised
	 */
	public void waitForInitilisation() {
		while (!isInitilised()) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
			}
		}
	}

	public synchronized void addInvitation(User from) {
		invitationsList.add(from);
	}

	public synchronized void addNextRoom(LinkedList<User> userlist) {
		newRoomList.add(userlist);
	}

	/**
	 * Add a user to the list of global users
	 * 
	 * @param user
	 *            The ID of the user to add
	 */
	public void addUser(User user) {
		this.users.put(user.getEmail().toLowerCase(), user);
	}

	public void createRoom(LinkedList<User> contacts) {
		newRoomList.add(contacts);
		typeList.add(true);
		outLink.createGroupChat();
	}

	public void createRoom(User user) {
		LinkedList<User> users = new LinkedList<User>();
		users.add(user);
		newRoomList.add(users);
		typeList.add(false);
		outLink.createSingleChat(user);
	}

	public FriendList getFriendList() {
		return this.friendlist;
	}

	public String getLatestPerson() {
		return latestperson;
	}

	public synchronized User getNextInvitation() {
		return invitationsList.pop();
	}

	public synchronized LinkedList<User> getNextRoom() {
		return newRoomList.pop();
	}

	public Boolean getNextType() {
		return typeList.pop();
	}

	public User getSelf() {
		return this.loggedInUser;
	}

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

	/**
	 * Get the connection to the server
	 * 
	 * @return The ClinetConnection connected to the server
	 */
	public ClientConnection getServer() {
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
		return this.users.get(user.toLowerCase());
	}

	public void quit() {
		this.getServer().logout();
		
		String filename = "/options.db";
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(this.getPath() + filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(this.options);
			out.close();
		} catch (IOException ex) {
		}
		
		System.exit(0);
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
	
	public Options getOptions() {
		
		if(this.options == null) {
			String filename = "/options.db";
			FileInputStream fis = null;
			ObjectInputStream in = null;
			try {
				fis = new FileInputStream(this.getPath() + filename);
				in = new ObjectInputStream(fis);
				this.options = (Options) in.readObject();
				in.close();
			} catch (IOException ex) {
				this.options = new Options();
			} catch (ClassNotFoundException ex) {
			}
		}
		
		return this.options;
	}

}