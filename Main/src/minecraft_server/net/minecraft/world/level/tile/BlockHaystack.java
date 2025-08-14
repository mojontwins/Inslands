package net.minecraft.world.level.tile;

import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockHaystack extends Block {
	
	// This block will be rendered using renderAxisOriented, which means that:
	// meta = 0 -> block is placed vertically, with ends on top/bottom and side in the rest of faces.
	// meta = 2, 3, 4, 5 -> block is placed horizontally, facing x or z.

	protected BlockHaystack(int id, int blockIndexInTexture) {
		super(id, blockIndexInTexture, Material.grass);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
	
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		// If meta == 0 -> side 0, 1 == ends, rest = sides.
		if(metadata == 0) return (side <= 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);		
		
		// otherwise, custom renderer will ask for side == 0 meaning sides, side == 1 meaning ends.
		return this.blockIndexInTexture + side;
	}
	
	public int getBlockTextureFromSide(int side) {
		return this.getBlockTextureFromSideAndMetadata(side, 0);
	}
	
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		// If side == 0 or side == 1 it will set metadata = 0, as in vanilla (vertical)
		// Otherwise, it will set metadata to side (horizontal).
		world.setBlockMetadata(x, y, z, side <= 1 ? 0 : side);
	}
    
	public boolean renderAsNormalBlock() {
		return true;
	}
	
	public boolean isOpaqueCube() {
		return true;
	}

	public int getRenderType() {
		return 107;
	}
	
	public boolean canGrowMushrooms() {
		return true;
	}
}
