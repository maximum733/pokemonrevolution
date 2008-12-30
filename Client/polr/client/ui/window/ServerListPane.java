package polr.client.ui.window;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import polr.client.ui.base.Button;
import polr.client.ui.base.Container;
import polr.client.ui.base.Label;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;
import polr.client.ui.base.event.ServerEvent;

/**
 * Displays a list of servers available to connect to
 * 
 * @author shinobi
 *
 */
public class ServerListPane extends Container {
	private int index;
	private Button scrollUp;
	private Button scrollDown;
	private ArrayList<Button> serverConnect;
	private ArrayList<Label> serverName;
	private Color black;
	
	/**
	 * Default constructor. Requires arrays of server info to be passed in.
	 * @param servers
	 */
	public ServerListPane(String [] servers) {
		super();
		this.setSize(400, 128);
		index = 0;
		black = new Color(0, 0, 0);
		serverConnect = new ArrayList<Button>();
		serverName = new ArrayList<Label>();
		for(int i = 0; i < servers.length; i++) {
			String [] details = servers[i].split(" ");
			ServerEvent s = new ServerEvent(details[1], Integer.parseInt(details[2]));
			Button b = new Button("Connect");
			b.setSize(64, 24);
			b.setVisible(true);
			b.addActionListener(s);
			serverConnect.add(b);
			
			Label l = new Label(details[0]);
			l.setVisible(true);
			l.pack();
			l.setForeground(black);
			serverName.add(l);
		}
		scrollUp = new Button("");
		scrollUp.setSize(24, 16);
		scrollUp.setLocation(4, 4);
		scrollUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index = index < serverName.size() - 1 ? index + 1 : index;
				scroll();
			}
		});
		
		scrollDown = new Button("");
		scrollDown.setSize(24, 16);
		scrollDown.setLocation(4, this.getHeight() - 48);
		scrollDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index = index > 0 ? index - 1 : index;
				scroll();
			}
		});
		
		scroll();
		this.setVisible(true);
	}
	
	/**
	 * Handles scrolling through the list
	 */
	public void scroll() {
		this.removeAll();
		this.add(scrollUp);
		this.add(scrollDown);
		for(int i = index; i < index + 3 && i < serverConnect.size(); i++) {
			serverName.get(i).setLocation(36, (i * 24) + 4);
			this.add(serverName.get(i));
			serverConnect.get(i).setLocation(320, i * 24);
			this.add(serverConnect.get(i));
		}
	}
}
