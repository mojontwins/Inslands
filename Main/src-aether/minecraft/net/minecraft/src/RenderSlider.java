package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSlider extends RenderLiving {
	public RenderSlider(ModelBase ms, float f) {
		super(ms, f);
		this.renderPassModel = ms;
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		EntitySlider e1 = (EntitySlider)entityliving;
		if(e1.harvey > 0.01F) {
			GL11.glRotatef(e1.harvey * -30.0F, (float)e1.rennis, 0.0F, (float)e1.dennis);
		}

	}

	protected boolean setSliderEyeBrightness(EntitySlider slider, int i, float f) {
		if(i != 0) {
			return false;
		} else {
			if(slider.awake) {
				if(slider.criticalCondition()) {
					this.loadTexture("/aether/mobs/sliderAwakeGlow_red.png");
				} else {
					this.loadTexture("/aether/mobs/sliderAwakeGlow.png");
				}
			} else {
				this.loadTexture("/aether/mobs/sliderSleepGlow.png");
			}

			float f1 = (1.0F - slider.getEntityBrightness(1.0F)) * 0.5F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
			return true;
		}
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.setSliderEyeBrightness((EntitySlider)entityliving, i, f);
	}
}
