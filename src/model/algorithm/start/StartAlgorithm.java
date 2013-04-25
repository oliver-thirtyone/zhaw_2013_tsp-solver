package model.algorithm.start;

import java.util.LinkedList;
import java.util.Set;

import model.algorithm.Algorithm;
import model.grid.Edge;
import model.grid.Node;

public abstract class StartAlgorithm extends Algorithm {

	// TODO: validate before run....
	
	public abstract LinkedList<Edge> run(Set<Node> nodes, Node startingNode);

}
