package net.minecraft.src;

public class GenLayerRiver extends GenLayer {
	public GenLayerRiver(long j1, GenLayer genLayer3) {
		super(j1);
		super.parent = genLayer3;
	}

	public int[] getInts(int i1, int i2, int i3, int i4) {
		int i5 = i1 - 1;
		int i6 = i2 - 1;
		int i7 = i3 + 2;
		int i8 = i4 + 2;
		int[] i9 = this.parent.getInts(i5, i6, i7, i8);
		int[] i10 = IntCache.getIntCache(i3 * i4);

		for(int i11 = 0; i11 < i4; ++i11) {
			for(int i12 = 0; i12 < i3; ++i12) {
				int i13 = i9[i12 + 0 + (i11 + 1) * i7];
				int i14 = i9[i12 + 2 + (i11 + 1) * i7];
				int i15 = i9[i12 + 1 + (i11 + 0) * i7];
				int i16 = i9[i12 + 1 + (i11 + 2) * i7];
				int i17 = i9[i12 + 1 + (i11 + 1) * i7];
				if(i17 != 0 && i13 != 0 && i14 != 0 && i15 != 0 && i16 != 0) {
					if(i17 == i13 && i17 == i15 && i17 == i14 && i17 == i16) {
						i10[i12 + i11 * i3] = -1;
					} else {
						i10[i12 + i11 * i3] = BiomeGenBase.river.biomeID;
					}
				} else {
					i10[i12 + i11 * i3] = BiomeGenBase.river.biomeID;
				}
			}
		}

		return i10;
	}
}
