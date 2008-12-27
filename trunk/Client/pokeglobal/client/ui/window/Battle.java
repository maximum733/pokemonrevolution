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

package pokeglobal.client.ui.window;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import pokeglobal.client.GlobalGame;
import pokeglobal.client.logic.OurPokemon;
import pokeglobal.client.logic.TempPokemon;
import pokeglobal.client.network.PacketGenerator;
import pokeglobal.client.ui.base.Button;
import pokeglobal.client.ui.base.Container;
import pokeglobal.client.ui.base.Frame;
import pokeglobal.client.ui.base.ImageButton;
import pokeglobal.client.ui.base.Label;
import pokeglobal.client.ui.base.ProgressBar;
import pokeglobal.client.ui.base.event.ActionEvent;
import pokeglobal.client.ui.base.event.ActionListener;

public class Battle extends Frame {
	private PacketGenerator packetGen;
	private Image buttonEnabled, buttonPressed, buttonSmall, buttonSmallPressed;
	private ImageButton f1, f2, f3, f4, f5, f6, f7;
	private Button advance;
	private OurPokemon[] ourPokes;
	//private OurPokemon playerPoke;
	private TempPokemon[] enemyPokes;
	//private TempPokemon enemyPoke;
	private ProgressBar playerHP, enemyHP;
	private Label ourPokemon, enemyPokemon, messages;
	private int playerIndex;
	private boolean isWild;
	private Color foreground, background;
	private Container battleDisplay, battleInterface;
	private ArrayList<String>battleMessages;
	private boolean update;
	private boolean displayEnemy = false;	
    Label playerInfo = new Label();
    Label enemyInfo = new Label();
    private GlobalGame thisGame;
    
	//This handles what and where buttons should be rendering
	//0 = main
	//1 = pokemon
	//2 = bag
	private int location;
	
	public Battle(GlobalGame game) {
		thisGame = game;
		initComponents();
	}
	
	public Battle(String playerName, PacketGenerator p, OurPokemon[] playerPokes, int playerInd, GlobalGame game) {
		thisGame = game;
		packetGen = p;
		enemyPokes = new TempPokemon[6];
		ourPokes = playerPokes;
		playerIndex = playerInd;
		initComponents();
		update = false;
	}
	
	public ImageButton getRun() {
		return f7;
	}
	
	public TempPokemon[] getEnemyPokes(){
		return enemyPokes;
	}
	
