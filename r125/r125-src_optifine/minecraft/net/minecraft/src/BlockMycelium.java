package net.minecraft.src;

import java.util.Random;

public class BlockMycelium extends Block {
	protected BlockMycelium(int i1) {
		super(i1, Material.grass);
		this.blockIndexInTexture = 77;
		this.setTickRandomly(true);
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return i1 == 1 ? 78 : (i1 == 0 ? 2 : 77);
	}

	public int getBlockTexture(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		if(i5 == 1) {
			return 78;
		} else if(i5 == 0) {
			return 2;
		} else {
			Material material6 = iBlockAccess1.getBlockMaterial(i2, i3 + 1, i4);
			return material6 != Material.snow && material6 != Material.craftedSnow ? 77 : 68;
		}
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(!world1.isRemote) {
			if(world1.getBlockLightValue(i2, i3 + 1, i4) < 4 && Block.lightOpacity[world1.getBlockId(i2, i3 + 1, i4)] > 2) {
				world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
			} else if(world1.getBlockLightValue(i2, i3 + 1, i4) >= 9) {
				for(int i6 = 0; i6 < 4; ++i6) {
					int i7 = i2 + random5.nextInt(3) - 1;
					int i8 = i3 + random5.nextInt(5) - 3;
					int i9 = i4 + random5.nextInt(3) - 1;
					int i10 = world1.getBlockId(i7, i8 + 1, i9);
					if(world1.getBlockId(i7, i8, i9) == Block.dirt.blockID && world1.getBlockLightValue(i7, i8 + 1, i9) >= 4 && Block.lightOpacity[i10] <= 2) {
						world1.setBlockWithNotify(i7, i8, i9, this.blockID);
					}
				}
			}

		}
	}

	public void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		super.randomDisplayTick(world1, i2, i3, i4, random5);
		if(random5.nextInt(10) == 0) {
			world1.spawnParticle("townaura", (double)((float)i2 + random5.nextFloat()), (double)((float)i3 + 1.1F), (double)((float)i4 + random5.nextFloat()), 0.0D, 0.0D, 0.0D);
		}

	}

	public int idDropped(int i1, Random random2, int i3) {
		return Block.dirt.idDropped(0, random2, i3);
	}
}
