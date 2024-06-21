package net.minecraft.src;

public class BlockEnchantmentTable extends BlockContainer {
	protected BlockEnchantmentTable(int i1) {
		super(i1, 166, Material.rock);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		this.setLightOpacity(0);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return this.getBlockTextureFromSide(i1);
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 0 ? this.blockIndexInTexture + 17 : (i1 == 1 ? this.blockIndexInTexture : this.blockIndexInTexture + 16);
	}

	public TileEntity getBlockEntity() {
		return new TileEntityEnchantmentTable();
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		if(world1.isRemote) {
			return true;
		} else {
			entityPlayer5.displayGUIEnchantment(i2, i3, i4);
			return true;
		}
	}
}
