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

package polr.client.network;


import java.util.HashMap;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.newdawn.slick.SlickException;

import polr.client.GlobalGame;
import polr.client.logic.OurPlayer;
import polr.client.logic.Player;
import polr.client.logic.TempPokemon;
import polr.client.logic.Player.Dirs;
import polr.client.language.*;

/** 
 * This handles all messages received from the server
 */
public class ProtocolHandler extends IoHandlerAdapter {
	private GlobalGame thisGame;
	
	public ProtocolHandler(GlobalGame game) {
		thisGame = game;
	}

	/** 
	    * Called once the session has begun
	    */
    public void sessionOpened(IoSession session) {
    	System.out.println("Connected to server!");
    }

    /** 
	    * Called once the session is closed
	    */
    public void sessionClosed(IoSession session) {
        // Print out total number of bytes read from the remote peer.
        System.err.println("Total " + session.getReadBytes() + " byte(s)");
        thisGame.setIsPlaying(false);
        thisGame.returnToServerSelect();
    }

    /** 
	    * Called when a message is received from the server. The first char of the message tells the client what to do.
	    */
    
	@SuppressWarnings("static-access")
	public void messageReceived(IoSession session, Object m) {
    	Player p;
        String message = (String) m;
        System.out.println(message);
        // Print out read buffer content.
        switch(message.charAt(0)) {
        case 'l':
        	//Login
    		thisGame.getLoading().setVisible(true);
        	switch(message.charAt(1)) {
        	case 's':
        		//Successful login
        		thisGame.getLogin().setVisible(false);
        		thisGame.setIsPlaying(true);
        		break;
        	case '0':
        		//Account exists
        		GlobalGame.messageBox("Account does not exist.", thisGame.getLogin());
        		thisGame.getLoading().setVisible(false);
        		thisGame.getLogin().enable();
        		break;
        	case '1':
        		//Banned username
        		GlobalGame.messageBox("Wrong password.", thisGame.getLogin());
        		thisGame.getLoading().setVisible(false);
        		thisGame.getLogin().enable();
        		break;
        	case '2':
        		//Invalid Data
        		GlobalGame.messageBox("An unknown error occured. \nPlease try again.", thisGame.getLogin());
        		thisGame.getLoading().setVisible(false);
        		thisGame.getLogin().enable();
        		break;
        	case '3':
        		//Unknown error
        		GlobalGame.messageBox("This account has been banned.", thisGame.getLogin());
        		thisGame.getLoading().setVisible(false);
        		thisGame.getLogin().enable();
        		break;
        	case '4':
        		try {
						Thread.sleep(120000);
				} catch (InterruptedException e1) {
						e1.printStackTrace();
				}
        		thisGame.getLoading().setVisible(false);
				thisGame.getLogin().enable();
        		break;
        	case '5':
        		//Unknown error
        		GlobalGame.messageBox("The server is attempting to save data from your last login. Please try login in a few minutes.", thisGame.getLogin());
        		try {
					Thread.sleep(120000);
        		} catch (InterruptedException e1) {
					e1.printStackTrace();
        		}
        		thisGame.getLoading().setVisible(false);
        		thisGame.getLogin().enable();
        		break;
        	}
        	break;
        case 'r':
        	//Registration packet
        	switch(message.charAt(1)) {
        	case 's':
        		//Successful registration
        		GlobalGame.messageBox("Successful Registation! \nYou may now login.", thisGame.getLogin());
        		thisGame.getLogin().goToLogin();
        		break;
        	case '0':
        		//Account exists
        		GlobalGame.messageBox("Username already exists.", thisGame.getLogin());
        		break;
        	case '1':
        		//Banned username
        		GlobalGame.messageBox("Username banned.", thisGame.getLogin());
        		break;
        	case '2':
        		//Invalid Data
        		GlobalGame.messageBox("Please fill out all of the form \nbefore attempting to register.", thisGame.getLogin());
        		break;
        	case '3':
        		//Unknown error
        		GlobalGame.messageBox("An unknown error occurred.", thisGame.getLogin());
        		break;
        	case '4':
        		//Lockdown mode
        		GlobalGame.messageBox("The server is in lockdown mode. Please try again later.", thisGame.getLogin());
        		break;
        	}
        	break;
        case 'A':
        	//Add a player to the map. INDEX:NAME:FACING:SPRITE:X:Y
        	String [] workload = message.substring(1).split(",");
        	if(workload[1].equalsIgnoreCase(thisGame.user)) {
        		p = new OurPlayer();
        		p = new OurPlayer();
                OurPlayer pl = (OurPlayer) p;
                pl.thisPlayer = true;
                if (thisGame.thisPlayer != null)
                        pl.transfer(thisGame.thisPlayer);
                thisGame.thisPlayer = pl;
                thisGame.getMapMatrix().setCurrentPlayer(p);
                if(thisGame.thisPlayer.getTeam() == null) {
                	//The player has just logged in, request bag & pokemon data
                	thisGame.getPacketGenerator().write("up");
                	thisGame.getPacketGenerator().write("ub");
                }
                if(thisGame.getLoading().isVisible())
                	thisGame.getLoading().setVisible(false);
                //We have changed maps, load new maps
        	}
        	else
        		p = new Player();

        	p.index = Long.parseLong(workload[0]);
        	if(workload[3].equalsIgnoreCase("Invisible")) {
        		p.username = "";
        		p.spriteType = "Invisible";
        	} else {
            	p.username = workload[1];
        		p.spriteType = workload[3];
        	}
        	p.setFacing(p.dirValue(workload[2]));
        	p.x = Short.parseShort(workload[4]);
        	p.y = Short.parseShort(workload[5]);
        	p.svrX = Short.parseShort(workload[4]);
        	p.svrY = Short.parseShort(workload[5]);
        	if(p instanceof OurPlayer) {
				thisGame.thisPlayer.map = thisGame.getMapMatrix().getMap(1, 1);
                thisGame.thisPlayer.map.setXOffset(GlobalGame.width / 2 - thisGame.thisPlayer.x);
                thisGame.thisPlayer.map.setYOffset(GlobalGame.height / 2 - thisGame.thisPlayer.y);
        	}
        	thisGame.queuePlayer(p);
        	break;
        case 'd':
        	//Delete a player from the map
        	for(int i = 0; i < thisGame.thisPlayer.map.getMapPlayers().size(); i++) {
        		if(thisGame.thisPlayer.map.getMapPlayers().get(i).index == Long.parseLong(message.substring(1))) {
        			thisGame.thisPlayer.map.getMapPlayers().remove(i);
        			break;
        		}
        	}
        	break;
        case 'P':
        	//Receiving Pokemon Information
        	//POKEMONNAME:SPECIESINDEX:PARTYINDEX:HP:MAXHP:ATK:DEF:SPD:SPATK:SPDEF:TYPE1:TYPE2:LEVEL:EXP:NATURE:GENDER:
        	//MOVE1:PP1:MOVE2:PP2:MOVE3:PP3:MOVE4:PP4:ABILITY
        	
        	//Store the information temporarily as the player might not be loaded yet
        	String [] pdata = message.substring(1).split(",");       	
        	if(thisGame.thisPlayer != null) {
        		thisGame.thisPlayer.initPokemon(pdata);
        		thisGame.setTeamUpdate(true);
        	} else {
        		System.err.println("ERROR WITH INITIALISING POKEMON DATA");
        	}
        	break;
        case 'B':
        	//Receiving bag information
        	switch(message.charAt(1)) {
        	case 'i':
        		//Initialise ITEMNUMBER:QUANTITY
        		message = message.substring(2);
        		while(message.length() >= 5) {
        			int q = Integer.parseInt(message.substring(3,5));
        			int id = Integer.parseInt(message.substring(0, 3));
        			for(int i = 1; i <= q; i++) {
        				thisGame.thisPlayer.addItem(id);
        			}
        			message = message.length() - 5 > 0 ? message.substring(5) : "";
        		}
        		thisGame.updateBagInfo();
        		break;
        	case 'a':
        		//Adding an item. ITEMNUMBER
        		message = message.substring(2);
        		thisGame.thisPlayer.addItem(Integer.parseInt(message));
        		thisGame.updateBagInfo();
        		break;
        	case 'r':
        		//Removing an item. ITEMNUMBER
        		thisGame.thisPlayer.useItem(Integer.parseInt(message.substring(2)));
        		thisGame.updateBagInfo();
        		break;
        	}
        	break;
        case 'm':
        	//Receiving map data
        	//Open chat window and prepare to load new maps
        	thisGame.getLoading().setVisible(true);
        	String [] mapWork = message.substring(1).split(",");
    		thisGame.mapX = Integer.parseInt(mapWork[0]);
    		thisGame.mapY = Integer.parseInt(mapWork[1]);
            thisGame.newMap = true;
        	break;
        case 'c':
        	//Chat packet
        	switch(message.charAt(1)) {
        	case 'w':
        		//World chat packet
        		thisGame.getInterface().addWorldChatLine(message.substring(2));
        		break;
        	case 'l':
        		//Local chat packet
        		thisGame.getInterface().addLocalChatLine(message.substring(2));
        		break;
        	case 'm':
        		GlobalGame.messageBox(message.substring(2), thisGame.getDisplay());
        		break;
        	case 's':
        		//Server Announcement
        		break;
        	}
        	break;
        case 'b':
        	//A battle message
        	switch(message.charAt(1)) {
        	case 'i':
        		//Initialise Battle. ENEMYTRAINERNAME:TYPE:INDEX
        		//TYPE: w = wild, p = player, n = npc
        		thisGame.beginBattle(message.substring(2, message.indexOf(',')), message.charAt(message.indexOf(',') + 1), Integer.parseInt(message.substring(message.lastIndexOf(",") + 1)));
        		break;
        	case 'I':
        		//Receiving player's battle index - PvP only
        		thisGame.getBattle().setPlayerBattleIndex(Integer.parseInt(message.substring(2)));
        		break;
        	case 'P':
        		//Enemy Pokemon Data. PARTYINDEX:POKEMONNAME:LEVEL:GENDER:HP
        		String [] ePokeData = message.substring(2).split(",");
        		thisGame.getBattle().addEnemyPokemon(ePokeData[1], Integer.parseInt(ePokeData[0]), Integer.parseInt(ePokeData[2]), Integer.parseInt(ePokeData[3]), Integer.parseInt(ePokeData[4]), ePokeData[5]);
        		break;
        	case 'a':
        		//Server is requesting the player to move
        		thisGame.getBattle().requestMove();
        		break;
        	case 'c':
        		//Attempt at capturing a pokemon. MESSAGEINDEX
        		int ballData = Integer.parseInt(message.substring(2));
        		switch (ballData){
        		case 1:
        			thisGame.getBattle().addMessage("The Pokemon was caught successfully!");
        			break;
        		case 2:
        			thisGame.getBattle().addMessage("There wasn't anywhere to put the Pokemon.");
        			thisGame.getBattle().addMessage("The pokemon was sent to your storage box.");
        			break;
        		case 3:
        			thisGame.getBattle().endBattle("CAUGHT");
        		case 4:
        			thisGame.getBattle().addMessage("It got away...");
        			break;
        		}
        	case '?':
        		//Server is requesting a Pokemon swap
        		thisGame.getBattle().forceSwitchPoke();
        		break;
        	case 'A':
        		//Re-enable battle moves
                if (thisGame.getBattle() != null) {
                    thisGame.getBattle().requestMove();
                }
                if (message.endsWith("s")) { // you have to switch, you
                    // fainted or something
            		thisGame.getBattle().forceSwitchPoke();
            	}

        		break;
        	case 'p':
        		//PP Data. MOVENAME:AMOUNT
        		try {
                    String[] ppCount = message.split("!");
                    thisGame.getBattle().updatePP(Integer.parseInt(ppCount[1]), Integer.parseInt(ppCount[2]));
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}

        		break;
        	case 'm':
        		//A move was used. TRAINERINDEX:POKEMONINDEX:MOVENAME
        		String[] data = message.split(";");
        		String moveMsg = data[1] + " used " + data[2] + ".";
        		thisGame.getBattle().addMessage(moveMsg);
        		break;
        	case 'd':
        		//A pokemon took damage. TRAINERINDEX:POKEMONINDEX:AMOUNT
        		try {
        			String[] comp = message.split(";");
                    TempPokemon struckPoke;
                    // is it our pokemon?
                    if (comp[1].equals(thisGame.user)) {
                    	System.out.println("OUR POKE HAS TAKEN DAMAGE");
                    	struckPoke = thisGame.getBattle().getCurPoke();
                    	struckPoke.setCurHP(struckPoke.getCurHP()
                    			+ Integer.parseInt(comp[3]));
                    	if (struckPoke.getCurHP() < 0) struckPoke.setCurHP(0);
                    	if (struckPoke.getCurHP() > struckPoke.getMaxHP())
                    		struckPoke.setCurHP(struckPoke.getMaxHP());
                        thisGame.getBattle().updatePlayerHP(struckPoke.getCurHP());
                    } else { // it's theirs
                    	System.out.println("ENEMY POKE HAS TAKEN DAMAGE");
                    	struckPoke = thisGame.getBattle().getCurEnemyPoke();
                    	struckPoke.setCurHP(struckPoke.getCurHP()
                    			+ Integer.parseInt(comp[3]));
                    	if (struckPoke.getCurHP() < 0) struckPoke.setCurHP(0);
                    	if (struckPoke.getCurHP() > struckPoke.getMaxHP())
                    		struckPoke.setCurHP(struckPoke.getMaxHP());
                    	thisGame.getBattle().updateEnemyHP(struckPoke.getCurHP());
                    }

                    // display damage or healing message
                    String damage;
                    if (Integer.parseInt(comp[3]) < 0) {
                            damage = comp[1] + "'s " + comp[2] + " took "
                            + Integer.parseInt(comp[3]) * -1 + " damage!\n"
                            + "It has only " + struckPoke.getCurHP() + " out" + " of "
                            + struckPoke.getMaxHP() + " HP remaining.";
                    } else {
                            damage = comp[1] + "'s " + comp[2] + " was healed for "
                            + Integer.parseInt(comp[3]) + " HP.\n" + "It now has "
                            + struckPoke.getCurHP() + " out" + " of "
                            + struckPoke.getMaxHP() + " HP remaining.";
                    }
                    thisGame.getBattle().addMessage(damage);
                   
        		} catch (NullPointerException e) {
                    e.printStackTrace();
        		}
        		break;
        	case 's':
        		//A pokemon was switched. TRAINERINDEX:OLDPOKEMONINDEX:NEWPOKEMONINDEX
                try {
                    String[] compo = message.split(";");
                    String switchMsg;
                    if (compo[1].equals(thisGame.user)) {
                            switchMsg = "You switched out "
                                    + thisGame.getBattle().getCurPoke().getName();
                            thisGame.getBattle().switchOurPoke(Integer.parseInt(compo[2]));
                            switchMsg += " and sent in "
                                    + thisGame.getBattle().getCurPoke().getName() + "!";
                    } else {
                            if(thisGame.getBattle().getCurEnemyPoke() != null) {
                                    switchMsg = compo[1] + " switched out "
                                    + thisGame.getBattle().getCurEnemyPoke().getName();
                                    thisGame.getBattle().switchEnemyPoke(Integer.parseInt(compo[2]));
                                    switchMsg += " and sent in "
                                            + thisGame.getBattle().getCurEnemyPoke().getName() + "!";       
                            }
                            else {
                                    thisGame.getBattle().switchEnemyPoke(Integer.parseInt(compo[2]));
                                    switchMsg = compo[1] + " sent out " + thisGame.getBattle().getCurEnemyPoke().getName() + "!";
                            }
                    }
                    //thisGame.getBattle().displayInBox(switchMsg);
                }
                catch (NullPointerException e) { 
                	e.printStackTrace();
                }
        		break;
        	case 'S':
        		//A pokemon received a status effect. TRAINER:EFFECTNAME
                String[] effectData = message.split(";");
           		
                //is is our poke?
                if (effectData[1].equals(thisGame.user)) {
                	switch (Integer.parseInt(effectData[1])){
                    case '0':
                    	// Burn
                    	thisGame.getBattle().getCurPoke().setEffect("burnt");
                    	break;
                    case '1':
                    	// Poison
                    	thisGame.getBattle().getCurPoke().setEffect("poisoned");
                    	break;
                    case '2':
                    	// Freeze
                    	thisGame.getBattle().getCurPoke().setEffect("frozen");
                    	break;
                    case '3':
                    	// Confused
                    	thisGame.getBattle().getCurPoke().setEffect("confused");
                    	break;
                    case '4':
                    	// Sleep
                    	thisGame.getBattle().getCurPoke().setEffect("asleep");
                    	break;
               		case '5':
            			// Paralysis
               			thisGame.getBattle().getCurPoke().setEffect("paralized");
            			break;
               		case 'a':
               			//ERROR
               			break;
           			}
                	thisGame.getBattle().addMessage(thisGame.getBattle()
                			.getCurPoke().getName() + " is " + 
                			thisGame.getBattle().getCurPoke().getEffect());
                }
                                
                //otherwise it's the opponent
                else {
                	switch (Integer.parseInt(effectData[2])){
                    case '0':
                    	// Burn
                    	thisGame.getBattle().getCurEnemyPoke().setEffect("burnt");
                    	break;
                    case '1':
                    	// Poison
                    	thisGame.getBattle().getCurEnemyPoke().setEffect("poisoned");
                    	break;
                    case '2':
                    	// Freeze
                    	thisGame.getBattle().getCurEnemyPoke().setEffect("frozen");
                    	break;
                    case '3':
                    	// Confused
                    	thisGame.getBattle().getCurEnemyPoke().setEffect("confused");
                    	break;
                    case '4':
                    	// Sleep
                    	thisGame.getBattle().getCurEnemyPoke().setEffect("asleep");
                    	break;
               		case '5':
            			// Paralysis
               			thisGame.getBattle().getCurEnemyPoke().setEffect("paralized");
            			break;
               		case 'a':
               			//ERROR
               			break;
           			}
                	thisGame.getBattle().addMessage(thisGame.getBattle()
                			.getCurEnemyPoke().getName() + " is " + 
                			thisGame.getBattle().getCurEnemyPoke().getEffect());                	
                }

        		break;
        	case 'R':
        		//Removes an effect. TRAINER
        		String[] effRemoved = message.split(";");
           		
                //is is our poke?
                if (effRemoved[1].equals(thisGame.user)) {
                	thisGame.getBattle().addMessage(thisGame.getBattle()
                			.getCurPoke().getName() + " is no longer " +
                			thisGame.getBattle().getCurPoke().getEffect());
                	thisGame.getBattle().getCurPoke().setEffect("normal");
                } else {
                	thisGame.getBattle().addMessage(thisGame.getBattle()
                			.getCurEnemyPoke().getName() + " is no longer " +
                			thisGame.getBattle().getCurEnemyPoke().getEffect());
                	thisGame.getBattle().getCurEnemyPoke().setEffect("normal");
                }
        	case 'M':
        		//A pokemon wants to learn a move. POKEMON:MOVENAME
        		String[] moves = message.split(",");
                for (int i = 2; i < moves.length; i++) {
                        thisGame.getBattle().learnMove(Integer.parseInt(moves[1]),
                        moves[2]);
                }
        		break;
        	case 'l':
        		//A pokemon leveled up. POKEMON:LEVEL
                String[] lvlData = message.split(",");
                int pkmnID = Integer.parseInt(lvlData[1]);
                int level = Integer.parseInt(lvlData[2]);
                thisGame.thisPlayer.getTeam()[pkmnID].setLevel(level);
                thisGame.getBattle().refreshPokemonInfo();
                /*thisGame.getBattle().getBattleSpeech().addSpeech(
                                thisGame.thisPlayer.getTeam()[pkmnID].getName()
                                + " has reached level " + level + "!");
                 */
        		break;
        	case 'e':
        		//A pokemon gained experience. POKEMON:EXP
        		thisGame.getBattle().giveExp(message.substring(1));
        		break;
        	case 'E':
        		//A pokemon is evolving. POKEMON:NEWPOKEMON
                String[] evosData = message.split(",");
                thisGame.getBattle().addMessage(
                                "What!? " + evosData[1] + " is evolving!");
                thisGame.getBattle().addMessage(
                                evosData[1] + " evolved into " + evosData[2]);
                break;
        	case 'f':
        		//Battle Ended. VICTOR:PRIZE
        		if (thisGame.getBattle() != null) {
                    thisGame.getBattle().endBattle(message.substring(1));
        		}
        		thisGame.setTeamUpdate(true);
        		break;
        	case 'z':
        		//Pokemon fainted: STRING:POKEMON
        		thisGame.getBattle().addMessage(message.substring(2) + "fainted");
        		break;
        	case 'r':
        		//Player Ran
        		thisGame.getBattle().run();
        	case '!':
        		//Error or server message
        		break;
        	}
        	break;
        case 'T':
        	//Returning speech of NPC
        	try {
        		thisGame.talkToNPC(message.substring(1));
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	break;
        case 'U':
        	//A player moved up
        	try {
        		if(thisGame.thisPlayer.map != null)
        			thisGame.getMapMatrix().findPlayer(message.substring(1)).moveUp();
        	} catch (Exception e) {
        		//Player went out of sync
        		thisGame.getLoading().setVisible(true);
        		thisGame.thisPlayer.map.wipe();
        		GlobalGame.getPacketGenerator().write("m");
        	}
        	break;
        case 'L':
        	//A player moved left
        	try {
        		if(thisGame.thisPlayer.map != null)
        			thisGame.getMapMatrix().findPlayer(message.substring(1)).moveLeft();
        	} catch (Exception e) {
        		//Player went out of sync
        		thisGame.getLoading().setVisible(true);
        		thisGame.thisPlayer.map.wipe();
        		GlobalGame.getPacketGenerator().write("m");
        	}
        	break;
        case 'R':
        	//A player moved right
        	try {
        		if(thisGame.thisPlayer.map != null)
        			thisGame.getMapMatrix().findPlayer(message.substring(1)).moveRight();
        	} catch (Exception e) {
        		//Player went out of sync
        		thisGame.getLoading().setVisible(true);
        		thisGame.thisPlayer.map.wipe();
        		GlobalGame.getPacketGenerator().write("m");
        	}
        	break;
        case 'D':
        	//A player moved down
        	try {
        		if(thisGame.thisPlayer.map != null)
        			thisGame.getMapMatrix().findPlayer(message.substring(1)).moveDown();
        	} catch (Exception e) {
        		//Player went out of sync
        		thisGame.getLoading().setVisible(true);
        		thisGame.thisPlayer.map.wipe();
        		GlobalGame.getPacketGenerator().write("m");
        	}
        	break;
        case 'C':
        	//A player is changing direction or sprite
        	switch(message.charAt(1)) {
        	case 'S':
        		//Change sprite ID:SPRITENAME
        		try {
        			thisGame.getMapMatrix().findPlayer(message.substring(2, message.indexOf(","))).spriteType
        				=  message.substring(message.indexOf(",") + 1);
        		} catch (Exception e) {
        			System.err.println("Could not find player for sprite change.");
        			e.printStackTrace();
        		}
        		break;
        	case 'U':
        		//Facing up
        		try {
        			thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Up);
        		} catch (Exception e) {
            		//Player went out of sync
            		thisGame.getLoading().setVisible(true);
            		thisGame.thisPlayer.map.wipe();
            		GlobalGame.getPacketGenerator().write("m");
            	}
        		break;
        	case 'D':
        		//Facing down
        		try {
        			thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Down);
        		} catch (Exception e) {
            		//Player went out of sync
            		thisGame.getLoading().setVisible(true);
            		thisGame.thisPlayer.map.wipe();
            		GlobalGame.getPacketGenerator().write("m");
            	}
        		break;
        	case 'L':
        		//Facing left
        		try {
        			thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Left);
        		} catch (Exception e) {
            		//Player went out of sync
            		thisGame.getLoading().setVisible(true);
            		thisGame.thisPlayer.map.wipe();
            		GlobalGame.getPacketGenerator().write("m");
            	}
        		break;
        	case 'R':
        		//Facing right
        		try {
        			thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Right);
        		} catch (Exception e) {
            		//Player went out of sync
            		thisGame.getLoading().setVisible(true);
            		thisGame.thisPlayer.map.wipe();
            		GlobalGame.getPacketGenerator().write("m");
            	}
        		break;
        	}
        	break;
        	
        case 'x':
        	//shop code
        	HashMap<String, Integer> merch = new HashMap<String, Integer>();
            String[] merchData = message.split(",");
            for (int i = 1; i < merchData.length; i += 2) {
                    merch.put(merchData[i], Integer.parseInt(merchData[i + 1]));
            }
            thisGame.useShop(merch);
            break;

        case 'w':
        	String[] pokeData = message.split(";");
        	thisGame.setWildEnemy(pokeData[1], pokeData[2], pokeData[3],
        			pokeData[4], pokeData[5]);
        	break;
        case 'S': // Box Acess
            try {
            	thisGame.usePokeStorageBox(message.substring(1));
            } catch (SlickException e2) {e2.printStackTrace();}
            break;
        }
	}
    
    /** 
	    * If a network error occurs, this gets called.
	    */
    public void exceptionCaught() {
    	System.err.println("An error occured in the Network Protocol.");
    }
    
    private String toProperCase(String target) {
		return target.toUpperCase().substring(0, 1)
		+ target.toLowerCase().substring(1);
	}
}