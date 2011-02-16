package uk.ac.glasgow.minder.event.impl;

public class Reminder {

	String recipientId;
	long timeBefore;

	/**
	 * Create a new reminder
	 * 
	 * @param recipientId
	 *            The recipient of the reminder
	 * @param timeBefore
	 *            The time in ms before the event to send the reminder
	 */
	public Reminder(String recipientId, long timeBefore) {
		this.recipientId = recipientId;
		this.timeBefore = timeBefore;
	}

	/**
	 * Get the recipient of the reminder
	 * 
	 * @return the recipient of the reminder
	 */
	public String getRecipientId() {
		return recipientId;
	}

	/**
	 * Get the time before the reminder to send the reminder
	 * 
	 * @return the time before the remind to send the reminder in ms
	 */
	public long getTimeBefore() {
		return timeBefore;
	}

}
