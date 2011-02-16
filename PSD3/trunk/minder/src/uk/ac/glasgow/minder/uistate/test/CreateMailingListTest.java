package uk.ac.glasgow.minder.uistate.test;


import java.io.File;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.impl.MailingList;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class CreateMailingListTest {
	
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
	public void labelExists() {
		s.createMailingList("Level 4");
		MailingList ml1 = s.searchRecipients("Level 4").toArray(new MailingList[0])[0];
		
		s.createMailingList("Level 4");
		MailingList ml2 = s.searchRecipients("Level 4").toArray(new MailingList[0])[0];
		
		Assert.assertEquals(ml1, ml2);
	}
	
	@Test
	public void correct() {
		s.createMailingList("Level 3");
		
		Set<Recipient> recipients = s.searchRecipients("Level 3");
		Assert.assertEquals(1, recipients.size());
	}
}
