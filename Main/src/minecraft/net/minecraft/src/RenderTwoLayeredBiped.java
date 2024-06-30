package net.minecraft.src;

public class RenderTwoLayeredBiped extends RenderBiped {
	protected ModelBiped layerTwo;
	
	public RenderTwoLayeredBiped() {
		super(new ModelBiped(0.0F, 0.0F, 0, 64, 64), 0.5F);
		this.layerTwo = new ModelBiped(0.3F, 0.0F, 32, 64, 64);
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving, int pass, float renderPartialTicks) {
		if(pass == 4) {
			this.setRenderPassModel(layerTwo);
			return true;
		} else return super.shouldRenderPass(entityLiving, pass, renderPartialTicks);
	}
}
