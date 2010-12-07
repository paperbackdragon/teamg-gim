package server.util;

public class UniqueID {

	private static UniqueID instance = null;
	private int clientID = 0;

	protected UniqueID()
	{
		// Exists only to defeat instantiation.
	}

	public static UniqueID getInstance() {
		if (instance == null) {
			instance = new UniqueID();
		}
		return instance;
	}
	
	/**
	 * I assume that this will never get to a point where it'll overflow...
	 * @return
	 */
	public int getNextClientID() {
		return this.clientID++;
	}

}
