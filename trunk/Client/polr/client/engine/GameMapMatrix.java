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

package polr.client.engine;

import java.util.Hashtable;

import polr.client.logic.Player;

public class GameMapMatrix {
	private GameMap[][] m_maps;
	private Hashtable<String, Player> m_players;
	private Player currentPlayer;
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	public Hashtable<String, Player> getPlayerList() {
		return m_players;
	}
	public GameMap getCurrentMap() {
		return m_maps[1][1];
	}
	public GameMapMatrix() {
		m_maps = new GameMap[3][3];
		m_players = new Hashtable<String, Player>();
	}
	
	public GameMap getMap(int x, int y) {
		return m_maps[x][y];
	}
	
	public void setMap(GameMap map, int x, int y) {
		m_maps[x][y] = map;
	}
	public void setCurrentPlayer(Player p) {
		currentPlayer = p;
	}
	protected void addToPlayers(Player p) {
		if (m_players.get(p.username) != null){
			m_players.remove(p.username);
		}
		m_players.put(p.username, p);
	}
	public Player findPlayer(String playerID) {
		for(int i = 0; i < getMap(1,1).getMapPlayers().size(); i++) {
			if(getMap(1,1).getMapPlayers().get(i).index == Long.parseLong(playerID))
				return getMap(1,1).getMapPlayers().get(i);
		}
		return null;
	}
}
