package polr.client.ui.window;

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
	private TextField m_username, m_password, m_confirmPass, m_email;
	private Label m_userLabel, m_passLabel, m_confPassLabel, m_emailLabel, m_logo;
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
		
		m_password = new TextField();
		m_password.setSize(128, 24);
		m_password.setLocation(16, 172);
		m_password.setVisible(true);
		this.add(m_password);
		
		m_login = new Button("Login");
		m_login.setSize(64, 24);
		m_login.setLocation(48, 200);
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
		m_register.setLocation(48, 224);
		m_register.setVisible(true);
		m_register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_logo.isVisible()) {
					//Go to registration
					goToRegistration();
				} else {
					//Register
				}
			}
		});
		
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
	private void goToLogin() {
		m_logo.setVisible(true);
	}
	
	/**
	 * Brings the user to the registration dialog
	 */
	private void goToRegistration() {
		m_logo.setVisible(false);
	}
}
