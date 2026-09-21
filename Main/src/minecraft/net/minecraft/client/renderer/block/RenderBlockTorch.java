package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.level.tile.Block;

public class RenderBlockTorch implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		int i5 = renderBlocks.blockAccess.getBlockMetadata(i2, i3, i4);
		Tessellator tessellator6 = Tessellator.instance;
		tessellator6.setBrightness(block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4));
		tessellator6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d7 = (double)0.4F;
		double d9 = 0.5D - d7;
		double d11 = (double)0.2F;
		if(i5 == 1) {
			RenderBlockUtil.renderTorchAtAngle(renderBlocks, block1, (double)i2 - d9, (double)i3 + d11, (double)i4, -d7, 0.0D);
		} else if(i5 == 2) {
			RenderBlockUtil.renderTorchAtAngle(renderBlocks, block1, (double)i2 + d9, (double)i3 + d11, (double)i4, d7, 0.0D);
		} else if(i5 == 3) {
			RenderBlockUtil.renderTorchAtAngle(renderBlocks, block1, (double)i2, (double)i3 + d11, (double)i4 - d9, 0.0D, -d7);
		} else if(i5 == 4) {
			RenderBlockUtil.renderTorchAtAngle(renderBlocks, block1, (double)i2, (double)i3 + d11, (double)i4 + d9, 0.0D, d7);
		} else {
			RenderBlockUtil.renderTorchAtAngle(renderBlocks, block1, (double)i2, (double)i3, (double)i4, 0.0D, 0.0D);
		}

		return true;
	}

	@Override
	public void renderBlockOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		tes.setNormal(0.0F, -1.0F, 0.0F);
		RenderBlockUtil.renderTorchAtAngle(renderBlocks, block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
		tes.draw();
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}