package net.minecraft.src;

public final class ItemStack {
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public NBTTagCompound stackTagCompound;
	private int itemDamage;

	public ItemStack(Block block1) {
		this((Block)block1, 1);
	}

	public ItemStack(Block block1, int i2) {
		this(block1.blockID, i2, 0);
	}

	public ItemStack(Block block1, int i2, int i3) {
		this(block1.blockID, i2, i3);
	}

	public ItemStack(Item item1) {
		this(item1.shiftedIndex, 1, 0);
	}

	public ItemStack(Item item1, int i2) {
		this(item1.shiftedIndex, i2, 0);
	}

	public ItemStack(Item item1, int i2, int i3) {
		this(item1.shiftedIndex, i2, i3);
	}

	public ItemStack(int i1, int i2, int i3) {
		this.stackSize = 0;
		this.itemID = i1;
		this.stackSize = i2;
		this.itemDamage = i3;
	}

	public static ItemStack loadItemStackFromNBT(NBTTagCompound nBTTagCompound0) {
		ItemStack itemStack1 = new ItemStack();
		itemStack1.readFromNBT(nBTTagCompound0);
		return itemStack1.getItem() != null ? itemStack1 : null;
	}

	private ItemStack() {
		this.stackSize = 0;
	}

	public ItemStack splitStack(int i1) {
		ItemStack itemStack2 = new ItemStack(this.itemID, i1, this.itemDamage);
		if(this.stackTagCompound != null) {
			itemStack2.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
		}

		this.stackSize -= i1;
		return itemStack2;
	}

	public Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public boolean useItem(EntityPlayer entityPlayer1, World world2, int i3, int i4, int i5, int i6) {
		boolean z7 = this.getItem().onItemUse(this, entityPlayer1, world2, i3, i4, i5, i6);
		if(z7) {
			entityPlayer1.addStat(StatList.objectUseStats[this.itemID], 1);
		}

		return z7;
	}

	public float getStrVsBlock(Block block1) {
		return this.getItem().getStrVsBlock(this, block1);
	}

	public ItemStack useItemRightClick(World world1, EntityPlayer entityPlayer2) {
		return this.getItem().onItemRightClick(this, world1, entityPlayer2);
	}

	public ItemStack onFoodEaten(World world1, EntityPlayer entityPlayer2) {
		return this.getItem().onFoodEaten(this, world1, entityPlayer2);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("id", (short)this.itemID);
		nBTTagCompound1.setByte("Count", (byte)this.stackSize);
		nBTTagCompound1.setShort("Damage", (short)this.itemDamage);
		if(this.stackTagCompound != null) {
			nBTTagCompound1.setTag("tag", this.stackTagCompound);
		}

		return nBTTagCompound1;
	}

	public void readFromNBT(NBTTagCompound nBTTagCompound1) {
		this.itemID = nBTTagCompound1.getShort("id");
		this.stackSize = nBTTagCompound1.getByte("Count");
		this.itemDamage = nBTTagCompound1.getShort("Damage");
		if(nBTTagCompound1.hasKey("tag")) {
			this.stackTagCompound = nBTTagCompound1.getCompoundTag("tag");
		}

	}

	public int getMaxStackSize() {
		return this.getItem().getItemStackLimit();
	}

	public boolean isStackable() {
		return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
	}

	public boolean isItemStackDamageable() {
		return Item.itemsList[this.itemID].getMaxDamage() > 0;
	}

	public boolean getHasSubtypes() {
		return Item.itemsList[this.itemID].getHasSubtypes();
	}

	public boolean isItemDamaged() {
		return this.isItemStackDamageable() && this.itemDamage > 0;
	}

	public int getItemDamageForDisplay() {
		return this.itemDamage;
	}

	public int getItemDamage() {
		return this.itemDamage;
	}

	public void setItemDamage(int i1) {
		this.itemDamage = i1;
	}

	public int getMaxDamage() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	public void damageItem(int i1, EntityLiving entityLiving2) {
		if(this.isItemStackDamageable()) {
			if(i1 > 0 && entityLiving2 instanceof EntityPlayer) {
				int i3 = EnchantmentHelper.getUnbreakingModifier(((EntityPlayer)entityLiving2).inventory);
				if(i3 > 0 && entityLiving2.worldObj.rand.nextInt(i3 + 1) > 0) {
					return;
				}
			}

			this.itemDamage += i1;
			if(this.itemDamage > this.getMaxDamage()) {
				entityLiving2.renderBrokenItemStack(this);
				if(entityLiving2 instanceof EntityPlayer) {
					((EntityPlayer)entityLiving2).addStat(StatList.objectBreakStats[this.itemID], 1);
				}

				--this.stackSize;
				if(this.stackSize < 0) {
					this.stackSize = 0;
				}

				this.itemDamage = 0;
			}

		}
	}

	public void hitEntity(EntityLiving entityLiving1, EntityPlayer entityPlayer2) {
		boolean z3 = Item.itemsList[this.itemID].hitEntity(this, entityLiving1, entityPlayer2);
		if(z3) {
			entityPlayer2.addStat(StatList.objectUseStats[this.itemID], 1);
		}

	}

