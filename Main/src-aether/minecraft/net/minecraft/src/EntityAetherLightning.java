package net.minecraft.src;

import java.util.List;

public class EntityAetherLightning extends EntityLightningBolt {
	private int field_27028_b;
	public long a = 0L;
	private int field_27030_c;

	public EntityAetherLightning(World var1, double var2, double var4, double var6) {
		super(var1, var2, var4, var6);
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.field_27028_b == 2) {
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
		}

		--this.field_27028_b;
		if(this.field_27028_b < 0) {
			if(this.field_27030_c == 0) {
				this.setEntityDead();
			} else if(this.field_27028_b < -this.rand.nextInt(10)) {
				--this.field_27030_c;
				this.field_27028_b = 1;
				this.a = this.rand.nextLong();
				if(this.worldObj.doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10)) {
					int var6 = MathHelper.floor_double(this.posX);
					int var2 = MathHelper.floor_double(this.posY);
					int var7 = MathHelper.floor_double(this.posZ);
					if(this.worldObj.getBlockId(var6, var2, var7) == 0 && Block.fire.canPlaceBlockAt(this.worldObj, var6, var2, var7)) {
						this.worldObj.setBlockWithNotify(var6, var2, var7, Block.fire.blockID);
					}
				}
			}
		}

		if(this.field_27028_b >= 0) {
			double d6 = 3.0D;
			List list7 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(this.posX - d6, this.posY - d6, this.posZ - d6, this.posX + d6, this.posY + 6.0D + d6, this.posZ + d6));

			for(int var4 = 0; var4 < list7.size(); ++var4) {
				Entity var5 = (Entity)list7.get(var4);
				if(!(var5 instanceof EntityPlayer)) {
					var5.onStruckByLightning(this);
				}
			}

			this.worldObj.field_27172_i = 2;
		}

	}
}
