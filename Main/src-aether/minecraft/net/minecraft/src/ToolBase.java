package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ToolBase {
	public static final ToolBase Pickaxe;
	public static final ToolBase Shovel;
	public static final ToolBase Axe;
	public ArrayList mineBlocks = new ArrayList();
	public ArrayList mineMaterials = new ArrayList();

	static {
		SAPI.showText();
		Pickaxe = new ToolBase();
		Shovel = new ToolBase();
		Axe = new ToolBase();
		List list = Arrays.asList(new Integer[]{1, 4, 16, 21, 22, 23, 24, 43, 44, 45, 48, 52, 61, 62, 67, 77, 79, 87, 89, 93, 94});
		Iterator iterator2 = list.iterator();

		Integer blockID;
		while(iterator2.hasNext()) {
			blockID = (Integer)iterator2.next();
			Pickaxe.mineBlocks.add(new BlockHarvestPower(blockID.intValue(), 20.0F));
		}

		list = Arrays.asList(new Integer[]{15, 42, 71});
		iterator2 = list.iterator();

		while(iterator2.hasNext()) {
			blockID = (Integer)iterator2.next();
			Pickaxe.mineBlocks.add(new BlockHarvestPower(blockID.intValue(), 40.0F));
		}

		list = Arrays.asList(new Integer[]{14, 41, 56, 57, 73, 74});
		iterator2 = list.iterator();

		while(iterator2.hasNext()) {
			blockID = (Integer)iterator2.next();
			Pickaxe.mineBlocks.add(new BlockHarvestPower(blockID.intValue(), 60.0F));
		}

		list = Arrays.asList(new Integer[]{49});
		iterator2 = list.iterator();

		while(iterator2.hasNext()) {
			blockID = (Integer)iterator2.next();
			Pickaxe.mineBlocks.add(new BlockHarvestPower(blockID.intValue(), 80.0F));
		}

		list = Arrays.asList(new Integer[]{2, 3, 12, 13, 78, 80, 82});
		iterator2 = list.iterator();

		while(iterator2.hasNext()) {
			blockID = (Integer)iterator2.next();
			Shovel.mineBlocks.add(new BlockHarvestPower(blockID.intValue(), 20.0F));
		}

		list = Arrays.asList(new Integer[]{5, 17, 18, 25, 47, 53, 54, 58, 63, 64, 65, 66, 68, 69, 81, 84, 85});
		iterator2 = list.iterator();

		while(iterator2.hasNext()) {
			blockID = (Integer)iterator2.next();
			Axe.mineBlocks.add(new BlockHarvestPower(blockID.intValue(), 20.0F));
		}

	}

	public boolean canHarvest(Block block, float currentPower) {
		Iterator iterator4 = this.mineMaterials.iterator();

		while(iterator4.hasNext()) {
			Material power = (Material)iterator4.next();
			if(power == block.blockMaterial) {
				return true;
			}
		}

		iterator4 = this.mineBlocks.iterator();

		BlockHarvestPower power1;
		do {
			if(!iterator4.hasNext()) {
				return false;
			}

			power1 = (BlockHarvestPower)iterator4.next();
		} while(block.blockID != power1.blockID && currentPower < power1.percentage);

		return true;
	}
}
