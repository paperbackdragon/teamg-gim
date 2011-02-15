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
import uk.ac.glasgow.minder.recipient.User;
import uk.ac.glasgow.minder.recipient.impl.MailingList;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class AddUserToML {

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
	public void userDoesntExist() {
		s.createMailingList("Level 4");
		s.addUserToMailingList("Frank Newton", "Level 4");
		
		Set<Recipient> recipients = s.searchRecipients("Level 4");
		//TODO: Doesn't work, println shows too many recipients
		System.out.println(recipients);
		Set<User> u = null;
		for(Recipient r : recipients) {
			u = ((MailingList) r).getMembers();
			System.out.println(u);
		}
		
		//TODO: Does addUserToMailingList allow users not in the system to be added?
		Assert.assertEquals(0, u);
	}

	@Test
	public void noUserSpecified() {
		
	}
	
	@Test
	public void userAlreadyInML() {
		
	}
	
	@Test
	public void MLDoesNotExist() {
		
	}
	
	@Test
	public void noMLSpecified() {
		
	}
	
	@Test
	public void correct() {
		
	}
}
