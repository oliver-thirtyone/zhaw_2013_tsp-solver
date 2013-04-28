package model.grid;

public class Edge  implements Comparable<Edge>{

	private final Node firstNode;
	private final Node secondNode;

	private Boolean accessible;
	
	private Double length;

	public Edge(Node firstNode, Node secondNode) {
		this.firstNode = firstNode;
		this.secondNode = secondNode;

		this.accessible = Boolean.TRUE;
		
		this.length = this.calcLinearDistance();

		this.firstNode.addEdge(this);
		this.secondNode.addEdge(this);
	}

	public Node getFirstNode() {
		return this.firstNode;
	}

	public Node getSecondNode() {
		return this.secondNode;
	}

	public Boolean isAccessible() {
		return this.accessible;
	}

	public void setAccessible(Boolean accessible) {
		this.accessible = accessible;
	}
	
	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double calcLinearDistance(){

		Integer deltaX = this.getSecondNode().getX() - this.getFirstNode().getX();
		Integer deltaY = this.getSecondNode().getY() - this.getFirstNode().getY();

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

	@Override
	public int compareTo(Edge edge) {
		return this.length.compareTo(edge.getLength());
	}

}
