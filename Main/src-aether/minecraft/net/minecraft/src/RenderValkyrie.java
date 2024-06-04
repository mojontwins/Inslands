package net.minecraft.src;

public class RenderValkyrie extends RenderBiped {
	public ModelValkyrie mv1;

	public RenderValkyrie(ModelBiped model, float f) {
		super(model, f);
		this.mv1 = (ModelValkyrie)model;
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		EntityValkyrie v1 = (EntityValkyrie)entityliving;
		this.mv1.sinage = v1.sinage;
		this.mv1.gonRound = v1.onGround;
		this.mv1.halow = !v1.otherDimension();
	}
}
