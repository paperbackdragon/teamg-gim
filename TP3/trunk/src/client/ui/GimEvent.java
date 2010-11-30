package client.ui;

import java.awt.event.*;
import javax.swing.*;

public class GimEvent implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(ContactPanel.getButton("add"))) {
			System.out.println("add clicked.");
		}
		else if(e.getSource().equals(ContactPanel.getButton("del"))) {
			System.out.println("del clicked.");
		}
		else if(e.getSource().equals(ContactPanel.getButton("chat"))) {
			System.out.println("chat clicked.");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new GimUI(new ChatPanel());
				}
			});
		}
		else if(e.getSource().equals(ContactPanel.getButton("group"))) {
			System.out.println("group clicked.");
		}
		else if(e.getSource().equals(GimUI.getMenuItem("logout"))) {
			System.out.println("logout clicked.");
		}
		else if(e.getSource().equals(GimUI.getMenuItem("quit"))) {
			System.out.println("quit clicked.");
		}
		else if(e.getSource().equals(GimUI.getMenuItem("setOptions"))) {
			System.out.println("setOptions clicked.");
		}
	}
}
