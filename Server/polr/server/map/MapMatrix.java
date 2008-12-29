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

package polr.server.map;

import polr.server.player.PlayerChar;


public class MapMatrix {
	private ServerMap[][] m_maps;
	
	public MapMatrix() {
		m_maps = new ServerMap[100][100];
	}
	
	public ServerMap getMap(int x, int y) {
		return m_maps[x+50][y+50];
	}
	
	public void setMap(ServerMap map, int x, int y) {
		map.setMapMatrix(this);
		m_maps[x+50][y+50] = map;
	}
	
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
}
