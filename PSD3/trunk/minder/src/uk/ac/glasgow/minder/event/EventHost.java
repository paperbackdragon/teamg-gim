package uk.ac.glasgow.minder.event;

import java.util.Date;
import java.util.Set;

public interface EventHost {

	public void createDeadlineEvent(Date date, String deliverable, String course);

	public void createLectureEvent(Date date, String location,
			String lecturerUsername, long duration, String title);

	Set<Event> searchEvents(String pattern);

	/**
	 * Registers a new reminder for the specified event.
	 * 
	 * @param eventid
	 *            the unique id of the event.
	 * @param recipientid
	 *            the mailing list label or username of a recipient of reminders
	 * @param timeBefore
	 *            the time before (milli-seconds) the event's start time.
	 */
	public void attachReminderToEvent(String eventid, String recipientid,
			long timeBefore);
}
