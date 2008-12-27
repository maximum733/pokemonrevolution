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

package pokeglobal.client.ui;


import java.util.HashMap;

import org.newdawn.slick.Color;

import pokeglobal.client.GlobalGame;
import pokeglobal.client.ui.base.Button;
import pokeglobal.client.ui.base.CheckBox;
import pokeglobal.client.ui.base.Frame;
import pokeglobal.client.ui.base.TextArea;
import pokeglobal.client.ui.base.event.ActionEvent;
import pokeglobal.client.ui.base.event.ActionListener;

public class ToSWindow extends Frame{
	
	private TextArea tostext;
	private CheckBox agree;
	private Button cont;
	
	private HashMap<String, String> options;
	
	public ToSWindow(){
		initGUI();
	}
	private void initGUI() {
		options = GlobalGame.getOptions();
		this.setTitle("Pokemon Global Rules");
		this.setBackground(new Color(0, 0, 0, 70));
		this.setForeground(new Color(255, 255, 255));
		this.setSize(360, 460);
		this.setLocation(GlobalGame.width / 2 - 360 / 2, 50);
		this.setResizable(false);
		this.setDraggable(false);
		this.getCloseButton().setVisible(false);
		
		tostext = new TextArea();
		tostext.setSize(355, 455);
		tostext.setText("In order to create the best Pokemon Global experience, we ask that you follow certain rules. \n" +

				"1. No swearing. Pokemon Global is a kid safe environment and we ask that you refrain from using excessive swearing. \n" +

				"2. No spamming. Spamming is when you repeat something in chat or in a whisper to another player for no other reason than to annoy them. \n" +

				"3. Only one account per person. Multiple accounts will result in a permanent IP ban. \n" +

				"4. Do not talk in all caps. It distracts people from reading in chat. \n" +

				"5. Do not scam. It tends to make us angry. \n" +

				"In addition, some guidelines to follow are: \n" +

				"*Do not discuss religion or politics. These tend to be very touchy subjects. \n" +

				"*Do not insult others. This is not a very nice thing to do. \n" +

				"*Do not give out your password or email. These are your private things, and the Pokemon Global staff will never need to ask for them. \n");
		tostext.setForeground(new Color(255, 255, 255));
		tostext.setBackground(new Color(0, 0, 0, 18));
		tostext.setBorderRendered(false);
		tostext.setEditable(false);
		this.add(tostext);
		
		agree = new CheckBox();
		agree.setSize(15, 15);
		agree.setLocation(25, 370);
		agree.setText("I agree to follow these rules");
		agree.setForeground(new Color(255, 255, 255));
		agree.setBackground(new Color(0, 0, 0, 18));
		this.add(agree);
		
		cont = new Button();
		cont.setText("Continue");
		cont.setSize(100, 30);
		cont.setLocation(25, 400);
		cont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (agree.isSelected())
				{
					GlobalGame.getLogin().goToRegistration();
				} else {
					GlobalGame.messageBox("You must agree to the ToS before you can play!", GlobalGame.getLogin());
					GlobalGame.getLogin().goToLogin();
				}
			}
		});
		this.add(cont);
	}
}
