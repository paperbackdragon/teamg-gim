package uk.ac.glasgow.minder.recipient.impl;

import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.User;

public class MailingList implements Recipient {

	private static final long serialVersionUID = 1L;

	private String label;

	private User owner;

	private Set<User> members = new HashSet<User>();

	/**
	 * Constructs a new, empty mailing list.
	 * 
	 * @param label
	 *            the unique identifier for the mailing list.
	 * @param open
	 *            a flag indicating whether ordinary users can join the mailing
	 *            list.
	 * @param owner
	 *            the list owner.
	 */
	public MailingList(String label, User owner) {
		this.label = label;
		this.owner = owner;
	}

	public String getLabel() {
		return label;
	}

	public User getOwner() {
		return owner;
	}

	public Set<User> getMembers() {
		return new HashSet<User>(members);
	}

	public void addMember(User newMember) {
		members.add(newMember);
	}

	@Override
	public Set<InternetAddress> getEmailAddresses() {
		Set<InternetAddress> emailAddresses = new HashSet<InternetAddress>();

		for (User user : members)
			emailAddresses.addAll(user.getEmailAddresses());

		return emailAddresses;
	}

	@Override
	public String getUid() {
		return label;
	}

	@Override
	public String toString() {
		Set<String> usernames = new HashSet<String>();
		for (User member : members)
			usernames.add(member.getUid());

		return this.label + "\t" + this.owner.getDisplayName() + "\t"
				+ usernames;
	}
}
