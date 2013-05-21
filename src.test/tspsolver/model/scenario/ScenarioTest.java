package tspsolver.model.scenario;

import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Vertex;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;
import tspsolver.model.validator.TSPValidator;

public class ScenarioTest {

	private Scenario scenario;

	@Before
	public void setUp() {
		this.scenario = new Scenario(new TSPValidator());
		this.scenario.setName("ScenarioTest");

		Grid grid = this.scenario.getGrid();
		Path path = this.scenario.getPath();
		PathUpdater pathUpdater = new PathUpdater(path);

		Vertex vertexNorth = GridFactory.createVertex("north", 0, 5);
		Vertex vertexEast = GridFactory.createVertex("east", 4, 0);
		Vertex vertexSouth = GridFactory.createVertex("south", 0, -5);
		Vertex vertexWest = GridFactory.createVertex("west", -5, 0);

		GridFactory.addVertex(grid, vertexNorth);
		GridFactory.addVertex(grid, vertexEast);
		GridFactory.addVertex(grid, vertexSouth);
		GridFactory.addVertex(grid, vertexWest);

		Edge edgeNorthEast = GridFactory.getEdge(vertexNorth, vertexEast);
		Edge edgeNorthWest = GridFactory.getEdge(vertexNorth, vertexWest);
		Edge edgeEastSouth = GridFactory.getEdge(vertexEast, vertexSouth);
		Edge edgeSouthWest = GridFactory.getEdge(vertexSouth, vertexWest);

		pathUpdater.addEdge(edgeNorthEast);
		pathUpdater.addEdge(edgeEastSouth);
		pathUpdater.addEdge(edgeSouthWest);
		pathUpdater.addEdge(edgeNorthWest);
		pathUpdater.updatePath();

		this.scenario.setStartingVertex(vertexNorth);
	}

	@Test
	public void testCopy() throws FileNotFoundException {
		Scenario copyOfScenario = this.scenario.copy();
		Assert.assertEquals(this.scenario, copyOfScenario);
	}

}
