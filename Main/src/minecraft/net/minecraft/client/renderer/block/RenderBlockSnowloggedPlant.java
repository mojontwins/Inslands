package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockFlower;
import net.minecraft.world.level.tile.BlockTallGrass;

public class RenderBlockSnowloggedPlant implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		// 1st render the plant
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		int colorMultiplier = block.colorMultiplier(renderBlocks.blockAccess, x, y, z);

		float r = (float)(colorMultiplier >> 16 & 255) / 255.0F;
		float g = (float)(colorMultiplier >> 8 & 255) / 255.0F;
		float b = (float)(colorMultiplier & 255) / 255.0F;

		tessellator.setColorOpaque_F(r, g, b);

		double xD = (double)x;
		double yD = (double)y;
		double zD = (double)z;

		long noise = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
		noise = noise * noise * 42317861L + noise * 11L;
		xD += ((double)((float)(noise >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
		yD += ((double)((float)(noise >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
		zD += ((double)((float)(noise >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;

		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		if(block instanceof BlockTallGrass) {
			int grassType = (meta >> 4) & 7;
			if(((meta >> 4) & 8) != 0) {
				RenderBlockUtil.drawCrossedSquaresDoubleHeight(renderBlocks, block, grassType, xD, yD, zD);
			} else {
				RenderBlockUtil.drawCrossedSquares(renderBlocks, block, grassType, xD, yD, zD);
			}
		} else {
			RenderBlockUtil.drawCrossedSquares(renderBlocks, block, meta, xD, yD, zD);
		}
		// now render a layer of snow
		int snowHeight = meta & 15;
		if(snowHeight > 0) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (float)(snowHeight + 1) / 16.0F, 1.0F);
			renderBlocks.overrideBlockTexture = Block.snow.blockIndexInTexture;
			RenderBlockUtil.renderStandardBlockWithColorMultiplier(renderBlocks, block, x, y, z, 1F, 1F, 1F);
			renderBlocks.overrideBlockTexture = -1;
			((BlockFlower)block).setMyBlockBounds();
		}
		return true;
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