package uk.ac.glasgow.minder.event.impl;

public class LectureEvent extends EventImpl {

	String location;
	String lecturerUsername;
	long duration;
	String title;

	public String getLocation() {
		return location;
	}

	public String getLecturerUsername() {
		return lecturerUsername;
	}

	public long getDuration() {
		return duration;
	}

	public String getTitle() {
		return title;
	}

	protected LectureEvent(String location, String lecturerUsername, long duration, String title, long time) {
		super(location + lecturerUsername + time, time);
		this.title = title;
		this.lecturerUsername = lecturerUsername;
		this.location = location;
		this.duration = duration;
	}

}
