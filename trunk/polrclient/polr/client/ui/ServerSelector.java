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
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextField;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

/**
 * Creates a small window with a list of 5 servers.
 */
public class ServerSelector extends Frame {
	private String[] listedServers = new String[5];
	private Button server1;
	private Button server2;
	private Button server3;
	private Button server4;
	private Button server5;
	private Button privateServer;
	private TextField privateIP;
	private TextField proxyName;
	private TextField proxyPort;
	private Label info;

	public ServerSelector() {
		initGUI();
	}

	public void initGUI() {
		this.setSize(320, 320);
		this.setLocation(400 - 160, 240);
		this.setVisible(true);
		this.setBackground(new Color(0, 0, 0, 70));
		this.setTitle("Pokemon Global Server Select");
		this.setResizable(false);
		this.setDraggable(false);
		this.getCloseButton().setVisible(false);

		// LOADS THE SERVER LISTINGS FROM FILE
		/*
		 * try { BufferedReader f = new BufferedReader(new
		 * FileReader("polr/client/network/servers.txt")); for(int i = 0;
		 * i < 5; i++) { listedServers[i] = f.readLine(); } } catch (Exception
		 * e) { e.printStackTrace(); }
		 */
		listedServers[0] = "polr.dyndns.org";
		listedServers[1] = "null";
		listedServers[2] = "localhost";
		listedServers[3] = "null";
		listedServers[4] = "pokemonproject.homeip.net";

		server1 = new Button();
		server1.setText("Snowpoint Temple Server");
		server1.setSize(280, 32);
		server1.setLocation(16, 32);
		server1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!proxyName.getText().equalsIgnoreCase("")
						&& !proxyPort.getText().equalsIgnoreCase("")) {
					GlobalGame.setProxy(proxyName.getText(), Integer
							.parseInt(proxyPort.getText()));
				}
				GlobalGame.setServer(listedServers[0]);
			}
		});
		if (listedServers[0].equalsIgnoreCase("null")) {
			server1.setEnabled(false);
		}
		this.add(server1);

		server2 = new Button();
		server2.setText("Newmoon Server");
		server2.setSize(280, 32);
		server2.setLocation(16, 64);
		server2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!proxyName.getText().equalsIgnoreCase("")
						&& !proxyPort.getText().equalsIgnoreCase("")) {
					GlobalGame.setProxy(proxyName.getText(), Integer
							.parseInt(proxyPort.getText()));
				}
				GlobalGame.setServer(listedServers[1]);
			}
		});
		if (listedServers[1].equalsIgnoreCase("null")) {
			server2.setEnabled(false);
		}
		this.add(server2);

		server3 = new Button();
		server3.setText("Burned Tower Server");
		server3.setSize(280, 32);
		server3.setLocation(16, 96);
		server3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!proxyName.getText().equalsIgnoreCase("")
						&& !proxyPort.getText().equalsIgnoreCase("")) {
					GlobalGame.setProxy(proxyName.getText(), Integer
							.parseInt(proxyPort.getText()));
				}
				GlobalGame.setServer(listedServers[2]);
			}
		});
		if (listedServers[2].equalsIgnoreCase("null")) {
			server3.setEnabled(false);
		}
		this.add(server3);

		server4 = new Button();
		server4.setText("New Island Server");
		server4.setSize(280, 32);
		server4.setLocation(16, 128);
		server4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!proxyName.getText().equalsIgnoreCase("")
						&& !proxyPort.getText().equalsIgnoreCase("")) {
					GlobalGame.setProxy(proxyName.getText(), Integer
							.parseInt(proxyPort.getText()));
				}
				GlobalGame.setServer(listedServers[3]);
			}
		});
		if (listedServers[3].equalsIgnoreCase("null")) {
			server4.setEnabled(false);
		}
		this.add(server4);

		server5 = new Button();
		server5.setText("Sky Pillar Server");
		server5.setSize(280, 32);
		server5.setLocation(16, 160);
		server5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!proxyName.getText().equalsIgnoreCase("")
						&& !proxyPort.getText().equalsIgnoreCase("")) {
					GlobalGame.setProxy(proxyName.getText(), Integer
							.parseInt(proxyPort.getText()));
				}
				GlobalGame.setServer(listedServers[4]);
			}
		});
		if (listedServers[4].equalsIgnoreCase("null")) {
			server5.setEnabled(false);
		}
		this.add(server5);

		privateIP = new TextField();
		privateIP.setLocation(16, 204);
		privateIP.setSize(128, 24);
		this.add(privateIP);

		privateServer = new Button();
		privateServer.setText("Private Server");
		privateServer.setSize(128, 24);
		privateServer.setLocation(168, 204);
		privateServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!proxyName.getText().equalsIgnoreCase("")
						&& !proxyPort.getText().equalsIgnoreCase("")) {
					GlobalGame.setProxy(proxyName.getText(), Integer
							.parseInt(proxyPort.getText()));
				}
				GlobalGame.setServer(getPrivateServer());
			}
		});
		this.add(privateServer);

		info = new Label();
		info.setText("Socks Proxy: (Ignore if not required)");
		info.setLocation(16, 226);
		info.setSize(256, 30);
		info.setFont(GlobalGame.getDPFontSmall());
		info.setForeground(new Color(255, 255, 255));
		info.setVisible(true);
		this.add(info);

		proxyName = new TextField();
		proxyName.setLocation(16, 256);
		proxyName.setSize(128, 24);
		proxyName.setText("");
		this.add(proxyName);

		proxyPort = new TextField();
		proxyPort.setLocation(168, 256);
		proxyPort.setSize(64, 24);
		proxyPort.setText("1080");
		this.add(proxyPort);
	}

	public String getPrivateServer() {
		if (privateIP.getText().length() > 0)
			return privateIP.getText();
		else
			return "localhost";
	}
}