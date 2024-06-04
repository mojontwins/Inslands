package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GuiAchievementAether extends GuiAchievement {
	public GuiAchievementAether(Minecraft minecraft) {
		super(minecraft);
	}

	public void updateAchievementWindow() {
		if(!GuiMainMenu.mmactive) {
			super.updateAchievementWindow();
		}

	}
}
