/*
 Pokemon Online Revolution. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright � 2007-2008 Pokemon Online Revolution Team

 This file is part of Pokemon Online Revolution.

 Pokemon Online Revolution is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Pokemon Online Revolution is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Pokemon Online Revolution.  If not, see <http://www.gnu.org/licenses/>.
*/

package polr.server.map;

import java.util.Random;

import polr.server.GameServer;
import polr.server.npc.NonPlayerChar;
import polr.server.player.PlayerChar;

/**
 * Stores all game maps in a 100x100 array. Also handles NPC movement.
 * 
 * @author TMKCodes
 * @author Shinobi
 *
 */
public class MapMatrix  implements Runnable {
	private ServerMap[][] m_maps;
	
	/**
	 * Default constructor
	 */
	public MapMatrix() {
		m_maps = new ServerMap[100][100];
	}
	
	/**
	 * Returns the requested map
	 * @param x
	 * @param y
	 * @return
	 */
	public ServerMap getMap(int x, int y) {
		return m_maps[x+50][y+50];
	}
	
	/**
	 * Sets a map in the map matrix
	 * 
	 * @param map
	 * @param x
	 * @param y
	 */
	public void setMap(ServerMap map, int x, int y) {
		map.setMapMatrix(this);
		m_maps[x+50][y+50] = map;
	}
	
	/**
	 * Moves a player between maps
	 * 
	 * @param character
	 * @param origin
	 * @param dest
	 */
	public void moveBetweenMaps(
			PlayerChar character, ServerMap origin, ServerMap dest) {
		origin.removePlayer(character);
		
		// reposition player so they're on the correct edge
		if (origin.getX() > dest.getX()) { // dest. map is to the left
			character.setX(dest.getWidth() * 32 - 32);
			character.setY(character.getY() + origin.getYOffsetModifier() - dest.getYOffsetModifier());
		} else if (origin.getX() < dest.getX()) { // to the right
			character.setX(0);
			character.setY(character.getY() + origin.getYOffsetModifier() - dest.getYOffsetModifier());
		} else if (origin.getY() > dest.getY()) {// up
			character.setY(dest.getHeight() * 32 - 40);
			character.setX((character.getX() + origin.getXOffsetModifier()) - dest.getXOffsetModifier());
		} else if (origin.getY() < dest.getY()) {// down
			character.setY(-8);
			character.setX((character.getX() - dest.getXOffsetModifier()) + origin.getXOffsetModifier());
		}
		dest.addPlayer(character);
	}

	/**
	 * Handles npc movements
	 */
	public void run() {
		ServerMap map = null;
		Random random = new Random();
		while(true) {
			while(map == null) {
				map = this.getMap(random.nextInt(99) - 50, random.nextInt(99) - 50);
				try {
					Thread.sleep(25);
				} catch(Exception e){}
			}
			NonPlayerChar npc = map.getRandomNPC();
			while(!npc.move(GameServer.getMechanics().getRandomDirection()));
			try {
				Thread.sleep(500);
			} catch(Exception e){}
		}
	} 
}
