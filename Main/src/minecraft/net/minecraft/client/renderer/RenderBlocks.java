package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBed;
import net.minecraft.client.renderer.tile.RenderBlockClassicPiston;
import net.minecraft.client.renderer.tile.RenderBlockHollowLog;
import net.minecraft.client.renderer.tile.RenderBlockModel;
import net.minecraft.src.Block;
import net.minecraft.src.BlockBed;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.BlockPane;
import net.minecraft.src.BlockPistonBase;
import net.minecraft.src.BlockPistonExtension;
import net.minecraft.src.BlockRail;
import net.minecraft.src.BlockRedstoneRepeater;
import net.minecraft.src.BlockRedstoneWire;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.world.level.material.Material;

public class RenderBlocks {
	private IBlockAccess blockAccess;
	private int overrideBlockTexture = -1;
	private boolean flipTexture = false;
	private boolean renderAllFaces = false;
	public static boolean fancyGrass = false;
	public boolean useInventoryTint = true;
	private int uvRotateEast = 0;
	private int uvRotateWest = 0;
	private int uvRotateSouth = 0;
	private int uvRotateNorth = 0;
	private int uvRotateTop = 0;
	private int uvRotateBottom = 0;
	private boolean enableAO;
	private float lightValueOwn;
	private float aoLightValueXNeg;
	private float aoLightValueYNeg;
	private float aoLightValueZNeg;
	private float aoLightValueXPos;
	private float aoLightValueYPos;
	private float aoLightValueZPos;
	private float aoLightValueScratchXYZNNN;
	private float aoLightValueScratchXYNN;
	private float aoLightValueScratchXYZNNP;
	private float aoLightValueScratchYZNN;
	private float aoLightValueScratchYZNP;
	private float aoLightValueScratchXYZPNN;
	private float aoLightValueScratchXYPN;
	private float aoLightValueScratchXYZPNP;
	private float aoLightValueScratchXYZNPN;
	private float aoLightValueScratchXYNP;
	private float aoLightValueScratchXYZNPP;
	private float aoLightValueScratchYZPN;
	private float aoLightValueScratchXYZPPN;
	private float aoLightValueScratchXYPP;
	private float aoLightValueScratchYZPP;
	private float aoLightValueScratchXYZPPP;
	private float aoLightValueScratchXZNN;
	private float aoLightValueScratchXZPN;
	private float aoLightValueScratchXZNP;
	private float aoLightValueScratchXZPP;
	private int aoBrightnessXYZNNN;
	private int aoBrightnessXYNN;
	private int aoBrightnessXYZNNP;
	private int aoBrightnessYZNN;
	private int aoBrightnessYZNP;
	private int aoBrightnessXYZPNN;
	private int aoBrightnessXYPN;
	private int aoBrightnessXYZPNP;
	private int aoBrightnessXYZNPN;
	private int aoBrightnessXYNP;
	private int aoBrightnessXYZNPP;
	private int aoBrightnessYZPN;
	private int aoBrightnessXYZPPN;
	private int aoBrightnessXYPP;
	private int aoBrightnessYZPP;
	private int aoBrightnessXYZPPP;
	private int aoBrightnessXZNN;
	private int aoBrightnessXZPN;
	private int aoBrightnessXZNP;
	private int aoBrightnessXZPP;
	private int aoType = 1;
	private int brightnessTopLeft;
	private int brightnessBottomLeft;
	private int brightnessBottomRight;
	private int brightnessTopRight;
	private float colorRedTopLeft;
	private float colorRedBottomLeft;
	private float colorRedBottomRight;
	private float colorRedTopRight;
	private float colorGreenTopLeft;
	private float colorGreenBottomLeft;
	private float colorGreenBottomRight;
	private float colorGreenTopRight;
	private float colorBlueTopLeft;
	private float colorBlueBottomLeft;
	private float colorBlueBottomRight;
	private float colorBlueTopRight;
	private boolean aoGrassXYZCPN;
	private boolean aoGrassXYZPPC;
	private boolean aoGrassXYZNPC;
	private boolean aoGrassXYZCPP;
	private boolean aoGrassXYZNCN;
	private boolean aoGrassXYZPCP;
	private boolean aoGrassXYZNCP;
	private boolean aoGrassXYZPCN;
	private boolean aoGrassXYZCNN;
	private boolean aoGrassXYZPNC;
	private boolean aoGrassXYZNNC;
	private boolean aoGrassXYZCNP;
	
	private int activeRenderPass = 0;

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
		int i5 = block.getRenderType();
		block.setBlockBoundsBasedOnState(this.blockAccess, x, y, z);

