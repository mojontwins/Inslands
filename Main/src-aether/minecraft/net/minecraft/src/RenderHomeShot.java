package net.minecraft.src;

public class RenderHomeShot extends RenderLiving {
	private ModelHomeShot shotty;

	public RenderHomeShot(ModelBase ms, float f) {
		super(ms, f);
		this.shotty = (ModelHomeShot)ms;
	}

	public void preRenderCallback(EntityLiving el, float f) {
		EntityHomeShot hs = (EntityHomeShot)el;

		for(int i = 0; i < 3; ++i) {
			this.shotty.sinage[i] = hs.sinage[i];
		}

	}
}
