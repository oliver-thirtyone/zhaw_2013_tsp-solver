package tspsolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tspsolver.controller.AlgorithmRunner;
import tspsolver.controller.Controller;
import tspsolver.controller.scenario.IScenarioLoader;
import tspsolver.controller.scenario.xml.XMLScenarioLoader;
import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.algorithm.optimizer.LinKernighanHeuristik;
import tspsolver.model.algorithm.optimizer.TwoOptHeuristik;
import tspsolver.model.algorithm.start.BruteForceAlgorithm;
import tspsolver.model.algorithm.start.MinimumSpanningTreeHeuristik;
import tspsolver.model.algorithm.start.NearestNeighborHeuristik;
import tspsolver.model.algorithm.start.RandomAlgorithm;
import tspsolver.model.scenario.Scenario;
import tspsolver.view.MainFrame;

public class Launcher {

	public static final int NUMBER_OF_ALGORITHM_RUNNERS = 2;
	public static final String SCENARIO_DIRECTORY = "data/scenario";

	public static void main(String[] args) {
		final IScenarioLoader scenarioLoader = new XMLScenarioLoader();

		// Check if the scenario directory exits
		final File scenarioDirectory = new File(Launcher.SCENARIO_DIRECTORY);
		if (!scenarioDirectory.exists() && !scenarioDirectory.isDirectory()) {
			System.err.println("Scenario directory does not exit: " + Launcher.SCENARIO_DIRECTORY);
			System.exit(1);
		}

		// Create the scenarios
		final List<Scenario> scenarioList = new ArrayList<Scenario>();
		for (final File scenarioFile : scenarioDirectory.listFiles()) {
			if (scenarioFile.isDirectory()) {
				continue;
			}
			try {
				final InputStream inputStream = new FileInputStream(scenarioFile);
				final Scenario scenario = scenarioLoader.loadScenario(inputStream);
				scenarioList.add(scenario);
			}
			catch (final IllegalArgumentException exception) {
				exception.printStackTrace();
			}
			catch (final FileNotFoundException exception) {
				exception.printStackTrace();
			}
		}
		final Scenario[] scenarios = scenarioList.toArray(new Scenario[scenarioList.size()]);

		// Create the algorithm runners
		final AlgorithmRunner[] algorithmRunners = new AlgorithmRunner[Launcher.NUMBER_OF_ALGORITHM_RUNNERS];
		for (int i = 0; i < Launcher.NUMBER_OF_ALGORITHM_RUNNERS; i++) {
			// Create the start-algorithms
			final StartAlgorithm[] startAlgorithms = new StartAlgorithm[4];
			startAlgorithms[0] = new BruteForceAlgorithm();
			startAlgorithms[1] = new NearestNeighborHeuristik();
			startAlgorithms[2] = new MinimumSpanningTreeHeuristik();
			startAlgorithms[3] = new RandomAlgorithm();

			// Create the optimizer-algorithms
			final OptimizerAlgorithm[] optimizerAlgorithms = new OptimizerAlgorithm[2];
			optimizerAlgorithms[0] = new TwoOptHeuristik();
			optimizerAlgorithms[1] = new LinKernighanHeuristik();

			algorithmRunners[i] = new AlgorithmRunner(startAlgorithms, optimizerAlgorithms);
		}

		// Create the main runner
		final Controller controller = new Controller(scenarios, algorithmRunners);

		// Create the main view
		final MainFrame mainFrame = new MainFrame(controller);
		mainFrame.setVisible(true);
	}
}
