package net.minecraft.src;

public class GuiVideoSettings extends GuiScreen {
	private GuiScreen parentGuiScreen;
	protected String screenTitle = "Video Settings";
	private GameSettings guiGameSettings;
	private static EnumOptions[] videoOptions = new EnumOptions[]{
			EnumOptions.DISPLAY_MODES,
			EnumOptions.GRAPHICS, 
			
			EnumOptions.RENDER_DISTANCE, 
			EnumOptions.AO_LEVEL,
			
			EnumOptions.FRAMERATE_LIMIT, 
			EnumOptions.GAMMA,
			
			EnumOptions.CLEAR_WATERS, 
			EnumOptions.COLOURED_ATHMOSPHERICS,
			
			EnumOptions.FOV,
			EnumOptions.VIEW_BOBBING, 
			
			EnumOptions.GUI_SCALE, 		
			EnumOptions.HAND,
			
			EnumOptions.MIPMAP_LEVEL, 
			EnumOptions.MIPMAP_TYPE,
			
			EnumOptions.ADVANCED_OPENGL
	};

	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private long mouseStillTime = 0L;
	
	public GuiVideoSettings(GuiScreen guiScreen1, GameSettings gameSettings2) {
		this.parentGuiScreen = guiScreen1;
		this.guiGameSettings = gameSettings2;
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		this.screenTitle = stringtranslate.translateKey("options.videoTitle");
		int i = 0;
		EnumOptions[] aenumoptions = videoOptions;
		int j = aenumoptions.length;

		int y;
		for(y = 0; y < j; ++y) {
			EnumOptions x = aenumoptions[y];
			int x1 = this.width / 2 - 155 + i % 2 * 160;
			int y1 = this.height / 6 + 21 * (i / 2) - 10;
			if(!x.getEnumFloat()) {
				this.controlList.add(new GuiSmallButton(x.returnEnumOrdinal(), x1, y1, x, this.guiGameSettings.getKeyBinding(x)));
			} else {
				this.controlList.add(new GuiSlider(x.returnEnumOrdinal(), x1, y1, x, this.guiGameSettings.getKeyBinding(x), this.guiGameSettings.getOptionFloatValue(x)));
			}

			++i;
		}

		y = this.height / 6 + 21 * (i / 2) - 10 + 21;

		int i10 = this.width / 2 - 155 + 0;
		
		this.controlList.add(new GuiButton(100, i10, y, 70, 20,  "Animations..."));
		i10 += 80;
		this.controlList.add(new GuiButton(101, i10, y, 70, 20, "Details..."));

		i10 = this.width / 2 - 155 + 160;
		this.controlList.add(new GuiButton(102, i10, y, 70, 20, "World..."));
		i10 += 80;
		this.controlList.add(new GuiButton(103, i10, y, 70, 20, "Other..."));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, stringtranslate.translateKey("gui.done")));
	}

	
	
	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id < 100 && guiButton1 instanceof GuiSmallButton) {
				this.guiGameSettings.setOptionValue(((GuiSmallButton)guiButton1).returnEnumOptions(), 1);
				guiButton1.displayString = this.guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guiButton1.id));
			}

			if(guiButton1.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentGuiScreen);
			}

			if(guiButton1.id == 100) {
				this.mc.gameSettings.saveOptions();
				GuiAnimationSettingsOF scaledresolution = new GuiAnimationSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(scaledresolution);
			}

			if(guiButton1.id == 101) {
				this.mc.gameSettings.saveOptions();
				GuiDetailSettingsOF settingsScreen = new GuiDetailSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(settingsScreen);
			}

			if(guiButton1.id == 102) {
				this.mc.gameSettings.saveOptions();
				GuiWorldSettingsOF settingsScreen = new GuiWorldSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(settingsScreen);
			}

			if(guiButton1.id == 103) {
				this.mc.gameSettings.saveOptions();
				GuiOtherSettingsOF settingsScreen = new GuiOtherSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(settingsScreen);
			}

			if(guiButton1.id != EnumOptions.BRIGHTNESS.ordinal() && guiButton1.id != EnumOptions.AO_LEVEL.ordinal()) {
				ScaledResolution scaledResolution2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int i3 = scaledResolution2.getScaledWidth();
				int i4 = scaledResolution2.getScaledHeight();
				this.setWorldAndResolution(this.mc, i3, i4);
			}
		}
	}

	public void drawScreen(int x, int y, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(x, y, f);
		if(Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5) {
			short activateDelay = 700;
			if(System.currentTimeMillis() >= this.mouseStillTime + (long)activateDelay) {
				int x1 = this.width / 2 - 150;
				int y1 = this.height / 6 - 5;
				if(y <= y1 + 98) {
					y1 += 105;
				}

				int x2 = x1 + 150 + 150;
				int y2 = y1 + 84 + 10;
				GuiButton btn = this.getSelectedButton(x, y);
				if(btn != null) {
					String s = this.getButtonName(btn.displayString);
					String[] lines = this.getTooltipLines(s);
					if(lines == null) {
						return;
					}

					this.drawGradientRect(x1, y1, x2, y2, -536870912, -536870912);

					for(int i = 0; i < lines.length; ++i) {
						String line = lines[i];
						this.fontRenderer.drawStringWithShadow(line, x1 + 5, y1 + 5 + i * 11, 14540253);
					}
				}

			}
		} else {
			this.lastMouseX = x;
			this.lastMouseY = y;
			this.mouseStillTime = System.currentTimeMillis();
		}
	}

	private String[] getTooltipLines(String btnName) {
		return btnName.equals("Graphics") ? new String[]{"Visual quality", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Changes the appearance of clouds, leaves, water,", "shadows and grass sides."} : (btnName.equals("Render Distance") ? new String[]{"Visible distance", "  Far - 256m (slower)", "  Normal - 128m", "  Short - 64m (faster)", "  Tiny - 32m (fastest)"} : (btnName.equals("Smooth Lighting") ? new String[]{"Smooth lighting", "  OFF - no smooth lighting (faster)", "  1% - light smooth lighting (slower)", "  100% - dark smooth lighting (slower)"} : (btnName.equals("Performance") ? new String[]{"FPS Limit", "  Max FPS - no limit (fastest)", "  Balanced - limit 120 FPS (slower)", "  Power saver - limit 40 FPS (slowest)", "  VSync - limit to monitor framerate (60, 30, 20)", "Balanced and Power saver decrease the FPS even if", "the limit value is not reached."} : (btnName.equals("3D Anaglyph") ? new String[]{"3D mode used with red-cyan 3D glasses."} : (btnName.equals("View Bobbing") ? new String[]{"More realistic movement.", "When using mipmaps set it to OFF for best results."} : (btnName.equals("GUI Scale") ? new String[]{"GUI Scale", "Smaller GUI might be faster"} : (btnName.equals("Advanced OpenGL") ? new String[]{"Detect and render only visible geometry", "  OFF - all geometry is rendered (slower)", "  Fast - ony visible geometry is rendered (fastest)", "  Fancy - conservative, avoids visual artifacts (faster)", "The option is available only if it is supported by the ", "graphic card."} : (btnName.equals("Fog") ? new String[]{"Fog type", "  Fast - faster fog", "  Fancy - slower fog, looks better", "The fancy fog is available only if it is supported by the ", "graphic card."} : (btnName.equals("Fog Start") ? new String[]{"Fog start", "  0.2 - the fog starts near the player", "  0.8 - the fog starts far from the player", "This option usually does not affect the performance."} : (btnName.equals("Mipmap Level") ? new String[]{"Visual effect which makes distant objects look better", "by smoothing the texture details", "  OFF - no smoothing", "  1 - minimum smoothing", "  4 - maximum smoothing", "This option usually does not affect the performance."} : (btnName.equals("Mipmap Type") ? new String[]{"Visual effect which makes distant objects look better", "by smoothing the texture details", "  Nearest - rough smoothing", "  Linear - fine smoothing", "This option usually does not affect the performance."} : (btnName.equals("Better Grass") ? new String[]{"Better Grass", "  OFF - default side grass texture, fastest", "  Fast - full side grass texture, slower", "  Fancy - dynamic side grass texture, slowest"} : (btnName.equals("Brightness") ? new String[]{"Increases the brightness of darker objects", "  OFF - standard brightness", "  100% - maximum brightness for darker objects", "This options does not change the brightness of ", "fully black objects"} : null)))))))))))));
	}

	private String getButtonName(String displayString) {
		int pos = displayString.indexOf(58);
		return pos < 0 ? displayString : displayString.substring(0, pos);
	}

	private GuiButton getSelectedButton(int i, int j) {
		for(int k = 0; k < this.controlList.size(); ++k) {
			GuiButton btn = (GuiButton)this.controlList.get(k);
			boolean flag = i >= btn.xPosition && j >= btn.yPosition && i < btn.xPosition + btn.width && j < btn.yPosition + btn.height;
			if(flag) {
				return btn;
			}
		}

		return null;
	}
}
