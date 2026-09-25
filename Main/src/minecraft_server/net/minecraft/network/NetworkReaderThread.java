package net.minecraft.network;

class NetworkReaderThread extends Thread {
	final NetworkManager netManager;

	NetworkReaderThread(NetworkManager netManager, String s) {
		super(s);
		this.netManager = netManager;
	}

	public void run() {
		synchronized(NetworkManager.threadCounterLock) {
			++NetworkManager.numReadThreads;
		}
		
		try {
			while(NetworkManager.isRunning(this.netManager) && !NetworkManager.isServerTerminating(this.netManager)) {
				while(NetworkManager.readTick(this.netManager)) {
				}
				
				try {
					sleep(2L); // Vanilla was: 100L
				} catch (InterruptedException interruptedException15) {
				}
			}
		} finally {
			synchronized(NetworkManager.threadCounterLock) {
				--NetworkManager.numReadThreads;
			}
		}
	
	}
}
