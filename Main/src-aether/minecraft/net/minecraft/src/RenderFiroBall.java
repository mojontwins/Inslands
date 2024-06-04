package net.minecraft.src;

public class RenderFiroBall extends RenderLiving {
	private ModelHomeShot shotty;

	public RenderFiroBall(ModelBase ms, float f) {
		super(ms, f);
		this.shotty = (ModelHomeShot)ms;
	}

	public void preRenderCallback(EntityLiving el, float f) {
		EntityFiroBall hs = (EntityFiroBall)el;

		for(int i = 0; i < 3; ++i) {
			this.shotty.sinage[i] = hs.sinage[i];
		}

	}
}
