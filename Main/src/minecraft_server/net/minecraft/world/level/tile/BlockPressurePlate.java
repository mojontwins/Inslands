package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumMobType;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AxisAlignedBB;

public class BlockPressurePlate extends Block {
	private EnumMobType triggerMobType;

	protected BlockPressurePlate(int id, int texId, EnumMobType mobType, Material material) {
		super(id, texId, material);
		this.triggerMobType = mobType;
		this.setTickOnLoad(true);
		float f5 = 0.0625F;
		this.setBlockBounds(f5, 0.0F, f5, 1.0F - f5, 0.03125F, 1.0F - f5);
		this.displayOnCreativeTab = CreativeTabs.tabRedstone;
	}

	public int tickRate() {
		return 20;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int i5) {
		boolean pressed = false;
		if(!world.isBlockNormalCube(x, y - 1, z)) {
			pressed = true;
		}

		if(pressed) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

	}

	public void updateTick(World world, int x, int y, int z, Random random5) {
		if(!world.isRemote) {
			if(world.getBlockMetadata(x, y, z) != 0) {
				this.setStateIfMobInteractsWithPlate(world, x, y, z);
			}
		}
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity5) {
		if(!world.isRemote) {
			if(world.getBlockMetadata(x, y, z) != 1) {
				this.setStateIfMobInteractsWithPlate(world, x, y, z);
			}
		}
	}

	private void setStateIfMobInteractsWithPlate(World world, int x, int y, int z) {
		boolean wasPressed = world.getBlockMetadata(x, y, z) == 1;
		boolean pressed = false;
		float f7 = 0.125F;
		List<Entity> validEntities = null;
		if(this.triggerMobType == EnumMobType.everything) {
			validEntities = world.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f7), (double)y, (double)((float)z + f7), (double)((float)(x + 1) - f7), (double)y + 0.25D, (double)((float)(z + 1) - f7)));
		}

		if(this.triggerMobType == EnumMobType.mobs) {
			validEntities = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f7), (double)y, (double)((float)z + f7), (double)((float)(x + 1) - f7), (double)y + 0.25D, (double)((float)(z + 1) - f7)));
		}

		if(this.triggerMobType == EnumMobType.players) {
			validEntities = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f7), (double)y, (double)((float)z + f7), (double)((float)(x + 1) - f7), (double)y + 0.25D, (double)((float)(z + 1) - f7)));
		}

		if(validEntities.size() > 0) {
			pressed = true;
		}
		
		if(pressed && !wasPressed) {
			world.setBlockMetadataWithNotify(x, y, z, 1);
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.markBlocksDirty(x, y, z, x, y, z);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
		}

		if(!pressed && wasPressed) {
			world.setBlockMetadataWithNotify(x, y, z, 0);
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.markBlocksDirty(x, y, z, x, y, z);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
		}

		if(pressed) {
			world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
		}

	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		int i5 = world.getBlockMetadata(x, y, z);
		if(i5 > 0) {
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		}

		super.onBlockRemoval(world, x, y, z);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		boolean wasPressed = world.getBlockMetadata(x, y, z) == 1;
		float f6 = 0.0625F;
		if(wasPressed) {
			this.setBlockBounds(f6, 0.0F, f6, 1.0F - f6, 0.03125F, 1.0F - f6);
		} else {
			this.setBlockBounds(f6, 0.0F, f6, 1.0F - f6, 0.0625F, 1.0F - f6);
		}

	}

	public boolean isPoweringTo(IBlockAccess world, int x, int y, int z, int i5) {
		return world.getBlockMetadata(x, y, z) > 0;
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int i5) {
		return world.getBlockMetadata(x, y, z) == 0 ? false : i5 == 1;
	}

	public boolean canProvidePower() {
		return true;
	}

	public void setBlockBoundsForItemRender() {
		float f1 = 0.5F;
		float f2 = 0.125F;
		float f3 = 0.5F;
		this.setBlockBounds(0.5F - f1, 0.5F - f2, 0.5F - f3, 0.5F + f1, 0.5F + f2, 0.5F + f3);
	}

	public int getMobilityFlag() {
		return 1;
	}
	
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return true;
	}
}
