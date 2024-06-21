package net.minecraft.src;

public abstract class GenLayer {
	private long worldGenSeed;
	protected GenLayer parent;
	private long chunkSeed;
	private long baseSeed;

	public static GenLayer[] func_48391_a(long j0, WorldType worldType2) {
		GenLayerIsland genLayerIsland3 = new GenLayerIsland(1L);
		GenLayerFuzzyZoom genLayerFuzzyZoom9 = new GenLayerFuzzyZoom(2000L, genLayerIsland3);
		GenLayerAddIsland genLayerAddIsland10 = new GenLayerAddIsland(1L, genLayerFuzzyZoom9);
		GenLayerZoom genLayerZoom11 = new GenLayerZoom(2001L, genLayerAddIsland10);
		genLayerAddIsland10 = new GenLayerAddIsland(2L, genLayerZoom11);
		GenLayerAddSnow genLayerAddSnow12 = new GenLayerAddSnow(2L, genLayerAddIsland10);
		genLayerZoom11 = new GenLayerZoom(2002L, genLayerAddSnow12);
		genLayerAddIsland10 = new GenLayerAddIsland(3L, genLayerZoom11);
		genLayerZoom11 = new GenLayerZoom(2003L, genLayerAddIsland10);
		genLayerAddIsland10 = new GenLayerAddIsland(4L, genLayerZoom11);
		GenLayerAddMushroomIsland genLayerAddMushroomIsland15 = new GenLayerAddMushroomIsland(5L, genLayerAddIsland10);
		byte b4 = 4;
		GenLayer genLayer5 = GenLayerZoom.func_35025_a(1000L, genLayerAddMushroomIsland15, 0);
		GenLayerRiverInit genLayerRiverInit13 = new GenLayerRiverInit(100L, genLayer5);
		genLayer5 = GenLayerZoom.func_35025_a(1000L, genLayerRiverInit13, b4 + 2);
		GenLayerRiver genLayerRiver14 = new GenLayerRiver(1L, genLayer5);
		GenLayerSmooth genLayerSmooth16 = new GenLayerSmooth(1000L, genLayerRiver14);
		GenLayer genLayer6 = GenLayerZoom.func_35025_a(1000L, genLayerAddMushroomIsland15, 0);
		GenLayerBiome genLayerBiome17 = new GenLayerBiome(200L, genLayer6, worldType2);
		genLayer6 = GenLayerZoom.func_35025_a(1000L, genLayerBiome17, 2);
		Object object18 = new GenLayerHills(1000L, genLayer6);

		for(int i7 = 0; i7 < b4; ++i7) {
			object18 = new GenLayerZoom((long)(1000 + i7), (GenLayer)object18);
			if(i7 == 0) {
				object18 = new GenLayerAddIsland(3L, (GenLayer)object18);
			}

			if(i7 == 1) {
				object18 = new GenLayerShore(1000L, (GenLayer)object18);
			}

			if(i7 == 1) {
				object18 = new GenLayerSwampRivers(1000L, (GenLayer)object18);
			}
		}

		GenLayerSmooth genLayerSmooth19 = new GenLayerSmooth(1000L, (GenLayer)object18);
		GenLayerRiverMix genLayerRiverMix20 = new GenLayerRiverMix(100L, genLayerSmooth19, genLayerSmooth16);
		GenLayerVoronoiZoom genLayerVoronoiZoom8 = new GenLayerVoronoiZoom(10L, genLayerRiverMix20);
		genLayerRiverMix20.initWorldGenSeed(j0);
		genLayerVoronoiZoom8.initWorldGenSeed(j0);
		return new GenLayer[]{genLayerRiverMix20, genLayerVoronoiZoom8, genLayerRiverMix20};
	}

	public GenLayer(long j1) {
		this.baseSeed = j1;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += j1;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += j1;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += j1;
	}

	public void initWorldGenSeed(long j1) {
		this.worldGenSeed = j1;
		if(this.parent != null) {
			this.parent.initWorldGenSeed(j1);
		}

		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
	}

	public void initChunkSeed(long j1, long j3) {
		this.chunkSeed = this.worldGenSeed;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += j1;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += j3;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += j1;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += j3;
	}

	protected int nextInt(int i1) {
		int i2 = (int)((this.chunkSeed >> 24) % (long)i1);
		if(i2 < 0) {
			i2 += i1;
		}

		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += this.worldGenSeed;
		return i2;
	}

	public abstract int[] getInts(int i1, int i2, int i3, int i4);
}
