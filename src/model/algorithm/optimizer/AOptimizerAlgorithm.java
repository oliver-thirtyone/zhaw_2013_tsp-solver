package model.algorithm.optimizer;

import java.util.LinkedList;

import model.algorithm.Algorithm;
import model.grid.Edge;

public abstract class AOptimizerAlgorithm extends Algorithm {

	public abstract LinkedList<Edge> run(LinkedList<Edge> path);

}
