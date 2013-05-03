package tspsolver.view.grid;

import java.awt.GridBagConstraints;

import javax.swing.JFrame;

import tspsolver.model.grid.Grid;
import tspsolver.model.grid.GridFactory;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;
import tspsolver.model.path.PathUpdater;
import tspsolver.util.LayoutManager;

public class TestGridView extends JFrame {

	private static final long serialVersionUID = 4966251849003014687L;

	private final LayoutManager layoutManager;

	private final GridView gridView;

	public TestGridView(Grid grid, Path path) {
		super("TestGridView");

		this.layoutManager = new LayoutManager(this.getContentPane());

		this.gridView = new GridView(grid, path);

		this.components();
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void components() {
		this.layoutManager.reset();
		this.layoutManager.setAnchor(GridBagConstraints.CENTER);
		this.layoutManager.setFill(GridBagConstraints.BOTH);
		this.layoutManager.setWeightX(1.0).setWeightY(1.0);

		this.layoutManager.setX(0).setY(0).addComponent(this.gridView);
	}

	public static void main(String[] args) throws InterruptedException {
		Grid grid = new Grid();
		Path path = new Path();
		PathUpdater pathUpdater = new PathUpdater(path);

		Node nodeNorth = GridFactory.createNode(350, 25);
		Node nodeEast = GridFactory.createNode(550, 250);
		Node nodeSouth = GridFactory.createNode(350, 500);
		Node nodeWest = GridFactory.createNode(125, 250);

		TestGridView testGridView = new TestGridView(grid, path);

		grid.addNode(nodeNorth);
		grid.addNode(nodeEast);
		grid.addNode(nodeSouth);
		grid.addNode(nodeWest);
		grid.setStartingNode(nodeNorth);

		testGridView.setSize(800, 600);
		testGridView.setLocationRelativeTo(null);
		testGridView.setVisible(true);

		// Add an edge to the path
		pathUpdater.addEdge(GridFactory.getEdge(nodeNorth, nodeEast));
		pathUpdater.updatePath();

		// Sleep two seconds
		Thread.sleep(2000);

		// Add two edges to the path
		pathUpdater.addEdge(GridFactory.getEdge(nodeEast, nodeSouth));
		pathUpdater.addEdge(GridFactory.getEdge(nodeSouth, nodeNorth));
		pathUpdater.updatePath();

		// Sleep two seconds
		Thread.sleep(2000);

		// Complete the path
		pathUpdater.removeEdge(GridFactory.getEdge(nodeSouth, nodeNorth));
		pathUpdater.addEdge(GridFactory.getEdge(nodeSouth, nodeWest));
		pathUpdater.addEdge(GridFactory.getEdge(nodeWest, nodeNorth));
		pathUpdater.updatePath();
	}
}
