package net.minecraft.src;

import java.util.Random;

public class BlockIncubator extends BlockContainer {
	private Random IncubatorRand;
	private int sideTexture;

	protected BlockIncubator(int blockID) {
		super(blockID, Material.rock);
		this.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/aether/blocks/IncubatorTop.png");
		this.sideTexture = ModLoader.addOverride("/terrain.png", "/aether/blocks/IncubatorSide.png");
		this.IncubatorRand = new Random();
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		this.setDefaultDirection(world, i, j, k);
	}

	private void setDefaultDirection(World world, int i, int j, int k) {
		if(!world.multiplayerWorld) {
			int l = world.getBlockId(i, j, k - 1);
			int i1 = world.getBlockId(i, j, k + 1);
			int j1 = world.getBlockId(i - 1, j, k);
			int k1 = world.getBlockId(i + 1, j, k);
			byte byte0 = 3;
			if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
				byte0 = 3;
			}

			if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
				byte0 = 2;
			}

			if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
				byte0 = 5;
			}

			if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
				byte0 = 4;
			}

			world.setBlockMetadataWithNotify(i, j, k, byte0);
		}
	}

	public int getBlockTextureFromSide(int i) {
		return i == 1 ? this.blockIndexInTexture : (i == 0 ? this.blockIndexInTexture : this.sideTexture);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if(world.multiplayerWorld) {
			return true;
		} else {
			TileEntityIncubator tileentityIncubator = (TileEntityIncubator)world.getBlockTileEntity(i, j, k);
			ModLoader.OpenGUI(entityplayer, new GuiIncubator(entityplayer.inventory, tileentityIncubator));
			return true;
		}
	}

	public static void updateIncubatorBlockState(boolean flag, World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		world.setBlockMetadataWithNotify(i, j, k, l);
		world.setBlockTileEntity(i, j, k, tileentity);
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityIncubator();
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(l == 0) {
			world.setBlockMetadataWithNotify(i, j, k, 2);
		}

		if(l == 1) {
			world.setBlockMetadataWithNotify(i, j, k, 5);
		}

		if(l == 2) {
			world.setBlockMetadataWithNotify(i, j, k, 3);
		}

		if(l == 3) {
			world.setBlockMetadataWithNotify(i, j, k, 4);
		}

	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		TileEntityIncubator tileentityIncubator = (TileEntityIncubator)world.getBlockTileEntity(i, j, k);

		for(int l = 0; l < tileentityIncubator.getSizeInventory(); ++l) {
			ItemStack itemstack = tileentityIncubator.getStackInSlot(l);
			if(itemstack != null) {
				float f = this.IncubatorRand.nextFloat() * 0.8F + 0.1F;
				float f1 = this.IncubatorRand.nextFloat() * 0.8F + 0.1F;
				float f2 = this.IncubatorRand.nextFloat() * 0.8F + 0.1F;

				while(itemstack.stackSize > 0) {
					int i1 = this.IncubatorRand.nextInt(21) + 10;
					if(i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}

					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(world, (double)((float)i + f), (double)((float)j + f1), (double)((float)k + f2), new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (double)((float)this.IncubatorRand.nextGaussian() * f3);
					entityitem.motionY = (double)((float)this.IncubatorRand.nextGaussian() * f3 + 0.2F);
					entityitem.motionZ = (double)((float)this.IncubatorRand.nextGaussian() * f3);
					world.entityJoinedWorld(entityitem);
				}
			}
		}

		super.onBlockRemoval(world, i, j, k);
	}
}
