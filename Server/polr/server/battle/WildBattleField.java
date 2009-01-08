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

import polr.server.battle.BattleTurn;
import polr.server.battle.MoveQueueException;
import polr.server.GameServer;
import polr.server.database.POLRDataEntry;
import polr.server.database.POLRDatabase;
import polr.server.database.POLREvolution.EvoTypes;
import polr.server.mechanics.BattleMechanics;
import polr.server.mechanics.statuses.BurnEffect;
import polr.server.mechanics.statuses.ConfuseEffect;
import polr.server.mechanics.statuses.FreezeEffect;
import polr.server.mechanics.statuses.ParalysisEffect;
import polr.server.mechanics.statuses.PoisonEffect;
import polr.server.mechanics.statuses.SleepEffect;
import polr.server.mechanics.statuses.StatusEffect;
import polr.server.player.PlayerChar;

/**
 * @author Sienide
 *
 * Wild Pokemon Battlefield
 * 
 */
public final class WildBattleField extends BattleField {
	PlayerChar m_humanPlayer;

	   /**
	    * The turns queued by the player and the Wild Pokemon
		*/
	BattleTurn[] m_queuedTurns = new BattleTurn[2];

	   /**
	    * A Set of all Pokemon who participated in the battle, used for EXP Gain
		*/
	Set<Pokemon> participatingPokemon
	= new LinkedHashSet<Pokemon>();

	private POLRDatabase m_polrDB;
	private int runCount;

	   /**
	    * Defines whether or not the battle is over
		*/
	private boolean m_isDone = false;

	   /**
	    * Constructor
	    * @param BattleMechanics mech - JewelMechanics
	    * @param Pokemon wildPokemon - Pokemon Object of the Pokemon that appeared
	    * @param PlayerChar player - the player who it appeared to
		*/
	public WildBattleField(BattleMechanics mech, Pokemon wildPokemon,
			PlayerChar player) {
		super(mech, new Pokemon[][] {
				player.getParty(),
				new Pokemon[] { wildPokemon }});
		m_humanPlayer = player;
		m_polrDB = m_humanPlayer.getPOLRdb();
		beginBattle();
	}

	   /**
	    * Ran at the start of each battle. Adds the first pokemon to participatingPokemon.
		*/
	public void beginBattle() {
		try {
			participatingPokemon.add(getParty(0)[0]);
			requestMoves();
		} catch (Exception e) {
			String message = "The battle cannot begin because "
				+ "a team has a problem:\n\n"
				+ e.getMessage();
			showMessage(message);
			endBattle(4);
		}
	}

	   /**
	    * Constructor
	    * @param BattleMechanics mech - JewelMechanics
		*/
	public WildBattleField(BattleMechanics mech) {
		super(mech);
	}

	   /**
	    * Called when the battle is over.
	    * 1 = Player ran away
	    * 2 = (NEVER USE CALL THIS)
	    * 3 = Player won
	    * 4 = Error
	    * 5 = Player caught the Pokemon
	    * @param BattleMechanics mech - JewelMechanics
		*/
	public void endBattle(int scenario) {
		m_isDone = true;
		// nullify all references to this object
		// in the IoSessions
		// call on clientHandler
		switch (scenario){
		//case 1 = resign
		//case 2 = faint
		//case 3 = win
		//case 4 = someone screwed up
		// case 5- catch
		case 1: 
			m_humanPlayer.getIoSession().write("br");
			m_humanPlayer.initialiseClientParty();
			m_humanPlayer.endBattle();
			break;
		case 2:
			//TODO: Teleport to last used pokecenter
			//TODO: Heal all pokemon in party
			//TODO: Send prize cash to opponent
			//TODO: Code to exit battle
			break;
		case 3:
			m_humanPlayer.initialiseClientParty();
			m_humanPlayer.endBattle();
			break;
		case 4:
			//TODO: something's wrong
			break;
		case 5:
			//Pokemon Capture
			m_humanPlayer.initialiseClientParty();
			m_humanPlayer.endBattle();
			break;
		}
	}

	   /**
	    * Returns the name of the Trainer based on the index of them in the battle
	    * @param int idx - Index of the Trainer (0 = Trainer, 1 = Wild Pokemon)
	    * @return String - Trainer's Name
		*/
	@Override
	public String getTrainerName(int idx) {
		if (idx == 0) return m_humanPlayer.getName();
		else return "Wild  " + m_pokemon[1][0].getName();
	}

