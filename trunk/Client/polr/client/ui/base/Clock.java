package polr.client.ui.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import polr.client.GameClient;

public class Clock extends Label implements Runnable {
	private int m_hour;
	private int m_minutes;
	
	public Clock() {
		super("00:00");
		this.setFont(GameClient.getDPFontSmall());
	}

	public void run() {
		String min;
		String hour;
		while(true) {
			m_minutes = m_minutes == 59 ? 0 : m_minutes + 1;
			if(m_minutes == 0)
				m_hour = m_hour == 23 ? 0 : m_hour + 1;
			hour = m_hour < 10 ? "0" + String.valueOf(m_hour) : String.valueOf(m_hour);
			min = m_minutes < 10 ? "0" + String.valueOf(m_minutes) : String.valueOf(m_minutes);
			this.setText(hour + ":" + min);
			this.pack();
			try {
				Thread.sleep(15000);
			} catch (Exception e) {}
		}
	}
	
	public void setTime(int hour, int minutes) {
		m_hour = hour;
		m_minutes = minutes;
	}
}
