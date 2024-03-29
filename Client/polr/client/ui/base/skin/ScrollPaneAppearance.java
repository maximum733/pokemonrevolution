/*
 * ScrollPaneAppearance.java
 *
 * Created on January 1, 2008, 9:56 PM
 */

package polr.client.ui.base.skin;

import polr.client.ui.base.ScrollBar;
import polr.client.ui.base.ScrollConstants;
import polr.client.ui.base.ScrollPane;

/**
 *
 * @author davedes
 */
public interface ScrollPaneAppearance extends ComponentAppearance, ScrollConstants {
    
    /**
     * Creates a scroll bar for this pane. The returned scroll bar must be the same
     * orientation as the passed orientation (either HORIZONTAL or VERTICAL).
     *
     * @param pane the parent scroll pane
     * @param orientation the orientation to use when creating the scroll bar
     * @return the scroll bar
     */
    public ScrollBar createScrollBar(ScrollPane pane, int orientation);
}
