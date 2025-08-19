package net.minecraft.client.render.entity;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class RenderArrow extends Render {
	public final void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		EntityArrow entityArrow19 = (EntityArrow)entity1;
		this.loadTexture("/item/arrows.png");
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		GL11.glRotatef(entityArrow19.prevRotationYaw + (entityArrow19.rotationYaw - entityArrow19.prevRotationYaw) * f9 - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entityArrow19.prevRotationPitch + (entityArrow19.rotationPitch - entityArrow19.prevRotationPitch) * f9, 0.0F, 0.0F, 1.0F);
		Tessellator tessellator18 = Tessellator.instance;
		GL11.glEnable(GL11.GL_NORMALIZE);
		float f20;
		if((f20 = (float)entityArrow19.arrowShake - f9) > 0.0F) {
			GL11.glRotatef(-MathHelper.sin(f20 * 3.0F) * f20, 0.0F, 0.0F, 1.0F);
		}

		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(0.05625F, 0.05625F, 0.05625F);
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(0.05625F, 0.0F, 0.0F);
		tessellator18.startDrawingQuads();
		tessellator18.addVertexWithUV(-7.0D, -2.0D, -2.0D, 0.0D, 0.15625D);
		tessellator18.addVertexWithUV(-7.0D, -2.0D, 2.0D, 0.15625D, 0.15625D);
		tessellator18.addVertexWithUV(-7.0D, 2.0D, 2.0D, 0.15625D, 0.3125D);
		tessellator18.addVertexWithUV(-7.0D, 2.0D, -2.0D, 0.0D, 0.3125D);
		tessellator18.draw();
		GL11.glNormal3f(-0.05625F, 0.0F, 0.0F);
		tessellator18.startDrawingQuads();
		tessellator18.addVertexWithUV(-7.0D, 2.0D, -2.0D, 0.0D, 0.15625D);
		tessellator18.addVertexWithUV(-7.0D, 2.0D, 2.0D, 0.15625D, 0.15625D);
		tessellator18.addVertexWithUV(-7.0D, -2.0D, 2.0D, 0.15625D, 0.3125D);
		tessellator18.addVertexWithUV(-7.0D, -2.0D, -2.0D, 0.0D, 0.3125D);
		tessellator18.draw();

		for(int i21 = 0; i21 < 4; ++i21) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, 0.05625F);
			tessellator18.startDrawingQuads();
			tessellator18.addVertexWithUV(-8.0D, -2.0D, 0.0D, 0.0D, 0.0D);
			tessellator18.addVertexWithUV(8.0D, -2.0D, 0.0D, 0.5D, 0.0D);
			tessellator18.addVertexWithUV(8.0D, 2.0D, 0.0D, 0.5D, 0.15625D);
			tessellator18.addVertexWithUV(-8.0D, 2.0D, 0.0D, 0.0D, 0.15625D);
			tessellator18.draw();
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}