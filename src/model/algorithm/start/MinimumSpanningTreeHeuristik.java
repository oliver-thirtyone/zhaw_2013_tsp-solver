package model.algorithm.start;

import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import model.grid.Edge;
import model.grid.Node;

public class MinimumSpanningTreeHeuristik extends StartAlgorithm {

	@Override
	public LinkedList<Edge> run(Set<Node> nodes, Node startingNode) {

		this.calcSpanningTree(nodes, startingNode);

		return this.doEulerianTrail();

	}

	private void calcSpanningTree(Set<Node> nodes, Node startingNode) {

		// TODO: Implement: Get all eages and sort them by length
		SortedMap<Double, Node> nodesSorted = new TreeMap<Double, Node>();

		for (Node node : nodes) {

		}

		// TODO: Implement: Take so long short eages while all nodes are visited, skip eages where no new eages contains
	}

	private LinkedList<Edge> doEulerianTrail() {

		// TODO: Implement

		return this.currentPath;
	}

}
