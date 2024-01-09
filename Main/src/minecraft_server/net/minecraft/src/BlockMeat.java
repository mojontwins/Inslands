package net.minecraft.src;

import java.util.Random;

public class BlockMeat extends BlockEntity {

	protected BlockMeat(int id, int blockIndexInTexture, Material material) {
		super(id, blockIndexInTexture, material);
	}

	@Override
	protected EntityBlockEntity getBlockEntity(World world) {
		return new EntityMeatBlock(world);
	}
	
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		double xd = x + 0.5D;
		double yd = y + 1.0D;
		double zd = z + 0.5D;
		world.spawnParticle("status_effect", xd, yd, zd, 0.7578125D, 0.44921875D, 0.44921875D);
	}

}
