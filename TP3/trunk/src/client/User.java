package client;

import java.awt.Image;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import util.Base64;

/**
 * Represents of the users and holds all of the information about that user.
 */
public class User {
	
	public static enum Status {
		OFFLINE, ONLINE, BUSY, AWAY
	}

	private String email;
	private String nickname = "";
	private Status status = Status.OFFLINE;
	private String personalMessage = "";
	private ImageIcon displayPic = new ImageIcon(Model.getInstance().getPath() + "default.jpg");
	private LinkedList<UserChangedListener> listeners = new LinkedList<UserChangedListener>();

	/**
	 * Constructor requiring only an email address
	 * 
	 * @param email
	 */
	public User(String email) {
		this.email = email;
		this.nickname = email;
	}

	/**
	 * Get the user's email address
	 * 
	 * @return The User's email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get the User's email address
	 * 
	 * @return The email address
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Set the user's nickname
	 * 
	 * @param nickname
	 *            The user's nickname
	 */
	public synchronized void setNickname(String nickname) {
		this.nickname = nickname;
		for (UserChangedListener l : this.listeners.toArray(new UserChangedListener[0])) {
			l.nicknameChanged();
			l.changed();
		}
	}

	/**
	 * Get the users status
	 * 
	 * @return The status of the user
	 */
	public String getStatus() {
		return status.toString().toLowerCase();
	}

	/**
	 * Set the status of the user
	 * 
	 * @param status
	 *            The status of the user
	 */
	public synchronized void setStatus(String status) {
		
		try {
			this.status = Status.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			this.status = Status.ONLINE;
		}

		// This shit is wack. Apparently if this isn't converted to an array
		// then
		// it throws ConcurrentModificationException
		for (UserChangedListener l : this.listeners.toArray(new UserChangedListener[0])) {
			l.statusChanged();
			l.changed();
		}
	}

	/**
	 * Get the personal message of the user
	 * 
	 * @return The users personal message
	 */
	public String getPersonalMessage() {
		return personalMessage;
	}

	/**
	 * Set the personal message of the user
	 * 
	 * @param personalMessage
	 *            The personal message of the user
	 */
	public synchronized void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;

		for (UserChangedListener l : this.listeners.toArray(new UserChangedListener[0])) {
			l.personalMessageChanged();
			l.changed();
		}
	}

	/**
	 * Get the display picture of the user
	 * 
	 * @return The display picture of the user as a base 64 encoded string
	 */
	public ImageIcon getDisplayPic() {
		return displayPic;
	}

	/**
	 * Get the display pic at a certain size
	 * 
	 * @param width
	 *            The width of the image
	 * @param height
	 *            The height of the image
	 * @return The new ImageIcon at the specified size
	 */
	public ImageIcon getDisplayPic(int width, int height) {
		return new ImageIcon(this.getDisplayPic().getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}

	/**
	 * Set the display picture of the user c
	 * 
	 * @param displayPic
	 *            The display picture of the user as a base 64 encoded string
	 */
	public synchronized void setDisplayPic(String displayPic) {

		this.displayPic = new ImageIcon(Base64.decode(displayPic));
		
		if(this.displayPic.getIconWidth() == 0) 
			this.displayPic = new ImageIcon(Model.getInstance().getPath() + "default.jpg");

		for (UserChangedListener l : this.listeners.toArray(new UserChangedListener[0])) {
			l.displayPicChanged();
			l.changed();
		}

	}

	/**
	 * Add a listener to the user
	 * 
	 * @param listner
	 *            The UserChangeListner to add
	 */
	public synchronized void addUserChangedListener(UserChangedListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Remove a listener from the user
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	public synchronized void removeUserChangedListener(UserChangedListener listener) {
		this.listeners.remove(listener);
	}

}
