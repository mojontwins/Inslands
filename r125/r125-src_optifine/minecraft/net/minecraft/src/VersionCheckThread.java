package net.minecraft.src;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionCheckThread extends Thread {
	public void run() {
		HttpURLConnection conn = null;

		try {
			Config.dbg("Checking for new version");
			URL e = new URL("http://optifine.net/version/1.2.5/HD_U.txt");
			conn = (HttpURLConnection)e.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.connect();

			try {
				InputStream in = conn.getInputStream();
				String verStr = Config.readInputStream(in);
				in.close();
				String[] verLines = Config.tokenize(verStr, "\n\r");
				if(verLines.length < 1) {
					return;
				}

				String newVer = verLines[0];
				Config.dbg("Version found: " + newVer);
				if(Config.compareRelease(newVer, "C7") > 0) {
					Config.setNewRelease(newVer);
					return;
				}
			} finally {
				if(conn != null) {
					conn.disconnect();
				}

			}

		} catch (Exception exception11) {
			exception11.printStackTrace();
		}
	}
}
