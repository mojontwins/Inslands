package com.misc.aether;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockBreakable;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.sentient.EntityAmazon;
import net.minecraft.world.level.material.Material;

public class BlockTrap extends BlockBreakable {
	public static int sprBronze = 10*16+14;
	public static int sprSilver = 16;	// TODO: Now it is cobblestone. Change
	public static int sprGold = 16;

	public BlockTrap(int blockID) {
		super(blockID, sprBronze, Material.rock, false);
		this.setTickOnLoad(true);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public int getRenderBlockPass() {
		return 1;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return j == 2 ? sprGold : (j == 1 ? sprSilver : sprBronze);
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		if(entity instanceof EntityPlayer) {
			world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "aether.sound.other.dungeontrap.activateTrap", 1.0F, 1.0F);
			int x = MathHelper.floor_double((double)i);
			int y = MathHelper.floor_double((double)j);
			int z = MathHelper.floor_double((double)k);
			switch(world.getBlockMetadata(i, j, k)) {
			case 0:
				//EntitySentry entityvalk1 = new EntitySentry(world);
				EntityAmazon entityvalk1 = new EntityAmazon(world);
				entityvalk1.setAmazonAngry(true);
				
				entityvalk1.setPosition((double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D);
				world.spawnEntityInWorld(entityvalk1);
				break;
			case 1:
				// EntityValkyrie entityvalk = new EntityValkyrie(world);
				EntityAmazon entityvalk = new EntityAmazon(world);
				entityvalk.setAmazonAngry(true);
				
				entityvalk.setPosition((double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D);
				world.spawnEntityInWorld(entityvalk);
			}

			world.setBlockAndMetadataWithNotify(i, j, k, Block.obsidian.blockID, world.getBlockMetadata(i, j, k));
		}

	}

	protected int damageDropped(int i) {
		return i;
	}
}
