package model.algorithm.start;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import junit.framework.Assert;
import model.grid.Edge;
import model.grid.Node;

import org.junit.Before;
import org.junit.Test;

public class MinimumSpanningTreeHeuristikTest {

	private AStartAlgorithm minimumSpanningTreeHeuristik;

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

	@Before
	public void setUp() {
		this.minimumSpanningTreeHeuristik = new MinimumSpanningTreeHeuristik();

		this.nodeNorth = new Node(3, 1);
		this.nodeEast = new Node(4, 2); 
		this.nodeSouth = new Node(1, 3);
		this.nodeWest = new Node(2, 4);

		this.nodes = new HashSet<Node>();
		this.nodes.add(nodeNorth);
		this.nodes.add(nodeEast);
		this.nodes.add(nodeSouth);
		this.nodes.add(nodeWest);

		this.edgeNorthEast = new Edge(nodeNorth, nodeEast);
		this.edgeNorthSouth = new Edge(nodeNorth, nodeSouth);
		this.edgeNorthWest = new Edge(nodeNorth, nodeWest);
		this.edgeEastSouth = new Edge(nodeEast, nodeSouth);
		this.edgeEastWest = new Edge(nodeEast, nodeWest);
		this.edgeSouthWest = new Edge(nodeSouth, nodeWest);
	}

	@Test
	public void test() {
		LinkedList<Edge> path = this.minimumSpanningTreeHeuristik.run(nodes, nodeNorth);

		// Check if we have four edges
		Assert.assertEquals(4, path.size());
		
		// Check if we went the right path
		Assert.assertTrue(path.contains(edgeNorthEast));
		Assert.assertTrue(path.contains(edgeEastSouth));
		Assert.assertTrue(path.contains(edgeSouthWest));
		Assert.assertTrue(path.contains(edgeNorthWest));

		// Check if these edges are not part of the path
		Assert.assertFalse(path.contains(edgeNorthSouth));
		Assert.assertFalse(path.contains(edgeEastWest));
	}

}
