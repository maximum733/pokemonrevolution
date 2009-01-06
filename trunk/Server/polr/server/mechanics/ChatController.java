package polr.server.mechanics;

import java.util.ArrayList;

import polr.server.ClientHandler;
import polr.server.player.PlayerChar;

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
			if(m_chatqueue.size() > 0 && m_chatqueue.get(0) != null) {
				try {
					switch(m_chatqueue.get(0)[1].charAt(0)) {
					case 'l':
						ClientHandler.getPlayerList().get(m_chatqueue.get(0)[0]).getMap().sendToAll("cl<" + m_chatqueue.get(0)[0] + "> " + m_chatqueue.get(0)[2]);
						break;
					default:
						PlayerChar receiver = ClientHandler.getPlayerList().get(m_chatqueue.get(0)[1]);
						PlayerChar sender = ClientHandler.getPlayerList().get(m_chatqueue.get(0)[0]);
						if(receiver != null && sender != null) {
							receiver.getIoSession().write("cp" + sender.getName() + ",<" + sender.getName() + "> "+ m_chatqueue.get(0)[2]);
							sender.getIoSession().write("cp" + receiver.getName() + ",<" + sender.getName() + "> "+ m_chatqueue.get(0)[2]);
						}
					}
					m_chatqueue.remove(0);
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
