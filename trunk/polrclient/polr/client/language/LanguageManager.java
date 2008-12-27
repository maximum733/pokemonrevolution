package polr.client.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LanguageManager {
	private Language currentLanguage;
	private HashMap<String, Language> languages;

	public LanguageManager() {
		loadLanguages();
		setLanguage("english");
	}

	private void loadLanguages() {
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(
							"res/languages.txt")));

			String f = null;
			int msgid = -1;
			while ((f = is.readLine()) != null) {
				if (f.substring(0, 1).equals("#"))
					msgid++;
				else if (f.substring(0, 1).equals(";"))
					;
				else {
					String lang = f.substring(0, f.indexOf(" "));
					String msg = f.substring(f.indexOf(" ")).trim();
					if (!languages.containsKey(lang)) {
						languages.put(lang, new Language());
					}
					// languages.get(lang).put(msgid, msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage(int id, String[] replacements) {
		return currentLanguage.get(id, replacements);
	}

	public void setLanguage(String lang) {
		currentLanguage = languages.get(lang);
	}
}
