package model.algorithm.start;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import model.grid.Edge;
import model.grid.Node;
import model.grid.Path;

import org.junit.Before;
import org.junit.Test;

public class BruteForceTest {

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

	private Set<Node> nodes;

	private StartAlgorithm algorithm;

	@Before
	public void setUp() {
		this.nodeNorth = new Node(0, 5);
		this.nodeEast = new Node(4, 0); // Nearest node to the node in the north
		this.nodeSouth = new Node(0, -5);
		this.nodeWest = new Node(-5, 0);

		this.nodes = new HashSet<Node>();
		this.nodes.add(this.nodeNorth);
		this.nodes.add(this.nodeEast);
		this.nodes.add(this.nodeSouth);
		this.nodes.add(this.nodeWest);

		this.edgeNorthEast = new Edge(this.nodeNorth, this.nodeEast);
		this.edgeNorthSouth = new Edge(this.nodeNorth, this.nodeSouth);
		this.edgeNorthWest = new Edge(this.nodeNorth, this.nodeWest);
		this.edgeEastSouth = new Edge(this.nodeEast, this.nodeSouth);
		this.edgeEastWest = new Edge(this.nodeEast, this.nodeWest);
		this.edgeSouthWest = new Edge(this.nodeSouth, this.nodeWest);

		this.algorithm = new BruteForce(this.nodes, this.nodeNorth);
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
		int stepCounter = 0;
		do {
			Assert.assertTrue(this.algorithm.step());
			stepCounter++;
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

		// Check if we tested all possibilities
		Assert.assertEquals(16 / 2, stepCounter);
	}
}