	   /**
	    * Informs the trainer a Pokemon fainted
		*/
	@Override
	public void informPokemonFainted(int trainer, int idx) {
		if (trainer == 0 && participatingPokemon.contains(getActivePokemon()[0])) {
			participatingPokemon.remove(getActivePokemon()[0]);
		}
		showMessage("bz" + this.getActivePokemon()[trainer].getName());
	}

	   /**
	    * Informs the player of a health change
		*/
	@Override
	public void informPokemonHealthChanged(Pokemon poke, int change) {
		m_humanPlayer.getIoSession().write("bd;" + 
				poke.getTrainerName() + ";"
				+ poke.getName() + ";" + change);
	}

	   /**
	    * Informs the player of a status change
		*/
	@Override
	public void informStatusApplied(Pokemon poke, StatusEffect eff) {
		char effName = 'a';
		
		if (eff instanceof BurnEffect)
			effName = '0';
		if (eff instanceof PoisonEffect)
			effName = '1';
		if (eff instanceof FreezeEffect)
			effName = '2';
		if (eff instanceof ConfuseEffect)
			effName = '3';
		if (eff instanceof SleepEffect)
			effName = '4';
		if (eff instanceof ParalysisEffect)
			effName = '5';
			
		showMessage("bS;" + poke.getTrainerName() + ";" + effName);
	}

	@Override
	public void informStatusRemoved(Pokemon poke, StatusEffect eff) {
		showMessage("bR;" + poke.getTrainerName());
	}

	   /**
	    * Informs the player a pokemon was swtiched
	    * @param int trainer - The index of the trainer
	    * @param Pokemon poke - The pokemon that is being sent out
		*/
	@Override
	public void informSwitchInPokemon(int trainer, Pokemon poke) {
		if (poke.getParty() == 0)
			participatingPokemon.add(poke);
		m_humanPlayer.getIoSession().write("bs;" +
				getTrainerName(trainer) + ";" +
				poke.getId()
				+ ";" + poke.getName());
	}

	   /**
	    * Informs the player a move is being used
	    * @param Pokemon poke - The pokemon using the move
	    * @param String name - The name of the move
		*/
	@Override
	public void informUseMove(Pokemon poke, String name) {
		showMessage("bm;" + poke.getName() + ";" + name);
	}

	public void positionNewMoves(int[] moveSlots) {

	}
	
