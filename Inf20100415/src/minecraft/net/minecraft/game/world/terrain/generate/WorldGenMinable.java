package net.minecraft.game.world.terrain.generate;

import java.util.Random;

import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

import util.MathHelper;

public final class WorldGenMinable extends WorldGenerator {
	private int minableBlockId;

	public WorldGenMinable(int i1) {
		this.minableBlockId = i1;
	}

	public final boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		float f6 = random2.nextFloat() * (float)Math.PI;
		double d7 = (double)((float)(i3 + 8) + MathHelper.sin(f6) * 2.0F);
		double d9 = (double)((float)(i3 + 8) - MathHelper.sin(f6) * 2.0F);
		double d11 = (double)((float)(i5 + 8) + MathHelper.cos(f6) * 2.0F);
		double d13 = (double)((float)(i5 + 8) - MathHelper.cos(f6) * 2.0F);
		double d15 = (double)(i4 + random2.nextInt(3) + 2);
		double d17 = (double)(i4 + random2.nextInt(3) + 2);

		for(i3 = 0; i3 <= 16; ++i3) {
			double d20 = d7 + (d9 - d7) * (double)i3 / 16.0D;
			double d22 = d15 + (d17 - d15) * (double)i3 / 16.0D;
			double d24 = d11 + (d13 - d11) * (double)i3 / 16.0D;
			double d26 = random2.nextDouble();
			double d28 = (double)(MathHelper.sin((float)i3 / 16.0F * (float)Math.PI) + 1.0F) * d26 + 1.0D;
			double d30 = (double)(MathHelper.sin((float)i3 / 16.0F * (float)Math.PI) + 1.0F) * d26 + 1.0D;

			for(i4 = (int)(d20 - d28 / 2.0D); i4 <= (int)(d20 + d28 / 2.0D); ++i4) {
				for(i5 = (int)(d22 - d30 / 2.0D); i5 <= (int)(d22 + d30 / 2.0D); ++i5) {
					for(int i41 = (int)(d24 - d28 / 2.0D); i41 <= (int)(d24 + d28 / 2.0D); ++i41) {
						double d35 = ((double)i4 + 0.5D - d20) / (d28 / 2.0D);
						double d37 = ((double)i5 + 0.5D - d22) / (d30 / 2.0D);
						double d39 = ((double)i41 + 0.5D - d24) / (d28 / 2.0D);
						if(d35 * d35 + d37 * d37 + d39 * d39 < 1.0D && world1.getBlockId(i4, i5, i41) == Block.stone.blockID) {
							world1.setTileNoUpdate(i4, i5, i41, this.minableBlockId);
						}
					}
				}
			}
		}

		return true;
	}
}