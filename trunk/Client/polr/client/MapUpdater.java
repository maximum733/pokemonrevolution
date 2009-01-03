package polr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MapUpdater extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private int m_version;
	private JLabel m_logo;
	private JLabel m_info;
	private JButton m_continue;
	
	/**
	 * Default constructor
	 * @param version
	 */
	public MapUpdater(int version) {
		super("POLR Updater");
		this.setSize(196, 196);
		this.getContentPane().setLayout(null);
		
		m_logo = new JLabel(new ImageIcon("res/pokeball.png"));
		m_logo.setSize(96, 96);
		m_logo.setLocation(38, 4);
		this.getContentPane().add(m_logo);
		
		m_info = new JLabel("Status: Updating...");
		m_info.setSize(128, 24);
		m_info.setLocation(36, 96);
		this.getContentPane().add(m_info);
		
		m_continue = new JButton("Continue");
		m_continue.setEnabled(false);
		m_continue.setSize(96, 32);
		m_continue.setLocation(48, 128);
		m_continue.addActionListener(this);
		this.getContentPane().add(m_continue);
		
		this.setLocation(32, 32);
		this.setResizable(false);
		this.setVisible(true);
		m_version = version;
	}
	
	/**
	 * Checks for map updates and updates appropriately
	 */
	public void checkSVN() {
		try {
			String url = "http://pokemonrevolution.googlecode.com/svn/trunk/Server/res/maps/";
			ArrayList<String> m_mapsList = new ArrayList<String>();
			
			URL svn = new URL(url + "list.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(svn.openStream()));
			String maps = "";
			String str;
			while ((str = in.readLine()) != null) {
				maps = maps + " " + str;
	        }
			StringTokenizer map = new StringTokenizer(maps);
			int currentVersion = Integer.parseInt(map.nextToken());
			if(m_version < currentVersion) {
				//Client is out of date, download latest maps
				String filename;
				URL nextMap;
				PrintWriter output;
				File f;
				while(map.hasMoreTokens()) {
					filename = map.nextToken();
					nextMap = new URL(url + filename);
					in = new BufferedReader(new InputStreamReader(nextMap.openStream()));
					f = new File("res/maps/" + filename);
					if(f.exists()) {
						f.delete();
					}
					output = new PrintWriter(new File("res/maps/" + filename));
					while ((str = in.readLine()) != null) {
						output.println(str);
						output.flush();
			        }
					output.close();
				}
				m_version = currentVersion;
			}
			m_info.setText("Status: Complete");
			m_continue.setEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
			m_info.setText("Status: Error!");
			m_continue.setEnabled(false);
		}
	}
	
	public int getMapRevision() {
		return m_version;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.dispose();
	}
}
