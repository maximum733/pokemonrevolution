package polr.client.engine;

public class Environment {
	private int m_daylight;
	private int m_targetDaylight;
	
	public void setDaylight(int time) {
		m_daylight = time;
	}
	
	public int getDaylight() {
		return m_daylight;
	}
	
	public int getTargetDaylight() {
		return m_targetDaylight;
	}
	
	public void setTargetDaylight(int target) {
		m_targetDaylight = target;
	}

}
