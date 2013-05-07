package tspsolver.view.grid;

import java.awt.GridBagConstraints;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JFrame;

import org.junit.Assert;

import tspsolver.controller.AlgorithmRunner;
import tspsolver.controller.scenario.xml.XMLScenarioLoader;
import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.algorithm.start.NearestNeighborHeuristik;
import tspsolver.model.scenario.Scenario;
import tspsolver.util.view.layout.LayoutManager;

public class TestGridView extends JFrame {

	private static final long serialVersionUID = 4966251849003014687L;

	private final LayoutManager layoutManager;

	private final GridView gridView;

	public TestGridView(Scenario scenario) {
		super("TestGridView");

		this.layoutManager = new LayoutManager(this.getContentPane());

		this.gridView = new GridView();

		this.components();
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public GridView getGridView() {
		return this.gridView;
	}

	private void components() {
		this.layoutManager.reset();
		this.layoutManager.setAnchor(GridBagConstraints.CENTER);
		this.layoutManager.setFill(GridBagConstraints.BOTH);
		this.layoutManager.setWeightX(1.0).setWeightY(1.0);

		this.layoutManager.setX(0).setY(0).addComponent(this.gridView);
	}

	public static void main(String[] args) throws Exception {
		// Load a scenario
		final XMLScenarioLoader scenarioLoader = new XMLScenarioLoader();
		final InputStream inputStream = new FileInputStream("data/scenario/test_north_south_east_west.xml");
		final Scenario scenario = scenarioLoader.loadScenario(inputStream);

		final TestGridView testGridView = new TestGridView(scenario);
		testGridView.setSize(800, 600);
		testGridView.setLocationRelativeTo(null);
		testGridView.setVisible(true);

		// Run an algorithm
		final StartAlgorithm algorithm = new NearestNeighborHeuristik();
		final AlgorithmRunner runner = new AlgorithmRunner(new StartAlgorithm[] { algorithm }, new OptimizerAlgorithm[] {});

		// Scenario copyOfScenario = scenario;
		final Scenario copyOfScenario = scenario.copy();

		// This does not work:
		// Scenario copyOfScenario = (Scenario) PipedDeepCopy.copy(scenario);

		Assert.assertEquals(scenario, copyOfScenario);
		testGridView.getGridView().updateScenario(copyOfScenario);

		runner.setSelectedScenario(copyOfScenario);
		runner.setSelectedStartAlgorithm(algorithm);
		runner.initialize(2000); // Initialize the runner with a "2 seconds"-step delay
		runner.start();
	}
}
