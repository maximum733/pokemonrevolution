package polr.server.database.events;

import org.simpleframework.xml.Element;

import polr.server.npc.NonPlayerChar.NpcType;

/**
 * Stores npc data to be loaded into a map
 * @author shinobi
 *
 */
public class NpcData {
	@Element
	private int m_x = 0;
	@Element
	private int m_y = 0;
	@Element
	private int m_sprite = 0;
	@Element
	private String m_name = "";
	@Element
	private NpcType m_type = NpcType.NORMAL;
	@Element(required=false)
	private String [] m_potentialPokemon;
	@Element
	private int m_minimalLevel = 1;
	@Element
	private String m_speech = "";
	@Element(required=false)
	private String m_questSpeech;
	@Element(required=false)
	private String m_endSpeech;
	@Element
	private boolean m_startsQuest = false;
	@Element
	private boolean m_endsQuest = false;
	@Element
	private int m_questId = 0;
	@Element
	private int m_badgeReq = 0;
	@Element
	private int m_itemReq = 0;
	@Element
	private int m_pokeReq = 0;
	@Element
	private int m_questReq = 0;
	@Element
	private int m_pokeLevelReq = 0;
	@Element
	private int m_money = 0;
	@Element(required=false)
	private String m_badge;
	
	/**
	 * Retursn the y co-ordinate of this npc
	 * @return
	 */
	public int getY() {
		return m_y;
	}
	
	/**
	 * Sets the y co-ordinate of this npc
	 * @param y
	 */
	public void setY(int y) {
		m_y = y;
	}
	
	/**
	 * Returns the x co-ordinate of this npc
	 * @return
	 */
	public int getX() {
		return m_x;
	}
	
	/**
	 * Set the x co-ordinate of this npc
	 * @param x
	 */
	public void setX(int x) {
		m_x = x;
	}
	
	/**
	 * Sets the sprite of this npc
	 * @param sprite
	 */
	public void setSprite(String sprite) {
		try {
			m_sprite = Integer.parseInt(sprite);
		} catch (Exception e) {
			m_sprite = 0;
		}
	}
	
	/**
	 * Returns the sprite of this NPC
	 * @return
	 */
	public int getSprite() {
		return m_sprite;
	}
	
	/**
	 * Sets the name of this npc
	 * @param name
	 */
	public void setName(String name) {
		m_name = name;
	}
	
	/**
	 * Returns the name of this npc
	 * @return
	 */
	public String getName() {
		return m_name;
	}
	
	/**
	 * Sets the quest id of this npc
	 * @param id
	 */
	public void setQuestId(int id) {
		m_questId = id;
	}
	
	/**
	 * Returns the quest id of this npc, if any
	 * @return
	 */
	public int getQuestId() {
		return m_questId;
	}
	
	/**
	 * Sets the npc type of this npc
	 * @param type
	 */
	public void setNpcType(String type) {
		try {
			m_type = NpcType.valueOf(type);
		} catch (Exception e) {
			m_type = NpcType.NORMAL;
		}
	}
	
	/**
	 * Returns the npc type of this npcs
	 * @return
	 */
	public NpcType getNpcType() {
		return m_type;
	}
	
	/**
	 * Sets a pokemon of this npc's party
	 * @param name
	 * @param level
	 * @param index
	 */
	public void setPotentialPokemon(String name, int index) {
		if(m_potentialPokemon == null)
			m_potentialPokemon = new String[6];
		m_potentialPokemon[index] = name;
	}
	
	/**
	 * Returns the potential pokemon of this npc
	 * @return
	 */
	public String [] getPotentialPokemon() {
		return m_potentialPokemon;
	}
	
	/**
	 * Set the minimal level of this NPC's potential party
	 * @param level
	 */
	public void setMinimalPokemonLevel(String level) {
		try {
			m_minimalLevel = Integer.parseInt(level);
		} catch (Exception e) {
			m_minimalLevel = 1;
		}
	}
	
	/**
	 * Returns the minimal level of this NPCs party
	 * @return
	 */
	public int getMinimalPokemonLevel() {
		return m_minimalLevel;
	}
	
