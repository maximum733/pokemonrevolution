package polr.client.network.security;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * 
 * @author HP_Administrator
 */
public class diskUtilities {

	/** Creates a new instance of DiskUtils */
	public diskUtilities() {
	}

	public static String getSerialNumber(String drive) {
		String os = System.getProperty("os.name");
		try {
			if (os.startsWith("Windows")) {
				return getSerialNumberWindows(drive);
			} else if (os.startsWith("Linux")) {
				// return;
			} else {
				System.out.println("unknown operating system: " + os);
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return "0";
	}

	public static String getSerialNumberWindows(String drive) {
		String result = "";
		try {
			// File file = File.createTempFile("realhowto",".vbs");
			File file = File.createTempFile("tmp", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
					+ "Set colDrives = objFSO.Drives\n"
					+ "Set objDrive = colDrives.item(\""
					+ drive
					+ "\")\n"
					+ "Wscript.Echo objDrive.SerialNumber"; // see note
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			System.out.println("HDS Fail " + e);

			System.exit(1);
		}
		if (result.trim().length() < 1) {
			System.exit(1);
		}

		return result.trim();
	}
}