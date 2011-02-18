package client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import client.Model;
import client.User;
import client.UserChangedListener;

/**
 * Specific class for a chat panel used for a conversation between more than two
 * people.
 * 
 * @author Heather
 */
public class GroupChatPanel extends ChatPanel {

	private static final long serialVersionUID = 1L;

	public User[] participants;

	private final DefaultListModel listModel;

	private final JList list;

	private final UserChangedListener listener;
	private static Model model = Model.getInstance();

	/**
	 * Constructor
	 * 
	 * @param roomID
	 *            The ID of the room used for identification
	 */
	public GroupChatPanel(String roomID) {
		super(null, roomID);

		setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, getBackground()));
		setLayout(new BorderLayout(5, 5));
		JScrollPane messagePane = new JScrollPane(messages);

		listModel = new DefaultListModel();
		list = new JList(listModel);

		JPanel guestList = new JPanel(new BorderLayout());
		guestList.add(list, BorderLayout.CENTER);

		JScrollPane guestPane = new JScrollPane(guestList);
		guestPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// The listener we'll be using to keep track of changes to people in
		// the list
		listener = new UserChangedListener() {
			@Override
			public void statusChanged() {
			}

			@Override
			public void personalMessageChanged() {
			}

			@Override
			public void nicknameChanged() {
			}

			@Override
			public void displayPicChanged() {
			}

			@Override
			public void changed() {
				list.repaint();
			}
		};

		// Create and populate the list model.
		participants = new User[1];
		participants[0] = model.getSelf();
		for (User u : participants) {
			listModel.addElement(u);
			u.addUserChangedListener(listener);
		}

		// Make sure the list is rendered correctly
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ContactListRenderer renderer = new ContactListRenderer();
		list.setCellRenderer(renderer);

		// Add the listeners
		list.addMouseListener(new MListener());

		// Bottom Panel
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatBox.setFont(chatPanel.getFont());
		EnterListener enterListener = new EnterListener();
		chatBox.addKeyListener(enterListener);
		JScrollPane chatPane = new JScrollPane(chatBox);
		chatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		send = new JButton("Send");
	

		send.setPreferredSize(new Dimension(65, 50));
		SendListener sendListener = new SendListener();
		send.addActionListener(sendListener);

		chatPanel.setPreferredSize(new Dimension(0, 50));
		chatPanel.add(chatPane, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new BorderLayout(0, 0));
		buttons.add(send, BorderLayout.EAST);
		chatPanel.add(buttons, BorderLayout.EAST);
		// END BOTTOM PANEL

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, messagePane, guestPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(1.0);

		splitPane.setDividerLocation(model.getOptions().chatWindowWidth - 210);

		add(splitPane, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.SOUTH);

	}

	public void updateUserList(User[] participants) {

		this.participants = participants;
		for (Object o : listModel.toArray()) {
			((User) o).removeUserChangedListener(listener);
		}

		// Create and populate the list model.
		DefaultListModel listModel = new DefaultListModel();
		for (User u : participants) {
			listModel.addElement(u);
			u.addUserChangedListener(listener);
		}

		list.setModel(listModel);
	}

	/**
	 * called when a user logs out, or is disconnected, while a group chat is
	 * open
	 */
	public void clearUserList() {
		list.setModel(new DefaultListModel());
		chatBox.setEnabled(false);
	}

	class MListener implements MouseListener {

		public void mousePressed(MouseEvent e) {
			// Look for right client events
			JList list = ((JList) e.getComponent());
			if (e.getButton() == MouseEvent.BUTTON3) {
				list.setSelectedIndex(list.locationToIndex(e.getPoint()));
				GroupChatContextMenu menu = new GroupChatContextMenu((User) list.getSelectedValue());
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

}
