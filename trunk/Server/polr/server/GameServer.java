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
import polr.server.time.WorldClock;

/**
 * @author TMKCodes
 * @author shinobi
 * 
 * GameSever class initialises a server and all databases needed
 * 
 */
public class GameServer {
	public static final int PORT = 2401;
	private static PokemonSpeciesData p;
	private static POLRDatabase polr;
	private static JewelMechanics m;
	private IoAcceptor acceptor;
	private ClientHandler clientHandler;
	private static WorldClock m_worldClock;
	
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
	
	/**
	 * Returns the game world clock
	 * @return
	 */
	public static WorldClock getWorldClock() {
		return m_worldClock;
	}
	
	/**
	 * Starts the Game Server
	 */
	public void start() {
		ByteBuffer.setUseDirectBuffers(false);
		ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

		acceptor = new SocketAcceptor(4, Executors
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
			MoveSetData ms = new MoveSetData();
			p = new PokemonSpeciesData();
			m = new JewelMechanics(5);
			//ms.loadFromFile("res/dpmovesets.db");
	        //p.loadSpeciesDatabase(new File("res/dpspecies.db"));
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
			clientHandler = new ClientHandler(ms, ml);
			acceptor.bind(new InetSocketAddress(PORT), clientHandler, cfg);
			
			//Start the world clock
			m_worldClock = new WorldClock();
			new Thread(m_worldClock).start();
			System.out.println("INFO: World Clock started");
			
			System.out.println("INFO: Server startup completed");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Allows for running without a gui
	 * @param args
	 */
	public static void main(String[] args) {
		GameServer g = new GameServer();
		g.start();
	}

	/**
	 * Shuts down the game server. Returns true when completed.
	 */
	public boolean stop() {
		if(clientHandler.logoutAll()) {
			acceptor.unbindAll();
			return true;
		}
		return false;
	}
} // end of class
