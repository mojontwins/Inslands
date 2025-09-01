package net.minecraft.network;

import java.io.IOException;

class NetworkWriterThread extends Thread {
	final NetworkManager netManager;

	NetworkWriterThread(NetworkManager networkManager, String s) {
		super(s);
		this.netManager = networkManager;
	}

	public void run() {
		synchronized(NetworkManager.threadCounterLock) {
			++NetworkManager.numWriteThreads;
		}

		try {
			while(NetworkManager.isRunning(this.netManager)) {
				while(NetworkManager.writeTick(this.netManager)) {
				}
				
				try {
					sleep(2L); // Vanilla was: 100L
				} catch (InterruptedException interruptedException16) {
				}
				
				try {
					if(NetworkManager.getSocketOutputStream(this.netManager) != null) {
						NetworkManager.getSocketOutputStream(this.netManager).flush();
					}
				} catch (IOException e) {
					if(!NetworkManager.isTerminating(this.netManager)) {
						NetworkManager.handleException(this.netManager, e);
					}

					e.printStackTrace();
				}
			}
		}
		finally {
			synchronized(NetworkManager.threadCounterLock) {
				--NetworkManager.numWriteThreads;
			}
		}
		
	}
}
