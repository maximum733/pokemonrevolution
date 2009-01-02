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

package polr.server;

//import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;

import polr.server.battle.PvPBattleField;
import polr.server.database.PlayerDataManager;
import polr.server.map.MapLoader;
import polr.server.map.MapMatrix;
import polr.server.mechanics.moves.MoveList;
import polr.server.mechanics.moves.MoveSetData;
import polr.server.player.PlayerChar;
import polr.server.trade.TradeLogic;
import tiled.io.xml.XMLMapTransformer;

/**
 * @author TMKCodes
 * @author Shinobi
 * Deals with connects, disconnects and packets received from the client
 * 
 */
public class ClientHandler extends IoHandlerAdapter {

	private MoveList m_moveList;
	private MoveSetData m_moveSets;
	private MapMatrix m_mapMatrix;

	private static ArrayList<String> m_ipbans = new ArrayList<String>();

	private PlayerDataManager m_persistor;
	private XMLMapTransformer m_mapReader;

	private static Map<String, PlayerChar> m_playerList;
	private ApplyItem m_applyItem;
	private boolean m_lockdown;

	static { 
		m_playerList = new HashMap<String, PlayerChar>();
	}

	   /**
	    * Returns a map of Players (PlayerChar object) on the server, the String is their username.
	    * @return Map<String, PlayerChar>
		*/
	public static Map<String, PlayerChar> getPlayerList() {
		return m_playerList;
	}
	
	   /**
	    * Standard Constructor
	    * @param BattleMechanics m - JewelMechanics Database
	    * @param MoveSetData ms - Moveset Database
	    * @param PokemonSpeciesData s - Pokemon Species Information
	    * @param MoveList ml - Movelist Database
	    * @param POLRDatabase polr - Database retrieved based on polr.ini
		*/
	public ClientHandler(MoveSetData ms, MoveList ml)
	throws Exception {
		m_lockdown = false;
		m_moveSets = ms;
		m_moveList = ml;

		m_applyItem = new ApplyItem();
		m_mapMatrix = new MapMatrix();
		m_mapReader = new XMLMapTransformer();
		
		//Load all the maps. Limit the map loading threads to ~20
		int initialThreadCount = Thread.activeCount();
		File map;
		for(int x = -50; x < 50; x++) {
			for(int y = -50; y < 50; y++) {
				map = new File("res/maps/" + String.valueOf(x) + "." + String.valueOf(y) + ".tmx");
				if(map.exists()) {
					new Thread(new MapLoader(m_mapMatrix, m_mapReader, x, y));
				}
				while(Thread.activeCount() > 20);
			}
		}
		//Wait for all the maps to finish loading
		while(initialThreadCount != Thread.activeCount());
		
		//Start the Player Data Manager
		m_persistor = new PlayerDataManager(m_mapMatrix, m_moveList);
		new Thread(m_persistor).start();
		
		//Start moving the NPCs
		new Thread(m_mapMatrix).start();
	}
	
	   /**
	    * If a disconnect-causing error occurs, attempt to save all the player data. If they were in a PvP Battle, they lose the battle.
		*/
	public void exceptionCaught(IoSession session, Throwable t)
	throws Exception {
		t.printStackTrace();
		try {
			PlayerChar player = (PlayerChar) session.getAttribute("player");
			if(player.isBattling()) {
				if(player.getField() instanceof PvPBattleField) {
					PvPBattleField bField = (PvPBattleField) player.getField();
					bField.playerDisconnected(player.getBattleID());
				}
			}
			player.getMap().removePlayer(player);
			m_playerList.remove(player.getName());
			m_persistor.attemptSave(player);
		} catch (NullPointerException e) {}
		session.close();
	}
	
	   /**
	    * Once the server receives a packet from the client, this method is run.
	    * @param IoSession session - A client session
	    * @param Object msg - The packet received from the client
		*/
	@SuppressWarnings("unchecked")
	public void messageReceived(IoSession session, Object msg) throws Exception {
		if(!m_lockdown) {
			//TODO: Add Ip ban check here
			String line = msg.toString().trim();
			String [] details;
			PlayerChar player = null;
			if (session.containsAttribute("player"))
				player = (PlayerChar) session.getAttribute("player");
			switch(line.charAt(0)) {
			case 'l':
				//User is logging in
				details = line.substring(1).split(",");
				m_persistor.attemptLogin(details[0], details[1], session);
				break;
			case 'r':
				//User is registering
				details = line.substring(1).split(",");
				if(m_persistor.register(details[0], details[1], details[2], 
						Integer.parseInt(details[3]), Integer.parseInt(details[4]))) {
					session.write("rs");
				} else
					session.write("re");
				break;
			case 'U':
				//User is moving up
				break;
			case 'D':
				//User is moving down
				break;
			case 'L':
				//User is moving left
				break;
			case 'R':
				//User is moving right
				break;
			}
		}
	}

	   /**
	    * Invoked when a client joins the server
	    * @param IoSession session - A client session
		*/
	public void sessionCreated(IoSession session) throws Exception {
		if(!m_lockdown) {
			System.out.println("Session created...");

			if (session.getTransportType() == TransportType.SOCKET)
				((SocketSessionConfig) session.getConfig())
				.setReceiveBufferSize(2048);
		} else {
			session.write("!0");
			session.close();
		}
	}
	
	   /**
	    * When the user disconnects at will, attempt to save all data
	    * @param IoSession session
		*/
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		try {
			PlayerChar player = (PlayerChar) session.getAttribute("player");
			if(player.isBattling()) {
				if(player.getField() instanceof PvPBattleField) {
					PvPBattleField bField = (PvPBattleField) player.getField();
					bField.playerDisconnected(player.getBattleID());
				}
			}
			if(player.getIsTrading()){
				TradeLogic tL = player.getTradeLogic();
				tL.playerDisconnected();
			}
			player.getMap().removePlayer(player);
			m_playerList.remove(player.getName());
			m_persistor.attemptSave(player);
		} catch (Exception e) {}
	}

	/**
	 * Logs every player out of the game. Returns true when completed.
	 * @return
	 */
	public boolean logoutAll() {
		m_lockdown = true;
		for(PlayerChar p: m_playerList.values()) {
			try {
				p.getIoSession().write("!0");
				sessionClosed(p.getIoSession());
			} catch (Exception e) {
				System.err.println("Error saving " + p.getName() + "'s account.");
			}
		}
		return true;
	}
}