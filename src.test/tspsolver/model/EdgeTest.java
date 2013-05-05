package tspsolver.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.GridFactory;
import tspsolver.model.grid.Node;

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
		Assert.assertEquals(1, this.firstNode.getNumberOfEdges());
		Assert.assertEquals(1, this.secondNode.getNumberOfEdges());
	}

}
