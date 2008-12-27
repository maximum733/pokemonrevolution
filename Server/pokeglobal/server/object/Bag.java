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

package pokeglobal.server.object;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;

public class Bag {
	@ElementList
	private ArrayList<BagObject> m_bag;
	
	/* Used only for serialization */
	public Bag() {
	}
	
	/* Used for other instances, pass in true to initialise correctly */
	public Bag(boolean t) {
		if(t)
			m_bag = new ArrayList<BagObject>();
	}
	
	public void addItem(int id, int q) {
		BagObject b = new BagObject(id, q);
		if(m_bag.contains(b)) {
			b = m_bag.get(m_bag.indexOf(b));
			b.addQuantity(q);
		} else {
			m_bag.add(b);
		}
	}
	
	public ArrayList<BagObject> getContents() {
		return m_bag;
	}
	
	public void useItem(int id) {
		BagObject b = new BagObject(id, 0);
		if(m_bag.contains(b)) {
			b = m_bag.get(m_bag.indexOf(b));
			b.decreaseQuantity(1);
		}
	}
	
	public boolean hasItem(int id) {
		BagObject b = new BagObject(id, 0);
		return m_bag.contains(b);
	}
}
