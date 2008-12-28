/*
 * TextComponentAppearance.java
 *
 * Created on December 29, 2007, 9:52 PM
 */

package polr.client.ui.base.skin;

import polr.client.ui.base.Point;
import polr.client.ui.base.TextComponent;

/**
 *
 * @author davedes
 */
public interface TextComponentAppearance extends ComponentAppearance {
    
    public int viewToModel(TextComponent comp, float x, float y);
    public Point modelToView(TextComponent comp, int pos);
}
