package client;

import javax.swing.SwingUtilities;
import client.ui.*;

public class GimClient {
	private static GimUI mainWindow;
	
	public static GimUI getMainWindow() {
		return mainWindow;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginPanel lp = new LoginPanel();
				mainWindow = new GimUI("GIM", lp);
				lp.setParent(mainWindow);
			}
		});
	}
}
