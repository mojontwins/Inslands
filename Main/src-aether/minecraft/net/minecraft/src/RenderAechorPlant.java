package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderAechorPlant extends RenderLiving {
	public ModelAechorPlant xd;

	public RenderAechorPlant(ModelAechorPlant mb, float f) {
		super(mb, f);
		this.setRenderPassModel(mb);
		this.xd = mb;
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		EntityAechorPlant b1 = (EntityAechorPlant)entityliving;
		float f1 = (float)Math.sin((double)b1.sinage);
		float f3;
		if(b1.hurtTime > 0) {
			f1 *= 0.45F;
			f1 -= 0.125F;
			f3 = 1.75F + (float)Math.sin((double)(b1.sinage + 2.0F)) * 1.5F;
		} else if(b1.seeprey) {
			f1 *= 0.25F;
			f3 = 1.75F + (float)Math.sin((double)(b1.sinage + 2.0F)) * 1.5F;
		} else {
			f1 *= 0.125F;
			f3 = 1.75F;
		}

		this.xd.sinage = f1;
		this.xd.sinage2 = f3;
		float f2 = 0.625F + (float)b1.size / 6.0F;
		this.xd.size = f2;
		this.shadowSize = f2 - 0.25F;
	}

	protected boolean a(EntityAechorPlant entityaechorplant, int i, float f) {
		if(i != 0) {
			return false;
		} else if(i != 0) {
			return false;
		} else {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.325F);
			return true;
		}
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.a((EntityAechorPlant)entityliving, i, f);
	}
}
