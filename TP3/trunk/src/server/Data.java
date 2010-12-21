package server;

import java.util.Collection;
import java.util.HashMap;

/**
 * A singleton class to hold references to all of the data on the server. This
 * can be called anywhere and allows any part of the server to access the data
 * in it at any time.
 * 
 * Apparently doing this as a singleton is a bad idea. I disagree. The reason
 * against this seems to be that it makes it more difficult to debug, you can't
 * tell which classes changed the data because anything can access it.
 * 
 * Oh really, what about doing a search for Data.getInstance()?
 */
public class Data {

	volatile private int clientID = 0;
	volatile private int roomID = 0;

	private HashMap<String, User> users = new HashMap<String, User>();
	private HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
	private HashMap<Integer, Worker> workers = new HashMap<Integer, Worker>();

	/**
	 * Do nothing.
	 */
	protected Data() {
	}

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

	/**
	 * Add a new user to the server
	 * 
	 * @param user
	 *            The User to add
	 */
	public void addUser(User user) {
		synchronized (users) {
			users.put(user.getId(), user);
		}
	}

	/**
	 * Get a user
	 * 
	 * @param id
	 *            The id of the user
	 * @return the User
	 */
	public User getUser(String id) {
		return users.get(id.toLowerCase());
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
		synchronized (users) {
			workers.put(new Integer(id), worker);
		}
	}

	/**
	 * Get a user
	 * 
	 * @param id
	 *            The id of the user
	 * @return the User
	 */
	public User getWorker(int id) {
		return users.get(new Integer(id));
	}

	/**
	 * Get all of the users
	 * 
	 * @return A HashMap of all the users
	 */
	public HashMap<String, User> getUsers() {
		return users;
	}

	/**
	 * Add a new room
	 * 
	 * @param room
	 *            the room to add
	 */
	public void addRoom(Room room) {
		synchronized (rooms) {
			rooms.put(room.getId(), room);
		}
	}

	/**
	 * Get a room by its ID
	 * 
	 * @param id
	 *            The ID of the room
	 * @return The room or null if the room does not exist
	 */
	public Room getRoom(int id) {
		return rooms.get(new Integer(id));
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

}
