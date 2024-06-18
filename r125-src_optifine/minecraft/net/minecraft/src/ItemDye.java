package net.minecraft.src;

public class ItemDye extends Item {
	public static final String[] dyeColorNames = new String[]{"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
	public static final int[] dyeColors = new int[]{1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 2651799, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

	public ItemDye(int i1) {
		super(i1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public int getIconFromDamage(int i1) {
		int i2 = MathHelper.clamp_int(i1, 0, 15);
		return this.iconIndex + i2 % 8 * 16 + i2 / 8;
	}

	public String getItemNameIS(ItemStack itemStack1) {
		int i2 = MathHelper.clamp_int(itemStack1.getItemDamage(), 0, 15);
		return super.getItemName() + "." + dyeColorNames[i2];
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(!entityPlayer2.canPlayerEdit(i4, i5, i6)) {
			return false;
		} else {
			if(itemStack1.getItemDamage() == 15) {
				int i8 = world3.getBlockId(i4, i5, i6);
				if(i8 == Block.sapling.blockID) {
					if(!world3.isRemote) {
						((BlockSapling)Block.sapling).growTree(world3, i4, i5, i6, world3.rand);
						--itemStack1.stackSize;
					}

					return true;
				}

				if(i8 == Block.mushroomBrown.blockID || i8 == Block.mushroomRed.blockID) {
					if(!world3.isRemote && ((BlockMushroom)Block.blocksList[i8]).fertilizeMushroom(world3, i4, i5, i6, world3.rand)) {
						--itemStack1.stackSize;
					}

					return true;
				}

				if(i8 == Block.melonStem.blockID || i8 == Block.pumpkinStem.blockID) {
					if(!world3.isRemote) {
						((BlockStem)Block.blocksList[i8]).fertilizeStem(world3, i4, i5, i6);
						--itemStack1.stackSize;
					}

					return true;
				}

				if(i8 == Block.crops.blockID) {
					if(!world3.isRemote) {
						((BlockCrops)Block.crops).fertilize(world3, i4, i5, i6);
						--itemStack1.stackSize;
					}

					return true;
				}

				if(i8 == Block.grass.blockID) {
					if(!world3.isRemote) {
						--itemStack1.stackSize;

						label73:
						for(int i9 = 0; i9 < 128; ++i9) {
							int i10 = i4;
							int i11 = i5 + 1;
							int i12 = i6;

							for(int i13 = 0; i13 < i9 / 16; ++i13) {
								i10 += itemRand.nextInt(3) - 1;
								i11 += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
								i12 += itemRand.nextInt(3) - 1;
								if(world3.getBlockId(i10, i11 - 1, i12) != Block.grass.blockID || world3.isBlockNormalCube(i10, i11, i12)) {
									continue label73;
								}
							}

							if(world3.getBlockId(i10, i11, i12) == 0) {
								if(itemRand.nextInt(10) != 0) {
									world3.setBlockAndMetadataWithNotify(i10, i11, i12, Block.tallGrass.blockID, 1);
								} else if(itemRand.nextInt(3) != 0) {
									world3.setBlockWithNotify(i10, i11, i12, Block.plantYellow.blockID);
								} else {
									world3.setBlockWithNotify(i10, i11, i12, Block.plantRed.blockID);
								}
							}
						}
					}

					return true;
				}
			}

			return false;
		}
	}

	public void useItemOnEntity(ItemStack itemStack1, EntityLiving entityLiving2) {
		if(entityLiving2 instanceof EntitySheep) {
			EntitySheep entitySheep3 = (EntitySheep)entityLiving2;
			int i4 = BlockCloth.getBlockFromDye(itemStack1.getItemDamage());
			if(!entitySheep3.getSheared() && entitySheep3.getFleeceColor() != i4) {
				entitySheep3.setFleeceColor(i4);
				--itemStack1.stackSize;
			}
		}

	}
}
