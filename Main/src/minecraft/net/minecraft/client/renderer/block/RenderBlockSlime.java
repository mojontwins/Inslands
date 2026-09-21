package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.level.tile.Block;

public class RenderBlockSlime implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		// This renderer sends different quads depending on the active renderPass
		
		if(renderBlocks.activeRenderPass == 0) {
			// Set dimensions of inner block
			block.setBlockBounds(0.125F, 0.125F, 0.125F, 0.875F, 0.875F, 0.875F);
			
			// Render inner box
			boolean wasRendered = RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
			
			// Set dimensions of outer block
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		
			return wasRendered;
		} else {
			// Render outer box
			return RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
		}
	}
}