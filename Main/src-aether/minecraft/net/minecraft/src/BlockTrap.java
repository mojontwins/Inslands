package net.minecraft.src;

import java.util.Random;

public class BlockTrap extends BlockBreakable {
	public static int sprBronze = ModLoader.addOverride("/terrain.png", "/aether/blocks/CarvedStone.png");
	public static int sprSilver = ModLoader.addOverride("/terrain.png", "/aether/blocks/AngelicStone.png");
	public static int sprGold = ModLoader.addOverride("/terrain.png", "/aether/blocks/HellfireStone.png");

	public BlockTrap(int blockID) {
		super(blockID, sprBronze, Material.rock, false);
		this.setTickOnLoad(true);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public int getRenderBlockPass() {
		return 1;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return j == 2 ? sprGold : (j == 1 ? sprSilver : sprBronze);
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		if(entity instanceof EntityPlayer) {
			world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "aether.sound.other.dungeontrap.activateTrap", 1.0F, 1.0F);
			int x = MathHelper.floor_double((double)i);
			int y = MathHelper.floor_double((double)j);
			int z = MathHelper.floor_double((double)k);
			switch(world.getBlockMetadata(i, j, k)) {
			case 0:
				EntitySentry entityvalk1 = new EntitySentry(world);
				entityvalk1.setPosition((double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D);
				world.entityJoinedWorld(entityvalk1);
				break;
			case 1:
				EntityValkyrie entityvalk = new EntityValkyrie(world);
				entityvalk.setPosition((double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D);
				world.entityJoinedWorld(entityvalk);
			}

			world.setBlockAndMetadataWithNotify(i, j, k, AetherBlocks.LockedDungeonStone.blockID, world.getBlockMetadata(i, j, k));
		}

	}

	protected int damageDropped(int i) {
		return i;
	}
}
