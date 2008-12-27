package polr.server.database;

import java.io.File;
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
import polr.server.exception.DeleteException;
import polr.server.exception.LoginException;
import polr.server.exception.RegisterException;
import polr.server.map.MapMatrix;
import polr.server.map.ServerMap.Directions;
import polr.server.mechanics.PokemonNature;
import polr.server.mechanics.moves.MoveList;
import polr.server.mechanics.moves.MoveListEntry;
import polr.server.object.Bag;
import polr.server.object.PlayerChar;
/*
 * Handles login, logout, saving, deletion, and registration
 */

public class PersistenceManager implements Runnable {
	private String m_path;

	private MoveList moveList;
	private MapMatrix m_maps;
	private long playerAmount;

	private Map<String, PlayerChar> playerList; 
	private ArrayList<PlayerChar> saveQueue;
	private ArrayList<PlayerChar> loginQueue;
	
	private Serializer serializer;
	private ArrayList<String []>loginDelay;
	
	private static PersistenceManager defaultInstance;
	
	
	public void run() {
		while(true) {
			//120 SECOND LOGIN DELAY AFTER LOGOUT
			for(int i = 0; i < loginDelay.size(); i++) {
				if(System.currentTimeMillis() - Long.parseLong(loginDelay.get(i)[1]) > 6) {
					loginDelay.remove(i);
				}
			}
			loginDelay.trimToSize();
			
			//NEXT PERSON IN SAVE QUEUE
			if(saveQueue != null && saveQueue.size() > 0) {
				PlayerChar player = saveQueue.get(0);
				File userfile = new File(m_path + player.getName() + ".usr.xml");
				userfile.delete();
				try {
					serializer.write(player, userfile);
					loginDelay.add(new String[] {player.getName(), String.valueOf(System.currentTimeMillis())});
					saveQueue.remove(0);
					saveQueue.trimToSize();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//NEXT PERSON IN LOGIN QUEUE
			//Login time changes based on amount of people online and amount of people trying to login
			//1 to 20 Players, login immediately
			//20 to 50 Players, login every 2 seconds
			//50 to 100 Players, login every 5 seconds
			//100 to 120 Players, login every 10 seconds
			if(loginQueue.size() > 0) {
				if(playerList.size() > 0 && playerList.size() <= 20) {
					//Low amount of players, allow login every second
					for(int i = 0; i < loginQueue.size(); i++) {
						PlayerChar p = loginQueue.get(i);
						p.getIoSession().write("ls");
						p.getMap().addPlayer(p);
					}
					loginQueue.clear();
					loginQueue.trimToSize();
				} else if(playerList.size() > 20 && playerList.size() <= 50) {
					for(int i = 0; i < loginQueue.size(); i++) {
						try {
							PlayerChar p = loginQueue.get(i);
							p.getIoSession().write("ls");
							p.getMap().addPlayer(p);
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					loginQueue.clear();
					loginQueue.trimToSize();
				} else if(playerList.size() > 50 && playerList.size() <= 100) {
					for(int i = 0; i < loginQueue.size(); i++) {
						try {
							PlayerChar p = loginQueue.get(i);
							p.getIoSession().write("ls");
							p.getMap().addPlayer(p);
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					loginQueue.clear();
					loginQueue.trimToSize();
				} else {
					for(int i = 0; i < loginQueue.size(); i++) {
						try {
							loginQueue.get(i).getIoSession().write("ls");
							loginQueue.get(i).getMap().addPlayer(loginQueue.get(i));
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					loginQueue.clear();
					loginQueue.trimToSize();
				}
			}
		}
	}
	
	public PersistenceManager(String path, MapMatrix maps, MoveList m) {
		loginDelay = new ArrayList<String[]>();
		saveQueue = new ArrayList<PlayerChar>();
		loginQueue = new ArrayList<PlayerChar>();
		moveList = m;
		m_path = path + "accounts/";
		m_maps = maps;
		playerList = ClientHandler.getPlayerList();
		moveList = m;
		
		Strategy strategy = new CycleStrategy("id", "ref");
		serializer = new Persister(strategy);
		
		defaultInstance = this;
		
		File location = new File(m_path);
		try {
			String loc = location.getAbsolutePath();
			location = new File(loc);
			playerAmount = location.list().length;
		}
		catch(Exception e) {
			playerAmount = 0;
		}
	}

	public static PersistenceManager getDefault() {
		return defaultInstance;
	}
	
	public void registerUser(String username, String encpass, String email,
			int sprite, int starter) throws RegisterException {
		try {
			// Throws a RegisterException if the server is in lockdown mode
			if (GameServer.getLockdown() > 0) throw new RegisterException(RegisterException.Types.LOCKDOWN);
			// Throws a RegisterException if the player is using a word with an odd symbol, or is too short or too long.
			Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{1,20}$");
			Matcher matcher = pattern.matcher(username);
			//if (username.contains(" ") || username.contains("|") || username.contains(";") || username.contains(","))
			if (!matcher.find())
				throw new RegisterException(
						RegisterException.Types.INVALID_DATA);
			// Throws a RegisterException if the name in question has been banned.
			// The other RegExp should remove the need for checking for an @ symbol
			// Also doesn't allow underscores
			pattern = Pattern.compile("(admin|mod|gm|sysop|" +
									"fuck|shit|ass|piss|vagina|" +
									"arse|bitch|PWO|PokemonWorldOnline)"
									, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(username);
			if (matcher.find())
				throw new RegisterException(
						RegisterException.Types.BANNED_USERNAME);
						
			String spriteName;
			spriteName = getSpriteName(sprite);
			PlayerChar newPlayer = new PlayerChar();

			newPlayer.setSprite(spriteName);
			newPlayer.setMap(m_maps.getMap(-49, -47));
			newPlayer.setX(640);
			newPlayer.setY(536 + 32);
			newPlayer.setName(username);
			newPlayer.setPasswordHash(encpass);
			newPlayer.setParty(new Pokemon[6]);
			
			//Setup bag
			newPlayer.setBag(new Bag(true));
			newPlayer.getBag().addItem(4, 5);
			newPlayer.setEmail(email);
			newPlayer.initBoxes();
			newPlayer.initPokedex();
			
			Random random = GameServer.getMechanics().getRandom();
			int speciesIndex;
			switch (starter) {
			case 1: // Bulbasaur
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Bulbasaur");
				
				break;
			case 2: // Charmander
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Charmander");
				
				break;
			case 3: // Squirtle
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Squirtle");
				
				break;
			case 4: // Chikorita
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Chikorita");
				
				break;
			case 5: // Cyndaquil
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Cyndaquil");
				
				break;
			case 6: // Totodile
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Totodile");
				
				break;
			case 7: // Treecko
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Treecko");
				
				break;
			case 8: // Torchic
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Torchic");
				
				break;
			case 9: // Mudkip
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Mudkip");
				
				break;
			case 10: // Turtwig
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Turtwig");
				
				break;
			case 11: // Chimchar
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Chimchar");
				
				break;
			case 12: // Piplup
				speciesIndex = GameServer.getSpeciesData().getPokemonByName("Piplup");
				break;
			default:
				throw new RegisterException(
						RegisterException.Types.INVALID_DATA);
			}
			Pokemon starterPkmn = createStarter(speciesIndex);
			newPlayer.setSeen(speciesIndex);
			newPlayer.setCaught(speciesIndex);
			newPlayer.getParty()[0] = starterPkmn;
			newPlayer.setNo(playerAmount);
			starterPkmn.setOriginalTrainer(username);
			starterPkmn.setOriginalNo(newPlayer.getNo());
               // calls a registerexception if the playerfile already exists.
			   // We may wish to add a check for other capitalizations for Linux systems.
			File userfile = new File(m_path + username + ".usr.xml");
			if (!userfile.createNewFile()) { // is the file already there?
				throw new RegisterException(
						RegisterException.Types.ALREADY_EXISTS); // it is, we got a BS registration attempt
			} else {
				// write the new player to a user file for storage
				serializer.write(newPlayer, userfile);
				playerAmount++;
			}
		} catch (RegisterException exc) {
			throw exc;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RegisterException(RegisterException.Types.OTHER);
		}
	}

	public void authUser(String username, String encpass,
			IoSession session) throws LoginException {
		for(int i = 0; i < saveQueue.size(); i++) {
			if(saveQueue.get(i).getName().equalsIgnoreCase(username))
				throw new LoginException(LoginException.Types.SAVEQUEUE);
		}
		for(int i = 0; i < loginDelay.size(); i++) {
			if(loginDelay.get(i)[0].equalsIgnoreCase(username))
				throw new LoginException(LoginException.Types.WAIT);
		}
		for (String extName : playerList.keySet()) {
			if (extName.equalsIgnoreCase(username)) {
				PlayerChar existingPlayer = playerList.get(extName);
				if (existingPlayer.getPasswordHash().equals(encpass)) {
					existingPlayer.getIoSession().removeAttribute("player");
					existingPlayer.getIoSession().close();
					existingPlayer.setIoSession(session);
					existingPlayer.setTalking(false);
					existingPlayer.setTalkingTo(null);
					playerList.remove(existingPlayer);
					playerList.put(existingPlayer.getName(), existingPlayer);
					existingPlayer.getMap().removePlayer(existingPlayer);
					existingPlayer.setMap(m_maps.getMap(existingPlayer.getMapX(), existingPlayer
							.getMapY()));
					try {
						existingPlayer.endBattle();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					loginQueue.add(existingPlayer);
					return;
				}
			}
		}
		try {
			File userfile = new File(m_path + username + ".usr.xml");
			if (userfile.exists()) {

				PlayerChar player = serializer.read(PlayerChar.class, userfile);
				if (player.getPasswordHash().equals(encpass)) {
					if (player.isBanned())
						throw new LoginException(LoginException.Types.BANNED);
					if (ClientHandler.isIPBanned(session.getRemoteAddress().toString().substring(1).split(":")[0]))
						throw new LoginException(LoginException.Types.BANNED);
					if (GameServer.getLockdown() > 1) {
						if ((GameServer.getLockdown() == 2) && !player.isPOK() && !player.isGM() && !player.isMod() && !player.isPMod())
							throw new LoginException(LoginException.Types.OTHER);
						if ((GameServer.getLockdown() == 3) && !player.isGM() && !player.isMod())
							throw new LoginException(LoginException.Types.OTHER);
					}
					player.setIoSession(session);
					session.setAttribute("player", player);
					player.reinitialise();
					playerList.put(player.getName(), player);
					player.setMap(m_maps.getMap(player.getMapX(), player
							.getMapY()));
					loginQueue.add(player);
					return;
				} else {
					throw new LoginException(
							LoginException.Types.WRONG_PASSWORD);
				}
			} else {
				throw new LoginException(LoginException.Types.ACCOUNT_NOT_EXIST);
			}
		} catch (LoginException loginex) {
			throw loginex;
		} catch (Exception exca) {
			exca.printStackTrace();
			throw new LoginException(LoginException.Types.OTHER);
		}
	}

	
	public void save(PlayerChar player) {
		saveQueue.add(player);
	}
	
	public String getSpriteName(int idx) {
		switch (idx) {
			case 0: return "girl";
			case 1: return "girlfive";
			case 2: return "girleight";
			case 3: return "girleleven";
			case 4: return "girlfifteen";
			case 5: return "girlfour";
			case 6: return "girlfourteen";
			case 7: return "girlnine";
			case 8: return "girlseven";
			case 9: return "girlsix";
			case 10: return "girlsixteen";
			case 11: return "girlten";
			case 12: return "girlthirteen";
			case 13: return "girlthree";
			case 14: return "girltwelve";
			case 15: return "girltwo";
			case 16: return "guy";
			case 17: return "guyeight";
			case 18: return "guyeleven";
			case 19: return "guyfive";
			case 20: return "guyfour";
			case 21: return "guyfourteen";
			case 22: return "guynine";
			case 23: return "guyseven";
			case 24: return "guysix";
			case 25: return "guyten";
			case 26: return "guythirteen";
			case 27: return "guythree";
			case 28: return "guytwelve";
			case 29: return "guytwo";
			case 30: return "hiker";
			case 31: return "hikertwo";
			case 32: return "karateguy";
			case 33: return "karateguytwo";
			case 34: return "lady";
			case 35: return "ladyfive";
			case 36: return "ladyfour";
			case 37: return "ladyseven";
			case 38: return "ladysix";
			case 39: return "ladythree";
			case 40: return "ladytwo";
			case 41: return "lass";
			case 42: return "man";
			case 43: return "manfive";
			case 44: return "manfour";
			case 45: return "manthree";
			case 46: return "mantwo";
			case 47: return "nerdthree";
			case 48: return "sailor";
			case 49: return "supernerd";
			case 50: return "swimmerguy";
			case 51: return "youngster";
			case 52: return "youngsterthree";
			case 53: return "youngstertwo";
			case 54: return "dude";
			case 55: return "fatguy";
			case 56: return "fatguytwo";
			case 57: return "fisherman";
			case 58: return "fishermantwo";
			case 59: return "camperboy";
			case 60: return "campergirl";
			case 61: return "campergirltwo";
			case 62: return "camperboytwo";
			case 63: return "bugcatcher";
			default: return "bugcatchertwo";
		}
	}
	public void selfdelete(String username, String encpass) throws DeleteException {
		try {
			if (new File(m_path + username + ".usr.xml").exists())
			{
				PlayerChar player = serializer.read(PlayerChar.class, new File(m_path + username + ".usr.xml"));
				if (player.getPasswordHash().equals(encpass)) {			
					if (!new File(m_path + username + ".usr.del.xml").exists()) {
						new File(m_path + username + ".usr.xml").renameTo(new File(m_path + username + ".usr.del.xml"));
						playerAmount--;
					}
				} else {
					throw new DeleteException(DeleteException.Types.WRONG_PASSWORD);
				}
			} else {
				throw new DeleteException(DeleteException.Types.ACCOUNT_NOT_EXIST);
			}
		} catch (DeleteException deleteex) {
			throw deleteex;
		} catch (Exception exca) {
			exca.printStackTrace();
			throw new DeleteException(DeleteException.Types.OTHER);
		}
	}

	private Pokemon createStarter(int speciesIndex) throws RegisterException {
		PokemonSpecies species = GameServer.getSpeciesData().getSpecies(speciesIndex);
		ArrayList<MoveListEntry> possibleMoves = new ArrayList<MoveListEntry>();
		MoveListEntry[] moves = new MoveListEntry[4];
		Random random = GameServer.getMechanics().getRandom();

		for (int i = 0; i < GameServer.getPOLRDB().getPokemonData(speciesIndex)
				.getStarterMoves().size(); i++) {
			possibleMoves.add(moveList.getMove(GameServer.getPOLRDB().getPokemonData(
					speciesIndex).getStarterMoves().get(i)));
		}
		for (int i = 1; i <= 5; i++) {
			if (GameServer.getPOLRDB().getPokemonData(speciesIndex).getMoves().containsKey(i)) {
				possibleMoves.add(moveList.getMove(GameServer.getPOLRDB().getPokemonData(
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
				species.getPossibleAbilities(GameServer.getSpeciesData())[random
						.nextInt(species.getPossibleAbilities(GameServer.getSpeciesData()).length)],
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
}