package net.minecraft.src;

public class ItemPigSlayer extends ItemSword {
	public ItemPigSlayer(int i) {
		super(i, EnumToolMaterial.IRON);
		this.setMaxDamage(0);
	}

	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		if(entityliving != null && entityliving1 != null) {
			String s = EntityList.getEntityString(entityliving);
			if(!s.equals("") && s.toLowerCase().contains("pig")) {
				if(entityliving.health > 0) {
					entityliving.health = 1;
					entityliving.hurtTime = 0;
					entityliving.attackEntityFrom(entityliving1, 9999);
				}

				for(int j = 0; j < 20; ++j) {
					double d = entityliving1.rand.nextGaussian() * 0.02D;
					double d1 = entityliving1.rand.nextGaussian() * 0.02D;
					double d2 = entityliving1.rand.nextGaussian() * 0.02D;
					double d3 = 5.0D;
					entityliving.worldObj.spawnParticle("flame", entityliving.posX + (double)(entityliving.rand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width - d * d3, entityliving.posY + (double)(entityliving.rand.nextFloat() * entityliving.height) - d1 * d3, entityliving.posZ + (double)(entityliving.rand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width - d2 * d3, d, d1, d2);
				}

				entityliving.dropFewItems();
				entityliving.isDead = true;
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean func_25008_a(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
		return true;
	}
}
