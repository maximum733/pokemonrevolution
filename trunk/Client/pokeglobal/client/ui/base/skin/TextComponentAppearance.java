/*
 * TextComponentAppearance.java
 *
 * Created on December 29, 2007, 9:52 PM
 */

package pokeglobal.client.ui.base.skin;

import pokeglobal.client.ui.base.Point;
import pokeglobal.client.ui.base.TextComponent;

/**
 *
 * @author davedes
 */
public interface TextComponentAppearance extends ComponentAppearance {
    
    public int viewToModel(TextComponent comp, float x, float y);
    public Point modelToView(TextComponent comp, int pos);
}
