package server;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import util.Command;

public class Room {

	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<String, User> invited = new ConcurrentHashMap<String, User>();
	private boolean isGroup = true;
	private int id;

	public Room() {
		this.id = Data.getInstance().getNextRoomID();
	}

	public Room(User creator, boolean isGroup) {

		this.isGroup = isGroup;

		if (creator != null) {
			this.addInvitiedUser(creator);
			this.join(creator);
		}

		System.out.println("Creating new room with id " + this.id);
	}

	public void addInvitiedUser(User user) {
		this.invited.put(user.getId(), user);
	}

	public String getType() {
		if (isGroup)
			return "GROUP";
		else
			return "PERSONAL";
	}

	public boolean isGroup() {
		return this.isGroup;
	}

	public int getId() {
		return this.id;
	}

	public Collection<User> getUsers() {
		return this.users.values();
	}

	public Collection<User> getInvitiedUsers() {
		return this.invited.values();
	}

	public boolean inRoom(User user) {
		return this.users.containsKey(user.getId());
	}

	public boolean join(User joined) {
		// Make sure they've been invited
		if (!this.invited.containsKey(joined.getId()))
			return false;
		else
			this.invited.remove(joined.getId());

		// Notify other users that they're joining
		Command j = new Command("ROOM", "JOINED", this.getId() + " " + Command.encode(joined.getId()));
		for (User user : getUsers()) {
			Worker w = user.getWorker();
			w.putResponse(j);
		}

		this.users.put(joined.getId(), joined);
		joined.addRoom(this);

		return true;
	}

	public boolean messageAll(User sender, String message) {
		// Whoever tried to sent it wasn't in the room
		if (!inRoom(sender))
			return false;

		// Send it to everyone except the user who sent it
		for (User user : users.values()) {
			if (user != sender)
				user.sendMessage(sender, this.getId(), message);
		}

		return true;
	}

	public boolean leave(User left) {
		if (!inRoom(left))
			return false;

		this.users.remove(left.getId());
		left.removeRoom(this);

		// TODO: Refactor out to another method so that the messages go where
		// they need to go.
		// Destroy the room if it's empty and unjoinable
		// TODO: Fix after testing
		if (this.getUsers().size() == 0 && this.getInvitiedUsers().size() == 0 && this.getId() != 0) {
			Data.getInstance().removeRoom(this.getId());
			return true;
		}

		// Notify other users that they've left
		Command l = new Command("ROOM", "LEFT", this.getId() + " " + Command.encode(left.getId()));
		for (User user : users.values()) 
			user.getWorker().putResponse(l);

		return true;
	}

	public synchronized boolean invite(User user, User by) {
		this.invited.put(user.getId(), user);
		user.getWorker().putResponse(new Command("ROOM", "INVITED", this.id + " " + Command.encode(by.getId())));
		return true;
	}

}
