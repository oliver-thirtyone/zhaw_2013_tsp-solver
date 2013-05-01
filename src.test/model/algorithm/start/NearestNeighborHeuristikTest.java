package model.algorithm.start;

import junit.framework.Assert;
import model.algorithm.Path;
import model.grid.Edge;
import model.grid.Grid;
import model.grid.GridFactory;
import model.grid.Node;

import org.junit.Before;
import org.junit.Test;

public class NearestNeighborHeuristikTest {

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

		this.nodeNorth = GridFactory.createNode(0, 5);
		this.nodeEast = GridFactory.createNode(4, 0); // Nearest node to the node in the north
		this.nodeSouth = GridFactory.createNode(0, -5);
		this.nodeWest = GridFactory.createNode(-5, 0);

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

		this.algorithm = new NearestNeighborHeuristik(this.grid);
	}

	@Test
	public void test() throws InterruptedException {
		Path currentPath = this.algorithm.getCurrentPath();

		// Make sure that we can not take a step yet
		Assert.assertFalse(this.algorithm.step());

		// Validate the arguments
		this.algorithm.validateArguments();
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Take the first step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessful());
		Assert.assertTrue(currentPath.containsEdge(this.edgeNorthEast));

		// Take the second step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessful());
		Assert.assertTrue(currentPath.containsEdge(this.edgeEastSouth));

		// Take the third step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessful());
		Assert.assertTrue(currentPath.containsEdge(this.edgeSouthWest));

		// Take the fourth step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertTrue(this.algorithm.hasFinishedSuccessful());
		Assert.assertTrue(currentPath.containsEdge(this.edgeNorthWest));

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if these edges are not part of the path
		Assert.assertFalse(currentPath.containsEdge(this.edgeNorthSouth));
		Assert.assertFalse(currentPath.containsEdge(this.edgeEastWest));
	}
}
