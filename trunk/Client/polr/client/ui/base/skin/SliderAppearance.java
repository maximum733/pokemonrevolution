/*
 * SliderAppearance.java
 *
 * Created on November 15, 2007, 5:54 PM
 */

package polr.client.ui.base.skin;

import polr.client.ui.base.Button;
import polr.client.ui.base.ScrollConstants;
import polr.client.ui.base.Slider;

/**
 *
 * @author Matt
 */
public interface SliderAppearance extends ComponentAppearance, ScrollConstants {
    
    /**
     * This is the knob or thumb button whic appears on the slider.
     */
    public Button createThumbButton(Slider slider); 
}
