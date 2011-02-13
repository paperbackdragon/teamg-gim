package uk.ac.glasgow.minder.event.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import uk.ac.glasgow.minder.event.Event;
import uk.ac.glasgow.minder.event.EventHost;

/**
 * This class controls the addition and issuing of events
 * 
 * @author 0800890m
 * 
 */
public class Controller implements Runnable, EventHost {

	ConcurrentHashMap<String, EventImpl> events = new ConcurrentHashMap<String, EventImpl>();

	@Override
	public void run() {

	}

	public void createDeadlineEvent(Date date, String deliverable, String course) {
		DeadlineEvent e = new DeadlineEvent(deliverable, course, date.getTime() / 1000);
		events.put(e.getUid(), e);
	}

	@Override
	public void createLectureEvent(Date date, String location,
			String lecturerUsername, long duration, String title) {
		LectureEvent e = new LectureEvent(location, lecturerUsername, duration, title, date.getTime() / 1000);
		events.put(e.getUid(), e);
	}

	@Override
	public Set<Event> searchEvents(String pattern) {
		Set<Event> matched = new HashSet<Event>();
		
		for(Event e : events.values()) {
			if(e.getUid().matches(pattern))
				matched.add(e);
		}
		
		return matched;
	}

	@Override
	public void attachReminderToEvent(String eventid, String recipientid,
			long timeBefore) { 
		EventImpl e = events.get(eventid);
		
		if(e == null)
			return;
		
		e.attachReminder(recipientid, timeBefore);
	}

}
