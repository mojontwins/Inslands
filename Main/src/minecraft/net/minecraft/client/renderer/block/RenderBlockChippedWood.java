package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Texels;
import net.minecraft.client.renderer.util.TextureAtlasSize;
import net.minecraft.world.level.tile.Block;

public class RenderBlockChippedWood implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		float x1, y1, z1, x2, y2, z2;
		float u1, v1, u2, v2;
		
		Tessellator tessellator = Tessellator.instance;
		
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		
		if((meta & 4) == 0) {
			block.setBlockBounds((1 / 16.0F), 0.0F, (1 / 16.0F), 15 * (1 / 16.0F), 1.0F, 15 * (1 / 16.0F));
			RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			return true;
		} else if((meta & 8) != 0) { 
			// facing North/South
			
			// Texture #1 (ends)
			int ti_1 = block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 1); 	// CUSTOM : side = 1 means "ends"
			if(renderBlocks.overrideBlockTexture >= 0) ti_1 = renderBlocks.overrideBlockTexture;
			
			float t1_u = (float) ((ti_1 & 0x0f) << 4) / TextureAtlasSize.w;
			float t1_v = (float) (ti_1 & 0xff0) / TextureAtlasSize.h;

			// Texture #2 (sides)
			int ti_2 = block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 0); 	// CUSTOM : side = 1 means "sides"
			if(renderBlocks.overrideBlockTexture >= 0) ti_2 = renderBlocks.overrideBlockTexture;
			
			float t2_u = (float) ((ti_2 & 0x0f) << 4) / TextureAtlasSize.w;
			float t2_v = (float) (ti_2 & 0xff0) / TextureAtlasSize.h;
			
			x1 = x + 0.0000F;
			y1 = y + 0.0625F;
			z1 = z + 0.0625F;
			x2 = x + 1.0000F;
			y2 = y + 0.9375F;
			z2 = z + 0.9375F;

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y + 1, z, 1.0F);
			u1 = t2_u; //  + 0.0000000F;
			v1 = t2_v; //  + 0.0000000F;
			u2 = t2_u + Texels.texelsU(16); // 0.0625000F;
			v2 = t2_v + Texels.texelsV(16); // 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y2, z1, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y - 1, z, 0.5F);
			tessellator.addVertexWithUV(x1, y1, z2, u1, v2);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u1, v1);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z - 1, 0.8F);
			tessellator.addVertexWithUV(x1, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z + 1, 0.8F);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v2);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x + 1, y, z, 0.6F);
			u1 = t1_u + Texels.texelsU(1); // 0.00390625F;
			v1 = t1_v + Texels.texelsV(1); // 0.00390625F;
			u2 = t1_u + Texels.texelsU(15); // 0.05859375F;
			v2 = t1_v + Texels.texelsV(15); // 0.05859375F;
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v2);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x - 1, y, z, 0.6F);
			tessellator.addVertexWithUV(x1, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);
			
			return true;
		} else {
			// facing East/West
			
			// Texture #1 (ends)
			int ti_1 = block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 1); 	// CUSTOM : side = 1 means "ends"
			if(renderBlocks.overrideBlockTexture >= 0) ti_1 = renderBlocks.overrideBlockTexture;
			float t1_u = (float) ((ti_1 & 0x0f) << 4) / TextureAtlasSize.w;
			float t1_v = (float) (ti_1 & 0xff0) / TextureAtlasSize.h;

			// Texture #2 (sides)
			int ti_2 = block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 0); 	// CUSTOM : side = 1 means "sides"
			if(renderBlocks.overrideBlockTexture >= 0) ti_2 = renderBlocks.overrideBlockTexture;
			float t2_u = (float) ((ti_2 & 0x0f) << 4) / TextureAtlasSize.w;
			float t2_v = (float) (ti_2 & 0xff0) / TextureAtlasSize.h;
			
			x1 = x + 0.0625F;
			y1 = y + 0.0625F;
			z1 = z + 0.0000F;
			x2 = x + 0.9375F;
			y2 = y + 0.9375F;
			z2 = z + 1.0000F;

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y + 1, z, 1.0F);
			u1 = t2_u; //  + 0.0000000F;
			v1 = t2_v; //  + 0.0000000F;
			u2 = t2_u + Texels.texelsU(16); // 0.0625000F;
			v2 = t2_v + Texels.texelsV(16); // 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x1, y2, z1, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u2, v1);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y - 1, z, 0.5F);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);
			tessellator.addVertexWithUV(x2, y1, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y1, z2, u1, v1);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x + 1, y, z, 0.6F);
			tessellator.addVertexWithUV(x1, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x - 1, y, z, 0.6F);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v2);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z - 1, 0.8F);
			u1 = t1_u + Texels.texelsU(1); // 0.00390625F;
			v1 = t1_v + Texels.texelsV(1); // 0.00390625F;
			u2 = t1_u + Texels.texelsU(15); // 0.05859375F;
			v2 = t1_v + Texels.texelsV(15); // 0.05859375F;
			tessellator.addVertexWithUV(x1, y2, z1, u2, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u1, v2);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);

			RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z + 1, 0.8F);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u1, v2);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u2, v1);
			
			return true;
		}

	}
}