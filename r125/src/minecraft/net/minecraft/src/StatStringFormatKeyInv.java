package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class StatStringFormatKeyInv implements IStatStringFormat {
	final Minecraft mc;

	public StatStringFormatKeyInv(Minecraft minecraft1) {
		this.mc = minecraft1;
	}

	public String formatString(String string1) {
		try {
			return String.format(string1, new Object[]{GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindInventory.keyCode)});
		} catch (Exception exception3) {
			return "Error: " + exception3.getLocalizedMessage();
		}
	}
}
