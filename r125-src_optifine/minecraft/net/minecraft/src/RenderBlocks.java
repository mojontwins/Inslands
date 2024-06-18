package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderBlocks {
	public IBlockAccess blockAccess;
	public int overrideBlockTexture = -1;
	public boolean flipTexture = false;
	public boolean renderAllFaces = false;
	public static boolean fancyGrass = true;
	public static boolean cfgGrassFix = true;
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
	public boolean aoLightValuesCalculated;
	public float aoLightValueOpaque = 0.2F;
	public static float[][] redstoneColors = new float[16][];

	public RenderBlocks(IBlockAccess par1IBlockAccess) {
		this.blockAccess = par1IBlockAccess;
		this.aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
	}

	public RenderBlocks() {
	}

	public void clearOverrideBlockTexture() {
		this.overrideBlockTexture = -1;
	}

	public void renderBlockUsingTexture(Block par1Block, int par2, int par3, int par4, int par5) {
		this.overrideBlockTexture = par5;
		this.renderBlockByRenderType(par1Block, par2, par3, par4);
		this.overrideBlockTexture = -1;
	}

	public void renderBlockAllFaces(Block par1Block, int par2, int par3, int par4) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(par1Block, par2, par3, par4);
		this.renderAllFaces = false;
	}

	public boolean renderBlockByRenderType(Block par1Block, int par2, int par3, int par4) {
		int i = par1Block.getRenderType();
		par1Block.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
		if(Config.isBetterSnow() && par1Block == Block.signPost && this.hasSnowNeighbours(par2, par3, par4)) {
			this.renderStandardBlock(Block.snow, par2, par3, par4);
		}

		return i == 0 ? this.renderStandardBlock(par1Block, par2, par3, par4) : (i == 4 ? this.renderBlockFluids(par1Block, par2, par3, par4) : (i == 13 ? this.renderBlockCactus(par1Block, par2, par3, par4) : (i == 1 ? this.renderCrossedSquares(par1Block, par2, par3, par4) : (i == 19 ? this.renderBlockStem(par1Block, par2, par3, par4) : (i == 23 ? this.renderBlockLilyPad(par1Block, par2, par3, par4) : (i == 6 ? this.renderBlockCrops(par1Block, par2, par3, par4) : (i == 2 ? this.renderBlockTorch(par1Block, par2, par3, par4) : (i == 3 ? this.renderBlockFire(par1Block, par2, par3, par4) : (i == 5 ? this.renderBlockRedstoneWire(par1Block, par2, par3, par4) : (i == 8 ? this.renderBlockLadder(par1Block, par2, par3, par4) : (i == 7 ? this.renderBlockDoor(par1Block, par2, par3, par4) : (i == 9 ? this.renderBlockMinecartTrack((BlockRail)par1Block, par2, par3, par4) : (i == 10 ? this.renderBlockStairs(par1Block, par2, par3, par4) : (i == 27 ? this.renderBlockDragonEgg((BlockDragonEgg)par1Block, par2, par3, par4) : (i == 11 ? this.renderBlockFence((BlockFence)par1Block, par2, par3, par4) : (i == 12 ? this.renderBlockLever(par1Block, par2, par3, par4) : (i == 14 ? this.renderBlockBed(par1Block, par2, par3, par4) : (i == 15 ? this.renderBlockRepeater(par1Block, par2, par3, par4) : (i == 16 ? this.renderPistonBase(par1Block, par2, par3, par4, false) : (i == 17 ? this.renderPistonExtension(par1Block, par2, par3, par4, true) : (i == 18 ? this.renderBlockPane((BlockPane)par1Block, par2, par3, par4) : (i == 20 ? this.renderBlockVine(par1Block, par2, par3, par4) : (i == 21 ? this.renderBlockFenceGate((BlockFenceGate)par1Block, par2, par3, par4) : (i == 24 ? this.renderBlockCauldron((BlockCauldron)par1Block, par2, par3, par4) : (i == 25 ? this.renderBlockBrewingStand((BlockBrewingStand)par1Block, par2, par3, par4) : (i == 26 ? this.renderBlockEndPortalFrame(par1Block, par2, par3, par4) : (Reflector.hasClass(0) ? Reflector.callBoolean(0, new Object[]{this, this.blockAccess, par2, par3, par4, par1Block, i}) : false)))))))))))))))))))))))))));
	}

	private boolean hasSnowNeighbours(int x, int y, int z) {
		int snowId = Block.snow.blockID;
		return this.blockAccess.getBlockId(x - 1, y, z) != snowId && this.blockAccess.getBlockId(x + 1, y, z) != snowId && this.blockAccess.getBlockId(x, y, z - 1) != snowId && this.blockAccess.getBlockId(x, y, z + 1) != snowId ? false : this.blockAccess.isBlockOpaqueCube(x, y - 1, z);
	}

	public boolean renderBlockEndPortalFrame(Block par1Block, int par2, int par3, int par4) {
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int j = i & 3;
		if(j == 0) {
			this.uvRotateTop = 3;
		} else if(j == 3) {
			this.uvRotateTop = 1;
		} else if(j == 1) {
			this.uvRotateTop = 2;
		}

		if(!BlockEndPortalFrame.isEnderEyeInserted(i)) {
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			par1Block.setBlockBoundsForItemRender();
			this.uvRotateTop = 0;
			return true;
		} else {
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.overrideBlockTexture = 174;
			par1Block.setBlockBounds(0.25F, 0.8125F, 0.25F, 0.75F, 1.0F, 0.75F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.clearOverrideBlockTexture();
			par1Block.setBlockBoundsForItemRender();
			this.uvRotateTop = 0;
			return true;
		}
	}

	public boolean renderBlockBed(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int j = BlockBed.getDirection(i);
		boolean flag = BlockBed.isBlockFootOfBed(i);
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		int k = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		tessellator.setBrightness(k);
		tessellator.setColorOpaque_F(f, f, f);
		int l = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0);
		int i1 = (l & 15) << 4;
		int j1 = l & 240;
		double d = (double)((float)i1 / 256.0F);
		double d1 = ((double)(i1 + 16) - 0.01D) / 256.0D;
		double d2 = (double)((float)j1 / 256.0F);
		double d3 = ((double)(j1 + 16) - 0.01D) / 256.0D;
		double d4 = (double)par2 + par1Block.minX;
		double d5 = (double)par2 + par1Block.maxX;
		double d6 = (double)par3 + par1Block.minY + 0.1875D;
		double d7 = (double)par4 + par1Block.minZ;
		double d8 = (double)par4 + par1Block.maxZ;
		tessellator.addVertexWithUV(d4, d6, d8, d, d3);
		tessellator.addVertexWithUV(d4, d6, d7, d, d2);
		tessellator.addVertexWithUV(d5, d6, d7, d1, d2);
		tessellator.addVertexWithUV(d5, d6, d8, d1, d3);
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
		tessellator.setColorOpaque_F(f1, f1, f1);
		l = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1);
		i1 = (l & 15) << 4;
		j1 = l & 240;
		d = (double)((float)i1 / 256.0F);
		d1 = ((double)(i1 + 16) - 0.01D) / 256.0D;
		d2 = (double)((float)j1 / 256.0F);
		d3 = ((double)(j1 + 16) - 0.01D) / 256.0D;
		d4 = d;
		d5 = d1;
		d6 = d2;
		d7 = d2;
		d8 = d;
		double d9 = d1;
		double d10 = d3;
		double d11 = d3;
		if(j == 0) {
			d5 = d;
			d6 = d3;
			d8 = d1;
			d11 = d2;
		} else if(j == 2) {
			d4 = d1;
			d7 = d3;
			d9 = d;
			d10 = d2;
		} else if(j == 3) {
			d4 = d1;
			d7 = d3;
			d9 = d;
			d10 = d2;
			d5 = d;
			d6 = d3;
			d8 = d1;
			d11 = d2;
		}

		double d12 = (double)par2 + par1Block.minX;
		double d13 = (double)par2 + par1Block.maxX;
		double d14 = (double)par3 + par1Block.maxY;
		double d15 = (double)par4 + par1Block.minZ;
		double d16 = (double)par4 + par1Block.maxZ;
		tessellator.addVertexWithUV(d13, d14, d16, d8, d10);
		tessellator.addVertexWithUV(d13, d14, d15, d4, d6);
		tessellator.addVertexWithUV(d12, d14, d15, d5, d7);
		tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
		l = Direction.headInvisibleFace[j];
		if(flag) {
			l = Direction.headInvisibleFace[Direction.footInvisibleFaceRemap[j]];
		}

		byte i11 = 4;
		switch(j) {
		case 0:
			i11 = 5;
			break;
		case 1:
			i11 = 3;
		case 2:
		default:
			break;
		case 3:
			i11 = 2;
		}

		if(l != 2 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2))) {
			tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1) : k);
			tessellator.setColorOpaque_F(f2, f2, f2);
			this.flipTexture = i11 == 2;
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2));
		}

		if(l != 3 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3))) {
			tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1) : k);
			tessellator.setColorOpaque_F(f2, f2, f2);
			this.flipTexture = i11 == 3;
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3));
		}

		if(l != 4 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4))) {
			tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4) : k);
			tessellator.setColorOpaque_F(f3, f3, f3);
			this.flipTexture = i11 == 4;
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4));
		}

		if(l != 5 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5))) {
			tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4) : k);
			tessellator.setColorOpaque_F(f3, f3, f3);
			this.flipTexture = i11 == 5;
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5));
		}

		this.flipTexture = false;
		return true;
	}

	public boolean renderBlockBrewingStand(BlockBrewingStand par1BlockBrewingStand, int par2, int par3, int par4) {
		par1BlockBrewingStand.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		this.overrideBlockTexture = 156;
		par1BlockBrewingStand.setBlockBounds(0.5625F, 0.0F, 0.3125F, 0.9375F, 0.125F, 0.6875F);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		par1BlockBrewingStand.setBlockBounds(0.125F, 0.0F, 0.0625F, 0.5F, 0.125F, 0.4375F);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		par1BlockBrewingStand.setBlockBounds(0.125F, 0.0F, 0.5625F, 0.5F, 0.125F, 0.9375F);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		this.clearOverrideBlockTexture();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(par1BlockBrewingStand.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f = 1.0F;
		int i = par1BlockBrewingStand.colorMultiplier(this.blockAccess, par2, par3, par4);
		float f1 = (float)(i >> 16 & 255) / 255.0F;
		float f2 = (float)(i >> 8 & 255) / 255.0F;
		float f3 = (float)(i & 255) / 255.0F;
		if(EntityRenderer.anaglyphEnable) {
			float j = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float k = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float l = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = j;
			f2 = k;
			f3 = l;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		int i34 = par1BlockBrewingStand.getBlockTextureFromSideAndMetadata(0, 0);
		if(this.overrideBlockTexture >= 0) {
			i34 = this.overrideBlockTexture;
		}

		int i35 = (i34 & 15) << 4;
		int i36 = i34 & 240;
		double d = (double)((float)i36 / 256.0F);
		double d1 = (double)(((float)i36 + 15.99F) / 256.0F);
		int i1 = this.blockAccess.getBlockMetadata(par2, par3, par4);

		for(int j1 = 0; j1 < 3; ++j1) {
			double d2 = (double)j1 * Math.PI * 2.0D / 3.0D + Math.PI / 2D;
			double d3 = (double)(((float)i35 + 8.0F) / 256.0F);
			double d4 = (double)(((float)i35 + 15.99F) / 256.0F);
			if((i1 & 1 << j1) != 0) {
				d3 = (double)(((float)i35 + 7.99F) / 256.0F);
				d4 = (double)(((float)i35 + 0.0F) / 256.0F);
			}

			double d5 = (double)par2 + 0.5D;
			double d6 = (double)par2 + 0.5D + Math.sin(d2) * 8.0D / 16.0D;
			double d7 = (double)par4 + 0.5D;
			double d8 = (double)par4 + 0.5D + Math.cos(d2) * 8.0D / 16.0D;
			tessellator.addVertexWithUV(d5, (double)(par3 + 1), d7, d3, d);
			tessellator.addVertexWithUV(d5, (double)(par3 + 0), d7, d3, d1);
			tessellator.addVertexWithUV(d6, (double)(par3 + 0), d8, d4, d1);
			tessellator.addVertexWithUV(d6, (double)(par3 + 1), d8, d4, d);
			tessellator.addVertexWithUV(d6, (double)(par3 + 1), d8, d4, d);
			tessellator.addVertexWithUV(d6, (double)(par3 + 0), d8, d4, d1);
			tessellator.addVertexWithUV(d5, (double)(par3 + 0), d7, d3, d1);
			tessellator.addVertexWithUV(d5, (double)(par3 + 1), d7, d3, d);
		}

		par1BlockBrewingStand.setBlockBoundsForItemRender();
		return true;
	}

	public boolean renderBlockCauldron(BlockCauldron par1BlockCauldron, int par2, int par3, int par4) {
		this.renderStandardBlock(par1BlockCauldron, par2, par3, par4);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(par1BlockCauldron.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f = 1.0F;
		int i = par1BlockCauldron.colorMultiplier(this.blockAccess, par2, par3, par4);
		float f1 = (float)(i >> 16 & 255) / 255.0F;
		float f2 = (float)(i >> 8 & 255) / 255.0F;
		float f3 = (float)(i & 255) / 255.0F;
		float f6;
		if(EntityRenderer.anaglyphEnable) {
			float c = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			f6 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float c1 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = c;
			f2 = f6;
			f3 = c1;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		short c1 = 154;
		f6 = 0.125F;
		this.renderSouthFace(par1BlockCauldron, (double)((float)par2 - 1.0F + f6), (double)par3, (double)par4, c1);
		this.renderNorthFace(par1BlockCauldron, (double)((float)par2 + 1.0F - f6), (double)par3, (double)par4, c1);
		this.renderWestFace(par1BlockCauldron, (double)par2, (double)par3, (double)((float)par4 - 1.0F + f6), c1);
		this.renderEastFace(par1BlockCauldron, (double)par2, (double)par3, (double)((float)par4 + 1.0F - f6), c1);
		short c11 = 139;
		this.renderTopFace(par1BlockCauldron, (double)par2, (double)((float)par3 - 1.0F + 0.25F), (double)par4, c11);
		this.renderBottomFace(par1BlockCauldron, (double)par2, (double)((float)par3 + 1.0F - 0.75F), (double)par4, c11);
		int j = this.blockAccess.getBlockMetadata(par2, par3, par4);
		if(j > 0) {
			short c2 = 205;
			if(j > 3) {
				j = 3;
			}

			int wc = CustomColorizer.getFluidColor(Block.waterStill, this.blockAccess, par2, par3, par4);
			float wr = (float)(wc >> 16 & 255) / 255.0F;
			float wg = (float)(wc >> 8 & 255) / 255.0F;
			float wb = (float)(wc & 255) / 255.0F;
			tessellator.setColorOpaque_F(wr, wg, wb);
			this.renderTopFace(par1BlockCauldron, (double)par2, (double)((float)par3 - 1.0F + (6.0F + (float)j * 3.0F) / 16.0F), (double)par4, c2);
		}

		return true;
	}

	public boolean renderBlockTorch(Block par1Block, int par2, int par3, int par4) {
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d = (double)0.4F;
		double d1 = 0.5D - d;
		double d2 = (double)0.2F;
		if(i == 1) {
			this.renderTorchAtAngle(par1Block, (double)par2 - d1, (double)par3 + d2, (double)par4, -d, 0.0D);
		} else if(i == 2) {
			this.renderTorchAtAngle(par1Block, (double)par2 + d1, (double)par3 + d2, (double)par4, d, 0.0D);
		} else if(i == 3) {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3 + d2, (double)par4 - d1, 0.0D, -d);
		} else if(i == 4) {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3 + d2, (double)par4 + d1, 0.0D, d);
		} else {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3, (double)par4, 0.0D, 0.0D);
			if(par1Block != Block.torchWood && Config.isBetterSnow() && this.hasSnowNeighbours(par2, par3, par4)) {
				this.renderStandardBlock(Block.snow, par2, par3, par4);
			}
		}

		return true;
	}

	public boolean renderBlockRepeater(Block par1Block, int par2, int par3, int par4) {
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int j = i & 3;
		int k = (i & 12) >> 2;
		this.renderStandardBlock(par1Block, par2, par3, par4);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d = -0.1875D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		double d3 = 0.0D;
		double d4 = 0.0D;
		switch(j) {
		case 0:
			d4 = -0.3125D;
			d2 = BlockRedstoneRepeater.repeaterTorchOffset[k];
			break;
		case 1:
			d3 = 0.3125D;
			d1 = -BlockRedstoneRepeater.repeaterTorchOffset[k];
			break;
		case 2:
			d4 = 0.3125D;
			d2 = -BlockRedstoneRepeater.repeaterTorchOffset[k];
			break;
		case 3:
			d3 = -0.3125D;
			d1 = BlockRedstoneRepeater.repeaterTorchOffset[k];
		}

		this.renderTorchAtAngle(par1Block, (double)par2 + d1, (double)par3 + d, (double)par4 + d2, 0.0D, 0.0D);
		this.renderTorchAtAngle(par1Block, (double)par2 + d3, (double)par3 + d, (double)par4 + d4, 0.0D, 0.0D);
		int l = par1Block.getBlockTextureFromSide(1);
		int i1 = (l & 15) << 4;
		int j1 = l & 240;
		double d5 = (double)((float)i1 / 256.0F);
		double d6 = (double)(((float)i1 + 15.99F) / 256.0F);
		double d7 = (double)((float)j1 / 256.0F);
		double d8 = (double)(((float)j1 + 15.99F) / 256.0F);
		double d9 = 0.125D;
		double d10 = (double)(par2 + 1);
		double d11 = (double)(par2 + 1);
		double d12 = (double)(par2 + 0);
		double d13 = (double)(par2 + 0);
		double d14 = (double)(par4 + 0);
		double d15 = (double)(par4 + 1);
		double d16 = (double)(par4 + 1);
		double d17 = (double)(par4 + 0);
		double d18 = (double)par3 + d9;
		if(j == 2) {
			d10 = d11 = (double)(par2 + 0);
			d12 = d13 = (double)(par2 + 1);
			d14 = d17 = (double)(par4 + 1);
			d15 = d16 = (double)(par4 + 0);
		} else if(j == 3) {
			d10 = d13 = (double)(par2 + 0);
			d11 = d12 = (double)(par2 + 1);
			d14 = d15 = (double)(par4 + 0);
			d16 = d17 = (double)(par4 + 1);
		} else if(j == 1) {
			d10 = d13 = (double)(par2 + 1);
			d11 = d12 = (double)(par2 + 0);
			d14 = d15 = (double)(par4 + 1);
			d16 = d17 = (double)(par4 + 0);
		}

		tessellator.addVertexWithUV(d13, d18, d17, d5, d7);
		tessellator.addVertexWithUV(d12, d18, d16, d5, d8);
		tessellator.addVertexWithUV(d11, d18, d15, d6, d8);
		tessellator.addVertexWithUV(d10, d18, d14, d6, d7);
		return true;
	}

	public void renderPistonBaseAllFaces(Block par1Block, int par2, int par3, int par4) {
		this.renderAllFaces = true;
		this.renderPistonBase(par1Block, par2, par3, par4, true);
		this.renderAllFaces = false;
	}

	public boolean renderPistonBase(Block par1Block, int par2, int par3, int par4, boolean par5) {
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		boolean flag = par5 || (i & 8) != 0;
		int j = BlockPistonBase.getOrientation(i);
		if(flag) {
			switch(j) {
			case 0:
				this.uvRotateEast = 3;
				this.uvRotateWest = 3;
				this.uvRotateSouth = 3;
				this.uvRotateNorth = 3;
				par1Block.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 1:
				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
				break;
			case 2:
				this.uvRotateSouth = 1;
				this.uvRotateNorth = 2;
				par1Block.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;
			case 3:
				this.uvRotateSouth = 2;
				this.uvRotateNorth = 1;
				this.uvRotateTop = 3;
				this.uvRotateBottom = 3;
				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
				break;
			case 4:
				this.uvRotateEast = 1;
				this.uvRotateWest = 2;
				this.uvRotateTop = 2;
				this.uvRotateBottom = 1;
				par1Block.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 5:
				this.uvRotateEast = 2;
				this.uvRotateWest = 1;
				this.uvRotateTop = 1;
				this.uvRotateBottom = 2;
				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
			}

			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.uvRotateEast = 0;
			this.uvRotateWest = 0;
			this.uvRotateSouth = 0;
			this.uvRotateNorth = 0;
			this.uvRotateTop = 0;
			this.uvRotateBottom = 0;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			switch(j) {
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

			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.uvRotateEast = 0;
			this.uvRotateWest = 0;
			this.uvRotateSouth = 0;
			this.uvRotateNorth = 0;
			this.uvRotateTop = 0;
			this.uvRotateBottom = 0;
		}

		return true;
	}

	public void renderPistonRodUD(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		int i = 108;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		Tessellator tessellator = Tessellator.instance;
		double d = (double)((float)(j + 0) / 256.0F);
		double d1 = (double)((float)(k + 0) / 256.0F);
		double d2 = ((double)j + par14 - 0.01D) / 256.0D;
		double d3 = ((double)((float)k + 4.0F) - 0.01D) / 256.0D;
		tessellator.setColorOpaque_F(par13, par13, par13);
		tessellator.addVertexWithUV(par1, par7, par9, d2, d1);
		tessellator.addVertexWithUV(par1, par5, par9, d, d1);
		tessellator.addVertexWithUV(par3, par5, par11, d, d3);
		tessellator.addVertexWithUV(par3, par7, par11, d2, d3);
	}

	public void renderPistonRodSN(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		int i = 108;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		Tessellator tessellator = Tessellator.instance;
		double d = (double)((float)(j + 0) / 256.0F);
		double d1 = (double)((float)(k + 0) / 256.0F);
		double d2 = ((double)j + par14 - 0.01D) / 256.0D;
		double d3 = ((double)((float)k + 4.0F) - 0.01D) / 256.0D;
		tessellator.setColorOpaque_F(par13, par13, par13);
		tessellator.addVertexWithUV(par1, par5, par11, d2, d1);
		tessellator.addVertexWithUV(par1, par5, par9, d, d1);
		tessellator.addVertexWithUV(par3, par7, par9, d, d3);
		tessellator.addVertexWithUV(par3, par7, par11, d2, d3);
	}

	public void renderPistonRodEW(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		int i = 108;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		Tessellator tessellator = Tessellator.instance;
		double d = (double)((float)(j + 0) / 256.0F);
		double d1 = (double)((float)(k + 0) / 256.0F);
		double d2 = ((double)j + par14 - 0.01D) / 256.0D;
		double d3 = ((double)((float)k + 4.0F) - 0.01D) / 256.0D;
		tessellator.setColorOpaque_F(par13, par13, par13);
		tessellator.addVertexWithUV(par3, par5, par9, d2, d1);
		tessellator.addVertexWithUV(par1, par5, par9, d, d1);
		tessellator.addVertexWithUV(par1, par7, par11, d, d3);
		tessellator.addVertexWithUV(par3, par7, par11, d2, d3);
	}

	public void renderPistonExtensionAllFaces(Block par1Block, int par2, int par3, int par4, boolean par5) {
		this.renderAllFaces = true;
		this.renderPistonExtension(par1Block, par2, par3, par4, par5);
		this.renderAllFaces = false;
	}

	public boolean renderPistonExtension(Block par1Block, int par2, int par3, int par4, boolean par5) {
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int j = BlockPistonExtension.getDirectionMeta(i);
		float f = par1Block.getBlockBrightness(this.blockAccess, par2, par3, par4);
		float f1 = par5 ? 1.0F : 0.5F;
		double d = par5 ? 16.0D : 8.0D;
		switch(j) {
		case 0:
			this.uvRotateEast = 3;
			this.uvRotateWest = 3;
			this.uvRotateSouth = 3;
			this.uvRotateNorth = 3;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + f1), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), f * 0.8F, d);
			this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + f1), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), f * 0.8F, d);
			this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + f1), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), f * 0.6F, d);
			this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + f1), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), f * 0.6F, d);
			break;
		case 1:
			par1Block.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 - 0.25F + 1.0F - f1), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), f * 0.8F, d);
			this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 - 0.25F + 1.0F - f1), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), f * 0.8F, d);
			this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 - 0.25F + 1.0F - f1), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), f * 0.6F, d);
			this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 - 0.25F + 1.0F - f1), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), f * 0.6F, d);
			break;
		case 2:
			this.uvRotateSouth = 1;
			this.uvRotateNorth = 2;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodSN((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.25F), (double)((float)par4 + 0.25F + f1), f * 0.6F, d);
			this.renderPistonRodSN((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.25F), (double)((float)par4 + 0.25F + f1), f * 0.6F, d);
			this.renderPistonRodSN((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.25F), (double)((float)par4 + 0.25F + f1), f * 0.5F, d);
			this.renderPistonRodSN((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.25F), (double)((float)par4 + 0.25F + f1), f, d);
			break;
		case 3:
			this.uvRotateSouth = 2;
			this.uvRotateNorth = 1;
			this.uvRotateTop = 3;
			this.uvRotateBottom = 3;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodSN((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par4 - 0.25F + 1.0F - f1), (double)((float)par4 - 0.25F + 1.0F), f * 0.6F, d);
			this.renderPistonRodSN((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par4 - 0.25F + 1.0F - f1), (double)((float)par4 - 0.25F + 1.0F), f * 0.6F, d);
			this.renderPistonRodSN((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.375F), (double)((float)par4 - 0.25F + 1.0F - f1), (double)((float)par4 - 0.25F + 1.0F), f * 0.5F, d);
			this.renderPistonRodSN((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.625F), (double)((float)par4 - 0.25F + 1.0F - f1), (double)((float)par4 - 0.25F + 1.0F), f, d);
			break;
		case 4:
			this.uvRotateEast = 1;
			this.uvRotateWest = 2;
			this.uvRotateTop = 2;
			this.uvRotateBottom = 1;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodEW((double)((float)par2 + 0.25F), (double)((float)par2 + 0.25F + f1), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), f * 0.5F, d);
			this.renderPistonRodEW((double)((float)par2 + 0.25F), (double)((float)par2 + 0.25F + f1), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), f, d);
			this.renderPistonRodEW((double)((float)par2 + 0.25F), (double)((float)par2 + 0.25F + f1), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), f * 0.6F, d);
			this.renderPistonRodEW((double)((float)par2 + 0.25F), (double)((float)par2 + 0.25F + f1), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), f * 0.6F, d);
			break;
		case 5:
			this.uvRotateEast = 2;
			this.uvRotateWest = 1;
			this.uvRotateTop = 1;
			this.uvRotateBottom = 2;
			par1Block.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodEW((double)((float)par2 - 0.25F + 1.0F - f1), (double)((float)par2 - 0.25F + 1.0F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), f * 0.5F, d);
			this.renderPistonRodEW((double)((float)par2 - 0.25F + 1.0F - f1), (double)((float)par2 - 0.25F + 1.0F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), f, d);
			this.renderPistonRodEW((double)((float)par2 - 0.25F + 1.0F - f1), (double)((float)par2 - 0.25F + 1.0F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), f * 0.6F, d);
			this.renderPistonRodEW((double)((float)par2 - 0.25F + 1.0F - f1), (double)((float)par2 - 0.25F + 1.0F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), f * 0.6F, d);
		}

		this.uvRotateEast = 0;
		this.uvRotateWest = 0;
		this.uvRotateSouth = 0;
		this.uvRotateNorth = 0;
		this.uvRotateTop = 0;
		this.uvRotateBottom = 0;
		par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockLever(Block par1Block, int par2, int par3, int par4) {
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int j = i & 7;
		boolean flag = (i & 8) > 0;
		Tessellator tessellator = Tessellator.instance;
		boolean flag1 = this.overrideBlockTexture >= 0;
		if(!flag1) {
			this.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
		}

		float f = 0.25F;
		float f1 = 0.1875F;
		float f2 = 0.1875F;
		if(j == 5) {
			par1Block.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
		} else if(j == 6) {
			par1Block.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, f2, 0.5F + f1);
		} else if(j == 4) {
			par1Block.setBlockBounds(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0F);
		} else if(j == 3) {
			par1Block.setBlockBounds(0.5F - f1, 0.5F - f, 0.0F, 0.5F + f1, 0.5F + f, f2);
		} else if(j == 2) {
			par1Block.setBlockBounds(1.0F - f2, 0.5F - f, 0.5F - f1, 1.0F, 0.5F + f, 0.5F + f1);
		} else if(j == 1) {
			par1Block.setBlockBounds(0.0F, 0.5F - f, 0.5F - f1, f2, 0.5F + f, 0.5F + f1);
		}

		this.renderStandardBlock(par1Block, par2, par3, par4);
		if(!flag1) {
			this.overrideBlockTexture = -1;
		}

		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f3 = 1.0F;
		if(Block.lightValue[par1Block.blockID] > 0) {
			f3 = 1.0F;
		}

		tessellator.setColorOpaque_F(f3, f3, f3);
		int k = par1Block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			k = this.overrideBlockTexture;
		}

		int l = (k & 15) << 4;
		int i1 = k & 240;
		float f4 = (float)l / 256.0F;
		float f5 = ((float)l + 15.99F) / 256.0F;
		float f6 = (float)i1 / 256.0F;
		float f7 = ((float)i1 + 15.99F) / 256.0F;
		Vec3D[] avec3d = new Vec3D[8];
		float f8 = 0.0625F;
		float f9 = 0.0625F;
		float f10 = 0.625F;
		avec3d[0] = Vec3D.createVector((double)(-f8), 0.0D, (double)(-f9));
		avec3d[1] = Vec3D.createVector((double)f8, 0.0D, (double)(-f9));
		avec3d[2] = Vec3D.createVector((double)f8, 0.0D, (double)f9);
		avec3d[3] = Vec3D.createVector((double)(-f8), 0.0D, (double)f9);
		avec3d[4] = Vec3D.createVector((double)(-f8), (double)f10, (double)(-f9));
		avec3d[5] = Vec3D.createVector((double)f8, (double)f10, (double)(-f9));
		avec3d[6] = Vec3D.createVector((double)f8, (double)f10, (double)f9);
		avec3d[7] = Vec3D.createVector((double)(-f8), (double)f10, (double)f9);

		for(int vec3d = 0; vec3d < 8; ++vec3d) {
			if(flag) {
				avec3d[vec3d].zCoord -= 0.0625D;
				avec3d[vec3d].rotateAroundX((float)Math.PI / 4.5F);
			} else {
				avec3d[vec3d].zCoord += 0.0625D;
				avec3d[vec3d].rotateAroundX(-0.69813174F);
			}

			if(j == 6) {
				avec3d[vec3d].rotateAroundY((float)Math.PI / 2F);
			}

			if(j < 5) {
				avec3d[vec3d].yCoord -= 0.375D;
				avec3d[vec3d].rotateAroundX((float)Math.PI / 2F);
				if(j == 4) {
					avec3d[vec3d].rotateAroundY(0.0F);
				}

				if(j == 3) {
					avec3d[vec3d].rotateAroundY((float)Math.PI);
				}

				if(j == 2) {
					avec3d[vec3d].rotateAroundY((float)Math.PI / 2F);
				}

				if(j == 1) {
					avec3d[vec3d].rotateAroundY(-1.5707964F);
				}

				avec3d[vec3d].xCoord += (double)par2 + 0.5D;
				avec3d[vec3d].yCoord += (double)((float)par3 + 0.5F);
				avec3d[vec3d].zCoord += (double)par4 + 0.5D;
			} else {
				avec3d[vec3d].xCoord += (double)par2 + 0.5D;
				avec3d[vec3d].yCoord += (double)((float)par3 + 0.125F);
				avec3d[vec3d].zCoord += (double)par4 + 0.5D;
			}
		}

		Vec3D vec3D30 = null;
		Vec3D vec3d1 = null;
		Vec3D vec3d2 = null;
		Vec3D vec3d3 = null;

		for(int k1 = 0; k1 < 6; ++k1) {
			if(k1 == 0) {
				f4 = (float)(l + 7) / 256.0F;
				f5 = ((float)(l + 9) - 0.01F) / 256.0F;
				f6 = (float)(i1 + 6) / 256.0F;
				f7 = ((float)(i1 + 8) - 0.01F) / 256.0F;
			} else if(k1 == 2) {
				f4 = (float)(l + 7) / 256.0F;
				f5 = ((float)(l + 9) - 0.01F) / 256.0F;
				f6 = (float)(i1 + 6) / 256.0F;
				f7 = ((float)(i1 + 16) - 0.01F) / 256.0F;
			}

			if(k1 == 0) {
				vec3D30 = avec3d[0];
				vec3d1 = avec3d[1];
				vec3d2 = avec3d[2];
				vec3d3 = avec3d[3];
			} else if(k1 == 1) {
				vec3D30 = avec3d[7];
				vec3d1 = avec3d[6];
				vec3d2 = avec3d[5];
				vec3d3 = avec3d[4];
			} else if(k1 == 2) {
				vec3D30 = avec3d[1];
				vec3d1 = avec3d[0];
				vec3d2 = avec3d[4];
				vec3d3 = avec3d[5];
			} else if(k1 == 3) {
				vec3D30 = avec3d[2];
				vec3d1 = avec3d[1];
				vec3d2 = avec3d[5];
				vec3d3 = avec3d[6];
			} else if(k1 == 4) {
				vec3D30 = avec3d[3];
				vec3d1 = avec3d[2];
				vec3d2 = avec3d[6];
				vec3d3 = avec3d[7];
			} else if(k1 == 5) {
				vec3D30 = avec3d[0];
				vec3d1 = avec3d[3];
				vec3d2 = avec3d[7];
				vec3d3 = avec3d[4];
			}

			tessellator.addVertexWithUV(vec3D30.xCoord, vec3D30.yCoord, vec3D30.zCoord, (double)f4, (double)f7);
			tessellator.addVertexWithUV(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, (double)f5, (double)f7);
			tessellator.addVertexWithUV(vec3d2.xCoord, vec3d2.yCoord, vec3d2.zCoord, (double)f5, (double)f6);
			tessellator.addVertexWithUV(vec3d3.xCoord, vec3d3.yCoord, vec3d3.zCoord, (double)f4, (double)f6);
		}

		if(Config.isBetterSnow() && this.hasSnowNeighbours(par2, par3, par4)) {
			this.renderStandardBlock(Block.snow, par2, par3, par4);
		}

		return true;
	}

	public boolean renderBlockFire(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		int j = (i & 15) << 4;
		int k = i & 240;
		double d = (double)((float)j / 256.0F);
		double d2 = (double)(((float)j + 15.99F) / 256.0F);
		double d4 = (double)((float)k / 256.0F);
		double d6 = (double)(((float)k + 15.99F) / 256.0F);
		float f = 1.4F;
		double d11;
		double d13;
		double d15;
		double d17;
		double d19;
		double d21;
		double d23;
		if(!this.blockAccess.isBlockNormalCube(par2, par3 - 1, par4) && !Block.fire.canBlockCatchFire(this.blockAccess, par2, par3 - 1, par4)) {
			float f45 = 0.2F;
			float f3 = 0.0625F;
			if((par2 + par3 + par4 & 1) == 1) {
				d = (double)((float)j / 256.0F);
				d2 = (double)(((float)j + 15.99F) / 256.0F);
				d4 = (double)((float)(k + 16) / 256.0F);
				d6 = (double)(((float)k + 15.99F + 16.0F) / 256.0F);
			}

			if((par2 / 2 + par3 / 2 + par4 / 2 & 1) == 1) {
				d11 = d2;
				d2 = d;
				d = d11;
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, par2 - 1, par3, par4)) {
				tessellator.addVertexWithUV((double)((float)par2 + f45), (double)((float)par3 + f + f3), (double)(par4 + 1), d2, d4);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 1), d2, d6);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 0), d, d6);
				tessellator.addVertexWithUV((double)((float)par2 + f45), (double)((float)par3 + f + f3), (double)(par4 + 0), d, d4);
				tessellator.addVertexWithUV((double)((float)par2 + f45), (double)((float)par3 + f + f3), (double)(par4 + 0), d, d4);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 0), d, d6);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 1), d2, d6);
				tessellator.addVertexWithUV((double)((float)par2 + f45), (double)((float)par3 + f + f3), (double)(par4 + 1), d2, d4);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, par2 + 1, par3, par4)) {
				tessellator.addVertexWithUV((double)((float)(par2 + 1) - f45), (double)((float)par3 + f + f3), (double)(par4 + 0), d, d4);
				tessellator.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 0), d, d6);
				tessellator.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 1), d2, d6);
				tessellator.addVertexWithUV((double)((float)(par2 + 1) - f45), (double)((float)par3 + f + f3), (double)(par4 + 1), d2, d4);
				tessellator.addVertexWithUV((double)((float)(par2 + 1) - f45), (double)((float)par3 + f + f3), (double)(par4 + 1), d2, d4);
				tessellator.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 1), d2, d6);
				tessellator.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 0), d, d6);
				tessellator.addVertexWithUV((double)((float)(par2 + 1) - f45), (double)((float)par3 + f + f3), (double)(par4 + 0), d, d4);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, par2, par3, par4 - 1)) {
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f + f3), (double)((float)par4 + f45), d2, d4);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 0), d2, d6);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + f3), (double)(par4 + 0), d, d6);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f + f3), (double)((float)par4 + f45), d, d4);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f + f3), (double)((float)par4 + f45), d, d4);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + f3), (double)(par4 + 0), d, d6);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 0), d2, d6);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f + f3), (double)((float)par4 + f45), d2, d4);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, par2, par3, par4 + 1)) {
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f + f3), (double)((float)(par4 + 1) - f45), d, d4);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + f3), (double)(par4 + 1 - 0), d, d6);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 1 - 0), d2, d6);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f + f3), (double)((float)(par4 + 1) - f45), d2, d4);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f + f3), (double)((float)(par4 + 1) - f45), d2, d4);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + f3), (double)(par4 + 1 - 0), d2, d6);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + f3), (double)(par4 + 1 - 0), d, d6);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f + f3), (double)((float)(par4 + 1) - f45), d, d4);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, par2, par3 + 1, par4)) {
				d11 = (double)par2 + 0.5D + 0.5D;
				d13 = (double)par2 + 0.5D - 0.5D;
				d15 = (double)par4 + 0.5D + 0.5D;
				d17 = (double)par4 + 0.5D - 0.5D;
				d19 = (double)par2 + 0.5D - 0.5D;
				d21 = (double)par2 + 0.5D + 0.5D;
				d23 = (double)par4 + 0.5D - 0.5D;
				double d24 = (double)par4 + 0.5D + 0.5D;
				double d1 = (double)((float)j / 256.0F);
				double d3 = (double)(((float)j + 15.99F) / 256.0F);
				double d5 = (double)((float)k / 256.0F);
				double d7 = (double)(((float)k + 15.99F) / 256.0F);
				++par3;
				float f1 = -0.2F;
				if((par2 + par3 + par4 & 1) == 0) {
					tessellator.addVertexWithUV(d19, (double)((float)par3 + f1), (double)(par4 + 0), d3, d5);
					tessellator.addVertexWithUV(d11, (double)(par3 + 0), (double)(par4 + 0), d3, d7);
					tessellator.addVertexWithUV(d11, (double)(par3 + 0), (double)(par4 + 1), d1, d7);
					tessellator.addVertexWithUV(d19, (double)((float)par3 + f1), (double)(par4 + 1), d1, d5);
					d1 = (double)((float)j / 256.0F);
					d3 = (double)(((float)j + 15.99F) / 256.0F);
					d5 = (double)((float)(k + 16) / 256.0F);
					d7 = (double)(((float)k + 15.99F + 16.0F) / 256.0F);
					tessellator.addVertexWithUV(d21, (double)((float)par3 + f1), (double)(par4 + 1), d3, d5);
					tessellator.addVertexWithUV(d13, (double)(par3 + 0), (double)(par4 + 1), d3, d7);
					tessellator.addVertexWithUV(d13, (double)(par3 + 0), (double)(par4 + 0), d1, d7);
					tessellator.addVertexWithUV(d21, (double)((float)par3 + f1), (double)(par4 + 0), d1, d5);
				} else {
					tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f1), d24, d3, d5);
					tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), d17, d3, d7);
					tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), d17, d1, d7);
					tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f1), d24, d1, d5);
					d1 = (double)((float)j / 256.0F);
					d3 = (double)(((float)j + 15.99F) / 256.0F);
					d5 = (double)((float)(k + 16) / 256.0F);
					d7 = (double)(((float)k + 15.99F + 16.0F) / 256.0F);
					tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f1), d23, d3, d5);
					tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), d15, d3, d7);
					tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), d15, d1, d7);
					tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f1), d23, d1, d5);
				}
			}
		} else {
			double f2 = (double)par2 + 0.5D + 0.2D;
			d11 = (double)par2 + 0.5D - 0.2D;
			d13 = (double)par4 + 0.5D + 0.2D;
			d15 = (double)par4 + 0.5D - 0.2D;
			d17 = (double)par2 + 0.5D - 0.3D;
			d19 = (double)par2 + 0.5D + 0.3D;
			d21 = (double)par4 + 0.5D - 0.3D;
			d23 = (double)par4 + 0.5D + 0.3D;
			tessellator.addVertexWithUV(d17, (double)((float)par3 + f), (double)(par4 + 1), d2, d4);
			tessellator.addVertexWithUV(f2, (double)(par3 + 0), (double)(par4 + 1), d2, d6);
			tessellator.addVertexWithUV(f2, (double)(par3 + 0), (double)(par4 + 0), d, d6);
			tessellator.addVertexWithUV(d17, (double)((float)par3 + f), (double)(par4 + 0), d, d4);
			tessellator.addVertexWithUV(d19, (double)((float)par3 + f), (double)(par4 + 0), d2, d4);
			tessellator.addVertexWithUV(d11, (double)(par3 + 0), (double)(par4 + 0), d2, d6);
			tessellator.addVertexWithUV(d11, (double)(par3 + 0), (double)(par4 + 1), d, d6);
			tessellator.addVertexWithUV(d19, (double)((float)par3 + f), (double)(par4 + 1), d, d4);
			d = (double)((float)j / 256.0F);
			d2 = (double)(((float)j + 15.99F) / 256.0F);
			d4 = (double)((float)(k + 16) / 256.0F);
			d6 = (double)(((float)k + 15.99F + 16.0F) / 256.0F);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f), d23, d2, d4);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), d15, d2, d6);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), d15, d, d6);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f), d23, d, d4);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f), d21, d2, d4);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), d13, d2, d6);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), d13, d, d6);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f), d21, d, d4);
			f2 = (double)par2 + 0.5D - 0.5D;
			d11 = (double)par2 + 0.5D + 0.5D;
			d13 = (double)par4 + 0.5D - 0.5D;
			d15 = (double)par4 + 0.5D + 0.5D;
			d17 = (double)par2 + 0.5D - 0.4D;
			d19 = (double)par2 + 0.5D + 0.4D;
			d21 = (double)par4 + 0.5D - 0.4D;
			d23 = (double)par4 + 0.5D + 0.4D;
			tessellator.addVertexWithUV(d17, (double)((float)par3 + f), (double)(par4 + 0), d, d4);
			tessellator.addVertexWithUV(f2, (double)(par3 + 0), (double)(par4 + 0), d, d6);
			tessellator.addVertexWithUV(f2, (double)(par3 + 0), (double)(par4 + 1), d2, d6);
			tessellator.addVertexWithUV(d17, (double)((float)par3 + f), (double)(par4 + 1), d2, d4);
			tessellator.addVertexWithUV(d19, (double)((float)par3 + f), (double)(par4 + 1), d, d4);
			tessellator.addVertexWithUV(d11, (double)(par3 + 0), (double)(par4 + 1), d, d6);
			tessellator.addVertexWithUV(d11, (double)(par3 + 0), (double)(par4 + 0), d2, d6);
			tessellator.addVertexWithUV(d19, (double)((float)par3 + f), (double)(par4 + 0), d2, d4);
			d = (double)((float)j / 256.0F);
			d2 = (double)(((float)j + 15.99F) / 256.0F);
			d4 = (double)((float)k / 256.0F);
			d6 = (double)(((float)k + 15.99F) / 256.0F);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f), d23, d, d4);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), d15, d, d6);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), d15, d2, d6);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f), d23, d2, d4);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + f), d21, d, d4);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), d13, d, d6);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), d13, d2, d6);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + f), d21, d2, d4);
		}

		return true;
	}

	public boolean renderBlockRedstoneWire(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int j = par1Block.getBlockTextureFromSideAndMetadata(1, i);
		if(this.overrideBlockTexture >= 0) {
			j = this.overrideBlockTexture;
		}

		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f = 1.0F;
		float f1 = (float)i / 15.0F;
		float f2 = f1 * 0.6F + 0.4F;
		if(i == 0) {
			f2 = 0.3F;
		}

		float f3 = f1 * f1 * 0.7F - 0.5F;
		float f4 = f1 * f1 * 0.6F - 0.7F;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f4 < 0.0F) {
			f4 = 0.0F;
		}

		int rsColor = CustomColorizer.getRedstoneColor(i);
		int k;
		int l;
		if(rsColor != -1) {
			k = rsColor >> 16 & 255;
			l = rsColor >> 8 & 255;
			int d = rsColor & 255;
			f2 = (float)k / 255.0F;
			f3 = (float)l / 255.0F;
			f4 = (float)d / 255.0F;
		}

		tessellator.setColorOpaque_F(f2, f3, f4);
		k = (j & 15) << 4;
		l = j & 240;
		double d1 = (double)((float)k / 256.0F);
		double d2 = (double)(((float)k + 15.99F) / 256.0F);
		double d4 = (double)((float)l / 256.0F);
		double d6 = (double)(((float)l + 15.99F) / 256.0F);
		boolean flag = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3, par4, 1) || !this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3 - 1, par4, -1);
		boolean flag1 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3, par4, 3) || !this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3 - 1, par4, -1);
		boolean flag2 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3, par4 - 1, 2) || !this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 - 1, par4 - 1, -1);
		boolean flag3 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3, par4 + 1, 0) || !this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 - 1, par4 + 1, -1);
		if(!this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
			if(this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3 + 1, par4, -1)) {
				flag = true;
			}

			if(this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3 + 1, par4, -1)) {
				flag1 = true;
			}

			if(this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 + 1, par4 - 1, -1)) {
				flag2 = true;
			}

			if(this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 + 1, par4 + 1, -1)) {
				flag3 = true;
			}
		}

		float f5 = (float)(par2 + 0);
		float f6 = (float)(par2 + 1);
		float f7 = (float)(par4 + 0);
		float f8 = (float)(par4 + 1);
		byte byte0 = 0;
		if((flag || flag1) && !flag2 && !flag3) {
			byte0 = 1;
		}

		if((flag2 || flag3) && !flag1 && !flag) {
			byte0 = 2;
		}

		if(byte0 != 0) {
			d1 = (double)((float)(k + 16) / 256.0F);
			d2 = (double)(((float)(k + 16) + 15.99F) / 256.0F);
			d4 = (double)((float)l / 256.0F);
			d6 = (double)(((float)l + 15.99F) / 256.0F);
		}

		if(byte0 == 0) {
			if(!flag) {
				f5 += 0.3125F;
			}

			if(!flag) {
				d1 += 0.01953125D;
			}

			if(!flag1) {
				f6 -= 0.3125F;
			}

			if(!flag1) {
				d2 -= 0.01953125D;
			}

			if(!flag2) {
				f7 += 0.3125F;
			}

			if(!flag2) {
				d4 += 0.01953125D;
			}

			if(!flag3) {
				f8 -= 0.3125F;
			}

			if(!flag3) {
				d6 -= 0.01953125D;
			}

			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f8, d2, d6);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f7, d2, d4);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f7, d1, d4);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f8, d1, d6);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f8, d2, d6 + 0.0625D);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f7, d2, d4 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f7, d1, d4 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f8, d1, d6 + 0.0625D);
		} else if(byte0 == 1) {
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f8, d2, d6);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f7, d2, d4);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f7, d1, d4);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f8, d1, d6);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f8, d2, d6 + 0.0625D);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f7, d2, d4 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f7, d1, d4 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f8, d1, d6 + 0.0625D);
		} else if(byte0 == 2) {
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f8, d2, d6);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f7, d1, d6);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f7, d1, d4);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f8, d2, d4);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f8, d2, d6 + 0.0625D);
			tessellator.addVertexWithUV((double)f6, (double)par3 + 0.015625D, (double)f7, d1, d6 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f7, d1, d4 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)par3 + 0.015625D, (double)f8, d2, d4 + 0.0625D);
		}

		double oldMaxY;
		if(!this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
			oldMaxY = (double)((float)(k + 16) / 256.0F);
			double d3 = (double)(((float)(k + 16) + 15.99F) / 256.0F);
			double d5 = (double)((float)l / 256.0F);
			double d7 = (double)(((float)l + 15.99F) / 256.0F);
			if(this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID) {
				tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
				tessellator.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), d3, d5);
				tessellator.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 1), oldMaxY, d5);
				tessellator.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 0), oldMaxY, d7);
				tessellator.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), d3, d7);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), d3, d5 + 0.0625D);
				tessellator.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 1), oldMaxY, d5 + 0.0625D);
				tessellator.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 0), oldMaxY, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), d3, d7 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID) {
				tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
				tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 1), oldMaxY, d7);
				tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), d3, d7);
				tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), d3, d5);
				tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 0), oldMaxY, d5);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 1), oldMaxY, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), d3, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), d3, d5 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 0), oldMaxY, d5 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID) {
				tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + 0.015625D, oldMaxY, d7);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, d3, d7);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, d3, d5);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + 0.015625D, oldMaxY, d5);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + 0.015625D, oldMaxY, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, d3, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, d3, d5 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + 0.015625D, oldMaxY, d5 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID) {
				tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, d3, d5);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, oldMaxY, d5);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, oldMaxY, d7);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, d3, d7);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, d3, d5 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, oldMaxY, d5 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, oldMaxY, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, d3, d7 + 0.0625D);
			}
		}

		if(Config.isBetterSnow() && this.hasSnowNeighbours(par2, par3, par4)) {
			oldMaxY = Block.snow.maxY;
			Block.snow.maxY = 0.01D;
			this.renderStandardBlock(Block.snow, par2, par3, par4);
			Block.snow.maxY = oldMaxY;
		}

		return true;
	}

	public boolean renderBlockMinecartTrack(BlockRail par1BlockRail, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int j = par1BlockRail.getBlockTextureFromSideAndMetadata(0, i);
		if(this.overrideBlockTexture >= 0) {
			j = this.overrideBlockTexture;
		}

		if(par1BlockRail.isPowered()) {
			i &= 7;
		}

		tessellator.setBrightness(par1BlockRail.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int k = (j & 15) << 4;
		int l = j & 240;
		double d = (double)((float)k / 256.0F);
		double d1 = (double)(((float)k + 15.99F) / 256.0F);
		double d2 = (double)((float)l / 256.0F);
		double d3 = (double)(((float)l + 15.99F) / 256.0F);
		double d4 = 0.0625D;
		double d5 = (double)(par2 + 1);
		double d6 = (double)(par2 + 1);
		double d7 = (double)(par2 + 0);
		double d8 = (double)(par2 + 0);
		double d9 = (double)(par4 + 0);
		double d10 = (double)(par4 + 1);
		double d11 = (double)(par4 + 1);
		double d12 = (double)(par4 + 0);
		double d13 = (double)par3 + d4;
		double d14 = (double)par3 + d4;
		double d15 = (double)par3 + d4;
		double d16 = (double)par3 + d4;
		if(i != 1 && i != 2 && i != 3 && i != 7) {
			if(i == 8) {
				d5 = d6 = (double)(par2 + 0);
				d7 = d8 = (double)(par2 + 1);
				d9 = d12 = (double)(par4 + 1);
				d10 = d11 = (double)(par4 + 0);
			} else if(i == 9) {
				d5 = d8 = (double)(par2 + 0);
				d6 = d7 = (double)(par2 + 1);
				d9 = d10 = (double)(par4 + 0);
				d11 = d12 = (double)(par4 + 1);
			}
		} else {
			d5 = d8 = (double)(par2 + 1);
			d6 = d7 = (double)(par2 + 0);
			d9 = d10 = (double)(par4 + 1);
			d11 = d12 = (double)(par4 + 0);
		}

		if(i != 2 && i != 4) {
			if(i == 3 || i == 5) {
				++d14;
				++d15;
			}
		} else {
			++d13;
			++d16;
		}

		tessellator.addVertexWithUV(d5, d13, d9, d1, d2);
		tessellator.addVertexWithUV(d6, d14, d10, d1, d3);
		tessellator.addVertexWithUV(d7, d15, d11, d, d3);
		tessellator.addVertexWithUV(d8, d16, d12, d, d2);
		tessellator.addVertexWithUV(d8, d16, d12, d, d2);
		tessellator.addVertexWithUV(d7, d15, d11, d, d3);
		tessellator.addVertexWithUV(d6, d14, d10, d1, d3);
		tessellator.addVertexWithUV(d5, d13, d9, d1, d2);
		if(Config.isBetterSnow() && this.hasSnowNeighbours(par2, par3, par4)) {
			double oldMaxY = Block.snow.maxY;
			Block.snow.maxY = 0.05D;
			this.renderStandardBlock(Block.snow, par2, par3, par4);
			Block.snow.maxY = oldMaxY;
		}

		return true;
	}

	public boolean renderBlockLadder(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f = 1.0F;
		tessellator.setColorOpaque_F(f, f, f);
		f = (float)((i & 15) << 4);
		int j = i & 240;
		double d = (double)(f / 256.0F);
		double d1 = (double)((f + 15.99F) / 256.0F);
		double d2 = (double)((float)j / 256.0F);
		double d3 = (double)(((float)j + 15.99F) / 256.0F);
		int k = this.blockAccess.getBlockMetadata(par2, par3, par4);
		double d4 = 0.0D;
		double d5 = (double)0.05F;
		if(k == 5) {
			tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 1) + d4, (double)(par4 + 1) + d4, d, d2);
			tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 0) - d4, (double)(par4 + 1) + d4, d, d3);
			tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 0) - d4, (double)(par4 + 0) - d4, d1, d3);
			tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 1) + d4, (double)(par4 + 0) - d4, d1, d2);
		}

		if(k == 4) {
			tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 0) - d4, (double)(par4 + 1) + d4, d1, d3);
			tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 1) + d4, (double)(par4 + 1) + d4, d1, d2);
			tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 1) + d4, (double)(par4 + 0) - d4, d, d2);
			tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 0) - d4, (double)(par4 + 0) - d4, d, d3);
		}

		if(k == 3) {
			tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 0) - d4, (double)par4 + d5, d1, d3);
			tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 1) + d4, (double)par4 + d5, d1, d2);
			tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 1) + d4, (double)par4 + d5, d, d2);
			tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 0) - d4, (double)par4 + d5, d, d3);
		}

		if(k == 2) {
			tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 1) + d4, (double)(par4 + 1) - d5, d, d2);
			tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 0) - d4, (double)(par4 + 1) - d5, d, d3);
			tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 0) - d4, (double)(par4 + 1) - d5, d1, d3);
			tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 1) + d4, (double)(par4 + 1) - d5, d1, d2);
		}

		return true;
	}

	public boolean renderBlockVine(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			int f = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, par2, par3, par4, -1, i);
			if(f >= 0) {
				j = f / 256;
				tessellator = Tessellator.instance.getSubTessellator(j);
				i = f % 256;
			}
		}

		float f1 = 1.0F;
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		j = CustomColorizer.getColorMultiplier(par1Block, this.blockAccess, par2, par3, par4);
		float f1 = (float)(j >> 16 & 255) / 255.0F;
		float d = (float)(j >> 8 & 255) / 255.0F;
		float f2 = (float)(j & 255) / 255.0F;
		tessellator.setColorOpaque_F(f1 * f1, f1 * d, f1 * f2);
		j = (i & 15) << 4;
		f1 = (float)(i & 240);
		d = (float)j / 256.0F;
		double d1 = (double)(((float)j + 15.99F) / 256.0F);
		double d2 = (double)(f1 / 256.0F);
		double d3 = (double)((f1 + 15.99F) / 256.0F);
		double d4 = (double)0.05F;
		int k = this.blockAccess.getBlockMetadata(par2, par3, par4);
		if((k & 2) != 0) {
			tessellator.addVertexWithUV((double)par2 + d4, (double)(par3 + 1), (double)(par4 + 1), (double)d, d2);
			tessellator.addVertexWithUV((double)par2 + d4, (double)(par3 + 0), (double)(par4 + 1), (double)d, d3);
			tessellator.addVertexWithUV((double)par2 + d4, (double)(par3 + 0), (double)(par4 + 0), d1, d3);
			tessellator.addVertexWithUV((double)par2 + d4, (double)(par3 + 1), (double)(par4 + 0), d1, d2);
			tessellator.addVertexWithUV((double)par2 + d4, (double)(par3 + 1), (double)(par4 + 0), d1, d2);
			tessellator.addVertexWithUV((double)par2 + d4, (double)(par3 + 0), (double)(par4 + 0), d1, d3);
			tessellator.addVertexWithUV((double)par2 + d4, (double)(par3 + 0), (double)(par4 + 1), (double)d, d3);
			tessellator.addVertexWithUV((double)par2 + d4, (double)(par3 + 1), (double)(par4 + 1), (double)d, d2);
		}

		if((k & 8) != 0) {
			tessellator.addVertexWithUV((double)(par2 + 1) - d4, (double)(par3 + 0), (double)(par4 + 1), d1, d3);
			tessellator.addVertexWithUV((double)(par2 + 1) - d4, (double)(par3 + 1), (double)(par4 + 1), d1, d2);
			tessellator.addVertexWithUV((double)(par2 + 1) - d4, (double)(par3 + 1), (double)(par4 + 0), (double)d, d2);
			tessellator.addVertexWithUV((double)(par2 + 1) - d4, (double)(par3 + 0), (double)(par4 + 0), (double)d, d3);
			tessellator.addVertexWithUV((double)(par2 + 1) - d4, (double)(par3 + 0), (double)(par4 + 0), (double)d, d3);
			tessellator.addVertexWithUV((double)(par2 + 1) - d4, (double)(par3 + 1), (double)(par4 + 0), (double)d, d2);
			tessellator.addVertexWithUV((double)(par2 + 1) - d4, (double)(par3 + 1), (double)(par4 + 1), d1, d2);
			tessellator.addVertexWithUV((double)(par2 + 1) - d4, (double)(par3 + 0), (double)(par4 + 1), d1, d3);
		}

		if((k & 4) != 0) {
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + d4, d1, d3);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)par4 + d4, d1, d2);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)par4 + d4, (double)d, d2);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + d4, (double)d, d3);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + d4, (double)d, d3);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)par4 + d4, (double)d, d2);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)par4 + d4, d1, d2);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + d4, d1, d3);
		}

		if((k & 1) != 0) {
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)(par4 + 1) - d4, (double)d, d2);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - d4, (double)d, d3);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - d4, d1, d3);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)(par4 + 1) - d4, d1, d2);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)(par4 + 1) - d4, d1, d2);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - d4, d1, d3);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - d4, (double)d, d3);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)(par4 + 1) - d4, (double)d, d2);
		}

		if(this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1) - d4, (double)(par4 + 0), (double)d, d2);
			tessellator.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1) - d4, (double)(par4 + 1), (double)d, d3);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1) - d4, (double)(par4 + 1), d1, d3);
			tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1) - d4, (double)(par4 + 0), d1, d2);
		}

		return true;
	}

	public boolean renderBlockPane(BlockPane par1BlockPane, int par2, int par3, int par4) {
		int i = this.blockAccess.getHeight();
		Tessellator tessellator = Tessellator.instance;
		boolean connected = par1BlockPane == Block.thinGlass && ConnectedTextures.isConnectedGlassPanes();
		Tessellator tessellatorFront = tessellator;
		tessellator.setBrightness(par1BlockPane.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f = 1.0F;
		int j = par1BlockPane.colorMultiplier(this.blockAccess, par2, par3, par4);
		float f1 = (float)(j >> 16 & 255) / 255.0F;
		float f2 = (float)(j >> 8 & 255) / 255.0F;
		float f3 = (float)(j & 255) / 255.0F;
		if(EntityRenderer.anaglyphEnable) {
			float k = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float l = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float kr = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = k;
			f2 = l;
			f3 = kr;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		boolean k1 = false;
		boolean l1 = false;
		int kz;
		int k2;
		int l2;
		int kr1;
		if(this.overrideBlockTexture >= 0) {
			k2 = this.overrideBlockTexture;
			l2 = this.overrideBlockTexture;
			connected = false;
		} else {
			kr1 = this.blockAccess.getBlockMetadata(par2, par3, par4);
			k2 = par1BlockPane.getBlockTextureFromSideAndMetadata(0, kr1);
			l2 = par1BlockPane.getSideTextureIndex();
			if(connected) {
				kz = Config.getMinecraft().renderEngine.getTexture("/ctm.png");
				tessellatorFront = Tessellator.instance.getSubTessellator(kz);
				k2 = 0;
			}
		}

		kr1 = k2;
		kz = k2;
		int kzr = k2;
		int blockIdXp = this.blockAccess.getBlockId(par2 + 1, par3, par4);
		int blockIdXn = this.blockAccess.getBlockId(par2 - 1, par3, par4);
		int blockIdZp = this.blockAccess.getBlockId(par2, par3, par4 + 1);
		int blockIdZn = this.blockAccess.getBlockId(par2, par3, par4 - 1);
		int j1;
		int k1;
		if(connected) {
			j1 = Block.thinGlass.blockID;
			k1 = this.blockAccess.getBlockId(par2, par3 + 1, par4);
			int d = this.blockAccess.getBlockId(par2, par3 - 1, par4);
			boolean linkXp = blockIdXp == j1;
			boolean d1 = blockIdXn == j1;
			boolean linkYp = k1 == j1;
			boolean d2 = d == j1;
			boolean linkZp = blockIdZp == j1;
			boolean d3 = blockIdZn == j1;
			k2 = this.getGlassPaneTexture(linkXp, d1, linkYp, d2);
			kr1 = this.getReverseGlassPaneTexture(k2);
			kz = this.getGlassPaneTexture(linkZp, d3, linkYp, d2);
			kzr = this.getReverseGlassPaneTexture(kz);
		}

		j1 = (k2 & 15) << 4;
		k1 = k2 & 240;
		double d1 = (double)((float)j1 / 256.0F);
		double d11 = (double)(((float)j1 + 7.99F) / 256.0F);
		double d21 = (double)(((float)j1 + 15.99F) / 256.0F);
		double d31 = (double)((float)k1 / 256.0F);
		double d4 = (double)(((float)k1 + 15.99F) / 256.0F);
		int j1r = (kr1 & 15) << 4;
		int k1r = kr1 & 240;
		double dr = (double)((float)j1r / 256.0F);
		double d1r = (double)(((float)j1r + 7.99F) / 256.0F);
		double d2r = (double)(((float)j1r + 15.99F) / 256.0F);
		double d3r = (double)((float)k1r / 256.0F);
		double d4r = (double)(((float)k1r + 15.99F) / 256.0F);
		int j1z = (kz & 15) << 4;
		int k1z = kz & 240;
		double dz = (double)((float)j1z / 256.0F);
		double d1z = (double)(((float)j1z + 7.99F) / 256.0F);
		double d2z = (double)(((float)j1z + 15.99F) / 256.0F);
		double d3z = (double)((float)k1z / 256.0F);
		double d4z = (double)(((float)k1z + 15.99F) / 256.0F);
		int j1zr = (kzr & 15) << 4;
		int k1zr = kzr & 240;
		double dzr = (double)((float)j1zr / 256.0F);
		double d1zr = (double)(((float)j1zr + 7.99F) / 256.0F);
		double d2zr = (double)(((float)j1zr + 15.99F) / 256.0F);
		double d3zr = (double)((float)k1zr / 256.0F);
		double d4zr = (double)(((float)k1zr + 15.99F) / 256.0F);
		int l1 = (l2 & 15) << 4;
		int i2 = l2 & 240;
		double d5 = (double)((float)(l1 + 7) / 256.0F);
		double d6 = (double)(((float)l1 + 8.99F) / 256.0F);
		double d7 = (double)((float)i2 / 256.0F);
		double d8 = (double)((float)(i2 + 8) / 256.0F);
		double d9 = (double)(((float)i2 + 15.99F) / 256.0F);
		double d10 = (double)par2;
		double d11 = (double)par2 + 0.5D;
		double d12 = (double)(par2 + 1);
		double d13 = (double)par4;
		double d14 = (double)par4 + 0.5D;
		double d15 = (double)(par4 + 1);
		double d16 = (double)par2 + 0.5D - 0.0625D;
		double d17 = (double)par2 + 0.5D + 0.0625D;
		double d18 = (double)par4 + 0.5D - 0.0625D;
		double d19 = (double)par4 + 0.5D + 0.0625D;
		boolean flag = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2, par3, par4 - 1));
		boolean flag1 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2, par3, par4 + 1));
		boolean flag2 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2 - 1, par3, par4));
		boolean flag3 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2 + 1, par3, par4));
		boolean flag4 = par1BlockPane.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1);
		boolean flag5 = par1BlockPane.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0);
		if(flag2 && flag3 || !flag2 && !flag3 && !flag && !flag1) {
			tessellatorFront.addVertexWithUV(d10, (double)(par3 + 1), d14, d1, d31);
			tessellatorFront.addVertexWithUV(d10, (double)(par3 + 0), d14, d1, d4);
			tessellatorFront.addVertexWithUV(d12, (double)(par3 + 0), d14, d21, d4);
			tessellatorFront.addVertexWithUV(d12, (double)(par3 + 1), d14, d21, d31);
			tessellatorFront.addVertexWithUV(d12, (double)(par3 + 1), d14, dr, d3r);
			tessellatorFront.addVertexWithUV(d12, (double)(par3 + 0), d14, dr, d4r);
			tessellatorFront.addVertexWithUV(d10, (double)(par3 + 0), d14, d2r, d4r);
			tessellatorFront.addVertexWithUV(d10, (double)(par3 + 1), d14, d2r, d3r);
			if(flag4) {
				tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d9);
			} else {
				if(par3 < i - 1 && this.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4)) {
					tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
				}

				if(par3 < i - 1 && this.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4)) {
					tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
				}
			}

			if(flag5) {
				tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d9);
			} else {
				if(par3 > 1 && this.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4)) {
					tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
				}

				if(par3 > 1 && this.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4)) {
					tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
				}
			}
		} else if(flag2 && !flag3) {
			tessellatorFront.addVertexWithUV(d10, (double)(par3 + 1), d14, d1, d31);
			tessellatorFront.addVertexWithUV(d10, (double)(par3 + 0), d14, d1, d4);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d14, d11, d4);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d14, d11, d31);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d14, d1r, d3r);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d14, d1r, d4r);
			tessellatorFront.addVertexWithUV(d10, (double)(par3 + 0), d14, d2r, d4r);
			tessellatorFront.addVertexWithUV(d10, (double)(par3 + 1), d14, d2r, d3r);
			if(!flag1 && !flag) {
				tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d5, d7);
				tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d5, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d6, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d6, d7);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d5, d7);
				tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d5, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d6, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d6, d7);
			}

			if(flag4 || par3 < i - 1 && this.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4)) {
				tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d8);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
				tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
			}

			if(flag5 || par3 > 1 && this.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4)) {
				tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
				tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
				tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
			}
		} else if(!flag2 && flag3) {
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d14, d11, d31);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d14, d11, d4);
			tessellatorFront.addVertexWithUV(d12, (double)(par3 + 0), d14, d21, d4);
			tessellatorFront.addVertexWithUV(d12, (double)(par3 + 1), d14, d21, d31);
			tessellatorFront.addVertexWithUV(d12, (double)(par3 + 1), d14, dr, d3r);
			tessellatorFront.addVertexWithUV(d12, (double)(par3 + 0), d14, dr, d4r);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d14, d1r, d4r);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d14, d1r, d3r);
			if(!flag1 && !flag) {
				tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d5, d7);
				tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d5, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d6, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d6, d7);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d5, d7);
				tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d5, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d6, d9);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d6, d7);
			}

			if(flag4 || par3 < i - 1 && this.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4)) {
				tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d8);
				tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
				tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
			}

			if(flag5 || par3 > 1 && this.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4)) {
				tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
				tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
				tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
			}
		}

		if((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
			if(flag && !flag1) {
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d13, dz, d3z);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d13, dz, d4z);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d14, d1z, d4z);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d14, d1z, d3z);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d14, d1zr, d3zr);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d14, d1zr, d4zr);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d13, d2zr, d4zr);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d13, d2zr, d3zr);
				if(!flag3 && !flag2) {
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d5, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d6, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d7);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d7);
					tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d5, d9);
					tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d6, d9);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d7);
				}

				if(flag4 || par3 < i - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1)) {
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d13, d6, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d8);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d8);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d13, d5, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d13, d6, d8);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d13, d5, d8);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d7);
				}

				if(flag5 || par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) {
					tessellator.addVertexWithUV(d16, (double)par3, d13, d6, d7);
					tessellator.addVertexWithUV(d16, (double)par3, d14, d6, d8);
					tessellator.addVertexWithUV(d17, (double)par3, d14, d5, d8);
					tessellator.addVertexWithUV(d17, (double)par3, d13, d5, d7);
					tessellator.addVertexWithUV(d16, (double)par3, d14, d6, d7);
					tessellator.addVertexWithUV(d16, (double)par3, d13, d6, d8);
					tessellator.addVertexWithUV(d17, (double)par3, d13, d5, d8);
					tessellator.addVertexWithUV(d17, (double)par3, d14, d5, d7);
				}
			} else if(!flag && flag1) {
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d14, d1z, d3z);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d14, d1z, d4z);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d15, d2z, d4z);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d15, d2z, d3z);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d15, dzr, d3zr);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d15, dzr, d4zr);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d14, d1zr, d4zr);
				tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d14, d1zr, d3zr);
				if(!flag3 && !flag2) {
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d7);
					tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d5, d9);
					tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d6, d9);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d5, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d6, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d7);
				}

				if(flag4 || par3 < i - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1)) {
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d15, d5, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d15, d6, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d8);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d15, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d15, d6, d8);
				}

				if(flag5 || par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) {
					tessellator.addVertexWithUV(d16, (double)par3, d14, d5, d8);
					tessellator.addVertexWithUV(d16, (double)par3, d15, d5, d9);
					tessellator.addVertexWithUV(d17, (double)par3, d15, d6, d9);
					tessellator.addVertexWithUV(d17, (double)par3, d14, d6, d8);
					tessellator.addVertexWithUV(d16, (double)par3, d15, d5, d8);
					tessellator.addVertexWithUV(d16, (double)par3, d14, d5, d9);
					tessellator.addVertexWithUV(d17, (double)par3, d14, d6, d9);
					tessellator.addVertexWithUV(d17, (double)par3, d15, d6, d8);
				}
			}
		} else {
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d15, dzr, d3zr);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d15, dzr, d4zr);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d13, d2zr, d4zr);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d13, d2zr, d3zr);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d13, dz, d3z);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d13, dz, d4z);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 0), d15, d2z, d4z);
			tessellatorFront.addVertexWithUV(d11, (double)(par3 + 1), d15, d2z, d3z);
			if(flag4) {
				tessellator.addVertexWithUV(d17, (double)(par3 + 1), d15, d6, d9);
				tessellator.addVertexWithUV(d17, (double)(par3 + 1), d13, d6, d7);
				tessellator.addVertexWithUV(d16, (double)(par3 + 1), d13, d5, d7);
				tessellator.addVertexWithUV(d16, (double)(par3 + 1), d15, d5, d9);
				tessellator.addVertexWithUV(d17, (double)(par3 + 1), d13, d6, d9);
				tessellator.addVertexWithUV(d17, (double)(par3 + 1), d15, d6, d7);
				tessellator.addVertexWithUV(d16, (double)(par3 + 1), d15, d5, d7);
				tessellator.addVertexWithUV(d16, (double)(par3 + 1), d13, d5, d9);
			} else {
				if(par3 < i - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1)) {
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d13, d6, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d8);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d8);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d13, d5, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d7);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d13, d6, d8);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d13, d5, d8);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d7);
				}

				if(par3 < i - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1)) {
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d15, d5, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d15, d6, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d8);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d15, d5, d8);
					tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d9);
					tessellator.addVertexWithUV(d17, (double)(par3 + 1), d15, d6, d8);
				}
			}

			if(flag5) {
				tessellator.addVertexWithUV(d17, (double)par3, d15, d6, d9);
				tessellator.addVertexWithUV(d17, (double)par3, d13, d6, d7);
				tessellator.addVertexWithUV(d16, (double)par3, d13, d5, d7);
				tessellator.addVertexWithUV(d16, (double)par3, d15, d5, d9);
				tessellator.addVertexWithUV(d17, (double)par3, d13, d6, d9);
				tessellator.addVertexWithUV(d17, (double)par3, d15, d6, d7);
				tessellator.addVertexWithUV(d16, (double)par3, d15, d5, d7);
				tessellator.addVertexWithUV(d16, (double)par3, d13, d5, d9);
			} else {
				if(par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) {
					tessellator.addVertexWithUV(d16, (double)par3, d13, d6, d7);
					tessellator.addVertexWithUV(d16, (double)par3, d14, d6, d8);
					tessellator.addVertexWithUV(d17, (double)par3, d14, d5, d8);
					tessellator.addVertexWithUV(d17, (double)par3, d13, d5, d7);
					tessellator.addVertexWithUV(d16, (double)par3, d14, d6, d7);
					tessellator.addVertexWithUV(d16, (double)par3, d13, d6, d8);
					tessellator.addVertexWithUV(d17, (double)par3, d13, d5, d8);
					tessellator.addVertexWithUV(d17, (double)par3, d14, d5, d7);
				}

				if(par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) {
					tessellator.addVertexWithUV(d16, (double)par3, d14, d5, d8);
					tessellator.addVertexWithUV(d16, (double)par3, d15, d5, d9);
					tessellator.addVertexWithUV(d17, (double)par3, d15, d6, d9);
					tessellator.addVertexWithUV(d17, (double)par3, d14, d6, d8);
					tessellator.addVertexWithUV(d16, (double)par3, d15, d5, d8);
					tessellator.addVertexWithUV(d16, (double)par3, d14, d5, d9);
					tessellator.addVertexWithUV(d17, (double)par3, d14, d6, d9);
					tessellator.addVertexWithUV(d17, (double)par3, d15, d6, d8);
				}
			}
		}

		if(Config.isBetterSnow() && this.hasSnowNeighbours(par2, par3, par4)) {
			this.renderStandardBlock(Block.snow, par2, par3, par4);
		}

		return true;
	}

	private int getReverseGlassPaneTexture(int texNum) {
		int col = texNum % 16;
		return col == 1 ? texNum + 2 : (col == 3 ? texNum - 2 : texNum);
	}

	private int getGlassPaneTexture(boolean linkP, boolean linkN, boolean linkYp, boolean linkYn) {
		return linkN && linkP ? (linkYp ? (linkYn ? 34 : 50) : (linkYn ? 18 : 2)) : (linkN && !linkP ? (linkYp ? (linkYn ? 35 : 51) : (linkYn ? 19 : 3)) : (!linkN && linkP ? (linkYp ? (linkYn ? 33 : 49) : (linkYn ? 17 : 1)) : (linkYp ? (linkYn ? 32 : 48) : (linkYn ? 16 : 0))));
	}

	public boolean renderCrossedSquares(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f = 1.0F;
		int i = CustomColorizer.getColorMultiplier(par1Block, this.blockAccess, par2, par3, par4);
		float f1 = (float)(i >> 16 & 255) / 255.0F;
		float f2 = (float)(i >> 8 & 255) / 255.0F;
		float f3 = (float)(i & 255) / 255.0F;
		if(EntityRenderer.anaglyphEnable) {
			float d = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float d1 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = d;
			f2 = f5;
			f3 = d1;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		double d1 = (double)par2;
		double d11 = (double)par3;
		double d2 = (double)par4;
		if(par1Block == Block.tallGrass) {
			long l = (long)(par2 * 3129871) ^ (long)par4 * 116129781L ^ (long)par3;
			l = l * l * 42317861L + l * 11L;
			d1 += ((double)((float)(l >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
			d11 += ((double)((float)(l >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
			d2 += ((double)((float)(l >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
		}

		this.drawCrossedSquares(par1Block, this.blockAccess.getBlockMetadata(par2, par3, par4), d1, d11, d2);
		if(Config.isBetterSnow() && this.hasSnowNeighbours(par2, par3, par4)) {
			this.renderStandardBlock(Block.snow, par2, par3, par4);
		}

		return true;
	}

	public boolean renderBlockStem(Block par1Block, int par2, int par3, int par4) {
		BlockStem blockstem = (BlockStem)par1Block;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(blockstem.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f = 1.0F;
		int i = CustomColorizer.getStemColorMultiplier(blockstem, this.blockAccess, par2, par3, par4);
		float f1 = (float)(i >> 16 & 255) / 255.0F;
		float f2 = (float)(i >> 8 & 255) / 255.0F;
		float f3 = (float)(i & 255) / 255.0F;
		if(EntityRenderer.anaglyphEnable) {
			float j = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = j;
			f2 = f5;
			f3 = f6;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		blockstem.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
		int j1 = blockstem.func_35296_f(this.blockAccess, par2, par3, par4);
		if(j1 < 0) {
			this.renderBlockStemSmall(blockstem, this.blockAccess.getBlockMetadata(par2, par3, par4), blockstem.maxY, (double)par2, (double)par3, (double)par4);
		} else {
			this.renderBlockStemSmall(blockstem, this.blockAccess.getBlockMetadata(par2, par3, par4), 0.5D, (double)par2, (double)par3, (double)par4);
			this.renderBlockStemBig(blockstem, this.blockAccess.getBlockMetadata(par2, par3, par4), j1, blockstem.maxY, (double)par2, (double)par3, (double)par4);
		}

		return true;
	}

	public boolean renderBlockCrops(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		this.renderBlockCropsImpl(par1Block, this.blockAccess.getBlockMetadata(par2, par3, par4), (double)par2, (double)((float)par3 - 0.0625F), (double)par4);
		return true;
	}

	public void renderTorchAtAngle(Block par1Block, double par2, double par4, double par6, double par8, double par10) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		float f = (float)j / 256.0F;
		float f1 = ((float)j + 15.99F) / 256.0F;
		float f2 = (float)k / 256.0F;
		float f3 = ((float)k + 15.99F) / 256.0F;
		double d = (double)f + 7.0D / 256D;
		double d1 = (double)f2 + 6.0D / 256D;
		double d2 = (double)f + 9.0D / 256D;
		double d3 = (double)f2 + 8.0D / 256D;
		par2 += 0.5D;
		par6 += 0.5D;
		double d4 = par2 - 0.5D;
		double d5 = par2 + 0.5D;
		double d6 = par6 - 0.5D;
		double d7 = par6 + 0.5D;
		double d8 = 0.0625D;
		double d9 = 0.625D;
		tessellator.addVertexWithUV(par2 + par8 * (1.0D - d9) - d8, par4 + d9, par6 + par10 * (1.0D - d9) - d8, d, d1);
		tessellator.addVertexWithUV(par2 + par8 * (1.0D - d9) - d8, par4 + d9, par6 + par10 * (1.0D - d9) + d8, d, d3);
		tessellator.addVertexWithUV(par2 + par8 * (1.0D - d9) + d8, par4 + d9, par6 + par10 * (1.0D - d9) + d8, d2, d3);
		tessellator.addVertexWithUV(par2 + par8 * (1.0D - d9) + d8, par4 + d9, par6 + par10 * (1.0D - d9) - d8, d2, d1);
		tessellator.addVertexWithUV(par2 - d8, par4 + 1.0D, d6, (double)f, (double)f2);
		tessellator.addVertexWithUV(par2 - d8 + par8, par4 + 0.0D, d6 + par10, (double)f, (double)f3);
		tessellator.addVertexWithUV(par2 - d8 + par8, par4 + 0.0D, d7 + par10, (double)f1, (double)f3);
		tessellator.addVertexWithUV(par2 - d8, par4 + 1.0D, d7, (double)f1, (double)f2);
		tessellator.addVertexWithUV(par2 + d8, par4 + 1.0D, d7, (double)f, (double)f2);
		tessellator.addVertexWithUV(par2 + par8 + d8, par4 + 0.0D, d7 + par10, (double)f, (double)f3);
		tessellator.addVertexWithUV(par2 + par8 + d8, par4 + 0.0D, d6 + par10, (double)f1, (double)f3);
		tessellator.addVertexWithUV(par2 + d8, par4 + 1.0D, d6, (double)f1, (double)f2);
		tessellator.addVertexWithUV(d4, par4 + 1.0D, par6 + d8, (double)f, (double)f2);
		tessellator.addVertexWithUV(d4 + par8, par4 + 0.0D, par6 + d8 + par10, (double)f, (double)f3);
		tessellator.addVertexWithUV(d5 + par8, par4 + 0.0D, par6 + d8 + par10, (double)f1, (double)f3);
		tessellator.addVertexWithUV(d5, par4 + 1.0D, par6 + d8, (double)f1, (double)f2);
		tessellator.addVertexWithUV(d5, par4 + 1.0D, par6 - d8, (double)f, (double)f2);
		tessellator.addVertexWithUV(d5 + par8, par4 + 0.0D, par6 - d8 + par10, (double)f, (double)f3);
		tessellator.addVertexWithUV(d4 + par8, par4 + 0.0D, par6 - d8 + par10, (double)f1, (double)f3);
		tessellator.addVertexWithUV(d4, par4 + 1.0D, par6 - d8, (double)f1, (double)f2);
	}

	public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.getBlockTextureFromSideAndMetadata(0, par2);
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j;
		int k;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			j = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, (int)par3, (int)par5, (int)par7, -1, i);
			if(j >= 0) {
				k = j / 256;
				tessellator = Tessellator.instance.getSubTessellator(k);
				i = j % 256;
			}
		}

		j = (i & 15) << 4;
		k = i & 240;
		double d = (double)((float)j / 256.0F);
		double d1 = (double)(((float)j + 15.99F) / 256.0F);
		double d2 = (double)((float)k / 256.0F);
		double d3 = (double)(((float)k + 15.99F) / 256.0F);
		double d4 = par3 + 0.5D - 0.45D;
		double d5 = par3 + 0.5D + 0.45D;
		double d6 = par7 + 0.5D - 0.45D;
		double d7 = par7 + 0.5D + 0.45D;
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d, d2);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d, d3);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d1, d3);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d1, d2);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d, d2);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d1, d3);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d1, d2);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d, d2);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d, d3);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d1, d3);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d1, d2);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d, d2);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d, d3);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d1, d3);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d1, d2);
	}

	public void renderBlockStemSmall(Block par1Block, int par2, double par3, double par5, double par7, double par9) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.getBlockTextureFromSideAndMetadata(0, par2);
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d = (double)((float)j / 256.0F);
		double d1 = (double)(((float)j + 15.99F) / 256.0F);
		double d2 = (double)((float)k / 256.0F);
		double d3 = ((double)k + 15.989999771118164D * par3) / 256.0D;
		double d4 = par5 + 0.5D - (double)0.45F;
		double d5 = par5 + 0.5D + (double)0.45F;
		double d6 = par9 + 0.5D - (double)0.45F;
		double d7 = par9 + 0.5D + (double)0.45F;
		tessellator.addVertexWithUV(d4, par7 + par3, d6, d, d2);
		tessellator.addVertexWithUV(d4, par7 + 0.0D, d6, d, d3);
		tessellator.addVertexWithUV(d5, par7 + 0.0D, d7, d1, d3);
		tessellator.addVertexWithUV(d5, par7 + par3, d7, d1, d2);
		tessellator.addVertexWithUV(d5, par7 + par3, d7, d, d2);
		tessellator.addVertexWithUV(d5, par7 + 0.0D, d7, d, d3);
		tessellator.addVertexWithUV(d4, par7 + 0.0D, d6, d1, d3);
		tessellator.addVertexWithUV(d4, par7 + par3, d6, d1, d2);
		tessellator.addVertexWithUV(d4, par7 + par3, d7, d, d2);
		tessellator.addVertexWithUV(d4, par7 + 0.0D, d7, d, d3);
		tessellator.addVertexWithUV(d5, par7 + 0.0D, d6, d1, d3);
		tessellator.addVertexWithUV(d5, par7 + par3, d6, d1, d2);
		tessellator.addVertexWithUV(d5, par7 + par3, d6, d, d2);
		tessellator.addVertexWithUV(d5, par7 + 0.0D, d6, d, d3);
		tessellator.addVertexWithUV(d4, par7 + 0.0D, d7, d1, d3);
		tessellator.addVertexWithUV(d4, par7 + par3, d7, d1, d2);
	}

	public boolean renderBlockLilyPad(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.blockIndexInTexture;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j;
		int k;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			j = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, par2, par3, par4, -1, i);
			if(j >= 0) {
				k = j / 256;
				tessellator = Tessellator.instance.getSubTessellator(k);
				i = j % 256;
			}
		}

		j = (i & 15) << 4;
		k = i & 240;
		float f = 0.015625F;
		double d = (double)((float)j / 256.0F);
		double d1 = (double)(((float)j + 15.99F) / 256.0F);
		double d2 = (double)((float)k / 256.0F);
		double d3 = (double)(((float)k + 15.99F) / 256.0F);
		long l = (long)(par2 * 3129871) ^ (long)par4 * 116129781L ^ (long)par3;
		l = l * l * 42317861L + l * 11L;
		int i1 = (int)(l >> 16 & 3L);
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float f1 = (float)par2 + 0.5F;
		float f2 = (float)par4 + 0.5F;
		float f3 = (float)(i1 & 1) * 0.5F * (float)(1 - i1 / 2 % 2 * 2);
		float f4 = (float)(i1 + 1 & 1) * 0.5F * (float)(1 - (i1 + 1) / 2 % 2 * 2);
		int col = CustomColorizer.getLilypadColor();
		tessellator.setColorOpaque_I(col);
		tessellator.addVertexWithUV((double)(f1 + f3 - f4), (double)((float)par3 + f), (double)(f2 + f3 + f4), d, d2);
		tessellator.addVertexWithUV((double)(f1 + f3 + f4), (double)((float)par3 + f), (double)(f2 - f3 + f4), d1, d2);
		tessellator.addVertexWithUV((double)(f1 - f3 + f4), (double)((float)par3 + f), (double)(f2 - f3 - f4), d1, d3);
		tessellator.addVertexWithUV((double)(f1 - f3 - f4), (double)((float)par3 + f), (double)(f2 + f3 - f4), d, d3);
		tessellator.setColorOpaque_I((col & 16711422) >> 1);
		tessellator.addVertexWithUV((double)(f1 - f3 - f4), (double)((float)par3 + f), (double)(f2 + f3 - f4), d, d3);
		tessellator.addVertexWithUV((double)(f1 - f3 + f4), (double)((float)par3 + f), (double)(f2 - f3 - f4), d1, d3);
		tessellator.addVertexWithUV((double)(f1 + f3 + f4), (double)((float)par3 + f), (double)(f2 - f3 + f4), d1, d2);
		tessellator.addVertexWithUV((double)(f1 + f3 - f4), (double)((float)par3 + f), (double)(f2 + f3 + f4), d, d2);
		return true;
	}

	public void renderBlockStemBig(Block par1Block, int par2, int par3, double par4, double par6, double par8, double par10) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.getBlockTextureFromSideAndMetadata(0, par2) + 16;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d = (double)((float)j / 256.0F);
		double d1 = (double)(((float)j + 15.99F) / 256.0F);
		double d2 = (double)((float)k / 256.0F);
		double d3 = ((double)k + 15.989999771118164D * par4) / 256.0D;
		double d4 = par6 + 0.5D - 0.5D;
		double d5 = par6 + 0.5D + 0.5D;
		double d6 = par10 + 0.5D - 0.5D;
		double d7 = par10 + 0.5D + 0.5D;
		double d8 = par6 + 0.5D;
		double d9 = par10 + 0.5D;
		if((par3 + 1) / 2 % 2 == 1) {
			double d10 = d1;
			d1 = d;
			d = d10;
		}

		if(par3 < 2) {
			tessellator.addVertexWithUV(d4, par8 + par4, d9, d, d2);
			tessellator.addVertexWithUV(d4, par8 + 0.0D, d9, d, d3);
			tessellator.addVertexWithUV(d5, par8 + 0.0D, d9, d1, d3);
			tessellator.addVertexWithUV(d5, par8 + par4, d9, d1, d2);
			tessellator.addVertexWithUV(d5, par8 + par4, d9, d1, d2);
			tessellator.addVertexWithUV(d5, par8 + 0.0D, d9, d1, d3);
			tessellator.addVertexWithUV(d4, par8 + 0.0D, d9, d, d3);
			tessellator.addVertexWithUV(d4, par8 + par4, d9, d, d2);
		} else {
			tessellator.addVertexWithUV(d8, par8 + par4, d7, d, d2);
			tessellator.addVertexWithUV(d8, par8 + 0.0D, d7, d, d3);
			tessellator.addVertexWithUV(d8, par8 + 0.0D, d6, d1, d3);
			tessellator.addVertexWithUV(d8, par8 + par4, d6, d1, d2);
			tessellator.addVertexWithUV(d8, par8 + par4, d6, d1, d2);
			tessellator.addVertexWithUV(d8, par8 + 0.0D, d6, d1, d3);
			tessellator.addVertexWithUV(d8, par8 + 0.0D, d7, d, d3);
			tessellator.addVertexWithUV(d8, par8 + par4, d7, d, d2);
		}

	}

	public void renderBlockCropsImpl(Block par1Block, int par2, double par3, double par5, double par7) {
		Tessellator tessellator = Tessellator.instance;
		int i = par1Block.getBlockTextureFromSideAndMetadata(0, par2);
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d = (double)((float)j / 256.0F);
		double d1 = (double)(((float)j + 15.99F) / 256.0F);
		double d2 = (double)((float)k / 256.0F);
		double d3 = (double)(((float)k + 15.99F) / 256.0F);
		double d4 = par3 + 0.5D - 0.25D;
		double d5 = par3 + 0.5D + 0.25D;
		double d6 = par7 + 0.5D - 0.5D;
		double d7 = par7 + 0.5D + 0.5D;
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d, d2);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d, d3);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d1, d3);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d1, d2);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d, d2);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d, d3);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d1, d3);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d1, d2);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d, d2);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d1, d3);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d1, d2);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d, d2);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d, d3);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d1, d3);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d1, d2);
		d4 = par3 + 0.5D - 0.5D;
		d5 = par3 + 0.5D + 0.5D;
		d6 = par7 + 0.5D - 0.25D;
		d7 = par7 + 0.5D + 0.25D;
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d, d2);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d, d3);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d1, d3);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d1, d2);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d, d2);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d, d3);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d1, d3);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d1, d2);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d, d2);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d1, d3);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d1, d2);
		tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d, d2);
		tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d, d3);
		tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d1, d3);
		tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d1, d2);
	}

	public boolean renderBlockFluids(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		int i = CustomColorizer.getFluidColor(par1Block, this.blockAccess, par2, par3, par4);
		float f = (float)(i >> 16 & 255) / 255.0F;
		float f1 = (float)(i >> 8 & 255) / 255.0F;
		float f2 = (float)(i & 255) / 255.0F;
		boolean flag = par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1);
		boolean flag1 = par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0);
		boolean[] aflag = new boolean[]{par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)};
		if(!flag && !flag1 && !aflag[0] && !aflag[1] && !aflag[2] && !aflag[3]) {
			return false;
		} else {
			boolean flag2 = false;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			double d = 0.0D;
			double d1 = 1.0D;
			Material material = par1Block.blockMaterial;
			int j = this.blockAccess.getBlockMetadata(par2, par3, par4);
			double d2 = (double)this.getFluidHeight(par2, par3, par4, material);
			double d3 = (double)this.getFluidHeight(par2, par3, par4 + 1, material);
			double d4 = (double)this.getFluidHeight(par2 + 1, par3, par4 + 1, material);
			double d5 = (double)this.getFluidHeight(par2 + 1, par3, par4, material);
			double d6 = 0.0010000000474974513D;
			int l;
			int i2;
			double d14;
			if(this.renderAllFaces || flag) {
				flag2 = true;
				l = par1Block.getBlockTextureFromSideAndMetadata(1, j);
				float i1 = (float)BlockFluid.func_293_a(this.blockAccess, par2, par3, par4, material);
				if(i1 > -999.0F) {
					l = par1Block.getBlockTextureFromSideAndMetadata(2, j);
				}

				d2 -= d6;
				d3 -= d6;
				d4 -= d6;
				d5 -= d6;
				int k1 = (l & 15) << 4;
				i2 = l & 240;
				double j2 = ((double)k1 + 8.0D) / 256.0D;
				double l2 = ((double)i2 + 8.0D) / 256.0D;
				if(i1 < -999.0F) {
					i1 = 0.0F;
				} else {
					j2 = (double)((float)(k1 + 16) / 256.0F);
					l2 = (double)((float)(i2 + 16) / 256.0F);
				}

				double d10 = (double)(MathHelper.sin(i1) * 8.0F) / 256.0D;
				double d12 = (double)(MathHelper.cos(i1) * 8.0F) / 256.0D;
				tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
				float f9 = 1.0F;
				tessellator.setColorOpaque_F(f4 * f9 * f, f4 * f9 * f1, f4 * f9 * f2);
				d14 = 3.90625E-5D;
				tessellator.addVertexWithUV((double)(par2 + 0), (double)par3 + d2, (double)(par4 + 0), j2 - d12 - d10 + d14, l2 - d12 + d10 + d14);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)par3 + d3, (double)(par4 + 1), j2 - d12 + d10 + d14, l2 + d12 + d10 - d14);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)par3 + d4, (double)(par4 + 1), j2 + d12 + d10 - d14, l2 + d12 - d10 - d14);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)par3 + d5, (double)(par4 + 0), j2 + d12 - d10 - d14, l2 - d12 - d10 + d14);
			}

			if(this.renderAllFaces || flag1) {
				tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
				float f64 = 1.0F;
				tessellator.setColorOpaque_F(f3 * f64 * f, f3 * f64 * f1, f3 * f64 * f2);
				this.renderBottomFace(par1Block, (double)par2, (double)par3 + d6, (double)par4, par1Block.getBlockTextureFromSide(0));
				flag2 = true;
			}

			for(l = 0; l < 4; ++l) {
				int i65 = par2;
				i2 = par4;
				if(l == 0) {
					i2 = par4 - 1;
				}

				if(l == 1) {
					++i2;
				}

				if(l == 2) {
					i65 = par2 - 1;
				}

				if(l == 3) {
					++i65;
				}

				int i66 = par1Block.getBlockTextureFromSideAndMetadata(l + 2, j);
				int k2 = (i66 & 15) << 4;
				int i67 = i66 & 240;
				if(this.renderAllFaces || aflag[l]) {
					double d9;
					double d11;
					double d13;
					double d15;
					double d16;
					if(l == 0) {
						d9 = d2;
						d11 = d5;
						d13 = (double)par2;
						d15 = (double)(par2 + 1);
						d14 = (double)par4 + d6;
						d16 = (double)par4 + d6;
					} else if(l == 1) {
						d9 = d4;
						d11 = d3;
						d13 = (double)(par2 + 1);
						d15 = (double)par2;
						d14 = (double)(par4 + 1) - d6;
						d16 = (double)(par4 + 1) - d6;
					} else if(l == 2) {
						d9 = d3;
						d11 = d2;
						d13 = (double)par2 + d6;
						d15 = (double)par2 + d6;
						d14 = (double)(par4 + 1);
						d16 = (double)par4;
					} else {
						d9 = d5;
						d11 = d4;
						d13 = (double)(par2 + 1) - d6;
						d15 = (double)(par2 + 1) - d6;
						d14 = (double)par4;
						d16 = (double)(par4 + 1);
					}

					flag2 = true;
					double d17 = (double)((float)(k2 + 0) / 256.0F);
					double d18 = ((double)(k2 + 16) - 0.01D) / 256.0D;
					double d19 = ((double)i67 + (1.0D - d9) * 16.0D) / 256.0D;
					double d20 = ((double)i67 + (1.0D - d11) * 16.0D) / 256.0D;
					double d21 = ((double)(i67 + 16) - 0.01D) / 256.0D;
					tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, i65, par3, i2));
					float f10 = 1.0F;
					if(l < 2) {
						f10 *= f5;
					} else {
						f10 *= f6;
					}

					tessellator.setColorOpaque_F(f4 * f10 * f, f4 * f10 * f1, f4 * f10 * f2);
					tessellator.addVertexWithUV(d13, (double)par3 + d9, d14, d17, d19);
					tessellator.addVertexWithUV(d15, (double)par3 + d11, d16, d18, d20);
					tessellator.addVertexWithUV(d15, (double)(par3 + 0), d16, d18, d21);
					tessellator.addVertexWithUV(d13, (double)(par3 + 0), d14, d17, d21);
				}
			}

			par1Block.minY = d;
			par1Block.maxY = d1;
			return flag2;
		}
	}

	public float getFluidHeight(int par1, int par2, int par3, Material par4Material) {
		int i = 0;
		float f = 0.0F;

		for(int j = 0; j < 4; ++j) {
			int k = par1 - (j & 1);
			int i1 = par3 - (j >> 1 & 1);
			if(this.blockAccess.getBlockMaterial(k, par2 + 1, i1) == par4Material) {
				return 1.0F;
			}

			Material material = this.blockAccess.getBlockMaterial(k, par2, i1);
			if(material != par4Material) {
				if(!material.isSolid()) {
					++f;
					++i;
				}
			} else {
				int j1 = this.blockAccess.getBlockMetadata(k, par2, i1);
				if(j1 >= 8 || j1 == 0) {
					f += BlockFluid.getFluidHeightPercent(j1) * 10.0F;
					i += 10;
				}

				f += BlockFluid.getFluidHeightPercent(j1);
				++i;
			}
		}

		return 1.0F - f / (float)i;
	}

	public void renderBlockFallingSand(Block par1Block, World par2World, int par3, int par4, int par5) {
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(par2World, par3, par4, par5));
		float f4 = 1.0F;
		float f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
		this.renderBottomFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(0));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
		this.renderTopFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(1));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
		this.renderEastFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(2));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
		this.renderWestFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(3));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
		this.renderNorthFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(4));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
		this.renderSouthFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(5));
		tessellator.draw();
	}

	public boolean renderStandardBlock(Block par1Block, int par2, int par3, int par4) {
		int i = CustomColorizer.getColorMultiplier(par1Block, this.blockAccess, par2, par3, par4);
		float f = (float)(i >> 16 & 255) / 255.0F;
		float f1 = (float)(i >> 8 & 255) / 255.0F;
		float f2 = (float)(i & 255) / 255.0F;
		if(EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		return Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[par1Block.blockID] == 0 ? this.renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, f, f1, f2) : this.renderStandardBlockWithColorMultiplier(par1Block, par2, par3, par4, f, f1, f2);
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
		this.enableAO = true;
		boolean defaultTexture = Tessellator.instance.defaultTexture;
		boolean betterGrass = Config.isBetterGrass() && defaultTexture;
		if(par1Block.getClass() == BlockGlass.class) {
			this.aoType = 0;
		} else {
			this.aoType = 1;
		}

		boolean flag = false;
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		boolean flag4 = true;
		boolean flag5 = true;
		boolean flag6 = true;
		this.aoLightValuesCalculated = false;
		int i = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(983055);
		if(par1Block.blockIndexInTexture == 3) {
			flag6 = false;
			flag5 = false;
			flag4 = false;
			flag3 = false;
			flag1 = false;
		}

		if(this.overrideBlockTexture >= 0) {
			flag6 = false;
			flag5 = false;
			flag4 = false;
			flag3 = false;
			flag1 = false;
		}

		int i1;
		float f6;
		float f13;
		float f20;
		float f27;
		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) {
			if(!this.aoLightValuesCalculated) {
				this.calculateAoLightValues(par1Block, par2, par3, par4);
			}

			i1 = i;
			if(par1Block.minY <= 0.0D) {
				i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
			}

			if(this.aoType > 0) {
				if(par1Block.minY <= 0.0D) {
					--par3;
				}

				this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
				this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
				this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
				this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
				this.aoLightValueScratchXYNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
				this.aoLightValueScratchYZNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
				this.aoLightValueScratchYZNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
				this.aoLightValueScratchXYPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
				if(!this.aoGrassXYZCNN && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
				} else {
					this.aoLightValueScratchXYZNNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
					this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
				}

				if(!this.aoGrassXYZCNP && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
					this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
				} else {
					this.aoLightValueScratchXYZNNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
					this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
				}

				if(!this.aoGrassXYZCNN && !this.aoGrassXYZPNC) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
				} else {
					this.aoLightValueScratchXYZPNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
					this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
				}

				if(!this.aoGrassXYZCNP && !this.aoGrassXYZPNC) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
					this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
				} else {
					this.aoLightValueScratchXYZPNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
					this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
				}

				if(par1Block.minY <= 0.0D) {
					++par3;
				}

				f6 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + this.aoLightValueYNeg) / 4.0F;
				f27 = (this.aoLightValueScratchYZNP + this.aoLightValueYNeg + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
				f20 = (this.aoLightValueYNeg + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
				f13 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + this.aoLightValueYNeg + this.aoLightValueScratchYZNN) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i1);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i1);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i1);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i1);
			} else {
				f27 = this.aoLightValueYNeg;
				f20 = this.aoLightValueYNeg;
				f13 = this.aoLightValueYNeg;
				f6 = this.aoLightValueYNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i1;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag1 ? par5 : 1.0F) * 0.5F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag1 ? par6 : 1.0F) * 0.5F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag1 ? par7 : 1.0F) * 0.5F;
			this.colorRedTopLeft *= f6;
			this.colorGreenTopLeft *= f6;
			this.colorBlueTopLeft *= f6;
			this.colorRedBottomLeft *= f13;
			this.colorGreenBottomLeft *= f13;
			this.colorBlueBottomLeft *= f13;
			this.colorRedBottomRight *= f20;
			this.colorGreenBottomRight *= f20;
			this.colorBlueBottomRight *= f20;
			this.colorRedTopRight *= f27;
			this.colorGreenTopRight *= f27;
			this.colorBlueTopRight *= f27;
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) {
			if(!this.aoLightValuesCalculated) {
				this.calculateAoLightValues(par1Block, par2, par3, par4);
			}

			i1 = i;
			if(par1Block.maxY >= 1.0D) {
				i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
			}

			if(this.aoType > 0) {
				if(par1Block.maxY >= 1.0D) {
					++par3;
				}

				this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
				this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
				this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
				this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
				this.aoLightValueScratchXYNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
				this.aoLightValueScratchXYPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
				this.aoLightValueScratchYZPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
				this.aoLightValueScratchYZPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
				if(!this.aoGrassXYZCPN && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
					this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
				} else {
					this.aoLightValueScratchXYZNPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
					this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
				}

				if(!this.aoGrassXYZCPN && !this.aoGrassXYZPPC) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
					this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
				} else {
					this.aoLightValueScratchXYZPPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
					this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
				}

				if(!this.aoGrassXYZCPP && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
				} else {
					this.aoLightValueScratchXYZNPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
					this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
				}

				if(!this.aoGrassXYZCPP && !this.aoGrassXYZPPC) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
				} else {
					this.aoLightValueScratchXYZPPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
					this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
				}

				if(par1Block.maxY >= 1.0D) {
					--par3;
				}

				f27 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + this.aoLightValueYPos) / 4.0F;
				f6 = (this.aoLightValueScratchYZPP + this.aoLightValueYPos + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
				f13 = (this.aoLightValueYPos + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
				f20 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + this.aoLightValueYPos + this.aoLightValueScratchYZPN) / 4.0F;
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, i1);
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, i1);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, i1);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
			} else {
				f27 = this.aoLightValueYPos;
				f20 = this.aoLightValueYPos;
				f13 = this.aoLightValueYPos;
				f6 = this.aoLightValueYPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i1;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = flag2 ? par5 : 1.0F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = flag2 ? par6 : 1.0F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = flag2 ? par7 : 1.0F;
			this.colorRedTopLeft *= f6;
			this.colorGreenTopLeft *= f6;
			this.colorBlueTopLeft *= f6;
			this.colorRedBottomLeft *= f13;
			this.colorGreenBottomLeft *= f13;
			this.colorBlueBottomLeft *= f13;
			this.colorRedBottomRight *= f20;
			this.colorGreenBottomRight *= f20;
			this.colorBlueBottomRight *= f20;
			this.colorRedTopRight *= f27;
			this.colorGreenTopRight *= f27;
			this.colorBlueTopRight *= f27;
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
			flag = true;
		}

		int k2;
		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) {
			if(!this.aoLightValuesCalculated) {
				this.calculateAoLightValues(par1Block, par2, par3, par4);
			}

			i1 = i;
			if(par1Block.minZ <= 0.0D) {
				i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
			}

			if(this.aoType > 0) {
				if(par1Block.minZ <= 0.0D) {
					--par4;
				}

				this.aoLightValueScratchXZNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
				this.aoLightValueScratchYZNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
				this.aoLightValueScratchYZPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
				this.aoLightValueScratchXZPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
				this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
				this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
				this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
				this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
				if(!this.aoGrassXYZNCN && !this.aoGrassXYZCNN) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
					this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
				}

				if(!this.aoGrassXYZNCN && !this.aoGrassXYZCPN) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
					this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
				}

				if(!this.aoGrassXYZPCN && !this.aoGrassXYZCNN) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
					this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
				}

				if(!this.aoGrassXYZPCN && !this.aoGrassXYZCPN) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
					this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
				}

				if(par1Block.minZ <= 0.0D) {
					++par4;
				}

				f6 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + this.aoLightValueZNeg + this.aoLightValueScratchYZPN) / 4.0F;
				f13 = (this.aoLightValueZNeg + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
				f20 = (this.aoLightValueScratchYZNN + this.aoLightValueZNeg + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
				f27 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + this.aoLightValueZNeg) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, i1);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, i1);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, i1);
			} else {
				f27 = this.aoLightValueZNeg;
				f20 = this.aoLightValueZNeg;
				f13 = this.aoLightValueZNeg;
				f6 = this.aoLightValueZNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i1;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag3 ? par5 : 1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag3 ? par6 : 1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag3 ? par7 : 1.0F) * 0.8F;
			this.colorRedTopLeft *= f6;
			this.colorGreenTopLeft *= f6;
			this.colorBlueTopLeft *= f6;
			this.colorRedBottomLeft *= f13;
			this.colorGreenBottomLeft *= f13;
			this.colorBlueBottomLeft *= f13;
			this.colorRedBottomRight *= f20;
			this.colorGreenBottomRight *= f20;
			this.colorBlueBottomRight *= f20;
			this.colorRedTopRight *= f27;
			this.colorGreenTopRight *= f27;
			this.colorBlueTopRight *= f27;
			k2 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2);
			if(betterGrass) {
				k2 = this.fixAoSideGrassTexture(k2, par2, par3, par4, 2, par5, par6, par7);
			}

			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, k2);
			if(defaultTexture && fancyGrass && k2 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= par5;
				this.colorRedBottomLeft *= par5;
				this.colorRedBottomRight *= par5;
				this.colorRedTopRight *= par5;
				this.colorGreenTopLeft *= par6;
				this.colorGreenBottomLeft *= par6;
				this.colorGreenBottomRight *= par6;
				this.colorGreenTopRight *= par6;
				this.colorBlueTopLeft *= par7;
				this.colorBlueBottomLeft *= par7;
				this.colorBlueBottomRight *= par7;
				this.colorBlueTopRight *= par7;
				this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) {
			if(!this.aoLightValuesCalculated) {
				this.calculateAoLightValues(par1Block, par2, par3, par4);
			}

			i1 = i;
			if(par1Block.maxZ >= 1.0D) {
				i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
			}

			if(this.aoType > 0) {
				if(par1Block.maxZ >= 1.0D) {
					++par4;
				}

				this.aoLightValueScratchXZNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
				this.aoLightValueScratchXZPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
				this.aoLightValueScratchYZNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
				this.aoLightValueScratchYZPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
				this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
				this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
				this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
				this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
				if(!this.aoGrassXYZNCP && !this.aoGrassXYZCNP) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
					this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
				}

				if(!this.aoGrassXYZNCP && !this.aoGrassXYZCPP) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
					this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
				}

				if(!this.aoGrassXYZPCP && !this.aoGrassXYZCNP) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
					this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
				}

				if(!this.aoGrassXYZPCP && !this.aoGrassXYZCPP) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
					this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
				}

				if(par1Block.maxZ >= 1.0D) {
					--par4;
				}

				f6 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + this.aoLightValueZPos + this.aoLightValueScratchYZPP) / 4.0F;
				f27 = (this.aoLightValueZPos + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
				f20 = (this.aoLightValueScratchYZNP + this.aoLightValueZPos + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
				f13 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + this.aoLightValueZPos) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, i1);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, i1);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, i1);
			} else {
				f27 = this.aoLightValueZPos;
				f20 = this.aoLightValueZPos;
				f13 = this.aoLightValueZPos;
				f6 = this.aoLightValueZPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i1;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag4 ? par5 : 1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag4 ? par6 : 1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag4 ? par7 : 1.0F) * 0.8F;
			this.colorRedTopLeft *= f6;
			this.colorGreenTopLeft *= f6;
			this.colorBlueTopLeft *= f6;
			this.colorRedBottomLeft *= f13;
			this.colorGreenBottomLeft *= f13;
			this.colorBlueBottomLeft *= f13;
			this.colorRedBottomRight *= f20;
			this.colorGreenBottomRight *= f20;
			this.colorBlueBottomRight *= f20;
			this.colorRedTopRight *= f27;
			this.colorGreenTopRight *= f27;
			this.colorBlueTopRight *= f27;
			k2 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3);
			if(betterGrass) {
				k2 = this.fixAoSideGrassTexture(k2, par2, par3, par4, 3, par5, par6, par7);
			}

			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, k2);
			if(defaultTexture && fancyGrass && k2 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= par5;
				this.colorRedBottomLeft *= par5;
				this.colorRedBottomRight *= par5;
				this.colorRedTopRight *= par5;
				this.colorGreenTopLeft *= par6;
				this.colorGreenBottomLeft *= par6;
				this.colorGreenBottomRight *= par6;
				this.colorGreenTopRight *= par6;
				this.colorBlueTopLeft *= par7;
				this.colorBlueBottomLeft *= par7;
				this.colorBlueBottomRight *= par7;
				this.colorBlueTopRight *= par7;
				this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) {
			if(!this.aoLightValuesCalculated) {
				this.calculateAoLightValues(par1Block, par2, par3, par4);
			}

			i1 = i;
			if(par1Block.minX <= 0.0D) {
				i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
			}

			if(this.aoType > 0) {
				if(par1Block.minX <= 0.0D) {
					--par2;
				}

				this.aoLightValueScratchXYNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
				this.aoLightValueScratchXZNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
				this.aoLightValueScratchXZNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
				this.aoLightValueScratchXYNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
				this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
				this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
				this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
				this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
				if(!this.aoGrassXYZNCN && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
					this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
				}

				if(!this.aoGrassXYZNCP && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
					this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
				}

				if(!this.aoGrassXYZNCN && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
					this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
				}

				if(!this.aoGrassXYZNCP && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
					this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
				}

				if(par1Block.minX <= 0.0D) {
					++par2;
				}

				f27 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + this.aoLightValueXNeg + this.aoLightValueScratchXZNP) / 4.0F;
				f6 = (this.aoLightValueXNeg + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
				f13 = (this.aoLightValueScratchXZNN + this.aoLightValueXNeg + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
				f20 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + this.aoLightValueXNeg) / 4.0F;
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i1);
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i1);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i1);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i1);
			} else {
				f27 = this.aoLightValueXNeg;
				f20 = this.aoLightValueXNeg;
				f13 = this.aoLightValueXNeg;
				f6 = this.aoLightValueXNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i1;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag5 ? par5 : 1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag5 ? par6 : 1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag5 ? par7 : 1.0F) * 0.6F;
			this.colorRedTopLeft *= f6;
			this.colorGreenTopLeft *= f6;
			this.colorBlueTopLeft *= f6;
			this.colorRedBottomLeft *= f13;
			this.colorGreenBottomLeft *= f13;
			this.colorBlueBottomLeft *= f13;
			this.colorRedBottomRight *= f20;
			this.colorGreenBottomRight *= f20;
			this.colorBlueBottomRight *= f20;
			this.colorRedTopRight *= f27;
			this.colorGreenTopRight *= f27;
			this.colorBlueTopRight *= f27;
			k2 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4);
			if(betterGrass) {
				k2 = this.fixAoSideGrassTexture(k2, par2, par3, par4, 4, par5, par6, par7);
			}

			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, k2);
			if(defaultTexture && fancyGrass && k2 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= par5;
				this.colorRedBottomLeft *= par5;
				this.colorRedBottomRight *= par5;
				this.colorRedTopRight *= par5;
				this.colorGreenTopLeft *= par6;
				this.colorGreenBottomLeft *= par6;
				this.colorGreenBottomRight *= par6;
				this.colorGreenTopRight *= par6;
				this.colorBlueTopLeft *= par7;
				this.colorBlueBottomLeft *= par7;
				this.colorBlueBottomRight *= par7;
				this.colorBlueTopRight *= par7;
				this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) {
			if(!this.aoLightValuesCalculated) {
				this.calculateAoLightValues(par1Block, par2, par3, par4);
			}

			i1 = i;
			if(par1Block.maxX >= 1.0D) {
				i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
			}

			if(this.aoType > 0) {
				if(par1Block.maxX >= 1.0D) {
					++par2;
				}

				this.aoLightValueScratchXYPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
				this.aoLightValueScratchXZPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
				this.aoLightValueScratchXZPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
				this.aoLightValueScratchXYPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
				this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
				this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
				this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
				this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
				if(!this.aoGrassXYZPNC && !this.aoGrassXYZPCN) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPNN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
					this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
				}

				if(!this.aoGrassXYZPNC && !this.aoGrassXYZPCP) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPNP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
					this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
				}

				if(!this.aoGrassXYZPPC && !this.aoGrassXYZPCN) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPPN = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
					this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
				}

				if(!this.aoGrassXYZPPC && !this.aoGrassXYZPCP) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPPP = this.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
					this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
				}

				if(par1Block.maxX >= 1.0D) {
					--par2;
				}

				f6 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + this.aoLightValueXPos + this.aoLightValueScratchXZPP) / 4.0F;
				f27 = (this.aoLightValueXPos + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
				f20 = (this.aoLightValueScratchXZPN + this.aoLightValueXPos + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
				f13 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + this.aoLightValueXPos) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, i1);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, i1);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, i1);
			} else {
				f27 = this.aoLightValueXPos;
				f20 = this.aoLightValueXPos;
				f13 = this.aoLightValueXPos;
				f6 = this.aoLightValueXPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i1;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag6 ? par5 : 1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag6 ? par6 : 1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag6 ? par7 : 1.0F) * 0.6F;
			this.colorRedTopLeft *= f6;
			this.colorGreenTopLeft *= f6;
			this.colorBlueTopLeft *= f6;
			this.colorRedBottomLeft *= f13;
			this.colorGreenBottomLeft *= f13;
			this.colorBlueBottomLeft *= f13;
			this.colorRedBottomRight *= f20;
			this.colorGreenBottomRight *= f20;
			this.colorBlueBottomRight *= f20;
			this.colorRedTopRight *= f27;
			this.colorGreenTopRight *= f27;
			this.colorBlueTopRight *= f27;
			k2 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5);
			if(betterGrass) {
				k2 = this.fixAoSideGrassTexture(k2, par2, par3, par4, 5, par5, par6, par7);
			}

			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, k2);
			if(defaultTexture && fancyGrass && k2 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= par5;
				this.colorRedBottomLeft *= par5;
				this.colorRedBottomRight *= par5;
				this.colorRedTopRight *= par5;
				this.colorGreenTopLeft *= par6;
				this.colorGreenBottomLeft *= par6;
				this.colorGreenBottomRight *= par6;
				this.colorGreenTopRight *= par6;
				this.colorBlueTopLeft *= par7;
				this.colorBlueBottomLeft *= par7;
				this.colorBlueBottomRight *= par7;
				this.colorBlueTopRight *= par7;
				this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			flag = true;
		}

		this.enableAO = false;
		return flag;
	}

	private void calculateAoLightValues(Block block, int i, int j, int k) {
		this.aoLightValueXNeg = this.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k);
		this.aoLightValueYNeg = this.getAmbientOcclusionLightValue(this.blockAccess, i, j - 1, k);
		this.aoLightValueZNeg = this.getAmbientOcclusionLightValue(this.blockAccess, i, j, k - 1);
		this.aoLightValueXPos = this.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k);
		this.aoLightValueYPos = this.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k);
		this.aoLightValueZPos = this.getAmbientOcclusionLightValue(this.blockAccess, i, j, k + 1);
		this.aoGrassXYZPPC = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j + 1, k)];
		this.aoGrassXYZPNC = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j - 1, k)];
		this.aoGrassXYZPCP = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j, k + 1)];
		this.aoGrassXYZPCN = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j, k - 1)];
		this.aoGrassXYZNPC = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j + 1, k)];
		this.aoGrassXYZNNC = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j - 1, k)];
		this.aoGrassXYZNCN = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j, k - 1)];
		this.aoGrassXYZNCP = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j, k + 1)];
		this.aoGrassXYZCPP = Block.canBlockGrass[this.blockAccess.getBlockId(i, j + 1, k + 1)];
		this.aoGrassXYZCPN = Block.canBlockGrass[this.blockAccess.getBlockId(i, j + 1, k - 1)];
		this.aoGrassXYZCNP = Block.canBlockGrass[this.blockAccess.getBlockId(i, j - 1, k + 1)];
		this.aoGrassXYZCNN = Block.canBlockGrass[this.blockAccess.getBlockId(i, j - 1, k - 1)];
		this.aoLightValuesCalculated = true;
	}

	private float getAmbientOcclusionLightValue(IBlockAccess iBlockAccess, int i, int j, int k) {
		Block block = Block.blocksList[iBlockAccess.getBlockId(i, j, k)];
		return block == null ? 1.0F : (block.getClass() == BlockGlass.class ? 1.0F : (block.blockMaterial.blocksMovement() && block.renderAsNormalBlock() ? this.aoLightValueOpaque : 1.0F));
	}

	private int fixAoSideGrassTexture(int tex, int x, int y, int z, int side, float f, float f1, float f2) {
		if(tex == 3 || tex == 77) {
			tex = Config.getSideGrassTexture(this.blockAccess, x, y, z, side, tex);
			if(tex == 0) {
				this.colorRedTopLeft *= f;
				this.colorRedBottomLeft *= f;
				this.colorRedBottomRight *= f;
				this.colorRedTopRight *= f;
				this.colorGreenTopLeft *= f1;
				this.colorGreenBottomLeft *= f1;
				this.colorGreenBottomRight *= f1;
				this.colorGreenTopRight *= f1;
				this.colorBlueTopLeft *= f2;
				this.colorBlueBottomLeft *= f2;
				this.colorBlueBottomRight *= f2;
				this.colorBlueTopRight *= f2;
			}
		}

		if(tex == 68) {
			tex = Config.getSideSnowGrassTexture(this.blockAccess, x, y, z, side);
		}

		return tex;
	}

	public int getAoBrightness(int par1, int par2, int par3, int par4) {
		if(par1 == 0) {
			par1 = par4;
		}

		if(par2 == 0) {
			par2 = par4;
		}

		if(par3 == 0) {
			par3 = par4;
		}

		return par1 + par2 + par3 + par4 >> 2 & 16711935;
	}

	public boolean renderStandardBlockWithColorMultiplier(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
		this.enableAO = false;
		boolean defaultTexture = Tessellator.instance.defaultTexture;
		boolean betterGrass = Config.isBetterGrass() && defaultTexture;
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		float f4 = f1 * par5;
		float f5 = f1 * par6;
		float f6 = f1 * par7;
		float f7 = f;
		float f8 = f2;
		float f9 = f3;
		float f10 = f;
		float f11 = f2;
		float f12 = f3;
		float f13 = f;
		float f14 = f2;
		float f15 = f3;
		if(par1Block != Block.grass) {
			f7 = f * par5;
			f8 = f2 * par5;
			f9 = f3 * par5;
			f10 = f * par6;
			f11 = f2 * par6;
			f12 = f3 * par6;
			f13 = f * par7;
			f14 = f2 * par7;
			f15 = f3 * par7;
		}

		int i = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) {
			tessellator.setBrightness(par1Block.minY <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4) : i);
			tessellator.setColorOpaque_F(f7, f10, f13);
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) {
			tessellator.setBrightness(par1Block.maxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4) : i);
			tessellator.setColorOpaque_F(f4, f5, f6);
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
			flag = true;
		}

		int i1;
		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) {
			tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1) : i);
			tessellator.setColorOpaque_F(f8, f11, f14);
			i1 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2);
			if(betterGrass) {
				if(i1 == 3 || i1 == 77) {
					i1 = Config.getSideGrassTexture(this.blockAccess, par2, par3, par4, 2, i1);
					if(i1 == 0) {
						tessellator.setColorOpaque_F(f8 * par5, f11 * par6, f14 * par7);
					}
				}

				if(i1 == 68) {
					i1 = Config.getSideSnowGrassTexture(this.blockAccess, par2, par3, par4, 2);
				}
			}

			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, i1);
			if(defaultTexture && fancyGrass && i1 == 3 && this.overrideBlockTexture < 0) {
				tessellator.setColorOpaque_F(f8 * par5, f11 * par6, f14 * par7);
				this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) {
			tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1) : i);
			tessellator.setColorOpaque_F(f8, f11, f14);
			i1 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3);
			if(betterGrass) {
				if(i1 == 3 || i1 == 77) {
					i1 = Config.getSideGrassTexture(this.blockAccess, par2, par3, par4, 3, i1);
					if(i1 == 0) {
						tessellator.setColorOpaque_F(f8 * par5, f11 * par6, f14 * par7);
					}
				}

				if(i1 == 68) {
					i1 = Config.getSideSnowGrassTexture(this.blockAccess, par2, par3, par4, 3);
				}
			}

			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, i1);
			if(defaultTexture && fancyGrass && i1 == 3 && this.overrideBlockTexture < 0) {
				tessellator.setColorOpaque_F(f8 * par5, f11 * par6, f14 * par7);
				this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) {
			tessellator.setBrightness(par1Block.minX <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4) : i);
			tessellator.setColorOpaque_F(f9, f12, f15);
			i1 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4);
			if(betterGrass) {
				if(i1 == 3 || i1 == 77) {
					i1 = Config.getSideGrassTexture(this.blockAccess, par2, par3, par4, 4, i1);
					if(i1 == 0) {
						tessellator.setColorOpaque_F(f8 * par5, f11 * par6, f14 * par7);
					}
				}

				if(i1 == 68) {
					i1 = Config.getSideSnowGrassTexture(this.blockAccess, par2, par3, par4, 4);
				}
			}

			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, i1);
			if(defaultTexture && fancyGrass && i1 == 3 && this.overrideBlockTexture < 0) {
				tessellator.setColorOpaque_F(f9 * par5, f12 * par6, f15 * par7);
				this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) {
			tessellator.setBrightness(par1Block.maxX >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4) : i);
			tessellator.setColorOpaque_F(f9, f12, f15);
			i1 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5);
			if(betterGrass) {
				if(i1 == 3 || i1 == 77) {
					i1 = Config.getSideGrassTexture(this.blockAccess, par2, par3, par4, 5, i1);
					if(i1 == 0) {
						tessellator.setColorOpaque_F(f8 * par5, f11 * par6, f14 * par7);
					}
				}

				if(i1 == 68) {
					i1 = Config.getSideSnowGrassTexture(this.blockAccess, par2, par3, par4, 5);
				}
			}

			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, i1);
			if(defaultTexture && fancyGrass && i1 == 3 && this.overrideBlockTexture < 0) {
				tessellator.setColorOpaque_F(f9 * par5, f12 * par6, f15 * par7);
				this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			flag = true;
		}

		return flag;
	}

	public boolean renderBlockCactus(Block par1Block, int par2, int par3, int par4) {
		int i = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
		float f = (float)(i >> 16 & 255) / 255.0F;
		float f1 = (float)(i >> 8 & 255) / 255.0F;
		float f2 = (float)(i & 255) / 255.0F;
		if(EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		return this.renderBlockCactusImpl(par1Block, par2, par3, par4, f, f1, f2);
	}

	public boolean renderBlockCactusImpl(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		float f4 = f * par5;
		float f5 = f1 * par5;
		float f6 = f2 * par5;
		float f7 = f3 * par5;
		float f8 = f * par6;
		float f9 = f1 * par6;
		float f10 = f2 * par6;
		float f11 = f3 * par6;
		float f12 = f * par7;
		float f13 = f1 * par7;
		float f14 = f2 * par7;
		float f15 = f3 * par7;
		float f16 = 0.0625F;
		int i = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) {
			tessellator.setBrightness(par1Block.minY <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4) : i);
			tessellator.setColorOpaque_F(f4, f8, f12);
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) {
			tessellator.setBrightness(par1Block.maxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4) : i);
			tessellator.setColorOpaque_F(f5, f9, f13);
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) {
			tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1) : i);
			tessellator.setColorOpaque_F(f6, f10, f14);
			tessellator.addTranslation(0.0F, 0.0F, f16);
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2));
			tessellator.addTranslation(0.0F, 0.0F, -f16);
			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) {
			tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1) : i);
			tessellator.setColorOpaque_F(f6, f10, f14);
			tessellator.addTranslation(0.0F, 0.0F, -f16);
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3));
			tessellator.addTranslation(0.0F, 0.0F, f16);
			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) {
			tessellator.setBrightness(par1Block.minX <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4) : i);
			tessellator.setColorOpaque_F(f7, f11, f15);
			tessellator.addTranslation(f16, 0.0F, 0.0F);
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4));
			tessellator.addTranslation(-f16, 0.0F, 0.0F);
			flag = true;
		}

		if(this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) {
			tessellator.setBrightness(par1Block.maxX >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4) : i);
			tessellator.setColorOpaque_F(f7, f11, f15);
			tessellator.addTranslation(-f16, 0.0F, 0.0F);
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5));
			tessellator.addTranslation(f16, 0.0F, 0.0F);
			flag = true;
		}

		return flag;
	}

	public boolean renderBlockFence(BlockFence par1BlockFence, int par2, int par3, int par4) {
		boolean flag = false;
		float f = 0.375F;
		float f1 = 0.625F;
		par1BlockFence.setBlockBounds(f, 0.0F, f, f1, 1.0F, f1);
		this.renderStandardBlock(par1BlockFence, par2, par3, par4);
		flag = true;
		boolean flag1 = false;
		boolean flag2 = false;
		if(par1BlockFence.canConnectFenceTo(this.blockAccess, par2 - 1, par3, par4) || par1BlockFence.canConnectFenceTo(this.blockAccess, par2 + 1, par3, par4)) {
			flag1 = true;
		}

		if(par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 - 1) || par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 + 1)) {
			flag2 = true;
		}

		boolean flag3 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2 - 1, par3, par4);
		boolean flag4 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2 + 1, par3, par4);
		boolean flag5 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 - 1);
		boolean flag6 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 + 1);
		if(!flag1 && !flag2) {
			flag1 = true;
		}

		f = 0.4375F;
		f1 = 0.5625F;
		float f2 = 0.75F;
		float f3 = 0.9375F;
		float f4 = flag3 ? 0.0F : f;
		float f5 = flag4 ? 1.0F : f1;
		float f6 = flag5 ? 0.0F : f;
		float f7 = flag6 ? 1.0F : f1;
		if(flag1) {
			par1BlockFence.setBlockBounds(f4, f2, f, f5, f3, f1);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			flag = true;
		}

		if(flag2) {
			par1BlockFence.setBlockBounds(f, f2, f6, f1, f3, f7);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			flag = true;
		}

		f2 = 0.375F;
		f3 = 0.5625F;
		if(flag1) {
			par1BlockFence.setBlockBounds(f4, f2, f, f5, f3, f1);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			flag = true;
		}

		if(flag2) {
			par1BlockFence.setBlockBounds(f, f2, f6, f1, f3, f7);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			flag = true;
		}

		par1BlockFence.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
		if(Config.isBetterSnow() && this.hasSnowNeighbours(par2, par3, par4)) {
			this.renderStandardBlock(Block.snow, par2, par3, par4);
		}

		return flag;
	}

	public boolean renderBlockDragonEgg(BlockDragonEgg par1BlockDragonEgg, int par2, int par3, int par4) {
		boolean flag = false;
		int i = 0;

		for(int j = 0; j < 8; ++j) {
			byte k = 0;
			byte byte0 = 1;
			if(j == 0) {
				k = 2;
			}

			if(j == 1) {
				k = 3;
			}

			if(j == 2) {
				k = 4;
			}

			if(j == 3) {
				k = 5;
				byte0 = 2;
			}

			if(j == 4) {
				k = 6;
				byte0 = 3;
			}

			if(j == 5) {
				k = 7;
				byte0 = 5;
			}

			if(j == 6) {
				k = 6;
				byte0 = 2;
			}

			if(j == 7) {
				k = 3;
			}

			float f = (float)k / 16.0F;
			float f1 = 1.0F - (float)i / 16.0F;
			float f2 = 1.0F - (float)(i + byte0) / 16.0F;
			i += byte0;
			par1BlockDragonEgg.setBlockBounds(0.5F - f, f2, 0.5F - f, 0.5F + f, f1, 0.5F + f);
			this.renderStandardBlock(par1BlockDragonEgg, par2, par3, par4);
		}

		flag = true;
		par1BlockDragonEgg.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return flag;
	}

	public boolean renderBlockFenceGate(BlockFenceGate par1BlockFenceGate, int par2, int par3, int par4) {
		boolean flag = true;
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		boolean flag1 = BlockFenceGate.isFenceGateOpen(i);
		int j = BlockDirectional.getDirection(i);
		float f3;
		float f7;
		float f11;
		float f15;
		if(j != 3 && j != 1) {
			f3 = 0.0F;
			f7 = 0.125F;
			f11 = 0.4375F;
			f15 = 0.5625F;
			par1BlockFenceGate.setBlockBounds(f3, 0.3125F, f11, f7, 1.0F, f15);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			f3 = 0.875F;
			f7 = 1.0F;
			par1BlockFenceGate.setBlockBounds(f3, 0.3125F, f11, f7, 1.0F, f15);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else {
			f3 = 0.4375F;
			f7 = 0.5625F;
			f11 = 0.0F;
			f15 = 0.125F;
			par1BlockFenceGate.setBlockBounds(f3, 0.3125F, f11, f7, 1.0F, f15);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			f11 = 0.875F;
			f15 = 1.0F;
			par1BlockFenceGate.setBlockBounds(f3, 0.3125F, f11, f7, 1.0F, f15);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		}

		if(!flag1) {
			if(j != 3 && j != 1) {
				f3 = 0.375F;
				f7 = 0.5F;
				f11 = 0.4375F;
				f15 = 0.5625F;
				par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.9375F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				f3 = 0.5F;
				f7 = 0.625F;
				par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.9375F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				f3 = 0.625F;
				f7 = 0.875F;
				par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.5625F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				par1BlockFenceGate.setBlockBounds(f3, 0.75F, f11, f7, 0.9375F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				f3 = 0.125F;
				f7 = 0.375F;
				par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.5625F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				par1BlockFenceGate.setBlockBounds(f3, 0.75F, f11, f7, 0.9375F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			} else {
				f3 = 0.4375F;
				f7 = 0.5625F;
				f11 = 0.375F;
				f15 = 0.5F;
				par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.9375F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				f11 = 0.5F;
				f15 = 0.625F;
				par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.9375F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				f11 = 0.625F;
				f15 = 0.875F;
				par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.5625F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				par1BlockFenceGate.setBlockBounds(f3, 0.75F, f11, f7, 0.9375F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				f11 = 0.125F;
				f15 = 0.375F;
				par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.5625F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				par1BlockFenceGate.setBlockBounds(f3, 0.75F, f11, f7, 0.9375F, f15);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			}
		} else if(j == 3) {
			par1BlockFenceGate.setBlockBounds(0.8125F, 0.375F, 0.0F, 0.9375F, 0.9375F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.8125F, 0.375F, 0.875F, 0.9375F, 0.9375F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.5625F, 0.375F, 0.0F, 0.8125F, 0.5625F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.5625F, 0.375F, 0.875F, 0.8125F, 0.5625F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.5625F, 0.75F, 0.0F, 0.8125F, 0.9375F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.5625F, 0.75F, 0.875F, 0.8125F, 0.9375F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else if(j == 1) {
			par1BlockFenceGate.setBlockBounds(0.0625F, 0.375F, 0.0F, 0.1875F, 0.9375F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0625F, 0.375F, 0.875F, 0.1875F, 0.9375F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.1875F, 0.375F, 0.0F, 0.4375F, 0.5625F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.1875F, 0.375F, 0.875F, 0.4375F, 0.5625F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.1875F, 0.75F, 0.0F, 0.4375F, 0.9375F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.1875F, 0.75F, 0.875F, 0.4375F, 0.9375F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else if(j == 0) {
			par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.8125F, 0.125F, 0.9375F, 0.9375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.8125F, 1.0F, 0.9375F, 0.9375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.5625F, 0.125F, 0.5625F, 0.8125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.5625F, 1.0F, 0.5625F, 0.8125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0F, 0.75F, 0.5625F, 0.125F, 0.9375F, 0.8125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.75F, 0.5625F, 1.0F, 0.9375F, 0.8125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else if(j == 2) {
			par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.0625F, 0.125F, 0.9375F, 0.1875F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.0625F, 1.0F, 0.9375F, 0.1875F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.1875F, 0.125F, 0.5625F, 0.4375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.1875F, 1.0F, 0.5625F, 0.4375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0F, 0.75F, 0.1875F, 0.125F, 0.9375F, 0.4375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.75F, 0.1875F, 1.0F, 0.9375F, 0.4375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		}

		par1BlockFenceGate.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return flag;
	}

	public boolean renderBlockStairs(Block par1Block, int par2, int par3, int par4) {
		int i = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int j = i & 3;
		float f = 0.0F;
		float f1 = 0.5F;
		float f2 = 0.5F;
		float f3 = 1.0F;
		if((i & 4) != 0) {
			f = 0.5F;
			f1 = 1.0F;
			f2 = 0.0F;
			f3 = 0.5F;
		}

		par1Block.setBlockBounds(0.0F, f, 0.0F, 1.0F, f1, 1.0F);
		this.renderStandardBlock(par1Block, par2, par3, par4);
		if(j == 0) {
			par1Block.setBlockBounds(0.5F, f2, 0.0F, 1.0F, f3, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
		} else if(j == 1) {
			par1Block.setBlockBounds(0.0F, f2, 0.0F, 0.5F, f3, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
		} else if(j == 2) {
			par1Block.setBlockBounds(0.0F, f2, 0.5F, 1.0F, f3, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
		} else if(j == 3) {
			par1Block.setBlockBounds(0.0F, f2, 0.0F, 1.0F, f3, 0.5F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
		}

		par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockDoor(Block par1Block, int par2, int par3, int par4) {
		Tessellator tessellator = Tessellator.instance;
		BlockDoor blockdoor = (BlockDoor)par1Block;
		boolean flag = false;
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		int i = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		tessellator.setBrightness(par1Block.minY <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4) : i);
		tessellator.setColorOpaque_F(f, f, f);
		this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
		flag = true;
		tessellator.setBrightness(par1Block.maxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4) : i);
		tessellator.setColorOpaque_F(f1, f1, f1);
		this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
		flag = true;
		tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1) : i);
		tessellator.setColorOpaque_F(f2, f2, f2);
		int j = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2);
		if(j < 0) {
			this.flipTexture = true;
			j = -j;
		}

		this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, j);
		flag = true;
		this.flipTexture = false;
		tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1) : i);
		tessellator.setColorOpaque_F(f2, f2, f2);
		j = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3);
		if(j < 0) {
			this.flipTexture = true;
			j = -j;
		}

		this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, j);
		flag = true;
		this.flipTexture = false;
		tessellator.setBrightness(par1Block.minX <= 0.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4) : i);
		tessellator.setColorOpaque_F(f3, f3, f3);
		j = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4);
		if(j < 0) {
			this.flipTexture = true;
			j = -j;
		}

		this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, j);
		flag = true;
		this.flipTexture = false;
		tessellator.setBrightness(par1Block.maxX >= 1.0D ? par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4) : i);
		tessellator.setColorOpaque_F(f3, f3, f3);
		j = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5);
		if(j < 0) {
			this.flipTexture = true;
			j = -j;
		}

		this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, j);
		flag = true;
		this.flipTexture = false;
		return flag;
	}

	public void renderBottomFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int rand;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			int minX = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, (int)par2, (int)par4, (int)par6, 0, par8);
			if(minX >= 0) {
				rand = minX / 256;
				tessellator = Tessellator.instance.getSubTessellator(rand);
				par8 = minX % 256;
			}
		}

		if(Config.isNaturalTextures() && this.overrideBlockTexture < 0) {
			NaturalProperties minX1 = NaturalTextures.getNaturalProperties(tessellator.textureID, par8);
			if(minX1 != null) {
				rand = Config.getRandom((int)par2, (int)par4, (int)par6, 0);
				if(minX1.rotation > 1) {
					this.uvRotateBottom = rand & 3;
				}

				if(minX1.rotation == 2) {
					this.uvRotateBottom = this.uvRotateBottom / 2 * 3;
				}

				if(minX1.flip) {
					this.flipTexture = (rand & 4) != 0;
				}
			}
		}

		double minX2 = par1Block.minX;
		double maxX = par1Block.maxX;
		double minZ = par1Block.minZ;
		double maxZ = par1Block.maxZ;
		if(minX2 < 0.0D || maxX > 1.0D) {
			minX2 = 0.0D;
			maxX = 1.0D;
		}

		if(minZ < 0.0D || maxZ > 1.0D) {
			minZ = 0.0D;
			maxZ = 1.0D;
		}

		int i = (par8 & 15) << 4;
		int j = par8 & 240;
		double d = ((double)i + minX2 * 16.0D) / 256.0D;
		double d1 = ((double)i + maxX * 16.0D - 0.01D) / 256.0D;
		double d2 = ((double)j + minZ * 16.0D) / 256.0D;
		double d3 = ((double)j + maxZ * 16.0D - 0.01D) / 256.0D;
		double d4;
		if(this.flipTexture) {
			d4 = d;
			d = d1;
			d1 = d4;
		}

		d4 = d1;
		double d5 = d;
		double d6 = d2;
		double d7 = d3;
		double d8;
		if(this.uvRotateBottom == 2) {
			d = ((double)i + minZ * 16.0D) / 256.0D;
			d2 = ((double)(j + 16) - maxX * 16.0D) / 256.0D;
			d1 = ((double)i + maxZ * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)(j + 16) - minX2 * 16.0D - 0.01D) / 256.0D;
			if(this.flipTexture) {
				d8 = d;
				d = d1;
				d1 = d8;
			}

			d6 = d2;
			d7 = d3;
			d4 = d;
			d5 = d1;
			d2 = d3;
			d3 = d6;
		} else if(this.uvRotateBottom == 1) {
			d = ((double)(i + 16) - maxZ * 16.0D) / 256.0D;
			d2 = ((double)j + minX2 * 16.0D) / 256.0D;
			d1 = ((double)(i + 16) - minZ * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + maxX * 16.0D - 0.01D) / 256.0D;
			if(this.flipTexture) {
				d8 = d;
				d = d1;
				d1 = d8;
			}

			d4 = d1;
			d5 = d;
			d = d1;
			d1 = d5;
			d6 = d3;
			d7 = d2;
		} else if(this.uvRotateBottom == 3) {
			d = ((double)(i + 16) - minX2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)(i + 16) - maxX * 16.0D) / 256.0D;
			d2 = ((double)(j + 16) - minZ * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)(j + 16) - maxZ * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d8 = d;
				d = d1;
				d1 = d8;
			}

			d4 = d1;
			d5 = d;
			d6 = d2;
			d7 = d3;
		}

		this.uvRotateBottom = 0;
		this.flipTexture = false;
		d8 = par2 + par1Block.minX;
		double d9 = par2 + par1Block.maxX;
		double d10 = par4 + par1Block.minY;
		double d11 = par6 + par1Block.minZ;
		double d12 = par6 + par1Block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.setBrightness(this.brightnessTopLeft);
			tessellator.addVertexWithUV(d8, d10, d12, d5, d7);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.setBrightness(this.brightnessBottomLeft);
			tessellator.addVertexWithUV(d8, d10, d11, d, d2);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.setBrightness(this.brightnessBottomRight);
			tessellator.addVertexWithUV(d9, d10, d11, d4, d6);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.setBrightness(this.brightnessTopRight);
			tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
		} else {
			tessellator.addVertexWithUV(d8, d10, d12, d5, d7);
			tessellator.addVertexWithUV(d8, d10, d11, d, d2);
			tessellator.addVertexWithUV(d9, d10, d11, d4, d6);
			tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
		}

	}

	public void renderTopFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int rand;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			int minX = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, (int)par2, (int)par4, (int)par6, 1, par8);
			if(minX >= 0) {
				rand = minX / 256;
				tessellator = Tessellator.instance.getSubTessellator(rand);
				par8 = minX % 256;
			}
		}

		if(Config.isNaturalTextures() && this.overrideBlockTexture < 0) {
			NaturalProperties minX1 = NaturalTextures.getNaturalProperties(tessellator.textureID, par8);
			if(minX1 != null) {
				rand = Config.getRandom((int)par2, (int)par4, (int)par6, 1);
				if(minX1.rotation > 1) {
					this.uvRotateTop = rand & 3;
				}

				if(minX1.rotation == 2) {
					this.uvRotateTop = this.uvRotateTop / 2 * 3;
				}

				if(minX1.flip) {
					this.flipTexture = (rand & 4) != 0;
				}
			}
		}

		double minX2 = par1Block.minX;
		double maxX = par1Block.maxX;
		double minZ = par1Block.minZ;
		double maxZ = par1Block.maxZ;
		if(minX2 < 0.0D || maxX > 1.0D) {
			minX2 = 0.0D;
			maxX = 1.0D;
		}

		if(minZ < 0.0D || maxZ > 1.0D) {
			minZ = 0.0D;
			maxZ = 1.0D;
		}

		int i = (par8 & 15) << 4;
		int j = par8 & 240;
		double d = ((double)i + minX2 * 16.0D) / 256.0D;
		double d1 = ((double)i + maxX * 16.0D - 0.01D) / 256.0D;
		double d2 = ((double)j + minZ * 16.0D) / 256.0D;
		double d3 = ((double)j + maxZ * 16.0D - 0.01D) / 256.0D;
		double d4;
		if(this.flipTexture) {
			d4 = d;
			d = d1;
			d1 = d4;
		}

		d4 = d1;
		double d5 = d;
		double d6 = d2;
		double d7 = d3;
		double d8;
		if(this.uvRotateTop == 1) {
			d = ((double)i + minZ * 16.0D) / 256.0D;
			d2 = ((double)(j + 16) - maxX * 16.0D) / 256.0D;
			d1 = ((double)i + maxZ * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)(j + 16) - minX2 * 16.0D - 0.01D) / 256.0D;
			if(this.flipTexture) {
				d8 = d;
				d = d1;
				d1 = d8;
			}

			d6 = d2;
			d7 = d3;
			d4 = d;
			d5 = d1;
			d2 = d3;
			d3 = d6;
		} else if(this.uvRotateTop == 2) {
			d = ((double)(i + 16) - maxZ * 16.0D) / 256.0D;
			d2 = ((double)j + minX2 * 16.0D) / 256.0D;
			d1 = ((double)(i + 16) - minZ * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + maxX * 16.0D - 0.01D) / 256.0D;
			if(this.flipTexture) {
				d8 = d;
				d = d1;
				d1 = d8;
			}

			d4 = d1;
			d5 = d;
			d = d1;
			d1 = d5;
			d6 = d3;
			d7 = d2;
		} else if(this.uvRotateTop == 3) {
			d = ((double)(i + 16) - minX2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)(i + 16) - maxX * 16.0D) / 256.0D;
			d2 = ((double)(j + 16) - minZ * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)(j + 16) - maxZ * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d8 = d;
				d = d1;
				d1 = d8;
			}

			d4 = d1;
			d5 = d;
			d6 = d2;
			d7 = d3;
		}

		this.uvRotateTop = 0;
		this.flipTexture = false;
		d8 = par2 + par1Block.minX;
		double d9 = par2 + par1Block.maxX;
		double d10 = par4 + par1Block.maxY;
		double d11 = par6 + par1Block.minZ;
		double d12 = par6 + par1Block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.setBrightness(this.brightnessTopLeft);
			tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.setBrightness(this.brightnessBottomLeft);
			tessellator.addVertexWithUV(d9, d10, d11, d4, d6);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.setBrightness(this.brightnessBottomRight);
			tessellator.addVertexWithUV(d8, d10, d11, d, d2);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.setBrightness(this.brightnessTopRight);
			tessellator.addVertexWithUV(d8, d10, d12, d5, d7);
		} else {
			tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
			tessellator.addVertexWithUV(d9, d10, d11, d4, d6);
			tessellator.addVertexWithUV(d8, d10, d11, d, d2);
			tessellator.addVertexWithUV(d8, d10, d12, d5, d7);
		}

	}

	public void renderEastFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int rand;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			int minX = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, (int)par2, (int)par4, (int)par6, 2, par8);
			if(minX >= 0) {
				rand = minX / 256;
				tessellator = Tessellator.instance.getSubTessellator(rand);
				par8 = minX % 256;
			}
		}

		if(Config.isNaturalTextures() && this.overrideBlockTexture < 0) {
			NaturalProperties minX1 = NaturalTextures.getNaturalProperties(tessellator.textureID, par8);
			if(minX1 != null) {
				rand = Config.getRandom((int)par2, (int)par4, (int)par6, 2);
				if(minX1.rotation > 1) {
					this.uvRotateEast = rand & 3;
				}

				if(minX1.rotation == 2) {
					this.uvRotateEast = this.uvRotateEast / 2 * 3;
				}

				if(minX1.flip) {
					this.flipTexture = (rand & 4) != 0;
				}
			}
		}

		double minX2 = par1Block.minX;
		double maxX = par1Block.maxX;
		double minY = par1Block.minY;
		double maxY = par1Block.maxY;
		if(minX2 < 0.0D || maxX > 1.0D) {
			minX2 = 0.0D;
			maxX = 1.0D;
		}

		if(minY < 0.0D || maxY > 1.0D) {
			minY = 0.0D;
			maxY = 1.0D;
		}

		int i = (par8 & 15) << 4;
		int j = par8 & 240;
		double d = ((double)i + minX2 * 16.0D) / 256.0D;
		double d1 = ((double)i + maxX * 16.0D - 0.01D) / 256.0D;
		double d2 = ((double)(j + 16) - maxY * 16.0D) / 256.0D;
		double d3 = ((double)(j + 16) - minY * 16.0D - 0.01D) / 256.0D;
		double d5;
		if(this.flipTexture) {
			d5 = d;
			d = d1;
			d1 = d5;
		}

		d5 = d1;
		double d6 = d;
		double d7 = d2;
		double d8 = d3;
		double d9;
		if(this.uvRotateEast == 2) {
			d = ((double)i + minY * 16.0D) / 256.0D;
			d2 = ((double)(j + 16) - minX2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)i + maxY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)(j + 16) - maxX * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d7 = d2;
			d8 = d3;
			d5 = d;
			d6 = d1;
			d2 = d3;
			d3 = d7;
		} else if(this.uvRotateEast == 1) {
			d = ((double)(i + 16) - maxY * 16.0D) / 256.0D;
			d2 = ((double)j + maxX * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)(i + 16) - minY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + minX2 * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d5 = d1;
			d6 = d;
			d = d1;
			d1 = d6;
			d7 = d3;
			d8 = d2;
		} else if(this.uvRotateEast == 3) {
			d = ((double)(i + 16) - minX2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)(i + 16) - maxX * 16.0D) / 256.0D;
			d2 = ((double)j + maxY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + minY * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d5 = d1;
			d6 = d;
			d7 = d2;
			d8 = d3;
		}

		this.uvRotateEast = 0;
		this.flipTexture = false;
		d9 = par2 + par1Block.minX;
		double d10 = par2 + par1Block.maxX;
		double d11 = par4 + par1Block.minY;
		double d12 = par4 + par1Block.maxY;
		double d13 = par6 + par1Block.minZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.setBrightness(this.brightnessTopLeft);
			tessellator.addVertexWithUV(d9, d12, d13, d5, d7);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.setBrightness(this.brightnessBottomLeft);
			tessellator.addVertexWithUV(d10, d12, d13, d, d2);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.setBrightness(this.brightnessBottomRight);
			tessellator.addVertexWithUV(d10, d11, d13, d6, d8);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.setBrightness(this.brightnessTopRight);
			tessellator.addVertexWithUV(d9, d11, d13, d1, d3);
		} else {
			tessellator.addVertexWithUV(d9, d12, d13, d5, d7);
			tessellator.addVertexWithUV(d10, d12, d13, d, d2);
			tessellator.addVertexWithUV(d10, d11, d13, d6, d8);
			tessellator.addVertexWithUV(d9, d11, d13, d1, d3);
		}

	}

	public void renderWestFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int rand;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			int minX = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, (int)par2, (int)par4, (int)par6, 3, par8);
			if(minX >= 0) {
				rand = minX / 256;
				tessellator = Tessellator.instance.getSubTessellator(rand);
				par8 = minX % 256;
			}
		}

		if(Config.isNaturalTextures() && this.overrideBlockTexture < 0) {
			NaturalProperties minX1 = NaturalTextures.getNaturalProperties(tessellator.textureID, par8);
			if(minX1 != null) {
				rand = Config.getRandom((int)par2, (int)par4, (int)par6, 3);
				if(minX1.rotation > 1) {
					this.uvRotateWest = rand & 3;
				}

				if(minX1.rotation == 2) {
					this.uvRotateWest = this.uvRotateWest / 2 * 3;
				}

				if(minX1.flip) {
					this.flipTexture = (rand & 4) != 0;
				}
			}
		}

		double minX2 = par1Block.minX;
		double maxX = par1Block.maxX;
		double minY = par1Block.minY;
		double maxY = par1Block.maxY;
		if(minX2 < 0.0D || maxX > 1.0D) {
			minX2 = 0.0D;
			maxX = 1.0D;
		}

		if(minY < 0.0D || maxY > 1.0D) {
			minY = 0.0D;
			maxY = 1.0D;
		}

		int i = (par8 & 15) << 4;
		int j = par8 & 240;
		double d = ((double)i + minX2 * 16.0D) / 256.0D;
		double d1 = ((double)i + maxX * 16.0D - 0.01D) / 256.0D;
		double d2 = ((double)(j + 16) - maxY * 16.0D) / 256.0D;
		double d3 = ((double)(j + 16) - minY * 16.0D - 0.01D) / 256.0D;
		double d5;
		if(this.flipTexture) {
			d5 = d;
			d = d1;
			d1 = d5;
		}

		d5 = d1;
		double d6 = d;
		double d7 = d2;
		double d8 = d3;
		double d9;
		if(this.uvRotateWest == 1) {
			d = ((double)i + minY * 16.0D) / 256.0D;
			d3 = ((double)(j + 16) - minX2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)i + maxY * 16.0D - 0.01D) / 256.0D;
			d2 = ((double)(j + 16) - maxX * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d7 = d2;
			d8 = d3;
			d5 = d;
			d6 = d1;
			d2 = d3;
			d3 = d7;
		} else if(this.uvRotateWest == 2) {
			d = ((double)(i + 16) - maxY * 16.0D) / 256.0D;
			d2 = ((double)j + minX2 * 16.0D) / 256.0D;
			d1 = ((double)(i + 16) - minY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + maxX * 16.0D - 0.01D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d5 = d1;
			d6 = d;
			d = d1;
			d1 = d6;
			d7 = d3;
			d8 = d2;
		} else if(this.uvRotateWest == 3) {
			d = ((double)(i + 16) - minX2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)(i + 16) - maxX * 16.0D) / 256.0D;
			d2 = ((double)j + maxY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + minY * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d5 = d1;
			d6 = d;
			d7 = d2;
			d8 = d3;
		}

		this.uvRotateWest = 0;
		this.flipTexture = false;
		d9 = par2 + par1Block.minX;
		double d10 = par2 + par1Block.maxX;
		double d11 = par4 + par1Block.minY;
		double d12 = par4 + par1Block.maxY;
		double d13 = par6 + par1Block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.setBrightness(this.brightnessTopLeft);
			tessellator.addVertexWithUV(d9, d12, d13, d, d2);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.setBrightness(this.brightnessBottomLeft);
			tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.setBrightness(this.brightnessBottomRight);
			tessellator.addVertexWithUV(d10, d11, d13, d1, d3);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.setBrightness(this.brightnessTopRight);
			tessellator.addVertexWithUV(d10, d12, d13, d5, d7);
		} else {
			tessellator.addVertexWithUV(d9, d12, d13, d, d2);
			tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
			tessellator.addVertexWithUV(d10, d11, d13, d1, d3);
			tessellator.addVertexWithUV(d10, d12, d13, d5, d7);
		}

	}

	public void renderNorthFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int rand;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			int minZ = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, (int)par2, (int)par4, (int)par6, 4, par8);
			if(minZ >= 0) {
				rand = minZ / 256;
				tessellator = Tessellator.instance.getSubTessellator(rand);
				par8 = minZ % 256;
			}
		}

		if(Config.isNaturalTextures() && this.overrideBlockTexture < 0) {
			NaturalProperties minZ1 = NaturalTextures.getNaturalProperties(tessellator.textureID, par8);
			if(minZ1 != null) {
				rand = Config.getRandom((int)par2, (int)par4, (int)par6, 4);
				if(minZ1.rotation > 1) {
					this.uvRotateNorth = rand & 3;
				}

				if(minZ1.rotation == 2) {
					this.uvRotateNorth = this.uvRotateNorth / 2 * 3;
				}

				if(minZ1.flip) {
					this.flipTexture = (rand & 4) != 0;
				}
			}
		}

		double minZ2 = par1Block.minZ;
		double maxZ = par1Block.maxZ;
		double minY = par1Block.minY;
		double maxY = par1Block.maxY;
		if(minZ2 < 0.0D || maxZ > 1.0D) {
			minZ2 = 0.0D;
			maxZ = 1.0D;
		}

		if(minY < 0.0D || maxY > 1.0D) {
			minY = 0.0D;
			maxY = 1.0D;
		}

		int i = (par8 & 15) << 4;
		int j = par8 & 240;
		double d = ((double)i + minZ2 * 16.0D) / 256.0D;
		double d1 = ((double)i + maxZ * 16.0D - 0.01D) / 256.0D;
		double d2 = ((double)(j + 16) - maxY * 16.0D) / 256.0D;
		double d3 = ((double)(j + 16) - minY * 16.0D - 0.01D) / 256.0D;
		double d5;
		if(this.flipTexture) {
			d5 = d;
			d = d1;
			d1 = d5;
		}

		d5 = d1;
		double d6 = d;
		double d7 = d2;
		double d8 = d3;
		double d9;
		if(this.uvRotateNorth == 1) {
			d = ((double)i + minY * 16.0D) / 256.0D;
			d2 = ((double)(j + 16) - maxZ * 16.0D) / 256.0D;
			d1 = ((double)i + maxY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)(j + 16) - minZ2 * 16.0D - 0.01D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d7 = d2;
			d8 = d3;
			d5 = d;
			d6 = d1;
			d2 = d3;
			d3 = d7;
		} else if(this.uvRotateNorth == 2) {
			d = ((double)(i + 16) - maxY * 16.0D) / 256.0D;
			d2 = ((double)j + minZ2 * 16.0D) / 256.0D;
			d1 = ((double)(i + 16) - minY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + maxZ * 16.0D - 0.01D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d5 = d1;
			d6 = d;
			d = d1;
			d1 = d6;
			d7 = d3;
			d8 = d2;
		} else if(this.uvRotateNorth == 3) {
			d = ((double)(i + 16) - minZ2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)(i + 16) - maxZ * 16.0D) / 256.0D;
			d2 = ((double)j + maxY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + minY * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d5 = d1;
			d6 = d;
			d7 = d2;
			d8 = d3;
		}

		this.uvRotateNorth = 0;
		this.flipTexture = false;
		d9 = par2 + par1Block.minX;
		double d10 = par4 + par1Block.minY;
		double d11 = par4 + par1Block.maxY;
		double d12 = par6 + par1Block.minZ;
		double d13 = par6 + par1Block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.setBrightness(this.brightnessTopLeft);
			tessellator.addVertexWithUV(d9, d11, d13, d5, d7);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.setBrightness(this.brightnessBottomLeft);
			tessellator.addVertexWithUV(d9, d11, d12, d, d2);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.setBrightness(this.brightnessBottomRight);
			tessellator.addVertexWithUV(d9, d10, d12, d6, d8);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.setBrightness(this.brightnessTopRight);
			tessellator.addVertexWithUV(d9, d10, d13, d1, d3);
		} else {
			tessellator.addVertexWithUV(d9, d11, d13, d5, d7);
			tessellator.addVertexWithUV(d9, d11, d12, d, d2);
			tessellator.addVertexWithUV(d9, d10, d12, d6, d8);
			tessellator.addVertexWithUV(d9, d10, d13, d1, d3);
		}

	}

	public void renderSouthFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int rand;
		if(Config.isConnectedTextures() && this.overrideBlockTexture < 0) {
			int minZ = ConnectedTextures.getConnectedTexture(this.blockAccess, par1Block, (int)par2, (int)par4, (int)par6, 5, par8);
			if(minZ >= 0) {
				rand = minZ / 256;
				tessellator = Tessellator.instance.getSubTessellator(rand);
				par8 = minZ % 256;
			}
		}

		if(Config.isNaturalTextures() && this.overrideBlockTexture < 0) {
			NaturalProperties minZ1 = NaturalTextures.getNaturalProperties(tessellator.textureID, par8);
			if(minZ1 != null) {
				rand = Config.getRandom((int)par2, (int)par4, (int)par6, 5);
				if(minZ1.rotation > 1) {
					this.uvRotateSouth = rand & 3;
				}

				if(minZ1.rotation == 2) {
					this.uvRotateSouth = this.uvRotateSouth / 2 * 3;
				}

				if(minZ1.flip) {
					this.flipTexture = (rand & 4) != 0;
				}
			}
		}

		double minZ2 = par1Block.minZ;
		double maxZ = par1Block.maxZ;
		double minY = par1Block.minY;
		double maxY = par1Block.maxY;
		if(minZ2 < 0.0D || maxZ > 1.0D) {
			minZ2 = 0.0D;
			maxZ = 1.0D;
		}

		if(minY < 0.0D || maxY > 1.0D) {
			minY = 0.0D;
			maxY = 1.0D;
		}

		int i = (par8 & 15) << 4;
		int j = par8 & 240;
		double d = ((double)i + minZ2 * 16.0D) / 256.0D;
		double d1 = ((double)i + maxZ * 16.0D - 0.01D) / 256.0D;
		double d2 = ((double)(j + 16) - maxY * 16.0D) / 256.0D;
		double d3 = ((double)(j + 16) - minY * 16.0D - 0.01D) / 256.0D;
		double d5;
		if(this.flipTexture) {
			d5 = d;
			d = d1;
			d1 = d5;
		}

		d5 = d1;
		double d6 = d;
		double d7 = d2;
		double d8 = d3;
		double d9;
		if(this.uvRotateSouth == 2) {
			d = ((double)i + minY * 16.0D) / 256.0D;
			d2 = ((double)(j + 16) - minZ2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)i + maxY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)(j + 16) - maxZ * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d7 = d2;
			d8 = d3;
			d5 = d;
			d6 = d1;
			d2 = d3;
			d3 = d7;
		} else if(this.uvRotateSouth == 1) {
			d = ((double)(i + 16) - maxY * 16.0D) / 256.0D;
			d2 = ((double)j + maxZ * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)(i + 16) - minY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + minZ2 * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d5 = d1;
			d6 = d;
			d = d1;
			d1 = d6;
			d7 = d3;
			d8 = d2;
		} else if(this.uvRotateSouth == 3) {
			d = ((double)(i + 16) - minZ2 * 16.0D - 0.01D) / 256.0D;
			d1 = ((double)(i + 16) - maxZ * 16.0D) / 256.0D;
			d2 = ((double)j + maxY * 16.0D - 0.01D) / 256.0D;
			d3 = ((double)j + minY * 16.0D) / 256.0D;
			if(this.flipTexture) {
				d9 = d;
				d = d1;
				d1 = d9;
			}

			d5 = d1;
			d6 = d;
			d7 = d2;
			d8 = d3;
		}

		this.uvRotateSouth = 0;
		this.flipTexture = false;
		d9 = par2 + par1Block.maxX;
		double d10 = par4 + par1Block.minY;
		double d11 = par4 + par1Block.maxY;
		double d12 = par6 + par1Block.minZ;
		double d13 = par6 + par1Block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.setBrightness(this.brightnessTopLeft);
			tessellator.addVertexWithUV(d9, d10, d13, d6, d8);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.setBrightness(this.brightnessBottomLeft);
			tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.setBrightness(this.brightnessBottomRight);
			tessellator.addVertexWithUV(d9, d11, d12, d5, d7);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.setBrightness(this.brightnessTopRight);
			tessellator.addVertexWithUV(d9, d11, d13, d, d2);
		} else {
			tessellator.addVertexWithUV(d9, d10, d13, d6, d8);
			tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
			tessellator.addVertexWithUV(d9, d11, d12, d5, d7);
			tessellator.addVertexWithUV(d9, d11, d13, d, d2);
		}

	}

	public void renderBlockAsItem(Block par1Block, int par2, float par3) {
		Tessellator tessellator = Tessellator.instance;
		boolean flag = par1Block.blockID == Block.grass.blockID;
		int j;
		float k1;
		float f5;
		float i2;
		if(this.useInventoryTint) {
			j = par1Block.getRenderColor(par2);
			if(flag) {
				j = 0xFFFFFF;
			}

			k1 = (float)(j >> 16 & 255) / 255.0F;
			f5 = (float)(j >> 8 & 255) / 255.0F;
			i2 = (float)(j & 255) / 255.0F;
			GL11.glColor4f(k1 * par3, f5 * par3, i2 * par3, 1.0F);
		}

		j = par1Block.getRenderType();
		int i14;
		if(j != 0 && j != 16) {
			if(j == 1) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.drawCrossedSquares(par1Block, par2, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if(j == 19) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				par1Block.setBlockBoundsForItemRender();
				this.renderBlockStemSmall(par1Block, par2, par1Block.maxY, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if(j == 23) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				par1Block.setBlockBoundsForItemRender();
				tessellator.draw();
			} else if(j == 13) {
				par1Block.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				k1 = 0.0625F;
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator.addTranslation(0.0F, 0.0F, k1);
				this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
				tessellator.addTranslation(0.0F, 0.0F, -k1);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator.addTranslation(0.0F, 0.0F, -k1);
				this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
				tessellator.addTranslation(0.0F, 0.0F, k1);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator.addTranslation(k1, 0.0F, 0.0F);
				this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
				tessellator.addTranslation(-k1, 0.0F, 0.0F);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator.addTranslation(-k1, 0.0F, 0.0F);
				this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
				tessellator.addTranslation(k1, 0.0F, 0.0F);
				tessellator.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if(j == 22) {
				ChestItemRenderHelper.instance.func_35609_a(par1Block, par2, par3);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			} else if(j == 6) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBlockCropsImpl(par1Block, par2, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if(j == 2) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderTorchAtAngle(par1Block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
				tessellator.draw();
			} else if(j == 10) {
				for(i14 = 0; i14 < 2; ++i14) {
					if(i14 == 0) {
						par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
					}

					if(i14 == 1) {
						par1Block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}
			} else if(j == 27) {
				i14 = 0;
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				tessellator.startDrawingQuads();

				for(int i15 = 0; i15 < 8; ++i15) {
					byte b16 = 0;
					byte b17 = 1;
					if(i15 == 0) {
						b16 = 2;
					}

					if(i15 == 1) {
						b16 = 3;
					}

					if(i15 == 2) {
						b16 = 4;
					}

					if(i15 == 3) {
						b16 = 5;
						b17 = 2;
					}

					if(i15 == 4) {
						b16 = 6;
						b17 = 3;
					}

					if(i15 == 5) {
						b16 = 7;
						b17 = 5;
					}

					if(i15 == 6) {
						b16 = 6;
						b17 = 2;
					}

					if(i15 == 7) {
						b16 = 3;
					}

					float f9 = (float)b16 / 16.0F;
					float f10 = 1.0F - (float)i14 / 16.0F;
					float f11 = 1.0F - (float)(i14 + b17) / 16.0F;
					i14 += b17;
					par1Block.setBlockBounds(0.5F - f9, f11, 0.5F - f9, 0.5F + f9, f10, 0.5F + f9);
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
				}

				tessellator.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if(j == 11) {
				for(i14 = 0; i14 < 4; ++i14) {
					f5 = 0.125F;
					if(i14 == 0) {
						par1Block.setBlockBounds(0.5F - f5, 0.0F, 0.0F, 0.5F + f5, 1.0F, f5 * 2.0F);
					}

					if(i14 == 1) {
						par1Block.setBlockBounds(0.5F - f5, 0.0F, 1.0F - f5 * 2.0F, 0.5F + f5, 1.0F, 1.0F);
					}

					f5 = 0.0625F;
					if(i14 == 2) {
						par1Block.setBlockBounds(0.5F - f5, 1.0F - f5 * 3.0F, -f5 * 2.0F, 0.5F + f5, 1.0F - f5, 1.0F + f5 * 2.0F);
					}

					if(i14 == 3) {
						par1Block.setBlockBounds(0.5F - f5, 0.5F - f5 * 3.0F, -f5 * 2.0F, 0.5F + f5, 0.5F - f5, 1.0F + f5 * 2.0F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if(j == 21) {
				for(i14 = 0; i14 < 3; ++i14) {
					f5 = 0.0625F;
					if(i14 == 0) {
						par1Block.setBlockBounds(0.5F - f5, 0.3F, 0.0F, 0.5F + f5, 1.0F, f5 * 2.0F);
					}

					if(i14 == 1) {
						par1Block.setBlockBounds(0.5F - f5, 0.3F, 1.0F - f5 * 2.0F, 0.5F + f5, 1.0F, 1.0F);
					}

					f5 = 0.0625F;
					if(i14 == 2) {
						par1Block.setBlockBounds(0.5F - f5, 0.5F, 0.0F, 0.5F + f5, 1.0F - f5, 1.0F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if(Reflector.hasClass(0)) {
				Reflector.callVoid(1, new Object[]{this, par1Block, par2, j});
			}
		} else {
			if(j == 16) {
				par2 = 1;
			}

			par1Block.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(0, par2));
			tessellator.draw();
			if(flag && this.useInventoryTint) {
				i14 = par1Block.getRenderColor(par2);
				f5 = (float)(i14 >> 16 & 255) / 255.0F;
				i2 = (float)(i14 >> 8 & 255) / 255.0F;
				float byte0 = (float)(i14 & 255) / 255.0F;
				GL11.glColor4f(f5 * par3, i2 * par3, byte0 * par3, 1.0F);
			}

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(1, par2));
			tessellator.draw();
			if(flag && this.useInventoryTint) {
				GL11.glColor4f(par3, par3, par3, 1.0F);
			}

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(2, par2));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(3, par2));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(4, par2));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(5, par2));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

	}

	public static boolean renderItemIn3d(int par0) {
		return par0 == 0 ? true : (par0 == 13 ? true : (par0 == 10 ? true : (par0 == 11 ? true : (par0 == 27 ? true : (par0 == 22 ? true : (par0 == 21 ? true : (par0 == 16 ? true : (Reflector.hasClass(0) ? Reflector.callBoolean(2, new Object[]{par0}) : false))))))));
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
