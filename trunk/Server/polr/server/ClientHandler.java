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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;

import polr.server.battle.PvPBattleField;
import polr.server.database.PlayerDataManager;
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
		m_moveSets = ms;
		m_moveList = ml;

		m_applyItem = new ApplyItem();
		m_mapMatrix = new MapMatrix();
		m_mapReader = new XMLMapTransformer();
		
		
		//mapMatrix.setMap(new ServerMap(mapReader.readMap("res/maps/0.0.tmx"), 0, 0), 0, 0); 
		
		m_persistor = new PlayerDataManager(m_mapMatrix, m_moveList);
		new Thread(m_persistor).start();
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
		//TODO: Add Ip ban check here
		String line = msg.toString().trim();
		PlayerChar player = null;
		if (session.containsAttribute("player"))
			player = (PlayerChar) session.getAttribute("player");
		switch(line.charAt(0)) {
		case 'l':
			//User is logging in
			break;
		case 'r':
			//User is registering
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

	   /**
	    * Invoked when a client joins the server
	    * @param IoSession session - A client session
		*/
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("Session created...");

		if (session.getTransportType() == TransportType.SOCKET)
			((SocketSessionConfig) session.getConfig())
			.setReceiveBufferSize(2048);

		//session.setIdleTime(IdleStatus.BOTH_IDLE, 10);
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
}