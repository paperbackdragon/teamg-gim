package client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
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

public class ContactListRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	public HashMap<String, ImageIcon> icons = new HashMap<String, ImageIcon>();
	private int lwidth = 0;

	Model model = Model.getInstance();

	public ContactListRenderer() {
		for (Status s : Status.values()) {
			ImageIcon statusIcon = new ImageIcon(model.getPath() + "status/" + s.toString().toLowerCase() + ".png",
					"Icon");
			icons.put(s.toString().toLowerCase(), statusIcon);
		}
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		
		if(this.lwidth == 0)
			this.lwidth = list.getWidth();

		return renderContact(list, value, index, isSelected, cellHasFocus);

	}

	private Component renderContact(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		User user = (User) value;

		setPreferredSize(list.getSize());

		JPanel contact = new JPanel(new BorderLayout(5, 0));
		contact.setOpaque(false);

		ImageIcon displayPictureIcon = user.getDisplayPic(32, 32);
		JLabel displayPicture = new JLabel(displayPictureIcon);
		displayPicture.setPreferredSize(new Dimension(32, 32));
		displayPicture.setIconTextGap(0);

		ImageIcon statusIcon = this.icons.get(user.getStatus());

		JLabel statusIconLabel = new JLabel(statusIcon);
		statusIconLabel.setPreferredSize(new Dimension(16, 16));

		JPanel userInfo = new JPanel(new GridLayout(2, 1));
		userInfo.setOpaque(false);

		JLabel username = new JLabel("<html>" + Html.escape(user.getNickname()) + "</html>");
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
			contact.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, UIManager
					.getColor("List.selectionBackground")));
			contact.setOpaque(true);

			username.setForeground(UIManager.getColor("List.selectionForeground"));
			username.setBackground(UIManager.getColor("List.selectionBackground"));
			username.setOpaque(true);
			
			personalMessage.setForeground(UIManager.getColor("List.selectionForeground"));
			personalMessage.setBackground(UIManager.getColor("List.selectionBackground"));
			personalMessage.setOpaque(true);
		} else {
			username.setForeground(UIManager.getColor("List.Foreground"));
			contact.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, UIManager.getColor("List.background")));
		}

		contact.add(displayPicture, BorderLayout.WEST);
		contact.add(info, BorderLayout.CENTER);
		contact.add(statusIconLabel, BorderLayout.EAST);

		return contact;
	}

}
