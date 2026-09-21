package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.client.renderer.util.Texels;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockRedstoneWire;
import org.lwjgl.opengl.GL11;

public class RenderBlockRedstoneWire implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		
		int i7 = block.getBlockTextureFromSideAndMetadata(1, i6);
		if(renderBlocks.overrideBlockTexture >= 0) {
			i7 = renderBlocks.overrideBlockTexture;
		}

		tessellator5.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
		float f8 = 1.0F;
		float f9 = (float)i6 / 15.0F;
		float f10 = f9 * 0.6F + 0.4F;
		if(i6 == 0) {
			f10 = 0.3F;
		}

		float f11 = f9 * f9 * 0.7F - 0.5F;
		float f12 = f9 * f9 * 0.6F - 0.7F;
		if(f11 < 0.0F) {
			f11 = 0.0F;
		}

		if(f12 < 0.0F) {
			f12 = 0.0F;
		}

		tessellator5.setColorOpaque_F(f10, f11, f12);
		/*
		int i13 = (i7 & 15) << 4;
		int i14 = i7 & 0xff0;
		double u1 = (double)((float)i13 / TextureAtlasSize.w);
		double u2 = (double)(((float)i13 + 15.99F) / TextureAtlasSize.w);
		double v1 = (double)((float)i14 / TextureAtlasSize.h);
		double v2 = (double)(((float)i14 + 15.99F) / TextureAtlasSize.h);
		*/
		Idx2uvF.calc(i7);

		double u1 = (double)Idx2uvF.u1;
		double u2 = (double)Idx2uvF.u2;
		double v1 = (double)Idx2uvF.v1;
		double v2 = (double)Idx2uvF.v2;
		
		double u10 = u1; 
		double u20 = u2;	
		
		boolean z29 = BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x - 1, y, z, 1) || !renderBlocks.blockAccess.isBlockNormalCube(x - 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x - 1, y - 1, z, -1);
		boolean z30 = BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x + 1, y, z, 3) || !renderBlocks.blockAccess.isBlockNormalCube(x + 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x + 1, y - 1, z, -1);
		boolean z31 = BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x, y, z - 1, 2) || !renderBlocks.blockAccess.isBlockNormalCube(x, y, z - 1) && BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x, y - 1, z - 1, -1);
		boolean z32 = BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x, y, z + 1, 0) || !renderBlocks.blockAccess.isBlockNormalCube(x, y, z + 1) && BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x, y - 1, z + 1, -1);
		if(!renderBlocks.blockAccess.isBlockNormalCube(x, y + 1, z)) {
			if(renderBlocks.blockAccess.isBlockNormalCube(x - 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x - 1, y + 1, z, -1)) {
				z29 = true;
			}

			if(renderBlocks.blockAccess.isBlockNormalCube(x + 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x + 1, y + 1, z, -1)) {
				z30 = true;
			}

			if(renderBlocks.blockAccess.isBlockNormalCube(x, y, z - 1) && BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x, y + 1, z - 1, -1)) {
				z31 = true;
			}

			if(renderBlocks.blockAccess.isBlockNormalCube(x, y, z + 1) && BlockRedstoneWire.isPowerProviderOrWire(renderBlocks.blockAccess, x, y + 1, z + 1, -1)) {
				z32 = true;
			}
		}

		float f34 = (float)(x + 0);
		float f35 = (float)(x + 1);
		float f36 = (float)(z + 0);
		float f37 = (float)(z + 1);
		byte b38 = 0;
		if((z29 || z30) && !z31 && !z32) {
			b38 = 1;
		}

		if((z31 || z32) && !z30 && !z29) {
			b38 = 2;
		}

		if(b38 != 0) {
			u1 = u10 + Texels.texelsU(16.0F);
			u2 = u20 + Texels.texelsU(16.0F);
		}

		if(b38 == 0) {
			if(!z29) {
				f34 += 0.3125F;
				}

			if(!z29) {
				u1 += Texels.texelsU(5); // 0.01953125D;
				}

			if(!z30) {
				f35 -= 0.3125F;
				}

			if(!z30) {
				u2 -= Texels.texelsU(5); // 0.01953125D;
				}

			if(!z31) {
				f36 += 0.3125F;
				}

			if(!z31) {
				v1 += Texels.texelsV(5); // 0.01953125D;
				}

			if(!z32) {
				f37 -= 0.3125F;
				}

			if(!z32) {
				v2 -= Texels.texelsV(5); // 0.01953125D;
			}

			tessellator5.addVertexWithUV((double)f35, (double)y + 0.015625D, (double)f37, u2, v2);
			tessellator5.addVertexWithUV((double)f35, (double)y + 0.015625D, (double)f36, u2, v1);
			tessellator5.addVertexWithUV((double)f34, (double)y + 0.015625D, (double)f36, u1, v1);
			tessellator5.addVertexWithUV((double)f34, (double)y + 0.015625D, (double)f37, u1, v2);
		} else if(b38 == 1) {
			tessellator5.addVertexWithUV((double)f35, (double)y + 0.015625D, (double)f37, u2, v2);
			tessellator5.addVertexWithUV((double)f35, (double)y + 0.015625D, (double)f36, u2, v1);
			tessellator5.addVertexWithUV((double)f34, (double)y + 0.015625D, (double)f36, u1, v1);
			tessellator5.addVertexWithUV((double)f34, (double)y + 0.015625D, (double)f37, u1, v2);
		} else if(b38 == 2) {
			tessellator5.addVertexWithUV((double)f35, (double)y + 0.015625D, (double)f37, u2, v2);
			tessellator5.addVertexWithUV((double)f35, (double)y + 0.015625D, (double)f36, u1, v2);
			tessellator5.addVertexWithUV((double)f34, (double)y + 0.015625D, (double)f36, u1, v1);
			tessellator5.addVertexWithUV((double)f34, (double)y + 0.015625D, (double)f37, u2, v1);
		}

		if(!renderBlocks.blockAccess.isBlockNormalCube(x, y + 1, z)) {
			u1 = u10 + Texels.texelsU(16.0F);
			u2 = u20 + Texels.texelsU(16.0F);
			
			if(renderBlocks.blockAccess.isBlockNormalCube(x - 1, y, z) && renderBlocks.blockAccess.getBlockID(x - 1, y + 1, z) == Block.redstoneWire.blockID) {
				tessellator5.setColorOpaque_F(f8 * f10, f8 * f11, f8 * f12);
				tessellator5.addVertexWithUV((double)x + 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 1), u2, v1);
				tessellator5.addVertexWithUV((double)x + 0.015625D, (double)(y + 0), (double)(z + 1), u1, v1);
				tessellator5.addVertexWithUV((double)x + 0.015625D, (double)(y + 0), (double)(z + 0), u1, v2);
				tessellator5.addVertexWithUV((double)x + 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 0), u2, v2);
			}

			if(renderBlocks.blockAccess.isBlockNormalCube(x + 1, y, z) && renderBlocks.blockAccess.getBlockID(x + 1, y + 1, z) == Block.redstoneWire.blockID) {
				tessellator5.setColorOpaque_F(f8 * f10, f8 * f11, f8 * f12);
				tessellator5.addVertexWithUV((double)(x + 1) - 0.015625D, (double)(y + 0), (double)(z + 1), u1, v2);
				tessellator5.addVertexWithUV((double)(x + 1) - 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 1), u2, v2);
				tessellator5.addVertexWithUV((double)(x + 1) - 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 0), u2, v1);
				tessellator5.addVertexWithUV((double)(x + 1) - 0.015625D, (double)(y + 0), (double)(z + 0), u1, v1);
			}

			if(renderBlocks.blockAccess.isBlockNormalCube(x, y, z - 1) && renderBlocks.blockAccess.getBlockID(x, y + 1, z - 1) == Block.redstoneWire.blockID) {
				tessellator5.setColorOpaque_F(f8 * f10, f8 * f11, f8 * f12);
				tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)z + 0.015625D, u1, v2);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 1) + 0.021875F), (double)z + 0.015625D, u2, v2);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 1) + 0.021875F), (double)z + 0.015625D, u2, v1);
				tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)z + 0.015625D, u1, v1);
			}

			if(renderBlocks.blockAccess.isBlockNormalCube(x, y, z + 1) && renderBlocks.blockAccess.getBlockID(x, y + 1, z + 1) == Block.redstoneWire.blockID) {
				tessellator5.setColorOpaque_F(f8 * f10, f8 * f11, f8 * f12);
				tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 1) + 0.021875F), (double)(z + 1) - 0.015625D, u2, v1);
				tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)(z + 1) - 0.015625D, u1, v1);
				tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)(z + 1) - 0.015625D, u1, v2);
				tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 1) + 0.021875F), (double)(z + 1) - 0.015625D, u2, v2);
			}
		}

		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}