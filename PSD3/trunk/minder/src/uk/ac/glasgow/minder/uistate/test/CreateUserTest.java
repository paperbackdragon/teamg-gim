package uk.ac.glasgow.minder.uistate.test;

import java.io.File;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.recipient.impl.UserImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class CreateUserTest {

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
	public void userAlreadyExists() {
		try {
			s.createUser("stevie", "Stevie", "meow123", new InternetAddress("cyblob@gmail.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {
		}
		UserImpl u1 = s.searchRecipients("Stevie").toArray(new UserImpl[0])[0];

		try {
			s.createUser("stevie", "Stevie", "meow123", new InternetAddress("cyblob@gmail.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {
		}
		UserImpl u2 = s.searchRecipients("Stevie").toArray(new UserImpl[0])[0];

		Assert.assertEquals(u1, u2);
	}

	@Test
	public void bigPassword() {
		try {
			s.createUser("jimi", "Jimi Hendrix1", "purplehaze", new InternetAddress("email@email.com"),
					Privilege.RECIPIENT);
		} catch (AddressException e) {
		}

		Set<Recipient> recipients = s.searchRecipients("Jimi Hendrix1");
		
		Assert.assertEquals(0, recipients.size());
	}

	@Test
	public void badPassword() {
		try {
			s.createUser("jimi", "ShouldFail", "%&o_O", new InternetAddress("email@email.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {
		}

		Set<Recipient> recipients = s.searchRecipients("ShouldFail");
		Assert.assertEquals(0, recipients.size());
	}

	@Test
	public void badEmail() {
		try {
			s.createUser("jimi", "Jimi Hendrix3", "meow123", new InternetAddress("@email.com"), Privilege.RECIPIENT);
		} catch (AddressException e) {
		}

		Set<Recipient> recipients = s.searchRecipients("Jimi Hendrix3");
		Assert.assertEquals(0, recipients.size());
	}

	@Test
	public void correct() {
		try {
			s
					.createUser("jimi", "Jimi Hendrix4", "meow123", new InternetAddress("email@email.com"),
							Privilege.RECIPIENT);
		} catch (AddressException e) {
		}

		Set<Recipient> recipients = s.searchRecipients("Jimi Hendrix4");
		Assert.assertEquals(1, recipients.size());
	}
}
