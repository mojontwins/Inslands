package net.minecraft.world.level.dimension;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.phys.Vec3D;

public class WorldProviderCaves extends WorldProvider {

	public WorldProviderCaves() {
	}

	public float getCloudHeight() {
		return 0.0F;
	}

	@Override
	public void setInitialSpawnLocation(World world) {
		world.findingSpawnPoint = true;

		// Caves are hollowed out of solid rock, so there is rarely any sky to
		// stand under. Hunt for an air pocket with solid ground beneath it and
		// build the indev house on that cave floor instead.
		int attemptsLeft = 8192;
		boolean found = false;

		while(attemptsLeft -- > 0) {
			int x = 24 + world.rand.nextInt(WorldSize.width - 48);
			int z = 24 + world.rand.nextInt(WorldSize.length - 48);
			int floorY = this.findHouseFloor(world, x, z);

			if(floorY >= 0) {
				world.getWorldInfo().setSpawn(x, floorY, z);
				found = true;
				break;
			}
		}

		// Try REALLY hard: if the random search came up empty, exhaustively
		// scan the map and take the cave floor closest to its center.
		if(!found) {
			int centerX = WorldSize.width / 2;
			int centerZ = WorldSize.length / 2;
			int bestDistSq = Integer.MAX_VALUE;
			int bestX = -1;
			int bestY = -1;
			int bestZ = -1;

			for(int x = 24; x < WorldSize.width - 24; x += 4) {
				for(int z = 24; z < WorldSize.length - 24; z += 4) {
					int dx = x - centerX;
					int dz = z - centerZ;
					int distSq = dx * dx + dz * dz;
					if(distSq >= bestDistSq) continue;

					int floorY = this.findHouseFloor(world, x, z);
					if(floorY < 0) continue;

					bestDistSq = distSq;
					bestX = x;
					bestY = floorY;
					bestZ = z;
				}
			}

			if(bestX >= 0) {
				world.getWorldInfo().setSpawn(bestX, bestY, bestZ);
				found = true;
			}
		}

		if(!found) {
			// Give up: default to the center of the map. The indev house raises
			// its own floor down to solid ground, so the player never falls into
			// the void.
			world.getWorldInfo().setSpawn(WorldSize.width / 2, 64, WorldSize.length / 2);
		}

		world.findingSpawnPoint = false;
		this.generateSpawnHouse(world);
	}

	// 1. Find an air pocket, 2. drop down to the ground beneath it, 3. make sure
	// the house actually fits there. Returns the floor Y, or -1 if unusable.
	private int findHouseFloor(World world, int x, int z) {
		int pocketY = this.findAirPocket(world, x, z);
		if(pocketY < 0) return -1;

		int groundY = this.findGroundBelow(world, x, z, pocketY);
		if(groundY < 0) return -1;

		int floorY = groundY + 1;
		if(!this.canBuildHouseAt(world, x, floorY, z)) return -1;

		return floorY;
	}

	// Walks a column down from the top of the world for the first pocket of air
	// sealed under solid rock - i.e. a cave.
	private int findAirPocket(World world, int x, int z) {
		boolean seenRock = false;

		for(int y = 126; y > 1; y --) {
			if(this.isSolidGround(world, x, y, z)) {
				seenRock = true;
			} else if(seenRock) {
				return y;
			}
		}

		return -1;
	}

	// Drops straight down from a cave pocket until solid ground is found.
	private int findGroundBelow(World world, int x, int z, int fromY) {
		for(int y = fromY - 1; y > 0; y --) {
			if(this.isSolidGround(world, x, y, z)) {
				return y;
			}
		}

		return -1;
	}

	// A cave floor is only usable if the house has headroom and the spot is not
	// flooded with liquid.
	private boolean canBuildHouseAt(World world, int x, int floorY, int z) {
		if(floorY < 4 || floorY > 120) return false;

		for(int y = floorY; y < floorY + 4; y ++) {
			if(!world.isAirBlock(x, y, z)) return false;
		}

		return true;
	}

	private boolean isSolidGround(World world, int x, int y, int z) {
		int blockID = world.getBlockID(x, y, z);
		if(blockID == 0) return false;

		Block block = Block.blocksList[blockID];
		return block != null && block.blockMaterial.getIsSolid();
	}
	
	@Override
	public Vec3D getSkyColor(World world, Entity entity1, float renderPartialTick) {
		return new Vec3D(0, 0, 0);
	}
}
