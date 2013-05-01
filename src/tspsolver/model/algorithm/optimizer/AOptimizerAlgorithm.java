package tspsolver.model.algorithm.optimizer;

import java.util.LinkedList;

import tspsolver.model.algorithm.Algorithm;
import tspsolver.model.grid.Edge;


public abstract class AOptimizerAlgorithm extends Algorithm {

	public abstract LinkedList<Edge> run(LinkedList<Edge> path);

}
