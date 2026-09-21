package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.level.tile.Block;

public class RenderBlockThinFeature implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		long l = (long)(x * 0x2fc20f) ^ (long)z * 0x6ebfff5L ^ (long)y;
		l = l * l * 0x285b825L + l * 11L;
		int i1 = (int)(l >> 16 & 7L);
		renderBlocks.uvRotateTop = i1 & 3;
		RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
		renderBlocks.uvRotateTop = 0;
		return true;
	}
}