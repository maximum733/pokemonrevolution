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

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

public class TempPokemon {
	static final long serialVersionUID = 1;
	
	//load sprite and icon
	private Image sprite;
	private Image icon;
	
	//load trainer data
	private int trainerID;
	
	// 0 = none, 1 = male, 2 = female
	private int gender;
	//name and species data
	private String name;
	private String nick;
	private Enums.Pokenum species;
	private boolean shiny;
	
	//level and types
	private int level;
	private Enums.Poketype type1, type2;
	
	//moves and pp
	private String[] moves = new String[4];
	private int[] movemaxPP = new int[4];
	private int[] movecurPP = new int[4];
	
	//stats
	private int maxHP, curHP;
	
	//status (effects)
	private String effect = "normal";
	
	public Image getSprite() {
		return sprite;
	}
	
	//sets the Pokemon's sprite
	public void setSprite() {
		try{
			LoadingList.setDeferredLoading(true);
			String path = new String();
			String index, isShiny = new String();
			
			if (!isShiny()){
				isShiny = "normal/";
			}else{
				isShiny = "shiny/";
			}
			
			int pokeNum = setSpriteNumber(this.species.ordinal());
			
			if (pokeNum < 10) {
				index = "00" + String.valueOf(pokeNum);
			}
			else if (pokeNum < 100){
				index = "0" + String.valueOf(pokeNum);
			}
			else{
				index = String.valueOf(pokeNum);
			}
			
			int pathGender = 0;
			switch (gender){
				case 0:
					pathGender = 3;
					break;
				case 1:
					pathGender = 3;
					break;
				case 2:
					pathGender = 2;
					break;
			}
			if (pathGender > 3) pathGender = 3;
			try {
				path = "res/sprites/front/normal/" + index + "-"
				+ pathGender + ".png";
				try{
					sprite = new Image(path.toString())/*.getSubImage(0, 0, 60, 60)*/;
				} catch (RuntimeException e){System.out.println("Sprite Loading Failed");
					System.out.println(path.toString()); e.printStackTrace();}
			}
			catch (Exception e) {
				if(pathGender == 3)
					pathGender = 2;
				else
					pathGender = 3;
				path = "res/sprites/front/normal/" + index + "-"
				+ pathGender + ".png";
				System.out.println(path);
				Image poke = new Image(path.toString());
				sprite = new Image(path.toString());
				e.printStackTrace();
			}
			LoadingList.setDeferredLoading(false);
		}catch (SlickException e){e.printStackTrace();}
	}
	public void resizeSprite(){
		sprite = sprite.getSubImage(0, 0, 80, 80);
	}
	public Image getIcon() {
		return icon;
	}
	public void setIcon() {
		try{
			LoadingList.setDeferredLoading(true);
			String path = new String();
			String index = new String();
			int pokeNum = setSpriteNumber(this.species.ordinal());
			
			if (pokeNum < 10) {
				index = "00" + String.valueOf(pokeNum);
			}
			else if (pokeNum < 100){
				index = "0" + String.valueOf(pokeNum);
			}
			else{
				index = String.valueOf(pokeNum);
			}
			
			path = "res/sprites/icons/" + index + ".gif";
				
			icon = new Image(path.toString());
			LoadingList.setDeferredLoading(false);
		}catch (SlickException e){e.printStackTrace();}
	}	
	public int getTrainerID() {
		return trainerID;
	}
	public void setTrainerID(int trainerID) {
		this.trainerID = trainerID;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public Enums.Pokenum getSpecies() {
		return species;
	}
	public void setSpecies(Enums.Pokenum species) {
		this.species = species;
	}
	public boolean isShiny() {
		return shiny;
	}
	public void setShiny(boolean shiny) {
		this.shiny = shiny;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Enums.Poketype getType1() {
		return type1;
	}
	public void setType1(Enums.Poketype type1) {
		this.type1 = type1;
	}
	public Enums.Poketype getType2() {
		return type2;
	}
	public void setType2(Enums.Poketype type2) {
		this.type2 = type2;
	}
	public String[] getMoves() {
		return moves;
	}
	public void setMoves(String[] moves) {
		this.moves = moves;
	}
	public int[] getMovemaxPP() {
		return movemaxPP;
	}
	public void setMovemaxPP(int[] movemaxPP) {
		this.movemaxPP = movemaxPP;
	}
	public int[] getMovecurPP() {
		return movecurPP;
	}
	public void setMovecurPP(int[] movecurPP) {
		this.movecurPP = movecurPP;
	}
	public void setMovecurPP(int move, int pp) {
		this.movecurPP[move] = pp;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}
	public int getCurHP() {
		return curHP;
	}
	public void setCurHP(int curHP) {
		this.curHP = curHP;
	}
	
	public void setEffect(String eff){
		effect = eff;
	}
	
	public String getEffect() {
		return effect;
	}
	public int setSpriteNumber(int x) {
		int i = 0;
		if (x <= 385) {
			i = x + 1;
		} else if (x <= 388) {
			i = 386;
		} else if (x <= 414) {
			i = x - 2;
		} else if (x <= 416) {
			i = 413;
		} else {
			i = x - 4;
		}
		return i;
	}
}
