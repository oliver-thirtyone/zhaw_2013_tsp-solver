package tspsolver.view.grid;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Vertex;

import com.kitfox.svg.Group;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.Text;
import com.kitfox.svg.animation.AnimationElement;

public class EdgeWeightView {

	public static final String LINE_STROKE_EDGE = "#000000";
	public static final String LINE_STROKE_PATH = "#0000ff";
	public static final String LINE_STROKE_PATH_NEW = "#00ff00";
	public static final String LINE_STROKE_PATH_OLD = "#ff0000";

	public static final String LINE_STROKE_WIDTH_EDGE = "1";
	public static final String LINE_STROKE_WIDTH_PATH = "3";

	public static final String LINE_STROKE_DASHARRAY_EDGE = "2, 8";
	public static final String LINE_STROKE_DASHARRAY_PATH = "2, 2";

	private final Edge edge;

	private final Group svgGroup;
	private final Text svgText;

	protected EdgeWeightView(Edge edge, SVGDiagram svgDiagram) {
		this.edge = edge;

		this.svgGroup = (Group) svgDiagram.getElement(GridView.SVG_GROUP_EDGEWEIGHTS);
		this.svgText = new Text();
	}

	protected void createText() {
		try {
			Vertex firstVertex = this.edge.getFirstVertex();
			Vertex secondVertex = this.edge.getSecondVertex();

			int deltaX = secondVertex.getX() - firstVertex.getX();
			int deltaY = secondVertex.getY() - firstVertex.getY();

			int coordinateX = secondVertex.getX() - (int) (deltaX * 0.45);
			int coordinateY = secondVertex.getY() - (int) (deltaY * 0.45);

			this.svgText.appendText(String.valueOf(this.edge.getWeight()));
			this.svgText.addAttribute("x", AnimationElement.AT_XML, String.valueOf(coordinateX));
			this.svgText.addAttribute("y", AnimationElement.AT_XML, String.valueOf(coordinateY));

			this.svgGroup.loaderAddChild(null, this.svgText);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void deleteText() {
		try {
			this.svgGroup.removeChild(this.svgText);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

}
