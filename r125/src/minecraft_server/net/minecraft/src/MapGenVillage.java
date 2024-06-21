package net.minecraft.src;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MapGenVillage extends MapGenStructure {
	public static List villageSpawnBiomes = Arrays.asList(new BiomeGenBase[]{BiomeGenBase.plains, BiomeGenBase.desert});
	private final int terrainType;

	public MapGenVillage(int i1) {
		this.terrainType = i1;
	}

	protected boolean canSpawnStructureAtCoords(int i1, int i2) {
		byte b3 = 32;
		byte b4 = 8;
		int i5 = i1;
		int i6 = i2;
		if(i1 < 0) {
			i1 -= b3 - 1;
		}

		if(i2 < 0) {
			i2 -= b3 - 1;
		}

		int i7 = i1 / b3;
		int i8 = i2 / b3;
		Random random9 = this.worldObj.setRandomSeed(i7, i8, 10387312);
		i7 *= b3;
		i8 *= b3;
		i7 += random9.nextInt(b3 - b4);
		i8 += random9.nextInt(b3 - b4);
		if(i5 == i7 && i6 == i8) {
			boolean z10 = this.worldObj.getWorldChunkManager().areBiomesViable(i5 * 16 + 8, i6 * 16 + 8, 0, villageSpawnBiomes);
			if(z10) {
				return true;
			}
		}

		return false;
	}

	protected StructureStart getStructureStart(int i1, int i2) {
		return new StructureVillageStart(this.worldObj, this.rand, i1, i2, this.terrainType);
	}
}
