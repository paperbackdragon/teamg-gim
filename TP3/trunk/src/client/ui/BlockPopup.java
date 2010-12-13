package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BlockPopup extends JPanel{

	//CONSTRUCTOR
	public BlockPopup() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		setPreferredSize(new Dimension(350, 100));
		final JOptionPane optionPane = new JOptionPane("Would you like to block\n CONTACT NAME?",JOptionPane.PLAIN_MESSAGE,JOptionPane.YES_NO_OPTION);
		add(optionPane);
	}
	
}
