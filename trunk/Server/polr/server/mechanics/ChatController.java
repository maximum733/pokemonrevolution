package polr.server.mechanics;

import java.util.ArrayList;

import polr.server.ClientHandler;

/**
 * Handles chat
 * @author shinobi
 *
 */
public class ChatController implements Runnable {
	private ArrayList<String []> m_chatqueue;
	
	public ChatController() {
		m_chatqueue = new ArrayList<String []>();
	}

	public void run() {
		while(true) {
			if(m_chatqueue.get(0) != null) {
				try {
					switch(m_chatqueue.get(0)[1].charAt(0)) {
					case 'l':
						ClientHandler.getPlayerList().get(m_chatqueue.get(0)[0]).getMap().sendToAll("cl<" + m_chatqueue.get(0)[0] + "> " + m_chatqueue.get(0)[2]);
						break;
					default:
						ClientHandler.getPlayerList().get(m_chatqueue.get(0)[1]).getIoSession().write("cp" + m_chatqueue.get(0)[0] + "," + m_chatqueue.get(0)[2]);
					}
				} catch (Exception e) {}
			}
			try {
				Thread.sleep(50);
			} catch(Exception e) {}
		}
	}
	
	public void addMessage(String sentFrom, String sendTo, String message) {
		m_chatqueue.add(new String[] {sentFrom, sendTo, message});
	}
}
