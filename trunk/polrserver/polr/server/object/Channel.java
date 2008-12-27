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

package polr.server.object;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Channel {
	private ArrayList<PlayerChar> subscribers;
	
	public Channel() {
		subscribers = new ArrayList<PlayerChar>();
	}

	public void addSubscriber(PlayerChar p) {
		subscribers.add(p);
	}
	
	public void removeSubscriber(PlayerChar p) {
		subscribers.remove(p);
	}
	
	public void sendToAll(String s) {
		try {
			for (PlayerChar p : subscribers) {
				p.getIoSession().write(s);
			}
		} catch (ConcurrentModificationException e) {}
	}
	
	public void sentToAllBut(PlayerChar exclude, String s) {
		try {
			for (PlayerChar p : subscribers) {
				if (!p.equals(exclude))
					p.getIoSession().write(s);
			}
		} catch (ConcurrentModificationException e) {}
	}
}
