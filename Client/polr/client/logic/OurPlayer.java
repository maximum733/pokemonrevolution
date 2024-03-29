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

package polr.client.logic;

import java.util.ArrayList;
import java.util.List;

import polr.client.GameClient;
import polr.client.engine.GameMap;

// loads the player data
public class OurPlayer extends Player {
	private OurPokemon[] m_pokemons;
	private List<Item> m_bag
		= new ArrayList<Item>();
	
	private int m_money;
	
	public int getMoney() {
		return m_money;
	}
	public void transfer (OurPlayer origin) {
		m_pokemons = origin.getTeam();
		m_bag = origin.getBag();
		m_money = origin.getMoney();
	}
	public List<Item> getBag() {
		return m_bag;
	}
	
	// using an item
	public void useItem(int id) {
		for(int i = 0; i < m_bag.size(); i++) {
			if(m_bag.get(i).getID() == id) {
				if(m_bag.get(i).getQuantity() == 1) {
					m_bag.remove(i);
				} else {
					m_bag.get(i).decreaseAmount();
				}
				break;
			}
		}
	}
	
	// adding an item
	public void addItem(int id) {
		Item item = null;
		for(int i = 0; i < m_bag.size(); i++) {
			if(m_bag.get(i).getID() == id) {
				item = m_bag.get(i);
				break;
			}
		}
		if(item == null) {
			item = new Item(id);
			m_bag.add(item);
		} else {
			item.increaseAmount();
		}
	}
	
	//gets the Pokemon team
	public OurPokemon[] getTeam() {
		return m_pokemons;
	}
	
	// sets Pokemon data
	public void initPokemon(String[] pdata) {
		if(m_pokemons == null)
			m_pokemons = new OurPokemon[6];
		int i = Integer.parseInt(pdata[0]);
		m_pokemons[i] = new OurPokemon();
		m_pokemons[i].setName(pdata[1]);
		m_pokemons[i].setSpecies(Enums.Pokenum.values()[Short
					.parseShort(pdata[1])]);
		m_pokemons[i].setCurHP(Integer.parseInt(pdata[3]));
		m_pokemons[i].setMaxHP(Integer.parseInt(pdata[4]));
		m_pokemons[i].setAtk(Integer.parseInt(pdata[5]));
		m_pokemons[i].setDef(Integer.parseInt(pdata[6]));
		m_pokemons[i].setSpeed(Integer.parseInt(pdata[7]));
		m_pokemons[i].setSpatk(Integer.parseInt(pdata[8]));
		m_pokemons[i].setSpdef(Integer.parseInt(pdata[9]));
		m_pokemons[i].setType1(Enums.Poketype.valueOf(pdata[10]));
		try {
			m_pokemons[i].setType1(Enums.Poketype.valueOf(pdata[11]));
		} catch (Exception e) {}
		m_pokemons[i].setLevel(Integer.parseInt(pdata[12]));
		m_pokemons[i].setExp((int) Double.parseDouble(pdata[12]));
		m_pokemons[i].setNature(pdata[14]);
		m_pokemons[i].setGender(Integer.valueOf(pdata[15]));
		m_pokemons[i].getMoves()[0] = pdata[16];
		m_pokemons[i].getMovemaxPP()[0] = Integer.valueOf(pdata[17]);
		m_pokemons[i].getMoves()[1] = pdata[18];
		m_pokemons[i].getMovemaxPP()[1] = Integer.valueOf(pdata[19]);
		m_pokemons[i].getMoves()[2] = pdata[20];
		m_pokemons[i].getMovemaxPP()[2] = Integer.valueOf(pdata[21]);
		m_pokemons[i].getMoves()[3] = pdata[22];
		m_pokemons[i].getMovemaxPP()[3] = Integer.valueOf(pdata[23]);
		m_pokemons[i].setAbility(pdata[24]);
		m_pokemons[i].setSprite();
		m_pokemons[i].setIcon();
		m_pokemons[i].setBackSprite();
	}
	
	public void setMap(GameMap currentMap) {
		this.map = currentMap;
		this.map.setXOffset(GameClient.width / 2 - this.x);
		this.map.setYOffset(GameClient.height / 2 - this.y);
	}
}
