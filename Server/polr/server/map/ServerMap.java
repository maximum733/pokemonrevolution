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

package polr.server.map;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Random;

import polr.server.GameServer;
import polr.server.battle.Pokemon;
import polr.server.battle.PokemonSpecies;
import polr.server.battle.PokemonSpeciesData;
import polr.server.database.POLRDatabase;
import polr.server.mechanics.BattleMechanics;
import polr.server.mechanics.PokemonNature;
import polr.server.mechanics.moves.MoveList;
import polr.server.mechanics.moves.MoveListEntry;
import polr.server.npc.NonPlayerChar;
import polr.server.object.Char;
import polr.server.object.WarpTile;
import polr.server.player.PlayerChar;
import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.TileLayer;

public class ServerMap {
	public enum MapPvPType { NONPVP, PVP, PVPENFORCED }
	private MapMatrix m_mapMatrix;

	private ArrayList<PlayerChar> m_players;
	private ArrayList<NonPlayerChar> m_npcs;
	private ArrayList<WarpTile> m_warpTiles;
	private MapItem m_mapItem;
	private MapPvPType m_pvpType;

	private int m_x;
	private int m_y;
	private int m_height;
	private int m_width;

	private int m_xOffsetModifier;
	private int m_yOffsetModifier;

	private int m_wildProbability;
	private int m_surfProbability;

	private HashMap<String, Integer> wildPokemonChances;
	private HashMap<String, int[]> wildLevels;
	private HashMap<String, Integer> surfPokemonChances;
	private HashMap<String, int[]> surfLevels;

	private Random random = new Random();

	private TileLayer blocked;;
	private TileLayer surf;
	private TileLayer grass;
	private TileLayer ledgesDown;
	private TileLayer ledgesLeft;
	private TileLayer ledgesRight;
	
	public static final String CHARSEP = new String(new char[] { (char) 27 });

	public enum Directions {
		up, down, left, right
	}
	
	/**
	 * Get the width of this map.
	 * @return
	 */
	public int getWidth() {
		return m_width;
	}
	
	/**
	 * Get the height of this map.
	 * @return
	 */
	public int getHeight() {
		return m_height;
	}

	/**
	 * Gets the mapMatrix.
	 * @return
	 */
	public MapMatrix getMapMatrix() {
		return m_mapMatrix;
	}

	/**
	 * Sets the mapMatrix.
	 * @param m
	 */
	public void setMapMatrix(MapMatrix m) {
		m_mapMatrix = m;
	}

	/**
	 * Returns the arraylist of Players.
	 * @return
	 */
	public ArrayList<PlayerChar> getPlayers() {
		return m_players;
	}

	/**
	 * Returns the arraylist of NPCs.
	 * @return
	 */
	public ArrayList<NonPlayerChar> getNPCs() {
		return m_npcs;
	}
	
	/**
	 * Returns a random NPC. Used for NPC movements.
	 * @return
	 */
	public NonPlayerChar getRandomNPC() {
		if(m_npcs != null && m_npcs.size() > 0)
			return m_npcs.get(GameServer.getMechanics().getRandom().nextInt(m_npcs.size()));
		else
			return null;
	}
	
	/**
	 * Drop an item
	 * @param x
	 * @param y
	 * @param id
	 */
	public void placeItem(int x, int y, int id) {
		m_mapItem = new MapItem(x, y, id);
	}
	
	/**
	 * Pick up an item
	 * @param p
	 * @return
	 */
	public int takeItem(PlayerChar p) {
		if(m_mapItem != null) {
			int id = m_mapItem.id;
			p.getBag().addItem(id, 1);
			m_mapItem = null;
			return id;
		} else {
			return -1;
		}
	}
	
