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

package polr.server.battle;

import java.util.LinkedHashSet;
import java.util.Set;

import polr.server.mechanics.BattleMechanics;
import polr.server.mechanics.statuses.BurnEffect;
import polr.server.mechanics.statuses.FlinchEffect;
import polr.server.mechanics.statuses.FreezeEffect;
import polr.server.mechanics.statuses.PoisonEffect;
import polr.server.mechanics.statuses.SleepEffect;
import polr.server.mechanics.statuses.StatusEffect;
import polr.server.object.PlayerChar;

/**
  * PvPBattleField manages turns and event data packets within PvP battles.
  *
  * @author Pivot
  * @author Sienide
  *
  */

public final class PvPBattleField extends BattleField {
	private long prizeMoney;
	private boolean m_isDone = false;
	Set<Pokemon> participatingPokemon = new LinkedHashSet<Pokemon>();
	PlayerChar[] players;
	BattleTurn[] queuedTurns = new BattleTurn[2];
	
	public PvPBattleField(BattleMechanics mech,
			PlayerChar[] p, long amount) {
		super(mech, new Pokemon[][] {p[0].getParty(), p[1].getParty()});
		players = p;
		prizeMoney = amount;
		beginBattle();
	}

	public void beginBattle() {
		try {
			//validateTeam(getParty(0), 0);
			//validateTeam(getParty(1), 1);
			healthCheck();
			participatingPokemon.add(getParty(0)[0]);
			participatingPokemon.add(getParty(1)[0]);
			requestMoves();
		} catch (Exception e) {
			String message = "The battle cannot begin because "
				+ "a team has a problem:\n\n"
				+ e.getMessage();
			showMessage(message);
			endBattle(4);
		}
	}

	public PvPBattleField(BattleMechanics mech) {
		super(mech);
	}
	
	public void healthCheck() {
		if(getParty(0)[0].getHealth() <= 0) {
			for(int i = 1; i < players[0].getParty().length && getParty(0)[i] != null; i++) {
				if(getParty(0)[i].getHealth() > 0) {
					Pokemon temp = getParty(0)[0];
					getParty(0)[0] = getParty(0)[i];
					getParty(0)[i] = temp;
					players[0].arrangeParty();
					players[0].updateClientParty();
					break;
				}
			}
		}
		if(getParty(1)[0].getHealth() <= 0) {
			for(int i = 1; i < players[1].getParty().length && getParty(1)[i] != null; i++) {
				if(getParty(1)[i].getHealth() > 0) {
					Pokemon temp = getParty(1)[0];
					getParty(1)[0] = getParty(1)[i];
					getParty(1)[i] = temp;
					players[1].arrangeParty();
					players[1].updateClientParty();
					break;
				}
			}
		}
	}

	public void endBattle(int scenario) {
		m_isDone = true;
		// nullify all references to this object
		// in the PlayerChars
		// call on clientHandler
		switch (scenario){
		//case 1 = resign
		//case 2 = faint
		//case 3 = win
		//case 4 = someone screwed up
		case 1: 
			//TODO: code to exit battle
			break;
		case 2:
			//TODO: Teleport to last used pokecenter
			//TODO: Heal all pokemon in party
			//TODO: Send prize cash to opponent
			//TODO: Code to exit battle
			break;
		case 3:
			players[0].endBattle();
			players[1].endBattle();
			players[0].updateClientParty();
			players[1].updateClientParty();
			break;
		case 4:
			//TODO: something's wrong
			break;
		}
	}

	public void healPokemon(int trainer) {
		for (Pokemon p : (Pokemon[])players[trainer].getParty()) {
			p.changeHealth(p.getRawStat(Pokemon.S_HP));
			p.setPp(0, p.getMaxPp(0));
			p.setPp(1, p.getMaxPp(1));
			p.setPp(2, p.getMaxPp(2));
			p.setPp(3, p.getMaxPp(3));
		}
	}
	@Override
	public String getTrainerName(int idx) {
		return players[idx].getName();
	}

