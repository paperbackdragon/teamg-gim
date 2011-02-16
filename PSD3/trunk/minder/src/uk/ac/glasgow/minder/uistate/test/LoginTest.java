package uk.ac.glasgow.minder.uistate.test;

import java.io.File;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.User;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.recipient.impl.UserImpl;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

public class LoginTest {
	
	private RecipientStoreImpl rs;
	private UIStateImpl s;

	@Before
	public void setUp() throws Exception {
		rs = new RecipientStoreImpl();
		rs.addUser("Administrator", "admin", "monkey",
				new InternetAddress("monkey.me@glasgow.ac.uk"), Privilege.ADMINISTRATOR);
		
		s = new UIStateImpl(rs, new Controller(rs));
	}

	@After
	public void tearDown() {
		File target = new File("mailing.obj");
		target.delete();
		target = new File("users.obj");
		target.delete();
	}
	
	@Test
	public void correct() {
		User u1 = s.login("admin", "monkey");
		User u2 = s.searchRecipients("admin").toArray(new UserImpl[0])[0];
		Assert.assertEquals(u1, u2);
	}
	
	@Test
	public void badPassword() {
		Assert.assertNull(s.login("admin", "sunshine"));
	}
	
	@Test
	public void badUsername() {
		Assert.assertNull(s.login("sdfsdfsdf", "monkey"));
	}		
	
	@Test
	public void badBoth() {
		Assert.assertNull(s.login("james", "isacat...meow"));
	}
}
