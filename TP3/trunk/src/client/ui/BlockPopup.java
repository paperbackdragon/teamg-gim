package client.ui;

import java.awt.*;
import javax.swing.*;

public class BlockPopup extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a block dialog box used to block a contact
	 * 
	 */
	public BlockPopup() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		
		setPreferredSize(new Dimension(350, 100));
		
		final JOptionPane optionPane = new JOptionPane("Would you like to block\n CONTACT NAME?",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION);
		add(optionPane);
	}

}
