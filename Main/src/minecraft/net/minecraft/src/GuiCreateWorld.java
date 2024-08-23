package net.minecraft.src;

import java.util.Random;

import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen {
	private GuiScreen parentGuiScreen;
	private GuiTextField textboxWorldName;
	private GuiTextField textboxSeed;
	private String folderName;
	private String gameMode = "survival";
	private boolean generateStructures = true;
	private boolean enableCheats = false;
	private boolean craftGuide = true;
	private boolean createClicked;
	private boolean moreOptions;
	private GuiButton gameModeButton;
	private GuiButton levelThemeButton;
	private GuiButton levelSizeButton;
	private GuiButton moreWorldOptions;
	private GuiButton generateStructuresButton;
	private GuiButton worldTypeButton;
	private GuiButton enableCheatsButton;
	private GuiButton craftingGuideButton;
	private GuiButton levelChecksButton;
	private String seed;
	private String localizedNewWorldText;
	private int worldType = -1;
	private int themeId = 0;
	private int sizeId = 1;
	
	private final String sizeStrings[] = WorldSize.sizeNames;
	private boolean generateCities = true;
	private boolean levelChecks = true;

	public GuiCreateWorld(GuiScreen par1GuiScreen) {
		this.parentGuiScreen = par1GuiScreen;
		this.seed = "";
		this.localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
	}

	public void updateScreen() {
		this.textboxWorldName.updateCursorCounter();
		this.textboxSeed.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, var1.translateKey("selectWorld.create")));
		this.controlList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, var1.translateKey("gui.cancel")));
		
		this.controlList.add(this.gameModeButton = new GuiButton(2, this.width / 2 - 75, 80, 150, 20, var1.translateKey("selectWorld.gameMode")));
		this.controlList.add(this.levelThemeButton = new GuiButton(8, this.width / 2 - 75, 100, 150, 20, var1.translateKey("selectWorld.levelTheme")));
		this.controlList.add(this.levelSizeButton = new GuiButton(9, this.width / 2 - 75, 120, 150, 20, var1.translateKey("selectWorld.levelSize")));
		this.controlList.add(this.worldTypeButton = new GuiButton(5, this.width / 2 - 75, 140, 150, 20, var1.translateKey("selectWorld.mapType")));
		
		this.controlList.add(this.moreWorldOptions = new GuiButton(3, this.width / 2 - 75, 172, 150, 20, var1.translateKey("selectWorld.moreWorldOptions")));
		
		this.controlList.add(this.generateStructuresButton = new GuiButton(4, this.width / 2 - 155, 100, 150, 20, var1.translateKey("selectWorld.mapFeatures")));
		this.generateStructuresButton.drawButton = false;
		this.controlList.add(this.levelChecksButton = new GuiButton(10, this.width / 2 + 5, 100, 150, 20, "Level checks"));
		this.levelChecksButton.drawButton = false;
		this.controlList.add(this.enableCheatsButton = new GuiButton(6, this.width / 2 - 155, 140, 150, 20, var1.translateKey("selectWorld.enableCheats")));
		this.enableCheatsButton.drawButton = false;
		this.controlList.add(this.craftingGuideButton = new GuiButton(7, this.width / 2 + 5, 140, 150, 20, var1.translateKey("selectWorld.craftingGuide")));
		this.craftingGuideButton.drawButton = false;
		
		this.textboxWorldName = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 50, 200, 20, var1.translateKey("selectWorld.newWorld"));
		this.textboxWorldName.isFocused = true;
		this.textboxWorldName.setText(this.localizedNewWorldText);
		this.textboxSeed = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, "");
		this.textboxSeed.setText(this.seed);
		this.makeUseableName();
		this.updateCaptions();
	}

	private void makeUseableName() {
		this.folderName = this.textboxWorldName.getText().trim();
		char[] var1 = ChatAllowedCharacters.allowedCharactersArray;
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			char var4 = var1[var3];
			this.folderName = this.folderName.replace(var4, '_');
		}

		if(MathHelper.stringNullOrLengthZero(this.folderName)) {
			this.folderName = "World";
		}

		this.folderName = func_25097_a(this.mc.getSaveLoader(), this.folderName);
	}

	private void updateCaptions() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.gameModeButton.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.gameMode);
		
		/*
		this.gameModeDescriptionLine1 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line1");
		this.gameModeDescriptionLine2 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line2");
		*/
		
		this.levelThemeButton.displayString = var1.translateKey("selectWorld.levelTheme") + ": " + var1.translateKey("theme." + LevelThemeSettings.findThemeById(this.themeId).name);
		this.levelSizeButton.displayString = var1.translateKey("selectWorld.levelSize") + ": " + var1.translateKey("size." + sizeStrings[this.sizeId]);
		
		this.generateStructuresButton.displayString = var1.translateKey("selectWorld.mapFeatures") + ": " + (this.generateStructures ? var1.translateKey("options.on") : var1.translateKey("options.off"));
		this.enableCheatsButton.displayString = var1.translateKey("selectWorld.enableCheats") + ": " + (this.enableCheats ? var1.translateKey("options.on") : var1.translateKey("options.off"));
		this.craftingGuideButton.displayString = var1.translateKey("selectWorld.craftingGuide") + ": " + (this.craftGuide ? var1.translateKey("options.on") : var1.translateKey("options.off"));
		this.levelChecksButton.displayString = "Level checks: " + (this.levelChecks ? var1.translateKey("options.on") : var1.translateKey("options.off")); 
		
		int i = this.worldType; if(i == -1) i = 0;
		this.worldTypeButton.displayString = var1.translateKey("selectWorld.mapType") + ": " + var1.translateKey(WorldType.worldTypes[i].getTranslateName());
	}

	public static String func_25097_a(ISaveFormat par0ISaveFormat, String par1Str) {
		for(par1Str = par1Str.replaceAll("[\\./\"]|COM", "_"); par0ISaveFormat.getWorldInfo(par1Str) != null; par1Str = par1Str + "-") {
		}

		return par1Str;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.enabled && par1GuiButton.drawButton) {
			if(par1GuiButton.id == 1) {
				this.mc.displayGuiScreen(this.parentGuiScreen);
			} else if(par1GuiButton.id == 0) {
				this.mc.displayGuiScreen((GuiScreen)null);
				if(this.createClicked) {
					return;
				}

				this.createClicked = true;
				long seed = (new Random()).nextLong();
				String var4 = this.textboxSeed.getText();
				if(!MathHelper.stringNullOrLengthZero(var4)) {
					try {
						long var9 = Long.parseLong(var4);
						if(var9 != 0L) {
							seed = var9;
						}
					} catch (NumberFormatException numberFormatException7) {
						seed = (long)var4.hashCode();
					}
				}
				
				this.mc.gameSettings.isCreative = this.gameMode.equals("creative");
				this.mc.gameSettings.enableCheats = this.enableCheats;
				this.mc.gameSettings.craftGuide = this.craftGuide;
				
				if(this.worldType == -1) this.worldType = WorldType.INFDEV.id;
				
				LevelThemeGlobalSettings.levelChecks = this.levelChecks;
				
				WorldType.worldTypes[this.worldType].onGUICreateWorldPress();
				
				this.mc.playerController = new PlayerControllerSP(this.mc);
				
				LevelThemeGlobalSettings.loadThemeById(this.themeId);
				LevelThemeGlobalSettings.worldTypeID = this.worldType;
				
				WorldSize.setSizeById(this.sizeId);
				
				Random rand = new Random(seed);
				GlobalVars.noiseOffsetX = rand.nextInt(1024) - rand.nextInt(1024);
				GlobalVars.noiseOffsetZ = rand.nextInt(1024) - rand.nextInt(1024);
				
				this.mc.startWorld(this.folderName, this.textboxWorldName.getText(), new WorldSettings(
						seed, 
						this.mc.gameSettings.isCreative ? 1 : 0, 
						this.generateStructures, 
						false, 
						this.generateCities,
						WorldType.worldTypes[this.worldType])
				);
				this.mc.displayGuiScreen((GuiScreen)null);
				
			} else if(par1GuiButton.id == 3) {
				this.moreOptions = !this.moreOptions;
				this.gameModeButton.drawButton = !this.moreOptions;
				this.levelSizeButton.drawButton = !this.moreOptions;
				this.levelThemeButton.drawButton = !this.moreOptions;
				this.worldTypeButton.drawButton = !this.moreOptions;
				this.generateStructuresButton.drawButton = this.moreOptions;
				this.enableCheatsButton.drawButton = this.moreOptions;
				this.craftingGuideButton.drawButton = this.moreOptions;
				this.levelChecksButton.drawButton = this.moreOptions;
				StringTranslate stringTranslate8;
				if(this.moreOptions) {
					stringTranslate8 = StringTranslate.getInstance();
					this.moreWorldOptions.displayString = stringTranslate8.translateKey("gui.done");
				} else {
					stringTranslate8 = StringTranslate.getInstance();
					this.moreWorldOptions.displayString = stringTranslate8.translateKey("selectWorld.moreWorldOptions");
				}
			} else if(par1GuiButton.id == 2) {
				if(this.gameMode.equals("survival")) {
					this.gameMode = "creative";
					this.updateCaptions();
				} else {
					this.gameMode = "survival";
					this.updateCaptions();
				}

				this.updateCaptions();
			} else if(par1GuiButton.id == 4) {
				this.generateStructures = !this.generateStructures;
				this.updateCaptions();
			} else if(par1GuiButton.id == 5) {
				++this.worldType;
				if(this.worldType >= WorldType.worldTypes.length) {
					this.worldType = 0;
				}

				while(WorldType.worldTypes[this.worldType] == null || !WorldType.worldTypes[this.worldType].getCanBeCreated()) {
					++this.worldType;
					if(this.worldType >= WorldType.worldTypes.length) {
						this.worldType = 0;
					}
				}
 
				this.updateCaptions();
			} else if(par1GuiButton.id == 6) {
				this.enableCheats = !this.enableCheats;
				this.updateCaptions(); 
			} else if(par1GuiButton.id == 7) {
				this.craftGuide = !this.craftGuide;
				this.updateCaptions();
			} else if(par1GuiButton.id == 10) {
				this.levelChecks = !this.levelChecks;
				this.updateCaptions();
			} else if(par1GuiButton.id == 8) {
				this.themeId ++;
				if(this.themeId == LevelThemeSettings.allThemeSettings.size()) {
					this.themeId = 0;
				}

				/*
				if(this.themeId == LevelThemeSettings.paradise.id) this.worldType = WorldType.SKY.id;
				if(this.themeId == LevelThemeSettings.forest.id) this.worldType = WorldType.INFDEV.id;
				*/
				int tentativeWorldType = LevelThemeSettings.findThemeById(this.themeId).preferredWorldType;
				if(tentativeWorldType >= 0) this.worldType = tentativeWorldType;
				
				this.updateCaptions();
			} else if(par1GuiButton.id == 9) {
				this.sizeId ++;
				if(this.sizeId == this.sizeStrings.length) this.sizeId = 0;
				this.updateCaptions();
			}
		}

	}

	protected void keyTyped(char par1, int par2) {
		if(this.textboxWorldName.isFocused && !this.moreOptions) {
			this.textboxWorldName.textboxKeyTyped(par1, par2);
			this.localizedNewWorldText = this.textboxWorldName.getText();
		} else if(this.textboxSeed.isFocused && this.moreOptions) {
			this.textboxSeed.textboxKeyTyped(par1, par2);
			this.seed = this.textboxSeed.getText();
		}

		if(par1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

		((GuiButton)this.controlList.get(0)).enabled = this.textboxWorldName.getText().length() > 0;
		this.makeUseableName();
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		if(!this.moreOptions) {
			this.textboxWorldName.mouseClicked(par1, par2, par3);
		} else {
			this.textboxSeed.mouseClicked(par1, par2, par3);
	}

	}

	public void drawScreen(int par1, int par2, float par3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.create"), this.width / 2, 20, 0xFFFFFF);
		if(!this.moreOptions) {
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 37, 10526880);
			//this.drawString(this.fontRenderer, var4.translateKey("selectWorld.resultFolder") + " " + this.folderName, this.width / 2 - 100, 85, 10526880);
			this.textboxWorldName.drawTextBox();
			//this.drawString(this.fontRenderer, this.gameModeDescriptionLine1, this.width / 2 - 100, 122, 10526880);
			//this.drawString(this.fontRenderer, this.gameModeDescriptionLine2, this.width / 2 - 100, 134, 10526880);
		} else {
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterSeed"), this.width / 2 - 100, 47, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.seedInfo"), this.width / 2 - 100, 85, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.mapFeatures.info"), this.width / 2 - 150, 122, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("Retry gen. if not correct"), this.width / 2 + 5, 122, 10526880);
			this.textboxSeed.drawTextBox();
		}

		super.drawScreen(par1, par2, par3);
	}
}
