package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSentry extends RenderLiving {
	public RenderSentry(ModelBase modelbase, float f) {
		super(modelbase, f);
		this.setRenderPassModel(modelbase);
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		float f1 = 1.75F;
		GL11.glScalef(f1, f1, f1);
	}

	protected boolean a(EntitySentry sentry, int i, float f) {
		if(i != 0) {
			return false;
		} else if(i != 0) {
			return false;
		} else if(sentry.active) {
			this.loadTexture("/aether/mobs/SentryEye.png");
			float f1 = (1.0F - sentry.getEntityBrightness(1.0F)) * 0.75F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
			return true;
		} else {
			return false;
		}
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.a((EntitySentry)entityliving, i, f);
	}
}
