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
	public enum NpcType { NORMAL, TRAINER, SHOP, HEALER, GYMLEADER, POKEMON }
	private Pokemon [] m_pokemon;
	private long m_index;
	private int m_speech;
	private NpcType m_type;
	private NpcAction m_firstAction;
	private NpcAction m_middleAction;
	private NpcAction m_lastAction;
	
	/**
	 * Default constructor, requires an NPC Type.
	 * @param n
	 */
	public NonPlayerChar(NpcType n) {
		m_type = n;
	}
	
	/**
	 * Sets pokemon party based on a string of information
	 * @param pokes
	 */
	public void setPokemonParty(String [] pokes) {
		m_pokemon = new Pokemon[6];
		for(int i = 0; i < 6; i++) {
			if(i < pokes.length) {
				if(pokes[i] != null) {
					getParty()[i] = new Pokemon(generatePokemon(
							Integer.parseInt(pokes[i].substring(0, pokes[i].indexOf(","))), 
							Integer.parseInt(pokes[i].substring(pokes[i].indexOf(",") + 1))));
				}
			}
			else {
				getParty()[i] = null;
			}
		}
	}
	
	/**
	 * Sets Pokemon party based on an array of Pokemon objects
	 * @param pokes
	 */
	public void setPokemonParty(Pokemon [] pokes) {
		m_pokemon = pokes;
	}
	
	/**
	 * Returns the number representation of speech.
	 * The number is sent to the client and its string is generated
	 * @return
	 */
	public int getSpeech() {
		return m_speech;
	}
	
	/**
	 * Sets the speech
	 * @param speechIndex
	 */
	public void setSpeech(int speechIndex) {
		m_speech = speechIndex;
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
		//If the NPC is a trainer, ensure the player hasn't battled them recently
		if(this.isTrainer()) {
			if(target.getNpcList().contains(this.getName())) {
				target.getIoSession().write("!1");
				return;
			}
		}
		//Execute this NPCs actions
		if(m_firstAction != null)
			m_firstAction.execute(target);
		if(m_middleAction != null)
			m_middleAction.execute(target);
	}
	
	/**
	 * Returns if the NPC has an action to be executed when interaction is finished
	 * @return
	 */
	public boolean hasLastAction() {
		return m_lastAction != null;
	}
	
	/**
	 * Called when the player is finished talking to the NPC.
	 * @param target
	 */
	public void finishTalkingTo(PlayerChar target) {
		if(m_lastAction != null)
			m_lastAction.execute(target);
	}
	
	/**
	 * Returns if the NPC is a trainer
	 * @return
	 */
	public boolean isTrainer() {
		return m_type == NpcType.TRAINER;
	}
	
	/**
	 * Get the NPC's Pokemon party.
	 */
	@Override
	public Pokemon [] getParty() {
		return m_pokemon;
	}
	
	/**
	 * Get the level of the highest level Pokemon in the NPC's party
	 */
	@Override
	public int getHighestLevel() {
		int result = 0;
		for(int i = 0; i < m_pokemon.length; i++) {
			if(m_pokemon[i] != null) {
				if(m_pokemon[i].getLevel() > result)
					result = m_pokemon[i].getLevel();
			}
		}
		return result;
	}
}
