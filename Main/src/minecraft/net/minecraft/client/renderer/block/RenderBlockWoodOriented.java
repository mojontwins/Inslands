package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.level.tile.Block;

public class RenderBlockWoodOriented implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		int orientation = meta & 12;
		if (orientation == 12) {
			renderBlocks.uvRotateEast = 1;
			renderBlocks.uvRotateWest = 1;
			renderBlocks.uvRotateTop = 1;
			renderBlocks.uvRotateBottom = 1;
		} else if (orientation == 4) {
			renderBlocks.uvRotateSouth = 1;
			renderBlocks.uvRotateNorth = 1;
		}

		boolean res = RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
		renderBlocks.uvRotateSouth = 0;
		renderBlocks.uvRotateEast = 0;
		renderBlocks.uvRotateWest = 0;
		renderBlocks.uvRotateNorth = 0;
		renderBlocks.uvRotateTop = 0;
		renderBlocks.uvRotateBottom = 0;
		return res;
	}
}