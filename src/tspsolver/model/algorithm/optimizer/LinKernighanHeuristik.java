package tspsolver.model.algorithm.optimizer;

import java.util.HashSet;
import java.util.Vector;

import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;
import tspsolver.model.validator.Validator;

public class LinKernighanHeuristik extends OptimizerAlgorithm {

	private final Vector<Node> nodesInOrder;

	private int j;

	public LinKernighanHeuristik() {
		this.nodesInOrder = new Vector<Node>();
	}

	@Override
	protected void doInitialize() {
	}

	@Override
	protected void doReset() {
		this.j = 0;
	}

	private void initNodesInOrder() {

		this.nodesInOrder.clear();

		// Get the node in line
		Node currentNode = this.getStartingNode();

		this.nodesInOrder.add(currentNode);

		final int numberOfEdges = this.getPath().getNumberOfEdges();

		final HashSet<Edge> edges = new HashSet<Edge>();
		for (final Edge edge : this.getPath().getEdges()) {
			edges.add(edge);
		}

		while (this.nodesInOrder.size() < numberOfEdges) {
			for (final Edge edge : edges) {
				if (edge.getFirstNode() == currentNode) {

					edges.remove(edge);

					currentNode = edge.getSecondNode();

					this.nodesInOrder.add(currentNode);
					break;
				}
				else if (edge.getSecondNode() == currentNode) {

					edges.remove(edge);

					currentNode = edge.getFirstNode();

					this.nodesInOrder.add(currentNode);
					break;
				}
			}

		}

	}

