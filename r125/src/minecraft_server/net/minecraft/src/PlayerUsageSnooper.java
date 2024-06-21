package net.minecraft.src;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlayerUsageSnooper {
	private Map field_52016_a = new HashMap();
	private final URL field_52015_b;

	public PlayerUsageSnooper(String string1) {
		try {
			this.field_52015_b = new URL("http://snoop.minecraft.net/" + string1);
		} catch (MalformedURLException malformedURLException3) {
			throw new IllegalArgumentException();
		}
	}

	public void func_52014_a(String string1, Object object2) {
		this.field_52016_a.put(string1, object2);
	}

	public void func_52012_a() {
		PlayerUsageSnooperThread playerUsageSnooperThread1 = new PlayerUsageSnooperThread(this, "reporter");
		playerUsageSnooperThread1.setDaemon(true);
		playerUsageSnooperThread1.start();
	}

	static URL func_52013_a(PlayerUsageSnooper playerUsageSnooper0) {
		return playerUsageSnooper0.field_52015_b;
	}

	static Map func_52011_b(PlayerUsageSnooper playerUsageSnooper0) {
		return playerUsageSnooper0.field_52016_a;
	}
}
