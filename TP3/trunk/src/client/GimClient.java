package client;

import client.ui.*;

public class GimClient {
	public static void main(String[] args) {
		GimUI gui = new GimUI();
		Thread g = new Thread(gui);
		g.start();
	}
}
