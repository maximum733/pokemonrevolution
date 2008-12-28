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

import polr.server.battle.PvPBattleField;
import polr.server.database.PersistenceManager;
import polr.server.map.MapMatrix;
import polr.server.map.ServerMap;
import polr.server.mechanics.moves.MoveList;
import polr.server.mechanics.moves.MoveSetData;
import polr.server.object.PlayerChar;
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
		
		
		mapMatrix.setMap(new ServerMap(mapReader.readMap("res/maps/0.0.tmx"), 0, 0), 0, 0); 
		
		persistor = new PersistenceManager("", mapMatrix, moveList);
		new Thread(persistor).start();

		loadIPBans();
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