package server;

import java.util.HashMap;

import client.net.ClientConnection;

public class GimServer {
	
	public enum Status {ONLINE, OFFLINE, BUSY, AWAY, APPEAR}
	
	private HashMap<String, ClientConnection> connectedClients = new HashMap<String, ClientConnection>();
	
	/**
	 * Starts some threads 
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	/**
	 * Return a HashMap of the currently connected clients
	 * @return
	 */
	public HashMap<String, ClientConnection> getConnectedClients() {
		return this.connectedClients;
	}
	
	

}
