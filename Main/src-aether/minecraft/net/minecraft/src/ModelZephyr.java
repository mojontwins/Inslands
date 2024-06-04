package net.minecraft.src;

public class ModelZephyr extends ModelBase {
	ModelRenderer body;

	public ModelZephyr() {
		byte byte0 = -16;
		this.body = new ModelRenderer(0, 0);
		this.body.addBox(-8.0F, -4.0F, -8.0F, 10, 7, 12);
		this.body.rotationPointY += (float)(24 + byte0);
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		this.body.render(f5);
	}
}
