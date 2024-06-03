package net.minecraft.src;

public class ItemInWorldManager {
	private WorldServer thisWorld;
	public EntityPlayer thisPlayer;
	private int initialDamage;
	private int curBlockX;
	private int curBlockY;
	private int curBlockZ;
	private int curblockDamage;
	private boolean isRemoving;
	private int lastBlockX;
	private int lastBlockY;
	private int lastBlockZ;
	private int lastDamage;

	public ItemInWorldManager(WorldServer worldServer1) {
		this.thisWorld = worldServer1;
	}

	public void updateBlockRemoving() {
		++this.curblockDamage;
		if(this.isRemoving) {
			int i1 = this.curblockDamage - this.lastDamage;
			int i2 = this.thisWorld.getBlockId(this.lastBlockX, this.lastBlockY, this.lastBlockZ);
			int meta = this.thisWorld.getBlockId(this.lastBlockX, this.lastBlockY, this.lastBlockZ);
			if(i2 != 0) {
				Block block3 = Block.blocksList[i2];
				float f4 = block3.blockStrength(this.thisPlayer, meta) * (float)(i1 + 1);
				if(f4 >= 1.0F) {
					this.isRemoving = false;
					this.blockHarvested(this.lastBlockX, this.lastBlockY, this.lastBlockZ);
				}
			} else {
				this.isRemoving = false;
			}
		}

	}

	public void blockClicked(int i1, int i2, int i3, int i4) {
		if(this.thisPlayer.isCreative) {
			if(!this.thisWorld.onBlockHit((EntityPlayer)null, i1, i2, i3, i4)) {
				this.blockHarvested(i1, i2, i3);
			}

		} else {
			this.thisWorld.onBlockHit((EntityPlayer)null, i1, i2, i3, i4);
			this.initialDamage = this.curblockDamage;
			int i5 = this.thisWorld.getBlockId(i1, i2, i3);
			int meta = this.thisWorld.getBlockMetadata(i1, i2, i3);
			if(i5 > 0) {
				Block.blocksList[i5].onBlockClicked(this.thisWorld, i1, i2, i3, this.thisPlayer);
			}
	
			if(i5 > 0 && Block.blocksList[i5].blockStrength(this.thisPlayer, meta) >= 1.0F) {
				this.blockHarvested(i1, i2, i3);
			} else {
				this.curBlockX = i1;
				this.curBlockY = i2;
				this.curBlockZ = i3;
			}

		}
	}

	public void blockRemoving(int i1, int i2, int i3) {
		if(i1 == this.curBlockX && i2 == this.curBlockY && i3 == this.curBlockZ) {
			int i4 = this.curblockDamage - this.initialDamage;
			int i5 = this.thisWorld.getBlockId(i1, i2, i3);
			int meta = this.thisWorld.getBlockMetadata(i1, i2, i3);
			if(i5 != 0) {
				Block block6 = Block.blocksList[i5];
				float f7 = block6.blockStrength(this.thisPlayer, meta) * (float)(i4 + 1);
				if(f7 >= 0.7F || this.thisPlayer.isCreative) {
					this.blockHarvested(i1, i2, i3);
				} else if(!this.isRemoving) {
					this.isRemoving = true;
					this.lastBlockX = i1;
					this.lastBlockY = i2;
					this.lastBlockZ = i3;
					this.lastDamage = this.initialDamage;
				}
			}
		}
	}

	public boolean removeBlock(int i1, int i2, int i3) {
		Block block4 = Block.blocksList[this.thisWorld.getBlockId(i1, i2, i3)];
		int i5 = this.thisWorld.getBlockMetadata(i1, i2, i3);
		boolean z6 = this.thisWorld.setBlockWithNotify(i1, i2, i3, 0);
		if(block4 != null && z6) {
			block4.onBlockDestroyedByPlayer(this.thisWorld, i1, i2, i3, i5);
		}

		return z6;
	}

	public boolean blockHarvested(int x, int y, int z) {
		int blockID = this.thisWorld.getBlockId(x, y, z);
		int meta = this.thisWorld.getBlockMetadata(x, y, z);
		this.thisWorld.playAuxSFXAtEntity(this.thisPlayer, 2001, x, y, z, blockID + this.thisWorld.getBlockMetadata(x, y, z) * 256);
		boolean wasRemoved = this.removeBlock(x, y, z);
		if(this.thisPlayer.isCreative) {
			((EntityPlayerMP)this.thisPlayer).playerNetServerHandler.sendPacket(new Packet53BlockChange(x, y, z, this.thisWorld));
		} else {
			ItemStack itemStack = this.thisPlayer.getCurrentEquippedItem();
			if(itemStack != null) {
				itemStack.onDestroyBlock(blockID, x, y, z, this.thisPlayer);
				if(itemStack.stackSize == 0) {
					itemStack.onItemDestroyedByUse(this.thisPlayer);
					this.thisPlayer.destroyCurrentEquippedItem();
				}
			}
	
			if(wasRemoved && this.thisPlayer.canHarvestBlock(Block.blocksList[blockID], meta)) {
				if(!this.thisPlayer.isCreative)  {
					// Gold tools have silver touch
					if(itemStack != null && itemStack.getItem() != null && itemStack.getItem().silkTouch) {
						Block.blocksList[blockID].silkTouchBlock(this.thisWorld, x, y, z, meta);
					} else {
						Block.blocksList[blockID].harvestBlock(this.thisWorld, this.thisPlayer, x, y, z, meta);
					}
				}
				((EntityPlayerMP)this.thisPlayer).playerNetServerHandler.sendPacket(new Packet53BlockChange(x, y, z, this.thisWorld));
			}
		}
	
		return wasRemoved;
	}

	public boolean itemUsed(EntityPlayer entityPlayer1, World world2, ItemStack itemStack3) {
		int i4 = itemStack3.stackSize;
		int i5 = itemStack3.itemDamage;
		ItemStack itemStack5 = itemStack3.useItemRightClick(world2, entityPlayer1);
		if(itemStack5 != itemStack3 || itemStack5 != null && itemStack5.stackSize != i4) {
			entityPlayer1.inventory.mainInventory[entityPlayer1.inventory.currentItem] = itemStack5;
			
			if(this.thisPlayer.isCreative) {
				itemStack5.stackSize = i4;
				itemStack5.setItemDamage(i5);
			}
			
			if(itemStack5.stackSize == 0) {
				entityPlayer1.inventory.mainInventory[entityPlayer1.inventory.currentItem] = null;
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean activeBlockOrUseItem(EntityPlayer entityPlayer1, World world2, ItemStack itemStack3, int i4, int i5, int i6, int i7, float xWithinFace, float yWithinFace, float zWithinFace) {
		int i8 = world2.getBlockId(i4, i5, i6);
		return i8 > 0 && Block.blocksList[i8].blockActivated(world2, i4, i5, i6, entityPlayer1) ? true : (itemStack3 == null ? false : itemStack3.useItem(entityPlayer1, world2, i4, i5, i6, i7, xWithinFace, yWithinFace, zWithinFace));
	}
}
