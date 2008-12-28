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

package polr.server.object;

import polr.server.map.ServerMap.Directions;

public abstract class NonPlayerChar extends Char {
	private long index;
	public void setIndex(long i) {
		index = i;
	}
	
	public long getIndex() {
		return index;
	}
	
	public void move(Directions dir) {
		if (getMap().canMove(dir, this)) {
			getMap().getNPCs().remove(new int[] {x, y});
			switch (dir) {
			case up:
				y -= 32;
				facing = Directions.up;
				getMap().sendToAll("CU" + x + "." + y);
				break;
			case down:
				y += 32;
				facing = Directions.down;
				getMap().sendToAll("CD" + x + "." + y);
				break;
			case left:
				x -= 32;
				facing = Directions.left;
				getMap().sendToAll("CL" + x + "." + y);
				break;
			case right:
				x += 32;
				facing = Directions.right;
				getMap().sendToAll("CR" + x + "." + y);
			}
			getMap().getNPCs().put(new int[] {x, y}, this);
		} else {
			switch (dir) {
			case up:
				facing = Directions.up;
				getMap().sendToAll("JCU" + x + "." + y);
				break;
			case down:
				facing = Directions.down;
				getMap().sendToAll("JCD" + x + "." + y);
				break;
			case left:
				facing = Directions.left;
				getMap().sendToAll("JCL" + x + "." + y);
				break;
			case right:
				facing = Directions.right;
				getMap().sendToAll("JCR" + x + "." + y);
			}
		}
	}
	
	protected void sendSpeech(String speech, PlayerChar target) {
		target.getIoSession().write("T" + speech);
	}
	public abstract void speakTo(PlayerChar target);
	//public abstract String getNPCTag();
}
