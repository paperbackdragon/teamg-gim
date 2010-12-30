package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;

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
		
		for(Object user : users) {
			data.addUser((User) user);
		}
		
		System.out.println(users.size() + " users loaded.");
		

		// Create a socket for the client to connect to
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}

		data.serverSocket = serverSocket;
		
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
				Worker worker = new Worker(clientID, serverSocket.accept());
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
