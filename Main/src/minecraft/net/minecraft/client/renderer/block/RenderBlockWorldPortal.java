package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockCloth;

public class RenderBlockWorldPortal implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		this.renderWorldPortalCube(renderBlocks, block, (float)x, (float)y, (float)z, 0.0F, 1.0F, 145);
		this.renderWorldPortalCube(renderBlocks, block, (float)x, (float)y, (float)z, 0.125F, 0.875F, 49);
		return true;
	}

	private void renderWorldPortalCube(RenderBlocks renderBlocks, Block block, float x, float y, float z, float min, float max, int textureIndex) {
		block.setBlockBounds(min, min, min, max, max, max);
		Tessellator tessellator = Tessellator.instance;

		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z, 1.0F);
		renderBlocks.renderBottomFace(block, x, y, z, textureIndex);

		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z, 0.5F);
		renderBlocks.renderTopFace(block, x, y, z, textureIndex);

		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z, 0.8F);
		renderBlocks.renderNorthFace(block, x, y, z, textureIndex);

		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z, 0.8F);
		renderBlocks.renderSouthFace(block, x, y, z, textureIndex);

		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z, 0.6F);
		renderBlocks.renderEastFace(block, x, y, z, textureIndex);

		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		RenderBlockUtil.setLightValue(tessellator, renderBlocks.blockAccess, block, x, y, z, 0.6F);
		renderBlocks.renderWestFace(block, x, y, z, textureIndex);

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void renderBlockOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		Tessellator tes = Tessellator.instance;
		int wool = meta & 15;
		int rgba = BlockCloth.clothColors[15 - wool];
		float r = (float)(rgba >> 16 & 255) / 255.0F;
		float g = (float)(rgba >> 8 & 255) / 255.0F;
		float b = (float)(rgba & 255) / 255.0F;

		this.renderWorldPortalInventoryCube(renderBlocks, tes, block, 0.0F, 1.0F, 145, 1.0F, 1.0F, 1.0F, brightness);
		this.renderWorldPortalInventoryCube(renderBlocks, tes, block, 0.125F, 0.875F, 64, r, g, b, brightness);
	}

	private void renderWorldPortalInventoryCube(RenderBlocks renderBlocks, Tessellator tes, Block block, float min, float max, int textureIndex, float cr, float cg, float cb, float brightness) {
		block.setBlockBounds(min, min, min, max, max, max);
		float dim = brightness;

		tes.startDrawingQuads();
		tes.setColorRGBA_F(cr * dim, cg * dim, cb * dim, 1.0F);
		renderBlocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, textureIndex);
		tes.draw();

		tes.startDrawingQuads();
		tes.setColorRGBA_F(cr * 0.5F * dim, cg * 0.5F * dim, cb * 0.5F * dim, 1.0F);
		renderBlocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, textureIndex);
		tes.draw();

		tes.startDrawingQuads();
		tes.setColorRGBA_F(cr * 0.8F * dim, cg * 0.8F * dim, cb * 0.8F * dim, 1.0F);
		renderBlocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, textureIndex);
		tes.draw();

		tes.startDrawingQuads();
		tes.setColorRGBA_F(cr * 0.8F * dim, cg * 0.8F * dim, cb * 0.8F * dim, 1.0F);
		renderBlocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, textureIndex);
		tes.draw();

		tes.startDrawingQuads();
		tes.setColorRGBA_F(cr * 0.6F * dim, cg * 0.6F * dim, cb * 0.6F * dim, 1.0F);
		renderBlocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, textureIndex);
		tes.draw();

		tes.startDrawingQuads();
		tes.setColorRGBA_F(cr * 0.6F * dim, cg * 0.6F * dim, cb * 0.6F * dim, 1.0F);
		renderBlocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, textureIndex);
		tes.draw();

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}