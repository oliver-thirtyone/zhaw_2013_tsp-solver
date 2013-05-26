package tspsolver.model.algorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.Assert;
import tspsolver.controller.scenario.xml.XMLScenarioLoader;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.validator.TSPValidator;
import tspsolver.model.validator.Validator;

public abstract class OptimizerAlgorithmHardcoreTest {

	public static final String SCENARIO_DIRECTORY = "data.test.hardcore/scenario/";

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss:SSS");
	{
		OptimizerAlgorithmHardcoreTest.TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private static final XMLScenarioLoader SCENARIO_LOADER = new XMLScenarioLoader();
	private static final Validator VALIDATOR = new TSPValidator();

	protected StartAlgorithm startAlgorithm;
	protected OptimizerAlgorithm optimizerAlgorithm;

	protected void testScenario(int numberOfVertices, String scenarioFile) {
		// Create the scenario
		Scenario scenario = new Scenario(VALIDATOR);

		System.out.println();
		System.out.println("--------------------------------------------------");
		System.out.println(this.startAlgorithm.toString() + " + " + this.optimizerAlgorithm.toString() + ": " + numberOfVertices + " vertices");
		System.out.println("--------------------------------------------------");

		// Load the scenario
		System.out.println("Load the scenario...");
		this.loadScenario(scenario, scenarioFile);

		// Initialize the algorithm
		System.out.println("Initialize the algorithm...");
		this.startAlgorithm.initialize(scenario);
		Assert.assertTrue(this.startAlgorithm.hasValidArguments());

		// Step through the algorithms
		System.out.println("Step through the algorithms...");
		long timeStarted = System.currentTimeMillis();
		do {
			Assert.assertTrue(this.startAlgorithm.step());
		} while (!this.startAlgorithm.hasFinishedSuccessfully());
		this.optimizerAlgorithm.initialize(scenario);
		do {
			Assert.assertTrue(this.optimizerAlgorithm.step());
		} while (!this.optimizerAlgorithm.hasFinishedSuccessfully());
		long timeStopped = System.currentTimeMillis();

		// Check if the path is valid
		Assert.assertTrue(scenario.isPathValid());

		// Print the time elapsed
		long timeElapsed = timeStopped - timeStarted;
		String timeElapsedFormated = OptimizerAlgorithmHardcoreTest.TIME_FORMAT.format(new Date(timeElapsed));
		System.out.println("Elapsed time: " + timeElapsedFormated);

		// Print the weight of the path
		System.out.println("Path weight: " + scenario.getPath().getWeight());

		System.out.println("--------------------------------------------------");
	}

	private void loadScenario(Scenario scenario, String scenarioFile) {
		try {
			SCENARIO_LOADER.loadScenario(scenario, new FileInputStream(SCENARIO_DIRECTORY + scenarioFile));
		}
		catch (IllegalArgumentException exception) {
			exception.printStackTrace();
		}
		catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}
	}
}
