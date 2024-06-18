package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderEnderCrystal extends Render {
	private int field_41037_a = -1;
	private ModelBase field_41036_b;

	public RenderEnderCrystal() {
		this.shadowSize = 0.5F;
	}

	public void func_41035_a(EntityEnderCrystal entityEnderCrystal1, double d2, double d4, double d6, float f8, float f9) {
		if(this.field_41037_a != 1) {
			this.field_41036_b = new ModelEnderCrystal(0.0F);
			this.field_41037_a = 1;
		}

		float f10 = (float)entityEnderCrystal1.innerRotation + f9;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		this.loadTexture("/mob/enderdragon/crystal.png");
		float f11 = MathHelper.sin(f10 * 0.2F) / 2.0F + 0.5F;
		f11 += f11 * f11;
		this.field_41036_b.render(entityEnderCrystal1, 0.0F, f10 * 3.0F, f11 * 0.2F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.func_41035_a((EntityEnderCrystal)entity1, d2, d4, d6, f8, f9);
	}
}
