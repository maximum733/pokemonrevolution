/*
 * ComponentAppearance.java
 *
 * Created on November 7, 2007, 8:06 PM
 */

package polr.client.ui.base.skin;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import polr.client.ui.base.Component;
import polr.client.ui.base.Skin;
import polr.client.ui.base.Theme;

/**
 * 
 * @author Matt
 */
public interface ComponentAppearance {
	public void render(GUIContext ctx, Graphics g, Component comp, Skin skin,
			Theme theme);

	public void update(GUIContext ctx, int delta, Component comp, Skin skin,
			Theme theme);

	public boolean contains(Component comp, float x, float y);

	public void install(Component comp, Skin skin, Theme theme);

	public void uninstall(Component comp, Skin skin, Theme theme);
}
