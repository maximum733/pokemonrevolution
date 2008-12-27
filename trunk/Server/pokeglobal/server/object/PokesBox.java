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

package pokeglobal.server.object;

import org.simpleframework.xml.ElementArray;

import pokeglobal.server.battle.Pokemon;

public class PokesBox {
	@ElementArray
	private Pokemon[] box;
	
	public PokesBox(){
		box = new Pokemon[30];
		
	}
	
	public Pokemon[] getPokes(){
		return box;
	}
	
	public void setPoke(int index, Pokemon poke){
		box[index] = poke;
	}
}