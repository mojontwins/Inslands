package net.minecraft.world.item;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockLeaves;
import net.minecraft.world.level.tile.BlockTallGrass;

public class ItemShears extends Item {
	public ItemShears(int id) {
		super(id);
		this.setMaxStackSize(1);
		this.setMaxDamage(238);
		
		this.displayOnCreativeTab = CreativeTabs.tabTools;
	}

	public boolean onBlockDestroyed(ItemStack stack, int blockID, int x, int y, int z, EntityLiving entity) {
		Block block = Block.blocksList[blockID];
		
		if(
				blockID == Block.web.blockID || 
				block instanceof BlockLeaves ||
				block instanceof BlockTallGrass || 
				blockID == Block.vine.blockID
		) {
			stack.damageItem(1, entity);
			return true;
		}

		return super.onBlockDestroyed(stack, blockID, x, y, z, entity);
	}

	public boolean canHarvestBlock(Block block) {
		return block.blockID == Block.web.blockID || 
				block instanceof BlockLeaves || 
				block instanceof BlockTallGrass|| 
				block.blockID == Block.vine.blockID;
	}

	public float getStrVsBlock(ItemStack stack, Block block) {
		return block.blockID != Block.web.blockID && 
				block.blockID != Block.leaves.blockID ? (block.blockID == Block.cloth.blockID ? 5.0F : super.getStrVsBlock(stack, block)) : 15.0F;
	}
}
