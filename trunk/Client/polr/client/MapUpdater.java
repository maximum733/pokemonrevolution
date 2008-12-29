package polr.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class MapUpdater {
	private int m_version;
	
	/**
	 * Default constructor
	 * @param version
	 */
	public MapUpdater(int version) {
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
				JOptionPane.showMessageDialog(null,
						"Maps Updated.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
			"Error in updating maps.");
		}
	}
	
	public int getMapRevision() {
		return m_version;
	}
}
