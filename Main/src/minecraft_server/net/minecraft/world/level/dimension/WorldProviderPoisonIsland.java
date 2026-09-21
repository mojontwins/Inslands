package net.minecraft.world.level.dimension;

import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;

public class WorldProviderPoisonIsland extends WorldProvider {

	@Override
	public void setInitialSpawnLocation(World world) {
		// Find somewhere low but above sea level, and try VERY hard.
		int attemptsLeft = 4096;
		boolean found = false;
		
		while(attemptsLeft -- > 0) {
			int x = 8 + world.rand.nextInt(WorldSize.width - 16);
			int z = 8 + world.rand.nextInt(WorldSize.length - 16);
			int y = world.getHeightValue(x, z) + 1;
			
			if(world.canBlockSeeTheSky(x, y, z) && y <= 70 && y >= 64) {
				world.getWorldInfo().setSpawn(x, y, z);
				found = true;
				break;
			}
		}
		
		if(!found) {
			// Couldn't find a low spot - fall back to the generic search.
			super.setInitialSpawnLocation(world);
			return;
		}
		
		world.findingSpawnPoint = false;
		this.generateSpawnHouse(world);
	}
}
