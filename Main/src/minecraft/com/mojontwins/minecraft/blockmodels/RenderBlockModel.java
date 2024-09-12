package com.mojontwins.minecraft.blockmodels;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Tessellator;

public class RenderBlockModel {

	public static boolean renderBlock(IBlockAccess blockAccess, Block block, int x, int y, int z, int angle) {
		
		BlockModel model = block.getBlockModel();
		if(model == null) return false;
		
		// Draw all elements in block model
		for(BlockElement element : model.getBlockElements()) {
			drawElement(blockAccess, block, x, y, z, element, angle);
		}
		
		return true;
	}

	private static void drawElement(IBlockAccess blockAccess, Block block, int x, int y, int z, BlockElement element, int angle) {
		Tessellator tessellator = Tessellator.instance;
		
		// Set brightness
		int brightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
		tessellator.setBrightness(brightness);
		
		// Draw all faces
		
		// down
		tessellator.setColorOpaque_F(0.5F, 0.5F, 0.5F);
		RenderBlockModel.renderBottomFace(block, (double)x, (double)y, (double)z, element, angle);
		
		// up
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		RenderBlockModel.renderTopFace(block, (double)x, (double)y, (double)z, element, angle);
		
		// east
		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
		RenderBlockModel.renderEastFace(block, (double)x, (double)y, (double)z, element, angle);
		
		// west
		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
		RenderBlockModel.renderWestFace(block, (double)x, (double)y, (double)z, element, angle);
		
		// north
		tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
		RenderBlockModel.renderNorthFace(block, (double)x, (double)y, (double)z, element, angle);
		
		// south
		tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
		RenderBlockModel.renderSouthFace(block, (double)x, (double)y, (double)z, element, angle);
	}
	
	public static void renderBlockAsItem(Tessellator tessellator, Block block) {
		BlockModel model = block.getBlockModel();
		if(model != null) {
			// Draw all elements in block model
			
			for(BlockElement element : model.getBlockElements()) {
				drawElementAsItem(tessellator, block, element);
			}
		}
	}
	
	public static void drawElementAsItem(Tessellator tessellator, Block block, BlockElement element) {
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		if(element.faces[BlockFace.DOWN] != null) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			RenderBlockModel.renderBottomFace(block, 0, 0, 0, element, 0);
			tessellator.draw();
		}
		
