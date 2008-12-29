package polr.server.player;

import org.simpleframework.xml.Element;

/**
 * Stores the class of Players and that classes information
 */
public class PlayerClass {
	public enum ClassType { NONE, TRAINER, RESEARCHER, THIEF, BREEDER, HUNTER }
	
	@Element
	private double m_exp;
	@Element
	private ClassType m_type;
	
	/** Constructor for serialization */
	public PlayerClass() {}
	
	public PlayerClass(ClassType c) {
		m_type = c;
		m_exp = 0;
	}
	
	public void setExp(double exp) {
		m_exp = exp;
	}
	
	public void setClass(ClassType c) {
		m_type = c;
	}
	
	public double getExp() {
		return m_exp;
	}
	
	public int getLevel() {
		return (int) (m_exp / 100);
	}
	
	public String getClassName() {
		return m_type.name();
	}
	
	public static double getRareRate(ClassType c) {
		switch(c) {
		case TRAINER:
			return 1.0;
		case RESEARCHER:
			return 0.8;
		case THIEF:
			return 0.8;
		case BREEDER:
			return 1.0;
		case HUNTER:
			return 1.2;
		default:
			return 1.0;
		}
	}
	
	public static double getLevelRate(ClassType c) {
		switch(c) {
		case TRAINER:
			return 1.1;
		case RESEARCHER:
			return 1.0;
		case THIEF:
			return 0.8;
		case BREEDER:
			return 0.9;
		case HUNTER:
			return 1.0;
		default:
			return 1.0;
		}
	}
	
	public static int getBoxAmount(ClassType c) {
		switch(c) {
		case TRAINER:
			return 3;
		case RESEARCHER:
			return 4;
		case THIEF:
			return 4;
		case BREEDER:
			return 4;
		case HUNTER:
			return 5;
		default:
			return 4;
		}
	}
	
	public static boolean hasBadgeRestriction(ClassType c) {
		return c != ClassType.RESEARCHER;
	}
}
