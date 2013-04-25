package model.algorithm.opimizer;

import java.util.LinkedList;

import model.algorithm.Algorithm;
import model.grid.Edge;

public abstract class OpimizerAlgorithm extends Algorithm {

	public abstract LinkedList<Edge> run(LinkedList<Edge> path);

}
