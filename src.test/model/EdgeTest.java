package model;

import junit.framework.Assert;
import model.grid.Edge;
import model.grid.Grid;
import model.grid.GridFactory;
import model.grid.Node;

import org.junit.Before;
import org.junit.Test;

public class EdgeTest {

	private Grid grid;

	private Node firstNode;
	private Node secondNode;
	private Edge edge;

	@Before
	public void setUp() {
		this.grid = new Grid();

		this.firstNode = GridFactory.createNode(2, 3);
		this.secondNode = GridFactory.createNode(-2, -3);

		this.grid.addNode(firstNode);
		this.grid.addNode(secondNode);

		this.edge = GridFactory.getEdge(firstNode, secondNode);
	}

	@Test
	public void testGetLength() {
		Assert.assertEquals(Math.sqrt(16 + 36), this.edge.getWeight());
	}

	@Test
	public void testNodeEdges() {
		Assert.assertEquals(1, this.firstNode.getEdgeCollection().size());
		Assert.assertEquals(1, this.secondNode.getEdgeCollection().size());
	}

}