		switch(i5) {
			case 0: return this.renderStandardBlock(block, x, y, z);
			case 1: return this.renderCrossedSquares(block, x, y, z);
			case 2: return this.renderBlockTorch(block, x, y, z);
			case 3: return this.renderBlockFire(block, x, y, z);
			case 4: return this.renderBlockFluids(block, x, y, z);
			case 5: return this.renderBlockRedstoneWire(block, x, y, z);
			case 6: return this.renderBlockCrops(block, x, y, z);
			case 7: return this.renderBlockDoor(block, x, y, z);
			case 8: return this.renderBlockLadder(block, x, y, z);
			case 9: return this.renderBlockrail((BlockRail)block, x, y, z);
			case 10: return this.renderBlockStairs(block, x, y, z);
			case 11: return this.renderBlockFence(block, x, y, z);
			case 12: return this.renderBlockLever(block, x, y, z);
			case 13: return this.renderBlockCactus(block, x, y, z); 
			case 14: return this.renderBlockBed(block, x, y, z);
			case 15: return this.renderBlockRepeater(block, x, y, z);
			case 18: return this.renderBlockPane((BlockPane)block, x, y, z);
			case 20: return this.renderBlockVine(block, x, y, z);
			case 23: return this.renderBlockLilyPad(block, x, y, z);
			case 100: return this.renderBlockWall(block, x, y, z);
			case 101: return this.renderBarbedWire(block, x, y, z);
			case 102: return this.renderHollowTrunk(block, x, y, z);
			case 103: return this.renderBlockDirtPath(block, x, y, z);
			case 104: return this.renderBlockStreetLantern(block, x, y, z);
			case 105: return this.renderBlockChippedWood(block, x, y, z);
			case 106: return this.renderBlockSlime(block, x, y, z);
			case 107: return this.renderBlockAxisOriented(block, x, y, z);
			case 108: return this.renderBlockWoodOriented(block, x, y, z);
			case 109: return this.renderBlockClassicPiston(block, x, y, z);
			case 110: return this.renderBlockChain(block, x, y, z);
			case 111: return this.renderBlockSnowloggedPlant(block, x, y, z);
			case 112: return this.renderBlockThinFeature(block, x, y, z);
			case 250: return this.renderBlockModel(block, x, y, z);
			default: return false;
		}
	}

	private boolean renderBlockThinFeature(Block block, int x, int y, int z) {
		long l = (long)(x * 0x2fc20f) ^ (long)z * 0x6ebfff5L ^ (long)y;
		l = l * l * 0x285b825L + l * 11L;
		int i1 = (int)(l >> 16 & 7L);
		this.uvRotateTop = i1 & 3;
		this.renderStandardBlock(block, x, y, z);	
		this.uvRotateTop = 0;
		return true;
	}

	private boolean renderBlockSnowloggedPlant(Block block, int x, int y, int z) {
		// 1st render the plant
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, x, y, z));
		int colorMultiplier = block.colorMultiplier(this.blockAccess, x, y, z);

		float r = (float)(colorMultiplier >> 16 & 255) / 255.0F;
		float g = (float)(colorMultiplier >> 8 & 255) / 255.0F;
		float b = (float)(colorMultiplier & 255) / 255.0F;

		tessellator.setColorOpaque_F(r, g, b);

		double xD = (double)x;
		double yD = (double)y;
		double zD = (double)z;

		long noise = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
		noise = noise * noise * 42317861L + noise * 11L;
		xD += ((double)((float)(noise >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
		yD += ((double)((float)(noise >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
		zD += ((double)((float)(noise >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;

		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		int grassType = (meta >> 4) & 7;
		
		if(((meta >> 4) & 8) != 0) {
			this.renderCrossedSquaresDoubleHeight(block, grassType, xD, yD, zD);
		} else {
			this.renderCrossedSquares(block, grassType, xD, yD, zD);
		}
		
		// now render a layer of snow
		int snowHeight = meta & 15;
		if(snowHeight > 0) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (float)(snowHeight + 1) / 16.0F, 1.0F);
			this.overrideBlockTexture = Block.snow.blockIndexInTexture;
			this.renderStandardBlockWithColorMultiplier(block, x, y, z, 1F, 1F, 1F);
			this.overrideBlockTexture = -1;
			((BlockFlower)block).setMyBlockBounds();
		}
		return true;
	}

	private boolean renderBlockChain(Block block, int x, int y, int z) {
		Tessellator tessellator9 = Tessellator.instance;
		
		tessellator9.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, x, y, z));
		tessellator9.setColorOpaque_F(1, 1, 1);
		
		int i10 = block.getBlockTextureFromSideAndMetadata(0, this.blockAccess.getBlockMetadata(x, y, z));
		if(this.overrideBlockTexture >= 0) {
			i10 = this.overrideBlockTexture;
		}

		int i11 = (i10 & 15) << 4;
		int i12 = i10 & 240;
		double u1 = (double)((float)i11 / 256.0F);
		double u2 = (double)(((float)i11 + 15.99F) / 256.0F);
		double v1 = (double)((float)i12 / 256.0F);
		double v2 = (double)(((float)i12 + 15.99F) / 256.0F);
		double x1 = (double)x + 0.5D - (double)0.45F;
		double x2 = (double)x + 0.5D + (double)0.45F;
		double z1 = (double)z + 0.5D - (double)0.45F;
		double z2 = (double)z + 0.5D + (double)0.45F;
		double yy = (double)y;
		
		// Square 1
		
		tessellator9.addVertexWithUV(x1, yy + 1.0D, z1,  u1, v2);
		tessellator9.addVertexWithUV(x1, yy + 0.0D, z1,  u1, v1);
		tessellator9.addVertexWithUV(x2, yy + 0.0D, z2,  u2, v1);
		tessellator9.addVertexWithUV(x2, yy + 1.0D, z2,  u2, v2)
		;
		tessellator9.addVertexWithUV(x2, yy + 1.0D, z2,  u1, v2);
		tessellator9.addVertexWithUV(x2, yy + 0.0D, z2,  u1, v1);
		tessellator9.addVertexWithUV(x1, yy + 0.0D, z1,  u2, v1);
		tessellator9.addVertexWithUV(x1, yy + 1.0D, z1,  u2, v2);
		
		// Square 2 [upside down]
		
		tessellator9.addVertexWithUV(x1, yy + 1.0D, z2,  u1, v1);
		tessellator9.addVertexWithUV(x1, yy + 0.0D, z2,  u1, v2);
		tessellator9.addVertexWithUV(x2, yy + 0.0D, z1,  u2, v2);
		tessellator9.addVertexWithUV(x2, yy + 1.0D, z1,  u2, v1);
		
		tessellator9.addVertexWithUV(x2, yy + 1.0D, z1,  u1, v1);
		tessellator9.addVertexWithUV(x2, yy + 0.0D, z1,  u1, v2);
		tessellator9.addVertexWithUV(x1, yy + 0.0D, z2,  u2, v2);
		tessellator9.addVertexWithUV(x1, yy + 1.0D, z2,  u2, v1);
	
		return true;
	}

	private boolean renderBlockBed(Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		int i7 = BlockBed.getDirectionFromMetadata(i6);
		boolean z8 = BlockBed.isBlockFootOfBed(i6);
		float f9 = 0.5F;
		float f10 = 1.0F;
		float f11 = 0.8F;
		float f12 = 0.6F;
		
		int i25 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4);
		tessellator5.setBrightness(i25);
		tessellator5.setColorOpaque_F(f9, f9, f9);
		
		int i26 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 0);
		int i27 = (i26 & 15) << 4;
		int i28 = i26 & 240;
		double d29 = (double)((float)i27 / 256.0F);
		double d31 = ((double)(i27 + 16) - 0.01D) / 256.0D;
		double d33 = (double)((float)i28 / 256.0F);
		double d35 = ((double)(i28 + 16) - 0.01D) / 256.0D;
		double d37 = (double)i2 + block1.minX;
		double d39 = (double)i2 + block1.maxX;
		double d41 = (double)i3 + block1.minY + 0.1875D;
		double d43 = (double)i4 + block1.minZ;
		double d45 = (double)i4 + block1.maxZ;
		tessellator5.addVertexWithUV(d37, d41, d45, d29, d35);
		tessellator5.addVertexWithUV(d37, d41, d43, d29, d33);
		tessellator5.addVertexWithUV(d39, d41, d43, d31, d33);
		tessellator5.addVertexWithUV(d39, d41, d45, d31, d35);
		float f64 = block1.getBlockBrightness(this.blockAccess, i2, i3 + 1, i4);
		tessellator5.setColorOpaque_F(f10 * f64, f10 * f64, f10 * f64);
		i27 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 1);
		i28 = (i27 & 15) << 4;
		int i67 = i27 & 240;
		double d30 = (double)((float)i28 / 256.0F);
		double d32 = ((double)(i28 + 16) - 0.01D) / 256.0D;
		double d34 = (double)((float)i67 / 256.0F);
		double d36 = ((double)(i67 + 16) - 0.01D) / 256.0D;
		double d38 = d30;
		double d40 = d32;
		double d42 = d34;
		double d44 = d34;
		double d46 = d30;
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
		i26 = ModelBed.headInvisibleFace[i7];
		if(z8) {
			i26 = ModelBed.headInvisibleFace[ModelBed.footInvisibleFaceRemap[i7]];
		}

		byte b65 = 4;
		switch(i7) {
		case 0:
			b65 = 5;
			break;
		case 1:
			b65 = 3;
		case 2:
		default:
			break;
		case 3:
			b65 = 2;
		}

		if(i26 != 2 && (this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2))) {
			tessellator5.setBrightness(block1.minZ > 0.0D ? i25 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1));
			tessellator5.setColorOpaque_F(f11, f11, f11);
			this.flipTexture = b65 == 2;
			this.renderEastFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 2));
		}

		if(i26 != 3 && (this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3))) {
			tessellator5.setBrightness(block1.maxZ < 1.0D ? i25 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1));
			tessellator5.setColorOpaque_F(f11, f11, f11);
			this.flipTexture = b65 == 3;
			this.renderWestFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 3));
		}

		if(i26 != 4 && (this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4))) {
			tessellator5.setBrightness(block1.minX > 0.0D ? i25 : block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4));
			tessellator5.setColorOpaque_F(f12, f12, f12);
			this.flipTexture = b65 == 4;
			this.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 4));
		}

		if(i26 != 5 && (this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5))) {
			tessellator5.setBrightness(block1.maxX < 1.0D ? i25 : block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4));
			tessellator5.setColorOpaque_F(f12, f12, f12);
			this.flipTexture = b65 == 5;
			this.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 5));
		}

		this.flipTexture = false;
		return true;
	}

	public boolean renderBlockTorch(Block block1, int i2, int i3, int i4) {
		int i5 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		Tessellator tessellator6 = Tessellator.instance;
		tessellator6.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
		tessellator6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d8 = (double)0.4F;
		double d10 = 0.5D - d8;
		double d12 = (double)0.2F;
		if(i5 == 1) {
			this.renderTorchAtAngle(block1, (double)i2 - d10, (double)i3 + d12, (double)i4, -d8, 0.0D);
		} else if(i5 == 2) {
			this.renderTorchAtAngle(block1, (double)i2 + d10, (double)i3 + d12, (double)i4, d8, 0.0D);
		} else if(i5 == 3) {
			this.renderTorchAtAngle(block1, (double)i2, (double)i3 + d12, (double)i4 - d10, 0.0D, -d8);
		} else if(i5 == 4) {
			this.renderTorchAtAngle(block1, (double)i2, (double)i3 + d12, (double)i4 + d10, 0.0D, d8);
		} else {
			this.renderTorchAtAngle(block1, (double)i2, (double)i3, (double)i4, 0.0D, 0.0D);
		}

		return true;
	}

	private boolean renderBlockRepeater(Block block1, int i2, int i3, int i4) {
		int i5 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		int i6 = i5 & 3;
		int i7 = (i5 & 12) >> 2;
		this.renderStandardBlock(block1, i2, i3, i4);
		Tessellator tessellator8 = Tessellator.instance;
		tessellator8.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
		tessellator8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d10 = -0.1875D;
		double d12 = 0.0D;
		double d14 = 0.0D;
		double d16 = 0.0D;
		double d18 = 0.0D;
		switch(i6) {
		case 0:
			d18 = -0.3125D;
			d14 = BlockRedstoneRepeater.repeaterTorchOffset[i7];
			break;
		case 1:
			d16 = 0.3125D;
			d12 = -BlockRedstoneRepeater.repeaterTorchOffset[i7];
			break;
		case 2:
			d18 = 0.3125D;
			d14 = -BlockRedstoneRepeater.repeaterTorchOffset[i7];
			break;
		case 3:
			d16 = -0.3125D;
			d12 = BlockRedstoneRepeater.repeaterTorchOffset[i7];
		}

		this.renderTorchAtAngle(block1, (double)i2 + d12, (double)i3 + d10, (double)i4 + d14, 0.0D, 0.0D);
		this.renderTorchAtAngle(block1, (double)i2 + d16, (double)i3 + d10, (double)i4 + d18, 0.0D, 0.0D);
		int i20 = block1.getBlockTextureFromSide(1);
		int i21 = (i20 & 15) << 4;
		int i22 = i20 & 240;
		double d23 = (double)((float)i21 / 256.0F);
		double d25 = (double)(((float)i21 + 15.99F) / 256.0F);
		double d27 = (double)((float)i22 / 256.0F);
		double d29 = (double)(((float)i22 + 15.99F) / 256.0F);
		float f31 = 0.125F;
		float f32 = (float)(i2 + 1);
		float f33 = (float)(i2 + 1);
		float f34 = (float)(i2 + 0);
		float f35 = (float)(i2 + 0);
		float f36 = (float)(i4 + 0);
		float f37 = (float)(i4 + 1);
		float f38 = (float)(i4 + 1);
		float f39 = (float)(i4 + 0);
		float f40 = (float)i3 + f31;
		if(i6 == 2) {
			f32 = f33 = (float)(i2 + 0);
			f34 = f35 = (float)(i2 + 1);
			f36 = f39 = (float)(i4 + 1);
			f37 = f38 = (float)(i4 + 0);
		} else if(i6 == 3) {
			f32 = f35 = (float)(i2 + 0);
			f33 = f34 = (float)(i2 + 1);
			f36 = f37 = (float)(i4 + 0);
			f38 = f39 = (float)(i4 + 1);
		} else if(i6 == 1) {
			f32 = f35 = (float)(i2 + 1);
			f33 = f34 = (float)(i2 + 0);
			f36 = f37 = (float)(i4 + 1);
			f38 = f39 = (float)(i4 + 0);
		}

		tessellator8.addVertexWithUV((double)f35, (double)f40, (double)f39, d23, d27);
		tessellator8.addVertexWithUV((double)f34, (double)f40, (double)f38, d23, d29);
		tessellator8.addVertexWithUV((double)f33, (double)f40, (double)f37, d25, d29);
		tessellator8.addVertexWithUV((double)f32, (double)f40, (double)f36, d25, d27);
		return true;
	}

	public void renderPistonBaseAllFaces(Block block1, int i2, int i3, int i4) {
		this.renderAllFaces = true;
		this.renderPistonBase(block1, i2, i3, i4, true);
		this.renderAllFaces = false;
	}

	private boolean renderPistonBase(Block block1, int i2, int i3, int i4, boolean z5) {
		int i6 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		boolean z7 = z5 || (i6 & 8) != 0;
		int i8 = BlockPistonBase.getOrientation(i6);
		if(z7) {
			switch(i8) {
			case 0:
				this.uvRotateEast = 3;
				this.uvRotateWest = 3;
				this.uvRotateSouth = 3;
				this.uvRotateNorth = 3;
				block1.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 1:
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
				break;
			case 2:
				this.uvRotateSouth = 1;
				this.uvRotateNorth = 2;
				block1.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;
			case 3:
				this.uvRotateSouth = 2;
				this.uvRotateNorth = 1;
				this.uvRotateTop = 3;
				this.uvRotateBottom = 3;
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
				break;
			case 4:
				this.uvRotateEast = 1;
				this.uvRotateWest = 2;
				this.uvRotateTop = 2;
				this.uvRotateBottom = 1;
				block1.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 5:
				this.uvRotateEast = 2;
				this.uvRotateWest = 1;
				this.uvRotateTop = 1;
				this.uvRotateBottom = 2;
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
			}

			this.renderStandardBlock(block1, i2, i3, i4);
			this.uvRotateEast = 0;
			this.uvRotateWest = 0;
			this.uvRotateSouth = 0;
			this.uvRotateNorth = 0;
			this.uvRotateTop = 0;
			this.uvRotateBottom = 0;
			block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			switch(i8) {
			case 0:
				this.uvRotateEast = 3;
				this.uvRotateWest = 3;
				this.uvRotateSouth = 3;
				this.uvRotateNorth = 3;
			case 1:
			default:
				break;
			case 2:
				this.uvRotateSouth = 1;
				this.uvRotateNorth = 2;
				break;
			case 3:
				this.uvRotateSouth = 2;
				this.uvRotateNorth = 1;
				this.uvRotateTop = 3;
				this.uvRotateBottom = 3;
				break;
			case 4:
				this.uvRotateEast = 1;
				this.uvRotateWest = 2;
				this.uvRotateTop = 2;
				this.uvRotateBottom = 1;
				break;
			case 5:
				this.uvRotateEast = 2;
				this.uvRotateWest = 1;
				this.uvRotateTop = 1;
				this.uvRotateBottom = 2;
			}

			this.renderStandardBlock(block1, i2, i3, i4);
			this.uvRotateEast = 0;
			this.uvRotateWest = 0;
			this.uvRotateSouth = 0;
			this.uvRotateNorth = 0;
			this.uvRotateTop = 0;
			this.uvRotateBottom = 0;
		}

		return true;
	}

	private void renderPistonRodUD(double d1, double d3, double d5, double d7, double d9, double d11, float f13, double d14) {
		int i16 = 108;
		if(this.overrideBlockTexture >= 0) {
			i16 = this.overrideBlockTexture;
		}

		int i17 = (i16 & 15) << 4;
		int i18 = i16 & 240;
		Tessellator tessellator19 = Tessellator.instance;
		double d20 = (double)((float)(i17 + 0) / 256.0F);
		double d22 = (double)((float)(i18 + 0) / 256.0F);
		double d24 = ((double)i17 + d14 - 0.01D) / 256.0D;
		double d26 = ((double)((float)i18 + 4.0F) - 0.01D) / 256.0D;
		tessellator19.setColorOpaque_F(f13, f13, f13);
		tessellator19.addVertexWithUV(d1, d7, d9, d24, d22);
		tessellator19.addVertexWithUV(d1, d5, d9, d20, d22);
		tessellator19.addVertexWithUV(d3, d5, d11, d20, d26);
		tessellator19.addVertexWithUV(d3, d7, d11, d24, d26);
	}

	private void renderPistonRodSN(double d1, double d3, double d5, double d7, double d9, double d11, float f13, double d14) {
		int i16 = 108;
		if(this.overrideBlockTexture >= 0) {
			i16 = this.overrideBlockTexture;
		}

		int i17 = (i16 & 15) << 4;
		int i18 = i16 & 240;
		Tessellator tessellator19 = Tessellator.instance;
		double d20 = (double)((float)(i17 + 0) / 256.0F);
		double d22 = (double)((float)(i18 + 0) / 256.0F);
		double d24 = ((double)i17 + d14 - 0.01D) / 256.0D;
		double d26 = ((double)((float)i18 + 4.0F) - 0.01D) / 256.0D;
		tessellator19.setColorOpaque_F(f13, f13, f13);
		tessellator19.addVertexWithUV(d1, d5, d11, d24, d22);
		tessellator19.addVertexWithUV(d1, d5, d9, d20, d22);
		tessellator19.addVertexWithUV(d3, d7, d9, d20, d26);
		tessellator19.addVertexWithUV(d3, d7, d11, d24, d26);
	}

	private void renderPistonRodEW(double d1, double d3, double d5, double d7, double d9, double d11, float f13, double d14) {
		int i16 = 108;
		if(this.overrideBlockTexture >= 0) {
			i16 = this.overrideBlockTexture;
		}

		int i17 = (i16 & 15) << 4;
		int i18 = i16 & 240;
		Tessellator tessellator19 = Tessellator.instance;
		double d20 = (double)((float)(i17 + 0) / 256.0F);
		double d22 = (double)((float)(i18 + 0) / 256.0F);
		double d24 = ((double)i17 + d14 - 0.01D) / 256.0D;
		double d26 = ((double)((float)i18 + 4.0F) - 0.01D) / 256.0D;
		tessellator19.setColorOpaque_F(f13, f13, f13);
		tessellator19.addVertexWithUV(d3, d5, d9, d24, d22);
		tessellator19.addVertexWithUV(d1, d5, d9, d20, d22);
		tessellator19.addVertexWithUV(d1, d7, d11, d20, d26);
		tessellator19.addVertexWithUV(d3, d7, d11, d24, d26);
	}

	public void renderPistonExtensionAllFaces(Block block1, int i2, int i3, int i4, boolean z5) {
		this.renderAllFaces = true;
		this.renderPistonExtension(block1, i2, i3, i4, z5);
		this.renderAllFaces = false;
	}

	private boolean renderPistonExtension(Block block1, int i2, int i3, int i4, boolean z5) {
		int i6 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		int i7 = BlockPistonExtension.getDirectionMeta(i6);
		float f11 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4);
		float f12 = z5 ? 1.0F : 0.5F;
		double d13 = z5 ? 16.0D : 8.0D;
		switch(i7) {
		case 0:
			this.uvRotateEast = 3;
			this.uvRotateWest = 3;
			this.uvRotateSouth = 3;
			this.uvRotateNorth = 3;
			block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
			this.renderStandardBlock(block1, i2, i3, i4);
			this.renderPistonRodUD((double)((float)i2 + 0.375F), (double)((float)i2 + 0.625F), (double)((float)i3 + 0.25F), (double)((float)i3 + 0.25F + f12), (double)((float)i4 + 0.625F), (double)((float)i4 + 0.625F), f11 * 0.8F, d13);
			this.renderPistonRodUD((double)((float)i2 + 0.625F), (double)((float)i2 + 0.375F), (double)((float)i3 + 0.25F), (double)((float)i3 + 0.25F + f12), (double)((float)i4 + 0.375F), (double)((float)i4 + 0.375F), f11 * 0.8F, d13);
			this.renderPistonRodUD((double)((float)i2 + 0.375F), (double)((float)i2 + 0.375F), (double)((float)i3 + 0.25F), (double)((float)i3 + 0.25F + f12), (double)((float)i4 + 0.375F), (double)((float)i4 + 0.625F), f11 * 0.6F, d13);
			this.renderPistonRodUD((double)((float)i2 + 0.625F), (double)((float)i2 + 0.625F), (double)((float)i3 + 0.25F), (double)((float)i3 + 0.25F + f12), (double)((float)i4 + 0.625F), (double)((float)i4 + 0.375F), f11 * 0.6F, d13);
			break;
		case 1:
			block1.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(block1, i2, i3, i4);
			this.renderPistonRodUD((double)((float)i2 + 0.375F), (double)((float)i2 + 0.625F), (double)((float)i3 - 0.25F + 1.0F - f12), (double)((float)i3 - 0.25F + 1.0F), (double)((float)i4 + 0.625F), (double)((float)i4 + 0.625F), f11 * 0.8F, d13);
			this.renderPistonRodUD((double)((float)i2 + 0.625F), (double)((float)i2 + 0.375F), (double)((float)i3 - 0.25F + 1.0F - f12), (double)((float)i3 - 0.25F + 1.0F), (double)((float)i4 + 0.375F), (double)((float)i4 + 0.375F), f11 * 0.8F, d13);
			this.renderPistonRodUD((double)((float)i2 + 0.375F), (double)((float)i2 + 0.375F), (double)((float)i3 - 0.25F + 1.0F - f12), (double)((float)i3 - 0.25F + 1.0F), (double)((float)i4 + 0.375F), (double)((float)i4 + 0.625F), f11 * 0.6F, d13);
			this.renderPistonRodUD((double)((float)i2 + 0.625F), (double)((float)i2 + 0.625F), (double)((float)i3 - 0.25F + 1.0F - f12), (double)((float)i3 - 0.25F + 1.0F), (double)((float)i4 + 0.625F), (double)((float)i4 + 0.375F), f11 * 0.6F, d13);
			break;
		case 2:
			this.uvRotateSouth = 1;
			this.uvRotateNorth = 2;
			block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
			this.renderStandardBlock(block1, i2, i3, i4);
			this.renderPistonRodSN((double)((float)i2 + 0.375F), (double)((float)i2 + 0.375F), (double)((float)i3 + 0.625F), (double)((float)i3 + 0.375F), (double)((float)i4 + 0.25F), (double)((float)i4 + 0.25F + f12), f11 * 0.6F, d13);
			this.renderPistonRodSN((double)((float)i2 + 0.625F), (double)((float)i2 + 0.625F), (double)((float)i3 + 0.375F), (double)((float)i3 + 0.625F), (double)((float)i4 + 0.25F), (double)((float)i4 + 0.25F + f12), f11 * 0.6F, d13);
			this.renderPistonRodSN((double)((float)i2 + 0.375F), (double)((float)i2 + 0.625F), (double)((float)i3 + 0.375F), (double)((float)i3 + 0.375F), (double)((float)i4 + 0.25F), (double)((float)i4 + 0.25F + f12), f11 * 0.5F, d13);
			this.renderPistonRodSN((double)((float)i2 + 0.625F), (double)((float)i2 + 0.375F), (double)((float)i3 + 0.625F), (double)((float)i3 + 0.625F), (double)((float)i4 + 0.25F), (double)((float)i4 + 0.25F + f12), f11, d13);
			break;
		case 3:
			this.uvRotateSouth = 2;
			this.uvRotateNorth = 1;
			this.uvRotateTop = 3;
			this.uvRotateBottom = 3;
			block1.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(block1, i2, i3, i4);
			this.renderPistonRodSN((double)((float)i2 + 0.375F), (double)((float)i2 + 0.375F), (double)((float)i3 + 0.625F), (double)((float)i3 + 0.375F), (double)((float)i4 - 0.25F + 1.0F - f12), (double)((float)i4 - 0.25F + 1.0F), f11 * 0.6F, d13);
			this.renderPistonRodSN((double)((float)i2 + 0.625F), (double)((float)i2 + 0.625F), (double)((float)i3 + 0.375F), (double)((float)i3 + 0.625F), (double)((float)i4 - 0.25F + 1.0F - f12), (double)((float)i4 - 0.25F + 1.0F), f11 * 0.6F, d13);
			this.renderPistonRodSN((double)((float)i2 + 0.375F), (double)((float)i2 + 0.625F), (double)((float)i3 + 0.375F), (double)((float)i3 + 0.375F), (double)((float)i4 - 0.25F + 1.0F - f12), (double)((float)i4 - 0.25F + 1.0F), f11 * 0.5F, d13);
			this.renderPistonRodSN((double)((float)i2 + 0.625F), (double)((float)i2 + 0.375F), (double)((float)i3 + 0.625F), (double)((float)i3 + 0.625F), (double)((float)i4 - 0.25F + 1.0F - f12), (double)((float)i4 - 0.25F + 1.0F), f11, d13);
			break;
		case 4:
			this.uvRotateEast = 1;
			this.uvRotateWest = 2;
			this.uvRotateTop = 2;
			this.uvRotateBottom = 1;
			block1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
			this.renderStandardBlock(block1, i2, i3, i4);
			this.renderPistonRodEW((double)((float)i2 + 0.25F), (double)((float)i2 + 0.25F + f12), (double)((float)i3 + 0.375F), (double)((float)i3 + 0.375F), (double)((float)i4 + 0.625F), (double)((float)i4 + 0.375F), f11 * 0.5F, d13);
			this.renderPistonRodEW((double)((float)i2 + 0.25F), (double)((float)i2 + 0.25F + f12), (double)((float)i3 + 0.625F), (double)((float)i3 + 0.625F), (double)((float)i4 + 0.375F), (double)((float)i4 + 0.625F), f11, d13);
			this.renderPistonRodEW((double)((float)i2 + 0.25F), (double)((float)i2 + 0.25F + f12), (double)((float)i3 + 0.375F), (double)((float)i3 + 0.625F), (double)((float)i4 + 0.375F), (double)((float)i4 + 0.375F), f11 * 0.6F, d13);
			this.renderPistonRodEW((double)((float)i2 + 0.25F), (double)((float)i2 + 0.25F + f12), (double)((float)i3 + 0.625F), (double)((float)i3 + 0.375F), (double)((float)i4 + 0.625F), (double)((float)i4 + 0.625F), f11 * 0.6F, d13);
			break;
		case 5:
			this.uvRotateEast = 2;
			this.uvRotateWest = 1;
			this.uvRotateTop = 1;
			this.uvRotateBottom = 2;
			block1.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(block1, i2, i3, i4);
			this.renderPistonRodEW((double)((float)i2 - 0.25F + 1.0F - f12), (double)((float)i2 - 0.25F + 1.0F), (double)((float)i3 + 0.375F), (double)((float)i3 + 0.375F), (double)((float)i4 + 0.625F), (double)((float)i4 + 0.375F), f11 * 0.5F, d13);
			this.renderPistonRodEW((double)((float)i2 - 0.25F + 1.0F - f12), (double)((float)i2 - 0.25F + 1.0F), (double)((float)i3 + 0.625F), (double)((float)i3 + 0.625F), (double)((float)i4 + 0.375F), (double)((float)i4 + 0.625F), f11, d13);
			this.renderPistonRodEW((double)((float)i2 - 0.25F + 1.0F - f12), (double)((float)i2 - 0.25F + 1.0F), (double)((float)i3 + 0.375F), (double)((float)i3 + 0.625F), (double)((float)i4 + 0.375F), (double)((float)i4 + 0.375F), f11 * 0.6F, d13);
			this.renderPistonRodEW((double)((float)i2 - 0.25F + 1.0F - f12), (double)((float)i2 - 0.25F + 1.0F), (double)((float)i3 + 0.625F), (double)((float)i3 + 0.375F), (double)((float)i4 + 0.625F), (double)((float)i4 + 0.625F), f11 * 0.6F, d13);
		}

		this.uvRotateEast = 0;
		this.uvRotateWest = 0;
		this.uvRotateSouth = 0;
		this.uvRotateNorth = 0;
		this.uvRotateTop = 0;
		this.uvRotateBottom = 0;
		block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockLever(Block block1, int i2, int i3, int i4) {
		int i5 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		int i6 = i5 & 7;
		boolean z7 = (i5 & 8) > 0;
		Tessellator tessellator8 = Tessellator.instance;
		boolean z9 = this.overrideBlockTexture >= 0;
		if(!z9) {
			this.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
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

		this.renderStandardBlock(block1, i2, i3, i4);
		if(!z9) {
			this.overrideBlockTexture = -1;
		}

		tessellator8.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
		float f13 = 1.0F;
		tessellator8.setColorOpaque_F(f13, f13, f13);
		int i14 = block1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i14 = this.overrideBlockTexture;
		}

		int i15 = (i14 & 15) << 4;
		int i16 = i14 & 240;
		float f17 = (float)i15 / 256.0F;
		float f18 = ((float)i15 + 15.99F) / 256.0F;
		float f19 = (float)i16 / 256.0F;
		float f20 = ((float)i16 + 15.99F) / 256.0F;
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
				f17 = (float)(i15 + 7) / 256.0F;
				f18 = ((float)(i15 + 9) - 0.01F) / 256.0F;
				f19 = (float)(i16 + 6) / 256.0F;
				f20 = ((float)(i16 + 8) - 0.01F) / 256.0F;
			} else if(i29 == 2) {
				f17 = (float)(i15 + 7) / 256.0F;
				f18 = ((float)(i15 + 9) - 0.01F) / 256.0F;
				f19 = (float)(i16 + 6) / 256.0F;
				f20 = ((float)(i16 + 16) - 0.01F) / 256.0F;
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

	public boolean renderBlockFire(Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = block1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i6 = this.overrideBlockTexture;
		}

		tessellator5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator5.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
		
		int i8 = (i6 & 15) << 4;
		int i9 = i6 & 240;
		double d10 = (double)((float)i8 / 256.0F);
		double d12 = (double)(((float)i8 + 15.99F) / 256.0F);
		double d14 = (double)((float)i9 / 256.0F);
		double d16 = (double)(((float)i9 + 15.99F) / 256.0F);
		float f18 = 1.4F;
		double d21;
		double d23;
		double d25;
		double d27;
		double d29;
		double d31;
		double d33;
		if(!this.blockAccess.isBlockNormalCube(i2, i3 - 1, i4) && !Block.fire.canBlockCatchFire(this.blockAccess, i2, i3 - 1, i4)) {
			float f37 = 0.2F;
			float f20 = 0.0625F;
			if((i2 + i3 + i4 & 1) == 1) {
				d10 = (double)((float)i8 / 256.0F);
				d12 = (double)(((float)i8 + 15.99F) / 256.0F);
				d14 = (double)((float)(i9 + 16) / 256.0F);
				d16 = (double)(((float)i9 + 15.99F + 16.0F) / 256.0F);
			}

			if((i2 / 2 + i3 / 2 + i4 / 2 & 1) == 1) {
				d21 = d12;
				d12 = d10;
				d10 = d21;
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i2 - 1, i3, i4)) {
				tessellator5.addVertexWithUV((double)((float)i2 + f37), (double)((float)i3 + f18 + f20), (double)(i4 + 1), d12, d14);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 1), d12, d16);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 0), d10, d16);
				tessellator5.addVertexWithUV((double)((float)i2 + f37), (double)((float)i3 + f18 + f20), (double)(i4 + 0), d10, d14);
				tessellator5.addVertexWithUV((double)((float)i2 + f37), (double)((float)i3 + f18 + f20), (double)(i4 + 0), d10, d14);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 0), d10, d16);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 1), d12, d16);
				tessellator5.addVertexWithUV((double)((float)i2 + f37), (double)((float)i3 + f18 + f20), (double)(i4 + 1), d12, d14);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i2 + 1, i3, i4)) {
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - f37), (double)((float)i3 + f18 + f20), (double)(i4 + 0), d10, d14);
				tessellator5.addVertexWithUV((double)(i2 + 1 - 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 0), d10, d16);
				tessellator5.addVertexWithUV((double)(i2 + 1 - 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 1), d12, d16);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - f37), (double)((float)i3 + f18 + f20), (double)(i4 + 1), d12, d14);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - f37), (double)((float)i3 + f18 + f20), (double)(i4 + 1), d12, d14);
				tessellator5.addVertexWithUV((double)(i2 + 1 - 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 1), d12, d16);
				tessellator5.addVertexWithUV((double)(i2 + 1 - 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 0), d10, d16);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - f37), (double)((float)i3 + f18 + f20), (double)(i4 + 0), d10, d14);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i2, i3, i4 - 1)) {
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18 + f20), (double)((float)i4 + f37), d12, d14);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 0), d12, d16);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)(i3 + 0) + f20), (double)(i4 + 0), d10, d16);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18 + f20), (double)((float)i4 + f37), d10, d14);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18 + f20), (double)((float)i4 + f37), d10, d14);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)(i3 + 0) + f20), (double)(i4 + 0), d10, d16);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 0), d12, d16);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18 + f20), (double)((float)i4 + f37), d12, d14);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i2, i3, i4 + 1)) {
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18 + f20), (double)((float)(i4 + 1) - f37), d10, d14);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)(i3 + 0) + f20), (double)(i4 + 1 - 0), d10, d16);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 1 - 0), d12, d16);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18 + f20), (double)((float)(i4 + 1) - f37), d12, d14);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18 + f20), (double)((float)(i4 + 1) - f37), d12, d14);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 0) + f20), (double)(i4 + 1 - 0), d12, d16);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)(i3 + 0) + f20), (double)(i4 + 1 - 0), d10, d16);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18 + f20), (double)((float)(i4 + 1) - f37), d10, d14);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i2, i3 + 1, i4)) {
				d21 = (double)i2 + 0.5D + 0.5D;
				d23 = (double)i2 + 0.5D - 0.5D;
				d25 = (double)i4 + 0.5D + 0.5D;
				d27 = (double)i4 + 0.5D - 0.5D;
				d29 = (double)i2 + 0.5D - 0.5D;
				d31 = (double)i2 + 0.5D + 0.5D;
				d33 = (double)i4 + 0.5D - 0.5D;
				double d35 = (double)i4 + 0.5D + 0.5D;
				d10 = (double)((float)i8 / 256.0F);
				d12 = (double)(((float)i8 + 15.99F) / 256.0F);
				d14 = (double)((float)i9 / 256.0F);
				d16 = (double)(((float)i9 + 15.99F) / 256.0F);
				++i3;
				f18 = -0.2F;
				if((i2 + i3 + i4 & 1) == 0) {
					tessellator5.addVertexWithUV(d29, (double)((float)i3 + f18), (double)(i4 + 0), d12, d14);
					tessellator5.addVertexWithUV(d21, (double)(i3 + 0), (double)(i4 + 0), d12, d16);
					tessellator5.addVertexWithUV(d21, (double)(i3 + 0), (double)(i4 + 1), d10, d16);
					tessellator5.addVertexWithUV(d29, (double)((float)i3 + f18), (double)(i4 + 1), d10, d14);
					d10 = (double)((float)i8 / 256.0F);
					d12 = (double)(((float)i8 + 15.99F) / 256.0F);
					d14 = (double)((float)(i9 + 16) / 256.0F);
					d16 = (double)(((float)i9 + 15.99F + 16.0F) / 256.0F);
					tessellator5.addVertexWithUV(d31, (double)((float)i3 + f18), (double)(i4 + 1), d12, d14);
					tessellator5.addVertexWithUV(d23, (double)(i3 + 0), (double)(i4 + 1), d12, d16);
					tessellator5.addVertexWithUV(d23, (double)(i3 + 0), (double)(i4 + 0), d10, d16);
					tessellator5.addVertexWithUV(d31, (double)((float)i3 + f18), (double)(i4 + 0), d10, d14);
				} else {
					tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18), d35, d12, d14);
					tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), d27, d12, d16);
					tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), d27, d10, d16);
					tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18), d35, d10, d14);
					d10 = (double)((float)i8 / 256.0F);
					d12 = (double)(((float)i8 + 15.99F) / 256.0F);
					d14 = (double)((float)(i9 + 16) / 256.0F);
					d16 = (double)(((float)i9 + 15.99F + 16.0F) / 256.0F);
					tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18), d33, d12, d14);
					tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), d25, d12, d16);
					tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), d25, d10, d16);
					tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18), d33, d10, d14);
				}
			}
		} else {
			double d19 = (double)i2 + 0.5D + 0.2D;
			d21 = (double)i2 + 0.5D - 0.2D;
			d23 = (double)i4 + 0.5D + 0.2D;
			d25 = (double)i4 + 0.5D - 0.2D;
			d27 = (double)i2 + 0.5D - 0.3D;
			d29 = (double)i2 + 0.5D + 0.3D;
			d31 = (double)i4 + 0.5D - 0.3D;
			d33 = (double)i4 + 0.5D + 0.3D;
			tessellator5.addVertexWithUV(d27, (double)((float)i3 + f18), (double)(i4 + 1), d12, d14);
			tessellator5.addVertexWithUV(d19, (double)(i3 + 0), (double)(i4 + 1), d12, d16);
			tessellator5.addVertexWithUV(d19, (double)(i3 + 0), (double)(i4 + 0), d10, d16);
			tessellator5.addVertexWithUV(d27, (double)((float)i3 + f18), (double)(i4 + 0), d10, d14);
			tessellator5.addVertexWithUV(d29, (double)((float)i3 + f18), (double)(i4 + 0), d12, d14);
			tessellator5.addVertexWithUV(d21, (double)(i3 + 0), (double)(i4 + 0), d12, d16);
			tessellator5.addVertexWithUV(d21, (double)(i3 + 0), (double)(i4 + 1), d10, d16);
			tessellator5.addVertexWithUV(d29, (double)((float)i3 + f18), (double)(i4 + 1), d10, d14);
			d10 = (double)((float)i8 / 256.0F);
			d12 = (double)(((float)i8 + 15.99F) / 256.0F);
			d14 = (double)((float)(i9 + 16) / 256.0F);
			d16 = (double)(((float)i9 + 15.99F + 16.0F) / 256.0F);
			tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18), d33, d12, d14);
			tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), d25, d12, d16);
			tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), d25, d10, d16);
			tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18), d33, d10, d14);
			tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18), d31, d12, d14);
			tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), d23, d12, d16);
			tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), d23, d10, d16);
			tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18), d31, d10, d14);
			d19 = (double)i2 + 0.5D - 0.5D;
			d21 = (double)i2 + 0.5D + 0.5D;
			d23 = (double)i4 + 0.5D - 0.5D;
			d25 = (double)i4 + 0.5D + 0.5D;
			d27 = (double)i2 + 0.5D - 0.4D;
			d29 = (double)i2 + 0.5D + 0.4D;
			d31 = (double)i4 + 0.5D - 0.4D;
			d33 = (double)i4 + 0.5D + 0.4D;
			tessellator5.addVertexWithUV(d27, (double)((float)i3 + f18), (double)(i4 + 0), d10, d14);
			tessellator5.addVertexWithUV(d19, (double)(i3 + 0), (double)(i4 + 0), d10, d16);
			tessellator5.addVertexWithUV(d19, (double)(i3 + 0), (double)(i4 + 1), d12, d16);
			tessellator5.addVertexWithUV(d27, (double)((float)i3 + f18), (double)(i4 + 1), d12, d14);
			tessellator5.addVertexWithUV(d29, (double)((float)i3 + f18), (double)(i4 + 1), d10, d14);
			tessellator5.addVertexWithUV(d21, (double)(i3 + 0), (double)(i4 + 1), d10, d16);
			tessellator5.addVertexWithUV(d21, (double)(i3 + 0), (double)(i4 + 0), d12, d16);
			tessellator5.addVertexWithUV(d29, (double)((float)i3 + f18), (double)(i4 + 0), d12, d14);
			d10 = (double)((float)i8 / 256.0F);
			d12 = (double)(((float)i8 + 15.99F) / 256.0F);
			d14 = (double)((float)i9 / 256.0F);
			d16 = (double)(((float)i9 + 15.99F) / 256.0F);
			tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18), d33, d10, d14);
			tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), d25, d10, d16);
			tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), d25, d12, d16);
			tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18), d33, d12, d14);
			tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f18), d31, d10, d14);
			tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), d23, d10, d16);
			tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), d23, d12, d16);
			tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f18), d31, d12, d14);
		}

		return true;
	}

	public boolean renderBlockRedstoneWire(Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		int i7 = block1.getBlockTextureFromSideAndMetadata(1, i6);
		if(this.overrideBlockTexture >= 0) {
			i7 = this.overrideBlockTexture;
		}

		tessellator5.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
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
		int i13 = (i7 & 15) << 4;
		int i14 = i7 & 240;
		double d15 = (double)((float)i13 / 256.0F);
		double d17 = (double)(((float)i13 + 15.99F) / 256.0F);
		double d19 = (double)((float)i14 / 256.0F);
		double d21 = (double)(((float)i14 + 15.99F) / 256.0F);
		boolean z26 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2 - 1, i3, i4, 1) || !this.blockAccess.isBlockNormalCube(i2 - 1, i3, i4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2 - 1, i3 - 1, i4, -1);
		boolean z27 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2 + 1, i3, i4, 3) || !this.blockAccess.isBlockNormalCube(i2 + 1, i3, i4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2 + 1, i3 - 1, i4, -1);
		boolean z28 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2, i3, i4 - 1, 2) || !this.blockAccess.isBlockNormalCube(i2, i3, i4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2, i3 - 1, i4 - 1, -1);
		boolean z29 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2, i3, i4 + 1, 0) || !this.blockAccess.isBlockNormalCube(i2, i3, i4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2, i3 - 1, i4 + 1, -1);
		if(!this.blockAccess.isBlockNormalCube(i2, i3 + 1, i4)) {
			if(this.blockAccess.isBlockNormalCube(i2 - 1, i3, i4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2 - 1, i3 + 1, i4, -1)) {
				z26 = true;
			}

			if(this.blockAccess.isBlockNormalCube(i2 + 1, i3, i4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2 + 1, i3 + 1, i4, -1)) {
				z27 = true;
			}

			if(this.blockAccess.isBlockNormalCube(i2, i3, i4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2, i3 + 1, i4 - 1, -1)) {
				z28 = true;
			}

			if(this.blockAccess.isBlockNormalCube(i2, i3, i4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i2, i3 + 1, i4 + 1, -1)) {
				z29 = true;
			}
		}

		float f31 = (float)(i2 + 0);
		float f32 = (float)(i2 + 1);
		float f33 = (float)(i4 + 0);
		float f34 = (float)(i4 + 1);
		byte b35 = 0;
		if((z26 || z27) && !z28 && !z29) {
			b35 = 1;
		}

		if((z28 || z29) && !z27 && !z26) {
			b35 = 2;
		}

		if(b35 != 0) {
			d15 = (double)((float)(i13 + 16) / 256.0F);
			d17 = (double)(((float)(i13 + 16) + 15.99F) / 256.0F);
			d19 = (double)((float)i14 / 256.0F);
			d21 = (double)(((float)i14 + 15.99F) / 256.0F);
		}

		if(b35 == 0) {
			if(z27 || z28 || z29 || z26) {
				if(!z26) {
					f31 += 0.3125F;
				}

				if(!z26) {
					d15 += 0.01953125D;
				}

				if(!z27) {
					f32 -= 0.3125F;
				}

				if(!z27) {
					d17 -= 0.01953125D;
				}

				if(!z28) {
					f33 += 0.3125F;
				}

				if(!z28) {
					d19 += 0.01953125D;
				}

				if(!z29) {
					f34 -= 0.3125F;
				}

				if(!z29) {
					d21 -= 0.01953125D;
				}
			}

			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f34, d17, d21);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f33, d17, d19);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f33, d15, d19);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f34, d15, d21);
			/*tessellator5.setColorOpaque_F(f8, f8, f8);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f34, d17, d21 + 0.0625D);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f33, d17, d19 + 0.0625D);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f33, d15, d19 + 0.0625D);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f34, d15, d21 + 0.0625D);*/
		} else if(b35 == 1) {
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f34, d17, d21);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f33, d17, d19);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f33, d15, d19);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f34, d15, d21);
			/*tessellator5.setColorOpaque_F(f8, f8, f8);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f34, d17, d21 + 0.0625D);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f33, d17, d19 + 0.0625D);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f33, d15, d19 + 0.0625D);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f34, d15, d21 + 0.0625D);*/
		} else if(b35 == 2) {
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f34, d17, d21);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f33, d15, d21);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f33, d15, d19);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f34, d17, d19);
			/*tessellator5.setColorOpaque_F(f8, f8, f8);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f34, d17, d21 + 0.0625D);
			tessellator5.addVertexWithUV((double)f32, (double)i3 + 0.015625D, (double)f33, d15, d21 + 0.0625D);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f33, d15, d19 + 0.0625D);
			tessellator5.addVertexWithUV((double)f31, (double)i3 + 0.015625D, (double)f34, d17, d19 + 0.0625D);*/
		}

		if(!this.blockAccess.isBlockNormalCube(i2, i3 + 1, i4)) {
			d15 = (double)((float)(i13 + 16) / 256.0F);
			d17 = (double)(((float)(i13 + 16) + 15.99F) / 256.0F);
			d19 = (double)((float)i14 / 256.0F);
			d21 = (double)(((float)i14 + 15.99F) / 256.0F);
			if(this.blockAccess.isBlockNormalCube(i2 - 1, i3, i4) && this.blockAccess.getBlockId(i2 - 1, i3 + 1, i4) == Block.redstoneWire.blockID) {
				tessellator5.setColorOpaque_F(f8 * f10, f8 * f11, f8 * f12);
				tessellator5.addVertexWithUV((double)((float)i2 + 0.015625F), (double)((float)(i3 + 1) + 0.021875F), (double)(i4 + 1), d17, d19);
				tessellator5.addVertexWithUV((double)((float)i2 + 0.015625F), (double)(i3 + 0), (double)(i4 + 1), d15, d19);
				tessellator5.addVertexWithUV((double)((float)i2 + 0.015625F), (double)(i3 + 0), (double)(i4 + 0), d15, d21);
				tessellator5.addVertexWithUV((double)((float)i2 + 0.015625F), (double)((float)(i3 + 1) + 0.021875F), (double)(i4 + 0), d17, d21);
				/*tessellator5.setColorOpaque_F(f8, f8, f8);
				tessellator5.addVertexWithUV((double)((float)i2 + 0.015625F), (double)((float)(i3 + 1) + 0.021875F), (double)(i4 + 1), d17, d19 + 0.0625D);
				tessellator5.addVertexWithUV((double)((float)i2 + 0.015625F), (double)(i3 + 0), (double)(i4 + 1), d15, d19 + 0.0625D);
				tessellator5.addVertexWithUV((double)((float)i2 + 0.015625F), (double)(i3 + 0), (double)(i4 + 0), d15, d21 + 0.0625D);
				tessellator5.addVertexWithUV((double)((float)i2 + 0.015625F), (double)((float)(i3 + 1) + 0.021875F), (double)(i4 + 0), d17, d21 + 0.0625D);*/
			}

			if(this.blockAccess.isBlockNormalCube(i2 + 1, i3, i4) && this.blockAccess.getBlockId(i2 + 1, i3 + 1, i4) == Block.redstoneWire.blockID) {
				tessellator5.setColorOpaque_F(f8 * f10, f8 * f11, f8 * f12);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - 0.015625F), (double)(i3 + 0), (double)(i4 + 1), d15, d21);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - 0.015625F), (double)((float)(i3 + 1) + 0.021875F), (double)(i4 + 1), d17, d21);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - 0.015625F), (double)((float)(i3 + 1) + 0.021875F), (double)(i4 + 0), d17, d19);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - 0.015625F), (double)(i3 + 0), (double)(i4 + 0), d15, d19);
				/*tessellator5.setColorOpaque_F(f8, f8, f8);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - 0.015625F), (double)(i3 + 0), (double)(i4 + 1), d15, d21 + 0.0625D);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - 0.015625F), (double)((float)(i3 + 1) + 0.021875F), (double)(i4 + 1), d17, d21 + 0.0625D);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - 0.015625F), (double)((float)(i3 + 1) + 0.021875F), (double)(i4 + 0), d17, d19 + 0.0625D);
				tessellator5.addVertexWithUV((double)((float)(i2 + 1) - 0.015625F), (double)(i3 + 0), (double)(i4 + 0), d15, d19 + 0.0625D);*/
			}

			if(this.blockAccess.isBlockNormalCube(i2, i3, i4 - 1) && this.blockAccess.getBlockId(i2, i3 + 1, i4 - 1) == Block.redstoneWire.blockID) {
				tessellator5.setColorOpaque_F(f8 * f10, f8 * f11, f8 * f12);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), (double)((float)i4 + 0.015625F), d15, d21);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)(i3 + 1) + 0.021875F), (double)((float)i4 + 0.015625F), d17, d21);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 1) + 0.021875F), (double)((float)i4 + 0.015625F), d17, d19);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), (double)((float)i4 + 0.015625F), d15, d19);
				/*tessellator5.setColorOpaque_F(f8, f8, f8);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), (double)((float)i4 + 0.015625F), d15, d21 + 0.0625D);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)(i3 + 1) + 0.021875F), (double)((float)i4 + 0.015625F), d17, d21 + 0.0625D);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 1) + 0.021875F), (double)((float)i4 + 0.015625F), d17, d19 + 0.0625D);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), (double)((float)i4 + 0.015625F), d15, d19 + 0.0625D);*/
			}

			if(this.blockAccess.isBlockNormalCube(i2, i3, i4 + 1) && this.blockAccess.getBlockId(i2, i3 + 1, i4 + 1) == Block.redstoneWire.blockID) {
				tessellator5.setColorOpaque_F(f8 * f10, f8 * f11, f8 * f12);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)(i3 + 1) + 0.021875F), (double)((float)(i4 + 1) - 0.015625F), d17, d19);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), (double)((float)(i4 + 1) - 0.015625F), d15, d19);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), (double)((float)(i4 + 1) - 0.015625F), d15, d21);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 1) + 0.021875F), (double)((float)(i4 + 1) - 0.015625F), d17, d21);
				/*tessellator5.setColorOpaque_F(f8, f8, f8);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)(i3 + 1) + 0.021875F), (double)((float)(i4 + 1) - 0.015625F), d17, d19 + 0.0625D);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)(i3 + 0), (double)((float)(i4 + 1) - 0.015625F), d15, d19 + 0.0625D);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)(i3 + 0), (double)((float)(i4 + 1) - 0.015625F), d15, d21 + 0.0625D);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)(i3 + 1) + 0.021875F), (double)((float)(i4 + 1) - 0.015625F), d17, d21 + 0.0625D);*/
			}
		}

		return true;
	}

	public boolean renderBlockrail(BlockRail blockRail1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		int i7 = blockRail1.getBlockTextureFromSideAndMetadata(0, i6);
		if(this.overrideBlockTexture >= 0) {
			i7 = this.overrideBlockTexture;
		}

		if(blockRail1.getIsPowered()) {
			i6 &= 7;
		}

		tessellator5.setBrightness(blockRail1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
		tessellator5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		
		int i9 = (i7 & 15) << 4;
		int i10 = i7 & 240;
		double d11 = (double)((float)i9 / 256.0F);
		double d13 = (double)(((float)i9 + 15.99F) / 256.0F);
		double d15 = (double)((float)i10 / 256.0F);
		double d17 = (double)(((float)i10 + 15.99F) / 256.0F);
		float f19 = 0.0625F;
		float f20 = (float)(i2 + 1);
		float f21 = (float)(i2 + 1);
		float f22 = (float)(i2 + 0);
		float f23 = (float)(i2 + 0);
		float f24 = (float)(i4 + 0);
		float f25 = (float)(i4 + 1);
		float f26 = (float)(i4 + 1);
		float f27 = (float)(i4 + 0);
		float f28 = (float)i3 + f19;
		float f29 = (float)i3 + f19;
		float f30 = (float)i3 + f19;
		float f31 = (float)i3 + f19;
		if(i6 != 1 && i6 != 2 && i6 != 3 && i6 != 7) {
			if(i6 == 8) {
				f20 = f21 = (float)(i2 + 0);
				f22 = f23 = (float)(i2 + 1);
				f24 = f27 = (float)(i4 + 1);
				f25 = f26 = (float)(i4 + 0);
			} else if(i6 == 9) {
				f20 = f23 = (float)(i2 + 0);
				f21 = f22 = (float)(i2 + 1);
				f24 = f25 = (float)(i4 + 0);
				f26 = f27 = (float)(i4 + 1);
			}
		} else {
			f20 = f23 = (float)(i2 + 1);
			f21 = f22 = (float)(i2 + 0);
			f24 = f25 = (float)(i4 + 1);
			f26 = f27 = (float)(i4 + 0);
		}

		if(i6 != 2 && i6 != 4) {
			if(i6 == 3 || i6 == 5) {
				++f29;
				++f30;
			}
		} else {
			++f28;
			++f31;
		}

		tessellator5.addVertexWithUV((double)f20, (double)f28, (double)f24, d13, d15);
		tessellator5.addVertexWithUV((double)f21, (double)f29, (double)f25, d13, d17);
		tessellator5.addVertexWithUV((double)f22, (double)f30, (double)f26, d11, d17);
		tessellator5.addVertexWithUV((double)f23, (double)f31, (double)f27, d11, d15);
		tessellator5.addVertexWithUV((double)f23, (double)f31, (double)f27, d11, d15);
		tessellator5.addVertexWithUV((double)f22, (double)f30, (double)f26, d11, d17);
		tessellator5.addVertexWithUV((double)f21, (double)f29, (double)f25, d13, d17);
		tessellator5.addVertexWithUV((double)f20, (double)f28, (double)f24, d13, d15);
		return true;
	}

	public boolean renderBlockLadder(Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = block1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i6 = this.overrideBlockTexture;
		}

		tessellator5.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
		float f7 = 1.0F;
		tessellator5.setColorOpaque_F(f7, f7, f7);
		int i8 = (i6 & 15) << 4;
		int i9 = i6 & 240;
		double d10 = (double)((float)i8 / 256.0F);
		double d12 = (double)(((float)i8 + 15.99F) / 256.0F);
		double d14 = (double)((float)i9 / 256.0F);
		double d16 = (double)(((float)i9 + 15.99F) / 256.0F);
		int i18 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		float f19 = 0.0F;
		float f20 = 0.05F;
		if(i18 == 5) {
			tessellator5.addVertexWithUV((double)((float)i2 + f20), (double)((float)(i3 + 1) + f19), (double)((float)(i4 + 1) + f19), d10, d14);
			tessellator5.addVertexWithUV((double)((float)i2 + f20), (double)((float)(i3 + 0) - f19), (double)((float)(i4 + 1) + f19), d10, d16);
			tessellator5.addVertexWithUV((double)((float)i2 + f20), (double)((float)(i3 + 0) - f19), (double)((float)(i4 + 0) - f19), d12, d16);
			tessellator5.addVertexWithUV((double)((float)i2 + f20), (double)((float)(i3 + 1) + f19), (double)((float)(i4 + 0) - f19), d12, d14);
		}

		if(i18 == 4) {
			tessellator5.addVertexWithUV((double)((float)(i2 + 1) - f20), (double)((float)(i3 + 0) - f19), (double)((float)(i4 + 1) + f19), d12, d16);
			tessellator5.addVertexWithUV((double)((float)(i2 + 1) - f20), (double)((float)(i3 + 1) + f19), (double)((float)(i4 + 1) + f19), d12, d14);
			tessellator5.addVertexWithUV((double)((float)(i2 + 1) - f20), (double)((float)(i3 + 1) + f19), (double)((float)(i4 + 0) - f19), d10, d14);
			tessellator5.addVertexWithUV((double)((float)(i2 + 1) - f20), (double)((float)(i3 + 0) - f19), (double)((float)(i4 + 0) - f19), d10, d16);
		}

		if(i18 == 3) {
			tessellator5.addVertexWithUV((double)((float)(i2 + 1) + f19), (double)((float)(i3 + 0) - f19), (double)((float)i4 + f20), d12, d16);
			tessellator5.addVertexWithUV((double)((float)(i2 + 1) + f19), (double)((float)(i3 + 1) + f19), (double)((float)i4 + f20), d12, d14);
			tessellator5.addVertexWithUV((double)((float)(i2 + 0) - f19), (double)((float)(i3 + 1) + f19), (double)((float)i4 + f20), d10, d14);
			tessellator5.addVertexWithUV((double)((float)(i2 + 0) - f19), (double)((float)(i3 + 0) - f19), (double)((float)i4 + f20), d10, d16);
		}

		if(i18 == 2) {
			tessellator5.addVertexWithUV((double)((float)(i2 + 1) + f19), (double)((float)(i3 + 1) + f19), (double)((float)(i4 + 1) - f20), d10, d14);
			tessellator5.addVertexWithUV((double)((float)(i2 + 1) + f19), (double)((float)(i3 + 0) - f19), (double)((float)(i4 + 1) - f20), d10, d16);
			tessellator5.addVertexWithUV((double)((float)(i2 + 0) - f19), (double)((float)(i3 + 0) - f19), (double)((float)(i4 + 1) - f20), d12, d16);
			tessellator5.addVertexWithUV((double)((float)(i2 + 0) - f19), (double)((float)(i3 + 1) + f19), (double)((float)(i4 + 1) - f20), d12, d14);
		}

		return true;
	}
	
	public boolean renderBlockVine(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			l = this.overrideBlockTexture;
		}

		tessellator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, i, j, k));
		
		int var7 = block.colorMultiplier(this.blockAccess, i, j, k);
		float var8 = (float)(var7 >> 16 & 255) / 255.0F;
		float var9 = (float)(var7 >> 8 & 255) / 255.0F;
		float var10 = (float)(var7 & 255) / 255.0F;
		
		tessellator.setColorOpaque_F(var8, var9, var10);
		
		int i11 = (l & 15) << 4;
		int i12 = l & 240;
		double d = (double)((float)i11 / 256.0F);
		double d1 = (double)(((float)i11 + 15.99F) / 256.0F);
		double d2 = (double)((float)i12 / 256.0F);
		double d3 = (double)(((float)i12 + 15.99F) / 256.0F);

		double d4 = 0.05D;
		
		int meta = this.blockAccess.getBlockMetadata(i, j, k);
		
		if ((meta & 2) != 0) {
			tessellator.addVertexWithUV((double)i + d4, j + 1, k + 1, d, d2);
			tessellator.addVertexWithUV((double)i + d4, j + 0, k + 1, d, d3);
			tessellator.addVertexWithUV((double)i + d4, j + 0, k + 0, d1, d3);
			tessellator.addVertexWithUV((double)i + d4, j + 1, k + 0, d1, d2);
			tessellator.addVertexWithUV((double)i + d4, j + 1, k + 0, d1, d2);
			tessellator.addVertexWithUV((double)i + d4, j + 0, k + 0, d1, d3);
			tessellator.addVertexWithUV((double)i + d4, j + 0, k + 1, d, d3);
			tessellator.addVertexWithUV((double)i + d4, j + 1, k + 1, d, d2);
		}

		if ((meta & 8) != 0) {
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 0, k + 1, d1, d3);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 1, k + 1, d1, d2);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 1, k + 0, d, d2);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 0, k + 0, d, d3);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 0, k + 0, d, d3);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 1, k + 0, d, d2);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 1, k + 1, d1, d2);
			tessellator.addVertexWithUV((double)(i + 1) - d4, j + 0, k + 1, d1, d3);
		}

		if ((meta & 4) != 0) {
			tessellator.addVertexWithUV(i + 1, j + 0, (double)k + d4, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + 1, (double)k + d4, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 1, (double)k + d4, d, d2);
			tessellator.addVertexWithUV(i + 0, j + 0, (double)k + d4, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 0, (double)k + d4, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 1, (double)k + d4, d, d2);
			tessellator.addVertexWithUV(i + 1, j + 1, (double)k + d4, d1, d2);
			tessellator.addVertexWithUV(i + 1, j + 0, (double)k + d4, d1, d3);
		}

		if ((meta & 1) != 0) {
			tessellator.addVertexWithUV(i + 1, j + 1, (double)(k + 1) - d4, d, d2);
			tessellator.addVertexWithUV(i + 1, j + 0, (double)(k + 1) - d4, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 0, (double)(k + 1) - d4, d1, d3);
			tessellator.addVertexWithUV(i + 0, j + 1, (double)(k + 1) - d4, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 1, (double)(k + 1) - d4, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 0, (double)(k + 1) - d4, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + 0, (double)(k + 1) - d4, d, d3);
			tessellator.addVertexWithUV(i + 1, j + 1, (double)(k + 1) - d4, d, d2);
		}
		
		if(this.blockAccess.isBlockNormalCube(i, j + 1, k)) {
			tessellator.addVertexWithUV((double)(i + 1), (double)(j + 1) - d4, (double)(k + 0), d, d2);
			tessellator.addVertexWithUV((double)(i + 1), (double)(j + 1) - d4, (double)(k + 1), d, d3);
			tessellator.addVertexWithUV((double)(i + 0), (double)(j + 1) - d4, (double)(k + 1), d1, d3);
			tessellator.addVertexWithUV((double)(i + 0), (double)(j + 1) - d4, (double)(k + 0), d1, d2);
		}
		
		return true;
	}
	
	public boolean renderBlockPane(BlockPane blockPane1, int x, int y, int z) { 
		int i5 = 128;
		Tessellator tessellator6 = Tessellator.instance;
		tessellator6.setBrightness(blockPane1.getMixedBrightnessForBlock(this.blockAccess, x, y, z));
		
		float f7 = 1.0F;
		int i8 = blockPane1.colorMultiplier(this.blockAccess, x, y, z);
		float f9 = (float)(i8 >> 16 & 255) / 255.0F;
		float f10 = (float)(i8 >> 8 & 255) / 255.0F;
		float f11 = (float)(i8 & 255) / 255.0F;
		
		tessellator6.setColorOpaque_F(f7 * f9, f7 * f10, f7 * f11);
		int i65;
		int i67;
		int i68;
		if(this.overrideBlockTexture >= 0) {
			i65 = this.overrideBlockTexture;
			i67 = this.overrideBlockTexture;
		} else {
			i68 = this.blockAccess.getBlockMetadata(x, y, z);
			i65 = blockPane1.getBlockTextureFromSideAndMetadata(0, i68);
			i67 = blockPane1.getSideTextureIndex();
		}

		i68 = (i65 & 15) << 4;
		int i15 = i65 & 0xff0;
		double faceU1 = (double)((float)i68 / 256.0);
		double faceU2 = (double)(((float)i68 + 7.99F) / 256.0);
		double faceU3 = (double)(((float)i68 + 15.99F) / 256.0);
		double faceV1 = (double)((float)i15 / 256.0);
		double faceV2 = (double)(((float)i15 + 15.99F) / 256.0);

		int i26 = (i67 & 15) << 4;
		int i27 = i67 & 0xff0;
		double sideU1 = (double)((float)(i26 + 7) / 256.0);
		double sideU2 = (double)(((float)i26 + 8.99F) / 256.0);
		double sideV3 = (double)((float)i27 / 256.0);
		double sideV1 = (double)((float)(i27 + 8) / 256.0);
		double sideV2 = (double)(((float)i27 + 15.99F) / 256.0);
		
		double d38 = (double)x;
		double d40 = (double)x + 0.5D;
		double d42 = (double)(x + 1);
		double d44 = (double)z;
		double d46 = (double)z + 0.5D;
		double d48 = (double)(z + 1);
		double d50 = (double)x + 0.5D - 0.0625D;
		double d52 = (double)x + 0.5D + 0.0625D;
		double d54 = (double)z + 0.5D - 0.0625D;
		double d56 = (double)z + 0.5D + 0.0625D;
		
		boolean z58 = blockPane1.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(x, y, z - 1));
		boolean z59 = blockPane1.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(x, y, z + 1));
		boolean z60 = blockPane1.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(x - 1, y, z));
		boolean z61 = blockPane1.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(x + 1, y, z));
		boolean z62 = blockPane1.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1);
		boolean z63 = blockPane1.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0);
		if((!z60 || !z61) && (z60 || z61 || z58 || z59)) {
			if(z60 && !z61) {
				tessellator6.addVertexWithUV(d38, (double)(y + 1), d46, faceU1, faceV1);
				tessellator6.addVertexWithUV(d38, (double)(y + 0), d46, faceU1, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d46, faceU2, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d46, faceU2, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d46, faceU1, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d46, faceU1, faceV2);
				tessellator6.addVertexWithUV(d38, (double)(y + 0), d46, faceU2, faceV2);
				tessellator6.addVertexWithUV(d38, (double)(y + 1), d46, faceU2, faceV1);
				if(!z59 && !z58) {
					tessellator6.addVertexWithUV(d40, (double)(y + 1), d56, sideU1, sideV3);
					tessellator6.addVertexWithUV(d40, (double)(y + 0), d56, sideU1, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 0), d54, sideU2, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 1), d54, sideU2, sideV3);
					tessellator6.addVertexWithUV(d40, (double)(y + 1), d54, sideU1, sideV3);
					tessellator6.addVertexWithUV(d40, (double)(y + 0), d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 0), d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 1), d56, sideU2, sideV3);
				}

				if(z62 || y < i5 - 1 && this.blockAccess.isAirBlock(x - 1, y + 1, z)) {
					tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d54, sideU1, sideV1);
				}

				if(z63 || y > 1 && this.blockAccess.isAirBlock(x - 1, y - 1, z)) {
					tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d54, sideU1, sideV1);
				}
			} else if(!z60 && z61) {
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d46, faceU2, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d46, faceU2, faceV2);
				tessellator6.addVertexWithUV(d42, (double)(y + 0), d46, faceU3, faceV2);
				tessellator6.addVertexWithUV(d42, (double)(y + 1), d46, faceU3, faceV1);
				tessellator6.addVertexWithUV(d42, (double)(y + 1), d46, faceU2, faceV1);
				tessellator6.addVertexWithUV(d42, (double)(y + 0), d46, faceU2, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d46, faceU3, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d46, faceU3, faceV1);
				if(!z59 && !z58) {
					tessellator6.addVertexWithUV(d40, (double)(y + 1), d54, sideU1, sideV3);
					tessellator6.addVertexWithUV(d40, (double)(y + 0), d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 0), d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 1), d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d40, (double)(y + 1), d56, sideU1, sideV3);
					tessellator6.addVertexWithUV(d40, (double)(y + 0), d56, sideU1, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 0), d54, sideU2, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 1), d54, sideU2, sideV3);
				}

				if(z62 || y < i5 - 1 && this.blockAccess.isAirBlock(x + 1, y + 1, z)) {
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d54, sideU1, sideV3);
					tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d54, sideU1, sideV3);
				}

				if(z63 || y > 1 && this.blockAccess.isAirBlock(x + 1, y - 1, z)) {
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d54, sideU1, sideV3);
					tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d54, sideU1, sideV3);
				}
			}
		} else {
			tessellator6.addVertexWithUV(d38, (double)(y + 1), d46, faceU1, faceV1);
			tessellator6.addVertexWithUV(d38, (double)(y + 0), d46, faceU1, faceV2);
			tessellator6.addVertexWithUV(d42, (double)(y + 0), d46, faceU3, faceV2);
			tessellator6.addVertexWithUV(d42, (double)(y + 1), d46, faceU3, faceV1);
			tessellator6.addVertexWithUV(d42, (double)(y + 1), d46, faceU1, faceV1);
			tessellator6.addVertexWithUV(d42, (double)(y + 0), d46, faceU1, faceV2);
			tessellator6.addVertexWithUV(d38, (double)(y + 0), d46, faceU3, faceV2);
			tessellator6.addVertexWithUV(d38, (double)(y + 1), d46, faceU3, faceV1);
			if(z62) {
				tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d56, sideU2, sideV2);
				tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d56, sideU2, sideV3);
				tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d54, sideU1, sideV3);
				tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d54, sideU1, sideV2);
				tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d56, sideU2, sideV2);
				tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d56, sideU2, sideV3);
				tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d54, sideU1, sideV3);
				tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d54, sideU1, sideV2);
			} else {
				if(y < i5 - 1 && this.blockAccess.isAirBlock(x - 1, y + 1, z)) {
					tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d38, (double)(y + 1) + 0.01D, d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d54, sideU1, sideV1);
				}

				if(y < i5 - 1 && this.blockAccess.isAirBlock(x + 1, y + 1, z)) {
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d54, sideU1, sideV3);
					tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d40, (double)(y + 1) + 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d42, (double)(y + 1) + 0.01D, d54, sideU1, sideV3);
				}
			}

			if(z63) {
				tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d56, sideU2, sideV2);
				tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d56, sideU2, sideV3);
				tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d54, sideU1, sideV3);
				tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d54, sideU1, sideV2);
				tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d56, sideU2, sideV2);
				tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d56, sideU2, sideV3);
				tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d54, sideU1, sideV3);
				tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d54, sideU1, sideV2);
			} else {
				if(y > 1 && this.blockAccess.isAirBlock(x - 1, y - 1, z)) {
					tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d56, sideU2, sideV2);
					tessellator6.addVertexWithUV(d38, (double)y - 0.01D, d54, sideU1, sideV2);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d54, sideU1, sideV1);
				}

				if(y > 1 && this.blockAccess.isAirBlock(x + 1, y - 1, z)) {
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d54, sideU1, sideV3);
					tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d56, sideU2, sideV3);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d56, sideU2, sideV1);
					tessellator6.addVertexWithUV(d40, (double)y - 0.01D, d54, sideU1, sideV1);
					tessellator6.addVertexWithUV(d42, (double)y - 0.01D, d54, sideU1, sideV3);
				}
			}
		}

		if((!z58 || !z59) && (z60 || z61 || z58 || z59)) {
			if(z58 && !z59) {
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d44, faceU1, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d44, faceU1, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d46, faceU2, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d46, faceU2, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d46, faceU1, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d46, faceU1, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d44, faceU2, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d44, faceU2, faceV1);
				if(!z61 && !z60) {
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU1, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 0), d46, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 0), d46, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU2, sideV3);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU1, sideV3);
					tessellator6.addVertexWithUV(d52, (double)(y + 0), d46, sideU1, sideV2);
					tessellator6.addVertexWithUV(d50, (double)(y + 0), d46, sideU2, sideV2);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU2, sideV3);
				}

				if(z62 || y < i5 - 1 && this.blockAccess.isAirBlock(x, y + 1, z - 1)) {
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d44, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU2, sideV1);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU1, sideV1);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d44, sideU1, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d44, sideU2, sideV1);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d44, sideU1, sideV1);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU1, sideV3);
				}

				if(z63 || y > 1 && this.blockAccess.isAirBlock(x, y - 1, z - 1)) {
					tessellator6.addVertexWithUV(d50, (double)y, d44, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)y, d46, sideU2, sideV1);
					tessellator6.addVertexWithUV(d52, (double)y, d46, sideU1, sideV1);
					tessellator6.addVertexWithUV(d52, (double)y, d44, sideU1, sideV3);
					tessellator6.addVertexWithUV(d50, (double)y, d46, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)y, d44, sideU2, sideV1);
					tessellator6.addVertexWithUV(d52, (double)y, d44, sideU1, sideV1);
					tessellator6.addVertexWithUV(d52, (double)y, d46, sideU1, sideV3);
				}
			} else if(!z58 && z59) {
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d46, faceU2, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d46, faceU2, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d48, faceU3, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d48, faceU3, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d48, faceU2, faceV1);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d48, faceU2, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 0), d46, faceU3, faceV2);
				tessellator6.addVertexWithUV(d40, (double)(y + 1), d46, faceU3, faceV1);
				if(!z61 && !z60) {
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU1, sideV3);
					tessellator6.addVertexWithUV(d52, (double)(y + 0), d46, sideU1, sideV2);
					tessellator6.addVertexWithUV(d50, (double)(y + 0), d46, sideU2, sideV2);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU1, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 0), d46, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 0), d46, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU2, sideV3);
				}

				if(z62 || y < i5 - 1 && this.blockAccess.isAirBlock(x, y + 1, z + 1)) {
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU1, sideV1);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d48, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d48, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU2, sideV1);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d48, sideU1, sideV1);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d48, sideU2, sideV1);
				}

				if(z63 || y > 1 && this.blockAccess.isAirBlock(x, y - 1, z + 1)) {
					tessellator6.addVertexWithUV(d50, (double)y, d46, sideU1, sideV1);
					tessellator6.addVertexWithUV(d50, (double)y, d48, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)y, d48, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)y, d46, sideU2, sideV1);
					tessellator6.addVertexWithUV(d50, (double)y, d48, sideU1, sideV1);
					tessellator6.addVertexWithUV(d50, (double)y, d46, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)y, d46, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)y, d48, sideU2, sideV1);
				}
			}
		} else {
			tessellator6.addVertexWithUV(d40, (double)(y + 1), d48, faceU1, faceV1);
			tessellator6.addVertexWithUV(d40, (double)(y + 0), d48, faceU1, faceV2);
			tessellator6.addVertexWithUV(d40, (double)(y + 0), d44, faceU3, faceV2);
			tessellator6.addVertexWithUV(d40, (double)(y + 1), d44, faceU3, faceV1);
			tessellator6.addVertexWithUV(d40, (double)(y + 1), d44, faceU1, faceV1);
			tessellator6.addVertexWithUV(d40, (double)(y + 0), d44, faceU1, faceV2);
			tessellator6.addVertexWithUV(d40, (double)(y + 0), d48, faceU3, faceV2);
			tessellator6.addVertexWithUV(d40, (double)(y + 1), d48, faceU3, faceV1);
			if(z62) {
				tessellator6.addVertexWithUV(d52, (double)(y + 1), d48, sideU2, sideV2);
				tessellator6.addVertexWithUV(d52, (double)(y + 1), d44, sideU2, sideV3);
				tessellator6.addVertexWithUV(d50, (double)(y + 1), d44, sideU1, sideV3);
				tessellator6.addVertexWithUV(d50, (double)(y + 1), d48, sideU1, sideV2);
				tessellator6.addVertexWithUV(d52, (double)(y + 1), d44, sideU2, sideV2);
				tessellator6.addVertexWithUV(d52, (double)(y + 1), d48, sideU2, sideV3);
				tessellator6.addVertexWithUV(d50, (double)(y + 1), d48, sideU1, sideV3);
				tessellator6.addVertexWithUV(d50, (double)(y + 1), d44, sideU1, sideV2);
			} else {
				if(y < i5 - 1 && this.blockAccess.isAirBlock(x, y + 1, z - 1)) {
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d44, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU2, sideV1);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU1, sideV1);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d44, sideU1, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d44, sideU2, sideV1);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d44, sideU1, sideV1);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU1, sideV3);
				}

				if(y < i5 - 1 && this.blockAccess.isAirBlock(x, y + 1, z + 1)) {
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU1, sideV1);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d48, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d48, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU2, sideV1);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d48, sideU1, sideV1);
					tessellator6.addVertexWithUV(d50, (double)(y + 1), d46, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d46, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)(y + 1), d48, sideU2, sideV1);
				}
			}

			if(z63) {
				tessellator6.addVertexWithUV(d52, (double)y, d48, sideU2, sideV2);
				tessellator6.addVertexWithUV(d52, (double)y, d44, sideU2, sideV3);
				tessellator6.addVertexWithUV(d50, (double)y, d44, sideU1, sideV3);
				tessellator6.addVertexWithUV(d50, (double)y, d48, sideU1, sideV2);
				tessellator6.addVertexWithUV(d52, (double)y, d44, sideU2, sideV2);
				tessellator6.addVertexWithUV(d52, (double)y, d48, sideU2, sideV3);
				tessellator6.addVertexWithUV(d50, (double)y, d48, sideU1, sideV3);
				tessellator6.addVertexWithUV(d50, (double)y, d44, sideU1, sideV2);
			} else {
				if(y > 1 && this.blockAccess.isAirBlock(x, y - 1, z - 1)) {
					tessellator6.addVertexWithUV(d50, (double)y, d44, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)y, d46, sideU2, sideV1);
					tessellator6.addVertexWithUV(d52, (double)y, d46, sideU1, sideV1);
					tessellator6.addVertexWithUV(d52, (double)y, d44, sideU1, sideV3);
					tessellator6.addVertexWithUV(d50, (double)y, d46, sideU2, sideV3);
					tessellator6.addVertexWithUV(d50, (double)y, d44, sideU2, sideV1);
					tessellator6.addVertexWithUV(d52, (double)y, d44, sideU1, sideV1);
					tessellator6.addVertexWithUV(d52, (double)y, d46, sideU1, sideV3);
				}

				if(y > 1 && this.blockAccess.isAirBlock(x, y - 1, z + 1)) {
					tessellator6.addVertexWithUV(d50, (double)y, d46, sideU1, sideV1);
					tessellator6.addVertexWithUV(d50, (double)y, d48, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)y, d48, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)y, d46, sideU2, sideV1);
					tessellator6.addVertexWithUV(d50, (double)y, d48, sideU1, sideV1);
					tessellator6.addVertexWithUV(d50, (double)y, d46, sideU1, sideV2);
					tessellator6.addVertexWithUV(d52, (double)y, d46, sideU2, sideV2);
					tessellator6.addVertexWithUV(d52, (double)y, d48, sideU2, sideV1);
				}
			}
		}

		return true;
	}

	public boolean renderCrossedSquares(Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		tessellator5.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
		int i7 = block1.colorMultiplier(this.blockAccess, i2, i3, i4);
		float f8 = (float)(i7 >> 16 & 255) / 255.0F;
		float f9 = (float)(i7 >> 8 & 255) / 255.0F;
		float f10 = (float)(i7 & 255) / 255.0F;
		if(GameRenderer.anaglyphEnable) {
			float f11 = (f8 * 30.0F + f9 * 59.0F + f10 * 11.0F) / 100.0F;
			float f12 = (f8 * 30.0F + f9 * 70.0F) / 100.0F;
			float f13 = (f8 * 30.0F + f10 * 70.0F) / 100.0F;
			f8 = f11;
			f9 = f12;
			f10 = f13;
		}

		tessellator5.setColorOpaque_F(f8, f9, f10);
		double d19 = (double)i2;
		double d20 = (double)i3;
		double d15 = (double)i4;
		if(block1 == Block.tallGrass) {
			long j17 = (long)(i2 * 3129871) ^ (long)i4 * 116129781L ^ (long)i3;
			j17 = j17 * j17 * 42317861L + j17 * 11L;
			d19 += ((double)((float)(j17 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
			d20 += ((double)((float)(j17 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
			d15 += ((double)((float)(j17 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
		}

		this.renderCrossedSquares(block1, this.blockAccess.getBlockMetadata(i2, i3, i4), d19, d20, d15);
		return true;
	}

	public boolean renderBlockCrops(Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		tessellator5.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
		tessellator5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		this.renderBlockCropsImpl(block1, this.blockAccess.getBlockMetadata(i2, i3, i4), (double)i2, (double)((float)i3 - 0.0625F), (double)i4);
		return true;
	}

	public void renderTorchAtAngle(Block block1, double d2, double d4, double d6, double d8, double d10) {
		Tessellator tessellator12 = Tessellator.instance;
		int i13 = block1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i13 = this.overrideBlockTexture;
		}

		int i14 = (i13 & 15) << 4;
		int i15 = i13 & 240;
		float f16 = (float)i14 / 256.0F;
		float f17 = ((float)i14 + 15.99F) / 256.0F;
		float f18 = (float)i15 / 256.0F;
		float f19 = ((float)i15 + 15.99F) / 256.0F;
		double d20 = (double)f16 + 7.0D / 256D;
		double d22 = (double)f18 + 6.0D / 256D;
		double d24 = (double)f16 + 9.0D / 256D;
		double d26 = (double)f18 + 8.0D / 256D;
		d2 += 0.5D;
		d6 += 0.5D;
		double d28 = d2 - 0.5D;
		double d30 = d2 + 0.5D;
		double d32 = d6 - 0.5D;
		double d34 = d6 + 0.5D;
		double d36 = 0.0625D;
		double d38 = 0.625D;
		tessellator12.addVertexWithUV(d2 + d8 * (1.0D - d38) - d36, d4 + d38, d6 + d10 * (1.0D - d38) - d36, d20, d22);
		tessellator12.addVertexWithUV(d2 + d8 * (1.0D - d38) - d36, d4 + d38, d6 + d10 * (1.0D - d38) + d36, d20, d26);
		tessellator12.addVertexWithUV(d2 + d8 * (1.0D - d38) + d36, d4 + d38, d6 + d10 * (1.0D - d38) + d36, d24, d26);
		tessellator12.addVertexWithUV(d2 + d8 * (1.0D - d38) + d36, d4 + d38, d6 + d10 * (1.0D - d38) - d36, d24, d22);
		tessellator12.addVertexWithUV(d2 - d36, d4 + 1.0D, d32, (double)f16, (double)f18);
		tessellator12.addVertexWithUV(d2 - d36 + d8, d4 + 0.0D, d32 + d10, (double)f16, (double)f19);
		tessellator12.addVertexWithUV(d2 - d36 + d8, d4 + 0.0D, d34 + d10, (double)f17, (double)f19);
		tessellator12.addVertexWithUV(d2 - d36, d4 + 1.0D, d34, (double)f17, (double)f18);
		tessellator12.addVertexWithUV(d2 + d36, d4 + 1.0D, d34, (double)f16, (double)f18);
		tessellator12.addVertexWithUV(d2 + d8 + d36, d4 + 0.0D, d34 + d10, (double)f16, (double)f19);
		tessellator12.addVertexWithUV(d2 + d8 + d36, d4 + 0.0D, d32 + d10, (double)f17, (double)f19);
		tessellator12.addVertexWithUV(d2 + d36, d4 + 1.0D, d32, (double)f17, (double)f18);
		tessellator12.addVertexWithUV(d28, d4 + 1.0D, d6 + d36, (double)f16, (double)f18);
		tessellator12.addVertexWithUV(d28 + d8, d4 + 0.0D, d6 + d36 + d10, (double)f16, (double)f19);
		tessellator12.addVertexWithUV(d30 + d8, d4 + 0.0D, d6 + d36 + d10, (double)f17, (double)f19);
		tessellator12.addVertexWithUV(d30, d4 + 1.0D, d6 + d36, (double)f17, (double)f18);
		tessellator12.addVertexWithUV(d30, d4 + 1.0D, d6 - d36, (double)f16, (double)f18);
		tessellator12.addVertexWithUV(d30 + d8, d4 + 0.0D, d6 - d36 + d10, (double)f16, (double)f19);
		tessellator12.addVertexWithUV(d28 + d8, d4 + 0.0D, d6 - d36 + d10, (double)f17, (double)f19);
		tessellator12.addVertexWithUV(d28, d4 + 1.0D, d6 - d36, (double)f17, (double)f18);
	}

	public void renderCrossedSquares(Block block1, int i2, double d3, double d5, double d7) {
		Tessellator tessellator9 = Tessellator.instance;
		int i10 = block1.getBlockTextureFromSideAndMetadata(0, i2);
		if(this.overrideBlockTexture >= 0) {
			i10 = this.overrideBlockTexture;
		}

		int i11 = (i10 & 15) << 4;
		int i12 = i10 & 240;
		double d13 = (double)((float)i11 / 256.0F);
		double d15 = (double)(((float)i11 + 15.99F) / 256.0F);
		double d17 = (double)((float)i12 / 256.0F);
		double d19 = (double)(((float)i12 + 15.99F) / 256.0F);
		double d21 = d3 + 0.5D - (double)0.45F;
		double d23 = d3 + 0.5D + (double)0.45F;
		double d25 = d7 + 0.5D - (double)0.45F;
		double d27 = d7 + 0.5D + (double)0.45F;
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d15, d17);
	}
	
	public void renderCrossedSquaresDoubleHeight(Block block1, int i2, double d3, double d5, double d7) {
		Tessellator tessellator9 = Tessellator.instance;
		int i10 = block1.getBlockTextureFromSideAndMetadata(0, i2);
		if(this.overrideBlockTexture >= 0) {
			i10 = this.overrideBlockTexture;
		}

		int i11 = (i10 & 15) << 4;
		int i12 = i10 & 240;
		double d13 = (double)((float)i11 / 256.0F);
		double d15 = (double)(((float)i11 + 15.99F) / 256.0F);
		double d17 = (double)((float)i12 / 256.0F);
		double d19 = (double)(((float)i12 + 15.99F) / 256.0F);
		double d21 = d3 + 0.5D - (double)0.45F;
		double d23 = d3 + 0.5D + (double)0.45F;
		double d25 = d7 + 0.5D - (double)0.45F;
		double d27 = d7 + 0.5D + (double)0.45F;
		tessellator9.addVertexWithUV(d21, d5 + 2.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 2.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 2.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 2.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d21, d5 + 2.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 2.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 2.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 2.0D, d27, d15, d17);
	}

	public boolean renderBlockLilyPad(Block block, int x, int y, int z) {
		Tessellator tessellator = Tessellator.instance;
		
		int i = block.blockIndexInTexture;

		int j = (i & 0xf) << 4;
		int k = i & 0xff0;
		
		float f = 0.015625F;
		
		float d = (float)j / 256F;
		float d1 = ((float)j + 15.99F) / 256F;
		float d2 = (float)k / 256F;
		float d3 = ((float)k + 15.99F) / 256F;
		
		long l = (long)(x * 0x2fc20f) ^ (long)z * 0x6ebfff5L ^ (long)y;
		l = l * l * 0x285b825L + l * 11L;
		int i1 = (int)(l >> 16 & 7L);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, x, y, z));

		
		float f1 = (float)x + 0.5F;
		float f2 = (float)z + 0.5F;
		float f3 = (float)(i1 & 1) * 0.5F * (float)(1 - (i1 & 2));
		float f4 = (float)(i1 + 1 & 1) * 0.5F * (float)(1 - ((i1 + 1) & 2));
		
		tessellator.addVertexWithUV((f1 + f3) - f4, (float)y + f, f2 + f3 + f4, d, d2);
		tessellator.addVertexWithUV(f1 + f3 + f4, (float)y + f, (f2 - f3) + f4, d1, d2);
		tessellator.addVertexWithUV((f1 - f3) + f4, (float)y + f, f2 - f3 - f4, d1, d3);
		tessellator.addVertexWithUV(f1 - f3 - f4, (float)y + f, (f2 + f3) - f4, d, d3);
		tessellator.addVertexWithUV(f1 - f3 - f4, (float)y + f, (f2 + f3) - f4, d, d3);
		tessellator.addVertexWithUV((f1 - f3) + f4, (float)y + f, f2 - f3 - f4, d1, d3);
		tessellator.addVertexWithUV(f1 + f3 + f4, (float)y + f, (f2 - f3) + f4, d1, d2);
		tessellator.addVertexWithUV((f1 + f3) - f4, (float)y + f, f2 + f3 + f4, d, d2);
		
		// Render a lily flower on top
		if((i1 & 4) != 0) {
			this.overrideBlockTexture = 12 * 16 + 2;
			this.renderCrossedSquares(block, x, y, z);
			this.overrideBlockTexture = -1;
		}
		
		return true;
	}

	public void renderBlockCropsImpl(Block block1, int i2, double d3, double d5, double d7) {
		Tessellator tessellator9 = Tessellator.instance;
		int i10 = block1.getBlockTextureFromSideAndMetadata(0, i2);
		if(this.overrideBlockTexture >= 0) {
			i10 = this.overrideBlockTexture;
		}

		int i11 = (i10 & 15) << 4;
		int i12 = i10 & 240;
		double d13 = (double)((float)i11 / 256.0F);
		double d15 = (double)(((float)i11 + 15.99F) / 256.0F);
		double d17 = (double)((float)i12 / 256.0F);
		double d19 = (double)(((float)i12 + 15.99F) / 256.0F);
		double d21 = d3 + 0.5D - 0.25D;
		double d23 = d3 + 0.5D + 0.25D;
		double d25 = d7 + 0.5D - 0.5D;
		double d27 = d7 + 0.5D + 0.5D;
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d15, d17);
		d21 = d3 + 0.5D - 0.5D;
		d23 = d3 + 0.5D + 0.5D;
		d25 = d7 + 0.5D - 0.25D;
		d27 = d7 + 0.5D + 0.25D;
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, d5 + 0.0D, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, d5 + 0.0D, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d15, d17);
	}

	public boolean renderBlockFluids(Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = block1.colorMultiplier(this.blockAccess, i2, i3, i4);
		float f7 = (float)(i6 >> 16 & 255) / 255.0F;
		float f8 = (float)(i6 >> 8 & 255) / 255.0F;
		float f9 = (float)(i6 & 255) / 255.0F;
		boolean z10 = block1.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1);
		boolean z11 = block1.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0);
		boolean[] z12 = new boolean[]{block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2), block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3), block1.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4), block1.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)};
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
			int i23 = this.blockAccess.getBlockMetadata(i2, i3, i4);
			float f24 = this.getFluidHeight(i2, i3, i4, material22);
			float f25 = this.getFluidHeight(i2, i3, i4 + 1, material22);
			float f26 = this.getFluidHeight(i2 + 1, i3, i4 + 1, material22);
			float f27 = this.getFluidHeight(i2 + 1, i3, i4, material22);
			int i28;
			int i31;
			float f36;
			float f37;
			float f38;
			if(this.renderAllFaces || z10) {
				z13 = true;
				i28 = block1.getBlockTextureFromSideAndMetadata(1, i23);
				float f29 = (float)BlockFluid.func_293_a(this.blockAccess, i2, i3, i4, material22);
				if(f29 > -999.0F) {
					i28 = block1.getBlockTextureFromSideAndMetadata(2, i23);
				}

				int i30 = (i28 & 15) << 4;
				i31 = i28 & 240;
				double d32 = ((double)i30 + 8.0D) / 256.0D;
				double d34 = ((double)i31 + 8.0D) / 256.0D;
				if(f29 < -999.0F) {
					f29 = 0.0F;
				} else {
					d32 = (double)((float)(i30 + 16) / 256.0F);
					d34 = (double)((float)(i31 + 16) / 256.0F);
				}

				f36 = MathHelper.sin(f29) * 8.0F / 256.0F;
				f37 = MathHelper.cos(f29) * 8.0F / 256.0F;
				tessellator5.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4));
				tessellator5.setColorOpaque_F(f15 * f7, f15 * f8, f15 * f9);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f24), (double)(i4 + 0), d32 - (double)f37 - (double)f36, d34 - (double)f37 + (double)f36);
				tessellator5.addVertexWithUV((double)(i2 + 0), (double)((float)i3 + f25), (double)(i4 + 1), d32 - (double)f37 + (double)f36, d34 + (double)f37 + (double)f36);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f26), (double)(i4 + 1), d32 + (double)f37 + (double)f36, d34 + (double)f37 - (double)f36);
				tessellator5.addVertexWithUV((double)(i2 + 1), (double)((float)i3 + f27), (double)(i4 + 0), d32 + (double)f37 - (double)f36, d34 - (double)f37 - (double)f36);
			}

			if(this.renderAllFaces || z11) {
				tessellator5.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4));
				tessellator5.setColorOpaque_F(f14, f14, f14);
				this.renderBottomFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTextureFromSide(0));
				z13 = true;
			}

			for(i28 = 0; i28 < 4; ++i28) {
				int i53 = i2;
				i31 = i4;
				if(i28 == 0) {
					i31 = i4 - 1;
				}

				if(i28 == 1) {
					++i31;
				}

				if(i28 == 2) {
					i53 = i2 - 1;
				}

				if(i28 == 3) {
					++i53;
				}

				int i54 = block1.getBlockTextureFromSideAndMetadata(i28 + 2, i23);
				int i33 = (i54 & 15) << 4;
				int i55 = i54 & 240;
				if(this.renderAllFaces || z12[i28]) {
					float f35;
					float f39;
					float f40;
					if(i28 == 0) {
						f35 = f24;
						f36 = f27;
						f37 = (float)i2;
						f39 = (float)(i2 + 1);
						f38 = (float)i4;
						f40 = (float)i4;
					} else if(i28 == 1) {
						f35 = f26;
						f36 = f25;
						f37 = (float)(i2 + 1);
						f39 = (float)i2;
						f38 = (float)(i4 + 1);
						f40 = (float)(i4 + 1);
					} else if(i28 == 2) {
						f35 = f25;
						f36 = f24;
						f37 = (float)i2;
						f39 = (float)i2;
						f38 = (float)(i4 + 1);
						f40 = (float)i4;
					} else {
						f35 = f27;
						f36 = f26;
						f37 = (float)(i2 + 1);
						f39 = (float)(i2 + 1);
						f38 = (float)i4;
						f40 = (float)(i4 + 1);
					}

					z13 = true;
					double d41 = (double)((float)(i33 + 0) / 256.0F);
					double d43 = ((double)(i33 + 16) - 0.01D) / 256.0D;
					double d45 = (double)(((float)i55 + (1.0F - f35) * 16.0F) / 256.0F);
					double d47 = (double)(((float)i55 + (1.0F - f36) * 16.0F) / 256.0F);
					double d49 = ((double)(i55 + 16) - 0.01D) / 256.0D;
					tessellator5.setBrightness(block1.getMixedBrightnessForBlock(this.blockAccess, i53, i3, i31));
					float f51;
					if(i28 < 2) {
						f51 = f16;
					} else {
						f51 = f17;
					}

					tessellator5.setColorOpaque_F(f15 * f51 * f7, f15 * f51 * f8, f15 * f51 * f9);
					tessellator5.addVertexWithUV((double)f37, (double)((float)i3 + f35), (double)f38, d41, d45);
					tessellator5.addVertexWithUV((double)f39, (double)((float)i3 + f36), (double)f40, d43, d47);
					tessellator5.addVertexWithUV((double)f39, (double)(i3 + 0), (double)f40, d43, d49);
					tessellator5.addVertexWithUV((double)f37, (double)(i3 + 0), (double)f38, d41, d49);
				}
			}

			block1.minY = d18;
			block1.maxY = d20;
			return z13;
		}
	}

	private float getFluidHeight(int i1, int i2, int i3, Material material4) {
		int i5 = 0;
		float f6 = 0.0F;

		for(int i7 = 0; i7 < 4; ++i7) {
			int i8 = i1 - (i7 & 1);
			int i10 = i3 - (i7 >> 1 & 1);
			if(this.blockAccess.getBlockMaterial(i8, i2 + 1, i10) == material4) {
				return 1.0F;
			}

			Material material11 = this.blockAccess.getBlockMaterial(i8, i2, i10);
			if(material11 != material4) {
				if(!material11.isSolid()) {
					++f6;
					++i5;
				}
			} else {
				int i12 = this.blockAccess.getBlockMetadata(i8, i2, i10);
				if(i12 >= 8 || i12 == 0) {
					f6 += BlockFluid.getPercentAir(i12) * 10.0F;
					i5 += 10;
				}

				f6 += BlockFluid.getPercentAir(i12);
				++i5;
			}
		}

		return 1.0F - f6 / (float)i5;
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

	public boolean renderStandardBlock(Block block1, int i2, int i3, int i4) {
		int i5 = block1.colorMultiplier(this.blockAccess, i2, i3, i4);
		float f6 = (float)(i5 >> 16 & 255) / 255.0F;
		float f7 = (float)(i5 >> 8 & 255) / 255.0F;
		float f8 = (float)(i5 & 255) / 255.0F;
		if(GameRenderer.anaglyphEnable) {
			float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
			float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
			float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
			f6 = f9;
			f7 = f10;
			f8 = f11;
		}

		return Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[block1.blockID] == 0 ? 
				this.renderStandardBlockWithAmbientOcclusion(block1, i2, i3, i4, f6, f7, f8) 
			: 
				this.renderStandardBlockWithColorMultiplier(block1, i2, i3, i4, f6, f7, f8);
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block block1, int i2, int i3, int i4, float f5, float f6, float f7) {
		this.enableAO = true;
		boolean z8 = false;
		float f9 = this.lightValueOwn;
		float f10 = this.lightValueOwn;
		float f11 = this.lightValueOwn;
		float f12 = this.lightValueOwn;
		boolean z13 = true;
		boolean z14 = true;
		boolean z15 = true;
		boolean z16 = true;
		boolean z17 = true;
		boolean z18 = true;
		this.lightValueOwn = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4);
		this.aoLightValueXNeg = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4);
		this.aoLightValueYNeg = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4);
		this.aoLightValueZNeg = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 - 1);
		this.aoLightValueXPos = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4);
		this.aoLightValueYPos = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4);
		this.aoLightValueZPos = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 + 1);
		int i19 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4);
		int i20 = i19;
		int i21 = i19;
		int i22 = i19;
		int i23 = i19;
		int i24 = i19;
		int i25 = i19;
		if(block1.minY <= 0.0D) {
			i21 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4);
		}

		if(block1.maxY >= 1.0D) {
			i24 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4);
		}

		if(block1.minX <= 0.0D) {
			i20 = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4);
		}

		if(block1.maxX >= 1.0D) {
			i23 = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4);
		}

		if(block1.minZ <= 0.0D) {
			i22 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1);
		}

		if(block1.maxZ >= 1.0D) {
			i25 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1);
		}

		Tessellator tessellator26 = Tessellator.instance;
		tessellator26.setBrightness(983055);
		this.aoGrassXYZPPC = Block.canBlockGrass[this.blockAccess.getBlockId(i2 + 1, i3 + 1, i4)];
		this.aoGrassXYZPNC = Block.canBlockGrass[this.blockAccess.getBlockId(i2 + 1, i3 - 1, i4)];
		this.aoGrassXYZPCP = Block.canBlockGrass[this.blockAccess.getBlockId(i2 + 1, i3, i4 + 1)];
		this.aoGrassXYZPCN = Block.canBlockGrass[this.blockAccess.getBlockId(i2 + 1, i3, i4 - 1)];
		this.aoGrassXYZNPC = Block.canBlockGrass[this.blockAccess.getBlockId(i2 - 1, i3 + 1, i4)];
		this.aoGrassXYZNNC = Block.canBlockGrass[this.blockAccess.getBlockId(i2 - 1, i3 - 1, i4)];
		this.aoGrassXYZNCN = Block.canBlockGrass[this.blockAccess.getBlockId(i2 - 1, i3, i4 - 1)];
		this.aoGrassXYZNCP = Block.canBlockGrass[this.blockAccess.getBlockId(i2 - 1, i3, i4 + 1)];
		this.aoGrassXYZCPP = Block.canBlockGrass[this.blockAccess.getBlockId(i2, i3 + 1, i4 + 1)];
		this.aoGrassXYZCPN = Block.canBlockGrass[this.blockAccess.getBlockId(i2, i3 + 1, i4 - 1)];
		this.aoGrassXYZCNP = Block.canBlockGrass[this.blockAccess.getBlockId(i2, i3 - 1, i4 + 1)];
		this.aoGrassXYZCNN = Block.canBlockGrass[this.blockAccess.getBlockId(i2, i3 - 1, i4 - 1)];
		if(block1.blockIndexInTexture == 3) {
			z18 = false;
			z17 = false;
			z16 = false;
			z15 = false;
			z13 = false;
		}

		if(this.overrideBlockTexture >= 0) {
			z18 = false;
			z17 = false;
			z16 = false;
			z15 = false;
			z13 = false;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0)) {
			if(this.aoType > 0) {
				if(block1.minY <= 0.0D) {
				--i3;
				}

				this.aoBrightnessXYNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4);
				this.aoBrightnessYZNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1);
				this.aoBrightnessYZNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1);
				this.aoBrightnessXYPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4);
				this.aoLightValueScratchXYNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4);
				this.aoLightValueScratchYZNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 - 1);
				this.aoLightValueScratchYZNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 + 1);
				this.aoLightValueScratchXYPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4);
				if(!this.aoGrassXYZCNN && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
				} else {
					this.aoLightValueScratchXYZNNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4 - 1);
					this.aoBrightnessXYZNNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4 - 1);
				}

				if(!this.aoGrassXYZCNP && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
					this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
				} else {
					this.aoLightValueScratchXYZNNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4 + 1);
					this.aoBrightnessXYZNNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4 + 1);
				}

				if(!this.aoGrassXYZCNN && !this.aoGrassXYZPNC) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
				} else {
					this.aoLightValueScratchXYZPNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4 - 1);
					this.aoBrightnessXYZPNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4 - 1);
				}

				if(!this.aoGrassXYZCNP && !this.aoGrassXYZPNC) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
					this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
				} else {
					this.aoLightValueScratchXYZPNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4 + 1);
					this.aoBrightnessXYZPNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4 + 1);
				}

				if(block1.minY <= 0.0D) {
					++i3;
				}

				f9 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + this.aoLightValueYNeg) / 4.0F;
				f12 = (this.aoLightValueScratchYZNP + this.aoLightValueYNeg + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
				f11 = (this.aoLightValueYNeg + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
				f10 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + this.aoLightValueYNeg + this.aoLightValueScratchYZNN) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i21);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i21);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i21);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i21);
			} else {
				f12 = this.aoLightValueYNeg;
				f11 = this.aoLightValueYNeg;
				f10 = this.aoLightValueYNeg;
				f9 = this.aoLightValueYNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = this.aoBrightnessXYNN;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (z13 ? f5 : 1.0F) * 0.5F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (z13 ? f6 : 1.0F) * 0.5F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (z13 ? f7 : 1.0F) * 0.5F;
			this.colorRedTopLeft *= f9;
			this.colorGreenTopLeft *= f9;
			this.colorBlueTopLeft *= f9;
			this.colorRedBottomLeft *= f10;
			this.colorGreenBottomLeft *= f10;
			this.colorBlueBottomLeft *= f10;
			this.colorRedBottomRight *= f11;
			this.colorGreenBottomRight *= f11;
			this.colorBlueBottomRight *= f11;
			this.colorRedTopRight *= f12;
			this.colorGreenTopRight *= f12;
			this.colorBlueTopRight *= f12;
			this.renderBottomFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 0));
			z8 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1)) {
			if(this.aoType > 0) {
				if(block1.maxY >= 1.0D) {
				++i3;
				}

				this.aoBrightnessXYNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4);
				this.aoBrightnessXYPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4);
				this.aoBrightnessYZPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1);
				this.aoBrightnessYZPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1);
				this.aoLightValueScratchXYNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4);
				this.aoLightValueScratchXYPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4);
				this.aoLightValueScratchYZPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 - 1);
				this.aoLightValueScratchYZPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 + 1);
				if(!this.aoGrassXYZCPN && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
					this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
				} else {
					this.aoLightValueScratchXYZNPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4 - 1);
					this.aoBrightnessXYZNPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4 - 1);
				}

				if(!this.aoGrassXYZCPN && !this.aoGrassXYZPPC) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
					this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
				} else {
					this.aoLightValueScratchXYZPPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4 - 1);
					this.aoBrightnessXYZPPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4 - 1);
				}

				if(!this.aoGrassXYZCPP && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
				} else {
					this.aoLightValueScratchXYZNPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4 + 1);
					this.aoBrightnessXYZNPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4 + 1);
				}

				if(!this.aoGrassXYZCPP && !this.aoGrassXYZPPC) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
				} else {
					this.aoLightValueScratchXYZPPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4 + 1);
					this.aoBrightnessXYZPPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4 + 1);
				}

				if(block1.maxY >= 1.0D) {
					--i3;
				}

				f12 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + this.aoLightValueYPos) / 4.0F;
				f9 = (this.aoLightValueScratchYZPP + this.aoLightValueYPos + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
				f10 = (this.aoLightValueYPos + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
				f11 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + this.aoLightValueYPos + this.aoLightValueScratchYZPN) / 4.0F;
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, i24);
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, i24);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, i24);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i24);
			} else {
				f12 = this.aoLightValueYPos;
				f11 = this.aoLightValueYPos;
				f10 = this.aoLightValueYPos;
				f9 = this.aoLightValueYPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i24;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = z14 ? f5 : 1.0F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = z14 ? f6 : 1.0F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = z14 ? f7 : 1.0F;
			this.colorRedTopLeft *= f9;
			this.colorGreenTopLeft *= f9;
			this.colorBlueTopLeft *= f9;
			this.colorRedBottomLeft *= f10;
			this.colorGreenBottomLeft *= f10;
			this.colorBlueBottomLeft *= f10;
			this.colorRedBottomRight *= f11;
			this.colorGreenBottomRight *= f11;
			this.colorBlueBottomRight *= f11;
			this.colorRedTopRight *= f12;
			this.colorGreenTopRight *= f12;
			this.colorBlueTopRight *= f12;
			this.renderTopFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 1));
			z8 = true;
		}

		int i27;
		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2)) {
			if(this.aoType > 0) {
				if(block1.minZ <= 0.0D) {
				--i4;
				}

				this.aoLightValueScratchXZNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4);
				this.aoLightValueScratchYZNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4);
				this.aoLightValueScratchYZPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4);
				this.aoLightValueScratchXZPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4);
				this.aoBrightnessXZNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4);
				this.aoBrightnessYZNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4);
				this.aoBrightnessYZPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4);
				this.aoBrightnessXZPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4);
				if(!this.aoGrassXYZNCN && !this.aoGrassXYZCNN) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3 - 1, i4);
					this.aoBrightnessXYZNNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3 - 1, i4);
				}

				if(!this.aoGrassXYZNCN && !this.aoGrassXYZCPN) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3 + 1, i4);
					this.aoBrightnessXYZNPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3 + 1, i4);
				}

				if(!this.aoGrassXYZPCN && !this.aoGrassXYZCNN) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3 - 1, i4);
					this.aoBrightnessXYZPNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3 - 1, i4);
				}

				if(!this.aoGrassXYZPCN && !this.aoGrassXYZCPN) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3 + 1, i4);
					this.aoBrightnessXYZPPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3 + 1, i4);
				}

				if(block1.minZ <= 0.0D) {
					++i4;
				}

				f9 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + this.aoLightValueZNeg + this.aoLightValueScratchYZPN) / 4.0F;
				f10 = (this.aoLightValueZNeg + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
				f11 = (this.aoLightValueScratchYZNN + this.aoLightValueZNeg + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
				f12 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + this.aoLightValueZNeg) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i22);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, i22);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, i22);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, i22);
			} else {
				f12 = this.aoLightValueZNeg;
				f11 = this.aoLightValueZNeg;
				f10 = this.aoLightValueZNeg;
				f9 = this.aoLightValueZNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i22;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (z15 ? f5 : 1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (z15 ? f6 : 1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (z15 ? f7 : 1.0F) * 0.8F;
			this.colorRedTopLeft *= f9;
			this.colorGreenTopLeft *= f9;
			this.colorBlueTopLeft *= f9;
			this.colorRedBottomLeft *= f10;
			this.colorGreenBottomLeft *= f10;
			this.colorBlueBottomLeft *= f10;
			this.colorRedBottomRight *= f11;
			this.colorGreenBottomRight *= f11;
			this.colorBlueBottomRight *= f11;
			this.colorRedTopRight *= f12;
			this.colorGreenTopRight *= f12;
			this.colorBlueTopRight *= f12;
			i27 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 2);
			this.renderEastFace(block1, (double)i2, (double)i3, (double)i4, i27);
			if(fancyGrass && i27 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f5;
				this.colorRedBottomLeft *= f5;
				this.colorRedBottomRight *= f5;
				this.colorRedTopRight *= f5;
				this.colorGreenTopLeft *= f6;
				this.colorGreenBottomLeft *= f6;
				this.colorGreenBottomRight *= f6;
				this.colorGreenTopRight *= f6;
				this.colorBlueTopLeft *= f7;
				this.colorBlueBottomLeft *= f7;
				this.colorBlueBottomRight *= f7;
				this.colorBlueTopRight *= f7;
				this.renderEastFace(block1, (double)i2, (double)i3, (double)i4, 38);
			}

			z8 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3)) {
			if(this.aoType > 0) {
				if(block1.maxZ >= 1.0D) {
				++i4;
				}

				this.aoLightValueScratchXZNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3, i4);
				this.aoLightValueScratchXZPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3, i4);
				this.aoLightValueScratchYZNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4);
				this.aoLightValueScratchYZPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4);
				this.aoBrightnessXZNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4);
				this.aoBrightnessXZPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4);
				this.aoBrightnessYZNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4);
				this.aoBrightnessYZPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4);
				if(!this.aoGrassXYZNCP && !this.aoGrassXYZCNP) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3 - 1, i4);
					this.aoBrightnessXYZNNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3 - 1, i4);
				}

				if(!this.aoGrassXYZNCP && !this.aoGrassXYZCPP) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 - 1, i3 + 1, i4);
					this.aoBrightnessXYZNPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3 + 1, i4);
				}

				if(!this.aoGrassXYZPCP && !this.aoGrassXYZCNP) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3 - 1, i4);
					this.aoBrightnessXYZPNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3 - 1, i4);
				}

				if(!this.aoGrassXYZPCP && !this.aoGrassXYZCPP) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2 + 1, i3 + 1, i4);
					this.aoBrightnessXYZPPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3 + 1, i4);
				}

				if(block1.maxZ >= 1.0D) {
					--i4;
				}

				f9 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + this.aoLightValueZPos + this.aoLightValueScratchYZPP) / 4.0F;
				f12 = (this.aoLightValueZPos + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
				f11 = (this.aoLightValueScratchYZNP + this.aoLightValueZPos + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
				f10 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + this.aoLightValueZPos) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, i25);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, i25);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i25);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, i25);
			} else {
				f12 = this.aoLightValueZPos;
				f11 = this.aoLightValueZPos;
				f10 = this.aoLightValueZPos;
				f9 = this.aoLightValueZPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i25;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (z16 ? f5 : 1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (z16 ? f6 : 1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (z16 ? f7 : 1.0F) * 0.8F;
			this.colorRedTopLeft *= f9;
			this.colorGreenTopLeft *= f9;
			this.colorBlueTopLeft *= f9;
			this.colorRedBottomLeft *= f10;
			this.colorGreenBottomLeft *= f10;
			this.colorBlueBottomLeft *= f10;
			this.colorRedBottomRight *= f11;
			this.colorGreenBottomRight *= f11;
			this.colorBlueBottomRight *= f11;
			this.colorRedTopRight *= f12;
			this.colorGreenTopRight *= f12;
			this.colorBlueTopRight *= f12;
			i27 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 3);
			this.renderWestFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 3));
			if(fancyGrass && i27 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f5;
				this.colorRedBottomLeft *= f5;
				this.colorRedBottomRight *= f5;
				this.colorRedTopRight *= f5;
				this.colorGreenTopLeft *= f6;
				this.colorGreenBottomLeft *= f6;
				this.colorGreenBottomRight *= f6;
				this.colorGreenTopRight *= f6;
				this.colorBlueTopLeft *= f7;
				this.colorBlueBottomLeft *= f7;
				this.colorBlueBottomRight *= f7;
				this.colorBlueTopRight *= f7;
				this.renderWestFace(block1, (double)i2, (double)i3, (double)i4, 38);
			}

			z8 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4)) {
			if(this.aoType > 0) {
				if(block1.minX <= 0.0D) {
				--i2;
				}

				this.aoLightValueScratchXYNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4);
				this.aoLightValueScratchXZNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 - 1);
				this.aoLightValueScratchXZNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 + 1);
				this.aoLightValueScratchXYNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4);
				this.aoBrightnessXYNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4);
				this.aoBrightnessXZNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1);
				this.aoBrightnessXZNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1);
				this.aoBrightnessXYNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4);
				if(!this.aoGrassXYZNCN && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4 - 1);
					this.aoBrightnessXYZNNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4 - 1);
				}

				if(!this.aoGrassXYZNCP && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4 + 1);
					this.aoBrightnessXYZNNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4 + 1);
				}

				if(!this.aoGrassXYZNCN && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4 - 1);
					this.aoBrightnessXYZNPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4 - 1);
				}

				if(!this.aoGrassXYZNCP && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4 + 1);
					this.aoBrightnessXYZNPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4 + 1);
				}

				if(block1.minX <= 0.0D) {
					++i2;
				}

				f12 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + this.aoLightValueXNeg + this.aoLightValueScratchXZNP) / 4.0F;
				f9 = (this.aoLightValueXNeg + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
				f10 = (this.aoLightValueScratchXZNN + this.aoLightValueXNeg + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
				f11 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + this.aoLightValueXNeg) / 4.0F;
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i20);
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i20);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i20);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i20);
			} else {
				f12 = this.aoLightValueXNeg;
				f11 = this.aoLightValueXNeg;
				f10 = this.aoLightValueXNeg;
				f9 = this.aoLightValueXNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i20;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (z17 ? f5 : 1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (z17 ? f6 : 1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (z17 ? f7 : 1.0F) * 0.6F;
			this.colorRedTopLeft *= f9;
			this.colorGreenTopLeft *= f9;
			this.colorBlueTopLeft *= f9;
			this.colorRedBottomLeft *= f10;
			this.colorGreenBottomLeft *= f10;
			this.colorBlueBottomLeft *= f10;
			this.colorRedBottomRight *= f11;
			this.colorGreenBottomRight *= f11;
			this.colorBlueBottomRight *= f11;
			this.colorRedTopRight *= f12;
			this.colorGreenTopRight *= f12;
			this.colorBlueTopRight *= f12;
			i27 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 4);
			this.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, i27);
			if(fancyGrass && i27 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f5;
				this.colorRedBottomLeft *= f5;
				this.colorRedBottomRight *= f5;
				this.colorRedTopRight *= f5;
				this.colorGreenTopLeft *= f6;
				this.colorGreenBottomLeft *= f6;
				this.colorGreenBottomRight *= f6;
				this.colorGreenTopRight *= f6;
				this.colorBlueTopLeft *= f7;
				this.colorBlueBottomLeft *= f7;
				this.colorBlueBottomRight *= f7;
				this.colorBlueTopRight *= f7;
				this.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, 38);
			}

			z8 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)) {
			if(this.aoType > 0) {
				if(block1.maxX >= 1.0D) {
				++i2;
				}

				this.aoLightValueScratchXYPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4);
				this.aoLightValueScratchXZPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 - 1);
				this.aoLightValueScratchXZPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3, i4 + 1);
				this.aoLightValueScratchXYPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4);
				this.aoBrightnessXYPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4);
				this.aoBrightnessXZPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1);
				this.aoBrightnessXZPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1);
				this.aoBrightnessXYPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4);
				if(!this.aoGrassXYZPNC && !this.aoGrassXYZPCN) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPNN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4 - 1);
					this.aoBrightnessXYZPNN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4 - 1);
				}

				if(!this.aoGrassXYZPNC && !this.aoGrassXYZPCP) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPNP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 - 1, i4 + 1);
					this.aoBrightnessXYZPNP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4 + 1);
				}

				if(!this.aoGrassXYZPPC && !this.aoGrassXYZPCN) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPPN = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4 - 1);
					this.aoBrightnessXYZPPN = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4 - 1);
				}

				if(!this.aoGrassXYZPPC && !this.aoGrassXYZPCP) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPPP = block1.getAmbientOcclusionLightValue(this.blockAccess, i2, i3 + 1, i4 + 1);
					this.aoBrightnessXYZPPP = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4 + 1);
				}

				if(block1.maxX >= 1.0D) {
					--i2;
				}

				f9 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + this.aoLightValueXPos + this.aoLightValueScratchXZPP) / 4.0F;
				f12 = (this.aoLightValueXPos + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
				f11 = (this.aoLightValueScratchXZPN + this.aoLightValueXPos + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
				f10 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + this.aoLightValueXPos) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i23);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, i23);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, i23);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, i23);
			} else {
				f12 = this.aoLightValueXPos;
				f11 = this.aoLightValueXPos;
				f10 = this.aoLightValueXPos;
				f9 = this.aoLightValueXPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i23;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (z18 ? f5 : 1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (z18 ? f6 : 1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (z18 ? f7 : 1.0F) * 0.6F;
			this.colorRedTopLeft *= f9;
			this.colorGreenTopLeft *= f9;
			this.colorBlueTopLeft *= f9;
			this.colorRedBottomLeft *= f10;
			this.colorGreenBottomLeft *= f10;
			this.colorBlueBottomLeft *= f10;
			this.colorRedBottomRight *= f11;
			this.colorGreenBottomRight *= f11;
			this.colorBlueBottomRight *= f11;
			this.colorRedTopRight *= f12;
			this.colorGreenTopRight *= f12;
			this.colorBlueTopRight *= f12;
			i27 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 5);
			this.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, i27);
			if(fancyGrass && i27 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f5;
				this.colorRedBottomLeft *= f5;
				this.colorRedBottomRight *= f5;
				this.colorRedTopRight *= f5;
				this.colorGreenTopLeft *= f6;
				this.colorGreenBottomLeft *= f6;
				this.colorGreenBottomRight *= f6;
				this.colorGreenTopRight *= f6;
				this.colorBlueTopLeft *= f7;
				this.colorBlueBottomLeft *= f7;
				this.colorBlueBottomRight *= f7;
				this.colorBlueTopRight *= f7;
				this.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, 38);
			}

			z8 = true;
		}

		this.enableAO = false;
		return z8;
	}

	private int getAoBrightness(int i1, int i2, int i3, int i4) {
		if(i1 == 0) {
			i1 = i4;
		}

		if(i2 == 0) {
			i2 = i4;
		}

		if(i3 == 0) {
			i3 = i4;
		}

		return i1 + i2 + i3 + i4 >> 2 & 16711935;
	}

	public boolean renderStandardBlockWithColorMultiplier(Block block1, int i2, int i3, int i4, float f5, float f6, float f7) {
		this.enableAO = false;
		Tessellator tessellator8 = Tessellator.instance;
		boolean z9 = false;
		float f10 = 0.5F;
		float f11 = 1.0F;
		float f12 = 0.8F;
		float f13 = 0.6F;
		float f14 = f11 * f5;
		float f15 = f11 * f6;
		float f16 = f11 * f7;
		float f17 = f10;
		float f18 = f12;
		float f19 = f13;
		float f20 = f10;
		float f21 = f12;
		float f22 = f13;
		float f23 = f10;
		float f24 = f12;
		float f25 = f13;
		if(block1 != Block.grass) {
			f17 = f10 * f5;
			f18 = f12 * f5;
			f19 = f13 * f5;
			f20 = f10 * f6;
			f21 = f12 * f6;
			f22 = f13 * f6;
			f23 = f10 * f7;
			f24 = f12 * f7;
			f25 = f13 * f7;
		}

		int i26 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4);
		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0)) {
			tessellator8.setBrightness(block1.minY > 0.0D ? i26 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4));
			tessellator8.setColorOpaque_F(f17, f20, f23);
			this.renderBottomFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 0));
			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1)) {
			tessellator8.setBrightness(block1.maxY < 1.0D ? i26 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4));
			tessellator8.setColorOpaque_F(f14, f15, f16);
			this.renderTopFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 1));
			z9 = true;
		}

		int i28;
		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2)) {
			tessellator8.setBrightness(block1.minZ > 0.0D ? i26 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1));
			tessellator8.setColorOpaque_F(f18, f21, f24);
			i28 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 2);
			this.renderEastFace(block1, (double)i2, (double)i3, (double)i4, i28);
			if(fancyGrass && i28 == 3 && this.overrideBlockTexture < 0) {
				tessellator8.setColorOpaque_F(f18 * f5, f21 * f6, f24 * f7);
				this.renderEastFace(block1, (double)i2, (double)i3, (double)i4, 38);
			}

			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3)) {
			tessellator8.setBrightness(block1.maxZ < 1.0D ? i26 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1));
			tessellator8.setColorOpaque_F(f18, f21, f24);
			i28 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 3);
			this.renderWestFace(block1, (double)i2, (double)i3, (double)i4, i28);
			if(fancyGrass && i28 == 3 && this.overrideBlockTexture < 0) {
				tessellator8.setColorOpaque_F(f18 * f5, f21 * f6, f24 * f7);
				this.renderWestFace(block1, (double)i2, (double)i3, (double)i4, 38);
			}

			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4)) {
			tessellator8.setBrightness(block1.minX > 0.0D ? i26 : block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4));
			tessellator8.setColorOpaque_F(f19, f22, f25);
			i28 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 4);
			this.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, i28);
			if(fancyGrass && i28 == 3 && this.overrideBlockTexture < 0) {
				tessellator8.setColorOpaque_F(f19 * f5, f22 * f6, f25 * f7);
				this.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, 38);
			}

			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)) {
			tessellator8.setBrightness(block1.maxX < 1.0D ? i26 : block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4));
			tessellator8.setColorOpaque_F(f19, f22, f25);
			i28 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 5);
			this.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, i28);
			if(fancyGrass && i28 == 3 && this.overrideBlockTexture < 0) {
				tessellator8.setColorOpaque_F(f19 * f5, f22 * f6, f25 * f7);
				this.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, 38);
			}

			z9 = true;
		}

		return z9;
	}

	public boolean renderBlockCactus(Block block1, int i2, int i3, int i4) {
		int i5 = block1.colorMultiplier(this.blockAccess, i2, i3, i4);
		float f6 = (float)(i5 >> 16 & 255) / 255.0F;
		float f7 = (float)(i5 >> 8 & 255) / 255.0F;
		float f8 = (float)(i5 & 255) / 255.0F;
		if(GameRenderer.anaglyphEnable) {
			float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
			float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
			float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
			f6 = f9;
			f7 = f10;
			f8 = f11;
		}

		return this.renderBlockCactusImpl(block1, i2, i3, i4, f6, f7, f8);
	}

	public boolean renderBlockCactusImpl(Block block1, int i2, int i3, int i4, float f5, float f6, float f7) {
		Tessellator tessellator8 = Tessellator.instance;
		boolean z9 = false;
		float f10 = 0.5F;
		float f11 = 1.0F;
		float f12 = 0.8F;
		float f13 = 0.6F;
		float f14 = f10 * f5;
		float f15 = f11 * f5;
		float f16 = f12 * f5;
		float f17 = f13 * f5;
		float f18 = f10 * f6;
		float f19 = f11 * f6;
		float f20 = f12 * f6;
		float f21 = f13 * f6;
		float f22 = f10 * f7;
		float f23 = f11 * f7;
		float f24 = f12 * f7;
		float f25 = f13 * f7;
		float f26 = 0.0625F;
		int i28 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4);
		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0)) {
			tessellator8.setBrightness(block1.minY > 0.0D ? i28 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4));
			tessellator8.setColorOpaque_F(f14, f18, f22);
			this.renderBottomFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 0));
			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1)) {
			tessellator8.setBrightness(block1.maxY < 1.0D ? i28 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4));
			tessellator8.setColorOpaque_F(f15, f19, f23);
			this.renderTopFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 1));
			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2)) {
			tessellator8.setBrightness(block1.minZ > 0.0D ? i28 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1));
			tessellator8.setColorOpaque_F(f16, f20, f24);
			tessellator8.addTranslation(0.0F, 0.0F, f26);
			this.renderEastFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 2));
			tessellator8.addTranslation(0.0F, 0.0F, -f26);
			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3)) {
			tessellator8.setBrightness(block1.maxZ < 1.0D ? i28 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1));
			tessellator8.setColorOpaque_F(f16, f20, f24);
			tessellator8.addTranslation(0.0F, 0.0F, -f26);
			this.renderWestFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 3));
			tessellator8.addTranslation(0.0F, 0.0F, f26);
			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4)) {
			tessellator8.setBrightness(block1.minX > 0.0D ? i28 : block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4));
			tessellator8.setColorOpaque_F(f17, f21, f25);
			tessellator8.addTranslation(f26, 0.0F, 0.0F);
			this.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 4));
			tessellator8.addTranslation(-f26, 0.0F, 0.0F);
			z9 = true;
		}

		if(this.renderAllFaces || block1.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)) {
			tessellator8.setBrightness(block1.maxX < 1.0D ? i28 : block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4));
			tessellator8.setColorOpaque_F(f17, f21, f25);
			tessellator8.addTranslation(-f26, 0.0F, 0.0F);
			this.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 5));
			tessellator8.addTranslation(f26, 0.0F, 0.0F);
			z9 = true;
		}

		return z9;
	}

	public boolean renderBlockFence(Block block, int x, int y, int z) {
		
		float min = 0.375F;
		float max = 0.625F;
		block.setBlockBounds(min, 0.0F, min, max, 1.0F, max);
		this.renderStandardBlock(block, x, y, z);
		
		boolean connectEorW = false;
		boolean connectNorS = false;
		if(this.blockAccess.getBlockId(x - 1, y, z) == block.blockID || this.blockAccess.getBlockId(x + 1, y, z) == block.blockID) {
			connectEorW = true;
		}

		if(this.blockAccess.getBlockId(x, y, z - 1) == block.blockID || this.blockAccess.getBlockId(x, y, z + 1) == block.blockID) {
			connectNorS = true;
		}

		boolean connectE = this.blockAccess.getBlockId(x - 1, y, z) == block.blockID;
		boolean connectW = this.blockAccess.getBlockId(x + 1, y, z) == block.blockID;
		boolean connectN = this.blockAccess.getBlockId(x, y, z - 1) == block.blockID;
		boolean connectS = this.blockAccess.getBlockId(x, y, z + 1) == block.blockID;
		if(!connectEorW && !connectNorS) {
			connectEorW = true;
		}

		min = 0.4375F;
		max = 0.5625F;
		float minY = 0.75F;
		float maxY = 0.9375F;
		float minX = connectE ? 0.0F : min;
		float maxX = connectW ? 1.0F : max;
		float minZ = connectN ? 0.0F : min;
		float maxZ = connectS ? 1.0F : max;
		
		if(connectEorW) {
			block.setBlockBounds(minX, minY, min, maxX, maxY, max);
			this.renderStandardBlock(block, x, y, z);
		}

		if(connectNorS) {
			block.setBlockBounds(min, minY, minZ, max, maxY, maxZ);
			this.renderStandardBlock(block, x, y, z);
		}

		minY = 0.375F;
		maxY = 0.5625F;
		if(connectEorW) {
			block.setBlockBounds(minX, minY, min, maxX, maxY, max);
			this.renderStandardBlock(block, x, y, z);
		}

		if(connectNorS) {
			block.setBlockBounds(min, minY, minZ, max, maxY, maxZ);
			this.renderStandardBlock(block, x, y, z);
		}

		//block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		block.setBlockBoundsBasedOnState(this.blockAccess, x, y, z);
		
		return true;
	}

	public boolean renderBlockStairs(Block block1, int i2, int i3, int i4) {
		boolean z5 = false;
		int i6 = this.blockAccess.getBlockMetadata(i2, i3, i4);
		boolean upsideDown = (i6 & 8) != 0;
		i6 &= 7;
		
		if(upsideDown) {
			if(i6 == 0) {
				block1.setBlockBounds(0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				block1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 1) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				block1.setBlockBounds(0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 2) {
				block1.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F);
				this.renderStandardBlock(block1, i2, i3, i4);
				block1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 3) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
				this.renderStandardBlock(block1, i2, i3, i4);
				block1.setBlockBounds(0.0F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				z5 = true;
			}
		} else {
			if(i6 == 0) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				block1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 1) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				block1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 2) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
				this.renderStandardBlock(block1, i2, i3, i4);
				block1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				z5 = true;
			} else if(i6 == 3) {
				block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
				this.renderStandardBlock(block1, i2, i3, i4);
				block1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
				this.renderStandardBlock(block1, i2, i3, i4);
				z5 = true;
			}
		}
		
		block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return z5;
	}

	public boolean renderBlockDoor(Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		boolean z7 = false;
		float f8 = 0.5F;
		float f9 = 1.0F;
		float f10 = 0.8F;
		float f11 = 0.6F;
		int i12 = block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4);
		tessellator5.setBrightness(block1.minY > 0.0D ? i12 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 - 1, i4));
		tessellator5.setColorOpaque_F(f8, f8, f8);
		this.renderBottomFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 0));
		z7 = true;
		tessellator5.setBrightness(block1.maxY < 1.0D ? i12 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3 + 1, i4));
		tessellator5.setColorOpaque_F(f9, f9, f9);
		this.renderTopFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 1));
		z7 = true;
		tessellator5.setBrightness(block1.minZ > 0.0D ? i12 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 - 1));
		tessellator5.setColorOpaque_F(f10, f10, f10);
		int i14 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 2);
		if(i14 < 0) {
			this.flipTexture = true;
			i14 = -i14;
		}

		this.renderEastFace(block1, (double)i2, (double)i3, (double)i4, i14);
		z7 = true;
		this.flipTexture = false;
		tessellator5.setBrightness(block1.maxZ < 1.0D ? i12 : block1.getMixedBrightnessForBlock(this.blockAccess, i2, i3, i4 + 1));
		tessellator5.setColorOpaque_F(f10, f10, f10);
		i14 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 3);
		if(i14 < 0) {
			this.flipTexture = true;
			i14 = -i14;
		}

		this.renderWestFace(block1, (double)i2, (double)i3, (double)i4, i14);
		z7 = true;
		this.flipTexture = false;
		tessellator5.setBrightness(block1.minX > 0.0D ? i12 : block1.getMixedBrightnessForBlock(this.blockAccess, i2 - 1, i3, i4));
		tessellator5.setColorOpaque_F(f11, f11, f11);
		i14 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 4);
		if(i14 < 0) {
			this.flipTexture = true;
			i14 = -i14;
		}

		this.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, i14);
		z7 = true;
		this.flipTexture = false;
		tessellator5.setBrightness(block1.maxX < 1.0D ? i12 : block1.getMixedBrightnessForBlock(this.blockAccess, i2 + 1, i3, i4));
		tessellator5.setColorOpaque_F(f11, f11, f11);
		i14 = block1.getBlockTexture(this.blockAccess, i2, i3, i4, 5);
		if(i14 < 0) {
			this.flipTexture = true;
			i14 = -i14;
		}

		this.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, i14);
		z7 = true;
		this.flipTexture = false;
		return z7;
	}

	public void renderBottomFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 240;
		double d12 = ((double)i10 + block1.minX * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block1.maxX * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)i11 + block1.minZ * 16.0D) / 256.0D;
		double d18 = ((double)i11 + block1.maxZ * 16.0D - 0.01D) / 256.0D;
		if(block1.minX < 0.0D || block1.maxX > 1.0D) {
			d12 = (double)(((float)i10 + 0.0F) / 256.0F);
			d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		}

		if(block1.minZ < 0.0D || block1.maxZ > 1.0D) {
			d16 = (double)(((float)i11 + 0.0F) / 256.0F);
			d18 = (double)(((float)i11 + 15.99F) / 256.0F);
		}

		double d20 = d14;
		double d22 = d12;
		double d24 = d16;
		double d26 = d18;
		if(this.uvRotateBottom == 2) {
			d12 = ((double)i10 + block1.minZ * 16.0D) / 256.0D;
			d16 = ((double)(i11 + 16) - block1.maxX * 16.0D) / 256.0D;
			d14 = ((double)i10 + block1.maxZ * 16.0D) / 256.0D;
			d18 = ((double)(i11 + 16) - block1.minX * 16.0D) / 256.0D;
			d24 = d16;
			d26 = d18;
			d20 = d12;
			d22 = d14;
			d16 = d18;
			d18 = d24;
		} else if(this.uvRotateBottom == 1) {
			d12 = ((double)(i10 + 16) - block1.maxZ * 16.0D) / 256.0D;
			d16 = ((double)i11 + block1.minX * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.minZ * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.maxX * 16.0D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d12 = d14;
			d14 = d22;
			d24 = d18;
			d26 = d16;
		} else if(this.uvRotateBottom == 3) {
			d12 = ((double)(i10 + 16) - block1.minX * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.maxX * 16.0D - 0.01D) / 256.0D;
			d16 = ((double)(i11 + 16) - block1.minZ * 16.0D) / 256.0D;
			d18 = ((double)(i11 + 16) - block1.maxZ * 16.0D - 0.01D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d24 = d16;
			d26 = d18;
		}

		double d28 = d2 + block1.minX;
		double d30 = d2 + block1.maxX;
		double d32 = d4 + block1.minY;
		double d34 = d6 + block1.minZ;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d32, d36, d22, d26);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d28, d32, d34, d12, d16);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d30, d32, d34, d20, d24);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d30, d32, d36, d14, d18);
		} else {
			tessellator9.addVertexWithUV(d28, d32, d36, d22, d26);
			tessellator9.addVertexWithUV(d28, d32, d34, d12, d16);
			tessellator9.addVertexWithUV(d30, d32, d34, d20, d24);
			tessellator9.addVertexWithUV(d30, d32, d36, d14, d18);
		}

	}

	public void renderTopFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 240;
		double d12 = ((double)i10 + block1.minX * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block1.maxX * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)i11 + block1.minZ * 16.0D) / 256.0D;
		double d18 = ((double)i11 + block1.maxZ * 16.0D - 0.01D) / 256.0D;
		if(block1.minX < 0.0D || block1.maxX > 1.0D) {
			d12 = (double)(((float)i10 + 0.0F) / 256.0F);
			d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		}

		if(block1.minZ < 0.0D || block1.maxZ > 1.0D) {
			d16 = (double)(((float)i11 + 0.0F) / 256.0F);
			d18 = (double)(((float)i11 + 15.99F) / 256.0F);
		}

		double d20 = d14;
		double d22 = d12;
		double d24 = d16;
		double d26 = d18;
		if(this.uvRotateTop == 1) {
			d12 = ((double)i10 + block1.minZ * 16.0D) / 256.0D;
			d16 = ((double)(i11 + 16) - block1.maxX * 16.0D) / 256.0D;
			d14 = ((double)i10 + block1.maxZ * 16.0D) / 256.0D;
			d18 = ((double)(i11 + 16) - block1.minX * 16.0D) / 256.0D;
			d24 = d16;
			d26 = d18;
			d20 = d12;
			d22 = d14;
			d16 = d18;
			d18 = d24;
		} else if(this.uvRotateTop == 2) {
			d12 = ((double)(i10 + 16) - block1.maxZ * 16.0D) / 256.0D;
			d16 = ((double)i11 + block1.minX * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.minZ * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.maxX * 16.0D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d12 = d14;
			d14 = d22;
			d24 = d18;
			d26 = d16;
		} else if(this.uvRotateTop == 3) {
			d12 = ((double)(i10 + 16) - block1.minX * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.maxX * 16.0D - 0.01D) / 256.0D;
			d16 = ((double)(i11 + 16) - block1.minZ * 16.0D) / 256.0D;
			d18 = ((double)(i11 + 16) - block1.maxZ * 16.0D - 0.01D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d24 = d16;
			d26 = d18;
		}

		double d28 = d2 + block1.minX;
		double d30 = d2 + block1.maxX;
		double d32 = d4 + block1.maxY;
		double d34 = d6 + block1.minZ;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d30, d32, d36, d14, d18);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d30, d32, d34, d20, d24);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d28, d32, d34, d12, d16);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d28, d32, d36, d22, d26);
		} else {
			tessellator9.addVertexWithUV(d30, d32, d36, d14, d18);
			tessellator9.addVertexWithUV(d30, d32, d34, d20, d24);
			tessellator9.addVertexWithUV(d28, d32, d34, d12, d16);
			tessellator9.addVertexWithUV(d28, d32, d36, d22, d26);
		}

	}

	public void renderEastFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 240;
		double d12 = ((double)i10 + block1.minX * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block1.maxX * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)(i11 + 16) - block1.maxY * 16.0D) / 256.0D;
		double d18 = ((double)(i11 + 16) - block1.minY * 16.0D - 0.01D) / 256.0D;
		double d20;
		if(this.flipTexture) {
			d20 = d12;
			d12 = d14;
			d14 = d20;
		}

		if(block1.minX < 0.0D || block1.maxX > 1.0D) {
			d12 = (double)(((float)i10 + 0.0F) / 256.0F);
			d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		}

		if(block1.minY < 0.0D || block1.maxY > 1.0D) {
			d16 = (double)(((float)i11 + 0.0F) / 256.0F);
			d18 = (double)(((float)i11 + 15.99F) / 256.0F);
		}

		d20 = d14;
		double d22 = d12;
		double d24 = d16;
		double d26 = d18;
		if(this.uvRotateEast == 2) {
			d12 = ((double)i10 + block1.minY * 16.0D) / 256.0D;
			d16 = ((double)(i11 + 16) - block1.minX * 16.0D) / 256.0D;
			d14 = ((double)i10 + block1.maxY * 16.0D) / 256.0D;
			d18 = ((double)(i11 + 16) - block1.maxX * 16.0D) / 256.0D;
			d24 = d16;
			d26 = d18;
			d20 = d12;
			d22 = d14;
			d16 = d18;
			d18 = d24;
		} else if(this.uvRotateEast == 1) {
			d12 = ((double)(i10 + 16) - block1.maxY * 16.0D) / 256.0D;
			d16 = ((double)i11 + block1.maxX * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.minY * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.minX * 16.0D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d12 = d14;
			d14 = d22;
			d24 = d18;
			d26 = d16;
		} else if(this.uvRotateEast == 3) {
			d12 = ((double)(i10 + 16) - block1.minX * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.maxX * 16.0D - 0.01D) / 256.0D;
			d16 = ((double)i11 + block1.maxY * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.minY * 16.0D - 0.01D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d24 = d16;
			d26 = d18;
		}

		double d28 = d2 + block1.minX;
		double d30 = d2 + block1.maxX;
		double d32 = d4 + block1.minY;
		double d34 = d4 + block1.maxY;
		double d36 = d6 + block1.minZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d34, d36, d20, d24);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d30, d34, d36, d12, d16);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d30, d32, d36, d22, d26);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d28, d32, d36, d14, d18);
		} else {
			tessellator9.addVertexWithUV(d28, d34, d36, d20, d24);
			tessellator9.addVertexWithUV(d30, d34, d36, d12, d16);
			tessellator9.addVertexWithUV(d30, d32, d36, d22, d26);
			tessellator9.addVertexWithUV(d28, d32, d36, d14, d18);
		}

	}

	public void renderWestFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 240;
		double d12 = ((double)i10 + block1.minX * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block1.maxX * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)(i11 + 16) - block1.maxY * 16.0D) / 256.0D;
		double d18 = ((double)(i11 + 16) - block1.minY * 16.0D - 0.01D) / 256.0D;
		double d20;
		if(this.flipTexture) {
			d20 = d12;
			d12 = d14;
			d14 = d20;
		}

		if(block1.minX < 0.0D || block1.maxX > 1.0D) {
			d12 = (double)(((float)i10 + 0.0F) / 256.0F);
			d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		}

		if(block1.minY < 0.0D || block1.maxY > 1.0D) {
			d16 = (double)(((float)i11 + 0.0F) / 256.0F);
			d18 = (double)(((float)i11 + 15.99F) / 256.0F);
		}

		d20 = d14;
		double d22 = d12;
		double d24 = d16;
		double d26 = d18;
		if(this.uvRotateWest == 1) {
			d12 = ((double)i10 + block1.minY * 16.0D) / 256.0D;
			d18 = ((double)(i11 + 16) - block1.minX * 16.0D) / 256.0D;
			d14 = ((double)i10 + block1.maxY * 16.0D) / 256.0D;
			d16 = ((double)(i11 + 16) - block1.maxX * 16.0D) / 256.0D;
			d24 = d16;
			d26 = d18;
			d20 = d12;
			d22 = d14;
			d16 = d18;
			d18 = d24;
		} else if(this.uvRotateWest == 2) {
			d12 = ((double)(i10 + 16) - block1.maxY * 16.0D) / 256.0D;
			d16 = ((double)i11 + block1.minX * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.minY * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.maxX * 16.0D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d12 = d14;
			d14 = d22;
			d24 = d18;
			d26 = d16;
		} else if(this.uvRotateWest == 3) {
			d12 = ((double)(i10 + 16) - block1.minX * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.maxX * 16.0D - 0.01D) / 256.0D;
			d16 = ((double)i11 + block1.maxY * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.minY * 16.0D - 0.01D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d24 = d16;
			d26 = d18;
		}

		double d28 = d2 + block1.minX;
		double d30 = d2 + block1.maxX;
		double d32 = d4 + block1.minY;
		double d34 = d4 + block1.maxY;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d34, d36, d12, d16);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d28, d32, d36, d22, d26);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d30, d32, d36, d14, d18);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d30, d34, d36, d20, d24);
		} else {
			tessellator9.addVertexWithUV(d28, d34, d36, d12, d16);
			tessellator9.addVertexWithUV(d28, d32, d36, d22, d26);
			tessellator9.addVertexWithUV(d30, d32, d36, d14, d18);
			tessellator9.addVertexWithUV(d30, d34, d36, d20, d24);
		}

	}

	public void renderNorthFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 240;
		double d12 = ((double)i10 + block1.minZ * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block1.maxZ * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)(i11 + 16) - block1.maxY * 16.0D) / 256.0D;
		double d18 = ((double)(i11 + 16) - block1.minY * 16.0D - 0.01D) / 256.0D;
		double d20;
		if(this.flipTexture) {
			d20 = d12;
			d12 = d14;
			d14 = d20;
		}

		if(block1.minZ < 0.0D || block1.maxZ > 1.0D) {
			d12 = (double)(((float)i10 + 0.0F) / 256.0F);
			d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		}

		if(block1.minY < 0.0D || block1.maxY > 1.0D) {
			d16 = (double)(((float)i11 + 0.0F) / 256.0F);
			d18 = (double)(((float)i11 + 15.99F) / 256.0F);
		}

		d20 = d14;
		double d22 = d12;
		double d24 = d16;
		double d26 = d18;
		if(this.uvRotateNorth == 1) {
			d12 = ((double)i10 + block1.minY * 16.0D) / 256.0D;
			d16 = ((double)(i11 + 16) - block1.maxZ * 16.0D) / 256.0D;
			d14 = ((double)i10 + block1.maxY * 16.0D) / 256.0D;
			d18 = ((double)(i11 + 16) - block1.minZ * 16.0D) / 256.0D;
			d24 = d16;
			d26 = d18;
			d20 = d12;
			d22 = d14;
			d16 = d18;
			d18 = d24;
		} else if(this.uvRotateNorth == 2) {
			d12 = ((double)(i10 + 16) - block1.maxY * 16.0D) / 256.0D;
			d16 = ((double)i11 + block1.minZ * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.minY * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.maxZ * 16.0D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d12 = d14;
			d14 = d22;
			d24 = d18;
			d26 = d16;
		} else if(this.uvRotateNorth == 3) {
			d12 = ((double)(i10 + 16) - block1.minZ * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.maxZ * 16.0D - 0.01D) / 256.0D;
			d16 = ((double)i11 + block1.maxY * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.minY * 16.0D - 0.01D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d24 = d16;
			d26 = d18;
		}

		double d28 = d2 + block1.minX;
		double d30 = d4 + block1.minY;
		double d32 = d4 + block1.maxY;
		double d34 = d6 + block1.minZ;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d32, d36, d20, d24);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d28, d32, d34, d12, d16);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d28, d30, d34, d22, d26);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d28, d30, d36, d14, d18);
		} else {
			tessellator9.addVertexWithUV(d28, d32, d36, d20, d24);
			tessellator9.addVertexWithUV(d28, d32, d34, d12, d16);
			tessellator9.addVertexWithUV(d28, d30, d34, d22, d26);
			tessellator9.addVertexWithUV(d28, d30, d36, d14, d18);
		}

	}

	public void renderSouthFace(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		int i11 = i8 & 240;
		double d12 = ((double)i10 + block1.minZ * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block1.maxZ * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)(i11 + 16) - block1.maxY * 16.0D) / 256.0D;
		double d18 = ((double)(i11 + 16) - block1.minY * 16.0D - 0.01D) / 256.0D;
		double d20;
		if(this.flipTexture) {
			d20 = d12;
			d12 = d14;
			d14 = d20;
		}

		if(block1.minZ < 0.0D || block1.maxZ > 1.0D) {
			d12 = (double)(((float)i10 + 0.0F) / 256.0F);
			d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		}

		if(block1.minY < 0.0D || block1.maxY > 1.0D) {
			d16 = (double)(((float)i11 + 0.0F) / 256.0F);
			d18 = (double)(((float)i11 + 15.99F) / 256.0F);
		}

		d20 = d14;
		double d22 = d12;
		double d24 = d16;
		double d26 = d18;
		if(this.uvRotateSouth == 2) {
			d12 = ((double)i10 + block1.minY * 16.0D) / 256.0D;
			d16 = ((double)(i11 + 16) - block1.minZ * 16.0D) / 256.0D;
			d14 = ((double)i10 + block1.maxY * 16.0D) / 256.0D;
			d18 = ((double)(i11 + 16) - block1.maxZ * 16.0D) / 256.0D;
			d24 = d16;
			d26 = d18;
			d20 = d12;
			d22 = d14;
			d16 = d18;
			d18 = d24;
		} else if(this.uvRotateSouth == 1) {
			d12 = ((double)(i10 + 16) - block1.maxY * 16.0D) / 256.0D;
			d16 = ((double)i11 + block1.maxZ * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.minY * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.minZ * 16.0D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d12 = d14;
			d14 = d22;
			d24 = d18;
			d26 = d16;
		} else if(this.uvRotateSouth == 3) {
			d12 = ((double)(i10 + 16) - block1.minZ * 16.0D) / 256.0D;
			d14 = ((double)(i10 + 16) - block1.maxZ * 16.0D - 0.01D) / 256.0D;
			d16 = ((double)i11 + block1.maxY * 16.0D) / 256.0D;
			d18 = ((double)i11 + block1.minY * 16.0D - 0.01D) / 256.0D;
			d20 = d14;
			d22 = d12;
			d24 = d16;
			d26 = d18;
		}

		double d28 = d2 + block1.maxX;
		double d30 = d4 + block1.minY;
		double d32 = d4 + block1.maxY;
		double d34 = d6 + block1.minZ;
		double d36 = d6 + block1.maxZ;
		if(this.enableAO) {
			tessellator9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator9.setBrightness(this.brightnessTopLeft);
			tessellator9.addVertexWithUV(d28, d30, d36, d22, d26);
			tessellator9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator9.setBrightness(this.brightnessBottomLeft);
			tessellator9.addVertexWithUV(d28, d30, d34, d14, d18);
			tessellator9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator9.setBrightness(this.brightnessBottomRight);
			tessellator9.addVertexWithUV(d28, d32, d34, d20, d24);
			tessellator9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator9.setBrightness(this.brightnessTopRight);
			tessellator9.addVertexWithUV(d28, d32, d36, d12, d16);
		} else {
			tessellator9.addVertexWithUV(d28, d30, d36, d22, d26);
			tessellator9.addVertexWithUV(d28, d30, d34, d14, d18);
			tessellator9.addVertexWithUV(d28, d32, d34, d20, d24);
			tessellator9.addVertexWithUV(d28, d32, d36, d12, d16);
		}

	}

	// Custom renderers

	public boolean renderBlockWall(Block block, int x, int y, int z) {
		Tessellator tessellator5 = Tessellator.instance;
		int i6 = block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i6 = this.overrideBlockTexture;
		}

		/*
		float f7 = block.getBlockBrightness(this.blockAccess, x, y, z);
		tessellator5.setColorOpaque_F(f7, f7, f7);
		*/
		tessellator5.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, x, y, z));
		tessellator5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		
		int i8 = (i6 & 15) << 4;
		int i9 = i6 & 240;
		double d10 = (double)((float)i8 / 256.0F);
		double d12 = (double)(((float)i8 + 15.99F) / 256.0F);
		double d14 = (double)((float)i9 / 256.0F);
		double d16 = (double)(((float)i9 + 15.99F) / 256.0F);
		int i18 = this.blockAccess.getBlockMetadata(x, y, z);
		
		float of1 = 0.49F; 
		float of2 = 0.51F;
		
		
		if(i18 == 4 || i18 == 5) {
			tessellator5.addVertexWithUV((double)((float)x + of1), (double)(y + 1), (double)(z + 1), d10, d14);
			tessellator5.addVertexWithUV((double)((float)x + of1), (double)(y + 0), (double)(z + 1), d10, d16);
			tessellator5.addVertexWithUV((double)((float)x + of1), (double)(y + 0), (double)(z + 0), d12, d16);
			tessellator5.addVertexWithUV((double)((float)x + of1), (double)(y + 1), (double)(z + 0), d12, d14);
			
			tessellator5.addVertexWithUV((double)((float)x + of2), (double)(y + 1), (double)(z + 0), d12, d14);
			tessellator5.addVertexWithUV((double)((float)x + of2), (double)(y + 0), (double)(z + 0), d12, d16);
			tessellator5.addVertexWithUV((double)((float)x + of2), (double)(y + 0), (double)(z + 1), d10, d16);
			tessellator5.addVertexWithUV((double)((float)x + of2), (double)(y + 1), (double)(z + 1), d10, d14);
		}

		if(i18 == 2 || i18 == 3) {
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)((float)z + of1), d12, d16);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 1), (double)((float)z + of1), d12, d14);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 1), (double)((float)z + of1), d10, d14);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)((float)z + of1), d10, d16);
			
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)((float)z + of1), d10, d16);
			tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 1), (double)((float)z + of1), d10, d14);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 1), (double)((float)z + of1), d12, d14);
			tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)((float)z + of1), d12, d16);
		}

		return true;
	}
	
	public boolean renderBarbedWire(Block block, int x, int y, int z) {
		Tessellator tessellator = Tessellator.instance;		
		
		/*
		float blockBrightness = block.getBlockBrightness(this.blockAccess, x, y, z);
		tessellator.setColorOpaque_F(blockBrightness, blockBrightness, blockBrightness);
		*/
		
		tessellator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, x, y, z));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		
		int textureIndex = block.getBlockTextureFromSide(2);
		if(this.overrideBlockTexture >= 0) {
			textureIndex = this.overrideBlockTexture;
		}
		
		int u0 = (textureIndex & 15) << 4;
		int v0 = textureIndex & 0xf0;
		
		double u1 = (double)((float)u0 / 256.0F);
		double uh = (double)(((float)u0 + 8.00F) / 256.0F);
		double u2 = (double)(((float)u0 + 15.99F) / 256.0F);
		
		double v1 = (double)((float)v0 / 256.0F);
		double v2 = (double)(((float)v0 + 15.99F) / 256.0F);		
		
		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		
		if (meta == 4 || meta == 5) {
			// half 1, side 1, add vertexes CCW
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 0.0F, (double)z + 1.0F, uh, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 1.0F, (double)z + 0.5F, u1, v1);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 1.0F, u1, v2);
		
			// half 1, side 2, add vertexes CW
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 1.0F, u1, v2);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 1.0F, (double)z + 0.5F, u1, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 0.0F, (double)z + 1.0F, uh, v2);			

			// half 2, side 1, add vertexes CCW
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.0F, u2, v2);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 1.0F, (double)z + 0.5F, u2, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 0.0F, (double)z + 0.0F, uh, v2);
					
			// half 2, side 2, add vertexes CW
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 0.0F, (double)z + 0.0F, uh, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 1.0F, (double)z + 0.5F, u2, v1);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.0F, u2, v2);
		} else if (meta == 2 || meta == 3) {
			
			// half 1, side 1, add vertexes CCW
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.5F, uh, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.0F, u1, v1);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.0F, u1, v2);
			
			// half 1, side 2, add vertexes CW
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.0F, u1, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.0F, u1, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 1.0F, (double)y + 0.0F, (double)z + 0.5F, uh, v2);		
			
			// half 2, side 1, add vertexes CCW
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 1.0F, u2, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 1.0F, u2, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 0.5F, uh, v2);
			
			// half 2, side 2, add vertexes CW
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 0.5F, uh, v2);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 0.5F, uh, v1);
			tessellator.addVertexWithUV((double)x + 0.5F, (double)y + 1.0F, (double)z + 1.0F, u2, v1);
			tessellator.addVertexWithUV((double)x + 0.0F, (double)y + 0.0F, (double)z + 1.0F, u2, v2);		
			
		}
		
		return true;
	}
	
	public boolean renderHollowTrunk(Block block, int x, int y, int z) {
		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		
		int ti_0, ti_1, ti_2;
		if(this.overrideBlockTexture >= 0) {
			ti_0 = ti_1 = ti_2 = overrideBlockTexture;
		} else {
			ti_0 = block.getBlockTextureFromSide(0);
			ti_1 = block.getBlockTextureFromSide(1);
			ti_2 = block.getBlockTextureFromSide(2);
		}
			
		RenderBlockHollowLog.renderBlock(this.blockAccess, block, meta, x, y, z, ti_0, ti_1, ti_2);
		return true;
	}
	
	public boolean renderBlockDirtPath(Block block, int x, int y, int z) {
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 15.0F * (1 / 16.0F), 1.0F);
		return this.renderStandardBlock(block, x, y, z);
	}
	
	
	public boolean renderBlockChippedWood(Block block, int x, int y, int z) {
		float x1, y1, z1, x2, y2, z2;
		float u1, v1, u2, v2;
		
		Tessellator tessellator = Tessellator.instance;
		
		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		
		if((meta & 4) == 0) {
			block.setBlockBounds((1 / 16.0F), 0.0F, (1 / 16.0F), 15 * (1 / 16.0F), 1.0F, 15 * (1 / 16.0F));
			this.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			return true;
		} else if((meta & 8) != 0) { 
			// facing North/South
			
			// Texture #1 (ends)
			int ti_1 = block.getBlockTexture(blockAccess, x, y, z, 1); 	// CUSTOM : side = 1 means "ends"
			if(this.overrideBlockTexture >= 0) ti_1 = this.overrideBlockTexture;
			
			float t1_u = (float) ((ti_1 & 0x0f) << 4) / 256.0F;
			float t1_v = (float) (ti_1 & 0xff0) / 256.0F;

			// Texture #2 (sides)
			int ti_2 = block.getBlockTexture(blockAccess, x, y, z, 0); 	// CUSTOM : side = 1 means "sides"
			if(this.overrideBlockTexture >= 0) ti_2 = this.overrideBlockTexture;
			
			float t2_u = (float) ((ti_2 & 0x0f) << 4) / 256.0F;
			float t2_v = (float) (ti_2 & 0xff0) / 256.0F;
			
			x1 = x + 0.0000F;
			y1 = y + 0.0625F;
			z1 = z + 0.0625F;
			x2 = x + 1.0000F;
			y2 = y + 0.9375F;
			z2 = z + 0.9375F;

			setLightValue(tessellator, blockAccess, block, x, y + 1, z, 1.0F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y2, z1, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);

			setLightValue(tessellator, blockAccess, block, x, y - 1, z, 0.5F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y1, z2, u1, v2);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u1, v1);

			setLightValue(tessellator, blockAccess, block, x, y, z - 1, 0.8F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);

			setLightValue(tessellator, blockAccess, block, x, y, z + 1, 0.8F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v2);

			setLightValue(tessellator, blockAccess, block, x + 1, y, z, 0.6F);
			u1 = t1_u + 0.00390625F;
			v1 = t1_v + 0.00390625F;
			u2 = t1_u + 0.05859375F;
			v2 = t1_v + 0.05859375F;
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v2);

			setLightValue(tessellator, blockAccess, block, x - 1, y, z, 0.6F);
			u1 = t1_u + 0.00390625F;
			v1 = t1_v + 0.00390625F;
			u2 = t1_u + 0.05859375F;
			v2 = t1_v + 0.05859375F;
			tessellator.addVertexWithUV(x1, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);
			
			return true;
		} else {
			// facing East/West
			
			// Texture #1 (ends)
			int ti_1 = block.getBlockTexture(blockAccess, x, y, z, 1); 	// CUSTOM : side = 1 means "ends"
			if(this.overrideBlockTexture >= 0) ti_1 = this.overrideBlockTexture;
			float t1_u = (float) ((ti_1 & 0x0f) << 4) / 256.0F;
			float t1_v = (float) (ti_1 & 0xff0) / 256.0F;

			// Texture #2 (sides)
			int ti_2 = block.getBlockTexture(blockAccess, x, y, z, 0); 	// CUSTOM : side = 1 means "sides"
			if(this.overrideBlockTexture >= 0) ti_2 = this.overrideBlockTexture;
			float t2_u = (float) ((ti_2 & 0x0f) << 4) / 256.0F;
			float t2_v = (float) (ti_2 & 0xff0) / 256.0F;
			
			x1 = x + 0.0625F;
			y1 = y + 0.0625F;
			z1 = z + 0.0000F;
			x2 = x + 0.9375F;
			y2 = y + 0.9375F;
			z2 = z + 1.0000F;

			setLightValue(tessellator, blockAccess, block, x, y + 1, z, 1.0F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x1, y2, z1, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u2, v1);

			setLightValue(tessellator, blockAccess, block, x, y - 1, z, 0.5F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);
			tessellator.addVertexWithUV(x2, y1, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y1, z2, u1, v1);

			setLightValue(tessellator, blockAccess, block, x + 1, y, z, 0.6F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);

			setLightValue(tessellator, blockAccess, block, x - 1, y, z, 0.6F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v2);

			setLightValue(tessellator, blockAccess, block, x, y, z - 1, 0.8F);
			u1 = t1_u + 0.00390625F;
			v1 = t1_v + 0.00390625F;
			u2 = t1_u + 0.05859375F;
			v2 = t1_v + 0.05859375F;
			tessellator.addVertexWithUV(x1, y2, z1, u2, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u1, v2);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);

			setLightValue(tessellator, blockAccess, block, x, y, z + 1, 0.8F);
			u1 = t1_u + 0.00390625F;
			v1 = t1_v + 0.00390625F;
			u2 = t1_u + 0.05859375F;
			v2 = t1_v + 0.05859375F;
			tessellator.addVertexWithUV(x1, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u1, v2);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u2, v1);
			
			return true;
		}

	}
	
	public boolean renderBlockStreetLantern(Block block, int x, int y, int z) {
		// Add a torch for non broken street lanterns
		if(this.activeRenderPass == 0) {
			if(block.blockID == Block.streetLantern.blockID) {
				Tessellator tessellator = Tessellator.instance;
				tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);		
				this.renderTorchAtAngle(Block.torchWood, (double)x, (double)y, (double)z, 0.0D, 0.0D);
				return true;
			} else {
				return false;
			}
		} else {
			return this.renderBlockCactus(block, x, y, z);
		}
	}
	
	public boolean renderBlockSlime(Block block, int x, int y, int z) {
		// This renderer sends different quads depending on the active renderPass
		
		if(this.activeRenderPass == 0) {
			// Set dimensions of inner block
			block.setBlockBounds(0.125F, 0.125F, 0.125F, 0.875F, 0.875F, 0.875F);
			
			// Render inner box
			boolean wasRendered = this.renderStandardBlock(block, x, y, z);
			
			// Set dimensions of outer block
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		
			return wasRendered;
		} else {
			// Render outer box
			return this.renderStandardBlock(block, x, y, z);
		}
	}
	
	public static void setLightValue (Tessellator tessellator, IBlockAccess blockAccess, Block block, float x, float y, float z, float factor) {
		/*
		float f;
		if (blockAccess == null) f = factor; else f = block.getBlockBrightness(blockAccess, (int) x, (int) y, (int) z) * factor;
		if (Block.lightValue[block.blockID] > 0) f = factor;
		tessellator.setColorOpaque_F(f, f, f);
		*/
		
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, (int)x, (int)y, (int)z));
		tessellator.setColorOpaque_F(factor, factor, factor);
	}
	
	/*
	public boolean renderBlockWoodOriented(Block block, int x, int y, int z) {
		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		if((meta & 4) == 0) {
			return this.renderStandardBlock(block, x, y, z); 
		} else {
			int standardMeta = (meta & 8) != 0 ? 4 : 2;
			return this.renderBlockAxisOriented(block, x, y, z, standardMeta);
		}
	}
	*/
	
	public boolean renderBlockWoodOriented(Block block, int x, int y, int z) {
		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		int orientation = meta & 12;
		if(orientation == 12) {
			this.uvRotateEast = 1;
			this.uvRotateWest = 1;
			this.uvRotateTop = 1;
			this.uvRotateBottom = 1;
		} else if(orientation == 4) {
			this.uvRotateSouth = 1;
			this.uvRotateNorth = 1;
		}

		boolean res = this.renderStandardBlock(block, x, y, z);
		this.uvRotateSouth = 0;
		this.uvRotateEast = 0;
		this.uvRotateWest = 0;
		this.uvRotateNorth = 0;
		this.uvRotateTop = 0;
		this.uvRotateBottom = 0;
		return res;
	}
	
	public boolean renderBlockAxisOriented(Block block, int x, int y, int z) {	
		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		
		if(meta == 4 || meta == 5) {
			this.uvRotateEast = 1;
			this.uvRotateWest = 1;
			this.uvRotateTop = 1;
			this.uvRotateBottom = 1;
		} else if(meta == 2 || meta == 3) {
			this.uvRotateSouth = 1;
			this.uvRotateNorth = 1;
		}

		boolean res = this.renderStandardBlock(block, x, y, z);
		this.uvRotateSouth = 0;
		this.uvRotateEast = 0;
		this.uvRotateWest = 0;
		this.uvRotateNorth = 0;
		this.uvRotateTop = 0;
		this.uvRotateBottom = 0;
		return res;
	}
	
	/*
	public boolean renderBlockAxisOriented(Block block, int x, int y, int z) {	
		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		return this.renderBlockAxisOriented(block, x, y, z, meta);
	}
	
	public boolean renderBlockAxisOriented(Block block, int x, int y, int z, int meta) {
		float x1, y1, z1, x2, y2, z2;
		float u1, v1, u2, v2;
		
		Tessellator tessellator = Tessellator.instance;
				
		if(meta == 0) {
			return this.renderStandardBlock(block, x, y, z);
		} else if(meta == 4 || meta == 5) {
			// facing North/South
			
			// Texture #1 (ends)
			int ti_1 = block.getBlockTexture(blockAccess, x, y, z, 1); 	// CUSTOM : side = 1 means "ends"
			float t1_u = (float) ((ti_1 & 0x0f) << 4) / 256.0F;
			float t1_v = (float) (ti_1 & 0xff0) / 256.0F;

			// Texture #2 (sides)
			int ti_2 = block.getBlockTexture(blockAccess, x, y, z, 0); 	// CUSTOM : side = 1 means "sides"
			float t2_u = (float) ((ti_2 & 0x0f) << 4) / 256.0F;
			float t2_v = (float) (ti_2 & 0xff0) / 256.0F;
			
			x1 = x + 0.0000F;
			y1 = y + 0.0000F;
			z1 = z + 0.0000F;
			x2 = x + 1.0000F;
			y2 = y + 1.0000F;
			z2 = z + 1.0000F;

			setLightValue(tessellator, blockAccess, block, x, y + 1, z, 1.0F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y2, z1, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);

			setLightValue(tessellator, blockAccess, block, x, y - 1, z, 0.5F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y1, z2, u1, v2);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u1, v1);

			setLightValue(tessellator, blockAccess, block, x, y, z - 1, 0.8F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);

			setLightValue(tessellator, blockAccess, block, x, y, z + 1, 0.8F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v2);

			setLightValue(tessellator, blockAccess, block, x + 1, y, z, 0.6F);
			u1 = t1_u + 0.0000000F;
			v1 = t1_v + 0.0000000F;
			u2 = t1_u + 0.0625000F;
			v2 = t1_v + 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v2);

			setLightValue(tessellator, blockAccess, block, x - 1, y, z, 0.6F);
			u1 = t1_u + 0.0000000F;
			v1 = t1_v + 0.0000000F;
			u2 = t1_u + 0.0625000F;
			v2 = t1_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);
			
			return true;
		} else if(meta == 2 || meta == 3) {
			// facing East/West
			
			// Texture #1 (ends)
			int ti_1 = block.getBlockTexture(blockAccess, x, y, z, 1); 	// CUSTOM : side = 1 means "ends"
			float t1_u = (float) ((ti_1 & 0x0f) << 4) / 256.0F;
			float t1_v = (float) (ti_1 & 0xff0) / 256.0F;

			// Texture #2 (sides)
			int ti_2 = block.getBlockTexture(blockAccess, x, y, z, 0); 	// CUSTOM : side = 1 means "sides"
			float t2_u = (float) ((ti_2 & 0x0f) << 4) / 256.0F;
			float t2_v = (float) (ti_2 & 0xff0) / 256.0F;
			
			x1 = x + 1.0000F;
			y1 = y + 0.0000F;
			z1 = z + 1.0000F;
			x2 = x + 0.0000F;
			y2 = y + 1.0000F;
			z2 = z + 0.0000F;

			setLightValue(tessellator, blockAccess, block, x, y + 1, z, 1.0F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x1, y2, z1, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u2, v1);

			setLightValue(tessellator, blockAccess, block, x, y - 1, z, 0.5F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);
			tessellator.addVertexWithUV(x2, y1, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y1, z2, u1, v1);

			setLightValue(tessellator, blockAccess, block, x + 1, y, z, 0.6F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x1, y2, z2, u1, v2);

			setLightValue(tessellator, blockAccess, block, x - 1, y, z, 0.6F);
			u1 = t2_u + 0.0000000F;
			v1 = t2_v + 0.0000000F;
			u2 = t2_u + 0.0625000F;
			v2 = t2_v + 0.0625000F;
			tessellator.addVertexWithUV(x2, y2, z1, u1, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u2, v2);

			setLightValue(tessellator, blockAccess, block, x, y, z + 1, 0.8F);
			u1 = t1_u + 0.0000000F;
			v1 = t1_v + 0.0000000F;
			u2 = t1_u + 0.0625000F;
			v2 = t1_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z1, u2, v1);
			tessellator.addVertexWithUV(x2, y2, z1, u1, v1);
			tessellator.addVertexWithUV(x2, y1, z1, u1, v2);
			tessellator.addVertexWithUV(x1, y1, z1, u2, v2);

			setLightValue(tessellator, blockAccess, block, x, y, z - 1, 0.8F);
			u1 = t1_u + 0.0000000F;
			v1 = t1_v + 0.0000000F;
			u2 = t1_u + 0.0625000F;
			v2 = t1_v + 0.0625000F;
			tessellator.addVertexWithUV(x1, y2, z2, u1, v1);
			tessellator.addVertexWithUV(x1, y1, z2, u1, v2);
			tessellator.addVertexWithUV(x2, y1, z2, u2, v2);
			tessellator.addVertexWithUV(x2, y2, z2, u2, v1);
			
			return true;
		}
		
		return false;
	}
	*/
	
	public boolean renderBlockClassicPiston(Block block, int x, int y, int z) {
		return RenderBlockClassicPiston.RenderWorldBlock(this, this.blockAccess, x, y, z, block, 0);
	}
	
	public boolean renderBlockModel(Block block, int x, int y, int z) {
		int meta = this.blockAccess.getBlockMetadata(x, y, z);
		return RenderBlockModel.renderBlock(this.blockAccess, block, x, y, z, meta);
	}
	
	// End
	
	public void renderBlockAsItem(Block block1, float f2) {
		int i3 = block1.getRenderType();
		Tessellator tessellator4 = Tessellator.instance;
		if(i3 == 0) {
			block1.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			float f5 = 0.5F;
			float f6 = 1.0F;
			float f7 = 0.8F;
			float f8 = 0.6F;
			tessellator4.startDrawingQuads();
			tessellator4.setColorRGBA_F(f6, f6, f6, f2);
			this.renderBottomFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(0));
			tessellator4.setColorRGBA_F(f5, f5, f5, f2);
			this.renderTopFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(1));
			tessellator4.setColorRGBA_F(f7, f7, f7, f2);
			this.renderEastFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(2));
			this.renderWestFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(3));
			tessellator4.setColorRGBA_F(f8, f8, f8, f2);
			this.renderNorthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(4));
			this.renderSouthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(5));
			tessellator4.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		} else {
			block1.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator4.startDrawingQuads();
			tessellator4.setColorRGBA_F(1.0F, 1.0F, 1.0F, f2);
			this.renderEastFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(0));
			tessellator4.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

	}
	
	public void renderBlockOnInventory(Block block1, int i2, float f3) {
		Tessellator tessellator4 = Tessellator.instance;
		int i5;
		float f6;
		float f7;
		if(this.useInventoryTint) {
			i5 = block1.getRenderColor(i2);
			f6 = (float)(i5 >> 16 & 255) / 255.0F;
			f7 = (float)(i5 >> 8 & 255) / 255.0F;
			float f8 = (float)(i5 & 255) / 255.0F;
			GL11.glColor4f(f6 * f3, f7 * f3, f8 * f3, 1.0F);
		}

		i5 = block1.getRenderType();
		
		// Patch for blocks which can be rendered as cubes in the inventory
		if(i5 >= 103 && i5 <= 108 && i5 != 104) i5 = 0;
		if(i5 == 255) i5 = 0;
		
		if(i5 == 250) {
			RenderBlockModel.renderBlockAsItem(tessellator4, block1, i2);
		} else if(i5 != 0 && i5 != 16) {
			if(i5 == 1 || i5 == 111) {
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderCrossedSquares(block1, i2, -0.5D, -0.5D, -0.5D);
				tessellator4.draw();
			} else if(i5 == 13 || i5 == 104) {
				block1.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				f6 = 0.0625F;
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBottomFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(0));
				tessellator4.draw();
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(0.0F, 1.0F, 0.0F);
				this.renderTopFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(1));
				tessellator4.draw();
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(0.0F, 0.0F, -1.0F);
				tessellator4.addTranslation(0.0F, 0.0F, f6);
				this.renderEastFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(2));
				tessellator4.addTranslation(0.0F, 0.0F, -f6);
				tessellator4.draw();
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(0.0F, 0.0F, 1.0F);
				tessellator4.addTranslation(0.0F, 0.0F, -f6);
				this.renderWestFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(3));
				tessellator4.addTranslation(0.0F, 0.0F, f6);
				tessellator4.draw();
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator4.addTranslation(f6, 0.0F, 0.0F);
				this.renderNorthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(4));
				tessellator4.addTranslation(-f6, 0.0F, 0.0F);
				tessellator4.draw();
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(1.0F, 0.0F, 0.0F);
				tessellator4.addTranslation(-f6, 0.0F, 0.0F);
				this.renderSouthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(5));
				tessellator4.addTranslation(f6, 0.0F, 0.0F);
				tessellator4.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if(i5 == 6) {
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBlockCropsImpl(block1, i2, -0.5D, -0.5D, -0.5D);
				tessellator4.draw();
			} else if(i5 == 2) {
				tessellator4.startDrawingQuads();
				tessellator4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderTorchAtAngle(block1, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
				tessellator4.draw();
			} else if(i5 == 102) {
				block1.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				tessellator4.startDrawingQuads();
				RenderBlockHollowLog.renderAsItem(this.blockAccess, block1, block1.getBlockTextureFromSide(0), block1.getBlockTextureFromSide(1), block1.getBlockTextureFromSide(2), tessellator4, 1.0F);
				tessellator4.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if(i5 == 109) {
				RenderBlockClassicPiston.RenderInvBlock(this, block1, 0, 0);
			} else {
				int i9;
				if(i5 == 10) {
					for(i9 = 0; i9 < 2; ++i9) {
						if(i9 == 0) {
							block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
						}

						if(i9 == 1) {
							block1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
						}

						GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(0.0F, -1.0F, 0.0F);
						this.renderBottomFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(0));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(0.0F, 1.0F, 0.0F);
						this.renderTopFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(1));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(0.0F, 0.0F, -1.0F);
						this.renderEastFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(2));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(0.0F, 0.0F, 1.0F);
						this.renderWestFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(3));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(-1.0F, 0.0F, 0.0F);
						this.renderNorthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(4));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(1.0F, 0.0F, 0.0F);
						this.renderSouthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(5));
						tessellator4.draw();
						GL11.glTranslatef(0.5F, 0.5F, 0.5F);
					}
				} else if(i5 == 11) {
					for(i9 = 0; i9 < 4; ++i9) {
						f7 = 0.125F;
						if(i9 == 0) {
							block1.setBlockBounds(0.5F - f7, 0.0F, 0.0F, 0.5F + f7, 1.0F, f7 * 2.0F);
						}

						if(i9 == 1) {
							block1.setBlockBounds(0.5F - f7, 0.0F, 1.0F - f7 * 2.0F, 0.5F + f7, 1.0F, 1.0F);
						}

						f7 = 0.0625F;
						if(i9 == 2) {
							block1.setBlockBounds(0.5F - f7, 1.0F - f7 * 3.0F, -f7 * 2.0F, 0.5F + f7, 1.0F - f7, 1.0F + f7 * 2.0F);
						}

						if(i9 == 3) {
							block1.setBlockBounds(0.5F - f7, 0.5F - f7 * 3.0F, -f7 * 2.0F, 0.5F + f7, 0.5F - f7, 1.0F + f7 * 2.0F);
						}

						GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(0.0F, -1.0F, 0.0F);
						this.renderBottomFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(0));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(0.0F, 1.0F, 0.0F);
						this.renderTopFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(1));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(0.0F, 0.0F, -1.0F);
						this.renderEastFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(2));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(0.0F, 0.0F, 1.0F);
						this.renderWestFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(3));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(-1.0F, 0.0F, 0.0F);
						this.renderNorthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(4));
						tessellator4.draw();
						tessellator4.startDrawingQuads();
						tessellator4.setNormal(1.0F, 0.0F, 0.0F);
						this.renderSouthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(5));
						tessellator4.draw();
						GL11.glTranslatef(0.5F, 0.5F, 0.5F);
					}

					block1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				}
			}
		} else {
			if(i5 == 16) {
				i2 = 1;
			}

			block1.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator4.startDrawingQuads();
			tessellator4.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSideAndMetadata(0, i2));
			tessellator4.draw();
			tessellator4.startDrawingQuads();
			tessellator4.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSideAndMetadata(1, i2));
			tessellator4.draw();
			tessellator4.startDrawingQuads();
			tessellator4.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSideAndMetadata(2, i2));
			tessellator4.draw();
			tessellator4.startDrawingQuads();
			tessellator4.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSideAndMetadata(3, i2));
			tessellator4.draw();
			tessellator4.startDrawingQuads();
			tessellator4.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSideAndMetadata(4, i2));
			tessellator4.draw();
			tessellator4.startDrawingQuads();
			tessellator4.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSideAndMetadata(5, i2));
			tessellator4.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

	}

	public static boolean renderItemIn3d(int i0) {
		//return i0 == 0 ? true : (i0 == 13 ? true : (i0 == 10 ? true : (i0 == 11 ? true : i0 == 16)));
		return (i0 == 0 || i0 == 13 || i0 == 10 || i0 == 11 || i0 == 16 || (i0 >= 102 && i0 <= 109) || i0 == 112 || i0 == 255 || i0 == 250);
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
