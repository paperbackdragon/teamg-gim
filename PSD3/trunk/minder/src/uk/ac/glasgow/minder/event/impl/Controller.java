package uk.ac.glasgow.minder.event.impl;

import java.util.Date;
import java.util.HashSet;
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

	public Controller(RecipientStore rs) {
		this.rs = rs;
		t = new Thread(this);
	}

	@Override
	public void run() {
		while (true) {
			EventImpl nextEvent = getNextReminderEvent();
			Reminder nextReminder = nextEvent.getNextReminder();
			long time = (nextEvent.getStartDate().getTime() - System
					.currentTimeMillis()) / 1000;

			if (time <= 0)
				time = Integer.MAX_VALUE;

			try {
				Thread.sleep(time - nextReminder.getTimeBefore());
			} catch (InterruptedException e) {
				continue;
			}

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

					String[] to = rs.searchRecipients(nextReminder.getRecipientId()).toArray(new String[0]);

					Session session = Session.getDefaultInstance(props, null);
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(from));

					InternetAddress[] toAddress = new InternetAddress[to.length];

					// To get the array of addresses
					for (int i = 0; i < to.length; i++) {
						toAddress[i] = new InternetAddress(to[i]);
					}

					System.out.println(Message.RecipientType.TO);

					for (int i = 0; i < toAddress.length; i++) {
						message.addRecipient(Message.RecipientType.TO,
								toAddress[i]);
					}

					if (nextEvent instanceof LectureEvent) {
						LectureEvent e = (LectureEvent) nextEvent;
						message.setSubject("Event Reminder: Lecture "
								+ e.getTitle());
						message.setText("This is an automated message to remind you that "
								+ e.getTitle()
								+ " is "
								+ "taking place at "
								+ e.getLocation()
								+ " by "
								+ e.getLecturerUsername()
								+ ". "
								+ "The lecture will take place on "
								+ e.getStartDate()
								+ " and last for "
								+ (e.getDuration() / 60) + "minutes");
					} else {
						DeadlineEvent e = (DeadlineEvent) nextEvent;
						message.setSubject("Event Reminder: Deadline for "
								+ e.getDeliverable());
						message.setText("This is an automated message to remind you that "
								+ e.getDeliverable()
								+ " is due at"
								+ e.getStartDate());
					}

					Transport transport = session.getTransport("smtp");
					transport.connect(host, from, pass);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();

				} catch (MessagingException e) {
					System.out.println("Send in the sharks.");
				}

			}

		}

	}

	private EventImpl getNextReminderEvent() {
		EventImpl nextEvent = null;
		long timeBefore = 0;

		for (EventImpl e : events.values()) {
			if (e.getNextReminder().timeBefore < timeBefore) {
				nextEvent = e;
				timeBefore = e.getNextReminder().timeBefore;
			}
		}

		return nextEvent;
	}

	public void createDeadlineEvent(Date date, String deliverable, String course) {
		DeadlineEvent e = new DeadlineEvent(deliverable, course,
				date.getTime() / 1000);
		events.put(e.getUid(), e);
		t.interrupt();
	}

	@Override
	public void createLectureEvent(Date date, String location,
			
			String lecturerUsername, long duration, String title) {
		LectureEvent e = new LectureEvent(location, lecturerUsername, duration,
				title, date.getTime() / 1000);
		events.put(e.getUid(), e);
		t.interrupt();
	}

	@Override
	public Set<Event> searchEvents(String pattern) {
		Set<Event> matched = new HashSet<Event>();

		for (Event e : events.values()) {
			if(e instanceof DeadlineEvent) {
				if (((DeadlineEvent) e).getDeliverable().matches(pattern)) {
					matched.add(e);
				}
			} else {
				if(((LectureEvent) e).getTitle().matches(pattern)) {
					matched.add(e);
				}
			}
		}

		return matched;
	}

	@Override
	public void attachReminderToEvent(String eventid, String recipientid,
			long timeBefore) {
		EventImpl e = events.get(eventid);

		if (e == null)
			return;

		e.attachReminder(recipientid, timeBefore);
	}

}
