package net.minecraft.src;

import com.mojontwins.minecraft.entity.status.StatusEffect;

public class ItemFood extends Item {
	private int healAmount;
	private boolean isWolfsFavoriteMeat;

	public ItemFood(int i1, int i2, boolean z3) {
		super(i1);
		this.healAmount = i2;
		this.isWolfsFavoriteMeat = z3;
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		if(!entityPlayer3.isCreative) --itemStack1.stackSize;
		entityPlayer3.heal(this.healAmount);
				
		if(this.status != null) entityPlayer3.addStatusEffect(new StatusEffect(this.status.id, this.statusTime, this.statusAmplifier));
		
		return itemStack1;
	}

	public int getHealAmount() {
		return this.healAmount;
	}

	public boolean getIsWolfsFavoriteMeat() {
		return this.isWolfsFavoriteMeat;
	}
}
