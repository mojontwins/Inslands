package net.minecraft.world.level.levelgen.city;

import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.tile.Block;

public class BuildingGraveyard extends Building {
	int y0 = 64;
	
	public BuildingGraveyard(int y0) {
		this.y0 = y0;
	}
	
	public void generate(int y0, Chunk chunk) {
		byte[] data = chunk.blocks;
		byte[] meta = chunk.data;
		
		// Layer 0

		data[0 | y0] = 2;
		data[2048 | y0] = 2;
		data[4096 | y0] = 2;
		data[6144 | y0] = 2;
		data[8192 | y0] = 2;
		data[10240 | y0] = 2;
		data[12288 | y0] = 2;
		data[14336 | y0] = 2;
		data[16384 | y0] = 2;
		data[18432 | y0] = 2;
		data[20480 | y0] = 2;
		data[22528 | y0] = 2;
		data[24576 | y0] = 2;
		data[26624 | y0] = 2;
		data[28672 | y0] = 2;
		data[30720 | y0] = 2;
		data[128 | y0] = 2;
		data[2176 | y0] = 2;
		data[4224 | y0] = 2;
		data[6272 | y0] = 2;
		data[8320 | y0] = 2;
		data[10368 | y0] = 2;
		data[12416 | y0] = 2;
		data[14464 | y0] = 2;
		data[16512 | y0] = 2;
		data[18560 | y0] = 2;
		data[20608 | y0] = 2;
		data[22656 | y0] = 2;
		data[24704 | y0] = 2;
		data[26752 | y0] = 2;
		data[28800 | y0] = 2;
		data[30848 | y0] = 2;
		data[256 | y0] = 2;
		data[2304 | y0] = 2;
		data[4352 | y0] = 2;
		data[6400 | y0] = 2;
		data[8448 | y0] = 2;
		data[10496 | y0] = 2;
		data[12544 | y0] = 2;
		data[14592 | y0] = 2;
		data[16640 | y0] = 2;
		data[18688 | y0] = 2;
		data[20736 | y0] = 2;
		data[22784 | y0] = 2;
		data[24832 | y0] = 2;
		data[26880 | y0] = 2;
		data[28928 | y0] = 2;
		data[30976 | y0] = 2;
		data[384 | y0] = 2;
		data[2432 | y0] = 2;
		data[4480 | y0] = 2;
		data[6528 | y0] = 2;
		data[8576 | y0] = 2;
		data[10624 | y0] = 2;
		data[12672 | y0] = 2;
		data[14720 | y0] = 2;
		data[16768 | y0] = 2;
		data[18816 | y0] = 2;
		data[20864 | y0] = 2;
		data[22912 | y0] = 2;
		data[24960 | y0] = 2;
		data[27008 | y0] = 2;
		data[29056 | y0] = 2;
		data[31104 | y0] = 2;
		data[512 | y0] = 2;
		data[2560 | y0] = 2;
		data[4608 | y0] = 2;
		data[6656 | y0] = 2;
		data[8704 | y0] = 2;
		data[10752 | y0] = 2;
		data[12800 | y0] = 2;
		data[14848 | y0] = 2;
		data[16896 | y0] = 2;
		data[18944 | y0] = 2;
		data[20992 | y0] = 2;
		data[23040 | y0] = 2;
		data[25088 | y0] = 2;
		data[27136 | y0] = 2;
		data[29184 | y0] = 2;
		data[31232 | y0] = 2;
		data[640 | y0] = 2;
		data[2688 | y0] = 2;
		data[4736 | y0] = 2;
		data[6784 | y0] = 2;
		data[8832 | y0] = 2;
		data[10880 | y0] = 2;
		data[12928 | y0] = 2;
		data[14976 | y0] = 2;
		data[17024 | y0] = 2;
		data[19072 | y0] = 2;
		data[21120 | y0] = 2;
		data[23168 | y0] = 2;
		data[25216 | y0] = 2;
		data[27264 | y0] = 2;
		data[29312 | y0] = 2;
		data[31360 | y0] = 2;
		data[768 | y0] = 2;
		data[2816 | y0] = 2;
		data[4864 | y0] = 2;
		data[6912 | y0] = 2;
		data[8960 | y0] = 2;
		data[11008 | y0] = 2;
		data[13056 | y0] = 2;
		data[15104 | y0] = 2;
		data[17152 | y0] = 2;
		data[19200 | y0] = 2;
		data[21248 | y0] = 2;
		data[23296 | y0] = 2;
		data[25344 | y0] = 2;
		data[27392 | y0] = 2;
		data[29440 | y0] = 2;
		data[31488 | y0] = 2;
		data[896 | y0] = 2;
		data[2944 | y0] = 2;
		data[4992 | y0] = 2;
		data[7040 | y0] = 2;
		data[9088 | y0] = 2;
		data[11136 | y0] = 2;
		data[13184 | y0] = 2;
		data[15232 | y0] = 2;
		data[17280 | y0] = 2;
		data[19328 | y0] = 2;
		data[21376 | y0] = 2;
		data[23424 | y0] = 2;
		data[25472 | y0] = 2;
		data[27520 | y0] = 2;
		data[29568 | y0] = 2;
		data[31616 | y0] = 2;
		data[1024 | y0] = 2;
		data[3072 | y0] = 2;
		data[5120 | y0] = 2;
		data[7168 | y0] = 2;
		data[9216 | y0] = 2;
		data[11264 | y0] = 2;
		data[13312 | y0] = 2;
		data[15360 | y0] = 2;
		data[17408 | y0] = 2;
		data[19456 | y0] = 2;
		data[21504 | y0] = 2;
		data[23552 | y0] = 2;
		data[25600 | y0] = 2;
		data[27648 | y0] = 2;
		data[29696 | y0] = 2;
		data[31744 | y0] = 2;
		data[1152 | y0] = 2;
		data[3200 | y0] = 2;
		data[5248 | y0] = 2;
		data[7296 | y0] = 2;
		data[9344 | y0] = 2;
		data[11392 | y0] = 2;
		data[13440 | y0] = 2;
		data[15488 | y0] = 2;
		data[17536 | y0] = 2;
		data[19584 | y0] = 2;
		data[21632 | y0] = 2;
		data[23680 | y0] = 2;
		data[25728 | y0] = 2;
		data[27776 | y0] = 2;
		data[29824 | y0] = 2;
		data[31872 | y0] = 2;
		data[1280 | y0] = 2;
		data[3328 | y0] = 2;
		data[5376 | y0] = 2;
		data[7424 | y0] = 2;
		data[9472 | y0] = 2;
		data[11520 | y0] = 2;
		data[13568 | y0] = 2;
		data[15616 | y0] = 2;
		data[17664 | y0] = 2;
		data[19712 | y0] = 2;
		data[21760 | y0] = 2;
		data[23808 | y0] = 2;
		data[25856 | y0] = 2;
		data[27904 | y0] = 2;
		data[29952 | y0] = 2;
		data[32000 | y0] = 2;
		data[1408 | y0] = 2;
		data[3456 | y0] = 2;
		data[5504 | y0] = 2;
		data[7552 | y0] = 2;
		data[9600 | y0] = 2;
		data[11648 | y0] = 2;
		data[13696 | y0] = 2;
		data[15744 | y0] = 2;
		data[17792 | y0] = 2;
		data[19840 | y0] = 2;
		data[21888 | y0] = 2;
		data[23936 | y0] = 2;
		data[25984 | y0] = 2;
		data[28032 | y0] = 2;
		data[30080 | y0] = 2;
		data[32128 | y0] = 2;
		data[1536 | y0] = 2;
		data[3584 | y0] = 2;
		data[5632 | y0] = 2;
		data[7680 | y0] = 2;
		data[9728 | y0] = 2;
		data[11776 | y0] = 2;
		data[13824 | y0] = 2;
		data[15872 | y0] = 2;
		data[17920 | y0] = 2;
		data[19968 | y0] = 2;
		data[22016 | y0] = 2;
		data[24064 | y0] = 2;
		data[26112 | y0] = 2;
		data[28160 | y0] = 2;
		data[30208 | y0] = 2;
		data[32256 | y0] = 2;
		data[1664 | y0] = 2;
		data[3712 | y0] = 2;
		data[5760 | y0] = 2;
		data[7808 | y0] = 2;
		data[9856 | y0] = 2;
		data[11904 | y0] = 2;
		data[13952 | y0] = 2;
		data[16000 | y0] = 2;
		data[18048 | y0] = 2;
		data[20096 | y0] = 2;
		data[22144 | y0] = 2;
		data[24192 | y0] = 2;
		data[26240 | y0] = 2;
		data[28288 | y0] = 2;
		data[30336 | y0] = 2;
		data[32384 | y0] = 2;
		data[1792 | y0] = 2;
		data[3840 | y0] = 2;
		data[5888 | y0] = 2;
		data[7936 | y0] = 2;
		data[9984 | y0] = 2;
		data[12032 | y0] = 2;
		data[14080 | y0] = 2;
		data[16128 | y0] = 2;
		data[18176 | y0] = 2;
		data[20224 | y0] = 2;
		data[22272 | y0] = 2;
		data[24320 | y0] = 2;
		data[26368 | y0] = 2;
		data[28416 | y0] = 2;
		data[30464 | y0] = 2;
		data[32512 | y0] = 2;
		data[1920 | y0] = 2;
		data[3968 | y0] = 2;
		data[6016 | y0] = 2;
		data[8064 | y0] = 2;
		data[10112 | y0] = 2;
		data[12160 | y0] = 2;
		data[14208 | y0] = 2;
		data[16256 | y0] = 2;
		data[18304 | y0] = 2;
		data[20352 | y0] = 2;
		data[22400 | y0] = 2;
		data[24448 | y0] = 2;
		data[26496 | y0] = 2;
		data[28544 | y0] = 2;
		data[30592 | y0] = 2;
		data[32640 | y0] = 2;
		y0++;
		// Layer 1

		data[0 | y0] = 98;
		data[2048 | y0] = 98;
		data[4096 | y0] = 98;
		data[6144 | y0] = 98;
		data[8192 | y0] = 98;
		data[10240 | y0] = 98;
		data[12288 | y0] = 98;
		data[14336 | y0] = 98;
		data[16384 | y0] = 98;
		data[18432 | y0] = 98;
		data[20480 | y0] = 98;
		data[22528 | y0] = 98;
		data[24576 | y0] = 98;
		data[26624 | y0] = 98;
		data[28672 | y0] = 98;
		data[30720 | y0] = 98;
		data[128 | y0] = 98;
		data[30848 | y0] = 98;
		data[256 | y0] = 98;
		data[4352 | y0] = 67;
		meta[2 << 11 | 2 << 7 | y0] = 3;
		data[8448 | y0] = 67;
		meta[4 << 11 | 2 << 7 | y0] = 3;
		data[10496 | y0] = 31;
		data[12544 | y0] = 67;
		meta[6 << 11 | 2 << 7 | y0] = 3;
		data[16640 | y0] = 67;
		meta[8 << 11 | 2 << 7 | y0] = 3;
		data[20736 | y0] = 67;
		meta[10 << 11 | 2 << 7 | y0] = 3;
		data[24832 | y0] = 67;
		meta[12 << 11 | 2 << 7 | y0] = 3;
		data[28928 | y0] = 31;
		data[30976 | y0] = 98;
		data[384 | y0] = 98;
		data[4480 | y0] = 44;
		data[8576 | y0] = 44;
		data[12672 | y0] = 44;
		data[16768 | y0] = 44;
		data[18816 | y0] = 37;
		data[20864 | y0] = 44;
		data[24960 | y0] = 44;
		data[31104 | y0] = 98;
		data[512 | y0] = 98;
		data[2560 | y0] = 37;
		data[10752 | y0] = 31;
		data[14848 | y0] = 31;
		data[20992 | y0] = 31;
		data[23040 | y0] = 31;
		data[27136 | y0] = 31;
		data[31232 | y0] = 98;
		data[640 | y0] = 98;
		data[6784 | y0] = 31;
		data[12928 | y0] = 31;
		data[14976 | y0] = 31;
		data[17024 | y0] = 31;
		data[23168 | y0] = 31;
		data[27264 | y0] = 31;
		data[29312 | y0] = 31;
		data[31360 | y0] = 98;
		data[768 | y0] = 98;
		data[4864 | y0] = 31;
		data[8960 | y0] = 31;
		data[11008 | y0] = 67;
		meta[5 << 11 | 6 << 7 | y0] = 3;
		data[15104 | y0] = 67;
		meta[7 << 11 | 6 << 7 | y0] = 3;
		data[19200 | y0] = 67;
		meta[9 << 11 | 6 << 7 | y0] = 3;
		data[23296 | y0] = 67;
		meta[11 << 11 | 6 << 7 | y0] = 3;
		data[27392 | y0] = 67;
		meta[13 << 11 | 6 << 7 | y0] = 3;
		data[31488 | y0] = 98;
		data[896 | y0] = 98;
		data[4992 | y0] = 37;
		data[7040 | y0] = 31;
		data[11136 | y0] = 44;
		data[13184 | y0] = 31;
		data[15232 | y0] = 44;
		data[19328 | y0] = 44;
		data[21376 | y0] = 31;
		data[23424 | y0] = 44;
		data[27520 | y0] = 44;
		data[31616 | y0] = 98;
		data[1024 | y0] = 98;
		data[5120 | y0] = 31;
		data[7168 | y0] = 31;
		data[9216 | y0] = 37;
		data[11264 | y0] = 31;
		data[13312 | y0] = 31;
		data[17408 | y0] = 31;
		data[21504 | y0] = 31;
		data[23552 | y0] = 31;
		data[25600 | y0] = 31;
		data[29696 | y0] = 37;
		data[31744 | y0] = 98;
		data[1152 | y0] = 98;
		data[9344 | y0] = 31;
		data[13440 | y0] = 37;
		data[15488 | y0] = 31;
		data[23680 | y0] = 31;
		data[25728 | y0] = 31;
		data[27776 | y0] = 31;
		data[31872 | y0] = 98;
		data[1280 | y0] = 98;
		data[3328 | y0] = 31;
		data[5376 | y0] = 67;
		meta[2 << 11 | 10 << 7 | y0] = 3;
		data[9472 | y0] = 67;
		meta[4 << 11 | 10 << 7 | y0] = 3;
		data[11520 | y0] = 37;
		data[13568 | y0] = 67;
		meta[6 << 11 | 10 << 7 | y0] = 3;
		data[15616 | y0] = 31;
		data[17664 | y0] = 67;
		meta[8 << 11 | 10 << 7 | y0] = 3;
		data[21760 | y0] = 67;
		meta[10 << 11 | 10 << 7 | y0] = 3;
		data[32000 | y0] = 98;
		data[1408 | y0] = 98;
		data[5504 | y0] = 44;
		data[7552 | y0] = 37;
		data[9600 | y0] = 44;
		data[11648 | y0] = 31;
		meta[5 << 11 | 11 << 7 | y0] = 1;
		data[13696 | y0] = 44;
		data[17792 | y0] = 44;
		data[21888 | y0] = 44;
		data[25984 | y0] = 37;
		data[30080 | y0] = 31;
		data[32128 | y0] = 98;
		data[1536 | y0] = 98;
		data[3584 | y0] = 31;
		data[5632 | y0] = 31;
		data[7680 | y0] = 31;
		data[9728 | y0] = 31;
		data[13824 | y0] = 31;
		data[15872 | y0] = 31;
		data[17920 | y0] = 31;
		data[19968 | y0] = 31;
		data[24064 | y0] = 31;
		data[26112 | y0] = 31;
		data[32256 | y0] = 98;
		data[1664 | y0] = 98;
		data[9856 | y0] = 31;
		data[18048 | y0] = 31;
		data[22144 | y0] = 31;
		data[32384 | y0] = 98;
		data[1792 | y0] = 98;
		data[3840 | y0] = 31;
		data[7936 | y0] = 31;
		data[16128 | y0] = 31;
		data[18176 | y0] = 31;
		data[30464 | y0] = 37;
		data[32512 | y0] = 98;
		data[1920 | y0] = 98;
		data[3968 | y0] = 98;
		data[6016 | y0] = 98;
		data[8064 | y0] = 98;
		data[10112 | y0] = 98;
		data[12160 | y0] = 98;
		data[14208 | y0] = 98;
		data[20352 | y0] = 98;
		data[22400 | y0] = 98;
		data[24448 | y0] = 98;
		data[26496 | y0] = 98;
		data[28544 | y0] = 98;
		data[30592 | y0] = 98;
		data[32640 | y0] = 98;
		y0++;
		// Layer 2

		data[0 | y0] = 98;
		data[2048 | y0] = 85;
		data[4096 | y0] = 85;
		data[6144 | y0] = 85;
		data[8192 | y0] = 85;
		data[10240 | y0] = 85;
		data[12288 | y0] = 85;
		data[14336 | y0] = 85;
		data[16384 | y0] = 85;
		data[18432 | y0] = 85;
		data[20480 | y0] = 85;
		data[22528 | y0] = 85;
		data[24576 | y0] = 85;
		data[26624 | y0] = 85;
		data[28672 | y0] = 85;
		data[30720 | y0] = 98;
		data[128 | y0] = 85;
		data[30848 | y0] = 85;
		data[256 | y0] = 85;
		data[30976 | y0] = 85;
		data[384 | y0] = 85;
		data[31104 | y0] = 85;
		data[512 | y0] = 85;
		data[31232 | y0] = 85;
		data[640 | y0] = 85;
		data[31360 | y0] = 85;
		data[768 | y0] = 85;
		data[31488 | y0] = 85;
		data[896 | y0] = 85;
		data[31616 | y0] = 85;
		data[1024 | y0] = 85;
		data[31744 | y0] = 85;
		data[1152 | y0] = 85;
		data[31872 | y0] = 85;
		data[1280 | y0] = 85;
		data[32000 | y0] = 85;
		data[1408 | y0] = 85;
		data[32128 | y0] = 85;
		data[1536 | y0] = 85;
		data[32256 | y0] = 85;
		data[1664 | y0] = 85;
		data[32384 | y0] = 85;
		data[1792 | y0] = 85;
		data[32512 | y0] = 85;
		data[1920 | y0] = 98;
		data[3968 | y0] = 85;
		data[6016 | y0] = 85;
		data[8064 | y0] = 85;
		data[10112 | y0] = 85;
		data[12160 | y0] = 85;
		data[14208 | y0] = 98;
		data[20352 | y0] = 98;
		data[22400 | y0] = 85;
		data[24448 | y0] = 85;
		data[26496 | y0] = 85;
		data[28544 | y0] = 85;
		data[30592 | y0] = 85;
		data[32640 | y0] = 98;
		y0++;
		// Layer 3

		data[14208 | y0] = 98;
		data[20352 | y0] = 98;
	}
	
