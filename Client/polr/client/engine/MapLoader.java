package polr.client.engine;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import polr.client.GameClient;
import polr.client.ui.screen.LoadingScreen;

public class MapLoader {
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
	
	public void setMapX(int x) {
		m_mapX = x;
	}
	
	public void setMapY(int y) {
		m_mapY = y;
	}

	public void load() {
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
			//Map to the right
			try {
				map = new GameMap("res/maps/" + String.valueOf(m_mapX + 1) + "." + String.valueOf(m_mapY) + ".tmx","res/maps");
				map.setGraphics(g);
                map.setPosition(m_mapX + 1, 1, m_mapMatrix);
                map.setCurrent(false);
				m_mapMatrix.setMap(map, 2, 1);
			} catch (Exception e) {
				m_mapMatrix.setMap(null, 2, 1);
				System.out.println("Map Load Failure: (" + (m_mapX + 1) + ", " + (m_mapY) + ")");
			}
			//Map to the left
			try {
				map = new GameMap("res/maps/" + String.valueOf(m_mapX - 1) + "." + String.valueOf(m_mapY) + ".tmx","res/maps");
				map.setGraphics(g);
                map.setPosition(m_mapX - 1, 1, m_mapMatrix);
                map.setCurrent(false);
				m_mapMatrix.setMap(map, 0, 1);
			} catch (Exception e) {
				m_mapMatrix.setMap(null, 0, 1);
				System.out.println("Map Load Failure: (" + (m_mapX - 1) + ", " + (m_mapY) + ")");
			}
			//Map above
			try {
				map = new GameMap("res/maps/" + String.valueOf(m_mapX) + "." + String.valueOf(m_mapY + 1) + ".tmx","res/maps");
				map.setGraphics(g);
                map.setPosition(1, 2, m_mapMatrix);
                map.setCurrent(false);
				m_mapMatrix.setMap(map, 1, 2);
			} catch(Exception e) {
				m_mapMatrix.setMap(null, 1, 2);
				System.out.println("Map Load Failure: (" + (m_mapX) + ", " + (m_mapY + 1) + ")");
			}
			//Map below
			try {
				map = new GameMap("res/maps/" + String.valueOf(m_mapX) + "." + String.valueOf(m_mapY - 1) + ".tmx","res/maps");
				map.setGraphics(g);
                map.setPosition(1, 0, m_mapMatrix);
                map.setCurrent(false);
				m_mapMatrix.setMap(map, 1, 0);
			} catch(Exception e) {
				m_mapMatrix.setMap(null, 1, 0);
				System.out.println("Map Load Failure: (" + (m_mapX) + ", " + (m_mapY - 1) + ")");
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
