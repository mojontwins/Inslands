package net.minecraft.network;

import java.io.IOException;

class NetworkWriterThread extends Thread {
	final NetworkManager netManager;

	NetworkWriterThread(NetworkManager networkManager, String s) {
		super(s);
		this.netManager = networkManager;
	}

	@SuppressWarnings("unused")
	public void run() {
		synchronized(NetworkManager.threadSyncObject) {
			++NetworkManager.numWriteThreads;
		}

		try {
			while(NetworkManager.isRunning(this.netManager)) {
				while(NetworkManager.sendNetworkPacket(this.netManager)) {
				}
				
				try {
					sleep(2L); // Vanilla was: 100L
				} catch (InterruptedException interruptedException16) {
				}
				
				try {
					if(NetworkManager.getSocketOutputStream(this.netManager) != null) {
						NetworkManager.getSocketOutputStream(this.netManager).flush();
					}
				} catch (IOException iOException18) {
					if(!NetworkManager.isTerminating(this.netManager)) {
						NetworkManager.onNetworkError(this.netManager, iOException18);
					}

					iOException18.printStackTrace();
				}
			}
		}
		finally {
			synchronized(NetworkManager.threadSyncObject) {
				--NetworkManager.numWriteThreads;
			}
		}
		
	}
}
