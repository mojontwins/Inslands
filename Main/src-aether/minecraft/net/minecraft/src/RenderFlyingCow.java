package net.minecraft.src;

public class RenderFlyingCow extends RenderLiving {
	private ModelBase wingmodel;

	public RenderFlyingCow(ModelBase modelbase, ModelBase modelbase1, float f) {
		super(modelbase, f);
		this.setRenderPassModel(modelbase1);
		this.wingmodel = modelbase1;
	}

	protected boolean setWoolColorAndRender(EntityFlyingCow flyingcow, int i, float f) {
		if(i == 0) {
			this.loadTexture("/aether/mobs/Mob_FlyingPigWings.png");
			ModelFlyingCow2.flyingcow = flyingcow;
			return true;
		} else {
			return false;
		}
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.setWoolColorAndRender((EntityFlyingCow)entityliving, i, f);
	}
}
