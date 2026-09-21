package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.level.tile.Block;

public class RenderBlockDirtPath implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 15.0F * (1 / 16.0F), 1.0F);
		return RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
	}
}