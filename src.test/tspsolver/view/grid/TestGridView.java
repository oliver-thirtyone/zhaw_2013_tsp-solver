package tspsolver.view.grid;

import java.awt.GridBagConstraints;

import javax.swing.JFrame;

import tspsolver.model.grid.Grid;
import tspsolver.model.grid.GridFactory;
import tspsolver.model.grid.Node;
import tspsolver.util.LayoutManager;

public class TestGridView extends JFrame {

	private static final long serialVersionUID = 4966251849003014687L;

	private final LayoutManager layoutManager;

	private final GridView gridView;

	public TestGridView(Grid grid) {
		super("TestGridView");

		this.layoutManager = new LayoutManager(this.getContentPane());

		this.gridView = new GridView(grid);

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

	public static void main(String[] args) {
		Grid grid = new Grid();

		Node nodeNorth = GridFactory.createNode(50, 5);
		Node nodeEast = GridFactory.createNode(90, 50);
		Node nodeSouth = GridFactory.createNode(50, 100);
		Node nodeWest = GridFactory.createNode(5, 50);

		grid.addNode(nodeNorth);
		grid.addNode(nodeEast);
		grid.addNode(nodeSouth);
		grid.addNode(nodeWest);
		grid.setStartingNode(nodeNorth);

		TestGridView testGridView = new TestGridView(grid);
		testGridView.setSize(300, 300);
		testGridView.setLocationRelativeTo(null);
		testGridView.setVisible(true);
	}

}
