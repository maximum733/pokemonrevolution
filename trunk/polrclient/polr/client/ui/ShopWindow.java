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

import java.util.HashMap;
import java.util.Iterator;

import polr.client.GlobalGame;
import polr.client.network.PacketGenerator;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

/*
 * This is the UI for shops.
 */
public class ShopWindow extends Frame {
	private Button[] m_merchButtons;

	private Button m_cancel;
	// string being the item name and integer being item quantity
	private HashMap<String, Integer> m_merch;

	private PacketGenerator packetGen;

	public ShopWindow(PacketGenerator out, HashMap<String, Integer> merch) {
		m_merch = merch;
		packetGen = out;
		initGUI();
	}

	public void itemClicked(String name) {
		packetGen.write("x" + name);
	}

	public void initGUI() {
		m_merchButtons = new Button[m_merch.size()];
		Iterator itemIterator = m_merch.keySet().iterator();
		Iterator priceIterator = m_merch.values().iterator();
		for (int i = 0; i < m_merch.size(); i++) {
			final String name = itemIterator.next().toString();
			m_merchButtons[i] = new Button(name + " | $" + priceIterator.next());
			m_merchButtons[i].setFont(GlobalGame.getDPFont());
			m_merchButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					itemClicked(name);
				}
			});
			getContentPane().add(m_merchButtons[i]);
		}

		m_cancel = new Button("Cancel");
		m_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelled();
			}
		});
		getContentPane().add(m_cancel);
		this.getResizer().setVisible(false);
		getCloseButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelled();
			}
		});
		setTitle("Shop");
		setResizable(false);
		setHeight(400);
		setWidth(300);
		setLocation(GlobalGame.width / 2 - 150, GlobalGame.height / 2 - 200);
		pack();
		setVisible(true);
	}

	public void cancelled() {
		setVisible(false);
		getDisplay().remove(this);
		packetGen.write("F");
	}

	public void pack() {
		m_cancel.setWidth(getWidth());
		m_cancel.setHeight(20);
		m_cancel.setY(getHeight() - getTitleBar().getHeight() - 20);
		m_cancel.setX(0);
		for (int i = 0; i < m_merchButtons.length; i++) {
			if (i > 0)
				m_merchButtons[i].setY(m_merchButtons[i - 1].getY()
						+ m_merchButtons[i - 1].getHeight());
			m_merchButtons[i].setHeight((getHeight()
					- getTitleBar().getHeight() - 20)
					/ m_merch.size());
			m_merchButtons[i].setWidth(getWidth());
		}
	}

	public static void main() {

	}
}
