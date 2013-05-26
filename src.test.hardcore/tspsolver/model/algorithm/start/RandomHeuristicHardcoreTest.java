package tspsolver.model.algorithm.start;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.StartAlgorithmHardcoreTest;

public class RandomHeuristicHardcoreTest extends StartAlgorithmHardcoreTest {

	@Before
	public void setUp() {
		this.algorithm = new RandomHeuristic();
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

	@Test
	public void testScenario00800Vertices() {
		this.testScenario(800, "test_hardcore_00800_vertices.xml");
	}

	@Test
	public void testScenario01000Vertices() {
		this.testScenario(1000, "test_hardcore_01000_vertices.xml");
	}

	@Test
	public void testScenario01200Vertices() {
		this.testScenario(1200, "test_hardcore_01200_vertices.xml");
	}

	@Test
	public void testScenario01400Vertices() {
		this.testScenario(1400, "test_hardcore_01400_vertices.xml");
	}

	@Test
	public void testScenario01600Vertices() {
		this.testScenario(1600, "test_hardcore_01600_vertices.xml");
	}

	@Test
	public void testScenario03200Vertices() {
		this.testScenario(3200, "test_hardcore_03200_vertices.xml");
	}

	@Test
	public void testScenario04000Vertices() {
		this.testScenario(4000, "test_hardcore_04000_vertices.xml");
	}

}
