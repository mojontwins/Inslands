package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderFloatingBlock extends Render {
	private static RenderBlocks renderBlocks;

	public RenderFloatingBlock() {
		renderBlocks = new RenderBlocks();
		this.shadowSize = 0.5F;
	}

	public void func_156_a(EntityFloatingBlock entityFloatingBlock, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		this.loadTexture("/terrain.png");
		Block block = Block.blocksList[entityFloatingBlock.blockID];
		World world = entityFloatingBlock.getWorld();
		GL11.glDisable(GL11.GL_LIGHTING);
		renderBlockFallingSand(block, world, MathHelper.floor_double(entityFloatingBlock.posX), MathHelper.floor_double(entityFloatingBlock.posY), MathHelper.floor_double(entityFloatingBlock.posZ), entityFloatingBlock.metadata);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	public static void renderBlockFallingSand(Block block, World world, int i, int j, int k, int meta) {
		int l = block.getRenderColor(meta);
		float red = (float)(l >> 16 & 255) / 255.0F;
		float green = (float)(l >> 8 & 255) / 255.0F;
		float blue = (float)(l & 255) / 255.0F;
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		float f4 = block.getBlockBrightness(world, i, j, k);
		float f5 = block.getBlockBrightness(world, i, j - 1, k);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f * f5 * red, f * f5 * green, f * f5 * blue);
		renderBlocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(0, meta));
		f5 = block.getBlockBrightness(world, i, j + 1, k);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f1 * f5 * red, f1 * f5 * green, f1 * f5 * blue);
		renderBlocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(1, meta));
		f5 = block.getBlockBrightness(world, i, j, k - 1);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f2 * f5 * red, f2 * f5 * green, f2 * f5 * blue);
		renderBlocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(2, meta));
		f5 = block.getBlockBrightness(world, i, j, k + 1);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f2 * f5 * red, f2 * f5 * green, f2 * f5 * blue);
		renderBlocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(3, meta));
		f5 = block.getBlockBrightness(world, i - 1, j, k);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f3 * f5 * red, f3 * f5 * green, f3 * f5 * blue);
		renderBlocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(4, meta));
		f5 = block.getBlockBrightness(world, i + 1, j, k);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f3 * f5 * red, f3 * f5 * green, f3 * f5 * blue);
		renderBlocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(5, meta));
		tessellator.draw();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.func_156_a((EntityFloatingBlock)entity, d, d1, d2, f, f1);
	}
}
