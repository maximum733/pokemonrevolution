package polr.client.engine;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import polr.client.GameClient;
import polr.client.ui.window.LoadingScreen;

public class MapLoader implements Runnable {
	private GameMapMatrix m_mapMatrix;
	private int m_mapX;
	private int m_mapY;
	private Graphics g;
	private LoadingScreen m_loading;
	
	public MapLoader(GameMapMatrix m_mapMatrix, int mapX, int mapY, Graphics g, LoadingScreen loading) {
		this.m_mapMatrix = m_mapMatrix;
		m_mapX = mapX;
		m_mapY = mapY;
		this.g = g;
		m_loading = loading;
	}

	@Override
	public void run() {
		GameMap map;
		try {
			map = new GameMap("res/maps/" + (m_mapX) + "." + (m_mapY) + ".tmx","res/maps");
			map.setGraphics(g);
            map.setPosition(1, 1, m_mapMatrix);
            map.setCurrent(true);
			m_mapMatrix.setMap(map, 1, 1);
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Load the other maps
		if(m_mapX > -30 && m_mapY > -30) {
			for(int x = -1; x < 2; x++) {
				try {
					if(x != 0) {
						map = new GameMap("res/maps/" + String.valueOf(m_mapX + x) + "." + String.valueOf(m_mapY) + ".tmx","res/maps");
						map.setGraphics(g);
		                map.setPosition(x + 1, 0, m_mapMatrix);
						m_mapMatrix.setMap(map, x + 1, 0);
					}
				} catch (Exception e) {
					m_mapMatrix.setMap(null, x + 1, 0);
					System.out.println("Map Load Failure: (" + (m_mapX + x) + ", " + (m_mapY) + ")");
				}
			}
			for(int y = -1; y < 2; y++) {
				try {
					if(y != 0) {
						map = new GameMap("res/maps/" + String.valueOf(m_mapX) + "." + String.valueOf(m_mapY + y) + ".tmx","res/maps");
						map.setGraphics(g);
		                map.setPosition(0, y + 1, m_mapMatrix);
						m_mapMatrix.setMap(map, 0, y + 1);
					}
				} catch (Exception e) {
					m_mapMatrix.setMap(null, 0, y + 1);
					System.out.println("Map Load Failure: (" + (m_mapX + 0) + ", " + (m_mapY + y) + ")");
				}
			}
		} else {
			m_mapMatrix.setMap(null, 0, 0);
			m_mapMatrix.setMap(null, 1, 0);
			m_mapMatrix.setMap(null, 2, 0);
			m_mapMatrix.setMap(null, 0, 1);
			m_mapMatrix.setMap(null, 2, 1);
			m_mapMatrix.setMap(null, 0, 2);
			m_mapMatrix.setMap(null, 1, 2);
			m_mapMatrix.setMap(null, 2, 2);
		}
		map = m_mapMatrix.getMap(1, 1);
		map.setXOffset(map.getXOffset());
		map.setYOffset(map.getYOffset());
		
		GameClient.getPacketGenerator().write("um");
		m_loading.setVisible(false);
	}
}
