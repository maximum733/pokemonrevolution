package polr.server.mechanics;

import java.util.ArrayList;

import polr.server.ClientHandler;

/**
 * Handles chat
 * @author shinobi
 *
 */
public class ChatController implements Runnable {
	private ArrayList<String> m_chatqueue;
	
	public ChatController() {
		m_chatqueue = new ArrayList<String>();
	}

	public void run() {
		while(true) {
			if(m_chatqueue.get(0) != null) {
				
			}
			try {
				Thread.sleep(200);
			} catch(Exception e) {}
		}
	}
}
