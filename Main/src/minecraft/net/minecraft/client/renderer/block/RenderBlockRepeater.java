package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockRedstoneRepeater;

public class RenderBlockRepeater implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		int i5 = renderBlocks.blockAccess.getBlockMetadata(i2, i3, i4);
		int i6 = i5 & 3;
		int i7 = (i5 & 12) >> 2;
		RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
		Tessellator tessellator8 = Tessellator.instance;
		tessellator8.setBrightness(block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4));
		tessellator8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d9 = -0.1875D;
		double d11 = 0.0D;
		double d13 = 0.0D;
		double d15 = 0.0D;
		double d17 = 0.0D;
		switch(i6) {
		case 0:
			d17 = -0.3125D;
			d13 = BlockRedstoneRepeater.repeaterTorchOffset[i7];
			break;
		case 1:
			d15 = 0.3125D;
			d11 = -BlockRedstoneRepeater.repeaterTorchOffset[i7];
			break;
		case 2:
			d17 = 0.3125D;
			d13 = -BlockRedstoneRepeater.repeaterTorchOffset[i7];
			break;
		case 3:
			d15 = -0.3125D;
			d11 = BlockRedstoneRepeater.repeaterTorchOffset[i7];
		}

		RenderBlockUtil.renderTorchAtAngle(renderBlocks, block1, (double)i2 + d11, (double)i3 + d9, (double)i4 + d13, 0.0D, 0.0D);
		RenderBlockUtil.renderTorchAtAngle(renderBlocks, block1, (double)i2 + d15, (double)i3 + d9, (double)i4 + d17, 0.0D, 0.0D);
		int i19 = block1.getBlockTextureFromSide(1);
		Idx2uvF.calc(i19);
		double u1 = Idx2uvF.u1;
		double u2 = Idx2uvF.u2;
		double v1 = Idx2uvF.v1;
		double v2 = Idx2uvF.v2;

		double d30 = 0.125D;
		double d32 = (double)(i2 + 1);
		double d34 = (double)(i2 + 1);
		double d36 = (double)(i2 + 0);
		double d38 = (double)(i2 + 0);
		double d40 = (double)(i4 + 0);
		double d42 = (double)(i4 + 1);
		double d44 = (double)(i4 + 1);
		double d46 = (double)(i4 + 0);
		double d48 = (double)i3 + d30;
		if(i6 == 2) {
			d32 = d34 = (double)(i2 + 0);
			d36 = d38 = (double)(i2 + 1);
			d40 = d46 = (double)(i4 + 1);
			d42 = d44 = (double)(i4 + 0);
		} else if(i6 == 3) {
			d32 = d38 = (double)(i2 + 0);
			d34 = d36 = (double)(i2 + 1);
			d40 = d42 = (double)(i4 + 0);
			d44 = d46 = (double)(i4 + 1);
		} else if(i6 == 1) {
			d32 = d38 = (double)(i2 + 1);
			d34 = d36 = (double)(i2 + 0);
			d40 = d42 = (double)(i4 + 1);
			d44 = d46 = (double)(i4 + 0);
		}

		tessellator8.addVertexWithUV(d38, d48, d46, u1, v1);
		tessellator8.addVertexWithUV(d36, d48, d44, u1, v2);
		tessellator8.addVertexWithUV(d34, d48, d42, u2, v2);
		tessellator8.addVertexWithUV(d32, d48, d40, u2, v1);
		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}