package polr.client.ui.chat;

import polr.client.GameClient;

public class LocalChat extends ChatBase {
	public LocalChat() {
		super();
		this.setTitle("Local Chat");
		this.setName("Local Chat");
	}
	
	public void addMessage(String s) {
		if (!m_conversation.getText().equals(""))
			m_conversation.setText(m_conversation.getText() + "\n" + s);
		else
			m_conversation.setText(s);
		this.checkChatWindow();
	}
	
	@Override
	public void sendMessage(String s) {
		GameClient.getPacketGenerator().write("cl" + s);
	}
}
