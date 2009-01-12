package polr.server.database.events;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import polr.server.map.ServerMap.MapPvPType;

/**
 * Stores MapData (NPCs, WarpTiles, Pokemon)
 * @author shinobi
 *
 */
@Root
public class MapData {
	@ElementList(required=false)
	private List<NpcData> m_npcs = new ArrayList<NpcData>();
	@ElementList(required=false)
	private List<WarpData> m_warps = new ArrayList<WarpData>();
	@ElementList(required=false)
	private List<WildPokemonData> m_dayPokemon = new ArrayList<WildPokemonData>();
	@ElementList(required=false)
	private List<WildPokemonData> m_nightPokemon = new ArrayList<WildPokemonData>();
	@ElementList(required=false)
	private List<WildPokemonData> m_surfPokemon = new ArrayList<WildPokemonData>();
	@Element
	private MapPvPType m_pvpType = MapPvPType.PVP;
	@Element
	private int m_wildProbability = 30;
	
	/**
	 * Returns the probability of a wild pokemon encounter on this map
	 * @return
	 */
	public int getWildProbability() {
		return m_wildProbability;
	}
	
	/**
	 * Sets the probability of wild Pokemon on this map
	 * @param w
	 */
	public void setWildProbability(int w) {
		m_wildProbability = w;
	}
	
	/**
	 * Returns the Pvp type of this map
	 * @return
	 */
	public MapPvPType getMapPvpType() {
		return m_pvpType;
	}
	
	/**
	 * Sets this maps pvp type. PVP, NONPVP, PVPENFORCED
	 * @param type
	 */
	public void setMapPvpType(String type) {
		try {
			m_pvpType = MapPvPType.valueOf(type);
		} catch (Exception e) {
			m_pvpType = MapPvPType.PVP;
		}
	}
	
	/**
	 * Returns the list of pokemon that appear during the day
	 * @return
	 */
	public List<WildPokemonData> getDayPokemon() {
		return m_dayPokemon;
	}
	
	/**
	 * Add a wild pokemon that appears during the day
	 * @param wp
	 */
	public void addDayPokemon(WildPokemonData wp) {
		m_dayPokemon.add(wp);
	}
	
	/**
	 * Remove a wild pokemon that appears during the day
	 * @param i
	 */
	public void removeDayPokemon(int i) {
		m_dayPokemon.remove(i);
	}
	
	/**
	 * Returns the list of pokemon that appear during the night
	 * @return
	 */
	public List<WildPokemonData> getNightPokemon() {
		return m_nightPokemon;
	}
	
	/**
	 * Add a wild pokemon that appears during the night
	 * @param wp
	 */
	public void addNightPokemon(WildPokemonData wp) {
		m_nightPokemon.add(wp);
	}
	
	/**
	 * Remove a wild pokemon that appears during the night
	 * @param i
	 */
	public void removeNightPokemon(int i) {
		m_nightPokemon.remove(i);
	}
	
	/**
	 * Returns the list of pokemon that appear during the day
	 * @return
	 */
	public List<WildPokemonData> getSurfPokemon() {
		return m_surfPokemon;
	}
	
	/**
	 * Add a wild pokemon that appears during the day
	 * @param wp
	 */
	public void addSurfPokemon(WildPokemonData wp) {
		m_surfPokemon.add(wp);
	}
	
	/**
	 * Remove a wild pokemon that appears during the day
	 * @param i
	 */
	public void removeSurfPokemon(int i) {
		m_surfPokemon.remove(i);
	}
	
	/**
	 * Add warptile data to this map data
	 * @param w
	 */
	public void addWarpData(WarpData w) {
		m_warps.add(w);
	}
	
	/**
	 * Remove warp tile data from this map data
	 * @param i
	 */
	public void removeWarpData(int i) {
		m_warps.remove(i);
	}
	
	/**
	 * Return the list of warp tiles
	 * @return
	 */
	public List<WarpData> getWarpTiles() {
		return m_warps;
	}
	
	/**
	 * Add npc data to this this map data
	 * @param n
	 */
	public void addNpcData(NpcData n) {
		m_npcs.add(n);
	}
	
	/**
	 * Remove npc data from this map data
	 * @param i
	 */
	public void removeNpcData(int i) {
		m_npcs.remove(i);
	}
	
	/**
	 * Return the list of npcs
	 * @return
	 */
	public List<NpcData> getNpcData() {
		return m_npcs;
	}
}
