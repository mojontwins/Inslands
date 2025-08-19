package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFarmland extends Block {
	protected BlockFarmland(int i1) {
		super(60, Material.ground);
		this.blockIndexInTexture = 87;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return new AxisAlignedBB((double)i1, (double)i2, (double)i3, (double)(i1 + 1), (double)(i2 + 1), (double)(i3 + 1));
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return i1 == 1 && i2 > 0 ? this.blockIndexInTexture - 1 : (i1 == 1 ? this.blockIndexInTexture : 2);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(random5.nextInt(5) == 0) {
			int i8 = i4;
			int i7 = i3;
			int i6 = i2;
			World world12 = world1;
			int i9 = i2 - 4;

			int i10;
			int i11;
			boolean z10000;
			label69:
			while(true) {
				if(i9 > i6 + 4) {
					z10000 = false;
					break;
				}

				for(i10 = i7; i10 <= i7 + 1; ++i10) {
					for(i11 = i8 - 4; i11 <= i8 + 4; ++i11) {
						if(world12.getBlockMaterial(i9, i10, i11) == Material.water) {
							z10000 = true;
							break label69;
						}
					}
				}

				++i9;
			}

			if(z10000) {
				world1.setBlockMetadataWithNotify(i2, i3, i4, 7);
				return;
			}

			int i13;
			if((i13 = world1.getBlockMetadata(i2, i3, i4)) > 0) {
				world1.setBlockMetadataWithNotify(i2, i3, i4, i13 - 1);
				return;
			}

			i8 = i4;
			i7 = i3;
			i6 = i2;
			world12 = world1;
			i10 = i2;

			label49:
			while(true) {
				if(i10 > i6) {
					z10000 = false;
					break;
				}

				for(i11 = i8; i11 <= i8; ++i11) {
					if(world12.getBlockId(i10, i7 + 1, i11) == Block.crops.blockID) {
						z10000 = true;
						break label49;
					}
				}

				++i10;
			}

			if(!z10000) {
				world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
			}
		}

	}

	public final void onEntityWalking(World world1, int i2, int i3, int i4) {
		if(world1.rand.nextInt(4) == 0) {
			world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
		}

	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		super.onNeighborBlockChange(world1, i2, i3, i4, i5);
		if(world1.getBlockMaterial(i2, i3 + 1, i4).isSolid()) {
			world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
		}

	}

	public final int idDropped(int i1, Random random2) {
		return Block.dirt.idDropped(0, random2);
	}
}