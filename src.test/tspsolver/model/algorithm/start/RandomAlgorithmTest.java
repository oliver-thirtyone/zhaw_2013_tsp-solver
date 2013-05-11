package tspsolver.model.algorithm.start;

import junit.framework.Assert;

import org.junit.Before;

import tspsolver.model.algorithm.StartAlgorithmTest;

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
