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
import net.minecraft.util.StringTranslate;
import net.minecraft.world.level.tile.BlockLeaves;
import net.minecraft.world.level.tile.BlockLog;
import net.minecraft.world.stats.StatCollector;

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
	public boolean alphaTrees = false;
	
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
		StringTranslate translator = StringTranslate.getInstance();
		return translator.translateKey(this.keyBindings[i1].keyDescription);
	}

	public String getOptionDisplayString(int i1) {
		return Keyboard.getKeyName(this.keyBindings[i1].keyCode);
	}

	public void setKeyBinding(int i1, int i2) {
		this.keyBindings[i1].keyCode = i2;
		this.saveOptions();
	}

	public void setOptionFloatValue(EnumOptions opt, float f2) {
		if(opt == EnumOptions.MUSIC) {
			this.musicVolume = f2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(opt == EnumOptions.SOUND) {
			this.soundVolume = f2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(opt == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = f2;
		}

		if(opt == EnumOptions.FOV) {
			this.FOV = (int)(f2 * 60);
		}

		if(opt == EnumOptions.GAMMA) {
			this.gammaSetting = f2;
		}

		if(opt == EnumOptions.BRIGHTNESS) {
			this.ofBrightness = f2;
		}

		if(opt == EnumOptions.CLOUD_HEIGHT) {
			this.ofCloudsHeight = f2;
		}

		if(opt == EnumOptions.AO_LEVEL) {
			this.ofAoLevel = f2;
			this.ambientOcclusion = this.ofAoLevel > 0.0F;
			this.mc.renderGlobal.loadRenderers();
		}

	}

	private void updateWaterOpacity() {

	}
	
	public void setOptionValue(EnumOptions opt, int i2) {
		if(opt == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if(opt == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + i2 & 3;
		}

		if(opt == EnumOptions.GUI_SCALE) {
			this.guiScale = this.guiScale + i2 & 3;
		}

		if(opt == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(opt == EnumOptions.ADVANCED_OPENGL) {
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

		if(opt == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}
		
		if(opt == EnumOptions.CLEAR_WATERS) {
			this.clearWaters = !this.clearWaters;
			this.mc.renderGlobal.loadRenderers();
		}

		if(opt == EnumOptions.FRAMERATE_LIMIT) {
			this.limitFramerate = (this.limitFramerate + i2) % 4;
			Display.setVSyncEnabled(this.limitFramerate == 3);
		}

		if(opt == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + i2 & 3;
		}

		if(opt == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			RenderBlocks.fancyGrass = Config.isGrassFancy();
			this.mc.renderGlobal.loadRenderers();
		}

		if(opt == EnumOptions.HAND) {
			this.retardedArm = !this.retardedArm;
		}

		if(opt == EnumOptions.THREADED_LIGHT) {
			this.threadedLighting = !this.threadedLighting;
		}

		if(opt == EnumOptions.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = !this.ambientOcclusion;
			this.mc.renderGlobal.loadRenderers();
		}

		if (opt == EnumOptions.IS_CREATIVE) {
			this.isCreative = !this.isCreative;
		}
		
		if (opt == EnumOptions.ENABLE_CHEATS) {
			this.enableCheats = !this.enableCheats;
		}
		
		if (opt == EnumOptions.CRAFT_GUIDE) {
			this.craftGuide = !this.craftGuide;
		}

		if(opt == EnumOptions.COLOURED_ATHMOSPHERICS) {
			this.colouredAthmospherics = !this.colouredAthmospherics;
		}

		if(opt == EnumOptions.DISPLAY_MODES) {
			int idx = MODES.indexOf(this.displayMode);
			idx ++; if(idx >= MODES.size()) idx = 0;
			this.displayMode = MODES.get(idx);
		}

		if(opt == EnumOptions.MELTBUILD) {
			this.meltBuild = !this.meltBuild;
		}
		
		if(opt == EnumOptions.FOG_FANCY) {
			if(!Config.isFancyFogAvailable()) {
				this.ofFogFancy = false;
			} else {
				this.ofFogFancy = !this.ofFogFancy;
			}
		}

		if(opt == EnumOptions.FOG_START) {
			this.ofFogStart += 0.2F;
			if(this.ofFogStart > 0.81F) {
				this.ofFogStart = 0.2F;
			}
		}

		if(opt == EnumOptions.MIPMAP_LEVEL) {
			++this.ofMipmapLevel;
			if(this.ofMipmapLevel > 4) {
				this.ofMipmapLevel = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(opt == EnumOptions.MIPMAP_TYPE) {
			this.ofMipmapLinear = !this.ofMipmapLinear;
			this.mc.renderEngine.refreshTextures();
		}

		if(opt == EnumOptions.LOAD_FAR) {
			this.ofLoadFar = !this.ofLoadFar;
			this.mc.renderGlobal.loadRenderers();
		}

		if(opt == EnumOptions.PRELOADED_CHUNKS) {
			this.ofPreloadedChunks += 2;
			if(this.ofPreloadedChunks > 8) {
				this.ofPreloadedChunks = 0;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(opt == EnumOptions.SMOOTH_FPS) {
			this.ofSmoothFps = !this.ofSmoothFps;
		}

		if(opt == EnumOptions.SMOOTH_INPUT) {
			this.ofSmoothInput = !this.ofSmoothInput;
		}

		if(opt == EnumOptions.CLOUDS) {
			++this.ofClouds;
			if(this.ofClouds > 3) {
				this.ofClouds = 0;
			}
		}

		if(opt == EnumOptions.TREES) {
			++this.ofTrees;
			if(this.ofTrees > 2) {
				this.ofTrees = 0;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(opt == EnumOptions.GRASS) {
			++this.ofGrass;
			if(this.ofGrass > 2) {
				this.ofGrass = 0;
			}

			RenderBlocks.fancyGrass = Config.isGrassFancy();
			this.mc.renderGlobal.loadRenderers();
		}

		if(opt == EnumOptions.RAIN) {
			++this.ofRain;
			if(this.ofRain > 3) {
				this.ofRain = 0;
			}
		}

		if(opt == EnumOptions.WATER) {
			++this.ofWater;
			if(this.ofWater > 2) {
				this.ofWater = 0;
			}
		}

		if(opt == EnumOptions.ANIMATED_WATER) {
			++this.ofAnimatedWater;
			if(this.ofAnimatedWater > 2) {
				this.ofAnimatedWater = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(opt == EnumOptions.ANIMATED_LAVA) {
			++this.ofAnimatedLava;
			if(this.ofAnimatedLava > 2) {
				this.ofAnimatedLava = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(opt == EnumOptions.ANIMATED_FIRE) {
			this.ofAnimatedFire = !this.ofAnimatedFire;
			this.mc.renderEngine.refreshTextures();
		}
		
		if(opt == EnumOptions.ANIMATED_TEXTURES) {
			this.animatedTextures = !this.animatedTextures;
			this.mc.renderEngine.refreshTextures();
		}

		if(opt == EnumOptions.ANIMATED_PORTAL) {
			this.ofAnimatedPortal = !this.ofAnimatedPortal;
			this.mc.renderEngine.refreshTextures();
		}

		if(opt == EnumOptions.ANIMATED_REDSTONE) {
			this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
		}

		if(opt == EnumOptions.ANIMATED_EXPLOSION) {
			this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
		}

		if(opt == EnumOptions.ANIMATED_FLAME) {
			this.ofAnimatedFlame = !this.ofAnimatedFlame;
		}

		if(opt == EnumOptions.ANIMATED_SMOKE) {
			this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
		}

		if(opt == EnumOptions.FAST_DEBUG_INFO) {
			this.ofFastDebugInfo = !this.ofFastDebugInfo;
		}

		if(opt == EnumOptions.AUTOSAVE_TICKS) {
			this.ofAutoSaveTicks *= 10;
			if(this.ofAutoSaveTicks > 40000) {
				this.ofAutoSaveTicks = 40;
			}
		}

		if(opt == EnumOptions.BETTER_GRASS) {
			++this.ofBetterGrass;
			if(this.ofBetterGrass > 3) {
				this.ofBetterGrass = 1;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(opt == EnumOptions.WEATHER) {
			this.ofWeather = !this.ofWeather;
		}

		if(opt == EnumOptions.SKY) {
			this.ofSky = !this.ofSky;
		}

		if(opt == EnumOptions.STARS) {
			this.ofStars = !this.ofStars;
		}

		if(opt == EnumOptions.CHUNK_UPDATES) {
			++this.ofChunkUpdates;
			if(this.ofChunkUpdates > 5) {
				this.ofChunkUpdates = 1;
			}
		}

		if(opt == EnumOptions.CHUNK_UPDATES_DYNAMIC) {
			this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
		}

		if(opt == EnumOptions.FAR_VIEW) {
			this.ofFarView = !this.ofFarView;
			this.mc.renderGlobal.loadRenderers();
		}

		if(opt == EnumOptions.TIME) {
			++this.ofTime;
			if(this.ofTime > 2) {
				this.ofTime = 0;
			}
		}

		if(opt == EnumOptions.CLEAR_WATER) {
			this.ofClearWater = !this.ofClearWater;
			this.updateWaterOpacity();
		}

		if(opt == EnumOptions.ALPHA_TREES) {
			this.alphaTrees = !this.alphaTrees;
			BlockLog.lockTextures = this.alphaTrees;
			BlockLeaves.lockTextures = this.alphaTrees;
		}

		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions opt) {
		//return opt == EnumOptions.MUSIC ? this.musicVolume : (opt == EnumOptions.SOUND ? this.soundVolume : (opt == EnumOptions.SENSITIVITY ? this.mouseSensitivity : 0.0F));
		if(opt == EnumOptions.MUSIC) return this.musicVolume;
		if(opt == EnumOptions.SOUND) return this.soundVolume;
		if(opt == EnumOptions.SENSITIVITY) return this.mouseSensitivity;
		if(opt == EnumOptions.FOV) return (float)this.FOV / 60.0F;
		if(opt == EnumOptions.GAMMA) return this.gammaSetting;
		if(opt == EnumOptions.CLOUD_HEIGHT) return this.gammaSetting;
		if(opt == EnumOptions.AO_LEVEL) return this.ofAoLevel;
		
		return 0.0F;
	}

	public boolean getOptionOrdinalValue(EnumOptions opt) {
		//switch(GameSettings.SyntheticClass_1.$SwitchMap$net$minecraft$src$EnumOptions[opt.ordinal()]) {
		switch(opt) {
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

	public String getKeyBinding(EnumOptions opt) {
		StringTranslate translator = StringTranslate.getInstance();
		String s = translator.translateKey(opt.getEnumString()) + ": ";
		if(opt.getEnumFloat()) {
			float f5 = this.getOptionFloatValue(opt);
			if(opt == EnumOptions.SENSITIVITY) {
				if(f5 == 0.0F) {
					return s + translator.translateKey("options.sensitivity.min");
				} else if(f5 == 1.0F) {
					return s + translator.translateKey("options.sensitivity.max");
				} else {
					return s + (int)(f5 * 200.0F) + "%";
				}
			} else if(opt == EnumOptions.GAMMA) {
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				return s + df.format(f5);
			} else if(opt == EnumOptions.FOV) {
				return s + (70 + (int)(f5 * 60)) + "º";
			} else if(opt == EnumOptions.AO_LEVEL) {
				if(f5 == 0.0F) {
					return s + translator.translateKey("options.off");
				} else if(f5 == 1.0F) {
					return s + "Full";
				} else {
					return s + (int)(f5 * 100.0F) + "%";
				}
			} else {
				if(f5 == 0.0F) {
					return s + translator.translateKey("options.off");
				} else {
					return s + (int)(f5 * 100.0F) + "%";
				}
			}

		} else if(opt.getEnumBoolean()) {
			boolean z4 = this.getOptionOrdinalValue(opt);
			return z4 ? s + translator.translateKey("options.on") : s + translator.translateKey("options.off");
		} else {
			switch(opt) {
				case RENDER_DISTANCE: return s + translator.translateKey(RENDER_DISTANCES[this.renderDistance]);
				case DIFFICULTY: return s + translator.translateKey(DIFFICULTIES[this.difficulty]);
				case GUI_SCALE: return s + translator.translateKey(GUISCALES[this.guiScale]);
				case FRAMERATE_LIMIT: return this.limitFramerate == 3 ? s + "VSync" : s + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]);
				case GRAPHICS: return (this.fancyGraphics ? s + translator.translateKey("options.graphics.fancy") : s + translator.translateKey("options.graphics.fast"));
				case HAND: return s + (this.retardedArm ? translator.translateKey("options.yes") : translator.translateKey("options.no"));
				case THREADED_LIGHT: return s + (this.threadedLighting ? translator.translateKey("options.on") : translator.translateKey("options.off"));
				case IS_CREATIVE: return s + (this.isCreative ? translator.translateKey("options.creative") : translator.translateKey("options.survival"));
				case ENABLE_CHEATS: return s + (this.enableCheats ? translator.translateKey("options.yes") : translator.translateKey("options.no"));
				case CRAFT_GUIDE: return s + (this.craftGuide ? translator.translateKey("options.yes") : translator.translateKey("options.no"));
				case COLOURED_ATHMOSPHERICS: return s + (this.colouredAthmospherics ? translator.translateKey("options.on") : translator.translateKey("options.off"));
				case CLEAR_WATERS: return s + (this.clearWaters ? translator.translateKey("options.on") : translator.translateKey("options.off"));
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
				case ALPHA_TREES: return this.alphaTrees ? s + "YES" : s + "NO";
				
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
					String[] tokens = string2.split(":");
					if(tokens[0].equals("music")) {
						this.musicVolume = this.parseFloat(tokens[1]);
					}

					if(tokens[0].equals("sound")) {
						this.soundVolume = this.parseFloat(tokens[1]);
					}

					if(tokens[0].equals("mouseSensitivity")) {
						this.mouseSensitivity = this.parseFloat(tokens[1]);
					}

					if(tokens[0].equals("invertYMouse")) {
						this.invertMouse = tokens[1].equals("true");
					}

					if(tokens[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(tokens[1]);
					}

					if(tokens[0].equals("guiScale")) {
						this.guiScale = Integer.parseInt(tokens[1]);
					}

					if(tokens[0].equals("bobView")) {
						this.viewBobbing = tokens[1].equals("true");
					}

					if(tokens[0].equals("anaglyph3d")) {
						this.anaglyph = tokens[1].equals("true");
					}
					
					if(tokens[0].equals("clearWaters")) {
						this.clearWaters = tokens[1].equals("true");
					}

					if(tokens[0].equals("advancedOpengl")) {
						this.advancedOpengl = tokens[1].equals("true");
					}

					if(tokens[0].equals("fpsLimit")) {
						this.limitFramerate = Integer.parseInt(tokens[1]);
					}

					if(tokens[0].equals("difficulty")) {
						this.difficulty = Integer.parseInt(tokens[1]);
					}

					if(tokens[0].equals("fancyGraphics")) {
						this.fancyGraphics = tokens[1].equals("true");
						System.out.println("IsGrassFancy... ofGrass = " + this.ofGrass + ", fancyGraphics = " + this.fancyGraphics + " isGrassFaancy?" + Config.isGrassFancy());
						RenderBlocks.fancyGrass = Config.isGrassFancy();
					}

					if(tokens[0].equals("hand")) {
						this.retardedArm = tokens[1].equals("true");
					}
					
					if(tokens[0].equals("threadedLighting")) {
						this.threadedLighting = tokens[1].equals("true");
					}

					if(tokens[0].equals("	")) {
						this.ambientOcclusion = tokens[1].equals("true");
						if(this.ambientOcclusion) {
							this.ofAoLevel = 1.0F;
						} else {
							this.ofAoLevel = 0.0F;
						}
					}

					if(tokens[0].equals("skin")) {
						this.skin = tokens[1];
					}

					if(tokens[0].equals("lastServer") && tokens.length >= 2) {
						this.lastServer = tokens[1];
					}

					if(tokens[0].equals("FOV")) {
						this.FOV = Integer.parseInt(tokens[1]);
					}
					
					if(tokens[0].equals("gammaSetting")) {
						this.gammaSetting = this.parseFloat(tokens[1]);
					}
					
					if(tokens[0].equals("colouredAthmospherics")) {
						this.colouredAthmospherics = tokens[1].equals("true");
					}

					if(tokens[0].equals("displayMode") && tokens.length >= 2) {
						this.displayMode = tokens[1];
					}

					if(tokens[0].equals("meltBuild")) {
						this.meltBuild = tokens[1].equals("true");
					}

					for(int i4 = 0; i4 < this.keyBindings.length; ++i4) {
						if(tokens[0].equals("key_" + this.keyBindings[i4].keyDescription)) {
							this.keyBindings[i4].keyCode = Integer.parseInt(tokens[1]);
						}
					}
					

					if(tokens[0].equals("ofFogFancy") && tokens.length >= 2) {
						this.ofFogFancy = tokens[1].equals("true");
					}

					if(tokens[0].equals("ofFogStart") && tokens.length >= 2) {
						this.ofFogStart = Float.valueOf(tokens[1]).floatValue();
						if(this.ofFogStart < 0.2F) {
							this.ofFogStart = 0.2F;
						}

						if(this.ofFogStart > 0.81F) {
							this.ofFogStart = 0.8F;
						}
					}

					if(tokens[0].equals("ofMipmapLevel") && tokens.length >= 2) {
						this.ofMipmapLevel = Integer.valueOf(tokens[1]).intValue();
						if(this.ofMipmapLevel < 0) {
							this.ofMipmapLevel = 0;
						}

						if(this.ofMipmapLevel > 4) {
							this.ofMipmapLevel = 4;
						}
					}

					if(tokens[0].equals("ofMipmapLinear") && tokens.length >= 2) {
						this.ofMipmapLinear = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofLoadFar") && tokens.length >= 2) {
						this.ofLoadFar = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofPreloadedChunks") && tokens.length >= 2) {
						this.ofPreloadedChunks = Integer.valueOf(tokens[1]).intValue();
						if(this.ofPreloadedChunks < 0) {
							this.ofPreloadedChunks = 0;
						}

						if(this.ofPreloadedChunks > 8) {
							this.ofPreloadedChunks = 8;
						}
					}

					if(tokens[0].equals("ofOcclusionFancy") && tokens.length >= 2) {
						this.ofOcclusionFancy = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofSmoothFps") && tokens.length >= 2) {
						this.ofSmoothFps = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofSmoothInput") && tokens.length >= 2) {
						this.ofSmoothInput = Boolean.valueOf(tokens[1]).booleanValue();
					}

					/*
					if(tokens[0].equals("ofBrightness") && tokens.length >= 2) {
						this.ofBrightness = Float.valueOf(tokens[1]).floatValue();
						this.ofBrightness = Config.limit(this.ofBrightness, 0.0F, 1.0F);
						// this.updateWorldLightLevels();
					}
					*/

					if(tokens[0].equals("ofAoLevel") && tokens.length >= 2) {
						this.ofAoLevel = Float.valueOf(tokens[1]).floatValue();
						this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
						this.ambientOcclusion = this.ofAoLevel > 0.0F;
					}

					if(tokens[0].equals("ofClouds") && tokens.length >= 2) {
						this.ofClouds = Integer.valueOf(tokens[1]).intValue();
						this.ofClouds = Config.limit(this.ofClouds, 0, 3);
					}

					if(tokens[0].equals("ofCloudsHeight") && tokens.length >= 2) {
						this.ofCloudsHeight = Float.valueOf(tokens[1]).floatValue();
						this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
					}

					if(tokens[0].equals("ofTrees") && tokens.length >= 2) {
						this.ofTrees = Integer.valueOf(tokens[1]).intValue();
						this.ofTrees = Config.limit(this.ofTrees, 0, 2);
					}

					if(tokens[0].equals("ofGrass") && tokens.length >= 2) {
						this.ofGrass = Integer.valueOf(tokens[1]).intValue();
						this.ofGrass = Config.limit(this.ofGrass, 0, 2);
						RenderBlocks.fancyGrass = Config.isGrassFancy();
					}

					if(tokens[0].equals("ofRain") && tokens.length >= 2) {
						this.ofRain = Integer.valueOf(tokens[1]).intValue();
						this.ofRain = Config.limit(this.ofRain, 0, 3);
					}

					if(tokens[0].equals("ofWater") && tokens.length >= 2) {
						this.ofWater = Integer.valueOf(tokens[1]).intValue();
						this.ofWater = Config.limit(this.ofWater, 0, 3);
					}

					if(tokens[0].equals("ofAnimatedWater") && tokens.length >= 2) {
						this.ofAnimatedWater = Integer.valueOf(tokens[1]).intValue();
						this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
					}

					if(tokens[0].equals("ofAnimatedLava") && tokens.length >= 2) {
						this.ofAnimatedLava = Integer.valueOf(tokens[1]).intValue();
						this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
					}

					if(tokens[0].equals("ofAnimatedFire") && tokens.length >= 2) {
						this.ofAnimatedFire = Boolean.valueOf(tokens[1]).booleanValue();
					}
					
					if(tokens[0].equals("animatedTextures") && tokens.length >= 2) {
						this.animatedTextures = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofAnimatedPortal") && tokens.length >= 2) {
						this.ofAnimatedPortal = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofAnimatedRedstone") && tokens.length >= 2) {
						this.ofAnimatedRedstone = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofAnimatedExplosion") && tokens.length >= 2) {
						this.ofAnimatedExplosion = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofAnimatedFlame") && tokens.length >= 2) {
						this.ofAnimatedFlame = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofAnimatedSmoke") && tokens.length >= 2) {
						this.ofAnimatedSmoke = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofFastDebugInfo") && tokens.length >= 2) {
						this.ofFastDebugInfo = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofAutoSaveTicks") && tokens.length >= 2) {
						this.ofAutoSaveTicks = Integer.valueOf(tokens[1]).intValue();
						this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
					}

					if(tokens[0].equals("ofBetterGrass") && tokens.length >= 2) {
						this.ofBetterGrass = Integer.valueOf(tokens[1]).intValue();
						this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
					}

					if(tokens[0].equals("ofWeather") && tokens.length >= 2) {
						this.ofWeather = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofSky") && tokens.length >= 2) {
						this.ofSky = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofStars") && tokens.length >= 2) {
						this.ofStars = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofChunkUpdates") && tokens.length >= 2) {
						this.ofChunkUpdates = Integer.valueOf(tokens[1]).intValue();
						this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
					}

					if(tokens[0].equals("ofChunkUpdatesDynamic") && tokens.length >= 2) {
						this.ofChunkUpdatesDynamic = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofFarView") && tokens.length >= 2) {
						this.ofFarView = Boolean.valueOf(tokens[1]).booleanValue();
					}

					if(tokens[0].equals("ofTime") && tokens.length >= 2) {
						this.ofTime = Integer.valueOf(tokens[1]).intValue();
						this.ofTime = Config.limit(this.ofTime, 0, 2);
					}

					if(tokens[0].equals("ofClearWater") && tokens.length >= 2) {
						this.ofClearWater = Boolean.valueOf(tokens[1]).booleanValue();
						this.updateWaterOpacity();
					}
					
					if(tokens[0].equals("alphaTrees") && tokens.length >= 2) {
						this.alphaTrees = Boolean.valueOf(tokens[1]).booleanValue();
						BlockLog.lockTextures = this.alphaTrees;
						BlockLeaves.lockTextures = this.alphaTrees;
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
			PrintWriter pw = new PrintWriter(new FileWriter(this.optionsFile));
			pw.println("music:" + this.musicVolume);
			pw.println("sound:" + this.soundVolume);
			pw.println("invertYMouse:" + this.invertMouse);
			pw.println("mouseSensitivity:" + this.mouseSensitivity);
			pw.println("viewDistance:" + this.renderDistance);
			pw.println("guiScale:" + this.guiScale);
			pw.println("bobView:" + this.viewBobbing);
			pw.println("anaglyph3d:" + this.anaglyph);
			pw.println("advancedOpengl:" + this.advancedOpengl);
			pw.println("fpsLimit:" + this.limitFramerate);
			pw.println("difficulty:" + this.difficulty);
			pw.println("fancyGraphics:" + this.fancyGraphics);
			pw.println("hand:" + this.retardedArm);
			pw.println("threadedLighting:" + this.threadedLighting);
			pw.println("clearWaters:" + this.clearWaters);
			pw.println("ao:" + this.ambientOcclusion);
			pw.println("skin:" + this.skin);
			pw.println("lastServer:" + this.lastServer);
			pw.println("FOV:" + this.FOV);
			pw.println("gammaSetting" + this.gammaSetting);
			pw.println("colouredAthmospherics" + this.colouredAthmospherics);
			pw.println("displayMode:" + this.displayMode);
			pw.println("meltBuild:" + this.meltBuild);

			for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
				pw.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
			}

			pw.println("ofFogFancy:" + this.ofFogFancy);
			pw.println("ofFogStart:" + this.ofFogStart);
			pw.println("ofMipmapLevel:" + this.ofMipmapLevel);
			pw.println("ofMipmapLinear:" + this.ofMipmapLinear);
			pw.println("ofLoadFar:" + this.ofLoadFar);
			pw.println("ofPreloadedChunks:" + this.ofPreloadedChunks);
			pw.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
			pw.println("ofSmoothFps:" + this.ofSmoothFps);
			pw.println("ofSmoothInput:" + this.ofSmoothInput);
			pw.println("ofAoLevel:" + this.ofAoLevel);
			pw.println("ofClouds:" + this.ofClouds);
			pw.println("ofCloudsHeight:" + this.ofCloudsHeight);
			pw.println("ofTrees:" + this.ofTrees);
			pw.println("ofGrass:" + this.ofGrass);
			pw.println("ofRain:" + this.ofRain);
			pw.println("ofWater:" + this.ofWater);
			pw.println("ofAnimatedWater:" + this.ofAnimatedWater);
			pw.println("ofAnimatedLava:" + this.ofAnimatedLava);
			pw.println("ofAnimatedFire:" + this.ofAnimatedFire);
			pw.println("animatedTextures:" + this.animatedTextures);
			pw.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
			pw.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
			pw.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
			pw.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
			pw.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
			pw.println("ofFastDebugInfo:" + this.ofFastDebugInfo);
			pw.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
			pw.println("ofBetterGrass:" + this.ofBetterGrass);
			pw.println("ofWeather:" + this.ofWeather);
			pw.println("ofSky:" + this.ofSky);
			pw.println("ofStars:" + this.ofStars);
			pw.println("ofChunkUpdates:" + this.ofChunkUpdates);
			pw.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
			pw.println("ofFarView:" + this.ofFarView);
			pw.println("ofTime:" + this.ofTime);
			pw.println("ofClearWater:" + this.ofClearWater);
			pw.println("alphaTrees:" + this.alphaTrees);
			
			pw.close();
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
