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

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import polr.client.GlobalGame;
import polr.client.logic.OurPlayer;
import polr.client.network.PacketGenerator;
import polr.client.ui.BagPane;
import polr.client.ui.PartyInfo;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.ImageButton;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextArea;
import polr.client.ui.base.TextField;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

public class MainInterface extends Frame {
	// Main Components
	private GlobalGame thisGame;
	private Color bg;
	private Color fg;
	private ImageButton chat;
	private ImageButton requests;
	private ImageButton pokemon;
	private ImageButton pokegear;
	private ImageButton help;
	private PacketGenerator packetGen;
	private Label current;
	private boolean isHidden;
	private int location;
	private BagPane bag;

	private Button b1;
	private Button b2;
	private Button b3;

	// Help Component
	private ArrayList<String> helpInfo;

	// Chat Components
	private Button autoScroll;
	private Button scrollUp;
	private Button scrollDown;
	private int chatLocation;
	private int charL;
	private TextArea chatList;
	private TextField chatType;
	private ArrayList<String> worldChat;
	private ArrayList<String> localChat;

	// Party Components
	private PartyInfo party;

	// PokeGear Components

	public MainInterface(GlobalGame game) {
		thisGame = game;
		initGUI();
	}

	public void initGUI() {
		bag = new BagPane(thisGame);
		bag.setVisible(false);
		this.add(bag);
		bag.setLocation(-28, 64);
		location = -1;
		isHidden = true;
		this.setSize(188, 64);
		this.setLocation(0, -24);
		this.setDraggable(false);
		this.setResizable(false);
		this.getTitleBar().setVisible(false);
		this.setVisible(false);
		bg = new Color(0, 0, 0, 85);
		fg = new Color(255, 255, 255);
		this.setBackground(bg);

		// Initialise help information
		helpInfo = new ArrayList<String>();
		helpInfo.add("Up/Down/Left/Right - Move");
		helpInfo.add("Space/Right Ctrl - Talk/Interact");
		helpInfo.add("");
		helpInfo
				.add("There are Game Moderators everywhere, they're more than willing to help.");
		helpInfo
				.add("For more help, go to www.polr.org and post in the forums Help Section");

		localChat = new ArrayList<String>();
		worldChat = new ArrayList<String>();
		charL = 0;
		chatLocation = 0;

		chatList = new TextArea();
		chatList.setLocation(4, 68);
		chatList.setSize(176, 280);
		chatList.setBorderRendered(false);
		chatList.setBackground(bg);
		chatList.setForeground(fg);
		chatList.setFont(GlobalGame.getDPFontSmall());
		chatList.setText("TEST");
		chatList.setEditable(false);
		chatList.setVisible(true);
		add(chatList);

		chatType = new TextField();
		chatType.setName("chatType");
		chatType.setSize(176, 25);
		chatType.setLocation(4, 272);
		getContentPane().add(chatType);
		chatType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chatTypeActionPerformed(evt);
			}
		});
		add(chatType);

		scrollUp = new Button();
		scrollUp.setSize(32, 16);
		scrollUp.setLocation(150, 48);
		scrollUp.setVisible(true);
		scrollUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				charL = charL > 0 ? charL - 1 : 0;
				reloadChat();
				autoScroll.setEnabled(true);
			}
		});
		add(scrollUp);

		scrollDown = new Button();
		scrollDown.setSize(32, 16);
		scrollDown.setLocation(150, 250);
		scrollDown.setVisible(true);
		scrollDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (chatLocation) {
				case 0:
					charL = charL < worldChat.size() ? charL + 1 : charL;
					reloadChat();
					autoScroll.setEnabled(true);
					break;
				case 1:
					charL = charL < localChat.size() ? charL + 1 : charL;
					reloadChat();
					autoScroll.setEnabled(true);
					break;
				}
			}
		});
		add(scrollDown);

		autoScroll = new Button();
		autoScroll.setSize(32, 16);
		autoScroll.setLocation(116, 250);
		autoScroll.setVisible(true);
		autoScroll.setEnabled(false);
		autoScroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (chatLocation) {
				case 0:
					charL = charL < worldChat.size() ? charL + 1 : charL;
					reloadChat();
					autoScroll.setEnabled(false);
					break;
				case 1:
					charL = charL < localChat.size() ? charL + 1 : charL;
					reloadChat();
					autoScroll.setEnabled(false);
					break;
				}
			}
		});
		add(autoScroll);

		b1 = new Button();
		b1.setSize(36, 24);
		b1.setText("World");
		b1.setLocation(68, 42);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (location) {
				case 0:
					// Chat - World Button
					chatLocation = 0;
					reloadChat();
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					// Show Bag
					bag.setVisible(true);
					break;
				case 4:
					break;
				}
			}
		});
		b1.setVisible(true);
		add(b1);

		b2 = new Button();
		b2.setSize(36, 24);
		b2.setText("Local");
		b2.setLocation(104, 42);
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (location) {
				case 0:
					// Chat - World Button
					chatLocation = 1;
					reloadChat();
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					bag.setVisible(false);
					break;
				case 4:
					break;
				}
			}
		});
		b2.setVisible(true);
		add(b2);

		current = new Label();
		current.setFont(GlobalGame.getDPFontSmall());
		current.setSize(32, 24);
		current.setLocation(4, 32);
		current.setHorizontalAlignment(current.LEFT_ALIGNMENT);
		current.setForeground(fg);
		current.setText("");
		add(current);

		chat = new ImageButton();
		try {
			chat.setImage(new Image("res/ui/chat_32x32.png"));
			chat.setDownImage(new Image("res/ui/chatPressed_32x32.png"));
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		chat.setSize(32, 32);
		chat.setLocation(4, 4);
		chat.setVisible(true);
		chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isHidden) {
					setHeight(320);
					isHidden = false;
					location = 0;
					current.setText("Chat");
					goToChat();
					chatType.grabFocus();
				} else if (location == 0) {
					hide();
				} else {
					current.setText("Chat");
					location = 0;
					goToChat();
					chatType.grabFocus();
				}
			}
		});
		add(chat);

		requests = new ImageButton();
		try {
			requests.setImage(new Image("res/ui/recuests_32x32.png"));
			requests
					.setDownImage(new Image("res/ui/recuestsPressed_32x32.png"));
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		requests.setSize(32, 32);
		requests.setLocation(40, 4);
		requests.setVisible(true);
		requests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isHidden) {
					setHeight(320);
					isHidden = false;
					location = 1;
					current.setText("PvP/Trade");
					goToRequests();
				} else if (location == 1) {
					hide();
				} else {
					location = 1;
					current.setText("PvP/Trade");
					goToRequests();
				}
			}
		});
		add(requests);

		pokemon = new ImageButton();
		try {
			pokemon.setImage(new Image("res/ui/pokemon_32x32.png"));
			pokemon.setDownImage(new Image("res/ui/pokemonPressed_32x32.png"));
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pokemon.setSize(32, 32);
		pokemon.setLocation(76, 4);
		pokemon.setVisible(true);
		pokemon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isHidden) {
					goToPokemon();
				} else if (location == 2) {
					hide();
				} else {
					location = 2;
					current.setText("My Pokemon");
					goToPokemon();
				}
			}
		});
		add(pokemon);

		pokegear = new ImageButton();
		try {
			pokegear.setImage(new Image("res/ui/pokegear_32x32.png"));
			pokegear
					.setDownImage(new Image("res/ui/pokegearPressed_32x32.png"));
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pokegear.setSize(32, 32);
		pokegear.setLocation(112, 4);
		pokegear.setVisible(true);
		pokegear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isHidden) {
					setHeight(320);
					isHidden = false;
					location = 3;
					current.setText("PokeGear");
					goToPokeGear();
				} else if (location == 3) {
					hide();
				} else {
					location = 3;
					current.setText("PokeGear");
					goToPokeGear();
				}
			}
		});
		add(pokegear);

		help = new ImageButton();
		try {
			help.setImage(new Image("res/ui/help_32x32.png"));
			help.setDownImage(new Image("res/ui/helpPressed_32x32.png"));
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		help.setSize(32, 32);
		help.setLocation(148, 4);
		help.setVisible(true);
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isHidden) {
					setHeight(320);
					isHidden = false;
					location = 4;
					current.setText("Help");
					goToHelp();
				} else if (location == 4) {
					setHeight(64);
					isHidden = true;
				} else {
					location = 4;
					current.setText("Help");
					goToHelp();
				}
			}
		});
		add(help);
		// setPokeTeam(new OurPokemon[]{null,null,null,null,null,null});
	}

	protected void chatTypeActionPerformed(ActionEvent evt) {
		if (chatType != null && chatType.getText().length() > 0)
			switch (chatLocation) {
			case 0:
				// World chat
				packetGen.write("cw" + chatType.getText() + "\r");
				chatType.setText("");
				chatType.grabFocus();
				break;
			case 1:
				// Local chat
				packetGen.write("cl" + chatType.getText() + "\r");
				chatType.setText("");
				chatType.grabFocus();
				break;
			}
	}

	public void setPacketGenerator(PacketGenerator pg) {
		packetGen = pg;
	}

	public void goToChat() {
		/*
		 * chatType.setVisible(true); chatList.setVisible(true);
		 * scrollUp.setVisible(true); scrollDown.setVisible(true);
		 * autoScroll.setVisible(true); b1.setVisible(true);
		 * b2.setVisible(true); b1.setText("World"); b2.setText("Local");
		 */
		bag.setVisible(false);
		party.setVisible(false);
		// reloadChat();
		if (!thisGame.getChat().isVisible())
			thisGame.getChat().setVisible(true);
		else
			thisGame.getChat().setVisible(false);
	}

	public void goToRequests() {
		chatType.setVisible(false);
		chatList.setVisible(false);
		scrollUp.setVisible(false);
		scrollDown.setVisible(false);
		autoScroll.setVisible(false);
		b1.setVisible(false);
		b2.setVisible(false);
		bag.setVisible(false);
		party.setVisible(false);
	}

	public void goToPokemon() {
		setHeight(320);
		isHidden = false;
		location = 2;
		current.setText("My Pokemon");
		chatType.setVisible(false);
		chatList.setVisible(false);
		scrollUp.setVisible(false);
		scrollDown.setVisible(false);
		autoScroll.setVisible(false);
		b1.setVisible(false);
		b2.setVisible(false);
		bag.setVisible(false);
		showPokeTeam();
	}

	public void goToPokeGear() {
		setHeight(320);
		chatType.setVisible(false);
		chatList.setVisible(false);
		scrollUp.setVisible(false);
		scrollDown.setVisible(false);
		autoScroll.setVisible(false);
		b1.setVisible(true);
		b2.setVisible(true);
		b1.setText("Items");
		b2.setText("Badges");
		bag.setVisible(true);
		party.setVisible(false);
	}

	public void goToHelp() {
		chatType.setVisible(false);
		chatList.setVisible(true);
		chatList.setText("");
		chatList.setCaretPosition(0);
		for (int i = charL; i < helpInfo.size()
				&& chatList.getLineCount() < chatList.getHeight()
						/ GlobalGame.getDPFontSmall().getLineHeight()
				&& chatList.getHeight() < 280; i++) {
			chatList.setText(chatList.getText().equalsIgnoreCase("") ? helpInfo
					.get(i) : chatList.getText() + "\n" + helpInfo.get(i));
		}
		scrollUp.setVisible(false);
		scrollDown.setVisible(false);
		autoScroll.setVisible(false);
		b1.setVisible(false);
		b2.setVisible(false);
		bag.setVisible(false);
		party.setVisible(false);
	}

	public void showPokeTeam() {
		try {
			getContentPane().remove(party);
		} catch (NullPointerException e) {
		}
		party.setLocation(5, 53);
		party.setVisible(true);
		getContentPane().add(party);
	}

	public void setPartyInfo(PartyInfo info) {
		party = info;
		bag.setPokemon(info.getPokemon());
	}

	public void addWorldChatLine(String newChat) {
		int endex = newChat.indexOf(">");
		newChat = newChat.substring(0, endex + 1)
				+ newChat.substring(endex + 1).replace(">", "=>= ").replace(
						"<", "=<=");
		if (worldChat.size() > 25)
			worldChat.remove(0);
		worldChat.trimToSize();
		worldChat.add(newChat);
		if (!autoScroll.isEnabled())
			reloadChat();
	}

	public void addLocalChatLine(String newChat) {
		int endex = newChat.indexOf(">");
		newChat = newChat.substring(0, endex + 1)
				+ newChat.substring(endex + 1).replace(">", "=>=").replace("<",
						"=<=");
		if (localChat.size() > 25)
			localChat.remove(0);
		localChat.trimToSize();
		localChat.add(newChat);
		if (!autoScroll.isEnabled())
			reloadChat();
	}

	private void reloadChat() {
		switch (chatLocation) {
		case 0:
			chatList.setText("");
			chatList.setCaretPosition(0);
			for (int i = charL; i < worldChat.size()
					&& chatList.getLineCount() < chatList.getHeight()
							/ GlobalGame.getDPFontSmall().getLineHeight()
					&& chatList.getHeight() < 280; i++) {
				chatList
						.setText(chatList.getText().equalsIgnoreCase("") ? worldChat
								.get(i)
								: chatList.getText() + "\n" + worldChat.get(i));
			}
			break;
		case 1:
			chatList.setText("");
			chatList.setCaretPosition(0);
			for (int i = charL; i < localChat.size()
					&& chatList.getLineCount() < chatList.getHeight()
							/ GlobalGame.getDPFontSmall().getLineHeight()
					&& chatList.getHeight() < 280; i++) {
				chatList
						.setText(chatList.getText().equalsIgnoreCase("") ? localChat
								.get(i)
								: chatList.getText() + "\n" + localChat.get(i));
			}
			break;
		}
	}

	public Label getCurrent() {
		return current;
	}

	public void hide() {
		bag.setVisible(false);
		if (current.getText() == "My Pokemon") {
			party.setVisible(false);
		}
		setHeight(64);
		isHidden = true;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public boolean isChatting() {
		if (chatType.hasFocus())
			return true;
		return false;
	}

	public void updateBag(OurPlayer thisPlayer) {
		bag.updatePokemon(thisPlayer.getTeam());
		bag.updateItems(thisPlayer.getBag());
	}
}
