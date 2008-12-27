/*
 * Popup.java
 *
 * Created on December 13, 2007, 2:45 PM
 */

package pokeglobal.client.ui.base;

/**
 *
 * @author davedes
 */
public class Popup extends Container {
    
    /**
     * Creates a new instance of Popup
     */
    public Popup() {
        this.setZIndex(Popup.POPUP_LAYER);
    }
}
