package tspsolver.model.algorithm.optimizer;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.OptimizerAlgorithmHardcoreTest;
import tspsolver.model.algorithm.start.RandomHeuristic;

public class RandomHeuristicAndTwoOptHeuristicHardcoreTest extends OptimizerAlgorithmHardcoreTest {

	@Before
	public void setUp() {
		this.startAlgorithm = new RandomHeuristic();
		this.optimizerAlgorithm = new TwoOptHeuristic();
	}

	@Test
	public void testScenario00012Vertices() {
		this.testScenario(12, "test_hardcore_00012_vertices.xml");
	}

	@Test
	public void testScenario00013Vertices() {
		this.testScenario(13, "test_hardcore_00013_vertices.xml");
	}

	@Test
	public void testScenario00100Vertices() {
		this.testScenario(100, "test_hardcore_00100_vertices.xml");
	}

	@Test
	public void testScenario00200Vertices() {
		this.testScenario(200, "test_hardcore_00200_vertices.xml");
	}

	@Test
	public void testScenario00400Vertices() {
		this.testScenario(400, "test_hardcore_00400_vertices.xml");
	}

}
