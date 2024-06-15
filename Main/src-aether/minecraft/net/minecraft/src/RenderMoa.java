package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderMoa extends RenderLiving {
	public RenderMoa(ModelBase modelbase, float f) {
		super(modelbase, f);
	}

	public void renderChicken(EntityMoa entitymoa, double d, double d1, double d2, float f, float f1) {
		super.doRenderLiving(entitymoa, d, d1, d2, f, f1);
	}

	protected float getWingRotation(EntityMoa entitymoa, float f) {
		float f1 = entitymoa.field_756_e + (entitymoa.field_752_b - entitymoa.field_756_e) * f;
		float f2 = entitymoa.field_757_d + (entitymoa.destPos - entitymoa.field_757_d) * f;
		return (MathHelper.sin(f1) + 1.0F) * f2;
	}

	protected float func_170_d(EntityLiving entityliving, float f) {
		return this.getWingRotation((EntityMoa)entityliving, f);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
		this.renderChicken((EntityMoa)entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.renderChicken((EntityMoa)entity, d, d1, d2, f, f1);
	}

	protected void scalemoa() {
		GL11.glScalef(1.8F, 1.8F, 1.8F);
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		if(!(entityliving instanceof EntityMoa) || !((EntityMoa)entityliving).baby) {
			this.scalemoa();
		}
	}
}
