package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.level.tile.Block;

public class RenderBlockPlant implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		return RenderBlockUtil.drawCrossedSquares(renderBlocks, block, x, y, z);
	}

	@Override
	public void renderBlockOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		tes.setNormal(0.0F, -1.0F, 0.0F);
		RenderBlockUtil.drawCrossedSquares(renderBlocks, block, meta, -0.5D, -0.5D, -0.5D);
		tes.draw();
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}