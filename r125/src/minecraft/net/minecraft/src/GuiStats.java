package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiStats extends GuiScreen {
	private static RenderItem renderItem = new RenderItem();
	protected GuiScreen parentGui;
	protected String statsTitle = "Select world";
	private GuiSlotStatsGeneral slotGeneral;
	private GuiSlotStatsItem slotItem;
	private GuiSlotStatsBlock slotBlock;
	private StatFileWriter statFileWriter;
	private GuiSlot selectedSlot = null;

	public GuiStats(GuiScreen guiScreen1, StatFileWriter statFileWriter2) {
		this.parentGui = guiScreen1;
		this.statFileWriter = statFileWriter2;
	}

	public void initGui() {
		this.statsTitle = StatCollector.translateToLocal("gui.stats");
		this.slotGeneral = new GuiSlotStatsGeneral(this);
		this.slotGeneral.registerScrollButtons(this.controlList, 1, 1);
		this.slotItem = new GuiSlotStatsItem(this);
		this.slotItem.registerScrollButtons(this.controlList, 1, 1);
		this.slotBlock = new GuiSlotStatsBlock(this);
		this.slotBlock.registerScrollButtons(this.controlList, 1, 1);
		this.selectedSlot = this.slotGeneral;
		this.addHeaderButtons();
	}

	public void addHeaderButtons() {
		StringTranslate stringTranslate1 = StringTranslate.getInstance();
		this.controlList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, stringTranslate1.translateKey("gui.done")));
		this.controlList.add(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, stringTranslate1.translateKey("stat.generalButton")));
		GuiButton guiButton2;
		this.controlList.add(guiButton2 = new GuiButton(2, this.width / 2 - 46, this.height - 52, 100, 20, stringTranslate1.translateKey("stat.blocksButton")));
		GuiButton guiButton3;
		this.controlList.add(guiButton3 = new GuiButton(3, this.width / 2 + 62, this.height - 52, 100, 20, stringTranslate1.translateKey("stat.itemsButton")));
		if(this.slotBlock.getSize() == 0) {
			guiButton2.enabled = false;
		}

		if(this.slotItem.getSize() == 0) {
			guiButton3.enabled = false;
		}

	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id == 0) {
				this.mc.displayGuiScreen(this.parentGui);
			} else if(guiButton1.id == 1) {
				this.selectedSlot = this.slotGeneral;
			} else if(guiButton1.id == 3) {
				this.selectedSlot = this.slotItem;
			} else if(guiButton1.id == 2) {
				this.selectedSlot = this.slotBlock;
			} else {
				this.selectedSlot.actionPerformed(guiButton1);
			}

		}
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.selectedSlot.drawScreen(i1, i2, f3);
		this.drawCenteredString(this.fontRenderer, this.statsTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}

	private void drawItemSprite(int i1, int i2, int i3) {
		this.drawButtonBackground(i1 + 1, i2 + 1);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		renderItem.drawItemIntoGui(this.fontRenderer, this.mc.renderEngine, i3, 0, Item.itemsList[i3].getIconFromDamage(0), i1 + 2, i2 + 2);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	private void drawButtonBackground(int i1, int i2) {
		this.drawSprite(i1, i2, 0, 0);
	}

	private void drawSprite(int i1, int i2, int i3, int i4) {
		int i5 = this.mc.renderEngine.getTexture("/gui/slot.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i5);
		Tessellator tessellator10 = Tessellator.instance;
		tessellator10.startDrawingQuads();
		tessellator10.addVertexWithUV((double)(i1 + 0), (double)(i2 + 18), (double)this.zLevel, (double)((float)(i3 + 0) * 0.0078125F), (double)((float)(i4 + 18) * 0.0078125F));
		tessellator10.addVertexWithUV((double)(i1 + 18), (double)(i2 + 18), (double)this.zLevel, (double)((float)(i3 + 18) * 0.0078125F), (double)((float)(i4 + 18) * 0.0078125F));
		tessellator10.addVertexWithUV((double)(i1 + 18), (double)(i2 + 0), (double)this.zLevel, (double)((float)(i3 + 18) * 0.0078125F), (double)((float)(i4 + 0) * 0.0078125F));
		tessellator10.addVertexWithUV((double)(i1 + 0), (double)(i2 + 0), (double)this.zLevel, (double)((float)(i3 + 0) * 0.0078125F), (double)((float)(i4 + 0) * 0.0078125F));
		tessellator10.draw();
	}

	static Minecraft getMinecraft(GuiStats guiStats0) {
		return guiStats0.mc;
	}

	static FontRenderer getFontRenderer1(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static StatFileWriter getStatsFileWriter(GuiStats guiStats0) {
		return guiStats0.statFileWriter;
	}

	static FontRenderer getFontRenderer2(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static FontRenderer getFontRenderer3(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static Minecraft getMinecraft1(GuiStats guiStats0) {
		return guiStats0.mc;
	}

	static void drawSprite(GuiStats guiStats0, int i1, int i2, int i3, int i4) {
		guiStats0.drawSprite(i1, i2, i3, i4);
	}

	static Minecraft getMinecraft2(GuiStats guiStats0) {
		return guiStats0.mc;
	}

	static FontRenderer getFontRenderer4(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static FontRenderer getFontRenderer5(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static FontRenderer getFontRenderer6(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static FontRenderer getFontRenderer7(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static FontRenderer getFontRenderer8(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static void drawGradientRect(GuiStats guiStats0, int i1, int i2, int i3, int i4, int i5, int i6) {
		guiStats0.drawGradientRect(i1, i2, i3, i4, i5, i6);
	}

	static FontRenderer getFontRenderer9(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static FontRenderer getFontRenderer10(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static void drawGradientRect1(GuiStats guiStats0, int i1, int i2, int i3, int i4, int i5, int i6) {
		guiStats0.drawGradientRect(i1, i2, i3, i4, i5, i6);
	}

	static FontRenderer getFontRenderer11(GuiStats guiStats0) {
		return guiStats0.fontRenderer;
	}

	static void drawItemSprite(GuiStats guiStats0, int i1, int i2, int i3) {
		guiStats0.drawItemSprite(i1, i2, i3);
	}
}