	public void populate(World world, Random rand, int chunkX, int chunkZ, Chunk chunk) {
		int x0 = chunkX * 16;
		int z0 = chunkZ * 16;
		
		// Chests
		int numChests = 1 + rand.nextInt(2);
		int numSpawners = 2 + rand.nextInt(2);
		
		for(int i = 0; i < numChests; i ++) {
			int row = rand.nextInt(3);
			int x = this.getXforRow(rand, row);		
			this.addChest(world, rand, x0 + x, this.y0, z0 + 11 - row * 4);
		}
		
		int x, row;
		for(int i = 0; i < numSpawners; i ++) {
			do {
				row = rand.nextInt(3);
				x = this.getXforRow(rand, row);
			} while (world.getblockID(x0 + x, this.y0, z0 + 11 - row * 4) != Block.grass.blockID);
			this.addSpawner(world, rand, x0 + x, this.y0, z0 + 11 - row * 4, "Skeleton");
		}
	}
	
	public void placeChestAtRow(World world, Random rand, int x0, int z0, int row) {
		int x = this.getXforRow(rand, row);
		
		this.addChest(world, rand, x0 + x, this.y0, z0 + 11 - row * 4);
	}
	
	public int getXforRow(Random rand, int row) {
		int x = 0;
		switch(row) {
			case 0: x = 2 + 2 * rand.nextInt(6); break;
			case 1: x = 5 + 2 * rand.nextInt(5); break;
			case 2: x = 2 + 2 * rand.nextInt(5); break;
		}
		return x;
	}
	
