package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class RenderEndPortal extends TileEntitySpecialRenderer {
	FloatBuffer field_40448_a = GLAllocation.createDirectFloatBuffer(16);

	public void func_40446_a(TileEntityEndPortal tileEntityEndPortal1, double d2, double d4, double d6, float f8) {
		float f9 = (float)this.tileEntityRenderer.playerX;
		float f10 = (float)this.tileEntityRenderer.playerY;
		float f11 = (float)this.tileEntityRenderer.playerZ;
		GL11.glDisable(GL11.GL_LIGHTING);
		Random random12 = new Random(31100L);
		float f13 = 0.75F;

		for(int i14 = 0; i14 < 16; ++i14) {
			GL11.glPushMatrix();
			float f15 = (float)(16 - i14);
			float f16 = 0.0625F;
			float f17 = 1.0F / (f15 + 1.0F);
			if(i14 == 0) {
				this.bindTextureByName("/misc/tunnel.png");
				f17 = 0.1F;
				f15 = 65.0F;
				f16 = 0.125F;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			if(i14 == 1) {
				this.bindTextureByName("/misc/particlefield.png");
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				f16 = 0.5F;
			}

			float f18 = (float)(-(d4 + (double)f13));
			float f19 = f18 + ActiveRenderInfo.objectY;
			float f20 = f18 + f15 + ActiveRenderInfo.objectY;
			float f21 = f19 / f20;
			f21 += (float)(d4 + (double)f13);
			GL11.glTranslatef(f9, f21, f11);
			GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
			GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
			GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
			GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
			GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, this.func_40447_a(1.0F, 0.0F, 0.0F, 0.0F));
			GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, this.func_40447_a(0.0F, 0.0F, 1.0F, 0.0F));
			GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, this.func_40447_a(0.0F, 0.0F, 0.0F, 1.0F));
			GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_40447_a(0.0F, 1.0F, 0.0F, 0.0F));
			GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
			GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
			GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
			GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 700000.0F, 0.0F);
			GL11.glScalef(f16, f16, f16);
			GL11.glTranslatef(0.5F, 0.5F, 0.0F);
			GL11.glRotatef((float)(i14 * i14 * 4321 + i14 * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
			GL11.glTranslatef(-f9, -f11, -f10);
			f19 = f18 + ActiveRenderInfo.objectY;
			GL11.glTranslatef(ActiveRenderInfo.objectX * f15 / f19, ActiveRenderInfo.objectZ * f15 / f19, -f10);
			Tessellator tessellator24 = Tessellator.instance;
			tessellator24.startDrawingQuads();
			f21 = random12.nextFloat() * 0.5F + 0.1F;
			float f22 = random12.nextFloat() * 0.5F + 0.4F;
			float f23 = random12.nextFloat() * 0.5F + 0.5F;
			if(i14 == 0) {
				f23 = 1.0F;
				f22 = 1.0F;
				f21 = 1.0F;
			}

			tessellator24.setColorRGBA_F(f21 * f17, f22 * f17, f23 * f17, 1.0F);
			tessellator24.addVertex(d2, d4 + (double)f13, d6);
			tessellator24.addVertex(d2, d4 + (double)f13, d6 + 1.0D);
			tessellator24.addVertex(d2 + 1.0D, d4 + (double)f13, d6 + 1.0D);
			tessellator24.addVertex(d2 + 1.0D, d4 + (double)f13, d6);
			tessellator24.draw();
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private FloatBuffer func_40447_a(float f1, float f2, float f3, float f4) {
		this.field_40448_a.clear();
		this.field_40448_a.put(f1).put(f2).put(f3).put(f4);
		this.field_40448_a.flip();
		return this.field_40448_a;
	}

	public void renderTileEntityAt(TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
		this.func_40446_a((TileEntityEndPortal)tileEntity1, d2, d4, d6, f8);
	}
}
