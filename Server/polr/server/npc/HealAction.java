package polr.server.npc;

import polr.server.player.PlayerChar;

/**
 * An action that heals the player
 * @author shinobi
 *
 */
public class HealAction implements NpcAction {

	public void execute(PlayerChar p) {
		p.healParty();
	}

}
