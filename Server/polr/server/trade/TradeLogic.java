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

package polr.server.trade;

import polr.server.GameServer;
import polr.server.battle.Pokemon;
import polr.server.database.POLRDataEntry;
import polr.server.database.POLRDatabase;
import polr.server.database.PlayerDataManager;
import polr.server.database.POLREvolution.EvoTypes;
import polr.server.player.PlayerChar;

public class TradeLogic {
	PlayerChar[] players;
	boolean[] acceptedTrade = new boolean[2];
	boolean[] offerValid = new boolean[2];
	int[] pkmnOffered = new int[2];
	long[] pdOffered = new long[2];
	private POLRDatabase m_polrDB;

	public TradeLogic(PlayerChar[] p) {
		players = p;
	}

	public boolean canCancel() {
//		if (acceptedTrade[0] && acceptedTrade[1]) {
//			return false;
//		} else {
			return true;
		}
	

	public boolean canOffer() {
		if (offerValid[0] && offerValid[1]) {
			return false;
		} else {
			return true;
		}
	}

	public void playerDisconnected() {
		players[1].forceEndTrade();
		players[0].forceEndTrade();
	}

	public void beginTrade() {
		players[0].getIoSession().write("ts" + players[1].getName());
		players[1].getIoSession().write("ts" + players[0].getName());
		acceptedTrade[0] = false;
		acceptedTrade[1] = false;
		offerValid[0] = false;
		offerValid[1] = false;
		pkmnOffered[0] = -1;
		pkmnOffered[1] = -1;
	}

	public void submitOffer(int pkmnOffer, long pdOffer, int idx) {
		if (pkmnOffer == 0) { // no pokemon up for trade
			pkmnOffered[idx] = -1;
		} else {
			pkmnOffered[idx] = pkmnOffer - 1; // party ID will be 1 lower than
												// ID client sent
		}
		if (pdOffer < 0 || !players[idx].validateTransaction(pdOffer)) {
			players[idx].getIoSession().write("tR");
			pkmnOffered[idx] = -1;
		} else {
			pdOffered[idx] = pdOffer;
			if (players[idx].getParty().length > 1 || pkmnOffered[idx] == -1) {
				offerValid[idx] = true;
			} else
				players[idx].getIoSession().write(
						"mYou can't trade your only Pokemon!");
		}
		// if (offerValid[0] != true || offerValid[1] != true){ // FIXME
		String pokemonInfo = null;
		if (idx == 1) {
			if (pkmnOffered[1] == -1) {
				pokemonInfo = null;
			} else {
				try {
					pokemonInfo = GameServer.getSpeciesData().getPokemonByName(
							players[1].getParty()[pkmnOffered[1]].getSpeciesName())
							+ ","
							+ players[1].getParty()[pkmnOffered[1]].getNature()
									.getName()
							+ ","
							+ players[1].getParty()[pkmnOffered[1]]
									.getAbilityName()
							+ ","
							+ players[1].getParty()[pkmnOffered[1]].getLevel()
							+ ","
							+ players[1].getParty()[pkmnOffered[1]].getGender()
							+ ","
							+ players[1].getParty()[pkmnOffered[1]]
									.getRawStat(Pokemon.S_ATTACK)
							+ ","
							+ players[1].getParty()[pkmnOffered[1]]
									.getRawStat(Pokemon.S_DEFENCE)
							+ ","
							+ players[1].getParty()[pkmnOffered[1]]
									.getRawStat(Pokemon.S_HP)
							+ ","
							+ players[1].getParty()[pkmnOffered[1]]
									.getRawStat(Pokemon.S_SPATTACK)
							+ ","
							+ players[1].getParty()[pkmnOffered[1]]
									.getRawStat(Pokemon.S_SPDEFENCE)
				            + ","
							+ players[1].getParty()[pkmnOffered[1]]
									.getRawStat(Pokemon.S_SPEED);
				players[0].getIoSession().write(
						"ti" + pdOffered[1] + "," + pokemonInfo);
				}
			catch (Exception e) {
				e.printStackTrace();
				players[0].forceEndTrade();
				players[0].getIoSession().write("tf");
			}	
			}
		} else {
			if (pkmnOffered[0] == -1) {
				pokemonInfo = null;
			} else {
				try {
					pokemonInfo = GameServer.getSpeciesData().getPokemonByName(
							players[0].getParty()[pkmnOffered[0]].getSpeciesName())
						+ ","
						+ players[0].getParty()[pkmnOffered[0]].getNature()
								.getName()
						+ ","
						+ players[0].getParty()[pkmnOffered[0]]
								.getAbilityName()
						+ ","
						+ players[0].getParty()[pkmnOffered[0]].getLevel()
						+ ","
						+ players[0].getParty()[pkmnOffered[0]].getGender()
						+ ","
						+ players[0].getParty()[pkmnOffered[0]]
								.getRawStat(Pokemon.S_ATTACK)
						+ ","
						+ players[0].getParty()[pkmnOffered[0]]
								.getRawStat(Pokemon.S_DEFENCE)
						+ ","
						+ players[0].getParty()[pkmnOffered[0]]
								.getRawStat(Pokemon.S_HP)
						+ ","
						+ players[0].getParty()[pkmnOffered[0]]
								.getRawStat(Pokemon.S_SPATTACK)
						+ ","
						+ players[0].getParty()[pkmnOffered[0]]
								.getRawStat(Pokemon.S_SPDEFENCE)
						+ ","
						+ players[0].getParty()[pkmnOffered[0]]
								.getRawStat(Pokemon.S_SPEED);
			players[1].getIoSession().write(
					"ti" + pdOffered[0] + "," + pokemonInfo);
				}
				catch (Exception e) {
					e.printStackTrace();
					players[1].forceEndTrade();
					players[1].getIoSession().write("tf");
				}
			}
		}
	}

