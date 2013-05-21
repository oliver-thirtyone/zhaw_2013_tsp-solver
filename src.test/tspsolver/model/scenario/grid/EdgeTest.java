package tspsolver.model.scenario.grid;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class EdgeTest {

	private Grid grid;

	private Vertex firstVertex;
	private Vertex secondVertex;
	private Edge edge;

	@Before
	public void setUp() {
		this.grid = new Grid();

		this.firstVertex = GridFactory.createVertex("firstVertex", 2, 3);
		this.secondVertex = GridFactory.createVertex("secondVertex", -2, -3);

		GridFactory.addVertex(this.grid, this.firstVertex);
		GridFactory.addVertex(this.grid, this.secondVertex);

		this.edge = GridFactory.getEdge(this.firstVertex, this.secondVertex);
	}

	@Test
	public void testGetWeight() {
		double sqrt = Math.sqrt(16 + 36);
		int intValue = (int) (sqrt + 0.5);

		Assert.assertEquals(intValue, this.edge.getWeight());
	}

	@Test
	public void testNumberOfEdges() {
		Assert.assertEquals(1, this.firstVertex.getNumberOfEdges());
		Assert.assertEquals(1, this.secondVertex.getNumberOfEdges());
	}

}
