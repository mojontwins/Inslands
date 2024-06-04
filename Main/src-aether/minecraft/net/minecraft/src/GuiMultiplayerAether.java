package net.minecraft.src;

import org.lwjgl.input.Keyboard;

import paulscode.sound.SoundSystem;

public class GuiMultiplayerAether extends GuiMultiplayer {
	private boolean mainMenu = false;
	private int musicID;
	private GuiScreen parentScreen;

	public GuiMultiplayerAether(GuiScreen guiscreen, int i) {
		super(guiscreen);
		this.parentScreen = guiscreen;
		this.musicID = i;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		if(!this.mainMenu) {
			this.mc.theWorld = null;
			this.mc.thePlayer = null;
			GuiMainMenu.mmactive = false;
			this.mc.ingameGUI = new GuiIngame(this.mc);

			try {
				SoundSystem e = (SoundSystem)ModLoader.getPrivateValue(SoundManager.class, (Object)null, 0);
				e.stop("sound_" + this.musicID);
				ModLoader.setPrivateValue(SoundManager.class, this.mc.sndManager, "i", 6000);
			} catch (Exception exception4) {
				if(exception4 instanceof NoSuchFieldException) {
					try {
						ModLoader.setPrivateValue(SoundManager.class, this.mc.sndManager, "ticksBeforeMusic", 6000);
					} catch (Exception exception3) {
						exception3.printStackTrace();
					}
				} else {
					exception4.printStackTrace();
				}
			}

			GuiMainMenu.loadingWorld = true;
			GuiMainMenu.musicId = -1;
		}
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 1) {
			this.mainMenu = true;
			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			super.actionPerformed(guibutton);
		}

	}
}
