package net.minecraft.src;

public class WorldProviderEnd extends WorldProvider {
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5F, 0.0F);
		this.worldType = 1;
		this.hasNoSky = true;
	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
	}

	public float calculateCelestialAngle(long j1, float f3) {
		return 0.0F;
	}

	public boolean canRespawnHere() {
		return false;
	}

	public boolean func_48567_d() {
		return false;
	}

	public boolean canCoordinateBeSpawn(int i1, int i2) {
		int i3 = this.worldObj.getFirstUncoveredBlock(i1, i2);
		return i3 == 0 ? false : Block.blocksList[i3].blockMaterial.blocksMovement();
	}

	public ChunkCoordinates getEntrancePortalLocation() {
		return new ChunkCoordinates(100, 50, 0);
	}

	public int getAverageGroundLevel() {
		return 50;
	}
}