	public void onDestroyBlock(int i1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		boolean z6 = Item.itemsList[this.itemID].onBlockDestroyed(this, i1, i2, i3, i4, entityPlayer5);
		if(z6) {
			entityPlayer5.addStat(StatList.objectUseStats[this.itemID], 1);
		}

	}

	public int getDamageVsEntity(Entity entity1) {
		return Item.itemsList[this.itemID].getDamageVsEntity(entity1);
	}

	public boolean canHarvestBlock(Block block1) {
		return Item.itemsList[this.itemID].canHarvestBlock(block1);
	}

	public void onItemDestroyedByUse(EntityPlayer entityPlayer1) {
	}

	public void useItemOnEntity(EntityLiving entityLiving1) {
		Item.itemsList[this.itemID].useItemOnEntity(this, entityLiving1);
	}

	public ItemStack copy() {
		ItemStack itemStack1 = new ItemStack(this.itemID, this.stackSize, this.itemDamage);
		if(this.stackTagCompound != null) {
			itemStack1.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
			if(!itemStack1.stackTagCompound.equals(this.stackTagCompound)) {
				return itemStack1;
			}
		}

		return itemStack1;
	}

	public static boolean func_46124_a(ItemStack itemStack0, ItemStack itemStack1) {
		return itemStack0 == null && itemStack1 == null ? true : (itemStack0 != null && itemStack1 != null ? (itemStack0.stackTagCompound == null && itemStack1.stackTagCompound != null ? false : itemStack0.stackTagCompound == null || itemStack0.stackTagCompound.equals(itemStack1.stackTagCompound)) : false);
	}

	public static boolean areItemStacksEqual(ItemStack itemStack0, ItemStack itemStack1) {
		return itemStack0 == null && itemStack1 == null ? true : (itemStack0 != null && itemStack1 != null ? itemStack0.isItemStackEqual(itemStack1) : false);
	}

	private boolean isItemStackEqual(ItemStack itemStack1) {
		return this.stackSize != itemStack1.stackSize ? false : (this.itemID != itemStack1.itemID ? false : (this.itemDamage != itemStack1.itemDamage ? false : (this.stackTagCompound == null && itemStack1.stackTagCompound != null ? false : this.stackTagCompound == null || this.stackTagCompound.equals(itemStack1.stackTagCompound))));
	}

	public boolean isItemEqual(ItemStack itemStack1) {
		return this.itemID == itemStack1.itemID && this.itemDamage == itemStack1.itemDamage;
	}

	public String getItemName() {
		return Item.itemsList[this.itemID].getItemNameIS(this);
	}

	public static ItemStack copyItemStack(ItemStack itemStack0) {
		return itemStack0 == null ? null : itemStack0.copy();
	}

	public String toString() {
		return this.stackSize + "x" + Item.itemsList[this.itemID].getItemName() + "@" + this.itemDamage;
	}

	public void updateAnimation(World world1, Entity entity2, int i3, boolean z4) {
		if(this.animationsToGo > 0) {
			--this.animationsToGo;
		}

		Item.itemsList[this.itemID].onUpdate(this, world1, entity2, i3, z4);
	}

	public void onCrafting(World world1, EntityPlayer entityPlayer2, int i3) {
		entityPlayer2.addStat(StatList.objectCraftStats[this.itemID], i3);
		Item.itemsList[this.itemID].onCreated(this, world1, entityPlayer2);
	}

	public boolean isStackEqual(ItemStack itemStack1) {
		return this.itemID == itemStack1.itemID && this.stackSize == itemStack1.stackSize && this.itemDamage == itemStack1.itemDamage;
	}

	public int getMaxItemUseDuration() {
		return this.getItem().getMaxItemUseDuration(this);
	}

	public EnumAction getItemUseAction() {
		return this.getItem().getItemUseAction(this);
	}

	public void onPlayerStoppedUsing(World world1, EntityPlayer entityPlayer2, int i3) {
		this.getItem().onPlayerStoppedUsing(this, world1, entityPlayer2, i3);
	}

	public boolean hasTagCompound() {
		return this.stackTagCompound != null;
	}

	public NBTTagCompound getTagCompound() {
		return this.stackTagCompound;
	}

	public NBTTagList getEnchantmentTagList() {
		return this.stackTagCompound == null ? null : (NBTTagList)this.stackTagCompound.getTag("ench");
	}

	public void setTagCompound(NBTTagCompound nBTTagCompound1) {
		this.stackTagCompound = nBTTagCompound1;
	}

	public boolean isItemEnchantable() {
		return !this.getItem().isItemTool(this) ? false : !this.isItemEnchanted();
	}

	public void addEnchantment(Enchantment enchantment1, int i2) {
		if(this.stackTagCompound == null) {
			this.setTagCompound(new NBTTagCompound());
		}

		if(!this.stackTagCompound.hasKey("ench")) {
			this.stackTagCompound.setTag("ench", new NBTTagList("ench"));
		}

		NBTTagList nBTTagList3 = (NBTTagList)this.stackTagCompound.getTag("ench");
		NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
		nBTTagCompound4.setShort("id", (short)enchantment1.effectId);
		nBTTagCompound4.setShort("lvl", (short)((byte)i2));
		nBTTagList3.appendTag(nBTTagCompound4);
	}

	public boolean isItemEnchanted() {
		return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench");
	}
}
