package polr.client.ui.base;

import polr.client.GameClient;

/**
 * A simple digital clock
 * @author tom
 *
 */
public class Clock extends Label implements Runnable {
	private int m_hour;
	private int m_minutes;
	
	/**
	 * Default constructor
	 */
	public Clock() {
		super("00:00");
		this.setFont(GameClient.getDPFontSmall());
	}
	
	/**
	 * Called when the clock is started
	 */
	public void run() {
		String min;
		String hour;
		while(true) {
			m_minutes = m_minutes == 59 ? 0 : m_minutes + 1;
			if(m_minutes == 0) {
				m_hour = m_hour == 23 ? 0 : m_hour + 1;
				switch(m_hour) {
				case 3:
					GameClient.getEnvironment().setTargetDaylight(150);
					break;
				case 4:
					GameClient.getEnvironment().setTargetDaylight(125);
					break;
				case 5:
					GameClient.getEnvironment().setTargetDaylight(100);
					break;
				case 6:
					GameClient.getEnvironment().setTargetDaylight(75);
					break;
				case 7:
					GameClient.getEnvironment().setTargetDaylight(0);
					break;
				case 17:
					GameClient.getEnvironment().setTargetDaylight(75);
					break;
				case 18:
					GameClient.getEnvironment().setTargetDaylight(100);
					break;
				case 19:
					GameClient.getEnvironment().setTargetDaylight(125);
					break;
				case 20:
					GameClient.getEnvironment().setTargetDaylight(150);
					break;
				case 21:
					GameClient.getEnvironment().setTargetDaylight(175);
					break;
				default:
					break;
				}
			}
			hour = m_hour < 10 ? "0" + String.valueOf(m_hour) : String.valueOf(m_hour);
			min = m_minutes < 10 ? "0" + String.valueOf(m_minutes) : String.valueOf(m_minutes);
			this.setText(hour + ":" + min);
			this.pack();
			try {
				Thread.sleep(15000);
			} catch (Exception e) {}
		}
	}
	
	/**
	 * Set the time
	 * @param hour
	 * @param minutes
	 */
	public void setTime(int hour, int minutes) {
		m_hour = hour;
		m_minutes = minutes;
		//Set the environmental time
		if(hour == 17) {
			GameClient.getEnvironment().setDaylight(75);
			GameClient.getEnvironment().setTargetDaylight(75);
		} else if(hour == 18) {
			GameClient.getEnvironment().setDaylight(100);
			GameClient.getEnvironment().setTargetDaylight(100);
		} else if(hour == 19) {
			GameClient.getEnvironment().setDaylight(125);
			GameClient.getEnvironment().setTargetDaylight(125);
		} else if(hour == 20) {
			GameClient.getEnvironment().setDaylight(150);
			GameClient.getEnvironment().setTargetDaylight(150);
		} else if(hour == 3) {
			GameClient.getEnvironment().setDaylight(150);
			GameClient.getEnvironment().setTargetDaylight(150);
		} else if(hour == 4) {
			GameClient.getEnvironment().setDaylight(125);
			GameClient.getEnvironment().setTargetDaylight(125);
		} else if(hour == 5) {
			GameClient.getEnvironment().setDaylight(100);
			GameClient.getEnvironment().setTargetDaylight(100);
		} else if(hour == 6) {
			GameClient.getEnvironment().setDaylight(75);
			GameClient.getEnvironment().setTargetDaylight(75);
		} else if(hour >= 8 && hour <= 17) {
			GameClient.getEnvironment().setDaylight(0);
			GameClient.getEnvironment().setTargetDaylight(0);
		} else {
			GameClient.getEnvironment().setDaylight(175);
			GameClient.getEnvironment().setTargetDaylight(175);
		}
	}
}
