package polr.server.mechanics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.simpleframework.xml.ElementMap;

import polr.server.mechanics.moves.MoveListEntry;
import polr.server.mechanics.moves.PokemonMove;

/**
 * Mechanics for the DS games (diamond/pearl), or the "Jewel Generation".
 * @author Colin
 */
public class JewelMechanics extends AdvanceMechanics {

    /**
     * Map indicating whether each move is special.
     */
	@ElementMap
    private static HashMap m_moves = new HashMap();
    
    public JewelMechanics(int bytes) {
        super(bytes);
    }
    
    /**
     * Load the move types from the given file.
     */
    public static void loadMoveTypes(String f) throws FileNotFoundException {
        m_moves.clear();
        String line;
        BufferedReader input = new BufferedReader(new FileReader(f));
        try {
            while ((line = input.readLine()) != null) {
                int space = line.lastIndexOf(' ');
                boolean special = (Integer.parseInt(line.substring(space + 1)) != 0);
                String move = line.substring(0, space);
                m_moves.put(move, new Boolean(special));
            }
        } catch (IOException e) {
            
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                
            }
        }
    }
    
 /**
     * Load the move types from the given file.
     */
    public static void loadMoveTypes(File f) throws FileNotFoundException {
        m_moves.clear();
        String line;
        BufferedReader input = new BufferedReader(new FileReader(f));
        try {
            while ((line = input.readLine()) != null) {
                int space = line.lastIndexOf(' ');
                boolean special = (Integer.parseInt(line.substring(space + 1)) != 0);
                String move = line.substring(0, space);
                m_moves.put(move, new Boolean(special));
            }
        } catch (IOException e) {
            
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                
            }
        }
    }
    

    
    /**
     * Return whether a given move deals special damage.
     * In Diamond/Pearl, this is based on the move, not its type.
     */
    public boolean isMoveSpecial(PokemonMove move) {
        MoveListEntry entry = move.getMoveListEntry();
        if (entry == null) {
            return move.getType().isSpecial();
        }
        Boolean b = (Boolean)m_moves.get(entry.getName());
        if (b == null) {
            System.out.println("Warning: no move type entry for " + entry.getName() + "!");
            return move.getType().isSpecial();
        }
        return b.booleanValue();
    }


}
