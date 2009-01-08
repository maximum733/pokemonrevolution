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
import java.util.Random;
import java.util.Set;

import polr.server.GameServer;
import polr.server.database.POLRDataEntry;
import polr.server.database.POLRDatabase;
import polr.server.database.POLREvolution.EvoTypes;
import polr.server.mechanics.BattleMechanics;
import polr.server.mechanics.statuses.BurnEffect;
import polr.server.mechanics.statuses.FlinchEffect;
import polr.server.mechanics.statuses.FreezeEffect;
import polr.server.mechanics.statuses.PoisonEffect;
import polr.server.mechanics.statuses.SleepEffect;
import polr.server.mechanics.statuses.StatusEffect;
import polr.server.npc.NonPlayerChar;
import polr.server.player.PlayerChar;

public class NPCBattleField extends BattleField {
       private Pokemon [] enemyParty;
       private String enemyName;
       private PlayerChar m_humanPlayer;
       private NonPlayerChar opponent;
       BattleTurn[] m_queuedTurns = new BattleTurn[2];

       Set<Pokemon> participatingPokemon
       = new LinkedHashSet<Pokemon>();

       private POLRDatabase m_polrDB;

       private boolean m_isDone = false;

       public NPCBattleField(BattleMechanics mech, Pokemon[] party, String name, PlayerChar player, NonPlayerChar t) {
               super(mech, new Pokemon[][] {
                               player.getParty(),
                                party});
               enemyName = name;
               m_humanPlayer = player;
               enemyParty = party;
               opponent = t;
               beginBattle();
       }

