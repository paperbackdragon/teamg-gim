package server;

import java.net.ServerSocket;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

// TODO: Reuse IDs for new rooms and workers

public class Data {

	/**
	 * Apparently doing it this way makes constructing it thread-safe...
	 */
	private static class SingeltonHolder {
		public static final Data INSTANCE = new Data();
	}

	/**
	 * Return the current instance of the data class.
	 * 
	 * @return the current instance
	 */
	public static Data getInstance() {
		return SingeltonHolder.INSTANCE;
	}
	
	public volatile int usersOnline = 0;
	public volatile int online = 0;
	public String uptime = "";
	public ServerSocket serverSocket = null;

	private volatile int clientID = 0;
	private volatile int roomID = 0;
	
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<Integer, Room> rooms = new ConcurrentHashMap<Integer, Room>();
	private ConcurrentHashMap<Integer, Worker> workers = new ConcurrentHashMap<Integer, Worker>();

	/**
	 * Do nothing.
	 */
	protected Data() {
	}

	/**
	 * Add a new room
	 * 
	 * @param room
	 *            the room to add
	 */
	public void addRoom(Room room) {
		this.rooms.put(room.getId(), room);
	}

	/**
	 * Add a new user to the server
	 * 
	 * @param user
	 *            The User to add
	 */
	public void addUser(User user) {
		this.users.put(user.getId(), user);
	}

	/**
	 * Add a new worker
	 * 
	 * @param id
	 *            The id of the worker
	 * @param worker
	 *            The worker itself
	 */
	public void addWorker(int id, Worker worker) {
		this.workers.put(new Integer(id), worker);
	}

	/**
	 * Get the next client ID
	 * 
	 * @return The next free Id for this client to use
	 */
	public int getNextClientID() {
		return this.clientID++;
	}

	/**
	 * Get the next unique room ID
	 * 
	 * @return The new free id for this room to use
	 */
	public int getNextRoomID() {
		return this.roomID++;
	}

	/**
	 * Get a room by its ID
	 * 
	 * @param id
	 *            The ID of the room
	 * @return The room or null if the room does not exist
	 */
	public Room getRoom(int id) {
		return this.rooms.get(new Integer(id));
	}

	/**
	 * Get a list of all the rooms
	 * 
	 * @return A Collection of all the rooms
	 */
	public Collection<Room> getRooms() {
		return this.rooms.values();
	}

	/**
	 * Get a user
	 * 
	 * @param id
	 *            The id of the user
	 * @return the User
	 */
	public User getUser(String id) {
		return this.users.get(id.toLowerCase());
	}

	/**
	 * Get all of the users
	 * 
	 * @return A HashMap of all the users
	 */
	public Collection<User> getUsers() {
		return this.users.values();
	}

	/**
	 * Get a user
	 * 
	 * @param id
	 *            The id of the user
	 * @return the User
	 */
	public User getWorker(int id) {
		return this.users.get(new Integer(id));
	}

	public Collection<Worker> getWorkers() {
		return this.workers.values();
	}

	/**
	 * Remove a room for the server
	 * 
	 * @param id
	 *            The id of the room to remove
	 */
	public void removeRoom(int id) {
		this.rooms.remove(new Integer(id));
	}

	public void removeWorker(Worker worker) {
		this.workers.remove(new Integer(worker.getID()));
	}

}
