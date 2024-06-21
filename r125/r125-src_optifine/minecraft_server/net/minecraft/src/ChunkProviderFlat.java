package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ChunkProviderFlat implements IChunkProvider {
	private World worldObj;
	private Random random;
	private final boolean useStructures;
	private MapGenVillage villageGen = new MapGenVillage(1);

	public ChunkProviderFlat(World world1, long j2, boolean z4) {
		this.worldObj = world1;
		this.useStructures = z4;
		this.random = new Random(j2);
	}

	private void generate(byte[] b1) {
		int i2 = b1.length / 256;

		for(int i3 = 0; i3 < 16; ++i3) {
			for(int i4 = 0; i4 < 16; ++i4) {
				for(int i5 = 0; i5 < i2; ++i5) {
					int i6 = 0;
					if(i5 == 0) {
						i6 = Block.bedrock.blockID;
					} else if(i5 <= 2) {
						i6 = Block.dirt.blockID;
					} else if(i5 == 3) {
						i6 = Block.grass.blockID;
					}

					b1[i3 << 11 | i4 << 7 | i5] = (byte)i6;
				}
			}
		}

	}

	public Chunk loadChunk(int i1, int i2) {
		return this.provideChunk(i1, i2);
	}

	public Chunk provideChunk(int i1, int i2) {
		byte[] b3 = new byte[32768];
		this.generate(b3);
		Chunk chunk4 = new Chunk(this.worldObj, b3, i1, i2);
		if(this.useStructures) {
			this.villageGen.generate(this, this.worldObj, i1, i2, b3);
		}

		BiomeGenBase[] biomeGenBase5 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, i1 * 16, i2 * 16, 16, 16);
		byte[] b6 = chunk4.getBiomeArray();

		for(int i7 = 0; i7 < b6.length; ++i7) {
			b6[i7] = (byte)biomeGenBase5[i7].biomeID;
		}

		chunk4.generateSkylightMap();
		return chunk4;
	}

	public boolean chunkExists(int i1, int i2) {
		return true;
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		this.random.setSeed(this.worldObj.getSeed());
		long j4 = this.random.nextLong() / 2L * 2L + 1L;
		long j6 = this.random.nextLong() / 2L * 2L + 1L;
		this.random.setSeed((long)i2 * j4 + (long)i3 * j6 ^ this.worldObj.getSeed());
		if(this.useStructures) {
			this.villageGen.generateStructuresInChunk(this.worldObj, this.random, i2, i3);
		}

	}

	public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return true;
	}

	public List getPossibleCreatures(EnumCreatureType enumCreatureType1, int i2, int i3, int i4) {
		BiomeGenBase biomeGenBase5 = this.worldObj.getBiomeGenForCoords(i2, i4);
		return biomeGenBase5 == null ? null : biomeGenBase5.getSpawnableList(enumCreatureType1);
	}

	public ChunkPosition findClosestStructure(World world1, String string2, int i3, int i4, int i5) {
		return null;
	}
}
