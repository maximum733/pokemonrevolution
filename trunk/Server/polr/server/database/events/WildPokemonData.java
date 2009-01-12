package polr.server.database.events;

import org.simpleframework.xml.Element;

/**
 * Stores information for a wild Pokemon on a map
 * @author shinobi
 *
 */
public class WildPokemonData {
	@Element
	private String m_species = "Caterpie";
	@Element
	private int m_minimalLevel = 1;
	@Element
	private int m_maximumLevel = 3;
	@Element
	private int m_encounterRate = 10;
	
	/**
	 * Set the species (name of pokemon)
	 * @param s
	 */
	public void setSpecies(String s) {
		m_species = s;
	}
	
	/**
	 * Returns the species (name of pokemon)
	 * @return
	 */
	public String getSpecies() {
		return m_species;
	}
	
	/**
	 * Sets the minimal level of this wild pokemon
	 * @param l
	 */
	public void setMinimalLevel(int l) {
		m_minimalLevel = 0;
	}
	
	/**
	 * Returns the minimal level of this wild pokemon
	 * @return
	 */
	public int getMinimalLevel() {
		return m_minimalLevel;
	}
	
	/**
	 * Sets the maximum level of this wild pokemon
	 * @param l
	 */
	public void setMaximumLevel(int l) {
		m_maximumLevel = l;
	}
	
	/**
	 * Returns the maximum level of this wild pokemon
	 * @return
	 */
	public int getMaximumLevel() {
		return m_maximumLevel;
	}
	
	/**
	 * Sets the encounter rate of this wild pokemon
	 * @param l
	 */
	public void setEncounterRate(int l) {
		m_encounterRate = l;
	}
	
	/**
	 * Returns the encounter rate of this wild pokemon
	 * @return
	 */
	public int getEncounterRate() {
		return m_encounterRate;
	}
}
