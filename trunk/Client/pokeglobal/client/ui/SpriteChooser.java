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
 * Sprite choosation!
 *
 * @author Pivot
 */

package pokeglobal.client.ui;

import java.util.Arrays;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import pokeglobal.client.ui.base.Frame;
import pokeglobal.client.ui.base.Label;
import pokeglobal.client.ui.base.ListBox;
import pokeglobal.client.ui.base.ScrollPane;
/*
 * The UI where the user can choose to change their sprite.
 */
public class SpriteChooser extends Frame {
	protected ListBox m_spriteList;
	protected Label m_spriteDisplay;
	
	protected String mustLoadSprite;
	
    public SpriteChooser() {
		m_spriteDisplay = new Label();
		m_spriteDisplay.setSize(124, 204);
		m_spriteDisplay.setLocation(105, 20);
		getContentPane().add(m_spriteDisplay);
		
		m_spriteList = new ListBox(Arrays.asList(
			new String[] {
					"girl",
					"girlfive",
					"girleight",
					"girleleven",
					"girlfifteen",
					"girlfour",
					"girlfourteen",
					"girlnine",
					"girlseven",
					"girlsix",
					"girlsixteen",
					"girlten",
					"girlthirteen",
					"girlthree",
					"girltwelve",
					"girltwo",
					"guy",
					"guyeight",
					"guyeleven",
					"guyfive",
					"guyfour",
					"guyfourteen",
					"guynine",
					"guyseven",
					"guysix",
					"guyten",
					"guythirteen",
					"guythree",
					"guytwelve",
					"guytwo",
					"hiker",
					"hikertwo",
					"karateguy",
					"karateguytwo",
					"lady",
					"ladyfive",
					"ladyfour",
					"ladyseven",
					"ladysix",
					"ladythree",
					"ladytwo",
					"lass",
					"man",
					"manfive",
					"manfour",
					"manthree",
					"mantwo",
					"nerdthree",
					"sailor",
					"supernerd",
					"swimmerguy",
					"youngster",
					"youngsterthree",
					"youngstertwo",
					"dude",
					"fatguy",
					"fatguytwo",
					"fisherman",
					"fishermantwo",
					"camperboy",
					"campergirl",
					"campergirltwo",
					"camperboytwo",
					"bugcatcher",
					"bugcatchertwo",

		}), false) {
			@Override
			protected void itemClicked(String itemName, int idx) {
				super.itemClicked(itemName, idx);
				mustLoadSprite = "res/sprites/players/" + itemName + ".png";
			}
		};
		getContentPane().add(m_spriteList);
				
		ScrollPane sprite = new ScrollPane(m_spriteList);
		sprite.setSize(264, 317);
		getContentPane().add(sprite);
		sprite.setVisible(true);
		
		setTitle("Please choose your character..");
		getCloseButton().setVisible(false);
		setSize(265, 340);
		setResizable(false);
		setDraggable(false);
		setVisible(true);
		
		initUse();
    }

	public void initUse() {
		
	}
	public int getChoice() {
		return m_spriteList.getSelectedIndex();
	}
	@Override
	public void render(GUIContext container, Graphics g) {
		super.render(container, g);
		if (mustLoadSprite != null) {
			try {
				m_spriteDisplay.setImage(new Image(mustLoadSprite));}
			catch (SlickException e) { 
				e.printStackTrace();
			}
			
			mustLoadSprite = null;
		}
	}
}
