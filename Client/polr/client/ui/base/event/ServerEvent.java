package polr.client.ui.base.event;

import polr.client.GameClient;

public class ServerEvent implements ActionListener {
	private String m_host;
	private int m_port;
	
	public ServerEvent(String host, int port) {
		m_host = host;
		m_port = host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1") ? 2401 : port;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GameClient.setPort(m_port);
		GameClient.setServer(m_host);
	}

}
