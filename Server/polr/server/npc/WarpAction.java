package polr.server.npc;

import polr.server.map.MapMatrix;
import polr.server.player.PlayerChar;

public class WarpAction implements NpcAction {
	private int m_mapX;
	private int m_mapY;
	private int m_x;
	private int m_y;
	
	public WarpAction(int mapX, int mapY, int x, int y) {
		m_mapX = mapX;
		m_mapY = mapY;
		m_x = x;
		m_y = y;
	}
	
	public void execute(PlayerChar p) {
		MapMatrix m_mapMatrix = p.getMap().getMapMatrix();
		m_mapMatrix.getMap(p.getMapX(), p.getMapY()).removePlayer(p);
		p.setX(m_x);
		p.setY(m_y);
		m_mapMatrix.getMap(m_mapX, m_mapY).addPlayer(p);
	}

}
