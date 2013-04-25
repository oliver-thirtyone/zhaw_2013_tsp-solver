package model.algorithm.start;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import junit.framework.Assert;
import model.grid.Edge;
import model.grid.Node;

import org.junit.Before;
import org.junit.Test;

public class NearestNeighborHeuristikTest {

	private StartAlgorithm nearestNeighborHeuristik;

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
		this.nearestNeighborHeuristik = new NearestNeighborHeuristik();

		this.nodeNorth = new Node(0, 5);
		this.nodeEast = new Node(4, 0); // Nearest node to the node in the north
		this.nodeSouth = new Node(0, -5);
		this.nodeWest = new Node(-5, 0);

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
		LinkedList<Edge> path = this.nearestNeighborHeuristik.run(nodes, nodeNorth);

		// Check if we have four edges
		Assert.assertEquals(4, path.size());
		
		// Check if we went the right path
		Assert.assertEquals(edgeNorthEast, path.get(0));
		Assert.assertEquals(edgeEastSouth, path.get(1));
		Assert.assertEquals(edgeSouthWest, path.get(2));
		Assert.assertEquals(edgeNorthWest, path.get(3));

		// Check if these edges are not part of the path
		Assert.assertFalse(path.contains(edgeNorthSouth));
		Assert.assertFalse(path.contains(edgeEastWest));
	}

}
