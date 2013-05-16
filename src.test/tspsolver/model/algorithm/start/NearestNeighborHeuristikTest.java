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

		// Take the first step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge15));

		// Take the second step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge45));

		// Take the third step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge34));

		// Take the fourth step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge23));

		// Take the fifth step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertTrue(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge12));

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edge13));
		Assert.assertFalse(this.path.containsEdge(edge14));
		Assert.assertFalse(this.path.containsEdge(edge24));
		Assert.assertFalse(this.path.containsEdge(edge25));
		Assert.assertFalse(this.path.containsEdge(edge35));
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

		// Take the first step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge15));

		// Take the second step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge45));

		// Take the third step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge34));

		// Take the fourth step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edge23));

		// Take the fifth step
		Assert.assertFalse(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edge13));
		Assert.assertFalse(this.path.containsEdge(edge14));
		Assert.assertFalse(this.path.containsEdge(edge24));
		Assert.assertFalse(this.path.containsEdge(edge25));
		Assert.assertFalse(this.path.containsEdge(edge35));
	}

	@Override
	protected void doTestScenarioUnsolvable() {
		Node north = GridFactory.getNode(this.grid, "north");
		Node east1 = GridFactory.getNode(this.grid, "east1");
		Node east2 = GridFactory.getNode(this.grid, "east2");
		Node south = GridFactory.getNode(this.grid, "south");
		Node west1 = GridFactory.getNode(this.grid, "west1");
		Node west2 = GridFactory.getNode(this.grid, "west2");

		Edge edgeNorthEast1 = GridFactory.getEdge(north, east1);
		Edge edgeNorthWest1 = GridFactory.getEdge(north, west1);
		Edge edgeEast1West1 = GridFactory.getEdge(east1, west1);
		Edge edgeSouthEast2 = GridFactory.getEdge(south, east2);
		Edge edgeSouthWest2 = GridFactory.getEdge(south, west2);
		Edge edgeedgeEast2West2 = GridFactory.getEdge(east2, west2);

		// Take the first step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edgeNorthEast1));

		// Take the second step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edgeEast1West1));

		// Take the third step
		Assert.assertFalse(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edgeNorthWest1));
		Assert.assertFalse(this.path.containsEdge(edgeSouthEast2));
		Assert.assertFalse(this.path.containsEdge(edgeSouthWest2));
		Assert.assertFalse(this.path.containsEdge(edgeedgeEast2West2));
	}

	@Override
	protected void doTestScenarioFortyoneNode() {
		// Take forty steps
		for (int i = 0; i < 40; i++) {
			Assert.assertTrue(this.algorithm.step());
			Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		}

		// Take the last step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertTrue(this.algorithm.hasFinishedSuccessfully());

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());
	}

}
