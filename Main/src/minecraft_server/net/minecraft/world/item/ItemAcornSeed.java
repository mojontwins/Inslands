package net.minecraft.world.item;

import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.entity.player.EntityPlayer;

public class ItemAcornSeed extends Item {

	public ItemAcornSeed(int i1) {
		super(i1);
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer thePlayer, World world, int x, int y, int z, int side) {
		if(side != 1) {
			return false;
		} else {
			int blockID = world.getBlockId(x, y, z);
			if(blockID == Block.tilledField.blockID && world.isAirBlock(x, y + 1, z)) {
				world.setBlockWithNotify(x, y + 1, z, Block.acorn.blockID);
				thePlayer.triggerAchievement(AchievementList.plantAcorn);
				if(!thePlayer.isCreative) --itemStack.stackSize;
				return true;
			} else {
				if (!world.isRemote) {
					world.getWorldAccess(0).showString("You must till the land.");
				}
				
				return false;
			}
		}
	}
}
