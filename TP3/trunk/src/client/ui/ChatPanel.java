package client.ui;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel{
	public ChatPanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		
		add(new JLabel("Chat Panel"));
	}
}
