package uk.ac.glasgow.minder.event.impl;

public class DeadlineEvent extends EventImpl {

	String deliverable;
	String course;

	protected DeadlineEvent(String deliverble, String course, long time) {
		super(deliverble + course + time, time);
		this.deliverable = deliverble;
		this.course = course;
	}

	public String getDeliverable() {
		return deliverable;
	}

	public String getCourse() {
		return course;
	}

}
