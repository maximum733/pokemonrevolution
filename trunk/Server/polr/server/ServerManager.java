package polr.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import polr.server.map.MapUpdater;

public class ServerManager extends JFrame {
	private static final long serialVersionUID = 1L;
	private GameServer m_server;
	private JButton m_start, m_stop, m_updateMaps, m_exit;
	private MapUpdater m_updater;
	private ServerManager m_default;
	
	public ServerManager() {
		super("POLR Server - Beta 1");
		this.setSize(160, 160);
		this.setLayout(null);
		
		m_start = new JButton("Start Server");
		m_start.setSize(128, 24);
		m_start.setLocation(4, 4);
		m_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_start.setEnabled(false);
				m_updateMaps.setEnabled(false);
				m_stop.setEnabled(true);
				m_server = new GameServer();
				m_server.start();
			}
		});
		this.add(m_start);
		
		m_stop = new JButton("Stop Server");
		m_stop.setSize(128, 24);
		m_stop.setLocation(4, 32);
		m_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_server.stop()) {
					enable();
					m_server = null;
					System.out.println("Server stopped");
				}
			}
		});
		this.add(m_stop);
		
		m_updateMaps = new JButton("Update Maps");
		m_updateMaps.setSize(128, 24);
		m_updateMaps.setLocation(4, 62);
		m_updateMaps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_updater = new MapUpdater(m_default);
				m_updater.checkSVN();
			}
		});
		this.add(m_updateMaps);
		
		m_exit = new JButton("Exit");
		m_exit.setSize(128, 24);
		m_exit.setLocation(4, 90);
		m_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_server == null)
					System.exit(0);
				else
					JOptionPane.showMessageDialog(null,"You need to shutdown the server before exiting");
			}
		});
		this.add(m_exit);
		
		m_default = this;
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public void enable() {
		m_start.setEnabled(true);
		m_updateMaps.setEnabled(true);
		m_stop.setEnabled(false);
	}
	
	/**
	 * Runs a server with gui management.
	 * @param args
	 */
	public static void main(String [] args) {
		ServerManager sm = new ServerManager();
	}
}
