package net.minecraft.world.item;

import java.util.List;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.tile.Block;

public class ItemBlock extends Item {
	protected int blockID;

	public ItemBlock(int i1) {
		super(i1);
		this.blockID = i1 + 256;
		this.setIconIndex(Block.blocksList[i1 + 256].getBlockTextureFromSide(2));
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xWithinFace, float yWithinFace, float zWithinFace) {
		if(world.getBlockID(x, y, z) == Block.snow.blockID) {
			side = 0;
		} else {
			if(side == 0) {
				--y;
			}

			if(side == 1) {
				++y;
			}

			if(side == 2) {
				--z;
			}

			if(side == 3) {
				++z;
			}

			if(side == 4) {
				--x;
			}

			if(side == 5) {
				++x;
			}
		}

		if(stack.stackSize == 0) {
			return false;
		} else if(y == 127 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
			return false;
		} else if(world.canBlockBePlacedAt(this.blockID, x, y, z, false, side)) {
			Block block8 = Block.blocksList[this.blockID];
			if(world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, this.getPlacedBlockMetadata(stack.getItemDamage()))) {
				Block.blocksList[this.blockID].onBlockPlaced(world, x, y, z, side, xWithinFace, yWithinFace, zWithinFace);
				Block.blocksList[this.blockID].onBlockPlacedBy(world, x, y, z, player);
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block8.stepSound.getStepSound(), (block8.stepSound.getVolume() + 1.0F) / 2.0F, block8.stepSound.getPitch() * 0.8F);
				if(!player.isCreative) --stack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	public String getItemNameIS(ItemStack itemStack1) {
		return Block.blocksList[this.blockID].getBlockName();
	}

	public String getItemName() {
		return Block.blocksList[this.blockID].getBlockName();
	}
	
	public CreativeTabs getCreativeTab() {
		return Block.blocksList[this.blockID].getCreativeTab();
	}

	public void getSubItems(int var1, CreativeTabs var2, List<ItemStack> var3) {
		Block.blocksList[this.blockID].getSubBlocks(var1, var2, var3);
	}
}
