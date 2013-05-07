package tspsolver.model.scenario;

import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;

public class ScenarioTest {

	private Scenario scenario;

	@Before
	public void setUp() {
		this.scenario = new Scenario("ScenarioTest");
		final Grid grid = this.scenario.getGrid();

		final Node nodeNorth = GridFactory.createNode(0, 5);
		final Node nodeEast = GridFactory.createNode(4, 0);
		final Node nodeSouth = GridFactory.createNode(0, -5);
		final Node nodeWest = GridFactory.createNode(-5, 0);

		GridFactory.addNode(grid, nodeNorth);
		GridFactory.addNode(grid, nodeEast);
		GridFactory.addNode(grid, nodeSouth);
		GridFactory.addNode(grid, nodeWest);

		this.scenario.setStartingNode(nodeNorth);
	}

	@Test
	public void testCopy() throws FileNotFoundException {
		final Scenario copyOfScenario = this.scenario.copy();
		Assert.assertEquals(this.scenario, copyOfScenario);
	}

}
