/*
 Pokemon Global. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright © 2007-2008 Pokemon Global Team

 This file is part of Pokemon Global.

 Pokemon Global is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Pokemon Global is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Pokemon Global.  If not, see <http://www.gnu.org/licenses/>.
*/

package pokeglobal.client.engine;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import pokeglobal.client.logic.Player;
import pokeglobal.client.logic.Player.Dirs;

// loads tmx maps
public class GameMap extends TiledMap {
	private ArrayList<Player> mapPlayers = new ArrayList<Player>();
	private ArrayList<Player> mapNPCs = new ArrayList<Player>();

	// the map walkable layers
	private int walkableLayer;

	// map offsets
	private int xOffset;
	private int yOffset;

	// map offset modifiers
	private int xOffsetModifier;
	private int yOffsetModifier;

	private Graphics graphics;

	// location of map on overworld
	private GameMapMatrix matrix;
	private int mapX;
	private int mapY;

	public boolean rendering = true;

	private boolean current = false;
	
	// sets map as a current being loaded
	public void setCurrent(boolean b) {
		current = b;
	}
	public void setGraphics(Graphics g) {
		graphics = g;
	}

	public int getXOffset() {
		return xOffset;
	}

	public Layer getLayer(int layer) {
		return (Layer)layers.get(layer);
	}
	
	public Layer getLayer(String layer) {
		int idx = this.getLayerIndex(layer);
		return getLayer(idx);
	}
	
//Checks collision
	public boolean isColliding(Player thisPlayer, Dirs dir) {
		int newX = 0, newY = 0;
		switch (dir) {
		case Up:
			newX = thisPlayer.x;
			newY = thisPlayer.y - 32;
			break;
		case Down:
			newX = thisPlayer.x;
			newY = thisPlayer.y + 32;
			break;
		case Left:
			newX = thisPlayer.x - 32;
			newY = thisPlayer.y;
			break;
		case Right:
			newX = thisPlayer.x + 32;
			newY = thisPlayer.y;
			break;
		}
		int colTileID =
			getLayer("Collisions").getTileID(
					newX / 32, (newY + 8) / 32);
		int watTileID;
		//Not all maps have a water layer
		/*try {
		watTileID =
			getLayer("Water").getTileID(
					newX / 32, (newY + 8) / 32);
		}
		catch (Exception e) {
			watTileID = 0;
		}*/
		//Not all maps have a ledges layer
		int ledTileID = 0;
		try {
			if(thisPlayer.facing != Dirs.Right) {
				ledTileID =
					getLayer("LedgesRight").getTileID(
							newX / 32, (newY + 8) / 32);
			}
			if(ledTileID == 0 && thisPlayer.facing != Dirs.Left) {
				ledTileID =
					getLayer("LedgesLeft").getTileID(
							newX / 32, (newY + 8) / 32);
			}
			if(ledTileID == 0 && thisPlayer.facing != Dirs.Down) {
				ledTileID =
					getLayer("LedgesDown").getTileID(
							newX / 32, (newY + 8) / 32);
			}
		}
		catch(Exception e) {
			ledTileID = 0;
		}
		if (colTileID + ledTileID != 0 
				|| getNPCAt(newX, newY) != null)
			return true;
		return false;
		
//Switches maps for the player		
	}
	public boolean isNewMap(Player thisPlayer, Dirs dir) {
		int newX = 0, newY = 0;
		switch (dir) {
		case Up:
			newX = thisPlayer.x;
			newY = thisPlayer.y - 32;
			break;
		case Down:
			newX = thisPlayer.x;
			newY = thisPlayer.y + 32;
			break;
		case Left:
			newX = thisPlayer.x - 32;
			newY = thisPlayer.y;
			break;
		case Right:
			newX = thisPlayer.x + 32;
			newY = thisPlayer.y;
			break;
		}
		if (newX < 0 || newX >= getWidth() * 32 ||
				newY < -8 || newY + 8 >= getHeight() * 32)
			return true;
		else return false;
	}
	// renders the map for the client
	public boolean isRendering() {
		if (rendering) {
			int drawWidth = getXOffset() + getWidth() * 32;
			int drawHeight = getYOffset() + getHeight() * 32;
			
			if (!(drawWidth < 0 && getXOffset() < 0 ||
					drawWidth > 832 && getXOffset() > 832) &&
					!(drawHeight < 0 && getYOffset() < 0 ||
							drawHeight > 632 && getYOffset() > 632)) {
				return true;
			}
		} return false;
	}
	
//sets offsets for the map	
	public void setPosition(int Xmap, int Ymap, GameMapMatrix mat) {
		mapX = Xmap;
		mapY = Ymap;
		matrix = mat;
	}

