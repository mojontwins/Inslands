package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

public final class ItemHoe extends Item {
	public ItemHoe(int i1, int i2) {
		super(i1);
		this.maxStackSize = 1;
		this.maxDamage = 32 << i2;
	}

	public final boolean onItemUse(ItemStack itemStack1, World world2, int i3, int i4, int i5, int i6) {
		i6 = world2.getBlockId(i3, i4, i5);
		if((world2.getBlockMaterial(i3, i4 + 1, i5).isSolid() || i6 != Block.grass.blockID) && i6 != Block.dirt.blockID) {
			return false;
		} else {
			Block block7 = Block.tilledField;
			double d10001 = (double)((float)i3 + 0.5F);
			double d10002 = (double)((float)i4 + 0.5F);
			double d10003 = (double)((float)i5 + 0.5F);
			String string10004 = block7.stepSound.getStepSound();
			StepSound stepSound8 = block7.stepSound;
			float f10005 = (block7.stepSound.stepSoundVolume + 1.0F) / 2.0F;
			stepSound8 = block7.stepSound;
			world2.playSoundEffect(d10001, d10002, d10003, string10004, f10005, block7.stepSound.stepSoundPitch * 0.8F);
			world2.setBlockWithNotify(i3, i4, i5, block7.blockID);
			itemStack1.damageItem(1);
			if(world2.rand.nextInt(8) == 0 && i6 == Block.grass.blockID) {
				for(int i9 = 0; i9 <= 0; ++i9) {
					float f10 = world2.rand.nextFloat() * 0.7F + 0.15F;
					float f12 = world2.rand.nextFloat() * 0.7F + 0.15F;
					EntityItem entityItem11;
					(entityItem11 = new EntityItem(world2, (double)((float)i3 + f10), (double)((float)i4 + 1.2F), (double)((float)i5 + f12), new ItemStack(Item.seeds))).delayBeforeCanPickup = 10;
					world2.spawnEntityInWorld(entityItem11);
				}
			}

			return true;
		}
	}
}