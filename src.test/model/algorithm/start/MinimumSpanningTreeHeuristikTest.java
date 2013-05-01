package model.algorithm.start;

import junit.framework.Assert;
import model.algorithm.Path;
import model.grid.Edge;
import model.grid.Grid;
import model.grid.GridFactory;
import model.grid.Node;

import org.junit.Before;
import org.junit.Test;

public class MinimumSpanningTreeHeuristikTest {

	private Grid grid;

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

	private AStartAlgorithm algorithm;

	@Before
	public void setUp() {
		this.grid = new Grid();

		this.nodeNorth = GridFactory.createNode(3, 1);
		this.nodeEast = GridFactory.createNode(4, 2);
		this.nodeSouth = GridFactory.createNode(1, 3);
		this.nodeWest = GridFactory.createNode(2, 4);

		this.grid.addNode(this.nodeNorth);
		this.grid.addNode(this.nodeEast);
		this.grid.addNode(this.nodeSouth);
		this.grid.addNode(this.nodeWest);
		this.grid.setStartingNode(nodeNorth);

		this.edgeNorthEast = GridFactory.getEdge(this.nodeNorth, this.nodeEast);
		this.edgeNorthSouth = GridFactory.getEdge(this.nodeNorth, this.nodeSouth);
		this.edgeNorthWest = GridFactory.getEdge(this.nodeNorth, this.nodeWest);
		this.edgeEastSouth = GridFactory.getEdge(this.nodeEast, this.nodeSouth);
		this.edgeEastWest = GridFactory.getEdge(this.nodeEast, this.nodeWest);
		this.edgeSouthWest = GridFactory.getEdge(this.nodeSouth, this.nodeWest);

		this.algorithm = new MinimumSpanningTreeHeuristik(this.grid);
	}

	@Test
	public void test() {
		Path currentPath = this.algorithm.getCurrentPath();

		// Make sure that we can not take a step yet
		Assert.assertFalse(this.algorithm.step());

		// Validate the arguments
		this.algorithm.validateArguments();
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessful());

		// Check if we have four edges
		Assert.assertEquals(4, currentPath.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(currentPath.containsEdge(this.edgeNorthEast));
		Assert.assertTrue(currentPath.containsEdge(this.edgeEastSouth));
		Assert.assertTrue(currentPath.containsEdge(this.edgeSouthWest));
		Assert.assertTrue(currentPath.containsEdge(this.edgeNorthWest));

		// Check if these edges are not part of the path
		Assert.assertFalse(currentPath.containsEdge(this.edgeNorthSouth));
		Assert.assertFalse(currentPath.containsEdge(this.edgeEastWest));
	}
}
