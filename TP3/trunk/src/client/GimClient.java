package client;

import javax.swing.SwingUtilities;
import client.ui.*;

public class GimClient {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GimUI("GIM", new ContactPanel()); 
			}
		});
	}
}
