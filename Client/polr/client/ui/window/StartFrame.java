package polr.client.ui.window;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import polr.client.GameClient;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextArea;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

/**
 * Displays disclaimer at startup.
 * 
 * @author TMKCodes
 *
 */
public class StartFrame extends Frame {
	private StartFrame m_default;

	/**
	 * Default constructor
	 */
	public StartFrame() {
		m_default = this;
		this.setSize(800, 600);
		this.setLocation(0, 0);
		Color black = new Color(0, 0, 0);
		this.setBackground(black);
		
		try {
			Label l = new Label(new Image("res/header.png"));
			l.pack();
			l.setLocation(16, 0);
			l.setVisible(true);
			this.add(l);
		}catch(Exception e){}
		
		TextArea text = new TextArea();
		text.setText("Pokemon is a registered trademark of Nintendo (1995 - Present). \n \nPokemon Online Revolution (POLR) is a non-profit, free, open source Pokemon MMORPG." +
				" We will never charge any subscription fees for POLR and will never compete with Nintendo's trademark " +
				"using this project. POLR is a solely PC based game, we do not support the use of it on any other platform.\n" +
				"\nPOLR is in no way affiliated or supported by Nintendo. Pokemon and all related products\n" +
				"are ownership of Nintendo. Please support Nintendo by purchasing their products.");
		text.setBackground(black);
		text.setSize(560, 280);
		text.setLocation(128, 256);
		text.setVisible(true);
		text.setBorderRendered(false);
		text.setEditable(false);
		this.add(text);
		
		Button b = new Button("Continue");
		b.setSize(64, 32);
		b.setLocation(366, 440);
		b.setVisible(true);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameClient.getStartScreen().getServerSelector().setVisible(true);
				GameClient.getStartScreen().getServerSelector().getServers();
				m_default.getCloseButton().press();
			}
		});
		this.add(b);
		this.setGlassPane(true);
		this.getTitleBar().setVisible(false);
		this.setBorderRendered(false);
		this.setDraggable(false);
		this.setResizable(false);
		this.setVisible(true);
	}
}
