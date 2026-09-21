package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.client.renderer.util.Texels;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.phys.Vec3D;

public class RenderBlockLever implements BlockRenderHandler {
	@Override
	public boolean renderBlock(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		int i5 = renderBlocks.blockAccess.getBlockMetadata(i2, i3, i4);
		int i6 = i5 & 7;
		boolean z7 = (i5 & 8) > 0;
		Tessellator tessellator8 = Tessellator.instance;
		boolean z9 = renderBlocks.overrideBlockTexture >= 0;
		if(!z9) {
			renderBlocks.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
		}

		float f10 = 0.25F;
		float f11 = 0.1875F;
		float f12 = 0.1875F;
		if(i6 == 5) {
			block1.setBlockBounds(0.5F - f11, 0.0F, 0.5F - f10, 0.5F + f11, f12, 0.5F + f10);
		} else if(i6 == 6) {
			block1.setBlockBounds(0.5F - f10, 0.0F, 0.5F - f11, 0.5F + f10, f12, 0.5F + f11);
		} else if(i6 == 4) {
			block1.setBlockBounds(0.5F - f11, 0.5F - f10, 1.0F - f12, 0.5F + f11, 0.5F + f10, 1.0F);
		} else if(i6 == 3) {
			block1.setBlockBounds(0.5F - f11, 0.5F - f10, 0.0F, 0.5F + f11, 0.5F + f10, f12);
		} else if(i6 == 2) {
			block1.setBlockBounds(1.0F - f12, 0.5F - f10, 0.5F - f11, 1.0F, 0.5F + f10, 0.5F + f11);
		} else if(i6 == 1) {
			block1.setBlockBounds(0.0F, 0.5F - f10, 0.5F - f11, f12, 0.5F + f10, 0.5F + f11);
		}

		RenderBlockUtil.renderStandardBlock(renderBlocks, block1, i2, i3, i4);
		if(!z9) {
			renderBlocks.overrideBlockTexture = -1;
		}

		tessellator8.setBrightness(block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4));
		float f13 = 1.0F;
		tessellator8.setColorOpaque_F(f13, f13, f13);
		int i14 = block1.getBlockTextureFromSide(0);
		if(renderBlocks.overrideBlockTexture >= 0) {
			i14 = renderBlocks.overrideBlockTexture;
		}

		Idx2uvF.calc(i14);

		double f17 = (double)Idx2uvF.u1;
		double f18 = (double)Idx2uvF.u2;
		double f19 = (double)Idx2uvF.v1;
		double f20 = (double)Idx2uvF.v2;
		
		Vec3D[] vec3D21 = new Vec3D[8];
		float f22 = 0.0625F;
		float f23 = 0.0625F;
		float f24 = 0.625F;
		vec3D21[0] = Vec3D.createVector((double)(-f22), 0.0D, (double)(-f23));
		vec3D21[1] = Vec3D.createVector((double)f22, 0.0D, (double)(-f23));
		vec3D21[2] = Vec3D.createVector((double)f22, 0.0D, (double)f23);
		vec3D21[3] = Vec3D.createVector((double)(-f22), 0.0D, (double)f23);
		vec3D21[4] = Vec3D.createVector((double)(-f22), (double)f24, (double)(-f23));
		vec3D21[5] = Vec3D.createVector((double)f22, (double)f24, (double)(-f23));
		vec3D21[6] = Vec3D.createVector((double)f22, (double)f24, (double)f23);
		vec3D21[7] = Vec3D.createVector((double)(-f22), (double)f24, (double)f23);

