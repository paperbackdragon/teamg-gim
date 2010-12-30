package client.ui;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
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
		optionPane.addPropertyChangeListener(
			    new PropertyChangeListener() {
			        public void propertyChange(PropertyChangeEvent e) {
			            String prop = e.getPropertyName();

			            if (optionPane.isVisible() 
			             && (e.getSource() == optionPane)
			             && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
			                //If you were going to check something
			                //before closing the window, you'd do
			                //it here.
			            	System.out.println("OK clicked");
			                
			            }
			        }
			    });

	}
}
	
