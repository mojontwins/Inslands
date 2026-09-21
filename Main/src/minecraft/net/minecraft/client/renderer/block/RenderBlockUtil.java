package net.minecraft.client.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.util.Idx2uvF;
import net.minecraft.client.renderer.util.Texels;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockFluid;
import org.lwjgl.opengl.GL11;

public class RenderBlockUtil {

	public static boolean renderStandardBlock(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		int i5 = block1.colorMultiplier(renderBlocks.blockAccess, i2, i3, i4);
		float f6 = (float)(i5 >> 16 & 255) / 255.0F;
		float f7 = (float)(i5 >> 8 & 255) / 255.0F;
		float f8 = (float)(i5 & 255) / 255.0F;
		if (GameRenderer.anaglyphEnable) {
			float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
			float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
			float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
			f6 = f9;
			f7 = f10;
			f8 = f11;
		}

		return Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[block1.blockID] == 0 ?
				renderStandardBlockWithAmbientOcclusion(renderBlocks, block1, i2, i3, i4, f6, f7, f8)
			:
				renderStandardBlockWithColorMultiplier(renderBlocks, block1, i2, i3, i4, f6, f7, f8);
	}

	public static boolean renderStandardBlockWithAmbientOcclusion(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4, float f5, float f6, float f7) {
		renderBlocks.enableAO = true;
		boolean z8 = false;
		float f9 = renderBlocks.lightValueOwn;
		float f10 = renderBlocks.lightValueOwn;
		float f11 = renderBlocks.lightValueOwn;
		float f12 = renderBlocks.lightValueOwn;
		boolean z13 = true;
		boolean z14 = true;
		boolean z15 = true;
		boolean z16 = true;
		boolean z17 = true;
		boolean z18 = true;
		renderBlocks.lightValueOwn = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4);
		renderBlocks.aoLightValueXNeg = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4);
		renderBlocks.aoLightValueYNeg = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4);
		renderBlocks.aoLightValueZNeg = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 - 1);
		renderBlocks.aoLightValueXPos = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4);
		renderBlocks.aoLightValueYPos = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4);
		renderBlocks.aoLightValueZPos = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 + 1);
		int i19 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4);
		int i20 = i19;
		int i21 = i19;
		int i22 = i19;
		int i23 = i19;
		int i24 = i19;
		int i25 = i19;
		if (block1.minY <= 0.0D) {
			i21 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4);
		}

		if (block1.maxY >= 1.0D) {
			i24 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4);
		}

		if (block1.minX <= 0.0D) {
			i20 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4);
		}

		if (block1.maxX >= 1.0D) {
			i23 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4);
		}

		if (block1.minZ <= 0.0D) {
			i22 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 - 1);
		}

		if (block1.maxZ >= 1.0D) {
			i25 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 + 1);
		}

		Tessellator tessellator26 = Tessellator.instance;
		tessellator26.setBrightness(983055);
		renderBlocks.aoGrassXYZPPC = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2 + 1, i3 + 1, i4)];
		renderBlocks.aoGrassXYZPNC = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2 + 1, i3 - 1, i4)];
		renderBlocks.aoGrassXYZPCP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2 + 1, i3, i4 + 1)];
		renderBlocks.aoGrassXYZPCN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2 + 1, i3, i4 - 1)];
		renderBlocks.aoGrassXYZNPC = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2 - 1, i3 + 1, i4)];
		renderBlocks.aoGrassXYZNNC = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2 - 1, i3 - 1, i4)];
		renderBlocks.aoGrassXYZNCN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2 - 1, i3, i4 - 1)];
		renderBlocks.aoGrassXYZNCP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2 - 1, i3, i4 + 1)];
		renderBlocks.aoGrassXYZCPP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2, i3 + 1, i4 + 1)];
		renderBlocks.aoGrassXYZCPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2, i3 + 1, i4 - 1)];
		renderBlocks.aoGrassXYZCNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2, i3 - 1, i4 + 1)];
		renderBlocks.aoGrassXYZCNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockID(i2, i3 - 1, i4 - 1)];
		if (block1.blockIndexInTexture == 3) {
			z18 = false;
			z17 = false;
			z16 = false;
			z15 = false;
			z13 = false;
		}

		if (renderBlocks.overrideBlockTexture >= 0) {
			z18 = false;
			z17 = false;
			z16 = false;
			z15 = false;
			z13 = false;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3 - 1, i4, 0)) {
			if (renderBlocks.aoType > 0) {
				if (block1.minY <= 0.0D) {
					--i3;
				}

				renderBlocks.aoBrightnessXYNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4);
				renderBlocks.aoBrightnessYZNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 - 1);
				renderBlocks.aoBrightnessYZNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 + 1);
				renderBlocks.aoBrightnessXYPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4);
				renderBlocks.aoLightValueScratchXYNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4);
				renderBlocks.aoLightValueScratchYZNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 - 1);
				renderBlocks.aoLightValueScratchYZNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 + 1);
				renderBlocks.aoLightValueScratchXYPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4);
				if (!renderBlocks.aoGrassXYZCNN && !renderBlocks.aoGrassXYZNNC) {
					renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXYNN;
					renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXYNN;
				} else {
					renderBlocks.aoLightValueScratchXYZNNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4 - 1);
					renderBlocks.aoBrightnessXYZNNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4 - 1);
				}

				if (!renderBlocks.aoGrassXYZCNP && !renderBlocks.aoGrassXYZNNC) {
					renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXYNN;
					renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXYNN;
				} else {
					renderBlocks.aoLightValueScratchXYZNNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4 + 1);
					renderBlocks.aoBrightnessXYZNNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4 + 1);
				}

				if (!renderBlocks.aoGrassXYZCNN && !renderBlocks.aoGrassXYZPNC) {
					renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXYPN;
					renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXYPN;
				} else {
					renderBlocks.aoLightValueScratchXYZPNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4 - 1);
					renderBlocks.aoBrightnessXYZPNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4 - 1);
				}

				if (!renderBlocks.aoGrassXYZCNP && !renderBlocks.aoGrassXYZPNC) {
					renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXYPN;
					renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXYPN;
				} else {
					renderBlocks.aoLightValueScratchXYZPNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4 + 1);
					renderBlocks.aoBrightnessXYZPNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4 + 1);
				}

				if (block1.minY <= 0.0D) {
					++i3;
				}

				f9 = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + renderBlocks.aoLightValueYNeg) / 4.0F;
				f12 = (renderBlocks.aoLightValueScratchYZNP + renderBlocks.aoLightValueYNeg + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F;
				f11 = (renderBlocks.aoLightValueYNeg + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F;
				f10 = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueYNeg + renderBlocks.aoLightValueScratchYZNN) / 4.0F;
				renderBlocks.brightnessTopLeft = getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessYZNP, i21);
				renderBlocks.brightnessTopRight = getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXYPN, i21);
				renderBlocks.brightnessBottomRight = getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNN, i21);
				renderBlocks.brightnessBottomLeft = getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessYZNN, i21);
			} else {
				f12 = renderBlocks.aoLightValueYNeg;
				f11 = renderBlocks.aoLightValueYNeg;
				f10 = renderBlocks.aoLightValueYNeg;
				f9 = renderBlocks.aoLightValueYNeg;
				renderBlocks.brightnessTopLeft = renderBlocks.brightnessBottomLeft = renderBlocks.brightnessBottomRight = renderBlocks.brightnessTopRight = renderBlocks.aoBrightnessXYNN;
			}

			renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = (z13 ? f5 : 1.0F) * 0.5F;
			renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = (z13 ? f6 : 1.0F) * 0.5F;
			renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = (z13 ? f7 : 1.0F) * 0.5F;
			renderBlocks.colorRedTopLeft *= f9;
			renderBlocks.colorGreenTopLeft *= f9;
			renderBlocks.colorBlueTopLeft *= f9;
			renderBlocks.colorRedBottomLeft *= f10;
			renderBlocks.colorGreenBottomLeft *= f10;
			renderBlocks.colorBlueBottomLeft *= f10;
			renderBlocks.colorRedBottomRight *= f11;
			renderBlocks.colorGreenBottomRight *= f11;
			renderBlocks.colorBlueBottomRight *= f11;
			renderBlocks.colorRedTopRight *= f12;
			renderBlocks.colorGreenTopRight *= f12;
			renderBlocks.colorBlueTopRight *= f12;
			renderBlocks.renderBottomFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 0));
			z8 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3 + 1, i4, 1)) {
			if (renderBlocks.aoType > 0) {
				if (block1.maxY >= 1.0D) {
					++i3;
				}

				renderBlocks.aoBrightnessXYNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4);
				renderBlocks.aoBrightnessXYPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4);
				renderBlocks.aoBrightnessYZPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 - 1);
				renderBlocks.aoBrightnessYZPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 + 1);
				renderBlocks.aoLightValueScratchXYNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4);
				renderBlocks.aoLightValueScratchXYPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4);
				renderBlocks.aoLightValueScratchYZPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 - 1);
				renderBlocks.aoLightValueScratchYZPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 + 1);
				if (!renderBlocks.aoGrassXYZCPN && !renderBlocks.aoGrassXYZNPC) {
					renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXYNP;
					renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXYNP;
				} else {
					renderBlocks.aoLightValueScratchXYZNPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4 - 1);
					renderBlocks.aoBrightnessXYZNPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4 - 1);
				}

				if (!renderBlocks.aoGrassXYZCPN && !renderBlocks.aoGrassXYZPPC) {
					renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXYPP;
					renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXYPP;
				} else {
					renderBlocks.aoLightValueScratchXYZPPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4 - 1);
					renderBlocks.aoBrightnessXYZPPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4 - 1);
				}

				if (!renderBlocks.aoGrassXYZCPP && !renderBlocks.aoGrassXYZNPC) {
					renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXYNP;
					renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXYNP;
				} else {
					renderBlocks.aoLightValueScratchXYZNPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4 + 1);
					renderBlocks.aoBrightnessXYZNPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4 + 1);
				}

				if (!renderBlocks.aoGrassXYZCPP && !renderBlocks.aoGrassXYZPPC) {
					renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXYPP;
					renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXYPP;
				} else {
					renderBlocks.aoLightValueScratchXYZPPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4 + 1);
					renderBlocks.aoBrightnessXYZPPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4 + 1);
				}

				if (block1.maxY >= 1.0D) {
					--i3;
				}

				f12 = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueYPos) / 4.0F;
				f9 = (renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueYPos + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
				f10 = (renderBlocks.aoLightValueYPos + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
				f11 = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueYPos + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
				renderBlocks.brightnessTopRight = getAoBrightness(renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessYZPP, i24);
				renderBlocks.brightnessTopLeft = getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXYZPPP, renderBlocks.aoBrightnessXYPP, i24);
				renderBlocks.brightnessBottomLeft = getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPN, i24);
				renderBlocks.brightnessBottomRight = getAoBrightness(renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, i24);
			} else {
				f12 = renderBlocks.aoLightValueYPos;
				f11 = renderBlocks.aoLightValueYPos;
				f10 = renderBlocks.aoLightValueYPos;
				f9 = renderBlocks.aoLightValueYPos;
				renderBlocks.brightnessTopLeft = renderBlocks.brightnessBottomLeft = renderBlocks.brightnessBottomRight = renderBlocks.brightnessTopRight = i24;
			}

			renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = z14 ? f5 : 1.0F;
			renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = z14 ? f6 : 1.0F;
			renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = z14 ? f7 : 1.0F;
			renderBlocks.colorRedTopLeft *= f9;
			renderBlocks.colorGreenTopLeft *= f9;
			renderBlocks.colorBlueTopLeft *= f9;
			renderBlocks.colorRedBottomLeft *= f10;
			renderBlocks.colorGreenBottomLeft *= f10;
			renderBlocks.colorBlueBottomLeft *= f10;
			renderBlocks.colorRedBottomRight *= f11;
			renderBlocks.colorGreenBottomRight *= f11;
			renderBlocks.colorBlueBottomRight *= f11;
			renderBlocks.colorRedTopRight *= f12;
			renderBlocks.colorGreenTopRight *= f12;
			renderBlocks.colorBlueTopRight *= f12;
			renderBlocks.renderTopFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 1));
			z8 = true;
		}

		int i27;
		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3, i4 - 1, 2)) {
			if (renderBlocks.aoType > 0) {
				if (block1.minZ <= 0.0D) {
					--i4;
				}

				renderBlocks.aoLightValueScratchXZNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4);
				renderBlocks.aoLightValueScratchYZNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4);
				renderBlocks.aoLightValueScratchYZPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4);
				renderBlocks.aoLightValueScratchXZPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4);
				renderBlocks.aoBrightnessXZNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4);
				renderBlocks.aoBrightnessYZNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4);
				renderBlocks.aoBrightnessYZPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4);
				renderBlocks.aoBrightnessXZPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4);
				if (!renderBlocks.aoGrassXYZNCN && !renderBlocks.aoGrassXYZCNN) {
					renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXZNN;
					renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXZNN;
				} else {
					renderBlocks.aoLightValueScratchXYZNNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3 - 1, i4);
					renderBlocks.aoBrightnessXYZNNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3 - 1, i4);
				}

				if (!renderBlocks.aoGrassXYZNCN && !renderBlocks.aoGrassXYZCPN) {
					renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXZNN;
					renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXZNN;
				} else {
					renderBlocks.aoLightValueScratchXYZNPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3 + 1, i4);
					renderBlocks.aoBrightnessXYZNPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3 + 1, i4);
				}

				if (!renderBlocks.aoGrassXYZPCN && !renderBlocks.aoGrassXYZCNN) {
					renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXZPN;
					renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXZPN;
				} else {
					renderBlocks.aoLightValueScratchXYZPNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3 - 1, i4);
					renderBlocks.aoBrightnessXYZPNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3 - 1, i4);
				}

				if (!renderBlocks.aoGrassXYZPCN && !renderBlocks.aoGrassXYZCPN) {
					renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXZPN;
					renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXZPN;
				} else {
					renderBlocks.aoLightValueScratchXYZPPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3 + 1, i4);
					renderBlocks.aoBrightnessXYZPPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3 + 1, i4);
				}

				if (block1.minZ <= 0.0D) {
					++i4;
				}

				f9 = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueZNeg + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
				f10 = (renderBlocks.aoLightValueZNeg + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
				f11 = (renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueZNeg + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F;
				f12 = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueZNeg) / 4.0F;
				renderBlocks.brightnessTopLeft = getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, i22);
				renderBlocks.brightnessBottomLeft = getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, i22);
				renderBlocks.brightnessBottomRight = getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXZPN, i22);
				renderBlocks.brightnessTopRight = getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessYZNN, i22);
			} else {
				f12 = renderBlocks.aoLightValueZNeg;
				f11 = renderBlocks.aoLightValueZNeg;
				f10 = renderBlocks.aoLightValueZNeg;
				f9 = renderBlocks.aoLightValueZNeg;
				renderBlocks.brightnessTopLeft = renderBlocks.brightnessBottomLeft = renderBlocks.brightnessBottomRight = renderBlocks.brightnessTopRight = i22;
			}

			renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = (z15 ? f5 : 1.0F) * 0.8F;
			renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = (z15 ? f6 : 1.0F) * 0.8F;
			renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = (z15 ? f7 : 1.0F) * 0.8F;
			renderBlocks.colorRedTopLeft *= f9;
			renderBlocks.colorGreenTopLeft *= f9;
			renderBlocks.colorBlueTopLeft *= f9;
			renderBlocks.colorRedBottomLeft *= f10;
			renderBlocks.colorGreenBottomLeft *= f10;
			renderBlocks.colorBlueBottomLeft *= f10;
			renderBlocks.colorRedBottomRight *= f11;
			renderBlocks.colorGreenBottomRight *= f11;
			renderBlocks.colorBlueBottomRight *= f11;
			renderBlocks.colorRedTopRight *= f12;
			renderBlocks.colorGreenTopRight *= f12;
			renderBlocks.colorBlueTopRight *= f12;
			i27 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 2);
			renderBlocks.renderEastFace(block1, (double)i2, (double)i3, (double)i4, i27);
			if (RenderBlocks.fancyGrass && i27 == 3 && renderBlocks.overrideBlockTexture < 0) {
				renderBlocks.colorRedTopLeft *= f5;
				renderBlocks.colorRedBottomLeft *= f5;
				renderBlocks.colorRedBottomRight *= f5;
				renderBlocks.colorRedTopRight *= f5;
				renderBlocks.colorGreenTopLeft *= f6;
				renderBlocks.colorGreenBottomLeft *= f6;
				renderBlocks.colorGreenBottomRight *= f6;
				renderBlocks.colorGreenTopRight *= f6;
				renderBlocks.colorBlueTopLeft *= f7;
				renderBlocks.colorBlueBottomLeft *= f7;
				renderBlocks.colorBlueBottomRight *= f7;
				renderBlocks.colorBlueTopRight *= f7;
				renderBlocks.renderEastFace(block1, (double)i2, (double)i3, (double)i4, 16 * 9 + 5);
			}

			z8 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3, i4 + 1, 3)) {
			if (renderBlocks.aoType > 0) {
				if (block1.maxZ >= 1.0D) {
					++i4;
				}

				renderBlocks.aoLightValueScratchXZNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3, i4);
				renderBlocks.aoLightValueScratchXZPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3, i4);
				renderBlocks.aoLightValueScratchYZNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4);
				renderBlocks.aoLightValueScratchYZPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4);
				renderBlocks.aoBrightnessXZNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4);
				renderBlocks.aoBrightnessXZPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4);
				renderBlocks.aoBrightnessYZNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4);
				renderBlocks.aoBrightnessYZPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4);
				if (!renderBlocks.aoGrassXYZNCP && !renderBlocks.aoGrassXYZCNP) {
					renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXZNP;
					renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXZNP;
				} else {
					renderBlocks.aoLightValueScratchXYZNNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3 - 1, i4);
					renderBlocks.aoBrightnessXYZNNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3 - 1, i4);
				}

				if (!renderBlocks.aoGrassXYZNCP && !renderBlocks.aoGrassXYZCPP) {
					renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXZNP;
					renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXZNP;
				} else {
					renderBlocks.aoLightValueScratchXYZNPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 - 1, i3 + 1, i4);
					renderBlocks.aoBrightnessXYZNPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3 + 1, i4);
				}

				if (!renderBlocks.aoGrassXYZPCP && !renderBlocks.aoGrassXYZCNP) {
					renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXZPP;
					renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXZPP;
				} else {
					renderBlocks.aoLightValueScratchXYZPNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3 - 1, i4);
					renderBlocks.aoBrightnessXYZPNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3 - 1, i4);
				}

				if (!renderBlocks.aoGrassXYZPCP && !renderBlocks.aoGrassXYZCPP) {
					renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXZPP;
					renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXZPP;
				} else {
					renderBlocks.aoLightValueScratchXYZPPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2 + 1, i3 + 1, i4);
					renderBlocks.aoBrightnessXYZPPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3 + 1, i4);
				}

				if (block1.maxZ >= 1.0D) {
					--i4;
				}

				f9 = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueZPos + renderBlocks.aoLightValueScratchYZPP) / 4.0F;
				f12 = (renderBlocks.aoLightValueZPos + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
				f11 = (renderBlocks.aoLightValueScratchYZNP + renderBlocks.aoLightValueZPos + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
				f10 = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + renderBlocks.aoLightValueZPos) / 4.0F;
				renderBlocks.brightnessTopLeft = getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessYZPP, i25);
				renderBlocks.brightnessTopRight = getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYZPPP, i25);
				renderBlocks.brightnessBottomRight = getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, i25);
				renderBlocks.brightnessBottomLeft = getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessYZNP, i25);
			} else {
				f12 = renderBlocks.aoLightValueZPos;
				f11 = renderBlocks.aoLightValueZPos;
				f10 = renderBlocks.aoLightValueZPos;
				f9 = renderBlocks.aoLightValueZPos;
				renderBlocks.brightnessTopLeft = renderBlocks.brightnessBottomLeft = renderBlocks.brightnessBottomRight = renderBlocks.brightnessTopRight = i25;
			}

			renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = (z16 ? f5 : 1.0F) * 0.8F;
			renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = (z16 ? f6 : 1.0F) * 0.8F;
			renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = (z16 ? f7 : 1.0F) * 0.8F;
			renderBlocks.colorRedTopLeft *= f9;
			renderBlocks.colorGreenTopLeft *= f9;
			renderBlocks.colorBlueTopLeft *= f9;
			renderBlocks.colorRedBottomLeft *= f10;
			renderBlocks.colorGreenBottomLeft *= f10;
			renderBlocks.colorBlueBottomLeft *= f10;
			renderBlocks.colorRedBottomRight *= f11;
			renderBlocks.colorGreenBottomRight *= f11;
			renderBlocks.colorBlueBottomRight *= f11;
			renderBlocks.colorRedTopRight *= f12;
			renderBlocks.colorGreenTopRight *= f12;
			renderBlocks.colorBlueTopRight *= f12;
			i27 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 3);
			renderBlocks.renderWestFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 3));
			if (RenderBlocks.fancyGrass && i27 == 3 && renderBlocks.overrideBlockTexture < 0) {
				renderBlocks.colorRedTopLeft *= f5;
				renderBlocks.colorRedBottomLeft *= f5;
				renderBlocks.colorRedBottomRight *= f5;
				renderBlocks.colorRedTopRight *= f5;
				renderBlocks.colorGreenTopLeft *= f6;
				renderBlocks.colorGreenBottomLeft *= f6;
				renderBlocks.colorGreenBottomRight *= f6;
				renderBlocks.colorGreenTopRight *= f6;
				renderBlocks.colorBlueTopLeft *= f7;
				renderBlocks.colorBlueBottomLeft *= f7;
				renderBlocks.colorBlueBottomRight *= f7;
				renderBlocks.colorBlueTopRight *= f7;
				renderBlocks.renderWestFace(block1, (double)i2, (double)i3, (double)i4, 16 * 9 + 5);
			}

			z8 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2 - 1, i3, i4, 4)) {
			if (renderBlocks.aoType > 0) {
				if (block1.minX <= 0.0D) {
					--i2;
				}

				renderBlocks.aoLightValueScratchXYNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4);
				renderBlocks.aoLightValueScratchXZNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 - 1);
				renderBlocks.aoLightValueScratchXZNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 + 1);
				renderBlocks.aoLightValueScratchXYNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4);
				renderBlocks.aoBrightnessXYNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4);
				renderBlocks.aoBrightnessXZNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 - 1);
				renderBlocks.aoBrightnessXZNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 + 1);
				renderBlocks.aoBrightnessXYNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4);
				if (!renderBlocks.aoGrassXYZNCN && !renderBlocks.aoGrassXYZNNC) {
					renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXZNN;
					renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXZNN;
				} else {
					renderBlocks.aoLightValueScratchXYZNNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4 - 1);
					renderBlocks.aoBrightnessXYZNNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4 - 1);
				}

				if (!renderBlocks.aoGrassXYZNCP && !renderBlocks.aoGrassXYZNNC) {
					renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXZNP;
					renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXZNP;
				} else {
					renderBlocks.aoLightValueScratchXYZNNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4 + 1);
					renderBlocks.aoBrightnessXYZNNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4 + 1);
				}

				if (!renderBlocks.aoGrassXYZNCN && !renderBlocks.aoGrassXYZNPC) {
					renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXZNN;
					renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXZNN;
				} else {
					renderBlocks.aoLightValueScratchXYZNPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4 - 1);
					renderBlocks.aoBrightnessXYZNPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4 - 1);
				}

				if (!renderBlocks.aoGrassXYZNCP && !renderBlocks.aoGrassXYZNPC) {
					renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXZNP;
					renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXZNP;
				} else {
					renderBlocks.aoLightValueScratchXYZNPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4 + 1);
					renderBlocks.aoBrightnessXYZNPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4 + 1);
				}

				if (block1.minX <= 0.0D) {
					++i2;
				}

				f12 = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueXNeg + renderBlocks.aoLightValueScratchXZNP) / 4.0F;
				f9 = (renderBlocks.aoLightValueXNeg + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F;
				f10 = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueXNeg + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F;
				f11 = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueXNeg) / 4.0F;
				renderBlocks.brightnessTopRight = getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, i20);
				renderBlocks.brightnessTopLeft = getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPP, i20);
				renderBlocks.brightnessBottomLeft = getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessXYNP, i20);
				renderBlocks.brightnessBottomRight = getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXZNN, i20);
			} else {
				f12 = renderBlocks.aoLightValueXNeg;
				f11 = renderBlocks.aoLightValueXNeg;
				f10 = renderBlocks.aoLightValueXNeg;
				f9 = renderBlocks.aoLightValueXNeg;
				renderBlocks.brightnessTopLeft = renderBlocks.brightnessBottomLeft = renderBlocks.brightnessBottomRight = renderBlocks.brightnessTopRight = i20;
			}

			renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = (z17 ? f5 : 1.0F) * 0.6F;
			renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = (z17 ? f6 : 1.0F) * 0.6F;
			renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = (z17 ? f7 : 1.0F) * 0.6F;
			renderBlocks.colorRedTopLeft *= f9;
			renderBlocks.colorGreenTopLeft *= f9;
			renderBlocks.colorBlueTopLeft *= f9;
			renderBlocks.colorRedBottomLeft *= f10;
			renderBlocks.colorGreenBottomLeft *= f10;
			renderBlocks.colorBlueBottomLeft *= f10;
			renderBlocks.colorRedBottomRight *= f11;
			renderBlocks.colorGreenBottomRight *= f11;
			renderBlocks.colorBlueBottomRight *= f11;
			renderBlocks.colorRedTopRight *= f12;
			renderBlocks.colorGreenTopRight *= f12;
			renderBlocks.colorBlueTopRight *= f12;
			i27 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 4);
			renderBlocks.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, i27);
			if (RenderBlocks.fancyGrass && i27 == 3 && renderBlocks.overrideBlockTexture < 0) {
				renderBlocks.colorRedTopLeft *= f5;
				renderBlocks.colorRedBottomLeft *= f5;
				renderBlocks.colorRedBottomRight *= f5;
				renderBlocks.colorRedTopRight *= f5;
				renderBlocks.colorGreenTopLeft *= f6;
				renderBlocks.colorGreenBottomLeft *= f6;
				renderBlocks.colorGreenBottomRight *= f6;
				renderBlocks.colorGreenTopRight *= f6;
				renderBlocks.colorBlueTopLeft *= f7;
				renderBlocks.colorBlueBottomLeft *= f7;
				renderBlocks.colorBlueBottomRight *= f7;
				renderBlocks.colorBlueTopRight *= f7;
				renderBlocks.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, 16 * 9 + 5);
			}

			z8 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2 + 1, i3, i4, 5)) {
			if (renderBlocks.aoType > 0) {
				if (block1.maxX >= 1.0D) {
					++i2;
				}

				renderBlocks.aoLightValueScratchXYPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4);
				renderBlocks.aoLightValueScratchXZPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 - 1);
				renderBlocks.aoLightValueScratchXZPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3, i4 + 1);
				renderBlocks.aoLightValueScratchXYPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4);
				renderBlocks.aoBrightnessXYPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4);
				renderBlocks.aoBrightnessXZPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 - 1);
				renderBlocks.aoBrightnessXZPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 + 1);
				renderBlocks.aoBrightnessXYPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4);
				if (!renderBlocks.aoGrassXYZPNC && !renderBlocks.aoGrassXYZPCN) {
					renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXZPN;
					renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXZPN;
				} else {
					renderBlocks.aoLightValueScratchXYZPNN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4 - 1);
					renderBlocks.aoBrightnessXYZPNN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4 - 1);
				}

				if (!renderBlocks.aoGrassXYZPNC && !renderBlocks.aoGrassXYZPCP) {
					renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXZPP;
					renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXZPP;
				} else {
					renderBlocks.aoLightValueScratchXYZPNP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 - 1, i4 + 1);
					renderBlocks.aoBrightnessXYZPNP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4 + 1);
				}

				if (!renderBlocks.aoGrassXYZPPC && !renderBlocks.aoGrassXYZPCN) {
					renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXZPN;
					renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXZPN;
				} else {
					renderBlocks.aoLightValueScratchXYZPPN = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4 - 1);
					renderBlocks.aoBrightnessXYZPPN = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4 - 1);
				}

				if (!renderBlocks.aoGrassXYZPPC && !renderBlocks.aoGrassXYZPCP) {
					renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXZPP;
					renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXZPP;
				} else {
					renderBlocks.aoLightValueScratchXYZPPP = block1.getAmbientOcclusionLightValue(renderBlocks.blockAccess, i2, i3 + 1, i4 + 1);
					renderBlocks.aoBrightnessXYZPPP = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4 + 1);
				}

				if (block1.maxX >= 1.0D) {
					--i2;
				}

				f9 = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueXPos + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
				f12 = (renderBlocks.aoLightValueXPos + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
				f11 = (renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueXPos + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
				f10 = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueXPos) / 4.0F;
				renderBlocks.brightnessTopLeft = getAoBrightness(renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, i23);
				renderBlocks.brightnessTopRight = getAoBrightness(renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPP, i23);
				renderBlocks.brightnessBottomRight = getAoBrightness(renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, renderBlocks.aoBrightnessXYPP, i23);
				renderBlocks.brightnessBottomLeft = getAoBrightness(renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXZPN, i23);
			} else {
				f12 = renderBlocks.aoLightValueXPos;
				f11 = renderBlocks.aoLightValueXPos;
				f10 = renderBlocks.aoLightValueXPos;
				f9 = renderBlocks.aoLightValueXPos;
				renderBlocks.brightnessTopLeft = renderBlocks.brightnessBottomLeft = renderBlocks.brightnessBottomRight = renderBlocks.brightnessTopRight = i23;
			}

			renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = (z18 ? f5 : 1.0F) * 0.6F;
			renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = (z18 ? f6 : 1.0F) * 0.6F;
			renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = (z18 ? f7 : 1.0F) * 0.6F;
			renderBlocks.colorRedTopLeft *= f9;
			renderBlocks.colorGreenTopLeft *= f9;
			renderBlocks.colorBlueTopLeft *= f9;
			renderBlocks.colorRedBottomLeft *= f10;
			renderBlocks.colorGreenBottomLeft *= f10;
			renderBlocks.colorBlueBottomLeft *= f10;
			renderBlocks.colorRedBottomRight *= f11;
			renderBlocks.colorGreenBottomRight *= f11;
			renderBlocks.colorBlueBottomRight *= f11;
			renderBlocks.colorRedTopRight *= f12;
			renderBlocks.colorGreenTopRight *= f12;
			renderBlocks.colorBlueTopRight *= f12;
			i27 = block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 5);
			renderBlocks.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, i27);
			if (RenderBlocks.fancyGrass && i27 == 3 && renderBlocks.overrideBlockTexture < 0) {
				renderBlocks.colorRedTopLeft *= f5;
				renderBlocks.colorRedBottomLeft *= f5;
				renderBlocks.colorRedBottomRight *= f5;
				renderBlocks.colorRedTopRight *= f5;
				renderBlocks.colorGreenTopLeft *= f6;
				renderBlocks.colorGreenBottomLeft *= f6;
				renderBlocks.colorGreenBottomRight *= f6;
				renderBlocks.colorGreenTopRight *= f6;
				renderBlocks.colorBlueTopLeft *= f7;
				renderBlocks.colorBlueBottomLeft *= f7;
				renderBlocks.colorBlueBottomRight *= f7;
				renderBlocks.colorBlueTopRight *= f7;
				renderBlocks.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, 16 * 9 + 5);
			}

			z8 = true;
		}

		renderBlocks.enableAO = false;
		return z8;
	}

	private static int getAoBrightness(int i1, int i2, int i3, int i4) {
		if (i1 == 0) {
			i1 = i4;
		}

		if (i2 == 0) {
			i2 = i4;
		}

		if (i3 == 0) {
			i3 = i4;
		}

		return i1 + i2 + i3 + i4 >> 2 & 16711935;
	}

	public static boolean renderStandardBlockWithColorMultiplier(RenderBlocks renderBlocks, Block block, int x, int y, int z, float r, float g, float b) {
		renderBlocks.enableAO = false;
		Tessellator tes = Tessellator.instance;
		boolean renderedAny = false;

		float lmBo = 0.5F;
		float lmTo = 1.0F;
		float lmEW = 0.8F;
		float lmNS = 0.6F;

		float rTop = lmTo * r;
		float gTop = lmTo * g;
		float bTop = lmTo * b;

		float rBottom = lmBo;
		float gBottom = lmBo;
		float bBottom = lmBo;

		float rEW = lmEW;
		float gEW = lmEW;
		float bEW = lmEW;

		float rNS = lmNS;
		float gNS = lmNS;
		float bNS = lmNS;

		if (block != Block.grass) {
			rBottom = lmBo * r;
			gBottom = lmBo * g;
			bBottom = lmBo * b;

			rEW = lmEW * r;
			gEW = lmEW * g;
			bEW = lmEW * b;

			rNS = lmNS * r;
			gNS = lmNS * g;
			bNS = lmNS * b;
		}

		int blockBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0)) {
			tes.setBrightness(block.minY > 0.0D ? blockBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z));
			tes.setColorOpaque_F(rBottom, gBottom, bBottom);
			renderBlocks.renderBottomFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 0));
			renderedAny = true;
		}

		if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1)) {
			tes.setBrightness(block.maxY < 1.0D ? blockBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z));
			tes.setColorOpaque_F(rTop, gTop, bTop);
			renderBlocks.renderTopFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 1));
			renderedAny = true;
		}

		int textureId;

		if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, 2)) {
			tes.setBrightness(block.minZ > 0.0D ? blockBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1));
			tes.setColorOpaque_F(rEW, gEW, bEW);
			textureId = block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 2);
			renderBlocks.renderEastFace(block, (double)x, (double)y, (double)z, textureId);
			if (RenderBlocks.fancyGrass && textureId == 3 && renderBlocks.overrideBlockTexture < 0) {
				tes.setColorOpaque_F(rEW * r, gEW * g, bEW * b);
				renderBlocks.renderEastFace(block, (double)x, (double)y, (double)z, 9 * 16 + 5);
			}

			renderedAny = true;
		}

		if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, 3)) {
			tes.setBrightness(block.maxZ < 1.0D ? blockBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1));
			tes.setColorOpaque_F(rEW, gEW, bEW);
			textureId = block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 3);
			renderBlocks.renderWestFace(block, (double)x, (double)y, (double)z, textureId);
			if (RenderBlocks.fancyGrass && textureId == 3 && renderBlocks.overrideBlockTexture < 0) {
				tes.setColorOpaque_F(rEW * r, gEW * g, bEW * b);
				renderBlocks.renderWestFace(block, (double)x, (double)y, (double)z, 9 * 16 + 5);
			}

			renderedAny = true;
		}

		if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, 4)) {
			tes.setBrightness(block.minX > 0.0D ? blockBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z));
			tes.setColorOpaque_F(rNS, gNS, bNS);
			textureId = block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 4);
			renderBlocks.renderNorthFace(block, (double)x, (double)y, (double)z, textureId);
			if (RenderBlocks.fancyGrass && textureId == 3 && renderBlocks.overrideBlockTexture < 0) {
				tes.setColorOpaque_F(rNS * r, gNS * g, bNS * b);
				renderBlocks.renderNorthFace(block, (double)x, (double)y, (double)z, 9 * 16 + 5);
			}

			renderedAny = true;
		}

		if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, 5)) {
			tes.setBrightness(block.maxX < 1.0D ? blockBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z));
			tes.setColorOpaque_F(rNS, gNS, bNS);
			textureId = block.getBlockTexture(renderBlocks.blockAccess, x, y, z, 5);
			renderBlocks.renderSouthFace(block, (double)x, (double)y, (double)z, textureId);
			if (RenderBlocks.fancyGrass && textureId == 3 && renderBlocks.overrideBlockTexture < 0) {
				tes.setColorOpaque_F(rNS * r, gNS * g, bNS * b);
				renderBlocks.renderSouthFace(block, (double)x, (double)y, (double)z, 9 * 16 + 5);
			}

			renderedAny = true;
		}

		return renderedAny;
	}

	public static boolean drawCrossedSquares(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4) {
		Tessellator tessellator5 = Tessellator.instance;
		tessellator5.setBrightness(block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4));
		int i7 = block1.colorMultiplier(renderBlocks.blockAccess, i2, i3, i4);
		float f8 = (float)(i7 >> 16 & 255) / 255.0F;
		float f9 = (float)(i7 >> 8 & 255) / 255.0F;
		float f10 = (float)(i7 & 255) / 255.0F;
		if (GameRenderer.anaglyphEnable) {
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
		if (block1 == Block.tallGrass) {
			long j17 = (long)(i2 * 3129871) ^ (long)i4 * 116129781L ^ (long)i3;
			j17 = j17 * j17 * 42317861L + j17 * 11L;
			d19 += ((double)((float)(j17 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
			d20 += ((double)((float)(j17 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
			d15 += ((double)((float)(j17 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
		}

		drawCrossedSquares(renderBlocks, block1, renderBlocks.blockAccess.getBlockMetadata(i2, i3, i4), d19, d20, d15);
		return true;
	}

	public static void drawCrossedSquares(RenderBlocks renderBlocks, Block block, int meta, double x, double y, double z) {
		Tessellator tes = Tessellator.instance;
		int texId = block.getBlockTextureFromSideAndMetadata(0, meta);
		if (renderBlocks.overrideBlockTexture >= 0) {
			texId = renderBlocks.overrideBlockTexture;
		}
		Idx2uvF.calc(texId);
		double u1 = Idx2uvF.u1;
		double u2 = Idx2uvF.u2;
		double v1 = Idx2uvF.v1;
		double v2 = Idx2uvF.v2;

		double x1 = x + 0.5D - 0.45D;
		double x2 = x + 0.5D + 0.45D;
		double z1 = z + 0.5D - 0.45D;
		double z2 = z + 0.5D + 0.45D;
		tes.addVertexWithUV(x1, y + 1.0D, z1, u1, v1);
		tes.addVertexWithUV(x1, y + 0.0D, z1, u1, v2);
		tes.addVertexWithUV(x2, y + 0.0D, z2, u2, v2);
		tes.addVertexWithUV(x2, y + 1.0D, z2, u2, v1);
		tes.addVertexWithUV(x2, y + 1.0D, z2, u1, v1);
		tes.addVertexWithUV(x2, y + 0.0D, z2, u1, v2);
		tes.addVertexWithUV(x1, y + 0.0D, z1, u2, v2);
		tes.addVertexWithUV(x1, y + 1.0D, z1, u2, v1);
		tes.addVertexWithUV(x1, y + 1.0D, z2, u1, v1);
		tes.addVertexWithUV(x1, y + 0.0D, z2, u1, v2);
		tes.addVertexWithUV(x2, y + 0.0D, z1, u2, v2);
		tes.addVertexWithUV(x2, y + 1.0D, z1, u2, v1);
		tes.addVertexWithUV(x2, y + 1.0D, z1, u1, v1);
		tes.addVertexWithUV(x2, y + 0.0D, z1, u1, v2);
		tes.addVertexWithUV(x1, y + 0.0D, z2, u2, v2);
		tes.addVertexWithUV(x1, y + 1.0D, z2, u2, v1);
	}

	public static void drawCrossedSquaresDoubleHeight(RenderBlocks renderBlocks, Block block, int meta, double x, double y, double z) {
		Tessellator tes = Tessellator.instance;
		int texId = block.getBlockTextureFromSideAndMetadata(0, meta);
		if (renderBlocks.overrideBlockTexture >= 0) {
			texId = renderBlocks.overrideBlockTexture;
		}
		Idx2uvF.calc(texId);
		double u1 = Idx2uvF.u1;
		double u2 = Idx2uvF.u2;
		double v1 = Idx2uvF.v1;
		double v2 = Idx2uvF.v2;

		double x1 = x + 0.5D - (double)0.45F;
		double x2 = x + 0.5D + (double)0.45F;
		double z1 = z + 0.5D - (double)0.45F;
		double z2 = z + 0.5D + (double)0.45F;
		tes.addVertexWithUV(x1, y + 2.0D, z1, u1, v1);
		tes.addVertexWithUV(x1, y + 0.0D, z1, u1, v2);
		tes.addVertexWithUV(x2, y + 0.0D, z2, u2, v2);
		tes.addVertexWithUV(x2, y + 2.0D, z2, u2, v1);
		tes.addVertexWithUV(x2, y + 2.0D, z2, u1, v1);
		tes.addVertexWithUV(x2, y + 0.0D, z2, u1, v2);
		tes.addVertexWithUV(x1, y + 0.0D, z1, u2, v2);
		tes.addVertexWithUV(x1, y + 2.0D, z1, u2, v1);
		tes.addVertexWithUV(x1, y + 2.0D, z2, u1, v1);
		tes.addVertexWithUV(x1, y + 0.0D, z2, u1, v2);
		tes.addVertexWithUV(x2, y + 0.0D, z1, u2, v2);
		tes.addVertexWithUV(x2, y + 2.0D, z1, u2, v1);
		tes.addVertexWithUV(x2, y + 2.0D, z1, u1, v1);
		tes.addVertexWithUV(x2, y + 0.0D, z1, u1, v2);
		tes.addVertexWithUV(x1, y + 0.0D, z2, u2, v2);
		tes.addVertexWithUV(x1, y + 2.0D, z2, u2, v1);
	}

	public static void renderTorchAtAngle(RenderBlocks renderBlocks, Block block1, double d2, double d4, double d6, double d8, double d10) {
		Tessellator tessellator12 = Tessellator.instance;
		int i13 = block1.getBlockTextureFromSide(0);
		if (renderBlocks.overrideBlockTexture >= 0) {
			i13 = renderBlocks.overrideBlockTexture;
		}
		Idx2uvF.calc(i13);

		double f16 = Idx2uvF.u1;
		double f17 = Idx2uvF.u2;
		double f18 = Idx2uvF.v1;
		double f19 = Idx2uvF.v2;

		double d20 = f16 + Texels.texelsU(7.0F);
		double d22 = f18 + Texels.texelsV(6.0F);
		double d24 = f16 + Texels.texelsU(9.0F);
		double d26 = f18 + Texels.texelsV(8.0F);

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

	public static void renderBlockCropsImpl(RenderBlocks renderBlocks, Block block1, int i2, double d3, double d5, double d7) {
		Tessellator tessellator9 = Tessellator.instance;
		int i10 = block1.getBlockTextureFromSideAndMetadata(0, i2);
		if (renderBlocks.overrideBlockTexture >= 0) {
			i10 = renderBlocks.overrideBlockTexture;
		}
		Idx2uvF.calc(i10);
		double d13 = Idx2uvF.u1;
		double d15 = Idx2uvF.u2;
		double d17 = Idx2uvF.v1;
		double d19 = Idx2uvF.v2;

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

	public static boolean renderBlockCactusImpl(RenderBlocks renderBlocks, Block block1, int i2, int i3, int i4, float f5, float f6, float f7) {
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
		int i28 = block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4);
		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3 - 1, i4, 0)) {
			tessellator8.setBrightness(block1.minY > 0.0D ? i28 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 - 1, i4));
			tessellator8.setColorOpaque_F(f14, f18, f22);
			renderBlocks.renderBottomFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 0));
			z9 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3 + 1, i4, 1)) {
			tessellator8.setBrightness(block1.maxY < 1.0D ? i28 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3 + 1, i4));
			tessellator8.setColorOpaque_F(f15, f19, f23);
			renderBlocks.renderTopFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 1));
			z9 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3, i4 - 1, 2)) {
			tessellator8.setBrightness(block1.minZ > 0.0D ? i28 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 - 1));
			tessellator8.setColorOpaque_F(f16, f20, f24);
			tessellator8.addTranslation(0.0F, 0.0F, f26);
			renderBlocks.renderEastFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 2));
			tessellator8.addTranslation(0.0F, 0.0F, -f26);
			z9 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2, i3, i4 + 1, 3)) {
			tessellator8.setBrightness(block1.maxZ < 1.0D ? i28 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2, i3, i4 + 1));
			tessellator8.setColorOpaque_F(f16, f20, f24);
			tessellator8.addTranslation(0.0F, 0.0F, -f26);
			renderBlocks.renderWestFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 3));
			tessellator8.addTranslation(0.0F, 0.0F, f26);
			z9 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2 - 1, i3, i4, 4)) {
			tessellator8.setBrightness(block1.minX > 0.0D ? i28 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 - 1, i3, i4));
			tessellator8.setColorOpaque_F(f17, f21, f25);
			tessellator8.addTranslation(f26, 0.0F, 0.0F);
			renderBlocks.renderNorthFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 4));
			tessellator8.addTranslation(-f26, 0.0F, 0.0F);
			z9 = true;
		}

		if (renderBlocks.renderAllFaces || block1.shouldSideBeRendered(renderBlocks.blockAccess, i2 + 1, i3, i4, 5)) {
			tessellator8.setBrightness(block1.maxX < 1.0D ? i28 : block1.getMixedBrightnessForBlock(renderBlocks.blockAccess, i2 + 1, i3, i4));
			tessellator8.setColorOpaque_F(f17, f21, f25);
			tessellator8.addTranslation(-f26, 0.0F, 0.0F);
			renderBlocks.renderSouthFace(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(renderBlocks.blockAccess, i2, i3, i4, 5));
			tessellator8.addTranslation(f26, 0.0F, 0.0F);
			z9 = true;
		}

		return z9;
	}

	public static float getFluidHeight(RenderBlocks renderBlocks, int i1, int i2, int i3, Material material4) {
		int i5 = 0;
		float f6 = 0.0F;

		for (int i7 = 0; i7 < 4; ++i7) {
			int i8 = i1 - (i7 & 1);
			int i10 = i3 - (i7 >> 1 & 1);
			if (renderBlocks.blockAccess.getBlockMaterial(i8, i2 + 1, i10) == material4) {
				return 1.0F;
			}

			Material material11 = renderBlocks.blockAccess.getBlockMaterial(i8, i2, i10);
			if (material11 != material4) {
				if (!material11.isSolid()) {
					++f6;
					++i5;
				}
			} else {
				int i12 = renderBlocks.blockAccess.getBlockMetadata(i8, i2, i10);
				if (i12 >= 8 || i12 == 0) {
					f6 += BlockFluid.getFluidHeightPercent(i12) * 10.0F;
					i5 += 10;
				}

				f6 += BlockFluid.getFluidHeightPercent(i12);
				++i5;
			}
		}

		return 1.0F - f6 / (float)i5;
	}

	public static void setLightValue(Tessellator tessellator, IBlockAccess blockAccess, Block block, float x, float y, float z, float factor) {
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, (int)x, (int)y, (int)z));
		tessellator.setColorOpaque_F(factor, factor, factor);
	}

	public static void renderCubeOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		Tessellator tes = Tessellator.instance;
		boolean isGrass = block.blockID == Block.grass.blockID;
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tes.startDrawingQuads();
		tes.setNormal(0.0F, -1.0F, 0.0F);
		renderBlocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, meta));
		tes.draw();

		if (isGrass && renderBlocks.useInventoryTint) {
			int rgba = block.getRenderColor(meta);
			float r = (float)(rgba >> 16 & 255) / 255.0F;
			float g = (float)(rgba >> 8 & 255) / 255.0F;
			float b = (float)(rgba & 255) / 255.0F;
			GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0F);
		}

		tes.startDrawingQuads();
		tes.setNormal(0.0F, 1.0F, 0.0F);
		renderBlocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, meta));
		tes.draw();

		if (isGrass && renderBlocks.useInventoryTint) {
			GL11.glColor4f(brightness, brightness, brightness, 1.0F);
		}

		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.0F, -1.0F);
		renderBlocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, meta));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderBlocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, meta));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		renderBlocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, meta));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(1.0F, 0.0F, 0.0F);
		renderBlocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, meta));
		tes.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public static void renderInsetCubeOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		Tessellator tes = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		float n1 = 0.0625F;
		tes.startDrawingQuads();
		tes.setNormal(0.0F, -1.0F, 0.0F);
		renderBlocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 1.0F, 0.0F);
		renderBlocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.0F, -1.0F);
		tes.addTranslation(0.0F, 0.0F, n1);
		renderBlocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
		tes.addTranslation(0.0F, 0.0F, -n1);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 0.0F, 1.0F);
		tes.addTranslation(0.0F, 0.0F, -n1);
		renderBlocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
		tes.addTranslation(0.0F, 0.0F, n1);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		tes.addTranslation(n1, 0.0F, 0.0F);
		renderBlocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
		tes.addTranslation(-n1, 0.0F, 0.0F);
		tes.draw();
		tes.startDrawingQuads();
		tes.setNormal(1.0F, 0.0F, 0.0F);
		tes.addTranslation(-n1, 0.0F, 0.0F);
		renderBlocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
		tes.addTranslation(n1, 0.0F, 0.0F);
		tes.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}