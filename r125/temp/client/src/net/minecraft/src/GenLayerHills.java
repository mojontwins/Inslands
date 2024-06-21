package net.minecraft.src;

public class GenLayerHills extends GenLayer {
	public GenLayerHills(long j1, GenLayer genLayer3) {
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
				if(this.nextInt(3) == 0) {
					int i10 = i9;
					if(i9 == BiomeGenBase.desert.biomeID) {
						i10 = BiomeGenBase.desertHills.biomeID;
					} else if(i9 == BiomeGenBase.forest.biomeID) {
						i10 = BiomeGenBase.forestHills.biomeID;
					} else if(i9 == BiomeGenBase.taiga.biomeID) {
						i10 = BiomeGenBase.taigaHills.biomeID;
					} else if(i9 == BiomeGenBase.plains.biomeID) {
						i10 = BiomeGenBase.forest.biomeID;
					} else if(i9 == BiomeGenBase.icePlains.biomeID) {
						i10 = BiomeGenBase.iceMountains.biomeID;
					} else if(i9 == BiomeGenBase.jungle.biomeID) {
						i10 = BiomeGenBase.jungleHills.biomeID;
					}

					if(i10 != i9) {
						int i11 = i5[i8 + 1 + (i7 + 1 - 1) * (i3 + 2)];
						int i12 = i5[i8 + 1 + 1 + (i7 + 1) * (i3 + 2)];
						int i13 = i5[i8 + 1 - 1 + (i7 + 1) * (i3 + 2)];
						int i14 = i5[i8 + 1 + (i7 + 1 + 1) * (i3 + 2)];
						if(i11 == i9 && i12 == i9 && i13 == i9 && i14 == i9) {
							i6[i8 + i7 * i3] = i10;
						} else {
							i6[i8 + i7 * i3] = i9;
						}
					} else {
						i6[i8 + i7 * i3] = i9;
					}
				} else {
					i6[i8 + i7 * i3] = i9;
				}
			}
		}

		return i6;
	}
}
