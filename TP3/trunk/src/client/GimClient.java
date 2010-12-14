package client;

import javax.swing.SwingUtilities;
import client.ui.*;

public class GimClient {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginPanel lp = new LoginPanel();
				GimUI gui = new GimUI("GIM", lp);
				lp.setParent(gui);
			}
		});
	}
}
