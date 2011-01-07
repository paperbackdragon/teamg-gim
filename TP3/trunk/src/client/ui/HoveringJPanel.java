package client.ui;

import java.awt.CardLayout;

import javax.swing.JPanel;

/**
 * Create a JPanel with a CardLAyout which switches to another JPanel on hover
 * 
 * @author james
 * 
 */
public class HoveringJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel normal = null;
	private JPanel hover = null;

	HoveringJPanel() {
		super();
		this.setLayout(new CardLayout(0, 0));
	}

	public JPanel getNormal() {
		return normal;
	}

	public void setNormal(JPanel normal) {
		if (this.normal != null)
			this.remove(this.normal);

		this.normal = normal;
		this.add(normal, "NORMAL");

		CardLayout cl = (CardLayout) (this.getLayout());
		cl.show(this, "NORMAL");

	}

	public JPanel getHover() {
		return hover;
	}

	public void setHover(JPanel hover) {
		if (this.hover != null)
			this.remove(this.hover);

		this.hover = hover;
		this.add(hover, "HOVER");
	}

	public void setHoverState(boolean hover) {
		CardLayout cl = (CardLayout) (this.getLayout());

		if (hover)
			cl.show(this, "HOVER");
		else
			cl.show(this, "NORMAL");
	}

}
