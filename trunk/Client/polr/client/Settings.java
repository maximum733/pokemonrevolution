package polr.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 
 * Stores user settings and allows the user to change them.
 * 
 * @author TMKCodes
 * @author Shinobi
 *
 */
public class Settings extends JFrame {
	private static final long serialVersionUID = 1L;
	private int m_width;
	private int m_height;
	private int m_mapRevision;
	private JButton m_save;
	
	/**
	 * Default constructor
	 */
	public Settings() {
		super("POLR Settings");
		this.setSize(320, 280);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		m_save = new JButton("Save");
		m_save.setSize(64, 32);
		m_save.setLocation(256, 256);
		m_save.setVisible(true);
		this.getContentPane().add(m_save);
		
		this.setVisible(false);
		this.loadSettings();
	}
	
	/**
	 * Returns the preferred screen width
	 * @return
	 */
	public int getScreenWidth() {
		return m_width;
	}
	
	/**
	 * Returns the prefered screen heigth
	 * @return
	 */
	public int getScreenHeight() {
		return m_height;
	}
	
	/**
	 * Returns the map revision this client has
	 * @return
	 */
	public int getMapRevision() {
		return m_mapRevision;
	}
	
	/**
	 * Set the map revision
	 * @param i
	 */
	public void setMapRevision(int i) {
		m_mapRevision = i;
	}
	
	/**
	 * Loads the users settings.
	 */
	public void loadSettings() {
		try {
			DataInputStream d = new DataInputStream(new FileInputStream("settings.dat"));
			m_width = d.readInt();
			m_height = d.readInt();
			m_mapRevision = d.readInt();
			d.close();
		} catch (Exception e) {
			m_width = 800;
			m_height = 600;
			m_mapRevision = 0;
			saveSettings();
		}
	}
	
	/**
	 * Saves the users settings.
	 */
	public void saveSettings() {
		try {
			DataOutputStream d = new DataOutputStream(new FileOutputStream("temp.dat"));
			d.writeInt(m_width);
			d.writeInt(m_height);
			d.writeInt(m_mapRevision);
			d.close();
			File f = new File("settings.dat");
			f.delete();
			f = new File("temp.dat");
			f.renameTo(new File("settings.dat"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
