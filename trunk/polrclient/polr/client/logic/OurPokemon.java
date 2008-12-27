/*
 Pokemon Global. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright © 2007-2008 Pokemon Global Team

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

//Pokemon stats
public class OurPokemon extends TempPokemon {
	private Image backSprite;
	private int exp;
	private int atk;
	private int def;
	private int speed;
	private int spatk;
	private int spdef;
	private String ability;
	private String nature;
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public Image getBackSprite() {
		return backSprite;
	}
	//determines Pokemon sprite
	public void setBackSprite() {
		try{
			LoadingList.setDeferredLoading(true);
			String path = new String();
			String index, isShiny = new String();
			
			if (!isShiny()){
				isShiny = "normal/";
			}else{
				isShiny = "shiny/";
			}
			
			int pokeNum = setSpriteNumber(getSpecies().ordinal());
			
			if (pokeNum < 9) {
				index = "00" + String.valueOf(pokeNum);
			}
			else if (pokeNum < 99){
				index = "0" + String.valueOf(pokeNum);
			}
			else{
				index = String.valueOf(pokeNum);
			}
			int gender = getGender();
			if(gender > 1 || gender < 0) gender = 1;
			try {
				path = "res/sprites/back/normal/" + index + "-"
				+ gender + ".png";
				try{
					backSprite = new Image(path).getSubImage(0, 0, 80, 80);
					//System.out.println("Sprite Loading Succesfull");
					//System.out.println(path.toString());
				} catch (RuntimeException e){System.out.println("Sprite Loading Failed");
					System.out.println(path.toString());}
			}
			catch (Exception e) {
				e.printStackTrace();
				/*if (gender == 3) {
					path = "res/sprites/back/normal/" + index + "-0.png";
				}
				else {
					path = "res/sprites/back/normal/" + index + "-1.png";
				}
				backSprite = new Image(path.toString());
				backSprite = backSprite.getSubImage(0, 0, 80, 80);*/
			}
			LoadingList.setDeferredLoading(false);
		}catch (Exception e){e.printStackTrace();}
	}
	public int getAtk() {
		return atk;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getSpatk() {
		return spatk;
	}
	public void setSpatk(int spatk) {
		this.spatk = spatk;
	}
	public int getSpdef() {
		return spdef;
	}
	public void setSpdef(int spdef) {
		this.spdef = spdef;
	}
	public String getAbility() {
		return ability;
	}
	public void setAbility(String ability) {
		this.ability = ability;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
}
