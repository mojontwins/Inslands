package net.minecraft.world.level.tile;

import java.util.ArrayList;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AxisAlignedBB;

public class BlockSofaPiece extends BlockSeat {

	public BlockSofaPiece(int id) {
		super(id, Material.wood);
		this.setLightOpacity(255);
		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 10;
	}
	
	@Override
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB aabb, ArrayList<AxisAlignedBB> collidingBoundingBoxes) {
		int meta = world.getBlockMetadata(x, y, z);
		meta &= 7;
		
		if(meta == 0) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
		} else if(meta == 1) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
		} else if(meta == 2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
		} else if(meta == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
		}
		
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
		int i = MathHelper.floor_double((double)((placer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		int meta = world.getBlockMetadata(x, y, z);

		switch (i) {
			case 0: meta |= 2; break;
			case 1: meta |= 1; break;
			case 2: meta |= 3; break;
			case 3: meta |= 0; break;
		}
		
		world.setBlockMetadataWithNotify(x, y, z, meta);
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		int back = 0;
		switch (metadata & 3) {
		case 0:back = 5; break;
		case 1:back = 4; break;
		case 2:back = 3; break;
		case 3:back = 2; break;
		}
		return side == 0 || side == back ? 4 : 64;
	}
}
