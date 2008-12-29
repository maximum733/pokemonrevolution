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

package polr.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.muffin.FileMuffin;

import polr.client.engine.Animator;
import polr.client.engine.GameMap;
import polr.client.engine.GameMapMatrix;
import polr.client.logic.OurPlayer;
import polr.client.logic.OurPokemon;
import polr.client.logic.Player;
import polr.client.logic.Player.Dirs;
import polr.client.network.PacketGenerator;
import polr.client.network.ProtocolHandler;
import polr.client.network.ProxiedProtocolHandler;
import polr.client.network.security.diskUtilities;
import polr.client.ui.ChatWindow;
import polr.client.ui.PartyInfo;
import polr.client.ui.base.Container;
import polr.client.ui.base.Display;
import polr.client.ui.base.MessageBox;
import polr.client.ui.speech.NPCSpeechPopup;
import polr.client.ui.window.Battle;
import polr.client.ui.window.IntroScreen;
import polr.client.ui.window.LoadingScreen;
import polr.client.ui.window.LoginScreen;
import polr.client.ui.window.MainInterface;

/**
 * GlobalGame launches the game client.
 * It runs the init() method and initialises everything needed.
 * Then, it loops between update and render and accepts input when given.
 */
public class GameClient extends BasicGame {
	static final long serialVersionUID = 6427136527014220517L;
	private static String SERVER = "";
	private static String PROXY = "";
	private static int PROXYPORT = 1080;
	private static final int PORT = 3724;
	private boolean isConnected;
	public static final int width = 800;
	public static final int height = 600;
	private ProxiedProtocolHandler proxyHandle;

	private ArrayList<Player> playerQueue;
	private static Font dpFont;
	private static Font dpFontSmall;

	private Color dimmingColor;
	
	/** Stores user's screen name. */
	public static String user;
	public int mapX;
	public int mapY;
	public boolean newMap;
	/** Stores the Player object which the user is playing as. */
	public OurPlayer thisPlayer;

	/** The top-level Sui display. */
	private Animator animator;
	private Display display;
	private static LoginScreen login;
	private Battle battle;
	private MainInterface mainInterface;
	private LoadingScreen loading;
	private static ChatWindow chat;
	
	public static PacketGenerator packetGen;
	private GameMapMatrix mapMatrix;
	private NPCSpeechPopup speechPopup;
	
	
	PartyInfo teamInfo;
	public static final String CHARSEP = new String(new char[] { 27 });
	private boolean isPlaying = false;
	public boolean showHUD = false;
	private static boolean updateTeamGUI = false;
	private static boolean updateBattle = false;
	
	public GameClient() {
		super("Pokemon Online Revolution - Beta 1");
	}
	
	public static void main(String args[]) {
		try {
			// Load the options HashMap under the Slick Muffin framework.
			// TODO: When packaging for webstart, turn this into a WebstartMuffin
			//		 (no filename required for it, and Files don't work in WS)
			AppGameContainer container = new AppGameContainer(new GameClient());
			container.setDisplayMode(width, height, false);
			container.start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error occurred! \n"+ e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(32);
		}
	}
	
	/**
	 *  Run at the beginning of the program to initialise anything needed.
	 *  
	 *  @param GameContainer g - The container to render to.
	 */
	public void init(GameContainer g) throws SlickException {
		// Get the hard disk serial number
		diskUtilities dskUtil;
		String hdserial = diskUtilities.getSerialNumber("C");
		System.out.println("HDS: " + hdserial);
		
		// read the game map using XML transformer
		g.setTargetFrameRate(30);
		g.setShowFPS(false);
		display = new Display(g);
		mapMatrix = new GameMapMatrix();
		animator = new Animator(mapMatrix, this);
		isConnected = false;
		newMap = false;
		playerQueue = new ArrayList<Player>();

		Player.loadSprites();
		loadDPFont();
		
		loading = new LoadingScreen();
		loading.setVisible(false);
		display.add(loading);

		/*
		 * title = new TitleScreen(this); title.setVisible(true);
		 * display.add(title);
		 */
		
		
		mainInterface = new MainInterface(this);
		chat = new ChatWindow(packetGen);
		mainInterface.setAlwaysOnTop(true);
		teamInfo = new PartyInfo(new OurPokemon[]{null, null, null, null, null, null}, packetGen, mainInterface);
		display.add(mainInterface);
		
		// out login window, gets shown now
		login = new LoginScreen(packetGen);
		login.setVisible(true);
		display.add(login);
		
		
		/*intro = new IntroScreen();
		display.add(intro);*/
		g.getInput().enableKeyRepeat(200, 100);
	}
	
