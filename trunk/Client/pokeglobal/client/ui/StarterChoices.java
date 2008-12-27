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

package pokeglobal.client.ui;


import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.loading.LoadingList;

import pokeglobal.client.ui.base.Frame;
import pokeglobal.client.ui.base.ToggleButton;
import pokeglobal.client.ui.base.event.ActionEvent;
import pokeglobal.client.ui.base.event.ActionListener;
/*
 * The UI where the user can choose their starter during registration
 */
public class StarterChoices extends Frame {
	private ToggleButton Button1;
	private ToggleButton Button2;
	private ToggleButton Button3;
	private ToggleButton Button4;
	private ToggleButton Button5;
	private ToggleButton Button6;
	private ToggleButton Button7;
	private ToggleButton Button8;
	private ToggleButton Button9;
	private ToggleButton Button10;
	private ToggleButton Button11;
	private ToggleButton Button12;
	private ToggleButton Button13;
	private ToggleButton Button14;
	private ToggleButton Button15;
	private Image starter1;
	private Image starter2;
	private Image starter3;
	private Image starter4;
	private Image starter5;
	private Image starter6;
	private Image starter7;
	private Image starter8;
	private Image starter9;
	private Image starter10;
	private Image starter11;
	private Image starter12;
	private Image starter13;
	private Image starter14;
	private Image starter15;
	
	private int poke = -1;
	
	public StarterChoices() {
		this.initGUI();
		this.setVisible(true);
	}
	
