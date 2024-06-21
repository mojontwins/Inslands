package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneLight extends Block {
	private final boolean powered;

	public BlockRedstoneLight(int i1, boolean z2) {
		super(i1, 211, Material.redstoneLight);
		this.powered = z2;
		if(z2) {
			this.setLightValue(1.0F);
			++this.blockIndexInTexture;
		}

	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(!world1.isRemote) {
			if(this.powered && !world1.isBlockIndirectlyGettingPowered(i2, i3, i4)) {
				world1.scheduleBlockUpdate(i2, i3, i4, this.blockID, 4);
			} else if(!this.powered && world1.isBlockIndirectlyGettingPowered(i2, i3, i4)) {
				world1.setBlockWithNotify(i2, i3, i4, Block.redstoneLampActive.blockID);
			}
		}

	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(!world1.isRemote) {
			if(this.powered && !world1.isBlockIndirectlyGettingPowered(i2, i3, i4)) {
				world1.scheduleBlockUpdate(i2, i3, i4, this.blockID, 4);
			} else if(!this.powered && world1.isBlockIndirectlyGettingPowered(i2, i3, i4)) {
				world1.setBlockWithNotify(i2, i3, i4, Block.redstoneLampActive.blockID);
			}
		}

	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(!world1.isRemote && this.powered && !world1.isBlockIndirectlyGettingPowered(i2, i3, i4)) {
			world1.setBlockWithNotify(i2, i3, i4, Block.redstoneLampIdle.blockID);
		}

	}

	public int idDropped(int i1, Random random2, int i3) {
		return Block.redstoneLampIdle.blockID;
	}
}
