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
	private DefaultMutableTreeNode contacts;
	
	
	//CONSTRUCTOR
	public ContactPanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		
		contacts = new DefaultMutableTreeNode("Contacts");
	
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
			
			contactTree = new JTree(contacts);
		    contactTree.addMouseListener(new SingleChatListener());
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
	
	public void createNodes(String[] online, String[] offline) {
		//TODO (heather): change node icons (see java tutorial)
		//TODO (heather): make two separate trees
		
		System.out.println("got here");
		
		contacts.removeAllChildren();
		DefaultMutableTreeNode status = null;
		DefaultMutableTreeNode contact = null;
		
		status = new DefaultMutableTreeNode("Online");
		contacts.add(status);
		
		//set online contacts
		for (String str : online) {
			contact = new DefaultMutableTreeNode(str);
		    status.add(contact);
		}
	    
	    status = new DefaultMutableTreeNode("Offline");
	    contacts.add(status);
	    
	    //set offline contacts
	    for (String str : offline) {
			contact = new DefaultMutableTreeNode(str);
		    status.add(contact);
		}
	    
	}
	
	private String[] getSelectedContacts() {
		TreePath[] nodes = contactTree.getSelectionPaths();
		DefaultMutableTreeNode node;
		String[] contacts = new String[nodes.length];
		for(int i=0; i < nodes.length; i++) {
			node = (DefaultMutableTreeNode) nodes[i].getLastPathComponent();
			contacts[i] = (String) node.getUserObject();
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
			//TODO (heather): make chat grayed out till someone is clicked
			//TODO (heather): how to make anything but nodes un-selectable?
			else if(e.getSource().equals(chat)) {
				// Check if there's already a chat open with this user
				if (!GimClient.findRoom(getSelectedContacts()[0])) {
					GimClient.getClient().createRoom(false, getSelectedContacts());
				}
				else {
					//uhm...
				}
				
			}
			else if(e.getSource().equals(group)) {
				GimClient.getClient().createRoom(true, getSelectedContacts());
			}
		}
	}
	
	class SingleChatListener implements MouseListener {
		public void mousePressed(MouseEvent e) {
			//TODO (heather): only do below if contact is clicked
			//TODO (heather): what if someone already has a chat going with this person?
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) contactTree.getLastSelectedPathComponent();
			String nodeInfo = (String) node.getUserObject();
			
			if(e.getClickCount() == 2) {
				System.out.println(nodeInfo);
				GimClient.getClient().createRoom(false, new String[] {nodeInfo});
			}
		}
		
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}