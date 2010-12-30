package server;

import java.io.IOException;
import java.util.Scanner;

public class Controller implements Runnable {

	private Data data = Data.getInstance();
	
	@Override
	public void run() {
		
		Scanner input = new Scanner(System.in);
		
		while(input.hasNextLine()) {
			
			if(input.nextLine().startsWith("quit")) {
				try {
					
					/**
					 * Disconnect any connected users
					 */
					for(User user : data.getUsers()) {
						if(user.getWorker() != null)
							user.getWorker().quit();
					}
					
					/**
					 * Close the socket, end the server
					 */
					data.serverSocket.close();
					break;
					
				} catch (IOException e) {
				}
				
			} else {
				System.out.println("Unrecognised command!");
			}
			
		}
		
	}

}
