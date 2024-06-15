package net.minecraft.src;

import paulscode.sound.SoundSystem;

public class GuiSelectWorldAether extends GuiSelectWorld {
	private int musicID;

	public GuiSelectWorldAether(GuiScreen guiscreen, int i) {
		super(guiscreen);
		this.musicID = i;
	}

	protected void actionPerformed(GuiButton var1) {
		super.actionPerformed(var1);
		if(var1.id == 3) {
			this.mc.displayGuiScreen(new GuiCreateWorldAether(this, this.musicID));
		}

	}

	public void selectWorld(int i) {
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
		super.selectWorld(i);
	}
}
