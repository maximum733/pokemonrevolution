/*
 * SimpleSkin.java
 *
 * Created on November 7, 2007, 6:52 PM
 */

package polr.client.ui.base.skin.simple;


import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.CursorLoader;
import org.newdawn.slick.util.Log;

import polr.client.ui.base.Button;
import polr.client.ui.base.CheckBox;
import polr.client.ui.base.Container;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.ScrollBar;
import polr.client.ui.base.ScrollPane;
import polr.client.ui.base.Skin;
import polr.client.ui.base.Slider;
import polr.client.ui.base.TextArea;
import polr.client.ui.base.TextField;
import polr.client.ui.base.ToggleButton;
import polr.client.ui.base.ToolTip;
import polr.client.ui.base.Window;
import polr.client.ui.base.event.MouseAdapter;
import polr.client.ui.base.event.MouseEvent;
import polr.client.ui.base.event.MouseListener;
import polr.client.ui.base.skin.ComponentAppearance;
import polr.client.ui.base.skin.FontUIResource;
import polr.client.ui.base.skin.FrameAppearance;
import polr.client.ui.base.skin.ImageUIResource;
import polr.client.ui.base.skin.ScrollBarAppearance;
import polr.client.ui.base.skin.ScrollPaneAppearance;
import polr.client.ui.base.skin.SliderAppearance;

/**
 *
 * @author davedes
 */
public class SimpleSkin implements Skin {
    
    private Image checkBoxImage;
    private Image closeButtonImage;
    private Image resizerImage;
    private Font font;
    private Cursor selectCursor;
    private Cursor resizeCursor;
    
    private boolean selectCursorFailed = false;
    private boolean resizeCursorFailed = false;
        
    private MouseListener selectCursorListener;
    private MouseListener resizeCursorListener;
    
    private static boolean roundRectanglesEnabled = true;
           
    //we can cache some of our appearances, others need to be created & attached to components
    private ComponentAppearance containerAppearance = new SimpleContainerAppearance();
    private ComponentAppearance toolTipAppearance = new SimpleToolTipAppearance();
    private ComponentAppearance labelAppearance = new SimpleLabelAppearance();
    private ComponentAppearance textAreaAppearance = new SimpleTextAreaAppearance();
    
    private ScrollPaneAppearance scrollPaneAppearance = new SimpleScrollPaneAppearance();
    private FrameAppearance frameAppearance = new SimpleFrameAppearance();
    private ScrollBarAppearance scrollBarAppearance = new SimpleScrollBarAppearance();
    private SliderAppearance sliderAppearance = new SimpleSliderAppearance();
        
    public static boolean isRoundRectanglesEnabled() {
        return roundRectanglesEnabled;
    }

    public static void setRoundRectanglesEnabled(boolean aRoundRectanglesEnabled) {
        roundRectanglesEnabled = aRoundRectanglesEnabled;
    }
    
    public String getName() {
        return "Simple";
    }
    
    public boolean isThemeable() {
        return true;
    }
    
    public void install() throws SlickException {
                ///////////////////
                // CACHE OBJECTS //
                ///////////////////
        
        //try loading
        //ResourceLoader will spit out a log message if there are problems
        
        //images
        if (checkBoxImage==null)
            checkBoxImage = tryImage("res/skin/simple/checkbox.png");
        if (closeButtonImage==null)
            closeButtonImage = tryImage("res/skin/simple/closewindow.png");
        if (selectCursor==null) 
            selectCursor = tryCursor("res/skin/shared/cursor_select.png", 4, 8);
            //selectCursor = tryCursor("res/skin/shared/cursor_hand.png", 6, 0);
        //if (resizeCursor==null)
        //    resizeCursor = tryCursor("res/skin/shared/cursor_resize.png", 4, 4);
        
        if (selectCursorListener==null && selectCursor!=null)
            selectCursorListener = new CursorListener(selectCursor);
        if (resizeCursorListener==null && resizeCursor!=null)
            resizeCursorListener = new CursorListener(resizeCursor);
        
        //fonts
        if (font==null)
            font = tryFont("res/skin/shared/verdana.fnt", "res/skin/shared/verdana.png");
    }
    
