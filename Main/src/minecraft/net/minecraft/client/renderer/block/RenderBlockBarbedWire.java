package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.client.renderer.util.Texels;
import net.minecraft.world.level.tile.Block;

public class RenderBlockBarbedWire implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		Tessellator tessellator = Tessellator.instance;		
		
		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		
		int textureIndex = block.getBlockTextureFromSide(2);
		if(renderBlocks.overrideBlockTexture >= 0) {
			textureIndex = renderBlocks.overrideBlockTexture;
		}
		
		Idx2uvF.calc(textureIndex);
		double u1 = Idx2uvF.u1;
		double uh = u1 + Texels.texelsV(8F);
		double u2 = Idx2uvF.u2;
		double v1 = Idx2uvF.v1;
		double v2 = Idx2uvF.v2;
		
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		
		if (meta == 4 || meta == 5) {
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 0.0F, (double)z + 1.0F, uh, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 1.0F, (double)z + 0.5F, u1, v1);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 1.0F, u1, v2);
		
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 1.0F, u1, v2);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 1.0F, (double)z + 0.5F, u1, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 0.0F, (double)z + 1.0F, uh, v2);			

			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.0F, u2, v2);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 1.0F, (double)z + 0.5F, u2, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 0.0F, (double)z + 0.0F, uh, v2);
					
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 0.0F, (double)z + 0.0F, uh, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 1.0F, (double)z + 0.5F, u2, v1);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.0F, u2, v2);
		} else if (meta == 2 || meta == 3) {
			
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.5F, uh, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.0F, u1, v1);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.0F, u1, v2);
			
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.0F, u1, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.0F, u1, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.5F, uh, v2);		
			
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 1.0F, u2, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 1.0F, u2, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 0.5F, uh, v2);
			
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 0.5F, uh, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 1.0F, u2, v1);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 1.0F, u2, v2);		
			
		}
		
		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}