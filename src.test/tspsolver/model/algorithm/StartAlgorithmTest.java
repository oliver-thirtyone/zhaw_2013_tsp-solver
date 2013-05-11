package tspsolver.model.algorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.controller.scenario.xml.XMLScenarioLoader;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.path.Path;

public abstract class StartAlgorithmTest {

	public static final String SCENARIO_NORTH_EAST_SOUTH_WEST = "data.test/scenario/test_north_south_east_west.xml";
	public static final String SCENARIO_FIVE_NODES = "data.test/scenario/test_five_nodes.xml";
	public static final String SCENARIO_FIVE_NODES_ONE_NON_ACCESSIBLE_EDGE = "data.test/scenario/test_five_nodes_one_non-accessible_edge.xml";
	public static final String SCENARIO_UNSOLVABLE = "data.test/scenario/test_unsolvable_scenario.xml";
	public static final String SCENARIO_FORTYONE_NODES = "data.test/scenario/test_fortyone_nodes.xml";

	protected Scenario scenarioNorthEastSouthWest;
	protected Scenario scenarioFiveNodes;
	protected Scenario scenarioFiveNodesOneNonAccessibleEdge;
	protected Scenario scenarioUnsolvable;
	protected Scenario scenarioFortyoneNodes;

	protected Grid grid;
	protected Path path;

	protected StartAlgorithm algorithm;

	@Before
	public void setUp() {
		XMLScenarioLoader scenarioLoader = new XMLScenarioLoader();

		// Load all scenarios
		try {
			this.scenarioNorthEastSouthWest = scenarioLoader.loadScenario(new FileInputStream(StartAlgorithmTest.SCENARIO_NORTH_EAST_SOUTH_WEST));
			this.scenarioFiveNodes = scenarioLoader.loadScenario(new FileInputStream(StartAlgorithmTest.SCENARIO_FIVE_NODES));
			this.scenarioFiveNodesOneNonAccessibleEdge = scenarioLoader.loadScenario(new FileInputStream(StartAlgorithmTest.SCENARIO_FIVE_NODES_ONE_NON_ACCESSIBLE_EDGE));
			this.scenarioUnsolvable = scenarioLoader.loadScenario(new FileInputStream(StartAlgorithmTest.SCENARIO_UNSOLVABLE));
			this.scenarioFortyoneNodes = scenarioLoader.loadScenario(new FileInputStream(StartAlgorithmTest.SCENARIO_FORTYONE_NODES));
		}
		catch (IllegalArgumentException exception) {
			exception.printStackTrace();
		}
		catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}

		// Reset the gird and the path
		this.grid = null;
		this.path = null;

		// Make sure that we can not take a step yet
		Assert.assertFalse(this.algorithm.step());
	}

	@Test
	public void testScenarioNorthEastSouthWest() {
		// Get the grid and the path
		this.grid = this.scenarioNorthEastSouthWest.getGrid();
		this.path = this.scenarioNorthEastSouthWest.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(this.scenarioNorthEastSouthWest);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioNorthEastSouthWest();
	}

	@Test
	public void testScenarioFiveNodes() {
		// Get the grid and the path
		this.grid = this.scenarioFiveNodes.getGrid();
		this.path = this.scenarioFiveNodes.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(this.scenarioFiveNodes);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFiveNodes();
	}

	@Test
	public void testScenarioFiveNodesOneNonAccessibleEdge() {
		// Get the grid and the path
		this.grid = this.scenarioFiveNodesOneNonAccessibleEdge.getGrid();
		this.path = this.scenarioFiveNodesOneNonAccessibleEdge.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(this.scenarioFiveNodesOneNonAccessibleEdge);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFiveNodesOneNonAccessibleEdge();
	}

	@Test
	public void testScenarioUnsolvable() {
		// Get the grid and the path
		this.grid = this.scenarioUnsolvable.getGrid();
		this.path = this.scenarioUnsolvable.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(this.scenarioUnsolvable);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioUnsolvable();
	}

	@Test
	public void testScenarioFortyoneNode() {
		// Get the grid and the path
		this.grid = this.scenarioFortyoneNodes.getGrid();
		this.path = this.scenarioFortyoneNodes.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(this.scenarioFortyoneNodes);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFortyoneNode();
	}

	protected abstract void doTestScenarioNorthEastSouthWest();

	protected abstract void doTestScenarioFiveNodes();

	protected abstract void doTestScenarioFiveNodesOneNonAccessibleEdge();

	protected abstract void doTestScenarioUnsolvable();

	protected abstract void doTestScenarioFortyoneNode();

}
