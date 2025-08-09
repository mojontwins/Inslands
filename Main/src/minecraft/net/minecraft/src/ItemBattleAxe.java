package net.minecraft.src;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.world.entity.Entity;

public class ItemBattleAxe extends ItemAxe {

	public ItemBattleAxe(int i1, EnumToolMaterial enumToolMaterial2, boolean silkTouch) {
		super(i1, 6, enumToolMaterial2, silkTouch);
		this.displayOnCreativeTab = CreativeTabs.tabCombat;
	}
	
	/*
	 * Add knock back when hitting an entity.
	 */
	public float getExtraKnockbackVsEntity(Entity entity) {
		return 0.7F;
	}

	/*
	 * Default swinging speed = 6, less is faster. 
	 */
	public int getSwingSpeed() {
		return 8;
	}
}
