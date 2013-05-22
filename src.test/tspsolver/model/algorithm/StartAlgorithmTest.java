package tspsolver.model.algorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
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

	// VM arguments: -Xms1024m -Xmx2048m
	public static final String SCENARIO_00100_VERTICES = "data.test/scenario/test_z_00100_vertices.xml";
	public static final String SCENARIO_00200_VERTICES = "data.test/scenario/test_z_00200_vertices.xml";
	public static final String SCENARIO_00400_VERTICES = "data.test/scenario/test_z_00400_vertices.xml";
	public static final String SCENARIO_00800_VERTICES = "data.test/scenario/test_z_00800_vertices.xml";
	public static final String SCENARIO_01600_VERTICES = "data.test/scenario/test_z_01600_vertices.xml";
	public static final String SCENARIO_03200_VERTICES = "data.test/scenario/test_z_03200_vertices.xml";

	private static final Validator VALIDATOR = new TSPValidator();

	protected static Scenario scenarioNorthEastSouthWest;
	protected static Scenario scenarioFiveVertices;
	protected static Scenario scenarioFiveVerticesOneNonAccessibleEdge;
	protected static Scenario scenarioUnsolvable;
	protected static Scenario scenarioFortyoneVertices;

	protected static Scenario scenario00100Vertices;
	protected static Scenario scenario00200Vertices;
	protected static Scenario scenario00400Vertices;
	protected static Scenario scenario00800Vertices;
	protected static Scenario scenario01600Vertices;
	protected static Scenario scenario03200Vertices;

	protected Grid grid;
	protected Path path;

	protected StartAlgorithm algorithm;

	@BeforeClass
	public static void setUpClass() {
		XMLScenarioLoader scenarioLoader = new XMLScenarioLoader();

		StartAlgorithmTest.scenarioNorthEastSouthWest = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenarioFiveVertices = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenarioFiveVerticesOneNonAccessibleEdge = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenarioUnsolvable = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenarioFortyoneVertices = new Scenario(StartAlgorithmTest.VALIDATOR);

		StartAlgorithmTest.scenario00100Vertices = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenario00200Vertices = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenario00400Vertices = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenario00800Vertices = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenario01600Vertices = new Scenario(StartAlgorithmTest.VALIDATOR);
		StartAlgorithmTest.scenario03200Vertices = new Scenario(StartAlgorithmTest.VALIDATOR);

		// Load all scenarios
		try {
			scenarioLoader.loadScenario(StartAlgorithmTest.scenarioNorthEastSouthWest, new FileInputStream(StartAlgorithmTest.SCENARIO_NORTH_EAST_SOUTH_WEST));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenarioFiveVertices, new FileInputStream(StartAlgorithmTest.SCENARIO_FIVE_VERTICES));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenarioFiveVerticesOneNonAccessibleEdge, new FileInputStream(StartAlgorithmTest.SCENARIO_FIVE_VERTICES_ONE_NON_ACCESSIBLE_EDGE));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenarioUnsolvable, new FileInputStream(StartAlgorithmTest.SCENARIO_UNSOLVABLE));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenarioFortyoneVertices, new FileInputStream(StartAlgorithmTest.SCENARIO_FORTYONE_VERTICES));

			scenarioLoader.loadScenario(StartAlgorithmTest.scenario00100Vertices, new FileInputStream(StartAlgorithmTest.SCENARIO_00100_VERTICES));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenario00200Vertices, new FileInputStream(StartAlgorithmTest.SCENARIO_00200_VERTICES));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenario00400Vertices, new FileInputStream(StartAlgorithmTest.SCENARIO_00400_VERTICES));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenario00800Vertices, new FileInputStream(StartAlgorithmTest.SCENARIO_00800_VERTICES));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenario01600Vertices, new FileInputStream(StartAlgorithmTest.SCENARIO_01600_VERTICES));
			scenarioLoader.loadScenario(StartAlgorithmTest.scenario03200Vertices, new FileInputStream(StartAlgorithmTest.SCENARIO_03200_VERTICES));
		}
		catch (IllegalArgumentException exception) {
			exception.printStackTrace();
		}
		catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}
	}

	@Before
	public void setUp() {
		// Reset the gird and the path
		this.grid = null;
		this.path = null;

		// Make sure that we can not take a step yet
		Assert.assertFalse(this.algorithm.step());
	}

	@Test
	public void testScenarioNorthEastSouthWest() {
		// Get the grid and the path
		this.grid = StartAlgorithmTest.scenarioNorthEastSouthWest.getGrid();
		this.path = StartAlgorithmTest.scenarioNorthEastSouthWest.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(StartAlgorithmTest.scenarioNorthEastSouthWest);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioNorthEastSouthWest();
	}

	@Test
	public void testScenarioFiveVertices() {
		// Get the grid and the path
		this.grid = StartAlgorithmTest.scenarioFiveVertices.getGrid();
		this.path = StartAlgorithmTest.scenarioFiveVertices.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(StartAlgorithmTest.scenarioFiveVertices);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFiveVertices();
	}

	@Test
	public void testScenarioFiveVerticesOneNonAccessibleEdge() {
		// Get the grid and the path
		this.grid = StartAlgorithmTest.scenarioFiveVerticesOneNonAccessibleEdge.getGrid();
		this.path = StartAlgorithmTest.scenarioFiveVerticesOneNonAccessibleEdge.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(StartAlgorithmTest.scenarioFiveVerticesOneNonAccessibleEdge);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFiveVerticesOneNonAccessibleEdge();
	}

	@Test
	public void testScenarioUnsolvable() {
		// Get the grid and the path
		this.grid = StartAlgorithmTest.scenarioUnsolvable.getGrid();
		this.path = StartAlgorithmTest.scenarioUnsolvable.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(StartAlgorithmTest.scenarioUnsolvable);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioUnsolvable();
	}

	@Test
	public void testScenarioFortyoneVertices() {
		// Get the grid and the path
		this.grid = StartAlgorithmTest.scenarioFortyoneVertices.getGrid();
		this.path = StartAlgorithmTest.scenarioFortyoneVertices.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(StartAlgorithmTest.scenarioFortyoneVertices);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Run the test
		this.doTestScenarioFortyoneVertices();
	}

	protected abstract void doTestScenarioNorthEastSouthWest();

	protected abstract void doTestScenarioFiveVertices();

	protected abstract void doTestScenarioFiveVerticesOneNonAccessibleEdge();

	protected abstract void doTestScenarioUnsolvable();

	protected abstract void doTestScenarioFortyoneVertices();

	protected void testScenario(Scenario scenario) {
		// Get the grid and the path
		this.grid = scenario.getGrid();
		this.path = scenario.getPath();

		// Initialize the algorithm
		this.algorithm.initialize(scenario);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if the path is valid
		Assert.assertTrue(scenario.isPathValid());
	}
}
