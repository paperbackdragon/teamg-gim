package uk.ac.glasgow.minder.event.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import uk.ac.glasgow.minder.event.Event;

/**
 * @author 0800890m
 * 
 */

public class EventImpl implements Event {

	private long time;
	private String uid;
	private LinkedList<Reminder> reminders = new LinkedList<Reminder>();

	/**
	 * Create a new event
	 * 
	 * @param time
	 *            The time in ms since 1st January 1970
	 */
	protected EventImpl(String uid, long time) {
		this.uid = uid;
		this.time = time;
	}

	/**
	 * The start date of the Event
	 * 
	 * @return A Date object representing the start date of the event
	 */
	public Date getStartDate() {
		return new Date(this.time);
	}

	/**
	 * Get the unique ID of the event
	 * 
	 * @return The unique ID of the event
	 */
	public String getUid() {
		return this.uid;
	}

	/**
	 * Get the basic properties of the Event
	 * 
	 * @return A Properties object with the following properties:
	 *         <ul>
	 *         <li>startTime - The start time of the event</li>
	 *         <li>uid - The unique id of the of Event</li>
	 *         <li>numberReminder - The number of reminders on this event</li>
	 *         </ul>
	 */
	public Properties getEventProperties() {
		Properties p = new Properties();
		p.setProperty("startTime", this.getStartDate().toString());
		p.setProperty("uid", this.getUid());
		p.setProperty("numberReminders", this.reminders.size() + "");
		return p;
	}

	/**
	 * Add the reminder to the event
	 * 
	 * @param recipientid
	 *            The recipient of the reminder
	 * @param timeBefore
	 *            The time before the event to add the reminder in ms
	 */
	public void attachReminder(String recipientid, long timeBefore) {
		System.out.println(new Date(this.getStartDate().getTime() - timeBefore) + " " + System.currentTimeMillis());

		if (timeBefore > 0 && (this.getStartDate().getTime() - timeBefore) >= System.currentTimeMillis())
			reminders.add(new Reminder(recipientid, timeBefore));
	}

	public Reminder getNextReminder() {
		Reminder next = null;

		for (Reminder r : this.reminders) {
			if (next == null || r.getTimeBefore() < next.getTimeBefore())
				next = r;
		}

		return next;
	}

	/**
	 * Remove a reminder from the event
	 * 
	 * @param r
	 */
	public void removeReminder(Reminder r) {
		reminders.remove(r);
	}

	/**
	 * Get the ID of this event
	 */
	@Override
	public String toString() {
		return this.getUid();
	}

}
