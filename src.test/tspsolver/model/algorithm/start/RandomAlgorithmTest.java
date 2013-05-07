package tspsolver.model.algorithm.start;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;

public class RandomAlgorithmTest {

	private Scenario scenario;
	private Grid grid;
	private Path path;

	private Node nodeNorth;
	private Node nodeEast;
	private Node nodeSouth;
	private Node nodeWest;

	private Set<Node> nodes;

	private StartAlgorithm algorithm;

	@Before
	public void setUp() {
		this.scenario = new Scenario("RandomAlgorithmTest");
		this.grid = this.scenario.getGrid();
		this.path = this.scenario.getPath();

		this.nodeNorth = GridFactory.createNode(0, 5);
		this.nodeEast = GridFactory.createNode(5, 0);
		this.nodeSouth = GridFactory.createNode(0, -5);
		this.nodeWest = GridFactory.createNode(-5, 0);

		this.nodes = new HashSet<Node>();
		this.nodes.add(this.nodeNorth);
		this.nodes.add(this.nodeEast);
		this.nodes.add(this.nodeSouth);
		this.nodes.add(this.nodeWest);

		GridFactory.addNode(this.grid, this.nodeNorth);
		GridFactory.addNode(this.grid, this.nodeEast);
		GridFactory.addNode(this.grid, this.nodeSouth);
		GridFactory.addNode(this.grid, this.nodeWest);

		this.scenario.setStartingNode(this.nodeNorth);

		this.algorithm = new RandomAlgorithm();
	}

	@Test
	public void test() {
		// Make sure that we can not take a step yet
		Assert.assertFalse(this.algorithm.step());

		// Initialize the algorithm
		this.algorithm.initialize(this.scenario);
		Assert.assertTrue(this.algorithm.hasValidArguments());

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
}
