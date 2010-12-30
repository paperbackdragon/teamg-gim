package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * GimServer listen for connections and passes them off to worker threads.
 */
public class GimServer {

	/**
	 * Starts some threads
	 */
	public static void main(String[] args) {

		Data data = Data.getInstance();

		data.addUser(new User("cyblob@gmail.com", "password"));
		data.addUser(new User("me@jamesmcminn.com", "password"));

		// Create a socket for the client to connect to
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}

		// Create a new thread to check for timeouts and update system data
		Timeout timeout = new Timeout(20);
		Thread timeoutThread = new Thread(timeout);
		timeoutThread.start();

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
				// Nothing we can do at this point except kill the program.
				break;
			}
		}

	}
}