	public void initComponents() {
		this.setGlassPane(true);
		this.setDraggable(false);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.getTitleBar().setVisible(false);
		battleMessages = new ArrayList<String>();
		
		background = new Color(0, 0, 0, 70);
		foreground = new Color(200, 200, 200);
		this.setBackground(background);
		
		startBattleDisplay();
		
		messages = new Label();
		messages.setForeground(foreground);
		messages.setFont(GlobalGame.getDPFontSmall());
		messages.setText("TEST");
		messages.setLocation(4, 132);
		messages.setSize(254, 32);
		messages.setVisible(true);
		battleDisplay.add(messages);
		
		advance = new Button();
		advance.setForeground(foreground);
		advance.setText("Continue");
		advance.setSize(64, 24);
		advance.setLocation((battleDisplay.getWidth() / 2) - 32, 150);
		advance.setVisible(true);
		battleDisplay.add(advance);
		
		
		try {
			buttonEnabled = new Image("res/battle/button.png");
			buttonPressed = new Image("res/battle/button_pressed.png");
		} catch (SlickException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		battleInterface = new Container();
		battleInterface.setLocation(0, 200);
		battleInterface.setSize(320, 200);
		battleInterface.setBackground(background);
		
		f1 = new ImageButton();
		f1.setFont(GlobalGame.getDPFontSmall());
		f1.setForeground(foreground);
		f1.setImage(buttonEnabled);
		f1.setText(ourPokes[0].getMoves()[0]);
		if(f1.getText().equalsIgnoreCase("null")) {
			f1.setText("");
			f1.setEnabled(false);
		}
		f1.setSize(116, 51);
		f1.setLocation(4, 0);
		f1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(location == 0) {
					//Player is attacking
					waitForTurn();
					packetGen.write("bm0");
				}
				else if(location == 1) {
					//Player is switching Pokemon
				}
			}
		});
		f1.setVisible(true);
		f1.setDownImage(buttonPressed);
		battleInterface.add(f1);
		
		f2 = new ImageButton();
		f2.setFont(GlobalGame.getDPFontSmall());
		f2.setForeground(foreground);
		f2.setBackground(background);
		f2.setImage(buttonEnabled);
		f2.setText(ourPokes[0].getMoves()[1]);
		if(f2.getText().equalsIgnoreCase("null")) {
			f2.setText("");
			f2.setEnabled(false);
		}
		f2.setVisible(true);
		f2.setSize(116, 51);
		f2.setLocation(f1.getWidth() + 18, 0);
		f2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(location == 0) {
					//Player is attacking
					waitForTurn();
					packetGen.write("bm1");
				}
				else if(location == 1) {
					//Player is switching Pokemon
				}
			}
		});
		f2.setDownImage(buttonPressed);
		battleInterface.add(f2);
		
		f3 = new ImageButton();
		f3.setFont(GlobalGame.getDPFontSmall());
		f3.setForeground(foreground);
		f3.setBackground(background);
		f3.setImage(buttonEnabled);
		f3.setText(ourPokes[0].getMoves()[2]);
		if(f3.getText().equalsIgnoreCase("null")) {
			f3.setText("");
			f3.setEnabled(false);
		}
		f3.setVisible(true);
		f3.setSize(116, 51);
		f3.setLocation(4, f1.getY() + f1.getHeight() + 8);
		f3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(location == 0) {
					//Player is attacking
					waitForTurn();
					packetGen.write("bm2");
				}
				else if(location == 1) {
					//Player is switching Pokemon
				}
			}
		});
		f3.setDownImage(buttonPressed);
		battleInterface.add(f3);
		
		f4 = new ImageButton();
		f4.setFont(GlobalGame.getDPFontSmall());
		f4.setForeground(foreground);
		f4.setBackground(background);
		f4.setImage(buttonEnabled);
		f4.setText(ourPokes[0].getMoves()[3]);
		if(f4.getText().equalsIgnoreCase("null")) {
			f4.setText("");
			f4.setEnabled(false);
		}
		f4.setVisible(true);
		f4.setSize(116, 51);
		f4.setLocation(f2.getX(), f2.getY() + f2.getHeight() + 8);
		f4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(location == 0) {
					//Player is attacking
					waitForTurn();
					packetGen.write("bm3");
				}
				else if(location == 1) {
					//Player is switching Pokemon
				}
			}
		});
		f4.setDownImage(buttonPressed);
		battleInterface.add(f4);
		
		try {
			buttonSmall = new Image("res/battle/button_small.png");
			buttonSmallPressed = new Image("res/battle/button_small_pressed.png");
		} catch (SlickException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		f5 = new ImageButton();
		f5.setFont(GlobalGame.getDPFontSmall());
		f5.setForeground(foreground);
		f5.setBackground(background);
		f5.setImage(buttonSmall);
		f5.setText("Bag");
		f5.setVisible(true);
		f5.setSize(82, 48);
		f5.setLocation(4, f3.getY() + f3.getHeight() + 8);
		f5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(location == 0) {
					//Player is using bag
					thisGame.getInterface().goToPokeGear();
				}
				else if(location == 1) {
					//Player is switching Pokemon
				}
			}
		});
		f5.setDownImage(buttonSmallPressed);
		battleInterface.add(f5);
		
		f6 = new ImageButton();
		f6.setFont(GlobalGame.getDPFontSmall());
		f6.setForeground(foreground);
		f6.setBackground(background);
		f6.setImage(buttonSmall);
		f6.setText("Pokemon");
		f6.setVisible(true);
		f6.setSize(82, 48);
		f6.setLocation(f4.getX() + (f4.getWidth() - f6.getWidth()), f5.getY());
		f6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(location == 0) {
					//Player is attacking
				}
				else if(location == 1) {
					//Player is switching Pokemon
				}
			}
		});
		f6.setDownImage(buttonSmallPressed);
		battleInterface.add(f6);
		
		f7 = new ImageButton();
		f7.setFont(GlobalGame.getDPFontSmall());
		f7.setForeground(foreground);
		f7.setBackground(background);
		f7.setImage(buttonSmall);
		f7.setText("Run");
		f7.setVisible(true);
		f7.setSize(82, 48);
		f7.setLocation(f5.getX() + f5.getWidth() + 2, f5.getY() + (f5.getHeight() / 2));
		f7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(location == 0) {
					//Player is Running
					packetGen.write("br");
				}
				else if(location == 1) {
					//Player is switching Pokemon
				}
			}
		});
		f7.setDownImage(buttonSmallPressed);
		battleInterface.add(f7);
		
		add(battleDisplay);
		add(battleInterface);
		this.setCenter();
		LoadingList.setDeferredLoading(false);
		
		// Original map music pauses
 	    GlobalGame.getSoundPlayer().pauseChannel("music");
 	    // Battle music starts
 	    GlobalGame.getSoundPlayer().playChannel("battle", "battleMusic", true);
	}
	
	public void setCenter() {
        int height = GlobalGame.height;
        int width = GlobalGame.width;
        int x = (width / 2) - ((int) this.getWidth() / 2);
        int y = (height / 2) - ((int) this.getHeight() / 2);
        this.setLocation(x, y);
	}
	
	public void goToMoveSelect() {
		if(!f1.getText().equalsIgnoreCase(""))
			f1.setEnabled(true);
		if(!f2.getText().equalsIgnoreCase(""))
			f2.setEnabled(true);
		if(!f3.getText().equalsIgnoreCase(""))
			f3.setEnabled(true);
		if(!f4.getText().equalsIgnoreCase(""))
			f4.setEnabled(true);
		f5.setEnabled(true);
		f6.setEnabled(true);
		if(isWild)
			f7.setEnabled(true);
	}
	
	public void disableInterface() {
		f1.setEnabled(false);
		f2.setEnabled(false);
		f3.setEnabled(false);
		f4.setEnabled(false);
		f5.setEnabled(false);
		f6.setEnabled(false);
		f7.setEnabled(false);
	}
	
	public void startBattleDisplay(){
		battleDisplay = new Container();
		battleDisplay.setLocation(0, 0);
		battleDisplay.setSize(256, 200);
		battleDisplay.setBackground(background);
		LoadingList.setDeferredLoading(true);
		Image pic;
		try {
			pic = new Image("res/battle/DP_darkgrass.png");
			Label bg = new Label(pic);
			bg.setSize(254, 144);
			bg.setLocation(0, 0);
			this.setSize(257, 420);
			battleDisplay.add(bg);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		ourPokemon = new Label(ourPokes[playerIndex].getBackSprite());
		ourPokemon.setBounds(-20, 68, 160, 80);
		ourPokemon.setVisible(true);
		battleDisplay.add(ourPokemon);
	}

	public void updatePP(int move, int pp){
        getCurPoke().setMovecurPP(move, pp);
        addMessage("Move " + (move + 1) + " has " + pp + " pp left");
        /*
        if (!getCurPoke().getMoves()[0].equals("null"))
        pp1.setText(getCurPoke().getMovecurPP()[0] + "/" + getCurPoke().getMovemaxPP()[0]);
        if (!getCurPoke().getMoves()[1].equals("null"))
        pp2.setText(getCurPoke().getMovecurPP()[1] + "/" + getCurPoke().getMovemaxPP()[1]);
        if (!getCurPoke().getMoves()[2].equals("null"))
        pp3.setText(getCurPoke().getMovecurPP()[2] + "/" + getCurPoke().getMovemaxPP()[2]);
        if (!getCurPoke().getMoves()[3].equals("null"))
        pp4.setText(getCurPoke().getMovecurPP()[3] + "/" + getCurPoke().getMovemaxPP()[3]);
         */
    }

	public void updateBagInfo() {
		// TODO Auto-generated method stub
	}

	public void addEnemyDisplay(){
		enemyPokemon = new Label(enemyPokes[0].getSprite());
		ourPokemon = new Label(enemyPokes[0].getSprite());
		enemyPokemon.setBounds(150, 25, 80, 80);
		enemyPokemon.setVisible(true);
		battleDisplay.add(enemyPokemon);
	}
	
	public void run(){
		endBattle("RUN");
	}
	
	public void setPlayerBattleIndex(int i) {
		playerIndex = i;
	}
	
	public void addEnemyPokemon(String pokemonName, int index, int level, int gender, 
			int hp, String species) {
		enemyPokes[index] = new TempPokemon();
		enemyPokes[index].setName(pokemonName);
		enemyPokes[index].setLevel(level);
		enemyPokes[index].setGender(gender);
		enemyPokes[index].setMaxHP(hp);
		enemyPokes[index].setCurHP(hp);
		enemyPokes[index].setSpecies(enemyPokes[index].getSpecies().valueOf(species));
		setEnemyDisplay(true);
		drawBattleData();
	}
	
	public void setEnemyDisplay(boolean flag){
		displayEnemy = flag;
	}

	public TempPokemon getCurEnemyPoke(){
		return getEnemyPokes()[0];
	}
	
	public OurPokemon getCurPoke(){
		return ourPokes[playerIndex];
	}

	public void drawBattleData(){
		// display player's data
        playerInfo.setText(getCurPoke().getSpecies().name() + "  Lv:" + getCurPoke().getLevel());
        playerInfo.setForeground(Color.white);
        playerInfo.setBounds(152, 109, 94, 12);

        // display enemy's data
        enemyInfo
        .setText(getCurEnemyPoke().getSpecies().name() + "  Lv:" + getCurEnemyPoke().getLevel());
        enemyInfo.setForeground(Color.white);
        enemyInfo.setBounds(12, 12, 96, 12);

		// show hp bar
		playerHP = new ProgressBar(0, (int)getCurPoke().getMaxHP());
		playerHP.setBounds(150, 126, 95, 10);  

		// show enemy hp bar
		enemyHP = new ProgressBar(0, (int)getCurEnemyPoke().getMaxHP());
		enemyHP.setBounds(11, 27, 95, 10);
		
		updatePlayerHP(getCurPoke().getCurHP());
		updateEnemyHP(getCurEnemyPoke().getCurHP());
		
		getContentPane().add(playerInfo);
		getContentPane().add(enemyInfo);
		getContentPane().add(playerHP);
		getContentPane().add(enemyHP);
	}
	
    public void updatePlayerHP(int newValue) {              
        playerHP.setValue(newValue);
        
        if(getCurPoke().getCurHP() > getCurPoke().getMaxHP() / 2){
                playerHP.setForeground(Color.green);
        }
        else if(getCurPoke().getCurHP() < getCurPoke().getMaxHP() / 2 && getCurPoke().getCurHP() > getCurPoke().getMaxHP() / 3){
                playerHP.setForeground(Color.orange);
        }
        else if(getCurPoke().getCurHP() < getCurPoke().getMaxHP() / 3){
                playerHP.setForeground(Color.red);
        }
        addMessage("PlayerHP: " + newValue);
    }
    
    public void updateEnemyHP(int newValue) {              
        getCurEnemyPoke().setCurHP(newValue);
        
    	enemyHP.setValue(newValue);
        
        if(newValue > getCurEnemyPoke().getMaxHP() / 2){
        		enemyHP.setForeground(Color.green);
        }
        else if(newValue < getCurEnemyPoke().getMaxHP() / 2 && getCurEnemyPoke().getCurHP() > getCurEnemyPoke().getMaxHP() / 3){
        		enemyHP.setForeground(Color.orange);
        }
        else if(newValue < getCurEnemyPoke().getMaxHP() / 3){
                enemyHP.setForeground(Color.red);
        }
        addMessage("EnemyHP: " + newValue);
    }

	
	public void switchPokemon(int trainer, int poke) {
		if(trainer == 0) {
			//We're switching
			battleDisplay.remove(ourPokemon);
			ourPokemon = new Label(ourPokes[poke].getBackSprite());
			ourPokemon.setSize(120, 80);
			ourPokemon.setLocation(32, 72);
			ourPokemon.setVisible(true);
			battleDisplay.add(ourPokemon);
			
			f1.setText(ourPokes[poke].getMoves()[0]);
			f2.setText(ourPokes[poke].getMoves()[1]);
			f3.setText(ourPokes[poke].getMoves()[2]);
			f4.setText(ourPokes[poke].getMoves()[3]);
			
			if(f1.getText().equalsIgnoreCase("null")) {
				f1.setText("");
				f1.setEnabled(false);
			}
			if(f2.getText().equalsIgnoreCase("null")) {
				f2.setText("");
				f2.setEnabled(false);
			}
			if(f3.getText().equalsIgnoreCase("null")) {
				f3.setText("");
				f3.setEnabled(false);
			}
			if(f4.getText().equalsIgnoreCase("null")) {
				f4.setText("");
				f4.setEnabled(false);
			}
		} else {
			//They're switching
			battleDisplay.remove(enemyPokemon);
			enemyPokemon = new Label(enemyPokes[poke].getSprite());
			enemyPokemon.setSize(120, 80);
			enemyPokemon.setLocation(32, 72);
			enemyPokemon.setVisible(true);
			battleDisplay.add(enemyPokemon);
		}
	}
	
	public void waitForTurn(){
        f1.setEnabled(false);
        f2.setEnabled(false);
        f3.setEnabled(false);
        f4.setEnabled(false);
        f5.setEnabled(false);
        f6.setEnabled(false);
        f7.setEnabled(false);
	}
	
	public void requestMove(){
		addMessage("You can move");
        f1.setEnabled(true);
        f2.setEnabled(true);
        f3.setEnabled(true);
        f4.setEnabled(true);
        f5.setEnabled(true);
        f6.setEnabled(true);
        f7.setEnabled(true);
	}
	
	public void addMessage(String message) {
		battleMessages.add(message);
		System.out.println(message);
	}
	
	public void endBattle(String victor) {
        if (victor.equalsIgnoreCase("RUN")){
        	addMessage("You got away safely!");
        	this.setVisible(false);
        } else if (victor.equalsIgnoreCase("CAUGHT")){
        	this.setVisible(false);
        } else {
        	addMessage(victor + " won the battle.");
        	//attackPane.setVisible(false);
        	//bagPane.setVisible(false);
        	//pokesContainer.setVisible(false);
        	this.setVisible(false);
        }
        
        // Battle music ends
        GlobalGame.getSoundPlayer().stopChannel("battleMusic");
        // Original map music starts again
 	    GlobalGame.getSoundPlayer().resumeChannel("music");
	}

	public void switchOurPoke(int i){
		//STUB		
		addMessage("Poke Switch isn't available");
	}
	
	public void switchEnemyPoke(int i){
		//STUB
		addMessage("Enemy Switch isn't available");
	}
	
	public void refreshPokemonInfo() {
        // display player's data
        playerInfo.setText(getCurPoke().getSpecies().name() + "  Lv:" + getCurPoke().getLevel());


        // display enemy's data
        enemyInfo
        .setText(getCurEnemyPoke().getSpecies().name() + "  Lv:" + getCurEnemyPoke().getLevel());
	}

	
	public void forceSwitchPoke(){
		//STUB
		addMessage("Forced Switch isn't available");
	}
	
	public void learnMove(int moveIndex, String name){
		//STUB
		addMessage("Move Learning isn't available");
	}
	
	public void giveExp(String a){
		String[] info;
		info = a.split(",");
		addMessage(ourPokes[Integer.parseInt(info[0])].getName() + " gained " +
			info[1] + " experience.");
		//0 = index | 1 = exp gained | 2 = lvl? | 3 = exp till
		if (info[2] == "Y"){
			addMessage(ourPokes[Integer.parseInt(info[0])].getName() + " leveled up!");
		}
		addMessage("It needs " + info[3] + " experience to reach" + 
			" the next level");
	}
}