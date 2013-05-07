package tspsolver.util.view.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * The {@code LayoutManager} manages the layout of a {@link Container}
 * 
 * @author Oliver Streuli
 * @version 1.0
 */
public class LayoutManager {

	private GridBagConstraints constraints;

	private final Container container;

	private final GridBagLayout layout;

	/**
	 * @param container
	 *            The container whose layout the {@code LayoutManager} will manage
	 */
	public LayoutManager(Container container) {
		this.container = container;

		this.layout = new GridBagLayout();
		this.container.setLayout(this.layout);

		this.reset();
	}

	/**
	 * Set the constraints of the component and add it to the container
	 * 
	 * @param component
	 *            The component
	 * @return {@code true} if the component count got increased
	 */
	public boolean addComponent(Component component) {
		final int componentCount = this.container.getComponentCount();

		this.layout.setConstraints(component, this.constraints);
		this.container.add(component);

		return this.container.getComponentCount() > componentCount;
	}

	/**
	 * Reset all constraints
	 */
	public void reset() {
		this.constraints = new GridBagConstraints();
		this.setWeightX(1);
		this.setWeightY(1);
		this.setInsets(5, 5, 5, 5);
	}

	/**
	 * Set the constraints anchor
	 * 
	 * @param anchor
	 *            The constraints anchor
	 * @return {@code this}
	 */
	public LayoutManager setAnchor(int anchor) {
		this.constraints.anchor = anchor;
		return this;
	}

	/**
	 * Set the constraints fill
	 * 
	 * @param fill
	 *            The constraints fill
	 * @return {@code this}
	 */
	public LayoutManager setFill(int fill) {
		this.constraints.fill = fill;
		return this;
	}

	/**
	 * Set the constraints gridheight
	 * 
	 * @param height
	 *            The constraints gridheight
	 * @return {@code this}
	 */
	public LayoutManager setHeight(int height) {
		this.constraints.gridheight = height;
		return this;
	}

	/**
	 * Set the constraints insets
	 * 
	 * @param top
	 *            The constraints top inset
	 * @param left
	 *            The constraints left inset
	 * @param bottom
	 *            The constraints bottom inset
	 * @param right
	 *            The constraints right inset
	 * @return {@code this}
	 */
	public LayoutManager setInsets(int top, int left, int bottom, int right) {
		this.constraints.insets = new Insets(top, left, bottom, right);
		return this;
	}

	/**
	 * Set the constraints weightx
	 * 
	 * @param x
	 *            The constraints weightx
	 * @return {@code this}
	 */
	public LayoutManager setWeightX(double x) {
		this.constraints.weightx = x;
		return this;
	}

	/**
	 * Set the constraints weighty
	 * 
	 * @param y
	 *            The constraints weighty
	 * @return {@code this}
	 */
	public LayoutManager setWeightY(double y) {
		this.constraints.weighty = y;
		return this;
	}

	/**
	 * Set the constraints gridwidth
	 * 
	 * @param width
	 *            The constraints gridwidth
	 * @return {@code this}
	 */
	public LayoutManager setWidth(int width) {
		this.constraints.gridwidth = width;
		return this;
	}

	/**
	 * Set the constraints gridx
	 * 
	 * @param x
	 *            The constraints gridx
	 * @return {@code this}
	 */
	public LayoutManager setX(int x) {
		this.constraints.gridx = x;
		return this;
	}

	/**
	 * Set the constraints gridy
	 * 
	 * @param y
	 *            The constraints gridy
	 * @return {@code this}
	 */
	public LayoutManager setY(int y) {
		this.constraints.gridy = y;
		return this;
	}

}
