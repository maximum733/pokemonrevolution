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

package polr.server.object;

import org.simpleframework.xml.Root;

import polr.server.map.ServerMap;
import polr.server.map.ServerMap.Directions;

@Root
public interface Positionable {
	public int getX();
	public int getY();

	public String getName();
	public String getSprite();

	public Directions getFacing();
	public ServerMap getMap();

	public void setSprite(String sprite);

	public int getMapX();
	public int getMapY();
	
	public void setMap(ServerMap map);
	public void setVisible(boolean visible);
	
	public boolean isVisible();
}
