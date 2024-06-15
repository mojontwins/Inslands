package net.minecraft.src;

public class ItemGummieSwet extends Item {
	private int healAmount;
	private boolean damZero;
	private boolean damOne;

	public ItemGummieSwet(int i) {
		super(i);
		this.maxStackSize = 64;
		this.damZero = false;
		this.damOne = false;
		this.healAmount = 20;
		this.setHasSubtypes(true);
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		--itemstack.stackSize;
		entityplayer.heal(this.healAmount);
		return itemstack;
	}

	public int getHealAmount() {
		return this.healAmount;
	}

	public int getColorFromDamage(int damage) {
		return damage == 1 ? 16777087 : 8765927;
	}

	public String getItemNameIS(ItemStack stack) {
		int i = stack.getItemDamage();
		if(i > 1) {
			i = 1;
		}

		return this.getItemName() + i;
	}
}
