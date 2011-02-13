package uk.ac.glasgow.minder.event;

import java.util.Date;
import java.util.Properties;

public interface Event {

	/**
	 * @return the start date of the event
	 */
	public Date getStartDate();

	/**
	 * @return the unique id of this event. Event ids must be unique within a
	 *         single EventHost
	 */
	public String getUid();

	/**
	 * @return the event type specific properties of an event.
	 * @see java.util.Properties
	 */
	public abstract Properties getEventProperties();

}