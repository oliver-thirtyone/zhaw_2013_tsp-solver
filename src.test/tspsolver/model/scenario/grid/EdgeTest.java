package tspsolver.model.scenario.grid;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;

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

		GridFactory.addNode(this.grid, firstNode);
		GridFactory.addNode(this.grid, secondNode);

		this.edge = GridFactory.getEdge(firstNode, secondNode);
	}

	@Test
	public void testGetWeight() {
		Assert.assertEquals(Math.sqrt(16 + 36), this.edge.getWeight());
	}

	@Test
	public void testNumberOfEdges() {
		Assert.assertEquals(1, this.firstNode.getNumberOfEdges());
		Assert.assertEquals(1, this.secondNode.getNumberOfEdges());
	}

}