    private Cursor tryCursor(String ref, int x, int y) {
        try {
            return CursorLoader.get().getCursor(ref, x, y);
        } catch (Exception e) {
            Log.error("Failed to load and apply SUI 'select' cursor.", e);
            return null;
        }
    }
    
    private Image tryImage(String s) {
        try { return new ImageUIResource(s); }
        catch (Exception e) { return null; }
    }
    
    private Font tryFont(String s1, String s2) {
        try { return new FontUIResource.AngelCodeFont(s1, s2); }
        catch (Exception e) { return null; }
    }

    public void uninstall() throws SlickException {
    }
    
    public Image getCheckBoxImage() {
        return checkBoxImage;
    }
    
    public Image getCloseButtonImage() {
        return closeButtonImage;
    }
    
    public Font getFont() {
        return font;
    }
    
    public Cursor getSelectCursor() {
        return selectCursor;
    }

    public Cursor getResizeCursor() {
        return resizeCursor;
    }
    
    public MouseListener getSelectCursorListener() {
        return selectCursorListener;
    }

    public MouseListener getResizeCursorListener() {
        return resizeCursorListener;
    }
    
    public ComponentAppearance getContainerAppearance(Container comp) {
        return containerAppearance;
    }

    public ComponentAppearance getCheckBoxAppearance(CheckBox comp) {
        return new SimpleCheckBoxAppearance(comp);
    }

    public FrameAppearance getFrameAppearance(Frame comp) {
        return frameAppearance;
    }

    public ComponentAppearance getButtonAppearance(Button comp) {
        return new SimpleButtonAppearance(comp);
    }

    public ComponentAppearance getToolTipAppearance(ToolTip comp) {
        return toolTipAppearance;
    }

    public ComponentAppearance getLabelAppearance(Label comp) {
        return labelAppearance;
    }
    
    public ComponentAppearance getToggleButtonAppearance(ToggleButton comp) {
        return new SimpleButtonAppearance(comp);
    }

    public ScrollBarAppearance getScrollBarAppearance(ScrollBar comp) {
        return scrollBarAppearance;
    }

    public ScrollPaneAppearance getScrollPaneAppearance(ScrollPane comp) {
        return scrollPaneAppearance;
    }
    
    public SliderAppearance getSliderAppearance(Slider comp) {
        return sliderAppearance;
    }
    
    public ComponentAppearance getTextFieldAppearance(TextField comp) {
        return new SimpleTextFieldAppearance(comp);
    }
    
    public ComponentAppearance getTextAreaAppearance(TextArea comp) {
        return textAreaAppearance;
    }
    
    public ComponentAppearance getWindowAppearance(Window window) {
        return containerAppearance;
    }
    
    private class CursorListener extends MouseAdapter {
        
        private Cursor c;
        private boolean failed = false;
        private boolean dragging = false;
        private boolean inside = false;
        
        public CursorListener(Cursor c) {
            this.c = c;
        }
        
        public void mouseReleased(MouseEvent ev) {
            dragging = false;
            if (!inside)
                release();
        }
        
        public void mouseDragged(MouseEvent ev) {
            dragging = true;
        }
        
        public void mouseEntered(MouseEvent ev) {
            inside = true;
            if (!failed) {
                try { Mouse.setNativeCursor(c); }
                catch (Exception e) {
                    failed = true; 
                }
            }
        }
        
        public void mouseExited(MouseEvent ev) {
            inside = false;
            if (!dragging)
                release();
        }
        
        void release() {
            try { Mouse.setNativeCursor(null); }
            catch (Exception e) { }
        }
    }
}
