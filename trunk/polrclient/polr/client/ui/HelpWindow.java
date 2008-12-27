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

import polr.client.GlobalGame;

import polr.client.ui.base.Frame;
import polr.client.ui.base.TextArea;

/*
 * The HelpWindow displays some text to the user to aid in playinng the game.
 * This would entail helpful information such as controls, and guidelines of play.
 */

public class HelpWindow extends Frame {

	private TextArea helptext;

	public HelpWindow() {
		initGUI();
	}

	private void initGUI() {
		this.setTitle("Pokemon Global: Online Help!");
		this.setBackground(new Color(0, 0, 0, 85));
		this.setForeground(new Color(255, 255, 255));
		this.setSize(360, 460);
		this.setLocation(200, 0);
		this.setResizable(false);

		helptext = new TextArea();
		helptext.setSize(355, 455);
		// setText Mover stuff to help panel.
		helptext
				.setText("The movement buttons are the arrow keys and/or WASD keys. \n"
						+ "The T key toggles the request window. \n"
						+ "The C key toggles the chat window. \n"
						+ "The P key toggles your Pokemon window.\n\n"
						+ "Use Spacebar to talk to NPC(Non Player Character) and to continue on the speechpopup.\n\n"
						+ "Use the /w USERNAME TEXT command to whisper to other players.\n"
						+ "Use the /playercount command to see how many players are online.");
		helptext.setFont(GlobalGame.getDPFontSmall());
		helptext.setForeground(new Color(255, 255, 255));
		helptext.setBackground(new Color(0, 0, 0, 18));
		helptext.setBorderRendered(false);
		helptext.setEditable(false);
		this.add(helptext);

	}

}
