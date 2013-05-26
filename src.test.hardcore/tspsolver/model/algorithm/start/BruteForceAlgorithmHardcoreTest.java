package tspsolver.model.algorithm.start;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.StartAlgorithmHardcoreTest;

public class BruteForceAlgorithmHardcoreTest extends StartAlgorithmHardcoreTest {

	@Before
	public void setUp() {
		this.algorithm = new BruteForceAlgorithm();
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
