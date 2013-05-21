package tspsolver.model.algorithm.start;

import junit.framework.Assert;

import org.junit.Before;

import tspsolver.model.algorithm.StartAlgorithmTest;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;

public class RandomAlgorithmTest extends StartAlgorithmTest {

	@Override
	@Before
	public void setUp() {
		this.algorithm = new RandomAlgorithm();
		super.setUp();
	}

	@Override
	protected void doTestScenarioNorthEastSouthWest() {
		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// DEBUG OUTPUT
		// System.out.println("Path: ");
		// for (Edge edge : this.path.getEdges()) {
		// System.out.println(edge.toString());
		// }
		// System.out.println();

		// Check if we have four edges
		Assert.assertEquals(4, this.path.getNumberOfEdges());

		// Check if the path is valid
		Assert.assertTrue(this.scenarioNorthEastSouthWest.isPathValid());
	}

	@Override
	protected void doTestScenarioFiveNodes() {
		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// DEBUG OUTPUT
		// System.out.println("Path: ");
		// for (Edge edge : this.path.getEdges()) {
		// System.out.println(edge.toString());
		// }
		// System.out.println();

		// Check if we have four edges
		Assert.assertEquals(5, this.path.getNumberOfEdges());

		// Check if the path is valid
		Assert.assertTrue(this.scenarioFiveNodes.isPathValid());
	}

	@Override
	protected void doTestScenarioFiveNodesOneNonAccessibleEdge() {
		// not testable
		Assert.assertFalse(this.scenarioFiveNodesOneNonAccessibleEdge.isPathValid());
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
		Edge edgeEast2West2 = GridFactory.getEdge(east2, west2);

		// Take the first step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		Assert.assertTrue(this.path.containsEdge(edgeNorthEast1) || this.path.containsEdge(edgeNorthWest1));

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
		Assert.assertFalse(this.path.containsEdge(edgeNorthEast1) && this.path.containsEdge(edgeNorthWest1));
		Assert.assertFalse(this.path.containsEdge(edgeSouthEast2));
		Assert.assertFalse(this.path.containsEdge(edgeSouthWest2));
		Assert.assertFalse(this.path.containsEdge(edgeEast2West2));

		// Check if the path is invalid
		Assert.assertFalse(this.scenarioUnsolvable.isPathValid());
	}

	@Override
	protected void doTestScenarioFortyoneNode() {
		// Take 39 steps
		for (int i = 0; i < 40; i++) {
			Assert.assertTrue(this.algorithm.step());
			Assert.assertFalse(this.algorithm.hasFinishedSuccessfully());
		}

		// Take the last step
		Assert.assertTrue(this.algorithm.step());
		Assert.assertTrue(this.algorithm.hasFinishedSuccessfully());

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if the path is valid
		Assert.assertTrue(this.scenarioFortyoneNodes.isPathValid());
	}
}
