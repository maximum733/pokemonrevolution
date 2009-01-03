package polr.client.ui.window;

import javax.swing.JOptionPane;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import polr.client.GameClient;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextField;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

/**
 * Handles login and registration
 * 
 * @author shinobo
 * @author TMKCodes
 *
 */
public class LoginFrame extends Frame {
	private TextField m_username, m_password, m_confirmPass;
	private Label m_userLabel, m_passLabel, m_confPassLabel, m_logo;
	private Button m_male, m_female, m_login, m_register;

	/**
	 * Default constructor
	 */
	public LoginFrame() {
		this.setSize(160, 280);
		
		try {
			m_logo = new Label(new Image("res/pokeball.png"));
			m_logo.setSize(96, 96);
			m_logo.setLocation(32, 4);
			m_logo.setVisible(true);
			this.add(m_logo);
		} catch (Exception e) {}
		
		m_userLabel = new Label("Username:");
		m_userLabel.setSize(96, 24);
		m_userLabel.pack();
		m_userLabel.setLocation(16, 108);
		m_userLabel.setVisible(true);
		this.add(m_userLabel);
		
		m_username = new TextField();
		m_username.setSize(128, 24);
		m_username.setLocation(16, 128);
		m_username.setVisible(true);
		this.add(m_username);
		
		m_passLabel = new Label("Password:");
		m_passLabel.pack();
		m_passLabel.setLocation(16, 154);
		m_passLabel.setVisible(true);
		this.add(m_passLabel);
		
		m_confPassLabel = new Label("Confirm Password:");
		m_confPassLabel.pack();
		m_confPassLabel.setLocation(16, 98);
		m_confPassLabel.setVisible(false);
		this.add(m_confPassLabel);
		
		m_password = new TextField();
		m_password.setSize(128, 24);
		m_password.setLocation(16, 172);
		m_password.setVisible(true);
		m_password.setMaskCharacter('*');
		m_password.setMaskEnabled(true);
		this.add(m_password);
		
		m_confirmPass = new TextField();
		m_confirmPass.setSize(128, 24);
		m_confirmPass.setLocation(16, 116);
		m_confirmPass.setVisible(false);
		m_confirmPass.setMaskCharacter('*');
		m_confirmPass.setMaskEnabled(true);
		this.add(m_confirmPass);
		
		m_login = new Button("Login");
		m_login.setSize(64, 24);
		m_login.setLocation(52, 200);
		m_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_logo.isVisible()) {
					//Login
					GameClient.getPacketGenerator().login(m_username.getText(), m_password.getText());
				} else {
					//Return to login
					goToLogin();
				}
			}
		});
		m_login.setVisible(true);
		this.add(m_login);
		
		m_register = new Button("Register");
		m_register.setSize(64, 24);
		m_register.setLocation(52, 226);
		m_register.setVisible(true);
		m_register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_logo.isVisible()) {
					//Go to registration
					goToRegistration();
				} else {
					//Register
					if(m_username.getText().length() >= 4) {
						if(m_username.getText().length() <= 12) {
							if(m_password.getText().length() >= 4) {
								if(m_password.getText().length() < 20) {
									if(m_password.getText().equals(m_confirmPass.getText())) {
										GameClient.getPacketGenerator().register(
												m_username.getText(), m_password.getText(),
												m_male.isEnabled() ? 2 : 1);
									} else {
										JOptionPane.showMessageDialog(null, "Passwords do not match.");
									}
								} else {
									JOptionPane.showMessageDialog(null, "Password too long, must shorter than 20 characters.");
								}
							} else {
								JOptionPane.showMessageDialog(null, "Password too short, must be at least 4 characters.");
							}
						} else {
							JOptionPane.showMessageDialog(null, "Username too long, must be no more than 12 characters.");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Username too short, must be at least 4 characters.");
					}
				}
			}
		});
		this.add(m_register);
		
		m_male = new Button("Male");
		m_male.setSize(64, 24);
		m_male.setLocation(16, 150);
		m_male.setVisible(false);
		m_male.setEnabled(false);
		m_male.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_female.setEnabled(true);
				m_male.setEnabled(false);
			}
		});
		this.add(m_male);
		
		m_female = new Button("Female");
		m_female.setSize(64, 24);
		m_female.setLocation(82, 150);
		m_female.setVisible(false);
		m_female.setEnabled(true);
		m_female.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_female.setEnabled(false);
				m_male.setEnabled(true);
			}
		});
		this.add(m_female);
		
		this.setResizable(false);
		this.setTitle("Login");
		this.setLocation(320, 280);
		this.getTitleBar().getCloseButton().setVisible(false);
		this.setBackground(new Color(0, 0, 0, 70));
		this.setVisible(false);
	}
	
	/**
	 * Brings the user to the login dialog
	 */
	public void goToLogin() {
		m_logo.setVisible(true);
		m_login.setText("Login");
		m_userLabel.setLocation(16, 108);
		m_username.setLocation(16, 128);
		m_passLabel.setLocation(16, 154);
		m_password.setLocation(16, 172);
		m_confirmPass.setVisible(false);
		m_confPassLabel.setVisible(false);
		m_male.setVisible(false);
		m_female.setVisible(false);
		this.setTitle("Login");
	}
	
	/**
	 * Brings the user to the registration dialog
	 */
	private void goToRegistration() {
		m_logo.setVisible(false);
		m_login.setText("Cancel");
		m_userLabel.setLocation(16, 4);
		m_username.setLocation(16, 24);
		m_passLabel.setLocation(16, 52);
		m_password.setLocation(16, 70);
		m_confirmPass.setVisible(true);
		m_confPassLabel.setVisible(true);
		m_male.setVisible(true);
		m_female.setVisible(true);
		this.setTitle("Registration");
	}
	
	public String getUsername() {
		return m_username.getText().trim();
	}
}
