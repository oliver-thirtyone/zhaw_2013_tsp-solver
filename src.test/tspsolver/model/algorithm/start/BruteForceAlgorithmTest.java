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
		Assert.assertEquals(3, stepCounter);
	}

	@Override
	protected void doTestScenarioFiveNodes() {
		Node node1 = GridFactory.getNode(this.grid, "1");
		Node node2 = GridFactory.getNode(this.grid, "2");
		Node node3 = GridFactory.getNode(this.grid, "3");
		Node node4 = GridFactory.getNode(this.grid, "4");
		Node node5 = GridFactory.getNode(this.grid, "5");

		Edge edge12 = GridFactory.getEdge(node1, node2);
		Edge edge13 = GridFactory.getEdge(node1, node3);
		Edge edge14 = GridFactory.getEdge(node1, node4);
		Edge edge15 = GridFactory.getEdge(node1, node5);
		Edge edge23 = GridFactory.getEdge(node2, node3);
		Edge edge24 = GridFactory.getEdge(node2, node4);
		Edge edge25 = GridFactory.getEdge(node2, node5);
		Edge edge34 = GridFactory.getEdge(node3, node4);
		Edge edge35 = GridFactory.getEdge(node3, node5);
		Edge edge45 = GridFactory.getEdge(node4, node5);

		// Step through the algorithm
		int stepCounter = 0;
		do {
			Assert.assertTrue(this.algorithm.step());
			stepCounter++;
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Check if we have five edges
		Assert.assertEquals(5, this.path.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(edge12));
		Assert.assertTrue(this.path.containsEdge(edge15));
		Assert.assertTrue(this.path.containsEdge(edge23));
		Assert.assertTrue(this.path.containsEdge(edge34));
		Assert.assertTrue(this.path.containsEdge(edge45));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edge13));
		Assert.assertFalse(this.path.containsEdge(edge14));
		Assert.assertFalse(this.path.containsEdge(edge24));
		Assert.assertFalse(this.path.containsEdge(edge25));
		Assert.assertFalse(this.path.containsEdge(edge35));

		// Check if we tested all possibilities
		Assert.assertEquals(12, stepCounter);
	}

	@Override
	protected void doTestScenarioFiveNodesOneNonAccessibleEdge() {
		Node node1 = GridFactory.getNode(this.grid, "1");
		Node node2 = GridFactory.getNode(this.grid, "2");
		Node node3 = GridFactory.getNode(this.grid, "3");
		Node node4 = GridFactory.getNode(this.grid, "4");
		Node node5 = GridFactory.getNode(this.grid, "5");

		Edge edge12 = GridFactory.getEdge(node1, node2);
		Edge edge13 = GridFactory.getEdge(node1, node3);
		Edge edge14 = GridFactory.getEdge(node1, node4);
		Edge edge15 = GridFactory.getEdge(node1, node5);
		Edge edge23 = GridFactory.getEdge(node2, node3);
		Edge edge24 = GridFactory.getEdge(node2, node4);
		Edge edge25 = GridFactory.getEdge(node2, node5);
		Edge edge34 = GridFactory.getEdge(node3, node4);
		Edge edge35 = GridFactory.getEdge(node3, node5);
		Edge edge45 = GridFactory.getEdge(node4, node5);

		// Check that one node is non-accessible
		Assert.assertFalse(GridFactory.hasEdge(node1, node2));
		Assert.assertNull(edge12);

		// Step through the algorithm
		int stepCounter = 0;
		do {
			Assert.assertTrue(this.algorithm.step());
			stepCounter++;
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Check if we have five edges
		Assert.assertEquals(5, this.path.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(edge13));
		Assert.assertTrue(this.path.containsEdge(edge23));
		Assert.assertTrue(this.path.containsEdge(edge24));
		Assert.assertTrue(this.path.containsEdge(edge45));
		Assert.assertTrue(this.path.containsEdge(edge15));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edge14));
		Assert.assertFalse(this.path.containsEdge(edge25));
		Assert.assertFalse(this.path.containsEdge(edge34));
		Assert.assertFalse(this.path.containsEdge(edge35));

		// Check if we tested all possibilities
		Assert.assertEquals(12, stepCounter);
	}

	@Override
	protected void doTestScenarioUnsolvable() {
		// Step through the algorithm (59 steps)
		for (int i = 0; i < 59; i++) {
			Assert.assertTrue(this.algorithm.step());
		}

		// Check if the 60. step fails
		Assert.assertFalse(this.algorithm.step());

		// Check if we are still not finished
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());

		// But make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if the path is empty
		Assert.assertTrue(this.path.isEmpty());
	}

	@Override
	protected void doTestScenarioFortyoneNode() {
		// not testable
		Assert.assertTrue(true);
	}

}
