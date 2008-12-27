/*
 * SimpleSquareButtonAppearance.java
 *
 * Created on November 8, 2007, 6:00 PM
 */

package pokeglobal.client.ui.base.skin.simple;


import org.newdawn.slick.geom.RoundedRectangle;

import pokeglobal.client.ui.base.Button;

/**
 *
 * @author davedes
 */
public class SimpleSquareButtonAppearance extends SimpleButtonAppearance {
    
    public SimpleSquareButtonAppearance(Button button) {
        super(button);
    }
    
    protected RoundedRectangle createRoundedBounds() {
        return null; //stores null
    }    
}
