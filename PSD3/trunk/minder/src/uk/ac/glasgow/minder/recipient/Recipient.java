package uk.ac.glasgow.minder.recipient;

import java.io.Serializable;
import java.util.Set;

import javax.mail.internet.InternetAddress;

/**
 * Specifies the general properties for a recipient of event reminder
 * notifications.
 * 
 * @author tws
 */
public interface Recipient extends Serializable{

	/**
	 * @return the store unique id for this recipient.
	 */
	public String getUid();

	/**
	 * @return all email addresses associated with this recipient.
	 */
	public Set<InternetAddress> getEmailAddresses();
}
