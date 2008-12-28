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
import polr.client.logic.OurPokemon;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
/*
 * This UI is primarily used for TM's, where an item must be applied to a particular moveslot in addition to a particular pokemon.
 */
public class UseItemMoveWindow extends Frame {
	private Button [] moves;
	
	public UseItemMoveWindow(final String packet, OurPokemon poke) {
		moves = new Button[4];
		
		this.setSize(256, 320);
		this.setLocation(274, 140);
		this.setTitle("Select a move to replace...");
		this.setResizable(false);
		this.setBackground(new Color(0, 0, 0, 85));
		this.setName("Move Selector");
		
		for(int i = 0; i < poke.getMoves().length; i++) {
			if(poke.getMoves()[i] != null && !poke.getMoves()[i].equalsIgnoreCase("null")) {
				moves[i] = new Button(poke.getMoves()[i]);
				moves[i].setSize(196, 64);
				moves[i].setLocation(32, (64 * i) + 16);
				switch(i) {
				case 0:
					moves[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							GameClient.getPacketGenerator().write(packet + 0);
							close();
						}
					});
					break;
				case 1:
					moves[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							GameClient.getPacketGenerator().write(packet + 1);
							close();
						}
					});
					break;
				case 2:
					moves[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							GameClient.getPacketGenerator().write(packet + 2);
							close();
						}
					});
					break;
				case 3:
					moves[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							GameClient.getPacketGenerator().write(packet + 3);
							close();
						}
					});
					break;
				}
				moves[i].setVisible(true);
				this.add(moves[i]);
			} else {
				moves[i] = new Button("EMPTY");
				moves[i].setSize(196, 64);
				moves[i].setLocation(32, (64 * i) + 16);
				moves[i].setVisible(true);
				this.add(moves[i]);
			}
		}
	}
	
	public void close() {
		this.getCloseButton().press();
	}

}
