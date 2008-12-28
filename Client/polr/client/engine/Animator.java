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

package polr.client.engine;

import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;

import polr.client.GameClient;
import polr.client.logic.Player;

public class Animator {
	private GameMapMatrix mapMatrix;

	// sets animation timers
	Timer animTimer;
	TimerTask animTask;

	private GameClient thisGame;

	private static final int ANIMATION_INCREMENT = 4;

	// Sets up calls
	public Animator(GameMapMatrix maps, GameClient g) {
		animTimer = new Timer();
		animTask = new TimerTask() {
			public void run() {
				animate();
			}
		};
		setThisGame(g);
		mapMatrix = maps;
	}

	// Prepares for animation
	public void animate() {
		try {
			for (Player p : mapMatrix.getPlayerList().values()) {
				animatePlayer(p);
			}
		} catch (ConcurrentModificationException e) {

		}
	}

	// moves player and animates sprite
	private void animatePlayer(Player p) {
		// Do as long as chosen character is the User's
		if (p.thisPlayer) {
			if (p.x > p.svrX) {
				p.map
						.setXOffset((p.map.getXOffset() + ANIMATION_INCREMENT)); //+ p.map
								//.getXOffsetModifier()));
			} else if (p.x < p.svrX) {
				p.map
						.setXOffset((p.map.getXOffset() - ANIMATION_INCREMENT)); //+ p.map
								//.getXOffsetModifier()));
			} else if (p.y > p.svrY) {
				p.map.setYOffset((p.map.getYOffset() + ANIMATION_INCREMENT));// + 
				//p.map.getYOffsetModifier()));
			} else if (p.y < p.svrY) {
				p.map.setYOffset((p.map.getYOffset() - ANIMATION_INCREMENT));// + 
				//p.map.getYOffsetModifier()));
			}
		}
			// move left
			if (p.x > p.svrX) {
				p.x -= ANIMATION_INCREMENT;
			// move right
			} else if (p.x < p.svrX) {
				p.x += ANIMATION_INCREMENT;
			// Move up
			} else if (p.y > p.svrY) {
				p.y -= ANIMATION_INCREMENT;
			// Move down
			} else if (p.y < p.svrY) {
				p.y += ANIMATION_INCREMENT;
			}
		if (p.x == p.svrX && p.y == p.svrY && p.isAnimating()) {
			p.setFacing(p.facing);
			p.setAnimating(false);
		}
	}

	// executes animation
	public void execute(int interval) {
		animTimer.schedule(animTask, 0, interval);
	}

	// sets client as game
	void setThisGame(GameClient thisGame) {
		this.thisGame = thisGame;
	}

	// gets client
	GameClient getThisGame() {
		return thisGame;
	}
}
