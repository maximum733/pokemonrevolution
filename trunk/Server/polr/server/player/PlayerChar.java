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

package polr.server.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.mina.common.IoSession;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import polr.server.GameServer;
import polr.server.battle.NPCBattleField;
import polr.server.battle.Pokemon;
import polr.server.battle.PvPBattleField;
import polr.server.battle.WildBattleField;
import polr.server.database.POLRDatabase;
import polr.server.database.POLREvolution.EvoTypes;
import polr.server.map.ServerMap.Directions;
import polr.server.object.Char;
import polr.server.object.NonPlayerChar;
import polr.server.object.Pokedex;
import polr.server.object.PokesBox;
import polr.server.object.TrainerNonPlayerChar;
import polr.server.player.PlayerClass.ClassType;
import polr.server.trade.TradeItem;
import polr.server.trade.TradeLogic;

@Root
public class PlayerChar extends Char {
	@Element
	private String m_passwordHash;

	@Element
	private boolean m_isMod = false;
	
	@Element
	private PlayerClass m_class;
	
	@Element
	private long m_money = 0;

	@Element
	private String m_email;

	@ElementArray
	private PokesBox[] m_boxes = new PokesBox[18];

	@Element
	private Bag m_bag;
	
	@ElementList(required = false)
	private List<String> m_badges
		= new ArrayList<String>();

	@Element
	private Date m_registerTime = new Date();

	@Element
	private long m_no;

	@Element(required = false)
	private String m_teamName;
	
	@Element(required = false)
	private Pokedex m_pokedex;

	@Element(required=false)
	private int lastHealX = 128;
	
	@Element(required=false)
	private int lastHealY = 408;

	@Element(required=false)
	private int lastHealMapX = 0;
	
	@Element(required=false)
	private int lastHealMapY = 0;
	
	@Element(required = false)
	private boolean m_banned = false;

	@Element(required = false)
	private boolean m_muted = false;
	
	@Element(required=false)
	private long m_muteExpiration = 0;
	
	@Element(required=false)
	private long m_freezeExpiration = 0;
	
	@Element(required=false)
	private long m_disableExpiration = 0;

	@Element(required = false)
	private boolean m_frozen = false;
	
	@ElementList(required = false)
	private List<String> m_npcNames = new ArrayList<String>();
	
	@Element(required = false)
	private long m_lastLogin;
	
	@Element(required=false)
	private boolean m_silentMuted = false;
	
	@Element(required=false)
	private boolean m_POK = false;
	
	private boolean m_bagInitialised = false;

	private int tradeID = -1;
	private PlayerChar tradingWith;
	private TradeLogic tLogic;
	
	private List<PlayerChar> m_tradeReq
		= new ArrayList<PlayerChar>();
	private boolean isTrading = false;
	
	private NonPlayerChar m_talkingTo;
	private boolean m_talking = false;

	@SuppressWarnings("unused")
	private TradeItem m_trades;
	private List<PlayerChar> m_challenges;

	private IoSession m_session;

	private Object m_target;
	
	public PlayerClass getPlayerClass() {
		return m_class;
	}
	
	public String getPasswordHash() {
		return m_passwordHash;
	}
	
	@Override
	public void setSurfing(boolean b) {
		if(!b)
			this.getIoSession().write("CS" + this.getNo() + "," + this.getSprite());
		else
			this.getIoSession().write("CS" + this.getNo() + ",swim");
		super.setSurfing(b);
	}

	public Pokemon[] getBox(int boxNum) {
		return m_boxes[boxNum].getPokes();
	}

	public void initBoxes() {
		for (int i = 0; i < m_boxes.length; i++) {
			m_boxes[i] = new PokesBox();
		}
	}

	public void switchPoke(int boxNum, int boxIndex, int teamIndex) {
		if (!(teamIndex == 0 && getParty()[1] == null && getBox(boxNum)[boxIndex] == null)) {
			Pokemon tempPoke;
			tempPoke = this.getParty()[teamIndex];
			this.getParty()[teamIndex] = m_boxes[boxNum].getPokes()[boxIndex];
			m_boxes[boxNum].setPoke(boxIndex, tempPoke);
			if (tempPoke != null) {
				tempPoke.calculateStats(true);
				tempPoke.reinitialise(GameServer.getMechanics());
			}
			tempPoke = null;
			arrangeParty();
		}
	}
	
