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
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import polr.client.GameClient;
import polr.client.logic.OurPokemon;
import polr.client.network.PacketGenerator;
import polr.client.ui.base.Button;
import polr.client.ui.base.Container;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.ProgressBar;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
import polr.client.ui.base.event.MouseAdapter;
import polr.client.ui.base.event.MouseEvent;
import polr.client.ui.base.skin.simple.SimpleArrowButton;
/*
 * Displays information on pokemon currently in your party.
 */
public class PartyInfo extends Frame {
	Container[] pokes = new Container[6];
	Label[] pokeBall = new Label[6];
	Label[] pokeIcon = new Label[6];
	Label[] pokeName = new Label[6];
	Label[] level = new Label[6];
	ProgressBar[] hp = new ProgressBar[6];
	Button[] switchUp = new Button[6];
	Button[] switchDown = new Button[6];
	Font dpFont;
	
	OurPokemon[] m_pokes;
	
	private PacketGenerator packetGen;
	
	public PartyInfo(OurPokemon[] ourPokes, PacketGenerator out){
		packetGen = out;;
		m_pokes = ourPokes;
		loadImages(ourPokes);
		initGUI();
	}
	
	public void setPokemon(OurPokemon[] ourPokes) {
		m_pokes = ourPokes;
		loadImages(ourPokes);
		initGUI();
	}
	
