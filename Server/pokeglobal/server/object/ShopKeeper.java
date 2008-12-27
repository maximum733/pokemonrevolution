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

package pokeglobal.server.object;

import java.util.HashMap;

public class ShopKeeper extends NonPlayerChar {
	private HashMap<String, Integer> m_merch;
	
	public ShopKeeper() {
		m_merch = new HashMap<String, Integer>();
		m_merch.put("Poke Ball", 100);
		m_merch.put("Moon Stone", 2000);
		m_merch.put("Fire Stone", 2000);
		m_merch.put("Water Stone", 2000);
	}
	@Override
	public void speakTo(PlayerChar target) {
		StringBuilder shopData = new StringBuilder("x");
		for (String itemName : m_merch.keySet()) {
			
			shopData.append("," + itemName + "," + m_merch.get(itemName));
		} 
		System.out.println(shopData);
		target.getIoSession().write(shopData);
	}
	
	public HashMap<String, Integer> getMerchandise() {
		return m_merch;
	}
}
