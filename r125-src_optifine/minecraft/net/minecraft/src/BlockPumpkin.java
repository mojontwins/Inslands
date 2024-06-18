package net.minecraft.src;

public class BlockPumpkin extends BlockDirectional {
	private boolean blockType;

	protected BlockPumpkin(int i1, int i2, boolean z3) {
		super(i1, Material.pumpkin);
		this.blockIndexInTexture = i2;
		this.setTickRandomly(true);
		this.blockType = z3;
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		if(i1 == 1) {
			return this.blockIndexInTexture;
		} else if(i1 == 0) {
			return this.blockIndexInTexture;
		} else {
			int i3 = this.blockIndexInTexture + 1 + 16;
			if(this.blockType) {
				++i3;
			}

			return i2 == 2 && i1 == 2 ? i3 : (i2 == 3 && i1 == 5 ? i3 : (i2 == 0 && i1 == 3 ? i3 : (i2 == 1 && i1 == 4 ? i3 : this.blockIndexInTexture + 16)));
		}
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? this.blockIndexInTexture : (i1 == 0 ? this.blockIndexInTexture : (i1 == 3 ? this.blockIndexInTexture + 1 + 16 : this.blockIndexInTexture + 16));
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		super.onBlockAdded(world1, i2, i3, i4);
		if(world1.getBlockId(i2, i3 - 1, i4) == Block.blockSnow.blockID && world1.getBlockId(i2, i3 - 2, i4) == Block.blockSnow.blockID) {
			if(!world1.isRemote) {
				world1.setBlock(i2, i3, i4, 0);
				world1.setBlock(i2, i3 - 1, i4, 0);
				world1.setBlock(i2, i3 - 2, i4, 0);
				EntitySnowman entitySnowman9 = new EntitySnowman(world1);
				entitySnowman9.setLocationAndAngles((double)i2 + 0.5D, (double)i3 - 1.95D, (double)i4 + 0.5D, 0.0F, 0.0F);
				world1.spawnEntityInWorld(entitySnowman9);
				world1.notifyBlockChange(i2, i3, i4, 0);
				world1.notifyBlockChange(i2, i3 - 1, i4, 0);
				world1.notifyBlockChange(i2, i3 - 2, i4, 0);
			}

			for(int i10 = 0; i10 < 120; ++i10) {
				world1.spawnParticle("snowshovel", (double)i2 + world1.rand.nextDouble(), (double)(i3 - 2) + world1.rand.nextDouble() * 2.5D, (double)i4 + world1.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		} else if(world1.getBlockId(i2, i3 - 1, i4) == Block.blockSteel.blockID && world1.getBlockId(i2, i3 - 2, i4) == Block.blockSteel.blockID) {
			boolean z5 = world1.getBlockId(i2 - 1, i3 - 1, i4) == Block.blockSteel.blockID && world1.getBlockId(i2 + 1, i3 - 1, i4) == Block.blockSteel.blockID;
			boolean z6 = world1.getBlockId(i2, i3 - 1, i4 - 1) == Block.blockSteel.blockID && world1.getBlockId(i2, i3 - 1, i4 + 1) == Block.blockSteel.blockID;
			if(z5 || z6) {
				world1.setBlock(i2, i3, i4, 0);
				world1.setBlock(i2, i3 - 1, i4, 0);
				world1.setBlock(i2, i3 - 2, i4, 0);
				if(z5) {
					world1.setBlock(i2 - 1, i3 - 1, i4, 0);
					world1.setBlock(i2 + 1, i3 - 1, i4, 0);
				} else {
					world1.setBlock(i2, i3 - 1, i4 - 1, 0);
					world1.setBlock(i2, i3 - 1, i4 + 1, 0);
				}

				EntityIronGolem entityIronGolem7 = new EntityIronGolem(world1);
				entityIronGolem7.func_48115_b(true);
				entityIronGolem7.setLocationAndAngles((double)i2 + 0.5D, (double)i3 - 1.95D, (double)i4 + 0.5D, 0.0F, 0.0F);
				world1.spawnEntityInWorld(entityIronGolem7);

				for(int i8 = 0; i8 < 120; ++i8) {
					world1.spawnParticle("snowballpoof", (double)i2 + world1.rand.nextDouble(), (double)(i3 - 2) + world1.rand.nextDouble() * 3.9D, (double)i4 + world1.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
				}

				world1.notifyBlockChange(i2, i3, i4, 0);
				world1.notifyBlockChange(i2, i3 - 1, i4, 0);
				world1.notifyBlockChange(i2, i3 - 2, i4, 0);
				if(z5) {
					world1.notifyBlockChange(i2 - 1, i3 - 1, i4, 0);
					world1.notifyBlockChange(i2 + 1, i3 - 1, i4, 0);
				} else {
					world1.notifyBlockChange(i2, i3 - 1, i4 - 1, 0);
					world1.notifyBlockChange(i2, i3 - 1, i4 + 1, 0);
				}
			}
		}

	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockId(i2, i3, i4);
		return (i5 == 0 || Block.blocksList[i5].blockMaterial.isGroundCover()) && world1.isBlockNormalCube(i2, i3 - 1, i4);
	}

	public void onBlockPlacedBy(World world1, int i2, int i3, int i4, EntityLiving entityLiving5) {
		int i6 = MathHelper.floor_double((double)(entityLiving5.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		world1.setBlockMetadataWithNotify(i2, i3, i4, i6);
	}
}
