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

package polr.server;

//import java.sql.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;

import polr.server.battle.BattleField;
import polr.server.battle.BattleTurn;
import polr.server.battle.Pokemon;
import polr.server.battle.PvPBattleField;
import polr.server.battle.WildBattleField;
import polr.server.database.PersistenceManager;
import polr.server.exception.LoginException;
import polr.server.exception.RegisterException;
import polr.server.map.MapMatrix;
import polr.server.map.ServerMap;
import polr.server.map.ServerMap.Directions;
import polr.server.mechanics.moves.MoveList;
import polr.server.mechanics.moves.MoveSetData;
import polr.server.object.NonPlayerChar;
import polr.server.object.PlayerChar;
import polr.server.object.ShopKeeper;
import polr.server.object.TrainerNonPlayerChar;
import polr.server.trade.TradeLogic;
import tiled.io.xml.XMLMapTransformer;

/**
 * @author Pivot
 * @author Ryan
 * @author TMKCodes
 * @author Fshy
 *		mapReader.
 * Deals with connects, disconnects and packets received from the client
 * 
 */
public class ClientHandler extends IoHandlerAdapter {

	private MoveList moveList;
	private MoveSetData moveSets;
	private MapMatrix mapMatrix;

	private static ArrayList<String> ipbans = new ArrayList<String>();

	private PersistenceManager persistor;
	private XMLMapTransformer mapReader;

	private static Map<String, PlayerChar> playerList;
	private Moderator modr;
	private ChatCommands chatCmd;
	private ApplyItem applyItem;

	static { 
		playerList = new HashMap<String, PlayerChar>();
	}

	   /**
	    * Returns a map of Players (PlayerChar object) on the server, the String is their username.
	    * @return Map<String, PlayerChar>
		*/
	public static Map<String, PlayerChar> getPlayerList() {
		return playerList;
	}
	
	public static boolean isIPBanned(String ip) {
		if(ipbans.contains(ip))
			return true;
		return false;
	}

	public static void addIPBan(String ip) {
		ipbans.add(ip);
		saveIPBans();
	}
	
	static void saveIPBans() {
		try
		{	
			PrintWriter banIP = new PrintWriter(new FileWriter(
					"res/ipbans.txt"));
			for(int i = 0; i < ipbans.size(); i++)
			{
				banIP.println(ipbans.get(i));
			}
			banIP.close();
		}
		catch (Exception e)
		{
			System.out.println("Could not save ip ban list");
		}
	}
	
	static void loadIPBans() {
		try
		{
			ipbans.clear();
			Scanner bannedIP = new Scanner(new File("res/ipbans.txt"));
			while(bannedIP.hasNextLine())
			{
				String s = bannedIP.nextLine();
				ipbans.add(s);
			}
			bannedIP.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	public static void removeIPBan(String ip) {
		ipbans.remove(ip);
		saveIPBans();
	}
	
	   /**
	    * No idea what this does!
	    * @return Map<String, Team>
		*/
	
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
		moveSets = ms;
		moveList = ml;

		applyItem = new ApplyItem();
		mapMatrix = new MapMatrix();
		mapReader = new XMLMapTransformer();
		
		loadMaps(mapMatrix);
		persistor = new PersistenceManager("", mapMatrix, moveList);
		new Thread(persistor).start();

		loadIPBans();
		modr = new Moderator();
		chatCmd = new ChatCommands();
	}
	
	   /**
	    * If a disconnect-causing error occurs, attempt to save all the player data. If they were in a PvP Battle, they lose the battle.
		*/
	public void exceptionCaught(IoSession session, Throwable t)
	throws Exception {
		// players.entrySet().remove(session);
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
			playerList.remove(player.getName());
			persistor.save(player);
		} catch (NullPointerException e) {}
		session.close();
		// sendToAll(getUserData());
	}

