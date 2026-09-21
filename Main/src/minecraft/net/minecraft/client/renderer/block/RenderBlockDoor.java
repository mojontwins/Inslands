package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.level.tile.Block;

public class RenderBlockDoor implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		boolean z7 = false;
		float f8 = 0.5F;
		float f9 = 1.0F;
		float f10 = 0.8F;
		float f11 = 0.6F;
		int i12 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4);
		tessellator5.setBrightness(block1.minY > 0.0D ? i12 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4));
		tessellator5.setColorOpaque_F(f8, f8, f8);
		renderBlocks.renderBottomFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 0));
		z7 = true;
		tessellator5.setBrightness(block1.maxY < 1.0D ? i12 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4));
		tessellator5.setColorOpaque_F(f9, f9, f9);
		renderBlocks.renderTopFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 1));
		z7 = true;
		tessellator5.setBrightness(block1.minZ > 0.0D ? i12 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 - 1));
		tessellator5.setColorOpaque_F(f10, f10, f10);
		int i14 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 2);
		if(i14 < 0) {
			renderBlocks.flipTexture = true;
			i14 = -i14;
		}

		renderBlocks.renderEastFace(block1, (double)i2, (double)i3, (double)i4, i14);
		z7 = true;
		renderBlocks.flipTexture = false;
		tessellator5.setBrightness(block1.maxZ < 1.0D ? i12 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 + 1));
		tessellator5.setColorOpaque_F(f10, f10, f10);
		i14 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 3);
		if(i14 < 0) {
			renderBlocks.flipTexture = true;
			i14 = -i14;
		}

		renderBlocks.renderWestFace(block1, (double)i2, (double)i3, (double)i4, i14);
		z7 = true;
		renderBlocks.flipTexture = false;
		tessellator5.setBrightness(block1.minX > 0.0D ? i12 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4));
		tessellator5.setColorOpaque_F(f11, f11, f11);
		i14 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 4);
		if(i14 < 0) {
			renderBlocks.flipTexture = true;
			i14 = -i14;
		}

		renderBlocks.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, i14);
		z7 = true;
		renderBlocks.flipTexture = false;
		tessellator5.setBrightness(block1.maxX < 1.0D ? i12 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4));
		tessellator5.setColorOpaque_F(f11, f11, f11);
		i14 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 5);
		if(i14 < 0) {
			renderBlocks.flipTexture = true;
			i14 = -i14;
		}

		renderBlocks.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, i14);
		z7 = true;
		renderBlocks.flipTexture = false;
		return z7;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}