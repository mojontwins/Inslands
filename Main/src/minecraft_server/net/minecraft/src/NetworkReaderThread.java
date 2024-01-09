package net.minecraft.src;

class NetworkReaderThread extends Thread {
	final NetworkManager netManager;

	NetworkReaderThread(final NetworkManager networkmanager, final String s) {
		super(s);
		this.netManager = networkmanager;
	}

	@Override
	public void run() {
		synchronized(NetworkManager.threadSyncObject) {
			++NetworkManager.numReadThreads;
			// monitorexit(NetworkManager.threadSyncObject)
			//break Label_0042;
		}

		try {
		while(true) {
				if (!NetworkManager.readNetworkPacket(this.netManager)) {
			try {
						Thread.sleep(2L);
					}  catch (InterruptedException ex) {}
					
				if(!NetworkManager.isRunning(this.netManager)) {
					break;
				}

				if(NetworkManager.isServerTerminating(this.netManager)) {
					break;
				}

					continue;
				}
				}
			} finally {
					synchronized(NetworkManager.threadSyncObject) {
						--NetworkManager.numReadThreads;
					}
			// monitorexit(NetworkManager.threadSyncObject)
		}

		synchronized(NetworkManager.threadSyncObject) {
			--NetworkManager.numReadThreads;
		}
	// monitorexit(NetworkManager.threadSyncObject)
	}
}
