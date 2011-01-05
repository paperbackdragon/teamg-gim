package server;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import util.Command;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Status {
		OFFLINE, ONLINE, BUSY, AWAY
	}

	/**
	 * Check if the ID is a valid one
	 * 
	 * @param id
	 * @return True if the user id is valid, false otherwise
	 * 
	 *         TODO: Make this work.
	 */
	public static boolean validID(String id) {
		return true;
	}

	private String id;
	private String passwordHash;
	private Status status = Status.OFFLINE;
	private String nickname;
	private String personalMessage;

	private String displayPic = "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";
	private ConcurrentHashMap<String, User> friendList = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<String, User> inFreindList = new ConcurrentHashMap<String, User>();

	private ConcurrentHashMap<String, User> friendRequests = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<String, User> blockedUsers = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<Integer, Room> rooms = new ConcurrentHashMap<Integer, Room>();

	private ConcurrentLinkedQueue<Command> queue = new ConcurrentLinkedQueue<Command>();
	private volatile boolean online = false;

	private transient Worker worker = null;

	/**
	 * Constructor with the minimum possible details
	 * 
	 * @param id
	 *            The user ID of the user
	 * @param passwordHash
	 *            The password hash of the user
	 */
	public User(String id, String passwordHash) {
		this.id = id.toLowerCase();
		this.passwordHash = passwordHash;
		this.status = Status.OFFLINE;
		this.nickname = this.id;
		this.personalMessage = "";
	}

	/**
	 * Constructor with all of the details
	 * 
	 * @param id
	 *            The user ID of the user
	 * @param passwordHash
	 *            The password hash of the user
	 * @param status
	 *            The Status of the user as an enum from User.status
	 * @param nickname
	 *            The nickname of the user
	 * @param personalMessage
	 *            The personal message of the user
	 * @param friendList
	 *            The friend list of the user
	 * @param inFreindList
	 *            A list of users who have this user in their friend list
	 * @param blockedUsers
	 *            A list of users this user has blocked
	 */
	public User(String id, String passwordHash, Status status, String nickname, String personalMessage,
			ConcurrentHashMap<String, User> friendList, ConcurrentHashMap<String, User> inFreindList,
			ConcurrentHashMap<String, User> blockedUsers) {

		this.id = id.toLowerCase();
		this.passwordHash = passwordHash;
		this.status = status;
		this.nickname = nickname;
		this.personalMessage = personalMessage;
		this.friendList = friendList;
		this.inFreindList = inFreindList;
		this.blockedUsers = blockedUsers;

	}

	/**
	 * Put the the user into the list of people this user has sent friend
	 * request to
	 * 
	 * @param user
	 *            The user to add
	 */
	public void addFreindRequest(User user) {
		this.friendRequests.put(user.getId(), user);
	}

	/**
	 * Add a user to this user's friend list and place this user into the list
	 * of people who have that user in their friend list.
	 * 
	 * @param user
	 *            The user to add as a friend.
	 */
	public void addFriend(User user) {
		this.friendList.put(user.getId(), user);
		this.getWorker().putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));
		user.addToInFriendList(this);
	}

	/**
	 * Add a room to the list of rooms that this user is currently in
	 * 
	 * @param room
	 *            the room to add
	 */
	public void addRoom(Room room) {
		this.rooms.put(room.getId(), room);
	}

	/**
	 * Add the user to this users list of people who have them as a friend
	 * 
	 * @param user
	 *            The user to add to the list
	 */
	public void addToInFriendList(User user) {
		this.inFreindList.put(user.getId(), user);
	}

	/**
	 * Add a user to this users list of blocked users
	 * 
	 * @param user
	 *            the user to block
	 */
	public void block(User user) {
		this.blockedUsers.put(user.getId(), user);
		this.getWorker().putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));
	}

	/**
	 * Get a list of all the blocked users
	 * 
	 * @return A Collection of the blocked users
	 */
	public Collection<User> getBlockedUsers() {
		return this.blockedUsers.values();
	}

	/**
	 * Get the display picture as a BASE64 encoded string
	 * 
	 * @return The display picture as a BASE64 encoded string
	 */
	public String getDisplayPic() {
		return this.displayPic;
	}

	/**
	 * Get a list of all the users this user is friends with
	 * 
	 * @return A Collection of the users this user is friends with
	 */
	public Collection<User> getFriendList() {
		return this.friendList.values();
	}

	/**
	 * Get it ID of the user
	 * 
	 * @return The ID of the user
	 */
	public String getId() {
		return this.id;
	}

	public Collection<User> getInFreindList() {
		return this.inFreindList.values();
	}

	/**
	 * Get the users nickname
	 * 
	 * @return Their nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Get the users password hash
	 * 
	 * @return The password hash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Get the users personal message
	 * 
	 * @return The personal message
	 */
	public String getPersonalMessage() {
		return personalMessage;
	}

	/**
	 * YOU MUST FOR ALL THINGS IN THIS WORLD SYNCHRONIZE ANY ADDITIONS TO THIS
	 * 
	 * @return The list of queued commands to the user
	 */
	public ConcurrentLinkedQueue<Command> getQueue() {
		return this.queue;
	}

	/**
	 * Get a list of the rooms this user is in
	 * 
	 * @return A collection of the rooms
	 */
	public Collection<Room> getRooms() {
		return this.rooms.values();
	}

	/**
	 * Get the users status
	 * 
	 * @return The status of the user
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Get a list of the users who have this user in their friend list and who
	 * are currently online
	 * 
	 * @return A hashmap of the users who are online or who this user is
	 *         currently interacting with
	 * 
	 */
	private Collection<User> getUsersToNotify() {
		Collection<User> users = new LinkedList<User>();

		for (User user : this.getInFreindList()) {
			if (user.isOnline()) {
				users.add(user);
			}
		}

		for (Room room : this.getRooms()) {
			for (User user : room.getUsers()) {
				users.add(user);
			}
		}

		return users;
	}

	/**
	 * Get the current worker for this user
	 * 
	 * @return A worker if the user is logged in, null otherwise.
	 */
	public Worker getWorker() {
		return this.worker;
	}

	/**
	 * Check if a user is in a room
	 * 
	 * @param roomID
	 *            The room ID to check against
	 * @return True if the user is in the room, false otherwise
	 */
	public boolean inRoom(int roomID) {
		return this.rooms.containsKey(new Integer(roomID));
	}

	/**
	 * Check if the user is in a room with another user
	 * 
	 * @param user
	 *            The user to look for
	 * @return True if they are in the same room as the user, false otherwise
	 */
	public boolean inRoomWith(User user) {
		for (Room room : this.rooms.values()) {
			if (room.inRoom(user))
				return true;
		}
		return false;
	}

	/**
	 * Check if the current user is friends with another user
	 * 
	 * @param user
	 *            The user to check against
	 * @return True if the user has the given user in their friend list, false
	 *         otherwise
	 */
	public boolean isFriendsWith(User user) {
		return this.friendList.containsKey(user.getId());
	}

	/**
	 * Check if the user is online (This is their action online state, not just
	 * their status. Appear offline counts as online)
	 * 
	 * @return True if they are online, false otherwise
	 */
	public boolean isOnline() {
		return this.online;
	}

	public void login(Worker w) {
		this.setOnline(true);
		this.setWorker(w);
	}

	/**
	 * Leave any rooms that the user is currently in and logout.
	 */
	public void logout() {
		for (Room room : this.getRooms()) {
			room.leave(this);
		}

		setOnline(false);
	}

	/**
	 * Set the user to not be in the other users friend list
	 * 
	 * @param user
	 *            The user to remove
	 */
	public void notInFriendList(User user) {
		this.inFreindList.remove(user.getId());
	}

	/**
	 * From a user from this users friend list
	 * 
	 * @param user
	 *            The user to remove
	 */
	public void removeFriend(User user) {
		this.friendList.remove(user.getId());
		this.getWorker().putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));
	}

	/**
	 * Remove a user from the list of pending requests
	 * 
	 * @param user
	 *            The user to remove
	 */
	public void removeFriendRequest(User user) {
		this.friendRequests.remove(user.getId());
	}

	/**
	 * Remove a room from the list of rooms this user is in
	 * 
	 * @param room
	 *            The room to remove
	 */
	public void removeRoom(Room room) {
		this.rooms.remove(room.getId());
	}

	/**
	 * Send a friend request to this user
	 * 
	 * @param from
	 *            The person the request was from
	 */
	public void sendFriendRequest(User from) {
		Command cmd = new Command("FRIENDREQUEST", null, Command.encode(from.getId()) + " "
				+ Command.encode(from.getNickname()));

		from.addFreindRequest(this);

		// Queue the message if the user is offline
		if (this.worker == null) {
			this.queue.add(cmd);
		} else {
			worker.putResponse(cmd);
		}
	}

	/**
	 * Send a message to this user
	 * 
	 * @param from
	 *            Who the message is from
	 * @param roomID
	 *            The room Id this message belongs to
	 * @param message
	 *            The message itself
	 */
	public void sendMessage(User from, int roomID, String message) {
		if (!this.blockedUsers.containsKey(from.getId()) && this.worker != null) {
			this.worker.putResponse(new Command("MESSAGE", null, roomID + " " + Command.encode(from.getId()) + " "
					+ Command.encode(message)));
		}
	}

	/**
	 * Send a command to all users who have this user in their friend list and
	 * who are online
	 * 
	 * @param cmd
	 *            The command to send
	 */
	public void sendToAll(Command cmd) {
		for (User user : getUsersToNotify()) {
			Worker w = user.getWorker();
			if (w != null)
				w.putResponse(cmd);
			else
				user.logout();
		}
	}

	/**
	 * Check if this user sent a friend request to another
	 * 
	 * @param user
	 *            The user to check for
	 * @return True if they did, false otherwise
	 */
	public boolean sentFriendRequestTo(User user) {
		return this.friendRequests.containsKey(user.getId());
	}

	/**
	 * Set the display picture for this user
	 * 
	 * @param displayPic
	 *            The new display picture BASE64 encoded
	 */
	public void setDisplayPic(String displayPic) {
		this.displayPic = displayPic;
		this.sendToAll(new Command("UPDATE", "DISPLAY_PIC", Command.encode(this.getId())));
	}

	/**
	 * Set the Id of the user
	 * 
	 * @param id
	 *            The new ID of the user
	 */
	public void setId(String id) {
		this.id = id.toLowerCase();
	}

	/**
	 * Set the users nickname
	 * 
	 * @param nickname
	 *            the new nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;

		this.sendToAll(new Command("UPDATE", "NICKNAME", Command.encode(this.getId())));
	}

	/**
	 * Set the users online status
	 * 
	 * @param online
	 *            True if the user is online, false otherwise
	 */
	public void setOnline(boolean online) {
		this.online = online;
		if (!online)
			this.setStatus("OFFLINE");
	}

	/**
	 * Set a new password for the user
	 * 
	 * @param passwordHash
	 *            The hash of the password
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;

	}

	/**
	 * Set the personal message for this user
	 * 
	 * @param personalMessage
	 *            The new personal message
	 */
	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;

		this.sendToAll(new Command("UPDATE", "PERSONAL_MESSAGE", Command.encode(this.getId())));
	}

	/**
	 * Set the status for this user
	 * 
	 * @param status
	 *            The new status
	 */
	public void setStatus(Status status) {
		this.status = status;

		this.sendToAll(new Command("UPDATE", "STATUS", Command.encode(this.getId())));
	}

	/**
	 * Set the status of this user
	 * 
	 * @param status
	 *            The new status
	 * @return True if it was successfully set, false otherwise
	 */
	public boolean setStatus(String status) {

		try {
			this.status = Status.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			return false;
		}

		this.sendToAll(new Command("UPDATE", "STATUS", Command.encode(this.getId())));

		return true;
	}

	/**
	 * Set the Worker for this user
	 * 
	 * @param worker
	 *            The worker currently handling this user
	 */
	public synchronized void setWorker(Worker worker) {
		this.worker = worker;
	}

	/**
	 * Remove a user from this users blocked list
	 * 
	 * @param user
	 *            The user to remove
	 */
	public void unblock(User user) {
		this.blockedUsers.remove(user.getId());
		this.getWorker().putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));
	}

}
