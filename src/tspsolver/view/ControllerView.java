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

import tspsolver.controller.Controller;
import tspsolver.model.scenario.Scenario;
import tspsolver.util.runner.Runner;
import tspsolver.util.view.layout.LayoutManager;

public class ControllerView extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 6150985298461587724L;

	private final Controller controller;
	private final LayoutManager layoutManager;

	private final JButton initialize;
	private final JButton reset;
	private final JButton start;
	private final JButton stop;
	private final JButton step;
	private final JButton pause;

	private final JLabel stepDelayLabel;
	private final JTextField stepDelay;

	private final JLabel scenarioLabel;
	private final JComboBox<Scenario> scenarios;

	public ControllerView(Controller mainRunner) {
		this.controller = mainRunner;

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

		this.stepDelayLabel = new JLabel("Instruction delay [ms]:");
		this.stepDelay = new JTextField(15);

		this.scenarioLabel = new JLabel("Scenario:");
		this.scenarios = new JComboBox<Scenario>();
		this.scenarios.setEditable(false);

		for (Scenario scenario : this.controller.getScenarios()) {
			this.scenarios.addItem(scenario);
		}
		this.scenarios.setActionCommand("select_scenario");
		this.scenarios.addActionListener(this);
		this.scenarios.setSelectedIndex(0);

		this.components();

		TitledBorder titledBorder = BorderFactory.createTitledBorder("");
		titledBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		this.setBorder(titledBorder);

		this.doUpdate(null);
		this.controller.addObserver(this);
	}

	private void components() {
		this.layoutManager.reset();
		this.layoutManager.setAnchor(GridBagConstraints.FIRST_LINE_START);
		this.layoutManager.setFill(GridBagConstraints.HORIZONTAL);
		this.layoutManager.setWeightY(0.0);
		this.layoutManager.setWeightX(0.0);

		this.layoutManager.setX(0).setY(0).addComponent(this.scenarioLabel);
		this.layoutManager.setX(0).setY(1).addComponent(this.stepDelayLabel);

		this.layoutManager.setWeightX(1.0);
		this.layoutManager.setX(1).setY(1).addComponent(this.stepDelay);
		this.layoutManager.setX(2).setY(1).addComponent(this.initialize);
		this.layoutManager.setX(3).setY(1).addComponent(this.start);
		this.layoutManager.setX(4).setY(1).addComponent(this.step);
		this.layoutManager.setX(5).setY(1).addComponent(this.pause);
		this.layoutManager.setX(6).setY(1).addComponent(this.stop);
		this.layoutManager.setX(7).setY(1).addComponent(this.reset);

		this.layoutManager.setWidth(7);
		this.layoutManager.setX(1).setY(0).addComponent(this.scenarios);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		final String actionCommand = actionEvent.getActionCommand();

		// Turn the stepDelay into an integer
		int number = 0;
		try {
			if (!this.stepDelay.getText().isEmpty()) {
				number = Integer.parseInt(this.stepDelay.getText());
			}
		} catch (NumberFormatException exception) {
			number = Runner.DEFAULT_STEP_DELAY;
		}
		final int stepDelay = number;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (actionCommand.equals("select_scenario")) {
					Scenario scenario = (Scenario) ControllerView.this.scenarios.getSelectedItem();
					ControllerView.this.controller.setSelectedScenario(scenario);
				} else if (actionCommand.equals("initialize")) {
					ControllerView.this.controller.initialize(stepDelay);
				} else if (actionCommand.equals("start")) {
					ControllerView.this.controller.start();
				} else if (actionCommand.equals("step")) {
					ControllerView.this.controller.step();
				} else if (actionCommand.equals("pause")) {
					ControllerView.this.controller.pause();
				} else if (actionCommand.equals("stop")) {
					ControllerView.this.controller.stop();
				} else if (actionCommand.equals("reset")) {
					ControllerView.this.controller.reset();
				}
			}
		});
	}

	private void doUpdate(Object argument) {
		String stepDelay = String.valueOf(this.controller.getStepDelay());
		this.stepDelay.setText(stepDelay);

		this.scenarios.setEnabled(this.controller.canInitialize());
		this.stepDelay.setEditable(this.controller.canInitialize());

		this.initialize.setEnabled(this.controller.canInitialize());
		this.start.setEnabled(this.controller.canStart());
		this.step.setEnabled(this.controller.canStep());
		this.pause.setEnabled(this.controller.canPause());
		this.stop.setEnabled(this.controller.canStop());
		this.reset.setEnabled(this.controller.canReset());
	}

	@Override
	public void update(Observable observable, final Object argument) {
		if (observable != this.controller)
			return;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ControllerView.this.doUpdate(argument);
			}
		});
	}

}
