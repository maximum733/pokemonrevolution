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
		this.setSize(240, 320);
		this.setResizable(false);
		this.setBackground(new Color(0, 0, 0, 70));
		this.setForeground(new Color(255, 255, 255));
		m_conversation = new TextArea();
		m_conversation.setSize(280, 256);
		m_conversation.setBackground(new Color(0, 0, 0, 20));
		m_conversation.setForeground(new Color(255, 255, 255));
		m_conversation.setBorderRendered(false);
		m_conversation.setEditable(false);
		m_conversation.setFont(GameClient.getDPFontSmall());
		this.getContentPane().add(m_conversation);
		
		m_speech = new TextField();
		m_speech.setName("chatType");
		m_speech.setSize(280, 25);
		m_speech.setLocation(0, 268);
		m_speech.setForeground(new Color(255, 255, 255));
		m_speech.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chatTypeActionPerformed(evt);
			}
		});
		this.getContentPane().add(m_speech);
		this.setLocation(800 - this.getWidth(), 600 - this.getHeight());
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
	
	public boolean isChatting() {
		return m_speech.hasFocus();
	}
	
	public void sendMessage(String s) {}
	
	public void checkChatWindow() {
		try {
			if(m_conversation.getLineCount() >= ( (int)getHeight() - 48) / GameClient.getDPFontSmall().getLineHeight()) {
				String [] s = m_conversation.getLinesAsText();
				m_conversation.setCaretPosition(0);
				m_conversation.setText("");
				//this next line causes the window to be unresizable
				//we need a way to calculate the max possible amount of lines in resizeable window can fit
				int adj = (int) ((s.length - (getHeight() - getTitleBar().getHeight() - m_speech.getHeight())/ GameClient.getDPFontSmall().getLineHeight()) + 1);
				for(int i = adj; i < s.length; i++) {
					m_conversation.setText(m_conversation.getText() + s[i]);
					m_conversation.setCaretPosition(m_conversation.getText().length());
				}
			}}
		catch ( Exception e) { 
			e.printStackTrace();
		}
		
	}
}
