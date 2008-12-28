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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import polr.client.logic.Player.Dirs;

public class SpriteFactory {
	private HashMap<String, SpriteSheet> spriteSheets;
	
	//gets overworld sprite data
	public Image getSprite(Dirs dir, boolean isMoving, 
			boolean isLeftFoot, String sprite) {
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
	
	//gets available sprites
	public SpriteFactory() {
		spriteSheets = new HashMap<String, SpriteSheet>();
			
		try {
				BufferedReader is = new BufferedReader(new InputStreamReader(getClass().getClassLoader().
					getResourceAsStream("res/sprites/players/index.txt")));
					
				String f = null;
				while ((f = is.readLine()) != null) {
					try {
						if (f.endsWith(".png")) {
							/*Image sheet = new Image(f.getPath(), trans);

							Color c = sheet.getColor(0,0);
							System.out.println("R: " + c.getRedByte() + " G: " + c.getGreenByte() + " B:" + c.getBlueByte() + " A:" + c.getAlphaByte());
							c = trans;
							System.out.println("N-R: " + c.getRedByte() + " G: " + c.getGreenByte() + " B:" + c.getBlueByte() + " A:" + c.getAlphaByte());

							sheet = sheet.getSubImage
								(1, 1,
										sheet.getWidth() - 2,
										sheet.getHeight() - 2);*/
							spriteSheets.put(f.replace(".png", ""),
									new SpriteSheet("res/sprites/players/" + f, 41, 51));

						}
					} catch (SlickException e) {

					}
				}
		} catch (Exception e) { 
			e.printStackTrace();
		}
	

		
	}
}
