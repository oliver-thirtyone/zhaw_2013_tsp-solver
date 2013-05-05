package tspsolver.model.grid;

import java.util.Observable;

public class Edge extends Observable {

	private final Node firstNode;
	private final Node secondNode;
	private final double weight;

	protected Edge(Node firstNode, Node secondNode, double weight) throws IllegalArgumentException {
		if (firstNode.equals(secondNode)) {
			throw new IllegalArgumentException("The nodes must not be equal");
		}

		this.firstNode = firstNode;
		this.secondNode = secondNode;
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

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

		if (this.firstNode == null) {
			if (other.firstNode != null) {
				return false;
			}
		} else if (!this.firstNode.equals(other.firstNode) && !this.firstNode.equals(other.secondNode)) {
			return false;
		}

		if (this.secondNode == null) {
			if (other.secondNode != null) {
				return false;
			}
		} else if (!this.secondNode.equals(other.secondNode) && !this.secondNode.equals(other.firstNode)) {
			return false;
		}

		if (Double.doubleToLongBits(this.weight) != Double.doubleToLongBits(other.weight)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.getFirstNode());
		builder.append(" -> ");
		builder.append(this.getSecondNode());

		return builder.toString();
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

	protected static double calcLinearDistance(Node firstNode, Node secondNode) {
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
