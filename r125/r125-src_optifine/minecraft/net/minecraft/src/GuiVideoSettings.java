package net.minecraft.src;

public class GuiVideoSettings extends GuiScreen {
	private GuiScreen parentGuiScreen;
	protected String screenTitle = "Video Settings";
	private GameSettings guiGameSettings;
	private boolean is64bit = false;
	private static EnumOptions[] videoOptions = new EnumOptions[]{EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE_FINE, EnumOptions.AO_LEVEL, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL, EnumOptions.GAMMA, EnumOptions.CHUNK_LOADING, EnumOptions.FOG_FANCY, EnumOptions.FOG_START};
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private long mouseStillTime = 0L;

	public GuiVideoSettings(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
		this.parentGuiScreen = par1GuiScreen;
		this.guiGameSettings = par2GameSettings;
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		this.screenTitle = stringtranslate.translateKey("options.videoTitle");
		int i = 0;
		EnumOptions[] aobj = videoOptions;
		int j = aobj.length;

		int y;
		for(y = 0; y < j; ++y) {
			EnumOptions x = aobj[y];
			int aobj2 = this.width / 2 - 155 + i % 2 * 160;
			int as = this.height / 6 + 21 * (i / 2) - 10;
			if(!x.getEnumFloat()) {
				this.controlList.add(new GuiSmallButton(x.returnEnumOrdinal(), aobj2, as, x, this.guiGameSettings.getKeyBinding(x)));
			} else {
				this.controlList.add(new GuiSlider(x.returnEnumOrdinal(), aobj2, as, x, this.guiGameSettings.getKeyBinding(x), this.guiGameSettings.getOptionFloatValue(x)));
			}

			++i;
		}

		y = this.height / 6 + 21 * (i / 2) - 10;
		boolean z13 = false;
		int i14 = this.width / 2 - 155 + 0;
		this.controlList.add(new GuiSmallButton(101, i14, y, "Details..."));
		i14 = this.width / 2 - 155 + 160;
		this.controlList.add(new GuiSmallButton(102, i14, y, "Quality..."));
		y += 21;
		i14 = this.width / 2 - 155 + 0;
		this.controlList.add(new GuiSmallButton(111, i14, y, "Animations..."));
		i14 = this.width / 2 - 155 + 160;
		this.controlList.add(new GuiSmallButton(112, i14, y, "Performance..."));
		y += 21;
		i14 = this.width / 2 - 155 + 0;
		this.controlList.add(new GuiSmallButton(121, i14, y, "Texture Packs..."));
		i14 = this.width / 2 - 155 + 160;
		this.controlList.add(new GuiSmallButton(122, i14, y, "Other..."));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, stringtranslate.translateKey("gui.done")));
		this.is64bit = false;
		String[] string15 = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
		String[] string16 = (String[])string15;
		int l = string16.length;

		for(int i1 = 0; i1 < l; ++i1) {
			String s = string16[i1];
			String s1 = System.getProperty(s);
			if(s1 != null && s1.indexOf("64") >= 0) {
				this.is64bit = true;
				break;
			}
		}

	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.enabled) {
			int i = this.guiGameSettings.guiScale;
			if(par1GuiButton.id < 100 && par1GuiButton instanceof GuiSmallButton) {
				this.guiGameSettings.setOptionValue(((GuiSmallButton)par1GuiButton).returnEnumOptions(), 1);
				par1GuiButton.displayString = this.guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
			}

			if(par1GuiButton.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentGuiScreen);
			}

			if(par1GuiButton.id == 101) {
				this.mc.gameSettings.saveOptions();
				GuiDetailSettingsOF scaledresolution = new GuiDetailSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(scaledresolution);
			}

			if(par1GuiButton.id == 102) {
				this.mc.gameSettings.saveOptions();
				GuiQualitySettingsOF scaledresolution1 = new GuiQualitySettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(scaledresolution1);
			}

			if(par1GuiButton.id == 111) {
				this.mc.gameSettings.saveOptions();
				GuiAnimationSettingsOF scaledresolution2 = new GuiAnimationSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(scaledresolution2);
			}

			if(par1GuiButton.id == 112) {
				this.mc.gameSettings.saveOptions();
				GuiPerformanceSettingsOF scaledresolution3 = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(scaledresolution3);
			}

			if(par1GuiButton.id == 121) {
				this.mc.gameSettings.saveOptions();
				GuiTexturePacks scaledresolution4 = new GuiTexturePacks(this);
				this.mc.displayGuiScreen(scaledresolution4);
			}

			if(par1GuiButton.id == 122) {
				this.mc.gameSettings.saveOptions();
				GuiOtherSettingsOF scaledresolution5 = new GuiOtherSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(scaledresolution5);
			}

			if(par1GuiButton.id != EnumOptions.AO_LEVEL.ordinal()) {
				if(this.guiGameSettings.guiScale != i) {
					ScaledResolution scaledresolution6 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
					int j = scaledresolution6.getScaledWidth();
					int k = scaledresolution6.getScaledHeight();
					this.setWorldAndResolution(this.mc, j, k);
				}

			}
		}
	}

	public void drawScreen(int x, int y, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		if(!this.is64bit && this.guiGameSettings.renderDistance == 0) {
			;
		}

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
		return btnName.equals("Graphics") ? new String[]{"Visual quality", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Changes the appearance of clouds, leaves, water,", "shadows and grass sides."} : (btnName.equals("Render Distance") ? new String[]{"Visible distance", "  Tiny - 32m (fastest)", "  Short - 64m (faster)", "  Normal - 128m", "  Far - 256m (slower)", "  Extreme - 512m (slowest!)", "The Extreme view distance is very resource demanding!"} : (btnName.equals("Smooth Lighting") ? new String[]{"Smooth lighting", "  OFF - no smooth lighting (faster)", "  1% - light smooth lighting (slower)", "  100% - dark smooth lighting (slower)"} : (btnName.equals("Performance") ? new String[]{"FPS Limit", "  Max FPS - no limit (fastest)", "  Balanced - limit 120 FPS (slower)", "  Power saver - limit 40 FPS (slowest)", "  VSync - limit to monitor framerate (60, 30, 20)", "Balanced and Power saver decrease the FPS even if", "the limit value is not reached."} : (btnName.equals("3D Anaglyph") ? new String[]{"3D mode used with red-cyan 3D glasses."} : (btnName.equals("View Bobbing") ? new String[]{"More realistic movement.", "When using mipmaps set it to OFF for best results."} : (btnName.equals("GUI Scale") ? new String[]{"GUI Scale", "Smaller GUI might be faster"} : (btnName.equals("Advanced OpenGL") ? new String[]{"Detect and render only visible geometry", "  OFF - all geometry is rendered (slower)", "  Fast - only visible geometry is rendered (fastest)", "  Fancy - conservative, avoids visual artifacts (faster)", "The option is available only if it is supported by the ", "graphic card."} : (btnName.equals("Fog") ? new String[]{"Fog type", "  Fast - faster fog", "  Fancy - slower fog, looks better", "  OFF - no fog, fastest", "The fancy fog is available only if it is supported by the ", "graphic card."} : (btnName.equals("Fog Start") ? new String[]{"Fog start", "  0.2 - the fog starts near the player", "  0.8 - the fog starts far from the player", "This option usually does not affect the performance."} : (btnName.equals("Brightness") ? new String[]{"Increases the brightness of darker objects", "  OFF - standard brightness", "  100% - maximum brightness for darker objects", "This options does not change the brightness of ", "fully black objects"} : (btnName.equals("Chunk Loading") ? new String[]{"Chunk Loading", "  Default - unstable FPS when loading chunks", "  Smooth - stable FPS", "  Multi-Core - stable FPS, 3x faster world loading", "Smooth and Multi-Core remove the stuttering and freezes", "caused by chunk loading.", "Multi-Core can speed up 3x the world loading and", "increase FPS by using a second CPU core."} : null)))))))))));
	}

	private String getButtonName(String displayString) {
		int pos = displayString.indexOf(58);
		return pos < 0 ? displayString : displayString.substring(0, pos);
	}

	private GuiButton getSelectedButton(int i, int j) {
		for(int k = 0; k < this.controlList.size(); ++k) {
			GuiButton btn = (GuiButton)this.controlList.get(k);
			boolean flag = i >= btn.xPosition && j >= btn.yPosition && i < btn.xPosition + btn.field_52008_a && j < btn.yPosition + btn.field_52007_b;
			if(flag) {
				return btn;
			}
		}

		return null;
	}
}
