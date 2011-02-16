package uk.ac.glasgow.minder.uistate.test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class SearchEventsTest {
	
	private static RecipientStoreImpl rs;
	private static UIStateImpl s;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rs = new RecipientStoreImpl();
		rs.addUser("Administrator", "admin", "monkey",
		new InternetAddress("monkey.me@glasgow.ac.uk"), Privilege.ADMINISTRATOR);
		
		s = new UIStateImpl(rs, new Controller(rs));
		s.login("admin", "monkey");
		
		Calendar cal1 = new GregorianCalendar(2011, 4, 30);
		Date date1 = cal1.getTime();
		s.createLectureEvent(date1, "Adam Smith", "Dr. Keats", 3600000, "English Lecture");
		
		Calendar cal2 = new GregorianCalendar(2011, 3, 4);
		Date date2 = cal2.getTime();
		s.createLectureEvent(date2, "Maths", "Mr. Lloyd", 3600000, "Maths Lecture");
		
		Calendar cal3 = new GregorianCalendar(2011, 2, 11);
		Date date3 = cal3.getTime();
		s.createDeadlineEvent(date3, "Report", "PSD3");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File target = new File("mailing.obj");
		target.delete();
		target = new File("users.obj");
		target.delete();
	}
	
	@Test
	public void noEventResults() {
		Assert.assertEquals(0, s.searchEvents("Lunch").size());
	}
	
	@Test
	public void oneEventResult() {
		Assert.assertEquals(1, s.searchEvents("Report").size());
	}
	
	@Test
	public void manyEventResults() {
		Assert.assertEquals(2, s.searchEvents(".* Lecture").size());
	}
}
