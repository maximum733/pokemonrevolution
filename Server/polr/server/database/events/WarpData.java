package polr.server.database.events;

import org.simpleframework.xml.Element;

/**
 * Stores warptile data for xml saving/loading.
 * @author shinobi
 *
 */
public class WarpData {
	@Element
	private int m_x;
	@Element
	private int m_y;
	@Element
	private int m_warpMapX;
	@Element
	private int m_warpMapY;
	@Element
	private int m_warpX;
	@Element
	private int m_warpY;
	@Element
	private int m_reqItem = 0;
	@Element
	private int m_reqQuest = 0;
	@Element
	private int m_reqBadge = 0;
	
	/**
	 * Returns required badge amount
	 * @return
	 */
	public int getRequiredBadge() {
		return m_reqBadge;
	}
	
	/**
	 * Set required badge amount
	 * @param i
	 */
	public void setRequiredBadge(int i) {
		m_reqBadge = i;
	}
	
	/**
	 * Returns required quest
	 * @return
	 */
	public int getRequiredQuest() {
		return m_reqQuest;
	}
	
	/**
	 * Set the required quest
	 * @param i
	 */
	public void setRequiredQuest(int i) {
		m_reqQuest = i;
	}
	
	/**
	 * Returns required item
	 * @return
	 */
	public int getRequiredItem() {
		return m_reqItem;
	}
	
	/**
	 * Sets the required item
	 * @param i
	 */
	public void setRequiredItem(int i) {
		m_reqItem = i;
	}
	
	/**
	 * Set the x co-ordinate of this tile on the map
	 * @param x
	 */
	public void setX(int x) {
		m_x = x;
	}
	
	/**
	 * Set the y co-ordinate of this tile on the map
	 * @param y
	 */
	public void setY(int y) {
		m_y = y;
	}
	
	/**
	 * Set the x co-ordinate of the map this tile warps to
	 * @param x
	 */
	public void setWarpMapX(int x) {
		m_warpMapX = x;
	}
	
	/**
	 * Set the y co-ordinate of the map this tile warps to
	 * @param y
	 */
	public void setWarpMapY(int y) {
		m_warpMapY = y;
	}
	
	/**
	 * Set the x co-ordinate of where this tile warps to
	 * @param x
	 */
	public void setWarpX(int x) {
		m_warpX = x;
	}
	
	/**
	 * Set the y co-ordinate of where this tile warps to
	 * @param y
	 */
	public void setWarpY(int y) {
		m_warpY = y;
	}
	
	/**
	 * Returns the x co-ordinate of this tile.
	 * @return
	 */
	public int getX() {
		return m_x;
	}
	
	/**
	 * Returns the y co-ordinate of this tile.
	 * @return
	 */
	public int getY() {
		return m_y;
	}
	
	/**
	 * Returns the x of the map this tile warps to
	 * @return
	 */
	public int getWarpToMapX() {
		return m_warpMapX;
	}
	
	/**
	 * Returns the y of the map this tile warps to
	 * @return
	 */
	public int getWarpToMapY() {
		return m_warpMapY;
	}
	
	/**
	 * Returns the x co-ordinate of where this tile warps to
	 * @return
	 */
	public int getWarpX() {
		return m_warpX;
	}
	
	/**
	 * Returns the y co-ordinate of where this tile warps to
	 * @return
	 */
	public int getWarpY() {
		return m_warpY;
	}
}
