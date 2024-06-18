package net.minecraft.src;

public class RenderBlaze extends RenderLiving {
	private int field_40278_c = ((ModelBlaze)this.mainModel).func_40321_a();

	public RenderBlaze() {
		super(new ModelBlaze(), 0.5F);
	}

	public void renderBlaze(EntityBlaze entityBlaze1, double d2, double d4, double d6, float f8, float f9) {
		int i10 = ((ModelBlaze)this.mainModel).func_40321_a();
		if(i10 != this.field_40278_c) {
			this.field_40278_c = i10;
			this.mainModel = new ModelBlaze();
		}

		super.doRenderLiving(entityBlaze1, d2, d4, d6, f8, f9);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderBlaze((EntityBlaze)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderBlaze((EntityBlaze)entity1, d2, d4, d6, f8, f9);
	}
}
