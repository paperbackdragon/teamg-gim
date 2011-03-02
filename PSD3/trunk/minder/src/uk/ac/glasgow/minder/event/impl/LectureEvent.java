package uk.ac.glasgow.minder.event.impl;

/**
 * A lecture event. Lectures are soooo much fun, I'm surprised people need
 * reminders for them.
 * 
 * @author james
 * 
 */
public class LectureEvent extends EventImpl {

	String location;
	String lecturerUsername;
	long duration;
	String title;

	/**
	 * Create a lecture event
	 * 
	 * @param location
	 *            The location of the lecture
	 * @param lecturerUsername
	 *            The username of the lecturer
	 * @param duration
	 *            The duration of the lecture
	 * @param title
	 *            The title of the lecture
	 * @param time
	 *            The time of the lecture in ms since 1st Jan 1970
	 */
	protected LectureEvent(String location, String lecturerUsername, long duration, String title, long time) {
		super(location + lecturerUsername + time, time);
		this.title = title;
		this.lecturerUsername = lecturerUsername;
		this.location = location;
		this.duration = duration;
	}

	/**
	 * Get the location of the lecture
	 * 
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Get the lecturer username
	 * 
	 * @return
	 */
	public String getLecturerUsername() {
		return lecturerUsername;
	}

	/**
	 * Get the duration of the lecture
	 * 
	 * @return
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Get the title of the lecture
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

}
