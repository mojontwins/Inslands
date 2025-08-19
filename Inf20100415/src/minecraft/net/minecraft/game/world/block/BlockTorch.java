package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockTorch extends Block {
	protected BlockTorch(int i1, int i2) {
		super(50, 80, Material.circuits);
		this.setTickOnLoad(true);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getRenderType() {
		return 2;
	}

	public final boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return world1.isSolid(i2 - 1, i3, i4) ? true : (world1.isSolid(i2 + 1, i3, i4) ? true : (world1.isSolid(i2, i3, i4 - 1) ? true : (world1.isSolid(i2, i3, i4 + 1) ? true : world1.isSolid(i2, i3 - 1, i4))));
	}

	public final void onBlockPlaced(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		if(i5 == 1 && world1.isSolid(i2, i3 - 1, i4)) {
			i6 = 5;
		}

		if(i5 == 2 && world1.isSolid(i2, i3, i4 + 1)) {
			i6 = 4;
		}

		if(i5 == 3 && world1.isSolid(i2, i3, i4 - 1)) {
			i6 = 3;
		}

		if(i5 == 4 && world1.isSolid(i2 + 1, i3, i4)) {
			i6 = 2;
		}

		if(i5 == 5 && world1.isSolid(i2 - 1, i3, i4)) {
			i6 = 1;
		}

		world1.setBlockMetadataWithNotify(i2, i3, i4, i6);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		super.updateTick(world1, i2, i3, i4, random5);
		if(world1.getBlockMetadata(i2, i3, i4) == 0) {
			this.onBlockAdded(world1, i2, i3, i4);
		}

	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(world1.isSolid(i2 - 1, i3, i4)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 1);
		} else if(world1.isSolid(i2 + 1, i3, i4)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 2);
		} else if(world1.isSolid(i2, i3, i4 - 1)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 3);
		} else if(world1.isSolid(i2, i3, i4 + 1)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 4);
		} else if(world1.isSolid(i2, i3 - 1, i4)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 5);
		}

		this.dropTorchIfCantStay(world1, i2, i3, i4);
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(this.dropTorchIfCantStay(world1, i2, i3, i4)) {
			i5 = world1.getBlockMetadata(i2, i3, i4);
			boolean z6 = false;
			if(!world1.isSolid(i2 - 1, i3, i4) && i5 == 1) {
				z6 = true;
			}

			if(!world1.isSolid(i2 + 1, i3, i4) && i5 == 2) {
				z6 = true;
			}

			if(!world1.isSolid(i2, i3, i4 - 1) && i5 == 3) {
				z6 = true;
			}

			if(!world1.isSolid(i2, i3, i4 + 1) && i5 == 4) {
				z6 = true;
			}

			if(!world1.isSolid(i2, i3 - 1, i4) && i5 == 5) {
				z6 = true;
			}

			if(z6) {
				this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
				world1.setBlockWithNotify(i2, i3, i4, 0);
			}
		}

	}

	private boolean dropTorchIfCantStay(World world1, int i2, int i3, int i4) {
		if(!this.canPlaceBlockAt(world1, i2, i3, i4)) {
			this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
			world1.setBlockWithNotify(i2, i3, i4, 0);
			return false;
		} else {
			return true;
		}
	}

	public final MovingObjectPosition collisionRayTrace(World world1, int i2, int i3, int i4, Vec3D vec3D5, Vec3D vec3D6) {
		int i7;
		if((i7 = world1.getBlockMetadata(i2, i3, i4)) == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.35F, 0.3F, 0.8F, 0.65F);
		} else if(i7 == 2) {
			this.setBlockBounds(0.7F, 0.2F, 0.35F, 1.0F, 0.8F, 0.65F);
		} else if(i7 == 3) {
			this.setBlockBounds(0.35F, 0.2F, 0.0F, 0.65F, 0.8F, 0.3F);
		} else if(i7 == 4) {
			this.setBlockBounds(0.35F, 0.2F, 0.7F, 0.65F, 0.8F, 1.0F);
		} else {
			this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.6F, 0.6F);
		}

		return super.collisionRayTrace(world1, i2, i3, i4, vec3D5, vec3D6);
	}

	public final void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		int i9 = world1.getBlockMetadata(i2, i3, i4);
		float f6 = (float)i2 + 0.5F;
		float f7 = (float)i3 + 0.7F;
		float f8 = (float)i4 + 0.5F;
		if(i9 == 1) {
			world1.spawnParticle("smoke", (double)(f6 - 0.27F), (double)(f7 + 0.22F), (double)f8, 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", (double)(f6 - 0.27F), (double)(f7 + 0.22F), (double)f8, 0.0D, 0.0D, 0.0D);
		} else if(i9 == 2) {
			world1.spawnParticle("smoke", (double)(f6 + 0.27F), (double)(f7 + 0.22F), (double)f8, 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", (double)(f6 + 0.27F), (double)(f7 + 0.22F), (double)f8, 0.0D, 0.0D, 0.0D);
		} else if(i9 == 3) {
			world1.spawnParticle("smoke", (double)f6, (double)(f7 + 0.22F), (double)(f8 - 0.27F), 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", (double)f6, (double)(f7 + 0.22F), (double)(f8 - 0.27F), 0.0D, 0.0D, 0.0D);
		} else if(i9 == 4) {
			world1.spawnParticle("smoke", (double)f6, (double)(f7 + 0.22F), (double)(f8 + 0.27F), 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", (double)f6, (double)(f7 + 0.22F), (double)(f8 + 0.27F), 0.0D, 0.0D, 0.0D);
		} else {
			world1.spawnParticle("smoke", (double)f6, (double)f7, (double)f8, 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", (double)f6, (double)f7, (double)f8, 0.0D, 0.0D, 0.0D);
		}
	}
}