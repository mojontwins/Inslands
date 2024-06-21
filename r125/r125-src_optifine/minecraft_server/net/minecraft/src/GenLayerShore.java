package net.minecraft.src;

public class GenLayerShore extends GenLayer {
	public GenLayerShore(long j1, GenLayer genLayer3) {
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
				int i10;
				int i11;
				int i12;
				int i13;
				if(i9 == BiomeGenBase.mushroomIsland.biomeID) {
					i10 = i5[i8 + 1 + (i7 + 1 - 1) * (i3 + 2)];
					i11 = i5[i8 + 1 + 1 + (i7 + 1) * (i3 + 2)];
					i12 = i5[i8 + 1 - 1 + (i7 + 1) * (i3 + 2)];
					i13 = i5[i8 + 1 + (i7 + 1 + 1) * (i3 + 2)];
					if(i10 != BiomeGenBase.ocean.biomeID && i11 != BiomeGenBase.ocean.biomeID && i12 != BiomeGenBase.ocean.biomeID && i13 != BiomeGenBase.ocean.biomeID) {
						i6[i8 + i7 * i3] = i9;
					} else {
						i6[i8 + i7 * i3] = BiomeGenBase.mushroomIslandShore.biomeID;
					}
				} else if(i9 != BiomeGenBase.ocean.biomeID && i9 != BiomeGenBase.river.biomeID && i9 != BiomeGenBase.swampland.biomeID && i9 != BiomeGenBase.extremeHills.biomeID) {
					i10 = i5[i8 + 1 + (i7 + 1 - 1) * (i3 + 2)];
					i11 = i5[i8 + 1 + 1 + (i7 + 1) * (i3 + 2)];
					i12 = i5[i8 + 1 - 1 + (i7 + 1) * (i3 + 2)];
					i13 = i5[i8 + 1 + (i7 + 1 + 1) * (i3 + 2)];
					if(i10 != BiomeGenBase.ocean.biomeID && i11 != BiomeGenBase.ocean.biomeID && i12 != BiomeGenBase.ocean.biomeID && i13 != BiomeGenBase.ocean.biomeID) {
						i6[i8 + i7 * i3] = i9;
					} else {
						i6[i8 + i7 * i3] = BiomeGenBase.beach.biomeID;
					}
				} else if(i9 == BiomeGenBase.extremeHills.biomeID) {
					i10 = i5[i8 + 1 + (i7 + 1 - 1) * (i3 + 2)];
					i11 = i5[i8 + 1 + 1 + (i7 + 1) * (i3 + 2)];
					i12 = i5[i8 + 1 - 1 + (i7 + 1) * (i3 + 2)];
					i13 = i5[i8 + 1 + (i7 + 1 + 1) * (i3 + 2)];
					if(i10 == BiomeGenBase.extremeHills.biomeID && i11 == BiomeGenBase.extremeHills.biomeID && i12 == BiomeGenBase.extremeHills.biomeID && i13 == BiomeGenBase.extremeHills.biomeID) {
						i6[i8 + i7 * i3] = i9;
					} else {
						i6[i8 + i7 * i3] = BiomeGenBase.extremeHillsEdge.biomeID;
					}
				} else {
					i6[i8 + i7 * i3] = i9;
				}
			}
		}

		return i6;
	}
}
