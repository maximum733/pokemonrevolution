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

import polr.client.GameClient;
import polr.client.network.PacketGenerator;
import polr.client.ui.base.Frame;
import polr.client.ui.base.TextArea;
import polr.client.ui.base.TextField;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
import polr.client.ui.base.event.MouseAdapter;
import polr.client.ui.base.event.MouseEvent;

public class ChatWindow extends Frame {
	static final long serialVersionUID = 8126828445828668638L;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	private PacketGenerator packetGen;
	private TextArea chatList;
	private TextField chatType;
	private Font dpFont;
	private Color backgroundColor;
	public TextField getChatBox() {
		return chatType;
	}
	public ChatWindow(PacketGenerator out) {
		super();

		// fix up our ref to the socket writer for use in the chat send
		packetGen = out;

		initGUI();
	}

	private void initGUI() {
		//options = GlobalGame.getOptions();
		this.setMinimumSize(206, 200);
		try {
			dpFont =GameClient.getDPFontSmall();
			setTitle("Chat");
			this.setBackground(new Color(0, 0, 0, 85));
			this.setForeground(new Color(255, 255, 255));
			{
				chatList = new TextArea();
				chatList.setSize(380, 250);
				chatList.setBackground(new Color(0, 0, 0, 20));
				chatList.setForeground(new Color(255, 255, 255));
				chatList.setBorderRendered(false);
				chatList.setEditable(false);
				chatList.setFont(dpFont);
				getContentPane().add(chatList);
			}
			{
				chatType = new TextField();
				chatType.setName("chatType");
				chatType.setSize(350, 25);
				chatType.setLocation(50, 250);
				getContentPane().add(chatType);
				chatType.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						chatTypeActionPerformed(evt);
					}
				});
			}
			this.getResizer().addMouseListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent event) {
					repositionUI();
				}
			});
			setSize(206, 500);
			repositionUI();
			chatType.grabFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void chatTypeActionPerformed(ActionEvent evt) {
		if (chatType.getText() != null && chatType.getText().length() != 0) {
			packetGen.write("C" + chatType.getText() + "\r");
		}
		chatType.setText("");
		chatType.grabFocus();
	}

	public void appendText(String newChat) {
		int endex = newChat.indexOf(">");
		newChat = newChat.substring(0, endex + 1) + 
			newChat.substring(endex + 1).replace(">", " is greater than ")
									.replace("<", " is less than ");
		// prevent having an extra blank line at the top
		if (!chatList.getText().equals(""))
			chatList.setText(chatList.getText() + "\n" + newChat);
		else
			chatList.setText(newChat);
		// scroll down
		chatList.setCaretPosition(chatList.getText().length());
		checkChatWindow();
	}

	private void repositionUI() {
		chatList.setWidth((int)getWidth() - 8);
		chatType.setLocation(0, (int)getHeight()- 50);
		chatType.setSize((int)getWidth(), 25);
		checkChatWindow();
	}
	
	private void checkChatWindow() {
		try {
			if(chatList.getLineCount() >= ( (int)getHeight() - 48) / dpFont.getLineHeight()) {
				String [] s = chatList.getLinesAsText();
				chatList.setCaretPosition(0);
				chatList.setText("");
				//this next line causes the window to be unresizable
				//we need a way to calculate the max possible amount of lines in resizeable window can fit
				int adj = (int) ((s.length - (getHeight() - getTitleBar().getHeight() - chatType.getHeight())/ dpFont.getLineHeight()) + 1);
				for(int i = adj; i < s.length; i++) {
					chatList.setText(chatList.getText() + s[i]);
					chatList.setCaretPosition(chatList.getText().length());
				}
			}}
		catch ( Exception e) { 
			e.printStackTrace();
		}
		
	}

	public void setPacketGenerator(PacketGenerator p) {
		packetGen = p;
	}
}
