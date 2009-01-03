package polr.server.mechanics;

import java.util.Iterator;

import polr.server.ClientHandler;
import polr.server.player.PlayerChar;

public class MovementController implements Runnable {
	
	public void run() {
		int i = 0;
		Iterator it;
		PlayerChar p;
		while(true) {
			it = ClientHandler.getPlayerList().values().iterator();
			while(it.hasNext()) {
				p = (PlayerChar) it.next();
				p.move();
			}
			i = 0;
			try {
				Thread.sleep(250);
			} catch (Exception e) {}
		}
	}

}
