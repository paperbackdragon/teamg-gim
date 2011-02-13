package uk.ac.glasgow.minder.recipient.test;

import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.recipient.RecipientStore;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;

public class RecipientBundleTest {

	private static RecipientStore recipientStore;

	@Before
	public void setUp() {
		recipientStore = new RecipientStoreImpl();
	}

	@Test
	public void anotherTest() {
	}

	@Test
	public void testStuff() throws AddressException {

		recipientStore.addUser("Phil Gray", "pdg", "monkey",
				new InternetAddress("Philip.Gray@glasgow.ac.uk"),
				Privilege.ADMINISTRATOR);

		Set<Recipient> recipients = recipientStore.searchRecipients("pdg");
		Assert.assertFalse(recipients.isEmpty());

	}
}
