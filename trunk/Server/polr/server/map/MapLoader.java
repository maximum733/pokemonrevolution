package polr.server.map;

import java.io.File;

import tiled.io.xml.XMLMapTransformer;

public class MapLoader implements Runnable {
	private MapMatrix m_mapMatrix;
	private XMLMapTransformer m_mapReader;
	private int m_mapX;
	private int m_mapY;
	
	public MapLoader(MapMatrix mapMatrix, XMLMapTransformer mapReader, int x, int y) {
		m_mapMatrix = mapMatrix;
		m_mapReader = mapReader;
		m_mapX = x;
		m_mapY = y;
	}

	public void run() {
		String mapName = "res/maps/" + String.valueOf(m_mapX) + "." + String.valueOf(m_mapY) + ".tmx";
		try {
			m_mapMatrix.setMap(new ServerMap(m_mapReader.readMap(mapName), m_mapX, m_mapY), m_mapX, m_mapY);
		} catch (Exception e) {				
			System.err.println(mapName + " could not be loaded");
		}
	}
}
