package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderMagmaCube extends RenderLiving {
	private int field_40276_c = ((ModelMagmaCube)this.mainModel).func_40343_a();

	public RenderMagmaCube() {
		super(new ModelMagmaCube(), 0.25F);
	}

	public void renderMagmaCube(EntityMagmaCube entityMagmaCube1, double d2, double d4, double d6, float f8, float f9) {
		int i10 = ((ModelMagmaCube)this.mainModel).func_40343_a();
		if(i10 != this.field_40276_c) {
			this.field_40276_c = i10;
			this.mainModel = new ModelMagmaCube();
			System.out.println("new lava slime model");
		}

		super.doRenderLiving(entityMagmaCube1, d2, d4, d6, f8, f9);
	}

	protected void scaleMagmaCube(EntityMagmaCube entityMagmaCube1, float f2) {
		int i3 = entityMagmaCube1.getSlimeSize();
		float f4 = (entityMagmaCube1.field_767_b + (entityMagmaCube1.field_768_a - entityMagmaCube1.field_767_b) * f2) / ((float)i3 * 0.5F + 1.0F);
		float f5 = 1.0F / (f4 + 1.0F);
		float f6 = (float)i3;
		GL11.glScalef(f5 * f6, 1.0F / f5 * f6, f5 * f6);
	}

	protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
		this.scaleMagmaCube((EntityMagmaCube)entityLiving1, f2);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderMagmaCube((EntityMagmaCube)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderMagmaCube((EntityMagmaCube)entity1, d2, d4, d6, f8, f9);
	}
}