	/**
	 * Update the game window and accept any input.
	 * 
	 * @param g			-	GameContainer to be updated
	 * @param delta		-	FPS of the GameContainer
	 */
	@Override
	public void update(GameContainer g, int delta) {
		if(!isConnected && SERVER.length() > 0) {
			connect();
		}
		if (thisPlayer != null && thisPlayer.map != null && playerQueue.size() > 0) {
			for(int i = 0; i < playerQueue.size(); i++) {
				thisPlayer.map.addPlayer(playerQueue.get(i));
			}
			playerQueue.clear();
			playerQueue.trimToSize();
		}
		if(newMap && isPlaying && loading.isVisible()) {		
			//Load the current map first
			GameMap map;
			try {
				map = new GameMap("res/maps/" + (mapX) + "." + (mapY) + ".tmx","res/maps");
				map.setGraphics(g.getGraphics());
                map.setPosition(1, 1, mapMatrix);
                map.setCurrent(true);
				mapMatrix.setMap(map, 1, 1);
			} catch (SlickException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Load the other maps
			int x = 0;
			if(mapX > -30 && mapY > -30) {
				//Only load surrounding maps if outdoors. All maps from -50,-50 to -31,-31 are indoors.
				for(int y = -1; y < 2; y++) {
					for(x = -1; x < 2; x++) {
						try {
							if(!(x == 0 && y == 0)) {
								map = new GameMap("res/maps/" + String.valueOf(mapX + x) + "." + String.valueOf(mapY + y) + ".tmx","res/maps");
								map.setGraphics(g.getGraphics());
				                map.setPosition(x + 1, y + 1, mapMatrix);
								mapMatrix.setMap(map, x + 1, y + 1);
							}
						} catch (Exception e) {
							mapMatrix.setMap(null, x + 1, y + 1);
							System.out.println("Map Load Failure: (" + (mapX + x) + ", " + (mapY + y) + ")");
						}
					}
				}
			} else {
				mapMatrix.setMap(null, 0, 0);
				mapMatrix.setMap(null, 1, 0);
				mapMatrix.setMap(null, 2, 0);
				mapMatrix.setMap(null, 0, 1);
				mapMatrix.setMap(null, 2, 1);
				mapMatrix.setMap(null, 0, 2);
				mapMatrix.setMap(null, 1, 2);
				mapMatrix.setMap(null, 2, 2);
			}
			map = mapMatrix.getMap(1, 1);
			map.setXOffset(map.getXOffset());
			map.setYOffset(map.getYOffset());
			//Tell the server the maps are loaded
			packetGen.write("um");
			
            //Reopen interface
			mainInterface.setVisible(true);
			loading.setVisible(false);
			newMap = false;
		}
		if (thisPlayer != null)
			animator.animate();
		if (battle != null && battle.isVisible() && dimmingColor != null && dimmingColor.getAlphaByte() < 150)
			dimmingColor = new Color(0, 0, 0, dimmingColor.getAlphaByte() + 2);
		if (battle != null && !battle.isVisible()) {
			//battle = null;
			showHUD = true;
		}
		if (speechPopup != null && !speechPopup.isVisible()) {
			showHUD = true;
			speechPopup = null;
		}
		if (updateTeamGUI){
			for (int i = 0; i < 6; i++){
				try{
					thisPlayer.getTeam()[i].setSprite();
					thisPlayer.getTeam()[i].resizeSprite();
					thisPlayer.getTeam()[i].setBackSprite();
				} catch (NullPointerException e){}
			}
			updateTeam(thisPlayer.getTeam());
			setTeamUpdate(false);
		}
		if (updateBattle){
			for (int i = 0; i < 6; i++){
				try{
					getBattle().getEnemyPokes()[i].setSprite();
					getBattle().getEnemyPokes()[i].resizeSprite();
					getBattle().setEnemyDisplay(true);
			    	getBattle().addEnemyDisplay();
				} catch (NullPointerException e){}
			}
			setBattleUpdate(false);
		}
		try {
			synchronized (display) {
				display.update(g, delta);
			}
		}
		catch (NullPointerException e) { 
			e.printStackTrace();
		}
	}
	
	public void setWildEnemy(String a, String b, String c, String d, String e){
		getBattle().addEnemyPokemon(a, 0, Integer.parseInt(b), Integer.parseInt(c),
				Integer.parseInt(d), e);
    	setBattleUpdate(true);
	}
	
	/**
	 * Render the maps and interface
	 */
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		if (isPlaying && !newMap && thisPlayer != null) {
			GameMap thisMap;
			arg1.setFont(getDPFont());
			arg1.scale(2, 2);
			for (int x = 0;
					x <= 2; x++) {
				for (int y = 0;
					y <= 2; y++) {
					thisMap = mapMatrix.getMap(x, y);
					if (thisMap != null && thisMap.isRendering()) {
						thisMap.render(thisMap.getXOffset() / 2,
								thisMap.getYOffset() / 2, 0, 0,
								(arg0.getScreenWidth() - thisMap.getXOffset()) / 32,
								(arg0.getScreenHeight() - thisMap.getYOffset()) / 32,
								true);
					}	
				}
			}
			
			arg1.resetTransform();
			
			if (battle != null && battle.isVisible() && dimmingColor != null) {
				arg1.setColor(dimmingColor);
				arg1.fillRect(0, 0, arg0.getWidth(), arg0.getHeight());
			}
		}
		try {
			display.render(arg0, arg1);}
		catch (NullPointerException e) { 
			e.printStackTrace();
		} catch (ConcurrentModificationException e) { }	
	}
	
