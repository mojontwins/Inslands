package net.minecraft.src;

import java.util.Random;

public class BlockStep extends Block {
	public static final String[] blockStepTypes = new String[]{"stone", "sand", "wood", "cobble"};
	private boolean blockType;

	public BlockStep(int id, boolean blockType) {
		super(id, 6, Material.rock);
		this.blockType = blockType;
		if(!blockType) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		this.setLightOpacity(255);
	}

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		switch(metadata) {
			case 1: return side == 0 ? 208 : (side == 1 ? 176 : 192);
			case 2: return 4;
			case 3: return 16;
			case 4: return 6;			
			default: return side <= 1 ? 6 : 5;
		}
	}

	public int getBlockTextureFromSide(int i1) {
		return this.getBlockTextureFromSideAndMetadata(i1, 0);
	}

	public boolean isOpaqueCube() {
		return this.blockType;
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(this != Block.stairSingle) {
			super.onBlockAdded(world1, i2, i3, i4);
		}

		int i5 = world1.getBlockId(i2, i3 - 1, i4);
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		int i7 = world1.getBlockMetadata(i2, i3 - 1, i4);
		if(i6 == i7) {
			if(i5 == stairSingle.blockID) {
				world1.setBlockWithNotify(i2, i3, i4, 0);
				world1.setBlockAndMetadataWithNotify(i2, i3 - 1, i4, Block.stairDouble.blockID, i6);
			}

		}
	}

	public int idDropped(int metadata, Random rand) {
		return Block.stairSingle.blockID;
	}

	protected int damageDropped(int i1) {
		return i1;
	}

	public int quantityDropped(Random rand) {
		return this.blockType ? 2 : 1;
	}

	public boolean renderAsNormalBlock() {
		return this.blockType;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(this != Block.stairSingle) {
			super.shouldSideBeRendered(blockAccess, x, y, z, side);
		}

		return side == 1 ? true : (!super.shouldSideBeRendered(blockAccess, x, y, z, side) ? false : (side == 0 ? true : blockAccess.getBlockId(x, y, z) != this.blockID));
	}
	
	public boolean seeThrough() {
		return !blockType; 
	}
	
	public Material getBlockMaterialBasedOnmetaData(int meta) {
		switch(meta) {
			case 2: 
				return Material.wood;
			default:
				return Material.rock;
		}		
	}	
}
