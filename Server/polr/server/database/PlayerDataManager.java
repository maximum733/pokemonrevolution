package polr.server.database;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.mina.common.IoSession;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Strategy;
import org.simpleframework.xml.graph.CycleStrategy;

import polr.server.ClientHandler;
import polr.server.GameServer;
import polr.server.battle.Pokemon;
import polr.server.battle.PokemonSpecies;
import polr.server.map.MapMatrix;
import polr.server.mechanics.PokemonNature;
import polr.server.mechanics.moves.MoveList;
import polr.server.mechanics.moves.MoveListEntry;
import polr.server.player.Bag;
import polr.server.player.PlayerChar;
import polr.server.player.PlayerClass.ClassType;

public class PlayerDataManager implements Runnable {
	private Serializer m_serializer;
	private MapMatrix m_mapMatrix;
	private MoveList m_moveList;
	private int m_playerAmount;
	private Map<String, PlayerChar> m_playerList; 
	private ArrayList<Object[]> m_loginQueue;
	private ArrayList<PlayerChar> m_saveQueue;
	private static PlayerDataManager m_instance;

	/**
	 * Default constructor. Requires the mapMatrix and moves list to be passed in.
	 * 
	 * @param maps
	 * @param m
	 */
	public PlayerDataManager(MapMatrix maps, MoveList m) {
		m_mapMatrix = maps;
		m_moveList = m;
		
		m_playerList = ClientHandler.getPlayerList();
		m_saveQueue = new ArrayList<PlayerChar>();
		m_loginQueue = new ArrayList<Object[]>();
		
		Strategy strategy = new CycleStrategy("id", "ref");
		m_serializer = new Persister(strategy);
		
		File location = new File("accounts/");
		try {
			String loc = location.getAbsolutePath();
			location = new File(loc);
			m_playerAmount = location.list().length;
		}
		catch(Exception e) {
			m_playerAmount = 0;
		}
		System.out.println("INFO: " + m_playerAmount + " registered on this server");
		m_instance = this;
	}
	
	/**
	 * Login and saving thread. NOTE: Only create one instance of this.
	 */
	public void run() {
		String username, password;
		IoSession session;
		while(true) {
			//First try login the next player in the login queue
			if(m_loginQueue.size() > 0 && m_loginQueue.get(0) != null) {
				username = (String) m_loginQueue.get(0)[0];
				password = (String) m_loginQueue.get(0)[1];
				session = (IoSession) m_loginQueue.get(0)[2];
				this.login(username, password, session);
				m_loginQueue.remove(0);
				m_loginQueue.trimToSize();
			}
			//Next try save the next player is the save queue
			if(m_saveQueue.size() > 0 && m_saveQueue.get(0) != null) {
				if(save(m_saveQueue.get(0))) {
					//If successfully saved, remove them from the save queue
					m_saveQueue.remove(0);
					m_saveQueue.trimToSize();
				} else {
					//Else, put them at the end of the queue
					PlayerChar p = m_saveQueue.remove(0);
					m_saveQueue.add(p);
				}
			}
			try {
				Thread.sleep(200);
			} catch(Exception e){}
		}
	}

	/**
	 * Attempts to log the player into the game by adding them to the login queue.
	 * If the player is already logged in, set the player session to the new session
	 * 
	 * @param username
	 * @param password
	 * @param session
	 */
	public void attemptLogin(String username, String password, IoSession session) {
		//First, check if the account exists
		File userfile = new File("accounts/" + username + ".usr.xml");
		if(userfile.exists()) {
			//If the user is already logged in, set the player's session to the new session
			for (String name : m_playerList.keySet()) {
				if(name.equalsIgnoreCase(username)) {
					PlayerChar existingPlayer = m_playerList.get(name);
					existingPlayer.getIoSession().close();
					existingPlayer.setIoSession(session);
				}
			}
			//If the user decided to be impatient and try login again, put them at the back of the queue
			for(int i = 0; i < m_loginQueue.size(); i++) {
				IoSession s = (IoSession) m_loginQueue.get(i)[2];
				if(session.equals(s)) {
					m_loginQueue.remove(i);
				} else if(username.equalsIgnoreCase((String) m_loginQueue.get(i)[0]) && password.equalsIgnoreCase((String)m_loginQueue.get(i)[1])) {
					m_loginQueue.remove(i);
				}
			}
			m_loginQueue.add(new Object[] {username, password, session});
		} else {
			//Tell the player that the account does not exist
			session.write("l0");
			return;
		}
	}
	
