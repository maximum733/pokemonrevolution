/*
 Pokemon Global. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright � 2007-2008 Pokemon Global Team

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

package pokeglobal.client.ui.speech;

import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.loading.LoadingList;

import pokeglobal.client.GlobalGame;
import pokeglobal.client.ui.base.Frame;
import pokeglobal.client.ui.base.Label;
import pokeglobal.client.ui.base.TextArea;
public class SpeechPopup extends Frame {
	
	//initiating variables
	Queue<String> speechQueue;
	TextArea speechDisplay;
	protected String stringToPrint;
	
	//old timer function
	//Timer printingTimer = new Timer();
	//TimerTask animAction;

	Polygon triangle;
	Image bg;
	
	boolean isGoingDown = true;
	
	
	
	//constructors
	public SpeechPopup() {
		speechQueue = new LinkedList<String>();
		initGUI();
	}
	public SpeechPopup(String speech) {
		speechQueue = new LinkedList<String>();
		for (String line : speech.split("/n"))
			speechQueue.add(line);
		initGUI();
	}


	//initiating GUI
	public void initGUI() {
		LoadingList.setDeferredLoading(true);
		try {
			bg = new Image("res/ui/hud/textbackground.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoadingList.setDeferredLoading(false);
		Label bg = new Label(this.bg);
		bg.setSize(400, 100);
		bg.setLocation(0, -11);
		speechDisplay = new TextArea();

		speechDisplay.setFocusable(false);
		speechDisplay.setSize(384, 100);
		speechDisplay.setLocation(16, 5);
		speechDisplay.setBorderRendered(false);
		speechDisplay.setFont(GlobalGame.getDPFont());
		speechDisplay.setOpaque(false);
		this.getContentPane().add(bg);
		this.getContentPane().add(speechDisplay);
		
		this.setWidth(400);
		this.setHeight(100);
		this.setX((GlobalGame.width / 2) - getWidth() / 2);
		this.setY((GlobalGame.height / 2) + getWidth() / 2);
		this.getTitleBar().setVisible(false);
		this.setResizable(false);
		
		this.setFocusable(false);
		
		this.setAlwaysOnTop(true);
		try {
			advance();
		}
		catch (Exception e) {
		}
	}

	public void advance() throws Exception {
		triangle = null;
		stringToPrint = speechQueue.poll();
		if (stringToPrint != null){
            speechDisplay.setText(stringToPrint);
        }
		else throw new Exception();
	}
	
	/*//commented out because it's too confusing
	public void advance() {

		triangle = null;

		if (animAction == null) {
			if (canAdvance()) {
				speechDisplay.setText("");
				if (stringToPrint != null) advancedPast(stringToPrint);
				stringToPrint = speechQueue.poll();
				
				if (stringToPrint != null) {
					animAction = new TimerTask() {
						public void run() {
							if (speechDisplay.getText().equals(stringToPrint)) {
								animAction = null;
								try {
									cancel();
								} catch (IllegalStateException e) { }
								triangulate();
							} else {
								try {
									speechDisplay.setText(stringToPrint.substring(0, speechDisplay.getText().length() + 1));
								} catch (StringIndexOutOfBoundsException e) {
									speechDisplay.setText(stringToPrint);
								}
							}
						}};
						try {
							printingTimer.schedule(animAction, 0, 30);}
						catch (Exception e) { 
							animAction = null;
							e.printStackTrace();
						}
						
						advancing(stringToPrint);
				}
			}
		} else {
			speechDisplay.setText("");
			animAction.cancel();
			animAction = null;
			speechDisplay.setText(stringToPrint);
			triangulate();
		}
	}
	*/
	
	//advancing void methods
	public void advancing(String toPrint) {

	}
	public void advancedPast(String printed) {
		
	}
	public void advanced(String done) {

	}
	
	//old method, never used
	public boolean canAdvance() {
		return true;
	}
	
	//setting the triangle
	public void triangulate() {
		triangle = new Polygon();
		
		triangle.addPoint(getWidth() - 30 + getX(), 60 + getY());
		triangle.addPoint(getWidth() - 30 + getX() + 10, 60 + getY());
		triangle.addPoint(getWidth() - 30 + getX() + 5, 60 + getY() + 10);
	}

	//rendering the triangle
	@Override
	public void render(GUIContext container, Graphics g) {
		super.render(container, g);

		if (triangle != null) {
			//if (canAdvance()) {
				
				g.setColor(Color.red);
				g.fill(triangle);
				
				if (Math.round(triangle.getCenterY()) > 584) triangle.setCenterY(584);
				else if (Math.round(triangle.getCenterY()) < 574) triangle.setCenterY(574);
				
				if (Math.round(triangle.getCenterY()) == 574) isGoingDown = true;
				else if (Math.round(triangle.getCenterY()) == 584) isGoingDown = false;
					
				if (isGoingDown) triangle.setCenterY(triangle.getCenterY() + .5f);
				else triangle.setCenterY(triangle.getCenterY() - .5f);
			//}
		}
	}
	
	//adding speech to be displayed to the queue
	public void addSpeech(String speech) {
		//triangulate();
		for(String line : speech.split("\n")) speechQueue.add(line);
	}

}
