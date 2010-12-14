package server;

import java.util.HashMap;

public class Users {

	public HashMap<String, User> users;

	public Users() {
		users = new HashMap<String, User>(150);
	}

	public synchronized boolean add(User user) {
		if(users.containsKey(user.getId()))
			return false;
		
		users.put(user.getId(), user);
		return true;
	}
	
	public synchronized User remove(User user) {
		return users.remove(user.getId());
	}

}
