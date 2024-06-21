package net.minecraft.src;

public class GenLayerRiverMix extends GenLayer {
	private GenLayer field_35033_b;
	private GenLayer field_35034_c;

	public GenLayerRiverMix(long j1, GenLayer genLayer3, GenLayer genLayer4) {
		super(j1);
		this.field_35033_b = genLayer3;
		this.field_35034_c = genLayer4;
	}

	public void initWorldGenSeed(long j1) {
		this.field_35033_b.initWorldGenSeed(j1);
		this.field_35034_c.initWorldGenSeed(j1);
		super.initWorldGenSeed(j1);
	}

	public int[] getInts(int i1, int i2, int i3, int i4) {
		int[] i5 = this.field_35033_b.getInts(i1, i2, i3, i4);
		int[] i6 = this.field_35034_c.getInts(i1, i2, i3, i4);
		int[] i7 = IntCache.getIntCache(i3 * i4);

		for(int i8 = 0; i8 < i3 * i4; ++i8) {
			if(i5[i8] == BiomeGenBase.ocean.biomeID) {
				i7[i8] = i5[i8];
			} else if(i6[i8] >= 0) {
				if(i5[i8] == BiomeGenBase.icePlains.biomeID) {
					i7[i8] = BiomeGenBase.frozenRiver.biomeID;
				} else if(i5[i8] != BiomeGenBase.mushroomIsland.biomeID && i5[i8] != BiomeGenBase.mushroomIslandShore.biomeID) {
					i7[i8] = i6[i8];
				} else {
					i7[i8] = BiomeGenBase.mushroomIslandShore.biomeID;
				}
			} else {
				i7[i8] = i5[i8];
			}
		}

		return i7;
	}
}
