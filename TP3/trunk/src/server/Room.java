package server;

import java.util.HashMap;

import util.Command;

// TODO: If the last user leaves and no one is queued to join the delete the room

public class Room {

	private HashMap<String, User> users;
	private HashMap<String, User> invited;
	private boolean isGroup;
	private int id;

	public Room() {
		this.users = new HashMap<String, User>();
		this.invited = new HashMap<String, User>();
		this.id = Data.getInstance().getNextRoomID();
	}

	public Room(User creator, boolean isGroup) {
		this();

		this.isGroup = isGroup;
		this.join(creator);

		System.out.println("Creating new room with id " + this.id);
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
		return id;
	}

	public HashMap<String, User> getUsers() {
		return this.users;
	}

	public HashMap<String, User> getInvitiedUsers() {
		return this.invited;
	}

	public boolean inRoom(User user) {
		return this.users.containsKey(user.getId());
	}

	public synchronized boolean join(User joined) {
		// Make sure they've been invited
		if (!this.invited.containsKey(joined.getId()))
			return false;
		else
			this.invited.remove(joined.getId());

		this.users.put(joined.getId(), joined);

		// Notify other users that they're joining
		Command j = new Command("ROOM", "JOINED", this.getId() + " " + Command.encode(joined.getId()));
		for (User user : users.values()) {
			user.getWorker().putResponse(j);
		}

		return true;
	}

	public boolean messageAll(User sender, String message) {
		if (!inRoom(sender))
			return false;

		// Send it to everyone except the user who went it
		for (User user : users.values()) {
			if (user != sender)
				user.sendMessage(sender, this.getId(), message);
		}

		return true;

	}

	public synchronized boolean leave(User left) {
		if (!inRoom(left))
			return false;

		this.users.remove(left.getId());

		// Notify other users that they've left
		Command l = new Command("ROOM", "LEFT", this.getId() + " " + Command.encode(left.getId()));
		for (User user : users.values()) {
			user.getWorker().putResponse(l);
		}

		return true;
	}

	public synchronized boolean invite(User user, User by) {

		this.invited.put(user.getId(), user);

		// We may have to encode the id here, I'm not sure
		user.getWorker().putResponse(new Command("ROOM", "INVITIED", this.id + " " + Command.encode(by.getId())));

		return true;
	}

}
