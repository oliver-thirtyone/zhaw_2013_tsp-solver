package tspsolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tspsolver.controller.runner.AlgorithmRunner;
import tspsolver.controller.runner.MainRunner;
import tspsolver.controller.scenario.IScenarioLoader;
import tspsolver.controller.scenario.xml.XMLScenarioLoader;
import tspsolver.model.Scenario;
import tspsolver.view.MainFrame;

public class Launcher {

	public static final int NUMBER_OF_ALGORITHM_RUNNERS = 2;
	public static final String SCENARIO_DIRECTORY = "data/scenario";

	public static void main(String[] args) {
		IScenarioLoader scenarioLoader = new XMLScenarioLoader();

		// Check if the scenario directory exits
		File scenarioDirectory = new File(Launcher.SCENARIO_DIRECTORY);
		if (!scenarioDirectory.exists() && !scenarioDirectory.isDirectory()) {
			System.err.println("Scenario directory does not exit: " + Launcher.SCENARIO_DIRECTORY);
			System.exit(1);
		}

		// Create the scenarios
		List<Scenario> scenarios = new ArrayList<Scenario>();
		for (File scenarioFile : scenarioDirectory.listFiles()) {
			if (scenarioFile.isDirectory()) {
				continue;
			}
			try {
				InputStream inputStream = new FileInputStream(scenarioFile);
				Scenario scenario = scenarioLoader.loadScenario(inputStream);
				scenarios.add(scenario);
			} catch (IllegalArgumentException exception) {
				exception.printStackTrace();
			} catch (FileNotFoundException exception) {
				exception.printStackTrace();
			}
		}

		// Create the algorithm runners
		List<AlgorithmRunner> algorithmRunners = new ArrayList<AlgorithmRunner>();
		for (int i = 0; i < NUMBER_OF_ALGORITHM_RUNNERS; i++) {
			algorithmRunners.add(new AlgorithmRunner());
		}

		// Create the main runner
		Scenario[] scenarioArray = scenarios.toArray(new Scenario[scenarios.size()]);
		AlgorithmRunner[] algorithmRunnerArray = algorithmRunners.toArray(new AlgorithmRunner[algorithmRunners.size()]);
		MainRunner mainRunner = new MainRunner(scenarioArray, algorithmRunnerArray);

		MainFrame mainFrame = new MainFrame(mainRunner);
		mainFrame.setVisible(true);
	}
}
