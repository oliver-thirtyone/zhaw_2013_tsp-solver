package tspsolver.model.scenario.grid;

import junit.framework.Assert;

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

		GridFactory.addNode(this.grid, this.firstNode);
		GridFactory.addNode(this.grid, this.secondNode);

		this.edge = GridFactory.getEdge(this.firstNode, this.secondNode);
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
