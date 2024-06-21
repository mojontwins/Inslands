package net.minecraft.src;

public class GenLayerAddSnow extends GenLayer {
	public GenLayerAddSnow(long j1, GenLayer genLayer3) {
		super(j1);
		this.parent = genLayer3;
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
				int i13 = i9[i12 + 1 + (i11 + 1) * i7];
				this.initChunkSeed((long)(i12 + i1), (long)(i11 + i2));
				if(i13 == 0) {
					i10[i12 + i11 * i3] = 0;
				} else {
					int i14 = this.nextInt(5);
					if(i14 == 0) {
						i14 = BiomeGenBase.icePlains.biomeID;
					} else {
						i14 = 1;
					}

					i10[i12 + i11 * i3] = i14;
				}
			}
		}

		return i10;
	}
}
