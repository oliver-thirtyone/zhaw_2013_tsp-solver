package tspsolver.model.algorithm.optimizer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;
import tspsolver.model.validator.TSPValidator;

public class TwoOptHeuristikTest {

	private Scenario scenario;
	private Grid grid;
	private Path path;

	private Node nodeNorth;
	private Node nodeEast;
	private Node nodeSouth;
	private Node nodeWest;

	private Edge edgeNorthEast;
	private Edge edgeNorthSouth;
	private Edge edgeNorthWest;
	private Edge edgeEastSouth;
	private Edge edgeEastWest;
	private Edge edgeSouthWest;

	private OptimizerAlgorithm algorithm;

	@Before
	public void setUp() {
		this.scenario = new Scenario(new TSPValidator());
		this.grid = this.scenario.getGrid();
		this.path = this.scenario.getPath();

		this.nodeNorth = GridFactory.createNode("north", 4, 2);
		this.nodeEast = GridFactory.createNode("east", 6, 4);
		this.nodeSouth = GridFactory.createNode("south", 4, 6);
		this.nodeWest = GridFactory.createNode("west", 2, 4);

		GridFactory.addNode(this.grid, this.nodeNorth);
		GridFactory.addNode(this.grid, this.nodeEast);
		GridFactory.addNode(this.grid, this.nodeSouth);
		GridFactory.addNode(this.grid, this.nodeWest);

		this.scenario.setStartingNode(this.nodeNorth);

		this.edgeNorthEast = GridFactory.getEdge(this.nodeNorth, this.nodeEast);
		this.edgeNorthSouth = GridFactory.getEdge(this.nodeNorth, this.nodeSouth);
		this.edgeNorthWest = GridFactory.getEdge(this.nodeNorth, this.nodeWest);
		this.edgeEastSouth = GridFactory.getEdge(this.nodeEast, this.nodeSouth);
		this.edgeEastWest = GridFactory.getEdge(this.nodeEast, this.nodeWest);
		this.edgeSouthWest = GridFactory.getEdge(this.nodeSouth, this.nodeWest);

		this.algorithm = new TwoOptHeuristik();
	}

	@Test
	public void test() {
		// Create a valid path that is too heavy
		final PathUpdater pathUpdater = new PathUpdater(this.path);
		pathUpdater.addEdge(this.edgeNorthSouth);
		pathUpdater.addEdge(this.edgeNorthWest);
		pathUpdater.addEdge(this.edgeEastWest);
		pathUpdater.addEdge(this.edgeEastSouth);
		pathUpdater.updatePath();

		// Make sure that we can not take a step yet
		Assert.assertFalse(this.algorithm.step());

		// Initialize the algorithm
		this.algorithm.initialize(this.scenario);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// DEBUG OUTPUT
		// System.out.println("Path: ");
		// for (Edge edge : this.path.getEdges()) {
		// System.out.println(edge.toString());
		// }
		// System.out.println();

		// Check if we have four edges
		Assert.assertEquals(4, this.path.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(this.edgeNorthEast));
		Assert.assertTrue(this.path.containsEdge(this.edgeEastSouth));
		Assert.assertTrue(this.path.containsEdge(this.edgeSouthWest));
		Assert.assertTrue(this.path.containsEdge(this.edgeNorthWest));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(this.edgeNorthSouth));
		Assert.assertFalse(this.path.containsEdge(this.edgeEastWest));
	}
}
