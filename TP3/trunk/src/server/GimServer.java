package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * GimServer listen for connections and passes them off to worker threads.
 */
public class GimServer {

	/**
	 * Starts some threads
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		Data data = Data.getInstance();

		System.out.println("Loading users...");

		// Load the users from the "db"
		String filename = "users.db";
		ArrayList<Object> users = new ArrayList<Object>();
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			users = (ArrayList<Object>) in.readObject();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Add all the users
		for (Object user : users) {
			data.addUser((User) user);
		}

		// Make the friendlists etc. work correctly by recreating the friendlist
		// from the list of users
		for (User user : data.getUsers()) {
			for (User friend : user.getFriendList()) {
				User realUser = data.getUser(friend.getId());
				user.addFreindRequest(realUser);
				realUser.addToInFriendList(user);
			}

			for (User blocked : user.getBlockedUsers()) {
				user.block(data.getUser(blocked.getId()));
			}
		}

		System.out.println(users.size() + " users loaded.");

		Room room = new Room(null, true);
		data.addRoom(room);

		SSLServerSocketFactory sslSrvFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try {
			data.serverSocket = (SSLServerSocket) sslSrvFactory.createServerSocket(4444);
		} catch (IOException e1) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}

		// Pick all AES algorithms of 128 bits key size
		String patternString = "AES.*128";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher;
		boolean matchFound;

		String supportedSuites[] = ((SSLServerSocket) data.serverSocket).getSupportedCipherSuites();
		String suitePickOrder[] = new String[supportedSuites.length];

		int j = 0, k = supportedSuites.length - 1;
		for (int i = 0; i < supportedSuites.length; i++) {
			matcher = pattern.matcher(supportedSuites[i]);
			matchFound = matcher.find();
			if (matchFound)
				suitePickOrder[j++] = supportedSuites[i];
			else
				suitePickOrder[k--] = supportedSuites[i];
		}
		((SSLServerSocket) data.serverSocket).setEnabledCipherSuites(suitePickOrder);

		// Create a new thread to check for timeouts and update system data
		Timeout timeout = new Timeout(20);
		Thread timeoutThread = new Thread(timeout);
		timeoutThread.start();

		// Create a control thread for the entire server
		Thread controller = new Thread(new Controller());
		controller.start();

		// Listen for connections and create a Worker for them
		while (true) {
			try {
				int clientID = data.getNextClientID();
				Worker worker = new Worker(clientID, data.serverSocket.accept());
				System.out.println("Creating new worker thread width id " + clientID);
				data.addWorker(clientID, worker);
				Thread t = new Thread(worker);
				t.setName(clientID + " ");
				t.start();
			} catch (IOException e) {
				break;
			}
		}

		/**
		 * Save the user data
		 */
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		ArrayList<User> list = new ArrayList<User>(data.getUsers());
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.exit(0);

	}
}
