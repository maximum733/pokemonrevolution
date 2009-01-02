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
 * @author TMKCodes
 *
 */
public class CreditsFrame extends Frame {
	private CreditsFrame m_credits;
	
	/**
	 *  Default constructor
	 */
	public CreditsFrame(){
		m_credits = this;
		this.setSize(800, 600);
		this.setLocation(0, 0);
		Color Black = new Color(0, 0, 0);
		this.setBackground(Black);
		
		// Text area 
		TextArea text = new TextArea();
		text.setText("Programmers: TMKCodes, Shinobi" + 
				"Mappers: LordAdmiral" +
				"More to come...");
		text.setBackground(Black);
		text.setSize(560, 280);
		text.setLocation(128, 20);
		text.setVisible(true);
		text.setBorderRendered(false);
		text.setEditable(false);
		this.add(text);
		
		// Continue button
		Button b = new Button("Continue");
		b.setSize(64, 32);
		b.setLocation(366, 440);
		b.setVisible(true);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_credits.getCloseButton().press();
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
