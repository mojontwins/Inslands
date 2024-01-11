package net.minecraft.src;

import com.mojontwins.minecraft.entity.status.StatusEffect;

public class ItemFood extends Item {
	private int healAmount;
	private boolean isWolfsFavoriteMeat;
	private boolean isCatsFavoriteMeat;

	public ItemFood(int i1, int i2, boolean z3, boolean z4) {
		super(i1);
		this.healAmount = i2;
		this.isWolfsFavoriteMeat = z3;
		this.isCatsFavoriteMeat = z4;
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!entityPlayer.isCreative) --itemStack.stackSize;
		int heal = this.healAmount;
		if(world.getWorldInfo().isBloodMoon() && rand.nextBoolean()) heal >>= 1;
		entityPlayer.heal(heal);
				
		if(this.status != null) entityPlayer.addStatusEffect(new StatusEffect(this.status.id, this.statusTime, this.statusAmplifier));
		
		return itemStack;
	}

	public int getHealAmount() {
		return this.healAmount;
	}

	public boolean getIsWolfsFavoriteMeat() {
		return this.isWolfsFavoriteMeat;
	}
	
	public boolean getIsCatsFavoriteMeat() {
		return this.isCatsFavoriteMeat;
	}
}
