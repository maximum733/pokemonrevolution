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

package polr.server.npc;

import polr.server.GameServer;
import polr.server.battle.Pokemon;
import polr.server.map.ServerMap.Directions;
import polr.server.object.Char;
import polr.server.player.PlayerChar;

/**
 * Handles Non Playable Characters
 * @author TMKCodes
 * @author shinobi
 *
 */
public class NonPlayerChar extends Char {
	public enum NpcType { NORMAL, TRAINER, SHOP, HEALER, GYMLEADER, POKEMON, QUEST }
	private String [] m_possiblePokemon = new String[6];
	private int m_minimalLevel = 1;
	private long m_index;
	private String m_speech = "";
	private String m_questSpeech;
	private String m_endSpeech = "";
	private String m_badge;
	private NpcType m_type;
	private int m_questId = 0;
	private int m_badgeReq = 0;
	private int m_itemReq = 0;
	private int m_pokeReq = 0;
	private int m_questReq = 0;
	private int m_pokeLevelReq = 0;
	private boolean m_endsQuest = false;
	private boolean m_startsQuest = false;
	private int m_money = 0;
	
	/**
	 * Default constructor, requires an NPC Type.
	 * @param n
	 */
	public NonPlayerChar(NpcType n) {
		m_type = n;
	}
	
	/**
	 * Set if this npc ends a quest
	 * @param b
	 */
	public void setEndsQuest(boolean b) {
		m_endsQuest = b;
	}
	
	/**
	 * Set if this npc starts a quest
	 * @param b
	 */
	public void setStartsQuest(boolean b) {
		m_startsQuest = b;
	}
	
	/**
	 * Sets the Pokemon Level Requirement of this npc
	 * @param i
	 */
	public void setPokemonLevelRequirement(int i) {
		m_pokeLevelReq = i;
	}
	
	/**
	 * Set the quest requirement of this npc
	 * @param i
	 */
	public void setQuestRequirement(int i) {
		m_questReq = i;
	}
	
	/**
	 * Set the pokemon requirement of this npc
	 * @param i
	 */
	public void setPokemonRequirement(int i) {
		m_pokeReq = i;
	}
	
	/**
	 * Set the item requirement of this npc
	 * @param i
	 */
	public void setItemRequirement(int i) {
		m_itemReq = i;
	}
	
	/**
	 * Sets the badge requirement
	 * @param i
	 */
	public void setBadgeRequirement(int i) {
		m_badgeReq = i;
	}
	
	/**
	 * Sets the quest if of this npc
	 * @param i
	 */
	public void setQuestId(int i) {
		m_questId = i;
	}
	
	/**
	 * Set the potential pokemon of this npc
	 * @param pokes
	 */
	public void setPotentialParty(String [] pokes) {
		m_possiblePokemon = pokes;
	}
	
	/**
	 * Returns a unique party based on a string of information
	 * The highest level of a player should be passed
	 * @param pokes
	 */
	public Pokemon [] generatePokemonParty(int level) {
		Pokemon [] m_pokemon = new Pokemon[6];
		for(int i = 0; i < 6; i++) {
			if(i < m_possiblePokemon.length) {
				if(m_possiblePokemon[i] != null) {
					int l = m_minimalLevel;
					if(level > m_minimalLevel + 3) {
						l = GameServer.getMechanics().getRandom().nextInt(3) + level;
					} else {
						l = GameServer.getMechanics().getRandom().nextInt(3) + m_minimalLevel;
					}
					m_pokemon[i] = new Pokemon(generatePokemon(
							Integer.parseInt(m_possiblePokemon[i].substring(0, m_possiblePokemon[i].indexOf(","))), l));
				}
			}
			else {
				m_pokemon[i] = null;
			}
		}
		return m_pokemon;
	}
	
	/**
	 * Returns the number representation of speech.
	 * The number is sent to the client and its string is generated
	 * @return
	 */
	public String getSpeech() {
		return m_speech;
	}
	
	/**
	 * Sets the speech
	 * @param speechIndex
	 */
	public void setSpeech(String speech) {
		m_speech = speech;
	}
	
	/**
	 * Sets the quest speech
	 * @param speech
	 */
	public void setQuestSpeech(String speech) {
		m_questSpeech = speech;
	}
	
	/**
	 * Returns the quest speech of this NPC
	 * @return
	 */
	public String getQuestSpeech() {
		return m_questSpeech;
	}
	
	/**
	 * Sets the index of the NPC on the map
	 * @param i
	 */
	public void setIndex(long i) {
		m_index = i;
	}
	
	/**
	 * Returns the index of the NPC on the map
	 * @return
	 */
	public long getIndex() {
		return m_index;
	}
	
	/**
	 * Set the badge this npc rewards, if any
	 * @param badge
	 */
	public void setBadge(String badge) {
		m_badge = badge;
	}
	
	/**
	 * Return the badge this npc rewards
	 * @return
	 */
	public String getBadge() {
		return m_badge;
	}
	
	/**
	 * Set how much money this NPC rewards when battled.
	 * @param money
	 */
	public void setMoney(int money) {
		m_money = money;
	}
	
