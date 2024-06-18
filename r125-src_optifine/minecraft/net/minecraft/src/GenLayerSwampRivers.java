package net.minecraft.src;

public class GenLayerSwampRivers extends GenLayer {
	public GenLayerSwampRivers(long j1, GenLayer genLayer3) {
		super(j1);
		this.parent = genLayer3;
	}

	public int[] getInts(int i1, int i2, int i3, int i4) {
		int[] i5 = this.parent.getInts(i1 - 1, i2 - 1, i3 + 2, i4 + 2);
		int[] i6 = IntCache.getIntCache(i3 * i4);

		for(int i7 = 0; i7 < i4; ++i7) {
			for(int i8 = 0; i8 < i3; ++i8) {
				this.initChunkSeed((long)(i8 + i1), (long)(i7 + i2));
				int i9 = i5[i8 + 1 + (i7 + 1) * (i3 + 2)];
				if(i9 == BiomeGenBase.swampland.biomeID && this.nextInt(6) == 0) {
					i6[i8 + i7 * i3] = BiomeGenBase.river.biomeID;
				} else if((i9 == BiomeGenBase.jungle.biomeID || i9 == BiomeGenBase.jungleHills.biomeID) && this.nextInt(8) == 0) {
					i6[i8 + i7 * i3] = BiomeGenBase.river.biomeID;
				} else {
					i6[i8 + i7 * i3] = i9;
				}
			}
		}

		return i6;
	}
}