	public int getXOffsetModifier() {
		return xOffsetModifier;
	}

	public void setXOffsetModifier(int offsetModifier) {
		xOffsetModifier = offsetModifier;
	}

	public int getYOffsetModifier() {
		return yOffsetModifier;
	}

	public void setYOffsetModifier(int offsetModifier) {
		yOffsetModifier = offsetModifier;
	}

	public void auxiliarySetXOffset(int offset) {
		xOffset = offset;
	}
	public void auxiliarySetYOffset(int offset) {
		yOffset = offset;
	}
	public void setXOffset(int offset) {
		xOffset = offset;

		GameMap map = matrix.getMap(mapX - 1, mapY);
		if (map != null)
			map.auxiliarySetXOffset(offset - map.getWidth() * 32 - getXOffsetModifier()
					+ map.getXOffsetModifier());

		map = matrix.getMap(mapX + 1, mapY);
		if (map != null)
			map.auxiliarySetXOffset(offset + getWidth() * 32 - getXOffsetModifier()
					+ map.getXOffsetModifier());

		map = matrix.getMap(mapX, mapY - 1);
		if (map != null)
			map.auxiliarySetXOffset(offset - getXOffsetModifier()
					+ map.getXOffsetModifier());

		map = matrix.getMap(mapX, mapY + 1);
		if (map != null)
			map.auxiliarySetXOffset(offset - getXOffsetModifier()
					+ map.getXOffsetModifier());
		
		map = matrix.getMap(mapX - 1, mapY - 1);
		if (map != null)
			map.auxiliarySetXOffset(offset - map.getWidth() * 32 - getXOffsetModifier()
					+ map.getXOffsetModifier());
		
		map = matrix.getMap(mapX - 1, mapY + 1);
		if (map != null)
			map.auxiliarySetXOffset(offset - map.getWidth() * 32 - getXOffsetModifier()
					+ map.getXOffsetModifier());
		
		map = matrix.getMap(mapX + 1, mapY + 1);
		if (map != null)
			map.auxiliarySetXOffset(offset + getWidth() * 32 - getXOffsetModifier()
					+ map.getXOffsetModifier());
		
		map = matrix.getMap(mapX + 1, mapY - 1);
		if (map != null)
			map.auxiliarySetXOffset(offset + getWidth() * 32 - getXOffsetModifier()
					+ map.getXOffsetModifier());
	}
	

	public int getYOffset() {
		return yOffset;
	}

