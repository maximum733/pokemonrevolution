package polr.client.ui.chat;

import polr.client.GameClient;

public class LocalChat extends ChatBase {
	public LocalChat() {
		super();
	}
	
	public void addMessage(String s) {
		if (!m_conversation.getText().equals(""))
			m_conversation.setText(m_conversation.getText() + "\n" + s);
		else
			m_conversation.setText(s);
	}
	
	@Override
	public void sendMessage(String s) {
		GameClient.getPacketGenerator().write("cl" + m_target + "," + s);
	}
}
