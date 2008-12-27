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

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.muffin.FileMuffin;
import org.newdawn.slick.muffin.Muffin;

import polr.client.GlobalGame;
import polr.client.ui.base.Button;
import polr.client.ui.base.CheckBox;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
import polr.client.ui.base.skin.simple.SimpleColorPicker;

/*
 * Game options like full screen, background color, muting sound, etc.
 * Is there a particular reason this isn't displayed yet?
 */
public class Options extends Frame {
	private HashMap<String, String> options;
	private Muffin muffin = new FileMuffin();

	private Button save;
	private Button close;

	private CheckBox fullScreen;
	private CheckBox muteSound;

	// private Label learnColorLabel;
	// private SimpleColorPicker learnColor;

	private Label backgroundColorLabel;
	private SimpleColorPicker backgroundColor;

	public Options() {
		options = GlobalGame.getOptions();

		initGUI();
	}

	@Override
	public void setVisible(boolean state) {
		options = GlobalGame.getOptions();
		super.setVisible(state);
	}

	public void initGUI() {
		{
			backgroundColorLabel = new Label("Color for window backgrounds:");
			backgroundColorLabel.pack();
			backgroundColorLabel.setLocation(10, 40);

			getContentPane().add(backgroundColorLabel);
		}
		{
			backgroundColor = new SimpleColorPicker();
			try {
				backgroundColor.setSelectedColor(Color.decode(options
						.get("backgroundColor")));
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			backgroundColor.setLocation(10, 70);

			getContentPane().add(backgroundColor);
		}
		/*
		 * { learnColorLabel = new Label("Highlight color for move learning:");
		 * learnColorLabel.pack(); learnColorLabel.setLocation(10, 40);
		 * 
		 * getContentPane().add(learnColorLabel); }
		 */
		/*
		 * { learnColor = new SimpleColorPicker(); try {
		 * learnColor.setSelectedColor(Color.decode(options.get("learnColor")));
		 * } catch (RuntimeException e) { e.printStackTrace(); }
		 * learnColor.setLocation(10, 70);
		 * 
		 * getContentPane().add(learnColor); }
		 */
		{
			fullScreen = new CheckBox("Full screen view");
			fullScreen.pack();
			fullScreen.setLocation(10, 10);

			fullScreen.setSelected(Boolean.parseBoolean(options
					.get("fullScreen")));
			getContentPane().add(fullScreen);
		}
		{
			muteSound = new CheckBox("Mute");
			muteSound.pack();
			muteSound.setLocation(150, 10);

			muteSound.setSelected(Boolean
					.parseBoolean(options.get("muteSound")));
			getContentPane().add(muteSound);
		}
		{
			save = new Button("Save");
			save.setSize(50, 25);
			save.setLocation(280, 240);
			getContentPane().add(save);

			save.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						/*
						 * options.remove("learnColor");
						 * options.put("learnColor",
						 * learnColor.getColorHexLabel(). getText());
						 */

						options.remove("backgroundColor");
						options.put("backgroundColor", backgroundColor
								.getColorHexLabel().getText());

						options.remove("fullScreen");
						options.put("fullScreen", Boolean.toString(fullScreen
								.isSelected()));

						options.remove("muteSound");
						options.put("muteSound", Boolean.toString(muteSound
								.isSelected()));
						if (muteSound.isSelected()) {
							GlobalGame.getSoundPlayer().muteAll();
						} else {
							GlobalGame.getSoundPlayer().unmuteAll();
						}

						muffin.saveFile(options, "options.dat");
						GlobalGame
								.messageBox(
										"Changes will not take effect until you restart.",
										getDisplay());
						GlobalGame.reloadOptions();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		{
			close = new Button("Close");
			close.setSize(60, 25);
			close.setLocation(335, 240);
			getContentPane().add(close);

			close.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
		}
		setTitle("Options");
		setSize(400, 300);
		this.setLocationRelativeTo(null);
	}
}