	   /**
	    * Informs the player of who won the battle and calls endBattle()
	    * @param int winner - The index of the trainer who won
		*/
	@Override
	public void informVictory(int winner) {
		// Obvious. 'winner' is either player0
		// or 1. 
		endBattle(3);
		if (winner == 0) { // add experience and EVs and money
			int moneyGain = 5;
			m_humanPlayer.setMoney(m_humanPlayer.getMoney() + moneyGain);
			//WRITE CODE FOR NEW BATTLEWINDOW
			showMessage("You got the $" + moneyGain + 
					" in loot that the wild Pokemon was hoarding! ");

			POLRDataEntry pkmnData = m_polrDB.getPokemonData(
					GameServer.getSpeciesData().
					getPokemonByName(this.getParty(1)[0].getSpeciesName()));
			int[] evValues = pkmnData.getEffortPoints();
			for (Pokemon pokemon : participatingPokemon) {
				// first EVs
				for (int i = 0; i < evValues.length; i++) {
					if (pokemon.getEv(i) > 252 || pokemon.getEv(i) < 0)
						pokemon.setEv(i, 252);
				}
				int newEvTotal = 0;
				for (int i = 0; i < evValues.length; i++) {
					if (pokemon.getEv(i) + evValues[i] < 252)
						newEvTotal += pokemon.getEv(i) + evValues[i];
					else
						newEvTotal += 252;
				}
				if(newEvTotal <= 510){
					for (int i = 0; i < evValues.length; i++) {
						if (pokemon.getEv(i) + evValues[i] < 252)
							pokemon.setEv(i, pokemon.getEv(i) + evValues[i]);
					}
				}

				// now exp!
				double expAmt =
					GameServer.getMechanics().calculateExpGain(
							this.getParty(1)[0],
							participatingPokemon.size(), false);
				double expTillLvl = 
					getMechanics().getExpForLevel(
					pokemon, pokemon.getLevel() + 1) 
					- (pokemon.getExp() + expAmt);
				// No more negative figures
				if (expTillLvl < 0){
					expTillLvl = getMechanics().getExpForLevel(
							pokemon, pokemon.getLevel() + 2) 
							- (pokemon.getExp() + expAmt);
					showMessage("e," + expAmt + "," + "Y" + ","+expTillLvl);
				}
				else{
					showMessage("e," + expAmt + "," + "N" + "," + expTillLvl);
				}
				pokemon.setExp(pokemon.getExp() + expAmt);
				int level = getMechanics().calculateLevel(pokemon);
				if ((pokemon.getLevel() != level) && (level!=101)) {
					POLRDataEntry moveData = m_polrDB.getPokemonData(
							GameServer.getSpeciesData().
							getPokemonByName(pokemon.getSpeciesName()));
					m_humanPlayer.getIoSession().write("n," + pokemon.getId() + "," + level);
					pokemon.setHappiness(pokemon.getHappiness() + 2);
					
					//check for possible evolutions
					for (int i = 0; i < moveData.getEvolutions().size(); i++){
						if (moveData.getEvolutions().get(i).getLevel() <= level){
							if(moveData.getEvolutions().get(i).getType() 
									== EvoTypes.Level){
								StringBuilder evoPacket = new StringBuilder("bE,"
										+ pokemon.getName());
								//stop hardcoding party
								this.getParty(0)[pokemon.getId()] = pokemon.evolve(pokemon, 
										GameServer.getSpeciesData()
										.getSpecies(GameServer
												.getSpeciesData()
												.getPokemonByName(moveData
														.getEvolutions().get(i)
														.getEvolveTo())));
								pokemon = this.getParty(0)[pokemon.getId()];
								evoPacket.append(",");
								evoPacket.append(GameServer.getSpeciesData()
										.getSpecies(GameServer.getSpeciesData()
												.getPokemonByName(moveData.getEvolutions()
														.get(i).getEvolveTo())).m_name);
								//Add entry to pokedex
								m_humanPlayer.setSeen(GameServer.getSpeciesData().getPokemonByName(pokemon.getSpeciesName()));
								m_humanPlayer.setCaught(GameServer.getSpeciesData().getPokemonByName(pokemon.getSpeciesName()));
								System.out.println(this.getParty(1)[0].m_name);
								System.out.println(evoPacket);
								m_humanPlayer.getIoSession().write(
										evoPacket.toString());
							}
						}
					}
					moveData = m_polrDB.getPokemonData(
							GameServer.getSpeciesData().
							getPokemonByName(pokemon.getSpeciesName()));
					
					int oldLevel = pokemon.getLevel();
					pokemon.setLevel(level);
					pokemon.calculateStats(false);
					
					pokemon = this.getParty(0)[pokemon.getId()];
					pokemon.getMovesLearning().clear();
					StringBuilder moveLearnPacket = new StringBuilder("bM,"
							+ pokemon.getId());
					for (int i = oldLevel + 1; i <= level; i++) {
						if (moveData.getMoves().get(i) != null) {
							informSwitchInPokemon(0, pokemon);
							String polrMove = moveData.getMoves().get(i);

							pokemon.getMovesLearning().add(
									polrMove);
							moveLearnPacket.append(",");
							moveLearnPacket.append(polrMove);

						}
						if (moveLearnPacket.length() > 1)
							m_humanPlayer.getIoSession().write(
									moveLearnPacket.toString());
					}
				}
			}
			m_humanPlayer.initialiseClientParty();
		} else {
			m_humanPlayer.lostBattle();
		}
		m_humanPlayer.getIoSession().write("bf" + this.getTrainerName(winner));
	}

