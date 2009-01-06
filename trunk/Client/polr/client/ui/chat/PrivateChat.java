package polr.client.ui.chat;

import polr.client.GameClient;

public class PrivateChat extends ChatBase {

	public PrivateChat(String c, String m) {
		super();
		this.m_target = c;
		this.setName(c);
		this.addMessage(m);
		this.setTitle("Private: " + c);
		this.setVisible(true);
		this.setLocation(160, 160);
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
		GameClient.getPacketGenerator().write("cp" + m_target + "," + s);
	}
}
