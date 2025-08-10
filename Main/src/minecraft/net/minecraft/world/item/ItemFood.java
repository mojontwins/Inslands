package net.minecraft.world.item;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.src.World;
import net.minecraft.world.entity.EnumAction;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.status.StatusEffect;

public class ItemFood extends Item {
	private int healAmount;
	private boolean isWolfsFavoriteMeat;
	private boolean isCatsFavoriteMeat;
	private boolean alwaysEdible;

	public ItemFood(int i1, int i2, boolean z3, boolean z4) {
		super(i1);
		this.healAmount = i2;
		this.isWolfsFavoriteMeat = z3;
		this.isCatsFavoriteMeat = z4;
		this.maxStackSize = 1;
		
		this.displayOnCreativeTab = CreativeTabs.tabFood;
	}

	@Override
	public ItemStack onFoodEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!entityPlayer.isCreative) --itemStack.stackSize;
		
		int heal = this.healAmount;
		if(world.getWorldInfo().isBloodMoon() && rand.nextBoolean()) heal >>= 1;
		entityPlayer.heal(heal);
		
		world.playSoundAtEntity(entityPlayer, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		
		if(this.status != null) entityPlayer.addStatusEffect(new StatusEffect(this.status.id, this.statusTime, this.statusAmplifier));

		return itemStack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack1) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack1) {
		return EnumAction.eat;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		/*
		if(!entityPlayer.isCreative) --itemStack.stackSize;
		int heal = this.healAmount;
		if(world.getWorldInfo().isBloodMoon() && rand.nextBoolean()) heal >>= 1;
		entityPlayer.heal(heal);
				
		if(this.status != null) entityPlayer.addStatusEffect(new StatusEffect(this.status.id, this.statusTime, this.statusAmplifier));
		*/

		if(entityPlayer.canEat(this.alwaysEdible)) {
			entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		}
		
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
	
	public ItemFood setAlwaysEdible() {
		this.alwaysEdible = true;
		return this;
	}
}
