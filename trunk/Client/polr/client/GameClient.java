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

import java.io.File;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ConcurrentModificationException;
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


import polr.client.engine.Animator;
import polr.client.engine.GameMap;
import polr.client.engine.GameMapMatrix;
import polr.client.engine.MapLoader;
import polr.client.logic.OurPlayer;
import polr.client.logic.Player;
import polr.client.logic.Player.Dirs;
import polr.client.network.PacketGenerator;
import polr.client.network.ProtocolHandler;
import polr.client.network.security.diskUtilities;
import polr.client.ui.PartyInfo;
import polr.client.ui.base.Container;
import polr.client.ui.base.Display;
import polr.client.ui.base.MessageBox;
import polr.client.ui.base.Sui;
import polr.client.ui.base.theme.RedTheme;
import polr.client.ui.screen.LoadingScreen;
import polr.client.ui.screen.StartScreen;

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
	private static int PORT = 2401;
	private boolean isConnected;
	public static final int width = 800;
	public static final int height = 600;

	private static Font dpFont;
	private static Font dpFontSmall;

	private Color dimmingColor;
	
	/** Stores user's screen name. */
	public static String user;
	public int mapX, mapY;
	public boolean newMap;
	/** Stores the Player object which the user is playing as. */
	public OurPlayer thisPlayer;

	/** The top-level Sui display. */
	private Animator animator;
	private Display display;
	private static StartScreen login;
	
	public static PacketGenerator packetGen;
	private GameMapMatrix mapMatrix;
	private MapLoader m_mapLoader;
	private LoadingScreen loading;
	private Settings m_settings;
	
	PartyInfo teamInfo;
	public static final String CHARSEP = new String(new char[] { 27 });
	private boolean isPlaying = false;
	public boolean showHUD = false;
	private static boolean updateTeamGUI = false;
	private static boolean updateBattle = false;
	
	/**
	 * Default constructor
	 * @param settings
	 */
	public GameClient(Settings settings) {
		super("Pokemon Online Revolution - Beta 1");
		m_settings = settings;
	}
	
	public static void main(String args[]) {
		try {
			//Load the user's settings (Create settings file if first time)
			Settings settings = new Settings();

			//Check for map updates (Download maps if first time)
			MapUpdater updater = new MapUpdater(settings.getMapRevision());
			updater.checkSVN();
			while(updater != null && updater.isVisible());
			settings.setMapRevision(updater.getMapRevision());
			settings.saveSettings();
			
			AppGameContainer container = new AppGameContainer(new GameClient(settings));
			container.setDisplayMode(settings.getScreenWidth(), settings.getScreenHeight(), false);
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
		
		g.setTargetFrameRate(50);
		g.setShowFPS(false);
		display = new Display(g);
		mapMatrix = new GameMapMatrix();
		animator = new Animator(mapMatrix, this);
		isConnected = false;
		newMap = false;

		Player.loadSprites();
		loadDPFont();
		
		loading = new LoadingScreen();
		loading.setVisible(false);
		display.add(loading);
		
		login = new StartScreen(packetGen);
		login.setVisible(true);
		display.add(login);
		
		g.getInput().enableKeyRepeat(50, 300);
		
		//sets the skin and updates the display
		RedTheme skin = new RedTheme();
		Sui.setTheme(skin);
		Sui.updateComponentTreeSkin(display); //<-- updates the skin

	}
	
	/**
	 * Update the game window and accept any input.
	 * 
	 * @param g			-	GameContainer to be updated
	 * @param delta		-	FPS of the GameContainer
	 */
	@Override
	public void update(GameContainer g, int delta) {
		try {
			synchronized (display) {
				display.update(g, delta);
			}
		}
		catch (NullPointerException e) { 
			e.printStackTrace();
		}
		if(!isConnected && SERVER.length() > 0) {
			connect();
		}
		if(newMap && isPlaying && loading.isVisible()) {		
			//Load the maps
			if(m_mapLoader == null)
				m_mapLoader = new MapLoader(mapMatrix, mapX, mapY, g.getGraphics(), loading);
			else {
				m_mapLoader.setMapX(mapX);
				m_mapLoader.setMapY(mapY);
			}
			m_mapLoader.load();
			newMap = false;
		}
		if (thisPlayer != null)
			animator.animate();
		/*if (battle != null && battle.isVisible() && dimmingColor != null && dimmingColor.getAlphaByte() < 150)
			dimmingColor = new Color(0, 0, 0, dimmingColor.getAlphaByte() + 2);
		if (battle != null && !battle.isVisible()) {
			//battle = null;
			showHUD = true;
		}*/
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
                             if (thisMap != null && thisMap.isRendering())
                                     thisMap.render((thisMap.getXOffset() / 2),
                                                     thisMap.getYOffset() / 2, 0, 0,
                                                     (arg0.getScreenWidth() - thisMap.getXOffset()) / 32,
                                                     (arg0.getScreenHeight() - thisMap.getYOffset()) / 32,
                                                     true);
                     }
             }
			
			arg1.resetTransform();
			
			/*if (battle != null && battle.isVisible() && dimmingColor != null) {
				arg1.setColor(dimmingColor);
				arg1.fillRect(0, 0, arg0.getWidth(), arg0.getHeight());
			}*/
		}
		try {
			display.render(arg0, arg1);}
		catch (NullPointerException e) { 
			e.printStackTrace();
		} catch (ConcurrentModificationException e) { }	
	}
	
	public static void loadDPFont() throws SlickException {
		dpFont = new AngelCodeFont("res/fonts/dp.fnt",
		"res/fonts/dp.png");	
		dpFontSmall = new AngelCodeFont("res/fonts/dp-small.fnt",
		"res/fonts/dp-small.png");
	}

	/**
	 * Connects to the game server. This method will adjust if a proxy was specified.
	 */
	public void connect() {
		try {
			 // Create TCP/IP connector.
			if(PROXY.equalsIgnoreCase("")) {
				System.out.println("Attempting to connect to " + SERVER + " on port " + PORT);
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
			}
	        isConnected = true;
	        login.setPacketGenerator(packetGen);
	        login.getServerSelector().setVisible(false);
	        login.getLoginFrame().setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LoadingScreen getLoading() {
		return loading;
	}

	public static void messageBox(String message, Container container) {
		new MessageBox(message.replace('~','\n'), container);
	}
	
	@Override
	public boolean closeRequested() {
		try {
			packetGen.logout();
		} catch (Exception e) {}
		return true;
	}
	
	public static Font getDPFont() {
		return dpFont;
	}
	
	public static Font getDPFontSmall() {
		return dpFontSmall;
	}
	
	public GameMapMatrix getMapMatrix() {
		return mapMatrix;
	}
	
	public void talkToNPC(String speech) throws SlickException {
		
	}
	
	public PartyInfo getPartyInfo(){
		return teamInfo;
	}
	
	public Display getDisplay() {
		return display;
	}

	public static StartScreen getStartScreen() {
		return login;
	}


	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		super.mouseClicked(button, x, y, clickCount);
		/*if (speechPopup != null && 
				speechPopup.getAbsoluteBounds().contains(x, y)) {
			try {
			speechPopup.advance();
			}
			catch (Exception e) {
				getDisplay().remove(speechPopup);
				speechPopup = null;
				packetGen.write("TF");
			}
		}*/
	}

	public static void setServer(String string) {
		SERVER = string;
	}
	
	public static void setProxy(String proxyName, int port) {
		PROXY = proxyName;
		PROXYPORT = port;
	}
	
	public static void setPort(int m_port) {
		PORT = m_port;
	}
	
	public static PacketGenerator getPacketGenerator() {
		return packetGen;
	}
	
	public static void setUser(String s) {
		user = s;
	}
	
	public void setIsPlaying(Boolean playing) {
		isPlaying = playing;
	}
	
	public void setIsConnected(Boolean b) {
		isConnected = b;
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
				packetGen.logout();
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (isPlaying && thisPlayer != null
				&& thisPlayer.svrX == thisPlayer.x 
				&& thisPlayer.svrY == thisPlayer.y
				&& (loading != null && !loading.isVisible())
				&& !newMap && (thisPlayer.map.getMapPlayers().size() > 0)) {
			if (key == (Input.KEY_DOWN)) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Down)) {
					packetGen.moveDown();
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Down)) {
					packetGen.moveDown();
				} else if (thisPlayer.facing != Dirs.Down) {
					packetGen.moveDown();
				}
			}
			else if (key == (Input.KEY_UP)) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Up)) {
					packetGen.moveUp();
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Up)) {
					//thisPlayer.moveUp();
					packetGen.moveUp();
				} else if (thisPlayer.facing != Dirs.Up) {
					packetGen.moveUp();
				}
			}
			else if (key == (Input.KEY_LEFT)) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Left)) {
					packetGen.moveLeft();
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Left)) {
					//thisPlayer.moveLeft();
					packetGen.moveLeft();
				} else if (thisPlayer.facing != Dirs.Left) {
					packetGen.moveLeft();
				}
			}
			else if (key == (Input.KEY_RIGHT)) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Right)) {
					packetGen.moveRight();
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Right)) {
					packetGen.moveRight();
				} else if (thisPlayer.facing != Dirs.Right) {
					packetGen.moveRight();
				}
			}
			else if(/*!mainInterface.isChatting() &&*/ (key == (Input.KEY_S))) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Down)) {
					packetGen.moveDown();
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Down)) {
					packetGen.moveDown();
				} else if (thisPlayer.facing != Dirs.Down) {
					packetGen.moveDown();
				}
			}
			else if (/*!mainInterface.isChatting() &&*/ (key == (Input.KEY_W))) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Up)) {
					packetGen.moveUp();
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Up)) {
					packetGen.moveUp();
				} else if (thisPlayer.facing != Dirs.Up) {
					packetGen.moveUp();
				}
			}
			else if (/*!mainInterface.isChatting() &&*/ (key == (Input.KEY_A))) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Left)) {
					packetGen.moveLeft();
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Left)) {
					packetGen.moveLeft();
				} else if (thisPlayer.facing != Dirs.Left) {
					packetGen.moveLeft();
				}
			}
			else if (/*!mainInterface.isChatting() &&*/ (key == (Input.KEY_D))) {
				if (thisPlayer.map.isNewMap(thisPlayer, Dirs.Right)) {
					packetGen.moveRight();
				} else if (!thisPlayer.map.isColliding(thisPlayer, Dirs.Right)) {
					packetGen.moveRight();
				} else if (thisPlayer.facing != Dirs.Right) {
					packetGen.moveRight();
				}
			}
			else if ((key == (Input.KEY_SPACE) || key == (Input.KEY_RCONTROL)) && !thisPlayer.isAnimating()
					/*(speechPopup == null || !speechPopup.isVisible())*/) {
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
			/*else if (!mainInterface.isChatting() && (key == (Input.KEY_P))){
				if (mainInterface.isHidden()){
					mainInterface.goToPokemon();
				}else{
					mainInterface.hide();
				}
			}*/
		}
		if ((key == (Input.KEY_SPACE) || key == (Input.KEY_RCONTROL))) {
			/*if ((speechPopup != null && speechPopup.isVisible())) {
				try {
					speechPopup.advance();
				} catch (Exception e) { getDisplay().remove(speechPopup);
				speechPopup = null;
				packetGen.write("TF"); }*/
			/*} else if (getLogin().getSpeechy() != null) {
				getLogin().getSpeechy().advance();*/
			/*} else if (battle != null && battle.getBattleSpeech() != null) {
				battle.getBattleSpeech().advance();
			}*/
		}
		
	}
	/**
	 * Slick Native library finder.
	 */
	  static {
	      String s = File.separator;
	      // Modify this to point to the location of the native libraries.
	      String newLibPath = System.getProperty("user.dir") + s + "lib" + s + "native";
	      System.setProperty("java.library.path", newLibPath);

	      Field fieldSysPath = null;
	      try {
	          fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
	      } catch (SecurityException e) {
	          e.printStackTrace();
	      } catch (NoSuchFieldException e) {
	          e.printStackTrace();
	      }

	      if (fieldSysPath != null) {
	          try {
	         fieldSysPath.setAccessible(true);
	         fieldSysPath.set(System.class.getClassLoader(), null);
	          } catch (IllegalArgumentException e) {
	         e.printStackTrace();
	          } catch (IllegalAccessException e) {
	         e.printStackTrace();
	          }
	      }
	    } 
}