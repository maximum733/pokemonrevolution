package polr.client.ui.chat;

import java.util.ArrayList;

import polr.client.GameClient;
import polr.client.ui.base.Button;
import polr.client.ui.base.Frame;
import polr.client.ui.base.FriendSelector;
import polr.client.ui.base.TextField;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

public class FriendList extends Frame {
	private ArrayList<FriendSelector> m_selector;
	private TextField m_username;
	private Button m_add;
	
	public FriendList(ArrayList<String> m_friends) {
		this.setName("Friends List");
		this.setTitle("Friends List");
		this.setSize(236, 372);
		this.setResizable(false);
		m_selector = new ArrayList<FriendSelector>();
		for(int i = 0; i < m_friends.size(); i++) {
			m_selector.add(new FriendSelector(m_friends.get(i)));
		}
		m_username = new TextField();
		m_username.setSize(160, 24);
		m_username.setLocation(4, 320);
		
		m_add = new Button("Add");
		m_add.setSize(48, 24);
		m_add.setLocation(m_username.getWidth() + 16, 320);
		m_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_username.getText() != null && !m_username.getText().equalsIgnoreCase(""))
					GameClient.getPacketGenerator().write("fa" + m_username.getText());
			}
		});
		
		this.reload();
		this.setLocation(48, 0);
	}
	
	public void reload() {
		this.getContentPane().removeAll();
		for(int i = 0; i < m_selector.size(); i++) {
			m_selector.get(i).setLocation(0, 32 * i);
			this.getContentPane().add(m_selector.get(i));
		}
		this.getContentPane().add(m_username);
		this.getContentPane().add(m_add);
	}

	public void addFriend(String friend) {
		if(m_selector.size() < 10)
			m_selector.add(new FriendSelector(friend));
		reload();
	}

	public void removeFriend(String friend) {
		for(int i = 0; i < m_selector.size(); i++) {
			if(m_selector.get(i).getFriendUsername().equalsIgnoreCase(friend)) {
				m_selector.remove(i);
				reload();
				return;
			}
		}
	}
	
}