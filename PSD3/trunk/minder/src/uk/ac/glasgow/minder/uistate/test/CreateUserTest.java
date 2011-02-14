package uk.ac.glasgow.minder.uistate.test;

import java.io.File;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class CreateUserTest {

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
	public void userAlreadyExists() throws Exception {
		try {
			s.createUser("stevie", "Stevie", "meow123", new InternetAddress("cyblob@gmail.com"), Privilege.RECIPIENT);
			s.createUser("stevie", "Stevie", "meow123", new InternetAddress("cyblob2@gmail.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {}
		
		Set<Recipient> recipients = s.searchRecipients("Stevie");
		//for(Recipient r : recipients) {
		//	Assert.assertTrue(r..getEmailAddresses().contains(new InternetAddress("cyblob@gmail.com")));
		//}
		
		//THIS ALLOWS OVERWRITING
		Assert.assertEquals(recipients.size(), 1);
	}
	
	@Test
	public void bigPassword() {
		try {
			s.createUser("jimi", "Jimi Hendrix", "purplehaze", new InternetAddress("email@email.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {}
		
		Set<Recipient> recipients = s.searchRecipients("Jimi Hendrix");
		Assert.assertEquals(recipients.size(), 0);
	}
	
	@Test
	public void badPassword() {
		try {
			s.createUser("jimi", "Jimi Hendrix", "%&o_O", new InternetAddress("email@email.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {}
		
		Set<Recipient> recipients = s.searchRecipients("Jimi Hendrix");
		Assert.assertEquals(recipients.size(), 0);
	}

	@Test
	public void badEmail() {		
		try {
			s.createUser("jimi", "Jimi Hendrix", "meow123", new InternetAddress("@email.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {}
		
		Set<Recipient> recipients = s.searchRecipients("Jimi Hendrix");
		Assert.assertEquals(recipients.size(), 0);
	}
	
	@Test
	public void correct() {
		try {
			s.createUser("jimi", "Jimi Hendrix", "meow123", new InternetAddress("email@email.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {}
		
		Set<Recipient> recipients = s.searchRecipients("Jimi Hendrix");
		Assert.assertEquals(recipients.size(), 1);
		
	}
}
