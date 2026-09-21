package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.world.Direction;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockBed;

public class RenderBlockBed implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = renderBlocks.blockAccess.getBlockMetadata(i2, i3, i4);
		int i7 = BlockBed.getDirectionFromMetadata(i6);
		boolean z8 = BlockBed.isBlockFootOfBed(i6);
		float f9 = 0.5F;
		float f10 = 1.0F;
		float f11 = 0.8F;
		float f12 = 0.6F;
		
		int i25 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4);
		tessellator5.setBrightness(i25);
		tessellator5.setColorOpaque_F(f9, f9, f9);
		
		int i27 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 0);
		Idx2uvF.calc(i27);
		double d30 = Idx2uvF.u1;
		double d32 = Idx2uvF.u2;
		double d34 = Idx2uvF.v1;
		double d36 = Idx2uvF.v2;
		
		double d38 = (double)i2 + block1.minX;
		double d40 = (double)i2 + block1.maxX;
		double d42 = (double)i3 + block1.minY + 0.1875D;
		double d44 = (double)i4 + block1.minZ;
		double d46 = (double)i4 + block1.maxZ;
		tessellator5.addVertexWithUV(d38, d42, d46, d30, d36);
		tessellator5.addVertexWithUV(d38, d42, d44, d30, d34);
		tessellator5.addVertexWithUV(d40, d42, d44, d32, d34);
		tessellator5.addVertexWithUV(d40, d42, d46, d32, d36);
		tessellator5.setBrightness(block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4));
		tessellator5.setColorOpaque_F(f10, f10, f10);
		i27 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 1);
		Idx2uvF.calc(i27);
		d30 = Idx2uvF.u1;
		d32 = Idx2uvF.u2;
		d34 = Idx2uvF.v1;
		d36 = Idx2uvF.v2;
		
		d38 = d30;
		d40 = d32;
		d42 = d34;
		d44 = d34;
		d46 = d30;
		double d48 = d32;
		double d50 = d36;
		double d52 = d36;
		if(i7 == 0) {
			d40 = d30;
			d42 = d36;
			d46 = d32;
			d52 = d34;
		} else if(i7 == 2) {
			d38 = d32;
			d44 = d36;
			d48 = d30;
			d50 = d34;
		} else if(i7 == 3) {
			d38 = d32;
			d44 = d36;
			d48 = d30;
			d50 = d34;
			d40 = d30;
			d42 = d36;
			d46 = d32;
			d52 = d34;
		}

		double d54 = (double)i2 + block1.minX;
		double d56 = (double)i2 + block1.maxX;
		double d58 = (double)i3 + block1.maxY;
		double d60 = (double)i4 + block1.minZ;
		double d62 = (double)i4 + block1.maxZ;
		tessellator5.addVertexWithUV(d56, d58, d62, d46, d50);
		tessellator5.addVertexWithUV(d56, d58, d60, d38, d42);
		tessellator5.addVertexWithUV(d54, d58, d60, d40, d44);
		tessellator5.addVertexWithUV(d54, d58, d62, d48, d52);
		i27 = Direction.headInvisibleFace[i7];
		if(z8) {
			i27 = Direction.headInvisibleFace[Direction.footInvisibleFaceRemap[i7]];
		}

		byte b64 = 4;
		switch(i7) {
		case 0:
			b64 = 5;
			break;
		case 1:
			b64 = 3;
		case 2:
		default:
			break;
		case 3:
			b64 = 2;
		}

		if(i27 != 2 && (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3, i4 - 1, 2))) {
			tessellator5.setBrightness(block1.minZ > 0.0D ? i25 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 - 1));
			tessellator5.setColorOpaque_F(f11, f11, f11);
			renderBlocks.flipTexture = b64 == 2;
			renderBlocks.renderEastFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 2));
		}

		if(i27 != 3 && (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3, i4 + 1, 3))) {
			tessellator5.setBrightness(block1.maxZ < 1.0D ? i25 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 + 1));
			tessellator5.setColorOpaque_F(f11, f11, f11);
			renderBlocks.flipTexture = b64 == 3;
			renderBlocks.renderWestFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 3));
		}

		if(i27 != 4 && (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2 - 1, i3, i4, 4))) {
			tessellator5.setBrightness(block1.minZ > 0.0D ? i25 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4));
			tessellator5.setColorOpaque_F(f12, f12, f12);
			renderBlocks.flipTexture = b64 == 4;
			renderBlocks.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 4));
		}

		if(i27 != 5 && (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2 + 1, i3, i4, 5))) {
			tessellator5.setBrightness(block1.maxZ < 1.0D ? i25 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4));
			tessellator5.setColorOpaque_F(f12, f12, f12);
			renderBlocks.flipTexture = b64 == 5;
			renderBlocks.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 5));
		}

		renderBlocks.flipTexture = false;
		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}