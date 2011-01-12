// CLASS NO LONGER NEEDED //
package client.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class RemovePopup extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a remove dialog box used to delete a contact
	 */
	public RemovePopup() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		setPreferredSize(new Dimension(350, 100));
		final JOptionPane optionPane = new JOptionPane("Would you like to remove\n CONTACT NAME?",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION);
		add(optionPane);
	}
}

class Selection implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
	}
}
