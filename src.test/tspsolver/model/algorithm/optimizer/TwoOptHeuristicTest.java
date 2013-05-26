package tspsolver.model.algorithm.optimizer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Vertex;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;
import tspsolver.model.validator.TSPValidator;

public class TwoOptHeuristicTest {

	private Scenario scenario;
	private Grid grid;
	private Path path;

	private Vertex vertexNorth;
	private Vertex vertexEast;
	private Vertex vertexSouth;
	private Vertex vertexWest;

	private Edge edgeNorthEast;
	private Edge edgeNorthSouth;
	private Edge edgeNorthWest;
	private Edge edgeEastSouth;
	private Edge edgeEastWest;
	private Edge edgeSouthWest;

	private OptimizerAlgorithm algorithm;

	@Before
	public void setUp() {
		this.scenario = new Scenario(new TSPValidator());
		this.grid = this.scenario.getGrid();
		this.path = this.scenario.getPath();

		this.vertexNorth = GridFactory.createVertex("north", 4, 2);
		this.vertexEast = GridFactory.createVertex("east", 6, 4);
		this.vertexSouth = GridFactory.createVertex("south", 4, 6);
		this.vertexWest = GridFactory.createVertex("west", 2, 4);

		GridFactory.addVertex(this.grid, this.vertexNorth);
		GridFactory.addVertex(this.grid, this.vertexEast);
		GridFactory.addVertex(this.grid, this.vertexSouth);
		GridFactory.addVertex(this.grid, this.vertexWest);

		this.scenario.setStartingVertex(this.vertexNorth);

		this.edgeNorthEast = GridFactory.getEdge(this.vertexNorth, this.vertexEast);
		this.edgeNorthSouth = GridFactory.getEdge(this.vertexNorth, this.vertexSouth);
		this.edgeNorthWest = GridFactory.getEdge(this.vertexNorth, this.vertexWest);
		this.edgeEastSouth = GridFactory.getEdge(this.vertexEast, this.vertexSouth);
		this.edgeEastWest = GridFactory.getEdge(this.vertexEast, this.vertexWest);
		this.edgeSouthWest = GridFactory.getEdge(this.vertexSouth, this.vertexWest);

		this.algorithm = new TwoOptHeuristic();
	}

	@Test
	public void test() {
		// Create a valid path that is too heavy
		PathUpdater pathUpdater = new PathUpdater(this.path);
		pathUpdater.addEdge(this.edgeNorthSouth);
		pathUpdater.addEdge(this.edgeNorthWest);
		pathUpdater.addEdge(this.edgeEastWest);
		pathUpdater.addEdge(this.edgeEastSouth);
		pathUpdater.updatePath();

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

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(this.edgeNorthEast));
		Assert.assertTrue(this.path.containsEdge(this.edgeEastSouth));
		Assert.assertTrue(this.path.containsEdge(this.edgeSouthWest));
		Assert.assertTrue(this.path.containsEdge(this.edgeNorthWest));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(this.edgeNorthSouth));
		Assert.assertFalse(this.path.containsEdge(this.edgeEastWest));

		// Check if the path is valid
		Assert.assertTrue(this.scenario.isPathValid());
	}
}