	/**
	 * Update player's party.
	 * @param pokes The player's pokemon team
	 */
	public void updateTeam(OurPokemon[] pokes){
		PartyInfo info = new PartyInfo(pokes, packetGen, mainInterface);
		teamInfo = info;
		for (int i = 0; i < 6; i++){
			try{
				thisPlayer.getTeam()[i].setBackSprite();
			} catch (NullPointerException e){}
		}
		getInterface().setPartyInfo(info);
		updateBagInfo();
		setTeamUpdate(false);
	}

	public void updateBattle(){
		setBattleUpdate(false);
	}
	
	public void queuePlayer(Player newPlayer) {
		playerQueue.add(newPlayer);
	}
	
	public static void loadDPFont() throws SlickException {
		dpFont = new AngelCodeFont("res/fonts/dp.fnt",
		"res/fonts/dp.png");	
		dpFontSmall = new AngelCodeFont("res/fonts/dp-small.fnt",
		"res/fonts/dp-small.png");
	}
	
	public void updateBagInfo() {
		//if (getBattle() != null)
			//getBattle().updateBagInfo();
		this.getInterface().updateBag(thisPlayer);
	}
	public Battle getBattle() {
		return battle;
	}
	
	public void beginBattle(String enemyName, char type, int index) {
		/*TempPokemon[] enemyPokes = new TempPokemon[6];
		
		String[] pokemonStats = battleData.split(";");
		enemyPokes[0] = new TempPokemon();
		enemyPokes[0].setName(pokemonStats[0]);
		enemyPokes[0].setSpecies(Enums.Pokenum.values()
				[Integer.parseInt(pokemonStats[1])]);
		enemyPokes[0].setType1(Enums.Poketype.valueOf(
				pokemonStats[2]));
		try {
			enemyPokes[0].setType2(Enums.Poketype.valueOf(
					pokemonStats[3]));
		} catch (Exception ex) {
		}
		enemyPokes[0].setGender(Integer.parseInt(pokemonStats[4]));
		enemyPokes[0].setCurHP(Integer.parseInt(pokemonStats[5]));
		enemyPokes[0].setMaxHP(Integer.parseInt(pokemonStats[6]));
		enemyPokes[0].setLevel(Integer.parseInt(pokemonStats[7]));*/
		
		dimmingColor = new Color(0, 0, 0, 0);
		battle = new Battle(enemyName, packetGen, thisPlayer.getTeam(), index, this);
		if(type == 'p')
			battle.getRun().setEnabled(false);
		
		showHUD=false;
		getDisplay().add(battle);
	}

