package net.minecraft.client.gui;

import net.minecraft.game.world.World;

public final class GuiDeleteWorld extends GuiSelectWorld {
	public GuiDeleteWorld(GuiScreen guiScreen1) {
		super(guiScreen1);
		this.screenTitle = "Delete world";
	}

	public final void initGui2() {
		this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
	}

	public final void selectWorld(int i1) {
		String string2;
		if((string2 = this.getWorldName(i1)) != null) {
			this.mc.displayGuiScreen(new GuiYesNo(this, "Are you sure you want to delete this world?", "\'" + string2 + "\' will be lost forever!", i1));
		}

	}

	public final void deleteWorld(boolean z1, int i2) {
		if(z1) {
			World.deleteWorld(this.mc.getAppDir(), this.getWorldName(i2));
		}

		this.mc.displayGuiScreen(this.parentScreen);
	}
}