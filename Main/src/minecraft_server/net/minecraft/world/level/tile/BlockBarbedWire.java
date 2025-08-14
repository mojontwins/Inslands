package net.minecraft.world.level.tile;

import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockBarbedWire extends Block {
	protected BlockBarbedWire(int id, int blockIndexInTexture) {
		super(id, blockIndexInTexture, Material.iron);
		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
        int i = MathHelper.floor_double((double)((entityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        switch (i) {
        	case 0: world.setBlockMetadata(x, y, z, 2); break;
        	case 1: world.setBlockMetadata(x, y, z, 5); break;
        	case 2: world.setBlockMetadata(x, y, z, 3); break;
        	case 3: world.setBlockMetadata(x, y, z, 4); break;
        }
    }
	
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 101;
	}
	
	public int quantityDropped(Random rand) {
		return 1;
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
		return null;
	}
	
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.setInWeb();
		entity.setInTrap();
	}
	
	public boolean seeThrough() {
		return true; 
	}
}
