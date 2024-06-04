package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelHomeShot extends ModelBase {
	public ModelRenderer[] head;
	public float[] sinage;
	private static final float sponge = 57.295773F;

	public ModelHomeShot() {
		this(0.0F);
	}

	public ModelHomeShot(float f) {
		this(f, 0.0F);
	}

	public ModelHomeShot(float f, float f1) {
		this.sinage = new float[3];
		this.head = new ModelRenderer[3];
		this.head[0] = new ModelRenderer(0, 0);
		this.head[1] = new ModelRenderer(32, 0);
		this.head[2] = new ModelRenderer(0, 16);

		for(int i = 0; i < 3; ++i) {
			this.head[i].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, f);
			this.head[i].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		}

	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		GL11.glTranslatef(0.0F, 0.75F, 0.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		GL11.glRotatef(this.sinage[0] * 57.295773F, 1.0F, 0.0F, 0.0F);
		this.head[0].render(f5);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glRotatef(this.sinage[1] * 57.295773F, 0.0F, 1.0F, 0.0F);
		this.head[1].render(f5);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glRotatef(this.sinage[2] * 57.295773F, 0.0F, 0.0F, 1.0F);
		this.head[2].render(f5);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		for(int i = 0; i < 3; ++i) {
			this.head[i].rotateAngleY = f3 / 57.29578F;
			this.head[i].rotateAngleX = f4 / 57.29578F;
		}

	}
}
