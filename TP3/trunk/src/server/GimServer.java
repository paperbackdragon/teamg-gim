package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

import server.util.UniqueID;

public class GimServer {
	
	private static HashMap<Integer, Worker> workers = new HashMap<Integer, Worker>();

	/**
	 * Starts some threads
	 */
	public static void main(String[] args) {

		ServerSocket serverSocket = null;
		// Create a socket for the client to connect to
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}

		// Listen for connections and pass them off to a Worker
		try {
			Worker s = null;
			while (true) {
				synchronized (workers) {
					s = new Worker(serverSocket.accept(), workers);
					workers.put(UniqueID.getInstance().getNextClientID(), s);
					Thread t = new Thread(s);
					t.start();
				}
			}
		} catch (IOException e) {
			// TODO: We've just crashed, cleanup
		}

	}

}
