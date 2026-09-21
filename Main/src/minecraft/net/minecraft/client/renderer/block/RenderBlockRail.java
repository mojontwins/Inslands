package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockRail;

public class RenderBlockRail implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		BlockRail blockRail = (BlockRail)block;
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		int i7 = blockRail.getBlockTextureFromSideAndMetadata(0, i6);
		if(renderBlocks.overrideBlockTexture >= 0) {
			i7 = renderBlocks.overrideBlockTexture;
		}

		if(blockRail.isPowered()) {
			i6 &= 7;
		}

		tessellator5.setBrightness(blockRail.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		tessellator5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		/*
		int i8 = (i7 & 15) << 4;
		int i9 = i7 & 0xff0;
		double d10 = (double)((float)i8 / 256F);
		double d12 = (double)(((float)i8 + 15.99F) / 256F);
		double d14 = (double)((float)i9 / 256F);
		double d16 = (double)(((float)i9 + 15.99F) / 256F);
		*/
		Idx2uvF.calc(i7);
		double d10 = Idx2uvF.u1;
		double d12 = Idx2uvF.u2;
		double d14 = Idx2uvF.v1;
		double d16 = Idx2uvF.v2;
		
		double d18 = 0.0625D;
		double d20 = (double)(x + 1);
		double d22 = (double)(x + 1);
		double d24 = (double)(x + 0);
		double d26 = (double)(x + 0);
		double d28 = (double)(z + 0);
		double d30 = (double)(z + 1);
		double d32 = (double)(z + 1);
		double d34 = (double)(z + 0);
		double d36 = (double)y + d18;
		double d38 = (double)y + d18;
		double d40 = (double)y + d18;
		double d42 = (double)y + d18;
		if(i6 != 1 && i6 != 2 && i6 != 3 && i6 != 7) {
			if(i6 == 8) {
				d20 = d22 = (double)(x + 0);
				d24 = d26 = (double)(x + 1);
				d28 = d34 = (double)(z + 1);
				d30 = d32 = (double)(z + 0);
			} else if(i6 == 9) {
				d20 = d26 = (double)(x + 0);
				d22 = d24 = (double)(x + 1);
				d28 = d30 = (double)(z + 0);
				d32 = d34 = (double)(z + 1);
			}
		} else {
			d20 = d26 = (double)(x + 1);
			d22 = d24 = (double)(x + 0);
			d28 = d30 = (double)(z + 1);
			d32 = d34 = (double)(z + 0);
		}

		if(i6 != 2 && i6 != 4) {
			if(i6 == 3 || i6 == 5) {
				++d38;
				++d40;
			}
		} else {
			++d36;
			++d42;
		}

		tessellator5.addVertexWithUV(d20, d36, d28, d12, d14);
		tessellator5.addVertexWithUV(d22, d38, d30, d12, d16);
		tessellator5.addVertexWithUV(d24, d40, d32, d10, d16);
		tessellator5.addVertexWithUV(d26, d42, d34, d10, d14);
		tessellator5.addVertexWithUV(d26, d42, d34, d10, d14);
		tessellator5.addVertexWithUV(d24, d40, d32, d10, d16);
		tessellator5.addVertexWithUV(d22, d38, d30, d12, d16);
		tessellator5.addVertexWithUV(d20, d36, d28, d12, d14);
		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}