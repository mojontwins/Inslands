package net.minecraft.src;

import java.io.*;

class NetworkWriterThread extends Thread
{
	final NetworkManager netManager;

    NetworkWriterThread(final NetworkManager networkmanager, final String s) {
        super(s);
        this.netManager = networkmanager;
	}

    @Override
	public void run() {
		synchronized(NetworkManager.threadSyncObject) {
			++NetworkManager.numWriteThreads;
		}
        // monitorexit(NetworkManager.threadSyncObject)
			try {
            while (NetworkManager.isRunning(this.netManager)) {
                while (NetworkManager.sendNetworkPacket(this.netManager)) {}
				try {
	                    Thread.sleep(2L);
				} catch (InterruptedException ex) {}

				try {
                    if (NetworkManager.getSocketOutputStream(this.netManager) == null) {
                        continue;
					}
					NetworkManager.getSocketOutputStream(this.netManager).flush();
				} catch (IOException ioexception) {
					if(!NetworkManager.isTerminating(this.netManager)) {
                        NetworkManager.onNetworkError(this.netManager, ioexception);
                    }
                    ioexception.printStackTrace();
					}
				}
			} finally {
					synchronized(NetworkManager.threadSyncObject) {
						--NetworkManager.numWriteThreads;
					}
	        // monitorexit(NetworkManager.threadSyncObject)
				}
		synchronized(NetworkManager.threadSyncObject) {
			--NetworkManager.numWriteThreads;
		}
       // monitorexit(NetworkManager.threadSyncObject)
	}
}
