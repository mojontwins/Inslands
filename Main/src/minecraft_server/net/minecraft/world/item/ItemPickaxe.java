package net.minecraft.world.item;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockObsidian;

public class ItemPickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{
		Block.cobblestone, 
		Block.stairDouble, 
		Block.stairSingle, 
		Block.stone, 
		Block.sandStone, 
		Block.cobblestoneMossy, 
		Block.oreIron, 
		Block.blockSteel, 
		Block.oreCoal, 
		Block.blockGold, 
		Block.oreGold, 
		Block.oreDiamond, 
		Block.blockDiamond, 
		Block.ice, 
		Block.bloodStone, 
		Block.oreLapis, 
		Block.blockLapis,
		Block.stairCompactCobblestone,
		Block.oreRedstone,
		Block.oreRedstoneGlowing,
		Block.oreGlow,
		Block.oreGlowGlowing,
		Block.brick,
		Block.button,
		Block.doorSteel,
		Block.pressurePlateStone,
		Block.ironWall, 
		Block.streetLanternFence, 
		Block.barbedWire,
		Block.mobSpawner,
		Block.stoneOvenIdle,
		Block.stoneOvenActive,
		Block.oreRuby,
		Block.oreEmerald,
		Block.stairsBrick,
		Block.stairsSandStone,
		Block.stairsStoneBrickSmooth
	};

	protected ItemPickaxe(int i1, EnumToolMaterial enumToolMaterial2, boolean silkTouch) {
		super(i1, 2, enumToolMaterial2, blocksEffectiveAgainst, silkTouch);
	}
	
	protected ItemPickaxe(int i1, int damageModifier, EnumToolMaterial enumToolMaterial2, boolean silkTouch) {
		super(i1, damageModifier, enumToolMaterial2, blocksEffectiveAgainst, silkTouch);
	}

	public boolean canHarvestBlock(Block block) {
		return (block instanceof BlockObsidian) ? 
			this.toolMaterial.getHarvestLevel() == 3
		: 
			(block != Block.blockDiamond && block != Block.oreDiamond && block != Block.oreRuby && block != Block.oreEmerald ? 
				(block != Block.blockGold && block != Block.oreGold ? 
					(block != Block.blockSteel && block != Block.oreIron && block != Block.oreGlow && block != Block.oreGlowGlowing ? 
						(block != Block.blockLapis && block != Block.oreLapis ? 
							(block != Block.oreRedstone && block != Block.oreRedstoneGlowing ? 
								(block.blockMaterial == Material.rock ? 
									true 
								: 
									block.blockMaterial == Material.iron
								) 
							: 
								this.toolMaterial.getHarvestLevel() >= 2
							) 
						: 
							this.toolMaterial.getHarvestLevel() >= 1
						) 
					: 
						this.toolMaterial.getHarvestLevel() >= 1
					) 
				: 
					this.toolMaterial.getHarvestLevel() >= 2
				) 
			: 
				this.toolMaterial.getHarvestLevel() >= 2
			);
	}
}
