/*
 * AbstractComponentAppearance.java
 *
 * Created on November 8, 2007, 4:50 PM
 */

package polr.client.ui.base.skin;

import polr.client.ui.base.Component;
import polr.client.ui.base.Skin;
import polr.client.ui.base.Theme;

/**
 *
 * @author davedes
 */
public abstract class AbstractComponentAppearance implements ComponentAppearance {
    
    public boolean contains(Component comp, float x, float y) {
        return comp.inside(x, y);
    }
    
    public void install(Component comp, Skin skin, Theme theme) {    
    }
    
    public void uninstall(Component comp, Skin skin, Theme theme) {   
    }
}
