package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.TextureAtlasSize;
import net.minecraft.util.MathHelper;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockFluid;

public class RenderBlockFluid implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = block1.colorMultiplier(renderBlocks.blockAccess, i2, i3, i4);
		float f7 = (float)(i6 >> 16 & 255) / 255.0F;
		float f8 = (float)(i6 >> 8 & 255) / 255.0F;
		float f9 = (float)(i6 & 255) / 255.0F;
		boolean z10 = block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3 + 1, i4, 1);
		boolean z11 = block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3 - 1, i4, 0);
		boolean[] z12 = new boolean[]{block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3, i4 - 1, 2), block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3, i4 + 1, 3), block1.shouldSideBeRendered(renderBlocks.blockAccess, i2 - 1, i3, i4, 4), block1.shouldSideBeRendered(renderBlocks.blockAccess, i2 + 1, i3, i4, 5)};
		if(!z10 && !z11 && !z12[0] && !z12[1] && !z12[2] && !z12[3]) {
			return false;
		} else {
			boolean z13 = false;
			float f14 = 0.5F;
			float f15 = 1.0F;
			float f16 = 0.8F;
			float f17 = 0.6F;
			double d18 = 0.0D;
			double d20 = 1.0D;
			Material material22 = block1.blockMaterial;
			int i23 = renderBlocks.blockAccess.getBlockMetadata(i2, i3, i4);
			
			double d24 = (double)RenderBlockUtil.getFluidHeight(renderBlocks, i2, i3, i4, material22);
			double d26 = (double)RenderBlockUtil.getFluidHeight(renderBlocks, i2, i3, i4 + 1, material22);
			double d28 = (double)RenderBlockUtil.getFluidHeight(renderBlocks, i2 + 1, i3, i4 + 1, material22);
			double d30 = (double)RenderBlockUtil.getFluidHeight(renderBlocks, i2 + 1, i3, i4, material22);

			double d32 = 0.0010000000474974513D;
			int i34;
			int i37;
			if(renderBlocks.renderAllFaces || z10) {
				z13 = true;
				i34 = block1.getBlockTextureFromSideAndMetadata(1, i23);
				float f35 = (float)BlockFluid.func_293_a(renderBlocks.blockAccess, i2, i3, i4, material22);
				if(f35 > -999.0F) {
					i34 = block1.getBlockTextureFromSideAndMetadata(2, i23);
				}

				d24 -= d32;
				d26 -= d32;
				d28 -= d32;
				d30 -= d32;

				int i36 = (i34 & 15) << 4;
				    i37 = i34 & 0xff0;

				double uCenter, vCenter;
				
				if(f35 < -999.0F) {
					f35 = 0.0F;
					uCenter = ((double)i36 + 8.0D) / TextureAtlasSize.w;
					vCenter = ((double)i37 + 8.0D) / TextureAtlasSize.h;
				} else {
					uCenter = (double)((float)(i36 + 16) / TextureAtlasSize.w);
					vCenter = (double)((float)(i37 + 16) / TextureAtlasSize.h);
				}

				double d42 = (double)(MathHelper.sin(f35) * 8.0F);
				double d44 = (double)(MathHelper.cos(f35) * 8.0F);

				double u1 = (d44 - d42) / TextureAtlasSize.w;
				double u2 = (d44 + d42) / TextureAtlasSize.w;
				double v1 = (d44 - d42) / TextureAtlasSize.h;
				double v2 = (d44 + d42) / TextureAtlasSize.h;

				tessellator5.setBrightness(block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4));
				float f46 = 1.0F;
				tessellator5.setColorOpaque_F(f15 * f46 * f7, f15 * f46 * f8, f15 * f46 * f9);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)i3 + d24, (double)(i4 + 0), uCenter - u2, vCenter - v1);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)i3 + d26, (double)(i4 + 1), uCenter - u1, vCenter + v2);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)i3 + d28, (double)(i4 + 1), uCenter + u2, vCenter + v1);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)i3 + d30, (double)(i4 + 0), uCenter + u1, vCenter - v2);
			}

			if(renderBlocks.renderAllFaces || z11) {
				tessellator5.setBrightness(block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4));
				float f64 = 1.0F;
				tessellator5.setColorOpaque_F(f14 * f64 * f7, f14 * f64 * f8, f14 * f64 * f9);
				renderBlocks.renderBottomFace(block1, (double)i2, (double)i3 + d32, (double)i4, block1.getBlockTextureFromSide(0));
				z13 = true;
			}

			for(i34 = 0; i34 < 4; ++i34) {
				int i65 = i2;
				i37 = i4;
				if(i34 == 0) {
					i37 = i4 - 1;
				}

				if(i34 == 1) {
					++i37;
				}

				if(i34 == 2) {
					i65 = i2 - 1;
				}

				if(i34 == 3) {
					++i65;
				}

				if(renderBlocks.renderAllFaces || z12[i34]) {
					double d41;
					double d43;
					double d45;
					double d47;
					double d49;
					double d51;
					if(i34 == 0) {
						d41 = d24;
						d43 = d30;
						d45 = (double)i2;
						d49 = (double)(i2 + 1);
						d47 = (double)i4 + d32;
						d51 = (double)i4 + d32;
					} else if(i34 == 1) {
						d41 = d28;
						d43 = d26;
						d45 = (double)(i2 + 1);
						d49 = (double)i2;
						d47 = (double)(i4 + 1) - d32;
						d51 = (double)(i4 + 1) - d32;
					} else if(i34 == 2) {
						d41 = d26;
						d43 = d24;
						d45 = (double)i2 + d32;
						d49 = (double)i2 + d32;
						d47 = (double)(i4 + 1);
						d51 = (double)i4;
					} else {
						d41 = d30;
						d43 = d28;
						d45 = (double)(i2 + 1) - d32;
						d49 = (double)(i2 + 1) - d32;
						d47 = (double)i4;
						d51 = (double)(i4 + 1);
					}

					z13 = true;
					int i66 = block1.getBlockTextureFromSideAndMetadata(i34 + 2, i23);
					
					int i39 = (i66 & 15) << 4;
					int i67 = i66 & 0xff0;

					double d53 = (double)((float)(i39 + 0) / TextureAtlasSize.w);
					double d55 = ((double)(i39 + 16) - 0.01D) / TextureAtlasSize.w;
					double d57 = ((double)i67 + (1.0D - d41) * 16.0D) / TextureAtlasSize.h;
					double d59 = ((double)i67 + (1.0D - d43) * 16.0D) / TextureAtlasSize.h;
					double d61 = ((double)(i67 + 16) - 0.01D) / TextureAtlasSize.h;
					
					tessellator5.setBrightness(block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i65, i3, i37));
					float f63 = 1.0F;
					if(i34 < 2) {
						f63 *= f16;
					} else {
						f63 *= f17;
					}

					tessellator5.setColorOpaque_F(f15 * f63 * f7, f15 * f63 * f8, f15 * f63 * f9);
					tessellator5.addVertexWithUV(d45, (double)i3 + d41, d47, d53, d57);
					tessellator5.addVertexWithUV(d49, (double)i3 + d43, d51, d55, d59);
					tessellator5.addVertexWithUV(d49, (double)(i3 + 0), d51, d55, d61);
					tessellator5.addVertexWithUV(d45, (double)(i3 + 0), d47, d53, d61);
				}
			}

			block1.minY = d18;
			block1.maxY = d20;
			return z13;
		}
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}