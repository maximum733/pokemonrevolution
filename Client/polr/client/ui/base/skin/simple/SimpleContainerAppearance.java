/*
 * SimpleContainerAppearance.java
 *
 * Created on November 7, 2007, 8:17 PM
 */

package polr.client.ui.base.skin.simple;


import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import polr.client.ui.base.Component;
import polr.client.ui.base.Skin;
import polr.client.ui.base.Theme;
import polr.client.ui.base.skin.SkinUtil;

/**
 *
 * @author davedes
 */
public class SimpleContainerAppearance extends SimpleComponentAppearance {
    
    public void render(GUIContext ctx, Graphics g, Component comp, Skin skin, Theme theme) {
        SkinUtil.renderComponentBase(g, comp);
    }
}
