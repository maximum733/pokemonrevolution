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

import java.io.PrintWriter;

import org.apache.mina.common.IoSession;

import polr.client.logic.Whirlpool;

/** 
 * Sends packets to the server. Allows easy remodeling of networking.
 */
public class PacketGenerator {
	private IoSession gameSession;
	private PrintWriter output;

	public PacketGenerator(IoSession session) {
		gameSession = session;
		output = null;
	}
	
	public PacketGenerator(PrintWriter out) {
		output = out;
		gameSession = null;
	}
	
	/** 
	 * Send a login packet
	 */
	public void login(String username, String password) {
		// initialize the hasher
        Whirlpool hasher = new Whirlpool();
        hasher.NESSIEinit();

        // add the plaintext password to it
        hasher.NESSIEadd(password);
        // create an array to hold the hashed bytes
        byte[] hashed = new byte[64];

        // run the hash
        hasher.NESSIEfinalize(hashed);

        // this stuff basically turns the byte array into a hexstring
        java.math.BigInteger bi = new java.math.BigInteger(hashed);
        String hashedStr = bi.toString(16);            // 120ff0
        if (hashedStr.length() % 2 != 0) {
                // Pad with 0
                hashedStr = "0"+hashedStr;
        }

        // send the hash to the server
        if(output == null)
        	gameSession.write("l" + username.trim() + new String(new char[] {(char) 27}) + hashedStr);
        else {
        	output.write("l" + username.trim() + new String(new char[] {(char) 27}) + hashedStr);
        	output.flush();
        }
	}
       // begin generating a Whirlpool password hash	
	public void register(String username, String password, int starterPokemon, int characterAppearance, String email) {
		Whirlpool hasher = new Whirlpool();
        hasher.NESSIEinit();

        // add the plaintext password to it
        hasher.NESSIEadd(password);

        // create an array to hold the hashed bytes
        byte[] hashed = new byte[64];

        // run the hash
        hasher.NESSIEfinalize(hashed);

        // this stuff basically turns the byte array into a hexstring
        java.math.BigInteger bi = new java.math.BigInteger(hashed);
        String hashedStr = bi.toString(16);            // 120ff0
        if (hashedStr.length() % 2 != 0) {
                // Pad with 0
                hashedStr = "0"+hashedStr;
        }
        
        if(output == null)
        	gameSession.write("r" + username.trim() + new String(new char[] {(char) 27}) +
                hashedStr + new String(new char[] {(char) 27}) + starterPokemon +
				   new String(new char[] {(char) 27}) + characterAppearance +
                new String(new char[] {(char) 27}) + email);
        else {
        	output.write("r" + username.trim() + new String(new char[] {(char) 27}) +
                    hashedStr + new String(new char[] {(char) 27}) + starterPokemon +
    				   new String(new char[] {(char) 27}) + characterAppearance +
                    new String(new char[] {(char) 27}) + email);
        	output.flush();
        }
	}
	
	
	public void write(String message) {
		if(output == null)
			gameSession.write(message);
		else {
			output.write(message);
			output.flush();
		}
	}
	
	public boolean isConnected() {
		return gameSession.isConnected();
	}

	public void logout() {
		if(output == null) {
			gameSession.write("Q");
			gameSession.close();
		} else {
			output.write("Q");
			output.flush();
			output.close();
		}
	}
}
