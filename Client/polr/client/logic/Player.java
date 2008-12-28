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

import org.newdawn.slick.Image;

import polr.client.engine.GameMap;

public class Player {
	private static SpriteFactory spriteFactory;
	
	// Foot to place for walking animation
	public boolean      leftorright;

	// Displayed sprite
	public Image        curImg;

	// Current Coordinates
	public short        x;
	public short        y;

	// Server Coordinates
	public short        svrX;
	public short        svrY;

	// User Data
	public String       username;

	// is this player yours?
	public boolean      thisPlayer;

	// can it be challenged/PMed/whatever?
	public boolean      isNPC = false;

	// is it animating?
	private boolean     animating;

	// type of sprite
	public String spriteType;
	
	// the game map
	public GameMap      map;

	// possible directions
	public Dirs facing;
	
	//unknown index, does not seem to be called in object
	public long index;
	
	//loads sprite
	public static void loadSprites() {
		spriteFactory = new SpriteFactory();
	}
	//sets direction of sprite
	public void setFacing(Dirs facing) {
		try {
			curImg = Player.spriteFactory.getSprite(facing,
					false, leftorright, spriteType);
		} catch (Exception e) {
			spriteType = "Invisible";
		}
		this.facing = facing;
	}
	
	//response to movement
	public Dirs dirValue(String direction) {
		if(direction.equalsIgnoreCase("Up"))
			return Dirs.Up;
		else if(direction.equalsIgnoreCase("Down"))
			return Dirs.Down;
		else if(direction.equalsIgnoreCase("Left"))
			return Dirs.Left;
		else
			return Dirs.Right;
	}

	//directions
	public enum Dirs {
		Up, Down, Left, Right
	}

	public Player() {

		// starting coordinates rel. to map
		x = 0;
		y = -8;
		svrX = 0;
		svrY = -8;
	}

	// Move Up
	public void moveUp() {
		svrY -= 32;
		facing = Dirs.Up;
		curImg = spriteFactory.getSprite(Dirs.Up, true, leftorright, spriteType);
		leftorright = !leftorright;
		setAnimating(true);
	}

	// Move Down
	public void moveDown() {
		svrY += 32;
		facing = Dirs.Down;
		curImg = spriteFactory.getSprite(Dirs.Down, true, leftorright, spriteType);
		leftorright = !leftorright;
		setAnimating(true);	
	}

	// Move Left
	public void moveLeft() {
		svrX -= 32;
		facing = Dirs.Left;
		curImg = spriteFactory.getSprite(Dirs.Left, true, leftorright, spriteType);
		leftorright = !leftorright;
		setAnimating(true);
	}

	// Move Right
	public void moveRight() {
		svrX += 32;
		facing = Dirs.Right;
		curImg = spriteFactory.getSprite(Dirs.Right, true, leftorright, spriteType);
		leftorright = !leftorright;
		setAnimating(true);
	}

	// is the person animating?
	public void setAnimating(boolean animating) {
		this.animating = animating;
	}

	// person is animating
	public boolean isAnimating() {
		return animating;
	}

}
