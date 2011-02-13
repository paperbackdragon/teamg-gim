package uk.ac.glasgow.minder.recipient;

import java.util.Set;

import javax.mail.internet.InternetAddress;

/**
 * Specifies the control interface for creating and searching reminder
 * recipients. Recipients are either atomic users or mailing list groups of
 * users.
 * 
 * @author tws
 * 
 */
public interface RecipientStore {

	/**
	 * 
	 * @param username the user to be authenticated.
	 * @param password the candidate password to be tested
	 * @return the authenticated user, if successful, otherwise null.
	 */
	public User authenticateUser(String username, String password);

	/**
	 * Creates a user in the store with the specified username, email address
	 * and privilege level.
	 * 
	 * @param displayName
	 *            a human readable name for the user
	 * @param username
	 *            a unique (for this store) character string for identifying the
	 *            user among all recipients.
	 * @param password
	 * 
	 * @param emailAddress
	 * 
	 * @param privilege
	 * @see uk.ac.glasgow.minder.recipient.Privilege
	 */
	public void addUser(
			String displayName,
			String username,
			String password,
			InternetAddress emailAddress,
			Privilege privilege);

	/**
	 * Creates a new mailing list in the store.
	 * 
	 * @param label
	 *            a unique (for this store) character string for identifying the
	 *            mailing list among all recipients.
	 * @param ownerUsername
	 *            the username of a user in the store.
	 */
	public void createMailingList(String label, String ownerUsername);

	/**
	 * Assigns a user to membership of the mailing list, if the user
	 * and mailing list exist in the store
	 * @param label - the label of the mailing list to be altered.
	 * @param username - the label of the user to be added to the mailing list 
	 */
	public void addUserToMailingList(String label, String username);

	/**
	 * Searches the recipients in the store according to the specified pattern.
	 * 
	 * @param pattern
	 *            the Java regular expression pattern to match recipient's uids.
	 *            For example, the pattern '.*' should return all recipients in
	 *            the store.
	 * @return the set of recipients whose uid matches the specified pattern.
	 * @see java.util.regex.Pattern
	 */
	public Set<Recipient> searchRecipients(String pattern);

}
