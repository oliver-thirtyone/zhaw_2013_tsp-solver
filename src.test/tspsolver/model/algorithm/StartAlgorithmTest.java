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
import tspsolver.model.validator.TSPValidator;
import tspsolver.model.validator.Validator;

public abstract class StartAlgorithmTest {

	public static final String SCENARIO_NORTH_EAST_SOUTH_WEST = "data.test/scenario/test_north_south_east_west.xml";
	public static final String SCENARIO_FIVE_VERTICES = "data.test/scenario/test_five_vertices.xml";
	public static final String SCENARIO_FIVE_VERTICES_ONE_NON_ACCESSIBLE_EDGE = "data.test/scenario/test_five_vertices_one_non-accessible_edge.xml";
	public static final String SCENARIO_UNSOLVABLE = "data.test/scenario/test_unsolvable_scenario.xml";
	public static final String SCENARIO_FORTYONE_VERTICES = "data.test/scenario/test_fortyone_vertices.xml";

	private static final Validator VALIDATOR = new TSPValidator();

	protected Scenario scenarioNorthEastSouthWest;
	protected Scenario scenarioFiveVertices;
	protected Scenario scenarioFiveVerticesOneNonAccessibleEdge;
	protected Scenario scenarioUnsolvable;
	protected Scenario scenarioFortyoneVertices;

	protected Grid grid;
	protected Path path;

	protected StartAlgorithm algorithm;

	@Before
	public void setUp() {
		XMLScenarioLoader scenarioLoader = new XMLScenarioLoader();

		this.scenarioNorthEastSouthWest = new Scenario(StartAlgorithmTest.VALIDATOR);
		this.scenarioFiveVertices = new Scenario(StartAlgorithmTest.VALIDATOR);
		this.scenarioFiveVerticesOneNonAccessibleEdge = new Scenario(StartAlgorithmTest.VALIDATOR);
		this.scenarioUnsolvable = new Scenario(StartAlgorithmTest.VALIDATOR);
		this.scenarioFortyoneVertices = new Scenario(StartAlgorithmTest.VALIDATOR);

		// Load all scenarios
		try {
			scenarioLoader.loadScenario(this.scenarioNorthEastSouthWest, new FileInputStream(StartAlgorithmTest.SCENARIO_NORTH_EAST_SOUTH_WEST));
			scenarioLoader.loadScenario(this.scenarioFiveVertices, new FileInputStream(StartAlgorithmTest.SCENARIO_FIVE_VERTICES));
			scenarioLoader.loadScenario(this.scenarioFiveVerticesOneNonAccessibleEdge, new FileInputStream(StartAlgorithmTest.SCENARIO_FIVE_VERTICES_ONE_NON_ACCESSIBLE_EDGE));
			scenarioLoader.loadScenario(this.scenarioUnsolvable, new FileInputStream(StartAlgorithmTest.SCENARIO_UNSOLVABLE));
			scenarioLoader.loadScenario(this.scenarioFortyoneVertices, new FileInputStream(StartAlgorithmTest.SCENARIO_FORTYONE_VERTICES));
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
	public void testScenarioFiveVertices() {
		// Get the grid and the path
		this.grid = this.scenarioFiveVertices.getGrid();
		this.path = this.scenarioFiveVertices.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(this.scenarioFiveVertices);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFiveVertices();
	}

	@Test
	public void testScenarioFiveVerticesOneNonAccessibleEdge() {
		// Get the grid and the path
		this.grid = this.scenarioFiveVerticesOneNonAccessibleEdge.getGrid();
		this.path = this.scenarioFiveVerticesOneNonAccessibleEdge.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(this.scenarioFiveVerticesOneNonAccessibleEdge);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFiveVerticesOneNonAccessibleEdge();
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
	public void testScenarioFortyoneVertex() {
		// Get the grid and the path
		this.grid = this.scenarioFortyoneVertices.getGrid();
		this.path = this.scenarioFortyoneVertices.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(this.scenarioFortyoneVertices);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFortyoneVertex();
	}

	protected abstract void doTestScenarioNorthEastSouthWest();

	protected abstract void doTestScenarioFiveVertices();

	protected abstract void doTestScenarioFiveVerticesOneNonAccessibleEdge();

	protected abstract void doTestScenarioUnsolvable();

	protected abstract void doTestScenarioFortyoneVertex();

}
