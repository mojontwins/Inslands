package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.src.StatBase;
import net.minecraft.src.StatCrafting;
import net.minecraft.src.StatList;

public class GuiSlotStatsItem extends GuiSlotStats {
	final GuiStats parentGuiStats;

	public GuiSlotStatsItem(GuiStats guiStats1) {
		super(guiStats1);
		this.parentGuiStats = guiStats1;
		this.statsList = new ArrayList<StatBase>();
		Iterator<StatBase> iterator2 = StatList.itemStats.iterator();

		while(iterator2.hasNext()) {
			StatCrafting statCrafting3 = (StatCrafting)iterator2.next();
			boolean z4 = false;
			int i5 = statCrafting3.getItemId();
			if(GuiStats.getStatsFileWriter(guiStats1).writeStat(statCrafting3) > 0) {
				z4 = true;
			} else if(StatList.objectBreakStats[i5] != null && GuiStats.getStatsFileWriter(guiStats1).writeStat(StatList.objectBreakStats[i5]) > 0) {
				z4 = true;
			} else if(StatList.objectCraftStats[i5] != null && GuiStats.getStatsFileWriter(guiStats1).writeStat(StatList.objectCraftStats[i5]) > 0) {
				z4 = true;
			}

			if(z4) {
				this.statsList.add(statCrafting3);
			}
		}

		this.statsSorter = new SorterStatsItem(this, guiStats1);
	}

	protected void drawButtons(int i1, int i2, Tessellator tessellator3) {
		super.drawButtons(i1, i2, tessellator3);
		if(this.currentStatsSection == 0) {
			GuiStats.drawSprite(this.parentGuiStats, i1 + 115 - 18 + 1, i2 + 1 + 1, 72, 18);
		} else {
			GuiStats.drawSprite(this.parentGuiStats, i1 + 115 - 18, i2 + 1, 72, 18);
		}

		if(this.currentStatsSection == 1) {
			GuiStats.drawSprite(this.parentGuiStats, i1 + 165 - 18 + 1, i2 + 1 + 1, 18, 18);
		} else {
			GuiStats.drawSprite(this.parentGuiStats, i1 + 165 - 18, i2 + 1, 18, 18);
		}

		if(this.currentStatsSection == 2) {
			GuiStats.drawSprite(this.parentGuiStats, i1 + 215 - 18 + 1, i2 + 1 + 1, 36, 18);
		} else {
			GuiStats.drawSprite(this.parentGuiStats, i1 + 215 - 18, i2 + 1, 36, 18);
		}

	}

	protected void drawSlot(int i1, int i2, int i3, int i4, Tessellator tessellator5) {
		StatCrafting statCrafting6 = this.func_27264_b(i1);
		int i7 = statCrafting6.getItemId();
		GuiStats.drawItemSprite(this.parentGuiStats, i2 + 40, i3, i7);
		this.func_27265_a((StatCrafting)StatList.objectBreakStats[i7], i2 + 115, i3, i1 % 2 == 0);
		this.func_27265_a((StatCrafting)StatList.objectCraftStats[i7], i2 + 165, i3, i1 % 2 == 0);
		this.func_27265_a(statCrafting6, i2 + 215, i3, i1 % 2 == 0);
	}

	protected String func_27263_a(int i1) {
		return i1 == 1 ? "stat.crafted" : (i1 == 2 ? "stat.used" : "stat.depleted");
	}
}
