package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;
import net.minecraft.game.world.material.Material;

public final class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int i1, boolean z2) {
		super(i1, Material.rock);
		this.isActive = z2;
		this.blockIndexInTexture = 45;
	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		super.onBlockAdded(world1, i2, i3, i4);
		setDefaultDirection(world1, i2, i3, i4);
	}

	private static void setDefaultDirection(World world0, int i1, int i2, int i3) {
		int i4 = world0.getBlockId(i1, i2, i3 - 1);
		int i5 = world0.getBlockId(i1, i2, i3 + 1);
		int i6 = world0.getBlockId(i1 - 1, i2, i3);
		int i7 = world0.getBlockId(i1 + 1, i2, i3);
		byte b8 = 3;
		if(Block.opaqueCubeLookup[i4] && !Block.opaqueCubeLookup[i5]) {
			b8 = 3;
		}

		if(Block.opaqueCubeLookup[i5] && !Block.opaqueCubeLookup[i4]) {
			b8 = 2;
		}

		if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i7]) {
			b8 = 5;
		}

		if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i6]) {
			b8 = 4;
		}

		world0.setBlockMetadataWithNotify(i1, i2, i3, b8);
	}

	public final int getBlockTexture(World world1, int i2, int i3, int i4, int i5) {
		if(i5 == 1) {
			return Block.stone.blockIndexInTexture;
		} else if(i5 == 0) {
			return Block.stone.blockIndexInTexture;
		} else {
			int i6;
			if((i6 = world1.getBlockMetadata(i2, i3, i4)) == 0) {
				setDefaultDirection(world1, i2, i3, i4);
				i6 = world1.getBlockMetadata(i2, i3, i4);
			}

			return i5 != i6 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
		}
	}

	public final void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		if(this.isActive) {
			int i6 = world1.getBlockMetadata(i2, i3, i4);
			float f7 = (float)i2 + 0.5F;
			float f8 = (float)i3 + random5.nextFloat() * 6.0F / 16.0F;
			float f9 = (float)i4 + 0.5F;
			float f10 = random5.nextFloat() * 0.6F - 0.3F;
			if(i6 == 4) {
				world1.spawnParticle("smoke", (double)(f7 - 0.52F), (double)f8, (double)(f9 + f10), 0.0D, 0.0D, 0.0D);
				world1.spawnParticle("flame", (double)(f7 - 0.52F), (double)f8, (double)(f9 + f10), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 5) {
				world1.spawnParticle("smoke", (double)(f7 + 0.52F), (double)f8, (double)(f9 + f10), 0.0D, 0.0D, 0.0D);
				world1.spawnParticle("flame", (double)(f7 + 0.52F), (double)f8, (double)(f9 + f10), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 2) {
				world1.spawnParticle("smoke", (double)(f7 + f10), (double)f8, (double)(f9 - 0.52F), 0.0D, 0.0D, 0.0D);
				world1.spawnParticle("flame", (double)(f7 + f10), (double)f8, (double)(f9 - 0.52F), 0.0D, 0.0D, 0.0D);
			} else {
				if(i6 == 3) {
					world1.spawnParticle("smoke", (double)(f7 + f10), (double)f8, (double)(f9 + 0.52F), 0.0D, 0.0D, 0.0D);
					world1.spawnParticle("flame", (double)(f7 + f10), (double)f8, (double)(f9 + 0.52F), 0.0D, 0.0D, 0.0D);
				}

			}
		}
	}

	public final int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? Block.stone.blockID : (i1 == 0 ? Block.stone.blockID : (i1 == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public final boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		TileEntityFurnace tileEntityFurnace6 = (TileEntityFurnace)world1.getBlockTileEntity(i2, i3, i4);
		entityPlayer5.displayFurnaceGUI(tileEntityFurnace6);
		return true;
	}

	protected final TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}
}