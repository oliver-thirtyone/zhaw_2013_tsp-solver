package tspsolver.model.updates;

public class ElementUpdate<E, A> {

	private final E element;
	private final A action;

	public ElementUpdate(E element, A action) {
		this.element = element;
		this.action = action;
	}

	public E getElement() {
		return this.element;
	}

	public A getAction() {
		return this.action;
	}

}
