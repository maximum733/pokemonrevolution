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

/**
 * The login screen class. It is a Frame, not a window or game state.
 * We didn't want to spend time managing states, and window decorations 
 * were unnecessary.
*
 * @author    Pivot
 * @author    Sienide
 *
 * Credits:
 * - Decypher, for designing graphics used here
 */

package polr.client.ui.window;
import java.util.Calendar;
import java.util.regex.Pattern;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import polr.client.GameClient;
import polr.client.network.PacketGenerator;
import polr.client.ui.StarterChoices;
import polr.client.ui.base.Button;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextField;
import polr.client.ui.base.ToggleButton;
import polr.client.ui.base.Window;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;


public class LoginScreen extends Window {
       static final long serialVersionUID = -261619500525051531L;

       private Label userLabel;
       private Label passLabel;
       private Label background;
       private TextField authUser;
       private TextField authPass;
       private StarterChoices starterChoices;
       private TextField regConfirmPass;
       private TextField regEmail;
       private Button login;
       private Button register;
       private String userName;
       private ToggleButton male;
       private ToggleButton female;
       private ToggleButton kanto;
       private ToggleButton johto;
       private ToggleButton hoenn;
       private ToggleButton sinnoh;
       private PacketGenerator packetGen;
       
       
       public String getUser() {
               return userName;
       }

       public LoginScreen(PacketGenerator out) {
               super();

               packetGen = out;
               initGUI();
       }
       public void register(String username, String password) {
               // send the hash to the server
       }
       
