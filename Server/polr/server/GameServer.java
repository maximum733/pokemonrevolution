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

package polr.server;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.simpleframework.xml.core.Persister;

import polr.server.battle.PokemonSpeciesData;
import polr.server.database.POLRDatabase;
import polr.server.mechanics.JewelMechanics;
import polr.server.mechanics.moves.MoveList;
import polr.server.mechanics.moves.MoveSetData;

//import java.sql.*;
/**
 * @author Pivot
 * 
 * GameSever class initialises a server and all databases needed
 * 
 */
public class GameServer {
	public static final int PORT = 3724;
	private static PokemonSpeciesData p;
	private static POLRDatabase polr;
	private static JewelMechanics m;
	private static int lockdownState = 0;
	private static boolean silenceState = false;

	/* Returns lock down state server is in
	 * 0 - None
	 * 1 - New Registrations
	 * 2 - All users except one flagged POK
	 * 3 - All users except GMs/Mods
	 */
	public static void setLockdown(int i) {
		lockdownState = i;
	}
	
	public static int getLockdown() {
		return lockdownState;
	}
	
	public static void setSilence(boolean silence) {
		silenceState = silence;
	}
	
	public static boolean isSilent() {
		return silenceState;
	}
	
   /**
    * Returns PokemonSpeciesData Database
    * @return PokemonSpeciesData
    */
	public static PokemonSpeciesData getSpeciesData() {
		return  p;
	}
	
   /**
    * Returns the POLR Database which contains information on move learning, pokedex, etc.
    * @return POLRDatabase
	*/
	public static POLRDatabase getPOLRDB() {
		return polr;
	}
	
   /**
    * Returns the POLR Database which contains information on move learning, pokedex, etc.
    * @return POLRDatabase
	*/
	public static JewelMechanics getMechanics() {
		return m;
	}
	
	public static void main(String[] args) {
		ByteBuffer.setUseDirectBuffers(false);
		ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

		IoAcceptor acceptor = new SocketAcceptor(6, Executors
				.newCachedThreadPool());

		SocketAcceptorConfig cfg = new SocketAcceptorConfig();
		((SocketSessionConfig) cfg.getSessionConfig()).setTcpNoDelay(true);
		cfg.getSessionConfig().setReuseAddress(true);
		// cfg.getFilterChain().addLast( "logger", new LoggingFilter() );
		cfg.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("US-ASCII"))));
		cfg.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors
				.newCachedThreadPool()));

		// Connection sqlink;
		try {
			// Class.forName("com.mysql.jdbc.Driver").newInstance();
			// sqlink =
			// DriverManager.getConnection("jdbc:mysql://localhost/pokeglobal","pokeglobal","H
			// ~%|HL\\\\ej0_LoZKc+<\\\\_0^S:Ye>xPUpqg7qp$[uw56#1%Zh+DX_<#Yel-L[du9");
			// sqlink.setAutoCommit(false);
			Persister stream = new Persister();

			MoveList ml = new MoveList(true);
			MoveSetData ms;
			m = new JewelMechanics(5);
			//Load all required databases
			JewelMechanics.loadMoveTypes("res/movetypes.txt");
			ms = stream.read(MoveSetData.class, GameServer.
                                class.getClassLoader().getResourceAsStream(
					"res/movesets.xml"));
			p = stream.read(PokemonSpeciesData.class, GameServer.
                                class.getClassLoader().getResourceAsStream(
					"res/species.xml"));
			polr = stream.read(POLRDatabase.class, GameServer.
                                class.getClassLoader().getResourceAsStream(
                                    "res/polrdb.xml"));
			
			//Start client acceptor
			acceptor.bind(new InetSocketAddress(PORT), new ClientHandler(ms, ml), cfg);
			System.out.println("Server started.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
} // end of class
