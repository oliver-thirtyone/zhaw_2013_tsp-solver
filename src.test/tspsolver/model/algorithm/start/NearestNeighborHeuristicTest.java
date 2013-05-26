package tspsolver.model.algorithm.start;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.StartAlgorithmTest;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Vertex;

public class NearestNeighborHeuristicTest extends StartAlgorithmTest {

	@Override
	@Before
	public void setUp() {
		this.algorithm = new NearestNeighborHeuristic();
		super.setUp();
	}

	@Override
	protected void doTestScenarioNorthEastSouthWest() {
		Vertex vertexNorth = GridFactory.getVertex(this.grid, "north");
		Vertex vertexEast = GridFactory.getVertex(this.grid, "east");
		Vertex vertexSouth = GridFactory.getVertex(this.grid, "south");
		Vertex vertexWest = GridFactory.getVertex(this.grid, "west");

		Edge edgeNorthEast = GridFactory.getEdge(vertexNorth, vertexEast);
		Edge edgeNorthSouth = GridFactory.getEdge(vertexNorth, vertexSouth);
		Edge edgeNorthWest = GridFactory.getEdge(vertexNorth, vertexWest);
		Edge edgeEastSouth = GridFactory.getEdge(vertexEast, vertexSouth);
		Edge edgeEastWest = GridFactory.getEdge(vertexEast, vertexWest);
		Edge edgeSouthWest = GridFactory.getEdge(vertexSouth, vertexWest);

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

		// Check if the path is valid
		Assert.assertTrue(StartAlgorithmTest.scenarioNorthEastSouthWest.isPathValid());
	}

	@Override
	protected void doTestScenarioFiveVertices() {
		Vertex vertex1 = GridFactory.getVertex(this.grid, "1");
		Vertex vertex2 = GridFactory.getVertex(this.grid, "2");
		Vertex vertex3 = GridFactory.getVertex(this.grid, "3");
		Vertex vertex4 = GridFactory.getVertex(this.grid, "4");
		Vertex vertex5 = GridFactory.getVertex(this.grid, "5");

		Edge edge12 = GridFactory.getEdge(vertex1, vertex2);
		Edge edge13 = GridFactory.getEdge(vertex1, vertex3);
		Edge edge14 = GridFactory.getEdge(vertex1, vertex4);
		Edge edge15 = GridFactory.getEdge(vertex1, vertex5);
		Edge edge23 = GridFactory.getEdge(vertex2, vertex3);
		Edge edge24 = GridFactory.getEdge(vertex2, vertex4);
		Edge edge25 = GridFactory.getEdge(vertex2, vertex5);
		Edge edge34 = GridFactory.getEdge(vertex3, vertex4);
		Edge edge35 = GridFactory.getEdge(vertex3, vertex5);
		Edge edge45 = GridFactory.getEdge(vertex4, vertex5);

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

		// Check if the path is valid
		Assert.assertTrue(StartAlgorithmTest.scenarioFiveVertices.isPathValid());
	}

	@Override
	protected void doTestScenarioFiveVerticesOneNonAccessibleEdge() {
		Vertex vertex1 = GridFactory.getVertex(this.grid, "1");
		Vertex vertex2 = GridFactory.getVertex(this.grid, "2");
		Vertex vertex3 = GridFactory.getVertex(this.grid, "3");
		Vertex vertex4 = GridFactory.getVertex(this.grid, "4");
		Vertex vertex5 = GridFactory.getVertex(this.grid, "5");

		Edge edge12 = GridFactory.getEdge(vertex1, vertex2);
		Edge edge13 = GridFactory.getEdge(vertex1, vertex3);
		Edge edge14 = GridFactory.getEdge(vertex1, vertex4);
		Edge edge15 = GridFactory.getEdge(vertex1, vertex5);
		Edge edge23 = GridFactory.getEdge(vertex2, vertex3);
		Edge edge24 = GridFactory.getEdge(vertex2, vertex4);
		Edge edge25 = GridFactory.getEdge(vertex2, vertex5);
		Edge edge34 = GridFactory.getEdge(vertex3, vertex4);
		Edge edge35 = GridFactory.getEdge(vertex3, vertex5);
		Edge edge45 = GridFactory.getEdge(vertex4, vertex5);

		// Check that one vertex is non-accessible
		Assert.assertFalse(GridFactory.hasEdge(vertex1, vertex2));
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

		// Check if the path is invalid
		Assert.assertFalse(StartAlgorithmTest.scenarioFiveVerticesOneNonAccessibleEdge.isPathValid());
	}

	@Override
	protected void doTestScenarioUnsolvable() {
		Vertex north = GridFactory.getVertex(this.grid, "north");
		Vertex east1 = GridFactory.getVertex(this.grid, "east1");
		Vertex east2 = GridFactory.getVertex(this.grid, "east2");
		Vertex south = GridFactory.getVertex(this.grid, "south");
		Vertex west1 = GridFactory.getVertex(this.grid, "west1");
		Vertex west2 = GridFactory.getVertex(this.grid, "west2");

		Edge edgeNorthEast1 = GridFactory.getEdge(north, east1);
		Edge edgeNorthWest1 = GridFactory.getEdge(north, west1);
		Edge edgeEast1West1 = GridFactory.getEdge(east1, west1);
		Edge edgeSouthEast2 = GridFactory.getEdge(south, east2);
		Edge edgeSouthWest2 = GridFactory.getEdge(south, west2);
		Edge edgeEast2West2 = GridFactory.getEdge(east2, west2);

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
		Assert.assertFalse(this.path.containsEdge(edgeEast2West2));

		// Check if the path is invalid
		Assert.assertFalse(StartAlgorithmTest.scenarioUnsolvable.isPathValid());
	}

	@Override
	protected void doTestScenarioFortyoneVertices() {
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
		Assert.assertTrue(StartAlgorithmTest.scenarioFortyoneVertices.isPathValid());
	}

	@Test
	public void testScenario00100Vertices() {
		this.testScenario(StartAlgorithmTest.scenario00100Vertices);
	}

	@Test
	public void testScenario00200Vertices() {
		this.testScenario(StartAlgorithmTest.scenario00200Vertices);
	}

	@Test
	public void testScenario0400Vertices() {
		this.testScenario(StartAlgorithmTest.scenario00400Vertices);
	}

	@Test
	public void testScenario00800Vertices() {
		this.testScenario(StartAlgorithmTest.scenario00800Vertices);
	}

	@Test
	public void testScenario01600Vertices() {
		this.testScenario(StartAlgorithmTest.scenario01600Vertices);
	}

}
