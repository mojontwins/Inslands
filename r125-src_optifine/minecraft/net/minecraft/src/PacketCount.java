package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class PacketCount {
	public static boolean allowCounting = true;
	private static final Map packetCountForID = new HashMap();
	private static final Map sizeCountForID = new HashMap();
	private static final Object lock = new Object();

	public static void countPacket(int i0, long j1) {
		if(allowCounting) {
			Object object3 = lock;
			synchronized(lock) {
				if(packetCountForID.containsKey(i0)) {
					packetCountForID.put(i0, ((Long)packetCountForID.get(i0)).longValue() + 1L);
					sizeCountForID.put(i0, ((Long)sizeCountForID.get(i0)).longValue() + j1);
				} else {
					packetCountForID.put(i0, 1L);
					sizeCountForID.put(i0, j1);
				}

			}
		}
	}
}
