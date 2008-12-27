/*
 * SimpleLabelAppearance.java
 *
 * Created on November 7, 2007, 8:25 PM
 */

package pokeglobal.client.ui.base.skin.simple;


import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import pokeglobal.client.ui.base.Component;
import pokeglobal.client.ui.base.Label;
import pokeglobal.client.ui.base.Skin;
import pokeglobal.client.ui.base.Theme;
import pokeglobal.client.ui.base.skin.SkinUtil;

/**
 *
 * @author davedes
 */
public class SimpleLabelAppearance extends SimpleContainerAppearance {
    
    public void render(GUIContext ctx, Graphics g, Component comp, Skin skin, Theme theme) {
        super.render(ctx, g, comp, skin, theme);
        SkinUtil.renderLabelBase(g, (Label)comp);
    }
}
