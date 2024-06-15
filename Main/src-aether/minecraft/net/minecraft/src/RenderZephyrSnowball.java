package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderZephyrSnowball extends Render {
	public void func_4012_a(EntityZephyrSnowball entityZephyrSnowball, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float f2 = 2.0F;
		GL11.glScalef(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
		int i = Item.snowball.getIconFromDamage(0);
		this.loadTexture("/gui/items.png");
		Tessellator tessellator = Tessellator.instance;
		float f3 = (float)(i % 16 * 16 + 0) / 256.0F;
		float f4 = (float)(i % 16 * 16 + 16) / 256.0F;
		float f5 = (float)(i / 16 * 16 + 0) / 256.0F;
		float f6 = (float)(i / 16 * 16 + 16) / 256.0F;
		float f7 = 1.0F;
		float f8 = 0.5F;
		float f9 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV((double)(0.0F - f8), (double)(0.0F - f9), 0.0D, (double)f3, (double)f6);
		tessellator.addVertexWithUV((double)(f7 - f8), (double)(0.0F - f9), 0.0D, (double)f4, (double)f6);
		tessellator.addVertexWithUV((double)(f7 - f8), (double)(1.0F - f9), 0.0D, (double)f4, (double)f5);
		tessellator.addVertexWithUV((double)(0.0F - f8), (double)(1.0F - f9), 0.0D, (double)f3, (double)f5);
		tessellator.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.func_4012_a((EntityZephyrSnowball)entity, d, d1, d2, f, f1);
	}
}
