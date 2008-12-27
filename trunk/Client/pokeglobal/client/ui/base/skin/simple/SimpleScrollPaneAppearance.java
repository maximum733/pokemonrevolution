/*
 * SimpleContainerAppearance.java
 *
 * Created on November 7, 2007, 8:17 PM
 */

package pokeglobal.client.ui.base.skin.simple;

import pokeglobal.client.ui.base.ScrollBar;
import pokeglobal.client.ui.base.ScrollPane;
import pokeglobal.client.ui.base.skin.ScrollPaneAppearance;


/**
 *
 * @author davedes
 */
public class SimpleScrollPaneAppearance extends SimpleContainerAppearance implements ScrollPaneAppearance {
    
    public ScrollBar createScrollBar(ScrollPane pane, int orientation) {
        ScrollBar bar = new ScrollBar(orientation);
        bar.setSize(ScrollPane.CORNER_SIZE, ScrollPane.CORNER_SIZE);
        return bar;
    }
}
