package net.minecraft.world.item;

import net.minecraft.src.MathHelper;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockBed;

public class ItemBed extends Item {
	public ItemBed(int i1) {
		super(i1);
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(i7 != 1) {
			return false;
		} else {
			++i5;
			BlockBed blockBed8 = (BlockBed)Block.blockBed;
			int i9 = MathHelper.floor_double((double)(entityPlayer2.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			byte b10 = 0;
			byte b11 = 0;
			if(i9 == 0) {
				b11 = 1;
			}

			if(i9 == 1) {
				b10 = -1;
			}

			if(i9 == 2) {
				b11 = -1;
			}

			if(i9 == 3) {
				b10 = 1;
			}

			if(world3.isAirBlock(i4, i5, i6) && world3.isAirBlock(i4 + b10, i5, i6 + b11) && world3.isBlockNormalCube(i4, i5 - 1, i6) && world3.isBlockNormalCube(i4 + b10, i5 - 1, i6 + b11)) {
				world3.setBlockAndMetadataWithNotify(i4, i5, i6, blockBed8.blockID, i9);
				world3.setBlockAndMetadataWithNotify(i4 + b10, i5, i6 + b11, blockBed8.blockID, i9 + 8);
				if(!entityPlayer2.isCreative) --itemStack1.stackSize;
				return true;
			} else {
				return false;
			}
		}
	}
	
	// Softlocked for b1.2
	public boolean softLocked() {
		return true;
	}
}
