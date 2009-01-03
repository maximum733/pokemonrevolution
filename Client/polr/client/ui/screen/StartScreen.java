/*
 Pokemon Global. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright � 2007-2008 Pokemon Global Team

 This file is part of Pokemon Global.

 Pokemon Global is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Pokemon Global is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Pokemon Global.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * The login screen class. It is a Frame, not a window or game state.
 * We didn't want to spend time managing states, and window decorations 
 * were unnecessary.
*
 * @author    Pivot
 * @author    Sienide
 *
 * Credits:
 * - Decypher, for designing graphics used here
 */

package polr.client.ui.screen;

import java.util.regex.Pattern;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import polr.client.GameClient;
import polr.client.network.PacketGenerator;
import polr.client.ui.StarterChoices;
import polr.client.ui.base.Button;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextField;
import polr.client.ui.base.ToggleButton;
import polr.client.ui.base.Window;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
import polr.client.ui.window.LoginFrame;
import polr.client.ui.window.ServerFrame;
import polr.client.ui.window.StartFrame;

/**
 * The starting screen, handles the server selector and login.
 * 
 * @author shinobi
 *
 */
public class StartScreen extends Window {
       static final long serialVersionUID = -261619500525051531L;
       private PacketGenerator packetGen;
       private LoginFrame loginFrame;
       private Label m_background;
       private ServerFrame serverSelector;

       public StartScreen(PacketGenerator out) {
               super();

               packetGen = out;
               initGUI();
       }
       
       @SuppressWarnings("deprecation")
       private void initGUI() {
    	   this.setSize(800, 600);
    	   this.setLocation(0, 0);
           try {
        	   LoadingList.setDeferredLoading(true);
        	   m_background = new Label(new Image("res/title.png"));
        	   m_background.setSize(800, 600);
        	   m_background.setLocation(0, 0);
        	   this.add(m_background);
        	   LoadingList.setDeferredLoading(false);
        	   loginFrame = new LoginFrame();
        	   this.add(loginFrame);
        	   serverSelector = new ServerFrame();
        	   this.add(serverSelector);
        	   StartFrame start = new StartFrame();
        	   this.add(start);
           } catch (Exception e) {
        	   e.printStackTrace();
           }
           this.setVisible(true);
       }
       
       public LoginFrame getLoginFrame() {
    	   return loginFrame;
       }
       
       public ServerFrame getServerSelector() {
    	   return serverSelector;
       }
      
       public void setPacketGenerator(PacketGenerator packetGen2) {
    	   packetGen = packetGen2;
       }
	
       public String getUser() {
    	   return loginFrame.getUsername();
       }
}