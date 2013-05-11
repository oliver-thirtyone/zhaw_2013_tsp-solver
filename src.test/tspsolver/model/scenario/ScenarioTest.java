package tspsolver.model.scenario;

import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public class ScenarioTest {

	private Scenario scenario;

	@Before
	public void setUp() {
		this.scenario = new Scenario("ScenarioTest");
		final Grid grid = this.scenario.getGrid();
		final Path path = this.scenario.getPath();
		final PathUpdater pathUpdater = new PathUpdater(path);

		final Node nodeNorth = GridFactory.createNode("north", 0, 5);
		final Node nodeEast = GridFactory.createNode("east", 4, 0);
		final Node nodeSouth = GridFactory.createNode("south", 0, -5);
		final Node nodeWest = GridFactory.createNode("west", -5, 0);

		GridFactory.addNode(grid, nodeNorth);
		GridFactory.addNode(grid, nodeEast);
		GridFactory.addNode(grid, nodeSouth);
		GridFactory.addNode(grid, nodeWest);

		Edge edgeNorthEast = GridFactory.getEdge(nodeNorth, nodeEast);
		Edge edgeNorthWest = GridFactory.getEdge(nodeNorth, nodeWest);
		Edge edgeEastSouth = GridFactory.getEdge(nodeEast, nodeSouth);
		Edge edgeSouthWest = GridFactory.getEdge(nodeSouth, nodeWest);

		pathUpdater.addEdge(edgeNorthEast);
		pathUpdater.addEdge(edgeEastSouth);
		pathUpdater.addEdge(edgeSouthWest);
		pathUpdater.addEdge(edgeNorthWest);
		pathUpdater.updatePath();

		this.scenario.setStartingNode(nodeNorth);
	}

	@Test
	public void testCopy() throws FileNotFoundException {
		final Scenario copyOfScenario = this.scenario.copy();
		Assert.assertEquals(this.scenario, copyOfScenario);
	}

}
