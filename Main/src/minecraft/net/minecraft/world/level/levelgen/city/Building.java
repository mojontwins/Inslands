package net.minecraft.world.level.levelgen.city;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.Chunk;

public class Building {
	public void generate(int y0, Chunk chunk) {}
	
	public void populate(World world, Random rand, int x0, int y0, Chunk chunk) {}
	
	public void addChest(World world, Random rand, int x, int y, int z) {
		world.setBlockWithNotify(x, y, z, Block.chest.blockID);
		TileEntityChest tileEntityChest = (TileEntityChest)world.getBlockTileEntity(x, y, z);
		
		if(tileEntityChest != null && tileEntityChest.getSizeInventory() > 0) {
			int ni = rand.nextInt(4) + 4;
			int level = rand.nextInt(10);
			
			for(int i = 0; i < ni; ++i) {
				ItemStack itemStack = this.getTreasure(level, rand);
				tileEntityChest.setInventorySlotContents(i, itemStack);
			}			
		}
	}
	
	public void addSpawner(World world, Random rand, int x, int y, int z, String mobId) {
		world.setBlockWithNotify(x, y, z, Block.mobSpawner.blockID);
		TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner)world.getBlockTileEntity(x, y, z);
		if(tileEntityMobSpawner != null) {
			tileEntityMobSpawner.setMobID(mobId);
		}
		
	}
	
	protected ItemStack getTreasure(int level, Random rand) {
		return new ItemStack(Item.ingotGold);
	}

}