	/**
	 * Return how much money this NPC rewards when battled.
	 * @return
	 */
	public int getMoney() {
		return m_money;
	}
	
	/**
	 * Set the speech this NPC says at the end of battles.
	 * @param speech
	 */
	public void setEndSpeech(String speech) {
		m_endSpeech = speech;
	}
	
	/**
	 * Return the speech this NPC says at the end of battles.
	 * @return
	 */
	public String getEndSpeech() {
		return m_endSpeech;
	}
	
	/**
	 * If this npc was battled, this is called when the battle is finished.
	 * @param p
	 */
	public void endBattle(PlayerChar p) {
		switch(m_type) {
		case TRAINER:
			if(m_money > 0)
				p.setMoney(p.getMoney() + m_money);
			if(m_endSpeech != null && !m_endSpeech.equalsIgnoreCase(""))
				p.getIoSession().write("c" + m_endSpeech);
			break;
		case GYMLEADER:
			if(m_badge != null && !m_badge.equalsIgnoreCase(""))
				p.addBadge(this.getBadge());
			if(m_money > 0)
				p.setMoney(p.getMoney() + m_money);
			if(m_endSpeech != null && !m_endSpeech.equalsIgnoreCase(""))
				p.getIoSession().write("c" + m_endSpeech);
			break;
		default:
		}
	}
	
	/**
	 * Moves the NPC
	 */
	public boolean move(Directions dir) {
		if(facing != dir) {
			switch (dir) {
			case up:
				facing = Directions.up;
				getMap().sendToAll("CU" + m_index);
				return true;
			case down:
				facing = Directions.down;
				getMap().sendToAll("CD" + m_index);
				return true;
			case left:
				facing = Directions.left;
				getMap().sendToAll("CL" + m_index);
				return true;
			case right:
				facing = Directions.right;
				getMap().sendToAll("CR" + m_index);
				return true;
			}
			return true;
		} else if (getMap().canMove(dir, this)) {
			switch (dir) {
			case up:
				y -= 32;
				facing = Directions.up;
				getMap().sendToAll("U" + m_index);
				return true;
			case down:
				y += 32;
				facing = Directions.down;
				getMap().sendToAll("D" + m_index);
				return true;
			case left:
				x -= 32;
				facing = Directions.left;
				getMap().sendToAll("L" + m_index);
				return true;
			case right:
				x += 32;
				facing = Directions.right;
				getMap().sendToAll("R" + m_index);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Speak to the player. Can execute up to two NPC actions.
	 * @param target
	 */
	public void speakTo(PlayerChar target) {
		switch(m_type) {
		case NORMAL:
			if(m_speech != null && !m_speech.equalsIgnoreCase(""))
				target.getIoSession().write("c" + m_speech);
			break;
		case TRAINER:
			if(!target.getNpcList().contains(this.getName()))
				target.startTrainerBattle(this);
			else
				target.getIoSession().write("!1");
			break;
		case SHOP:
			target.getIoSession().write("cs");
			break;
		case HEALER:
			if(m_speech != null && !m_speech.equalsIgnoreCase(""))
				target.getIoSession().write("c" + m_speech);
			target.healParty();
			break;
		case GYMLEADER:
			break;
		case POKEMON:
			break;
		case QUEST:
			if(!target.hasCompletedQuest(m_questId)) {
				if(m_questSpeech != null && !m_questSpeech.equalsIgnoreCase("") &&
						target.getBadgeCount() >= m_badgeReq &&
						target.getHighestLevel() >= m_pokeLevelReq &&
						(m_itemReq > 0 && target.getBag().hasItem(m_itemReq)) &&
						(m_questReq > 0 && target.hasCompletedQuest(m_questReq))) {
					if(target.getQuestId() == m_questId) {
						//The player is on this quest
						if(m_endsQuest) {
							//End the quest this NPC ends the quest
							target.endQuest();
						}
						//Send quest speech
						target.getIoSession().write("c" + m_questSpeech);
					} else {
						if(m_startsQuest) {
							//Start the quest if this NPC starts the quest
							target.setQuestId(m_questId);
							target.getIoSession().write("c" + m_questSpeech);
						} else {
							//The player is not on this quest
							target.getIoSession().write("c" + m_speech);
						}
					}
				} else {
					//The player does not meet the minimum requirements to do this quest
					target.getIoSession().write("c" + m_speech);
				}
			} else {
				//Quest was completed
				target.getIoSession().write("!3");
			}
			break;
		}
	}
	
	/**
	 * Returns if the NPC is a trainer
	 * @return
	 */
	public boolean isTrainer() {
		return m_type == NpcType.TRAINER;
	}

	/**
	 * Get the level of the highest possible level Pokemon in the NPC's party
	 */
	public int getHighestLevel() {
		return m_minimalLevel;
	}
	
	/**
	 * Sets the minimal level of Pokemon in this NPCs potential party
	 * @param l
	 */
	public void setMinimalLevel(int l) {
		m_minimalLevel = l;
	}
}
