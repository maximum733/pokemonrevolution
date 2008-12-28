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


import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import polr.client.GameClient;
import polr.client.logic.Player;

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