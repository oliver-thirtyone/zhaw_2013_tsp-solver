package tspsolver.model.algorithm.optimizer;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.OptimizerAlgorithmHardcoreTest;
import tspsolver.model.algorithm.start.NearestNeighborHeuristic;

public class NearestNeighborHeuristicAndLinKernighanHeuristicHardcoreTest extends OptimizerAlgorithmHardcoreTest {

	@Before
	public void setUp() {
		this.startAlgorithm = new NearestNeighborHeuristic();
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

}
