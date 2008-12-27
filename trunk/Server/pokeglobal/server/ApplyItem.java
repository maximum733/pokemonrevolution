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

package pokeglobal.server;

import pokeglobal.server.battle.BattleField;
import pokeglobal.server.battle.BattleTurn;
import pokeglobal.server.battle.MoveQueueException;
import pokeglobal.server.battle.WildBattleField;
import pokeglobal.server.mechanics.statuses.BurnEffect;
import pokeglobal.server.mechanics.statuses.FreezeEffect;
import pokeglobal.server.mechanics.statuses.ParalysisEffect;
import pokeglobal.server.mechanics.statuses.PoisonEffect;
import pokeglobal.server.mechanics.statuses.SleepEffect;
import pokeglobal.server.object.PlayerChar;
/*
 * This handles item application serverside 
 */
public class ApplyItem {
	public boolean useItem(PlayerChar p, int itemID, String otherInfo) {
		String [] info;
		BattleField field = p.getField();
		//Player is battling
		try {
			switch(itemID) {
			case 0:
				break;
			case 1:
				//Master Ball
				if(p.isBattling()) {
					if (field instanceof WildBattleField) {
						if (field.getMechanics().isCaught(field.getActivePokemon()[1],
								GameServer.getPOLRDB().getPokemonData(
										GameServer.getSpeciesData().getPokemonByName(
												(field.getActivePokemon()[1].
														getSpeciesName()))).
														getRareness(), 255, 1)) {
							p.getIoSession().write("bc1");
							try {
								p.catchPokemon(field.getActivePokemon()[1]);
								
							} catch (Exception e) {
								p.getIoSession().write("bc2");
							} finally {
								((WildBattleField)(field)).endBattle(5);
								p.getIoSession().write("bc3");
							}
						} else {
							p.getIoSession().write("bc4");
							try {
								field.queueMove(0, BattleTurn.getMoveTurn(-1));
							} catch (MoveQueueException e) {
								e.printStackTrace();
							}
						}
						return true;
					} else {
						p.getIoSession().write("ba");
						return false;
					}
				}
				return false;
			case 2:
				//Ultra Ball
				if(p.isBattling()) {
					if (field instanceof WildBattleField) {
						if (field.getMechanics().isCaught(field.getActivePokemon()[1],
								GameServer.getPOLRDB().getPokemonData(
										GameServer.getSpeciesData().getPokemonByName(
												(field.getActivePokemon()[1].
														getSpeciesName()))).
														getRareness(), 2, 1)) {
							p.getIoSession().write("bc1");
							try {
								p.catchPokemon(field.getActivePokemon()[1]);
								
							} catch (Exception e) {
								p.getIoSession().write("bc2");
							} finally {
								((WildBattleField)(field)).endBattle(5);
								p.getIoSession().write("bc3");
							}
						} else {
							p.getIoSession().write("bc4");
							try {
								field.queueMove(0, BattleTurn.getMoveTurn(-1));
							} catch (MoveQueueException e) {
								e.printStackTrace();
							}
						}
						return true;
					} else {
						p.getIoSession().write("ba");
						return false;
					}
				}
				return false;
			case 3:
				//Great Ball
				if(p.isBattling()) {
					if (field instanceof WildBattleField) {
						if (field.getMechanics().isCaught(field.getActivePokemon()[1],
								GameServer.getPOLRDB().getPokemonData(
										GameServer.getSpeciesData().getPokemonByName(
												(field.getActivePokemon()[1].
														getSpeciesName()))).
														getRareness(), 1.5, 1)) {
							p.getIoSession().write("bc1");
							try {
								p.catchPokemon(field.getActivePokemon()[1]);
								
							} catch (Exception e) {
								p.getIoSession().write("bc2");
							} finally {
								((WildBattleField)(field)).endBattle(5);
								p.getIoSession().write("bc3");
							}
						} else {
							p.getIoSession().write("bc4");
							try {
								field.queueMove(0, BattleTurn.getMoveTurn(-1));
							} catch (MoveQueueException e) {
								e.printStackTrace();
							}
						}
						return true;
					} else {
						p.getIoSession().write("ba");
						return false;
					}
				}
				return false;
			case 4:
				//Pokeball
				if(p.isBattling()) {
					if (field instanceof WildBattleField) {
						if (field.getMechanics().isCaught(field.getActivePokemon()[1],
								GameServer.getPOLRDB().getPokemonData(
										GameServer.getSpeciesData().getPokemonByName(
												(field.getActivePokemon()[1].
														getSpeciesName()))).
														getRareness(), 1, 1)) {
							p.getIoSession().write("bc1");
							try {
								p.catchPokemon(field.getActivePokemon()[1]);
								
							} catch (Exception e) {
								p.getIoSession().write("bc2");
							} finally {
								((WildBattleField)(field)).endBattle(5);
								p.getIoSession().write("bc3");
							}
						} else {
							p.getIoSession().write("bc4");
							try {
								field.queueMove(0, BattleTurn.getMoveTurn(-1));
							} catch (MoveQueueException e) {
								e.printStackTrace();
							}
						}
						return true;
					} else {
						p.getIoSession().write("ba");
						return false;
					}
				}
				return false;
			case 5:
				//Potion
				//POKEMONINDEX
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].changeHealth(20);
				p.updateClientPartyByIndex(Integer.parseInt(info[0]));
				return true;
			case 6:
				//Antidote
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new PoisonEffect());
				return true;
			case 7:
				//Burn Heal
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new BurnEffect());
				return true;
			case 8:
				//Ice Heal
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new FreezeEffect());
				return true;
			case 9:
				//Awakening
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new SleepEffect());
				return true;
			case 10:
				//Paralyz Heal
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new ParalysisEffect());
				return true;
			case 11:
				//Full Restore
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new ParalysisEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new SleepEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new FreezeEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new BurnEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new PoisonEffect());
				p.getParty()[Integer.parseInt(info[0])].changeHealth(p.getParty()[Integer.parseInt(info[0])].getStat(0) - p.getParty()[Integer.parseInt(info[0])].getHealth());
				p.updateClientPartyByIndex(Integer.parseInt(info[0]));
				return true;
			case 12:
				//Max Potion
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].changeHealth(p.getParty()[Integer.parseInt(info[0])].getStat(0) - p.getParty()[Integer.parseInt(info[0])].getHealth());
				p.updateClientPartyByIndex(Integer.parseInt(info[0]));
				return true;
			case 13:
				//Hyper Potion
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].changeHealth(200);
				p.updateClientPartyByIndex(Integer.parseInt(info[0]));
				return true;
			case 14:
				//Super Potion
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].changeHealth(50);
				p.updateClientPartyByIndex(Integer.parseInt(info[0]));
				return true;
			case 15:
				//Full Heal
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new ParalysisEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new SleepEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new FreezeEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new BurnEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new PoisonEffect());
				p.updateClientPartyByIndex(Integer.parseInt(info[0]));
				return true;
			case 16:
				//Revive
				info = otherInfo.split(",");
				if(p.getParty()[Integer.parseInt(info[0])].isFainted()) {
					p.getParty()[Integer.parseInt(info[0])].calculateStats(true);
					p.getParty()[Integer.parseInt(info[0])].reinitialise(GameServer.getMechanics());
					p.getParty()[Integer.parseInt(info[0])].changeHealth(-(p.getParty()[Integer.parseInt(info[0])].getHealth() / 2));
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 17:
				//Max Revive
				info = otherInfo.split(",");
				if(p.getParty()[Integer.parseInt(info[0])].isFainted()) {
					p.getParty()[Integer.parseInt(info[0])].calculateStats(true);
					p.getParty()[Integer.parseInt(info[0])].reinitialise(GameServer.getMechanics());
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 18:
				//Rare Candy
				info = otherInfo.split(",");
				if(!p.isBattling()) {
					p.getParty()[Integer.parseInt(info[0])].setLevel(p.getParty()[Integer.parseInt(info[0])].getLevel() + 1);
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 19:
				//Lava Cookie
				info = otherInfo.split(",");
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new ParalysisEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new SleepEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new FreezeEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new BurnEffect());
				p.getParty()[Integer.parseInt(info[0])].removeStatus(new PoisonEffect());
				return true;
			//ALL ITEMS FROM 20 to 70 are TMs, CURRENTLY BASED ON GEN 3
			case 20:
				//TM01 - FOCUS PUNCH
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Focus Punch");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 21:
				//TM02 - DRAGON CLAW
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Dragon Claw");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 22:
				//TM03 - WATER PULSE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Water Pulse");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 23:
				//TM04 - CALM MIND
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Calm Mind");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 24:
				//TM05 - ROAR
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Roar");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 25:
				//TM06 - TOXIC
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Toxic");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 26:
				//TM07 - HAIL
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Hail");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 27:
				//TM08 - BULK UP
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Bulk Up");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 28:
				//TM09 - BULLET SEED
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Bullet Seed");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 29:
				//TM10 - HIDDEN POWER
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Hidden Power");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 30:
				//TM11 - SUNNY DAY
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Sunny Day");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 31:
				//TM12 - TAUNT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Taunt");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 32:
				//TM13 - ICE BEAM
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Ice Beam");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 33:
				//TM14 - BLIZZARD
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Blizzard");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 34:
				//TM15 - HYPER BEAM
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Hyper Beam");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 35:
				//TM16 - LIGHT SCREEN
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Light Screen");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 36:
				//TM17 - PROTECT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Protect");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 37:
				//TM18 - RAIN DANCE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Rain Dance");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 38:
				//TM19 - GIGA DRAIN
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Giga Drain");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 39:
				//TM20 - SAFEGUARD
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Safeguard");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 40:
				//TM21 - FRUSTRATION
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Frustration");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 41:
				//TM22 - SOLARBEAM
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Solarbeam");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 42:
				//TM23 - IRON TAIL
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Iron Tail");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 43:
				//TM24 - THUNDERBOLT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Thunderbolt");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 44:
				//TM25 - THUNDER
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Thunder");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 45:
				//TM26 - EARTHQUAKE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Earthquake");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 46:
				//TM27 - RETURN
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Return");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 47:
				//TM28 - DIG
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Dig");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 48:
				//TM29 - PSYCHIC
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Psychic");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 49:
				//TM30 - SHADOW BALL
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Shadow Ball");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 50:
				//TM31 - BRICK BREAK
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Brick Break");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 51:
				//TM32 - DOUBLE TEAM
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Double Team");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 52:
				//TM33 - REFLECT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Reflect");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 53:
				//TM34 - SHOCK WAVE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Shock Wave");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 54:
				//TM35 - FLAMETHROWER
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Flamethrower");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 55:
				//TM36 - SLUDGE BOMB
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Sludge Bomb");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 56:
				//TM37 - SANDSTORM
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Sandstorm");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 57:
				//TM38 - FIRE BLAST
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Fire Blast");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 58:
				//TM39 - ROCK TOMB
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Rock Tomb");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 59:
				//TM40 - AERIAL ACE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Aerial Ace");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 60:
				//TM41 - TORMENT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Torment");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 61:
				//TM42 - FACADE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Facade");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 62:
				//TM43 - SECRET POWER
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Secret Power");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 63:
				//TM44 - REST
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Rest");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 64:
				//TM45 - ATTRACT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Attract");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 65:
				//TM46 - THIEF
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Thief");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 66:
				//TM47 - Steel Wing
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Steel Wing");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 67:
				//TM48 - Skill Swap
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Skill Swap");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 68:
				//TM49 - SNATCH
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Snatch");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 69:
				//TM50 - OVERHEAT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Overheat");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 70:
				//TM51 - ROOST
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Roost");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 71:
				//TM52 - FOCUS BLAST
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Focus Blast");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 72:
				//TM53 - ENERGY BALL
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Energy Ball");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 73:
				//TM54 - FALSE SWIPE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "False Swipe");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 74:
				//TM55 - BRINE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Brine");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 75:
				//TM56 - FLING
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Fling");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 76:
				//TM57 - CHARGE BEAM
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Charge Beam");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 77:
				//TM58 - ENDURE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Endure");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 78:
				//TM59 - DRAGON PULSE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Dragon Pulse");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 79:
				//TM60 - DRAIN PUNCH
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Drain Punch");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 80:
				//TM61 - WILL-O-WISP
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Will-o-wisp");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 81:
				//TM62 - SILVER WIND
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Silver Wind");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 82:
				//TM63 - EMBARGO
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Embargo");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 83:
				//TM64 - EXPLOSION
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Explosion");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 84:
				//TM65 - SHADOW CLAW
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Shadow Claw");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 85:
				//TM66 - PAYBACK
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Payback");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 86:
				//TM67 - RECYCLE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Recycle");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 87:
				//TM68 - GIGA IMPACT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Giga Impact");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 88:
				//TM69 - ROCK POLISH
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Rock Polish");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 89:
				//TM70 - FLASH
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Flash");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 90:
				//TM71 - STONE EDGE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Stone Edge");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 91:
				//TM72 - AVALANCHE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Avalanche");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 92:
				//TM73 - THUNDER WAVE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Thunder Wave");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 93:
				//TM74 - GYRO BALL
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Gyro Ball");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 94:
				//TM75 - SWORDS DANCE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Swords Dance");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 95:
				//TM76 - STEALTH ROCK
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Stealth Rock");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 96:
				//TM77 - PSYCH UP
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Psych Up");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 97:
				//TM78 - CAPTIVATE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Captivate");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 98:
				//TM79 - DARK PULSE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Dark Pulse");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 99:
				//TM80 - ROCK SLIDE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Rock Slide");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 100:
				//TM81 - X-SCISSOR
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "X-Scissor");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 101:
				//TM82 - SLEEP TALK
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Sleep Talk");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 102:
				//TM83 - NATURAL GIFT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Natural Gift");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 103:
				//TM84 - POISON JAB
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Poison Jab");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 104:
				//TM85 - DREAM EATER
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Dream Eater");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 105:
				//TM86 - GRASS KNOT
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Grass Knot");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 106:
				//TM87 - SWAGGER
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Swagger");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 107:
				//TM88 - PLUCK
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Pluck");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 108:
				//TM89 - U-TURN
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "U-Turn");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 109:
				//TM90 - SUBSTITUTE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Substitute");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 110:
				//TM91 - FLASH CANNON
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Flash Cannon");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 111:
				//TM92 - TRICK ROOM
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					p.getParty()[Integer.parseInt(info[0])].learnMove(Integer.parseInt(info[1]), "Trick Room");
					p.updateClientPartyByIndex(Integer.parseInt(info[0]));
					return true;
				}
				return false;
			case 112:
				//FIRESTONE
				//Ensure the Pokemon is one that requires it to evolve
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Vulpix")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Ninetales")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Growlithe")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Arcanine")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Eevee")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Flareon")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					}
				}
				return false;
			case 113:
				//WATERSTONE
				//Ensure the Pokemon is one that requires it to evolve
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Poliwhirl")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Poliwrath")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Shellder")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Cloyster")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Staryu")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Starmie")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Eevee")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Vaporeon")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Lombre")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Ludicolo")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					}
				}
				return false;
			case 114:
				//THUNDERSTONE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Pikachu")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Riachu")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Eevee")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Jolteon")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					}
				}
				return false;
			case 115:
				//LEAF STONE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Gloom")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Vileplume")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Weepinbell")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Victreebel")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Exeggcute")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Exeggutor")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Nuzleaf")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Shiftry")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					}
				}
				return false;
			case 116:
				//MOONSTONE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Nidorina")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Nidoqueen")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Nidorino")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Nidoking")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Clefairy")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Clefable")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Jigglypuff")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Wigglytuff")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Skitty")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Delcatty")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} 
				}
				return false;
			case 117:
				//SUNSTONE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Gloom")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Bellossom")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Sunkern")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Sunflora")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					}
				}
				return false;
			case 118:
				//SHINY STONE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Togetic")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Togekiss")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Roselia")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Roserade")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					}
				}
				return false;
			case 119:
				//DUSK STONE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Murkrow")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Honchkrow")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Misdreavus")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Mismagius")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					}
				}
				return false;
			case 120:
				//DAWN STONE
				if(!p.isBattling()) {
					info = otherInfo.split(",");
					int i = Integer.parseInt(info[0]);
					if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Kirlia")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Gallade")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					} else if(p.getParty()[i].getSpeciesName().equalsIgnoreCase("Snorunt")) {
						p.getParty()[i] = 
							p.getParty()[i].evolve
							(p.getParty()[i], 
									GameServer.getSpeciesData().getSpecies
									(GameServer.getSpeciesData().getPokemonByName("Froslass")));
						p.setSeen(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.setCaught(GameServer.getSpeciesData().getPokemonByName(p.getParty()[i].getSpeciesName()));
						p.updateClientPartyByIndex(i);
						return true;
					}
				}
				return false;
			default:
				//If it's not a valid item, return false
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
