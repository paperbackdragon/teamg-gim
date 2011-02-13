package uk.ac.glasgow.minder.recipient;

/**
 * Specifies the properties of user recipients.
 * @author tws
 *
 */
public interface User extends Recipient{

	/**
	 * Tests the candidate password against this user's password. The user is
	 * authenticated if the candidate matches the user's password.
	 * 
	 * @param candidate
	 *            the password string to test.
	 * @return true if the candidate matches the user's password, else false.
	 */
	public boolean authenticate(String candidate);

	/**
	 * Display names may not be unique amongst users in a common store.
	 * 
	 * @return a pretty-printable name for the user.
	 */
	public String getDisplayName();

	/**
	 * 
	 * @return the user privilege level.
	 */
	public Privilege getPrivilege();
}