package net.minecraft.src;

public class GenLayerRiverInit extends GenLayer {
	public GenLayerRiverInit(long j1, GenLayer genLayer3) {
		super(j1);
		this.parent = genLayer3;
	}

	public int[] getInts(int i1, int i2, int i3, int i4) {
		int[] i5 = this.parent.getInts(i1, i2, i3, i4);
		int[] i6 = IntCache.getIntCache(i3 * i4);

		for(int i7 = 0; i7 < i4; ++i7) {
			for(int i8 = 0; i8 < i3; ++i8) {
				this.initChunkSeed((long)(i8 + i1), (long)(i7 + i2));
				i6[i8 + i7 * i3] = i5[i8 + i7 * i3] > 0 ? this.nextInt(2) + 2 : 0;
			}
		}

		return i6;
	}
}
