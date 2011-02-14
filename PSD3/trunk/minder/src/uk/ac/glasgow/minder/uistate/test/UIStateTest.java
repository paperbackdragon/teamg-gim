package uk.ac.glasgow.minder.uistate.test;

import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class UIStateTest {

	private RecipientStoreImpl rs;
	private UIStateImpl s;
	
	@Before
	public void setUp() {
		this.rs = new RecipientStoreImpl();
		try {
			rs.addUser("Administrator", "admin", "monkey",
					new InternetAddress("monkey.me@glasgow.ac.uk"), Privilege.ADMINISTRATOR);
		} catch (AddressException e) {
		}
		
		this.s = new UIStateImpl(rs, new Controller(rs));
		
		s.login("admin", "monkey");
	}

	@Test
	public void userAlreadyExists() {
		try {
			s.createUser("stevie", "Stevie", "meow123", new InternetAddress("cyblob@gmail.com"), Privilege.RECIPIENT);
			s.createUser("stevie", "Stevie", "meow123", new InternetAddress("cyblob@gmail.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {
		}
		
		Set<Recipient> recipients = s.searchRecipients("Stevie");
		Assert.assertEquals(recipients.size(), 1);
	}
	
	@Test
	public void badPassword() {
		try {
			s.createUser("jimi", "J Hendrix", "purplehaze", new InternetAddress("email@email.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {
		}
		
		Set<Recipient> recipients = s.searchRecipients("J Hendrix");
		Assert.assertEquals(recipients.size(), 0);
	}

}
