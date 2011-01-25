package client;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores a list of users and can map them to online and off-line categories
 * 
 * @author James McMinn
 * 
 */
public class FriendList {

	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
	private LinkedList<User> blocked = new LinkedList<User>();
	private boolean initilised = false;

	/**
	 * Default constructor
	 */
	public FriendList() {
	}

	public synchronized void setInitilised(boolean i) {
		this.initilised = i;
		notifyAll();
	}

	public boolean isInitilised() {
		return this.initilised;
	}

	/**
	 * Get all of the users in the list
	 * 
	 * @return
	 */
	public Collection<User> getFriendList() {
		return users.values();
	}

	/**
	 * Get a user from the list
	 * 
	 * @param userID
	 *            The userID of the User
	 * @return The User or null if the user is not in the list
	 */
	public User getUser(String userId) {
		return users.get(userId);
	}

	/**
	 * Add a user to the friends
	 * 
	 * @param user
	 *            The user to add
	 */
	public void addUser(User user) {
		users.put(user.getEmail(), user);
	}

	/**
	 * Remove a user from the users friend list
	 * 
	 * @param user
	 *            the user to remove
	 */
	public void removeUser(User user) {
		users.remove(user.getEmail());
	}

	/**
	 * Get a list of the users who are currently online
	 * 
	 * @return A collection of users who are currently online
	 */
	public Collection<User> getOnlineUsers() {
		LinkedList<User> online = new LinkedList<User>();

		for (User u : this.users.values()) {
			if (u.getStatus().equalsIgnoreCase("OFFLINE") == false)
				online.add(u);
		}

		return online;
	}

	/**
	 * Get a list of the users who are currently off-line
	 * 
	 * @return A collection of users who are currently off-line
	 */
	public Collection<User> getOfflineUsers() {
		LinkedList<User> online = new LinkedList<User>();

		for (User u : this.users.values()) {
			if (u.getStatus().equalsIgnoreCase("OFFLINE"))
				online.add(u);
		}

		return online;
	}

	/**
	 * Get a list of the users which this user has blocked
	 * 
	 * @return A list of users which the user has blocked
	 */
	public Collection<User> getBlockedUsers() {
		return this.blocked;
	}

	/**
	 * Add a user to the list of blocked users
	 * 
	 * @param user
	 *            The user to add to the list
	 */
	public void addBlockedUser(User user) {
		this.blocked.add(user);
	}

	/**
	 * Check if the user is blocked
	 * 
	 * @param id
	 *            The id of the user
	 * @return True if the use is blocked, false otherwise.
	 */
	public boolean isBlocked(String id) {
		return this.blocked.contains(id);
	}

	/**
	 * Check if the friend list contains a user
	 * 
	 * @param user
	 *            The user to check for
	 * @return True if the list contains the user, false otherwise
	 */
	public boolean containts(User user) {
		return this.users.containsKey(user.getEmail());
	}

	public synchronized void waitForInitilisation() {
		while(!isInitilised()) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

}