	public void acceptTrade(int idx) {
		acceptedTrade[idx] = true;
		if (acceptedTrade[0] == true && acceptedTrade[1] == true) {
        trade();
				PlayerDataManager.getDefault().attemptSave(players[0]);
				PlayerDataManager.getDefault().attemptSave(players[1]);
				try {
					players[0].endTrade(1);
				}
				catch (Exception e) {
					//The other player must have been storing the trade data
					e.printStackTrace();
					//endTrade(1);
					//players[0].forceEndTrade();
					//players[1].forceEndTrade();
				}
		}
	}

	// actual exchange takes place here, returns true on success
	public void trade() {
		// check to make sure they don't have the same ip address first
		if((players[0].getIoSession().getRemoteAddress().toString().substring(1).split(":")[0] != players[1].getIoSession().getRemoteAddress().toString().substring(1).split(":")[0]) || (players[0].getIoSession().getRemoteAddress().toString().substring(1).split(":")[0].equals(players[1].getIoSession().getRemoteAddress().toString().substring(1).split(":")[0]) && players[0].getIoSession().getRemoteAddress().toString().substring(1).split(":")[0].equals("127.0.0.1"))) {
			exchangeMoney(0, 1, pdOffered[0]);
			exchangeMoney(1, 0, pdOffered[1]);
			if (pkmnOffered[0] == -1) {
			} else
				evolvePokemon(0, pkmnOffered[0]);
			if (pkmnOffered[1] == -1) {
			} else
				evolvePokemon(1, pkmnOffered[1]);
			
			Pokemon p1;
			Pokemon p2;
			if (pkmnOffered[0] != -1)
				p1 = players[0].getParty()[pkmnOffered[0]];
			else
				p1 = null;
			
			if (pkmnOffered[1] != -1)
				p2 = players[1].getParty()[pkmnOffered[1]];
			else
				p2 = null;
				
			executePokemonTrade(p1, p2);
		}
		else {
			players[0].endTrade(0);
			players[0].getIoSession().write("mTrading with yourself is against the rules!");
		}
	}

