/*
 * SimpleTextAreaAppearance.java
 *
 * Created on December 27, 2007, 2:06 AM
 */

package polr.client.ui.base.skin.simple;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import polr.client.ui.base.Component;
import polr.client.ui.base.Padding;
import polr.client.ui.base.Skin;
import polr.client.ui.base.TextArea;
import polr.client.ui.base.Theme;
import polr.client.ui.base.skin.SkinUtil;

/**
 *
 * @author davedes
 */
public class SimpleTextAreaAppearance extends SimpleTextComponentAppearance {
    
    public void install(Component comp, Skin skin, Theme theme) {
        super.install(comp, skin, theme);
        comp.setPadding(2, 2, 2, 2);
        comp.setOpaque(true);
    }
    
    public void render(GUIContext ctx, Graphics g, Component comp, Skin skin, Theme theme) {        
        TextArea area = (TextArea)comp;
        
        SkinUtil.renderComponentBase(g, comp);
        
        Rectangle bounds = area.getAbsoluteBounds();
        
        Font oldFont = g.getFont();
        Rectangle oldClip = g.getClip();
        
        Font font = area.getFont();
        int cursorPos = area.getCaretPosition();
        Padding pad = area.getPadding();
        boolean hasFocus = area.hasFocus();
       
        //use default font
        if (font==null)
            font=g.getFont();
        
        g.setFont(font);
        g.setColor(area.getForeground());
        g.setClip(bounds);
        
        //string bounds
        float startX = bounds.getX() + pad.left;
        float startY = bounds.getY() + pad.top;
        
        int linePos = area.getCurrentLine();
        int caretPos = area.getCaretPosition();
        
        for(int i=0; i<area.getLineCount(); i++) {
            TextArea.Line line = area.getLine(i);
            if (line != null) {
				int offset = line.offset;
            	float yoff = line.yoff;
            	float lineHeight = line.height;
            	String str = line.str;
            	float lineY = (startY+(i*lineHeight));
            	g.drawString(str, (int)startX, (int)lineY);
            
           	 	//if this line is where the caret is at, let's draw it
	            if (hasFocus && linePos == i && renderCaret) {
	                int endIndex = caretPos-offset;
	                float cpos = font.getWidth(str.substring(0, endIndex));
	                g.fillRect((int)(startX+cpos+1), (int)lineY, 1, lineHeight-2);
	            }
			}
        }
        
        g.setFont(oldFont);
        g.setClip(oldClip);
        
        if (area.isBorderRendered()) {
            g.setColor( hasFocus ? theme.getPrimaryBorder2() : theme.getPrimaryBorder1());
            g.draw(bounds);
        }
    }
    
}