		for(int i25 = 0; i25 < 8; ++i25) {
			if(z7) {
				vec3D21[i25].zCoord -= 0.0625D;
				vec3D21[i25].rotateAroundX((float)Math.PI / 4.5F);
			} else {
				vec3D21[i25].zCoord += 0.0625D;
				vec3D21[i25].rotateAroundX(-0.69813174F);
			}

			if(i6 == 6) {
				vec3D21[i25].rotateAroundY((float)Math.PI / 2F);
			}

			if(i6 < 5) {
				vec3D21[i25].yCoord -= 0.375D;
				vec3D21[i25].rotateAroundX((float)Math.PI / 2F);
				if(i6 == 4) {
					vec3D21[i25].rotateAroundY(0.0F);
				}

				if(i6 == 3) {
					vec3D21[i25].rotateAroundY((float)Math.PI);
				}

				if(i6 == 2) {
					vec3D21[i25].rotateAroundY((float)Math.PI / 2F);
				}

				if(i6 == 1) {
					vec3D21[i25].rotateAroundY(-1.5707964F);
				}

				vec3D21[i25].xCoord += (double)i2 + 0.5D;
				vec3D21[i25].yCoord += (double)((float)i3 + 0.5F);
				vec3D21[i25].zCoord += (double)i4 + 0.5D;
			} else {
				vec3D21[i25].xCoord += (double)i2 + 0.5D;
				vec3D21[i25].yCoord += (double)((float)i3 + 0.125F);
				vec3D21[i25].zCoord += (double)i4 + 0.5D;
			}
		}

		Vec3D vec3D30 = null;
		Vec3D vec3D26 = null;
		Vec3D vec3D27 = null;
		Vec3D vec3D28 = null;

		for(int i29 = 0; i29 < 6; ++i29) {
			if(i29 == 0) {
				f17 = Idx2uvF.u1 + Texels.texelsU(7.0F);
				f18 = Idx2uvF.u1 + Texels.texelsU(9.0F);
				f19 = Idx2uvF.v1 + Texels.texelsV(6.0F);
				f20 = Idx2uvF.v1 + Texels.texelsV(8.0F);
			} else if(i29 == 2) {
				f17 = Idx2uvF.u1 + Texels.texelsU(7.0F);
				f18 = Idx2uvF.u1 + Texels.texelsU(9.0F);
				f19 = Idx2uvF.v1 + Texels.texelsV(6.0F);
				f20 = Idx2uvF.v1 + Texels.texelsV(16.0F);
			}

			if(i29 == 0) {
				vec3D30 = vec3D21[0];
				vec3D26 = vec3D21[1];
				vec3D27 = vec3D21[2];
				vec3D28 = vec3D21[3];
			} else if(i29 == 1) {
				vec3D30 = vec3D21[7];
				vec3D26 = vec3D21[6];
				vec3D27 = vec3D21[5];
				vec3D28 = vec3D21[4];
			} else if(i29 == 2) {
				vec3D30 = vec3D21[1];
				vec3D26 = vec3D21[0];
				vec3D27 = vec3D21[4];
				vec3D28 = vec3D21[5];
			} else if(i29 == 3) {
				vec3D30 = vec3D21[2];
				vec3D26 = vec3D21[1];
				vec3D27 = vec3D21[5];
				vec3D28 = vec3D21[6];
			} else if(i29 == 4) {
				vec3D30 = vec3D21[3];
				vec3D26 = vec3D21[2];
				vec3D27 = vec3D21[6];
				vec3D28 = vec3D21[7];
			} else if(i29 == 5) {
				vec3D30 = vec3D21[0];
				vec3D26 = vec3D21[3];
				vec3D27 = vec3D21[7];
				vec3D28 = vec3D21[4];
			}

			tessellator8.addVertexWithUV(vec3D30.xCoord, vec3D30.yCoord, vec3D30.zCoord, (double)f17, (double)f20);
			tessellator8.addVertexWithUV(vec3D26.xCoord, vec3D26.yCoord, vec3D26.zCoord, (double)f18, (double)f20);
			tessellator8.addVertexWithUV(vec3D27.xCoord, vec3D27.yCoord, vec3D27.zCoord, (double)f18, (double)f19);
			tessellator8.addVertexWithUV(vec3D28.xCoord, vec3D28.yCoord, vec3D28.zCoord, (double)f17, (double)f19);
		}

		return true;
	}

	@Override
	public boolean renderItemIn3d() {
		return false;
	}
}