	/**
	 * Returns true if there is an item hidden at that location
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean itemAt(int x, int y) {
		return m_mapItem == null ? false : m_mapItem.x == x && m_mapItem.y == y;
	}

	/**
	 * Generates a new wild Pokemon (land)
	 * @param mech
	 * @param polrData
	 * @param speciesData
	 * @return
	 */
	public Pokemon generateWildPokemon(BattleMechanics mech,
			POLRDatabase polrData, PokemonSpeciesData speciesData) {
		String speciesName = getWildSpecies();
		int speciesIndex;
		try {
			speciesIndex = speciesData.getPokemonByName(speciesName);	}
		catch ( Exception e) { 
			speciesIndex = speciesData.getPokemonByName("Rattata");
		}
			
		PokemonSpecies species;
		try {
			species = speciesData.getSpecies(speciesIndex);}
		catch ( Exception e) { 
			species = speciesData.getSpecies(speciesData.getPokemonByName("Rattata"));
		}
		
		MoveListEntry[] moves = new MoveListEntry[4];
		ArrayList<MoveListEntry> possibleMoves = new ArrayList<MoveListEntry>();
		MoveList moveList = MoveList.getDefaultData();
		int level = getWildLevel(speciesName);
		for (int i = 0; i < polrData.getPokemonData(speciesIndex)
				.getStarterMoves().size(); i++) {
			possibleMoves.add(moveList.getMove(polrData.getPokemonData(
					speciesIndex).getStarterMoves().get(i)));
		}
		for (int i = 1; i <= level; i++) {
			if (polrData.getPokemonData(speciesIndex).getMoves().containsKey(i)) {
				possibleMoves.add(moveList.getMove(polrData.getPokemonData(
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
		Pokemon wildPokemon = new Pokemon(
				mech,
				species,
				PokemonNature.getNature(random.nextInt(PokemonNature
						.getNatureNames().length)),
						species.getPossibleAbilities(species.getDefaultData())[random
						                          						     .nextInt(species.getPossibleAbilities(species.getDefaultData()).length)],
				null, species.getPossibleGenders(), level, new int[] {
						random.nextInt(32), // IVs
						random.nextInt(32), random.nextInt(32),
						random.nextInt(32), random.nextInt(32),
						random.nextInt(32) }, new int[] { 0, 0, 0, 0, 0, 0 }, // EVs
				moves, new int[] { 0, 0, 0, 0 });
		wildPokemon.setBaseExp(polrData.getPokemonData(speciesIndex)
				.getBaseEXP());
		wildPokemon.setExpType(polrData.getPokemonData(speciesIndex)
				.getGrowthRate());
		wildPokemon.setExp(mech.getExpForLevel(wildPokemon, level));
		wildPokemon.setHappiness(polrData.getPokemonData(speciesIndex).getHappiness());
		return wildPokemon;
	}
	
	/**
	 * Generates a new wild Pokemon (surfing)
	 * @param mech
	 * @param polrData
	 * @param speciesData
	 * @return
	 */
	public Pokemon generateSurfPokemon(BattleMechanics mech,
						POLRDatabase polrData, PokemonSpeciesData speciesData) {
		System.out.println("Attempting to send battle info") ;
		String speciesName = getSurfSpecies();
		System.out.println("Species received") ;
		int speciesIndex;
		try {
			speciesIndex = speciesData.getPokemonByName(speciesName);	}
		catch ( Exception e) { 
			speciesIndex = speciesData.getPokemonByName("Rattata");
		}
			
		PokemonSpecies species;
		try {
			System.out.println("Attempting to send species info") ;
			species = speciesData.getSpecies(speciesIndex);}
		catch ( Exception e) { 
			species = speciesData.getSpecies(speciesData.getPokemonByName("Rattata"));
		}
		System.out.println("Attempting to get MoveList");
		MoveListEntry[] moves = new MoveListEntry[4];
		ArrayList<MoveListEntry> possibleMoves = new ArrayList<MoveListEntry>();
		MoveList moveList = MoveList.getDefaultData();
		int level = getSurfLevel(speciesName);
		for (int i = 0; i < polrData.getPokemonData(speciesIndex)
				.getStarterMoves().size(); i++) {
			possibleMoves.add(moveList.getMove(polrData.getPokemonData(
					speciesIndex).getStarterMoves().get(i)));
		}
		for (int i = 1; i <= level; i++) {
			if (polrData.getPokemonData(speciesIndex).getMoves().containsKey(i)) {
				possibleMoves.add(moveList.getMove(polrData.getPokemonData(
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
		Pokemon surfPokemon = new Pokemon(
				mech,
				species,
				PokemonNature.getNature(random.nextInt(PokemonNature
						.getNatureNames().length)),
						species.getPossibleAbilities(species.getDefaultData())[random
						        .nextInt(species.getPossibleAbilities(species.getDefaultData()).length)],
				null, species.getPossibleGenders(), level, new int[] {
						random.nextInt(32), // IVs
						random.nextInt(32), random.nextInt(32),
						random.nextInt(32), random.nextInt(32),
						random.nextInt(32) }, new int[] { 0, 0, 0, 0, 0, 0 }, // EVs
				moves, new int[] { 0, 0, 0, 0 });
		surfPokemon.setBaseExp(polrData.getPokemonData(speciesIndex)
				.getBaseEXP());
		surfPokemon.setExpType(polrData.getPokemonData(speciesIndex)
				.getGrowthRate());
		surfPokemon.setExp(mech.getExpForLevel(surfPokemon, level));
		surfPokemon.setHappiness(polrData.getPokemonData(speciesIndex).getHappiness());
		System.out.println("finished generating pokemon");
		return surfPokemon;

	}
	
	/**
	 * Returns a string of possible wild pokemons
	 * @return
	 */
	private String getWildSpecies() {
		ArrayList<String> potentialSpecies = new ArrayList<String>();
		do {
			for (String species : wildPokemonChances.keySet()) {
				if (random.nextInt(101) < wildPokemonChances.get(species))
					potentialSpecies.add(species);
			}
		} while (potentialSpecies.size() <= 0);
		return potentialSpecies.get(random.nextInt(potentialSpecies.size()));
	}

	/**
	 * Returns a level for wild pokemon. NOTE: It's possible levels are preset.
	 * @param speciesName
	 * @return
	 */
	private int getWildLevel(String speciesName) {
		int[] range = wildLevels.get(speciesName);
		int unshiftedLevel = random.nextInt((range[1] - range[0]) + 1);
		return unshiftedLevel + range[0];
	}

	/**
	 * Returns a string of potential species that appear while surfing
	 * @return
	 */
	private String getSurfSpecies() {
		ArrayList<String> potentialSpecies = new ArrayList<String>();
		do {
			for (String surfspecies : surfPokemonChances.keySet()) {
				if (random.nextInt(101) < surfPokemonChances.get(surfspecies))
					potentialSpecies.add(surfspecies);
			}
		} while (potentialSpecies.size() <= 0);
		return potentialSpecies.get(random.nextInt(potentialSpecies.size()));
	}

	/**
	 * Returns a level of pokemon while surfing. NOTE: Possible levels are preset.
	 * @param speciesName
	 * @return
	 */
	private int getSurfLevel(String speciesName) {
		int[] range = surfLevels.get(speciesName);
		int unshiftedLevel = random.nextInt((range[1] - range[0]) + 1);
		return unshiftedLevel + range[0];
	}

	/**
	 * Add a player to the map
	 * @param p
	 */
	public void addPlayer(PlayerChar p) {
		getPlayers().add(p);
		sendToAllBut("A" + p.getNo() + "," + p.getName() + ","
				+ p.getFacing().toString() + "," + (p.isSurfing() ? "swim":p.getSprite()) + "," +
				p.getX() + "," + p.getY(), p);
		p.setMap(this);
		p.getIoSession().write("mS" + String.valueOf(this.m_x) + "," + String.valueOf(this.m_y));
	}

	/**
	 * Remove a player from the map
	 * @param p
	 */
	public void removePlayer(PlayerChar p) {
		sendToAllBut("PR" + p.getNo(), p);
		getPlayers().remove(p);
	}
	
	/**
	 * Add a warp tile
	 * @param w
	 */
	public void addWarpTile(WarpTile w) {
		m_warpTiles.add(w);
	}
	
	/**
	 * Get the warp tile at co-ordinates (x,y)
	 * @param x
	 * @param y
	 * @return
	 */
	public WarpTile getWarpTileAt(int x, int y) {
		for(int i = 0; i < m_warpTiles.size(); i++) {
			WarpTile w = m_warpTiles.get(i);
			if(w.getX() == x && w.getY() == y)
				return w;
		}
		return null;
	}
	
	/**
	 * Add an NPC to the map
	 * @param npc
	 */
	public void addNPC(NonPlayerChar npc) {
		npc.setIndex(-1 - m_npcs.size());
		m_npcs.add(npc);
	}

	/**
	 * Default constructor
	 * 
	 * @param map
	 * @param x
	 * @param y
	 */
	public ServerMap(Map map, int x, int y) {
		m_width = map.getWidth();
		m_height = map.getHeight();
		m_players = new ArrayList<PlayerChar>();
		m_npcs = new ArrayList<NonPlayerChar>();
		m_warpTiles = new ArrayList<WarpTile>();

		this.m_x = x;
		this.m_y = y;

		//Load in all the collisions, water tiles, etc.
		for (int i = 0; i < map.getTotalLayers(); i++) {
			MapLayer l = map.getLayer(i);
			if (l.getName().equalsIgnoreCase("Collisions"))
				blocked = (TileLayer) l;
			else if(l.getName().equalsIgnoreCase("LedgesRight"))
				ledgesRight = (TileLayer)l;
			else if(l.getName().equalsIgnoreCase("LedgesLeft"))
				ledgesLeft = (TileLayer) l;
			else if(l.getName().equalsIgnoreCase("LedgesDown"))
				ledgesDown = (TileLayer)l;
			else if(l.getName().equalsIgnoreCase("Water"))
				surf = (TileLayer)l;
			else if (l.getName().equalsIgnoreCase("Grass"))
				grass = (TileLayer) l;
		
		}
		
		//Set the map's PvP property
		try {
			String pvpType = map.getProperties().getProperty("pvp");
			if(pvpType.equalsIgnoreCase("enforced")) {
				m_pvpType = MapPvPType.PVPENFORCED;
			} else if(pvpType.equalsIgnoreCase("no")) {
				m_pvpType = MapPvPType.NONPVP;
			} else {
				m_pvpType = MapPvPType.PVP;
			}
		} catch (Exception e) {
			m_pvpType = MapPvPType.PVP;
		}
		
		//Set the map offset
		m_xOffsetModifier = Integer.parseInt(map.getProperties().getProperty(
				"xOffsetModifier"));
		m_yOffsetModifier = Integer.parseInt(map.getProperties().getProperty(
				"yOffsetModifier"));
		//Set the probability of wild pokemon
		try {
			m_wildProbability = Integer.parseInt(map.getProperties().getProperty(
			"wildProbability"));
		} catch (Exception e) {
			System.err.println("wildProbability variable missing for " + this.getX() + "," + this.getY());
			m_wildProbability = 0;
		}
		//Set the probability of water pokemon
		try {
			m_surfProbability = Integer.parseInt(map.getProperties().getProperty(
			"surfProbability"));
		} catch (Exception e) {
			System.err.println("surfProbability variable missing for " + this.getX() + "," + this.getY());
			m_surfProbability = 0;
		}
		//Set the possible pokemons & levels for them
		wildPokemonChances = new HashMap<String, Integer>();
		wildLevels = new HashMap<String, int[]>();
		String[] species;
		String[] levels;
		try {
			species = map.getProperties().getProperty("wildSpecies")
			.split(";");
			levels = map.getProperties().getProperty("wildLevels")
			.split(";");
		} catch (Exception e) {
			System.err.println("wildSpecies or wildLevels variable missing for " + this.getX() + "," + this.getY());
			species = new String[]{""};
			levels = new String[]{""};
		}
		if (!species[0].equals("") && !levels[0].equals("")
				&& species.length == levels.length) {
			for (int i = 0; i < species.length; i++) {
				String[] speciesInfo = species[i].split(",");
				wildPokemonChances.put(speciesInfo[0], Integer
						.parseInt(speciesInfo[1]));
				String[] levelInfo = levels[i].split("-");
				wildLevels.put(speciesInfo[0], new int[] {
						Integer.parseInt(levelInfo[0]),
						Integer.parseInt(levelInfo[1]) });

			}
		}
		surfPokemonChances = new HashMap<String, Integer>();
		surfLevels = new HashMap<String, int[]>();
		String[] surfspecies = new String[]{""};
		String[] surflevels = new String[]{""};
		try {
			surfspecies = map.getProperties().getProperty("surfSpecies")
			.split(";");
			surflevels = map.getProperties().getProperty("surfLevels")
			.split(";");
		} catch (Exception e) {
			System.err.println("surfSpecies or surfLevels variable missing for " + this.getX() + "," + this.getY());
			surfspecies = new String[]{""};
			surflevels = new String[]{""};
		}
		if (!surfspecies[0].equals("") && !surflevels[0].equals("")
				&& surfspecies.length == surflevels.length) {
			for (int i = 0; i < surfspecies.length; i++) {
				String[] surfspeciesInfo = surfspecies[i].split(",");
				surfPokemonChances.put(surfspeciesInfo[0], Integer
						.parseInt(surfspeciesInfo[1]));
				String[] surflevelInfo = surflevels[i].split("-");
				surfLevels.put(surfspeciesInfo[0], new int[] {
						Integer.parseInt(surflevelInfo[0]),
						Integer.parseInt(surflevelInfo[1]) });

			}
		}
	}	

	private void getPlayerData(PlayerChar p2) {
		for (PlayerChar p : m_players) {
			synchronized (m_players) {
				// clientHandler handler =
				// handlers.
				// (clientHandler)handlers.elementAt(i);
				// if (handler.authed) {
				
				p2.getIoSession().write("mA" + p.getNo() + "," + p.getName() + ","
						+ p.getFacing().toString() + "," + (p.isSurfing() ? "swim":p.getSprite()) + "," +
						p.getX() + "," + p.getY());
			}
		}
	}

	/**
	 * Returns true if a wild Pokemon appeared.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isWildEncounter(int x, int y) {
		if (random.nextInt(2874) < m_wildProbability * 16)
			if (grass != null && grass.getTileAt(x / 32, y / 32) != null)
				return true;
		return false;
	}
	
	/**
	 * Returns true if a wild Pokemon appeared (surfing).
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isSurfEncounter(int x, int y) {
		if (random.nextInt(2874) < m_surfProbability * 16)
			if (surf != null && surf.getTileAt(x / 32, y / 32) != null)
				return true;
		return false;
	}

	/**
	 * Sends all NPC data
	 * @param p2
	 */
	private void getNPCData(PlayerChar p2) {
		NonPlayerChar p;
		synchronized (m_npcs) {
			for (int i = 0; i < m_npcs.size(); i++) {
				p = m_npcs.get(i);
				p2.getIoSession().write("mA" + p.getIndex() + "," + p.getName() + ","
						+ p.getFacing().toString() + "," + p.getSprite() + "," +
						p.getX() + "," + p.getY());
			}
		}
	}

	/**
	 * Sends all map information to a player
	 * @param p
	 */
	public void propagateMapData(PlayerChar p) {
		if(!p.isPropagated()) {
			getPlayerData(p);
			getNPCData(p);
		}
	}

	/**
	 * Sends the string to every player
	 * @param s
	 */
	public void sendToAll(String s) {
		try {
			for (PlayerChar p : m_players) {
				p.getIoSession().write(s);
			}
		} catch (ConcurrentModificationException e) {}
	}
	
	/**
	 * Sends the string to every player on the map except for the player passed into the method
	 * @param s
	 * @param player
	 */
	public void sendToAllBut(String s, PlayerChar player) {
		for (PlayerChar p : m_players) {
			if (p != player)
				p.getIoSession().write(s);
		}
	}

	/**
	 * Returns the NPC at (x, y)
	 * @param x
	 * @param y
	 * @return
	 */
	public NonPlayerChar getNPCAt(int x, int y) {
		NonPlayerChar n;
		for(int i = 0; i < m_npcs.size(); i++) {
			n = m_npcs.get(i);
			if (n.getX() == x && n.getY() == y)
				return n;
		}
		n = null;
		return n;
	}
	
	/**
	 * Returns true there is a collision at (x, y)
	 * @param tileX
	 * @param tileY
	 * @param directions
	 * @return
	 */
	public boolean isBlocked(int tileX, int tileY, Directions directions) {
		if (blocked.getTileAt(tileX, tileY) != null)
			return true;
		if(m_mapItem != null && m_mapItem.x == tileX && m_mapItem.y == tileY)
			return true;
		if(ledgesRight != null && ledgesRight.getTileAt(tileX, tileY) != null) {
			if(directions == Directions.left || directions == Directions.up || directions == Directions.down)
				return true;
		}
		if(ledgesLeft != null && ledgesLeft.getTileAt(tileX, tileY) != null) {
			if(directions == Directions.right || directions == Directions.up || directions == Directions.down)
				return true;
		}
		if(ledgesDown != null && ledgesDown.getTileAt(tileX, tileY) != null) {
			if(directions == Directions.left || directions == Directions.up || directions == Directions.right)
				return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the char can move
	 * @param dir
	 * @param p
	 * @return
	 */
	public boolean canMove(Directions dir, Char p) {
		int playerX = p.getX();
		int playerY = p.getY();

		switch (dir) {
		case up:
			if (playerY >= 1) {
				if (!isBlocked(playerX / 32, ((playerY + 8) - 32) / 32, Directions.up)
						&& (getNPCAt(playerX, playerY - 32) == null)) {
					if(p.isSurfing()) {
						if(surf != null && surf.getTileAt(playerX / 32, ((playerY + 8) - 32) / 32) != null)
							return true;
						else {
							//The player can move but is no longer surfing
							p.setSurfing(false);
							return true;
						}
					} else {
						if(surf != null && surf.getTileAt(playerX / 32, ((playerY + 8) - 32) / 32) != null) {
							if(p instanceof PlayerChar) {
								PlayerChar p1 = (PlayerChar) p;
								if(p1.getBadgeCount() > 5) {
									p1.setSurfing(true);
									p1.move(p1.getFacing());
									return true;
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else {
							//TODO: Add warp tile check
							return true;
						}
					}
				} else {
					return false;
				}
			} else {
				ServerMap newMap = m_mapMatrix.getMap(m_x, m_y - 1);
				if (newMap != null) {
					m_mapMatrix.moveBetweenMaps((PlayerChar) p, this, newMap);
				}
			}
			break;
		case down:
			if (playerY + 40 < m_height * 32) {
				if (!isBlocked(playerX / 32, ((playerY + 8) + 32) / 32, Directions.down)
						&& (getNPCAt(playerX, playerY + 32) == null)) {
					if(p.isSurfing()) {
						if(surf != null && surf.getTileAt(playerX / 32, ((playerY + 8) + 32) / 32) != null)
							return true;
						else {
							//The player can move but is no longer surfing
							p.setSurfing(false);
							return true;
						}
					} else {
						if(surf != null && surf.getTileAt(playerX / 32, ((playerY + 8) + 32) / 32) != null) {
							if(p instanceof PlayerChar) {
								PlayerChar p1 = (PlayerChar) p;
								if(p1.getBadgeCount() > 5) {
									p1.setSurfing(true);
									p1.move(p1.getFacing());
									return true;
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else {
							//TODO: Add warp tile check
							return true;
						}
					}
				} else {
					return false;
				}
			} else {
				ServerMap newMap = m_mapMatrix.getMap(m_x, m_y + 1);
				if (newMap != null) {
					m_mapMatrix.moveBetweenMaps((PlayerChar) p, this, newMap);
				}
			}
			break;
		case left:
			if (playerX >= 32) {
				if (!isBlocked((playerX - 32) / 32, (playerY + 8) / 32, Directions.left)
						&& (getNPCAt(playerX - 32, playerY) == null)) {
					if(p.isSurfing()) {
						if(surf != null && surf.getTileAt((playerX - 32) / 32, (playerY + 8) / 32) != null)
							return true;
						else {
							//The player can move but is no longer surfing
							p.setSurfing(false);
							return true;
						}
					} else {
						if(surf != null && surf.getTileAt((playerX - 32) / 32, (playerY + 8) / 32) != null) {
							if(p instanceof PlayerChar) {
								PlayerChar p1 = (PlayerChar) p;
								if(p1.getBadgeCount() > 5) {
									p1.setSurfing(true);
									p1.move(p1.getFacing());
									return true;
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else {
							//TODO: Add warp tile check
							return true;
						}
					}
				} else {
					return false;
				}
			} else {
				ServerMap newMap = m_mapMatrix.getMap(m_x - 1, m_y);
				if (newMap != null) {
					m_mapMatrix.moveBetweenMaps((PlayerChar) p, this, newMap);
				}
			}
			break;
		case right:
			if (playerX + 32 < m_width * 32) {
				if (!isBlocked((playerX + 32) / 32, (playerY + 8) / 32, Directions.right)
						&& (getNPCAt(playerX + 32, playerY) == null)) {
					if(p.isSurfing()) {
						if(surf != null && surf.getTileAt((playerX + 32) / 32, (playerY + 8) / 32) != null)
							return true;
						else {
							//The player can move but is no longer surfing
							p.setSurfing(false);
							return true;
						}
					} else {
						if(surf != null && surf.getTileAt((playerX + 32) / 32, (playerY + 8) / 32) != null) {
							if(p instanceof PlayerChar) {
								PlayerChar p1 = (PlayerChar) p;
								if(p1.getBadgeCount() > 5) {
									p1.setSurfing(true);
									p1.move(p1.getFacing());
									return true;
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else {
							//TODO: Add warp tile check
							return true;
						}
					}
				} else {
					return false;
				}
			} else {
				ServerMap newMap = m_mapMatrix.getMap(m_x + 1, m_y);
				if (newMap != null) {
					m_mapMatrix.moveBetweenMaps((PlayerChar) p, this, newMap);
				}
			}
			break;
		}
		return false;
	}

	/**
	 * Returns the x co-ordinate of this map in the map matrix
	 * @return
	 */
	public int getX() {
		return m_x;
	}

	/**
	 * Returns the y co-ordinate of this map in the map matrix
	 * @return
	 */
	public int getY() {
		return m_y;
	}

	/**
	 * Returns the x offset of the map
	 * @return
	 */
	public int getXOffsetModifier() {
		return m_xOffsetModifier;
	}

	/**
	 * Returns the y offset of the map
	 * @return
	 */
	public int getYOffsetModifier() {
		return m_yOffsetModifier;
	}
}
