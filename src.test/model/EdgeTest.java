package model;

import junit.framework.Assert;
import model.grid.Edge;
import model.grid.Node;

import org.junit.Before;
import org.junit.Test;

public class EdgeTest {

	private Node firstNode;
	private Node secondNode;
	private Edge edge;

	@Before
	public void setUp() {
		this.firstNode = new Node(2, 3);
		this.secondNode = new Node(-2, -3);
		this.edge = new Edge(this.firstNode, this.secondNode);
	}

	@Test
	public void testGetLength() {
		Assert.assertEquals(Math.sqrt(16 + 36), this.edge.getLength());
	}

	@Test
	public void testNodeEdges() {
		Assert.assertEquals(1, this.firstNode.getEdges().size());
		Assert.assertEquals(1, this.secondNode.getEdges().size());
	}

}
