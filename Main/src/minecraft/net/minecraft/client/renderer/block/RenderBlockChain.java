package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.world.level.tile.Block;

public class RenderBlockChain implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		Tessellator tessellator9 = Tessellator.instance;
		
		tessellator9.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		tessellator9.setColorOpaque_F(1, 1, 1);
		
		int i10 = block.getBlockTextureFromSideAndMetadata(0, renderBlocks.blockAccess.getBlockMetadata(x, y, z));
		if(renderBlocks.overrideBlockTexture >= 0) {
			i10 = renderBlocks.overrideBlockTexture;
		}
		
		Idx2uvF.calc(i10);
		double u1 = Idx2uvF.u1;
		double u2 = Idx2uvF.u2;
		double v1 = Idx2uvF.v1;
		double v2 = Idx2uvF.v2;
		
		double x1 = (double)x + 0.5D - (double)0.45F;
		double x2 = (double)x + 0.5D + (double)0.45F;
		double z1 = (double)z + 0.5D - (double)0.45F;
		double z2 = (double)z + 0.5D + (double)0.45F;
		double yy = (double)y;
		
		// Square 1
		
		tessellator9.addVertexWithUV(x1, yy + 1.0D, z1,  u1, v2);
		tessellator9.addVertexWithUV(x1, yy + 0.0D, z1,  u1, v1);
		tessellator9.addVertexWithUV(x2, yy + 0.0D, z2,  u2, v1);
		tessellator9.addVertexWithUV(x2, yy + 1.0D, z2,  u2, v2)
		;
		tessellator9.addVertexWithUV(x2, yy + 1.0D, z2,  u1, v2);
		tessellator9.addVertexWithUV(x2, yy + 0.0D, z2,  u1, v1);
		tessellator9.addVertexWithUV(x1, yy + 0.0D, z1,  u2, v1);
		tessellator9.addVertexWithUV(x1, yy + 1.0D, z1,  u2, v2);
		
		// Square 2 [upside down]
		
		tessellator9.addVertexWithUV(x1, yy + 1.0D, z2,  u1, v1);
		tessellator9.addVertexWithUV(x1, yy + 0.0D, z2,  u1, v2);
		tessellator9.addVertexWithUV(x2, yy + 0.0D, z1,  u2, v2);
		tessellator9.addVertexWithUV(x2, yy + 1.0D, z1,  u2, v1);
		
		tessellator9.addVertexWithUV(x2, yy + 1.0D, z1,  u1, v1);
		tessellator9.addVertexWithUV(x2, yy + 0.0D, z1,  u1, v2);
		tessellator9.addVertexWithUV(x1, yy + 0.0D, z2,  u2, v2);
		tessellator9.addVertexWithUV(x1, yy + 1.0D, z2,  u2, v1);
	
		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}