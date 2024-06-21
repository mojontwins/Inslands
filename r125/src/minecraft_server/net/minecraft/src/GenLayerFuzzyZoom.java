package net.minecraft.src;

public class GenLayerFuzzyZoom extends GenLayer {
	public GenLayerFuzzyZoom(long j1, GenLayer genLayer3) {
		super(j1);
		super.parent = genLayer3;
	}

	public int[] getInts(int i1, int i2, int i3, int i4) {
		int i5 = i1 >> 1;
		int i6 = i2 >> 1;
		int i7 = (i3 >> 1) + 3;
		int i8 = (i4 >> 1) + 3;
		int[] i9 = this.parent.getInts(i5, i6, i7, i8);
		int[] i10 = IntCache.getIntCache(i7 * 2 * i8 * 2);
		int i11 = i7 << 1;

		int i13;
		for(int i12 = 0; i12 < i8 - 1; ++i12) {
			i13 = i12 << 1;
			int i14 = i13 * i11;
			int i15 = i9[0 + (i12 + 0) * i7];
			int i16 = i9[0 + (i12 + 1) * i7];

			for(int i17 = 0; i17 < i7 - 1; ++i17) {
				this.initChunkSeed((long)(i17 + i5 << 1), (long)(i12 + i6 << 1));
				int i18 = i9[i17 + 1 + (i12 + 0) * i7];
				int i19 = i9[i17 + 1 + (i12 + 1) * i7];
				i10[i14] = i15;
				i10[i14++ + i11] = this.choose(i15, i16);
				i10[i14] = this.choose(i15, i18);
				i10[i14++ + i11] = this.choose(i15, i18, i16, i19);
				i15 = i18;
				i16 = i19;
			}
		}

		int[] i20 = IntCache.getIntCache(i3 * i4);

		for(i13 = 0; i13 < i4; ++i13) {
			System.arraycopy(i10, (i13 + (i2 & 1)) * (i7 << 1) + (i1 & 1), i20, i13 * i3, i3);
		}

		return i20;
	}

	protected int choose(int i1, int i2) {
		return this.nextInt(2) == 0 ? i1 : i2;
	}

	protected int choose(int i1, int i2, int i3, int i4) {
		int i5 = this.nextInt(4);
		return i5 == 0 ? i1 : (i5 == 1 ? i2 : (i5 == 2 ? i3 : i4));
	}
}
