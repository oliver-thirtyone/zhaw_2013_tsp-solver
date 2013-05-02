package tspsolver.model.algorithm.optimizer;

import tspsolver.model.grid.Path;

public class TwoOptHeuristik extends AOptimizerAlgorithm {

	public TwoOptHeuristik(Path path) {
		super(path);
	}

	@Override
	protected boolean doStep() {

		// TODO: magic
		// http://en.wikipedia.org/wiki/2-opt

		return false;
	}

}
