package net.minecraft.src;

public class ModelMagmaCube extends ModelBase {
	ModelRenderer[] field_40345_a = new ModelRenderer[8];
	ModelRenderer field_40344_b;

	public ModelMagmaCube() {
		for(int i1 = 0; i1 < this.field_40345_a.length; ++i1) {
			byte b2 = 0;
			int i3 = i1;
			if(i1 == 2) {
				b2 = 24;
				i3 = 10;
			} else if(i1 == 3) {
				b2 = 24;
				i3 = 19;
			}

			this.field_40345_a[i1] = new ModelRenderer(this, b2, i3);
			this.field_40345_a[i1].addBox(-4.0F, (float)(16 + i1), -4.0F, 8, 1, 8);
		}

		this.field_40344_b = new ModelRenderer(this, 0, 16);
		this.field_40344_b.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
	}

	public int func_40343_a() {
		return 5;
	}

	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6) {
	}

	public void setLivingAnimations(EntityLiving entityLiving1, float f2, float f3, float f4) {
		EntityMagmaCube entityMagmaCube5 = (EntityMagmaCube)entityLiving1;
		float f6 = entityMagmaCube5.field_767_b + (entityMagmaCube5.field_768_a - entityMagmaCube5.field_767_b) * f4;
		if(f6 < 0.0F) {
			f6 = 0.0F;
		}

		for(int i7 = 0; i7 < this.field_40345_a.length; ++i7) {
			this.field_40345_a[i7].rotationPointY = (float)(-(4 - i7)) * f6 * 1.7F;
		}

	}

	public void render(Entity entity1, float f2, float f3, float f4, float f5, float f6, float f7) {
		this.setRotationAngles(f2, f3, f4, f5, f6, f7);
		this.field_40344_b.render(f7);

		for(int i8 = 0; i8 < this.field_40345_a.length; ++i8) {
			this.field_40345_a[i8].render(f7);
		}

	}
}
