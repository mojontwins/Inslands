package net.minecraft.game.world.block;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockTorch extends Block {
	protected BlockTorch(int var1, int var2) {
		super(50, 80, Material.circuits);
		this.setTickOnLoad(true);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
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

	public final boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return var1.isSolid(var2 - 1, var3, var4) ? true : (var1.isSolid(var2 + 1, var3, var4) ? true : (var1.isSolid(var2, var3, var4 - 1) ? true : (var1.isSolid(var2, var3, var4 + 1) ? true : var1.isSolid(var2, var3 - 1, var4))));
	}

	public final void onBlockPlaced(World var1, int var2, int var3, int var4, int var5) {
		if(var5 == 1) {
			var1.isSolid(var2, var3 - 1, var4);
		}

		if(var5 == 2) {
			var1.isSolid(var2, var3, var4 + 1);
		}

		if(var5 == 3) {
			var1.isSolid(var2, var3, var4 - 1);
		}

		if(var5 == 4) {
			var1.isSolid(var2 + 1, var3, var4);
		}

		if(var5 == 5) {
			var1.isSolid(var2 - 1, var3, var4);
		}

	}

	public final MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
		this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.6F, 0.6F);
		return super.collisionRayTrace(var1, var2, var3, var4, var5, var6);
	}
}
