package net.minecraft.src;

public class ModelFlyingPig2 extends ModelBase {
	private ModelRenderer leftWingInner = new ModelRenderer(0, 0);
	private ModelRenderer leftWingOuter = new ModelRenderer(20, 0);
	private ModelRenderer rightWingInner = new ModelRenderer(0, 0);
	private ModelRenderer rightWingOuter = new ModelRenderer(40, 0);
	public static EntityPhyg pig;

	public ModelFlyingPig2() {
		this.leftWingInner.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
		this.leftWingOuter.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
		this.rightWingInner.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
		this.rightWingOuter.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
		this.rightWingOuter.rotateAngleY = (float)Math.PI;
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		float wingBend = -((float)Math.acos((double)pig.wingFold));
		float x = 32.0F * pig.wingFold / 4.0F;
		float y = -32.0F * (float)Math.sqrt((double)(1.0F - pig.wingFold * pig.wingFold)) / 4.0F;
		float z = 0.0F;
		float x2 = x * (float)Math.cos((double)pig.wingAngle) - y * (float)Math.sin((double)pig.wingAngle);
		float y2 = x * (float)Math.sin((double)pig.wingAngle) + y * (float)Math.cos((double)pig.wingAngle);
		this.leftWingInner.setRotationPoint(4.0F + x2, y2 + 12.0F, z);
		this.rightWingInner.setRotationPoint(-4.0F - x2, y2 + 12.0F, z);
		x *= 3.0F;
		x2 = x * (float)Math.cos((double)pig.wingAngle) - y * (float)Math.sin((double)pig.wingAngle);
		y2 = x * (float)Math.sin((double)pig.wingAngle) + y * (float)Math.cos((double)pig.wingAngle);
		this.leftWingOuter.setRotationPoint(4.0F + x2, y2 + 12.0F, z);
		this.rightWingOuter.setRotationPoint(-4.0F - x2, y2 + 12.0F, z);
		this.leftWingInner.rotateAngleZ = pig.wingAngle + wingBend + (float)Math.PI / 2F;
		this.leftWingOuter.rotateAngleZ = pig.wingAngle - wingBend + (float)Math.PI / 2F;
		this.rightWingInner.rotateAngleZ = -(pig.wingAngle + wingBend - (float)Math.PI / 2F);
		this.rightWingOuter.rotateAngleZ = -(pig.wingAngle - wingBend + (float)Math.PI / 2F);
		this.leftWingOuter.render(f5);
		this.leftWingInner.render(f5);
		this.rightWingOuter.render(f5);
		this.rightWingInner.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
	}
}
