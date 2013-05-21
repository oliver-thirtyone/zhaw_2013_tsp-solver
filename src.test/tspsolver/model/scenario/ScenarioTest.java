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

		Node nodeNorth = GridFactory.createNode("north", 0, 5);
		Node nodeEast = GridFactory.createNode("east", 4, 0);
		Node nodeSouth = GridFactory.createNode("south", 0, -5);
		Node nodeWest = GridFactory.createNode("west", -5, 0);

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
		Scenario copyOfScenario = this.scenario.copy();
		Assert.assertEquals(this.scenario, copyOfScenario);
	}

}
