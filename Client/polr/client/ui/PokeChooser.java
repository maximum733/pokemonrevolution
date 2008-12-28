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

package polr.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import polr.client.GameClient;
import polr.client.logic.Enums;

public class PokeChooser extends javax.swing.JDialog {
	static final long serialVersionUID = 8428417333515943016L;

	JComboBox poke1;
	JComboBox poke2;
	JComboBox poke3;
	JComboBox poke4;
	JComboBox poke5;
	JComboBox poke6;
	JButton setTeam;
	Random random = new Random();
	private JLabel picPanel6;
	private JLabel picPanel5;
	private JLabel picPanel2;
	private JLabel picPanel3;
	private JLabel picPanel4;
	private JLabel picPanel1;
	JButton cancel;
	private JButton randomize;

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager
					.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	java.io.PrintWriter netOut;

	public PokeChooser(java.io.PrintWriter out) {
		netOut = out;
		this.initGUI();
		this.setVisible(true);
	}

	public PokeChooser(java.io.PrintWriter out, GameClient g) {
		super(new JFrame(), true);
		netOut = out;
		this.initGUI();
		this.setVisible(true);
		cancel.setEnabled(false);
	}

	public void initGUI() {
		this.setTitle("Choose your team");

		getContentPane().setLayout(null);
		this.setPreferredSize(new java.awt.Dimension(246, 334));
		this.setResizable(false);

		// models contain items in list
		ComboBoxModel pokeModel1 = new DefaultComboBoxModel(Enums.Pokenum
				.values());
		ComboBoxModel pokeModel2 = new DefaultComboBoxModel(Enums.Pokenum
				.values());
		ComboBoxModel pokeModel3 = new DefaultComboBoxModel(Enums.Pokenum
				.values());
		ComboBoxModel pokeModel4 = new DefaultComboBoxModel(Enums.Pokenum
				.values());
		ComboBoxModel pokeModel5 = new DefaultComboBoxModel(Enums.Pokenum
				.values());
		ComboBoxModel pokeModel6 = new DefaultComboBoxModel(Enums.Pokenum
				.values());
		{
			poke1 = new JComboBox();
			getContentPane().add(poke1);
			poke1.setModel(pokeModel1);
			poke1.setBounds(63, 12, 167, 23);
			poke1.setSelectedIndex(random.nextInt(492));
			poke1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					loadImage(poke1, picPanel1);
				}
			});

		}
		{
			poke2 = new JComboBox();
			getContentPane().add(poke2);
			poke2.setModel(pokeModel2);
			poke2.setBounds(63, 52, 167, 23);
			poke2.setSelectedIndex(random.nextInt(492));
			poke2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					loadImage(poke2, picPanel2);
				}
			});
		}
		{
			poke3 = new JComboBox();
			getContentPane().add(poke3);
			poke3.setModel(pokeModel3);
			poke3.setBounds(63, 92, 167, 23);
			poke3.setSelectedIndex(random.nextInt(492));
			poke3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					loadImage(poke3, picPanel3);
				}
			});
		}
		{
			poke4 = new JComboBox();
			getContentPane().add(poke4);
			poke4.setModel(pokeModel4);
			poke4.setBounds(63, 132, 167, 23);
			poke4.setSelectedIndex(random.nextInt(492));
			poke4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					loadImage(poke4, picPanel4);
				}
			});
		}
		{
			poke5 = new JComboBox();
			getContentPane().add(poke5);
			poke5.setModel(pokeModel5);
			poke5.setBounds(63, 172, 167, 23);
			poke5.setSelectedIndex(random.nextInt(492));
			poke5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					loadImage(poke5, picPanel5);
				}
			});
		}
		{
			poke6 = new JComboBox();
			getContentPane().add(poke6);
			poke6.setModel(pokeModel6);
			poke6.setBounds(63, 212, 167, 23);
			poke6.setSelectedIndex(random.nextInt(492));
			poke6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					loadImage(poke6, picPanel6);
				}
			});
		}
		{
			cancel = new JButton();
			getContentPane().add(cancel);
			cancel.setText("Cancel");
			cancel.setBounds(1, 269, 79, 31);
		}
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// close window
				close();
			}
		});
		{
			setTeam = new JButton();
			getContentPane().add(setTeam);
			setTeam.setText("Done");
			setTeam.setBounds(165, 269, 75, 31);
		}
		{
			picPanel1 = new JLabel();
			getContentPane().add(picPanel1);
			picPanel1.setBounds(12, 12, 40, 40);
			picPanel1.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
		}
		{
			picPanel4 = new JLabel();
			getContentPane().add(picPanel4);
			picPanel4.setBorder(BorderFactory.createBevelBorder(
					BevelBorder.RAISED, null, null, null, null));
			picPanel4.setBounds(12, 132, 40, 40);
		}
		{
			picPanel3 = new JLabel();
			getContentPane().add(picPanel3);
			picPanel3.setBorder(BorderFactory.createBevelBorder(
					BevelBorder.RAISED, null, null, null, null));
			picPanel3.setBounds(12, 92, 40, 40);
		}
		{
			picPanel2 = new JLabel();
			getContentPane().add(picPanel2);
			picPanel2.setBorder(BorderFactory.createBevelBorder(
					BevelBorder.RAISED, null, null, null, null));
			picPanel2.setBounds(12, 52, 40, 40);
		}
		{
			picPanel5 = new JLabel();
			getContentPane().add(picPanel5);
			picPanel5.setBorder(BorderFactory.createBevelBorder(
					BevelBorder.RAISED, null, null, null, null));
			picPanel5.setBounds(12, 172, 40, 40);
		}
		{
			picPanel6 = new JLabel();
			getContentPane().add(picPanel6);
			picPanel6.setBorder(BorderFactory.createBevelBorder(
					BevelBorder.RAISED, null, null, null, null));
			picPanel6.setBounds(12, 212, 40, 40);
		}
		{
			randomize = new JButton();
			getContentPane().add(randomize);
			randomize.setText("Random");
			randomize.setBounds(80, 269, 85, 31);
		}
		randomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				randomizePokes();
			}
		});

		loadImage(poke1, picPanel1);
		loadImage(poke2, picPanel2);
		loadImage(poke3, picPanel3);
		loadImage(poke4, picPanel4);
		loadImage(poke5, picPanel5);
		loadImage(poke6, picPanel6);

		setTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setPokes();
				close();
			}
		});
		this.setSize(246, 334);
		setCenter();
		pack();
	}

	public void setCenter() {
		int height = this.getToolkit().getScreenSize().height;
		int width = this.getToolkit().getScreenSize().width;
		int x = (width / 2) - 123;
		int y = (height / 2) - 167;
		this.setBounds(x, y, 246, 334);
	}

	public int setSpriteNumber(JComboBox selector) {
		int i = 0;
		if (selector.getSelectedIndex() <= 385) {
			i = selector.getSelectedIndex() + 1;
		} else if (selector.getSelectedIndex() <= 388) {
			i = 386;
		} else if (selector.getSelectedIndex() <= 414) {
			i = selector.getSelectedIndex() - 2;
		} else if (selector.getSelectedIndex() <= 416) {
			i = 413;
		} else {
			i = selector.getSelectedIndex() - 4;
		}
		return i;
	}

	public void loadImage(JComboBox selector, JLabel surface) {
		String path;
		if (selector.getSelectedIndex() + 1 != 29 &
			selector.getSelectedIndex() + 1 != 30 &
			selector.getSelectedIndex() + 1 != 31 &
			selector.getSelectedIndex() + 1 != 96 &
			selector.getSelectedIndex() + 1 != 113 &
			selector.getSelectedIndex() + 1 != 115 &
			selector.getSelectedIndex() + 1 != 124 &
			selector.getSelectedIndex() + 1 != 238 &
			selector.getSelectedIndex() + 1 != 241 &
			selector.getSelectedIndex() + 1 != 242 &
			selector.getSelectedIndex() + 1 != 314 &
			selector.getSelectedIndex() + 1 != 380 &
			selector.getSelectedIndex() + 1 != 413 &
			selector.getSelectedIndex() + 1 != 416 &
			selector.getSelectedIndex() + 1 != 440 &
			selector.getSelectedIndex() + 1 != 478)
		{
			switch (String.valueOf(selector.getSelectedIndex() + 1).length()) {
			case 1:
				path = "res/sprites/front/normal/00"
					+ setSpriteNumber(selector) + "-3.png";
				break;
			case 2:
				path = "res/sprites/front/normal/0"
					+ setSpriteNumber(selector) + "-3.png";
				break;
			case 3:
				path = "res/sprites/front/normal/"
						+ setSpriteNumber(selector) + "-3.png";
				break;
			default:
				throw new RuntimeException("invalid sprite number");
			}
			ImageIcon pic = null;
			try{
			pic = new ImageIcon(this.getClass().getClassLoader().getResource(path));
			
			java.awt.Image pic2 = createImage(new java.awt.image.FilteredImageSource(
					pic.getImage().getSource(), new java.awt.image.CropImageFilter(
							0, 0, 80, 80)));
		
			pic.setImage(pic2
					.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
			}catch (NullPointerException e){System.out.println(setSpriteNumber(selector));}
			surface.setIcon(pic);
		}
		else {
			switch (String.valueOf(selector.getSelectedIndex() + 1).length()) {
				case 1:
					path = "res/sprites/front/normal/00"
						+ setSpriteNumber(selector) + "-2.png";
					break;
				case 2:
					path = "res/sprites/front/normal/0"
						+ setSpriteNumber(selector) + "-2.png";
					break;
				case 3:
					path = "res/sprites/front/normal/"
						+ setSpriteNumber(selector) + "-2.png";
					break;
				default:
					throw new RuntimeException("invalid sprite number");
				}
			ImageIcon pic = null;
			
			try{
			pic = new ImageIcon(this.getClass().getClassLoader().getResource(path));
			}catch (NullPointerException e){System.out.println(
					selector.getSelectedItem() + " " + setSpriteNumber(selector));}
			
			
			java.awt.Image pic2 = createImage(new java.awt.image.FilteredImageSource(
					pic.getImage().getSource(), new java.awt.image.CropImageFilter(
							0, 0, 80, 80)));
		
			pic.setImage(pic2
					.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
			
			surface.setIcon(pic);
		}
	}

	public void setPokes() {

		System.out.println("P," + poke1.getSelectedIndex() + ","
				+ poke2.getSelectedIndex() + "," + poke3.getSelectedIndex()
				+ "," + poke4.getSelectedIndex() + ","
				+ poke5.getSelectedIndex() + "," + poke6.getSelectedIndex());
		netOut.print("P," + poke1.getSelectedIndex() + ","
				+ poke2.getSelectedIndex() + "," + poke3.getSelectedIndex()
				+ "," + poke4.getSelectedIndex() + ","
				+ poke5.getSelectedIndex() + "," + poke6.getSelectedIndex());
		netOut.println();
		netOut.flush();
	}

	public void close() {
		this.setVisible(false);
		this.dispose();
	}

	public void randomizePokes() {
		poke1.setSelectedIndex(random.nextInt(495));
		poke2.setSelectedIndex(random.nextInt(495));
		poke3.setSelectedIndex(random.nextInt(495));
		poke4.setSelectedIndex(random.nextInt(495));
		poke5.setSelectedIndex(random.nextInt(495));
		poke6.setSelectedIndex(random.nextInt(495));
	}

	public static void main(String[] args) {
		new PokeChooser(null);
	}
}
