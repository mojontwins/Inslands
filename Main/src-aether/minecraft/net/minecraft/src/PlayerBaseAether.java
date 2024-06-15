package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlayerBaseAether extends PlayerBase {
	public int maxHealth = 20;
	public InventoryAether inv = new InventoryAether(this.player);

	public PlayerBaseAether(EntityPlayerSP p) {
		super(p);
		this.player.inventorySlots = new ContainerAether(this.player.inventory, this.inv, !this.player.worldObj.multiplayerWorld);
		this.player.craftingInventory = this.player.inventorySlots;
		this.readCustomData();
	}

	public void playerInit() {
	}

	public void increaseMaxHP(int i) {
		if(this.maxHealth <= 40 - i) {
			this.maxHealth += i;
			this.player.health += i;
		}

	}

	public boolean heal(int i) {
		if(this.player.health <= 0) {
			return false;
		} else {
			this.player.health += i;
			if(this.player.health > this.maxHealth) {
				this.player.health = this.maxHealth;
			}

			this.player.heartsLife = this.player.heartsHalvesLife / 2;
			return true;
		}
	}

	public boolean attackEntityFrom(Entity attacker, int damage) {
		if(GuiMainMenu.mmactive) {
			if(attacker != null) {
				attacker.attackEntityFrom(attacker, damage);
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean onLivingUpdate() {
		if(GuiMainMenu.mmactive) {
			this.player.setPosition(this.player.lastTickPosX, this.player.lastTickPosY, this.player.lastTickPosZ);
		}

		if(this.player.ticksExisted % 400 == 0) {
			if(this.inv.slots[0] != null && this.inv.slots[0].itemID == AetherItems.ZanitePendant.shiftedIndex) {
				this.inv.slots[0].damageItem(1, this.player);
				if(this.inv.slots[0].stackSize < 1) {
					this.inv.slots[0] = null;
				}
			}

			if(this.inv.slots[4] != null && this.inv.slots[4].itemID == AetherItems.ZaniteRing.shiftedIndex) {
				this.inv.slots[4].damageItem(1, this.player);
				if(this.inv.slots[4].stackSize < 1) {
					this.inv.slots[4] = null;
				}
			}

			if(this.inv.slots[5] != null && this.inv.slots[5].itemID == AetherItems.ZaniteRing.shiftedIndex) {
				this.inv.slots[5].damageItem(1, this.player);
				if(this.inv.slots[5].stackSize < 1) {
					this.inv.slots[5] = null;
				}
			}

			if(this.inv.slots[0] != null && this.inv.slots[0].itemID == AetherItems.IcePendant.shiftedIndex) {
				this.inv.slots[0].damageItem(1, this.player);
				if(this.inv.slots[0].stackSize < 1) {
					this.inv.slots[0] = null;
				}
			}

			if(this.inv.slots[4] != null && this.inv.slots[4].itemID == AetherItems.IceRing.shiftedIndex) {
				this.inv.slots[4].damageItem(1, this.player);
				if(this.inv.slots[4].stackSize < 1) {
					this.inv.slots[4] = null;
				}
			}

			if(this.inv.slots[5] != null && this.inv.slots[5].itemID == AetherItems.IceRing.shiftedIndex) {
				this.inv.slots[5].damageItem(1, this.player);
				if(this.inv.slots[5].stackSize < 1) {
					this.inv.slots[5] = null;
				}
			}
		}

		if(this.player.worldObj.difficultySetting == 0 && this.player.health >= 20 && this.player.health < this.maxHealth && this.player.ticksExisted % 20 == 0) {
			this.player.heal(1);
		}

		return false;
	}

	public boolean invisible() {
		return !this.player.isSwinging && this.inv.slots[1] != null && this.inv.slots[1].itemID == AetherItems.InvisibilityCloak.shiftedIndex;
	}

	public boolean writeEntityToNBT(NBTTagCompound tag) {
		if(!this.player.worldObj.multiplayerWorld) {
			this.writeCustomData(this.inv);
		}

		return false;
	}

	private void writeCustomData(InventoryAether inv) {
		NBTTagCompound customData = new NBTTagCompound();
		customData.setByte("MaxHealth", (byte)this.maxHealth);
		customData.setTag("Inventory", inv.writeToNBT(new NBTTagList()));

		try {
			File ioexception = new File(((SaveHandler)this.player.worldObj.saveHandler).getSaveDirectory(), "aether.dat");
			CompressedStreamTools.writeGzippedCompoundToOutputStream(customData, new FileOutputStream(ioexception));
		} catch (IOException iOException4) {
			iOException4.printStackTrace();
			throw new RuntimeException("Failed to create player data");
		}
	}

	public boolean readEntityFromNBT(NBTTagCompound tag) {
		if(!this.player.worldObj.multiplayerWorld) {
			this.readCustomData();
		}

		return false;
	}

	private void readCustomData() {
		new NBTTagCompound();

		try {
			File ioexception = new File(((SaveHandler)this.player.worldObj.saveHandler).getSaveDirectory(), "aether.dat");
			NBTTagCompound customData = CompressedStreamTools.func_1138_a(new FileInputStream(ioexception));
			this.maxHealth = customData.getByte("MaxHealth");
			if(this.maxHealth < 20) {
				this.maxHealth = 20;
			}

			NBTTagList nbttaglist = customData.getTagList("Inventory");
			this.inv.readFromNBT(nbttaglist);
		} catch (IOException iOException4) {
			System.out.println("Failed to read player data. Making new");
			this.maxHealth = 20;
		}

	}

	public boolean setEntityDead() {
		if(GuiMainMenu.mmactive) {
			return true;
		} else {
			if(!this.player.worldObj.multiplayerWorld) {
				this.writeCustomData(new InventoryAether(this.player));
			}

			return false;
		}
	}

	public double getDistanceSq(double d, double d1, double d2, double answer) {
		return this.invisible() ? 1000.0D : answer;
	}

	public boolean isInWater(boolean inWater) {
		return inWater && (!this.wearingNeptuneArmor() || this.player.isJumping);
	}

	public float getCurrentPlayerStrVsBlock(Block block, float f) {
		if(this.inv.slots[0] != null && this.inv.slots[0].itemID == AetherItems.ZanitePendant.shiftedIndex) {
			f *= 1.0F + (float)this.inv.slots[0].getItemDamage() / ((float)this.inv.slots[0].getMaxDamage() * 3.0F);
		}

		if(this.inv.slots[4] != null && this.inv.slots[4].itemID == AetherItems.ZaniteRing.shiftedIndex) {
			f *= 1.0F + (float)this.inv.slots[4].getItemDamage() / ((float)this.inv.slots[4].getMaxDamage() * 3.0F);
		}

		if(this.inv.slots[5] != null && this.inv.slots[5].itemID == AetherItems.ZaniteRing.shiftedIndex) {
			f *= 1.0F + (float)this.inv.slots[5].getItemDamage() / ((float)this.inv.slots[5].getMaxDamage() * 3.0F);
		}

		return f;
	}

	private boolean wearingNeptuneArmor() {
		return this.player.inventory.armorInventory[3] != null && this.player.inventory.armorInventory[3].itemID == AetherItems.NeptuneHelmet.shiftedIndex && this.player.inventory.armorInventory[2] != null && this.player.inventory.armorInventory[2].itemID == AetherItems.NeptuneChestplate.shiftedIndex && this.player.inventory.armorInventory[1] != null && this.player.inventory.armorInventory[1].itemID == AetherItems.NeptuneLeggings.shiftedIndex && this.player.inventory.armorInventory[0] != null && this.player.inventory.armorInventory[0].itemID == AetherItems.NeptuneBoots.shiftedIndex && this.inv.slots[6] != null && this.inv.slots[6].itemID == AetherItems.NeptuneGlove.shiftedIndex;
	}
}
