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

		// Create a socket for the client to connect to
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}

		// Listen for connections and create a Worker for them
		try {
			while (true) {
				Worker s = new Worker(serverSocket.accept());
				System.out.println("Creating new worker thread width id " + Data.getInstance().getNextClientID());
				Thread t = new Thread(s);
				t.start();
			}
		} catch (IOException e) {
			// TODO: We've just crashed, cleanup
		}

	}

}
