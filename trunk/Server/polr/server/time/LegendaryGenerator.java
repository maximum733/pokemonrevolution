package polr.server.time;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import polr.server.GameServer;
import polr.server.battle.Pokemon;

/**
 * Handles legendary Pokemon appearances
 * 
 * @author TMKCodes
 * @author shinobi
 *
 */
@Root
public class LegendaryGenerator {
	@ElementList
	private ArrayList<Pokemon> m_legendaries;
	private boolean m_appearance = false;
	
	public LegendaryGenerator() {}
	
	/**
	 * Setup all the Pokemon, only to be called if it's the server's first time starting up.
	 */
	public void initialise() {
		m_legendaries = new ArrayList<Pokemon>();
		this.generateLegendaries();
	}
	
	/**
	 * Returns if a legendary has appeared in the Pokemon World
	 * @return
	 */
	public boolean isLegendaryAppeared() {
		return m_appearance;
	}
	
	/**
	 * Generates the appearance of a legendary
	 */
	public void generateAppearance() {
		Pokemon legend = m_legendaries.get(GameServer.getMechanics().getRandom().nextInt(m_legendaries.size()));
		if(legend.getSpeciesName().equalsIgnoreCase("articuno")) {
			//TODO: Add to map
		} else if(legend.getSpeciesName().equalsIgnoreCase("zapdos")) {
			//TODO: Add to map
		} else if(legend.getSpeciesName().equalsIgnoreCase("moltres")) {
			//TODO: Add to map
		} else if(legend.getSpeciesName().equalsIgnoreCase("mew")) {
			//TODO: Add to map
		} else if(legend.getSpeciesName().equalsIgnoreCase("mewtwo")) {
			//TODO: Add to map
		}
		//TODO: Finish the rest
		m_appearance = true;
	}
	
	/**
	 * Ends the appearance of a legendary
	 */
	public void killAppearance() {
		//TODO: Remove the NPC from the map
	}
	
	/**
	 * Generates Pokemon objects of every legendary
	 */
	private void generateLegendaries() {
		//TODO: Generate all the Pokemon
	}
	
	/**
	 * To be called i
	 * @param species
	 */
	public void legendaryCaught(String species) {
		for(int i = 0; i < m_legendaries.size(); i++) {
			if(m_legendaries.get(i).getSpeciesName().equalsIgnoreCase(species)) {
				m_legendaries.remove(i);
				break;
			}
		}
	}
}
