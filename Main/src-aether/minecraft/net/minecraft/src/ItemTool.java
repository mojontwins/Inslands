package net.minecraft.src;

import java.util.Iterator;

public class ItemTool extends Tool {
	public EnumToolMaterial toolMaterial;

	protected ItemTool(int itemID, int damage, EnumToolMaterial material, Block[] blocks) {
		super(false, (ToolBase)null, itemID, material.getMaxUses(), (float)(damage + material.getDamageVsEntity()), getToolPower(material), material.getEfficiencyOnProperMaterial());
		this.toolMaterial = material;
		this.toolBase = this.getToolBase();
		Block[] block8 = blocks;
		int i7 = blocks.length;

		for(int i6 = 0; i6 < i7; ++i6) {
			Block block = block8[i6];
			this.mineBlocks.add(new BlockHarvestPower(block.blockID, 0.0F));
		}

	}

	public ToolBase getToolBase() {
		return this instanceof ItemPickaxe ? ToolBase.Pickaxe : (this instanceof ItemAxe ? ToolBase.Axe : (this instanceof ItemSpade ? ToolBase.Shovel : null));
	}

	public static float getToolPower(EnumToolMaterial material) {
		return material == EnumToolMaterial.EMERALD ? 80.0F : (material == EnumToolMaterial.IRON ? 60.0F : (material == EnumToolMaterial.STONE ? 40.0F : 20.0F));
	}

	public boolean canHarvest(Block block) {
		if(!this.usingSAPI && !this.isBlockOnList(block.blockID)) {
			if(this instanceof ItemPickaxe) {
				if(this.shiftedIndex <= 369) {
					return false;
				}

				if(block.blockMaterial == Material.rock || block.blockMaterial == Material.ice) {
					return true;
				}

				if(block.blockMaterial == Material.iron && this.basePower >= 40.0F) {
					return true;
				}
			} else if(this instanceof ItemAxe) {
				if(this.shiftedIndex <= 369) {
					return false;
				}

				if(block.blockMaterial == Material.wood || block.blockMaterial == Material.leaves || block.blockMaterial == Material.plants || block.blockMaterial == Material.cactus || block.blockMaterial == Material.pumpkin) {
					return true;
				}
			} else if(this instanceof ItemSpade) {
				if(this.shiftedIndex <= 369) {
					return false;
				}

				if(block.blockMaterial == Material.grassMaterial || block.blockMaterial == Material.ground || block.blockMaterial == Material.sand || block.blockMaterial == Material.snow || block.blockMaterial == Material.builtSnow || block.blockMaterial == Material.clay) {
					return true;
				}
			}
		}

		return super.canHarvest(block);
	}

	private boolean isBlockOnList(int blockID) {
		Iterator iterator3 = this.mineBlocks.iterator();

		BlockHarvestPower power;
		while(iterator3.hasNext()) {
			power = (BlockHarvestPower)iterator3.next();
			if(power.blockID == blockID) {
				return true;
			}
		}

		iterator3 = this.toolBase.mineBlocks.iterator();

		while(iterator3.hasNext()) {
			power = (BlockHarvestPower)iterator3.next();
			if(power.blockID == blockID) {
				return true;
			}
		}

		return false;
	}
}
