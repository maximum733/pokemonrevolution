package polr.client.ui.window;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.loading.LoadingList;

import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextArea;

public class ServerFrame extends Frame {
	private LoginFrame m_login;
	
	public ServerFrame(LoginFrame l) {
		m_login = l;
		this.setSize(600, 480);
		Color white = new Color(255, 255, 255, 90);
		Color black = new Color(0, 0, 0);
		this.setBackground(white);
		this.setLocation(100, 60);
		this.setTitle("Welcome");
		this.setDraggable(false);
		this.setResizable(false);
		this.getTitleBar().getCloseButton().setVisible(false);
		
		try {
			LoadingList.setDeferredLoading(true);
			Label oak = new Label(new Image("res/oak.png"));
			oak.setSize(160, 298);
			oak.setLocation(12, 64);
			oak.setVisible(true);
			this.add(oak);
			LoadingList.setDeferredLoading(false);
		} catch (Exception e) { e.printStackTrace(); }
		
		TextArea speech = new TextArea();
		speech.setText("Hello! I'm Professor Oak! Welcome to the world of \n" +
				"Pokemon Online Revolution! \n \nBefore we begin, you need to select a game server to play on." +
				" Once you are connected to a server, your adventure into the world of POLR begins!");
		speech.setForeground(black);
		speech.setBackground(new Color(255, 255, 255));
		speech.setSize(400, 128);
		speech.setVisible(true);
		speech.setLocation(180, 12);
		speech.setEditable(false);
		this.add(speech);
		
		Label s_1 = new Label("Official Servers");
		s_1.setForeground(black);
		s_1.setSize(128, 24);
		s_1.setLocation(300, 128);
		this.add(s_1);
		
		Label s_2 = new Label("Non-Official Servers");
		s_2.setForeground(black);
		s_2.setSize(128, 24);
		s_2.setLocation(300, 256);
		this.add(s_2);
		
		this.setVisible(true);
	}
}