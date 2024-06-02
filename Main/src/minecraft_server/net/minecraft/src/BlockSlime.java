package net.minecraft.src;

import com.mojang.minecraft.creative.CreativeTabs;

public class BlockSlime extends Block {

	protected BlockSlime(int id, int blockIndexInTexture, Material material) {
		super(id, blockIndexInTexture, material);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 106;
	}
	
	public int getRenderBlockPass() {
		return 2;
	}
}
