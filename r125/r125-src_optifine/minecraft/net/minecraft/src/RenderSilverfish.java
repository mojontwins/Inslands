package net.minecraft.src;

public class RenderSilverfish extends RenderLiving {
	public RenderSilverfish() {
		super(new ModelSilverfish(), 0.3F);
	}

	protected float getSilverfishDeathRotation(EntitySilverfish entitySilverfish1) {
		return 180.0F;
	}

	public void renderSilverfish(EntitySilverfish entitySilverfish1, double d2, double d4, double d6, float f8, float f9) {
		super.doRenderLiving(entitySilverfish1, d2, d4, d6, f8, f9);
	}

	protected int shouldSilverfishRenderPass(EntitySilverfish entitySilverfish1, int i2, float f3) {
		return -1;
	}

	protected float getDeathMaxRotation(EntityLiving entityLiving1) {
		return this.getSilverfishDeathRotation((EntitySilverfish)entityLiving1);
	}

	protected int shouldRenderPass(EntityLiving entityLiving1, int i2, float f3) {
		return this.shouldSilverfishRenderPass((EntitySilverfish)entityLiving1, i2, f3);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderSilverfish((EntitySilverfish)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderSilverfish((EntitySilverfish)entity1, d2, d4, d6, f8, f9);
	}
}