	public void addBadge(String badge) {
		if(!m_badges.contains(badge)) {
			m_badges.add(badge);
		}
	}
	
	public boolean hasBadge(String badge) {
		return m_badges.contains(badge);
	}
	
	public int getBadgeCount() {
		return m_badges.size();
	}

	public String getEmail() {
		return m_email;
	}

	public void setEmail(String email) {
		m_email = email;
	}

	public boolean isTalking() {
		return m_talking;
	}

	public String getTeamName() {
		return m_teamName;
	}
	
	public void setTradeLogic(TradeLogic tL){
		tLogic = tL;
	}
	
	public void setTradeID(int tID){
		tradeID = tID;
	}
	
	public int getTradeID() {
		return tradeID;
	}
	
	public void setIsTrading(boolean tradeStatus){
		isTrading = tradeStatus;
	}
	
	public boolean getIsTrading(){
		return isTrading;
	}
	
	public void setTradeWith(PlayerChar tradeWith){
		tradingWith = tradeWith;
	}
	
	public void endTrade(int endCode){
		try { if (tLogic.canCancel()){
				tLogic.endTrade(endCode);
				tradingWith.setTradeID(-1);
				tradingWith.setTradeWith(null);
				tradingWith.setTradeLogic(null);
				tradingWith.setIsTrading(false);
				tLogic = null;
				tradeID = -1;
				isTrading = false;
				if(tradingWith != null && m_tradeReq.contains(tradingWith)) {
					m_tradeReq.remove(tradingWith);
				}
				tradingWith = null;
			}}		catch ( Exception e) { 			e.printStackTrace();		}
		
	}
	
	public void forceEndTrade() {
			try {
				tradingWith.setTradeID(-1);
				tradingWith.setTradeWith(null);
				tradingWith.setTradeLogic(null);
				tradingWith.setIsTrading(false);
				tLogic = null;
				tradeID = -1;
				isTrading = false;
				if(tradingWith != null && m_tradeReq.contains(tradingWith)) {
					m_tradeReq.remove(tradingWith);
				}
				tradingWith = null;
				this.getIoSession().write("tf");
			}
			catch ( Exception e) { 
				e.printStackTrace();
			}
			
	}
	
	public void acceptTrade(){
		tLogic.acceptTrade(tradeID);
	}
	
	public void tradeOffer(long pokedollars, int p){
		if (tLogic.canOffer()){
			tLogic.submitOffer(p, pokedollars, tradeID);
		}
	}
	
	// called by trade requestee
	public void startTradeWith(PlayerChar tradingWith) {
		clearTradeRequestsForTrade(tradingWith);
		this.getIoSession().write("ts");
		tradingWith.getIoSession().write("ts");
		tLogic = new TradeLogic(new PlayerChar[] {this, tradingWith});
		this.setTradeWith(tradingWith);
		this.setTradeID(0);
		this.isTrading = true;
		tradingWith.setTradeLogic(tLogic);
		tradingWith.setTradeWith(this);
		tradingWith.setTradeID(1);
		tradingWith.isTrading = true;
		
		tLogic.beginTrade();
	}
	
	public void reqTradeWith(PlayerChar target) {
		if(this.getIoSession().getRemoteAddress() != target.getIoSession().getRemoteAddress()){
			if (m_tradeReq.contains(target)) {
				startTradeWith(target);
			}
			else {
				if (target != null)
					target.reqTradeWithBy(this);
			}
		}
	}
	
	public void reqTradeWithBy(PlayerChar requester) {
		if (m_tradeReq.contains(requester)) {
			this.removeTradeRequest(requester);
		}
		m_tradeReq.add(requester);
		getIoSession().write("ta" + requester.getName());
	}
	
	public void removeTradeRequest(PlayerChar p) {
		if(p != null) {
			m_tradeReq.remove(p);
			getIoSession().write("tr" + p.getName());
		}
	}
	
	public TradeLogic getTradeLogic() {
		return tLogic;
	}
	