       @SuppressWarnings("deprecation")
	private void initGUI() {
    	   this.setSize(800, 600);
    	   this.setLocation(0, 0);
           try {
        	   userName = "";
        	   Image bg = new Image("res/title.png");
        	   background = new Label(bg);
        	   background.setSize(800, 600);
        	   background.setLocation(0, 0);
        	   add(background);
        	   
        	   userLabel = new Label();
        	   userLabel.setText("Username:");
        	   userLabel.setFont(GameClient.getDPFontSmall());
        	   userLabel.setForeground(new Color(255, 255, 255));
        	   userLabel.setBackground(new Color(0, 0, 0));
        	   userLabel.setSize(64, 32);
        	   userLabel.setLocation(526, 453);
        	   userLabel.setVisible(false);
        	   add(userLabel);
        	   
        	   passLabel = new Label();
        	   passLabel.setText("Password:");
        	   passLabel.setFont(GameClient.getDPFontSmall());
        	   passLabel.setForeground(new Color(255, 255, 255));
        	   passLabel.setBackground(new Color(0, 0, 0));
        	   passLabel.setSize(64, 32);
        	   passLabel.setLocation(526, 483);
        	   passLabel.setVisible(false);
        	   add(passLabel);
        	   
        	   authUser = new TextField();
        	   authUser.setLocation(600, 460);
        	   authUser.setSize(128, 24);
        	   authUser.setVisible(false);
        	   add(authUser);
        	   
        	   authPass = new TextField();
        	   authPass.setLocation(600, 490);
        	   authPass.setSize(128, 24);
        	   authPass.setVisible(false);
        	   authPass.setMaskCharacter('*');
        	   authPass.setMaskEnabled(true);
        	   add(authPass);
        	   
        	   login = new Button();
        	   login.setText("Login");
        	   login.setSize(64, 24);
        	   login.setLocation(665, 520);
        	   login.setVisible(false);
        	   login.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(regConfirmPass.isVisible())
						goToLogin();
					else {
						login.setEnabled(false);
						register.setEnabled(false);
						GameClient.setUser(authUser.getText()); 
						packetGen.login(authUser.getText(), authPass.getText());
					}
				}
        		   
        	   });
        	   add(login);
        	   
        	   register = new Button();
        	   register.setText("Register");
        	   register.setSize(64, 24);
        	   register.setLocation(600, 520);
        	   register.setVisible(false);
        	   register.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					register();
				}
        	   });
        	   add(register);
        	   
        	   regConfirmPass = new TextField();
        	   regConfirmPass.setSize(128, 24);
        	   regConfirmPass.setMaskCharacter('*');
        	   regConfirmPass.setMaskEnabled(true);
        	   regConfirmPass.setLocation(346, 472);
        	   regConfirmPass.setVisible(false);
        	   add(regConfirmPass);
        	   
        	   regEmail = new TextField();
        	   regEmail.setSize(128, 24);
        	   regEmail.setLocation(346, 514);
        	   regEmail.setVisible(false);
        	   add(regEmail);
        	   
        	   starterChoices = new StarterChoices();
        	   starterChoices.setVisible(false);
        	   add(starterChoices);
        	   
        	   male = new ToggleButton();
        	   male.setText("Male");
        	   male.setSelected(true);
        	   male.setSize(48, 24);
        	   male.setLocation(524, 516);
        	   male.setVisible(false);
        	   male.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					female.setSelected(false);
					male.setSelected(true);
				}   
        	   });
        	   add(male);
        	   
        	   female = new ToggleButton();
        	   female.setText("Female");
        	   female.setSelected(false);
        	   female.setSize(48, 24);
        	   female.setLocation(572, 516);
        	   female.setVisible(false);
        	   female.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					male.setSelected(false);
					female.setSelected(true);
				}   
        	   });
        	   add(female);
        	   
        	   kanto = new ToggleButton();
        	   kanto.setText("Kanto");
        	   kanto.setSelected(false);
        	   kanto.setSize(64, 20);
        	   kanto.setLocation(646, 516);
        	   kanto.setVisible(false);
        	   kanto.setEnabled(false);
        	   kanto.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					johto.setSelected(false);
					hoenn.setSelected(false);
					sinnoh.setSelected(false);
					kanto.setSelected(true);
				}   
        	   });
        	   add(kanto);
        	   
        	   johto = new ToggleButton();
        	   johto.setText("Johto");
        	   johto.setSelected(true);
        	   johto.setSize(64, 20);
        	   johto.setLocation(708, 516);
        	   johto.setVisible(false);
        	   johto.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					johto.setSelected(true);
					hoenn.setSelected(false);
					sinnoh.setSelected(false);
					kanto.setSelected(false);
				}   
        	   });
        	   add(johto);
        	   
        	   hoenn = new ToggleButton();
        	   hoenn.setText("Hoenn");
        	   hoenn.setSelected(false);
        	   hoenn.setSize(64, 20);
        	   hoenn.setLocation(646, 536);
        	   hoenn.setVisible(false);
        	   hoenn.setEnabled(false);
        	   hoenn.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					johto.setSelected(false);
					hoenn.setSelected(true);
					sinnoh.setSelected(false);
					kanto.setSelected(false);
				}   
        	   });
        	   add(hoenn);
        	   
        	   sinnoh = new ToggleButton();
        	   sinnoh.setText("Sinnoh");
        	   sinnoh.setSelected(false);
        	   sinnoh.setSize(64, 20);
        	   sinnoh.setLocation(708, 536);
        	   sinnoh.setVisible(false);
        	   sinnoh.setEnabled(false);
        	   sinnoh.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					johto.setSelected(false);
					hoenn.setSelected(false);
					sinnoh.setSelected(true);
					kanto.setSelected(false);
				}   
        	   });
        	   add(sinnoh);
        	   
        	   // Intro screen music
        	   Calendar now = Calendar.getInstance();
        	   System.out.println(now.get(Calendar.MONTH));
        	   System.out.println(now.get(Calendar.DAY_OF_MONTH));
           }
           catch (Exception e) {
        	   e.printStackTrace();
           }
       }
       
       private void authDeleteActionPerformed(ActionEvent evt) {
               if (authUser.getText() == "") {
                       GameClient.messageBox("You must enter a username.", this);
               }
               else if(authPass.getText() == "") {
                       GameClient.messageBox("You must enter a password.", this);
               }
               else {
                     
               }
       }

       private boolean isEmailValid(String email){
               //Initialize reg ex for email.
               String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

               Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
               if (pattern.matcher(email).matches())
                       return true;
               else
                       return false;
       }
       
       private boolean validData() {
               if (starterChoices.getChoice() != -1) {
                       if (!authPass.getText().equals("") &&
                                       authPass.getText().equals(
                                               regConfirmPass.getText())) {
                                       if (isEmailValid(regEmail.getText())) {
                                               if (!authUser.getText().equals("")) {
                                                       if (!authUser.getText().contains(" ")) {
                                                               return true;
                                                       } else {
                                                               GameClient.messageBox("You may not have spaces in your username.", this);
                                                       }
                                               } else {
                                                       GameClient.messageBox("Type in your username.", this);
                                               }
                                       } else {
                                               GameClient.messageBox("Invalid e-mail address.", this);
                                       }
                       } else {
                               GameClient.messageBox("Your password must be typed in both boxes.", this);
                       }
               } else {
                       GameClient.messageBox("Make sure your Pokemon is selected correctly.", this);
               }
               return false;
       }
       // somebody clicked the Confirm Register button!
       private void register() {
               if (validData()) {
            	   System.out.println(authUser.getText() + " " + authPass.getText() + " " + starterChoices.getChoice() + " " + regEmail.getText());
            	   if(female.isSelected())
            		   packetGen.register(authUser.getText(), authPass.getText(), starterChoices.getChoice(), 12, regEmail.getText());
            	   else
            		   packetGen.register(authUser.getText(), authPass.getText(), starterChoices.getChoice(), 29, regEmail.getText());
               }
       }
	
       public void goToLogin() {
    	   passLabel.setVisible(true);
    	   userLabel.setVisible(true);
    	   authUser.setVisible(true);
    	   authPass.setVisible(true);
    	   regConfirmPass.setVisible(false);
    	   regEmail.setVisible(false);
    	   login.setText("Login");
    	   authUser.setLocation(600, 460);
    	   authPass.setLocation(600, 490);
    	   login.setLocation(665, 520);
    	   register.setLocation(600, 520);
    	   login.setVisible(true);
    	   register.setVisible(true);
    	   starterChoices.setVisible(false);
    	   male.setVisible(false);
    	   female.setVisible(false);
    	   kanto.setVisible(false);
    	   johto.setVisible(false);
    	   hoenn.setVisible(false);
    	   sinnoh.setVisible(false);
    	   try {
    		   LoadingList.setDeferredLoading(true);
    		   Image bg = new Image("res/title.png");
    		   background.setImage(bg);
    		   LoadingList.setDeferredLoading(false);
       	   } catch (SlickException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
       	   }
       }
       
       public void goToRegistration() {
    	   passLabel.setVisible(false);
   			userLabel.setVisible(false);
    	   authUser.setLocation(346, 390);
    	   authPass.setLocation(346, 432);
    	   regConfirmPass.setVisible(true);
    	   regEmail.setVisible(true);
    	   login.setLocation(586, 560);
    	   register.setLocation(520, 560);
    	   login.setText("Cancel");
    	   starterChoices.setVisible(true);
    	   male.setVisible(true);
    	   female.setVisible(true);
    	   kanto.setVisible(true);
    	   johto.setVisible(true);
    	   hoenn.setVisible(true);
    	   sinnoh.setVisible(true);
    	   try {
				LoadingList.setDeferredLoading(true);
				Image bg = new Image("res/reg.png");
				background.setImage(bg);
				LoadingList.setDeferredLoading(false);
    	   } catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	   }
       }

	public void setPacketGenerator(PacketGenerator packetGen2) {
		packetGen = packetGen2;
	}

	public void goToServerSelect() {
		goToLogin();
		authUser.setVisible(false);
		authPass.setVisible(false);
		login.setVisible(false);
		register.setVisible(false);
		passLabel.setVisible(false);
		userLabel.setVisible(false);
	}

	public void enable() {
		login.setEnabled(true);
		register.setEnabled(true);
	}
}