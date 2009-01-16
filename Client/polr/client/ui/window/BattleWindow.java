package polr.client.ui.window;

import polr.client.ui.base.Frame;

public class BattleWindow extends Frame {
	private boolean m_isWild;
	private String m_enemyName;

	public BattleWindow(boolean isWild, String enemyName) {
		m_isWild = isWild;
		m_enemyName = enemyName;
		
		this.setSize(800, 600);
	}
}
