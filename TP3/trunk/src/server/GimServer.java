package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

import server.util.UniqueID;

/**
 * GimServer listen for connections and passes them off to worker threads.
 * @author James McMinn
 */
public class GimServer {
	
	private static HashMap<Integer, Worker> workers = new HashMap<Integer, Worker>();

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
			Worker s = null;
			while (true) {
				synchronized (workers) {
					s = new Worker(serverSocket.accept(), workers);
					int id = UniqueID.getInstance().getNextClientID();
					workers.put(id, s);
					System.out.println("Creating new worker thread width id " + id);
					Thread t = new Thread(s);
					t.start();
				}
			}
		} catch (IOException e) {
			// TODO: We've just crashed, cleanup
		}

	}

}