	public void initGUI() {
		
		this.setTitle("Choose your starter");
		this.setBorderRendered(false);
		//RowLayout layout = new RowLayout(true);
		//getContentPane().setLayout(layout);
		{
			Button1 = new ToggleButton();
			getContentPane().add(Button1);
			Button1.setText("");
			Button1.setBounds(12, 8, 32, 32);
			Button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				starterPicked(1);
			}
			});
		}
		{
			Button2 = new ToggleButton();
			getContentPane().add(Button2);
			Button2.setText("");
			Button2.setBounds(48, 8, 32, 32);
			Button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(2);
				}
				});
		}
		{
			Button3 = new ToggleButton();
			getContentPane().add(Button3);
			Button3.setText("");
			Button3.setBounds(84, 8, 32, 32);
			Button3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(3);
				}
				});
		}
		
		{
			Button4 = new ToggleButton();
			getContentPane().add(Button4);
			Button4.setText("");
			Button4.setBounds(120, 8, 32, 32);
			Button4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(4);
				}
				});
		}
		{
			Button5 = new ToggleButton();
			getContentPane().add(Button5);
			Button5.setText("");
			Button5.setBounds(156, 8, 32, 32);
			Button5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(5);
				}
				});
		}
		{
			Button6 = new ToggleButton();
			getContentPane().add(Button6);
			Button6.setText("");
			Button6.setBounds(192, 8, 32, 32);
			Button6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(6);
				}
				});
		}
		{
			Button7 = new ToggleButton();
			getContentPane().add(Button7);
			Button7.setText("");
			Button7.setBounds(12, 44, 32, 32);
			Button7.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(7);
				}
				});
		}
		{
			Button8 = new ToggleButton();
			getContentPane().add(Button8);
			Button8.setText("");
			Button8.setBounds(48, 44, 32, 32);
			Button8.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(8);
				}
				});
		}
		{
			Button9 = new ToggleButton();
			getContentPane().add(Button9);
			Button9.setText("");
			Button9.setBounds(84, 44, 32, 32);
			Button9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(9);
				}
				});
		}
		{
			Button10 = new ToggleButton();
			getContentPane().add(Button10);
			Button10.setText("");
			Button10.setBounds(120, 44, 32, 32);
			Button10.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(10);
				}
				});
		}
		{
			Button11 = new ToggleButton();
			getContentPane().add(Button11);
			Button11.setText("");
			Button11.setBounds(156, 44, 32, 32);
			Button11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(11);
				}
				});
		}
		{
			Button12 = new ToggleButton();
			getContentPane().add(Button12);
			Button12.setText("");
			Button12.setBounds(192, 44, 32, 32);
			Button12.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(12);
				}
				});
		}
		{
			Button13 = new ToggleButton();
			getContentPane().add(Button13);
			Button13.setText("");
			Button13.setBounds(12, 80, 32, 32);
			Button13.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(13);
				}
				});
		}
		{
			Button14 = new ToggleButton();
			getContentPane().add(Button14);
			Button14.setText("");
			Button14.setBounds(48, 80, 32, 32);
			Button14.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(14);
				}
				});
		}
		{
			Button15 = new ToggleButton();
			getContentPane().add(Button15);
			Button15.setText("");
			Button15.setBounds(84, 80, 32, 32);
			Button15.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					starterPicked(15);
				}
				});
		}
		this.setResizable(false);
		this.setSize(236, Button12.getAbsoluteY() + Button12.getHeight() + 4);
		this.setDraggable(false);
		this.setLocation(516, 378);
		this.getTitleBar().setVisible(false);
		this.setBackground(new Color(255, 255, 255, 40));
		loadImages();
		//pack();
	}
	public void loadImages() {
		try{
			LoadingList.setDeferredLoading(true); 
			starter1 = new Image("res/sprites/icons/001.gif");
			starter2 = new Image("res/sprites/icons/004.gif");
			starter3 = new Image("res/sprites/icons/007.gif");
			starter4 = new Image("res/sprites/icons/152.gif");
			starter5 = new Image("res/sprites/icons/155.gif");
			starter6 = new Image("res/sprites/icons/158.gif");
			starter7 = new Image("res/sprites/icons/252.gif");
			starter8 = new Image("res/sprites/icons/255.gif");
			starter9 = new Image("res/sprites/icons/258.gif");
			starter10 = new Image("res/sprites/icons/387.gif");
			starter11 = new Image("res/sprites/icons/390.gif");
			starter12 = new Image("res/sprites/icons/393.gif");
			starter13 = new Image("res/sprites/icons/025.gif");
			starter14 = new Image("res/sprites/icons/183.gif");
			starter15 = new Image("res/sprites/icons/133.gif");
			LoadingList.setDeferredLoading(false);
			Button1.setImage(starter1);
			Button2.setImage(starter2);
			Button3.setImage(starter3);
			Button4.setImage(starter4);
			Button5.setImage(starter5);
			Button6.setImage(starter6);
			Button7.setImage(starter7);
			Button8.setImage(starter8);
			Button9.setImage(starter9);
			Button10.setImage(starter10);
			Button11.setImage(starter11);
			Button12.setImage(starter12);
			Button13.setImage(starter13);
			Button14.setImage(starter14);
			Button15.setImage(starter15);
		}catch (Exception e){e.printStackTrace();}
	}
    
	public void starterPicked(int choice){
		Button1.setSelected(false);
		Button2.setSelected(false);
		Button3.setSelected(false);
		Button4.setSelected(false);
		Button5.setSelected(false);
		Button6.setSelected(false);
		Button7.setSelected(false);
		Button8.setSelected(false);
		Button9.setSelected(false);
		Button10.setSelected(false);
		Button11.setSelected(false);
		Button12.setSelected(false);
		Button13.setSelected(false);
		Button14.setSelected(false);
		Button15.setSelected(false);
		switch(choice){
		case 1:
				System.out.println("Bulbasaur");
				Button1.setSelected(true);
				break;
		case 2: 
				System.out.println("Charmander");
				Button2.setSelected(true);
				break;
		case 3: 
				System.out.println("Squirtle");
				Button3.setSelected(true);
				break;
		case 4: 
				System.out.println("Chikorita");
				Button4.setSelected(true);
				break;
		case 5: 
				System.out.println("Cyndaquil");
				Button5.setSelected(true);
				break;
		case 6: 
				System.out.println("Totodile");
				Button6.setSelected(true);
				break;
		case 7: 
				System.out.println("Treecko");
				Button7.setSelected(true);
				break;
		case 8: 
				System.out.println("Torchic");
				Button8.setSelected(true);
				break;
		case 9: 
				System.out.println("Mudkip");
				Button9.setSelected(true);
				break;
		case 10:
				System.out.println("Turtwig");
				Button10.setSelected(true);
				break;
		case 11:
				System.out.println("Chimchar");
				Button11.setSelected(true);
				break;
		case 12:
				System.out.println("Piplup");
				Button12.setSelected(true);
				break;
		}
		if (poke == choice) poke = -1;
		else poke = choice;
	}
	
	public int getChoice() {
		return poke;
	}
	
	public void showHidden() {
		this.setSize(236, Button15.getAbsoluteY() + Button15.getHeight() + 4);
	}
}