	@Override
	public void informPokemonFainted(int trainer, int idx) {
		// TODO Auto-generated method stub
		showMessage("!" + this.getActivePokemon()[trainer].getName() + " fainted!");
	}

	@Override
	public void informPokemonHealthChanged(Pokemon poke, int change) {
		// TODO Auto-generated method stub
		players[0].getIoSession().write("H;" + 
				poke.getTrainerName() + ";"
				+ poke.getName() + ";" + change);
		players[1].getIoSession().write("H;" + 
				poke.getTrainerName() + ";"
				+ poke.getName() + ";" + change);
	}

	@Override
	public void informStatusApplied(Pokemon poke, StatusEffect eff) {
		// TODO Auto-generated method stub
		if (eff instanceof BurnEffect)
			showMessage("!Ouch! " + poke.getTrainerName() +
					"'s " + poke.getName() + " was burned!");
		if (eff instanceof PoisonEffect)
			showMessage("!Uh-oh.. " + poke.getTrainerName() +
					"'s " + poke.getName() + " has been poisoned!");
		if (eff instanceof FreezeEffect)
			showMessage("!Brr! " + poke.getTrainerName() +
					"'s " + poke.getName() + " is frozen!");
		if (eff instanceof FlinchEffect)
			showMessage(poke.getTrainerName() +
					"'s " + poke.getName() + " flinched!");
		if (eff instanceof SleepEffect)
			showMessage("!Zzzzzz..... " + poke.getTrainerName() +
					"'s " + poke.getName() + " fell asleep..");
	}

	@Override
	public void informStatusRemoved(Pokemon poke, StatusEffect eff) {
		// TODO Auto-generated method stub

	}

	@Override
	public void informSwitchInPokemon(int trainer, Pokemon poke) {
		players[0].getIoSession().write("p;" +
				players[trainer].getName() + ";" +
				poke.getId()
				+ ";" + poke.getName());
		players[1].getIoSession().write("p;" +
				players[trainer].getName() + ";" +
				poke.getId()
				+ ";" + poke.getName());
	}

	@Override
	public void informUseMove(Pokemon poke, String name) {
		// TODO Auto-generated method stub
		showMessage(poke.getTrainerName() + "'s " + poke.getName() + " used " + name + ".");
	}

	@Override
	public void informVictory(int winner) {
		// TODO Auto-generated method stub
		// Obvious. 'winner' is either player0
		// or 1. 
		endBattle(3);
		//Reward winner
		if(this.getTrainerName(winner) == players[0].getName()) {
			players[0].setMoney(players[0].getMoney() + prizeMoney);
			players[1].setMoney(players[1].getMoney() - prizeMoney);
			players[1].lostBattle();
		}
		else {
			players[0].setMoney(players[0].getMoney() - prizeMoney);
			players[1].setMoney(players[1].getMoney() + prizeMoney);
			players[0].lostBattle();
		}	
		players[0].getIoSession().write("v" + this.getTrainerName(winner));
		players[1].getIoSession().write("v" + this.getTrainerName(winner));
		players[0].updateClientParty();
		players[1].updateClientParty();
	}

