package polr.client.ui.chat;

import polr.client.GameClient;
import polr.client.ui.base.Frame;

public class ChatBase extends Frame {
	private String m_target;
	
	public ChatBase() {
		this.setSize(240, 240);
	}
	
	public String getTarget() {
		return m_target;
	}
	
	public void setTarget(String t) {
		m_target = t;
	}
	
	public void sendMessage(String s) {
		GameClient.getPacketGenerator().sendChatMessage(m_target + s);
	}
}
