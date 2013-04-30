package model.grid;

public class Edge implements Comparable<Edge> {

	private final Node firstNode;
	private final Node secondNode;

	private final double weight;
	private final boolean accessible;

	public Edge(Node firstNode, Node secondNode) {
		this(firstNode, secondNode, Edge.calcLinearDistance(firstNode, secondNode), true);
	}

	public Edge(Node firstNode, Node secondNode, boolean accessible, double weight) {
		this(firstNode, secondNode, weight, accessible);
	}

	public Edge(Node firstNode, Node secondNode, boolean accessible) {
		this(firstNode, secondNode, Edge.calcLinearDistance(firstNode, secondNode), accessible);
	}

	public Edge(Node firstNode, Node secondNode, double weight, boolean accessible) {
		this.firstNode = firstNode;
		this.secondNode = secondNode;

		this.weight = weight;
		this.accessible = accessible;

		this.firstNode.addEdge(this);
		this.secondNode.addEdge(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + (this.accessible ? 1231 : 1237);
		result = prime * result + ((this.firstNode == null) ? 0 : this.firstNode.hashCode());
		result = prime * result + ((this.secondNode == null) ? 0 : this.secondNode.hashCode());

		long temp = Double.doubleToLongBits(this.weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}

		Edge other = (Edge) obj;

		if (this.accessible != other.accessible) {
			return false;
		}

		if (this.firstNode == null) {
			if (other.firstNode != null) {
				return false;
			}
		}
		else if (!this.firstNode.equals(other.firstNode)) {
			return false;
		}

		if (this.secondNode == null) {
			if (other.secondNode != null) {
				return false;
			}
		}
		else if (!this.secondNode.equals(other.secondNode)) {
			return false;
		}

		if (Double.doubleToLongBits(this.weight) != Double.doubleToLongBits(other.weight)) {
			return false;
		}

		return true;
	}

	public Node getFirstNode() {
		return this.firstNode;
	}

	public Node getSecondNode() {
		return this.secondNode;
	}

	public double getWeight() {
		return this.weight;
	}

	public boolean isAccessible() {
		return this.accessible;
	}

	@Override
	public int compareTo(Edge edge) { // FIXME: compareTo is the wrong name
		return new Double(this.weight).compareTo(new Double(edge.getWeight()));
	}

	private static double calcLinearDistance(Node firstNode, Node secondNode) {
		int deltaX = secondNode.getX() - firstNode.getX();
		int deltaY = secondNode.getY() - firstNode.getY();

		// Make deltaX positive
		if (deltaX < 0) {
			deltaX *= -1;
		}

		// Make deltaY positive
		if (deltaY < 0) {
			deltaY *= -1;
		}

		return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}

}
