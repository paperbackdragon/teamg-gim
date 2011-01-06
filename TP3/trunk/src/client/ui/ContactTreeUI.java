package client.ui;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.AbstractLayoutCache;

public class ContactTreeUI extends BasicTreeUI {
	
	

	@Override
	protected void installListeners() {
		super.installListeners();

		tree.addComponentListener(componentListener);
	}

	@Override
	protected void uninstallListeners() {
		tree.removeComponentListener(componentListener);

		super.uninstallListeners();
	}

	@Override
	protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
		return new NodeDimensionsHandler() {
			@Override
			public Rectangle getNodeDimensions(Object value, int row, int depth, boolean expanded, Rectangle size) {
				Rectangle dimensions = super.getNodeDimensions(value, row, depth, expanded, size);
				dimensions.width = tree.getWidth() - getRowX(row, depth);
				return dimensions;
			}
		};
	}
	
/*
	@Override
	protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
		// do nothing.
	}

	@Override
	protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom) {
		// do nothing.
	}
*/
	
	private final ComponentListener componentListener = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			treeState.invalidateSizes();
			tree.repaint();
		};
	};

}
