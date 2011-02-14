package uk.ac.glasgow.minder.uistate.impl;

import java.util.Date;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import uk.ac.glasgow.minder.event.Event;
import uk.ac.glasgow.minder.event.EventHost;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.RecipientStore;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.User;
import static uk.ac.glasgow.minder.recipient.Privilege.*;
import uk.ac.glasgow.minder.uistate.UIState;

/**
 * Implements the UIState interface, providing a facade to other bundles in the
 * minder system.
 * 
 * @author tws
 * 
 */
public class UIStateImpl implements UIState {

	private RecipientStore recipientStore;
	private EventHost eventHost;

	private User currentUser;

	/**
	 * @param recipientStore
	 * @param eventHost
	 */
	public UIStateImpl(RecipientStore recipientStore, EventHost eventHost) {
		this.recipientStore = recipientStore;
		this.eventHost = eventHost;
	}

	@Override
	public User login(String username, String password) {
		currentUser = recipientStore.authenticateUser(username, password);
		return currentUser;
	}

	@Override
	public void createUser(String displayName, String username,
			String password, InternetAddress emailAddress, Privilege privilege) {

		if (!checkPrivilege(ADMINISTRATOR))
			return;

		recipientStore.addUser(displayName, username, password, emailAddress,
				privilege);
	}

	@Override
	public void createMailingList(String label) {

		if (!checkPrivilege(ADMINISTRATOR))
			return;

		recipientStore.createMailingList(label, currentUser.getUid());
	}

	@Override
	public void addUserToMailingList(String username, String label) {

		if (!checkPrivilege(ADMINISTRATOR))
			return;

		recipientStore.addUserToMailingList(label, username);
	}

	@Override
	public Set<Recipient> searchRecipients(String pattern) {
		if (!checkPrivilege(RECIPIENT))
			return null;
		
		return recipientStore.searchRecipients(pattern);
	}

	@Override
	public void createDeadlineEvent(Date date, String deliverable, String course) {

		if (!checkPrivilege(EVENT_MANAGER))
			return;

		eventHost.createDeadlineEvent(date, deliverable, course);

	}

	@Override
	public void createLectureEvent(Date date, String location,
			String lecturerUsername, long duration, String title) {

		if (!checkPrivilege(EVENT_MANAGER))
			return;

		eventHost.createLectureEvent(date, location, lecturerUsername,
				duration, title);
	}

	@Override
	public void attachReminderToEvent(String eventid, String recipientid,
			long timeBefore) {
		eventHost.attachReminderToEvent(eventid, recipientid, timeBefore);
	}

	@Override
	public Set<Event> searchEvents(String pattern) {
		if (!checkPrivilege(RECIPIENT))
			return null;

		return eventHost.searchEvents(pattern);
	}

	private boolean checkPrivilege(Privilege privilege) {
		if (currentUser == null)
			return false;
		return currentUser.getPrivilege().compareTo(privilege) <= 0;
	}
}
