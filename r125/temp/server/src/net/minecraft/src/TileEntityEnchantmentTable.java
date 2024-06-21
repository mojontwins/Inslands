package net.minecraft.src;

import java.util.Random;

public class TileEntityEnchantmentTable extends TileEntity {
	public int tickCount;
	public float pageFlip;
	public float pageFlipPrev;
	public float field_40066_d;
	public float field_40067_e;
	public float bookSpread;
	public float bookSpreadPrev;
	public float bookRotation2;
	public float bookRotationPrev;
	public float bookRotation;
	private static Random rand = new Random();

	public void updateEntity() {
		super.updateEntity();
		this.bookSpreadPrev = this.bookSpread;
		this.bookRotationPrev = this.bookRotation2;
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayer((double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 0.5F), (double)((float)this.zCoord + 0.5F), 3.0D);
		if(entityPlayer1 != null) {
			double d2 = entityPlayer1.posX - (double)((float)this.xCoord + 0.5F);
			double d4 = entityPlayer1.posZ - (double)((float)this.zCoord + 0.5F);
			this.bookRotation = (float)Math.atan2(d4, d2);
			this.bookSpread += 0.1F;
			if(this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
				float f6 = this.field_40066_d;

				do {
					this.field_40066_d += (float)(rand.nextInt(4) - rand.nextInt(4));
				} while(f6 == this.field_40066_d);
			}
		} else {
			this.bookRotation += 0.02F;
			this.bookSpread -= 0.1F;
		}

		while(this.bookRotation2 >= (float)Math.PI) {
			this.bookRotation2 -= 6.2831855F;
		}

		while(this.bookRotation2 < -3.1415927F) {
			this.bookRotation2 += 6.2831855F;
		}

		while(this.bookRotation >= (float)Math.PI) {
			this.bookRotation -= 6.2831855F;
		}

		while(this.bookRotation < -3.1415927F) {
			this.bookRotation += 6.2831855F;
		}

		float f7;
		for(f7 = this.bookRotation - this.bookRotation2; f7 >= (float)Math.PI; f7 -= 6.2831855F) {
		}

		while(f7 < -3.1415927F) {
			f7 += 6.2831855F;
		}

		this.bookRotation2 += f7 * 0.4F;
		if(this.bookSpread < 0.0F) {
			this.bookSpread = 0.0F;
		}

		if(this.bookSpread > 1.0F) {
			this.bookSpread = 1.0F;
		}

		++this.tickCount;
		this.pageFlipPrev = this.pageFlip;
		float f3 = (this.field_40066_d - this.pageFlip) * 0.4F;
		float f8 = 0.2F;
		if(f3 < -f8) {
			f3 = -f8;
		}

		if(f3 > f8) {
			f3 = f8;
		}

		this.field_40067_e += (f3 - this.field_40067_e) * 0.9F;
		this.pageFlip += this.field_40067_e;
	}
}
