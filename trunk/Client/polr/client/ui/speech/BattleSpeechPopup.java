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

package polr.client.ui.speech;

import polr.client.ui.window.Battle;

public class BattleSpeechPopup extends SpeechPopup {
	
	//variables
	private Battle m_battle;
	
	//constructor
	BattleSpeechPopup() {
		super();
	}
	public BattleSpeechPopup(Battle battleWindow) {
		super("");
		m_battle = battleWindow;
	}
	
	//other methods
	public void addSpeech(String speech) {
		if (stringToPrint != null && (stringToPrint.equals("Awaiting your move.") || stringToPrint.equals("Awaiting players' moves."))
				&& speechQueue.peek() == null)
			triangulate();
		speechQueue.add(speech);
		if (stringToPrint == null || stringToPrint.equals(""))
			try {
			advance();
			}
			catch (Exception e) {
				setVisible(false);
			}
	}
	
	public void advancedPast(String printed) {
		if (speechQueue.peek() == null) {
			triangle = null;
			setVisible(false);
		}
	}

	//override method all commented out because incompatibility with Battle class
	/*
	@Override
	public void advancing(String toPrint) {
		if (toPrint.equals("Awaiting your move.") || toPrint.equals("Awaiting players' moves."))
			m_battle.setUIToMove();
		else if (toPrint.contains(" wants to learn ")) {
			int indexOfWants = toPrint.indexOf(" wants to learn ");
			String afterWants = toPrint.substring(indexOfWants
					+ " wants to learn ".length());
			String moveName = afterWants.substring(0, afterWants.indexOf("."));
			m_battle.setUIToLearn(moveName);
		}
		else if (toPrint.contains("Awaiting opponent's Pokemon switch.")) {
			m_battle.disableMoves();
		}
		else if(toPrint.contains("has disconnected.")) {
			m_battle.disableMoves();
		}
	}
	@Override
	public boolean canAdvance() {
		if (speechQueue.peek() == null &&
				stringToPrint != null && (stringToPrint.equals("Awaiting your move.") || stringToPrint.equals("Awaiting players' moves.") || stringToPrint.equals("Awaiting opponent's Pokemon switch."))) {
			return false;
		} else if (m_battle != null && m_battle.isLearningMove()) return false;
		else return true;
	}
	@Override
	public void update(GUIContext ctx, int delta) {
		super.update(ctx, delta);
		if (speechDisplay.getText().equals("")
				&& speechQueue.peek() != null &&
				(speechQueue.peek().equals("Awaiting your move.") || speechQueue.peek().equals("Awaiting players' moves.") || speechQueue.peek().equals("Awaiting opponent's Pokemon switch.")))
			advance();
	}
	@Override
	public void advancedPast(String printed) {
		if ((speechQueue.peek() == null &&
				!printed.equals("Awaiting your move.") && 
				!printed.equals("Awaiting players' moves.") && 
				!printed.equals("Awaiting opponent's Pokemon switch.") &&
				!printed.startsWith("Sorry, the move ")  &&
				!m_battle.isMoving() && !m_battle.isLearningMove() &&  (!m_battle.enemyHasMorePokes()) || printed.contains(" won the battle."))
				|| printed.equals("Got away safely!")
				|| printed.endsWith(" was caught successfully!")) {
			m_battle.setBattleSpeech(null);
			m_battle.setVisible(false);
			this.setVisible(false);
			triangle = null;
		}
	}
	*/
}
