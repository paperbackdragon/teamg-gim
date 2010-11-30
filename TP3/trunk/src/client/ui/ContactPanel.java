package client.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class ContactPanel extends JPanel {
	private static JButton add, del, chat, group;
	
	public static JButton getButton(String str) {
		if(str.equals("add"))
			return add;
		else if(str.equals("del"))
			return del;
		else if(str.equals("chat"))
			return chat;
		else if(str.equals("group"))
			return group;
		else
			return null;
	}
	
	public ContactPanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new PersonalInfo());
		add(new ContactList());
		add(new ButtonPanel());
	}
	
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
			ImageIcon icon = createImageIcon("icon1.jpg", "Icon");
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
			
			//assuming we are grouping by online/offline status:
			DefaultMutableTreeNode contacts = new DefaultMutableTreeNode("Contacts");
			createNodes(contacts);
			JTree contactTree = new JTree(contacts);
			
			add(contactTree, BorderLayout.CENTER);
		}
	}
	
	class ButtonPanel extends JPanel {
		public ButtonPanel() {
			setLayout(new GridLayout(1,4,5,5));
			add = new JButton("ADD");
			del = new JButton("DEL");
			chat = new JButton("CHAT");
			group = new JButton("GROUP");
			
			add.addActionListener(GimUI.getHandler());
			del.addActionListener(GimUI.getHandler());
			chat.addActionListener(GimUI.getHandler());
			group.addActionListener(GimUI.getHandler());
			
			add(add);
			add(del);
			add(chat);
			add(group);	
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}
	
	private void createNodes(DefaultMutableTreeNode root) {
		DefaultMutableTreeNode status = null;
		DefaultMutableTreeNode contact = null;
		
		status = new DefaultMutableTreeNode("Online");
		root.add(status);
		
		//get contacts somehow
		contact = new DefaultMutableTreeNode("Contact One");
	    status.add(contact);
	    contact = new DefaultMutableTreeNode("Contact Two");
	    status.add(contact);
	    
	    status = new DefaultMutableTreeNode("Offline");
	    root.add(status);
	    
	    //get contacts somehow
	    contact = new DefaultMutableTreeNode("Contact Three");
	    status.add(contact);
	    contact = new DefaultMutableTreeNode("Contact Four");
	    status.add(contact);
	}
}