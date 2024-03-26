package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ThreadLightingUpdater implements Runnable {
	private static final int maxLightsToUpdateInBulk = 100;
	private static boolean keepGoing = true;
	private Minecraft mc;
	private static long timeMark;
	
	public ThreadLightingUpdater(Minecraft mc) {
		this.mc = mc;
	}

	@Override
	public void run() {
		World world = this.mc.theWorld;
		long newTime;
		int size; 
		
		System.out.println ("Launching ThreadLightingUpdater");
		
		timeMark = System.currentTimeMillis();
		
		while(keepGoing) {			
			if(world != null) {
				int lightsUpdated = 0;

				MetadataChunkBlock update;
				while(
						(update = world.getLightingToUpdateDeque().pollLast()) != null && 
						lightsUpdated ++ < maxLightsToUpdateInBulk
				) {
					update.recalculateLighting(world);
				}
				
				newTime = System.currentTimeMillis();
				if (newTime > timeMark + 1000 && (size = world.getLightingToUpdateDeque().size()) > 0) {
					timeMark = newTime;
					System.out.println("Lighting to update size = " + size);
				}
			}
			Thread.yield();
		}
		
		System.out.println ("Shutting down ThreadLightingUpdater");
	}
	
	public void shutdownLighting() {
		keepGoing = false;
	}

}