	// the player this is being called on is the challenger,
	// the argument is the challenged
	public void startPvPBattleWith(PlayerChar challenged, long amount) {
		clearChallengesForPvP(challenged);
		challenged.clearChallengesForPvP(this);
		this.getIoSession().write("bi" + challenged.getName() + ",p");
		challenged.getIoSession().write("bi" + this.getName() + ",p");
		StringBuilder enemyData = new StringBuilder("h");
		for (Pokemon p : challenged.getParty()) {
			if(p != null) {
			enemyData.append(","
					+ p.getName()
					+ ";"
					+ GameServer.getSpeciesData().getPokemonByName(p.getSpeciesName())
					+ ";"
					+ p.getTypes()[0].toString()
					+ ";"
					+ ((p.getTypes().length > 1) ? p.getTypes()[1].toString()
							: "") + ";" + p.getGender() + ";"
					+ +p.getRawStat(Pokemon.S_HP) + ";"
					+ p.getRawStat(Pokemon.S_HP) + ";" + p.getLevel());
			}
			else
				enemyData.append(",");
		}
		this.getIoSession().write(enemyData);

		enemyData = new StringBuilder("h");
		for (Pokemon p : this.getParty()) {
			if(p != null) {
			enemyData.append(","
					+ p.getName()
					+ ";"
					+ GameServer.getSpeciesData().getPokemonByName(p.getSpeciesName())
					+ ";"
					+ p.getTypes()[0].toString()
					+ ";"
					+ ((p.getTypes().length > 1) ? p.getTypes()[1].toString()
							: "") + ";" + p.getGender() + ";"
					+ +p.getRawStat(Pokemon.S_HP) + ";"
					+ p.getRawStat(Pokemon.S_HP) + ";" + p.getLevel());
			}
			else
				enemyData.append(",");
		}
		challenged.getIoSession().write(enemyData);

		PvPBattleField pvpField = new PvPBattleField(GameServer.getMechanics(),
				new PlayerChar[] { this, challenged }, amount);
		this.setField(pvpField);
		this.setOpponent(challenged);
		this.setBattleID(0);
		challenged.setField(pvpField);
		challenged.setOpponent(this);
		challenged.setBattleID(1);
	}

	public void setTalking(boolean talking) {
		m_talking = talking;
	}

	public void setPasswordHash(String hash) {
		m_passwordHash = hash;
	}

	public IoSession getIoSession() {
		return m_session;
	}

