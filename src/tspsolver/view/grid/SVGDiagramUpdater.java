package tspsolver.view.grid;

import javax.swing.SwingUtilities;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;

public class SVGDiagramUpdater implements Runnable {

	public static final int UPDATE_DELAY = 100;

	private final GridView gridView;
	private final SVGDiagram svgDiagram;

	private Thread thread;
	private boolean updating;

	public SVGDiagramUpdater(GridView gridView, SVGDiagram svgDiagram) {
		this.gridView = gridView;
		this.svgDiagram = svgDiagram;

		this.updating = false;
	}

	public void update() {
		this.updating = true;

		if (this.thread == null || !this.thread.isAlive()) {
			this.thread = new Thread(this);
			this.thread.start();
		}
	}

	@Override
	public void run() {
		// Sleep while we are still updating
		while (this.updating) {
			try {
				this.updating = false;
				Thread.sleep(SVGDiagramUpdater.UPDATE_DELAY);
			}
			catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}

		// Update the diagram
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SVGDiagramUpdater.this.svgDiagram.updateTime(0.0);
					SVGDiagramUpdater.this.gridView.repaint();
				}
				catch (SVGException exception) {
					exception.printStackTrace();
				}
			}
		});
	}

}
