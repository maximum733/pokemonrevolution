package polr.server.mechanics;

import java.util.Iterator;

import polr.server.ClientHandler;
import polr.server.player.PlayerChar;

public class MovementController implements Runnable {
	
	public void run() {
		Iterator it;
		PlayerChar p;
		while(true) {
			it = ClientHandler.getPlayerList().values().iterator();
			while(it.hasNext()) {
				try {
					p = (PlayerChar) it.next();
					p.move();
				} catch (Exception e) {}
			}
			try {
				Thread.sleep(250);
			} catch (Exception e) {}
		}
	}

}