	public void executePokemonTrade(Pokemon p1, Pokemon p2) {
		if (p1 != null)
			players[0].removePokemon(p1);
		if (p2 != null)
			players[1].removePokemon(p2);
		
		if (p2 != null)
			players[0].addTradePokemon(p2);
		if (p1 != null)
			players[1].addTradePokemon(p1);
	}
	public void endTrade(int endCode) {
		switch (endCode) {
		case 0: // trade failed
			players[0].getIoSession().write("tf");
			players[1].getIoSession().write("tf");
			break;
		case 1: // trade success
			players[0].getIoSession().write("tz");

			players[1].getIoSession().write("tz");
			
			break;
		case 2: // player cancelled trade
			players[0].getIoSession().write("tx");
			players[1].getIoSession().write("tx");
			break;
			
		}
		players[0].arrangeParty();
		players[1].arrangeParty();
		players[0].updateClientParty();
		players[1].updateClientParty();
	}

	public void exchangeMoney(int sender, int receiver, long amount) {
		players[sender].setMoney(players[sender].getMoney() - amount);
		players[receiver].setMoney(players[receiver].getMoney() + amount);
	}

	public void evolvePokemon(int sender, int partyID) {
		//If Pokemon evolves via trade, evolve it now!
		try {/*
			if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Haunter")) {
				players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Gengar")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Graveler")) {
				players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Golem")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Kadabra")) {
				players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Alakazam")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Machoke")) {
				players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Machamp")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Poliwhirl")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("King's Rock"))
						players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Politoed")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Slowpoke")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("King's Rock"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Slowking")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Onix")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Metal Coat"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Steelix")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Rhydon")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Protector"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Rhyperior")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Seadra")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Dragon Scale"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Kingdra")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Scyther")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Metal Coat"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Scizor")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Electabuzz")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Electirizer"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Electivire")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Magmar")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Magmarizer"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Magmortar")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Porygon")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Up-Grade"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Porygon2")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Porygon2")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Dubious Disc"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Porygonz")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Dusclops")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("Reaper Cloth"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Dusknoir")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Clamperl")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("DeepSeaTooth"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Huntail")));
			}
			else if(players[sender].getParty()[partyID].getName().equalsIgnoreCase("Clamperl")) {
				if(players[sender].getParty()[partyID].getItem() != null && players[sender].getParty()[partyID].getItem().getName().equalsIgnoreCase("DeepSeaScale"))
					players[sender].getParty()[partyID] = players[sender].getParty()[partyID].evolve(players[sender].getParty()[partyID], GameServer.getSpeciesData().getSpecies(GameServer.getSpeciesData().getPokemonByName("Gorebyss")));
			}*/
			
			POLRDataEntry moveData = m_polrDB.getPokemonData(
						GameServer.getSpeciesData().
						getPokemonByName(players[sender].getParty()[partyID].getSpeciesName()));
			Pokemon pokemon = players[sender].getParty()[partyID];
			                                             
			                                             
			for (int i = 0; i < moveData.getEvolutions().size(); i++){
				if(moveData.getEvolutions().get(i).getType() 
							== EvoTypes.Trade || (moveData.getEvolutions().get(i).getType()
									== EvoTypes.TradeItem && moveData.getEvolutions().get(i).getAttribute() ==
										pokemon.getItem().toString())){
						StringBuilder evoPacket = new StringBuilder("e,"
								+ pokemon.getName());
						
						players[sender].getParty()[partyID] = pokemon.evolve(pokemon, 
								GameServer.getSpeciesData()
								.getSpecies(GameServer
										.getSpeciesData()
										.getPokemonByName(moveData
												.getEvolutions().get(i)
												.getEvolveTo())));
						pokemon = players[sender].getParty()[partyID];
						evoPacket.append(",");
						evoPacket.append(GameServer.getSpeciesData()
								.getSpecies(GameServer.getSpeciesData()
										.getPokemonByName(moveData.getEvolutions()
												.get(i).getEvolveTo())).getName());
						//Add entry to pokedex
						players[sender].setSeen(GameServer.getSpeciesData().getPokemonByName(pokemon.getSpeciesName()));
						players[sender].setCaught(GameServer.getSpeciesData().getPokemonByName(pokemon.getSpeciesName()));
						System.out.println(players[sender].getParty()[partyID].getName());
						System.out.println(evoPacket);
						players[sender].getIoSession().write(
								evoPacket.toString());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
