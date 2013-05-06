package tspsolver.view;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import tspsolver.controller.AlgorithmRunner;
import tspsolver.controller.Controller;
import tspsolver.util.view.layout.LayoutManager;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -5157138756987495470L;

	private final LayoutManager layoutManager;

	private final ControllerView mainRunnerView;
	private final List<AlgorithmRunnerView> algorithmRunnerViews;

	public MainFrame(Controller controller) {
		super("TSP Solver");
		this.layoutManager = new LayoutManager(this.getContentPane());

		this.mainRunnerView = new ControllerView(controller);
		this.algorithmRunnerViews = new ArrayList<AlgorithmRunnerView>();

		for (AlgorithmRunner algorithmRunner : controller.getAlgorithmRunners()) {
			AlgorithmRunnerView algorithmRunnerView = new AlgorithmRunnerView(algorithmRunner);
			this.algorithmRunnerViews.add(algorithmRunnerView);
		}

		this.components();
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void components() {
		this.layoutManager.reset();
		this.layoutManager.setAnchor(GridBagConstraints.NORTHWEST);
		this.layoutManager.setFill(GridBagConstraints.BOTH);
		this.layoutManager.setWeightX(1.0).setWeightY(1.0);

		this.layoutManager.setY(1);
		for (int x = 0; x < this.algorithmRunnerViews.size(); x++) {
			this.layoutManager.setX(x).addComponent(this.algorithmRunnerViews.get(x));
		}

		this.layoutManager.setFill(GridBagConstraints.HORIZONTAL);
		this.layoutManager.setWeightY(0.0).setWidth(this.algorithmRunnerViews.size());
		this.layoutManager.setX(0).setY(0).addComponent(this.mainRunnerView);

	}
}
