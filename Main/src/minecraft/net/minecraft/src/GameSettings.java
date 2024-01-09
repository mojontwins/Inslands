package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import net.minecraft.client.Minecraft;

public class GameSettings {
	private static final String DEFAULT_DISPLAY_STRING = "DEFAULT";
	private static final String[] RENDER_DISTANCES = new String[]{"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
	private static final String[] DIFFICULTIES = new String[]{"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};
	private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
	private static final String[] LIMIT_FRAMERATES = new String[]{"performance.max", "performance.balanced", "performance.powersaver"};
	public float musicVolume = 1.0F;
	public float soundVolume = 1.0F;
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse = false;
	public int renderDistance = 1;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;
	public boolean advancedOpengl = false;
	public int limitFramerate = 1;
	public boolean fancyGraphics = false;
	public boolean ambientOcclusion = false;
	public String skin = "Default";
	public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
	public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
	public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
	public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
	public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
	public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
	public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
	public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
	public KeyBinding keyBindToggleFog = new KeyBinding("key.fog", 33);
	public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
	public KeyBinding keyBindCreative = new KeyBinding("key.creativeInventory", Keyboard.KEY_C);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog, this.keyBindCreative};
	protected Minecraft mc;
	private File optionsFile;
	public int difficulty = 2;
	public boolean hideGUI = false;
	public boolean thirdPersonView = false;
	public boolean showDebugInfo = false;
	public String lastServer = "";
	public boolean field_22275_C = false;
	public boolean smoothCamera = false;
	public boolean field_22273_E = false;
	public float field_22272_F = 1.0F;
	public float field_22271_G = 1.0F;
	public int guiScale = 0;
	
	public boolean enableCheats = false;
	public boolean craftGuide = false;
	public boolean isCreative = false;
	
	// Mine
	public boolean clearWaters = true;
	public boolean colouredAthmospherics = true;
	public int FOV = 0;
	public float gammaSetting = 0.0F;
	
	public String displayMode = DEFAULT_DISPLAY_STRING;

    public static final ArrayList<String> MODES = new ArrayList<String>();

	public GameSettings(Minecraft minecraft1, File file2) {
		this.mc = minecraft1;
		this.optionsFile = new File(file2, "options.txt");
		this.loadOptions();
	}

	public GameSettings() {
	}

	public String getKeyBindingDescription(int i1) {
		StringTranslate stringTranslate2 = StringTranslate.getInstance();
		return stringTranslate2.translateKey(this.keyBindings[i1].keyDescription);
	}

	public String getOptionDisplayString(int i1) {
		return Keyboard.getKeyName(this.keyBindings[i1].keyCode);
	}

	public void setKeyBinding(int i1, int i2) {
		this.keyBindings[i1].keyCode = i2;
		this.saveOptions();
	}

	public void setOptionFloatValue(EnumOptions enumOptions1, float f2) {
		if(enumOptions1 == EnumOptions.MUSIC) {
			this.musicVolume = f2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(enumOptions1 == EnumOptions.SOUND) {
			this.soundVolume = f2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(enumOptions1 == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = f2;
		}

		if(enumOptions1 == EnumOptions.FOV) {
			this.FOV = (int)(f2 * 60);
		}

		if(enumOptions1 == EnumOptions.GAMMA) {
			this.gammaSetting = f2;
		}

	}

	public void setOptionValue(EnumOptions enumOptions1, int i2) {
		if(enumOptions1 == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if(enumOptions1 == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + i2 & 3;
		}

		if(enumOptions1 == EnumOptions.GUI_SCALE) {
			this.guiScale = this.guiScale + i2 & 3;
		}

		if(enumOptions1 == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(enumOptions1 == EnumOptions.ADVANCED_OPENGL) {
			this.advancedOpengl = !this.advancedOpengl;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}
		
		if(enumOptions1 == EnumOptions.CLEAR_WATERS) {
			this.clearWaters = !this.clearWaters;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.FRAMERATE_LIMIT) {
			this.limitFramerate = (this.limitFramerate + i2 + 3) % 3;
		}

		if(enumOptions1 == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + i2 & 3;
		}

		if(enumOptions1 == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = !this.ambientOcclusion;
			this.mc.renderGlobal.loadRenderers();
		}

		if (enumOptions1 == EnumOptions.IS_CREATIVE) {
			this.isCreative = !this.isCreative;
		}
		
		if (enumOptions1 == EnumOptions.ENABLE_CHEATS) {
			this.enableCheats = !this.enableCheats;
		}
		
		if (enumOptions1 == EnumOptions.CRAFT_GUIDE) {
			this.craftGuide = !this.craftGuide;
		}

		if(enumOptions1 == EnumOptions.COLOURED_ATHMOSPHERICS) {
			this.colouredAthmospherics = !this.colouredAthmospherics;
		}

		if(enumOptions1 == EnumOptions.DISPLAY_MODES) {
			int idx = MODES.indexOf(this.displayMode);
			idx ++; if(idx >= MODES.size()) idx = 0;
			this.displayMode = MODES.get(idx);
		}

		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions enumOptions1) {
		//return enumOptions1 == EnumOptions.MUSIC ? this.musicVolume : (enumOptions1 == EnumOptions.SOUND ? this.soundVolume : (enumOptions1 == EnumOptions.SENSITIVITY ? this.mouseSensitivity : 0.0F));
		if(enumOptions1 == EnumOptions.MUSIC) return this.musicVolume;
		if(enumOptions1 == EnumOptions.SOUND) return this.soundVolume;
		if(enumOptions1 == EnumOptions.SENSITIVITY) return this.mouseSensitivity;
		if(enumOptions1 == EnumOptions.FOV) return (float)this.FOV / 60.0F;
		if(enumOptions1 == EnumOptions.GAMMA) return this.gammaSetting;
		
		return 0.0F;
	}

	public boolean getOptionOrdinalValue(EnumOptions enumOptions1) {
		//switch(GameSettings.SyntheticClass_1.$SwitchMap$net$minecraft$src$EnumOptions[enumOptions1.ordinal()]) {
		switch(enumOptions1) {
		case INVERT_MOUSE:
			return this.invertMouse;
		case VIEW_BOBBING:
			return this.viewBobbing;
		case CLEAR_WATERS:
			//return this.anaglyph;
			return this.clearWaters;
		case ADVANCED_OPENGL:
			return this.advancedOpengl;
		case AMBIENT_OCCLUSION:
			return this.ambientOcclusion;
		case COLOURED_ATHMOSPHERICS:
			return this.colouredAthmospherics;
		default:
			return false;
		}
	}

	public String getKeyBinding(EnumOptions enumOptions1) {
		StringTranslate stringTranslate2 = StringTranslate.getInstance();
		String string3 = stringTranslate2.translateKey(enumOptions1.getEnumString()) + ": ";
		if(enumOptions1.getEnumFloat()) {
			float f5 = this.getOptionFloatValue(enumOptions1);
			if(enumOptions1 == EnumOptions.SENSITIVITY) {
				if(f5 == 0.0F) {
					return string3 + stringTranslate2.translateKey("options.sensitivity.min");
				} else if(f5 == 1.0F) {
					return string3 + stringTranslate2.translateKey("options.sensitivity.max");
				} else {
					return string3 + (int)(f5 * 200.0F) + "%";
				}
			} else if(enumOptions1 == EnumOptions.GAMMA) {
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				return string3 + df.format(f5);
			} else if(enumOptions1 == EnumOptions.FOV) {
				return string3 + (70 + (int)(f5 * 60)) + "º";
			} else {
				if(f5 == 0.0F) {
					return string3 + stringTranslate2.translateKey("options.off");
				} else {
					return string3 + (int)(f5 * 100.0F) + "%";
				}
			}

		} else if(enumOptions1.getEnumBoolean()) {
			boolean z4 = this.getOptionOrdinalValue(enumOptions1);
			return z4 ? string3 + stringTranslate2.translateKey("options.on") : string3 + stringTranslate2.translateKey("options.off");
		} else {
			switch(enumOptions1) {
				case RENDER_DISTANCE: return string3 + stringTranslate2.translateKey(RENDER_DISTANCES[this.renderDistance]);
				case DIFFICULTY: return string3 + stringTranslate2.translateKey(DIFFICULTIES[this.difficulty]);
				case GUI_SCALE: return string3 + stringTranslate2.translateKey(GUISCALES[this.guiScale]);
				case FRAMERATE_LIMIT: return string3 + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]);
				case GRAPHICS: return (this.fancyGraphics ? string3 + stringTranslate2.translateKey("options.graphics.fancy") : string3 + stringTranslate2.translateKey("options.graphics.fast"));
				case IS_CREATIVE: return string3 + (this.isCreative ? stringTranslate2.translateKey("options.creative") : stringTranslate2.translateKey("options.survival"));
				case ENABLE_CHEATS: return string3 + (this.enableCheats ? stringTranslate2.translateKey("options.yes") : stringTranslate2.translateKey("options.no"));
				case CRAFT_GUIDE: return string3 + (this.craftGuide ? stringTranslate2.translateKey("options.yes") : stringTranslate2.translateKey("options.no"));
				case COLOURED_ATHMOSPHERICS: return string3 + (this.colouredAthmospherics ? stringTranslate2.translateKey("options.on") : stringTranslate2.translateKey("options.off"));
				case CLEAR_WATERS: return string3 + (this.clearWaters ? stringTranslate2.translateKey("options.on") : stringTranslate2.translateKey("options.off"));
				case DISPLAY_MODES: return string3 + this.displayMode;
				default: return string3;
			}
		}
	}

	public void loadOptions() {
		try {
			if(!this.optionsFile.exists()) {
				return;
			}

			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.optionsFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				try {
					String[] string3 = string2.split(":");
					if(string3[0].equals("music")) {
						this.musicVolume = this.parseFloat(string3[1]);
					}

					if(string3[0].equals("sound")) {
						this.soundVolume = this.parseFloat(string3[1]);
					}

					if(string3[0].equals("mouseSensitivity")) {
						this.mouseSensitivity = this.parseFloat(string3[1]);
					}

					if(string3[0].equals("invertYMouse")) {
						this.invertMouse = string3[1].equals("true");
					}

					if(string3[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(string3[1]);
					}

					if(string3[0].equals("guiScale")) {
						this.guiScale = Integer.parseInt(string3[1]);
					}

					if(string3[0].equals("bobView")) {
						this.viewBobbing = string3[1].equals("true");
					}

					if(string3[0].equals("anaglyph3d")) {
						this.anaglyph = string3[1].equals("true");
					}
					
					if(string3[0].equals("clearWaters")) {
						this.clearWaters = string3[1].equals("true");
					}

					if(string3[0].equals("advancedOpengl")) {
						this.advancedOpengl = string3[1].equals("true");
					}

					if(string3[0].equals("fpsLimit")) {
						this.limitFramerate = Integer.parseInt(string3[1]);
					}

					if(string3[0].equals("difficulty")) {
						this.difficulty = Integer.parseInt(string3[1]);
					}

					if(string3[0].equals("fancyGraphics")) {
						this.fancyGraphics = string3[1].equals("true");
					}

					if(string3[0].equals("ao")) {
						this.ambientOcclusion = string3[1].equals("true");
					}

					if(string3[0].equals("skin")) {
						this.skin = string3[1];
					}

					if(string3[0].equals("lastServer") && string3.length >= 2) {
						this.lastServer = string3[1];
					}

					if(string3[0].equals("FOV")) {
						this.FOV = Integer.parseInt(string3[1]);
					}
					
					if(string3[0].equals("gammaSetting")) {
						this.gammaSetting = this.parseFloat(string3[1]);
					}
					
					if(string3[0].equals("colouredAthmospherics")) {
						this.colouredAthmospherics = string3[1].equals("true");
					}

					if(string3[0].equals("displayMode") && string3.length >= 2) {
						this.displayMode = string3[1];
					}

					for(int i4 = 0; i4 < this.keyBindings.length; ++i4) {
						if(string3[0].equals("key_" + this.keyBindings[i4].keyDescription)) {
							this.keyBindings[i4].keyCode = Integer.parseInt(string3[1]);
						}
					}
				} catch (Exception exception5) {
					System.out.println("Skipping bad option: " + string2);
				}
			}

			bufferedReader1.close();
		} catch (Exception exception6) {
			System.out.println("Failed to load options");
			exception6.printStackTrace();
		}

	}

	private float parseFloat(String string1) {
		return string1.equals("true") ? 1.0F : (string1.equals("false") ? 0.0F : Float.parseFloat(string1));
	}

	public void saveOptions() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.optionsFile));
			printWriter1.println("music:" + this.musicVolume);
			printWriter1.println("sound:" + this.soundVolume);
			printWriter1.println("invertYMouse:" + this.invertMouse);
			printWriter1.println("mouseSensitivity:" + this.mouseSensitivity);
			printWriter1.println("viewDistance:" + this.renderDistance);
			printWriter1.println("guiScale:" + this.guiScale);
			printWriter1.println("bobView:" + this.viewBobbing);
			printWriter1.println("anaglyph3d:" + this.anaglyph);
			printWriter1.println("advancedOpengl:" + this.advancedOpengl);
			printWriter1.println("fpsLimit:" + this.limitFramerate);
			printWriter1.println("difficulty:" + this.difficulty);
			printWriter1.println("fancyGraphics:" + this.fancyGraphics);
			printWriter1.println("clearWaters:" + this.clearWaters);
			printWriter1.println("ao:" + this.ambientOcclusion);
			printWriter1.println("skin:" + this.skin);
			printWriter1.println("lastServer:" + this.lastServer);
			printWriter1.println("FOV:" + this.FOV);
			printWriter1.println("gammaSetting" + this.gammaSetting);
			printWriter1.println("colouredAthmospherics" + this.colouredAthmospherics);
			printWriter1.println("displayMode:" + this.displayMode);

			for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
				printWriter1.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
			}

			printWriter1.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}

	}

	static {
		DisplayMode current = Display.getDisplayMode();

        final ArrayList<DisplayMode> Resolutions = new ArrayList<DisplayMode>();
        MODES.add(DEFAULT_DISPLAY_STRING);
        try {
            final DisplayMode[] MODES = Display.getAvailableDisplayModes();
            for (int i = 0; i < MODES.length; ++i) {
                final DisplayMode mode = MODES[i];
                Resolutions.add(mode);
            }
        }
        catch (LWJGLException e) {
            e.printStackTrace();
        }

        for (final DisplayMode mode : Resolutions) {
        	if(mode.getBitsPerPixel() == current.getBitsPerPixel() && mode.getFrequency() == current.getFrequency());
            MODES.add(mode.getWidth() + "x" + mode.getHeight() + "x" + mode.getBitsPerPixel() + " " + mode.getFrequency() + "Hz");
        }

        Collections.sort(MODES, new GraphicsModeSorter());
    }

}
