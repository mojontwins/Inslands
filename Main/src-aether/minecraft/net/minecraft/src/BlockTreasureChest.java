package net.minecraft.src;

import java.util.Random;

public class BlockTreasureChest extends BlockContainer {
	private Random random = new Random();
	private int sideTexture;

	protected BlockTreasureChest(int i, int j, int k) {
		super(i, Material.rock);
		this.blockIndexInTexture = j;
		this.sideTexture = k;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(l == 1) {
			return 62;
		} else if(l == 0) {
			return 62;
		} else {
			int i1 = iblockaccess.getBlockId(i, j, k - 1);
			int j1 = iblockaccess.getBlockId(i, j, k + 1);
			int k1 = iblockaccess.getBlockId(i - 1, j, k);
			int l1 = iblockaccess.getBlockId(i + 1, j, k);
			byte byte0 = 3;
			if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[j1]) {
				byte0 = 3;
			}

			if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[i1]) {
				byte0 = 2;
			}

			if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[l1]) {
				byte0 = 5;
			}

			if(Block.opaqueCubeLookup[l1] && !Block.opaqueCubeLookup[k1]) {
				byte0 = 4;
			}

			return l != byte0 ? this.sideTexture : this.blockIndexInTexture;
		}
	}

	public int getBlockTextureFromSide(int i) {
		return i == 1 ? 62 : (i == 0 ? 62 : (i == 3 ? this.blockIndexInTexture : this.sideTexture));
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return false;
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if(world.multiplayerWorld) {
			return true;
		} else {
			int meta = world.getBlockMetadata(i, j, k);
			TileEntityChest chest = (TileEntityChest)world.getBlockTileEntity(i, j, k);
			if(meta % 2 == 1) {
				ModLoader.OpenGUI(entityplayer, new GuiTreasureChest(entityplayer.inventory, chest, meta));
				return true;
			} else {
				ItemStack itemstack = entityplayer.inventory.getCurrentItem();
				if(itemstack != null && itemstack.itemID == AetherItems.Key.shiftedIndex && itemstack.getItemDamage() == 0 && meta == 0) {
					entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
					world.setBlockMetadata(i, j, k, meta + 1);
					world.setBlockTileEntity(i, j, k, chest);
					return true;
				} else if(itemstack != null && itemstack.itemID == AetherItems.Key.shiftedIndex && itemstack.getItemDamage() == 1 && meta == 2) {
					entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
					world.setBlockMetadata(i, j, k, meta + 1);
					world.setBlockTileEntity(i, j, k, chest);
					return true;
				} else if(itemstack != null && itemstack.itemID == AetherItems.Key.shiftedIndex && itemstack.getItemDamage() == 2 && meta == 4) {
					entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
					world.setBlockMetadata(i, j, k, meta + 1);
					world.setBlockTileEntity(i, j, k, chest);
					return true;
				} else {
					return false;
				}
			}
		}
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}
