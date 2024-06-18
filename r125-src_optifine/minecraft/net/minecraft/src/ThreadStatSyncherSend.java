package net.minecraft.src;

import java.util.Map;

class ThreadStatSyncherSend extends Thread {
	final Map field_27233_a;
	final StatsSyncher syncher;

	ThreadStatSyncherSend(StatsSyncher statsSyncher1, Map map2) {
		this.syncher = statsSyncher1;
		this.field_27233_a = map2;
	}

	public void run() {
		try {
			StatsSyncher.func_27412_a(this.syncher, this.field_27233_a, StatsSyncher.getUnsentDataFile(this.syncher), StatsSyncher.getUnsentTempFile(this.syncher), StatsSyncher.getUnsentOldFile(this.syncher));
		} catch (Exception exception5) {
			exception5.printStackTrace();
		} finally {
			StatsSyncher.setBusy(this.syncher, false);
		}

	}
}
