package tspsolver.view.grid;

import tspsolver.model.scenario.grid.Node;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Group;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;

public class NodeView {

	public static final String CIRCLE_RADIUS = "8";

	public static final String CIRCLE_FILL_NODE = "#000000";
	public static final String CIRCLE_FILL_STARTINGNODE = "#0000ff";

	private final Node node;

	private final Group svgGroup;
	private final Circle svgCircle;

	protected NodeView(Node node, SVGDiagram svgDiagram) {
		this.node = node;

		this.svgGroup = (Group) svgDiagram.getElement(GridView.SVG_GROUP_NODES);
		this.svgCircle = new Circle();
	}

	protected void createCircle() {
		try {
			this.svgCircle.addAttribute("cx", AnimationElement.AT_XML, String.valueOf(this.node.getX()));
			this.svgCircle.addAttribute("cy", AnimationElement.AT_XML, String.valueOf(this.node.getY()));
			this.svgCircle.addAttribute("r", AnimationElement.AT_XML, NodeView.CIRCLE_RADIUS);

			this.svgGroup.loaderAddChild(null, this.svgCircle);
		}
		catch (final SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void deleteCircle() {
		try {
			this.svgGroup.removeChild(this.svgCircle);
		}
		catch (final SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void updateCircle(String circleFill) {
		try {
			if (this.svgCircle.hasAttribute("fill", AnimationElement.AT_CSS)) {
				this.svgCircle.setAttribute("fill", AnimationElement.AT_CSS, circleFill);
			}
			else {
				this.svgCircle.addAttribute("fill", AnimationElement.AT_CSS, circleFill);
			}
		}
		catch (final SVGException exception) {
			exception.printStackTrace();
		}
	}
}
