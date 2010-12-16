package client.ui;

import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GimUI extends JFrame {
	private JMenuItem logout, quit, setOptions;
	private JPanel main;
	
	//CONSTRUCTOR
	public GimUI(String title, JPanel panel) {
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}*/
		
		setTitle(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMainPanel(panel);
		
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu optionMenu = new JMenu("Options");
		JMenu helpMenu = new JMenu("Help");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		optionMenu.setMnemonic(KeyEvent.VK_O);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		logout = new JMenuItem("Logout");
		quit = new JMenuItem("Quit");
		
		MenuListener menuListener = new MenuListener();
		logout.addActionListener(menuListener);
		quit.addActionListener(menuListener);
		logout.setMnemonic(KeyEvent.VK_L);
		quit.setMnemonic(KeyEvent.VK_Q);
		fileMenu.add(logout);
		fileMenu.add(quit);
		
		setOptions = new JMenuItem("Set Options...");
		setOptions.addActionListener(menuListener);
		optionMenu.add(setOptions);
		
		menu.add(fileMenu);
		menu.add(optionMenu);
		menu.add(helpMenu);
		
		setJMenuBar(menu);
		setContentPane(main);
		pack();
		setVisible(true);
	}
	
	//HELPER METHODS
	public void setMainPanel(JPanel newPanel) {
		main = newPanel;
		setContentPane(main);
		pack();
	}
	
	//ACTION LISTENERS
	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(logout)) {
				System.out.println("logout clicked.");
				LoginPanel panel = new LoginPanel();
				panel.setParent(GimUI.this);
				setMainPanel(panel);
			}
			else if(e.getSource().equals(quit)) {
				System.out.println("quit clicked.");
				System.exit(0);
			}
			else if(e.getSource().equals(setOptions)) {
				System.out.println("setOptions clicked.");
			}
		}
	}
}
