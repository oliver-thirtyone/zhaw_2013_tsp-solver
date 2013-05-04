package tspsolver.model.algorithm.optimizer;

import tspsolver.model.Scenario;

public class LinKernighanHeuristik extends AOptimizerAlgorithm {

	public LinKernighanHeuristik(Scenario scenario) {
		super(scenario);
	}

	@Override
	protected boolean doStep() {

		// TODO: magic
		// http://en.wikipedia.org/wiki/2-opt

		return false;
	}

}
