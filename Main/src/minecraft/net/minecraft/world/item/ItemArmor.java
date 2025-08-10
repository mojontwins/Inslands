package net.minecraft.world.item;

import com.mojang.minecraft.creative.CreativeTabs;

public class ItemArmor extends Item {
	private static final int[] damageReduceAmountArray = new int[]{3, 8, 6, 3};
	private static final int[] maxDamageArray = new int[]{11, 16, 15, 13};
	public final int armorLevel;
	public final int armorType;
	public final int damageReduceAmount;
	public final int renderIndex;

	public static final int PIRATE = 5;
	public static final int RAGS = 6;

	public ItemArmor(int i1, int i2, int i3, int i4) {
		super(i1);
		this.armorLevel = i2;
		this.armorType = i4;
		this.renderIndex = i3;
		this.damageReduceAmount = damageReduceAmountArray[i4];
		this.setMaxDamage(maxDamageArray[i4] * 3 << i2);
		this.maxStackSize = 1;
		
		this.displayOnCreativeTab = CreativeTabs.tabCombat;
	}
	
	/*
	 * tier: for armored mobs,  0: leather, 1: chain, 2: gold, 3: iron, 4: diamond
	 * type = 0: helmet, 1: plate, 2:legs, 3:boots
	 */
	public static ItemStack getArmorPieceForTier(int tier, int type) {
		return new ItemStack (Item.armorPieceForTier[tier][type]);
	}
}
