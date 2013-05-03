package tspsolver.model.algorithm.optimizer;

import tspsolver.model.algorithm.Algorithm;
import tspsolver.model.path.Path;

public abstract class AOptimizerAlgorithm extends Algorithm {

	public AOptimizerAlgorithm(Path path) {
		super(path);
	}

	@Override
	public final void validateArguments() {
		boolean validArguments = true;

		// TODO: validate the path: Entspricht er den Vorgaben eines TSP-Pfades

		this.setValidArguments(validArguments);
	}

}