	/**
	 * Connects to the game server. This method will adjust if a proxy was specified.
	 */
	public void connect() {
		try {
			 // Create TCP/IP connector.
			if(PROXY.equalsIgnoreCase("")) {
				SocketConnector connector = new SocketConnector();
			      
		        SocketConnectorConfig cfg = new SocketConnectorConfig();
		        ((SocketSessionConfig) cfg.getSessionConfig()).setTcpNoDelay(true);
		        cfg.getFilterChain().addLast(
	                  "codec",
	                  new ProtocolCodecFilter(
	                          new TextLineCodecFactory(Charset.forName("US-ASCII"))));
		        cfg.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors
						.newCachedThreadPool()));
		        // Start communication.
		       ConnectFuture cf = connector.connect(new InetSocketAddress(
		                SERVER, PORT), new ProtocolHandler(this), cfg);
		        // Wait for the connection attempt to be finished
		        cf.join();
		        int i = 0;
		        while(!cf.isConnected()) {
		        	i++;
		        	//Connection attempt times out and a dialog appears
		        	if(i >= 10000) {
		        		JOptionPane.showMessageDialog(null,
								"Connection timed out.\n"
								+ "The server may be offline.\n"
								+ "Contact an administrator for assistance.");
						SERVER = "";
						return;
		        	}
		        }
		        packetGen = new PacketGenerator(cf.getSession());
			} else {
				//Player requires a proxy
				System.getProperties().put( "proxySet", "true" );
				System.getProperties().put( "socksProxyHost", PROXY );
				System.getProperties().put( "socksProxyPort", PROXYPORT );
				/*Connect to proxy and then to server */
				SocketAddress addr = new InetSocketAddress(PROXY, PROXYPORT);
				Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
				Socket gameSocket = new Socket(proxy);
				InetSocketAddress dest = new InetSocketAddress(SERVER, PORT);
				try {
					gameSocket.connect(dest);
					
					//Use input and output streams instead of apache mina
	                BufferedReader netIn = new BufferedReader(new InputStreamReader(
	                                gameSocket.getInputStream()));
	                final PrintWriter netOut = new PrintWriter(new OutputStreamWriter(
	                                gameSocket.getOutputStream()));
	                int i = 0;
	                while(!gameSocket.isConnected()) {
			        	i++;
			        	//Connection attempt times out and a dialog appears
			        	if(i >= 10000) {
			        		JOptionPane.showMessageDialog(null,
									"Connection timed out.\n"
									+ "The server may be offline.\n"
									+ "Contact an administrator for assistance.");
							SERVER = "";
							return;
			        	}
			        }
	                packetGen = new PacketGenerator(netOut);
	                proxyHandle = new ProxiedProtocolHandler(this, netIn, netOut);
	                new Thread(proxyHandle);
				} catch (SocketException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Connection to port " + PORT + " not allowed by proxy.");
					SERVER = "";
					return;
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Unknown network error occured.");
					SERVER = "";
					return;
				}
			}
	        isConnected = true;
	        login.setPacketGenerator(packetGen);
	        login.goToLogin();
	        mainInterface.setPacketGenerator(packetGen);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LoadingScreen getLoading() {
		return loading;
	}

	public void setBattle(Battle b) {
		battle = b;
	}