	public void setIoSession(IoSession session) {
		m_session = session;
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	public boolean isBlocked() {
		return (isBattling()) || (isTalking()) || (isFrozen());
	}

	public void healParty() {
		for (Pokemon pokemon : getParty()) {
			if (pokemon != null) {
				pokemon.calculateStats(true);
				pokemon.reinitialise(GameServer.getMechanics());
			}
		}
	}

	public void challenge(PlayerChar target, long amount) {
		if(m_challenges.contains(target)) {
			startPvPBattleWith(target, amount);
		}
		else {
			if(amount > m_money) {
				getIoSession().write("mYou cannot wager more money than you have");
				getIoSession().write("fr" + target.getName());
			}
			else {
				target.challengedBy(this, amount);
				System.out.println(target.getName() + "was added to " + this.getName() + "'s Challenge List");
			}
		}
	}

	public void challengedBy(PlayerChar challenger, long amount) {
			if(amount > m_money) {
				challenger.removeChallenge(this);
			}
			else {
				if (m_challenges.contains(challenger)){
					this.removeChallenge(challenger);
				}
				m_challenges.add(challenger);
				getIoSession().write("fa" + challenger.getName() + "," + challenger.getHighestLevel() + "," + amount );	
			}
	}
	
	// Regenerates player data after logging in.
	public void reinitialise() {
		arrangeParty();
		boolean newPokedex = false;
		if (hasPokedex() == null) {
			newPokedex = true;
			initPokedex();
		}
		if (m_badges == null)
			m_badges = new ArrayList<String>();
		if(m_class == null)
			m_class = new PlayerClass(ClassType.NONE);
		for (Pokemon p : getParty()) {
			if (p != null) {
				p.reinitialise(GameServer.getMechanics());
				if (newPokedex) {
					setSeen(GameServer.getSpeciesData().getPokemonByName(
							p.getSpeciesName()));
					setCaught(GameServer.getSpeciesData().getPokemonByName(
							p.getSpeciesName()));
				}
			}
		}
		for (int i = 0; i < getBoxCount(); i++) {
			for (Pokemon p : getBox(i)) {
				if (p != null) {
					p.reinitialise(GameServer.getMechanics());
					if (newPokedex) {
						setSeen(GameServer.getSpeciesData().getPokemonByName(
								p.getSpeciesName()));
						setCaught(GameServer.getSpeciesData().getPokemonByName(
								p.getSpeciesName()));
					}
				}
			}
		}
		if(System.currentTimeMillis() - m_lastLogin >= 21600000) {
			//Wipe the NPC list
			m_npcNames.clear();
		}
		//Set the last login to the current time
		m_lastLogin = System.currentTimeMillis();
		m_challenges = Collections
				.synchronizedList(new ArrayList<PlayerChar>());
	}

	public void lostBattle() {
		healParty();
		updateClientParty();
		gameOverTeleport();
	}

	public void gameOverTeleport() {
		getMap().removePlayer(this);
		this.setX(lastHealX);
		this.setY(lastHealY);
		getMap().getMapMatrix().getMap(lastHealMapX, lastHealMapY).addPlayer(this);
		setMap(getMap().getMapMatrix().getMap(lastHealMapX, lastHealMapY));
		getIoSession().write(
				"C" + getName() + " ran to the Pokemon Center with"
						+ " the injured Pokemon and nursed them to health.");
	}
	
	public void setLastHeal(int MapX, int MapY, int X, int Y) {
		lastHealX = X;
		lastHealY = Y;
		lastHealMapX = MapX;
		lastHealMapY = MapY;
	}
	
	public void switchOutHealthyPoke() {
			for(int i = 1; i < getParty().length && getParty()[i] != null; i++) {
				if(getParty()[i].getHealth() > 0) {
					Pokemon temp = getParty()[0];
					getParty()[0] = getParty()[i];
					getParty()[i] = temp;
					arrangeParty();
					updateClientParty();
					break;
				}
			}
	}

	public void startWildBattle() {
		clearChallenges();
		if(getParty()[0].getHealth() <= 0) {
			switchOutHealthyPoke();
		}
		try {
			Pokemon wildPokemon = getMap().generateWildPokemon(GameServer.getMechanics(),
					GameServer.getPOLRDB(), GameServer.getSpeciesData());
			this.getIoSession().write("bi" + wildPokemon.getName() + ",w,0");
			/*this.getIoSession().write(
					"w"
							+ wildPokemon.getName()
							+ ";"
							+ GameServer.getSpeciesData().getPokemonByName(
									wildPokemon.getSpeciesName())
							+ ";"
							+ wildPokemon.getTypes()[0].toString()
							+ ";"
							+ ((wildPokemon.getTypes().length > 1) ? wildPokemon
									.getTypes()[1].toString() : "") + ";"
							+ wildPokemon.getGender() + ";"
							+ +wildPokemon.getRawStat(Pokemon.S_HP) + ";"
							+ wildPokemon.getRawStat(Pokemon.S_HP) + ";"
							+ wildPokemon.getLevel());*/
			this.getIoSession().write(
					"w" + ";"
					+ wildPokemon.getName()	+ ";"
					+ wildPokemon.getLevel() + ";"
					+ wildPokemon.getGender() + ";"
					+ wildPokemon.getRawStat(Pokemon.S_HP) + ";"
					+ wildPokemon.getSpeciesName());
			m_pokedex.setSeen(GameServer.getSpeciesData().getPokemonByName(
					wildPokemon.getSpeciesName()));
			this.setBattleID(0);
			this.setField(new WildBattleField(GameServer.getMechanics(), wildPokemon, this));
		}
		catch (Exception e) {
			e.printStackTrace();
			endBattle();
		}
	}
	public void startSurfBattle() {
		clearChallenges();
		if(getParty()[0].getHealth() <= 0) {
			switchOutHealthyPoke();
		}
		try {
			Pokemon surfPokemon = getMap().generateSurfPokemon(GameServer.getMechanics(),
					GameServer.getPOLRDB(), GameServer.getSpeciesData());
			System.out.println("bi" + surfPokemon.getName() + ",w,0");
			this.getIoSession().write("bi" + surfPokemon.getName() + ",w,0");
			/*this.getIoSession().write(
					"w"
							+ wildPokemon.getName()
							+ ";"
							+ GameServer.getSpeciesData().getPokemonByName(
									wildPokemon.getSpeciesName())
							+ ";"
							+ wildPokemon.getTypes()[0].toString()
							+ ";"
							+ ((wildPokemon.getTypes().length > 1) ? wildPokemon
									.getTypes()[1].toString() : "") + ";"
							+ wildPokemon.getGender() + ";"
							+ +wildPokemon.getRawStat(Pokemon.S_HP) + ";"
							+ wildPokemon.getRawStat(Pokemon.S_HP) + ";"
							+ wildPokemon.getLevel());*/
			this.getIoSession().write(
					"w" + ";"
					+ surfPokemon.getName()	+ ";"
					+ surfPokemon.getLevel() + ";"
					+ surfPokemon.getGender() + ";"
					+ surfPokemon.getRawStat(Pokemon.S_HP) + ";"
					+ surfPokemon.getSpeciesName());
			m_pokedex.setSeen(GameServer.getSpeciesData().getPokemonByName(
					surfPokemon.getSpeciesName()));
			this.setBattleID(0);
			this.setField(new WildBattleField(GameServer.getMechanics(), surfPokemon, this));
		}
		catch (Exception e) {
			e.printStackTrace();
			endBattle();
		}
	}
	
	public void catchPokemon(Pokemon caught) throws Exception {
		addPokemon(caught);
		caught.setOriginalTrainer(getName());
		caught.setOriginalNo(m_no);
		m_pokedex.setCaught(GameServer.getSpeciesData().getPokemonByName(
				caught.getSpeciesName()));
		arrangeParty();
		throw new Exception();
	}
	
	public void addTradePokemon(Pokemon newPoke) {
		m_pokedex.setSeen(GameServer.getSpeciesData().getPokemonByName(
				newPoke.getSpeciesName()));
		m_pokedex.setCaught(GameServer.getSpeciesData().getPokemonByName(
				newPoke.getSpeciesName()));
		// Check for Evolution via trading, or via trading with a held item
		POLRDatabase polrDB = GameServer.getPOLRDB();
		for (int i = 0; i < polrDB.getPokemonData(newPoke.getId()).getEvolutions().size(); i++){
		if(polrDB.getPokemonData(newPoke.getId()).getEvolutions().get(i).getType() 
				== EvoTypes.Trade){
			StringBuilder evoPacket = new StringBuilder("e,"
					+ newPoke.getName());
			newPoke = newPoke.evolve(newPoke, 
					GameServer.getSpeciesData()
					.getSpecies(GameServer.getSpeciesData()
							.getPokemonByName(polrDB.getPokemonData(newPoke.getId()).getEvolutions().get(i)
									.getEvolveTo())));
			evoPacket.append(",");
			evoPacket.append(GameServer.getSpeciesData()
					.getSpecies(GameServer.getSpeciesData()
							.getPokemonByName(polrDB.getPokemonData(newPoke.getId()).getEvolutions()
									.get(i).getEvolveTo())).getName());
			System.out.println(newPoke.getName());
			System.out.println(evoPacket);
			this.getIoSession().write(
					evoPacket.toString());
		}
		}
		m_pokedex.setSeen(GameServer.getSpeciesData().getPokemonByName(
				newPoke.getSpeciesName()));
		m_pokedex.setCaught(GameServer.getSpeciesData().getPokemonByName(
				newPoke.getSpeciesName()));
		for (int i = 0; i < getParty().length; i++) {
			if (getParty()[i] == null) {
				getParty()[i] = newPoke;
				return;
			}
		}
		for (int i = 0; i < m_boxes.length; i++)
			for (int x = 0; x < m_boxes[i].getPokes().length; x++) {
				if (m_boxes[i].getPokes()[x] == null) {
					m_boxes[i].setPoke(x, newPoke);
					return;
				}
			}
	}
	
	public void addPokemon(Pokemon newPoke) {
		for (int i = 0; i < getParty().length; i++) {
			if (getParty()[i] == null) {
				getParty()[i] = newPoke;
				return;
			}
		}
		for (int i = 0; i < m_boxes.length; i++)
			for (int x = 0; x < m_boxes[i].getPokes().length; x++) {
				if (m_boxes[i].getPokes()[x] == null) {
					m_boxes[i].setPoke(x, newPoke);
					return;
				}
			}
	}

	public void getBoxString(int boxNum) {
		StringBuilder box = new StringBuilder("S,");
		for (int i = 0; i < 30; i++) {
			try {
				box.append(GameServer.getSpeciesData().getPokemonByName(
						m_boxes[boxNum].getPokes()[i].getSpeciesName()));
				if (i != 29)
					box.append(",");
			} catch (NullPointerException e) {
				box.append(" ,");
			}
		}
		System.out.println(box);
		getIoSession().write(box);
	}

	public int getBoxCount() {
		return m_boxes.length;
	}
	
	public void move(Directions dir) {
		if (!isBlocked()) {
			if(facing != dir) {
				switch (dir) {
				case up:
					facing = Directions.up;
					getMap().sendToAll("CU" + m_no);
					break;
				case down:
					facing = Directions.down;
					getMap().sendToAll("CD" + m_no);
					break;
				case left:
					facing = Directions.left;
					getMap().sendToAll("CL" + m_no);
					break;
				case right:
					facing = Directions.right;
					getMap().sendToAll("CR" + m_no);
				}
			} else if (getMap().canMove(dir, this)) {
				switch (dir) {
				case up:
					y -= 32;
					facing = Directions.up;
					getMap().sendToAll("U" + m_no);
					break;
				case down:
					y += 32;
					facing = Directions.down;
					getMap().sendToAll("D" + m_no);
					break;
				case left:
					x -= 32;
					facing = Directions.left;
					getMap().sendToAll("L" + m_no);
					break;
				case right:
					x += 32;
					facing = Directions.right;
					getMap().sendToAll("R" + m_no);
				}
				if (getMap().isWildEncounter(x, y + 8))
					startWildBattle();
				if (getMap().isSurfEncounter(x, y + 8))
					startSurfBattle();
				if(m_challenges.size() > 0)
					clearChallenges();
				if(m_tradeReq.size() > 0)
					clearTradeRequests();
			}
		}
	}
	
	public void clearTradeRequests() {
		getIoSession().write("tw");
		try {
		for(int i = 0; i < m_tradeReq.size(); i++) {
			m_tradeReq.get(i).removeTradeRequest(this);
			m_tradeReq.remove(m_challenges.get(i));
		}
		} catch (Exception e) { }
		m_tradeReq.clear();
		
	}
	
	public void clearTradeRequestsForTrade(PlayerChar target) {
		getIoSession().write("tw");
		for(int i = 0; i < m_tradeReq.size(); i++) {
			if(m_tradeReq.get(i) == target){
			m_tradeReq.get(i).removeTradeRequest(this);
			}
		}
		m_tradeReq.clear();
	}
	
	public void clearChallenges() {
		getIoSession().write("fc");
		for(int i = 0; i < m_challenges.size(); i++) {
			m_challenges.get(i).removeChallenge(this);
			m_challenges.remove(m_challenges.get(i));
		}
		m_challenges.clear();
	}
	
	public void clearChallengesForPvP(PlayerChar target) {
		getIoSession().write("fc");
		for(PlayerChar p: m_challenges) {
			if(p != target) {
				p.removeChallenge(this);
			}
		}
		m_challenges.clear();
	}
	
	public void removeChallenge(PlayerChar p) {
		if(p != null) {
			m_challenges.remove(p);
			getIoSession().write("fr" + p.getName());
		}
	}

	public void switchPokes(int a, int b) throws Exception {
		if (a >= 0 && b <= 5 && getParty()[a] != null && getParty()[b] != null
				&& !isBlocked()) {
			Pokemon temp = getParty()[a];
			getParty()[a] = getParty()[b];
			getParty()[b] = temp;
			arrangeParty();
			updateClientParty();
		}
	}

	public long getMoney() {
		return m_money;
	}

	public void setMoney(long m_money) {
		this.m_money = m_money;
	}
	
	public boolean validateTransaction(long amount) {
		long finalAmount;
		finalAmount = this.getMoney() - amount;
		if (finalAmount < 0){
			return false;
		}
		else {
			return true;
		}
	}
	
	public boolean isMod() {
		return m_isMod;
	}

	public void setMod(boolean setMod) {
		m_isMod = setMod;
	}
	
	public void initialiseClientBag() {
		if(!m_bagInitialised) {
			String packet = "Bi";
			for(int i = 0; i < m_bag.getContents().size();i++) {
				packet = packet + m_bag.getContents().get(i).getIdValue() + m_bag.getContents().get(i).getQuantityValue();
			}
			this.getIoSession().write(packet);
			m_bagInitialised = true;
		}
	}

	@Override
	public void endBattle() {
		super.endBattle();
	}

	public void arrangeParty() {
		ArrayList<Pokemon> tempArrange = new ArrayList<Pokemon>();
		for (Pokemon p : getParty()) {
			if (p != null)
				tempArrange.add(p);
		}
		setParty(new Pokemon[6]);
		for (int i = 0; i < tempArrange.size(); i++) {
			getParty()[i] = tempArrange.get(i);
		}
	}

	public void updateClientParty() {
		int i = 0;
		for (Pokemon p : getParty()) {
			if (p != null)
				this.getIoSession().write("P"
						+ p.getName()
						+ ","
						+ GameServer.getSpeciesData().getPokemonByName(p.getSpeciesName())
						+ "," + i + "," + p.getHealth() + "," + p.getRawStat(Pokemon.S_HP) + ","
						+ p.getRawStat(Pokemon.S_ATTACK) + "," + p.getRawStat(Pokemon.S_DEFENCE) + ","
						+ p.getRawStat(Pokemon.S_SPEED) + "," + p.getRawStat(Pokemon.S_SPATTACK) + ","
						+ p.getRawStat(Pokemon.S_SPDEFENCE) + "," + p.getTypes()[0].toString() + ","
						+ ((p.getTypes().length > 1) ? p.getTypes()[1].toString() : "") + ","
						+ p.getLevel() + "," + p.getExp() + ","
						+ p.getNature().getName() +"," + p.getGender() + ","
						+ p.getMoveName(0) + "," + p.getMaxPp(0) + ","
						+ p.getMoveName(1) + "," + p.getMaxPp(1) + ","
						+ p.getMoveName(2) + "," + p.getMaxPp(2) + ","
						+ p.getMoveName(3) + "," + p.getMaxPp(3) + ","
						+ p.getAbility().getName());
			i++;
		}
	}
	
	/* Allows to update specfic Pokemon without updating the rest */
	public void updateClientPartyByIndex(int i) {
		if(getParty()[i] != null) {
			Pokemon p = getParty()[i];
			this.getIoSession().write("P"
					+ p.getName()
					+ ","
					+ GameServer.getSpeciesData().getPokemonByName(p.getSpeciesName())
					+ "," + i + "," + p.getHealth() + "," + p.getRawStat(Pokemon.S_HP) + ","
					+ p.getRawStat(Pokemon.S_ATTACK) + "," + p.getRawStat(Pokemon.S_DEFENCE) + ","
					+ p.getRawStat(Pokemon.S_SPEED) + "," + p.getRawStat(Pokemon.S_SPATTACK) + ","
					+ p.getRawStat(Pokemon.S_SPDEFENCE) + "," + p.getTypes()[0].toString() + ","
					+ ((p.getTypes().length > 1) ? p.getTypes()[1].toString() : "") + ","
					+ p.getLevel() + "," + p.getExp() + ","
					+ p.getNature().getName() +"," + p.getGender() + ","
					+ p.getMoveName(0) + "," + p.getMaxPp(0) + ","
					+ p.getMoveName(1) + "," + p.getMaxPp(1) + ","
					+ p.getMoveName(2) + "," + p.getMaxPp(2) + ","
					+ p.getMoveName(3) + "," + p.getMaxPp(3) + ","
					+ p.getAbility().getName());
		}
	}

	public void setBag(Bag b) {
		m_bag = b;
	}

	public Bag getBag() {
		return m_bag;
	}

	public void setTalkingTo(NonPlayerChar talkingTo) {
		m_talkingTo = talkingTo;
	}

	public NonPlayerChar getTalkingTo() {
		return m_talkingTo;
	}
	
	public TrainerNonPlayerChar getTrainerTalkingTo() {
		return (TrainerNonPlayerChar) m_talkingTo;
	}

	public void setRegisterTime(Date m_registerTime) {
		this.m_registerTime = m_registerTime;
	}

	public Date getRegisterTime() {
		return m_registerTime;
	}

	public void setNo(long m_no) {
		this.m_no = m_no;
	}

	public long getNo() {
		return m_no;
	}

	public void initPokedex() {
		m_pokedex = new Pokedex();
	}

	public Pokedex hasPokedex() {
		return m_pokedex;
	}

	public void updateClientPokedex() {
		StringBuilder pokedex = new StringBuilder("aseen,");
		List<Integer> seen = m_pokedex.seenList();
		Iterator<Integer> pokedexIter = seen.iterator();
		while (pokedexIter.hasNext()) {
			pokedex.append(pokedexIter.next() + ",");
		}
		pokedex.append("caught");
		List<Integer> caught = m_pokedex.caughtList();
		pokedexIter = caught.iterator();
		while (pokedexIter.hasNext()) {
			pokedex.append("," + pokedexIter.next());
		}
		//getIoSession().write(pokedex);
	}

	public boolean isSeen(int pokeId) {
		return m_pokedex.isSeen(pokeId);
	}

	public void setSeen(int pokeId) {
		m_pokedex.setSeen(pokeId);
	}

	public boolean isCaught(int pokeId) {
		return m_pokedex.isCaught(pokeId);
	}

	public void setCaught(int pokeId) {
		m_pokedex.setCaught(pokeId);
	}

	public void setBanned(boolean m_banned) {
		this.m_banned = m_banned;
	}

	public boolean isBanned() {
		return m_banned;
	}

	public void setMuted(boolean m_muted) {
		this.m_muted = m_muted;
	}

	public boolean isMuted() {
		return m_muted;
	}
	
	public long getMuteExpiration() {
		return m_muteExpiration;
	}
	
	public void setMuteExpiration(long time) {
		this.m_muteExpiration = time;
	}
	
	public void setSilentMuted(boolean m_silentMuted) {
		this.m_silentMuted = m_silentMuted;
	}
	
	public boolean isSilentMuted() {
		return m_silentMuted;
	}
	
	public void setPOK(boolean m_POK) {
		this.m_POK = m_POK;
	}
	
	public boolean isPOK() {
		return m_POK;
	}

	public void setFrozen(boolean m_frozen) {
		this.m_frozen = m_frozen;
	}

	public boolean isFrozen() {
		return m_frozen;
	}
	
	public long getFreezeExpiration() {
		return m_freezeExpiration;
	}

	public void setFreezeExpiration(long freezeExpiration) {
		this.m_freezeExpiration = freezeExpiration;
	}
	
	public long getDisableExpiration() {
		return m_disableExpiration;
	}
	
	public void setDisableExpiration(long disableExpiration) {
		this.m_disableExpiration = disableExpiration;
	}
	
	public void startTrainerBattle(TrainerNonPlayerChar opponent) {
		clearChallenges();
		this.getIoSession().write("bi" + opponent.getName() + ",n,0");
		if(!m_npcNames.contains(opponent.getName())) {
			arrangeParty();
			m_npcNames.add(opponent.getName());
			if(getParty()[0].getHealth() <= 0) {
				switchOutHealthyPoke();
			}
			Pokemon [] newParty = new Pokemon[6];
			if(opponent.getHighestLevel() < this.getHighestLevel()) {
				for(int i = 0; i < 6; i++) {
					if(opponent.getParty()[i] != null && opponent.getParty()[i].getLevel() < getHighestLevel()) {
						newParty[i] = null;
						newParty[i] = new Pokemon(generatePokemon(GameServer.getSpeciesData().getPokemonByName(opponent.getParty()[i].getSpeciesName()), this.getHighestLevel()));
					} else
						newParty[i] = null;
				}
			}
			else {
				for(int i = 0; i < 6; i++) {
					if(opponent.getParty()[i] != null)
						newParty[i] = new Pokemon(opponent.getParty()[i]);
					else
						newParty[i] = null;
				}
			}
			StringBuilder enemyData = new StringBuilder("h");
			for (Pokemon p : newParty) {
				if(p != null) {
				enemyData.append(","
						+ p.getName()
						+ ";"
						+ GameServer.getSpeciesData().getPokemonByName(p.getSpeciesName())
						+ ";"
						+ p.getTypes()[0].toString()
						+ ";"
						+ ((p.getTypes().length > 1) ? p.getTypes()[1].toString()
								: "") + ";" + p.getGender() + ";"
						+ +p.getRawStat(Pokemon.S_HP) + ";"
						+ p.getRawStat(Pokemon.S_HP) + ";" + p.getLevel());
				}
				else
					enemyData.append(",");
			}
			this.getIoSession().write(enemyData);
			this.setBattleID(0);
			this.setField(new NPCBattleField(GameServer.getMechanics(), newParty, opponent.getName(), this, opponent));
		}
	}

	public void setTarget(Object m_target) {
		this.m_target = m_target;
	}

	public Object getTarget() {
		return m_target;
	}
	
	@SuppressWarnings("unchecked")
	public List getNpcList() {
		return m_npcNames;
	}

	public void setTeamName(String m_teamName) {
		this.m_teamName = m_teamName;
	}
}
