package net.minecraft.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public final class ThreadDownloadResources extends Thread {
	private File resourcesFolder;
	private Minecraft mc;
	private boolean closing = false;

	public ThreadDownloadResources(File var1, Minecraft var2) {
		this.mc = var2;
		this.setName("Resource download thread");
		this.setDaemon(true);
		this.resourcesFolder = new File(var1, "resources/");
		if(!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
		}
	}

	public final void run() {
		// $FF: Couldn't be decompiled
	}

	private void downloadResource(URL var1, File var2) throws IOException {
		byte[] var3 = new byte[4096];
		DataInputStream var5 = new DataInputStream(var1.openStream());
		DataOutputStream var6 = new DataOutputStream(new FileOutputStream(var2));

		do {
			int var4 = var5.read(var3);
			if(var4 < 0) {
				var5.close();
				var6.close();
				return;
			}

			var6.write(var3, 0, var4);
		} while(!this.closing);

	}

	public final void closeMinecraft() {
		this.closing = true;
	}
}