	public void initGUI(){
		int y = 0;
		dpFont = GameClient.getDPFontSmall();
		this.setFont(dpFont);
		this.setResizable(false);
		this.setTitle("My Pokemon");
		this.setBackground(new Color(0, 0, 0, 85));
		this.setForeground(new Color(255, 255, 255));
		for (int i = 0; i < 6; i++){
			final int j = i;
			pokes[i] = new Container();
			pokes[i].setSize(170,42);
			pokes[i].setVisible(true);
			pokes[i].setLocation(0, y);
			pokes[i].setBackground(new Color(0, 0, 0, 0));
			y += 41;
			add(pokes[i]);
			pokes[i].setOpaque(true);
			try{
				
				pokes[i].add(pokeBall[i]);
				pokeBall[i].setLocation(4, 4);
				pokeName[i].setFont(dpFont);
				pokeName[i].setForeground(new Color(255, 255, 255));
				pokeName[i].addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(MouseEvent e) {
						super.mouseReleased(e);
						PokeInfoPane info = new PokeInfoPane(m_pokes[j]);
						info.setAlwaysOnTop(true);
						info.setLocationRelativeTo(null);
						getDisplay().add(info);
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						super.mouseEntered(e);
						pokeName[j].setForeground(new Color(255, 215, 0));
					}
					@Override
					public void mouseExited(MouseEvent e) {
						super.mouseExited(e);
						pokeName[j].setForeground(new Color(255, 255, 255));
					}

				});
				pokes[i].add(pokeIcon[i]);
				pokeIcon[i].setLocation(2, 3);
				pokes[i].add(pokeName[i]);
				pokeName[i].setLocation(42, 5);
				pokes[i].add(level[i]);
				level[i].setFont(dpFont);
				level[i].setForeground(new Color(255, 255, 255));
				level[i].setLocation(pokeName[i].getX() + pokeName[i].getWidth() + 10, pokeName[i].getY());
				pokes[i].add(hp[i]);
				hp[i].setSize(114, 10);
				hp[i].setLocation(40, pokeName[i].getY() + pokeName[i].getHeight() + 5);
				if (i != 0) {
					switchUp[i] = new SimpleArrowButton(
							SimpleArrowButton.FACE_UP);
					switchUp[i].addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									packetGen.write("I" + String.valueOf(j) + String.valueOf(j - 1));
								}
							});
					switchUp[i].setHeight(16);
					switchUp[i].setWidth(16);
					pokes[i].add(switchUp[i]);
				}
				if (i != 5) {
					switchDown[i] = new SimpleArrowButton(
							SimpleArrowButton.FACE_DOWN);
					switchDown[i].addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									packetGen.write("I" + String.valueOf(j) + String.valueOf(j + 1));
								}
							});
					
					switchDown[i].setHeight(16);
					switchDown[i].setWidth(16);
					switchDown[i].setX(24);
					pokes[i].add(switchDown[i]);
					
				}
			} catch (NullPointerException e){e.printStackTrace();}
		}
		//this.getTitleBar().setGlassPane(true);
		//this.setResizable(false);
		this.ensureZOrder();
		this.setSize(170, 288);
		this.setLocationRelativeTo(null);
		this.setBorderRendered(false);
		//this.setTitle("Pokemon Team");
	}
	
	public void loadImages(OurPokemon[] pokes){
		LoadingList.setDeferredLoading(true);
		for (int i = 0; i < 6; i++){
			pokeIcon[i] = new Label();
			pokeBall[i] = new Label();
			pokeName[i] = new Label();
			
			level[i] = new Label();
			hp[i] = new ProgressBar(0, 0);
			hp[i].setForeground(Color.green);
						
			pokeIcon[i].setSize(32, 32);
			
			pokeName[i].pack();
			
			try{
				pokeBall[i].setImage(new Image("res/Pokeball.gif"));
				pokeBall[i].setSize(30, 30);
			} catch (SlickException e){}
			try {
				if(pokes != null) {
					if (pokes[i] != null) {
						level[i].setText("Lv: " + String.valueOf(pokes[i].getLevel()));
						level[i].pack();
						pokeName[i].setText(pokes[i].getName());
						pokeIcon[i].setImage(pokes[i].getIcon());
						pokes[i].setIcon();
					hp[i].setMaximum(pokes[i].getMaxHP());
					hp[i].setForeground(Color.green);
					hp[i].setValue(pokes[i].getCurHP());
					if(pokes[i].getCurHP() > pokes[i].getMaxHP() / 2){
						hp[i].setForeground(Color.green);
					}
					else if(pokes[i].getCurHP() < pokes[i].getMaxHP() / 2 && pokes[i].getCurHP() > pokes[i].getMaxHP() / 3){
						hp[i].setForeground(Color.orange);
					}
					else if(pokes[i].getCurHP() < pokes[i].getMaxHP() / 3){
						hp[i].setForeground(Color.red);
					}
					pokes[i].setIcon();
					pokeIcon[i].setImage(pokes[i].getIcon());
					pokeIcon[i].setSize(32, 32);
					pokeName[i].setText(pokes[i].getName());
					pokeName[i].pack();
					level[i].setText("Lv: " + String.valueOf(pokes[i].getLevel()));
					level[i].pack();
					} else {
						hp[i].setVisible(false);
					}
				}
			} catch (NullPointerException e){ e.printStackTrace(); }
		}
		LoadingList.setDeferredLoading(false);
	}
	
	public void update(OurPokemon[] pokes){
		m_pokes = pokes;
		LoadingList.setDeferredLoading(true);
		for (int i = 0; i < 6; i++){
			try {
				if (pokes[i] != null) {
					hp[i].setMaximum(pokes[i].getMaxHP());
				hp[i].setValue(pokes[i].getCurHP());
				if(pokes[i].getCurHP() > pokes[i].getMaxHP() / 2){
					hp[i].setForeground(Color.green);
				}
				else if(pokes[i].getCurHP() < pokes[i].getMaxHP() / 2 && pokes[i].getCurHP() > pokes[i].getMaxHP() / 3){
					hp[i].setForeground(Color.orange);
				}
				else if(pokes[i].getCurHP() < pokes[i].getMaxHP() / 3){
					hp[i].setForeground(Color.red);
				}
				pokes[i].setIcon();
				pokeIcon[i].setImage(pokes[i].getIcon());
				pokeName[i].setText(pokes[i].getName());
				pokeName[i].pack();
				level[i].setText("Lv: " + String.valueOf(pokes[i].getLevel()));
				level[i].pack();
				level[i].setLocation(pokeName[i].getX() + pokeName[i].getWidth() + 10, 5);
				
				pokeBall[i].setLocation(4, 4);
				pokeIcon[i].setLocation(2, 3);
				pokeName[i].setLocation(40, 5);
				hp[i].setLocation(40, pokeName[i].getY() + pokeName[i].getHeight() + 5);
				hp[i].setVisible(true);
				if (i != 0)
					switchUp[i].setVisible(true);
				if (i != 5)
					switchDown[i].setVisible(true);
				} else {
					if (i != 0)
						switchUp[i].setVisible(false);
					if (i != 5)
						switchDown[i].setVisible(false);
					hp[i].setVisible(false);
					level[i].setText("");
					level[i].pack();
					pokeIcon[i].setImage(null);
				}
			} catch (NullPointerException e){ e.printStackTrace(); }
		}
		LoadingList.setDeferredLoading(false);
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
	
	public OurPokemon [] getPokemon() {
		return m_pokes;
	}
}