	public void loadMaps(MapMatrix matrix){
		int a, b;
		String path;
		a = 0;
		b = 0;
		for (int matX = 0; matX < 100; matX++){
			for (int matY = 0; matY < 100; matY++){
				path = "res/maps/" + a + "." + b + ".tmx";
				try{
					mapMatrix.setMap(
							new ServerMap(mapReader.readMap(
									path), a, b), a, b);
					System.out.println("Success " + a + " " + b);
				} catch (Exception e) {}
				if (b == -99)
					b = 1;
				b--;
			}
			a--;
		}
	}
	
	   /**
	    * Once the server receives a packet from the client, this method is run.
	    * @param IoSession session - A client session
	    * @param Object msg - The packet received from the client
		*/
	@SuppressWarnings("unchecked")
	public void messageReceived(IoSession session, Object msg) throws Exception {
		String line = msg.toString().trim();
		PlayerChar player = null;
		if (session.containsAttribute("player"))
			player = (PlayerChar) session.getAttribute("player");
		if (player != null) {
			if (player.isFrozen() && player.isMuted() && (player.getDisableExpiration() > 0))
			{
				if (player.getDisableExpiration() < System.currentTimeMillis())
				{
					player.setFrozen(false);
					player.setMuted(false);
					player.setFreezeExpiration(0);
					player.setMuteExpiration(0);
					player.setDisableExpiration(0);
				}
			}
			if (player.isFrozen())
			{
				if ((player.getFreezeExpiration() > 0) && (player.getFreezeExpiration() < System.currentTimeMillis()))
				{
						player.setFrozen(false);
						player.setFreezeExpiration(0);
				}
			}
			if (player.isMuted())
			{
				if ((player.getMuteExpiration() > 0) && (player.getMuteExpiration() < System.currentTimeMillis()))
				{
					player.setMuted(false);
					player.setMuteExpiration(0);
				}
			}
		}
		if (!session.containsAttribute("player") || 
				!player.isFrozen() || line.charAt(0) == 'c') {
			
			System.out.println(line);
			switch (line.charAt(0)) {
			case 'S':
				player.setSurfing(true);
				player.move(player.getFacing());
				break;
			case 'Q': // Disconnect user
				session.close();
				break;
			case 'u':
				//Player is requesting some form of information
				switch(line.charAt(1)) {
				case 'p':
					//Requesting Pokemon info
					player.updateClientParty();
					break;
				case 'b':
					//Requesting Bag info
					player.initialiseClientBag();
					break;
				case 'm':
					player.getMap().propagateMapData(player);
					break;
				}
				break;
			case 'm':
				//Player has maps loaded, send them player info
				player.getMap().propagateMapData(player);
				break;
			case 'l':
				//User is logging in. USERNAME:PASSWORD
				String[] parsed = line.split(new String(new char[] { (char) 27 }));
				try {
					persistor.authUser(parsed[0].substring(1, parsed[0].length()), parsed[1], session);
				} catch (LoginException e) {
					switch (e.getType()) {
					case ACCOUNT_NOT_EXIST:
						session.write("l0");
						break;
					case WRONG_PASSWORD:
						session.write("l1");
						break;
					case OTHER:
						session.write("l2");
						break;
					case BANNED:
						session.write("l3");
						break;
					case WAIT:
						session.write("l4");
						break;
					case SAVEQUEUE:
						session.write("l5");
						break;
					}
				}
				break;
			case 'r':
				//User is registering. USERNAME:PASSWORD:STARTERPOKEMON:CHARACTERSPRITE:EMAIL
				String parse[] = line.split(new String(new char[] { (char) 27 }));
				try {
					persistor.registerUser(
							parse[0].substring(1, parse[0].length()), parse[1],
							parse[4], Integer.parseInt(parse[3]),  Integer.parseInt(parse[2]));
					session.write("rs");
				} catch (RegisterException e) {
					switch (e.getType()) {
					case ALREADY_EXISTS:
						session.write("r0");
						break;
					case BANNED_USERNAME:
						session.write("r1");
						break;
					case INVALID_DATA:
						session.write("r2");
						break;
					case OTHER:
						session.write("r3");
						break;
					case LOCKDOWN:
						session.write("r4");
					}
				}
				break;
			case 'c':
				//User is chatting
				if (!player.isMuted()) {
					String filtered = modr.filter(line.substring(2), player);
					filtered = chatCmd.filter(filtered, player);
					if (!filtered.equals("")) {
						switch(line.charAt(1)) {
						case 'w':
							//World chat
							if (player.isSilentMuted())
							{
								player.getIoSession().write("cw<" + player.getName() + "> " + filtered);
							}
							else
							{
								for(int x = -50; x < 50; x++) {
									for(int y = 0; y < 50; y++) {
										ServerMap sm = mapMatrix.getMap(x,y);
										if(sm != null)
											sm.sendToAll("cw<" + player.getName() + "> " + filtered);
									}
								}
							}
							break;
						case 'l':
							//Local chat
							if (player.isSilentMuted())
							{
								player.getIoSession().write("cl<" + player.getName() + "> " + filtered);
							}
							else
							{
								player.getMap().sendToAll("cl<" + player.getName() + "> " + filtered);
							}
							break;
						}
					}
				}
				break;
			case 'P':
				//Player is switching Pokemon outside of battle
				break;
			case 'b':
				//A battle packet.
				switch(line.charAt(1)) {
				case 'm':
					//Player selected a move
                    if (player.isBattling()) {
                        BattleField bField = player.getField();
                        BattleTurn ourSwitch = BattleTurn.getMoveTurn(Integer
                                        .parseInt(line.substring(2)));
                        bField.queueMove(player.getBattleID(), ourSwitch);
                    }
                    break;
				case 'r':
					//Player ran
                    if (player.getField() != null) {
                        if (player.getField() instanceof WildBattleField) {
                                WildBattleField field = 
                                        (WildBattleField)player.getField();
                                field.run();
                        }
                    }
					break;
				case 's':
					//Player switched Pokemon
                    if (player.isBattling()) {
                        BattleField bField = player.getField();
                        BattleTurn ourSwitch = BattleTurn.getSwitchTurn(Integer
                                        .parseInt(line.substring(1)));
                        bField.queueMove(player.getBattleID(), ourSwitch);
                    }
					break;

				case 'M':
					//Player is learning a move
                    String[] comp = line.split(",");
                    Pokemon moveLearner = player.getParty()
                    [Integer.parseInt(comp[1])];
                    if (moveLearner.getMovesLearning().contains(comp[2])) {
                            moveLearner.learnMove(
                                            Integer.parseInt(comp[3]), comp[2]);
                    }
                    break;

				case 'e':
					//Player is allowing evolution
					break;
				}
			case 'B':
				//Bag operation
				switch(line.charAt(1)) {
				case 'd':
					//Dropping an item
					int itemNum = Integer.parseInt(line.substring(2));
					if(player.getBag().hasItem(itemNum)) {
						player.getMap().placeItem(player.getX(), player.getY(), itemNum);
						player.getBag().useItem(itemNum);
						player.getIoSession().write("Br" + itemNum);
					}
					break;
				case 'u':
					//Using an item
					if(line.indexOf(",") > 0) {
						//Extra information was sent for this item
						int itemNo = Integer.parseInt(line.substring(2, line.indexOf(',')));
						if(player.getBag().hasItem(itemNo)) {
							if(applyItem.useItem(player, itemNo, line.substring(line.indexOf(',') + 1))) {
								player.getBag().useItem(itemNo);
								player.getIoSession().write("Br" + itemNo);
							}
						}
					} else {
						//No extra information sent, e.g. PokeBall being used
						int itemNo = Integer.parseInt(line.substring(2));
						if(player.getBag().hasItem(itemNo)) {
							if(applyItem.useItem(player, itemNo, "")) {
								player.getBag().useItem(itemNo);
								player.getIoSession().write("Br" + itemNo);
							}
						}
					}
					break;
				}
				break;
			case 'T':
				//Talk/Interact
				switch(line.charAt(1)) {
				case 'T':
					String[] coords = line.split(new String(new char[] { 27 }));
					int npcX = Integer.parseInt(coords[1]);
					int npcY = Integer.parseInt(coords[2]);
					NonPlayerChar npc = player.getMap().getNPCAt(npcX, npcY);
					if (npc != null && !player.isBlocked()) {
						if (player.isFacing(npc)) {
							player.setTalking(true);
							player.setTalkingTo(npc);
							npc.speakTo(player);
						}
					}
					if(!player.isBlocked() && player.getMap().itemAt(npcX, npcY)) {
						int mapItemId = player.getMap().takeItem(player);
						if(mapItemId > -1) {
							player.getIoSession().write("Ba" + mapItemId);
						}
					}
					/*else {
						for(String key : playerList.keySet()) {
							PlayerChar p = playerList.get(key);
							if(p.getX() == npcX && p.getY() == npcY && !p.isBlocked() && !player.isBlocked())
								player.getIoSession().write("*" + p.getName() + "," + p.getHighestLevel());
						}
					}*/
					break;
				case 'F':
					//Finished talking
					if(player.getTalkingTo() instanceof TrainerNonPlayerChar) {
                        player.setTalking(false);
                        TrainerNonPlayerChar t = (TrainerNonPlayerChar) player.getTalkingTo();
                        t.setupPokemon();
                        player.setTalkingTo(null);
                        player.startTrainerBattle(t);
					} else {
                        player.setTalking(false);
                        player.setTalkingTo(null);
					}
					break;
				}
				
				break;
			case 'U':
				//Player is moving up
				player.move(Directions.up);
				break;
			case 'L':
				//Player is moving left
				player.move(Directions.left);
				break;
			case 'D':
				//Player is moving down
				player.move(Directions.down);
				break;
			case 'R':
				//Player is moving right
				player.move(Directions.right);
				break;
			case 'x':
				if (player.getTalkingTo() instanceof ShopKeeper) {
					
				}
				break;
			}
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
	    * Normalise a player's name (no idea why this is here?)
	    * @param String playername
		*/
	private String normalize(String playername) {
		String[] parts = playername.split(" | ");
		return parts[0];
	}
	
	   /**
	    * When the user disconnects at will, attempt to save all data
	    * @param IoSession session
		*/
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
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
			playerList.remove(player.getName());
			persistor.save(player);
		} catch (NullPointerException e) {
		}
	}
	
