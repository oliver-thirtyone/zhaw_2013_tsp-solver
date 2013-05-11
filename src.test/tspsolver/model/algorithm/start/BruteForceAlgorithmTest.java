package tspsolver.model.algorithm.start;

import junit.framework.Assert;

import org.junit.Before;

import tspsolver.model.algorithm.StartAlgorithmTest;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;

public class BruteForceAlgorithmTest extends StartAlgorithmTest {

	@Override
	@Before
	public void setUp() {
		this.algorithm = new BruteForceAlgorithm();
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

		// Step through the algorithm
		int stepCounter = 0;
		do {
			Assert.assertTrue(this.algorithm.step());
			stepCounter++;
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Check if we have four edges
		Assert.assertEquals(4, this.path.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(edgeNorthEast));
		Assert.assertTrue(this.path.containsEdge(edgeEastSouth));
		Assert.assertTrue(this.path.containsEdge(edgeSouthWest));
		Assert.assertTrue(this.path.containsEdge(edgeNorthWest));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edgeNorthSouth));
		Assert.assertFalse(this.path.containsEdge(edgeEastWest));

		// Check if we tested all possibilities
		Assert.assertEquals(6, stepCounter);
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
