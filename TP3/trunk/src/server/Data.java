package server;

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

	private int clientID = 0;
	private int roomID = 0;

	private HashMap<String, User> users = new HashMap<String, User>();
	private HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();

	/**
	 * Do nothing.
	 */
	protected Data() {
	}

	/**
	 * Apparently doing it this way makes it thread-safe...
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

	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	public User getUser(String id) {
		return users.get(id);
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

	public void addRoom(Room room) {
		rooms.put(0, room);
	}

	public Room getRoom(Integer id) {
		return rooms.get(id);
	}

	public HashMap<Integer, Room> getRooms() {
		return this.rooms;
	}

	/**
	 * Get the next client ID
	 * 
	 * @return
	 */
	public int getNextClientID() {
		return this.clientID++;
	}

	/**
	 * Get the next unique room ID
	 * 
	 * @return
	 */
	public int getNextRoomID() {
		return this.roomID++;
	}

}
