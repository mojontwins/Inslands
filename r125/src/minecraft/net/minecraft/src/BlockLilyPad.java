package net.minecraft.src;

public class BlockLilyPad extends BlockFlower {
	protected BlockLilyPad(int i1, int i2) {
		super(i1, i2);
		float f3 = 0.5F;
		float f4 = 0.015625F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f4, 0.5F + f3);
	}

	public int getRenderType() {
		return 23;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return AxisAlignedBB.getBoundingBoxFromPool((double)i2 + this.minX, (double)i3 + this.minY, (double)i4 + this.minZ, (double)i2 + this.maxX, (double)i3 + this.maxY, (double)i4 + this.maxZ);
	}

	public int getBlockColor() {
		return 2129968;
	}

	public int getRenderColor(int i1) {
		return 2129968;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return super.canPlaceBlockAt(world1, i2, i3, i4);
	}

	public int colorMultiplier(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		return 2129968;
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i1) {
		return i1 == Block.waterStill.blockID;
	}

	public boolean canBlockStay(World world1, int i2, int i3, int i4) {
		return i3 >= 0 && i3 < 256 ? world1.getBlockMaterial(i2, i3 - 1, i4) == Material.water && world1.getBlockMetadata(i2, i3 - 1, i4) == 0 : false;
	}
}
