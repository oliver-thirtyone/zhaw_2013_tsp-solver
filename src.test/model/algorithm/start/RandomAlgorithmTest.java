package model.algorithm.start;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import model.grid.Edge;
import model.grid.Node;
import model.grid.Path;

import org.junit.Before;
import org.junit.Test;

public class RandomAlgorithmTest {

	private Node nodeNorth;
	private Node nodeEast;
	private Node nodeSouth;
	private Node nodeWest;

	private Set<Node> nodes;

	private AStartAlgorithm algorithm;

	@Before
	public void setUp() {
		this.nodeNorth = new Node(0, 5);
		this.nodeEast = new Node(5, 0);
		this.nodeSouth = new Node(0, -5);
		this.nodeWest = new Node(-5, 0);

		this.nodes = new HashSet<Node>();
		this.nodes.add(this.nodeNorth);
		this.nodes.add(this.nodeEast);
		this.nodes.add(this.nodeSouth);
		this.nodes.add(this.nodeWest);

		new Edge(this.nodeNorth, this.nodeEast);
		new Edge(this.nodeNorth, this.nodeSouth);
		new Edge(this.nodeNorth, this.nodeWest);
		new Edge(this.nodeEast, this.nodeSouth);
		new Edge(this.nodeEast, this.nodeWest);
		new Edge(this.nodeSouth, this.nodeWest);

		this.algorithm = new RandomAlgorithm(this.nodes, this.nodeNorth);
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

		// DEBUG OUTPUT
		System.out.println("Path: ");
		for (Edge edge : currentPath.getEdges()) {
			Node n1 = edge.getFirstNode();
			Node n2 = edge.getSecondNode();
			System.out.println("(" + n1.getX() + "," + n1.getY() + ")" + " -> " + "(" + n2.getX() + "," + n2.getY() + ")");
		}
		System.out.println();

		// Check if we have four edges
		Assert.assertEquals(4, currentPath.getNumberOfEdges());
	}
}
