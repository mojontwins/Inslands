package net.minecraft.src;

import java.util.Random;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;

public class BlockWoodenSpikes extends Block {

	protected BlockWoodenSpikes(int id, int blockIndexInTexture) {
		super(id, blockIndexInTexture, Material.wood);
		
		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		float f5 = 0.0625F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f5), (double)y, (double)((float)z + f5), (double)((float)(x + 1) - f5), (double)((float)(y + 1) - f5), (double)((float)(z + 1) - f5));
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		float f5 = 0.0625F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f5), (double)y, (double)((float)z + f5), (double)((float)(x + 1) - f5), (double)(y + 1), (double)((float)(z + 1) - f5));
	}
	

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
	
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(entity instanceof EntityLiving) {
			entity.attackEntityFrom((Entity)null, 1);
		}
	}
	
	public int quantityDropped(Random rand) {
		return 5;
	}

	public int idDropped(int metadata, Random rand) {
		return Item.stick.shiftedIndex;
	}
	
	public boolean seeThrough() {
		return true; 
	}
}
