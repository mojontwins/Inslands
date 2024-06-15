package net.minecraft.src;

public class BlockHolystone extends Block {
	public static int sprNormal = ModLoader.addOverride("/terrain.png", "/aether/blocks/Holystone.png");
	public static int sprMossy = ModLoader.addOverride("/terrain.png", "/aether/blocks/MossyHolystone.png");

	protected BlockHolystone(int blockID) {
		super(blockID, sprNormal, Material.rock);
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta) {
		entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
		ItemStack stack = new ItemStack(this.blockID, 1, meta <= 1 ? 0 : 2);
		if(mod_Aether.equippedSkyrootPick() && (meta == 0 || meta == 2)) {
			stack.stackSize *= 2;
		}

		SAPI.drop(world, new Loc(x, y, z), stack);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return j > 1 ? sprMossy : sprNormal;
	}
}
