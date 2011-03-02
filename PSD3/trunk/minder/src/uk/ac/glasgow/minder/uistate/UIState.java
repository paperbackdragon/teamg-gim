package uk.ac.glasgow.minder.uistate;


import java.util.Date;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import uk.ac.glasgow.minder.event.Event;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.User;

public interface UIState {
	public User login(String username, String password);
	
	public void createUser(
			String displayName,
			String username,
			String password,
			InternetAddress emailAddress,
			Privilege privilege);
	
	public void createMailingList(String label);
	
	public void addUserToMailingList(String username, String label);
	
	public void createDeadlineEvent(
			Date date, 
			String deliverable, String course);
	
	public void createLectureEvent(
			Date date,
			String location, String lecturerUsername, long duration, String title);
	
	public void attachReminderToEvent(String eventid, String recipientid, long timeBefore);
		
	public Set<Recipient> searchRecipients(String pattern);
		
	public Set<Event> searchEvents(String pattern);

	void createConference(String title, Date startDate, Date endDaye);

	public void createConference(String event, String conference);
}