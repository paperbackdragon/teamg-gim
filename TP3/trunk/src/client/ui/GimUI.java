package client.ui;

import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GimUI extends JFrame{
	private static GimEvent eventHandler = new GimEvent();
	private static JMenuItem logout, quit, setOptions;
	private static JPanel main;
	
	public static GimEvent getHandler() {
		return eventHandler;
	}
	
	public static JMenuItem getMenuItem(String str) {
		if(str.equals("logout"))
			return logout;
		else if(str.equals("quit"))
			return quit;
		else if(str.equals("setOptions"))
			return setOptions;
		else
			return null;
	}
	
	public GimUI(String title, JPanel panel) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		
		setTitle(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		main = panel;
		
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu optionMenu = new JMenu("Options");
		JMenu helpMenu = new JMenu("Help");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		optionMenu.setMnemonic(KeyEvent.VK_O);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		logout = new JMenuItem("Logout");
		quit = new JMenuItem("Quit");
		logout.addActionListener(eventHandler);
		quit.addActionListener(eventHandler);
		logout.setMnemonic(KeyEvent.VK_L);
		quit.setMnemonic(KeyEvent.VK_Q);
		fileMenu.add(logout);
		fileMenu.add(quit);
		
		setOptions = new JMenuItem("Set Options...");
		setOptions.addActionListener(eventHandler);
		optionMenu.add(setOptions);
		
		menu.add(fileMenu);
		menu.add(optionMenu);
		menu.add(helpMenu);
		
		setJMenuBar(menu);
		setContentPane(main);
		pack();
		setVisible(true);
	}
}
