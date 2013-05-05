package tspsolver.view;

import java.awt.GridBagConstraints;

import javax.swing.JFrame;

import tspsolver.controller.runner.MainRunner;
import tspsolver.util.LayoutManager;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -5157138756987495470L;

	private final LayoutManager layoutManager;

	// private final MemoryView memoryView;
	private final MainRunnerView runnerView;

	// private final ProcessorView processorBinaryView;
	// private final ProcessorView processorDecimalView;

	public MainFrame(MainRunner mainRunner) {
		super("TSP Solver");
		this.layoutManager = new LayoutManager(this.getContentPane());

		this.runnerView = new MainRunnerView(mainRunner);
		// this.memoryView = new MemoryView(runner.getMemory(), runner.getProcessor().getInstructionDecoder());
		// this.processorBinaryView = new ProcessorView(runner.getProcessor(), Boolean.TRUE);
		// this.processorDecimalView = new ProcessorView(runner.getProcessor(), Boolean.FALSE);

		this.components();
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void components() {
		this.layoutManager.reset();
		this.layoutManager.setAnchor(GridBagConstraints.NORTHWEST);
		this.layoutManager.setFill(GridBagConstraints.HORIZONTAL);
		this.layoutManager.setWeightX(1.0).setWeightY(0.0);

		this.layoutManager.setX(0).setY(0).setWidth(2).addComponent(this.runnerView);
		// this.layoutManager.setX(1).setY(1).addComponent(this.processorBinaryView);
		// this.layoutManager.setX(1).setY(2).addComponent(this.processorDecimalView);

		// this.layoutManager.setFill(GridBagConstraints.BOTH);
		// this.layoutManager.setWeightX(1.0).setWeightY(1.0);
		// this.layoutManager.setX(0).setY(1).setWidth(1).setHeight(2).addComponent(this.memoryView);
	}

}
