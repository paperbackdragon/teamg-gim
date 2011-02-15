package uk.ac.glasgow.minder.uistate.test;


import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class createLectureEvent {
	
	private RecipientStoreImpl rs;
	private UIStateImpl s;

	@Before
	public void setUp() throws Exception {
		
		rs = new RecipientStoreImpl();
		rs.addUser("Administrator", "admin", "monkey",
		new InternetAddress("monkey.me@glasgow.ac.uk"), Privilege.ADMINISTRATOR);
		
		s = new UIStateImpl(rs, new Controller(rs));
		s.login("admin", "monkey");
		
	}

	@After
	public void tearDown() throws Exception {
		
		File target = new File("mailing.obj");
		target.delete();
		target = new File("users.obj");
		target.delete();
		
	}
	
	@Test
	
	public void dateInvalid() {
		
		s.createMailingList("Level 4");
		Calendar cal = new GregorianCalendar(1800, 01, 21);
		Date date = cal.getTime();
		
		s.createLectureEvent(date, "Kelvin Hall",  "Ted Tedson", 7200, "Class Test");
		
		
		// ?? am i searching on the right thing, james?
		Assert.assertEquals(0, s.searchEvents("Exercise 1").size());
		
	}

	@Test
	public void dateNotProvided() {
		
	}
	
	@Test
	
	public void noLocationGiven() {
		
	}
	
	@Test
	public void noLecturerUsernameGiven() {
		
	}
	
	@Test
	public void validInput() {
		
	}
}
