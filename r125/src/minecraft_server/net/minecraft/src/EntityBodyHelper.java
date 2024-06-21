package net.minecraft.src;

public class EntityBodyHelper {
	private EntityLiving field_48435_a;
	private int field_48433_b = 0;
	private float field_48434_c = 0.0F;

	public EntityBodyHelper(EntityLiving entityLiving1) {
		this.field_48435_a = entityLiving1;
	}

	public void func_48431_a() {
		double d1 = this.field_48435_a.posX - this.field_48435_a.prevPosX;
		double d3 = this.field_48435_a.posZ - this.field_48435_a.prevPosZ;
		if(d1 * d1 + d3 * d3 > 2.500000277905201E-7D) {
			this.field_48435_a.renderYawOffset = this.field_48435_a.rotationYaw;
			this.field_48435_a.rotationYawHead = this.func_48432_a(this.field_48435_a.renderYawOffset, this.field_48435_a.rotationYawHead, 75.0F);
			this.field_48434_c = this.field_48435_a.rotationYawHead;
			this.field_48433_b = 0;
		} else {
			float f5 = 75.0F;
			if(Math.abs(this.field_48435_a.rotationYawHead - this.field_48434_c) > 15.0F) {
				this.field_48433_b = 0;
				this.field_48434_c = this.field_48435_a.rotationYawHead;
			} else {
				++this.field_48433_b;
				if(this.field_48433_b > 10) {
					f5 = Math.max(1.0F - (float)(this.field_48433_b - 10) / 10.0F, 0.0F) * 75.0F;
				}
			}

			this.field_48435_a.renderYawOffset = this.func_48432_a(this.field_48435_a.rotationYawHead, this.field_48435_a.renderYawOffset, f5);
		}
	}

	private float func_48432_a(float f1, float f2, float f3) {
		float f4;
		for(f4 = f1 - f2; f4 < -180.0F; f4 += 360.0F) {
		}

		while(f4 >= 180.0F) {
			f4 -= 360.0F;
		}

		if(f4 < -f3) {
			f4 = -f3;
		}

		if(f4 >= f3) {
			f4 = f3;
		}

		return f1 - f4;
	}
}
