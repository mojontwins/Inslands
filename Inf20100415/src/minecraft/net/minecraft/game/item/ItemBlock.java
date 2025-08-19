package net.minecraft.game.item;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

public final class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int i1) {
		super(i1);
		this.blockID = i1 + 256;
		this.setIconIndex(Block.blocksList[i1 + 256].getBlockTextureFromSide(2));
	}

	public final boolean onItemUse(ItemStack itemStack1, World world2, int i3, int i4, int i5, int i6) {
		if(i6 == 0) {
			--i4;
		}

		if(i6 == 1) {
			++i4;
		}

		if(i6 == 2) {
			--i5;
		}

		if(i6 == 3) {
			++i5;
		}

		if(i6 == 4) {
			--i3;
		}

		if(i6 == 5) {
			++i3;
		}

		if(itemStack1.stackSize == 0) {
			return false;
		} else {
			int i7 = world2.getBlockId(i3, i4, i5);
			Block block10 = Block.blocksList[i7];
			AxisAlignedBB axisAlignedBB8 = Block.blocksList[this.blockID].getCollisionBoundingBoxFromPool(i3, i4, i5);
			if(this.blockID > 0 && block10 == null || block10 == Block.waterMoving || block10 == Block.waterStill || block10 == Block.lavaMoving || block10 == Block.lavaStill || block10 == Block.fire) {
				block10 = Block.blocksList[this.blockID];
				if((axisAlignedBB8 == null || world2.checkIfAABBIsClear1(axisAlignedBB8)) && block10.canPlaceBlockAt(world2, i3, i4, i5) && world2.setBlockWithNotify(i3, i4, i5, this.blockID)) {
					Block.blocksList[this.blockID].onBlockPlaced(world2, i3, i4, i5, i6);
					double d10001 = (double)((float)i3 + 0.5F);
					double d10002 = (double)((float)i4 + 0.5F);
					double d10003 = (double)((float)i5 + 0.5F);
					String string10004 = block10.stepSound.getStepSound();
					StepSound stepSound9 = block10.stepSound;
					float f10005 = (block10.stepSound.stepSoundVolume + 1.0F) / 2.0F;
					stepSound9 = block10.stepSound;
					world2.playSoundEffect(d10001, d10002, d10003, string10004, f10005, block10.stepSound.stepSoundPitch * 0.8F);
					--itemStack1.stackSize;
				}
			}

			return true;
		}
	}
}