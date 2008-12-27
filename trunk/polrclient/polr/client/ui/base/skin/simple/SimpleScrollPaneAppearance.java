/*
 * SimpleContainerAppearance.java
 *
 * Created on November 7, 2007, 8:17 PM
 */

package polr.client.ui.base.skin.simple;

import polr.client.ui.base.ScrollBar;
import polr.client.ui.base.ScrollPane;
import polr.client.ui.base.skin.ScrollPaneAppearance;

/**
 * 
 * @author davedes
 */
public class SimpleScrollPaneAppearance extends SimpleContainerAppearance
		implements ScrollPaneAppearance {

	public ScrollBar createScrollBar(ScrollPane pane, int orientation) {
		ScrollBar bar = new ScrollBar(orientation);
		bar.setSize(ScrollPane.CORNER_SIZE, ScrollPane.CORNER_SIZE);
		return bar;
	}
}
