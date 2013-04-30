package model.algorithm.start;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import model.grid.Edge;
import model.grid.Node;
import model.grid.Path;

import org.junit.Before;
import org.junit.Test;

public class NearestNeighborHeuristikTest {

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

		this.algorithm = new NearestNeighborHeuristik(this.nodes, this.nodeNorth);
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