	/* OLD PROTOCOL CODE */
	
	/*case 'u': // use item
	if (player.getField() != null) {
		for (Baggable b : player.getBag()) {
			if (b.getName().equals(line.substring(1))) {
				if (b.useInBattle(player, player.getField())) {
					player.getBag().remove(b);
					player.updateClientBag();
				}
				break;
			}
		}
	}
	break;
case 'r':
	if (player.getField() != null) {
		if (player.getField() instanceof WildBattleField) {
			WildBattleField field = 
				(WildBattleField)player.getField();
			field.run();
		}
	}
	break;
case 'j': // I'd like to join the team I'm currently looking at.
	if (player.getTalkingTo().getNPCTag().equals("team")) {
		Team team = (Team)player.getTarget();
		team.apply(player);

		player.setTalkingTo(null);
		player.setTarget(null);
		player.setTalking(false);
	}
	break;
case '*': // remove this guy from my team
	if (player.getTalkingTo().getNPCTag().equals("team")) {
		Team team = (Team)player.getTarget();
		if (team.getPermissions(player.getName()) == 
				Permissions.FOUNDER) {
			team.removeMember(normalize(line.substring(1)));

			player.setTalkingTo(null);
			player.setTarget(null);
			player.setTalking(false);
		}
	}
	break;
case '#': // give founder
	if (player.getTalkingTo().getNPCTag().equals("team")) {
		Team team = (Team)player.getTarget();
		if (team.getPermissions(player.getName()) == 
				Permissions.FOUNDER) {
			team.newFounder(player.getName(), normalize(line.substring(1)));

			player.setTalkingTo(null);
			player.setTarget(null);
			player.setTalking(false);
		}
	}
	break;
case '^': // approve this team applicant
	if (player.getTalkingTo().getNPCTag().equals("team")) {
		Team team = (Team)player.getTarget();
		if (team.getPermissions(player.getName()) == 
				Permissions.FOUNDER) {
			team.approve(line.substring(1));

			player.setTalkingTo(null);
			player.setTarget(null);
			player.setTalking(false);
		}
	}
	break;
case 's':
	if (player.isBattling()) {
		BattleField bField = player.getField();
		BattleTurn ourSwitch = BattleTurn.getSwitchTurn(Integer
				.parseInt(line.substring(1)));
		bField.queueMove(player.getBattleID(), ourSwitch);
	}
	break;
case 'M':
	if (player.isBattling()) {
		BattleField bField = player.getField();
		BattleTurn ourSwitch = BattleTurn.getMoveTurn(Integer
				.parseInt(line.substring(1)));
		bField.queueMove(player.getBattleID(), ourSwitch);
	}
	break;
case 'C':
	if (!player.isMuted()) {
		String filtered = modr.filter(line.substring(1), player);
		filtered = chatCmd.filter(filtered, player);
		if (!filtered.equals(""))
			player.getMap()
			.sendToAll("C<" + player.getName() + "> " + filtered);
	}
	break;
/*case 'l': // learn a move
	String[] comp = line.split(",");
	Pokemon moveLearner = player.getParty()
	[Integer.parseInt(comp[1])];
	if (moveLearner.getMovesLearning().contains(comp[2])) {
		moveLearner.learnMove(
				Integer.parseInt(comp[3]), comp[2]);
	}
	break;
case 'c': // challenge someone to a battle
	PlayerChar target = playerList.get(line.substring(1, line.indexOf(',')));
	long amount = (long) Integer.parseInt(line.substring(line.indexOf(',') + 1));
	if (target != null &&
			!player.isBlocked() &&
			player.isFacing(target)
			&& !target.isBlocked()) {
		player.challenge(target, amount);
	}
	break;
case 't': // trade packet
	switch (line.charAt(1)){
	case 'b': // request trade with someone
		if (!player.getIsTrading()){
			PlayerChar tradeTarget = playerList.get(line.substring(2));
			player.reqTradeWith(tradeTarget);
		}
		break;
	case 'o': // pokedollar/pokemon offer
		if (player.getIsTrading()){
			String[] compz = line.split(",");
			player.tradeOffer(Long.parseLong(compz[1]), Integer.parseInt(compz[2]));
		}
		break;
	case 'k': // accept a trade
		if (player.getIsTrading()){
			player.acceptTrade();
		}
		break;
	case 'c': // cancel trade
		if (player.getIsTrading()){
			player.endTrade(2);
		}
		break;
	}
	break;
case '!': // switch sprite
	if (player.getTalkingTo().getNPCTag().equals("sprite")) {
		if (player.getMoney() >= 1000) {
			player.setMoney(player.getMoney() - 1000);
			int spriteID = Integer.parseInt(line.substring(1));
			player.setSprite(persistor.getSpriteName(spriteID));
		
			player.getMap().removePlayer(player);
			player.getMap().addPlayer(player);
		
			player.updateClientParty();
		} else
			showDialog(player, "You need P1000 to change your sprite!");
		player.setTalking(false);
		player.setTalkingTo(null);
	}
	break;
case 'f': // release some poke
	String[] pokeInfo = line.split(";");
	int boxNum = Integer.parseInt(pokeInfo[1]);
	int pokeNum = Integer.parseInt(pokeInfo[2]);
	if (player.isBoxing()) {
		player.getBox(boxNum)[pokeNum] = null;
	}
	break;
case '~': // non-Java client login
	System.out.println("Entering DSauth stage");
	String username = line.substring(1, line.indexOf(";"));
	String unhashedPass = line.substring(line.indexOf(";") + 1);
	System.out.println("USERNAME : " + username);
	System.out.println("PASSWORD : " + unhashedPass);
	try {
		 // initialize the hasher
           Whirlpool hasher = new Whirlpool();
           hasher.NESSIEinit();

           // add the plaintext password to it
           hasher.NESSIEadd(unhashedPass);
           // create an array to hold the hashed bytes
           byte[] hashed = new byte[64];

           // run the hash
           hasher.NESSIEfinalize(hashed);

           // this stuff basically turns the byte array into a hexstring
           java.math.BigInteger bi = new java.math.BigInteger(hashed);
           String hashedStr = bi.toString(16);            // 120ff0
           if (hashedStr.length() % 2 != 0) {
                   // Pad with 0
                   hashedStr = "0"+hashedStr;
           }

		player = persistor.authUser(username, unhashedPass, session);
		player.setIoSession(session);

		session.setAttribute("player", player);
		playerList.put(player.getName(), player);
		player.setMap(mapMatrix.getMap(player.getMapX(), player
				.getMapY()));
		player.getMap().addPlayer(player);
		player.setMechanics(gameMechanics);
		player.setSpeciesData(speciesData);
		player.setPOLRdb(polrDB);
		player.updateClientParty();
		player.updateClientBag();
		player.updateClientPokedex();

	} catch (LoginException e) {
		switch (e.getType()) {
		case ACCOUNT_NOT_EXIST:
			showDialog(session, "The account you are attempting to log in on does not exist.~"
					+ "Make sure you are typing the username correctly.~"
					+ "Click Register to create a new account.");
			break;
		case WRONG_PASSWORD:
			showDialog(session, "Your password did not match.~"
					+ "Ensure that it is entered correctly.");
			break;
		case OTHER:
			showDialog(session,
			"Unknown error in login process. Contact an administrator.");
			break;
		case BANNED:
			showDialog(session, "The account you are attempting to log in on has been banned.~"
					+ "If you believe this is a mistake, please contact an administrator.");
			break;
		}
	}
	break;
case 'l':
	System.out.println("Entering auth stage");
	String[] parsed = line.split(new String(new char[] { (char) 27 }));

	try {
		player = persistor.authUser(parsed[0].substring(1, parsed[0]
		                                                          .length()), parsed[1], session);
		player.setIoSession(session);

		session.setAttribute("player", player);
		playerList.put(player.getName(), player);
		player.setMap(mapMatrix.getMap(player.getMapX(), player
				.getMapY()));
		player.getMap().addPlayer(player);
		player.setMechanics(gameMechanics);
		player.setSpeciesData(speciesData);
		player.setPOLRdb(polrDB);
		player.updateClientParty();
		player.updateClientBag();
		player.updateClientPokedex();

	} catch (LoginException e) {
		switch (e.getType()) {
		case ACCOUNT_NOT_EXIST:
			showDialog(session, "The account you are attempting to log in on does not exist.~"
					+ "Make sure you are typing the username correctly.~"
					+ "Click Register to create a new account.");
			break;
		case WRONG_PASSWORD:
			showDialog(session, "Your password did not match.~"
					+ "Ensure that it is entered correctly.");
			break;
		case OTHER:
			showDialog(session,
			"Unknown error in login process. Contact an administrator.");
			break;
		case BANNED:
			showDialog(session, "The account you are attempting to log in on has been banned.~"
					+ "If you believe this is a mistake, please contact an administrator.");
			break;
		}
	}
	break;
case 'S':
	String parse[] = line.split(new String(new char[] { (char) 27 }));
	try {
		persistor.registerUser(
				parse[0].substring(1, parse[0].length()), parse[1],
				parse[4], Integer.parseInt(parse[3]),  Integer.parseInt(parse[2]));
		session.write("r");
	} catch (RegisterException e) {
		switch (e.getType()) {
		case ALREADY_EXISTS:
			showDialog(session, "Your registration attempt failed.~"
					+ "The account name has already been taken.~"
					+ "Try to use a different name.");
			break;
		case BANNED_USERNAME:
			showDialog(session,
					"This username is banned.~");
			break;
		case INVALID_DATA:
			showDialog(session,
			"Invalid registration data. Contact an administrator.");
			break;
		case OTHER:
			showDialog(session,
			"Unknown registration error. Contact an administrator.");
		}
	}
	// out.println("CSuccess!\r");
	break;
case 'd':
	String sdparse[] = line.split(new String(new char[] { (char) 27 }));
	try {
		persistor.selfdelete(sdparse[0].substring(1, sdparse[0].length()), sdparse[1]);
		showDialog(session, "Account deletion successful!");
	} catch (DeleteException e) {
		switch (e.getType()) {
		case ACCOUNT_NOT_EXIST:
			showDialog(session, "The account you are attempting to delete does not exist.~");
			break;
		case WRONG_PASSWORD:
			showDialog(session, "Incorrect password.");
			break;
		case OTHER:
			showDialog(session, "Unknown error in deletion process. Contact an administrator.");
			break;
		}
	}
	break;
case 'U':
	player.move(Directions.up);
	break;
case 'D':
	player.move(Directions.down);
	break;
case 'L':
	player.move(Directions.left);
	break;
case 'R':
	player.move(Directions.right);
	break;
case 'T':
	String[] coords = line.split(new String(new char[] { 27 }));
	int npcX = Integer.parseInt(coords[1]);
	int npcY = Integer.parseInt(coords[2]);
	NonPlayerChar npc = player.getMap().getNPCAt(npcX, npcY);
	if (npc != null && !player.isBlocked()) {
		if (player.isFacing(npc)) {
			player.setTalking(true);
			player.setTalkingTo(npc);
			npc.speakTo(player);
		}
	}
	else {
		for(String key : playerList.keySet()) {
			PlayerChar p = playerList.get(key);
			if(p.getX() == npcX && p.getY() == npcY && !p.isBlocked() && !player.isBlocked())
				player.getIoSession().write("a" + p.getName() + "," + p.getHighestLevel());
		}
	}
	break;
case 'I':
	if(line.length() == 3 && !player.isBlocked()) {
		player.switchPokes(Integer.parseInt(line.substring(1,2)) , Integer.parseInt(line.substring(2)));
	}
	break;
case 'F':
	if(player.getTalkingTo() instanceof TrainerNonPlayerChar) {
		player.setTalking(false);
		TrainerNonPlayerChar t = (TrainerNonPlayerChar) player.getTalkingTo();
		t.setupPokemon();
		player.setTalkingTo(null);
		player.startTrainerBattle(t);
	} else if (player.getTalkingTo() != null && player.getTalkingTo().getNPCTag().equals("team")) {
		Team team = (Team)player.getTarget();
		persistor.tryUnloadTeam(team);
		player.setTalking(false);
		player.setTalkingTo(null);
	}
	else {
		player.setTalking(false);
		player.setTalkingTo(null);
	}
	break;

case 'w': //switch pokemon from box
	if (player.isBoxing()) {
		String switchInfo[] = line.split(",");
		player.switchPoke(Integer.parseInt(switchInfo[0].substring(1)),
				Integer.parseInt(switchInfo[1]), Integer.parseInt(switchInfo[2]));
		player.updateClientParty();
	}
	break;*/
}