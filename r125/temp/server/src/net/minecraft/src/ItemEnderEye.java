package net.minecraft.src;

public class ItemEnderEye extends Item {
	public ItemEnderEye(int i1) {
		super(i1);
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		int i8 = world3.getBlockId(i4, i5, i6);
		int i9 = world3.getBlockMetadata(i4, i5, i6);
		if(entityPlayer2.canPlayerEdit(i4, i5, i6) && i8 == Block.endPortalFrame.blockID && !BlockEndPortalFrame.isEnderEyeInserted(i9)) {
			if(world3.isRemote) {
				return true;
			} else {
				world3.setBlockMetadataWithNotify(i4, i5, i6, i9 + 4);
				--itemStack1.stackSize;

				int i10;
				for(i10 = 0; i10 < 16; ++i10) {
					double d11 = (double)((float)i4 + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
					double d13 = (double)((float)i5 + 0.8125F);
					double d15 = (double)((float)i6 + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
					double d17 = 0.0D;
					double d19 = 0.0D;
					double d21 = 0.0D;
					world3.spawnParticle("smoke", d11, d13, d15, d17, d19, d21);
				}

				i10 = i9 & 3;
				int i23 = 0;
				int i12 = 0;
				boolean z24 = false;
				boolean z14 = true;
				int i25 = Direction.enderEyeMetaToDirection[i10];

				int i16;
				int i18;
				int i20;
				int i26;
				int i27;
				for(i16 = -2; i16 <= 2; ++i16) {
					i26 = i4 + Direction.offsetX[i25] * i16;
					i18 = i6 + Direction.offsetZ[i25] * i16;
					i27 = world3.getBlockId(i26, i5, i18);
					if(i27 == Block.endPortalFrame.blockID) {
						i20 = world3.getBlockMetadata(i26, i5, i18);
						if(!BlockEndPortalFrame.isEnderEyeInserted(i20)) {
							z14 = false;
							break;
						}

						if(!z24) {
							i23 = i16;
							i12 = i16;
							z24 = true;
						} else {
							i12 = i16;
						}
					}
				}

				if(z14 && i12 == i23 + 2) {
					for(i16 = i23; i16 <= i12; ++i16) {
						i26 = i4 + Direction.offsetX[i25] * i16;
						i18 = i6 + Direction.offsetZ[i25] * i16;
						i26 += Direction.offsetX[i10] * 4;
						i18 += Direction.offsetZ[i10] * 4;
						i27 = world3.getBlockId(i26, i5, i18);
						i20 = world3.getBlockMetadata(i26, i5, i18);
						if(i27 != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(i20)) {
							z14 = false;
							break;
						}
					}

					for(i16 = i23 - 1; i16 <= i12 + 1; i16 += 4) {
						for(i26 = 1; i26 <= 3; ++i26) {
							i18 = i4 + Direction.offsetX[i25] * i16;
							i27 = i6 + Direction.offsetZ[i25] * i16;
							i18 += Direction.offsetX[i10] * i26;
							i27 += Direction.offsetZ[i10] * i26;
							i20 = world3.getBlockId(i18, i5, i27);
							int i28 = world3.getBlockMetadata(i18, i5, i27);
							if(i20 != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(i28)) {
								z14 = false;
								break;
							}
						}
					}

					if(z14) {
						for(i16 = i23; i16 <= i12; ++i16) {
							for(i26 = 1; i26 <= 3; ++i26) {
								i18 = i4 + Direction.offsetX[i25] * i16;
								i27 = i6 + Direction.offsetZ[i25] * i16;
								i18 += Direction.offsetX[i10] * i26;
								i27 += Direction.offsetZ[i10] * i26;
								world3.setBlockWithNotify(i18, i5, i27, Block.endPortal.blockID);
							}
						}
					}
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		MovingObjectPosition movingObjectPosition4 = this.getMovingObjectPositionFromPlayer(world2, entityPlayer3, false);
		if(movingObjectPosition4 != null && movingObjectPosition4.typeOfHit == EnumMovingObjectType.TILE) {
			int i5 = world2.getBlockId(movingObjectPosition4.blockX, movingObjectPosition4.blockY, movingObjectPosition4.blockZ);
			if(i5 == Block.endPortalFrame.blockID) {
				return itemStack1;
			}
		}

		if(!world2.isRemote) {
			ChunkPosition chunkPosition7 = world2.findClosestStructure("Stronghold", (int)entityPlayer3.posX, (int)entityPlayer3.posY, (int)entityPlayer3.posZ);
			if(chunkPosition7 != null) {
				EntityEnderEye entityEnderEye6 = new EntityEnderEye(world2, entityPlayer3.posX, entityPlayer3.posY + 1.62D - (double)entityPlayer3.yOffset, entityPlayer3.posZ);
				entityEnderEye6.func_40056_a((double)chunkPosition7.x, chunkPosition7.y, (double)chunkPosition7.z);
				world2.spawnEntityInWorld(entityEnderEye6);
				world2.playSoundAtEntity(entityPlayer3, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
				world2.playAuxSFXAtEntity((EntityPlayer)null, 1002, (int)entityPlayer3.posX, (int)entityPlayer3.posY, (int)entityPlayer3.posZ, 0);
				if(!entityPlayer3.capabilities.isCreativeMode) {
					--itemStack1.stackSize;
				}
			}
		}

		return itemStack1;
	}
}
