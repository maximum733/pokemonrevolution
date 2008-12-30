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
import polr.server.player.PlayerChar;

public abstract class NonPlayerChar extends Char {
	private long index;
	public void setIndex(long i) {
		index = i;
	}
	
	public long getIndex() {
		return index;
	}
	
	public boolean move(Directions dir) {
		if(facing != dir) {
			switch (dir) {
			case up:
				facing = Directions.up;
				getMap().sendToAll("CU" + index);
				return true;
			case down:
				facing = Directions.down;
				getMap().sendToAll("CD" + index);
				return true;
			case left:
				facing = Directions.left;
				getMap().sendToAll("CL" + index);
				return true;
			case right:
				facing = Directions.right;
				getMap().sendToAll("CR" + index);
				return true;
			}
			return true;
		} else if (getMap().canMove(dir, this)) {
			switch (dir) {
			case up:
				y -= 32;
				facing = Directions.up;
				getMap().sendToAll("U" + index);
				return true;
			case down:
				y += 32;
				facing = Directions.down;
				getMap().sendToAll("D" + index);
				return true;
			case left:
				x -= 32;
				facing = Directions.left;
				getMap().sendToAll("L" + index);
				return true;
			case right:
				x += 32;
				facing = Directions.right;
				getMap().sendToAll("R" + index);
				return true;
			}
		}
		return false;
	}
	
	protected void sendSpeech(String speech, PlayerChar target) {
		target.getIoSession().write("T" + speech);
	}
	public abstract void speakTo(PlayerChar target);
	//public abstract String getNPCTag();
}
