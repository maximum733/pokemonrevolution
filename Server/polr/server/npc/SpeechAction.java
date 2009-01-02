package polr.server.npc;

import polr.server.player.PlayerChar;

public class SpeechAction implements NpcAction {
	private String m_speech;
	
	public SpeechAction(String speech) {
		m_speech = speech;
	}
	
	public void execute(PlayerChar p) {
		p.getIoSession().write("T" + m_speech);
	}

}
