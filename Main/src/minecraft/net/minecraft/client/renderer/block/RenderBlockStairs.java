package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.world.level.tile.Block;

public class RenderBlockStairs implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		boolean z5 = false;
		int i6 = renderBlocks.blockAccess.getBlockMetadata(i2, i3, i4);
		boolean upsideDown = (i6 & 8) != 0;
		i6 &= 7;
		
		if(upsideDown) {
			if(i6 == 0) {
				block1.setBlockBounds(0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				block1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 1) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				block1.setBlockBounds(0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 2) {
				block1.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				block1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 3) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				block1.setBlockBounds(0.0F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				z5 = true;
			}
		} else {
			if(i6 == 0) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				block1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 1) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				block1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 2) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				block1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 3) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				block1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
				RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
				z5 = true;
			}
		}
		
		block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return z5;
	}

	@Override
	public void renderBlockOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		Tessellator tes = Tessellator.instance;
		for(int i9 = 0; i9 < 2; ++i9) {
			if(i9 == 0) {
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			}

			if(i9 == 1) {
				block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
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
	}
}