	@Override
	protected boolean doStep() {

		// 1. Generate a random initial tour T.

		this.initNodesInOrder();

		while (this.j < this.nodesInOrder.size()) {

			Vector<Node> t = new Vector<Node>();

			// 2. Let i = 1.
			int i = 1;

			// Choose t1.
			Node t1 = this.nodesInOrder.get(this.j);
			t.add(t1);

			Node t2;
			if (this.j < this.nodesInOrder.size() - 1) {
				t2 = this.nodesInOrder.get(this.j + 1);
			}
			else {
				t2 = this.nodesInOrder.get(0);
			}
			t.add(t2);

			boolean hasTriedBackward = false;
			while (true) {

				Vector<Edge> x = new Vector<Edge>();

				// 3. Choose x1 = (t1, t2) element from T.
				Edge x1 = t1.getEdgeToNode(t2);
				if (x1 == null) {
					// FIXME: this path does not work, what do we do now?
					throw new IllegalStateException();
				}
				x.add(x1);

				// 4. Choose y1 = (t2, t3) not element from T such that G1 >
				// 0.
				// If this is not possible, go to Step 12.
				boolean foundNoneNewY1 = true;
				for (Edge y1 : t2.getEdges()) {
					if (x1.getWeight() - y1.getWeight() > 0 && this.getPath().containsEdge(y1) == false) {

						foundNoneNewY1 = false;

						Vector<Edge> y = new Vector<Edge>();

						y.add(y1);

						Node t3 = y1.getFirstNode() == t2 ? y1.getSecondNode() : y1.getFirstNode();

						t.add(t3);

						// 5. Let i = i + 1.
						i++;

						// 6. Choose xi = (t2i-1, t2i) element from T such
						// that
						for (Edge x2 : t3.getEdges()) {

							Node t4 = x2.getFirstNode() == t3 ? x2.getSecondNode() : x2.getFirstNode();

							// (b) xi != ys for all s < i.
							if (y.contains(x2) == false && t.contains(t4) == false) {
								x.add(x2);
								t.add(t4);
								// (a) if t2i is joined to t1, the
								// resulting configuration is a tour T', and
								{
									Edge yi = t4.getEdgeToNode(t1);
									if (yi == null) {
										// FIXME: this path does not work, what
										// do
										// we do now?
										throw new IllegalStateException();
									}
									y.add(yi);

									// If T' is a better tour than T, let T = T'
									// and go to Step 2.
									Path newPath = this.generateNewPath(x, y);

									if (newPath.getNumberOfEdges() == this.getPath().getNumberOfEdges() && newPath.getWeight() < this.getPath().getWeight() && this.isTour(newPath)) {
										this.getPathUpdater().clearPath();
										this.getPathUpdater().addPath(newPath);
										this.getPathUpdater().updatePath();

										this.j++;
										return true;
									}

									y.remove(yi);
								}

								// 7. Choose yi = (t2i, t2i+1) not element from
								// T such that
								// (a) Gi > 0,
								// (b) yi != xs for all s <= i, and
								// (c) xi+1 exists.
								for (Edge y2 : t4.getEdges()) {
									y.add(y2);

									if (this.getPath().containsEdge(y2) == false && this.calcGain(x, y) > 0 && x.contains(y2) == false) {

										Node t5 = y2.getFirstNode() == t2 ? y2.getSecondNode() : y2.getFirstNode();

										t.add(t5);

										// If such yi exists, go to Step
										// 5.

										while (true) {
											// 5. Let i = i + 1.
											i++;

											Node t2iMinus1 = t.get((2 * i - 1) - 1);

											// 6. Choose xi = (t2i-1, t2i)
											// element from T such
											// that
											Edge xi = null;
											Node t2i = null;
											for (Edge edge : t2iMinus1.getEdges()) {

												t2i = edge.getFirstNode() == t2iMinus1 ? edge.getSecondNode() : edge.getFirstNode();

												// (b) xi != ys for all s < i.
												if (y.contains(edge) == false && t.contains(t2i) == false) {

													xi = edge;
													break;
												}
											}

											if (xi == null) {
												break;
											}

											x.add(xi);
											t.add(t2i);

											// (a) if t2i is joined to t1, the
											// resulting configuration is a tour
											// T', and
											{
												Edge yi = t2iMinus1.getEdgeToNode(t2i);
												if (yi == null) {
													// FIXME: this path does not
													// work, what
													// do
													// we do now?
													throw new IllegalStateException();
												}
												y.add(yi);

												// If T' is a better tour than
												// T, let T = T' and go to Step
												// 2.
												Path newPath = this.generateNewPath(x, y);

												if (newPath.getNumberOfEdges() == this.getPath().getNumberOfEdges() && newPath.getWeight() < this.getPath().getWeight() && this.isTour(newPath)) {
													this.getPathUpdater().clearPath();
													this.getPathUpdater().addPath(newPath);
													this.getPathUpdater().updatePath();

													this.j++;
													return true;
												}

												y.remove(yi);
											}

											// 7. Choose yi = (t2i, t2i+1) not
											// element from
											// T such that
											// (a) Gi > 0,
											// (b) yi != xs for all s <= i, and
											// (c) xi+1 exists.
											Edge yi = null;
											for (Edge edge : t2i.getEdges()) {

												y.add(edge);
												if (this.getPath().containsEdge(edge) == false && this.calcGain(x, y) > 0 && x.contains(edge) == false) {

													Node t2iPlus1 = edge.getFirstNode() == t2i ? edge.getSecondNode() : edge.getFirstNode();

													t.add(t2iPlus1);

													yi = edge;
													break;
												}

												y.remove(edge);
											}

											if (yi == null) {

												x.remove(xi);
												t.remove(t2i);

												break;
											}

											// If such yi exists, go to Step
											// 5.
										}
										t.remove(t5);
										while (t.size() > 4) {
											t.remove(t.size() - 1);
										}
									}

									y.remove(y2);
									while (y.size() > 1) {
										y.remove(y.size() - 1);
									}

									i = 2;
								}// 8. If there is an untried alternative for
									// y2, let i = 2 and go to Step 7.

								x.remove(x2);
								while (x.size() > 1) {
									x.remove(x.size() - 1);
								}

								t.remove(t4);
								while (t.size() > 3) {
									t.remove(t.size() - 1);
								}
							}
							i = 2;
						}// 9. If there is an untried alternative for x2, let i
							// = 2 and go to Step 6.
						t.remove(t3);
					}

					i = 1;
				} // 10. If there is an untried alternative for y1, let i = 1
					// and go to Step 4.

				if (foundNoneNewY1) {
					break;
				}

				// 11. If there is an untried alternative for x1, let i = 1 and
				// go to Step 3.
				if (hasTriedBackward == false) {

					t.clear();
					t.add(t1);

					// Take the Node in the other way.
					if (this.j > 0) {
						t2 = this.nodesInOrder.get(this.j - 1);
					}
					else {
						t2 = this.nodesInOrder.get(this.nodesInOrder.size() - 1);
					}
					t.add(t2);

					i = 1;

					hasTriedBackward = true;
				}
				else {
					break;
				}
			}

			this.j++;
		}// 12. If there is an untried alternative for t1, then go to Step 2.

		if (this.j >= this.nodesInOrder.size()) {
			this.finishedSuccessfully();
		}

		return true;
	}

	private double calcGain(Vector<Edge> x, Vector<Edge> y) {

		double gain = 0.0;

		for (Edge edge : x) {
			gain += edge.getWeight();
		}

		for (Edge edge : y) {
			gain -= edge.getWeight();
		}

		return gain;
	}

	// Generate a new path, replace x with y.
	private Path generateNewPath(Vector<Edge> x, Vector<Edge> y) {

		Path newPath = new Path();

		PathUpdater pathUpdater = new PathUpdater(newPath);

		pathUpdater.addEdges(this.getPath().getEdges());

		pathUpdater.removeEdges(x);

		pathUpdater.addEdges(y);

		pathUpdater.updatePath();

		return pathUpdater.getPath();
	}

	// Check if the configuration builts a tour.
	private boolean isTour(Path newPath) {
		Scenario scenario = this.getScenario();
		Grid grid = this.getGrid();

		Validator validator = this.getScenario().getValidator();
		return validator.validatePath(scenario, grid, newPath);
	}

}
