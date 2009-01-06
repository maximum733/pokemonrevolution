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


import javax.swing.JOptionPane;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import polr.client.GameClient;
import polr.client.logic.OurPlayer;
import polr.client.logic.Player;
import polr.client.logic.Player.Dirs;

/** 
 * This handles all messages received from the server
 */
public class ProtocolHandler extends IoHandlerAdapter {
	private GameClient thisGame;
	
	public ProtocolHandler(GameClient game) {
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
    	try {
			super.sessionClosed(session);
	        thisGame.setIsPlaying(false);
	        thisGame.setIsConnected(false);
    		GameClient.setServer("");
	        GameClient.getStartScreen().getLoginFrame().setVisible(false);
	        GameClient.getStartScreen().getServerSelector().setVisible(true);
	        GameClient.getStartScreen().setVisible(true);
	        thisGame.getUI().setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /** 
	    * Called when a message is received from the server. The first char of the message tells the client what to do.
	    */
	@SuppressWarnings("static-access")
	public void messageReceived(IoSession session, Object m) {
    	Player p = null;
        String message = (String) m;
        String [] details;
        System.out.println(message);
        // Print out read buffer content.
        switch(message.charAt(0)) {
        case 'l':
        	//Login information
        	switch(message.charAt(1)) {
        	case 's':
        		//Successful login
        		thisGame.setUser(thisGame.getStartScreen().getUser());
        		thisGame.setIsPlaying(true);
        		thisGame.getStartScreen().setVisible(false);
        		thisGame.getLoading().setVisible(true);
        		thisGame.getUI().setVisible(true);
        		break;
        	case '0':
        		//Account does not exist
        		JOptionPane.showMessageDialog(null, "Account does not exist.");
        		break;
        	case '1':
        		//Account is banned
        		JOptionPane.showMessageDialog(null, "Account is banned.");
        		break;
        	case '2':
        		//Password is wrong
        		JOptionPane.showMessageDialog(null, "Password is incorrect.");
        		break;
        	}
        	break;
        case 'r':
        	//Registration information
        	switch(message.charAt(1)) {
        	case 's':
        		//Successful Registration
        		JOptionPane.showMessageDialog(null, "Registration successful, you may now login.");
        		thisGame.getStartScreen().getLoginFrame().goToLogin();
        		break;
        	case 'e':
        		//An error occurred
        		JOptionPane.showMessageDialog(null, "An error occurred in registration.");
        		break;
        	case '1':
        		//Banned username
        		JOptionPane.showMessageDialog(null, "Banned username, please choose another.");
        		break;
        	case '2':
        		//Username exists
        		JOptionPane.showMessageDialog(null, "Username already exists.");
        		break;
        	}
        	break;
        case 'U':
        	//A player is moving up
        	try {
            	thisGame.getMapMatrix().findPlayer(message.substring(1)).moveUp();
        	} catch (Exception e) {}
        	break;
        case 'D':
        	//A player is moving down
        	try {
            	thisGame.getMapMatrix().findPlayer(message.substring(1)).moveDown();
        	} catch (Exception e) {}
        	break;
        case 'L':
        	//A player is moving left
        	try {
            	thisGame.getMapMatrix().findPlayer(message.substring(1)).moveLeft();
        	} catch (Exception e) {}
        	break;
        case 'R':
        	//A player is moving right
        	try {
            	thisGame.getMapMatrix().findPlayer(message.substring(1)).moveRight();
        	} catch (Exception e) {}
        	break;
        case 'C':
        	//A player is changing sometime
        	switch(message.charAt(1)) {
        	case 'U':
        		//Direction - Up
        		try {
                	thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Up);
            	} catch (Exception e) {}
        		break;
        	case 'D':
        		//Direction - Down
        		try {
                	thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Down);
            	} catch (Exception e) {}
        		break;
        	case 'L':
        		//Direction - Left
        		try {
                	thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Left);
            	} catch (Exception e) {}
        		break;
        	case 'R':
        		//Direction - Right
        		try {
                	thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Right);
            	} catch (Exception e) {}
        		break;
        	case 'S':
        		//Sprite
        		try {
        			details = message.substring(2).split(",");
        			thisGame.getMapMatrix().findPlayer(details[0]).spriteType = Integer.parseInt(details[1]);
            	} catch (Exception e) {}
        		break;
        	case 'T':
        		//Time of day
        		details = message.substring(2).split(",");
        		thisGame.getUI().startClock(Integer.parseInt(details[0]), Integer.parseInt(details[1]));
        		break;
        	case 'W':
        		//Weather
        		break;
        	}
        	break;
        case '!':
        	//Server message
        	switch(message.charAt(1)) {
        	case '0':
        		//Server has gone into lockdown
        		sessionClosed(session);
        		break;
        	case '1':
        		//You cannot challenge this trainer
        		break;
        	case '2':
        		//You cannot add anymore friends
        		JOptionPane.showMessageDialog(null, "You can only have 10 friends.");
        		break;
        	}
        	break;
        case 'm':
        	//Map information
        	switch(message.charAt(1)) {
        	case 'S':
        		//Set map
        		details = message.substring(2).split(",");
            	thisGame.mapX = Integer.parseInt(details[0]);
            	thisGame.mapY = Integer.parseInt(details[1]);
            	thisGame.newMap = true;
            	thisGame.getLoading().setVisible(true);
        		break;
        	case 'A':
        		//Add a player
        		details = message.substring(2).split(",");
        		if(details[1].equals(thisGame.user)) {
    				p = new OurPlayer();
    				OurPlayer pl = (OurPlayer) p;
        			pl.thisPlayer = true;
        			if(thisGame.thisPlayer != null) {
        				pl.transfer(thisGame.thisPlayer);
        			}
    				thisGame.thisPlayer = pl;
    				thisGame.getMapMatrix().getCurrentMap().setCurrent(true);
        			thisGame.getMapMatrix().setCurrentPlayer(p);
        		} else {
       				p = new Player();
        		}
        		p.svrX = Short.parseShort(details[4]);
        		p.svrY = Short.parseShort(details[5]);
        		p.x = p.svrX;
				p.y = p.svrY;
				p.index = Integer.parseInt(details[0]);
				p.username = details[1];
				p.spriteType = Integer.parseInt(details[3]);
				p.setFacing(p.dirValue(details[2]));
				if(p instanceof OurPlayer)
        			thisGame.thisPlayer.setMap(thisGame.getMapMatrix().getCurrentMap());
        		thisGame.getMapMatrix().getCurrentMap().addPlayer(p);
        		break;
        	case 'R':
        		//Remove a player
        		try {
        			thisGame.getMapMatrix().getCurrentMap().removePlayer(Long.parseLong(message.substring(2)));
        		} catch (Exception e) {}
        		break;
        	}
        	break;
        case 'c':
        	//Chat messages
        	switch(message.charAt(1)) {
        	case 'l':
        		thisGame.getUI().addLocalChatMessage(message.substring(2));
        		break;
        	case 'p':
        		//Private Chat
        		details = message.substring(2).split(",");
        		thisGame.addPrivateChat(details[0], details[1]);
        		break;
        	}
        	break;
        case 'f':
        	//Friends list information
        	switch(message.charAt(1)) {
        	case 'i':
        		//Initialise friend list
        		details = message.substring(2).split(",");
        		for(int i = 0; i < details.length; i++)
        			thisGame.getUI().addFriend(details[i]);
        		break;
        	case 'a':
        		//Add a friend
        		thisGame.getUI().addFriend(message.substring(2));
        		break;
        	case 'r':
        		//Remove a friend
        		thisGame.getUI().removeFriend(message.substring(2));
        		break;
        	}
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