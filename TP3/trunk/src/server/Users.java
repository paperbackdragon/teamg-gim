package server;

import java.util.HashMap;

public class Users {

	private HashMap<String, User> users;

	public Users() {
		users = new HashMap<String, User>(150);
	}
	
	public synchronized boolean addUser(User user) {
		users.put(user.getId(), user);
		
		return true;
	}

}
