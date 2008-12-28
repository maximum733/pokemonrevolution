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

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;

import polr.client.GameClient;
import polr.client.logic.OurPlayer;
import polr.client.logic.Player;
import polr.client.logic.Player.Dirs;

public class ProxiedProtocolHandler implements Runnable {
	private GameClient thisGame;
	private BufferedReader input;
	private PrintWriter output;
	private Player p;
	
	public ProxiedProtocolHandler(GameClient game, BufferedReader in, PrintWriter out) {
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
		
	}
            
}