       private void beginBattle() {
               try {
                       showMessage(enemyName + " challenged you to a battle!");
                       participatingPokemon.add(getParty(0)[0]);
                       showMessage(m_humanPlayer.getName() + " sent out " + getParty(0)[0].getName() + ".");
               
                       showMessage(enemyName + " sent out " + enemyParty[0].getName() + ".");
                       requestMoves();
               } catch (Exception e) {
                       String message = "The battle cannot begin because "
                               + "a team has a problem:\n\n"
                               + e.getMessage();
                       showMessage(message);
                       endBattle(4);
               }
       }
       
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
                       m_humanPlayer.endBattle();
                       break;
               case 4:
                       //TODO: something's wrong
                       break;
               case 5:
                       m_humanPlayer.initialiseClientParty();
                       m_humanPlayer.endBattle();
                       break;
               }
       }
       
       public NPCBattleField(BattleMechanics mech) {
               super(mech);
       }

       @Override
       public String getTrainerName(int idx) {
               if (idx == 0) return m_humanPlayer.getName();
               else return enemyName;
       }

       @Override
       public void informPokemonFainted(int trainer, int idx) {
               if (trainer == 0 && participatingPokemon.contains(getActivePokemon()[0])) {
                       participatingPokemon.remove(getActivePokemon()[0]);
               }
               else {
                       participatingPokemon.remove(getActivePokemon()[1]);
               }
               if (trainer == 1) {
                       POLRDataEntry pkmnData = GameServer.getPOLRDB().getPokemonData(
                                       GameServer.getSpeciesData().
                                       getPokemonByName(this.getParty(trainer)[idx].getSpeciesName()));
                       int[] evValues = pkmnData.getEffortPoints();
                       for (Pokemon pokemon : participatingPokemon) {
                               // first EVs
                               for (int i = 0; i < evValues.length; i++) {
                                       pokemon.setEv(i, pokemon.getEv(i) + evValues[i]);
                               }
                               
                               // now exp!
                               double expAmt =
                                       GameServer.getMechanics().calculateExpGain(
                                                       this.getParty(trainer)[idx],
                                                       participatingPokemon.size(), false);
                               showMessage(pokemon.getName() + " gained " + (int)expAmt +
                               " experience points.");
                               double expTillLvl =
                                       getMechanics().getExpForLevel(
                                       pokemon, pokemon.getLevel() + 1)
                                       - (pokemon.getExp() + expAmt);
                               // No more negatives
                               if (expTillLvl < 0){
                                       expTillLvl = 0;
                               }
                               showMessage("It needs " +
                                               (int)expTillLvl
                                               + " more points to level up!");
                               pokemon.setExp(pokemon.getExp() + expAmt);
                               int level = getMechanics().calculateLevel(pokemon);
                               if (pokemon.getLevel() != level) {
                                       POLRDataEntry moveData = GameServer.getPOLRDB().getPokemonData(
                                                       GameServer.getSpeciesData().
                                                       getPokemonByName(pokemon.getSpeciesName()));
                                       m_humanPlayer.getIoSession().write("n," + pokemon.getId() + "," + level);
                                       pokemon.setHappiness(pokemon.getHappiness() + 2);
                                       for (int i = 0; i < moveData.getEvolutions().size(); i++){
                                               if (moveData.getEvolutions().get(i).getLevel() == level){
                                                       //System.out.println(pokemon.m_name + " evolves at level 16");
                                                       if(moveData.getEvolutions().get(i).getType()
                                                                       == EvoTypes.Level){
                                                               StringBuilder evoPacket = new StringBuilder("e,"
                                                                               + pokemon.getName());
                                                               //stop hardcoding party
                                                               this.getParty(0)[pokemon.getId()] = pokemon.evolve(pokemon,
                                                                               GameServer.getSpeciesData()
                                                                               .getSpecies(GameServer.getSpeciesData()
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

                                       moveData = GameServer.getPOLRDB().getPokemonData(
                                                       GameServer.getSpeciesData().
                                                       getPokemonByName(pokemon.getSpeciesName()));
                                       
                                       int oldLevel = pokemon.getLevel();
                                       pokemon.setLevel(level);
                                       pokemon.calculateStats(false);
                                       
                                       pokemon = this.getParty(0)[pokemon.getId()];
                                       pokemon.getMovesLearning().clear();
                                       StringBuilder moveLearnPacket = new StringBuilder("l,"
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
                       participatingPokemon.clear();
               }
               showMessage("!" + this.getActivePokemon()[trainer].getName() + " fainted!");
       }

       @Override
       public void informPokemonHealthChanged(Pokemon poke, int change) {
               m_humanPlayer.getIoSession().write("H;" +
                               poke.getTrainerName() + ";"
                               + poke.getName() + ";" + change);
       }

       @Override
       public void informStatusApplied(Pokemon poke, StatusEffect eff) {
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
               if (poke.getParty() == 0)
                       participatingPokemon.add(poke);
               m_humanPlayer.getIoSession().write("p;" +
                               getTrainerName(trainer) + ";" +
                               poke.getId()
                               + ";" + poke.getName());
       }

       @Override
       public void informUseMove(Pokemon poke, String name) {
               if (poke.getParty() == 0)
                       showMessage(poke.getTrainerName() + "'s " + poke.getName() + " used " + name + ".");
               else
                       showMessage(enemyName + "'s " + poke.getName() + " used " + name + ".");
       }

       @Override
       public void informVictory(int winner) {
               endBattle(3);
               clearQueue();
               if (winner == 0) { // add experience and EVs and money
                       int moneyGain = m_humanPlayer.getHighestLevel() + 10;
                       m_humanPlayer.setMoney(m_humanPlayer.getMoney() + moneyGain);
                       showMessage("You won $" + moneyGain +
                                       " for winning the battle! ");
                       
                               POLRDataEntry pkmnData = GameServer.getPOLRDB().getPokemonData(
                                               GameServer.getSpeciesData().
                                               getPokemonByName(this.getParty(1)[0].getSpeciesName()));
                               int[] evValues = pkmnData.getEffortPoints();
                               for (Pokemon pokemon : participatingPokemon) {
                                       // first EVs
                                       for (int i = 0; i < evValues.length; i++) {
                                               pokemon.setEv(i, pokemon.getEv(i) + evValues[i]);
                                       }

                                       // now exp!
                                       double expAmt =
                                               GameServer.getMechanics().calculateExpGain(
                                                               this.getParty(1)[0],
                                                               participatingPokemon.size(), false);
                                       showMessage(pokemon.getName() + " gained " + (int)expAmt +
                                       " experience points.");
                                       double expTillLvl = GameServer.getMechanics().getExpForLevel(pokemon, pokemon.getLevel() + 1) - (pokemon.getExp() + expAmt);
                                       // No more negatives
                                       if (expTillLvl < 0){
                                               expTillLvl = 0;
                                       }
                                       showMessage("It needs " +
                                                       (int)expTillLvl
                                                       + " more points to level up!");
                                       pokemon.setExp(pokemon.getExp() + expAmt);
                                       int level = getMechanics().calculateLevel(pokemon);
                                       if (pokemon.getLevel() != level) {
                                               POLRDataEntry moveData = GameServer.getPOLRDB().getPokemonData(
                                                               GameServer.getSpeciesData().
                                                               getPokemonByName(pokemon.getSpeciesName()));
                                               m_humanPlayer.getIoSession().write("n," + pokemon.getId() + "," + level);
                                               pokemon.setHappiness(pokemon.getHappiness() + 2);
                                               for (int i = 0; i < moveData.getEvolutions().size(); i++){
                                                       if (moveData.getEvolutions().get(i).getLevel() == level){
                                                               //System.out.println(pokemon.m_name + " evolves at level 16");
                                                               if(moveData.getEvolutions().get(i).getType()
                                                                               == EvoTypes.Level || (moveData.getEvolutions().get(i).getType() == EvoTypes.Happiness && pokemon.getHappiness() >=220)){
                                                                       StringBuilder evoPacket = new StringBuilder("e,"
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

                                               moveData = GameServer.getPOLRDB().getPokemonData(
                                                               GameServer.getSpeciesData().
                                                               getPokemonByName(pokemon.getSpeciesName()));

                                               int oldLevel = pokemon.getLevel();
                                               pokemon.setLevel(level);
                                               pokemon.calculateStats(false);

                                               pokemon = this.getParty(0)[pokemon.getId()];
                                               pokemon.getMovesLearning().clear();
                                               StringBuilder moveLearnPacket = new StringBuilder("l,"
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
                       //opponent.battleWon(m_humanPlayer);
               } else {
                       int moneyGain = m_humanPlayer.getHighestLevel() + 2;
                       m_humanPlayer.setMoney(m_humanPlayer.getMoney() - moneyGain);
                       showMessage("You lost $" + moneyGain);
                       m_humanPlayer.lostBattle();
               }
               m_humanPlayer.getIoSession().write("v" + this.getTrainerName(winner));
       }

       @Override
       public void queueMove(int trainer, BattleTurn move) throws MoveQueueException {
               if (m_queuedTurns[trainer] == null) {
                       if (move.getId() == -1) {
                               if(trainer == 0 && m_queuedTurns[1] != null)
                                       executeTurn(m_queuedTurns);
                               else {
                                       m_queuedTurns[trainer] = move;
                               }
                       } else {
                               if (this.getActivePokemon()[trainer].isFainted()) {
                                       if (!move.isMoveTurn()) {
                                               this.switchInPokemon(trainer, move.getId());
                                               requestMoves();
                                       } else {
                                               if (participatingPokemon.contains(getActivePokemon()[trainer]))
                                                       participatingPokemon.remove(getActivePokemon()[trainer]);
                                               if (trainer == 0) {
                                                       m_humanPlayer.getIoSession().write("Ms");
                                               }
                                       }
                               } else {
                                       if (move.isMoveTurn()) {
                                               if (getActivePokemon()[trainer].mustStruggle())
                                                       m_queuedTurns[trainer] = BattleTurn.getMoveTurn(-1);
                                               else {
                                                       if (this.getActivePokemon()[trainer].getPp(move.getId()) <= 0) {


                                                               if (trainer == 0) {
                                                                       m_humanPlayer.getIoSession().write("M");
                                                                       m_humanPlayer.getIoSession().write("s!Sorry, the move " +
                                                                                       this.getActivePokemon()[trainer].getMoveName(
                                                                                                       move.getId()) + " has no PP left. " +
                                                                       "Select a different move.");
                                                               }
                                                       } else {
                                                               m_queuedTurns[trainer] = move;
                                                       }
                                               }
                                       } else {
                                               if (this.m_pokemon[trainer][move.getId()].isActive()) {
                                                       m_queuedTurns[trainer] = move;
                                               } else {
                                                       if (trainer == 0)
                                                               m_humanPlayer.getIoSession().write("M");
                                               }
                                       }
                               }
                       }
               }
               if(m_queuedTurns[1] == null && trainer == 0 && move.isMoveTurn()) {
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
 
               if (m_queuedTurns[0] != null &&
                               m_queuedTurns[1] != null) {
                   executeTurn(m_queuedTurns);
                   if(trainer == 0 && move.isMoveTurn()) {
                 		m_humanPlayer.getIoSession().write("g!" + move.getId() +
                 				"!" + this.getActivePokemon()[trainer].getPp(move.getId()));
                 	}
               }
               
               System.out.println(m_humanPlayer.getName() + " " + m_queuedTurns[0] + " " + enemyName + " " + m_queuedTurns[1]);
       }
       
       public void clearQueue() {
               m_queuedTurns[0] = null;
               m_queuedTurns[1] = null;
       }
       
       public int getNextEnemyPokemon() {
               Random gen = new Random(6);
               int i = gen.nextInt(6);
               if(enemyParty[i] != null) {
                   if(enemyParty[i].getHealth() > 0) {
                           return i;
                   }
                   else {
                           while(enemyParty[i] == null || enemyParty[i].getHealth() == 0) {
                                   i = gen.nextInt(5);
                                   if(enemyParty[i] != null) {
                                           if(enemyParty[i].getHealth() > 0)
                                                   return i;
                                   }
                           }
                   }
           }

               return 0;
       }

       @Override
       public void refreshActivePokemon() {
               // TODO Auto-generated method stub
               
       }

       @Override
       public void requestAndWaitForSwitch(int party) {
               // TODO Auto-generated method stub
               
       }

       @Override
       protected void requestMoves() {
               // TODO Auto-generated method stub
               if (this.getActivePokemon()[0].isActive() &&
                               this.getActivePokemon()[1].isActive() && !m_isDone) {
                       m_humanPlayer.getIoSession().write("M");

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

       @Override
       protected void requestPokemonReplacement(int i) {
               try {
                       if (this.getAliveCount(i) > 0 && i == 0)
                               m_humanPlayer.getIoSession().write("Ms");
                       else if(this.getAliveCount(i) > 0 && i == 1) {
                               this.switchInPokemon(1, BattleTurn.getSwitchTurn(getNextEnemyPokemon()).getId());
                               m_queuedTurns[0] = null;
                               requestMoves();
                       }
               } catch (NullPointerException e) {
                       e.printStackTrace();
               }
       }

       @Override
       public void showMessage(String message) {
               m_humanPlayer.getIoSession().write("s" + message);
       }

}
