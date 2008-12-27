/*
 * SliderAppearance.java
 *
 * Created on November 15, 2007, 5:54 PM
 */

package pokeglobal.client.ui.base.skin;

import pokeglobal.client.ui.base.Button;
import pokeglobal.client.ui.base.ScrollConstants;
import pokeglobal.client.ui.base.Slider;

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
