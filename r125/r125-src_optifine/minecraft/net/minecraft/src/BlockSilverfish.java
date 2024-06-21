package net.minecraft.src;

import java.util.Random;

public class BlockSilverfish extends Block {
	public BlockSilverfish(int i1) {
		super(i1, 1, Material.clay);
		this.setHardness(0.0F);
	}

	public void harvestBlock(World world1, EntityPlayer entityPlayer2, int i3, int i4, int i5, int i6) {
		super.harvestBlock(world1, entityPlayer2, i3, i4, i5, i6);
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return i2 == 1 ? Block.cobblestone.blockIndexInTexture : (i2 == 2 ? Block.stoneBrick.blockIndexInTexture : Block.stone.blockIndexInTexture);
	}

	public void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4, int i5) {
		if(!world1.isRemote) {
			EntitySilverfish entitySilverfish6 = new EntitySilverfish(world1);
			entitySilverfish6.setLocationAndAngles((double)i2 + 0.5D, (double)i3, (double)i4 + 0.5D, 0.0F, 0.0F);
			world1.spawnEntityInWorld(entitySilverfish6);
			entitySilverfish6.spawnExplosionParticle();
		}

		super.onBlockDestroyedByPlayer(world1, i2, i3, i4, i5);
	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	public static boolean getPosingIdByMetadata(int i0) {
		return i0 == Block.stone.blockID || i0 == Block.cobblestone.blockID || i0 == Block.stoneBrick.blockID;
	}

	public static int getMetadataForBlockType(int i0) {
		return i0 == Block.cobblestone.blockID ? 1 : (i0 == Block.stoneBrick.blockID ? 2 : 0);
	}

	protected ItemStack createStackedBlock(int i1) {
		Block block2 = Block.stone;
		if(i1 == 1) {
			block2 = Block.cobblestone;
		}

		if(i1 == 2) {
			block2 = Block.stoneBrick;
		}

		return new ItemStack(block2);
	}
}
