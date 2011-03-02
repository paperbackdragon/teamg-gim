package uk.ac.glasgow.minder.event.impl;

import java.util.Date;
import java.util.LinkedList;

/**
 * A conference. One day I want to go to one of them.
 * 
 * @author james
 * 
 */
public class ConferenceEvent extends EventImpl {

	private String title;
	private Date startDate;
	private Date endDate;
	private LinkedList<EventImpl> events = new LinkedList<EventImpl>();

	public ConferenceEvent(String title, Date startDate, Date endDate) {
		super(title + startDate.toString() + endDate.toString(), startDate.getTime());
	}

	void addEvent(EventImpl e) {
		events.add(e);
	}

	LinkedList<EventImpl> getEvents() {
		return events;
	}

	public String getTitle() {
		return title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

}