	@Override
	public void queueMove(int trainer, BattleTurn move)
	throws MoveQueueException {
		// TODO Auto-generated method stub
		// Not entirely sure what this is.
		// I think we may need to handle storing
		// a created BattleTurn here. ??
		// It doesn't seem to get called.
		if (queuedTurns[trainer] == null) {
			if(move.getId() == -1) {
				if((trainer == 0 && queuedTurns[1] != null) || (trainer == 1 && queuedTurns[0] != null))
					executeTurn(queuedTurns);
				else {
					queuedTurns[trainer] = move;
				}
			}
			else {
				if (this.getActivePokemon()[trainer].isFainted()) {
					if (!move.isMoveTurn()) {
						this.switchInPokemon(trainer, move.getId());
						requestMoves();
					} else {
						if (trainer == 0) {
							if (participatingPokemon.contains(getActivePokemon()[0]))
								participatingPokemon.remove(getActivePokemon()[0]);
							players[trainer].getIoSession().write("Ms");
						}
						else if(trainer == 1) {
							if (participatingPokemon.contains(getActivePokemon()[1]))
								participatingPokemon.remove(getActivePokemon()[1]);
							players[trainer].getIoSession().write("Ms");
						}
					}
				} else {
					if (move.isMoveTurn())
						if (getActivePokemon()[trainer].mustStruggle())
							queuedTurns[trainer] = BattleTurn.getMoveTurn(-1);
						else {
							if (this.getActivePokemon()[trainer].getPp(move.getId()) <= 0) {
								players[trainer].getIoSession().write("M");
								players[trainer].getIoSession().write("s!Sorry, the move " + 
										this.getActivePokemon()[trainer].getMoveName(
												move.getId()) + " has no PP left. " +
								"Select a different move.");
							} else
								queuedTurns[trainer] = move;
						}
					else {
						if (this.m_pokemon[trainer][move.getId()].isActive())
							queuedTurns[trainer] = move;
						else {
							players[trainer].getIoSession().write("M");
						}
					}
				}
			}
		}
		if (queuedTurns[0] != null &&
				queuedTurns[1] != null) {
			executeTurn(queuedTurns);
		}
		if(move.isMoveTurn()) {
			players[trainer].getIoSession().write("g!" + move.getId() +
	   				"!" + this.getActivePokemon()[trainer].getPp(move.getId()));
		}
	}

	public void clearQueue() {
		for (int i = 0;	i < queuedTurns.length; i++)
			queuedTurns[i] = null;
	}
	@Override
	public void refreshActivePokemon() {
		// TODO Auto-generated method stub

		// I *think* this is meant for us
		// to send the info for whatever
		// Pokes have been switched in
		// to both clients.

	}

	@Override
	public void requestAndWaitForSwitch(int party) {
		// TODO ?? what is this?

	}

	@Override
	protected void requestMoves() {
		// TODO Auto-generated method stub

		// Happens every turn, ask for a move
		// from each player.

		// Doesn't get called if a Pokemon faints.

		// Think we need to get BattleTurns out of
		// what the client sends back to us,
		// then call executeTurn() on those.
		if (this.getActivePokemon()[0].isActive() &&
				this.getActivePokemon()[1].isActive() && !m_isDone) {
			players[0].getIoSession().write("M");
			players[1].getIoSession().write("M");
		}
	}

	@Override
	protected void requestPokemonReplacement(int i) {
		// TODO Send request to client
		// A Pokemon has fainted, 'i' indicates
		// whether it was Player 1 or 0's.

		// We need to solicit a new Pokemon to put in.

		// Should probably call replaceFaintedPokemon()
		// on getting that.

		try {
			if (this.getAliveCount(i) > 0)
				players[i].getIoSession().write("Ms");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showMessage(String message) {
		// TODO Send to client, display in window textbox
		// This method sends messages such as "It was super-effective!"
		if (players != null) {
			if (players[0] != null)
				players[0].getIoSession().write("s" + message);
			if (players[1] != null)
				players[1].getIoSession().write("s" + message);
		}
	}

    public int getTrainerIndex(String s) {
    	if(s.equalsIgnoreCase(players[0].getName())) {
    			return 0;
    	}
    	else {
    		return 1;
    	}
    }

	public void playerDisconnected(int trainerIndex) {
		if(this.getTrainerName(trainerIndex) == players[1].getName()) {
			//Player 1 disconnected
			players[0].getIoSession().write("s" + this.getTrainerName(1) + " has disconnected.");
			players[0].getIoSession().write("v" + this.getTrainerName(0));
			players[0].setMoney(players[0].getMoney() + prizeMoney);
			players[1].setMoney(players[1].getMoney() - prizeMoney);
		}
		else {
			//Player 0 disconnected
			players[1].getIoSession().write("s" + this.getTrainerName(0) + " has disconnected.");
			players[1].getIoSession().write("v" + this.getTrainerName(1));
			players[0].setMoney(players[0].getMoney() - prizeMoney);
			players[1].setMoney(players[1].getMoney() + prizeMoney);
		}
		players[0].endBattle();
		players[1].endBattle();
	}
}
