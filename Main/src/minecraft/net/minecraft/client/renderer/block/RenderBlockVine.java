package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.world.level.tile.Block;

public class RenderBlockVine implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.getBlockTextureFromSide(0);
		if(renderBlocks.overrideBlockTexture >= 0) {
			l = renderBlocks.overrideBlockTexture;
		}

		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, i, j, k));
		
		int var7 = block.colorMultiplier(renderBlocks.blockAccess, i, j, k);
		float var8 = (float)(var7 >> 16 & 255) / 255.0F;
		float var9 = (float)(var7 >> 8 & 255) / 255.0F;
		float var10 = (float)(var7 & 255) / 255.0F;
		
		tessellator.setColorOpaque_F(var8, var9, var10);
		
		/*
		int i11 = (l & 15) << 4;
		int i12 = l & 240;
		double d = (double)((float)i11 / 256.0F);
		double d1 = (double)(((float)i11 + 15.99F) / 256.0F);
		double d2 = (double)((float)i12 / 256.0F);
		double d3 = (double)(((float)i12 + 15.99F) / 256.0F);
		*/
		Idx2uvF.calc(l);
		double d = Idx2uvF.u1;
		double d1 = Idx2uvF.u2;
		double d2 = Idx2uvF.v1;
		double d3 = Idx2uvF.v2;
		
		double d4 = 0.05D;
		
		int meta = renderBlocks.blockAccess.getBlockMetadata(i, j, k);
		
		if ((meta & 2) != 0) {
			tessellator.addVertexWithUV((double)i + d4, j + 1, k + 1, d, d2);
			tessellator.addVertexWithUV((double)i + d4, j + 0, k + 1, d, d3);
			tessellator.addVertexWithUV((double)i + d4, j + 0, k + 0, d1, d3);
			tessellator.addVertexWithUV((double)i + d4, j + 1, k + 0, d1, d2);
			tessellator.addVertexWithUV((double)i + d4, j + 1, k + 0, d1, d2);
			tessellator.addVertexWithUV((double)i + d4, j + 0, k + 0, d1, d3);
			tessellator.addVertexWithUV((double)i + d4, j + 0, k + 1, d, d3);
			tessellator.addVertexWithUV((double)i + d4, j + 1, k + 1, d, d2);
		}

		if ((meta & 8) != 0) {
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 0, k + 1, d1, d3);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 1, k + 1, d1, d2);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 1, k + 0, d, d2);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 0, k + 0, d, d3);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 0, k + 0, d, d3);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 1, k + 0, d, d2);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 1, k + 1, d1, d2);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 0, k + 1, d1, d3);
		}

		if ((meta & 4) != 0) {
			tessellator.addVertexWithUV(i + 1, j + 0, (double)k + d4, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + 1, (double)k + d4, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 1, (double)k + d4, d, d2);
			tessellator.addVertexWithUV(i + 0, j + 0, (double)k + d4, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 0, (double)k + d4, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 1, (double)k + d4, d, d2);
			tessellator.addVertexWithUV(i + 1, j + 1, (double)k + d4, d1, d2);
			tessellator.addVertexWithUV(i + 1, j + 0, (double)k + d4, d1, d3);
		}

		if ((meta & 1) != 0) {
			tessellator.addVertexWithUV(i + 1, j + 1, (double)(k + 1) - d4, d, d2);
			tessellator.addVertexWithUV(i + 1, j + 0, (double)(k + 1) - d4, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 0, (double)(k + 1) - d4, d1, d3);
			tessellator.addVertexWithUV(i + 0, j + 1, (double)(k + 1) - d4, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 1, (double)(k + 1) - d4, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 0, (double)(k + 1) - d4, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + 0, (double)(k + 1) - d4, d, d3);
			tessellator.addVertexWithUV(i + 1, j + 1, (double)(k + 1) - d4, d, d2);
		}
		
		if(renderBlocks.blockAccess.isBlockNormalCube(i, j + 1, k)) {
			tessellator.addVertexWithUV((double)(i + 1), (double)(j + 1) - d4, (double)(k + 0), d, d2);
			tessellator.addVertexWithUV((double)(i + 1), (double)(j + 1) - d4, (double)(k + 1), d, d3);
			tessellator.addVertexWithUV((double)(i + 0), (double)(j + 1) - d4, (double)(k + 1), d1, d3);
			tessellator.addVertexWithUV((double)(i + 0), (double)(j + 1) - d4, (double)(k + 0), d1, d2);
		}
		
		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}