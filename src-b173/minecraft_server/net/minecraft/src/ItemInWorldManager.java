package net.minecraft.src;

public class ItemInWorldManager {
	private WorldServer thisWorld;
	public EntityPlayer thisPlayer;
	private int s_field_22055_d;
	private int s_field_22054_g;
	private int s_field_22053_h;
	private int s_field_22052_i;
	private int s_field_22051_j;
	private boolean s_field_22050_k;
	private int s_field_22049_l;
	private int s_field_22048_m;
	private int s_field_22047_n;
	private int s_field_22046_o;

	public ItemInWorldManager(WorldServer worldServer1) {
		this.thisWorld = worldServer1;
	}

	public void s_func_328_a() {
		++this.s_field_22051_j;
		if(this.s_field_22050_k) {
			int i1 = this.s_field_22051_j - this.s_field_22046_o;
			int i2 = this.thisWorld.getBlockId(this.s_field_22049_l, this.s_field_22048_m, this.s_field_22047_n);
			if(i2 != 0) {
				Block block3 = Block.blocksList[i2];
				float f4 = block3.blockStrength(this.thisPlayer) * (float)(i1 + 1);
				if(f4 >= 1.0F) {
					this.s_field_22050_k = false;
					this.s_func_325_c(this.s_field_22049_l, this.s_field_22048_m, this.s_field_22047_n);
				}
			} else {
				this.s_field_22050_k = false;
			}
		}

	}

	public void s_func_324_a(int i1, int i2, int i3, int i4) {
		this.thisWorld.onBlockHit((EntityPlayer)null, i1, i2, i3, i4);
		this.s_field_22055_d = this.s_field_22051_j;
		int i5 = this.thisWorld.getBlockId(i1, i2, i3);
		if(i5 > 0) {
			Block.blocksList[i5].onBlockClicked(this.thisWorld, i1, i2, i3, this.thisPlayer);
		}

		if(i5 > 0 && Block.blocksList[i5].blockStrength(this.thisPlayer) >= 1.0F) {
			this.s_func_325_c(i1, i2, i3);
		} else {
			this.s_field_22054_g = i1;
			this.s_field_22053_h = i2;
			this.s_field_22052_i = i3;
		}

	}

	public void s_func_22045_b(int i1, int i2, int i3) {
		if(i1 == this.s_field_22054_g && i2 == this.s_field_22053_h && i3 == this.s_field_22052_i) {
			int i4 = this.s_field_22051_j - this.s_field_22055_d;
			int i5 = this.thisWorld.getBlockId(i1, i2, i3);
			if(i5 != 0) {
				Block block6 = Block.blocksList[i5];
				float f7 = block6.blockStrength(this.thisPlayer) * (float)(i4 + 1);
				if(f7 >= 0.7F) {
					this.s_func_325_c(i1, i2, i3);
				} else if(!this.s_field_22050_k) {
					this.s_field_22050_k = true;
					this.s_field_22049_l = i1;
					this.s_field_22048_m = i2;
					this.s_field_22047_n = i3;
					this.s_field_22046_o = this.s_field_22055_d;
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

	public boolean s_func_325_c(int i1, int i2, int i3) {
		int i4 = this.thisWorld.getBlockId(i1, i2, i3);
		int i5 = this.thisWorld.getBlockMetadata(i1, i2, i3);
		this.thisWorld.playAuxSFXAtEntity(this.thisPlayer, 2001, i1, i2, i3, i4 + this.thisWorld.getBlockMetadata(i1, i2, i3) * 256);
		boolean z6 = this.removeBlock(i1, i2, i3);
		ItemStack itemStack7 = this.thisPlayer.getCurrentEquippedItem();
		if(itemStack7 != null) {
			itemStack7.onDestroyBlock(i4, i1, i2, i3, this.thisPlayer);
			if(itemStack7.stackSize == 0) {
				itemStack7.func_1097_a(this.thisPlayer);
				this.thisPlayer.destroyCurrentEquippedItem();
			}
		}

		if(z6 && this.thisPlayer.canHarvestBlock(Block.blocksList[i4])) {
			Block.blocksList[i4].harvestBlock(this.thisWorld, this.thisPlayer, i1, i2, i3, i5);
			((EntityPlayerMP)this.thisPlayer).playerNetServerHandler.sendPacket(new Packet53BlockChange(i1, i2, i3, this.thisWorld));
		}

		return z6;
	}

	public boolean s_func_6154_a(EntityPlayer entityPlayer1, World world2, ItemStack itemStack3) {
		int i4 = itemStack3.stackSize;
		ItemStack itemStack5 = itemStack3.useItemRightClick(world2, entityPlayer1);
		if(itemStack5 != itemStack3 || itemStack5 != null && itemStack5.stackSize != i4) {
			entityPlayer1.inventory.mainInventory[entityPlayer1.inventory.currentItem] = itemStack5;
			if(itemStack5.stackSize == 0) {
				entityPlayer1.inventory.mainInventory[entityPlayer1.inventory.currentItem] = null;
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean activeBlockOrUseItem(EntityPlayer entityPlayer1, World world2, ItemStack itemStack3, int i4, int i5, int i6, int i7) {
		int i8 = world2.getBlockId(i4, i5, i6);
		return i8 > 0 && Block.blocksList[i8].blockActivated(world2, i4, i5, i6, entityPlayer1) ? true : (itemStack3 == null ? false : itemStack3.useItem(entityPlayer1, world2, i4, i5, i6, i7));
	}
}
