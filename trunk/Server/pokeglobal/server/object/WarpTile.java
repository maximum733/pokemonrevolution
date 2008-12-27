/*
 Pokemon Global. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright © 2007-2008 Pokemon Global Team

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

package pokeglobal.server.object;

import pokeglobal.server.map.ServerMap;

public abstract class WarpTile extends NonPlayerChar {
	private int mapX, mapY, x, y;
	
	public WarpTile(int mapX, int mapY, int x, int y, ServerMap serverMap) {
		this.mapX = mapX;
		this.mapY = mapY;
		this.x = x;
		this.y = y;
		this.setMap(serverMap);
		this.setSprite("Invisible");
		this.setName("");
	}
	
	@Override
	public abstract void speakTo(PlayerChar target);
	
	public int getWarpMapX() {
		return mapX;
	}
	
	public int getWarpMapY() {
		return mapY;
	}
	
	public int getWarpX() {
		return x;
	}
	
	public int getWarpY() {
		return y;
	}
}
