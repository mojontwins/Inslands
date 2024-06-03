package net.minecraft.src;

import java.util.Random;

public class BlockSnow extends Block {
	protected BlockSnow(int id, int blockIndex) {
		super(id, blockIndex, Material.snow);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickOnLoad(true);
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		int meta = blockAccess.getBlockMetadata(x, y, z) & 15;
		float height = (float)(meta + 1) / 16.0F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, height, 1.0F);
	}
	
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		/*
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
		*/
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y - 1, z)];
		if(block == null) return false;
		if(!block.isOpaqueCube()) return false;
		if(block == Block.sand ) return false;
		return world.getBlockMaterial(x, y - 1, z).getIsSolid();
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		this.canSnowStay(world, x, y, z);
	}

	private boolean canSnowStay(World world, int x, int y, int z) {
		if(!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public void harvestBlock(World world, int x, int y, int z, int metadata) {
		int i6 = Item.snowball.shiftedIndex;
		float f7 = 0.7F;
		double d8 = (double)(world.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		double d10 = (double)(world.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		double d12 = (double)(world.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		EntityItem entityItem14 = new EntityItem(world, (double)x + d8, (double)y + d10, (double)z + d12, new ItemStack(i6));
		entityItem14.delayBeforeCanPickup = 10;
		world.entityJoinedWorld(entityItem14);
		world.setBlockWithNotify(x, y, z, 0);
	}

	public int idDropped(int metadata, Random rand) {
		return Item.snowball.shiftedIndex;
	}

	public int quantityDropped(Random rand) {
		return 0;
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		if(world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

		}

	/*
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		Material material6 = blockAccess.getBlockMaterial(x, y, z);
		return side == 1 ? true : (material6 == this.material ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side));
	}
	*/

	public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5) {
		var5.setInSnow();
	}
}
