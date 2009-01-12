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
	private int m_x, m_y, m_warpMapX, m_warpMapY, m_warpX, m_warpY;
	private int m_badgeReq, m_itemReq, m_questReq;
	
	/**
	 * Constructor
	 * @param x
	 * @param y
	 * @param warpX
	 * @param warpY
	 * @param warpMapX
	 * @param warpMapY
	 * @param badgeReq
	 * @param itemReq
	 * @param questReq
	 */
	public WarpTile(int x, int y, int warpX, int warpY, int warpMapX,
			int warpMapY, int badgeReq, int itemReq, int questReq) {
		this.m_x = x;
		this.m_y = y;
		this.m_warpX = warpX;
		this.m_warpY = warpY;
		this.m_warpMapX = warpMapX;
		this.m_warpMapY = warpMapY;
		this.m_badgeReq = badgeReq;
		this.m_itemReq = itemReq;
		this.m_questReq = questReq;
	}
	
	/**
	 * Warps a player
	 * @param p
	 */
	public void warpPlayer(PlayerChar p) {
		if(p.getBadgeCount() >= m_questReq && (m_itemReq > 0 && p.getBag().hasItem(m_itemReq)) &&
				p.hasCompletedQuest(m_questReq)) {
			MapMatrix m_mapMatrix = p.getMap().getMapMatrix();
			m_mapMatrix.getMap(p.getMapX(), p.getMapY()).removePlayer(p);
			p.setX(m_warpX);
			p.setY(m_warpY);
			m_mapMatrix.getMap(m_warpMapX, m_warpMapY).addPlayer(p);
		}
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

	public int getX() {
		return m_x;
	}

	public int getY() {
		return m_y;
	}
}