	public void setYOffset(int offset) {
		yOffset = offset;
		GameMap map = matrix.getMap(mapX - 1, mapY);
		if (map != null)
			map.auxiliarySetYOffset(offset - getYOffsetModifier()
					+ map.getYOffsetModifier());

		map = matrix.getMap(mapX + 1, mapY);
		if (map != null)
			map.auxiliarySetYOffset(offset - getYOffsetModifier()
					+ map.getYOffsetModifier());

		map = matrix.getMap(mapX, mapY - 1);
		if (map != null)
			map.auxiliarySetYOffset(offset - map.getHeight() * 32);

		map = matrix.getMap(mapX, mapY + 1);
		if (map != null) {
			map.auxiliarySetYOffset(offset + getHeight() * 32);
		}
		
		map = matrix.getMap(mapX + 1, mapY - 1);
		if (map != null)
			map.auxiliarySetYOffset(offset - map.getHeight() * 32
					+ map.getYOffsetModifier());
		
		map = matrix.getMap(mapX - 1, mapY - 1);
		if (map != null)
			map.auxiliarySetYOffset(offset - map.getHeight() * 32);
		
		map = matrix.getMap(mapX + 1, mapY + 1);
		if (map != null)
			map.auxiliarySetYOffset(offset + getHeight() * 32);
		
		map = matrix.getMap(mapX - 1, mapY + 1);
		if (map != null)
			map.auxiliarySetYOffset(offset + getHeight() * 32);
	}

	public void addPlayer(Player p) {
		p.map = this;
		for (int i = 0; i < mapPlayers.size(); i++) {
			if (mapPlayers.get(i).index == p.index)
				mapPlayers.remove(i);
		}
		mapPlayers.add(p);
		matrix.addToPlayers(p);
	}

	public GameMap(String mapPath, String packageName) throws SlickException {
		super(mapPath, packageName);

		xOffsetModifier = Integer.parseInt(getMapProperty("xOffsetModifier",
				"0").trim());
		yOffsetModifier = Integer.parseInt(getMapProperty("yOffsetModifier",
				"0").trim());
		
		System.out.println("XMOD: " + xOffsetModifier + " YMOD: "
				+ yOffsetModifier);

		xOffset = xOffsetModifier;
		yOffset = yOffsetModifier;
		walkableLayer = getLayerCount() - 2;
	}

	@Override
	protected void renderedLine(int visualY, int mapY, int layer) {
		if (current) {
		try {
			graphics.resetTransform();
			if (layer == walkableLayer) {
				synchronized (mapPlayers) {
					for (Player p : mapPlayers) {
						if ((p.y >= mapY * 32 - 39) && (p.y <= mapY * 32 + 32)
								&& (p.curImg != null))
							p.curImg.draw(xOffset + p.x, yOffset + p.y);
					}
				}
				synchronized (mapNPCs) {
					for (Player p : mapNPCs) {
						if ((p.y >= mapY * 32 - 39) && (p.y <= mapY * 32 + 32)
								&& (p.curImg != null))
							p.curImg.draw(xOffset + p.x, yOffset + p.y);
					}
				}
			}
			if (layer == walkableLayer) {
				for (Player p : mapPlayers) {
					if ((p.y >= mapY * 32 - 39) && (p.y <= mapY * 32 + 32)
							&& (p.curImg != null))
						graphics.drawString(p.username, xOffset + (p.x
								- (graphics.getFont().getWidth(p.username) / 2)) + 14, yOffset + p.y
								- 36);
				}
			}
			graphics.scale(2, 2);
		} catch (ConcurrentModificationException e) {

		}
		}
	}

	// draws NPCs
	public Player getNPCAt(int x, int y) {
		for (Player p : mapNPCs) {
			if (p.x == x && p.y == y)
				return p;
		}
		return null;
	}

	// creates players
	public ArrayList<Player> getMapPlayers() {
		return mapPlayers;
	}

	// creates NPCs
	public ArrayList<Player> getMapNPCs() {
		return mapNPCs;
	}

	// creates walkable layer
	public int getWalkableLayer() {
		return walkableLayer;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public GameMapMatrix getMatrix() {
		return matrix;
	}

	public int getMapX() {
		return mapX;
	}

	public int getMapY() {
		return mapY;
	}
	
	public void recalibrate() {
		xOffset = xOffsetModifier;
		yOffset = yOffsetModifier;
	}
	public void wipe() {
		mapPlayers.clear();
		mapPlayers.trimToSize();
		mapNPCs.clear();
		mapNPCs.trimToSize();
	}
}
