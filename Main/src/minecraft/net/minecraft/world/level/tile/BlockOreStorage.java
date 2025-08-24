package net.minecraft.world.level.tile;

import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockOreStorage extends Block {
	public BlockOreStorage(int i1, int i2) {
		super(i1, Material.iron);
		this.blockIndexInTexture = i2;
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int getBlockTextureFromSide(int side) {
		int tex = this.blockIndexInTexture;
		
		if(this.blockID == Block.blockGold.blockID ||
				this.blockID == Block.blockGold.blockID ||
				this.blockID == Block.blockDiamond.blockID) {
			tex = side == 0 ? tex + 32 : side == 1 ? tex : tex + 16;
		}
		
		return tex;
	}
}
