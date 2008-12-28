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

import polr.server.battle.Pokemon;

public abstract class TrainerNonPlayerChar extends NonPlayerChar {
	private String [] pokeData;
	private Pokemon[] party;
	
	public TrainerNonPlayerChar(String name, Pokemon [] pokes) {
		this.setName(name);
		for(int i = 0; i < pokes.length; i++) {
			if(pokes[i] != null)
				this.getParty()[i] = new Pokemon(pokes[i]);
			else
				this.getParty()[i] = null;
		}
	}
	
	public TrainerNonPlayerChar(String name, String pokes) {
		this.setName(name);
		pokeData = pokes.split(";");
		setupPokemon();
	}
	
	public void setupPokemon() {
		party = new Pokemon[6];
		for(int i = 0; i < 6; i++) {
			if(i < pokeData.length) {
				if(pokeData[i] != null) {
					getParty()[i] = new Pokemon(generatePokemon(Integer.parseInt(pokeData[i].substring(0, pokeData[i].indexOf(","))), Integer.parseInt(pokeData[i].substring(pokeData[i].indexOf(",") + 1))));
				}
			}
			else {
				getParty()[i] = null;
			}
		}
	}
	
	@Override
	public Pokemon [] getParty() {
		return party;
	}
	
	@Override
	public int getHighestLevel() {
		int result = 0;
		for(int i = 0; i < party.length; i++) {
			if(party[i] != null) {
				if(party[i].getLevel() > result)
					result = party[i].getLevel();
			}
		}
		return result;
	}
	
	@Override
	protected void sendSpeech(String speech, PlayerChar target) {
		if(!target.getNpcList().contains(this.getName()) && !target.isBattling()) {
			target.getIoSession().write("T" + speech);
		}
		else {
			if(!target.isBattling())
				target.getIoSession().write("TYou cannot challenge the same Trainer more than once a day.");
		}
	}
	
	public String[] getPokeData() {
		return pokeData;
	}
	
	@Override
	public abstract void speakTo(PlayerChar target);
	
	public abstract void battleWon(PlayerChar target);
}