	public static void messageBox(String message, Container container) {
		new MessageBox(message.replace('~','\n'), container);
	}
	@Override
	public boolean closeRequested() {
		try {
			finish();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static Font getDPFont() {
		return dpFont;
	}
	
	public static Font getDPFontSmall() {
		return dpFontSmall;
	}
	
	private void finish() throws IOException {
		try {
			packetGen.logout();
		} catch (Exception e)  {
		}
		//netOut.close();
		System.exit(0);
	}
	
	/*
	 * public GlobalGame() { xOffset = 0; yOffset = 0; user = "hi";
	 * startRendering(); }
	 */
	public GameMapMatrix getMapMatrix() {
		return mapMatrix;
	}
	
	public void talkToNPC(String speech) throws SlickException {
		speechPopup = new NPCSpeechPopup(speech);
		
		showHUD = false;
		getDisplay().add(speechPopup);
	}
	
	public PartyInfo getPartyInfo(){
		return teamInfo;
	}
	
	public Display getDisplay() {
		return display;
	}

	public static LoginScreen getLogin() {
		return login;
	}


	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		super.mouseClicked(button, x, y, clickCount);
		if (speechPopup != null && 
				speechPopup.getAbsoluteBounds().contains(x, y)) {
			try {
			speechPopup.advance();
			}
			catch (Exception e) {
				getDisplay().remove(speechPopup);
				speechPopup = null;
				packetGen.write("TF");
			}
		/*} else if (getBattle() != null && getBattle().isVisible() &&
				getBattle().getBattleSpeech() != null &&
				getBattle().getBattleSpeech().getAbsoluteBounds().contains(x, y)) {
			getBattle().getBattleSpeech().advance(); */
		/*} else if (getLogin() != null && 
				getLogin().getSpeechy() != null &&
				getLogin().getSpeechy().getAbsoluteBounds().contains(x, y)) {
			getLogin().getSpeechy().advance();*/
		}
		//TODO: RELEASE FOCUS OF INTERFACE
		/*if (this.getChat() != null && this.getChat().getChatBox().hasFocus() && 
				!this.getChat().getAbsoluteBounds().contains(x, y)) {
			this.getChat().getChatBox().releaseFocus();
		}*/
	}

	public static void setServer(String string) {
		SERVER = string;
	}
	
	public static void setProxy(String proxyName, int port) {
		PROXY = proxyName;
		PROXYPORT = port;
	}
	
	public static PacketGenerator getPacketGenerator() {
		return packetGen;
	}
	
	public static void setUser(String s) {
		user = s;
	}
	
	public static void setTeamUpdate(boolean flag){
		updateTeamGUI = flag;
	}
	
	public static void setBattleUpdate(boolean flag){
		updateBattle = flag;
	}
	
	public MainInterface getInterface() {
		return mainInterface;
	}
	
	public void returnToServerSelect() {
		login = new LoginScreen(packetGen);
		login.setVisible(true);
		login.goToServerSelect();
		user = "";
		SERVER = "";
		thisPlayer = null;
		isConnected = false;
		packetGen = null;
	}
	
	public void setIsPlaying(Boolean playing) {
		isPlaying = playing;
	}
	
	/**
	 * Accepts the user input.
	 * @param key The integer representing the key pressed.
	 * @param c ???
	 */
	@Override
	public void keyPressed(int key, char c) {
		if (key == (Input.KEY_ESCAPE)) {
			try {
				finish();
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (isPlaying && thisPlayer != null
				&& thisPlayer.svrX == thisPlayer.x 
				&& thisPlayer.svrY == thisPlayer.y
				&& (loading != null && !loading.isVisible())
				&& (battle == null || !battle.isVisible())
				&& !newMap && (playerQueue.size() == 0 && thisPlayer.map.getMapPlayers().size() > 0)) {
			if (!mainInterface.isChatting() && (key == (Input.KEY_RSHIFT))) {
				//SURF TESTING
				packetGen.write("S");
			}
			else if (key == (Input.KEY_DOWN)) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Down)) {
					loading.setVisible(true);
					mainInterface.setVisible(false);
					packetGen.write("D");
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Down)) {
					//thisPlayer.moveDown();
					packetGen.write("D");
				} else {
					if (thisPlayer.facing != Dirs.Down) {
						packetGen.write("D");
					}
				}
				//getReqWindow().clear();
			}
			else if (key == (Input.KEY_UP)) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Up)) {
					loading.setVisible(true);
					mainInterface.setVisible(false);
					packetGen.write("U");
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Up)) {
					//thisPlayer.moveUp();
					packetGen.write("U");
				} else {
					if (thisPlayer.facing != Dirs.Up) {
						packetGen.write("U");
					}
				}
				//getReqWindow().clear();
			}
			else if (key == (Input.KEY_LEFT)) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Left)) {
					loading.setVisible(true);
					mainInterface.setVisible(false);
					packetGen.write("L");
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Left)) {
					//thisPlayer.moveLeft();
					packetGen.write("L");
				} else {
					if (thisPlayer.facing != Dirs.Left) {
						packetGen.write("L");
					}
				}
				//getReqWindow().clear();
			}
			else if (key == (Input.KEY_RIGHT)) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Right)) {
					loading.setVisible(true);
					mainInterface.setVisible(false);
					packetGen.write("R");
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Right)) {
					//thisPlayer.moveRight();
					packetGen.write("R");
				} else {
					if (thisPlayer.facing != Dirs.Right) {
						packetGen.write("R");
					}
				}
				//getReqWindow().clear();
			}
			else if(!mainInterface.isChatting() && (key == (Input.KEY_S))) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Down)) {
					loading.setVisible(true);
					mainInterface.setVisible(false);
					packetGen.write("D");
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Down)) {
					//thisPlayer.moveDown();
					packetGen.write("D");
				} else {
					if (thisPlayer.facing != Dirs.Down) {
						packetGen.write("D");
					}
				}
				//getReqWindow().clear();
			}
			else if (!mainInterface.isChatting() && (key == (Input.KEY_W))) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Up)) {
					loading.setVisible(true);
					mainInterface.setVisible(false);
					packetGen.write("U");
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Up)) {
					//thisPlayer.moveUp();
					packetGen.write("U");
				} else {
					if (thisPlayer.facing != Dirs.Up) {
						packetGen.write("U");
					}
				}
				//getReqWindow().clear();
			}
			else if (!mainInterface.isChatting() && (key == (Input.KEY_A))) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Left)) {
					loading.setVisible(true);
					mainInterface.setVisible(false);
					packetGen.write("L");
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Left)) {
					//thisPlayer.moveLeft();
					packetGen.write("L");
				} else {
					if (thisPlayer.facing != Dirs.Left) {
						packetGen.write("L");
					}
				}
				//getReqWindow().clear();
			}
			else if (!mainInterface.isChatting() && (key == (Input.KEY_D))) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Right)) {
					loading.setVisible(true);
					mainInterface.setVisible(false);
					packetGen.write("R");
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Right)) {
					//thisPlayer.moveRight();
					packetGen.write("R");
				} else {
					if (thisPlayer.facing != Dirs.Right) {
						packetGen.write("R");
					}
				}
				//getReqWindow().clear();
			}
			else if ((key == (Input.KEY_SPACE) || key == (Input.KEY_RCONTROL)) && !thisPlayer.isAnimating() &&
					(speechPopup == null || !speechPopup.isVisible())) {
				int facingX = thisPlayer.x;
				int facingY = thisPlayer.y;
				
				if (thisPlayer.facing == Dirs.Up) {
					facingY -= 32;
				} else if (thisPlayer.facing == Dirs.Down) {
					facingY += 32;
				} else if (thisPlayer.facing == Dirs.Left) {
					facingX -= 32;
				} else if (thisPlayer.facing == Dirs.Right) {
					facingX += 32;
				}
				
				if (thisPlayer.facing == Dirs.Up) {
					packetGen.write("TT" + GameClient.CHARSEP + thisPlayer.x + GameClient.CHARSEP + (thisPlayer.y - 32));
				} else if (thisPlayer.facing == Dirs.Down) {
					packetGen.write("TT" + GameClient.CHARSEP + thisPlayer.x + GameClient.CHARSEP + (thisPlayer.y + 32));
				} else if (thisPlayer.facing == Dirs.Left) {
					packetGen.write("TT" + GameClient.CHARSEP + (thisPlayer.x - 32) + GameClient.CHARSEP + thisPlayer.y);
				} else if (thisPlayer.facing == Dirs.Right) {
					packetGen.write("TT" + GameClient.CHARSEP + (thisPlayer.x + 32) + GameClient.CHARSEP + thisPlayer.y);
				}
			}
			// Toggles chat box on/off
			else if (key == (Input.KEY_C)) {
				
			}
			// Toggles trade/pvp on/off
			else if (key == (Input.KEY_T)){
				
			}
			else if (key == (Input.KEY_F1)){
				
			}
			// Gives chat box focus
			else if (key == (Input.KEY_ENTER)){
				
			}
			// Opens up and closes also the pokemon window
			else if (!mainInterface.isChatting() && (key == (Input.KEY_P))){
				if (mainInterface.isHidden()){
					mainInterface.goToPokemon();
				}else{
					mainInterface.hide();
				}
			}
		}
		if ((key == (Input.KEY_SPACE) || key == (Input.KEY_RCONTROL))) {
			if ((speechPopup != null && speechPopup.isVisible())) {
				try {
					speechPopup.advance();
				} catch (Exception e) { getDisplay().remove(speechPopup);
				speechPopup = null;
				packetGen.write("TF"); }
			/*} else if (getLogin().getSpeechy() != null) {
				getLogin().getSpeechy().advance();*/
			} /*else if (battle != null && battle.getBattleSpeech() != null) {
				battle.getBattleSpeech().advance();
			}*/
		}
	}

	public static ChatWindow getChat(){
		return chat;
	}
}