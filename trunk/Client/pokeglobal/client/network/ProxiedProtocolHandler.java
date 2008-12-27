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

package pokeglobal.client.network;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;

import pokeglobal.client.GlobalGame;
import pokeglobal.client.logic.OurPlayer;
import pokeglobal.client.logic.Player;
import pokeglobal.client.logic.Player.Dirs;

public class ProxiedProtocolHandler implements Runnable {
	private GlobalGame thisGame;
	private BufferedReader input;
	private PrintWriter output;
	private Player p;
	
	public ProxiedProtocolHandler(GlobalGame game, BufferedReader in, PrintWriter out) {
		super();
		thisGame = game;
		input = in;
		output = out;
	}

	public void run() {
		String message;
		
		try {
            // loop endlessly as long as we have a connection
            // read from the socket in the main game class
            while ((message = (String) input.readLine()) != null) {
                    System.out.println(message);
                    if(message != null && !message.equalsIgnoreCase(""))
                    	messageReceived(message);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	public void messageReceived(String message) {
		switch(message.charAt(0)) {
        case 'l':
        	//Login
    		thisGame.getLoading().setVisible(true);
        	switch(message.charAt(1)) {
        	case 's':
        		//Successful login
        		thisGame.getLogin().setVisible(false);
        		thisGame.setIsPlaying(true);
        		output.write('p');
        		output.flush();
        		//thisGame.showNews();
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
                	output.write("up");
                	output.flush();
                	output.write("ub");
                	output.flush();
                }
                if(thisGame.getLoading().isVisible())
                	thisGame.getLoading().setVisible(false);
                //We have changed maps, load new maps
        	}
        	else
        		p = new Player();

        	p.index = Long.parseLong(workload[0]);
        	p.username = workload[1];
        	p.spriteType = workload[3];
        	if(p.spriteType.equalsIgnoreCase("Invisible"))
        		p.username = "";
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
        	case 'a':
        		//Adding an item. ITEMNUMBER:QUANTITY
        		break;
        	case 'r':
        		//Removing an item. ITEMNUMBER
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
        		break;
        	case '?':
        		//Server is requesting a Pokemon swap
        		break;
        	case 'p':
        		//PP Data. MOVENAME:AMOUNT
        		break;
        	case 'm':
        		//A move was used. TRAINERINDEX:POKEMONINDEX:MOVENAME
        		break;
        	case 'd':
        		//A pokemon took damage. TRAINERINDEX:POKEMONINDEX:AMOUNT
        		break;
        	case 's':
        		//A pokemon was switched. TRAINERINDEX:OLDPOKEMONINDEX:NEWPOKEMONINDEX
        		break;
        	case 'S':
        		//A pokemon received a status effect. POKEMON:EFFECTNAME
        		break;
        	case 'M':
        		//A pokemon wants to learn a move. POKEMON:MOVENAME
        		break;
        	case 'l':
        		//A pokemon leveled up. POKEMON:LEVEL
        		break;
        	case 'e':
        		//A pokemon gained experience. POKEMON:EXP
        		break;
        	case 'E':
        		//A pokemon is evolving. POKEMON:NEWPOKEMON
        		break;
        	case 'f':
        		//Battle Ended. VICTOR:PRIZE
        		break;
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
        		output.write("m");
        		output.flush();
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
        		output.write("m");
        		output.flush();
        	}
        	break;
        case 'R':
        	//A plyaer moved right
        	try {
        		if(thisGame.thisPlayer.map != null)
        			thisGame.getMapMatrix().findPlayer(message.substring(1)).moveRight();
        	} catch (Exception e) {
        		//Player went out of sync
        		thisGame.getLoading().setVisible(true);
        		thisGame.thisPlayer.map.wipe();
        		output.write("m");
        		output.flush();
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
        		output.write("m");
        		output.flush();
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
            		output.write("m");
            		output.flush();
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
            		output.write("m");
            		output.flush();
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
            		output.write("m");
            		output.flush();
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
            		output.write("m");
            		output.flush();
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

        }
	}
            
}
