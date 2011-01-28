package client;

import java.util.LinkedList;

/**
 * Represents of the users and holds all of the information about that user.
 */
public class User {

	private String email;
	private String nickname = "";
	private String status = "OFFLINE";
	private String personalMessage = "This is a personal message.";
	private String displayPic = "";
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
	public void setNickname(String nickname) {
		this.nickname = nickname;
		
		for (UserChangedListener l : listeners) {
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
		return status;
	}

	/**
	 * Set the status of the user
	 * 
	 * @param status
	 *            The status of the user
	 */
	public void setStatus(String status) {
		this.status = status;

		for (UserChangedListener l : listeners) {
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
	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;

		for (UserChangedListener l : listeners) {
			l.personalMessageChanged();
			l.changed();
		}
	}

	/**
	 * Get the display picture of the user
	 * 
	 * @return The display picture of the user as a base 64 encoded string
	 */
	public String getDisplayPic() {
		return displayPic;
	}

	/**
	 * Set the display picture of the user
	 * 
	 * @param displayPic
	 *            The display picture of the user as a base 64 encoded string
	 */
	public void setDisplayPic(String displayPic) {
		this.displayPic = displayPic;

		for (UserChangedListener l : listeners) {
			l.DisplayPicChnaged();
			l.changed();
		}
	}

	/**
	 * Add a listener to the user
	 * 
	 * @param listner
	 *            The UserChangeListner to add
	 */
	public void addUserChangedListener(UserChangedListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Remove a listener from the user
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	public void removeUserChangedListener(UserChangedListener listener) {
		this.listeners.remove(listener);
	}

}
