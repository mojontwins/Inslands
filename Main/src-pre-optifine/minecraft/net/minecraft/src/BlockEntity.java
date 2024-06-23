package net.minecraft.src;

public abstract class BlockEntity extends Block {

	protected BlockEntity(int id, int blockIndexInTexture, Material material) {
		super(id, blockIndexInTexture, material);
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		
		EntityBlockEntity entity = world.getBlockEntityIfExists(x, y, z);
		if(entity == null) entity = this.getBlockEntity(world);
		world.setBlockEntity(x, y, z, entity);
		entity.setLocationAndAngles((double)x + 0.5D, (double)y - 0.5D, (double)z + 0.5D, world.rand.nextFloat() * 360.0F, 0.0F);
		entity.setTilePosition(x, y, z);
		world.entityJoinedWorld(entity);
		
	}
	
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		world.removeBlockEntity(x, y, z);
		world.updateEntityList();
	}
	
	protected abstract EntityBlockEntity getBlockEntity(World world);
}