	/**
	 * Set the standard speech of this npc
	 * @param speech
	 */
	public void setSpeech(String speech) {
		m_speech = speech;
	}
	
	/**
	 * Returns the standard speech of this npc
	 * @return
	 */
	public String getSpeech() {
		return m_speech;
	}
	
	/**
	 * Set the quest speech of this npc
	 * @param speech
	 */
	public void setQuestSpeech(String speech) {
		m_questSpeech = speech;
	}
	
	/**
	 * Returns the quest speech of this npc
	 * @return
	 */
	public String getQuestSpeech() {
		return m_questSpeech;
	}
	
	/**
	 * Set the speech of this npc after a battle
	 * @param speech
	 */
	public void setEndSpeech(String speech) {
		m_endSpeech = speech;
	}
	
	/**
	 * Returns the speech of this npc after a battle
	 * @return
	 */
	public String getEndSpeech() {
		return m_endSpeech;
	}
	
	/**
	 * Set if this npc starts a quest
	 * @param b
	 */
	public void setStartsQuest(String b) {
		try {
			m_startsQuest = Boolean.parseBoolean(b);
		} catch (Exception e) {
			m_startsQuest = false;
		}
	}
	
	/**
	 * Returns if this npc starts a quest
	 * @return
	 */
	public boolean startsQuest() {
		return m_startsQuest;
	}
	
	/**
	 * Set if the npc ends a quest
	 * @param b
	 */
	public void setEndsQuest(String b) {
		try {
			m_endsQuest = Boolean.parseBoolean(b);
		} catch (Exception e) {
			m_endsQuest = false;
		}
	}
	
	/**
	 * Returns if this npc ends a quest
	 * @return
	 */
	public boolean endsQuest() {
		return m_endsQuest;
	}
	
	/**
	 * Sets the badge requirement for this quest npc
	 * @param i
	 */
	public void setBadgeRequirement(int i) {
		m_badgeReq = i;
	}
	
	/**
	 * Return the badge requirement for this quest npc
	 * @return
	 */
	public int getBadgeRequirement() {
		return m_badgeReq;
	}
	
	/**
	 * Sets the item requirement for this quest npc
	 * @param i
	 */
	public void setItemRequirement(int i) {
		m_itemReq = i;
	}
	
	/**
	 * Return the item requirement for this quest npc
	 * @return
	 */
	public int getItemRequirement() {
		return m_itemReq;
	}
	
	/**
	 * Sets the pokemon requirement of this quest npc
	 * @param i
	 */
	public void setPokemonRequirement(int i) {
		m_pokeReq = i;
	}
	
	/**
	 * Return the pokemon requirement for this quest npc
	 * @return
	 */
	public int getPokemonRequirement() {
		return m_pokeReq;
	}
	
	/**
	 * Sets if a previous quest is required for this quest npc
	 * @param i
	 */
	public void setQuestRequirement(int i) {
		m_questReq = i;
	}
	
	/**
	 * Return the quest requirement for this quest npc
	 * @return
	 */
	public int getQuestRequirement() {
		return m_questReq;
	}
	
	/**
	 * Set the pokemon level requirement for this quest npc
	 * @param i
	 */
	public void setPokemonLevelRequirement(int i) {
		m_pokeLevelReq = i;
	}
	
	/**
	 * Return the pokemon level requirement for this quest npc
	 * @return
	 */
	public int getPokemonLevelRequirement() {
		return m_pokeLevelReq;
	}
	
	/**
	 * Set the badge this npc gives (Gym Leader only)
	 * @param badge
	 */
	public void setBadge(String badge) {
		m_badge = badge;
	}
	
	/**
	 * Returns the badge this npc gives if gym leader
	 * @return
	 */
	public String getBadge() {
		return m_badge;
	}
	
	/**
	 * Set the money this npc gives after battle
	 * @param money
	 */
	public void setMoney(String money) {
		try {
			m_money = Integer.parseInt(money);
		} catch (Exception e) {
			m_money = 0;
		}
	}
	
	/**
	 * Returns the money this npc gives after battle
	 * @return
	 */
	public int getMoney() {
		return m_money;
	}
}
