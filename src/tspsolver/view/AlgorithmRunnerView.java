package tspsolver.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import tspsolver.controller.AlgorithmRunner;
import tspsolver.model.algorithm.Algorithm;
import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.path.Path;
import tspsolver.util.runner.RunnerState;
import tspsolver.util.view.layout.LayoutManager;
import tspsolver.view.grid.GridView;

public class AlgorithmRunnerView extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 6150985298461587724L;

	private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss:SSS");
	{
		AlgorithmRunnerView.TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private final AlgorithmRunner algorithmRunner;
	private final LayoutManager layoutManager;

	private final JLabel startAlgorithmLabel;
	private final JComboBox<StartAlgorithm> startAlgorithms;

	private final JLabel optimizerAlgorithmLabel;
	private final JComboBox<OptimizerAlgorithm> optimizerAlgorithms;

	private final GridView gridView;

	private final JLabel runnerStateLabel;
	private final JTextField runnerState;

	private final JLabel runningAlgorithmLabel;
	private final JTextField runningAlgorithm;

	private final JLabel timeElapsedLabel;
	private final JTextField timeElapsed;

	private final JLabel stepCounterLabel;
	private final JTextField stepCounter;

	private final JLabel finishedSuccessfullyLabel;
	private final JTextField finishedSuccessfully;

	private final JLabel pathWeightLabel;
	private final JTextField pathWeight;

	private final JLabel numberOfEdgesLabel;
	private final JTextField numberOfEdges;
	private final JLabel slashLabel;
	private final JTextField numberOfNodes;

	public AlgorithmRunnerView(AlgorithmRunner algorithmRunner) {
		this.algorithmRunner = algorithmRunner;

		this.layoutManager = new LayoutManager(this);

		// Start-Algorithm
		this.startAlgorithmLabel = new JLabel("Start-Algorithm: ");
		this.startAlgorithms = new JComboBox<StartAlgorithm>();
		this.startAlgorithms.setEditable(false);

		for (final StartAlgorithm startAlgorithm : this.algorithmRunner.getStartAlgorithms()) {
			this.startAlgorithms.addItem(startAlgorithm);
		}
		this.startAlgorithms.setActionCommand("select_startAlgorithm");
		this.startAlgorithms.addActionListener(this);

		// Optimizer-Algorithm
		this.optimizerAlgorithmLabel = new JLabel("Optimizer-Algorithm: ");
		this.optimizerAlgorithms = new JComboBox<OptimizerAlgorithm>();
		this.optimizerAlgorithms.setEditable(false);

		this.optimizerAlgorithms.addItem(null);
		for (final OptimizerAlgorithm optimizerAlgorithm : this.algorithmRunner.getOptimizerAlgorithms()) {
			this.optimizerAlgorithms.addItem(optimizerAlgorithm);
		}
		this.optimizerAlgorithms.setActionCommand("select_optimizerAlgorithm");
		this.optimizerAlgorithms.addActionListener(this);

		// Grid view
		this.gridView = new GridView();
		this.gridView.setPreferredSize(new Dimension(GridView.MAP_SWITZERLAND_WIDTH, GridView.MAP_SWITZERLAND_HEIGHT));

		// Runner state
		this.runnerStateLabel = new JLabel("Runner state: ");
		this.runnerState = new JTextField();
		this.runnerState.setEditable(false);

		// Running algorithm
		this.runningAlgorithmLabel = new JLabel("Running algorithm: ");
		this.runningAlgorithm = new JTextField();
		this.runningAlgorithm.setEditable(false);

		// Finished successfully
		this.finishedSuccessfullyLabel = new JLabel("Finished successfully: ");
		this.finishedSuccessfully = new JTextField();
		this.finishedSuccessfully.setEditable(false);

		// Step counter
		this.stepCounterLabel = new JLabel("Step counter: ");
		this.stepCounter = new JTextField();
		this.stepCounter.setEditable(false);

		// Elapsed time
		this.timeElapsedLabel = new JLabel("Elapsed time: ");
		this.timeElapsed = new JTextField();
		this.timeElapsed.setEditable(false);

		// Path weight
		this.pathWeightLabel = new JLabel("Path weight: ");
		this.pathWeight = new JTextField();
		this.pathWeight.setEditable(false);

		// Number of edges
		this.numberOfEdgesLabel = new JLabel("Number of edges: ");
		this.numberOfEdges = new JTextField();
		this.numberOfEdges.setEditable(false);
		this.slashLabel = new JLabel("/");
		this.numberOfNodes = new JTextField();
		this.numberOfNodes.setEditable(false);

		// Create the components
		this.components();

		// Create the titled border
		final TitledBorder titledBorder = BorderFactory.createTitledBorder("");
		titledBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		this.setBorder(titledBorder);

		// Initialize
		this.doUpdate(null);
		this.algorithmRunner.addObserver(this);
	}

	private void components() {
		this.layoutManager.reset();
		this.layoutManager.setAnchor(GridBagConstraints.FIRST_LINE_START);
		this.layoutManager.setFill(GridBagConstraints.HORIZONTAL);
		this.layoutManager.setWeightY(0.0);
		this.layoutManager.setWeightX(0.0);

		this.layoutManager.setX(0).setY(0).addComponent(this.startAlgorithmLabel);
		this.layoutManager.setX(0).setY(1).addComponent(this.optimizerAlgorithmLabel);

		this.layoutManager.setX(0).setY(3).addComponent(this.runnerStateLabel);
		this.layoutManager.setX(0).setY(4).addComponent(this.runningAlgorithmLabel);
		this.layoutManager.setX(0).setY(5).addComponent(this.timeElapsedLabel);
		this.layoutManager.setX(0).setY(6).addComponent(this.stepCounterLabel);
		this.layoutManager.setX(0).setY(7).addComponent(this.finishedSuccessfullyLabel);
		this.layoutManager.setX(0).setY(8).addComponent(this.pathWeightLabel);
		this.layoutManager.setX(0).setY(9).addComponent(this.numberOfEdgesLabel);
		this.layoutManager.setX(2).setY(9).addComponent(this.slashLabel);

		this.layoutManager.setWeightX(1.0);

		this.layoutManager.setX(1).setY(9).addComponent(this.numberOfEdges);
		this.layoutManager.setX(3).setY(9).addComponent(this.numberOfNodes);

		this.layoutManager.setWidth(3);

		this.layoutManager.setX(1).setY(0).addComponent(this.startAlgorithms);
		this.layoutManager.setX(1).setY(1).addComponent(this.optimizerAlgorithms);

		this.layoutManager.setX(1).setY(3).addComponent(this.runnerState);
		this.layoutManager.setX(1).setY(4).addComponent(this.runningAlgorithm);
		this.layoutManager.setX(1).setY(5).addComponent(this.timeElapsed);
		this.layoutManager.setX(1).setY(6).addComponent(this.stepCounter);
		this.layoutManager.setX(1).setY(7).addComponent(this.finishedSuccessfully);
		this.layoutManager.setX(1).setY(8).addComponent(this.pathWeight);

		this.layoutManager.setWeightY(1.0).setWidth(4);
		this.layoutManager.setFill(GridBagConstraints.BOTH);
		this.layoutManager.setX(0).setY(2).addComponent(this.gridView);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		final String actionCommand = actionEvent.getActionCommand();

		if (actionCommand.equals("select_startAlgorithm")) {
			final StartAlgorithm startAlgorithm = (StartAlgorithm) this.startAlgorithms.getSelectedItem();
			this.algorithmRunner.setSelectedStartAlgorithm(startAlgorithm);
		}
		else if (actionCommand.equals("select_optimizerAlgorithm")) {
			final Object selectedItem = this.optimizerAlgorithms.getSelectedItem();
			OptimizerAlgorithm optimizerAlgorithm = null;

			if (selectedItem instanceof OptimizerAlgorithm) {
				optimizerAlgorithm = (OptimizerAlgorithm) this.optimizerAlgorithms.getSelectedItem();
			}
			this.algorithmRunner.setSelectedOptimizerAlgorithm(optimizerAlgorithm);
		}
	}

	private void doUpdate(Object argument) {
		// The runner step-counter changed
		if (argument == null || argument instanceof Long) {
			// Update the runner statistics
			this.timeElapsed.setText(AlgorithmRunnerView.TIME_FORMAT.format(new Date(this.algorithmRunner.getTimeElapsed())));
			this.stepCounter.setText(String.valueOf(this.algorithmRunner.getStepCounter()));

			// Update the algorithm statistics
			final Algorithm runningAlgorithm = this.algorithmRunner.getRunningAlgorithm();
			if (runningAlgorithm != null) {
				this.finishedSuccessfully.setText(String.valueOf(runningAlgorithm.hasFinishedSuccessfully()));
			}
			else {
				this.finishedSuccessfully.setText(String.valueOf(false));
			}

			// Update the scenario statistics
			final Scenario scenario = this.algorithmRunner.getSelectedScenario();
			if (scenario != null) {
				final Path path = scenario.getPath();
				this.pathWeight.setText(String.valueOf(path.getWeight()));
				this.numberOfEdges.setText(String.valueOf(path.getNumberOfEdges()));

			}
			else {
				this.pathWeight.setText(String.valueOf(0.0));
				this.numberOfEdges.setText(String.valueOf(0));
			}
		}

		// The runner state changed
		if (argument == null || argument instanceof RunnerState) {
			// Update the comboboxes
			this.startAlgorithms.setEnabled(this.algorithmRunner.canInitialize());
			this.optimizerAlgorithms.setEnabled(this.algorithmRunner.canInitialize());

			// Update the runner statistics
			this.runnerState.setText(this.algorithmRunner.getState().toString());
		}

		// The running algorithm changed
		if (argument == null || argument instanceof Algorithm) {
			final Algorithm runningAlgorithm = this.algorithmRunner.getRunningAlgorithm();
			if (runningAlgorithm != null) {
				this.runningAlgorithm.setText(runningAlgorithm.toString());
			}
			else {
				this.runningAlgorithm.setText("");
			}
		}

		// The scenario changed
		if (argument == null || argument instanceof Scenario) {
			final Scenario scenario = this.algorithmRunner.getSelectedScenario();
			this.gridView.updateScenario(scenario);

			if (scenario != null) {
				final Grid grid = scenario.getGrid();
				this.numberOfNodes.setText(String.valueOf(grid.getNumberOfNodes()));
			}
			else {
				this.numberOfNodes.setText(String.valueOf(0));
			}
		}

	}

	@Override
	public void update(Observable observable, final Object argument) {
		if (observable != this.algorithmRunner) {
			return;
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AlgorithmRunnerView.this.doUpdate(argument);
			}
		});
	}
}
