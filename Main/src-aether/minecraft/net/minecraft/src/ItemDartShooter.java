package net.minecraft.src;

public class ItemDartShooter extends Item {
	public static int sprNormal = ModLoader.addOverride("/gui/items.png", "/aether/items/DartShooter.png");
	public static int sprPoison = ModLoader.addOverride("/gui/items.png", "/aether/items/DartShooterPoison.png");
	public static int sprEnchanted = ModLoader.addOverride("/gui/items.png", "/aether/items/DartShooterEnchanted.png");

	public ItemDartShooter(int i) {
		super(i);
		this.setHasSubtypes(true);
		this.maxStackSize = 1;
	}

	public int getIconFromDamage(int damage) {
		return damage == 0 ? sprNormal : (damage == 1 ? sprPoison : (damage == 2 ? sprEnchanted : sprNormal));
	}

	public String getItemNameIS(ItemStack stack) {
		int i = stack.getItemDamage();
		if(i > 2) {
			i = 2;
		}

		return this.getItemName() + i;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		int consume = this.consumeItem(entityplayer, AetherItems.Dart.shiftedIndex, itemstack.getItemDamage());
		if(consume != -1) {
			world.playSoundAtEntity(entityplayer, "aether.sound.other.dartshooter.shootDart", 2.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if(!world.multiplayerWorld) {
				Object dart = null;
				if(consume == 1) {
					dart = new EntityDartPoison(world, entityplayer);
				}

				if(consume == 2) {
					dart = new EntityDartEnchanted(world, entityplayer);
				}

				if(dart == null) {
					dart = new EntityDartGolden(world, entityplayer);
				}

				world.entityJoinedWorld((Entity)dart);
			}
		}

		return itemstack;
	}

	private int consumeItem(EntityPlayer player, int itemID, int maxDamage) {
		InventoryPlayer inv = player.inventory;

		for(int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null) {
				int damage = stack.getItemDamage();
				if(stack.itemID == itemID && stack.getItemDamage() == maxDamage) {
					--stack.stackSize;
					if(stack.stackSize == 0) {
						stack = null;
					}

					inv.setInventorySlotContents(i, stack);
					return damage;
				}
			}
		}

		return -1;
	}
}
