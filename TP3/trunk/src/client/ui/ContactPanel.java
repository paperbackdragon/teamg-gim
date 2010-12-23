package client.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import client.GimClient;

@SuppressWarnings("serial")
public class ContactPanel extends JPanel {
	private GimUI parent;
	private JButton add, del, chat, group;
	private JTree contactTree;
	
	//CONSTRUCTOR
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
	
	//PANELS
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
			contactTree = new JTree(contacts);
			contactTree.addMouseListener(new SingleChatListener());
			
			//expand all nodes as default
			for(int i=0; i < contactTree.getRowCount(); i++)
				contactTree.expandRow(i);
			
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
			
			ButtonListener buttonListener = new ButtonListener();
			add.addActionListener(buttonListener);
			del.addActionListener(buttonListener);
			chat.addActionListener(buttonListener);
			group.addActionListener(buttonListener);
			
			add(add);
			add(del);
			add(chat);
			add(group);	
		}
	}
	
	//HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}
	
	public void setParent(JFrame frame) {
		parent = (GimUI) frame;
	}
	
	public void createNodes(DefaultMutableTreeNode root) {
		// TODO change node icons (see java tutorial)
		// TODO make two separate trees
		
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
	
	private String[] getSelectedContacts() {
		TreePath[] nodes = contactTree.getSelectionPaths();
		DefaultMutableTreeNode node;
		String[] contacts = new String[nodes.length];
		for(int i=0; i < nodes.length; i++) {
			node = (DefaultMutableTreeNode) nodes[i].getLastPathComponent();
			//contacts[i] = (String) node.getUserObject();
			System.out.println((String) node.getUserObject());
		}
		return contacts;
	}
	
	//ACTION LISTENERS
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(add)) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						GimUI ui = new GimUI("GIM - Find Contact", new FindPopup());
						ui.setLocationRelativeTo(null);//center new chat window
					}
				});
			}
			else if(e.getSource().equals(del)) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						GimUI ui = new GimUI("GIM - Remove Contact", new RemovePopup());
						ui.setLocationRelativeTo(null);//center new chat window
					}
				});
			}
			//TODO make chat grayed out till someone is clicked
			else if(e.getSource().equals(chat)) {
				//TODO how to make anything but nodes un-selectable?
				GimClient.getClient().createRoom(false, getSelectedContacts());
			}
			else if(e.getSource().equals(group)) {
				GimClient.getClient().createRoom(true, getSelectedContacts());
			}
		}
	}
	
	class SingleChatListener implements MouseListener {
		public void mousePressed(MouseEvent e) {
			//TODO only do below if contact is clicked
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) contactTree.getLastSelectedPathComponent();
			String nodeInfo = (String) node.getUserObject();
			
			if(e.getClickCount() == 2) {
				System.out.println(nodeInfo);
			}
		}
		
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}