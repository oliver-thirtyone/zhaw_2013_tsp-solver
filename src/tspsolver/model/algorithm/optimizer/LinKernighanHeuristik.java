package tspsolver.model.algorithm.optimizer;

import tspsolver.model.path.Path;

public class LinKernighanHeuristik extends AOptimizerAlgorithm {

	public LinKernighanHeuristik(Path path) {
		super(path);
	}

	@Override
	protected boolean doStep() {

		// TODO: magic
		// http://en.wikipedia.org/wiki/2-opt

		return false;
	}

}
