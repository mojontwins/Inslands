package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public class RenderLiving extends Render {
	protected ModelBase mainModel;
	private ModelBase renderPassModel;

	public RenderLiving(ModelBase modelBase1, float f2) {
		this.mainModel = modelBase1;
		this.shadowSize = f2;
	}

	public final void setRenderPassModel(ModelBase modelBase1) {
		this.renderPassModel = modelBase1;
	}

	public void a(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);

		try {
			f8 = entityLiving1.prevRenderYawOffset + (entityLiving1.renderYawOffset - entityLiving1.prevRenderYawOffset) * f9;
			float f10 = entityLiving1.prevRotationYaw + (entityLiving1.rotationYaw - entityLiving1.prevRotationYaw) * f9;
			float f11 = entityLiving1.prevRotationPitch + (entityLiving1.rotationPitch - entityLiving1.prevRotationPitch) * f9;
			GL11.glTranslatef((float)d2, (float)d4, (float)d6);
			float f14 = (float)entityLiving1.ticksExisted + f9;
			GL11.glRotatef(180.0F - f8, 0.0F, 1.0F, 0.0F);
			float f3;
			if(entityLiving1.deathTime > 0) {
				if((f3 = MathHelper.sqrt_float(((float)entityLiving1.deathTime + f9 - 1.0F) / 20.0F * 1.6F)) > 1.0F) {
					f3 = 1.0F;
				}

				GL11.glRotatef(f3 * this.getDeathMaxRotation(entityLiving1), 0.0F, 0.0F, 1.0F);
			}

			GL11.glScalef(-0.0625F, -0.0625F, 0.0625F);
			this.preRenderCallback(entityLiving1, f9);
			GL11.glTranslatef(0.0F, -24.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			f3 = entityLiving1.newPosZ + (entityLiving1.newRotationYaw - entityLiving1.newPosZ) * f9;
			float f15 = entityLiving1.newRotationPitch - entityLiving1.newRotationYaw * (1.0F - f9);
			if(f3 > 1.0F) {
				f3 = 1.0F;
			}

			this.loadDownloadableImageTexture(entityLiving1.skinUrl, entityLiving1.getEntityTexture());
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.mainModel.render(f15, f3, f14, f10 - f8, f11, 1.0F);

			for(int i5 = 0; i5 < 4; ++i5) {
				if(this.shouldRenderPass(entityLiving1, i5)) {
					this.renderPassModel.render(f15, f3, f14, f10 - f8, f11, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			float f16 = entityLiving1.getEntityBrightness(f9);
			int i17;
			if((i17 = this.getColorMultiplier(entityLiving1, f16, f9)) >>> 24 > 0 || entityLiving1.hurtTime > 0 || entityLiving1.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);
				if(entityLiving1.hurtTime > 0 || entityLiving1.deathTime > 0) {
					GL11.glColor4f(f16, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(f15, f3, f14, f10 - f8, f11, 1.0F);

					for(int i7 = 0; i7 < 4; ++i7) {
						if(this.shouldRenderPass(entityLiving1, i7)) {
							GL11.glColor4f(f16, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(f15, f3, f14, f10 - f8, f11, 1.0F);
						}
					}
				}

				if(i17 >>> 24 > 0) {
					float f19 = (float)(i17 >> 16 & 255) / 255.0F;
					f16 = (float)(i17 >> 8 & 255) / 255.0F;
					f9 = (float)(i17 & 255) / 255.0F;
					float f18 = (float)(i17 >>> 24) / 255.0F;
					GL11.glColor4f(f19, f16, f9, f18);
					this.mainModel.render(f15, f3, f14, f10 - f8, f11, 1.0F);

					for(int i12 = 0; i12 < 4; ++i12) {
						if(this.shouldRenderPass(entityLiving1, i12)) {
							GL11.glColor4f(f19, f16, f9, f18);
							this.renderPassModel.render(f15, f3, f14, f10 - f8, f11, 1.0F);
						}
					}
				}

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable(GL11.GL_NORMALIZE);
		} catch (Exception exception13) {
			exception13.printStackTrace();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2) {
		return false;
	}

	protected float getDeathMaxRotation(EntityLiving entityLiving1) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLiving entityLiving1, float f2, float f3) {
		return 0;
	}

	protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.a((EntityLiving)entity1, d2, d4, d6, f8, f9);
	}
}