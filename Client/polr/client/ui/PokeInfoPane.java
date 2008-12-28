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

package polr.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.loading.LoadingList;

import polr.client.logic.OurPokemon;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.window.MainInterface;
/*
 * This displays more detailed information on a single pokemon.
 * Currently only called by the PartyInfo dialog, but should be called by storage/boxes for easier viewing of boxed pokemon.
 */
public class PokeInfoPane extends Frame{
	private Label icon = new Label();
	private Label data[] = new Label[14];
	private Label labels[] = new Label[14];
	
	public PokeInfoPane(OurPokemon poke, MainInterface gui){
		gui.hide();
		loadImage(poke);
		initGUI(poke);
	}
	
	public PokeInfoPane(OurPokemon poke){
		loadImage(poke);
		initGUI(poke);
	}
	
	public void loadImage(OurPokemon poke){
		LoadingList.setDeferredLoading(true);
		poke.setSprite();
		poke.resizeSprite();
		icon.setImage(poke.getSprite());
		icon.setSize(60,60);
		icon.setLocation(15, 5);
		this.add(icon);
		LoadingList.setDeferredLoading(false);
	}
	
	public void initGUI(OurPokemon poke){
		this.setBackground(new Color(255,255,255,200));
		int x = 90;
		int y = 5;
		for (int i = 0; i < 14; i++){
			data[i] = new Label();
			labels[i] = new Label();
			data[i].setX(x + 90);
			data[i].setY(y);
			labels[i].setX(x);
			labels[i].setY(y);
			labels[i].setHorizontalAlignment(Label.RIGHT_ALIGNMENT);
			y += 20;
			getContentPane().add(labels[i]);
			getContentPane().add(data[i]);
		}
		labels[0].setText("Level:");
		labels[1].setText("Name:");
		labels[2].setText("HP:");
		labels[3].setText("Attack:");
		labels[4].setText("Defense:");
		labels[5].setText("Sp. Attack:");
		labels[6].setText("Sp. Defense:");
		labels[7].setText("Speed:");
		labels[8].setText("Ability:");
		labels[9].setText("Exp:");
		labels[10].setText("Nature:");
		labels[11].setText("Type one:");
		labels[12].setText("Type two:");
		labels[13].setText("Gender:");
		//labels[13].setText("Exp to next level:");
		data[0].setText(String.valueOf(poke.getLevel()));
		data[1].setText(poke.getName());
		data[2].setText(String.valueOf(poke.getCurHP()) + "/" 
				+ String.valueOf(poke.getMaxHP()));
		data[3].setText(String.valueOf(poke.getAtk()));
		data[4].setText(String.valueOf(poke.getDef()));
		data[5].setText(String.valueOf(poke.getSpatk()));
		data[6].setText(String.valueOf(poke.getSpdef()));
		data[7].setText(String.valueOf(poke.getSpeed()));
		data[8].setText(poke.getAbility());
		data[9].setText(String.valueOf(poke.getExp()));
		data[10].setText(poke.getNature());
		
		data[11].setText(String.valueOf(poke.getType1()));
		if(poke.getType2() == null){
			data[12].setText("");
		}else{
			data[12].setText(String.valueOf(poke.getType2()));
		}
		if(poke.getGender() == 1){
			data[13].setText("Male");
		}else{
			data[13].setText("Female");
		}
		
		for (int i = 0; i < data.length; i++) {
			data[i].pack();
		}
		for (int i = 0; i < labels.length; i++) {
			labels[i].pack();
		}
		loadImage(poke);
		setVisible(true);
		setSize(270, 310);
		setResizable(false);
		setTitle(poke.getName());
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
