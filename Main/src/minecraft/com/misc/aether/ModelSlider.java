package com.misc.aether;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

public class ModelSlider extends ModelBase {
	public ModelRenderer head;

	public ModelSlider() {
		this(0.0F);
	}

	public ModelSlider(float f) {
		this(f, 0.0F);
	}

	public ModelSlider(float f, float f1) {
		this.head = new ModelRenderer(0, 0);
		this.head.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16, f);
		this.head.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		this.head.render(f5);
		GL11.glPopMatrix();
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		this.head.rotateAngleY = 0.0F;
		this.head.rotateAngleX = 0.0F;
	}
}
