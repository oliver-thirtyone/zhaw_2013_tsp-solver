package tspsolver.view.grid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;

public class GridView extends JPanel implements Observer {

	private static final long serialVersionUID = -5210001067574218993L;

	private final Grid grid;

	public GridView(Grid grid) {
		this.grid = grid;

		this.grid.addObserver(this);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(Color.RED);

		for (Node node : grid.getNodes()) {
			graphics2D.drawOval(node.getX(), node.getY(), 5, 5);
		}
	}

	@Override
	public void update(Observable observable, Object argument) {
		// TODO
	}

}
