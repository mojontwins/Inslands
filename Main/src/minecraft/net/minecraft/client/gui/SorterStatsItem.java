package net.minecraft.client.gui;

import java.util.Comparator;

import net.minecraft.world.stats.StatBase;
import net.minecraft.world.stats.StatCrafting;
import net.minecraft.world.stats.StatList;

class SorterStatsItem implements Comparator<Object> {
	final GuiStats statsGUI;
	final GuiSlotStatsItem slotStatsItemGUI;

	SorterStatsItem(GuiSlotStatsItem guiSlotStatsItem1, GuiStats guiStats2) {
		this.slotStatsItemGUI = guiSlotStatsItem1;
		this.statsGUI = guiStats2;
	}

	public int func_27371_a(StatCrafting statCrafting1, StatCrafting statCrafting2) {
		int i3 = statCrafting1.getItemId();
		int i4 = statCrafting2.getItemId();
		StatBase statBase5 = null;
		StatBase statBase6 = null;
		if(this.slotStatsItemGUI.sectionId == 0) {
			statBase5 = StatList.objectBreakStats[i3];
			statBase6 = StatList.objectBreakStats[i4];
		} else if(this.slotStatsItemGUI.sectionId == 1) {
			statBase5 = StatList.objectCraftStats[i3];
			statBase6 = StatList.objectCraftStats[i4];
		} else if(this.slotStatsItemGUI.sectionId == 2) {
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

			int i7 = GuiStats.getStatsFileWriter(this.slotStatsItemGUI.parentGuiStats).writeStat(statBase5);
			int i8 = GuiStats.getStatsFileWriter(this.slotStatsItemGUI.parentGuiStats).writeStat(statBase6);
			if(i7 != i8) {
				return (i7 - i8) * this.slotStatsItemGUI.field_27270_f;
			}
		}

		return i3 - i4;
	}

	public int compare(Object object1, Object object2) {
		return this.func_27371_a((StatCrafting)object1, (StatCrafting)object2);
	}
}
