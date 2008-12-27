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

package polr.client.ui.window;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;

public class LoadingScreen extends Frame {
	private Label info;

	public LoadingScreen() {
		super();
		initGUI();
	}

	private void initGUI() {
		this.setSize(800, 632);
		this.setLocation(0, -24);
		this.setAlwaysOnTop(true);
		this.getTitleBar().setVisible(false);
		this.setBackground(new Color(0, 0, 205, 70));
		info = new Label();
		try {
			Image img = new Image("res/loading.png");
			info.setImage(img);
			info.setSize(32, 32);
			info.setLocation((800 / 2) - (info.getWidth() / 2), (600 / 2)
					- (info.getHeight() / 2));
			add(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
