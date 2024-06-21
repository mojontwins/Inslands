package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ContainerCreative extends Container {
	public List itemList = new ArrayList();

	public ContainerCreative(EntityPlayer entityPlayer1) {
		Block[] block2 = new Block[]{Block.cobblestone, Block.stone, Block.oreDiamond, Block.oreGold, Block.oreIron, Block.oreCoal, Block.oreLapis, Block.oreRedstone, Block.stoneBrick, Block.stoneBrick, Block.stoneBrick, Block.stoneBrick, Block.blockClay, Block.blockDiamond, Block.blockGold, Block.blockSteel, Block.bedrock, Block.blockLapis, Block.brick, Block.cobblestoneMossy, Block.stairSingle, Block.stairSingle, Block.stairSingle, Block.stairSingle, Block.stairSingle, Block.stairSingle, Block.obsidian, Block.netherrack, Block.slowSand, Block.glowStone, Block.wood, Block.wood, Block.wood, Block.wood, Block.leaves, Block.leaves, Block.leaves, Block.leaves, Block.dirt, Block.grass, Block.sand, Block.sandStone, Block.sandStone, Block.sandStone, Block.gravel, Block.web, Block.planks, Block.planks, Block.planks, Block.planks, Block.sapling, Block.sapling, Block.sapling, Block.sapling, Block.deadBush, Block.sponge, Block.ice, Block.blockSnow, Block.plantYellow, Block.plantRed, Block.mushroomBrown, Block.mushroomRed, Block.cactus, Block.melon, Block.pumpkin, Block.pumpkinLantern, Block.vine, Block.fenceIron, Block.thinGlass, Block.netherBrick, Block.netherFence, Block.stairsNetherBrick, Block.whiteStone, Block.mycelium, Block.waterlily, Block.tallGrass, Block.tallGrass, Block.chest, Block.workbench, Block.glass, Block.tnt, Block.bookShelf, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.dispenser, Block.stoneOvenIdle, Block.music, Block.jukebox, Block.pistonStickyBase, Block.pistonBase, Block.fence, Block.fenceGate, Block.ladder, Block.rail, Block.railPowered, Block.railDetector, Block.torchWood, Block.stairCompactPlanks, Block.stairCompactCobblestone, Block.stairsBrick, Block.stairsStoneBrickSmooth, Block.lever, Block.pressurePlateStone, Block.pressurePlatePlanks, Block.torchRedstoneActive, Block.button, Block.trapdoor, Block.enchantmentTable, Block.redstoneLampIdle};
		int i3 = 0;
		int i4 = 0;
		int i5 = 0;
		int i6 = 0;
		int i7 = 0;
		int i8 = 0;
		int i9 = 0;
		int i10 = 0;
		int i11 = 1;

		int i12;
		int i13;
		for(i12 = 0; i12 < block2.length; ++i12) {
			i13 = 0;
			if(block2[i12] == Block.cloth) {
				i13 = i3++;
			} else if(block2[i12] == Block.stairSingle) {
				i13 = i4++;
			} else if(block2[i12] == Block.wood) {
				i13 = i5++;
			} else if(block2[i12] == Block.planks) {
				i13 = i6++;
			} else if(block2[i12] == Block.sapling) {
				i13 = i7++;
			} else if(block2[i12] == Block.stoneBrick) {
				i13 = i8++;
			} else if(block2[i12] == Block.sandStone) {
				i13 = i9++;
			} else if(block2[i12] == Block.tallGrass) {
				i13 = i11++;
			} else if(block2[i12] == Block.leaves) {
				i13 = i10++;
			}

			this.itemList.add(new ItemStack(block2[i12], 1, i13));
		}

		for(i12 = 256; i12 < Item.itemsList.length; ++i12) {
			if(Item.itemsList[i12] != null && Item.itemsList[i12].shiftedIndex != Item.potion.shiftedIndex && Item.itemsList[i12].shiftedIndex != Item.monsterPlacer.shiftedIndex) {
				this.itemList.add(new ItemStack(Item.itemsList[i12]));
			}
		}

		for(i12 = 1; i12 < 16; ++i12) {
			this.itemList.add(new ItemStack(Item.dyePowder.shiftedIndex, 1, i12));
		}

		Iterator iterator15 = EntityList.entityEggs.keySet().iterator();

		while(iterator15.hasNext()) {
			Integer integer17 = (Integer)iterator15.next();
			this.itemList.add(new ItemStack(Item.monsterPlacer.shiftedIndex, 1, integer17.intValue()));
		}

		InventoryPlayer inventoryPlayer16 = entityPlayer1.inventory;

		for(i13 = 0; i13 < 9; ++i13) {
			for(int i14 = 0; i14 < 8; ++i14) {
				this.addSlot(new Slot(GuiContainerCreative.getInventory(), i14 + i13 * 8, 8 + i14 * 18, 18 + i13 * 18));
			}
		}

		for(i13 = 0; i13 < 9; ++i13) {
			this.addSlot(new Slot(inventoryPlayer16, i13, 8 + i13 * 18, 184));
		}

		this.scrollTo(0.0F);
	}

	public boolean canInteractWith(EntityPlayer entityPlayer1) {
		return true;
	}

	public void scrollTo(float f1) {
		int i2 = this.itemList.size() / 8 - 8 + 1;
		int i3 = (int)((double)(f1 * (float)i2) + 0.5D);
		if(i3 < 0) {
			i3 = 0;
		}

		for(int i4 = 0; i4 < 9; ++i4) {
			for(int i5 = 0; i5 < 8; ++i5) {
				int i6 = i5 + (i4 + i3) * 8;
				if(i6 >= 0 && i6 < this.itemList.size()) {
					GuiContainerCreative.getInventory().setInventorySlotContents(i5 + i4 * 8, (ItemStack)this.itemList.get(i6));
				} else {
					GuiContainerCreative.getInventory().setInventorySlotContents(i5 + i4 * 8, (ItemStack)null);
				}
			}
		}

	}

	protected void retrySlotClick(int i1, int i2, boolean z3, EntityPlayer entityPlayer4) {
	}
}
