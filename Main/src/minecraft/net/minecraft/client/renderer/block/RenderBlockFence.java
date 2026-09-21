package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.world.level.tile.Block;

public class RenderBlockFence implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		
		float min = 0.375F;
		float max = 0.625F;
		block.setBlockBounds(min, 0.0F, min, max, 1.0F, max);
		RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
		
		boolean connectEorW = false;
		boolean connectNorS = false;
		if(renderBlocks.blockAccess.getBlockID(x - 1, y, z) == block.blockID || renderBlocks.blockAccess.getBlockID(x + 1, y, z) == block.blockID) {
			connectEorW = true;
		}

		if(renderBlocks.blockAccess.getBlockID(x, y, z - 1) == block.blockID || renderBlocks.blockAccess.getBlockID(x, y, z + 1) == block.blockID) {
			connectNorS = true;
		}

		boolean connectE = renderBlocks.blockAccess.getBlockID(x - 1, y, z) == block.blockID;
		boolean connectW = renderBlocks.blockAccess.getBlockID(x + 1, y, z) == block.blockID;
		boolean connectN = renderBlocks.blockAccess.getBlockID(x, y, z - 1) == block.blockID;
		boolean connectS = renderBlocks.blockAccess.getBlockID(x, y, z + 1) == block.blockID;
		if(!connectEorW && !connectNorS) {
			connectEorW = true;
		}

		min = 0.4375F;
		max = 0.5625F;
		float minY = 0.75F;
		float maxY = 0.9375F;
		float minX = connectE ? 0.0F : min;
		float maxX = connectW ? 1.0F : max;
		float minZ = connectN ? 0.0F : min;
		float maxZ = connectS ? 1.0F : max;
		
		if(connectEorW) {
			block.setBlockBounds(minX, minY, min, maxX, maxY, max);
			RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
		}

		if(connectNorS) {
			block.setBlockBounds(min, minY, minZ, max, maxY, maxZ);
			RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
		}

		minY = 0.375F;
		maxY = 0.5625F;
		if(connectEorW) {
			block.setBlockBounds(minX, minY, min, maxX, maxY, max);
			RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
		}

		if(connectNorS) {
			block.setBlockBounds(min, minY, minZ, max, maxY, maxZ);
			RenderBlockUtil.renderStandardBlock(renderBlocks, block, x, y, z);
		}

		//block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		block.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
		
		return true;
	}

	@Override
	public void renderBlockOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		Tessellator tes = Tessellator.instance;
		float n2;
		for(int i9 = 0; i9 < 4; ++i9) {
			n2 = 0.125F;
			if(i9 == 0) {
				block.setBlockBounds(0.5F - n2, 0.0F, 0.0F, 0.5F + n2, 1.0F, n2 * 2.0F);
			}

			if(i9 == 1) {
				block.setBlockBounds(0.5F - n2, 0.0F, 1.0F - n2 * 2.0F, 0.5F + n2, 1.0F, 1.0F);
			}

			n2 = 0.0625F;
			if(i9 == 2) {
				block.setBlockBounds(0.5F - n2, 1.0F - n2 * 3.0F, -n2 * 2.0F, 0.5F + n2, 1.0F - n2, 1.0F + n2 * 2.0F);
			}

			if(i9 == 3) {
				block.setBlockBounds(0.5F - n2, 0.5F - n2 * 3.0F, -n2 * 2.0F, 0.5F + n2, 0.5F - n2, 1.0F + n2 * 2.0F);
			}

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tes.startDrawingQuads();
			tes.setNormal(0.0F, -1.0F, 0.0F);
			renderBlocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
			tes.draw();
			tes.startDrawingQuads();
			tes.setNormal(0.0F, 1.0F, 0.0F);
			renderBlocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
			tes.draw();
			tes.startDrawingQuads();
			tes.setNormal(0.0F, 0.0F, -1.0F);
			renderBlocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
			tes.draw();
			tes.startDrawingQuads();
			tes.setNormal(0.0F, 0.0F, 1.0F);
			renderBlocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
			tes.draw();
			tes.startDrawingQuads();
			tes.setNormal(-1.0F, 0.0F, 0.0F);
			renderBlocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
			tes.draw();
			tes.startDrawingQuads();
			tes.setNormal(1.0F, 0.0F, 0.0F);
			renderBlocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
			tes.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}