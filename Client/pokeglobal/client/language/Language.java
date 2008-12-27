package pokeglobal.client.language;

import java.util.HashMap;

public class Language {
	private HashMap<Integer, String> strings;
	private HashMap<Integer, String> pokemon;
	private HashMap<Integer, String> attacks;
	private HashMap<Integer, String> weffects;
	private HashMap<Integer, String> seffects;
	private HashMap<Integer, String> abilities;
	
	public Language() {
		
	}
	
	public String get(int i, String[] replacements) {
		String temp = strings.get(i);
		for (int id = 0; temp.contains("%" + id); id++) {
			try {
				if (replacements[id].substring(0,1).equals("p")) {
					replacements[id] = pokemon.get(replacements[id].substring(1,replacements[id].length() - 1));
				} else if (replacements[id].substring(0,1).equals("a")) {
					replacements[id] = attacks.get(replacements[id].substring(1,replacements[id].length() - 1));
				} else if (replacements[id].substring(0,1).equals("w")) {
					replacements[id] = weffects.get(replacements[id].substring(1,replacements[id].length() - 1));
				} else if (replacements[id].substring(0,1).equals("e")) {
					replacements[id] = seffects.get(replacements[id].substring(1,replacements[id].length() - 1));
				} else if (replacements[id].substring(0,1).equals("b")) {
					replacements[id] = abilities.get(replacements[id].substring(1,replacements[id].length() - 1));
				}
				temp.replace("%" + id, replacements[id]);
			} catch (Exception e) {
				System.out.println("Language: Replacement String Not Found");
			}
		}
		// Could make it read what happened out loud...
		return temp;
	}
}
