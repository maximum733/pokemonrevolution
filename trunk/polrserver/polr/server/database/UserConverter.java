package polr.server.database;

import java.io.*;

import org.simpleframework.xml.Serializer;

import polr.server.object.PlayerChar;

public class UserConverter {
	private Serializer serializer;
	public UserConverter() {
		String[] filenames;
		File f = new File("accounts/");
		filenames = f.list();
		// PlayerCharOld olduserfile;
		// Just make copies of the old character stuff for this
		PlayerChar newuserfile;
		for(String userfile : filenames) {
			if (userfile.endsWith(".usr.xml")) {
				//olduserfile = serializer.read(PlayerCharOld.class, new File("accounts/" + userfile);
				newuserfile = new PlayerChar();
				//switch all stuff from old one over to new one
				File delfile = new File("accounts/" + userfile);
				delfile.delete();
				try {
					serializer.write(newuserfile, new File("accounts/" + userfile));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