	   /**
	    * Called when the trainer runs from battle
		*/
	public void run() {
		if (m_queuedTurns[0] == null) {
			if (canRun()) { // can we run?
				showMessage("Got away safely!");
				endBattle(1);
			} else {
				showMessage("Can't escape!");
				try {
					queueMove(0, BattleTurn.getMoveTurn(-1));}
				catch (MoveQueueException e) { 
					// we're screwed
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * canRun formula
	 */
	private boolean canRun(){
			// Formula from http://bulbapedia.bulbagarden.net/wiki/escape
			float A = getActivePokemon()[0].getStat(Pokemon.S_SPEED);
			float B = getActivePokemon()[1].getStat(Pokemon.S_SPEED);
			int C = ++runCount;
			
			float F = (((A * 32) / (B / 4)) + 30) * C;
			
			if(F > 255) return true;
			
			if(getMechanics().getRandom().nextInt(255) <= F) return true;
			
			return false;
	}
	
	   /**
	    * Queues a move and then if the other player has selected their move, the moves get executed
	    * @param int participant - The person using the move
	    * @param BattleTurn move - The move, either an attack or a switch in Pokemon
		*/
	@Override
	public void queueMove(int participant, BattleTurn move)
	throws MoveQueueException {
		// Not entirely sure what this is.
		// I think we may need to handle storing
		// a created BattleTurn here. ??
		// It doesn't seem to get called.
		if (m_queuedTurns[participant] == null) {
			if (move.getId() == -1) {
				if (participant == 0 && m_queuedTurns[1] != null)
					executeTurn(m_queuedTurns);
			} else {
				if (this.getActivePokemon()[participant].isFainted()) {
					if (!move.isMoveTurn()) {
						this.switchInPokemon(participant, move.getId());
						requestMoves();
					} else {
						if (participant == 0) {
							if (participatingPokemon.contains(getActivePokemon()[0]))
								participatingPokemon.remove(getActivePokemon()[0]);
							m_humanPlayer.getIoSession().write("bAs");
						}
					}
				} else {
					if (move.isMoveTurn()) {
						if (getActivePokemon()[participant].mustStruggle())
							m_queuedTurns[participant] = BattleTurn.getMoveTurn(-1);
						else {
							if (this.getActivePokemon()[participant].getPp(move.getId()) <= 0) {

								if (participant == 0) {
									m_humanPlayer.getIoSession().write("bA");
									m_humanPlayer.getIoSession().write("s!Sorry, the move " + 
											this.getActivePokemon()[participant].getMoveName(
													move.getId()) + " has no PP left. " +
									"Select a different move.");
								}
							} else {
								m_queuedTurns[participant] = move;
							}
						}
					} else {
						if (this.m_pokemon[participant][move.getId()].isActive()) {
							m_queuedTurns[participant] = move;

						} else {
							if (participant == 0) {
								m_humanPlayer.getIoSession().write("bA");

							}
						}
					}
				}
			}
		} 
		if (m_queuedTurns[0] != null &&
				m_queuedTurns[1] != null) {
			executeTurn(m_queuedTurns);
			if(participant==0 && move.isMoveTurn()) {
				m_humanPlayer.getIoSession().write("bp!" + move.getId() +
						"!" + this.getActivePokemon()[participant].getPp(move.getId()));
				}
		}

	}

	   /**
	    * Clears all moves queued for execution
		*/
	public void clearQueue() {
		m_queuedTurns[0] = null;
		m_queuedTurns[1] = null;
	}

	@Override
	public void refreshActivePokemon() {
		// TODO Add code

		// I *think* this is meant for us
		// to send the info for whatever
		// Pokes have been switched in
		// to both clients.

	}

	@Override
	public void requestAndWaitForSwitch(int party) {
		// TODO ?? what is this?

	}

	   /**
	    * Requests a move from the player and sets the next move for the Wild Pokemon
		*/
	@Override
	protected void requestMoves() {
		// Happens every turn, ask for a move
		// from each player.

		// Doesn't get called if a Pokemon faints.

		// Think we need to get BattleTurns out of
		// what the client sends back to us,
		// then call executeTurn() on those.
		if (this.getActivePokemon()[0].isActive() &&
				this.getActivePokemon()[1].isActive() && !m_isDone) {
			m_humanPlayer.getIoSession().write("ba");
			

			try {
				int moveID = getMechanics().getRandom().nextInt(4);
				while (getActivePokemon()[1].getMove(moveID) == null)
					moveID = getMechanics().getRandom().nextInt(4);
				queueMove(1, BattleTurn.getMoveTurn(
						moveID));
			} catch (MoveQueueException x) {
				x.printStackTrace();
			}
		}
	}

	   /**
	    * Called when a Pokemon faints, sends a request to the player for another Pokemon
	    * @param int i = index of the player
		*/
	@Override
	protected void requestPokemonReplacement(int i) {
		// A Pokemon has fainted, 'i' indicates
		// whether it was Player 1 or 0's.

		// We need to solicit a new Pokemon to put in.

		// Should probably call replaceFaintedPokemon()
		// on getting that.

		try {
			if (this.getAliveCount(i) > 0 && i == 0)
				m_humanPlayer.getIoSession().write("b?");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	   /**
	    * Sends a message to the player's BattleWindow
	    * @param String message
		*/
	@Override
	public void showMessage(String message) {
		// This method sends messages such as "It was super-effective!"
		m_humanPlayer.getIoSession().write(message);
	}

}
