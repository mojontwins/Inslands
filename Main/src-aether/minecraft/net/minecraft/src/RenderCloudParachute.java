package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderCloudParachute extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks();

	public RenderCloudParachute() {
		this.shadowSize = 0.5F;
	}

	public void renderCloud(EntityCloudParachute entitycloud, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(180.0F - f, 0.0F, 1.0F, 0.0F);
		this.loadTexture("/terrain.png");
		GL11.glDisable(GL11.GL_LIGHTING);
		RenderFloatingBlock.renderBlockFallingSand(AetherBlocks.Aercloud, entitycloud.getWorld(), MathHelper.floor_double(entitycloud.posX), MathHelper.floor_double(entitycloud.posY), MathHelper.floor_double(entitycloud.posZ), entitycloud.gold ? 2 : 0);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.renderCloud((EntityCloudParachute)entity, d, d1, d2, f, f1);
	}
}
