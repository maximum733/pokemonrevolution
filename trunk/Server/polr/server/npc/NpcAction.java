package polr.server.npc;

import polr.server.player.PlayerChar;

/**
 * An interface to handle Npc actions
 * @author TMKCodes
 * @author shinobi
 *
 */
public interface NpcAction {
	public void execute(PlayerChar p);
}