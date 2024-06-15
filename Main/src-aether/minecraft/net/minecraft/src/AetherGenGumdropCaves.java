package net.minecraft.src;

import java.util.Random;

public class AetherGenGumdropCaves extends WorldGenerator {
	private int field_100003_a;
	private int field_100002_b;

	public AetherGenGumdropCaves(int i, int j) {
		this.field_100003_a = i;
		this.field_100002_b = j;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		float f = random.nextFloat() * 3.141593F;
		double d = (double)((float)(i + 8) + MathHelper.sin(f) * (float)this.field_100002_b / 8.0F);
		double d1 = (double)((float)(i + 8) - MathHelper.sin(f) * (float)this.field_100002_b / 8.0F);
		double d2 = (double)((float)(k + 8) + MathHelper.cos(f) * (float)this.field_100002_b / 8.0F);
		double d3 = (double)((float)(k + 8) - MathHelper.cos(f) * (float)this.field_100002_b / 8.0F);
		double d4 = (double)(j + random.nextInt(3) + 2);
		double d5 = (double)(j + random.nextInt(3) + 2);

		for(int l = 0; l <= this.field_100002_b; ++l) {
			double d6 = d + (d1 - d) * (double)l / (double)this.field_100002_b;
			double d7 = d4 + (d5 - d4) * (double)l / (double)this.field_100002_b;
			double d8 = d2 + (d3 - d2) * (double)l / (double)this.field_100002_b;
			double d9 = random.nextDouble() * (double)this.field_100002_b / 16.0D;
			double d10 = (double)(MathHelper.sin((float)l * 3.141593F / (float)this.field_100002_b) + 1.0F) * d9 + 1.0D;
			double d11 = (double)(MathHelper.sin((float)l * 3.141593F / (float)this.field_100002_b) + 1.0F) * d9 + 1.0D;
			int i1 = (int)(d6 - d10 / 2.0D);
			int j1 = (int)(d7 - d11 / 2.0D);
			int k1 = (int)(d8 - d10 / 2.0D);
			int l1 = (int)(d6 + d10 / 2.0D);
			int i2 = (int)(d7 + d11 / 2.0D);
			int j2 = (int)(d8 + d10 / 2.0D);

			for(int k2 = i1; k2 <= l1; ++k2) {
				double d12 = ((double)k2 + 0.5D - d6) / (d10 / 2.0D);
				if(d12 * d12 < 1.0D) {
					for(int l2 = j1; l2 <= i2; ++l2) {
						double d13 = ((double)l2 + 0.5D - d7) / (d11 / 2.0D);
						if(d12 * d12 + d13 * d13 < 1.0D) {
							for(int i3 = k1; i3 <= j2; ++i3) {
								double d14 = ((double)i3 + 0.5D - d8) / (d10 / 2.0D);
								int bID = world.getBlockId(k2, l2, i3);
								int meta = world.getBlockMetadata(k2, l2, i3);
								if(d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && (bID == AetherBlocks.Holystone.blockID && meta <= 1 || bID == AetherBlocks.Grass.blockID || bID == AetherBlocks.Dirt.blockID)) {
									world.setBlockWithNotify(k2, l2, i3, this.field_100003_a);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}
