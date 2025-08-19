package net.minecraft.client.render.entity;

import java.util.Random;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.EnumArt;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class RenderPainting extends Render {
	private Random rand = new Random();

	public final void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		EntityPainting entityPainting22 = (EntityPainting)entity1;
		this.rand.setSeed(187L);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		GL11.glRotatef(f8, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);
		this.loadTexture("/art/kz.png");
		EnumArt enumArt3 = entityPainting22.art;
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		int i25 = enumArt3.offsetY;
		int i5 = enumArt3.offsetX;
		int i24 = enumArt3.sizeY;
		int i23 = enumArt3.sizeX;
		entityPainting22 = entityPainting22;
		RenderPainting renderPainting21 = this;
		float f7 = (float)(-i23) / 2.0F;
		f8 = (float)(-i24) / 2.0F;

		for(int i26 = 0; i26 < i23 / 16; ++i26) {
			for(int i10 = 0; i10 < i24 / 16; ++i10) {
				float f11 = f7 + (float)(i26 + 1 << 4);
				float f27 = f7 + (float)(i26 << 4);
				float f13 = f8 + (float)(i10 + 1 << 4);
				float f14 = f8 + (float)(i10 << 4);
				float f10002 = (f11 + f27) / 2.0F;
				float f18 = (f13 + f14) / 2.0F;
				float f17 = f10002;
				int i19 = MathHelper.floor_double(entityPainting22.posX);
				int i28 = MathHelper.floor_double(entityPainting22.posY + (double)(f18 / 16.0F));
				int i20 = MathHelper.floor_double(entityPainting22.posZ);
				if(entityPainting22.direction == 0) {
					i19 = MathHelper.floor_double(entityPainting22.posX + (double)(f17 / 16.0F));
				}

				if(entityPainting22.direction == 1) {
					i20 = MathHelper.floor_double(entityPainting22.posZ - (double)(f17 / 16.0F));
				}

				if(entityPainting22.direction == 2) {
					i19 = MathHelper.floor_double(entityPainting22.posX - (double)(f17 / 16.0F));
				}

				if(entityPainting22.direction == 3) {
					i20 = MathHelper.floor_double(entityPainting22.posZ + (double)(f17 / 16.0F));
				}

				float f15;
				GL11.glColor3f(f15 = renderPainting21.renderManager.worldObj.getBrightness(i19, i28, i20), f15, f15);
				f15 = (float)(i5 + i23 - (i26 << 4)) / 256.0F;
				float f16 = (float)(i5 + i23 - (i26 + 1 << 4)) / 256.0F;
				f17 = (float)(i25 + i24 - (i10 << 4)) / 256.0F;
				f18 = (float)(i25 + i24 - (i10 + 1 << 4)) / 256.0F;
				Tessellator tessellator29 = Tessellator.instance;
				Tessellator.instance.startDrawingQuads();
				Tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator29.addVertexWithUV((double)f11, (double)f14, -0.5D, (double)f16, (double)f17);
				tessellator29.addVertexWithUV((double)f27, (double)f14, -0.5D, (double)f15, (double)f17);
				tessellator29.addVertexWithUV((double)f27, (double)f13, -0.5D, (double)f15, (double)f18);
				tessellator29.addVertexWithUV((double)f11, (double)f13, -0.5D, (double)f16, (double)f18);
				Tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator29.addVertexWithUV((double)f11, (double)f13, 0.5D, 0.75D, 0.0D);
				tessellator29.addVertexWithUV((double)f27, (double)f13, 0.5D, 0.8125D, 0.0D);
				tessellator29.addVertexWithUV((double)f27, (double)f14, 0.5D, 0.8125D, 0.0625D);
				tessellator29.addVertexWithUV((double)f11, (double)f14, 0.5D, 0.75D, 0.0625D);
				Tessellator.setNormal(0.0F, -1.0F, 0.0F);
				tessellator29.addVertexWithUV((double)f11, (double)f13, -0.5D, 0.75D, 0.001953125D);
				tessellator29.addVertexWithUV((double)f27, (double)f13, -0.5D, 0.8125D, 0.001953125D);
				tessellator29.addVertexWithUV((double)f27, (double)f13, 0.5D, 0.8125D, 0.001953125D);
				tessellator29.addVertexWithUV((double)f11, (double)f13, 0.5D, 0.75D, 0.001953125D);
				Tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator29.addVertexWithUV((double)f11, (double)f14, 0.5D, 0.75D, 0.001953125D);
				tessellator29.addVertexWithUV((double)f27, (double)f14, 0.5D, 0.8125D, 0.001953125D);
				tessellator29.addVertexWithUV((double)f27, (double)f14, -0.5D, 0.8125D, 0.001953125D);
				tessellator29.addVertexWithUV((double)f11, (double)f14, -0.5D, 0.75D, 0.001953125D);
				Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator29.addVertexWithUV((double)f11, (double)f13, 0.5D, 0.751953125D, 0.0D);
				tessellator29.addVertexWithUV((double)f11, (double)f14, 0.5D, 0.751953125D, 0.0625D);
				tessellator29.addVertexWithUV((double)f11, (double)f14, -0.5D, 0.751953125D, 0.0625D);
				tessellator29.addVertexWithUV((double)f11, (double)f13, -0.5D, 0.751953125D, 0.0D);
				Tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator29.addVertexWithUV((double)f27, (double)f13, -0.5D, 0.751953125D, 0.0D);
				tessellator29.addVertexWithUV((double)f27, (double)f14, -0.5D, 0.751953125D, 0.0625D);
				tessellator29.addVertexWithUV((double)f27, (double)f14, 0.5D, 0.751953125D, 0.0625D);
				tessellator29.addVertexWithUV((double)f27, (double)f13, 0.5D, 0.751953125D, 0.0D);
				tessellator29.draw();
			}
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}