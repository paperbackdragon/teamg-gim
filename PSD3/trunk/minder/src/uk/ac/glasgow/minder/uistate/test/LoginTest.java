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
		//TODO: Create User.equals method, then complete login tests
		
		try {
			User user1 = new UserImpl("Administrator", "admin", "monkey", 
					new InternetAddress("monkey.me@glasgow.ac.uk"), Privilege.ADMINISTRATOR);
			rs.addUser("Administrator", "admin", "monkey",
					new InternetAddress("monkey.me@glasgow.ac.uk"), Privilege.ADMINISTRATOR);
			
			Assert.assertEquals(user1, s.login("admin", "monkey"));
		} catch (AddressException e) {}		
	}
	
	@Test
	public void badPassword() {
	}
	
	@Test
	public void badUsername() {
	}		
	
	@Test
	public void badBoth() {
	}
}
