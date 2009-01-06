package polr.client.ui.chat;

import polr.client.GameClient;

public class PrivateChat extends ChatBase {

	public PrivateChat(String c, String m) {
		super();
		this.m_target = c;
		this.setName(c);
		this.addMessage(m);
		this.setTitle("Private: " + c);
	}
	
	public void addMessage(String s) {
		if (!m_conversation.getText().equals(""))
			m_conversation.setText(m_conversation.getText() + "\n" + s);
		else
			m_conversation.setText(s);
	}
	
	@Override
	public void sendMessage(String s) {
		GameClient.getPacketGenerator().write("cl" + s);
	}
}
