package tspsolver.model.algorithm.start;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tspsolver.model.Scenario;
import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Node;
import tspsolver.model.grid.comparators.EdgeWeightComparator;

public class MinimumSpanningTreeHeuristik extends AStartAlgorithm {

	private enum Phase {
		CREATE_SPANNING_TREE, DO_EULERIAN_TRAIL
	}

	private Phase phase;

	private Vector<Edge> spanningTreeEdges;
	private Set<Node> spanningTreeNodes;
	private TreeSet<Edge> spanningTreePossibleEdges;

	private Node currentNode;
	private Stack<Node> brancheNodes;

	public MinimumSpanningTreeHeuristik(Scenario scenario) {
		super(scenario);

		this.initSpanningTree();
	}

	@Override
	public boolean doStep() {

		switch (this.phase) {
		case CREATE_SPANNING_TREE:
			return this.doStepCreateSpanningTree();

		case DO_EULERIAN_TRAIL:
			return this.doStepEulerianTrail();

		default:
			throw new NotImplementedException();
		}

	}

	private void initSpanningTree() {

		this.spanningTreeEdges = new Vector<Edge>();

		this.spanningTreeNodes = new HashSet<Node>();
		this.spanningTreeNodes.add(this.getStartingNode());

		// Sort all possible edges by weight
		this.spanningTreePossibleEdges = new TreeSet<Edge>(new EdgeWeightComparator());
		for (Edge edge : this.getStartingNode().getEdges()) {
			this.spanningTreePossibleEdges.add(edge);
		}

		this.phase = Phase.CREATE_SPANNING_TREE;
	}

	private boolean doStepCreateSpanningTree() {

		boolean successfulStep = true;

		// Take the lowest one, that not build a circle and add it to the tree.
		for (Edge edge : this.spanningTreePossibleEdges) {

			if (this.spanningTreeNodes.contains(edge.getFirstNode()) == false) {

				// Add the edge to the spanning tree.
				this.spanningTreeEdges.add(edge);
				this.getPathUpdater().addEdge(edge);

				if (this.spanningTreeEdges.size() < this.getGrid().getNumberOfNodes() - 1) {

					// Prepare for next step.
					this.spanningTreeNodes.add(edge.getFirstNode());

					// Add all new possible edges
					// FIXME: Bin nicht sicher ob dieser Schritt richtig
					// funktioniert, ich gehe davon aus das in einem TreeSet
					// jeweils nur eine Instanz des selben Edges drin sein kann.
					for (Edge edgeToAdd : edge.getFirstNode().getEdges()) {
						this.spanningTreePossibleEdges.add(edgeToAdd);
					}

					// The edge is used now.
					this.spanningTreePossibleEdges.remove(edge);

				} else {
					// finish create spanning tree
					this.initEulerianTrail();
				}

				break;

			} else if (this.spanningTreeNodes.contains(edge.getSecondNode()) == false) {

				// Add the edge to the spanning tree.
				this.spanningTreeEdges.add(edge);
				this.getPathUpdater().addEdge(edge);

				if (this.spanningTreeEdges.size() < this.getGrid().getNumberOfNodes() - 1) {

					// Prepare for next step.
					this.spanningTreeNodes.add(edge.getSecondNode());

					// Add all new possible edges
					// FIXME: Bin nicht sicher ob dieser Schritt richtig
					// funktioniert, ich gehe davon aus das in einem TreeSet
					// jeweils nur eine Instanz des selben Edges drin sein kann.
					for (Edge edgeToAdd : edge.getSecondNode().getEdges()) {
						this.spanningTreePossibleEdges.add(edgeToAdd);
					}

					// The edge is used now.
					this.spanningTreePossibleEdges.remove(edge);

				} else {
					// finish create spanning tree
					this.initEulerianTrail();
				}

				break;

			} else {
				// This edge will build a circle, remove it to improve
				// performance
				// FIXME: Alternative zu diesem Schritt währe, dass man die
				// jeweiligen Kanten, welche einen Kreis bilden, nicht
				// hinzufügt. Ich denke aber das diese Variante bei einer
				// grossen Anzahl Knoten besser ist.
				this.spanningTreeNodes.remove(edge);
			}
		}

		this.getPathUpdater().updatePath();
		return successfulStep;
	}

	private void initEulerianTrail() {

		this.currentNode = this.getStartingNode();

		this.brancheNodes = new Stack<Node>();
		this.brancheNodes.push(this.currentNode);

		this.phase = Phase.DO_EULERIAN_TRAIL;
	}

	private boolean doStepEulerianTrail() {

		Node brancheNode = this.brancheNodes.pop();

		int i = 1;
		while (spanningTreeEdges.isEmpty() == false) {
			Edge edge = spanningTreeEdges.elementAt(spanningTreeEdges.size() - i);

			// Find the next edge in the spanning tree that is connected to the
			// current branch node.
			if (edge.getFirstNode() == brancheNode) {

				Edge newEdge = this.currentNode.getEdgeToNode(edge.getSecondNode());
				if (newEdge == null) {
					// FIXME: this path does not work, what do we do now?
					return false;
				}

				// Remove the edge from the spanning tree, because this edge is
				// used
				spanningTreeEdges.remove(edge);

				this.getPathUpdater().removeEdge(edge);

				// Add the new edge to the path.
				this.getPathUpdater().addEdge(newEdge);

				// Set the other node as new current node.
				this.currentNode = edge.getSecondNode();

				this.brancheNodes.add(brancheNode);
				this.brancheNodes.add(this.currentNode);

				this.getPathUpdater().updatePath();
				return true;

			} else if (edge.getSecondNode() == brancheNode) {

				Edge newEdge = this.currentNode.getEdgeToNode(edge.getFirstNode());
				if (newEdge == null) {
					// FIXME: this path does not work, what do we do now?
					return false;
				}

				// Remove the edge from the spanning tree, because this edge is
				// used
				spanningTreeEdges.remove(edge);

				this.getPathUpdater().removeEdge(edge);

				// Add the new edge to the path.
				this.getPathUpdater().addEdge(newEdge);

				// Set the other node as new current node.
				this.currentNode = edge.getFirstNode();

				this.brancheNodes.add(brancheNode);
				this.brancheNodes.add(this.currentNode);

				this.getPathUpdater().updatePath();
				return true;

			} else if (i >= spanningTreeEdges.size()) {
				// A leaf from the spanning tree
				// Nothing happen to the path, so recall the step.
				return doStepEulerianTrail();
			} else {
				i++;
			}
		}

		// Finishing the eulerian trail:
		// Connect the last node from the eulerian path with the start node to
		// close the circle
		Edge newEdge = this.getStartingNode().getEdgeToNode(this.currentNode);
		if (newEdge == null) {
			// FIXME: this path does not work, what do we do now?
			return false;
		}

		this.getPathUpdater().addEdge(newEdge);

		this.getPathUpdater().updatePath();
		this.setFinishedSuccessful(true);
		return true;
	}
}
