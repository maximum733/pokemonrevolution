/*
 * DemoFrameTest.java
 *
 * Created on November 10, 2007, 10:45 PM
 */

package polr.client.ui.base.test;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import polr.client.ui.base.Container;
import polr.client.ui.base.Display;
import polr.client.ui.base.Sui;

/**
 *
 * @author davedes
 */
public class DemoFrameTest extends BasicGame {
    
    public static void main(String[] args) throws Exception {
        AppGameContainer app = new AppGameContainer(new DemoFrameTest());
        app.setDisplayMode(800,600,false);
        app.start();
    }
    
    /**
     * Creates a new instance of DemoFrameTest
     */
    public DemoFrameTest() {
        super("DemoContainerTest");
    }
    
    private Display disp;
    
    public void init(GameContainer container) throws SlickException {
        container.getGraphics().setBackground(Sui.getTheme().getBackground().darker());
        
        disp = new Display(container);
        
        Container demo = new DemoFrame();
        demo.setLocationRelativeTo(disp);
        
        disp.add(demo);
    }
    
    public void update(GameContainer container, int delta) throws SlickException {
        disp.update(container, delta);
        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE))
            container.exit();
    }
    
    public void render(GameContainer container, Graphics g) throws SlickException {
        disp.render(container, g);
    }
}
