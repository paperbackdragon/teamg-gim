package server;

import java.net.Socket;
import java.util.HashMap;

public class ServerConnection implements Runnable {
	
	HashMap<String, ServerConnection> otherConnections = null;
	Socket socket = null;

	public ServerConnection(Socket socket, HashMap<String, ServerConnection> hashMap) {
		this.otherConnections = hashMap;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
	}

}
