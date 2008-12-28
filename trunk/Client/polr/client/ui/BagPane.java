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

import java.util.List;

import org.newdawn.slick.Color;

import polr.client.GameClient;
import polr.client.logic.Item;
import polr.client.logic.OurPokemon;
import polr.client.ui.base.Button;
import polr.client.ui.base.Container;
import polr.client.ui.base.Label;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
/*
 * This is the BagWindow, where items are displayed to the player
 */
public class BagPane extends Container {
	private GameClient thisGame;
	private Button [] m_use;
	private Button [] m_drop;
	private Label [] m_label;
	private Color foreground;
	private boolean isSecondPage;
	private OurPokemon [] pokes;
	
	public BagPane(GameClient game) {
		super();
		thisGame = game;
		m_use = new Button[14];
		m_label = new Label[14];
		m_drop = new Button[14];
		foreground = new Color(255, 255, 255);
		isSecondPage = false;
		this.setSize(224, 256);
	}
	
	public void updatePokemon(OurPokemon [] pokes) {
		this.pokes = pokes;
	}
	
	public void updateItems(List<Item> m_items) {
		this.removeAll();
		for(int i = 0; i < 7 && i < m_items.size(); i++) {
			m_label[i] = new Label(m_items.get(i).getName());
			m_label[i].setForeground(foreground);
			m_label[i].setSize(120, 24);
			m_label[i].setLocation(2, (32 * i) + 4);
			m_label[i].setVisible(true);
			this.add(m_label[i]);
			
			m_use[i] = new Button();
			m_use[i].setText("" + m_items.get(i).getQuantity());
			m_use[i].setSize(32, 24);
			m_use[i].setLocation(128, (32 * i) + 4);
			m_use[i].setVisible(true);
			final int itemNo = m_items.get(i).getID();
			if(m_items.get(i).pokeSelect()) {
				if(m_items.get(i).moveSlotSelect()) {
					m_use[i].addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							openPokemonSelectDialog(true, itemNo);
						}
					});
				} else {
					m_use[i].addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							openPokemonSelectDialog(false, itemNo);
						}
					});
				}
			} else {
				m_use[i].addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						try{
							System.out.println("You Can't Move");
							thisGame.getBattle().waitForTurn();
						} catch (NullPointerException s){s.printStackTrace();}
						GameClient.getPacketGenerator().write("Bu" + itemNo);
					}
				});
			}
			this.add(m_use[i]);
			
			m_drop[i] = new Button();
			m_drop[i].setText("Drop");
			m_drop[i].setSize(48, 24);
			m_drop[i].setLocation(160, (32 * i) + 4);
			m_drop[i].setVisible(true);
			m_drop[i].addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					GameClient.getPacketGenerator().write("Bd" + itemNo);
				}
			});
			this.add(m_drop[i]);
		}
	}
	
	public void openPokemonSelectDialog(boolean moveSelect, int num) {
		//Ensure a dialog isn't already open
		UseItemPokemonWindow pokeSelect;
		for(int i = 0; i < this.getParent().getDisplay().getChildren().length; i++) {
			try {
				if(this.getDisplay().getChildren()[i].getName().equalsIgnoreCase("Pokemon Selector")) {
					pokeSelect = (UseItemPokemonWindow) this.getDisplay().getChildren()[i];
					this.getDisplay().remove(pokeSelect);
					break;
				}
			} catch(Exception e) {
				pokeSelect = null;
			}
		}
		this.getDisplay().add(new UseItemPokemonWindow(num, moveSelect, pokes));
	}

	public void setPokemon(OurPokemon[] pokemon) {
		pokes = pokemon;
	}
	
}
