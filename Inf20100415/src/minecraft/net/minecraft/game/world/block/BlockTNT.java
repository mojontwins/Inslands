package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.entity.misc.EntityTNT;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockTNT extends Block {
	public BlockTNT(int i1, int i2) {
		super(46, 8, Material.tnt);
	}

	public final int getBlockTextureFromSide(int i1) {
		return i1 == 0 ? this.blockIndexInTexture + 2 : (i1 == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public final int quantityDropped(Random random1) {
		return 0;
	}

	public final void onBlockDestroyedByExplosion(World world1, int i2, int i3, int i4) {
		EntityTNT entityTNT5;
		(entityTNT5 = new EntityTNT(world1, (float)i2 + 0.5F, (float)i3 + 0.5F, (float)i4 + 0.5F)).fuse = world1.rand.nextInt(entityTNT5.fuse / 4) + entityTNT5.fuse / 8;
		world1.spawnEntityInWorld(entityTNT5);
	}

	public final void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4, int i5) {
		EntityTNT entityTNT6 = new EntityTNT(world1, (float)i2 + 0.5F, (float)i3 + 0.5F, (float)i4 + 0.5F);
		world1.spawnEntityInWorld(entityTNT6);
		world1.playSoundAtEntity(entityTNT6, "random.fuse", 1.0F, 1.0F);
	}
}