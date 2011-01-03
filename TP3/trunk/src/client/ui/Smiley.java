package client.ui;

import javax.swing.text.Style;

public class Smiley {
	
	public String text;
	public String icon;
	public Style style;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text.toUpperCase();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public Smiley(String text, String icon) {
		this.text = text;
		this.icon = icon;
	}
	
}
