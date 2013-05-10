package tspsolver.model.algorithm.start;

import java.util.Vector;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public class BruteForceAlgorithm extends StartAlgorithm {

	private final Vector<Node> startPath;
	private final Vector<Node> currentPath;

	public BruteForceAlgorithm() {
		this.startPath = new Vector<Node>();
		this.currentPath = new Vector<Node>();
	}

	@Override
	protected void doInitialize() {
		this.getPathUpdater().addPath(new Path());

		// convert to node path
		Node startingNode = this.getStartingNode();

		for (Node node : this.getGrid().getNodes()) {
			if (startingNode != node) {
				this.startPath.add(node);
			}
		}

		this.currentPath.addAll(this.startPath);
	}

	@Override
	protected void doReset() {
		this.startPath.clear();
		this.currentPath.clear();
	}

	@Override
	protected boolean doStep() {
		boolean successfulStep = true;
		boolean isLastPermutation = this.next_permutation();

		// Create the new path
		Path newPath = this.convertToPath(this.currentPath);

		// Check if the new path is lighter
		if (this.getPath().isEmpty() || (!newPath.isEmpty() && newPath.getWeight() < this.getPath().getWeight())) {
			this.getPathUpdater().clearPath();
			this.getPathUpdater().addPath(newPath);
		}

		// Check if we are finished
		if (isLastPermutation) {
			if (!this.getPath().isEmpty()) {
				this.finishedSuccessfully();
			}
			else {
				successfulStep = false;
			}
		}

		this.getPathUpdater().updatePath();
		return successfulStep;
	}

	private boolean next_permutation() {

		// letztes Element; man arbeitet sich von hinten nach vorne durch
		int i = this.currentPath.size() - 1; //

		// keine Endlosschleife, da i dekrementiert wird und damit irgendwann 0
		// wird
		while (true) {
			if (i <= 0) {
				// a ist letzte Permutation
				return true;
			}
			i -= 1;
			// FIXME: Dies könnte optimiert werden jedes Node müsste einen
			// spezfische reihenfolge, ändlich dem eines alphabet, die grösse
			// funktioniert nicht weil nicht eindeutig
			// lexikogr. Nachfolger hat größeres a[i]
			if (this.startPath.indexOf(this.currentPath.get(i)) < this.startPath.indexOf(this.currentPath.get(i + 1))) {
				break;
			}
		}
		int j = this.currentPath.size();
		while (true) {
			j -= 1;
			if (this.startPath.indexOf(this.currentPath.get(i)) < this.startPath.indexOf(this.currentPath.get(j))) {
				break;
			}
		}

		this.swap(i, j);
		// sortiere aufsteigend zwischen a[i] und Ende
		// zur Zeit absteigend sortiert => invertieren

		i += 1;
		j = this.currentPath.size() - 1;

		while (i < j) {
			this.swap(i, j);
			i += 1;
			j -= 1;
		}

		// eine weitere Permutation gefunden
		return false;
	}

	private void swap(int first, int second) {
		Node temp = this.currentPath.get(first);

		this.currentPath.set(first, this.currentPath.get(second));

		this.currentPath.set(second, temp);
	}

	private Path convertToPath(Vector<Node> nodes) {
		Path newPath = new Path();
		PathUpdater newPathUpdater = new PathUpdater(newPath);

		boolean isNewPathValid = true;
		Node currentNode = this.getStartingNode();

		// Link all nodes
		for (Node node : nodes) {
			Edge edge = currentNode.getEdgeToNode(node);

			if (edge != null) {
				newPathUpdater.addEdge(edge);
				currentNode = node;
			}
			else {
				isNewPathValid = false;
			}
		}

		// Link the last node with the starting node
		Edge edge = currentNode.getEdgeToNode(this.getStartingNode());
		if (edge != null) {
			newPathUpdater.addEdge(edge);
		}
		else {
			isNewPathValid = false;
		}

		// Update the path if it is valid
		if (isNewPathValid) {
			newPathUpdater.updatePath();
		}

		return newPath;
	}
}
