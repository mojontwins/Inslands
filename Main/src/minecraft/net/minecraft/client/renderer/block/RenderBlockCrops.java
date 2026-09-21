package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.level.tile.Block;

public class RenderBlockCrops implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		Tessellator tessellator5 = Tessellator.instance;
		tessellator5.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		tessellator5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		RenderBlockUtil.renderBlockCropsImpl(renderBlocks, block, renderBlocks.blockAccess.getBlockMetadata(x, y, z), (double)x, (double)((float)y - 0.0625F), (double)z);
		return true;
	}

	@Override
	public void renderBlockOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		tes.setNormal(0.0F, -1.0F, 0.0F);
		RenderBlockUtil.renderBlockCropsImpl(renderBlocks, block, meta, -0.5D, -0.5D, -0.5D);
		tes.draw();
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}