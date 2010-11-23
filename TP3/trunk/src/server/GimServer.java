package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class GimServer {

	public enum Status {
		ONLINE, OFFLINE, BUSY, AWAY, APPEAR
	}

	private static HashMap<String, ServerConnection> connectedClients = new HashMap<String, ServerConnection>();

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

		try {
			ServerConnection s = null;

			while (true) {
				synchronized (connectedClients) {
					s = new ServerConnection(serverSocket.accept(),
							getConnectedClients());
					s.run();
					connectedClients.put(s.toString(), s);
				}
			}

		} catch (IOException e) {
			System.exit(1);
		}

	}

	/**
	 * Return a HashMap of the currently connected clients
	 * 
	 * @return
	 */
	public static HashMap<String, ServerConnection> getConnectedClients() {
		return connectedClients;
	}

}
