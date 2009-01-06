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

package polr.client.logic;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import polr.client.logic.Player.Dirs;

/**
 * Handles overworld sprites
 * @author shinobi
 *
 */
public class SpriteFactory {
	private HashMap<Integer, SpriteSheet> spriteSheets;
	
	/**
	 * Returns the requested sprite
	 * @param dir
	 * @param isMoving
	 * @param isLeftFoot
	 * @param sprite
	 * @return
	 */
	public Image getSprite(Dirs dir, boolean isMoving, 
			boolean isLeftFoot, int sprite) {
		SpriteSheet sheet = spriteSheets.get(sprite);
		if (isMoving) {
			if (isLeftFoot) {
				switch (dir) {
				case Up:
					return sheet.getSprite(0, 0);
				case Down:
					return sheet.getSprite(0, 2);
				case Left:
					return sheet.getSprite(0, 3);
				case Right:
					return sheet.getSprite(0, 1);
				}
			} else {
				switch (dir) {
				case Up:
					return sheet.getSprite(2, 0);
				case Down:
					return sheet.getSprite(2, 2);
				case Left:
					return sheet.getSprite(2, 3);
				case Right:
					return sheet.getSprite(2, 1);
				}
			}
		} else {
			switch (dir) {
			case Up:
				return sheet.getSprite(1, 0);
			case Down:
				return sheet.getSprite(1, 2);
			case Left:
				return sheet.getSprite(1, 3);
			case Right:
				return sheet.getSprite(1, 1);
			}
		}
		return null;
	}
	
	/**
	 * Initialises the database of sprites
	 */
	public SpriteFactory() {
		spriteSheets = new HashMap<Integer, SpriteSheet>();	
		try {
			File location = new File("res/sprites/players");
			String loc = location.getAbsolutePath();
			location = new File(loc);
			for(int i = 0; i < location.list().length; i++) {
				try {
					loc = location.getAbsolutePath() + "/" + location.list()[i];
					int num = Integer.parseInt(location.list()[i].replace(".png", ""));
					spriteSheets.put(num, new SpriteSheet(loc, 41, 51));
				} catch (Exception e) {}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