		if(element.faces[BlockFace.UP] != null) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			RenderBlockModel.renderTopFace(block, 0, 0, 0, element, 0);
			tessellator.draw();
		}
		
		if(element.faces[BlockFace.EAST] != null) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			RenderBlockModel.renderEastFace(block, 0, 0, 0, element, 0);
			tessellator.draw();
		}
		
		if(element.faces[BlockFace.WEST] != null) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			RenderBlockModel.renderWestFace(block, 0, 0, 0, element, 0);
			tessellator.draw();
		}

		if(element.faces[BlockFace.NORTH] != null) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			RenderBlockModel.renderNorthFace(block, 0, 0, 0, element, 0);
			tessellator.draw();
		}
		
		if(element.faces[BlockFace.SOUTH] != null) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			RenderBlockModel.renderSouthFace(block, 0, 0, 0, element, 0);
			tessellator.draw();
		}
	}
	
	private static void renderSouthFace(Block block, double x, double y, double z, BlockElement blockElement, int angle) {
		int faceIdx = BlockFace.rotatedFace(angle, BlockFace.SOUTH);
		
		Tessellator tessellator = Tessellator.instance;
		BlockFace blockFace = blockElement.faces[faceIdx];
		
		if(blockFace == null) return;
		
		if(blockFace.textureIndex == -1) {
			blockFace.overrideTexture(block.getBlockTextureFromSide(faceIdx));
		}
		
		/* u1 v2, u2 v2, u2 v1, u1 v1
		 *   1      3      2      0 
		 *
		 * x1 y2, x1 y1, x2 y1, x2 y2
		 */
		
		double x1 = x + blockElement.from[angle].xCoord;
		double x2 = x + blockElement.to[angle].xCoord;
		
		double z2 = z + blockElement.to[angle].zCoord;
		
		double y1 = y + blockElement.from[angle].yCoord;
		double y2 = y + blockElement.to[angle].yCoord;
		
		tessellator.addVertexWithUV(x1, y2, z2, blockFace.getRotatedU(0), blockFace.getRotatedV(0));
		tessellator.addVertexWithUV(x1, y1, z2, blockFace.getRotatedU(1), blockFace.getRotatedV(1));
		tessellator.addVertexWithUV(x2, y1, z2, blockFace.getRotatedU(2), blockFace.getRotatedV(2));
		tessellator.addVertexWithUV(x2, y2, z2, blockFace.getRotatedU(3), blockFace.getRotatedV(3));
		
	}

	private static void renderNorthFace(Block block, double x, double y, double z, BlockElement blockElement, int angle) {
		int faceIdx = BlockFace.rotatedFace(angle, BlockFace.NORTH);
		
		Tessellator tessellator = Tessellator.instance;
		BlockFace blockFace = blockElement.faces[faceIdx];
		
		if(blockFace == null) return;
		
		if(blockFace.textureIndex == -1) {
			blockFace.overrideTexture(block.getBlockTextureFromSide(faceIdx));
		}
		
		/* u2 v1, u1 v1, u1 v2, u2 v2
		 *   2      0      1      3 
		 *
		 * y2 z2, y2 z1, y1 z1, y1 z2
		 */
		
		double x1 = x + blockElement.from[angle].xCoord;
		double x2 = x + blockElement.to[angle].xCoord;
		
		double z1 = z + blockElement.from[angle].zCoord;
		
		double y1 = y + blockElement.from[angle].yCoord;
		double y2 = y + blockElement.to[angle].yCoord;
		
		tessellator.addVertexWithUV(x2, y2, z1, blockFace.getRotatedU(0), blockFace.getRotatedV(0));
		tessellator.addVertexWithUV(x2, y1, z1, blockFace.getRotatedU(1), blockFace.getRotatedV(1));
		tessellator.addVertexWithUV(x1, y1, z1, blockFace.getRotatedU(2), blockFace.getRotatedV(2));
		tessellator.addVertexWithUV(x1, y2, z1, blockFace.getRotatedU(3), blockFace.getRotatedV(3));
		
	}

	private static void renderWestFace(Block block, double x, double y, double z, BlockElement blockElement, int angle) {
		int faceIdx = BlockFace.rotatedFace(angle, BlockFace.WEST);
		
		Tessellator tessellator = Tessellator.instance;
		BlockFace blockFace = blockElement.faces[faceIdx];
		
		if(blockFace == null) return;
		
		if(blockFace.textureIndex == -1) {
			blockFace.overrideTexture(block.getBlockTextureFromSide(faceIdx));
		}
		
		/* u1 v1, u1 v2, u2 v2, u2 v1
		 *   0      1      3      2 
		 *
		 * x1 y2, x1 y1, x2 y1, x2 y2
		 */
		
		double x1 = x + blockElement.from[angle].xCoord;
		
		double z1 = z + blockElement.from[angle].zCoord;
		double z2 = z + blockElement.to[angle].zCoord;
		
		double y1 = y + blockElement.from[angle].yCoord;
		double y2 = y + blockElement.to[angle].yCoord;
		
		tessellator.addVertexWithUV(x1, y2, z1, blockFace.getRotatedU(0), blockFace.getRotatedV(0));
		tessellator.addVertexWithUV(x1, y1, z1, blockFace.getRotatedU(1), blockFace.getRotatedV(1));
		tessellator.addVertexWithUV(x1, y1, z2, blockFace.getRotatedU(2), blockFace.getRotatedV(2));
		tessellator.addVertexWithUV(x1, y2, z2, blockFace.getRotatedU(3), blockFace.getRotatedV(3));
		
	}

	private static void renderEastFace(Block block, double x, double y, double z, BlockElement blockElement, int angle) {
		int faceIdx = BlockFace.rotatedFace(angle, BlockFace.EAST);
		
		Tessellator tessellator = Tessellator.instance;
		BlockFace blockFace = blockElement.faces[faceIdx];
		
		if(blockFace == null) return;
		
		if(blockFace.textureIndex == -1) {
			blockFace.overrideTexture(block.getBlockTextureFromSide(faceIdx));
		}
		
		/* u2 v1, u1 v1, u1 v2, u2 v2
		 *   2      0      1      3 
		 *
		 * x1 y2, x2 y2, x2 y1, x1 y1
		 */
		
		double x2 = x + blockElement.to[angle].xCoord;
		
		double z1 = z + blockElement.from[angle].zCoord;
		double z2 = z + blockElement.to[angle].zCoord;
		
		double y1 = y + blockElement.from[angle].yCoord;
		double y2 = y + blockElement.to[angle].yCoord;
		
		tessellator.addVertexWithUV(x2, y2, z2, blockFace.getRotatedU(0), blockFace.getRotatedV(0));
		tessellator.addVertexWithUV(x2, y1, z2, blockFace.getRotatedU(1), blockFace.getRotatedV(1));
		tessellator.addVertexWithUV(x2, y1, z1, blockFace.getRotatedU(2), blockFace.getRotatedV(2));
		tessellator.addVertexWithUV(x2, y2, z1, blockFace.getRotatedU(3), blockFace.getRotatedV(3));
		
	}

	private static void renderTopFace(Block block, double x, double y, double z, BlockElement blockElement, int angle) {
		Tessellator tessellator = Tessellator.instance;
		BlockFace blockFace = blockElement.faces[BlockFace.UP];
		
		if(blockFace == null) return;
		
		if(blockFace.textureIndex == -1) {
			blockFace.overrideTexture(block.getBlockTextureFromSide(BlockFace.UP));
		}
		
		/* u2 v2, u2 v1, u1 v1, u1 v2
		 *   3      2      0      1 
		 *
		 * x2 z2, x2 z1, x1 z1, x1 z2
		 */
		
		double x1 = x + blockElement.from[angle].xCoord;
		double x2 = x + blockElement.to[angle].xCoord;
		
		double z1 = z + blockElement.from[angle].zCoord;
		double z2 = z + blockElement.to[angle].zCoord;
		
		double y2 = y + blockElement.to[angle].yCoord;
		
		tessellator.addVertexWithUV(x2, y2, z1, blockFace.getRotatedU(0), blockFace.getRotatedV(0));
		tessellator.addVertexWithUV(x1, y2, z1, blockFace.getRotatedU(1), blockFace.getRotatedV(1));
		tessellator.addVertexWithUV(x1, y2, z2, blockFace.getRotatedU(2), blockFace.getRotatedV(2));
		tessellator.addVertexWithUV(x2, y2, z2, blockFace.getRotatedU(3), blockFace.getRotatedV(3));
	}

	private static void renderBottomFace(Block block, double x, double y, double z, BlockElement blockElement, int angle) {
		Tessellator tessellator = Tessellator.instance;
		BlockFace blockFace = blockElement.faces[BlockFace.DOWN];
		
		if(blockFace == null) return;
		
		if(blockFace.textureIndex == -1) {
			blockFace.overrideTexture(block.getBlockTextureFromSide(BlockFace.DOWN));
		}
		
		/* u1 v2, u1 v1, u2 v1, u2 v2
		 *   1      0      2      3 
		 *
		 * x1 z2, x1 z1, x2 z1, x2 z2
		 */
		
		double x1 = x + blockElement.from[angle].xCoord;
		double x2 = x + blockElement.to[angle].xCoord;
		
		double z1 = z + blockElement.from[angle].zCoord;
		double z2 = z + blockElement.to[angle].zCoord;
		
		double y1 = y + blockElement.from[angle].yCoord;
		
		tessellator.addVertexWithUV(x2, y1, z2, blockFace.getRotatedU(0), blockFace.getRotatedV(0));
		tessellator.addVertexWithUV(x1, y1, z2, blockFace.getRotatedU(1), blockFace.getRotatedV(1));
		tessellator.addVertexWithUV(x1, y1, z1, blockFace.getRotatedU(2), blockFace.getRotatedV(2));
		tessellator.addVertexWithUV(x2, y1, z1, blockFace.getRotatedU(3), blockFace.getRotatedV(3));
	}
 
}
