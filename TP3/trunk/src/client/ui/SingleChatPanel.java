package client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import util.Html;

import client.User;
import client.UserChangedListener;

/**
 * Specific class for a chat panel used for a conversation between two people.
 */
public class SingleChatPanel extends ChatPanel {

	private static final long serialVersionUID = 1L;
	private User user;

	/**
	 * Constructor
	 * 
	 * @param roomID
	 *            The ID of the room
	 */
	public SingleChatPanel(User user, String roomID) {
		super(roomID);
		this.user = user;

		setLayout(new BorderLayout(5, 5));
		setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, getBackground()));

		JScrollPane messagePane = new JScrollPane(messages);

		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());

		chatBox = new JTextArea();
		chatBox.setEditable(true);
		chatBox.setLineWrap(true);
		chatBox.setWrapStyleWord(true);
		chatBox.setFont(chatPanel.getFont());

		EnterListener enterListener = new EnterListener();
		chatBox.addKeyListener(enterListener);
		JScrollPane chatPane = new JScrollPane(chatBox);
		chatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatPane.setPreferredSize(new Dimension(235, 50));

		send = new JButton("Send");
		send.setPreferredSize(new Dimension(65, 50));
		SendListener sendListener = new SendListener();
		send.addActionListener(sendListener);

		chatPanel.setPreferredSize(new Dimension(0, 50));
		chatPanel.add(chatPane, BorderLayout.CENTER);
		chatPanel.add(send, BorderLayout.EAST);

		add(new ContactInfo(), BorderLayout.NORTH);
		add(messagePane, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.SOUTH);

	}

	// PANELS
	class ContactInfo extends JPanel {

		private static final long serialVersionUID = 1L;

		/**
		 * A panel containing the information of the contact
		 */
		public ContactInfo() {

			setLayout(new BorderLayout(5, 0));
			setOpaque(false);

			ImageIcon displayPictureIcon = user.getDisplayPic(48, 48);
			final JLabel displayPicture = new JLabel(displayPictureIcon);
			displayPicture.setPreferredSize(new Dimension(48, 48));
			displayPicture.setBorder(BorderFactory.createLineBorder(UIManager.getColor("controlShadow")));
			displayPicture.setIconTextGap(0);

			final ImageIcon statusIcon = new ImageIcon(model.getPath() + "status/"
					+ user.getStatus().toString().toLowerCase() + ".png", "Icon");

			JLabel statusIconLabel = new JLabel(statusIcon);
			statusIconLabel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, getBackground()));
			statusIconLabel.setPreferredSize(new Dimension(26, 26));

			JPanel userInfo = new JPanel(new GridLayout(2, 1));
			userInfo.setOpaque(false);

			final JLabel username = new JLabel("<html>" + Html.escape(user.getNickname()) + "</html>");
			final JLabel personalMessage = new JLabel("<html>" + Html.escape(user.getPersonalMessage()) + "</html>");

			userInfo.add(username);
			userInfo.add(personalMessage);

			add(displayPicture, BorderLayout.WEST);
			add(userInfo, BorderLayout.CENTER);
			add(statusIconLabel, BorderLayout.EAST);

			user.addUserChangedListener(new UserChangedListener() {
				@Override
				public void statusChanged() {
					statusIcon.setImage(new ImageIcon(model.getPath() + "status/"
							+ user.getStatus().toString().toLowerCase() + ".png", "Icon").getImage());
				}

				@Override
				public void personalMessageChanged() {
					personalMessage.setText("<html>" + Html.escape(user.getPersonalMessage()) + "</html>");
				}

				@Override
				public void nicknameChanged() {
					username.setText("<html>" + Html.escape(user.getNickname()) + "</html>");
				}

				@Override
				public void displayPicChanged() {
					displayPicture.setIcon(user.getDisplayPic(displayPicture.getWidth(), displayPicture.getHeight()));
				}

				@Override
				public void changed() {
				}
			});

		}

	}

	public User getUser() {
		return this.user;
	}
}
