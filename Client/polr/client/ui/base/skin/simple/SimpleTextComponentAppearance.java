/*
 * SimpleTextComponentAppearance.java
 *
 * Created on December 29, 2007, 5:57 PM
 */

package polr.client.ui.base.skin.simple;

import org.newdawn.slick.gui.GUIContext;

import polr.client.ui.base.Component;
import polr.client.ui.base.Point;
import polr.client.ui.base.Skin;
import polr.client.ui.base.TextComponent;
import polr.client.ui.base.Theme;
import polr.client.ui.base.Timer;
import polr.client.ui.base.event.ChangeEvent;
import polr.client.ui.base.event.ChangeListener;
import polr.client.ui.base.skin.TextComponentAppearance;

/**
 *
 * @author davedes
 */
public class SimpleTextComponentAppearance extends SimpleContainerAppearance 
                                                implements TextComponentAppearance {
    
    protected Timer flashTimer = new Timer(500);
        
    protected boolean renderCaret = false;
    protected boolean still = false;
    
    protected Timer delayTimer = new Timer(800);
    
    protected ChangeListener change = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            still = true;
            delayTimer.restart();
        }
    };
        
    public SimpleTextComponentAppearance() {
        flashTimer.setRepeats(true);
        delayTimer.setRepeats(false);
        flashTimer.start();
    }
    
    public void update(GUIContext ctx, int delta, Component comp, Skin skin, Theme theme) {
        super.update(ctx, delta, comp, skin, theme);
        flashTimer.update(ctx, delta);
        delayTimer.update(ctx, delta);
        
        if (delayTimer.isAction()) {
            still = false;
        }
        
        if (still)
            renderCaret = true;
        else if (flashTimer.isAction())
            renderCaret = !renderCaret;
    }
    
    public void install(Component comp, Skin skin, Theme theme) {
        super.install(comp, skin, theme);
        if (skin instanceof SimpleSkin) {
            comp.addMouseListener(((SimpleSkin)skin).getSelectCursorListener());
        }
        ((TextComponent)comp).addChangeListener(change);
    }
    
    public void uninstall(Component comp, Skin skin, Theme theme) {
        super.uninstall(comp, skin, theme);
        if (skin instanceof SimpleSkin) {
            comp.removeMouseListener(((SimpleSkin)skin).getSelectCursorListener());
        }
        ((TextComponent)comp).removeChangeListener(change);
    }
    
    
    public int viewToModel(TextComponent comp, float x, float y) { 
        return -1;
    }
    
    public Point modelToView(TextComponent comp, int pos) {
        return null;
    }
}
