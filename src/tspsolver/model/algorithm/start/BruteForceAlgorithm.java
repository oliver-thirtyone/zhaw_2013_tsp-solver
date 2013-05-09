package tspsolver.model.algorithm.start;

import java.util.Vector;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public class BruteForceAlgorithm extends StartAlgorithm {

	private Path lightestPath;
	private Vector<Node> startPath;
	private Vector<Node> currentPath;

	public BruteForceAlgorithm() {

		this.startPath = new Vector<Node>();
		this.currentPath = new Vector<Node>();
		this.lightestPath = new Path();

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

		this.currentPath = (Vector<Node>) this.startPath.clone();

	}

	@Override
	protected void doReset() {

		this.startPath.clear();
		this.currentPath.clear();

	}

	@Override
	protected boolean doStep() {

		boolean isNotLastPermutation = next_permutation();

		Path newPath = convertToPath(this.currentPath);

		this.getPathUpdater().clearPath();
		this.getPathUpdater().addPath(this.lightestPath);
		this.getPathUpdater().addPath(newPath);

		if (newPath.getWeight() < this.lightestPath.getWeight()
				|| this.lightestPath.getWeight() == 0.0) {
			this.lightestPath = newPath;
		}

		if (isNotLastPermutation == false) {

			this.getPathUpdater().clearPath();
			this.getPathUpdater().addPath(this.lightestPath);

			this.finishedSuccessfully();
		}

		this.getPathUpdater().updatePath();
		return true;
	}

	private boolean next_permutation() {

		// letztes Element; man arbeitet sich von hinten nach vorne durch
		int i = this.currentPath.size() - 1; //

		// keine Endlosschleife, da i dekrementiert wird und damit irgendwann 0
		// wird
		while (true) {
			if (i <= 0) {
				// a ist letzte Permutation
				return false;
			}
			i -= 1;
			// FIXME: Dies könnte optimiert werden jedes Node müsste einen
			// spezfische reihenfolge, ändlich dem eines alphabet, die grösse
			// funktioniert nicht weil nicht eindeutig
			// lexikogr. Nachfolger hat größeres a[i]
			if (this.startPath.indexOf(this.currentPath.get(i)) < this.startPath
					.indexOf(this.currentPath.get(i + 1))) {
				break;
			}
		}
		int j = this.currentPath.size();
		while (true) {
			j -= 1;
			if (this.startPath.indexOf(this.currentPath.get(i)) < this.startPath
					.indexOf(this.currentPath.get(j))) {
				break;
			}
		}

		swap(i, j);
		// sortiere aufsteigend zwischen a[i] und Ende
		// zur Zeit absteigend sortiert => invertieren

		i += 1;
		j = this.currentPath.size() - 1;

		while (i < j) {
			swap(i, j);
			i += 1;
			j -= 1;
		}
		return true;
		// eine weitere Permutation gefunden
	}

	private void swap(int first, int second) {
		Node temp = this.currentPath.get(first);

		this.currentPath.set(first, this.currentPath.get(second));

		this.currentPath.set(second, temp);
	}

	private Path convertToPath(Vector<Node> nodes) {

		Node currentNode = this.getStartingNode();

		PathUpdater pathUpdater = new PathUpdater(new Path());

		for (Node node : nodes) {

			Edge edge = currentNode.getEdgeToNode(node);
			if (edge == null) {
				// FIXME: this path does not work, what do we do now?
				throw new IllegalStateException();
			}

			pathUpdater.addEdge(edge);
			
			currentNode = node;
		}

		Edge edge = currentNode.getEdgeToNode(this.getStartingNode());
		if (edge == null) {
			// FIXME: this path does not work, what do we do now?
			throw new IllegalStateException();
		}
		
		pathUpdater.addEdge(edge);
		
		pathUpdater.updatePath();
		
		return pathUpdater.getPath();
	}

}
