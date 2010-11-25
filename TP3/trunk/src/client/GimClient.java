package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GimClient extends JFrame{
	public GimClient() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		
		setTitle("GIM");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel main = new MainWindow();
		
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu optionMenu = new JMenu("Options");
		JMenu helpMenu = new JMenu("Help");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		optionMenu.setMnemonic(KeyEvent.VK_O);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem logout = new JMenuItem("Logout");
		JMenuItem quit = new JMenuItem("Quit");
		logout.setMnemonic(KeyEvent.VK_L);
		quit.setMnemonic(KeyEvent.VK_Q);
		fileMenu.add(logout);
		fileMenu.add(quit);
		
		JMenuItem setOptions = new JMenuItem("Set Options...");
		optionMenu.add(setOptions);
		
		menu.add(fileMenu);
		menu.add(optionMenu);
		menu.add(helpMenu);
		
		setJMenuBar(menu);
		setContentPane(main);
		pack();
		setVisible(true);
	}
	
	class MainWindow extends JPanel {
		class PersonalInfo extends JPanel {
			class TextField extends JPanel {
				public TextField() {
					setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
					JLabel name = new JLabel("<html><b>Nickname</b></html>");
					JLabel message = new JLabel("Personal Message");
					JLabel status = new JLabel("<html><font size=\"3\">Status: Online</font></html>");
					add(name);
					add(message);
					add(status);
				}
			}
			
			public PersonalInfo() {
				setLayout(new FlowLayout(FlowLayout.LEFT));
				ImageIcon icon = createImageIcon("images/icon1.jpg", "Icon");
				JLabel iconLabel = new JLabel(icon);
				iconLabel.setPreferredSize(new Dimension(50, 50));
				iconLabel.setBorder(BorderFactory.createLineBorder(Color.black));
				add(iconLabel);
				add(new TextField());
			}
			
			protected ImageIcon createImageIcon(String path, String description) {
				java.net.URL imgURL = getClass().getResource(path);
				if (imgURL != null) {
					return new ImageIcon(imgURL, description);
				} else {
					System.err.println("Couldn't find file: " + path);
					return null;
				}
			}
		}
		
		class ContactList extends JPanel {
			public ContactList() {
				setLayout(new BorderLayout());
				//JTree thing instead of JList??
				String[] contacts = {"<html><b>Online +</b><html>", "     Contact One", "     Contact Two", "     Contact Three", "<html><b>Offline -</b><html>", "     Contact Four"};
				JList myList = new JList(contacts);
				myList.setBorder(BorderFactory.createLineBorder(Color.black));
				add(myList, BorderLayout.CENTER);
			}
		}
		
		class ButtonPanel extends JPanel {
			public ButtonPanel() {
				setLayout(new GridLayout(1,4,5,5));
				JButton addContact = new JButton("ADD");
				JButton delContact = new JButton("DEL");
				JButton chat = new JButton("CHAT");
				JButton groupChat = new JButton("GROUP");
				
				add(addContact);
				add(delContact);
				add(chat);
				add(groupChat);	
			}
		}
		
		public MainWindow() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(new PersonalInfo());
			add(new ContactList());
			add(new ButtonPanel());
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(300, 400);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GimClient(); 
			}
		});
	}
}
