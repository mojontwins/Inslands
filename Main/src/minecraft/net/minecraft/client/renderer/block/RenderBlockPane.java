package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.TextureAtlasSize;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockPane;

public class RenderBlockPane implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		BlockPane blockPane = (BlockPane) block;
		int maxHeight = 128;
		Tessellator tessellator6 = Tessellator.instance;
		tessellator6.setBrightness(blockPane.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

		float light = 1.0F;
		int rgbai = blockPane.colorMultiplier(renderBlocks.blockAccess, x, y, z);
		float r = (float)(rgbai >> 16 & 255) / 255.0F;
		float g = (float)(rgbai >> 8 & 255) / 255.0F;
		float b = (float)(rgbai & 255) / 255.0F;
		
		tessellator6.setColorOpaque_F(light * r, light * g, light * b);
		int texFace;
		int texSide;

		if(renderBlocks.overrideBlockTexture >= 0) {
			texFace = renderBlocks.overrideBlockTexture;
			texSide = renderBlocks.overrideBlockTexture;
		} else {
			int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
			texFace = blockPane.getBlockTextureFromSideAndMetadata(0, meta);
			texSide = blockPane.getSideTextureIndex();
		}

		int texX = (texFace & 15) << 4;
		int texY = texFace & 0xff0;
		double faceU1 = (double)((float)texX / TextureAtlasSize.w);
		double faceU2 = (double)(((float)texX + 7.99F) / TextureAtlasSize.w);
		double faceU3 = (double)(((float)texX + 15.99F) / TextureAtlasSize.w);
		double faceV1 = (double)((float)texY / TextureAtlasSize.h);
		double faceV2 = (double)(((float)texY + 15.99F) / TextureAtlasSize.h);

		texX = (texSide & 15) << 4;
		texY = texSide & 0xff0;
		double sideU1 = (double)((float)(texX + 7) / TextureAtlasSize.w);
		double sideU2 = (double)(((float)texX + 8.99F) / TextureAtlasSize.w);
		double sideV3 = (double)((float)texY / TextureAtlasSize.h);
		double sideV1 = (double)((float)(texY + 8) / TextureAtlasSize.h);
		double sideV2 = (double)(((float)texY + 15.99F) / TextureAtlasSize.h);
		
		double dX1 = (double)x;
		double dXh = (double)x + 0.5D;
		double dX2 = (double)(x + 1);
		double dZ1 = (double)z;
		double dZh = (double)z + 0.5D;
		double dZ2 = (double)(z + 1);
		double dXc1 = (double)x + 0.5D - 0.0625D;
		double dXc2 = (double)x + 0.5D + 0.0625D;
		double dZc1 = (double)z + 0.5D - 0.0625D;
		double dZc2 = (double)z + 0.5D + 0.0625D;
		
		boolean connectN = blockPane.canThisPaneConnectToThisblockID(renderBlocks.blockAccess.getBlockID(x, y, z - 1));
		boolean connectS = blockPane.canThisPaneConnectToThisblockID(renderBlocks.blockAccess.getBlockID(x, y, z + 1));
		boolean connectW = blockPane.canThisPaneConnectToThisblockID(renderBlocks.blockAccess.getBlockID(x - 1, y, z));
		boolean connectE = blockPane.canThisPaneConnectToThisblockID(renderBlocks.blockAccess.getBlockID(x + 1, y, z));
		boolean renderTop = blockPane.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1);
		boolean renderBottom = blockPane.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0);

		if((!connectW || !connectE) && (connectW || connectE || connectN || connectS)) {
			// Not both sides W/E at the same time
			if(connectW && !connectE) {
				tessellator6.addVertexWithUV(dX1, (double)(y + 1), dZh, faceU1, faceV1);
				tessellator6.addVertexWithUV(dX1, (double)(y + 0), dZh, faceU1, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZh, faceU2, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZh, faceU2, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZh, faceU1, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZh, faceU1, faceV2);
				tessellator6.addVertexWithUV(dX1, (double)(y + 0), dZh, faceU2, faceV2);
				tessellator6.addVertexWithUV(dX1, (double)(y + 1), dZh, faceU2, faceV1);
				if(!connectS && !connectN) {
					tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZc2, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZc2, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZc1, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZc1, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZc1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZc2, sideU2, sideV3);
				}

				if(renderTop || y < maxHeight - 1 && renderBlocks.blockAccess.isAirBlock(x - 1, y + 1, z)) {
					tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV1);
				}

				if(renderBottom || y > 1 && renderBlocks.blockAccess.isAirBlock(x - 1, y - 1, z)) {
					tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc1, sideU1, sideV1);
				}
			} else if(!connectW && connectE) {
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZh, faceU2, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZh, faceU2, faceV2);
				tessellator6.addVertexWithUV(dX2, (double)(y + 0), dZh, faceU3, faceV2);
				tessellator6.addVertexWithUV(dX2, (double)(y + 1), dZh, faceU3, faceV1);
				tessellator6.addVertexWithUV(dX2, (double)(y + 1), dZh, faceU2, faceV1);
				tessellator6.addVertexWithUV(dX2, (double)(y + 0), dZh, faceU2, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZh, faceU3, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZh, faceU3, faceV1);
				if(!connectS && !connectN) {
					tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZc1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZc2, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZc2, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZc1, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZc1, sideU2, sideV3);
				}

				if(renderTop || y < maxHeight - 1 && renderBlocks.blockAccess.isAirBlock(x + 1, y + 1, z)) {
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV3);
				}

				if(renderBottom || y > 1 && renderBlocks.blockAccess.isAirBlock(x + 1, y - 1, z)) {
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc1, sideU1, sideV3);
				}
			}
		} else {
			// Both sides W/E at the same time

			tessellator6.addVertexWithUV(dX1, (double)(y + 1), dZh, faceU1, faceV1);
			tessellator6.addVertexWithUV(dX1, (double)(y + 0), dZh, faceU1, faceV2);
			tessellator6.addVertexWithUV(dX2, (double)(y + 0), dZh, faceU3, faceV2);
			tessellator6.addVertexWithUV(dX2, (double)(y + 1), dZh, faceU3, faceV1);
			tessellator6.addVertexWithUV(dX2, (double)(y + 1), dZh, faceU1, faceV1);
			tessellator6.addVertexWithUV(dX2, (double)(y + 0), dZh, faceU1, faceV2);
			tessellator6.addVertexWithUV(dX1, (double)(y + 0), dZh, faceU3, faceV2);
			tessellator6.addVertexWithUV(dX1, (double)(y + 1), dZh, faceU3, faceV1);
			if(renderTop) {
				tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV2);
				tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV3);
				tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV3);
				tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV2);
				tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV2);
				tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV3);
				tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV3);
				tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV2);
			} else {
				if(y < maxHeight - 1 && renderBlocks.blockAccess.isAirBlock(x - 1, y + 1, z)) {
					tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dX1, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV1);
				}

				if(y < maxHeight - 1 && renderBlocks.blockAccess.isAirBlock(x + 1, y + 1, z)) {
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dX2, (double)(y + 1) + 0.01D, dZc1, sideU1, sideV3);
				}
			}

			if(renderBottom) {
				tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc2, sideU2, sideV2);
				tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc2, sideU2, sideV3);
				tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc1, sideU1, sideV3);
				tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc1, sideU1, sideV2);
				tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc2, sideU2, sideV2);
				tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc2, sideU2, sideV3);
				tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc1, sideU1, sideV3);
				tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc1, sideU1, sideV2);
			} else {
				if(y > 1 && renderBlocks.blockAccess.isAirBlock(x - 1, y - 1, z)) {
					tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dX1, (double)y - 0.01D, dZc1, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc1, sideU1, sideV1);
				}

				if(y > 1 && renderBlocks.blockAccess.isAirBlock(x + 1, y - 1, z)) {
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc2, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc2, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXh, (double)y - 0.01D, dZc1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dX2, (double)y - 0.01D, dZc1, sideU1, sideV3);
				}
			}
		}

		if((!connectN || !connectS) && (connectW || connectE || connectN || connectS)) {
			// Not both sides N/S at the same time
			if(connectN && !connectS) {
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZ1, faceU1, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZ1, faceU1, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZh, faceU2, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZh, faceU2, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZh, faceU1, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZh, faceU1, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZ1, faceU2, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZ1, faceU2, faceV1);
				if(!connectE && !connectW) {
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 0), dZh, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 0), dZh, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 0), dZh, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 0), dZh, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU2, sideV3);
				}

				if(renderTop || y < maxHeight - 1 && renderBlocks.blockAccess.isAirBlock(x, y + 1, z - 1)) {
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ1, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ1, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU1, sideV3);
				}

				if(renderBottom || y > 1 && renderBlocks.blockAccess.isAirBlock(x, y - 1, z - 1)) {
					tessellator6.addVertexWithUV(dXc1, (double)y, dZ1, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZh, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZh, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZ1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZh, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZ1, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZ1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZh, sideU1, sideV3);
				}
			} else if(!connectN && connectS) {
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZh, faceU2, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZh, faceU2, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZ2, faceU3, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZ2, faceU3, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZ2, faceU2, faceV1);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZ2, faceU2, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZh, faceU3, faceV2);
				tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZh, faceU3, faceV1);
				if(!connectE && !connectW) {
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 0), dZh, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 0), dZh, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 0), dZh, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 0), dZh, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU2, sideV3);
				}

				if(renderTop || y < maxHeight - 1 && renderBlocks.blockAccess.isAirBlock(x, y + 1, z + 1)) {
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ2, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ2, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ2, sideU2, sideV1);
				}

				if(renderBottom || y > 1 && renderBlocks.blockAccess.isAirBlock(x, y - 1, z + 1)) {
					tessellator6.addVertexWithUV(dXc1, (double)y, dZh, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZ2, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZ2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZh, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZ2, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZh, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZh, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZ2, sideU2, sideV1);
				}
			}
		} else {
			// Both sides N/S at the same time
			tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZ2, faceU1, faceV1);
			tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZ2, faceU1, faceV2);
			tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZ1, faceU3, faceV2);
			tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZ1, faceU3, faceV1);
			tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZ1, faceU1, faceV1);
			tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZ1, faceU1, faceV2);
			tessellator6.addVertexWithUV(dXh, (double)(y + 0), dZ2, faceU3, faceV2);
			tessellator6.addVertexWithUV(dXh, (double)(y + 1), dZ2, faceU3, faceV1);
			if(renderTop) {
				tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ2, sideU2, sideV2);
				tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ1, sideU2, sideV3);
				tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ1, sideU1, sideV3);
				tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ2, sideU1, sideV2);
				tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ1, sideU2, sideV2);
				tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ2, sideU2, sideV3);
				tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ2, sideU1, sideV3);
				tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ1, sideU1, sideV2);
			} else {
				if(y < maxHeight - 1 && renderBlocks.blockAccess.isAirBlock(x, y + 1, z - 1)) {
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ1, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ1, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU1, sideV3);
				}

				if(y < maxHeight - 1 && renderBlocks.blockAccess.isAirBlock(x, y + 1, z + 1)) {
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ2, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZ2, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)(y + 1), dZh, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZh, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)(y + 1), dZ2, sideU2, sideV1);
				}
			}

			if(renderBottom) {
				tessellator6.addVertexWithUV(dXc2, (double)y, dZ2, sideU2, sideV2);
				tessellator6.addVertexWithUV(dXc2, (double)y, dZ1, sideU2, sideV3);
				tessellator6.addVertexWithUV(dXc1, (double)y, dZ1, sideU1, sideV3);
				tessellator6.addVertexWithUV(dXc1, (double)y, dZ2, sideU1, sideV2);
				tessellator6.addVertexWithUV(dXc2, (double)y, dZ1, sideU2, sideV2);
				tessellator6.addVertexWithUV(dXc2, (double)y, dZ2, sideU2, sideV3);
				tessellator6.addVertexWithUV(dXc1, (double)y, dZ2, sideU1, sideV3);
				tessellator6.addVertexWithUV(dXc1, (double)y, dZ1, sideU1, sideV2);
			} else {
				if(y > 1 && renderBlocks.blockAccess.isAirBlock(x, y - 1, z - 1)) {
					tessellator6.addVertexWithUV(dXc1, (double)y, dZ1, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZh, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZh, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZ1, sideU1, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZh, sideU2, sideV3);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZ1, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZ1, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZh, sideU1, sideV3);
				}

				if(y > 1 && renderBlocks.blockAccess.isAirBlock(x, y - 1, z + 1)) {
					tessellator6.addVertexWithUV(dXc1, (double)y, dZh, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZ2, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZ2, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZh, sideU2, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZ2, sideU1, sideV1);
					tessellator6.addVertexWithUV(dXc1, (double)y, dZh, sideU1, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZh, sideU2, sideV2);
					tessellator6.addVertexWithUV(dXc2, (double)y, dZ2, sideU2, sideV1);
				}
			}
		}

		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}