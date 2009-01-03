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

import tiled.io.xml.XMLMapTransformer;

public class MapLoader implements Runnable {
	private MapMatrix m_mapMatrix;
	private XMLMapTransformer m_mapReader;
	private int m_mapX;
	private int m_mapY;
	
	public MapLoader(MapMatrix mapMatrix, int x, int y) {
		m_mapMatrix = mapMatrix;
		m_mapReader = new XMLMapTransformer();
		m_mapX = x;
		m_mapY = y;
	}

	public void run() {
		String mapName = "res/maps/" + String.valueOf(m_mapX) + "." + String.valueOf(m_mapY) + ".tmx";
		try {
			m_mapMatrix.setMap(new ServerMap(m_mapReader.readMap(mapName), m_mapX, m_mapY), m_mapX, m_mapY);
			System.out.println(mapName + " loaded");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(mapName + " could not be loaded");
		}
	}
}
