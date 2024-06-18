package net.minecraft.src;

public class ItemMonsterPlacer extends Item {
	public ItemMonsterPlacer(int i1) {
		super(i1);
		this.setHasSubtypes(true);
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

			if(func_48390_a(world3, itemStack1.getItemDamage(), (double)i4 + 0.5D, (double)i5 + d9, (double)i6 + 0.5D) && !entityPlayer2.capabilities.isCreativeMode) {
				--itemStack1.stackSize;
			}

			return true;
		}
	}

	public static boolean func_48390_a(World world0, int i1, double d2, double d4, double d6) {
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
