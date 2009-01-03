package polr.server.time;

import polr.server.GameServer;
import polr.server.mechanics.statuses.field.FieldEffect;
import polr.server.mechanics.statuses.field.HailEffect;
import polr.server.mechanics.statuses.field.RainEffect;
import polr.server.mechanics.statuses.field.SunEffect;

/**
 * Handles weather and time
 * 
 * @author TMKCodes
 * @author shinobi
 *
 */
public class WorldClock implements Runnable {
	private long m_lastWeatherUpdate;
	private long m_lastDaylightUpdate;
	private boolean m_isNight;
	private Weather m_weather;
	private LegendaryGenerator m_legendary;
	
	public enum Weather { NORMAL, RAIN, HAIL, SUN }
	
	/**
	 * Default constructor
	 */
	public WorldClock() {
		this.generateWeather();
		m_lastWeatherUpdate = System.currentTimeMillis();
		m_isNight = false;
		m_lastDaylightUpdate = System.currentTimeMillis();
		m_legendary = new LegendaryGenerator();
	}
	
	/**
	 * Generate new weather
	 */
	private void generateWeather() {
		switch(GameServer.getMechanics().getRandom().nextInt(4)) {
		case 0:
			m_weather = Weather.SUN;
			break;
		case 1:
			m_weather = Weather.NORMAL;
			break;
		case 2:
			m_weather = Weather.HAIL;
			break;
		case 3:
			m_weather = Weather.RAIN;
			break;
		default:
			m_weather = Weather.NORMAL;
		}
	}
	
	/**
	 * Return the effect the current weather has on a battle
	 * @return
	 */
	public FieldEffect getWeatherEffect() {
		switch(m_weather) {
		case NORMAL:
			return null;
		case RAIN:
			return new RainEffect();
		case HAIL:
			return new HailEffect();
		case SUN:
			return new SunEffect();
		default:
			return null;
		}
	}
	
	/**
	 * The clock itself. NOTE: Only one thread should exist.
	 */
	public void run() {
		while(true) {
			//Check if weather should be updated
			if(System.currentTimeMillis() - m_lastWeatherUpdate >= 10800000) {
				generateWeather();
				m_lastWeatherUpdate = System.currentTimeMillis();
				if(!m_legendary.isLegendaryAppeared()) {
					if(GameServer.getMechanics().getRandom().nextDouble() == 0.1) {
						//A legendary has appeared!
						m_legendary.generateAppearance();
					}
				} else {
					//A legendary had already appeared, time to get rid of it!
					m_legendary.killAppearance();
				}
			}
			//Check if day/night should be updated
			if(System.currentTimeMillis() - m_lastDaylightUpdate >= 21600000) {
				m_isNight = !m_isNight;
				m_lastDaylightUpdate = System.currentTimeMillis();
			}
			try {
				Thread.sleep(900000);
			} catch (Exception e) {}
		}
	}

}
