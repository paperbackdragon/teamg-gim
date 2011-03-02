package uk.ac.glasgow.minder.event.impl;

/**
 * A deadline event.
 * 
 * Deadlines are totally fun.
 * 
 * @author james
 * 
 */
public class DeadlineEvent extends EventImpl {

	String deliverable;
	String course;

	/**
	 * Constructor for constructing.
	 * 
	 * @param deliverble
	 *            The name of the deliverable
	 * @param course
	 *            The name of the course
	 * @param time
	 *            The time in ms since 1st Jan 1970 that this deadline occurs
	 */
	protected DeadlineEvent(String deliverble, String course, long time) {
		super(deliverble + course + time, time);
		this.deliverable = deliverble;
		this.course = course;
	}

	/**
	 * Get the name of the deliverable
	 * 
	 * @return The name of the deliverable
	 */
	public String getDeliverable() {
		return deliverable;
	}

	/**
	 * Get the course of the deliverable
	 * 
	 * @return The name of the course
	 */
	public String getCourse() {
		return course;
	}

}
