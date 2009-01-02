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

package polr.server.object;

import polr.server.map.MapMatrix;
import polr.server.map.ServerMap;
import polr.server.player.PlayerChar;

/**
 * Handles warp tiles
 * @author shinobi
 *
 */
public class WarpTile {
	private int m_mapX, m_mapY, m_x, m_y, m_warpMapX, m_warpMapY, m_warpX, m_warpY;
	
	public WarpTile(int mapX, int mapY, int x, int y, ServerMap serverMap) {
		this.m_mapX = mapX;
		this.m_mapY = mapY;
		this.m_x = x;
		this.m_y = y;
	}
	
	public void warpPlayer(PlayerChar p) {
		MapMatrix m_mapMatrix = p.getMap().getMapMatrix();
		m_mapMatrix.getMap(p.getMapX(), p.getMapY()).removePlayer(p);
		p.setX(m_warpX);
		p.setY(m_warpY);
		m_mapMatrix.getMap(m_warpMapX, m_warpMapY).addPlayer(p);
	}
	
	public void warpChar() {
		
	}
	
	public int getWarpMapX() {
		return m_warpMapX;
	}
	
	public int getWarpMapY() {
		return m_warpMapY;
	}
	
	public int getWarpX() {
		return m_warpX;
	}
	
	public int getWarpY() {
		return m_warpY;
	}

	public int getMapX() {
		return m_mapX;
	}

	public int getMapY() {
		return m_mapY;
	}

	public String getName() {
		return "";
	}

	public int getSprite() {
		return 0;
	}

	public int getX() {
		return m_x;
	}

	public int getY() {
		return m_y;
	}
}
