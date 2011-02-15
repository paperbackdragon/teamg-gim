package uk.ac.glasgow.minder.uistate.test;

import java.io.File;

import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;


public class SearchRecipientTest {
	
	private static RecipientStoreImpl rs;
	private static UIStateImpl s;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rs = new RecipientStoreImpl();
		rs.addUser("Administrator", "admin", "monkey",
		new InternetAddress("monkey.me@glasgow.ac.uk"), Privilege.ADMINISTRATOR);
		
		s = new UIStateImpl(rs, new Controller(rs));
		s.login("admin", "monkey");
		
		s.createMailingList("Level 2");
		s.createMailingList("Level 4");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File target = new File("mailing.obj");
		target.delete();
		target = new File("users.obj");
		target.delete();
	}
	
	@Test
	public void noRecipResults() {
		Assert.assertEquals(0, s.searchRecipients("Level 3").size());
	}
	
	@Test
	public void oneRecipResult() {
		Assert.assertEquals(1, s.searchRecipients("Level 4").size());
	}
	
	@Test
	public void manyRecipResults() {
		Assert.assertEquals(2, s.searchRecipients("Level *").size());
	}
}
