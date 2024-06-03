package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockStairs extends Block {
	private Block modelBlock;

	protected BlockStairs(int id, Block modelBlock) {
		super(id, modelBlock.blockIndexInTexture, modelBlock.blockMaterial);
		this.modelBlock = modelBlock;
		this.setHardness(modelBlock.blockHardness);
		this.setResistance(modelBlock.blockResistance / 3.0F);
		this.setStepSound(modelBlock.stepSound);
		this.setLightOpacity(255);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return super.getCollisionBoundingBoxFromPool(world1, i2, i3, i4);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 10;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return super.shouldSideBeRendered(blockAccess, x, y, z, side);
	}

	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB aabb, ArrayList<AxisAlignedBB> collidingBoundingBoxes) {
		int i7 = world.getBlockMetadata(x, y, z);
		if(i7 == 0) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
		} else if(i7 == 1) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
		} else if(i7 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
		} else if(i7 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		this.modelBlock.randomDisplayTick(world, x, y, z, rand);
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.modelBlock.onBlockClicked(world, x, y, z, entityPlayer);
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
		this.modelBlock.onBlockDestroyedByPlayer(world, x, y, z, metadata);
	}

	public float getBlockBrightness(IBlockAccess blockAccess, int x, int y, int z) {
		return this.modelBlock.getBlockBrightness(blockAccess, x, y, z);
	}

	public float getExplosionResistance(Entity entity) {
		return this.modelBlock.getExplosionResistance(entity);
	}

	public int getRenderBlockPass() {
		return this.modelBlock.getRenderBlockPass();
	}

	public int idDropped(int metadata, Random rand) {
		return this.modelBlock.idDropped(metadata, rand);
	}

	public int quantityDropped(Random rand) {
		return this.modelBlock.quantityDropped(rand);
	}

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		return this.modelBlock.getBlockTextureFromSideAndMetadata(side, metadata);
	}

	public int getBlockTextureFromSide(int side) {
		return this.modelBlock.getBlockTextureFromSide(side);
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return this.modelBlock.getBlockTexture(blockAccess, x, y, z, side);
	}

	public int tickRate() {
		return this.modelBlock.tickRate();
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return this.modelBlock.getSelectedBoundingBoxFromPool(world1, i2, i3, i4);
	}

	public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3D velocityVector) {
		this.modelBlock.velocityToAddToEntity(world, x, y, z, entity, velocityVector);
	}

	public boolean isCollidable() {
		return this.modelBlock.isCollidable();
	}

	public boolean canCollideCheck(int metadata, boolean z2) {
		return this.modelBlock.canCollideCheck(metadata, z2);
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return this.modelBlock.canPlaceBlockAt(world, x, y, z);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		this.onNeighborBlockChange(world, x, y, z, 0);
		this.modelBlock.onBlockAdded(world, x, y, z);
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		this.modelBlock.onBlockRemoval(world, x, y, z);
	}

	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance) {
		this.modelBlock.dropBlockAsItemWithChance(world, x, y, z, metadata, chance);
	}

	public void dropBlockAsItem(World world, int x, int y, int z, int metadata) {
		this.modelBlock.dropBlockAsItem(world, x, y, z, metadata);
	}

	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		this.modelBlock.onEntityWalking(world, x, y, z, entity);
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		this.modelBlock.updateTick(world, x, y, z, rand);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		return this.modelBlock.blockActivated(world, x, y, z, entityPlayer);
	}

	public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
		this.modelBlock.onBlockDestroyedByExplosion(world, x, y, z);
		}

	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, Entity par5EntityLiving) {
		int i = MathHelper.floor_double((double)((par5EntityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

		switch (i) {
			case 0: par1World.setBlockMetadataWithNotify(par2, par3, par4, 2); break;
			case 1: par1World.setBlockMetadataWithNotify(par2, par3, par4, 1); break;
			case 2: par1World.setBlockMetadataWithNotify(par2, par3, par4, 3); break;
			case 3: par1World.setBlockMetadataWithNotify(par2, par3, par4, 0); break;
		}
	}
}
