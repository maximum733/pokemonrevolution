package polr.client.ui.window;

import org.newdawn.slick.Color;

import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextField;

public class LoginFrame extends Frame {
	private TextField m_username;
	private TextField m_password;
	private Label m_userLabel;
	private Label m_passLabel;

	public LoginFrame() {
		this.setSize(224, 280);
		m_username = new TextField();
		m_username.setSize(128, 24);
		m_username.setLocation(32, 64);
		m_username.setVisible(true);
		this.add(m_username);
		
		this.setDraggable(false);
		this.setResizable(false);
		this.setTitle("Login");
		this.setLocation(530, 96);
		this.getTitleBar().getCloseButton().setVisible(false);
		this.setBackground(new Color(0, 0, 0, 70));
	}
}
