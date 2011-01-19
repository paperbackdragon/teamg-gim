package server;

import java.io.IOException;
import java.util.Scanner;

import util.Command;

public class Controller implements Runnable {

	private Data data = Data.getInstance();

	@Override
	public void run() {

		Scanner input = new Scanner(System.in);

		while (input.hasNextLine()) {

			String line = input.nextLine();

			if (line.startsWith("quit")) {
				try {

					/**
					 * Disconnect any connected users
					 */
					for (User user : data.getUsers()) {
						if (user.getWorker() != null)
							user.getWorker().quit();
					}

					/**
					 * Close the socket, end the server
					 */
					data.serverSocket.close();
					break;

				} catch (IOException e) {
				}

			} else if (line.startsWith("broadcast")) {

				String[] message = line.split(" ", 2);
				for (User user : data.getUsers()) {
					if (user.getWorker() != null)
						user.getWorker().putResponse(new Command("BROADCAST", message[1]));
				}

			} else {
				System.out.println("Unrecognised command!");
			}

		}

	}
}
