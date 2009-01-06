package polr.client.ui.base;

import polr.client.GameClient;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

public class FriendSelector extends Container {
	private Label m_name;
	private Button m_chat, m_delete;
	
	public FriendSelector(String username) {
		super();
		this.setSize(240, 32);
		
		m_name = new Label(username);
		m_name.pack();
		m_name.setLocation(8, 8);
		this.add(m_name);
		
		m_chat = new Button("Chat");
		m_chat.setSize(48, 24);
		m_chat.setLocation(132, 4);
		m_chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startChat();
			}
		});
		this.add(m_chat);
		
		m_delete = new Button("Delete");
		m_delete.setSize(48, 24);
		m_delete.setLocation(132 + 48, 4);
		m_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_name.getText() != null && !m_name.getText().equalsIgnoreCase(""))
					GameClient.getPacketGenerator().write("fr" + m_name.getText());
			}
		});
		this.add(m_delete);
	}
	
	public void startChat() {
		if(m_name.getText() != null && !m_name.getText().equalsIgnoreCase(""))
			GameClient.getPacketGenerator().write("cp" + m_name.getText() + ",(Opened private chat)");
	}
	
	public String getFriendUsername() {
		return m_name.getText();
	}
}
