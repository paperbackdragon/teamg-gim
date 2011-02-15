package uk.ac.glasgow.minder.recipient.impl;

import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.User;

public class UserImpl implements User {

	private static final long serialVersionUID = 1L;

	private String displayName;

	private String username;

	private String password;

	private InternetAddress emailAddress;

	private Privilege privilege;

	public UserImpl(

	String displayName, String username, String password, InternetAddress emailAddress, Privilege privilege) {
		this.username = username;
		this.displayName = displayName;
		this.password = password;
		this.emailAddress = emailAddress;
		this.privilege = privilege;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the emailAddress
	 */
	public InternetAddress getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the privilege
	 */
	@Override
	public Privilege getPrivilege() {
		return privilege;
	}

	/**
	 * @return the displayName
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the singleton set containing the emailAddress of this user.
	 */
	@Override
	public Set<InternetAddress> getEmailAddresses() {

		Set<InternetAddress> emailAddresses = new HashSet<InternetAddress>();
		emailAddresses.add(emailAddress);

		return emailAddresses;
	}

	@Override
	public String getUid() {
		return username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.glasgow.minder.recipient.impl.User#authenticate(java.lang.String)
	 */
	public boolean authenticate(String candidate) {
		return password.equals(candidate);
	}

	@Override
	public String toString() {
		return username + "\t" + displayName + "\t" + privilege + "\t" + emailAddress;
	}

}
