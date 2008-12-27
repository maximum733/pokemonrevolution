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

import java.io.PrintWriter;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import polr.client.logic.OurPokemon;
import polr.client.network.PacketGenerator;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.ToggleButton;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

/*
 * This is the display for the PC storage boxes.
 */
public class PokeStorageBoxPopup extends Frame {

	private PacketGenerator packetGen;

	ToggleButton[] buttons = new ToggleButton[30];
	Image[] images = new Image[30];
	int[] indexes = new int[400];
	int buttonChosen = 0;
	Button switchPoke, close, changeBox, release;
	int boxNum, boxIndex = 0;
	OurPokemon[] team = new OurPokemon[6];

	public PokeStorageBoxPopup(PacketGenerator out, String pokeList,
			OurPokemon[] playerTeam) throws SlickException {
		// System.out.println("Created");

		team = playerTeam;

		packetGen = out;
		String pokes[] = pokeList.split(",");
		for (int i = 0; i <= 29; i++) {
			System.out.print(pokes[i + 1] + ", ");
		}
		for (int i = 0; i <= 29; i++) {
			try {
				indexes[i] = Integer.parseInt(pokes[i + 1]);
			} catch (NumberFormatException e) {
				indexes[i] = 999;
			}
		}

		layoutButtons();
		loadImages(indexes);

		this.setSize(231, 248);
		this.setLocation(400 - getWidth() / 2, 300 - getHeight() / 2);
		this.setTitle("Box Number " + String.valueOf(boxNum + 1));
		this.getTitleBar().getCloseButton().setVisible(false);
		this.setResizable(false);
		this.setVisible(true);
	}

	public void loadImages(int[] pokes) {
		try {
			LoadingList.setDeferredLoading(true);
			for (int i = 0; i <= 29; i++) {
				String[] path = new String[30];
				String[] index = new String[30];

				pokes[i] = setSpriteNumber(pokes[i]);
				if (pokes[i] < 10) {
					index[i] = "00" + String.valueOf(pokes[i]);
				} else if (pokes[i] < 100) {
					index[i] = "0" + String.valueOf(pokes[i]);
				} else if (pokes[i] != 995) {
					index[i] = String.valueOf(pokes[i]);
				}
				if (pokes[i] != 995) {
					path[i] = "res/sprites/icons/" + index[i] + ".gif";
					images[i] = new Image(path[i].toString());
				} else {
					images[i] = null;
				}
			}
			LoadingList.setDeferredLoading(false);
			for (int i = 0; i <= 29; i++) {
				buttons[i].setImage(images[i]);
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
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

	public void setChoice(int x) {
		for (int i = 0; i <= 29; i++) {
			buttons[i].setSelected(false);
		}
		buttons[x].setSelected(true);
		switchPoke.setEnabled(true);
		release.setEnabled(true);
		boxIndex = x;
	}

	public void layoutButtons() {
		int buttonX = 7;
		int buttonY = 5;
		int buttonCount = 0;

		for (int i = 0; i <= 29; i++) {
			buttons[i] = new ToggleButton();
			final int j = i;
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setChoice(j);
				}
			});
			buttons[i].setSize(32, 32);
		}

		for (int row = 0; row < 5; row++) {
			for (int column = 0; column < 6; column++) {
				buttons[buttonCount].setLocation(buttonX, buttonY);
				buttonX += 37;
				buttonCount += 1;
			}
			buttonX = 7;
			buttonY += 37;
		}

		for (int i = 0; i <= 29; i++) {
			add(buttons[i]);
		}

		switchPoke = new Button();
		close = new Button();
		changeBox = new Button();
		release = new Button();

		switchPoke.setText("Switch");
		switchPoke.pack();
		switchPoke.setLocation(5, 192);
		switchPoke.setEnabled(false);
		switchPoke.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				TeamForBox teamPanel = new TeamForBox(team, boxNum, boxIndex,
						packetGen);
				getDisplay().add(teamPanel);
				teamPanel.setLocation(getDisplay().getWidth() / 2
						- teamPanel.getWidth() / 2, getDisplay().getHeight()
						/ 2 - teamPanel.getHeight() / 2);
			}
		});

		changeBox.setText("Box 1");
		changeBox.pack();
		changeBox.setLocation(switchPoke.getX() + switchPoke.getWidth(), 192);
		changeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// TODO code for changing boxes
			}
		});
		changeBox.setEnabled(false);

		release.setText("Release");
		release.pack();
		release.setLocation(changeBox.getX() + changeBox.getWidth(), 192);
		release.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);

				final Frame confirm = new Frame("Release");
				confirm.getCloseButton().setVisible(false);

				confirm.setResizable(false);
				confirm.setSize(370, 70);
				confirm.setLocationRelativeTo(null);
				Label yousure = new Label(
						"Are you sure you want to release your Pokemon?");
				yousure.pack();
				Button yes = new Button("Release");
				yes.pack();
				yes.setLocation(0, confirm.getHeight()
						- confirm.getTitleBar().getHeight() - yes.getHeight());
				yes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						confirm.setVisible(false);
						getDisplay().remove(confirm);

						packetGen.write("f;" + boxNum + ";" + boxIndex);

						packetGen.write("F");
					}
				});
				Button no = new Button("Keep");
				no.pack();
				no.setLocation(yes.getWidth(), confirm.getHeight()
						- confirm.getTitleBar().getHeight() - no.getHeight());
				no.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						confirm.setVisible(false);
						getDisplay().remove(confirm);

						packetGen.write("F");
					}
				});
				confirm.getContentPane().add(yousure);
				confirm.getContentPane().add(yes);
				confirm.getContentPane().add(no);

				getDisplay().add(confirm);
			}
		});
		release.setEnabled(false);

		close.setText("Bye");
		close.pack();
		close.setLocation(release.getX() + release.getWidth(), 192);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				packetGen.write("F");
			}
		});

		add(switchPoke);
		add(close);
		add(changeBox);
		add(release);
	}

	public void reEnableButtons() {
		for (int i = 0; i <= 29; i++) {
			buttons[i].setEnabled(true);
		}
	}
}