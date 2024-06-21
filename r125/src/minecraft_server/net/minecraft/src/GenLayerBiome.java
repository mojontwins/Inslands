package net.minecraft.src;

public class GenLayerBiome extends GenLayer {
	private BiomeGenBase[] allowedBiomes = new BiomeGenBase[]{BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga, BiomeGenBase.jungle};

	public GenLayerBiome(long j1, GenLayer genLayer3, WorldType worldType4) {
		super(j1);
		this.parent = genLayer3;
		if(worldType4 == WorldType.DEFAULT_1_1) {
			this.allowedBiomes = new BiomeGenBase[]{BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga};
		}

	}

	public int[] getInts(int i1, int i2, int i3, int i4) {
		int[] i5 = this.parent.getInts(i1, i2, i3, i4);
		int[] i6 = IntCache.getIntCache(i3 * i4);

		for(int i7 = 0; i7 < i4; ++i7) {
			for(int i8 = 0; i8 < i3; ++i8) {
				this.initChunkSeed((long)(i8 + i1), (long)(i7 + i2));
				int i9 = i5[i8 + i7 * i3];
				if(i9 == 0) {
					i6[i8 + i7 * i3] = 0;
				} else if(i9 == BiomeGenBase.mushroomIsland.biomeID) {
					i6[i8 + i7 * i3] = i9;
				} else if(i9 == 1) {
					i6[i8 + i7 * i3] = this.allowedBiomes[this.nextInt(this.allowedBiomes.length)].biomeID;
				} else {
					i6[i8 + i7 * i3] = BiomeGenBase.icePlains.biomeID;
				}
			}
		}

		return i6;
	}
}
