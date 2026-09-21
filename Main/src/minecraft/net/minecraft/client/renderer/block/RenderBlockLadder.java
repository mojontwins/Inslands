package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.world.level.tile.Block;
import org.lwjgl.opengl.GL11;

public class RenderBlockLadder implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = block.getBlockTextureFromSide(0);
		if(renderBlocks.overrideBlockTexture >= 0) {
			i6 = renderBlocks.overrideBlockTexture;
		}

		tessellator5.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		float f7 = 1.0F;
		tessellator5.setColorOpaque_F(f7, f7, f7);
		
		/*
		int i22 = (i6 & 15) << 4;
		int i8 = i6 & 0xff0;
		double d9 = (double)((float)i22 / 256F);
		double d11 = (double)(((float)i22 + 15.99F) / 256F);
		double d13 = (double)((float)i8 / 256F);
		double d15 = (double)(((float)i8 + 15.99F) / 256F);
		*/
		
		Idx2uvF.calc(i6);
		double d9 = Idx2uvF.u1;
		double d11 = Idx2uvF.u2;
		double d13 = Idx2uvF.v1;
		double d15 = Idx2uvF.v2;
		
		int i17 = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		double d18 = 0.0D;
		double d20 = (double)0.05F;
		if(i17 == 5) {
			tessellator5.addVertexWithUV((double)x + d20, (double)(y + 1) + d18, (double)(z + 1) + d18, d9, d13);
			tessellator5.addVertexWithUV((double)x + d20, (double)(y + 0) - d18, (double)(z + 1) + d18, d9, d15);
			tessellator5.addVertexWithUV((double)x + d20, (double)(y + 0) - d18, (double)(z + 0) - d18, d11, d15);
			tessellator5.addVertexWithUV((double)x + d20, (double)(y + 1) + d18, (double)(z + 0) - d18, d11, d13);
		}

		if(i17 == 4) {
			tessellator5.addVertexWithUV((double)(x + 1) - d20, (double)(y + 0) - d18, (double)(z + 1) + d18, d11, d15);
			tessellator5.addVertexWithUV((double)(x + 1) - d20, (double)(y + 1) + d18, (double)(z + 1) + d18, d11, d13);
			tessellator5.addVertexWithUV((double)(x + 1) - d20, (double)(y + 1) + d18, (double)(z + 0) - d18, d9, d13);
			tessellator5.addVertexWithUV((double)(x + 1) - d20, (double)(y + 0) - d18, (double)(z + 0) - d18, d9, d15);
		}

		if(i17 == 3) {
			tessellator5.addVertexWithUV((double)(x + 1) + d18, (double)(y + 0) - d18, (double)z + d20, d11, d15);
			tessellator5.addVertexWithUV((double)(x + 1) + d18, (double)(y + 1) + d18, (double)z + d20, d11, d13);
			tessellator5.addVertexWithUV((double)(x + 0) - d18, (double)(y + 1) + d18, (double)z + d20, d9, d13);
			tessellator5.addVertexWithUV((double)(x + 0) - d18, (double)(y + 0) - d18, (double)z + d20, d9, d15);
		}

		if(i17 == 2) {
			tessellator5.addVertexWithUV((double)(x + 1) + d18, (double)(y + 1) + d18, (double)(z + 1) - d20, d9, d13);
			tessellator5.addVertexWithUV((double)(x + 1) + d18, (double)(y + 0) - d18, (double)(z + 1) - d20, d9, d15);
			tessellator5.addVertexWithUV((double)(x + 0) - d18, (double)(y + 0) - d18, (double)(z + 1) - d20, d11, d15);
			tessellator5.addVertexWithUV((double)(x + 0) - d18, (double)(y + 1) + d18, (double)(z + 1) - d20, d11, d13);
		}

		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}