package uk.ac.glasgow.minder.uistate.test;

import java.io.File;

import javax.mail.internet.InternetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.Privilege;
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
