package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

class GuiSlotLanguage extends GuiSlot {
	private ArrayList field_44013_b;
	private TreeMap field_44014_c;
	final GuiLanguage field_44015_a;

	public GuiSlotLanguage(GuiLanguage guiLanguage1) {
		super(guiLanguage1.mc, guiLanguage1.width, guiLanguage1.height, 32, guiLanguage1.height - 65 + 4, 18);
		this.field_44015_a = guiLanguage1;
		this.field_44014_c = StringTranslate.getInstance().getLanguageList();
		this.field_44013_b = new ArrayList();
		Iterator iterator2 = this.field_44014_c.keySet().iterator();

		while(iterator2.hasNext()) {
			String string3 = (String)iterator2.next();
			this.field_44013_b.add(string3);
		}

	}

	protected int getSize() {
		return this.field_44013_b.size();
	}

	protected void elementClicked(int i1, boolean z2) {
		StringTranslate.getInstance().setLanguage((String)this.field_44013_b.get(i1));
		this.field_44015_a.mc.fontRenderer.setUnicodeFlag(StringTranslate.getInstance().isUnicode());
		GuiLanguage.func_44005_a(this.field_44015_a).language = (String)this.field_44013_b.get(i1);
		this.field_44015_a.fontRenderer.setBidiFlag(StringTranslate.isBidrectional(GuiLanguage.func_44005_a(this.field_44015_a).language));
		GuiLanguage.func_46028_b(this.field_44015_a).displayString = StringTranslate.getInstance().translateKey("gui.done");
	}

	protected boolean isSelected(int i1) {
		return ((String)this.field_44013_b.get(i1)).equals(StringTranslate.getInstance().getCurrentLanguage());
	}

	protected int getContentHeight() {
		return this.getSize() * 18;
	}

	protected void drawBackground() {
		this.field_44015_a.drawDefaultBackground();
	}

	protected void drawSlot(int i1, int i2, int i3, int i4, Tessellator tessellator5) {
		this.field_44015_a.fontRenderer.setBidiFlag(true);
		this.field_44015_a.drawCenteredString(this.field_44015_a.fontRenderer, (String)this.field_44014_c.get(this.field_44013_b.get(i1)), this.field_44015_a.width / 2, i3 + 1, 0xFFFFFF);
		this.field_44015_a.fontRenderer.setBidiFlag(StringTranslate.isBidrectional(GuiLanguage.func_44005_a(this.field_44015_a).language));
	}
}