	protected ItemStack getTreasure(int level, Random rand) {
		// level should be rand 0-9
		
		if(level < 4) {
			switch(rand.nextInt(6)) {
			case 0:
				return new ItemStack(Item.ingotIron, rand.nextInt(4) + 1);
			case 1:
				return new ItemStack(Item.bucketEmpty);
			case 2:
				return new ItemStack(Item.bread);
			case 3:
			case 5:
			default:
				return new ItemStack(Block.torchWood, rand.nextInt(16) + 1);
			case 4:
				return new ItemStack(Item.wheat, rand.nextInt(3) + 1);
			}
		} else if(level < 9) {
			switch(rand.nextInt(8)) {
			case 0:
			case 1:
			case 2:
				return this.getTreasure(1, rand);
			case 3:
			case 7:
			default:
				return new ItemStack(Item.potionPoison);
			case 4:
				return new ItemStack(Item.potionSlowness);
			case 5:
				return new ItemStack(Item.potionAutoHealing);
			case 6:
				return new ItemStack(Item.potionInstantDamage);
			}
		} else {
			switch(rand.nextInt(8)) {
			case 0:
			case 1:
			case 2:
				return this.getTreasure(2, rand);
			case 3:
			case 7:
			default:
				return new ItemStack(Item.diamond);
			case 4:
				return new ItemStack(Item.appleGold);
			case 5:
				return new ItemStack(Block.sponge);
			case 6:
				return new ItemStack(Item.saddle);
			}
		} 
	}
}
