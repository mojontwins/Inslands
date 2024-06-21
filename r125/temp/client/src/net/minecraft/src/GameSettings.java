package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

public class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[]{"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
	private static final String[] DIFFICULTIES = new String[]{"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};
	private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
	private static final String[] PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
	private static final String[] LIMIT_FRAMERATES = new String[]{"performance.max", "performance.balanced", "performance.powersaver"};
	public float musicVolume = 1.0F;
	public float soundVolume = 1.0F;
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse = false;
	public int renderDistance = 0;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;
	public boolean advancedOpengl = false;
	public int limitFramerate = 1;
	public boolean fancyGraphics = true;
	public boolean ambientOcclusion = true;
	public boolean clouds = true;
	public String skin = "Default";
	public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
	public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
	public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
	public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
	public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
	public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
	public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
	public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
	public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
	public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100);
	public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99);
	public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15);
	public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock};
	protected Minecraft mc;
	private File optionsFile;
	public int difficulty = 2;
	public boolean hideGUI = false;
	public int thirdPersonView = 0;
	public boolean showDebugInfo = false;
	public boolean field_50119_G = false;
	public String lastServer = "";
	public boolean noclip = false;
	public boolean smoothCamera = false;
	public boolean debugCamEnable = false;
	public float noclipRate = 1.0F;
	public float debugCamRate = 1.0F;
	public float fovSetting = 0.0F;
	public float gammaSetting = 0.0F;
	public int guiScale = 0;
	public int particleSetting = 0;
	public String language = "en_US";

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
		int i2 = this.keyBindings[i1].keyCode;
		return getKeyDisplayString(i2);
	}

	public static String getKeyDisplayString(int i0) {
		return i0 < 0 ? StatCollector.translateToLocalFormatted("key.mouseButton", new Object[]{i0 + 101}) : Keyboard.getKeyName(i0);
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
			this.fovSetting = f2;
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

		if(enumOptions1 == EnumOptions.PARTICLES) {
			this.particleSetting = (this.particleSetting + i2) % 3;
		}

		if(enumOptions1 == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(enumOptions1 == EnumOptions.RENDER_CLOUDS) {
			this.clouds = !this.clouds;
		}

		if(enumOptions1 == EnumOptions.ADVANCED_OPENGL) {
			this.advancedOpengl = !this.advancedOpengl;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
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

		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions enumOptions1) {
		return enumOptions1 == EnumOptions.FOV ? this.fovSetting : (enumOptions1 == EnumOptions.GAMMA ? this.gammaSetting : (enumOptions1 == EnumOptions.MUSIC ? this.musicVolume : (enumOptions1 == EnumOptions.SOUND ? this.soundVolume : (enumOptions1 == EnumOptions.SENSITIVITY ? this.mouseSensitivity : 0.0F))));
	}

	public boolean getOptionOrdinalValue(EnumOptions enumOptions1) {
		switch(GameSettings.SyntheticClass_1.$SwitchMap$net$minecraft$src$EnumOptions[enumOptions1.ordinal()]) {
		case 1:
			return this.invertMouse;
		case 2:
			return this.viewBobbing;
		case 3:
			return this.anaglyph;
		case 4:
			return this.advancedOpengl;
		case 5:
			return this.ambientOcclusion;
		case 6:
			return this.clouds;
		default:
			return false;
		}
	}

	private static String func_48571_a(String[] string0, int i1) {
		if(i1 < 0 || i1 >= string0.length) {
			i1 = 0;
		}

		StringTranslate stringTranslate2 = StringTranslate.getInstance();
		return stringTranslate2.translateKey(string0[i1]);
	}

	public String getKeyBinding(EnumOptions enumOptions1) {
		StringTranslate stringTranslate2 = StringTranslate.getInstance();
		String string3 = stringTranslate2.translateKey(enumOptions1.getEnumString()) + ": ";
		if(enumOptions1.getEnumFloat()) {
			float f5 = this.getOptionFloatValue(enumOptions1);
			return enumOptions1 == EnumOptions.SENSITIVITY ? (f5 == 0.0F ? string3 + stringTranslate2.translateKey("options.sensitivity.min") : (f5 == 1.0F ? string3 + stringTranslate2.translateKey("options.sensitivity.max") : string3 + (int)(f5 * 200.0F) + "%")) : (enumOptions1 == EnumOptions.FOV ? (f5 == 0.0F ? string3 + stringTranslate2.translateKey("options.fov.min") : (f5 == 1.0F ? string3 + stringTranslate2.translateKey("options.fov.max") : string3 + (int)(70.0F + f5 * 40.0F))) : (enumOptions1 == EnumOptions.GAMMA ? (f5 == 0.0F ? string3 + stringTranslate2.translateKey("options.gamma.min") : (f5 == 1.0F ? string3 + stringTranslate2.translateKey("options.gamma.max") : string3 + "+" + (int)(f5 * 100.0F) + "%")) : (f5 == 0.0F ? string3 + stringTranslate2.translateKey("options.off") : string3 + (int)(f5 * 100.0F) + "%")));
		} else if(enumOptions1.getEnumBoolean()) {
			boolean z4 = this.getOptionOrdinalValue(enumOptions1);
			return z4 ? string3 + stringTranslate2.translateKey("options.on") : string3 + stringTranslate2.translateKey("options.off");
		} else {
			return enumOptions1 == EnumOptions.RENDER_DISTANCE ? string3 + func_48571_a(RENDER_DISTANCES, this.renderDistance) : (enumOptions1 == EnumOptions.DIFFICULTY ? string3 + func_48571_a(DIFFICULTIES, this.difficulty) : (enumOptions1 == EnumOptions.GUI_SCALE ? string3 + func_48571_a(GUISCALES, this.guiScale) : (enumOptions1 == EnumOptions.PARTICLES ? string3 + func_48571_a(PARTICLES, this.particleSetting) : (enumOptions1 == EnumOptions.FRAMERATE_LIMIT ? string3 + func_48571_a(LIMIT_FRAMERATES, this.limitFramerate) : (enumOptions1 == EnumOptions.GRAPHICS ? (this.fancyGraphics ? string3 + stringTranslate2.translateKey("options.graphics.fancy") : string3 + stringTranslate2.translateKey("options.graphics.fast")) : string3)))));
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

					if(string3[0].equals("fov")) {
						this.fovSetting = this.parseFloat(string3[1]);
					}

					if(string3[0].equals("gamma")) {
						this.gammaSetting = this.parseFloat(string3[1]);
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

					if(string3[0].equals("particles")) {
						this.particleSetting = Integer.parseInt(string3[1]);
					}

					if(string3[0].equals("bobView")) {
						this.viewBobbing = string3[1].equals("true");
					}

					if(string3[0].equals("anaglyph3d")) {
						this.anaglyph = string3[1].equals("true");
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

					if(string3[0].equals("clouds")) {
						this.clouds = string3[1].equals("true");
					}

					if(string3[0].equals("skin")) {
						this.skin = string3[1];
					}

					if(string3[0].equals("lastServer") && string3.length >= 2) {
						this.lastServer = string3[1];
					}

					if(string3[0].equals("lang") && string3.length >= 2) {
						this.language = string3[1];
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

			KeyBinding.resetKeyBindingArrayAndHash();
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
			printWriter1.println("fov:" + this.fovSetting);
			printWriter1.println("gamma:" + this.gammaSetting);
			printWriter1.println("viewDistance:" + this.renderDistance);
			printWriter1.println("guiScale:" + this.guiScale);
			printWriter1.println("particles:" + this.particleSetting);
			printWriter1.println("bobView:" + this.viewBobbing);
			printWriter1.println("anaglyph3d:" + this.anaglyph);
			printWriter1.println("advancedOpengl:" + this.advancedOpengl);
			printWriter1.println("fpsLimit:" + this.limitFramerate);
			printWriter1.println("difficulty:" + this.difficulty);
			printWriter1.println("fancyGraphics:" + this.fancyGraphics);
			printWriter1.println("ao:" + this.ambientOcclusion);
			printWriter1.println("clouds:" + this.clouds);
			printWriter1.println("skin:" + this.skin);
			printWriter1.println("lastServer:" + this.lastServer);
			printWriter1.println("lang:" + this.language);

			for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
				printWriter1.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
			}

			printWriter1.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}

	}

	public boolean shouldRenderClouds() {
		return this.renderDistance < 2 && this.clouds;
	}

	static final class SyntheticClass_1 {
		static final int[] $SwitchMap$net$minecraft$src$EnumOptions = new int[EnumOptions.values().length];

		static {
			try {
				$SwitchMap$net$minecraft$src$EnumOptions[EnumOptions.INVERT_MOUSE.ordinal()] = 1;
			} catch (NoSuchFieldError noSuchFieldError6) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOptions[EnumOptions.VIEW_BOBBING.ordinal()] = 2;
			} catch (NoSuchFieldError noSuchFieldError5) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOptions[EnumOptions.ANAGLYPH.ordinal()] = 3;
			} catch (NoSuchFieldError noSuchFieldError4) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOptions[EnumOptions.ADVANCED_OPENGL.ordinal()] = 4;
			} catch (NoSuchFieldError noSuchFieldError3) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOptions[EnumOptions.AMBIENT_OCCLUSION.ordinal()] = 5;
			} catch (NoSuchFieldError noSuchFieldError2) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOptions[EnumOptions.RENDER_CLOUDS.ordinal()] = 6;
			} catch (NoSuchFieldError noSuchFieldError1) {
			}

		}
	}
}
