package server;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

import util.Command;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Status {
		OFFLINE, ONLINE, BUSY, AWAY
	}

	/**
	 * Taken from:
	 * http://stackoverflow.com/questions/153716/verify-email-in-java I hope it
	 * works correctly...
	 */
	private static final Pattern rfc2822 = Pattern
			.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

	/**
	 * Check if the ID is a valid one
	 * 
	 * @param id
	 * @return True if the user id is valid, false otherwise
	 */
	public static boolean validID(String id) {
		return rfc2822.matcher(id).matches();
	}

	private String id;
	private String passwordHash;
	private Status status = Status.OFFLINE;
	private String nickname;
	private String personalMessage;

	// It's Horrendous...but it works. I'd like to fix this if I ever get time.
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
		this.friendRequests.remove(user.getId());

		Worker w = this.getWorker();
		if (w != null)
			w.putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));

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
		Worker w = this.getWorker();
		if (w != null)
			w.putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));
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
			if (user.isOnline() && !blockedUsers.contains(user)) {
				users.add(user);
			}
		}

		for (Room room : this.getRooms()) {
			for (User user : room.getUsers()) {
				if (!blockedUsers.contains(user))
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
