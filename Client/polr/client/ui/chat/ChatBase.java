package polr.client.ui.chat;

import org.newdawn.slick.Color;

import polr.client.GameClient;
import polr.client.ui.base.Frame;
import polr.client.ui.base.TextArea;
import polr.client.ui.base.TextField;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

public class ChatBase extends Frame {
	protected String m_target;
	protected TextArea m_conversation;
	protected TextField m_speech;
	
	public ChatBase() {
		this.setSize(320, 320);
		m_conversation = new TextArea();
		m_conversation.setSize(320, 256);
		m_conversation.setBackground(new Color(0, 0, 0, 20));
		m_conversation.setForeground(new Color(255, 255, 255));
		m_conversation.setBorderRendered(false);
		m_conversation.setEditable(false);
		m_conversation.setFont(GameClient.getDPFontSmall());
		this.getContentPane().add(m_conversation);
		
		m_speech = new TextField();
		m_speech.setName("chatType");
		m_speech.setSize(350, 25);
		m_speech.setLocation(50, 250);
		m_speech.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chatTypeActionPerformed(evt);
			}
		});
		this.getContentPane().add(m_speech);
		
		this.setVisible(true);
	}
	
	public String getTarget() {
		return m_target;
	}
	
	public void setTarget(String t) {
		m_target = t;
	}
	
	private void chatTypeActionPerformed(ActionEvent evt) {
		if (m_speech.getText() != null && m_speech.getText().length() != 0) {
			sendMessage(m_speech.getText());
		}
		m_speech.setText("");
		m_speech.grabFocus();
	}
	
	public void sendMessage(String s) {}
}
