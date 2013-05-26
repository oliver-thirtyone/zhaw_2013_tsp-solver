package tspsolver.model.algorithm.optimizer;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.OptimizerAlgorithmHardcoreTest;
import tspsolver.model.algorithm.start.MinimumSpanningTreeHeuristic;

public class MinimumSpanningTreeHeuristicAndLinKernighanHeuristicHardcoreTest extends OptimizerAlgorithmHardcoreTest {

	@Before
	public void setUp() {
		this.startAlgorithm = new MinimumSpanningTreeHeuristic();
		this.optimizerAlgorithm = new LinKernighanHeuristic();
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
	public void testScenario00050Vertices() {
		this.testScenario(50, "test_hardcore_00050_vertices.xml");
	}

}
