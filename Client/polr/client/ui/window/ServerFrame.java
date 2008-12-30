package polr.client.ui.window;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.loading.LoadingList;

import polr.client.GameClient;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextArea;
import polr.client.ui.base.TextField;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

/**
 * A server selector.
 * 
 * @author shinobi
 *
 */
public class ServerFrame extends Frame {
	private TextField m_privateHost, m_privatePort, m_proxyHost, m_proxyPort;
	private Button m_privateConnect, m_setProxy;
	
	/**
	 * Default constructor
	 */
	public ServerFrame() {
		super();
		this.setSize(600, 520);
		this.setLocation(100, 20);
		this.setTitle("Welcome");
		this.setDraggable(false);
		this.setResizable(false);
		this.getTitleBar().getCloseButton().setVisible(false);
		
		this.setVisible(true);
	}
	
	/**
	 * Sets up the UI
	 */
	private void initGUI() {
		Color white = new Color(255, 255, 255, 90);
		Color black = new Color(0, 0, 0);
		this.setBackground(white);
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
		
		Label s_3 = new Label("Private Server");
		s_3.setForeground(black);
		s_3.setSize(128, 24);
		s_3.setLocation(300, 360);
		this.add(s_3);
		
		Label s_4 = new Label("Proxy Server");
		s_4.setForeground(black);
		s_4.setSize(128, 24);
		s_4.setLocation(300, 420);
		this.add(s_4);
		
		Label s_5 = new Label("Host:");
		s_5.setForeground(black);
		s_5.setSize(64, 24);
		s_5.setLocation(168, 386);
		this.add(s_5);
		
		m_privateHost = new TextField();
		m_privateHost.setSize(102, 24);
		m_privateHost.setEditable(true);
		m_privateHost.setLocation(224, 386);
		this.add(m_privateHost);
		
		Label s_6 = new Label("Port:");
		s_6.setForeground(black);
		s_6.setSize(64, 24);
		s_6.setLocation(320, 386);
		this.add(s_6);
		
		m_privatePort = new TextField();
		m_privatePort.setSize(64, 24);
		m_privatePort.setEditable(true);
		m_privatePort.setLocation(368, 386);
		this.add(m_privatePort);
		
		m_privateConnect = new Button("Connect");
		m_privateConnect.setSize(64, 24);
		m_privateConnect.setLocation(446, 386);
		m_privateConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_privatePort.getText() != null && !m_privatePort.getText().equalsIgnoreCase("")) {
					if(m_privateHost.getText() != null && !m_privateHost.getText().equalsIgnoreCase("")) {
						GameClient.setPort(Integer.parseInt(m_privatePort.getText()));
						GameClient.setServer(m_privateHost.getText());
					}
				}
			}
		});
		this.add(m_privateConnect);
	}
	
	/**
	 * Gets the latest list of servers
	 */
	public void getServers() {
		for(int i = 0; i < this.getChildCount(); i++) {
			this.remove(this.getChild(i));
		}
		URL list;
		try {
			ArrayList<String> official = new ArrayList<String>();
			ArrayList<String> non_official = new ArrayList<String>();
			list = new URL("http://pokemonrevolution.org/getservers.php");
			BufferedReader in = new BufferedReader(new InputStreamReader(list.openStream()));
			String str;
			while ((str = in.readLine()) != null) {
				switch(Integer.parseInt(str.substring(str.length() - 2, str.length() - 1))) {
				case 0:
					non_official.add(str);
					break;
				case 1:
					official.add(str);
					break;
				default:
					non_official.add(str);
					break;
				}
	        }
			String [] officialList = new String[official.size()];
			for(int i = 0; i < official.size(); i++) {
				officialList[i] = official.get(i);
			}
			String [] nonOfficialList = new String[non_official.size()];
			for(int i = 0; i < non_official.size(); i++) {
				nonOfficialList[i] = non_official.get(i);
			}
			ServerListPane list1 = new ServerListPane(officialList);
			list1.setLocation(180, 150);
			this.add(list1);
			ServerListPane list2 = new ServerListPane(nonOfficialList);
			list2.setLocation(180, 280);
			this.add(list2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.initGUI();
	}
}