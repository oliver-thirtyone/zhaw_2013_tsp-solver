package tspsolver.model.algorithm.start;

import junit.framework.Assert;

import org.junit.Before;

import tspsolver.model.algorithm.StartAlgorithmTest;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;

public class NearestNeighborHeuristikTest extends StartAlgorithmTest {

	@Override
	@Before
	public void setUp() {
		this.algorithm = new NearestNeighborHeuristik();
		super.setUp();
	}

	@Override
	protected void doTestScenarioNorthEastSouthWest() {
		Node nodeNorth = GridFactory.getNode(this.grid, "north");
		Node nodeEast = GridFactory.getNode(this.grid, "east");
		Node nodeSouth = GridFactory.getNode(this.grid, "south");
		Node nodeWest = GridFactory.getNode(this.grid, "west");

		Edge edgeNorthEast = GridFactory.getEdge(nodeNorth, nodeEast);
		Edge edgeNorthSouth = GridFactory.getEdge(nodeNorth, nodeSouth);
		Edge edgeNorthWest = GridFactory.getEdge(nodeNorth, nodeWest);
		Edge edgeEastSouth = GridFactory.getEdge(nodeEast, nodeSouth);
		Edge edgeEastWest = GridFactory.getEdge(nodeEast, nodeWest);
		Edge edgeSouthWest = GridFactory.getEdge(nodeSouth, nodeWest);

		// Take the first step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edgeNorthEast));

		// Take the second step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edgeEastSouth));

		// Take the third step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edgeSouthWest));

		// Take the fourth step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertTrue(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edgeNorthWest));

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edgeNorthSouth));
		Assert.assertFalse(this.path.containsEdge(edgeEastWest));
	}

	@Override
	protected void doTestScenarioFiveNodes() {
		Assert.fail();
	}

	@Override
	protected void doTestScenarioFiveNodesOneNonAccessibleEdge() {
		Assert.fail();
	}

	@Override
	protected void doTestScenarioUnsolvable() {
		Assert.fail();
	}

	@Override
	protected void doTestScenarioFortyoneNode() {
		Assert.fail();
	}

}
