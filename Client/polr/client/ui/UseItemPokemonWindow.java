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

import polr.client.GameClient;
import polr.client.logic.Item;
import polr.client.logic.OurPokemon;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
/*
 * This UI is for the selection of a pokemon when applying an item to a pokemon.
 * Used in potions, TM's, evostones, etc.
 */
public class UseItemPokemonWindow extends Frame {
	private Button [] select;
	private Label [] label;
	private OurPokemon [] pokes;
	
	public void openMoveSelect(String packet, int index) {
		UseItemMoveWindow moveSelect;
		for(int i = 0; i < this.getParent().getDisplay().getChildren().length; i++) {
			try {
				if(this.getDisplay().getChildren()[i].getName().equalsIgnoreCase("Move Selector")) {
					moveSelect = (UseItemMoveWindow) this.getDisplay().getChildren()[i];
					this.getDisplay().remove(moveSelect);
					break;
				}
			} catch(Exception e) {
				moveSelect = null;
			}
		}
		this.getDisplay().add(new UseItemMoveWindow(packet, pokes[index]));
	}
	
	public void close() {
		this.getCloseButton().press();
	}
	
	public UseItemPokemonWindow(final int itemID, boolean moveSelect, OurPokemon [] pokes) {
		super();
		this.setName("Pokemon Selector");
		this.setTitle(Item.getName(itemID) + "-Select a Pokemon");
		this.setSize(256, 224);
		this.setLocation(272, 198);
		this.setBackground(new Color(0, 0, 0, 85));
		this.pokes = pokes;
		this.setResizable(false);
		
		select = new Button[6];
		label = new Label[6];
		for(int i = 0; i < pokes.length; i++) {
			if(pokes[i] != null) {
				label[i] = new Label(pokes[i].getName());
				label[i].setForeground(new Color(255, 255, 255));
				label[i].setFont(GameClient.getDPFontSmall());
				label[i].setSize(196, 32);
				label[i].setLocation(4, (32 * i) + 4);
				label[i].setVisible(true);
				this.add(label[i]);
				
				select[i] = new Button();
				select[i].setText("Select");
				select[i].setSize(64, 32);
				select[i].setLocation(192, (32 * i) + 4);
				select[i].setVisible(true);
				if(moveSelect) {
					switch(i) {
					case 0:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openMoveSelect("Bu" + itemID + "," + 0 + ",", 0);
								close();
							}
						});
						break;
					case 1:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openMoveSelect("Bu" + itemID + "," + 1 + ",", 1);
								close();
							}
						});
						break;
					case 2:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openMoveSelect("Bu" + itemID + "," + 2 + ",", 2);
								close();
							}
						});
						break;
					case 3:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openMoveSelect("Bu" + itemID + "," + 3 + ",", 3);
								close();
							}
						});
						break;
					case 4:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openMoveSelect("Bu" + itemID + "," + 4 + ",", 4);
								close();
							}
						});
						break;
					case 5:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openMoveSelect("Bu" + itemID + "," + 5 + ",", 5);
								close();
							}
						});
						break;
					}
				} else {
					switch(i) {
					case 0:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								GameClient.getPacketGenerator().write("Bu" + itemID + "," + 0);
								close();
							}
						});
						break;
					case 1:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								GameClient.getPacketGenerator().write("Bu" + itemID + "," + 1);
								close();
							}
						});
						break;
					case 2:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								GameClient.getPacketGenerator().write("Bu" + itemID + "," + 2);
								close();
							}
						});
						break;
					case 3:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								GameClient.getPacketGenerator().write("Bu" + itemID + "," + 3);
								close();
							}
						});
						break;
					case 4:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								GameClient.getPacketGenerator().write("Bu" + itemID + "," + 4);
								close();
							}
						});
						break;
					case 5:
						select[i].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								GameClient.getPacketGenerator().write("Bu" + itemID + "," + 5);
								close();
							}
						});
						break;
					}
				}
				this.add(select[i]);
			} else {
				select[i] = new Button();
				select[i].setText("Select");
				select[i].setSize(64, 32);
				select[i].setLocation(192, (32 * i) + 4);
				select[i].setEnabled(false);
				select[i].setVisible(true);
				this.add(select[i]);
			}
		}
	}
}
