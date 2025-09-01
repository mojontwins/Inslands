package net.minecraft.client.multiplayer;

import net.minecraft.network.NetworkManager;

class ThreadCloseConnection extends Thread {
	final NetworkManager networkManager;

	ThreadCloseConnection(NetworkManager networkManager1) {
		this.networkManager = networkManager1;
	}

	public void run() {
		try {
			Thread.sleep(2000L);
			if(NetworkManager.isRunning(this.networkManager)) {
				NetworkManager.getWriteThread(this.networkManager).interrupt();
				this.networkManager.close("disconnect.closed", new Object[0]);
			}
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}

	}
}
