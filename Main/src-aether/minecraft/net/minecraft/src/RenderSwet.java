package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSwet extends RenderLiving {
	private ModelBase field_22001_a;

	public RenderSwet(ModelBase modelbase, ModelBase modelbase1, float f) {
		super(modelbase, f);
		this.field_22001_a = modelbase1;
	}

	protected boolean a(EntitySwet entityswets, int i, float f) {
		if(i == 0) {
			this.setRenderPassModel(this.field_22001_a);
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			return true;
		} else {
			if(i == 1) {
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			return false;
		}
	}

	protected void a(EntitySwet entityswets, float f) {
		float f2 = 1.0F;
		float f1 = 1.0F;
		float f3 = 1.5F;
		if(!entityswets.onGround) {
			if(entityswets.motionY > (double)0.85F) {
				f1 = 1.425F;
				f2 = 0.575F;
			} else if(entityswets.motionY < -0.8500000238418579D) {
				f1 = 0.575F;
				f2 = 1.425F;
			} else {
				float f4 = (float)entityswets.motionY * 0.5F;
				f1 += f4;
				f2 -= f4;
			}
		}

		if(entityswets.riddenByEntity != null) {
			f3 = 1.5F + (entityswets.riddenByEntity.width + entityswets.riddenByEntity.height) * 0.75F;
		}

		GL11.glScalef(f2 * f3, f1 * f3, f2 * f3);
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		this.a((EntitySwet)entityliving, f);
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.a((EntitySwet)entityliving, i, f);
	}
}
