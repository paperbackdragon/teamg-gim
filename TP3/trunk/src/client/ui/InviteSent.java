package client.ui;

import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class InviteSent extends JPanel {

	public InviteSent() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		setPreferredSize(new Dimension(350, 100));
		final JOptionPane optionPane = new JOptionPane("An invite has been sent to\n CONTACT NAME",JOptionPane.PLAIN_MESSAGE);
		add(optionPane);
	}
	
}
	
