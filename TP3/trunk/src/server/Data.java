package server;

/**
 * A singleton class to hold references to all of the data on the server. This
 * can be called anywhere and allows any part of the server to access the data
 * in it at any time.
 */
public class Data {
	
	private int clientID = 0;

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

	/**
	 * I assume that this will never get to a point where it'll overflow...
	 * 
	 * @return
	 */
	public int getNextClientID() {
		return this.clientID++;
	}

}
