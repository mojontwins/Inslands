package net.minecraft.src;

import paulscode.sound.SoundSystem;

public class GuiCreateWorldAether extends GuiCreateWorld {
	private int musicID;

	public GuiCreateWorldAether(GuiScreen guiscreen, int i) {
		super(guiscreen);
		this.musicID = i;
	}

	protected void actionPerformed(GuiButton guiscreen) {
		this.mc.theWorld = null;
		this.mc.thePlayer = null;
		GuiMainMenu.mmactive = false;

		try {
			SoundSystem e = (SoundSystem)ModLoader.getPrivateValue(SoundManager.class, (Object)null, 0);
			e.stop("sound_" + this.musicID);
			ModLoader.setPrivateValue(SoundManager.class, this.mc.sndManager, "i", 6000);
		} catch (Exception exception5) {
			if(exception5 instanceof NoSuchFieldException) {
				try {
					ModLoader.setPrivateValue(SoundManager.class, this.mc.sndManager, "ticksBeforeMusic", 6000);
				} catch (Exception exception4) {
					exception4.printStackTrace();
				}
			} else {
				exception5.printStackTrace();
			}
		}

		GuiMainMenu.musicId = -1;
		GuiMainMenu.loadingWorld = true;
		super.actionPerformed(guiscreen);
	}
}
