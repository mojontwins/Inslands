package net.minecraft.src;

class GuiSlotStatsGeneral extends GuiSlot {
	final GuiStats field_27276_a;

	public GuiSlotStatsGeneral(GuiStats guiStats1) {
		super(GuiStats.getMinecraft(guiStats1), guiStats1.width, guiStats1.height, 32, guiStats1.height - 64, 10);
		this.field_27276_a = guiStats1;
		this.func_27258_a(false);
	}

	protected int getSize() {
		return StatList.generalStats.size();
	}

	protected void elementClicked(int i1, boolean z2) {
	}

	protected boolean isSelected(int i1) {
		return false;
	}

	protected int getContentHeight() {
		return this.getSize() * 10;
	}

	protected void drawBackground() {
		this.field_27276_a.drawDefaultBackground();
	}

	protected void drawSlot(int i1, int i2, int i3, int i4, Tessellator tessellator5) {
		StatBase statBase6 = (StatBase)StatList.generalStats.get(i1);
		this.field_27276_a.drawString(GuiStats.getFontRenderer1(this.field_27276_a), StatCollector.translateToLocal(statBase6.getName()), i2 + 2, i3 + 1, i1 % 2 == 0 ? 0xFFFFFF : 9474192);
		String string7 = statBase6.func_27084_a(GuiStats.getStatsFileWriter(this.field_27276_a).writeStat(statBase6));
		this.field_27276_a.drawString(GuiStats.getFontRenderer2(this.field_27276_a), string7, i2 + 2 + 213 - GuiStats.getFontRenderer3(this.field_27276_a).getStringWidth(string7), i3 + 1, i1 % 2 == 0 ? 0xFFFFFF : 9474192);
	}
}
