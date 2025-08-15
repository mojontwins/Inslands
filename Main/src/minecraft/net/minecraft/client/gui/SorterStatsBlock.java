package net.minecraft.client.gui;

import java.util.Comparator;

import net.minecraft.world.stats.StatBase;
import net.minecraft.world.stats.StatCrafting;
import net.minecraft.world.stats.StatList;

class SorterStatsBlock implements Comparator<Object> {
	final GuiStats statsGUI;
	final GuiSlotStatsBlock slotStatsBlockGUI;

	SorterStatsBlock(GuiSlotStatsBlock guiSlotStatsBlock1, GuiStats guiStats2) {
		this.slotStatsBlockGUI = guiSlotStatsBlock1;
		this.statsGUI = guiStats2;
	}

	public int func_27297_a(StatCrafting statCrafting1, StatCrafting statCrafting2) {
		int i3 = statCrafting1.getItemId();
		int i4 = statCrafting2.getItemId();
		StatBase statBase5 = null;
		StatBase statBase6 = null;
		if(this.slotStatsBlockGUI.sectionId == 2) {
			statBase5 = StatList.mineBlockStatArray[i3];
			statBase6 = StatList.mineBlockStatArray[i4];
		} else if(this.slotStatsBlockGUI.sectionId == 0) {
			statBase5 = StatList.objectCraftStats[i3];
			statBase6 = StatList.objectCraftStats[i4];
		} else if(this.slotStatsBlockGUI.sectionId == 1) {
			statBase5 = StatList.objectUseStats[i3];
			statBase6 = StatList.objectUseStats[i4];
		}

		if(statBase5 != null || statBase6 != null) {
			if(statBase5 == null) {
				return 1;
			}

			if(statBase6 == null) {
				return -1;
			}

			int i7 = GuiStats.getStatsFileWriter(this.slotStatsBlockGUI.guiStats).writeStat(statBase5);
			int i8 = GuiStats.getStatsFileWriter(this.slotStatsBlockGUI.guiStats).writeStat(statBase6);
			if(i7 != i8) {
				return (i7 - i8) * this.slotStatsBlockGUI.field_27270_f;
			}
		}

		return i3 - i4;
	}

	public int compare(Object object1, Object object2) {
		return this.func_27297_a((StatCrafting)object1, (StatCrafting)object2);
	}
}
