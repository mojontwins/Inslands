package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.block.BlockRenderHandler;
import net.minecraft.client.renderer.block.BlockRenderType;
import net.minecraft.client.renderer.block.RenderBlockUtil;
import net.minecraft.client.renderer.util.TextureAtlasSize;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class RenderBlocks {
	public IBlockAccess blockAccess;
	public int overrideBlockTexture = -1;
	public boolean flipTexture = false;
	public boolean renderAllFaces = false;
	public static boolean fancyGrass = false;
	public boolean useInventoryTint = true;
	public int uvRotateEast = 0;
	public int uvRotateWest = 0;
	public int uvRotateSouth = 0;
	public int uvRotateNorth = 0;
	public int uvRotateTop = 0;
	public int uvRotateBottom = 0;
	public boolean enableAO;
	public float lightValueOwn;
	public float aoLightValueXNeg;
	public float aoLightValueYNeg;
	public float aoLightValueZNeg;
	public float aoLightValueXPos;
	public float aoLightValueYPos;
	public float aoLightValueZPos;
	public float aoLightValueScratchXYZNNN;
	public float aoLightValueScratchXYNN;
	public float aoLightValueScratchXYZNNP;
	public float aoLightValueScratchYZNN;
	public float aoLightValueScratchYZNP;
	public float aoLightValueScratchXYZPNN;
	public float aoLightValueScratchXYPN;
	public float aoLightValueScratchXYZPNP;
	public float aoLightValueScratchXYZNPN;
	public float aoLightValueScratchXYNP;
	public float aoLightValueScratchXYZNPP;
	public float aoLightValueScratchYZPN;
	public float aoLightValueScratchXYZPPN;
	public float aoLightValueScratchXYPP;
	public float aoLightValueScratchYZPP;
	public float aoLightValueScratchXYZPPP;
	public float aoLightValueScratchXZNN;
	public float aoLightValueScratchXZPN;
	public float aoLightValueScratchXZNP;
	public float aoLightValueScratchXZPP;
	public int aoBrightnessXYZNNN;
	public int aoBrightnessXYNN;
	public int aoBrightnessXYZNNP;
	public int aoBrightnessYZNN;
	public int aoBrightnessYZNP;
	public int aoBrightnessXYZPNN;
	public int aoBrightnessXYPN;
	public int aoBrightnessXYZPNP;
	public int aoBrightnessXYZNPN;
	public int aoBrightnessXYNP;
	public int aoBrightnessXYZNPP;
	public int aoBrightnessYZPN;
	public int aoBrightnessXYZPPN;
	public int aoBrightnessXYPP;
	public int aoBrightnessYZPP;
	public int aoBrightnessXYZPPP;
	public int aoBrightnessXZNN;
	public int aoBrightnessXZPN;
	public int aoBrightnessXZNP;
	public int aoBrightnessXZPP;
	public int aoType = 1;
	public int brightnessTopLeft;
	public int brightnessBottomLeft;
	public int brightnessBottomRight;
	public int brightnessTopRight;
	public float colorRedTopLeft;
	public float colorRedBottomLeft;
	public float colorRedBottomRight;
	public float colorRedTopRight;
	public float colorGreenTopLeft;
	public float colorGreenBottomLeft;
	public float colorGreenBottomRight;
	public float colorGreenTopRight;
	public float colorBlueTopLeft;
	public float colorBlueBottomLeft;
	public float colorBlueBottomRight;
	public float colorBlueTopRight;
	public boolean aoGrassXYZCPN;
	public boolean aoGrassXYZPPC;
	public boolean aoGrassXYZNPC;
	public boolean aoGrassXYZCPP;
	public boolean aoGrassXYZNCN;
	public boolean aoGrassXYZPCP;
	public boolean aoGrassXYZNCP;
	public boolean aoGrassXYZPCN;
	public boolean aoGrassXYZCNN;
	public boolean aoGrassXYZPNC;
	public boolean aoGrassXYZNNC;
	public boolean aoGrassXYZCNP;
	
	public int activeRenderPass = 0;

	public static float[][] redstoneColors = new float[16][];
	
	public RenderBlocks(IBlockAccess iBlockAccess1) {
		this.blockAccess = iBlockAccess1;
	}

	public RenderBlocks() {
	}
	
	public int getActiveRenderPass() {
		return activeRenderPass;
	}

	public void setActiveRenderPass(int activeRenderPass) {
		this.activeRenderPass = activeRenderPass;
	}

	public void renderBlockUsingTexture(Block block1, int i2, int i3, int i4, int i5) {
		this.overrideBlockTexture = i5;
		this.renderBlockByRenderType(block1, i2, i3, i4);
		this.overrideBlockTexture = -1;
	}

	public void func_31075_a(Block block1, int i2, int i3, int i4) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(block1, i2, i3, i4);
		this.renderAllFaces = false;
	}

	public boolean renderBlockByRenderType(Block block, int x, int y, int z) {
		block.setBlockBoundsBasedOnState(this.blockAccess, x, y, z);
		BlockRenderHandler handler = BlockRenderType.get(block.getRenderType());
		return handler != null && handler.renderBlock(this, block, x, y, z);
	}

	public void renderBlockFallingSand(Block block1, World world2, int i3, int i4, int i5) {
		float f6 = 0.5F;
		float f7 = 1.0F;
		float f8 = 0.8F;
		float f9 = 0.6F;
		Tessellator tessellator10 = Tessellator.instance;
		tessellator10.startDrawingQuads();
		tessellator10.setBrightness(block1.getMixedBrightnessForBlock(world2, i3, i4, i5));

		tessellator10.setColorOpaque_F(f6, f6, f6);
		this.renderBottomFace(block1, -0.5D, -0.5D, -0.5D, block1.getBlockTextureFromSide(0));
		
		tessellator10.setColorOpaque_F(f7, f7, f7);
		this.renderTopFace(block1, -0.5D, -0.5D, -0.5D, block1.getBlockTextureFromSide(1));
		
		tessellator10.setColorOpaque_F(f8, f8, f8);
		this.renderEastFace(block1, -0.5D, -0.5D, -0.5D, block1.getBlockTextureFromSide(2));
		
		tessellator10.setColorOpaque_F(f8, f8, f8);
		this.renderWestFace(block1, -0.5D, -0.5D, -0.5D, block1.getBlockTextureFromSide(3));

		tessellator10.setColorOpaque_F(f9, f9, f9);
		this.renderNorthFace(block1, -0.5D, -0.5D, -0.5D, block1.getBlockTextureFromSide(4));
		
		tessellator10.setColorOpaque_F(f9, f9, f9);
		this.renderSouthFace(block1, -0.5D, -0.5D, -0.5D, block1.getBlockTextureFromSide(5));
		tessellator10.draw();
	}

	public boolean renderStandardBlock(Block block, int x, int y, int z) {
		return RenderBlockUtil.renderStandardBlock(this, block, x, y, z);
	}
	public void renderBottomFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		final double taw = TextureAtlasSize.w;
		final double tah = TextureAtlasSize.h;
		
		double u1, u2, v1, v2;
		double ru2, ru1, rv1, rv2;

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 0xff0;
				;
		if(this.uvRotateBottom == 2) {
			u1 = ((double)i10 + block1.minZ * 16.0D) / taw;
			v1 = ((double)(i11 + 16) - block1.maxX * 16.0D) / taw;
			u2 = ((double)i10 + block1.maxZ * 16.0D) / tah;
			v2 = ((double)(i11 + 16) - block1.minX * 16.0D) / tah;
			rv1 = v1;
			rv2 = v2;
			ru2 = u1;
			ru1 = u2;
			v1 = v2;
			v2 = rv1;
		} else if(this.uvRotateBottom == 1) {
			u1 = ((double)(i10 + 16) - block1.maxZ * 16.0D) / taw;
			v1 = ((double)i11 + block1.minX * 16.0D) / tah;
			u2 = ((double)(i10 + 16) - block1.minZ * 16.0D) / taw;
			v2 = ((double)i11 + block1.maxX * 16.0D) / tah;
			ru2 = u2;
			ru1 = u1;
			u1 = u2;
			u2 = ru1;
			rv1 = v2;
			rv2 = v1;
		} else if(this.uvRotateBottom == 3) {
			u1 = ((double)(i10 + 16) - block1.minX * 16.0D) / taw;
			u2 = ((double)(i10 + 16) - block1.maxX * 16.0D - 0.01D) / taw;
			v1 = ((double)(i11 + 16) - block1.minZ * 16.0D) / tah;
			v2 = ((double)(i11 + 16) - block1.maxZ * 16.0D - 0.01D) / tah;
			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		} else {
			u1 = ((double)i10 + block1.minX * 16.0D) / taw;
			u2 = ((double)i10 + block1.maxX * 16.0D - 0.01D) / taw;
			v1 = ((double)i11 + block1.minZ * 16.0D) / tah;
			v2 = ((double)i11 + block1.maxZ * 16.0D - 0.01D) / tah;
			
			if(block1.minX < 0.0D || block1.maxX > 1.0D) {
				u1 = (double)(((float)i10 + 0.0F) / taw);
				u2 = (double)(((float)i10 + 15.99F) / taw);
			}

			if(block1.minZ < 0.0D || block1.maxZ > 1.0D) {
				v1 = (double)(((float)i11 + 0.0F) / tah);
				v2 = (double)(((float)i11 + 15.99F) / tah);
			}

			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		}

		double d28 = d2 + block1.minX;
		double d30 = d2 + block1.maxX;
		double d32 = d4 + block1.minY;
		double d34 = d6 + block1.minZ;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d32, d36, ru1, rv2);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d28, d32, d34, u1, v1);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d30, d32, d34, ru2, rv1);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d30, d32, d36, u2, v2);
		} else {
			tessellator9.addVertexWithUV(d28, d32, d36, ru1, rv2);
			tessellator9.addVertexWithUV(d28, d32, d34, u1, v1);
			tessellator9.addVertexWithUV(d30, d32, d34, ru2, rv1);
			tessellator9.addVertexWithUV(d30, d32, d36, u2, v2);
		}

	}

	public void renderTopFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		final double taw = TextureAtlasSize.w;
		final double tah = TextureAtlasSize.h;
		
		double u1, u2, v1, v2;
		double ru2, ru1, rv1, rv2;
		
		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 0xff0;
	
		if(this.uvRotateTop == 1) {
			u1 = ((double)i10 + block1.minZ * 16.0D) / taw;
			v1 = ((double)(i11 + 16) - block1.maxX * 16.0D) / tah;
			u2 = ((double)i10 + block1.maxZ * 16.0D) / taw;
			v2 = ((double)(i11 + 16) - block1.minX * 16.0D) / tah;
			rv1 = v1;
			rv2 = v2;
			ru2 = u1;
			ru1 = u2;
			v1 = v2;
			v2 = rv1;
		} else if(this.uvRotateTop == 2) {
			u1 = ((double)(i10 + 16) - block1.maxZ * 16.0D) / taw;
			v1 = ((double)i11 + block1.minX * 16.0D) / tah;
			u2 = ((double)(i10 + 16) - block1.minZ * 16.0D) / taw;
			v2 = ((double)i11 + block1.maxX * 16.0D) / tah;
			ru2 = u2;
			ru1 = u1;
			u1 = u2;
			u2 = ru1;
			rv1 = v2;
			rv2 = v1;
		} else if(this.uvRotateTop == 3) {
			u1 = ((double)(i10 + 16) - block1.minX * 16.0D) / taw;
			u2 = ((double)(i10 + 16) - block1.maxX * 16.0D - 0.01D) / taw;
			v1 = ((double)(i11 + 16) - block1.minZ * 16.0D) / tah;
			v2 = ((double)(i11 + 16) - block1.maxZ * 16.0D - 0.01D) / tah;
			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		} else {
			u1 = ((double)i10 + block1.minX * 16.0D) / taw;
			u2 = ((double)i10 + block1.maxX * 16.0D - 0.01D) / taw;
			v1 = ((double)i11 + block1.minZ * 16.0D) / tah;
			v2 = ((double)i11 + block1.maxZ * 16.0D - 0.01D) / tah;
			if(block1.minX < 0.0D || block1.maxX > 1.0D) {
				u1 = (double)(((float)i10 + 0.0F) / taw);
				u2 = (double)(((float)i10 + 15.99F) / taw);
			}

			if(block1.minZ < 0.0D || block1.maxZ > 1.0D) {
				v1 = (double)(((float)i11 + 0.0F) / tah);
				v2 = (double)(((float)i11 + 15.99F) / tah);
			}

			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		}

		double d28 = d2 + block1.minX;
		double d30 = d2 + block1.maxX;
		double d32 = d4 + block1.maxY;
		double d34 = d6 + block1.minZ;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d30, d32, d36, u2, v2);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d30, d32, d34, ru2, rv1);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d28, d32, d34, u1, v1);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d28, d32, d36, ru1, rv2);
		} else {
			tessellator9.addVertexWithUV(d30, d32, d36, u2, v2);
			tessellator9.addVertexWithUV(d30, d32, d34, ru2, rv1);
			tessellator9.addVertexWithUV(d28, d32, d34, u1, v1);
			tessellator9.addVertexWithUV(d28, d32, d36, ru1, rv2);
		}

	}

	public void renderEastFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 0xff0;
		
		final double taw = TextureAtlasSize.w;
		final double tah = TextureAtlasSize.h;
		
		double u1, u2, v1, v2;
		double ru2, ru1, rv1, rv2;
		
		if(this.uvRotateEast == 2) {
			u1 = ((double)i10 + block1.minY * 16.0D) / taw;
			v1 = ((double)(i11 + 16) - block1.minX * 16.0D) / tah;
			u2 = ((double)i10 + block1.maxY * 16.0D) / taw;
			v2 = ((double)(i11 + 16) - block1.maxX * 16.0D) / tah;
			rv1 = v1;
			rv2 = v2;
			ru2 = u1;
			ru1 = u2;
			v1 = v2;
			v2 = rv1;
		} else if(this.uvRotateEast == 1) {
			u1 = ((double)(i10 + 16) - block1.maxY * 16.0D) / taw;
			v1 = ((double)i11 + block1.maxX * 16.0D) / tah;
			u2 = ((double)(i10 + 16) - block1.minY * 16.0D) / taw;
			v2 = ((double)i11 + block1.minX * 16.0D) / tah;
			ru2 = u2;
			ru1 = u1;
			u1 = u2;
			u2 = ru1;
			rv1 = v2;
			rv2 = v1;
		} else if(this.uvRotateEast == 3) {
			u1 = ((double)(i10 + 16) - block1.minX * 16.0D) / taw;
			u2 = ((double)(i10 + 16) - block1.maxX * 16.0D - 0.01D) / taw;
			v1 = ((double)i11 + block1.maxY * 16.0D) / tah;
			v2 = ((double)i11 + block1.minY * 16.0D - 0.01D) / tah;
			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		} else {
			u1 = ((double)i10 + block1.minX * 16.0D) / taw;
			u2 = ((double)i10 + block1.maxX * 16.0D - 0.01D) / taw;
			v1 = ((double)(i11 + 16) - block1.maxY * 16.0D) / tah;
			v2 = ((double)(i11 + 16) - block1.minY * 16.0D - 0.01D) / tah;

			if(this.flipTexture) {
				ru2 = u1;
				u1 = u2;
				u2 = ru2;
			}

			if(block1.minX < 0.0D || block1.maxX > 1.0D) {
				u1 = (double)(((float)i10 + 0.0F) / taw);
				u2 = (double)(((float)i10 + 15.99F) / taw);
			}

			if(block1.minY < 0.0D || block1.maxY > 1.0D) {
				v1 = (double)(((float)i11 + 0.0F) / tah);
				v2 = (double)(((float)i11 + 15.99F) / tah);
			}

			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		}

		double d28 = d2 + block1.minX;
		double d30 = d2 + block1.maxX;
		double d32 = d4 + block1.minY;
		double d34 = d4 + block1.maxY;
		double d36 = d6 + block1.minZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d34, d36, ru2, rv1);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d30, d34, d36, u1, v1);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d30, d32, d36, ru1, rv2);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d28, d32, d36, u2, v2);
		} else {
			tessellator9.addVertexWithUV(d28, d34, d36, ru2, rv1);
			tessellator9.addVertexWithUV(d30, d34, d36, u1, v1);
			tessellator9.addVertexWithUV(d30, d32, d36, ru1, rv2);
			tessellator9.addVertexWithUV(d28, d32, d36, u2, v2);
		}

	}

	public void renderWestFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 0xff0;
		
		final double taw = TextureAtlasSize.w;
		final double tah = TextureAtlasSize.h;
		
		double u1, u2, v1, v2;
		double ru2, ru1, rv1, rv2;
		
		if(this.uvRotateWest == 1) {
			u1 = ((double)i10 + block1.minY * 16.0D) / taw;
			v2 = ((double)(i11 + 16) - block1.minX * 16.0D) / tah;
			u2 = ((double)i10 + block1.maxY * 16.0D) / taw;
			v1 = ((double)(i11 + 16) - block1.maxX * 16.0D) / tah;
			rv1 = v1;
			rv2 = v2;
			ru2 = u1;
			ru1 = u2;
			v1 = v2;
			v2 = rv1;
		} else if(this.uvRotateWest == 2) {
			u1 = ((double)(i10 + 16) - block1.maxY * 16.0D) / taw;
			v1 = ((double)i11 + block1.minX * 16.0D) / tah;
			u2 = ((double)(i10 + 16) - block1.minY * 16.0D) / taw;
			v2 = ((double)i11 + block1.maxX * 16.0D) / tah;
			ru2 = u2;
			ru1 = u1;
			u1 = u2;
			u2 = ru1;
			rv1 = v2;
			rv2 = v1;
		} else if(this.uvRotateWest == 3) {
			u1 = ((double)(i10 + 16) - block1.minX * 16.0D) / taw;
			u2 = ((double)(i10 + 16) - block1.maxX * 16.0D - 0.01D) / taw;
			v1 = ((double)i11 + block1.maxY * 16.0D) / tah;
			v2 = ((double)i11 + block1.minY * 16.0D - 0.01D) / tah;
			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		} else {
			u1 = ((double)i10 + block1.minX * 16.0D) / taw;
			u2 = ((double)i10 + block1.maxX * 16.0D - 0.01D) / taw;
			v1 = ((double)(i11 + 16) - block1.maxY * 16.0D) / tah;
			v2 = ((double)(i11 + 16) - block1.minY * 16.0D - 0.01D) / tah;
			if(this.flipTexture) {
				ru2 = u1;
				u1 = u2;
				u2 = ru2;
			}

			if(block1.minX < 0.0D || block1.maxX > 1.0D) {
				u1 = (double)(((float)i10 + 0.0F) / taw);
				u2 = (double)(((float)i10 + 15.99F) / taw);
			}

			if(block1.minY < 0.0D || block1.maxY > 1.0D) {
				v1 = (double)(((float)i11 + 0.0F) / tah);
				v2 = (double)(((float)i11 + 15.99F) / tah);
			}

			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		}

		double d28 = d2 + block1.minX;
		double d30 = d2 + block1.maxX;
		double d32 = d4 + block1.minY;
		double d34 = d4 + block1.maxY;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d34, d36, u1, v1);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d28, d32, d36, ru1, rv2);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d30, d32, d36, u2, v2);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d30, d34, d36, ru2, rv1);
		} else {
			tessellator9.addVertexWithUV(d28, d34, d36, u1, v1);
			tessellator9.addVertexWithUV(d28, d32, d36, ru1, rv2);
			tessellator9.addVertexWithUV(d30, d32, d36, u2, v2);
			tessellator9.addVertexWithUV(d30, d34, d36, ru2, rv1);
		}

	}

	public void renderNorthFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 0xff0;
		
		final double taw = TextureAtlasSize.w;
		final double tah = TextureAtlasSize.h;
		
		double u1, u2, v1, v2;
		double ru2, ru1, rv1, rv2;
		
		if(this.uvRotateNorth == 1) {
			u1 = ((double)i10 + block1.minY * 16.0D) / taw;
			v1 = ((double)(i11 + 16) - block1.maxZ * 16.0D) / tah;
			u2 = ((double)i10 + block1.maxY * 16.0D) / taw;
			v2 = ((double)(i11 + 16) - block1.minZ * 16.0D) / tah;
			rv1 = v1;
			rv2 = v2;
			ru2 = u1;
			ru1 = u2;
			v1 = v2;
			v2 = rv1;
		} else if(this.uvRotateNorth == 2) {
			u1 = ((double)(i10 + 16) - block1.maxY * 16.0D) / taw;
			v1 = ((double)i11 + block1.minZ * 16.0D) / tah;
			u2 = ((double)(i10 + 16) - block1.minY * 16.0D) / taw;
			v2 = ((double)i11 + block1.maxZ * 16.0D) / tah;
			ru2 = u2;
			ru1 = u1;
			u1 = u2;
			u2 = ru1;
			rv1 = v2;
			rv2 = v1;
		} else if(this.uvRotateNorth == 3) {
			u1 = ((double)(i10 + 16) - block1.minZ * 16.0D) / taw;
			u2 = ((double)(i10 + 16) - block1.maxZ * 16.0D - 0.01D) / taw;
			v1 = ((double)i11 + block1.maxY * 16.0D) / tah;
			v2 = ((double)i11 + block1.minY * 16.0D - 0.01D) / tah;
			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		} else {
			u1 = ((double)i10 + block1.minZ * 16.0D) / taw;
			u2 = ((double)i10 + block1.maxZ * 16.0D - 0.01D) / taw;
			v1 = ((double)(i11 + 16) - block1.maxY * 16.0D) / tah;
			v2 = ((double)(i11 + 16) - block1.minY * 16.0D - 0.01D) / tah;

			if(this.flipTexture) {
				ru2 = u1;
				u1 = u2;
				u2 = ru2;
			}

			if(block1.minZ < 0.0D || block1.maxZ > 1.0D) {
				u1 = (double)(((float)i10 + 0.0F) / taw);
				u2 = (double)(((float)i10 + 15.99F) / taw);
			}

			if(block1.minY < 0.0D || block1.maxY > 1.0D) {
				v1 = (double)(((float)i11 + 0.0F) / tah);
				v2 = (double)(((float)i11 + 15.99F) / tah);
			}

			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		}

		double d28 = d2 + block1.minX;
		double d30 = d4 + block1.minY;
		double d32 = d4 + block1.maxY;
		double d34 = d6 + block1.minZ;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d32, d36, ru2, rv1);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d28, d32, d34, u1, v1);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d28, d30, d34, ru1, rv2);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d28, d30, d36, u2, v2);
		} else {
			tessellator9.addVertexWithUV(d28, d32, d36, ru2, rv1);
			tessellator9.addVertexWithUV(d28, d32, d34, u1, v1);
			tessellator9.addVertexWithUV(d28, d30, d34, ru1, rv2);
			tessellator9.addVertexWithUV(d28, d30, d36, u2, v2);
		}

	}

	public void renderSouthFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 0xff0;
		
		final double taw = TextureAtlasSize.w;
		final double tah = TextureAtlasSize.h;
		
		double u1, u2, v1, v2;
		double ru2, ru1, rv1, rv2;
		
		if(this.uvRotateSouth == 2) {
			u1 = ((double)i10 + block1.minY * 16.0D) / taw;
			v1 = ((double)(i11 + 16) - block1.minZ * 16.0D) / tah;
			u2 = ((double)i10 + block1.maxY * 16.0D) / taw;
			v2 = ((double)(i11 + 16) - block1.maxZ * 16.0D) / tah;
			rv1 = v1;
			rv2 = v2;
			ru2 = u1;
			ru1 = u2;
			v1 = v2;
			v2 = rv1;
		} else if(this.uvRotateSouth == 1) {
			u1 = ((double)(i10 + 16) - block1.maxY * 16.0D) / taw;
			v1 = ((double)i11 + block1.maxZ * 16.0D) / tah;
			u2 = ((double)(i10 + 16) - block1.minY * 16.0D) / taw;
			v2 = ((double)i11 + block1.minZ * 16.0D) / tah;
			ru2 = u2;
			ru1 = u1;
			u1 = u2;
			u2 = ru1;
			rv1 = v2;
			rv2 = v1;
		} else if(this.uvRotateSouth == 3) {
			u1 = ((double)(i10 + 16) - block1.minZ * 16.0D) / taw;
			u2 = ((double)(i10 + 16) - block1.maxZ * 16.0D - 0.01D) / taw;
			v1 = ((double)i11 + block1.maxY * 16.0D) / tah;
			v2 = ((double)i11 + block1.minY * 16.0D - 0.01D) / tah;
			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		} else {
			u1 = ((double)i10 + block1.minZ * 16.0D) / taw;
			u2 = ((double)i10 + block1.maxZ * 16.0D - 0.01D) / taw;
			v1 = ((double)(i11 + 16) - block1.maxY * 16.0D) / tah;
			v2 = ((double)(i11 + 16) - block1.minY * 16.0D - 0.01D) / tah;

			if(this.flipTexture) {
				ru2 = u1;
				u1 = u2;
				u2 = ru2;
			}

			if(block1.minZ < 0.0D || block1.maxZ > 1.0D) {
				u1 = (double)(((float)i10 + 0.0F) / taw);
				u2 = (double)(((float)i10 + 15.99F) / taw);
			}

			if(block1.minY < 0.0D || block1.maxY > 1.0D) {
				v1 = (double)(((float)i11 + 0.0F) / tah);
				v2 = (double)(((float)i11 + 15.99F) / tah);
			}

			ru2 = u2;
			ru1 = u1;
			rv1 = v1;
			rv2 = v2;
		}

		double d28 = d2 + block1.maxX;
		double d30 = d4 + block1.minY;
		double d32 = d4 + block1.maxY;
		double d34 = d6 + block1.minZ;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d30, d36, ru1, rv2);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d28, d30, d34, u2, v2);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d28, d32, d34, ru2, rv1);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d28, d32, d36, u1, v1);
		} else {
			tessellator9.addVertexWithUV(d28, d30, d36, ru1, rv2);
			tessellator9.addVertexWithUV(d28, d30, d34, u2, v2);
			tessellator9.addVertexWithUV(d28, d32, d34, ru2, rv1);
			tessellator9.addVertexWithUV(d28, d32, d36, u1, v1);
		}

	}


	// Custom renderers

	public void renderBlockAsItem(Block block, float brightness) {
		int renderType = block.getRenderType();
		Tessellator tes = Tessellator.instance;
		if(renderType == 0) {
			block.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			float f5 = 0.5F;
			float n1 = 1.0F;
			float n2 = 0.8F;
			float f8 = 0.6F;
			tes.startDrawingQuads();
			tes.setColorRGBA_F(n1, n1, n1, brightness);
			this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
			tes.setColorRGBA_F(f5, f5, f5, brightness);
			this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
			tes.setColorRGBA_F(n2, n2, n2, brightness);
			this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
			this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
			tes.setColorRGBA_F(f8, f8, f8, brightness);
			this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
			this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
			tes.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		} else {
			block.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tes.startDrawingQuads();
			tes.setColorRGBA_F(1.0F, 1.0F, 1.0F, brightness);
			this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
			tes.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

	}
	
	public void renderBlockOnInventory(Block block, int meta, float brightness) {
		if(this.useInventoryTint) {
			int rgba = block.getRenderColor(meta);
			float r = (float)(rgba >> 16 & 255) / 255.0F;
			float g = (float)(rgba >> 8 & 255) / 255.0F;
			float b = (float)(rgba & 255) / 255.0F;
			GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0F);
		}

		BlockRenderHandler handler = BlockRenderType.get(block.getRenderType());
		if(handler != null) {
			handler.renderBlockOnInventory(this, block, meta, brightness);
		}
	}

	public static boolean renderItemIn3d(int i0) {
		BlockRenderHandler handler = BlockRenderType.get(i0);
		return handler != null && handler.renderItemIn3d();
	}
	

	static {
		for(int i = 0; i < redstoneColors.length; ++i) {
			float f = (float)i / 15.0F;
			float f1 = f * 0.6F + 0.4F;
			if(i == 0) {
				f = 0.0F;
			}

			float f2 = f * f * 0.7F - 0.5F;
			float f3 = f * f * 0.6F - 0.7F;
			if(f2 < 0.0F) {
				f2 = 0.0F;
			}

			if(f3 < 0.0F) {
				f3 = 0.0F;
			}

			redstoneColors[i] = new float[]{f1, f2, f3};
		}

	}
}
