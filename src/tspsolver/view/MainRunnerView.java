package tspsolver.view;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import tspsolver.controller.runner.MainRunner;
import tspsolver.controller.runner.RunnerState;
import tspsolver.model.Scenario;
import tspsolver.util.LayoutManager;

public class MainRunnerView extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 6150985298461587724L;

	private final MainRunner mainRunner;
	private final LayoutManager layoutManager;

	private final JButton initialize;
	private final JButton reset;
	private final JButton start;
	private final JButton stop;
	private final JButton step;
	private final JButton pause;

	private final JLabel modeLabel;
	private final JTextField mode;

	private final JLabel stepDelayLabel;
	private final JTextField stepDelay;

	private final JLabel scenarioLabel;
	private final JComboBox<Scenario> scenarios;

	public MainRunnerView(MainRunner mainRunner) {
		this.mainRunner = mainRunner;

		this.layoutManager = new LayoutManager(this);

		this.initialize = new JButton("initialize");
		this.initialize.setActionCommand("initialize");
		this.initialize.addActionListener(this);

		this.reset = new JButton("reset");
		this.reset.setActionCommand("reset");
		this.reset.addActionListener(this);

		this.start = new JButton("start");
		this.start.setActionCommand("start");
		this.start.addActionListener(this);

		this.stop = new JButton("stop");
		this.stop.setActionCommand("stop");
		this.stop.addActionListener(this);

		this.step = new JButton("step");
		this.step.setActionCommand("step");
		this.step.addActionListener(this);

		this.pause = new JButton("pause");
		this.pause.setActionCommand("pause");
		this.pause.addActionListener(this);

		this.modeLabel = new JLabel("Mode: ");
		this.mode = new JTextField();
		this.mode.setEditable(false);

		this.stepDelayLabel = new JLabel("Instruction delay [ms]:");
		this.stepDelay = new JTextField(15);

		this.scenarioLabel = new JLabel("Scenario:");
		this.scenarios = new JComboBox<Scenario>();

		for (Scenario scenario : this.mainRunner.getScenarios()) {
			this.scenarios.addItem(scenario);
		}
		this.scenarios.setActionCommand("select_scenario");
		this.scenarios.addActionListener(this);
		this.scenarios.setSelectedIndex(0);

		this.components();

		TitledBorder titledBorder = BorderFactory.createTitledBorder("");
		titledBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		this.setBorder(titledBorder);

		this.doUpdate(this.mainRunner.getState());
		this.mainRunner.addObserver(this);
	}

	private void components() {
		this.layoutManager.reset();
		this.layoutManager.setAnchor(GridBagConstraints.FIRST_LINE_START);
		this.layoutManager.setFill(GridBagConstraints.HORIZONTAL);
		this.layoutManager.setWeightY(0.0);
		this.layoutManager.setWeightX(0.0);

		this.layoutManager.setX(2).setY(0).addComponent(this.initialize);
		this.layoutManager.setX(3).setY(0).addComponent(this.reset);
		this.layoutManager.setX(2).setY(1).addComponent(this.start);
		this.layoutManager.setX(3).setY(1).addComponent(this.stop);
		this.layoutManager.setX(2).setY(2).addComponent(this.step);
		this.layoutManager.setX(3).setY(2).addComponent(this.pause);

		this.layoutManager.setX(0).setY(0).addComponent(this.modeLabel);
		this.layoutManager.setX(0).setY(1).addComponent(this.scenarioLabel);
		this.layoutManager.setX(0).setY(2).addComponent(this.stepDelayLabel);

		this.layoutManager.setWeightX(1.0);
		this.layoutManager.setX(1).setY(0).addComponent(this.mode);
		this.layoutManager.setX(1).setY(1).addComponent(this.scenarios);
		this.layoutManager.setX(1).setY(2).addComponent(this.stepDelay);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();

		Integer stepDelay = null;
		try {
			if (!this.stepDelay.getText().isEmpty()) {
				stepDelay = Integer.parseInt(this.stepDelay.getText());
			}
		} catch (NumberFormatException exception) {
			stepDelay = 0;
		}

		if (actionCommand.equals("initialize")) {
			this.mainRunner.initialize(stepDelay);
		}
		if (actionCommand.equals("start")) {
			this.mainRunner.start();
		} else if (actionCommand.equals("step")) {
			this.mainRunner.step();
		} else if (actionCommand.equals("pause")) {
			this.mainRunner.pause();
		} else if (actionCommand.equals("stop")) {
			this.mainRunner.stop();
		} else if (actionCommand.equals("reset")) {
			this.mainRunner.reset();
		} else if (actionCommand.equals("select_scenario")) {
			Scenario scenario = (Scenario) this.scenarios.getSelectedItem();
			this.mainRunner.setSelectedScenario(scenario);
		}
	}

	private void doUpdate(RunnerState state) {
		// Nothing else changed, our job here is done
		if (state == null)
			return;

		this.scenarios.setEditable(this.mainRunner.canInitialize());
		this.stepDelay.setEditable(this.mainRunner.canInitialize());

		this.initialize.setEnabled(this.mainRunner.canInitialize());
		this.start.setEnabled(this.mainRunner.canStart());
		this.step.setEnabled(this.mainRunner.canStep());
		this.pause.setEnabled(this.mainRunner.canPause());
		this.stop.setEnabled(this.mainRunner.canStop());
		this.reset.setEnabled(this.mainRunner.canReset());

		this.mode.setText(state.toString());

		String stepDelay = String.valueOf(this.mainRunner.getStepDelay());
		this.stepDelay.setText(stepDelay);
	}

	@Override
	public void update(Observable observable, final Object argument) {
		if (observable != mainRunner)
			return;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				RunnerState state = (argument instanceof RunnerState) ? (RunnerState) argument : null;
				MainRunnerView.this.doUpdate(state);
			}
		});
	}

}
