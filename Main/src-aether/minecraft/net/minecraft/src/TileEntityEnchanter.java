package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class TileEntityEnchanter extends TileEntity implements IInventory {
	private static List enchantments = new ArrayList();
	private ItemStack[] enchanterItemStacks = new ItemStack[3];
	public int enchantProgress = 0;
	public int enchantPowerRemaining = 0;
	public int enchantTimeForItem = 0;
	private Enchantment currentEnchantment;

	public int getSizeInventory() {
		return this.enchanterItemStacks.length;
	}

	public ItemStack getStackInSlot(int i) {
		return this.enchanterItemStacks[i];
	}

	public ItemStack decrStackSize(int i, int j) {
		if(this.enchanterItemStacks[i] != null) {
			ItemStack itemstack1;
			if(this.enchanterItemStacks[i].stackSize <= j) {
				itemstack1 = this.enchanterItemStacks[i];
				this.enchanterItemStacks[i] = null;
				return itemstack1;
			} else {
				itemstack1 = this.enchanterItemStacks[i].splitStack(j);
				if(this.enchanterItemStacks[i].stackSize == 0) {
					this.enchanterItemStacks[i] = null;
				}

				return itemstack1;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.enchanterItemStacks[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

	}

	public String getInvName() {
		return "Enchanter";
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		this.enchanterItemStacks = new ItemStack[this.getSizeInventory()];

		for(int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if(byte0 >= 0 && byte0 < this.enchanterItemStacks.length) {
				this.enchanterItemStacks[byte0] = new ItemStack(nbttagcompound1);
			}
		}

		this.enchantProgress = nbttagcompound.getShort("BurnTime");
		this.enchantTimeForItem = nbttagcompound.getShort("CookTime");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("BurnTime", (short)this.enchantProgress);
		nbttagcompound.setShort("CookTime", (short)this.enchantTimeForItem);
		NBTTagList nbttaglist = new NBTTagList();

		for(int i = 0; i < this.enchanterItemStacks.length; ++i) {
			if(this.enchanterItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.enchanterItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.setTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public int getCookProgressScaled(int i) {
		return this.enchantTimeForItem == 0 ? 0 : this.enchantProgress * i / this.enchantTimeForItem;
	}

	public int getBurnTimeRemainingScaled(int i) {
		return this.enchantPowerRemaining * i / 500;
	}

	public boolean isBurning() {
		return this.enchantPowerRemaining > 0;
	}

	public void updateEntity() {
		if(this.enchantPowerRemaining > 0) {
			--this.enchantPowerRemaining;
			if(this.currentEnchantment != null) {
				++this.enchantProgress;
			}
		}

		if(this.currentEnchantment != null && (this.enchanterItemStacks[0] == null || this.enchanterItemStacks[0].itemID != this.currentEnchantment.enchantFrom.itemID)) {
			this.currentEnchantment = null;
			this.enchantProgress = 0;
		}

		if(this.currentEnchantment != null && this.enchantProgress >= this.currentEnchantment.enchantPowerNeeded) {
			if(this.enchanterItemStacks[2] == null) {
				this.setInventorySlotContents(2, new ItemStack(this.currentEnchantment.enchantTo.getItem(), 1, this.currentEnchantment.enchantTo.getItemDamage()));
			} else {
				this.setInventorySlotContents(2, new ItemStack(this.currentEnchantment.enchantTo.getItem(), this.getStackInSlot(2).stackSize + 1, this.currentEnchantment.enchantTo.getItemDamage()));
			}

			this.decrStackSize(0, 1);
			this.enchantProgress = 0;
			this.currentEnchantment = null;
			this.enchantTimeForItem = 0;
		}

		if(this.enchantPowerRemaining <= 0 && this.currentEnchantment != null && this.getStackInSlot(1) != null && this.getStackInSlot(1).itemID == AetherItems.AmbrosiumShard.shiftedIndex) {
			this.enchantPowerRemaining += 500;
			this.decrStackSize(1, 1);
		}

		if(this.currentEnchantment == null) {
			ItemStack itemstack = this.getStackInSlot(0);

			for(int i = 0; i < enchantments.size(); ++i) {
				if(itemstack != null && enchantments.get(i) != null && itemstack.itemID == ((Enchantment)enchantments.get(i)).enchantFrom.itemID) {
					if(this.enchanterItemStacks[2] == null) {
						this.currentEnchantment = (Enchantment)enchantments.get(i);
						this.enchantTimeForItem = this.currentEnchantment.enchantPowerNeeded;
					} else if(this.enchanterItemStacks[2].itemID == ((Enchantment)enchantments.get(i)).enchantTo.itemID && ((Enchantment)enchantments.get(i)).enchantTo.getItem().getItemStackLimit() > this.enchanterItemStacks[2].stackSize) {
						this.currentEnchantment = (Enchantment)enchantments.get(i);
						this.enchantTimeForItem = this.currentEnchantment.enchantPowerNeeded;
					}
				}
			}
		}

	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	public static void addEnchantment(ItemStack from, ItemStack to, int i) {
		enchantments.add(new Enchantment(from, to, i));
	}

	static {
		addEnchantment(new ItemStack(AetherBlocks.GravititeOre, 1), new ItemStack(AetherBlocks.EnchantedGravitite, 1), 1000);
		addEnchantment(new ItemStack(AetherItems.PickSkyroot, 1), new ItemStack(AetherItems.PickSkyroot, 1), 250);
		addEnchantment(new ItemStack(AetherItems.SwordSkyroot, 1), new ItemStack(AetherItems.SwordSkyroot, 1), 250);
		addEnchantment(new ItemStack(AetherItems.ShovelSkyroot, 1), new ItemStack(AetherItems.ShovelSkyroot, 1), 200);
		addEnchantment(new ItemStack(AetherItems.AxeSkyroot, 1), new ItemStack(AetherItems.AxeSkyroot, 1), 200);
		addEnchantment(new ItemStack(AetherItems.PickHolystone, 1), new ItemStack(AetherItems.PickHolystone, 1), 600);
		addEnchantment(new ItemStack(AetherItems.SwordHolystone, 1), new ItemStack(AetherItems.SwordHolystone, 1), 600);
		addEnchantment(new ItemStack(AetherItems.ShovelHolystone, 1), new ItemStack(AetherItems.ShovelHolystone, 1), 500);
		addEnchantment(new ItemStack(AetherItems.AxeHolystone, 1), new ItemStack(AetherItems.AxeHolystone, 1), 500);
		addEnchantment(new ItemStack(AetherItems.PickZanite, 1), new ItemStack(AetherItems.PickZanite, 1), 2500);
		addEnchantment(new ItemStack(AetherItems.SwordZanite, 1), new ItemStack(AetherItems.SwordZanite, 1), 2500);
		addEnchantment(new ItemStack(AetherItems.ShovelZanite, 1), new ItemStack(AetherItems.ShovelZanite, 1), 2000);
		addEnchantment(new ItemStack(AetherItems.AxeZanite, 1), new ItemStack(AetherItems.AxeZanite, 1), 2000);
		addEnchantment(new ItemStack(AetherItems.PickGravitite, 1), new ItemStack(AetherItems.PickGravitite, 1), 6000);
		addEnchantment(new ItemStack(AetherItems.SwordGravitite, 1), new ItemStack(AetherItems.SwordGravitite, 1), 6000);
		addEnchantment(new ItemStack(AetherItems.ShovelGravitite, 1), new ItemStack(AetherItems.ShovelGravitite, 1), 5000);
		addEnchantment(new ItemStack(AetherItems.AxeGravitite, 1), new ItemStack(AetherItems.AxeGravitite, 1), 5000);
		addEnchantment(new ItemStack(AetherItems.Dart, 1, 0), new ItemStack(AetherItems.Dart, 1, 2), 250);
		addEnchantment(new ItemStack(AetherItems.Bucket, 1, 2), new ItemStack(AetherItems.Bucket, 1, 3), 1000);
		addEnchantment(new ItemStack(Item.record13, 1), new ItemStack(AetherItems.BlueMusicDisk, 1), 2500);
		addEnchantment(new ItemStack(Item.recordCat, 1), new ItemStack(AetherItems.BlueMusicDisk, 1), 2500);
		addEnchantment(new ItemStack(Item.helmetLeather, 1), new ItemStack(Item.helmetLeather, 1), 400);
		addEnchantment(new ItemStack(Item.plateLeather, 1), new ItemStack(Item.plateLeather, 1), 500);
		addEnchantment(new ItemStack(Item.legsLeather, 1), new ItemStack(Item.legsLeather, 1), 500);
		addEnchantment(new ItemStack(Item.bootsLeather, 1), new ItemStack(Item.bootsLeather, 1), 400);
		addEnchantment(new ItemStack(Item.pickaxeWood, 1), new ItemStack(Item.pickaxeWood, 1), 500);
		addEnchantment(new ItemStack(Item.shovelWood, 1), new ItemStack(Item.shovelWood, 1), 400);
		addEnchantment(new ItemStack(Item.swordWood, 1), new ItemStack(Item.swordWood, 1), 500);
		addEnchantment(new ItemStack(Item.axeWood, 1), new ItemStack(Item.axeWood, 1), 400);
		addEnchantment(new ItemStack(Item.hoeWood, 1), new ItemStack(Item.hoeWood, 1), 300);
		addEnchantment(new ItemStack(Item.pickaxeStone, 1), new ItemStack(Item.pickaxeStone, 1), 1000);
		addEnchantment(new ItemStack(Item.shovelStone, 1), new ItemStack(Item.shovelStone, 1), 750);
		addEnchantment(new ItemStack(Item.swordStone, 1), new ItemStack(Item.swordStone, 1), 1000);
		addEnchantment(new ItemStack(Item.axeStone, 1), new ItemStack(Item.axeStone, 1), 750);
		addEnchantment(new ItemStack(Item.hoeStone, 1), new ItemStack(Item.hoeStone, 1), 750);
		addEnchantment(new ItemStack(Item.helmetSteel, 1), new ItemStack(Item.helmetSteel, 1), 1500);
		addEnchantment(new ItemStack(Item.plateSteel, 1), new ItemStack(Item.plateSteel, 1), 2000);
		addEnchantment(new ItemStack(Item.legsSteel, 1), new ItemStack(Item.legsSteel, 1), 2000);
		addEnchantment(new ItemStack(Item.bootsSteel, 1), new ItemStack(Item.bootsSteel, 1), 1500);
		addEnchantment(new ItemStack(Item.pickaxeSteel, 1), new ItemStack(Item.pickaxeSteel, 1), 2500);
		addEnchantment(new ItemStack(Item.shovelSteel, 1), new ItemStack(Item.shovelSteel, 1), 2000);
		addEnchantment(new ItemStack(Item.swordSteel, 1), new ItemStack(Item.swordSteel, 1), 2500);
		addEnchantment(new ItemStack(Item.axeSteel, 1), new ItemStack(Item.axeSteel, 1), 1500);
		addEnchantment(new ItemStack(Item.hoeSteel, 1), new ItemStack(Item.hoeSteel, 1), 1500);
		addEnchantment(new ItemStack(Item.helmetGold, 1), new ItemStack(Item.helmetGold, 1), 1000);
		addEnchantment(new ItemStack(Item.plateGold, 1), new ItemStack(Item.plateGold, 1), 1200);
		addEnchantment(new ItemStack(Item.legsGold, 1), new ItemStack(Item.legsGold, 1), 1200);
		addEnchantment(new ItemStack(Item.bootsGold, 1), new ItemStack(Item.bootsGold, 1), 1000);
		addEnchantment(new ItemStack(Item.pickaxeGold, 1), new ItemStack(Item.pickaxeGold, 1), 1500);
		addEnchantment(new ItemStack(Item.shovelGold, 1), new ItemStack(Item.shovelGold, 1), 1000);
		addEnchantment(new ItemStack(Item.swordGold, 1), new ItemStack(Item.swordGold, 1), 1500);
		addEnchantment(new ItemStack(Item.axeGold, 1), new ItemStack(Item.axeGold, 1), 1000);
		addEnchantment(new ItemStack(Item.hoeGold, 1), new ItemStack(Item.hoeGold, 1), 1000);
		addEnchantment(new ItemStack(Item.helmetDiamond, 1), new ItemStack(Item.helmetDiamond, 1), 5000);
		addEnchantment(new ItemStack(Item.plateDiamond, 1), new ItemStack(Item.plateDiamond, 1), 6000);
		addEnchantment(new ItemStack(Item.legsDiamond, 1), new ItemStack(Item.legsDiamond, 1), 6000);
		addEnchantment(new ItemStack(Item.bootsDiamond, 1), new ItemStack(Item.bootsDiamond, 1), 5000);
		addEnchantment(new ItemStack(Item.pickaxeDiamond, 1), new ItemStack(Item.pickaxeDiamond, 1), 7000);
		addEnchantment(new ItemStack(Item.shovelDiamond, 1), new ItemStack(Item.shovelDiamond, 1), 6000);
		addEnchantment(new ItemStack(Item.swordDiamond, 1), new ItemStack(Item.swordDiamond, 1), 6500);
		addEnchantment(new ItemStack(Item.axeDiamond, 1), new ItemStack(Item.axeDiamond, 1), 6000);
		addEnchantment(new ItemStack(Item.hoeDiamond, 1), new ItemStack(Item.hoeDiamond, 1), 6000);
		addEnchantment(new ItemStack(Item.fishingRod, 1), new ItemStack(Item.fishingRod, 1), 500);
		addEnchantment(new ItemStack(AetherBlocks.Quicksoil, 1), new ItemStack(AetherBlocks.QuicksoilGlass, 1), 250);
		addEnchantment(new ItemStack(AetherBlocks.Holystone, 1), new ItemStack(AetherItems.HealingStone, 1), 750);
		addEnchantment(new ItemStack(AetherItems.GravititeHelmet, 1), new ItemStack(AetherItems.GravititeHelmet, 1), 12000);
		addEnchantment(new ItemStack(AetherItems.GravititeBodyplate, 1), new ItemStack(AetherItems.GravititeBodyplate, 1), 20000);
		addEnchantment(new ItemStack(AetherItems.GravititePlatelegs, 1), new ItemStack(AetherItems.GravititePlatelegs, 1), 15000);
		addEnchantment(new ItemStack(AetherItems.GravititeBoots, 1), new ItemStack(AetherItems.GravititeBoots, 1), 12000);
		addEnchantment(new ItemStack(AetherItems.GravititeGlove, 1), new ItemStack(AetherItems.GravititeGlove, 1), 10000);
		addEnchantment(new ItemStack(AetherItems.ZaniteHelmet, 1), new ItemStack(AetherItems.ZaniteHelmet, 1), 6000);
		addEnchantment(new ItemStack(AetherItems.ZaniteChestplate, 1), new ItemStack(AetherItems.ZaniteChestplate, 1), 10000);
		addEnchantment(new ItemStack(AetherItems.ZaniteLeggings, 1), new ItemStack(AetherItems.ZaniteLeggings, 1), 8000);
		addEnchantment(new ItemStack(AetherItems.ZaniteBoots, 1), new ItemStack(AetherItems.ZaniteBoots, 1), 5000);
		addEnchantment(new ItemStack(AetherItems.ZaniteGlove, 1), new ItemStack(AetherItems.ZaniteGlove, 1), 4000);
		addEnchantment(new ItemStack(AetherItems.ZaniteRing, 1), new ItemStack(AetherItems.ZaniteRing, 1), 2000);
		addEnchantment(new ItemStack(AetherItems.ZanitePendant, 1), new ItemStack(AetherItems.ZanitePendant, 1), 2000);
		addEnchantment(new ItemStack(AetherItems.LeatherGlove, 1), new ItemStack(AetherItems.LeatherGlove, 1), 300);
		addEnchantment(new ItemStack(AetherItems.IronGlove, 1), new ItemStack(AetherItems.IronGlove, 1), 1200);
		addEnchantment(new ItemStack(AetherItems.GoldGlove, 1), new ItemStack(AetherItems.GoldGlove, 1), 800);
		addEnchantment(new ItemStack(AetherItems.DiamondGlove, 1), new ItemStack(AetherItems.DiamondGlove, 1), 4000);
		addEnchantment(new ItemStack(AetherItems.DartShooter, 1, 0), new ItemStack(AetherItems.DartShooter, 1, 2), 2000);
	}
}
