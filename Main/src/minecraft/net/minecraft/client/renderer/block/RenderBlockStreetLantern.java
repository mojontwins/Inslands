package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.level.tile.Block;

public class RenderBlockStreetLantern implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		// Add a torch for non broken street lanterns
		if(renderBlocks.activeRenderPass == 0) {
			if(block.blockID == Block.streetLantern.blockID) {
				Tessellator tessellator = Tessellator.instance;
				tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);		
				RenderBlockUtil.renderTorchAtAngle(renderBlocks, Block.torchWood, (double)x, (double)y, (double)z, 0.0D, 0.0D);
				return true;
			} else {
				return false;
			}
		} else {
			int i5 = block.colorMultiplier(renderBlocks.blockAccess, x, y, z);
			float f6 = (float)(i5 >> 16 & 255) / 255.0F;
			float f7 = (float)(i5 >> 8 & 255) / 255.0F;
			float f8 = (float)(i5 & 255) / 255.0F;
			if(GameRenderer.anaglyphEnable) {
				float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
				float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
				float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
				f6 = f9;
				f7 = f10;
				f8 = f11;
			}

			return RenderBlockUtil.renderBlockCactusImpl(renderBlocks, block, x, y, z, f6, f7, f8);
		}
	}

	@Override
	public void renderBlockOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		RenderBlockUtil.renderInsetCubeOnInventory(renderBlocks, block, meta, brightness);
	}
}