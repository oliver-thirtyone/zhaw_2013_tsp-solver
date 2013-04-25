package model.algorithm;

import java.util.LinkedList;
import java.util.Observable;

import model.grid.Edge;
import model.grid.Node;

public abstract class Algorithm extends Observable {

	protected final LinkedList<Edge> currentPath;
	protected Node currentNode;

	public Algorithm() {
		this.currentPath = new LinkedList<Edge>();
		this.currentNode = null;
	}

	public LinkedList<Edge> getCurrentPath() {
		return this.currentPath;
	}

	public Node getCurrentNode() {
		return this.currentNode;
	}

}
