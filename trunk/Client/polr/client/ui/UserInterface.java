package polr.client.ui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import polr.client.logic.Item;
import polr.client.logic.OurPokemon;
import polr.client.ui.base.Clock;
import polr.client.ui.base.Display;
import polr.client.ui.base.Frame;
import polr.client.ui.base.ImageButton;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
import polr.client.ui.chat.FriendList;
import polr.client.ui.chat.LocalChat;

/**
 * Handles the main UI
 * @author shinobi
 */
public class UserInterface extends Frame {
	private ImageButton m_openFriendList, m_openChat, m_openPokemonInfo, m_openPokedex, m_openBag, m_openHelp;
	private ArrayList<String> m_friends;
	private OurPokemon [] m_pokemon;
	private Display m_display;
	private LocalChat m_localChat;
	private FriendList m_friendList;
	private Pokedex m_pokedex;
	private Bag m_bag;
	private Help m_help;
	private ArrayList<Item> m_items;
	private Clock m_clock;
	
	/**
	 * Default constructor.
	 * @param d
	 */
	public UserInterface(Display d) {
		m_display = d;
		m_friends = new ArrayList<String>();
		m_items = new ArrayList<Item>();
		m_help = new Help();
		m_localChat = new LocalChat();
		
		this.setSize(48, 256);
		this.setDraggable(false);
		this.setResizable(false);
		this.setBackground(new Color(0, 0, 0, 90));
		this.getTitleBar().setVisible(false);
		this.setLocation(0, -24);
		this.setBorderRendered(false);
		
		//Set up the buttons
		try {
			m_clock = new Clock();
			m_clock.setLocation(4, 8);
			this.getContentPane().add(m_clock);
			
			m_openFriendList = new ImageButton();
			m_openFriendList.setImage(new Image("res/ui/friend_32x32.png"));
			m_openFriendList.setDownImage(new Image("res/ui/friendPressed_32x32.png"));
			m_openFriendList.pack();
			m_openFriendList.setLocation(8, 32);
			m_openFriendList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openFriendList();
				}
			});
			this.getContentPane().add(m_openFriendList);
			
			m_openChat = new ImageButton();
			m_openChat.setImage(new Image("res/ui/chat_32x32.png"));
			m_openChat.setDownImage(new Image("res/ui/chatPressed_32x32.png"));
			m_openChat.pack();
			m_openChat.setLocation(8, 68);
			m_openChat.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openChat();
				}
			});
			this.getContentPane().add(m_openChat);
			
			m_openPokemonInfo = new ImageButton();
			m_openPokemonInfo.setImage(new Image("res/ui/pokemon_32x32.png"));
			m_openPokemonInfo.setDownImage(new Image("res/ui/pokemonPressed_32x32.png"));
			m_openPokemonInfo.pack();
			m_openPokemonInfo.setLocation(8, 104);
			m_openPokemonInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openPartyWindow();
				}
			});
			this.getContentPane().add(m_openPokemonInfo);
			
			m_openPokedex = new ImageButton();
			m_openPokedex.setImage(new Image("res/ui/pokedex_32x32.png"));
			m_openPokedex.setDownImage(new Image("res/ui/pokedexPressed_32x32.png"));
			m_openPokedex.pack();
			m_openPokedex.setLocation(8, 142);
			m_openPokedex.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openPokedex();
				}
			});
			this.getContentPane().add(m_openPokedex);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the Pokemon array to be used.
	 * @param pokes
	 */
	public void setPokemon(OurPokemon [] pokes) {
		m_pokemon = pokes;
	}
	
	/**
	 * Adds a friend to the friend list
	 * @param friend
	 */
	public void addFriend(String friend) {
		m_friends.add(friend);
		if(m_friendList != null)
			m_friendList.addFriend(friend);
	}
	
	/**
	 * Removes a friend from the friend list
	 * @param friend
	 */
	public void removeFriend(String friend) {
		m_friends.remove(friend);
		if(m_friendList != null)
			m_friendList.removeFriend(friend);
	}
	
	/**
	 * Add a message to the local chat window
	 * @param message
	 */
	public void addLocalChatMessage(String message) {
		if(m_localChat != null)
			m_localChat.addMessage(message);
	}
	
	/**
	 * Opens the friend list
	 */
	public void openFriendList() {
		if(m_friendList != null && m_display.containsChild(m_friendList))
			m_display.remove(m_friendList);
		m_friendList = new FriendList(m_friends);
		m_display.add(m_friendList);
	}
	
	/**
	 * Opens the pokedex
	 */
	public void openPokedex() {
		if(m_pokedex != null && m_display.containsChild(m_pokedex))
			m_display.remove(m_pokedex);
		m_pokedex = new Pokedex();
		m_display.add(m_pokedex);
	}
	
	/**
	 * Opens the local chat
	 */
	public void openChat() {
		if(m_localChat != null && m_display.containsChild(m_localChat))
			m_localChat.setVisible(true);
		else {
			m_localChat = new LocalChat();
			m_display.add(m_localChat);
		}
	}
	
	/**
	 * Opens the player's bag
	 */
	public void openBag() {
		if(m_bag != null && m_display.containsChild(m_bag))
			m_display.remove(m_bag);
		m_bag = new Bag(m_items);
		m_display.add(m_bag);
	}
	
	/**
	 * Opens the help window
	 */
	public void openHelp() {
		if(m_help != null && m_display.containsChild(m_help))
			m_display.remove(m_help);
		m_display.add(m_help);
	}
	
	/**
	 * Opens Pokemon info window.
	 */
	public void openPartyWindow() {
		
	}

	/**
	 * Starts the world clock. Only to be called once!
	 * @param hours
	 * @param minutes
	 */
	public void startClock(int hours, int minutes) {
		m_clock.setTime(hours, minutes);
		new Thread(m_clock).start();
	}
	
	/**
	 * Returns if the user is chatting (used for WSAD controls)
	 * @return
	 */
	public boolean isChatting() {
		return m_localChat.isChatting();
	}
}
