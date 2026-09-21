package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.client.renderer.util.Texels;
import net.minecraft.world.level.tile.Block;

public class RenderBlockFire implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = block.getBlockTextureFromSide(0);
		if(renderBlocks.overrideBlockTexture >= 0) {
			i6 = renderBlocks.overrideBlockTexture;
		}

		tessellator5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator5.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		
		/*
		int i7 = (i6 & 15) << 4;
		int i8 = i6 & 0xff0;
		double d9 = (double)((float)i7 / 256F);
		double d11 = (double)(((float)i7 + 15.99F) / 256F);
		double d13 = (double)((float)i8 / 256F);
		double d15 = (double)(((float)i8 + 15.99F) / 256F);
		*/
		
		Idx2uvF.calc(i6);

		double d9 = (double)Idx2uvF.u1;
		double d11 = (double)Idx2uvF.u2;
		double d13 = (double)Idx2uvF.v1;
		double d15 = (double)Idx2uvF.v2;
		
		float f17 = 1.4F;
		double d20;
		double d22;
		double d24;
		double d26;
		double d28;
		double d30;
		double d32;
		if(!renderBlocks.blockAccess.isBlockNormalCube(x, y - 1, z) && !Block.fire.canBlockCatchFire(renderBlocks.blockAccess, x, y - 1, z)) {
			float f36 = 0.2F;
			float f19 = 0.0625F;
			if((x + y + z & 1) == 1) {
				/*
				d9 = (double)((float)i7 / 256F);
				d11 = (double)(((float)i7 + 15.99F) / 256F);
				d13 = (double)((float)(i8 + 16) / 256F);
				d15 = (double)(((float)i8 + 15.99F + 16.0F) / 256F);
				*/
				d13 = (double)Idx2uvF.v1 + Texels.texelsV(16.0F);
				d15 = (double)Idx2uvF.v2 + Texels.texelsV(16.0F);
			}

			if((x / 2 + y / 2 + z / 2 & 1) == 1) {
				d20 = d11;
				d11 = d9;
				d9 = d20;
			}

			if(Block.fire.canBlockCatchFire(renderBlocks.blockAccess, x - 1, y, z)) {
				tessellator5.addVertexWithUV((double)((float)x + f36), (double)((float)y + f17 + f19), (double)(z + 1), d11, d13);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f19), (double)(z + 1), d11, d15);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f19), (double)(z + 0), d9, d15);
				tessellator5.addVertexWithUV((double)((float)x + f36), (double)((float)y + f17 + f19), (double)(z + 0), d9, d13);
				tessellator5.addVertexWithUV((double)((float)x + f36), (double)((float)y + f17 + f19), (double)(z + 0), d9, d13);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f19), (double)(z + 0), d9, d15);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f19), (double)(z + 1), d11, d15);
				tessellator5.addVertexWithUV((double)((float)x + f36), (double)((float)y + f17 + f19), (double)(z + 1), d11, d13);
			}

			if(Block.fire.canBlockCatchFire(renderBlocks.blockAccess, x + 1, y, z)) {
				tessellator5.addVertexWithUV((double)((float)(x + 1) - f36), (double)((float)y + f17 + f19), (double)(z + 0), d9, d13);
				tessellator5.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f19), (double)(z + 0), d9, d15);
				tessellator5.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f19), (double)(z + 1), d11, d15);
				tessellator5.addVertexWithUV((double)((float)(x + 1) - f36), (double)((float)y + f17 + f19), (double)(z + 1), d11, d13);
				tessellator5.addVertexWithUV((double)((float)(x + 1) - f36), (double)((float)y + f17 + f19), (double)(z + 1), d11, d13);
				tessellator5.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f19), (double)(z + 1), d11, d15);
				tessellator5.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f19), (double)(z + 0), d9, d15);
				tessellator5.addVertexWithUV((double)((float)(x + 1) - f36), (double)((float)y + f17 + f19), (double)(z + 0), d9, d13);
			}

			if(Block.fire.canBlockCatchFire(renderBlocks.blockAccess, x, y, z - 1)) {
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17 + f19), (double)((float)z + f36), d11, d13);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f19), (double)(z + 0), d11, d15);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f19), (double)(z + 0), d9, d15);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17 + f19), (double)((float)z + f36), d9, d13);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17 + f19), (double)((float)z + f36), d9, d13);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f19), (double)(z + 0), d9, d15);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f19), (double)(z + 0), d11, d15);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17 + f19), (double)((float)z + f36), d11, d13);
			}

			if(Block.fire.canBlockCatchFire(renderBlocks.blockAccess, x, y, z + 1)) {
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17 + f19), (double)((float)(z + 1) - f36), d9, d13);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f19), (double)(z + 1 - 0), d9, d15);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f19), (double)(z + 1 - 0), d11, d15);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17 + f19), (double)((float)(z + 1) - f36), d11, d13);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17 + f19), (double)((float)(z + 1) - f36), d11, d13);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f19), (double)(z + 1 - 0), d11, d15);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f19), (double)(z + 1 - 0), d9, d15);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17 + f19), (double)((float)(z + 1) - f36), d9, d13);
			}

			if(Block.fire.canBlockCatchFire(renderBlocks.blockAccess, x, y + 1, z)) {
				d20 = (double)x + 0.5D + 0.5D;
				d22 = (double)x + 0.5D - 0.5D;
				d24 = (double)z + 0.5D + 0.5D;
				d26 = (double)z + 0.5D - 0.5D;
				d28 = (double)x + 0.5D - 0.5D;
				d30 = (double)x + 0.5D + 0.5D;
				d32 = (double)z + 0.5D - 0.5D;
				double d34 = (double)z + 0.5D + 0.5D;
				
				/*
				d9 = (double)((float)i7 / 256F);
				d11 = (double)(((float)i7 + 15.99F) / 256F);
				d13 = (double)((float)i8 / 256F);
				d15 = (double)(((float)i8 + 15.99F) / 256F);
				*/
				d9 = (double)Idx2uvF.u1;
				d11 = (double)Idx2uvF.u2;
				d13 = (double)Idx2uvF.v1;
				d15 = (double)Idx2uvF.v2;
				
				++y;
				f17 = -0.2F;
				if((x + y + z & 1) == 0) {
					tessellator5.addVertexWithUV(d28, (double)((float)y + f17), (double)(z + 0), d11, d13);
					tessellator5.addVertexWithUV(d20, (double)(y + 0), (double)(z + 0), d11, d15);
					tessellator5.addVertexWithUV(d20, (double)(y + 0), (double)(z + 1), d9, d15);
					tessellator5.addVertexWithUV(d28, (double)((float)y + f17), (double)(z + 1), d9, d13);
					
					/*
					d9 = (double)((float)i7 / 256F);
					d11 = (double)(((float)i7 + 15.99F) / 256F);
					d13 = (double)((float)(i8 + 16) / 256F);
					d15 = (double)(((float)i8 + 15.99F + 16.0F) / 256F);
					*/
					d13 = (double)Idx2uvF.v1 + Texels.texelsV(16.0F);
					d15 = (double)Idx2uvF.v2 + Texels.texelsV(16.0F);
					
					tessellator5.addVertexWithUV(d30, (double)((float)y + f17), (double)(z + 1), d11, d13);
					tessellator5.addVertexWithUV(d22, (double)(y + 0), (double)(z + 1), d11, d15);
					tessellator5.addVertexWithUV(d22, (double)(y + 0), (double)(z + 0), d9, d15);
					tessellator5.addVertexWithUV(d30, (double)((float)y + f17), (double)(z + 0), d9, d13);
				} else {
					tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17), d34, d11, d13);
					tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d26, d11, d15);
					tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d26, d9, d15);
					tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17), d34, d9, d13);
					/*
					d9 = (double)((float)i7 / 256F);
					d11 = (double)(((float)i7 + 15.99F) / 256F);
					d13 = (double)((float)(i8 + 16) / 256F);
					d15 = (double)(((float)i8 + 15.99F + 16.0F) / 256F);
					*/
					d13 = (double)Idx2uvF.v1 + Texels.texelsV(16.0F);
					d15 = (double)Idx2uvF.v2 + Texels.texelsV(16.0F);
					tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17), d32, d11, d13);
					tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d24, d11, d15);
					tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d24, d9, d15);
					tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17), d32, d9, d13);
				}
			}
		} else {
			double d18 = (double)x + 0.5D + 0.2D;
			d20 = (double)x + 0.5D - 0.2D;
			d22 = (double)z + 0.5D + 0.2D;
			d24 = (double)z + 0.5D - 0.2D;
			d26 = (double)x + 0.5D - 0.3D;
			d28 = (double)x + 0.5D + 0.3D;
			d30 = (double)z + 0.5D - 0.3D;
			d32 = (double)z + 0.5D + 0.3D;
			tessellator5.addVertexWithUV(d26, (double)((float)y + f17), (double)(z + 1), d11, d13);
			tessellator5.addVertexWithUV(d18, (double)(y + 0), (double)(z + 1), d11, d15);
			tessellator5.addVertexWithUV(d18, (double)(y + 0), (double)(z + 0), d9, d15);
			tessellator5.addVertexWithUV(d26, (double)((float)y + f17), (double)(z + 0), d9, d13);
			tessellator5.addVertexWithUV(d28, (double)((float)y + f17), (double)(z + 0), d11, d13);
			tessellator5.addVertexWithUV(d20, (double)(y + 0), (double)(z + 0), d11, d15);
			tessellator5.addVertexWithUV(d20, (double)(y + 0), (double)(z + 1), d9, d15);
			tessellator5.addVertexWithUV(d28, (double)((float)y + f17), (double)(z + 1), d9, d13);
			
			/*
			d9 = (double)((float)i7 / 256F);
			d11 = (double)(((float)i7 + 15.99F) / 256F);
			d13 = (double)((float)(i8 + 16) / 256F);
			d15 = (double)(((float)i8 + 15.99F + 16.0F) / 256F);
			*/
			d13 = (double)Idx2uvF.v1 + Texels.texelsV(16.0F);
			d15 = (double)Idx2uvF.v2 + Texels.texelsV(16.0F);
			
			tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17), d32, d11, d13);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d24, d11, d15);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d24, d9, d15);
			tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17), d32, d9, d13);
			tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17), d30, d11, d13);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d22, d11, d15);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d22, d9, d15);
			tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17), d30, d9, d13);
			d18 = (double)x + 0.5D - 0.5D;
			d20 = (double)x + 0.5D + 0.5D;
			d22 = (double)z + 0.5D - 0.5D;
			d24 = (double)z + 0.5D + 0.5D;
			d26 = (double)x + 0.5D - 0.4D;
			d28 = (double)x + 0.5D + 0.4D;
			d30 = (double)z + 0.5D - 0.4D;
			d32 = (double)z + 0.5D + 0.4D;
			tessellator5.addVertexWithUV(d26, (double)((float)y + f17), (double)(z + 0), d9, d13);
			tessellator5.addVertexWithUV(d18, (double)(y + 0), (double)(z + 0), d9, d15);
			tessellator5.addVertexWithUV(d18, (double)(y + 0), (double)(z + 1), d11, d15);
			tessellator5.addVertexWithUV(d26, (double)((float)y + f17), (double)(z + 1), d11, d13);
			tessellator5.addVertexWithUV(d28, (double)((float)y + f17), (double)(z + 1), d9, d13);
			tessellator5.addVertexWithUV(d20, (double)(y + 0), (double)(z + 1), d9, d15);
			tessellator5.addVertexWithUV(d20, (double)(y + 0), (double)(z + 0), d11, d15);
			tessellator5.addVertexWithUV(d28, (double)((float)y + f17), (double)(z + 0), d11, d13);
			
			/*
			d9 = (double)((float)i7 / 256F);
			d11 = (double)(((float)i7 + 15.99F) / 256F);
			d13 = (double)((float)i8 / 256F);
			d15 = (double)(((float)i8 + 15.99F) / 256F);
			*/
			d13 = (double)Idx2uvF.v1;
			d15 = (double)Idx2uvF.v2;
			
			tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17), d32, d9, d13);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d24, d9, d15);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d24, d11, d15);
			tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17), d32, d11, d13);
			tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f17), d30, d9, d13);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d22, d9, d15);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d22, d11, d15);
			tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f17), d30, d11, d13);
		}

		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}