package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.world.level.tile.Block;

public class RenderBlockWall implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = block.getBlockTextureFromSide(0);
		if(renderBlocks.overrideBlockTexture >= 0) {
			i6 = renderBlocks.overrideBlockTexture;
		}

		tessellator5.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		tessellator5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		
		Idx2uvF.calc(i6);
		double d10 = Idx2uvF.u1;
		double d12 = Idx2uvF.u2;
		double d14 = Idx2uvF.v1;
		double d16 = Idx2uvF.v2;
		
		int i18 = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		
		float of1 = 0.49F; 
		float of2 = 0.51F;
		
		if(i18 == 4 || i18 == 5) {
			tessellator5.addVertexWithUV((double)((float)x + of1), (double)(y + 1), (double)(z + 1), d10, d14);
			tessellator5.addVertexWithUV((double)((float)x + of1), (double)(y + 0), (double)(z + 1), d10, d16);
			tessellator5.addVertexWithUV((double)((float)x + of1), (double)(y + 0), (double)(z + 0), d12, d16);
			tessellator5.addVertexWithUV((double)((float)x + of1), (double)(y + 1), (double)(z + 0), d12, d14);
			
			tessellator5.addVertexWithUV((double)((float)x + of2), (double)(y + 1), (double)(z + 0), d12, d14);
			tessellator5.addVertexWithUV((double)((float)x + of2), (double)(y + 0), (double)(z + 0), d12, d16);
			tessellator5.addVertexWithUV((double)((float)x + of2), (double)(y + 0), (double)(z + 1), d10, d16);
			tessellator5.addVertexWithUV((double)((float)x + of2), (double)(y + 1), (double)(z + 1), d10, d14);
		}

		if(i18 == 2 || i18 == 3) {
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)((float)z + of1), d12, d16);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 1), (double)((float)z + of1), d12, d14);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 1), (double)((float)z + of1), d10, d14);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)((float)z + of1), d10, d16);
			
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)((float)z + of1), d10, d16);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 1), (double)((float)z + of1), d10, d14);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 1), (double)((float)z + of1), d12, d14);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)((float)z + of1), d12, d16);
		}

		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}