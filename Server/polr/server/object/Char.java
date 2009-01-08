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

package polr.server.object;

import java.util.ArrayList;
import java.util.Random;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import polr.server.GameServer;
import polr.server.battle.BattleField;
import polr.server.battle.Pokemon;
import polr.server.battle.PokemonSpecies;
import polr.server.database.POLRDatabase;
import polr.server.map.ServerMap;
import polr.server.map.ServerMap.Directions;
import polr.server.mechanics.PokemonNature;
import polr.server.mechanics.moves.MoveList;
import polr.server.mechanics.moves.MoveListEntry;

@Root
public abstract class Char implements Battleable, Positionable {
	@Element
	protected int x = 128;
	@Element
	protected int y = 408;

	@Attribute
	private String name;
	@Element
	private int sprite = 0;
	@Element
	protected Directions facing = Directions.down;
	
	@ElementArray
	private Pokemon[] party;

	@Element
	private boolean visible;

	private int battleID = -1;
	private Battleable opponent;
	private BattleField field;

	private ServerMap map;
	@Element
	private int mapX;
	@Element
	private int mapY;
	
	@Element(required=false)
	private boolean m_isSurfing = false;
	
	public boolean isSurfing() {
		return m_isSurfing;
	}
	
	public void setSurfing(boolean b) {
		m_isSurfing = b;
	}
	
	private Random random = new Random();

	public void endBattle() {
		if (field != null)
			field.clearQueue();
		battleID = -1;
		opponent = null;
		field = null;
		for (Pokemon pokemon : party) {
			if (pokemon != null) {
				pokemon.removeStatus(0);
			}
		}
	}
	public boolean isBattling() {
		return field != null;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	public int getSprite() {
		return sprite;
	}

	public Pokemon[] getParty() {
		return party;
	}
	
	public int getHighestLevel() {
		int result = 0;
		for(Pokemon p: getParty()) {
			if(p != null) {
				if(p.getLevel() > result)
					result = p.getLevel();
			}
		}
		return result;
	}
	public POLRDatabase getPOLRdb() {
		return GameServer.getPOLRDB();
	}
	public boolean isFacing(Char c) {
		if (c.getMap() == getMap()) {
			switch (getFacing()) {
			case up:
				if (getX() == c.getX() && 
						getY() - 32 == c.getY()) {
					return true;
				}
				break;
			case down:
				if (getX() == c.getX() && 
						getY() + 32 == c.getY()) {
					return true;
				}
				break;
			case left:
				if (getX() - 32 == c.getX() && 
						getY() == c.getY()) {
					return true;
				}
				break;
			case right:
				if (getX() + 32 == c.getX() && 
						getY() == c.getY()) {
					return true;
				}
			}
		}
		return false;
	}
	public int getBattleID() {
		return battleID;
	}
	
	public void removePokemon(Pokemon p) {
		for(int i = 0; i < this.getParty().length; i++) {
			if(getParty()[i] != null) {
				if(getParty()[i] == p) {
					getParty()[i] = null;
					return;
				}
			}
		}
	}

	public Battleable getOpponent() {
		return opponent;
	}

	public BattleField getField() {
		return field;
	}

	public ServerMap getMap() {
		return map;
	}

	public abstract boolean move(Directions dir);

	public void setName(String name) {
		this.name = name;
	}

	public void setSprite(int sprite) {
		this.sprite = sprite;
	}

	public void setParty(Pokemon[] team) {
		this.party = team;
	}
	public void setX(int newX) {
		x = newX;
	}
	public void setY(int newY) {
		y = newY;
	}
	public void setBattleID(int battleID) {
		this.battleID = battleID;
	}

	public void setOpponent(Battleable opponent) {
		this.opponent = opponent;
	}

	public void setField(BattleField field) {
		this.field = field;
	}

	public int getMapX() {
		return mapX;
	}
	public int getMapY() {
		return mapY;
	}
	public void setMap(ServerMap map) {
		this.map = map;
		this.mapX = map.getX();
		this.mapY = map.getY();
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isVisible() {
		return visible;
	}

	public Directions getFacing() {
		return facing;
	}

	public Pokemon generatePokemon(int speciesIndex, int level) {
		PokemonSpecies species = GameServer.getSpeciesData().getSpecies(speciesIndex);
		MoveListEntry[] moves = new MoveListEntry[4];
		ArrayList<MoveListEntry> possibleMoves = new ArrayList<MoveListEntry>();
		MoveList moveList = MoveList.getDefaultData();
		for (int i = 0; i < GameServer.getPOLRDB().getPokemonData(speciesIndex)
				.getStarterMoves().size(); i++) {
			possibleMoves.add(moveList.getMove(GameServer.getPOLRDB().getPokemonData(
					speciesIndex).getStarterMoves().get(i)));
		}
		for (int i = 1; i <= level; i++) {
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
		Pokemon trainerPokemon = new Pokemon(
				GameServer.getMechanics(),
				species,
				PokemonNature.getNature(random.nextInt(PokemonNature
						.getNatureNames().length)),
						species.getPossibleAbilities(species.getDefaultData())[random
						      .nextInt(species.getPossibleAbilities(species.getDefaultData()).length)],
				null, Pokemon.generateGender(species.getPossibleGenders()), level, new int[]{
						random.nextInt(32), // IVs
						random.nextInt(32), random.nextInt(32),
						random.nextInt(32), random.nextInt(32),
						random.nextInt(32) }, new int[] { 0, 0, 0, 0, 0, 0 }, // EVs
				moves, new int[] { 0, 0, 0, 0 });
		trainerPokemon.setBaseExp(GameServer.getPOLRDB().getPokemonData(speciesIndex)
				.getBaseEXP());
		trainerPokemon.setExpType(GameServer.getPOLRDB().getPokemonData(speciesIndex)
				.getGrowthRate());
		trainerPokemon.setExp(GameServer.getMechanics().getExpForLevel(trainerPokemon, level));
		trainerPokemon.setHappiness(GameServer.getPOLRDB().getPokemonData(speciesIndex).getHappiness());
		return trainerPokemon;
	}
}
