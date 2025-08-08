package net.minecraft.client.gui;

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

import net.minecraft.client.Config;
import net.minecraft.client.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GraphicsModeSorter;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StringTranslate;

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
	
	// Optifine
	
	public boolean ofFogFancy = false;
	public float ofFogStart = 0.8F;
	public int ofMipmapLevel = 0;
	public boolean ofMipmapLinear = false;
	public boolean ofLoadFar = false;
	public int ofPreloadedChunks = 0;
	public boolean ofOcclusionFancy = false;
	public boolean ofSmoothFps = false;
	public boolean ofSmoothInput = false;
	public float ofBrightness = 0.0F;
	public float ofAoLevel = 0.0F;
	public int ofClouds = 0;
	public float ofCloudsHeight = 0.0F;
	public int ofTrees = 0;
	public int ofGrass = 0;
	public int ofRain = 0;
	public int ofWater = 0;
	public int ofBetterGrass = 3;
	public int ofAutoSaveTicks = 4000;
	public boolean ofFastDebugInfo = false;
	public boolean ofWeather = true;
	public boolean ofSky = true;
	public boolean ofStars = true;
	public int ofChunkUpdates = 1;
	public boolean ofChunkUpdatesDynamic = true;
	public boolean ofFarView = false;
	public int ofTime = 0;
	public boolean ofClearWater = false;
	public int ofAnimatedWater = 0;
	public int ofAnimatedLava = 0;
	public boolean ofAnimatedFire = true;
	public boolean ofAnimatedPortal = true;
	public boolean ofAnimatedRedstone = true;
	public boolean ofAnimatedExplosion = true;
	public boolean ofAnimatedFlame = true;
	public boolean ofAnimatedSmoke = true;
	
	public static final int DEFAULT = 0;
	public static final int FAST = 1;
	public static final int FANCY = 2;
	public static final int OFF = 3;
	public static final int ANIM_ON = 0;
	public static final int ANIM_GENERATED = 1;
	public static final int ANIM_OFF = 2;
	public KeyBinding ofKeyBindZoom;
	
	// Custom
	public boolean meltBuild = true;
	
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
	public boolean retardedArm = true;
	public boolean threadedLighting = true;
	
	// Mine
	public boolean enableCheats = false;
	public boolean craftGuide = false;
	public boolean isCreative = false;
	
	public boolean clearWaters = true;
	public boolean colouredAthmospherics = true;
	public int FOV = 0;
	public float gammaSetting = 0.0F;
	
	public String displayMode = DEFAULT_DISPLAY_STRING;
	public boolean animatedTextures = true;
	
    public static final ArrayList<String> MODES = new ArrayList<String>();

	public GameSettings(Minecraft minecraft1, File file2) {
		this.mc = minecraft1;
		this.optionsFile = new File(file2, "options.txt");
		this.loadOptions();
		Config.setGameSettings(this);
		Config.setMinecraft(minecraft1);
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

		if(enumOptions1 == EnumOptions.BRIGHTNESS) {
			this.ofBrightness = f2;
		}

		if(enumOptions1 == EnumOptions.CLOUD_HEIGHT) {
			this.ofCloudsHeight = f2;
		}

		if(enumOptions1 == EnumOptions.AO_LEVEL) {
			this.ofAoLevel = f2;
			this.ambientOcclusion = this.ofAoLevel > 0.0F;
			this.mc.renderGlobal.loadRenderers();
		}

	}

	private void updateWaterOpacity() {

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
			if(!Config.isOcclusionAvailable()) {
				this.ofOcclusionFancy = false;
				this.advancedOpengl = false;
			} else if(!this.advancedOpengl) {
				this.advancedOpengl = true;
				this.ofOcclusionFancy = false;
			} else if(!this.ofOcclusionFancy) {
				this.ofOcclusionFancy = true;
			} else {
				this.ofOcclusionFancy = false;
				this.advancedOpengl = false;
			}

			//this.mc.renderGlobal.setAllRenderesVisible();
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
			this.limitFramerate = (this.limitFramerate + i2) % 4;
			Display.setVSyncEnabled(this.limitFramerate == 3);
		}

		if(enumOptions1 == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + i2 & 3;
		}

		if(enumOptions1 == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.HAND) {
			this.retardedArm = !this.retardedArm;
		}

		if(enumOptions1 == EnumOptions.THREADED_LIGHT) {
			this.threadedLighting = !this.threadedLighting;
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

		if(enumOptions1 == EnumOptions.MELTBUILD) {
			this.meltBuild = !this.meltBuild;
		}
		
		if(enumOptions1 == EnumOptions.FOG_FANCY) {
			if(!Config.isFancyFogAvailable()) {
				this.ofFogFancy = false;
			} else {
				this.ofFogFancy = !this.ofFogFancy;
			}
		}

		if(enumOptions1 == EnumOptions.FOG_START) {
			this.ofFogStart += 0.2F;
			if(this.ofFogStart > 0.81F) {
				this.ofFogStart = 0.2F;
			}
		}

		if(enumOptions1 == EnumOptions.MIPMAP_LEVEL) {
			++this.ofMipmapLevel;
			if(this.ofMipmapLevel > 4) {
				this.ofMipmapLevel = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(enumOptions1 == EnumOptions.MIPMAP_TYPE) {
			this.ofMipmapLinear = !this.ofMipmapLinear;
			this.mc.renderEngine.refreshTextures();
		}

		if(enumOptions1 == EnumOptions.LOAD_FAR) {
			this.ofLoadFar = !this.ofLoadFar;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.PRELOADED_CHUNKS) {
			this.ofPreloadedChunks += 2;
			if(this.ofPreloadedChunks > 8) {
				this.ofPreloadedChunks = 0;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.SMOOTH_FPS) {
			this.ofSmoothFps = !this.ofSmoothFps;
		}

		if(enumOptions1 == EnumOptions.SMOOTH_INPUT) {
			this.ofSmoothInput = !this.ofSmoothInput;
		}

		if(enumOptions1 == EnumOptions.CLOUDS) {
			++this.ofClouds;
			if(this.ofClouds > 3) {
				this.ofClouds = 0;
			}
		}

		if(enumOptions1 == EnumOptions.TREES) {
			++this.ofTrees;
			if(this.ofTrees > 2) {
				this.ofTrees = 0;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.GRASS) {
			++this.ofGrass;
			if(this.ofGrass > 2) {
				this.ofGrass = 0;
			}

			RenderBlocks.fancyGrass = Config.isGrassFancy();
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.RAIN) {
			++this.ofRain;
			if(this.ofRain > 3) {
				this.ofRain = 0;
			}
		}

		if(enumOptions1 == EnumOptions.WATER) {
			++this.ofWater;
			if(this.ofWater > 2) {
				this.ofWater = 0;
			}
		}

		if(enumOptions1 == EnumOptions.ANIMATED_WATER) {
			++this.ofAnimatedWater;
			if(this.ofAnimatedWater > 2) {
				this.ofAnimatedWater = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(enumOptions1 == EnumOptions.ANIMATED_LAVA) {
			++this.ofAnimatedLava;
			if(this.ofAnimatedLava > 2) {
				this.ofAnimatedLava = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(enumOptions1 == EnumOptions.ANIMATED_FIRE) {
			this.ofAnimatedFire = !this.ofAnimatedFire;
			this.mc.renderEngine.refreshTextures();
		}
		
		if(enumOptions1 == EnumOptions.ANIMATED_TEXTURES) {
			this.animatedTextures = !this.animatedTextures;
			this.mc.renderEngine.refreshTextures();
		}

		if(enumOptions1 == EnumOptions.ANIMATED_PORTAL) {
			this.ofAnimatedPortal = !this.ofAnimatedPortal;
			this.mc.renderEngine.refreshTextures();
		}

		if(enumOptions1 == EnumOptions.ANIMATED_REDSTONE) {
			this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
		}

		if(enumOptions1 == EnumOptions.ANIMATED_EXPLOSION) {
			this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
		}

		if(enumOptions1 == EnumOptions.ANIMATED_FLAME) {
			this.ofAnimatedFlame = !this.ofAnimatedFlame;
		}

		if(enumOptions1 == EnumOptions.ANIMATED_SMOKE) {
			this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
		}

		if(enumOptions1 == EnumOptions.FAST_DEBUG_INFO) {
			this.ofFastDebugInfo = !this.ofFastDebugInfo;
		}

		if(enumOptions1 == EnumOptions.AUTOSAVE_TICKS) {
			this.ofAutoSaveTicks *= 10;
			if(this.ofAutoSaveTicks > 40000) {
				this.ofAutoSaveTicks = 40;
			}
		}

		if(enumOptions1 == EnumOptions.BETTER_GRASS) {
			++this.ofBetterGrass;
			if(this.ofBetterGrass > 3) {
				this.ofBetterGrass = 1;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.WEATHER) {
			this.ofWeather = !this.ofWeather;
		}

		if(enumOptions1 == EnumOptions.SKY) {
			this.ofSky = !this.ofSky;
		}

		if(enumOptions1 == EnumOptions.STARS) {
			this.ofStars = !this.ofStars;
		}

		if(enumOptions1 == EnumOptions.CHUNK_UPDATES) {
			++this.ofChunkUpdates;
			if(this.ofChunkUpdates > 5) {
				this.ofChunkUpdates = 1;
			}
		}

		if(enumOptions1 == EnumOptions.CHUNK_UPDATES_DYNAMIC) {
			this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
		}

		if(enumOptions1 == EnumOptions.FAR_VIEW) {
			this.ofFarView = !this.ofFarView;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumOptions1 == EnumOptions.TIME) {
			++this.ofTime;
			if(this.ofTime > 2) {
				this.ofTime = 0;
			}
		}

		if(enumOptions1 == EnumOptions.CLEAR_WATER) {
			this.ofClearWater = !this.ofClearWater;
			this.updateWaterOpacity();
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
		if(enumOptions1 == EnumOptions.CLOUD_HEIGHT) return this.gammaSetting;
		if(enumOptions1 == EnumOptions.AO_LEVEL) return this.ofAoLevel;
		
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
		case MELTBUILD:
			return this.meltBuild;
		case HAND:
			return this.retardedArm;
		case THREADED_LIGHT:
			return this.threadedLighting;
		default:
			return false;
		}
	}

	public String getKeyBinding(EnumOptions enumOptions1) {
		StringTranslate stringTranslate2 = StringTranslate.getInstance();
		String s = stringTranslate2.translateKey(enumOptions1.getEnumString()) + ": ";
		if(enumOptions1.getEnumFloat()) {
			float f5 = this.getOptionFloatValue(enumOptions1);
			if(enumOptions1 == EnumOptions.SENSITIVITY) {
				if(f5 == 0.0F) {
					return s + stringTranslate2.translateKey("options.sensitivity.min");
				} else if(f5 == 1.0F) {
					return s + stringTranslate2.translateKey("options.sensitivity.max");
				} else {
					return s + (int)(f5 * 200.0F) + "%";
				}
			} else if(enumOptions1 == EnumOptions.GAMMA) {
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				return s + df.format(f5);
			} else if(enumOptions1 == EnumOptions.FOV) {
				return s + (70 + (int)(f5 * 60)) + "º";
			} else if(enumOptions1 == EnumOptions.AO_LEVEL) {
				if(f5 == 0.0F) {
					return s + stringTranslate2.translateKey("options.off");
				} else if(f5 == 1.0F) {
					return s + "Full";
				} else {
					return s + (int)(f5 * 100.0F) + "%";
				}
			} else {
				if(f5 == 0.0F) {
					return s + stringTranslate2.translateKey("options.off");
				} else {
					return s + (int)(f5 * 100.0F) + "%";
				}
			}

		} else if(enumOptions1.getEnumBoolean()) {
			boolean z4 = this.getOptionOrdinalValue(enumOptions1);
			return z4 ? s + stringTranslate2.translateKey("options.on") : s + stringTranslate2.translateKey("options.off");
		} else {
			switch(enumOptions1) {
				case RENDER_DISTANCE: return s + stringTranslate2.translateKey(RENDER_DISTANCES[this.renderDistance]);
				case DIFFICULTY: return s + stringTranslate2.translateKey(DIFFICULTIES[this.difficulty]);
				case GUI_SCALE: return s + stringTranslate2.translateKey(GUISCALES[this.guiScale]);
				case FRAMERATE_LIMIT: return this.limitFramerate == 3 ? s + "VSync" : s + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]);
				case GRAPHICS: return (this.fancyGraphics ? s + stringTranslate2.translateKey("options.graphics.fancy") : s + stringTranslate2.translateKey("options.graphics.fast"));
				case HAND: return s + (this.retardedArm ? stringTranslate2.translateKey("options.yes") : stringTranslate2.translateKey("options.no"));
				case THREADED_LIGHT: return s + (this.threadedLighting ? stringTranslate2.translateKey("options.on") : stringTranslate2.translateKey("options.off"));
				case IS_CREATIVE: return s + (this.isCreative ? stringTranslate2.translateKey("options.creative") : stringTranslate2.translateKey("options.survival"));
				case ENABLE_CHEATS: return s + (this.enableCheats ? stringTranslate2.translateKey("options.yes") : stringTranslate2.translateKey("options.no"));
				case CRAFT_GUIDE: return s + (this.craftGuide ? stringTranslate2.translateKey("options.yes") : stringTranslate2.translateKey("options.no"));
				case COLOURED_ATHMOSPHERICS: return s + (this.colouredAthmospherics ? stringTranslate2.translateKey("options.on") : stringTranslate2.translateKey("options.off"));
				case CLEAR_WATERS: return s + (this.clearWaters ? stringTranslate2.translateKey("options.on") : stringTranslate2.translateKey("options.off"));
				case DISPLAY_MODES: return s + this.displayMode;
				case FOG_FANCY: return this.ofFogFancy ? s + "Fancy" : s + "Fast";
				case FOG_START: return s + this.ofFogStart;
				case MIPMAP_LEVEL: return s + this.ofMipmapLevel;
				case MIPMAP_TYPE: return this.ofMipmapLinear ? s + "Linear" : s + "Nearest";
				case LOAD_FAR: return this.ofLoadFar ? s + "ON" : s + "OFF"; // Won't work/show on Inslands
				case PRELOADED_CHUNKS: return this.ofPreloadedChunks == 0 ? s + "OFF" : s + this.ofPreloadedChunks; // Won't work/show on Inslands
				case SMOOTH_FPS: return this.ofSmoothFps ? s + "ON" : s + "OFF";
				case SMOOTH_INPUT: return this.ofSmoothInput ? s + "ON" : s + "OFF";
				case CLOUDS: {
					switch(this.ofClouds) {
					case 1:
						return s + "Fast";
					case 2:
						return s + "Fancy";
					case 3:
						return s + "OFF";
					default:
						return s + "Default";
					}
				}
				case TREES: {
					switch(this.ofTrees) {
					case 1:
						return s + "Fast";
					case 2:
						return s + "Fancy";
					default:
						return s + "Default";
					}
				}
				case GRASS: {
					switch(this.ofGrass) {
					case 1:
						return s + "Fast";
					case 2:
						return s + "Fancy";
					default:
						return s + "Default";
					}
				}
				case RAIN: {
					switch(this.ofRain) {
					case 1:
						return s + "Fast";
					case 2:
						return s + "Fancy";
					case 3:
						return s + "OFF";
					default:
						return s + "Default";
					}
				}
				case WATER: {
					switch(this.ofWater) {
					case 1:
						return s + "Fast";
					case 2:
						return s + "Fancy";
					case 3:
						return s + "OFF";
					default:
						return s + "Default";
					}
				}
				case ANIMATED_WATER: {
					switch(this.ofAnimatedWater) {
					case 1:
						return s + "Dynamic";
					case 2:
						return s + "OFF";
					default:
						return s + "ON";
					}
				}
				case ANIMATED_LAVA: {
					switch(this.ofAnimatedLava) {
					case 1:
						return s + "Dynamic";
					case 2:
						return s + "OFF";
					default:
						return s + "ON";
					}
				}
				case ANIMATED_FIRE: return this.ofAnimatedFire ? s + "ON" : s + "OFF";
				case ANIMATED_TEXTURES: return this.animatedTextures ? s + "ON" : s + "OFF";
				case ANIMATED_PORTAL: return this.ofAnimatedPortal ? s + "ON" : s + "OFF";
				case ANIMATED_REDSTONE: return this.ofAnimatedRedstone ? s + "ON" : s + "OFF";
				case ANIMATED_EXPLOSION: return this.ofAnimatedExplosion ? s + "ON" : s + "OFF";
				case ANIMATED_FLAME: return this.ofAnimatedFlame ? s + "ON" : s + "OFF";
				case ANIMATED_SMOKE: return this.ofAnimatedSmoke ? s + "ON" : s + "OFF";
				case FAST_DEBUG_INFO: return this.ofFastDebugInfo ? s + "ON" : s + "OFF"; // No effect / Won't use
				case AUTOSAVE_TICKS: return this.ofAutoSaveTicks <= 40 ? s + "Default (2s)" : (this.ofAutoSaveTicks <= 400 ? s + "20s" : (this.ofAutoSaveTicks <= 4000 ? s + "3min" : s + "30min"));
				case BETTER_GRASS: {
					switch(this.ofBetterGrass) {
					case 1:
						return s + "Fast";
					case 2:
						return s + "Fancy";
					default:
						return s + "OFF";
					}
				}
				case WEATHER: return this.ofWeather ? s + "ON" : s + "OFF";
				case SKY: return this.ofSky ? s + "ON" : s + "OFF";
				case STARS: return this.ofStars ? s + "ON" : s + "OFF";
				case CHUNK_UPDATES: return s + this.ofChunkUpdates;
				case CHUNK_UPDATES_DYNAMIC: return this.ofChunkUpdatesDynamic ? s + "ON" : s + "OFF";
				case FAR_VIEW: return this.ofFarView ? s + "ON" : s + "OFF";
				case TIME: return this.ofTime == 1 ? s + "Day Only" : (this.ofTime == 2 ? s + "Night Only" : s + "Default");
				case CLEAR_WATER: return this.ofClearWater ? s + "ON" : s + "OFF";
				
				default: return s;
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

					if(string3[0].equals("hand")) {
						this.retardedArm = string3[1].equals("true");
					}
					
					if(string3[0].equals("threadedLighting")) {
						this.threadedLighting = string3[1].equals("true");
					}

					if(string3[0].equals("	")) {
						this.ambientOcclusion = string3[1].equals("true");
						if(this.ambientOcclusion) {
							this.ofAoLevel = 1.0F;
						} else {
							this.ofAoLevel = 0.0F;
						}
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

					if(string3[0].equals("meltBuild")) {
						this.meltBuild = string3[1].equals("true");
					}

					for(int i4 = 0; i4 < this.keyBindings.length; ++i4) {
						if(string3[0].equals("key_" + this.keyBindings[i4].keyDescription)) {
							this.keyBindings[i4].keyCode = Integer.parseInt(string3[1]);
						}
					}
					

					if(string3[0].equals("ofFogFancy") && string3.length >= 2) {
						this.ofFogFancy = string3[1].equals("true");
					}

					if(string3[0].equals("ofFogStart") && string3.length >= 2) {
						this.ofFogStart = Float.valueOf(string3[1]).floatValue();
						if(this.ofFogStart < 0.2F) {
							this.ofFogStart = 0.2F;
						}

						if(this.ofFogStart > 0.81F) {
							this.ofFogStart = 0.8F;
						}
					}

					if(string3[0].equals("ofMipmapLevel") && string3.length >= 2) {
						this.ofMipmapLevel = Integer.valueOf(string3[1]).intValue();
						if(this.ofMipmapLevel < 0) {
							this.ofMipmapLevel = 0;
						}

						if(this.ofMipmapLevel > 4) {
							this.ofMipmapLevel = 4;
						}
					}

					if(string3[0].equals("ofMipmapLinear") && string3.length >= 2) {
						this.ofMipmapLinear = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofLoadFar") && string3.length >= 2) {
						this.ofLoadFar = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofPreloadedChunks") && string3.length >= 2) {
						this.ofPreloadedChunks = Integer.valueOf(string3[1]).intValue();
						if(this.ofPreloadedChunks < 0) {
							this.ofPreloadedChunks = 0;
						}

						if(this.ofPreloadedChunks > 8) {
							this.ofPreloadedChunks = 8;
						}
					}

					if(string3[0].equals("ofOcclusionFancy") && string3.length >= 2) {
						this.ofOcclusionFancy = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofSmoothFps") && string3.length >= 2) {
						this.ofSmoothFps = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofSmoothInput") && string3.length >= 2) {
						this.ofSmoothInput = Boolean.valueOf(string3[1]).booleanValue();
					}

					/*
					if(string3[0].equals("ofBrightness") && string3.length >= 2) {
						this.ofBrightness = Float.valueOf(string3[1]).floatValue();
						this.ofBrightness = Config.limit(this.ofBrightness, 0.0F, 1.0F);
						// this.updateWorldLightLevels();
					}
					*/

					if(string3[0].equals("ofAoLevel") && string3.length >= 2) {
						this.ofAoLevel = Float.valueOf(string3[1]).floatValue();
						this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
						this.ambientOcclusion = this.ofAoLevel > 0.0F;
					}

					if(string3[0].equals("ofClouds") && string3.length >= 2) {
						this.ofClouds = Integer.valueOf(string3[1]).intValue();
						this.ofClouds = Config.limit(this.ofClouds, 0, 3);
					}

					if(string3[0].equals("ofCloudsHeight") && string3.length >= 2) {
						this.ofCloudsHeight = Float.valueOf(string3[1]).floatValue();
						this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
					}

					if(string3[0].equals("ofTrees") && string3.length >= 2) {
						this.ofTrees = Integer.valueOf(string3[1]).intValue();
						this.ofTrees = Config.limit(this.ofTrees, 0, 2);
					}

					if(string3[0].equals("ofGrass") && string3.length >= 2) {
						this.ofGrass = Integer.valueOf(string3[1]).intValue();
						this.ofGrass = Config.limit(this.ofGrass, 0, 2);
					}

					if(string3[0].equals("ofRain") && string3.length >= 2) {
						this.ofRain = Integer.valueOf(string3[1]).intValue();
						this.ofRain = Config.limit(this.ofRain, 0, 3);
					}

					if(string3[0].equals("ofWater") && string3.length >= 2) {
						this.ofWater = Integer.valueOf(string3[1]).intValue();
						this.ofWater = Config.limit(this.ofWater, 0, 3);
					}

					if(string3[0].equals("ofAnimatedWater") && string3.length >= 2) {
						this.ofAnimatedWater = Integer.valueOf(string3[1]).intValue();
						this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
					}

					if(string3[0].equals("ofAnimatedLava") && string3.length >= 2) {
						this.ofAnimatedLava = Integer.valueOf(string3[1]).intValue();
						this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
					}

					if(string3[0].equals("ofAnimatedFire") && string3.length >= 2) {
						this.ofAnimatedFire = Boolean.valueOf(string3[1]).booleanValue();
					}
					
					if(string3[0].equals("animatedTextures") && string3.length >= 2) {
						this.animatedTextures = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofAnimatedPortal") && string3.length >= 2) {
						this.ofAnimatedPortal = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofAnimatedRedstone") && string3.length >= 2) {
						this.ofAnimatedRedstone = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofAnimatedExplosion") && string3.length >= 2) {
						this.ofAnimatedExplosion = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofAnimatedFlame") && string3.length >= 2) {
						this.ofAnimatedFlame = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofAnimatedSmoke") && string3.length >= 2) {
						this.ofAnimatedSmoke = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofFastDebugInfo") && string3.length >= 2) {
						this.ofFastDebugInfo = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofAutoSaveTicks") && string3.length >= 2) {
						this.ofAutoSaveTicks = Integer.valueOf(string3[1]).intValue();
						this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
					}

					if(string3[0].equals("ofBetterGrass") && string3.length >= 2) {
						this.ofBetterGrass = Integer.valueOf(string3[1]).intValue();
						this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
					}

					if(string3[0].equals("ofWeather") && string3.length >= 2) {
						this.ofWeather = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofSky") && string3.length >= 2) {
						this.ofSky = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofStars") && string3.length >= 2) {
						this.ofStars = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofChunkUpdates") && string3.length >= 2) {
						this.ofChunkUpdates = Integer.valueOf(string3[1]).intValue();
						this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
					}

					if(string3[0].equals("ofChunkUpdatesDynamic") && string3.length >= 2) {
						this.ofChunkUpdatesDynamic = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofFarView") && string3.length >= 2) {
						this.ofFarView = Boolean.valueOf(string3[1]).booleanValue();
					}

					if(string3[0].equals("ofTime") && string3.length >= 2) {
						this.ofTime = Integer.valueOf(string3[1]).intValue();
						this.ofTime = Config.limit(this.ofTime, 0, 2);
					}

					if(string3[0].equals("ofClearWater") && string3.length >= 2) {
						this.ofClearWater = Boolean.valueOf(string3[1]).booleanValue();
						this.updateWaterOpacity();
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
			printWriter1.println("hand:" + this.retardedArm);
			printWriter1.println("threadedLighting:" + this.threadedLighting);
			printWriter1.println("clearWaters:" + this.clearWaters);
			printWriter1.println("ao:" + this.ambientOcclusion);
			printWriter1.println("skin:" + this.skin);
			printWriter1.println("lastServer:" + this.lastServer);
			printWriter1.println("FOV:" + this.FOV);
			printWriter1.println("gammaSetting" + this.gammaSetting);
			printWriter1.println("colouredAthmospherics" + this.colouredAthmospherics);
			printWriter1.println("displayMode:" + this.displayMode);
			printWriter1.println("meltBuild:" + this.meltBuild);

			for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
				printWriter1.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
			}

			printWriter1.println("ofFogFancy:" + this.ofFogFancy);
			printWriter1.println("ofFogStart:" + this.ofFogStart);
			printWriter1.println("ofMipmapLevel:" + this.ofMipmapLevel);
			printWriter1.println("ofMipmapLinear:" + this.ofMipmapLinear);
			printWriter1.println("ofLoadFar:" + this.ofLoadFar);
			printWriter1.println("ofPreloadedChunks:" + this.ofPreloadedChunks);
			printWriter1.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
			printWriter1.println("ofSmoothFps:" + this.ofSmoothFps);
			printWriter1.println("ofSmoothInput:" + this.ofSmoothInput);
			printWriter1.println("ofAoLevel:" + this.ofAoLevel);
			printWriter1.println("ofClouds:" + this.ofClouds);
			printWriter1.println("ofCloudsHeight:" + this.ofCloudsHeight);
			printWriter1.println("ofTrees:" + this.ofTrees);
			printWriter1.println("ofGrass:" + this.ofGrass);
			printWriter1.println("ofRain:" + this.ofRain);
			printWriter1.println("ofWater:" + this.ofWater);
			printWriter1.println("ofAnimatedWater:" + this.ofAnimatedWater);
			printWriter1.println("ofAnimatedLava:" + this.ofAnimatedLava);
			printWriter1.println("ofAnimatedFire:" + this.ofAnimatedFire);
			printWriter1.println("animatedTextures:" + this.animatedTextures);
			printWriter1.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
			printWriter1.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
			printWriter1.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
			printWriter1.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
			printWriter1.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
			printWriter1.println("ofFastDebugInfo:" + this.ofFastDebugInfo);
			printWriter1.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
			printWriter1.println("ofBetterGrass:" + this.ofBetterGrass);
			printWriter1.println("ofWeather:" + this.ofWeather);
			printWriter1.println("ofSky:" + this.ofSky);
			printWriter1.println("ofStars:" + this.ofStars);
			printWriter1.println("ofChunkUpdates:" + this.ofChunkUpdates);
			printWriter1.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
			printWriter1.println("ofFarView:" + this.ofFarView);
			printWriter1.println("ofTime:" + this.ofTime);
			printWriter1.println("ofClearWater:" + this.ofClearWater);
			
			printWriter1.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}

	}
	
	public boolean shouldRenderClouds() {
		return this.renderDistance < 2;
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