	/**
	 * Logs the player into the game.
	 * 
	 * @param username
	 * @param password
	 * @param session
	 */
	private void login(String username, String password, IoSession session) {
		try {
			File userfile = new File("accounts/" + username + ".usr.xml");
			PlayerChar player = m_serializer.read(PlayerChar.class, userfile);
			if(!player.isBanned()) {
				if(player.getPasswordHash().equals(password)) {
					player.setIoSession(session);
					session.setAttribute("player", player);
					player.reinitialise();
					m_playerList.put(player.getName(), player);
					session.write("ls");
					m_mapMatrix.getMap(player.getMapX(), player.getMapY()).addPlayer(player);
				} else {
					//Tell the user the password was wrong
					session.write("l2");
				}
			} else {
				//Tell the user their account was banned
				session.write("l1");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Attempt to save a player's data by adding them to the save queue.
	 * 
	 * @param p
	 */
	public void attemptSave(PlayerChar p) {
		m_saveQueue.add(p);
	}
	
	/**
	 * Attempts to save a PlayerChar object. Returns true if successful.
	 * 
	 * @param p - Player to be saved
	 * @return
	 */
	private boolean save(PlayerChar p) {
		File userfile = new File("accounts/" + p.getName() + ".usr.xml");
		userfile.delete();
		try {
			m_serializer.write(p, userfile);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Attempts to register a new player.
	 * 
	 * @param username
	 * @param password
	 * @param email
	 * @param sprite
	 * @param starter
	 */
	public boolean register(String username, String password,
			int sprite, IoSession session) {
		File userfile = new File("accounts/" + username + ".usr.xml");
		try {
			if(username.length() < 4 || username.length() > 12) {
				return false;
			} else {
				//Now check the username contains no curse words or otherwise
				Pattern pattern = Pattern.compile("(admin|mod|gm|sysop|" +
						"fuck|shit|ass|piss|vagina|nigger|" +
						"arse|bitch|PWO|PokemonWorldOnline)"
						, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(username);
				if(matcher.find()) {
					session.write("r1");
					return false;
				} else {
					if(userfile.exists()) {
						session.write("r2");
						return false;
					} else {
						userfile.createNewFile();
						//Everything is okay, generate a new player account
						PlayerChar p = new PlayerChar();
						p.setSprite(sprite);
						p.setName(username);
						p.setPasswordHash(password);
						p.setParty(new Pokemon[6]);
						p.setX(544);
						p.setY(512);
						p.setMap(m_mapMatrix.getMap(25, -25));
						p.setBag(new Bag(true));
						p.setMoney(10);
						p.setNo(m_playerAmount);
						p.setPlayerClass(ClassType.NONE);
						p.setSurfing(false);
						p.initBoxes();
						p.initPokedex();
						//Save the user account and increment the total amount of players
						m_serializer.write(p, userfile);
						m_playerAmount++;
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.write("re");
			return false;
		}
	}
	
	/**
	 * Generates a new Pokemon, used for creating a new player's starter Pokemon.
	 * 
	 * @param speciesIndex
	 * @return
	 * @throws Exception
	 */
	private Pokemon createStarter(int speciesIndex) throws Exception {
		PokemonSpecies species = GameServer.getSpeciesData().getSpecies(speciesIndex);
		ArrayList<MoveListEntry> possibleMoves = new ArrayList<MoveListEntry>();
		MoveListEntry[] moves = new MoveListEntry[4];
		Random random = GameServer.getMechanics().getRandom();

		for (int i = 0; i < GameServer.getPOLRDB().getPokemonData(speciesIndex)
				.getStarterMoves().size(); i++) {
			possibleMoves.add(m_moveList.getMove(GameServer.getPOLRDB().getPokemonData(
					speciesIndex).getStarterMoves().get(i)));
		}
		for (int i = 1; i <= 5; i++) {
			if (GameServer.getPOLRDB().getPokemonData(speciesIndex).getMoves().containsKey(i)) {
				possibleMoves.add(m_moveList.getMove(GameServer.getPOLRDB().getPokemonData(
						speciesIndex).getMoves().get(i)));
			}
		}
		if (possibleMoves.size() <= 4) {
			for (int i = 0; i < possibleMoves.size(); i++) {
				moves[i] = possibleMoves.get(i);
			}
		} else {
			for (int i = 0; i < moves.length; i++) {
				if (possibleMoves.size() == 0)
					moves[i] = null;
				moves[i] = possibleMoves.get(random.nextInt(possibleMoves
						.size()));
				possibleMoves.remove(moves[i]);
			}
		}
		Pokemon starter = new Pokemon(
				GameServer.getMechanics(),
				species,
				PokemonNature.getNature(random.nextInt(PokemonNature
						.getNatureNames().length)),
						species.getPossibleAbilities(species.getDefaultData())[random
						     .nextInt(species.getPossibleAbilities(species.getDefaultData()).length)],
				null, (random.nextInt(100) > 87 ? Pokemon.GENDER_FEMALE
						: Pokemon.GENDER_MALE), 5, new int[] {
						random.nextInt(32), // IVs
						random.nextInt(32), random.nextInt(32),
						random.nextInt(32), random.nextInt(32),
						random.nextInt(32) }, new int[] { 0, 0, 0, 0, 0, 0 }, //EVs
				moves, new int[] { 0, 0, 0, 0 });
		starter.setExpType(GameServer.getPOLRDB().getPokemonData(speciesIndex)
				.getGrowthRate());
		starter.setBaseExp(GameServer.getPOLRDB().getPokemonData(speciesIndex).getBaseEXP());
		starter.setExp(GameServer.getMechanics().getExpForLevel(starter, 5));
		starter.setHappiness(GameServer.getPOLRDB().getPokemonData(speciesIndex).getHappiness());
		return starter;
	}

	public static PlayerDataManager getDefault() {
		return m_instance;
	}
}
