package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;


/**
 * GimServer listen for connections and passes them off to worker threads.
 */
public class GimServer {

	/**
	 * Starts some threads
	 */
	public static void main(String[] args) {
	
		Data data = Data.getInstance();

		data.addUser(new User("cyblob@gmail.com", "password", User.Status.OFFLINE, "James McMinn", "I'm a panda.",
				new HashMap<String, User>(), new HashMap<String, User>(), new HashMap<String, User>()));
		
		data.addUser(new User("me@jamesmcminn.com", "password", User.Status.OFFLINE, "Andrew McMinn", "I'm not a panda.",
				new HashMap<String, User>(), new HashMap<String, User>(), new HashMap<String, User>()));

		// Create a socket for the client to connect to
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}
		

		// TODO: A thread to check for clients to timeout

		// Listen for connections and create a Worker for them
		while (true) {
			try {
				Worker worker = new Worker(serverSocket.accept());
				int clientID = data.getNextClientID();
				System.out.println("Creating new worker thread width id " + clientID);
				data.addWorker(clientID, worker);
				Thread t = new Thread(worker);
				t.start();
			} catch (IOException e) {
				// TODO: We've just crashed, cleanup
			}
		}

	}
}
