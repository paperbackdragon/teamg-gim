package uk.ac.glasgow.minder.recipient.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;

import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.RecipientStore;
import uk.ac.glasgow.minder.recipient.User;

public class RecipientStoreImpl implements RecipientStore {

	private Map<String, Recipient> recipients = new HashMap<String, Recipient>();
	private Map<String, User> users;
	private Map<String, MailingList> mailingLists;

	public RecipientStoreImpl() {
		readFromStore();
	}

	@Override
	public void addUser(String displayName, String username, String password, InternetAddress emailAddress,
			Privilege privilege) {

		UserImpl user = new UserImpl(displayName, username, password, emailAddress, privilege);
		users.put(username, user);
		recipients.put(username, user);
		writeOutToStore();
	}

	@Override
	public void addUserToMailingList(String label, String username) {
		MailingList mailingList = mailingLists.get(label);
		User user = users.get(username);
		if (mailingList != null && user != null) {
			mailingList.addMember(user);
			writeOutToStore();
		}

	}

	@Override
	public void createMailingList(String label, String ownerUsername) {
		User owner = users.get(ownerUsername);
		if (owner != null) {
			MailingList mailingList = new MailingList(label, owner);
			recipients.put(label, mailingList);
			mailingLists.put(label, mailingList);
			writeOutToStore();
		}
	}

	@Override
	public Set<Recipient> searchRecipients(String pattern) {
		Set<Recipient> result = new HashSet<Recipient>();

		Pattern p = Pattern.compile(pattern);

		for (Recipient recipient : recipients.values()) {

			String name = recipient.getUid();

			if (p.matcher(name).matches())
				result.add(recipient);
		}
		return result;
	}

	@Override
	public User authenticateUser(String username, String password) {

		User user = users.get(username);
		if (user != null && user.authenticate(password))
			return user;
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	private void readFromStore() {
		try {
			ObjectInputStream mois = new ObjectInputStream(new FileInputStream("mailing.obj"));
			this.mailingLists = (Map) mois.readObject();
			mois.close();

			ObjectInputStream uois = new ObjectInputStream(new FileInputStream("users.obj"));
			this.users = (Map) uois.readObject();
			uois.close();

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		if (mailingLists == null)
			mailingLists = new HashMap<String, MailingList>();
		if (users == null)
			users = new HashMap<String, User>();
		recipients.putAll(mailingLists);
		recipients.putAll(users);
	}

	private void writeOutToStore() {
		try {
			ObjectOutputStream uoos = new ObjectOutputStream(new FileOutputStream("users.obj"));
			uoos.writeObject(users);
			ObjectOutputStream moos = new ObjectOutputStream(new FileOutputStream("mailing.obj"));
			moos.writeObject(mailingLists);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}