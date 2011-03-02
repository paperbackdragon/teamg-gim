package uk.ac.glasgow.minder.event.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import uk.ac.glasgow.minder.event.Event;
import uk.ac.glasgow.minder.event.EventHost;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.RecipientStore;

/**
 * This class controls the addition and issuing of events
 * 
 * @author 0800890m
 * 
 */
public class Controller implements Runnable, EventHost {

	ConcurrentHashMap<String, EventImpl> events = new ConcurrentHashMap<String, EventImpl>();
	RecipientStore rs = null;
	Thread t;

	/**
	 * Creates a new controllers instance and starts the scheduler.
	 * 
	 * @param rs
	 *            The RecipientStore to use
	 */
	public Controller(RecipientStore rs) {
		this.rs = rs;

		// Start the scheduler, to, you know, schedule things.
		t = new Thread(this);
		t.start();
	}

	/**
	 * Send reminders when needed.
	 */
	@Override
	public void run() {
		while (true) {
			EventImpl nextEvent = getNextReminderEvent();
			Reminder nextReminder = null;
			long time = Long.MAX_VALUE;

			if (nextEvent != null) {
				nextReminder = nextEvent.getNextReminder();
				time = nextEvent.getStartDate().getTime() - System.currentTimeMillis() - nextReminder.getTimeBefore();
			}

			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				continue;
			}

			// Try and stop funny things from happening due to interrupts
			synchronized (this) {
				try {
					String host = "smtp.gmail.com";
					String from = "psd310g";
					String pass = "unsecure7";
					Properties props = System.getProperties();
					props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.smtp.host", host);
					props.put("mail.smtp.user", from);
					props.put("mail.smtp.password", pass);
					props.put("mail.smtp.port", "587");
					props.put("mail.smtp.auth", "true");

					Set<Recipient> recipients = rs.searchRecipients(nextReminder.getRecipientId());

					Session session = Session.getDefaultInstance(props, null);
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(from));

					// To get the array of addresses
					LinkedList<InternetAddress> emails = new LinkedList<InternetAddress>();
					for (Recipient r : recipients) {
						for (InternetAddress email : r.getEmailAddresses()) {
							emails.add(email);
						}
					}

					// Add them recipients to the email
					InternetAddress[] toAddress = emails.toArray(new InternetAddress[0]);
					for (int i = 0; i < toAddress.length; i++) {
						message.addRecipient(Message.RecipientType.TO, toAddress[i]);
					}

					// Set the email contents. Not very petty, but it works.
					if (nextEvent instanceof LectureEvent) {
						LectureEvent e = (LectureEvent) nextEvent;
						message.setSubject("Event Reminder: Lecture " + e.getTitle());
						message.setText("This is an automated message to remind you that " + e.getTitle() + " is "
								+ "taking place at " + e.getLocation() + " by " + e.getLecturerUsername() + ". "
								+ "The lecture will take place on " + e.getStartDate() + " and last for "
								+ (e.getDuration() / 60) + "minutes");
					} else if (nextEvent instanceof DeadlineEvent) {
						DeadlineEvent e = (DeadlineEvent) nextEvent;
						message.setSubject("Event Reminder: Deadline for " + e.getDeliverable());
						message.setText("This is an automated message to remind you that " + e.getDeliverable()
								+ " is due on " + e.getStartDate());
					} else if (nextEvent instanceof ConferenceEvent) {
						ConferenceEvent e = (ConferenceEvent) nextEvent;

						message.setSubject("Conference Reminder: " + e.getTitle() + " from " + e.getStartDate()
								+ " till " + e.getEndDate());
						
						String msg = "The conference has the following deadlines:\n";

						for (Event deadline : e.getEvents()) {
							if (deadline instanceof DeadlineEvent) {
								msg += "\t" + ((DeadlineEvent) deadline).getDeliverable() + " for ";
								msg += ((DeadlineEvent) deadline).getCourse() + " due ";
								msg += ((DeadlineEvent) deadline).getStartDate() + "\n";
							} else if (deadline instanceof LectureEvent) {
								msg += "\t" + ((LectureEvent) deadline).getTitle()+ " by ";
								msg += ((LectureEvent) deadline).getLecturerUsername() + " at ";
								msg += ((LectureEvent) deadline).getLocation() + " on ";
								msg += ((LectureEvent) deadline).getStartDate() + "\n";
							}
						}

						message.setText(msg);

					}

					Transport transport = session.getTransport("smtp");
					transport.connect(host, from, pass);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();

					nextEvent.removeReminder(nextReminder);

				} catch (MessagingException e) {
					System.out.println("Send in the sharks.");
				}

			}

		}

	}

	/**
	 * Find the event which has the next reminder that needs to be sent.
	 * 
	 * @return The Event which the reminder belongs to
	 */
	private EventImpl getNextReminderEvent() {
		EventImpl nextEvent = null;
		long timeBefore = Long.MAX_VALUE;

		for (EventImpl e : events.values()) {
			Reminder r = e.getNextReminder();
			if (r != null && r.timeBefore < timeBefore) {
				nextEvent = e;
				timeBefore = e.getNextReminder().timeBefore;
			}
		}

		return nextEvent;
	}

	/**
	 * Create a new deadline event. It's not like we have enough of them
	 * already.
	 */
	@Override
	public void createDeadlineEvent(Date date, String deliverable, String course) {
		if (date == null || date.before(new Date()) || deliverable == null || deliverable.length() == 0
				|| course == null || course.length() == 0)
			return;

		DeadlineEvent e = new DeadlineEvent(deliverable, course, date.getTime());

		if (events.get(e.getUid()) == null)
			events.put(e.getUid(), e);
	}

	/**
	 * Create a lecture event.
	 */
	@Override
	public void createLectureEvent(Date date, String location, String lecturerUsername, long duration, String title) {
		// F*** yeah. I mean no. But, really, it's not pretty and I don't like
		// it, but it's the easiest way of doing it.
		if (date == null || date.before(new Date()) || location == null || location.length() == 0 || duration < 0
				|| title == null || title.length() == 0 || lecturerUsername == null || lecturerUsername.length() == 0)
			return;

		LectureEvent e = new LectureEvent(location, lecturerUsername, duration, title, date.getTime());

		if (events.get(e.getUid()) == null)
			events.put(e.getUid(), e);
	}

	/**
	 * Search for an event which matched the given pattern.
	 */
	@Override
	public Set<Event> searchEvents(String pattern) {
		Set<Event> matched = new HashSet<Event>();
		for (Event e : events.values()) {
			if (e instanceof DeadlineEvent) {
				if (((DeadlineEvent) e).getDeliverable().matches(pattern)) {
					matched.add(e);
				}
			} else {
				if (((LectureEvent) e).getTitle().matches(pattern)) {
					matched.add(e);
				}
			}
		}

		return matched;
	}

	/**
	 * Attach a reminder to an existing event
	 */
	@Override
	public void attachReminderToEvent(String eventid, String recipientid, long timeBefore) {
		EventImpl e = events.get(eventid);
		if (e == null) {
			System.out.println("Could not find event to add reminder to");
			return;
		}

		if (rs.searchRecipients(recipientid).size() == 0)
			return;

		System.out.println("Attached reminder to " + e.getUid());
		e.attachReminder(recipientid, timeBefore);

		// Don't interrupt the thread if we're in the middle of sending an email
		synchronized (this) {
			t.interrupt();
		}
	}

	@Override
	public void createConference(String title, Date startDate, Date endDate) {
		if (title == null || title.length() == 0 || startDate == null || endDate == null || startDate.after(endDate))
			return;

		ConferenceEvent e = new ConferenceEvent(title, startDate, endDate);

		if (events.get(e.getUid()) == null)
			events.put(e.getUid(), e);
	}

	@Override
	public void addToConference(String event, String conference) {
		EventImpl e = events.get(event);
		if (e == null) {
			System.out.println("Could not find event to add reminder to");
			return;
		}

		EventImpl c = events.get(conference);
		if (c == null || !(c instanceof ConferenceEvent)) {
			System.out.println("Could not find event to add reminder to");
			return;
		}

		((ConferenceEvent) c).addEvent(e);
	}

}
