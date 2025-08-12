package net.minecraft.world.item;

import net.minecraft.world.level.creative.CreativeTabs;

public class ItemCoal extends Item {
	public ItemCoal(int i1) {
		super(i1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		
		this.displayOnCreativeTab = CreativeTabs.tabMaterials;
	}

	public String getItemNameIS(ItemStack itemStack1) {
		return itemStack1.getItemDamage() == 1 ? "item.charcoal" : "item.coal";
	}
}
