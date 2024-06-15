package net.minecraft.src;

public class BlockDungeon extends Block {
	public static int sprBronze = BlockTrap.sprBronze;
	public static int sprSilver = BlockTrap.sprSilver;
	public static int sprGold = BlockTrap.sprGold;
	public static int sprBronzeLit = ModLoader.addOverride("/terrain.png", "/aether/blocks/LightCarvedStone.png");
	public static int sprSilverLit = ModLoader.addOverride("/terrain.png", "/aether/blocks/LightAngelicStone.png");
	public static int sprGoldLit = ModLoader.addOverride("/terrain.png", "/aether/blocks/LightHellfireStone.png");

	protected BlockDungeon(int i) {
		super(i, Material.rock);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return j == 2 ? (this.isLit() ? sprGoldLit : sprGold) : (j == 1 ? (this.isLit() ? sprSilverLit : sprSilver) : (this.isLit() ? sprBronzeLit : sprBronze));
	}

	private boolean isLit() {
		return this.blockID == AetherBlocks.LightDungeonStone.blockID || this.blockID == AetherBlocks.LockedLightDungeonStone.blockID;
	}

	protected int damageDropped(int i) {
		return i;
	}

	public static int func_21034_c(int i) {
		return ~i & 15;
	}

	public static int func_21035_d(int i) {
		return ~i & 15;
	}
}
