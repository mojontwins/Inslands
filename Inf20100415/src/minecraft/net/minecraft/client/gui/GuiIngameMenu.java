package net.minecraft.client.gui;

import net.minecraft.client.GuiMainMenu;
import net.minecraft.game.world.World;

public final class GuiIngameMenu extends GuiScreen {
	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4, "Options..."));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 24, "Change world..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48, "Quit game"));
		this.controlList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 120, "Back to game"));
	}

	protected final void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if(guiButton1.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if(guiButton1.id == 2) {
			this.mc.closeWorld((World)null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		}

		if(guiButton1.id == 4) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

	public final void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}
}