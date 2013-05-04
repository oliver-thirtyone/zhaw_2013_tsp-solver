package tspsolver.view.grid;

import java.awt.GridBagConstraints;
import java.io.InputStream;

import javax.swing.JFrame;

import tspsolver.controller.Runner;
import tspsolver.controller.scenario.XMLScenarioLoader;
import tspsolver.model.Scenario;
import tspsolver.model.algorithm.Algorithm;
import tspsolver.model.algorithm.start.NearestNeighborHeuristik;
import tspsolver.util.LayoutManager;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

public class TestGridView extends JFrame {

	private static final long serialVersionUID = 4966251849003014687L;

	private final LayoutManager layoutManager;

	private final GridView gridView;

	public TestGridView(Scenario scenario) {
		super("TestGridView");

		this.layoutManager = new LayoutManager(this.getContentPane());

		this.gridView = new GridView(scenario);

		this.components();
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void components() {
		this.layoutManager.reset();
		this.layoutManager.setAnchor(GridBagConstraints.CENTER);
		this.layoutManager.setFill(GridBagConstraints.BOTH);
		this.layoutManager.setWeightX(1.0).setWeightY(1.0);

		this.layoutManager.setX(0).setY(0).addComponent(this.gridView);
	}

	public static void main(String[] args) throws Exception {
		Scenario scenario = new Scenario();

		TestGridView testGridView = new TestGridView(scenario);
		testGridView.setSize(800, 600);
		testGridView.setLocationRelativeTo(null);
		testGridView.setVisible(true);

		// Load a scenario
		XMLScenarioLoader scenarioLoader = new XMLScenarioLoader();
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("tspsolver/data/scenario/test_north_south_east_west.xml");
		scenarioLoader.loadScenario(inputStream, scenario);

		// Run an algorithm
		Algorithm algorithm = new NearestNeighborHeuristik(scenario);
		Runner runner = new Runner(algorithm);
		runner.initialize(2000); // Initialize the runner with a "2 seconds"-step delay
		runner.start();
	}
}
