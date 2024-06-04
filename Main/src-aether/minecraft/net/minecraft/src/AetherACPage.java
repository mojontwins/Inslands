package net.minecraft.src;

import java.util.Random;

public class AetherACPage extends ACPage {
	public AetherACPage() {
		super("Aether");
	}

	public int bgGetSprite(Random random, int i, int j) {
		int k = Block.sand.blockIndexInTexture;
		int l = random.nextInt(1 + j) + j / 2;
		if(l <= 37 && j != 35) {
			if(l == 22) {
				k = random.nextInt(2) != 0 ? BlockHolystone.sprMossy : AetherBlocks.GravititeOre.blockIndexInTexture;
			} else if(l == 10) {
				k = AetherBlocks.ZaniteOre.blockIndexInTexture;
			} else if(l == 8) {
				k = AetherBlocks.AmbrosiumOre.blockIndexInTexture;
			} else if(l > 4) {
				k = AetherBlocks.Holystone.blockIndexInTexture;
			} else if(l > 0) {
				k = AetherBlocks.Dirt.blockIndexInTexture;
			}
		} else {
			k = AetherBlocks.Aercloud.blockIndexInTexture;
		}

		return k;
	}
}
