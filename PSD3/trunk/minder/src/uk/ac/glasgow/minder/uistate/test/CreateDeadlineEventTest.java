package uk.ac.glasgow.minder.uistate.test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.event.impl.EventImpl;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class CreateDeadlineEventTest {

	private RecipientStoreImpl rs;
	private UIStateImpl s;

	@Before
	public void setUp() throws Exception {
		rs = new RecipientStoreImpl();
		rs.addUser("Administrator", "admin", "monkey", new InternetAddress("monkey.me@glasgow.ac.uk"),
				Privilege.ADMINISTRATOR);

		s = new UIStateImpl(rs, new Controller(rs));
		s.login("admin", "monkey");

	}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
            File target = new File("mailing.obj");
            target.delete();
            target = new File("users.obj");
            target.delete();
    }
	
	@Test
	public void dateInvalid() {
		Calendar cal = new GregorianCalendar(1800, 01, 01);
		Date date = cal.getTime();

		s.createDeadlineEvent(date, "Exercise 1", "History");
		Assert.assertEquals(0, s.searchEvents("Exercise 1").size());
	}

	@Test
	public void dateNotProvided() {
		s.createDeadlineEvent(null, "Exercise 5", "History");
		Assert.assertEquals(0, s.searchEvents("Exercise 5").size());
	}

	@Test
	public void alreadyHasADeadline() {
		Calendar cal = new GregorianCalendar(2010, 25, 11);
		Date date = cal.getTime();

		s.createDeadlineEvent(date, "Exercise 2", "Maths");
		EventImpl e1 = s.searchEvents("Exercise 2").toArray(new EventImpl[0])[0];
		s.createDeadlineEvent(date, "Exercise 2", "Maths");
		EventImpl e2 = s.searchEvents("Exercise 2").toArray(new EventImpl[0])[0];
		Assert.assertEquals(e1, e2);
	}

	@Test
	public void courseNotProvided() {
		Calendar cal = new GregorianCalendar(2010, 25, 11);
		Date date = cal.getTime();
		
		s.createDeadlineEvent(date, "Exercise 4", null);
		Assert.assertEquals(0, s.searchEvents("Exercise 4").size());
	}

	// ************
	// WHAT ABOUT DEADLINE NOT PROVIDED? - WILL ASK YOU GUYS LATER :P
	// ************

	@Test
	public void validInput() {
		Calendar cal = new GregorianCalendar(2010, 25, 11);
		Date date = cal.getTime();

		s.createDeadlineEvent(date, "Exercise 3", "Maths");

		Assert.assertEquals(1, s.searchEvents("Exercise 3").size());
	}

}
