package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.world.level.tile.Block;

public class RenderBlockLilyPad implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		Tessellator tessellator = Tessellator.instance;

		int textureIndex = block.blockIndexInTexture;
		Idx2uvF.calc(textureIndex);
		double u1 = Idx2uvF.u1;
		double u2 = Idx2uvF.u2;
		double v1 = Idx2uvF.v1;
		double v2 = Idx2uvF.v2;

		long coordinateHash = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
		coordinateHash = coordinateHash * coordinateHash * 42317861L + coordinateHash * 11L;
		int variation = (int)((coordinateHash >> 16) & 7L);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

		float fX = (float)x + 0.5F;
		float fZ = (float)z + 0.5F;
		float fR1 = (float)(variation & 1) * 0.5F * (float)(1 - variation / 2 % 2 * 2);
		float fR2 = (float)(variation + 1 & 1) * 0.5F * (float)(1 - (variation + 1) / 2 % 2 * 2);
		double fY = (double)y + 0.015625F;

		tessellator.addVertexWithUV((double)(fX + fR1 - fR2), fY, (double)(fZ + fR1 + fR2), u1, v1);
		tessellator.addVertexWithUV((double)(fX + fR1 + fR2), fY, (double)(fZ - fR1 + fR2), u2, v1);
		tessellator.addVertexWithUV((double)(fX - fR1 + fR2), fY, (double)(fZ - fR1 - fR2), u2, v2);
		tessellator.addVertexWithUV((double)(fX - fR1 - fR2), fY, (double)(fZ + fR1 - fR2), u1, v2);
		tessellator.addVertexWithUV((double)(fX - fR1 - fR2), fY, (double)(fZ + fR1 - fR2), u1, v2);
		tessellator.addVertexWithUV((double)(fX - fR1 + fR2), fY, (double)(fZ - fR1 - fR2), u2, v2);
		tessellator.addVertexWithUV((double)(fX + fR1 + fR2), fY, (double)(fZ - fR1 + fR2), u2, v1);
		tessellator.addVertexWithUV((double)(fX + fR1 - fR2), fY, (double)(fZ + fR1 + fR2), u1, v1);

		if ((variation & 4) != 0) {
			renderBlocks.overrideBlockTexture = 12 * 16 + 2;
			RenderBlockUtil.drawCrossedSquares(renderBlocks, block, x, y, z);
			renderBlocks.overrideBlockTexture = -1;
		}

		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}