package client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import util.Html;
import client.Model;
import client.User;
import client.User.Status;

public class ContactListRenderer extends JPanel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	public HashMap<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

	Model model = Model.getInstance();

	public ContactListRenderer() {
		for (Status s : Status.values()) {
			ImageIcon statusIcon = new ImageIcon(model.getPath() + "status/" + s.toString().toLowerCase() + ".png",
					"Icon");
			icons.put(s.toString().toLowerCase(), statusIcon);
		}
		
		ImageIcon statusIcon = new ImageIcon(model.getPath() + "status/blocked.png", "Icon");
		icons.put("blocked", statusIcon);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		User user = (User) value;
		setPreferredSize(list.getSize());

		JPanel contact = new JPanel(new BorderLayout(5, 0));
		contact.setOpaque(false);

		// Gray out user icons when they're offline
		Image displayPictureIcon = user.getDisplayPic(32, 32).getImage();
		if (user.getStatus().equalsIgnoreCase("offline") || model.getFriendList().isBlocked(user.getEmail())) {
			
			// TODO: If this works it's a hack. Find out why.
			model.getServer().getStatus(user.getEmail());
			
			Image image = new BufferedImage(32, 32, BufferedImage.TYPE_BYTE_GRAY);
			Graphics g = image.getGraphics();
			g.drawImage(displayPictureIcon, 0, 0, null);
			g.dispose();
			displayPictureIcon = image;
		}

		JLabel displayPicture = new JLabel(new ImageIcon(displayPictureIcon));
		displayPicture.setPreferredSize(new Dimension(32, 32));
		displayPicture.setIconTextGap(0);

		// Set their status icon
		String status, blocked = "";
		if (model.getFriendList().isBlocked(user.getEmail())) {
			status = "blocked";
			blocked = "<i>(blocked)</i>";
		} else {
			status = user.getStatus();
		}

		ImageIcon statusIcon = this.icons.get(status.toLowerCase());
		JLabel statusIconLabel = new JLabel(statusIcon);
		statusIconLabel.setPreferredSize(new Dimension(16, 16));

		JPanel userInfo = new JPanel(new GridLayout(2, 1));
		userInfo.setOpaque(false);

		JLabel username = new JLabel("<html> " + blocked + " " + Html.escape(user.getNickname()) + "</html>");
		JLabel personalMessage = new JLabel("<html><small>" + Html.escape(user.getPersonalMessage())
				+ "</small></html>");

		userInfo.add(username);
		userInfo.add(personalMessage);

		JScrollPane info = new JScrollPane(userInfo, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		info.setPreferredSize(new Dimension(-1, contact.getHeight()));
		info.setBorder(null);
		info.setViewportBorder(null);

		if (isSelected) {
			contact.setBackground(UIManager.getColor("List.selectionBackground"));
			contact.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 5, UIManager
					.getColor("List.selectionBackground")));
			contact.setOpaque(true);

			username.setForeground(UIManager.getColor("List.selectionForeground"));
			username.setBackground(UIManager.getColor("List.selectionBackground"));
			username.setOpaque(true);

			personalMessage.setForeground(UIManager.getColor("List.selectionForeground"));
			personalMessage.setBackground(UIManager.getColor("List.selectionBackground"));
			personalMessage.setOpaque(true);
		} else {
			username.setBackground(UIManager.getColor("EditorPane.background"));
			username.setOpaque(true);
			
			personalMessage.setBackground(UIManager.getColor("EditorPane.background"));
			personalMessage.setOpaque(true);
			
			contact.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 5, UIManager.getColor("EditorPane.background")));
		}

		contact.add(displayPicture, BorderLayout.WEST);
		contact.add(info, BorderLayout.CENTER);
		contact.add(statusIconLabel, BorderLayout.EAST);
		
		contact.setToolTipText("Email: " + user.getEmail());
		
		return contact;
	}

	
}
