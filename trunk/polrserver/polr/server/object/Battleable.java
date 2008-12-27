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

import org.simpleframework.xml.Root;

import polr.server.battle.BattleField;
import polr.server.battle.Pokemon;
import polr.server.battle.PokemonSpeciesData;
import polr.server.database.POLRDatabase;
import polr.server.mechanics.BattleMechanics;

@Root
public interface Battleable {
	public boolean isBattling();

	public String getName();
	public Pokemon[] getParty();
	public int getBattleID();
	
	public Battleable getOpponent();
	public BattleField getField();
	
	public void setName(String name);
	public void setParty(Pokemon[] team);
	public void setBattleID(int battleID);

	public void setOpponent(Battleable opponent);

	public void setField(BattleField field);
}
