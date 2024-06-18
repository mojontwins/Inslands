package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenStronghold extends MapGenStructure {
	private BiomeGenBase[] allowedBiomeGenBases = new BiomeGenBase[]{BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.desertHills, BiomeGenBase.forestHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.jungle, BiomeGenBase.jungleHills};
	private boolean ranBiomeCheck;
	private ChunkCoordIntPair[] structureCoords = new ChunkCoordIntPair[3];

	protected boolean canSpawnStructureAtCoords(int i1, int i2) {
		if(!this.ranBiomeCheck) {
			Random random3 = new Random();
			random3.setSeed(this.worldObj.getSeed());
			double d4 = random3.nextDouble() * Math.PI * 2.0D;

			for(int i6 = 0; i6 < this.structureCoords.length; ++i6) {
				double d7 = (1.25D + random3.nextDouble()) * 32.0D;
				int i9 = (int)Math.round(Math.cos(d4) * d7);
				int i10 = (int)Math.round(Math.sin(d4) * d7);
				ArrayList arrayList11 = new ArrayList();
				BiomeGenBase[] biomeGenBase12 = this.allowedBiomeGenBases;
				int i13 = biomeGenBase12.length;

				for(int i14 = 0; i14 < i13; ++i14) {
					BiomeGenBase biomeGenBase15 = biomeGenBase12[i14];
					arrayList11.add(biomeGenBase15);
				}

				ChunkPosition chunkPosition19 = this.worldObj.getWorldChunkManager().findBiomePosition((i9 << 4) + 8, (i10 << 4) + 8, 112, arrayList11, random3);
				if(chunkPosition19 != null) {
					i9 = chunkPosition19.x >> 4;
					i10 = chunkPosition19.z >> 4;
				} else {
					System.out.println("Placed stronghold in INVALID biome at (" + i9 + ", " + i10 + ")");
				}

				this.structureCoords[i6] = new ChunkCoordIntPair(i9, i10);
				d4 += Math.PI * 2D / (double)this.structureCoords.length;
			}

			this.ranBiomeCheck = true;
		}

		ChunkCoordIntPair[] chunkCoordIntPair16 = this.structureCoords;
		int i17 = chunkCoordIntPair16.length;

		for(int i5 = 0; i5 < i17; ++i5) {
			ChunkCoordIntPair chunkCoordIntPair18 = chunkCoordIntPair16[i5];
			if(i1 == chunkCoordIntPair18.chunkXPos && i2 == chunkCoordIntPair18.chunkZPos) {
				System.out.println(i1 + ", " + i2);
				return true;
			}
		}

		return false;
	}

	protected List func_40203_a() {
		ArrayList arrayList1 = new ArrayList();
		ChunkCoordIntPair[] chunkCoordIntPair2 = this.structureCoords;
		int i3 = chunkCoordIntPair2.length;

		for(int i4 = 0; i4 < i3; ++i4) {
			ChunkCoordIntPair chunkCoordIntPair5 = chunkCoordIntPair2[i4];
			if(chunkCoordIntPair5 != null) {
				arrayList1.add(chunkCoordIntPair5.getChunkPosition(64));
			}
		}

		return arrayList1;
	}

	protected StructureStart getStructureStart(int i1, int i2) {
		StructureStrongholdStart structureStrongholdStart3;
		for(structureStrongholdStart3 = new StructureStrongholdStart(this.worldObj, this.rand, i1, i2); structureStrongholdStart3.getComponents().isEmpty() || ((ComponentStrongholdStairs2)structureStrongholdStart3.getComponents().get(0)).portalRoom == null; structureStrongholdStart3 = new StructureStrongholdStart(this.worldObj, this.rand, i1, i2)) {
		}

		return structureStrongholdStart3;
	}
}
