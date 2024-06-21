package net.minecraft.src;

public class ItemMonsterPlacer extends Item {
	public ItemMonsterPlacer(int i1) {
		super(i1);
		this.setHasSubtypes(true);
	}

	public String getItemDisplayName(ItemStack itemStack1) {
		String string2 = ("" + StatCollector.translateToLocal(this.getItemName() + ".name")).trim();
		String string3 = EntityList.getStringFromID(itemStack1.getItemDamage());
		if(string3 != null) {
			string2 = string2 + " " + StatCollector.translateToLocal("entity." + string3 + ".name");
		}

		return string2;
	}

	public int getColorFromDamage(int i1, int i2) {
		EntityEggInfo entityEggInfo3 = (EntityEggInfo)EntityList.entityEggs.get(i1);
		return entityEggInfo3 != null ? (i2 == 0 ? entityEggInfo3.primaryColor : entityEggInfo3.secondaryColor) : 0xFFFFFF;
	}

	public boolean func_46058_c() {
		return true;
	}

	public int func_46057_a(int i1, int i2) {
		return i2 > 0 ? super.func_46057_a(i1, i2) + 16 : super.func_46057_a(i1, i2);
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(world3.isRemote) {
			return true;
		} else {
			int i8 = world3.getBlockId(i4, i5, i6);
			i4 += Facing.offsetsXForSide[i7];
			i5 += Facing.offsetsYForSide[i7];
			i6 += Facing.offsetsZForSide[i7];
			double d9 = 0.0D;
			if(i7 == 1 && i8 == Block.fence.blockID || i8 == Block.netherFence.blockID) {
				d9 = 0.5D;
			}

			if(func_48440_a(world3, itemStack1.getItemDamage(), (double)i4 + 0.5D, (double)i5 + d9, (double)i6 + 0.5D) && !entityPlayer2.capabilities.isCreativeMode) {
				--itemStack1.stackSize;
			}

			return true;
		}
	}

	public static boolean func_48440_a(World world0, int i1, double d2, double d4, double d6) {
		if(!EntityList.entityEggs.containsKey(i1)) {
			return false;
		} else {
			Entity entity8 = EntityList.createEntityByID(i1, world0);
			if(entity8 != null) {
				entity8.setLocationAndAngles(d2, d4, d6, world0.rand.nextFloat() * 360.0F, 0.0F);
				world0.spawnEntityInWorld(entity8);
				((EntityLiving)entity8).playLivingSound();
			}

			return entity8 != null;
		}
	}
}
