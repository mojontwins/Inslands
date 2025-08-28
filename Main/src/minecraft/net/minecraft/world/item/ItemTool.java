package net.minecraft.world.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.tile.Block;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	protected float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;
	protected EnumToolMaterial toolMaterial;

	protected ItemTool(int id, int attackDmg, EnumToolMaterial toolMaterial, Block[] blocksEffectiveAgainst, boolean silkTouch) {
		super(id);
		this.toolMaterial = toolMaterial;
		this.blocksEffectiveAgainst = blocksEffectiveAgainst;
		this.maxStackSize = 1;
		this.setMaxDamage(toolMaterial.getMaxUses());
		this.efficiencyOnProperMaterial = toolMaterial.getEfficiencyOnProperMaterial();
		this.damageVsEntity = attackDmg + toolMaterial.getDamageVsEntity();
		this.silkTouch = silkTouch;
		
		this.displayOnCreativeTab = CreativeTabs.tabTools;
	}

	public float getStrVsBlock(ItemStack itemStack1, Block block2) {
		for(int i3 = 0; i3 < this.blocksEffectiveAgainst.length; ++i3) {
			if(this.blocksEffectiveAgainst[i3] == block2) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public boolean hitEntity(ItemStack itemStack1, EntityLiving entityLiving2, EntityLiving entityLiving3) {
		itemStack1.damageItem(2, entityLiving3);
		return true;
	}

	public boolean onBlockDestroyed(ItemStack itemStack1, int i2, int i3, int i4, int i5, EntityLiving entityLiving6) {
		itemStack1.damageItem(1, entityLiving6);
		return true;
	}

	public int getDamageVsEntity(Entity entity1) {
		return this.damageVsEntity;
	}

	public boolean isFull3D() {
		return true;
	}
}
