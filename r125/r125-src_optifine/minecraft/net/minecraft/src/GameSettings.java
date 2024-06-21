package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

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
	public int ofRenderDistanceFine = 128;
	public int ofFogType = 1;
	public float ofFogStart = 0.8F;
	public int ofMipmapLevel = 0;
	public boolean ofMipmapLinear = false;
	public boolean ofLoadFar = false;
	public int ofPreloadedChunks = 0;
	public boolean ofOcclusionFancy = false;
	public boolean ofSmoothFps = false;
	public boolean ofSmoothInput = true;
	public float ofAoLevel = 1.0F;
	public int ofAaLevel = 0;
	public int ofAfLevel = 1;
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
	public boolean ofSunMoon = true;
	public int ofChunkUpdates = 1;
	public int ofChunkLoading = 0;
	public boolean ofChunkUpdatesDynamic = false;
	public int ofTime = 0;
	public boolean ofClearWater = false;
	public boolean ofDepthFog = true;
	public boolean ofProfiler = false;
	public boolean ofBetterSnow = false;
	public String ofFullscreenMode = "Default";
	public boolean ofSwampColors = true;
	public boolean ofRandomMobs = true;
	public boolean ofSmoothBiomes = true;
	public boolean ofCustomFonts = true;
	public boolean ofCustomColors = true;
	public boolean ofShowCapes = true;
	public int ofConnectedTextures = 2;
	public boolean ofNaturalTextures = false;
	public int ofAnimatedWater = 0;
	public int ofAnimatedLava = 0;
	public boolean ofAnimatedFire = true;
	public boolean ofAnimatedPortal = true;
	public boolean ofAnimatedRedstone = true;
	public boolean ofAnimatedExplosion = true;
	public boolean ofAnimatedFlame = true;
	public boolean ofAnimatedSmoke = true;
	public boolean ofVoidParticles = true;
	public boolean ofWaterParticles = true;
	public boolean ofRainSplash = true;
	public boolean ofPortalParticles = true;
	public boolean ofDrippingWaterLava = true;
	public boolean ofAnimatedTerrain = true;
	public boolean ofAnimatedItems = true;
	public boolean ofAnimatedTextures = true;
	public static final int DEFAULT = 0;
	public static final int FAST = 1;
	public static final int FANCY = 2;
	public static final int OFF = 3;
	public static final int ANIM_ON = 0;
	public static final int ANIM_GENERATED = 1;
	public static final int ANIM_OFF = 2;
	public static final int CL_DEFAULT = 0;
	public static final int CL_SMOOTH = 1;
	public static final int CL_THREADED = 2;
	public static final String DEFAULT_STR = "Default";
	public KeyBinding ofKeyBindZoom;
	public String skin = "Default";
	public KeyBinding keyBindForward;
	public KeyBinding keyBindLeft;
	public KeyBinding keyBindBack;
	public KeyBinding keyBindRight;
	public KeyBinding keyBindJump;
	public KeyBinding keyBindInventory;
	public KeyBinding keyBindDrop;
	public KeyBinding keyBindChat;
	public KeyBinding keyBindSneak;
	public KeyBinding keyBindAttack;
	public KeyBinding keyBindUseItem;
	public KeyBinding keyBindPlayerList;
	public KeyBinding keyBindPickBlock;
	public KeyBinding[] keyBindings;
	protected Minecraft mc;
	private File optionsFile;
	public int difficulty;
	public boolean hideGUI;
	public int thirdPersonView;
	public boolean showDebugInfo;
	public boolean field_50119_G;
	public String lastServer;
	public boolean noclip;
	public boolean smoothCamera;
	public boolean debugCamEnable;
	public float noclipRate;
	public float debugCamRate;
	public float fovSetting;
	public float gammaSetting;
	public int guiScale;
	public int particleSetting;
	public String language;
	private File optionsFileOF;

	public GameSettings(Minecraft par1Minecraft, File par2File) {
		this.renderDistance = 1;
		this.limitFramerate = 0;
		this.keyBindForward = new KeyBinding("key.forward", 17);
		this.keyBindLeft = new KeyBinding("key.left", 30);
		this.keyBindBack = new KeyBinding("key.back", 31);
		this.keyBindRight = new KeyBinding("key.right", 32);
		this.keyBindJump = new KeyBinding("key.jump", 57);
		this.keyBindInventory = new KeyBinding("key.inventory", 18);
		this.keyBindDrop = new KeyBinding("key.drop", 16);
		this.keyBindChat = new KeyBinding("key.chat", 20);
		this.keyBindSneak = new KeyBinding("key.sneak", 42);
		this.keyBindAttack = new KeyBinding("key.attack", -100);
		this.keyBindUseItem = new KeyBinding("key.use", -99);
		this.keyBindPlayerList = new KeyBinding("key.playerlist", 15);
		this.keyBindPickBlock = new KeyBinding("key.pickItem", -98);
		this.ofKeyBindZoom = new KeyBinding("Zoom", Keyboard.KEY_LCONTROL);
		this.keyBindings = new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.ofKeyBindZoom};
		this.difficulty = 2;
		this.hideGUI = false;
		this.thirdPersonView = 0;
		this.showDebugInfo = false;
		this.field_50119_G = false;
		this.lastServer = "";
		this.noclip = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.noclipRate = 1.0F;
		this.debugCamRate = 1.0F;
		this.fovSetting = 0.0F;
		this.gammaSetting = 0.0F;
		this.guiScale = 0;
		this.particleSetting = 0;
		this.language = "en_US";
		this.mc = par1Minecraft;
		this.optionsFile = new File(par2File, "options.txt");
		this.optionsFileOF = new File(par2File, "optionsof.txt");
		this.loadOptions();
		Config.setGameSettings(this);
	}

	public GameSettings() {
		this.renderDistance = 1;
		this.limitFramerate = 0;
		this.keyBindForward = new KeyBinding("key.forward", 17);
		this.keyBindLeft = new KeyBinding("key.left", 30);
		this.keyBindBack = new KeyBinding("key.back", 31);
		this.keyBindRight = new KeyBinding("key.right", 32);
		this.keyBindJump = new KeyBinding("key.jump", 57);
		this.keyBindInventory = new KeyBinding("key.inventory", 18);
		this.keyBindDrop = new KeyBinding("key.drop", 16);
		this.keyBindChat = new KeyBinding("key.chat", 20);
		this.keyBindSneak = new KeyBinding("key.sneak", 42);
		this.keyBindAttack = new KeyBinding("key.attack", -100);
		this.keyBindUseItem = new KeyBinding("key.use", -99);
		this.keyBindPlayerList = new KeyBinding("key.playerlist", 15);
		this.keyBindPickBlock = new KeyBinding("key.pickItem", -98);
		this.keyBindings = new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock};
		this.difficulty = 2;
		this.hideGUI = false;
		this.thirdPersonView = 0;
		this.showDebugInfo = false;
		this.field_50119_G = false;
		this.lastServer = "";
		this.noclip = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.noclipRate = 1.0F;
		this.debugCamRate = 1.0F;
		this.fovSetting = 0.0F;
		this.gammaSetting = 0.0F;
		this.guiScale = 0;
		this.particleSetting = 0;
		this.language = "en_US";
		Config.setGameSettings(this);
	}

	public String getKeyBindingDescription(int par1) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		return stringtranslate.translateKey(this.keyBindings[par1].keyDescription);
	}

	public String getOptionDisplayString(int par1) {
		int i = this.keyBindings[par1].keyCode;
		return getKeyDisplayString(i);
	}

	public static String getKeyDisplayString(int par0) {
		return par0 < 0 ? StatCollector.translateToLocalFormatted("key.mouseButton", new Object[]{par0 + 101}) : Keyboard.getKeyName(par0);
	}

	public void setKeyBinding(int par1, int par2) {
		this.keyBindings[par1].keyCode = par2;
		this.saveOptions();
	}

	public void setOptionFloatValue(EnumOptions par1EnumOptions, float par2) {
		if(par1EnumOptions == EnumOptions.MUSIC) {
			this.musicVolume = par2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(par1EnumOptions == EnumOptions.SOUND) {
			this.soundVolume = par2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(par1EnumOptions == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = par2;
		}

		if(par1EnumOptions == EnumOptions.FOV) {
			this.fovSetting = par2;
		}

		if(par1EnumOptions == EnumOptions.GAMMA) {
			this.gammaSetting = par2;
		}

		if(par1EnumOptions == EnumOptions.CLOUD_HEIGHT) {
			this.ofCloudsHeight = par2;
		}

		if(par1EnumOptions == EnumOptions.AO_LEVEL) {
			this.ofAoLevel = par2;
			this.ambientOcclusion = this.ofAoLevel > 0.0F;
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.RENDER_DISTANCE_FINE) {
			this.ofRenderDistanceFine = 32 + (int)(par2 * 480.0F);
			this.ofRenderDistanceFine = this.ofRenderDistanceFine >> 4 << 4;
			this.ofRenderDistanceFine = Config.limit(this.ofRenderDistanceFine, 32, 512);
			this.renderDistance = 3;
			if(this.ofRenderDistanceFine > 32) {
				this.renderDistance = 2;
			}

			if(this.ofRenderDistanceFine > 64) {
				this.renderDistance = 1;
			}

			if(this.ofRenderDistanceFine > 128) {
				this.renderDistance = 0;
			}

			this.mc.renderGlobal.loadRenderers();
		}

	}

	private void updateWaterOpacity() {
		byte opacity = 3;
		if(this.ofClearWater) {
			opacity = 1;
		}

		Block.waterStill.setLightOpacity(opacity);
		Block.waterMoving.setLightOpacity(opacity);
		if(this.mc.theWorld != null) {
			IChunkProvider cp = this.mc.theWorld.chunkProvider;
			if(cp != null) {
				for(int x = -512; x < 512; ++x) {
					for(int z = -512; z < 512; ++z) {
						if(cp.chunkExists(x, z)) {
							Chunk c = cp.provideChunk(x, z);
							if(c != null && !(c instanceof EmptyChunk)) {
								ExtendedBlockStorage[] ebss = c.getBlockStorageArray();

								for(int i = 0; i < ebss.length; ++i) {
									ExtendedBlockStorage ebs = ebss[i];
									if(ebs != null) {
										NibbleArray na = ebs.getSkylightArray();
										if(na != null) {
											byte[] data = na.data;

											for(int d = 0; d < data.length; ++d) {
												data[d] = 0;
											}
										}
									}
								}

								c.generateSkylightMap();
							}
						}
					}
				}

				this.mc.renderGlobal.loadRenderers();
			}
		}
	}

	public void updateChunkLoading() {
		switch(this.ofChunkLoading) {
		case 1:
			WrUpdates.setWrUpdater(new WrUpdaterSmooth());
			break;
		case 2:
			WrUpdates.setWrUpdater(new WrUpdaterThreaded());
			break;
		default:
			WrUpdates.setWrUpdater((IWrUpdater)null);
		}

		this.mc.renderGlobal.loadRenderers();
	}

	public void setAllAnimations(boolean flag) {
		int animVal = flag ? 0 : 2;
		this.ofAnimatedWater = animVal;
		this.ofAnimatedLava = animVal;
		this.ofAnimatedFire = flag;
		this.ofAnimatedPortal = flag;
		this.ofAnimatedRedstone = flag;
		this.ofAnimatedExplosion = flag;
		this.ofAnimatedFlame = flag;
		this.ofAnimatedSmoke = flag;
		this.ofVoidParticles = flag;
		this.ofWaterParticles = flag;
		this.ofRainSplash = flag;
		this.ofPortalParticles = flag;
		this.particleSetting = flag ? 0 : 2;
		this.ofDrippingWaterLava = flag;
		this.ofAnimatedTerrain = flag;
		this.ofAnimatedItems = flag;
		this.ofAnimatedTextures = flag;
		this.mc.renderEngine.refreshTextures();
	}

	public void setOptionValue(EnumOptions par1EnumOptions, int par2) {
		if(par1EnumOptions == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if(par1EnumOptions == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + par2 & 3;
			this.ofRenderDistanceFine = 32 << 3 - this.renderDistance;
		}

		if(par1EnumOptions == EnumOptions.GUI_SCALE) {
			this.guiScale = this.guiScale + par2 & 3;
		}

		if(par1EnumOptions == EnumOptions.PARTICLES) {
			this.particleSetting = (this.particleSetting + par2) % 3;
		}

		if(par1EnumOptions == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(par1EnumOptions == EnumOptions.RENDER_CLOUDS) {
			this.clouds = !this.clouds;
		}

		if(par1EnumOptions == EnumOptions.ADVANCED_OPENGL) {
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

			this.mc.renderGlobal.setAllRenderersVisible();
		}

		if(par1EnumOptions == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.FRAMERATE_LIMIT) {
			this.limitFramerate = (this.limitFramerate + par2) % 4;
			Display.setVSyncEnabled(this.limitFramerate == 3);
		}

		if(par1EnumOptions == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + par2 & 3;
		}

		if(par1EnumOptions == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = !this.ambientOcclusion;
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.FOG_FANCY) {
			switch(this.ofFogType) {
			case 1:
				this.ofFogType = 2;
				if(!Config.isFancyFogAvailable()) {
					this.ofFogType = 3;
				}
				break;
			case 2:
				this.ofFogType = 3;
				break;
			case 3:
				this.ofFogType = 1;
				break;
			default:
				this.ofFogType = 1;
			}
		}

		if(par1EnumOptions == EnumOptions.FOG_START) {
			this.ofFogStart += 0.2F;
			if(this.ofFogStart > 0.81F) {
				this.ofFogStart = 0.2F;
			}
		}

		if(par1EnumOptions == EnumOptions.MIPMAP_LEVEL) {
			++this.ofMipmapLevel;
			if(this.ofMipmapLevel > 4) {
				this.ofMipmapLevel = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.MIPMAP_TYPE) {
			this.ofMipmapLinear = !this.ofMipmapLinear;
			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.LOAD_FAR) {
			this.ofLoadFar = !this.ofLoadFar;
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.PRELOADED_CHUNKS) {
			this.ofPreloadedChunks += 2;
			if(this.ofPreloadedChunks > 8) {
				this.ofPreloadedChunks = 0;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.SMOOTH_FPS) {
			this.ofSmoothFps = !this.ofSmoothFps;
		}

		if(par1EnumOptions == EnumOptions.SMOOTH_INPUT) {
			this.ofSmoothInput = !this.ofSmoothInput;
		}

		if(par1EnumOptions == EnumOptions.CLOUDS) {
			++this.ofClouds;
			if(this.ofClouds > 3) {
				this.ofClouds = 0;
			}
		}

		if(par1EnumOptions == EnumOptions.TREES) {
			++this.ofTrees;
			if(this.ofTrees > 2) {
				this.ofTrees = 0;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.GRASS) {
			++this.ofGrass;
			if(this.ofGrass > 2) {
				this.ofGrass = 0;
			}

			RenderBlocks.fancyGrass = Config.isGrassFancy();
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.RAIN) {
			++this.ofRain;
			if(this.ofRain > 3) {
				this.ofRain = 0;
			}
		}

		if(par1EnumOptions == EnumOptions.WATER) {
			++this.ofWater;
			if(this.ofWater > 2) {
				this.ofWater = 0;
			}
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_WATER) {
			++this.ofAnimatedWater;
			if(this.ofAnimatedWater > 2) {
				this.ofAnimatedWater = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_LAVA) {
			++this.ofAnimatedLava;
			if(this.ofAnimatedLava > 2) {
				this.ofAnimatedLava = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_FIRE) {
			this.ofAnimatedFire = !this.ofAnimatedFire;
			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_PORTAL) {
			this.ofAnimatedPortal = !this.ofAnimatedPortal;
			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_REDSTONE) {
			this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_EXPLOSION) {
			this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_FLAME) {
			this.ofAnimatedFlame = !this.ofAnimatedFlame;
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_SMOKE) {
			this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
		}

		if(par1EnumOptions == EnumOptions.VOID_PARTICLES) {
			this.ofVoidParticles = !this.ofVoidParticles;
		}

		if(par1EnumOptions == EnumOptions.WATER_PARTICLES) {
			this.ofWaterParticles = !this.ofWaterParticles;
		}

		if(par1EnumOptions == EnumOptions.PORTAL_PARTICLES) {
			this.ofPortalParticles = !this.ofPortalParticles;
		}

		if(par1EnumOptions == EnumOptions.DRIPPING_WATER_LAVA) {
			this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_TERRAIN) {
			this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_TEXTURES) {
			this.ofAnimatedTextures = !this.ofAnimatedTextures;
		}

		if(par1EnumOptions == EnumOptions.ANIMATED_ITEMS) {
			this.ofAnimatedItems = !this.ofAnimatedItems;
			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.RAIN_SPLASH) {
			this.ofRainSplash = !this.ofRainSplash;
		}

		if(par1EnumOptions == EnumOptions.FAST_DEBUG_INFO) {
			this.ofFastDebugInfo = !this.ofFastDebugInfo;
		}

		if(par1EnumOptions == EnumOptions.AUTOSAVE_TICKS) {
			this.ofAutoSaveTicks *= 10;
			if(this.ofAutoSaveTicks > 40000) {
				this.ofAutoSaveTicks = 40;
			}
		}

		if(par1EnumOptions == EnumOptions.BETTER_GRASS) {
			++this.ofBetterGrass;
			if(this.ofBetterGrass > 3) {
				this.ofBetterGrass = 1;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.CONNECTED_TEXTURES) {
			++this.ofConnectedTextures;
			if(this.ofConnectedTextures > 3) {
				this.ofConnectedTextures = 1;
			}

			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.WEATHER) {
			this.ofWeather = !this.ofWeather;
		}

		if(par1EnumOptions == EnumOptions.SKY) {
			this.ofSky = !this.ofSky;
		}

		if(par1EnumOptions == EnumOptions.STARS) {
			this.ofStars = !this.ofStars;
		}

		if(par1EnumOptions == EnumOptions.SUN_MOON) {
			this.ofSunMoon = !this.ofSunMoon;
		}

		if(par1EnumOptions == EnumOptions.CHUNK_UPDATES) {
			++this.ofChunkUpdates;
			if(this.ofChunkUpdates > 5) {
				this.ofChunkUpdates = 1;
			}
		}

		if(par1EnumOptions == EnumOptions.CHUNK_LOADING) {
			++this.ofChunkLoading;
			if(this.ofChunkLoading > 2) {
				this.ofChunkLoading = 0;
			}

			this.updateChunkLoading();
		}

		if(par1EnumOptions == EnumOptions.CHUNK_UPDATES_DYNAMIC) {
			this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
		}

		if(par1EnumOptions == EnumOptions.TIME) {
			++this.ofTime;
			if(this.ofTime > 3) {
				this.ofTime = 0;
			}
		}

		if(par1EnumOptions == EnumOptions.CLEAR_WATER) {
			this.ofClearWater = !this.ofClearWater;
			this.updateWaterOpacity();
		}

		if(par1EnumOptions == EnumOptions.DEPTH_FOG) {
			this.ofDepthFog = !this.ofDepthFog;
		}

		if(par1EnumOptions == EnumOptions.AA_LEVEL) {
			int[] modeList = new int[]{0, 2, 4, 6, 8, 12, 16};
			boolean index = false;

			for(int l = 0; l < modeList.length - 1; ++l) {
				if(this.ofAaLevel == modeList[l]) {
					this.ofAaLevel = modeList[l + 1];
					index = true;
					break;
				}
			}

			if(!index) {
				this.ofAaLevel = 0;
			}
		}

		if(par1EnumOptions == EnumOptions.AF_LEVEL) {
			this.ofAfLevel *= 2;
			if(this.ofAfLevel > 16) {
				this.ofAfLevel = 1;
			}

			this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
			this.mc.renderEngine.refreshTextures();
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.PROFILER) {
			this.ofProfiler = !this.ofProfiler;
		}

		if(par1EnumOptions == EnumOptions.BETTER_SNOW) {
			this.ofBetterSnow = !this.ofBetterSnow;
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.SWAMP_COLORS) {
			this.ofSwampColors = !this.ofSwampColors;
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.RANDOM_MOBS) {
			this.ofRandomMobs = !this.ofRandomMobs;
			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.SMOOTH_BIOMES) {
			this.ofSmoothBiomes = !this.ofSmoothBiomes;
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.CUSTOM_FONTS) {
			this.ofCustomFonts = !this.ofCustomFonts;
			this.mc.renderEngine.refreshTextures();
		}

		if(par1EnumOptions == EnumOptions.CUSTOM_COLORS) {
			this.ofCustomColors = !this.ofCustomColors;
			this.mc.renderEngine.refreshTextures();
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.SHOW_CAPES) {
			this.ofShowCapes = !this.ofShowCapes;
			this.mc.renderGlobal.updateCapes();
		}

		if(par1EnumOptions == EnumOptions.NATURAL_TEXTURES) {
			this.ofNaturalTextures = !this.ofNaturalTextures;
			this.mc.renderEngine.refreshTextures();
			this.mc.renderGlobal.loadRenderers();
		}

		if(par1EnumOptions == EnumOptions.FULLSCREEN_MODE) {
			List list6 = Arrays.asList(Config.getFullscreenModes());
			if(this.ofFullscreenMode.equals("Default")) {
				this.ofFullscreenMode = (String)list6.get(0);
			} else {
				int i7 = list6.indexOf(this.ofFullscreenMode);
				if(i7 < 0) {
					this.ofFullscreenMode = "Default";
				} else {
					++i7;
					if(i7 >= list6.size()) {
						this.ofFullscreenMode = "Default";
					} else {
						this.ofFullscreenMode = (String)list6.get(i7);
					}
				}
			}
		}

		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions par1EnumOptions) {
		return par1EnumOptions == EnumOptions.FOV ? this.fovSetting : (par1EnumOptions == EnumOptions.GAMMA ? this.gammaSetting : (par1EnumOptions == EnumOptions.MUSIC ? this.musicVolume : (par1EnumOptions == EnumOptions.SOUND ? this.soundVolume : (par1EnumOptions == EnumOptions.SENSITIVITY ? this.mouseSensitivity : (par1EnumOptions == EnumOptions.CLOUD_HEIGHT ? this.ofCloudsHeight : (par1EnumOptions == EnumOptions.AO_LEVEL ? this.ofAoLevel : (par1EnumOptions == EnumOptions.RENDER_DISTANCE_FINE ? (float)(this.ofRenderDistanceFine - 32) / 480.0F : 0.0F)))))));
	}

	public boolean getOptionOrdinalValue(EnumOptions par1EnumOptions) {
		switch(EnumOptionsMappingHelper.enumOptionsMappingHelperArray[par1EnumOptions.ordinal()]) {
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

	private static String func_48571_a(String[] par0ArrayOfStr, int par1) {
		if(par1 < 0 || par1 >= par0ArrayOfStr.length) {
			par1 = 0;
		}

		StringTranslate stringtranslate = StringTranslate.getInstance();
		return stringtranslate.translateKey(par0ArrayOfStr[par1]);
	}

	public String getKeyBinding(EnumOptions par1EnumOptions) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		String prefix = stringtranslate.translateKey(par1EnumOptions.getEnumString());
		if(prefix == null) {
			prefix = par1EnumOptions.getEnumString();
		}

		String s = prefix + ": ";
		if(par1EnumOptions.getEnumFloat()) {
			float flag1 = this.getOptionFloatValue(par1EnumOptions);
			if(par1EnumOptions == EnumOptions.SENSITIVITY) {
				return flag1 == 0.0F ? s + stringtranslate.translateKey("options.sensitivity.min") : (flag1 == 1.0F ? s + stringtranslate.translateKey("options.sensitivity.max") : s + (int)(flag1 * 200.0F) + "%");
			} else if(par1EnumOptions == EnumOptions.FOV) {
				return flag1 == 0.0F ? s + stringtranslate.translateKey("options.fov.min") : (flag1 == 1.0F ? s + stringtranslate.translateKey("options.fov.max") : s + (int)(70.0F + flag1 * 40.0F));
			} else if(par1EnumOptions == EnumOptions.GAMMA) {
				return flag1 == 0.0F ? s + stringtranslate.translateKey("options.gamma.min") : (flag1 == 1.0F ? s + stringtranslate.translateKey("options.gamma.max") : s + "+" + (int)(flag1 * 100.0F) + "%");
			} else if(par1EnumOptions == EnumOptions.RENDER_DISTANCE_FINE) {
				String str = "Tiny";
				short baseDist = 32;
				if(this.ofRenderDistanceFine >= 64) {
					str = "Short";
					baseDist = 64;
				}

				if(this.ofRenderDistanceFine >= 128) {
					str = "Normal";
					baseDist = 128;
				}

				if(this.ofRenderDistanceFine >= 256) {
					str = "Far";
					baseDist = 256;
				}

				if(this.ofRenderDistanceFine >= 512) {
					str = "Extreme";
					baseDist = 512;
				}

				int diff = this.ofRenderDistanceFine - baseDist;
				return diff == 0 ? s + str : s + str + " +" + diff;
			} else {
				return flag1 == 0.0F ? s + stringtranslate.translateKey("options.off") : s + (int)(flag1 * 100.0F) + "%";
			}
		} else if(par1EnumOptions == EnumOptions.ADVANCED_OPENGL) {
			return !this.advancedOpengl ? s + "OFF" : (this.ofOcclusionFancy ? s + "Fancy" : s + "Fast");
		} else if(par1EnumOptions.getEnumBoolean()) {
			boolean flag = this.getOptionOrdinalValue(par1EnumOptions);
			return flag ? s + stringtranslate.translateKey("options.on") : s + stringtranslate.translateKey("options.off");
		} else if(par1EnumOptions == EnumOptions.RENDER_DISTANCE) {
			return s + func_48571_a(RENDER_DISTANCES, this.renderDistance);
		} else if(par1EnumOptions == EnumOptions.DIFFICULTY) {
			return s + func_48571_a(DIFFICULTIES, this.difficulty);
		} else if(par1EnumOptions == EnumOptions.GUI_SCALE) {
			return s + func_48571_a(GUISCALES, this.guiScale);
		} else if(par1EnumOptions == EnumOptions.PARTICLES) {
			return s + func_48571_a(PARTICLES, this.particleSetting);
		} else if(par1EnumOptions == EnumOptions.FRAMERATE_LIMIT) {
			return this.limitFramerate == 3 ? s + "VSync" : s + func_48571_a(LIMIT_FRAMERATES, this.limitFramerate);
		} else if(par1EnumOptions == EnumOptions.FOG_FANCY) {
			switch(this.ofFogType) {
			case 1:
				return s + "Fast";
			case 2:
				return s + "Fancy";
			case 3:
				return s + "OFF";
			default:
				return s + "OFF";
			}
		} else if(par1EnumOptions == EnumOptions.FOG_START) {
			return s + this.ofFogStart;
		} else if(par1EnumOptions == EnumOptions.MIPMAP_LEVEL) {
			return this.ofMipmapLevel == 0 ? s + "OFF" : (this.ofMipmapLevel == 4 ? s + "Max" : s + this.ofMipmapLevel);
		} else if(par1EnumOptions == EnumOptions.MIPMAP_TYPE) {
			return this.ofMipmapLinear ? s + "Linear" : s + "Nearest";
		} else if(par1EnumOptions == EnumOptions.LOAD_FAR) {
			return this.ofLoadFar ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.PRELOADED_CHUNKS) {
			return this.ofPreloadedChunks == 0 ? s + "OFF" : s + this.ofPreloadedChunks;
		} else if(par1EnumOptions == EnumOptions.SMOOTH_FPS) {
			return this.ofSmoothFps ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.SMOOTH_INPUT) {
			return this.ofSmoothInput ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.CLOUDS) {
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
		} else if(par1EnumOptions == EnumOptions.TREES) {
			switch(this.ofTrees) {
			case 1:
				return s + "Fast";
			case 2:
				return s + "Fancy";
			default:
				return s + "Default";
			}
		} else if(par1EnumOptions == EnumOptions.GRASS) {
			switch(this.ofGrass) {
			case 1:
				return s + "Fast";
			case 2:
				return s + "Fancy";
			default:
				return s + "Default";
			}
		} else if(par1EnumOptions == EnumOptions.RAIN) {
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
		} else if(par1EnumOptions == EnumOptions.WATER) {
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
		} else if(par1EnumOptions == EnumOptions.ANIMATED_WATER) {
			switch(this.ofAnimatedWater) {
			case 1:
				return s + "Dynamic";
			case 2:
				return s + "OFF";
			default:
				return s + "ON";
			}
		} else if(par1EnumOptions == EnumOptions.ANIMATED_LAVA) {
			switch(this.ofAnimatedLava) {
			case 1:
				return s + "Dynamic";
			case 2:
				return s + "OFF";
			default:
				return s + "ON";
			}
		} else if(par1EnumOptions == EnumOptions.ANIMATED_FIRE) {
			return this.ofAnimatedFire ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.ANIMATED_PORTAL) {
			return this.ofAnimatedPortal ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.ANIMATED_REDSTONE) {
			return this.ofAnimatedRedstone ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.ANIMATED_EXPLOSION) {
			return this.ofAnimatedExplosion ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.ANIMATED_FLAME) {
			return this.ofAnimatedFlame ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.ANIMATED_SMOKE) {
			return this.ofAnimatedSmoke ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.VOID_PARTICLES) {
			return this.ofVoidParticles ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.WATER_PARTICLES) {
			return this.ofWaterParticles ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.PORTAL_PARTICLES) {
			return this.ofPortalParticles ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.DRIPPING_WATER_LAVA) {
			return this.ofDrippingWaterLava ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.ANIMATED_TERRAIN) {
			return this.ofAnimatedTerrain ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.ANIMATED_TEXTURES) {
			return this.ofAnimatedTextures ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.ANIMATED_ITEMS) {
			return this.ofAnimatedItems ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.RAIN_SPLASH) {
			return this.ofRainSplash ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.FAST_DEBUG_INFO) {
			return this.ofFastDebugInfo ? s + "ON" : s + "OFF";
		} else if(par1EnumOptions == EnumOptions.AUTOSAVE_TICKS) {
			return this.ofAutoSaveTicks <= 40 ? s + "Default (2s)" : (this.ofAutoSaveTicks <= 400 ? s + "20s" : (this.ofAutoSaveTicks <= 4000 ? s + "3min" : s + "30min"));
		} else if(par1EnumOptions == EnumOptions.BETTER_GRASS) {
			switch(this.ofBetterGrass) {
			case 1:
				return s + "Fast";
			case 2:
				return s + "Fancy";
			default:
				return s + "OFF";
			}
		} else if(par1EnumOptions == EnumOptions.CONNECTED_TEXTURES) {
			switch(this.ofConnectedTextures) {
			case 1:
				return s + "Fast";
			case 2:
				return s + "Fancy";
			default:
				return s + "OFF";
			}
		} else {
			return par1EnumOptions == EnumOptions.WEATHER ? (this.ofWeather ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.SKY ? (this.ofSky ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.STARS ? (this.ofStars ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.SUN_MOON ? (this.ofSunMoon ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.CHUNK_UPDATES ? s + this.ofChunkUpdates : (par1EnumOptions == EnumOptions.CHUNK_LOADING ? (this.ofChunkLoading == 1 ? s + "Smooth" : (this.ofChunkLoading == 2 ? s + "Multi-Core" : s + "Default")) : (par1EnumOptions == EnumOptions.CHUNK_UPDATES_DYNAMIC ? (this.ofChunkUpdatesDynamic ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.TIME ? (this.ofTime == 1 ? s + "Day Only" : (this.ofTime == 3 ? s + "Night Only" : s + "Default")) : (par1EnumOptions == EnumOptions.CLEAR_WATER ? (this.ofClearWater ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.DEPTH_FOG ? (this.ofDepthFog ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.AA_LEVEL ? (this.ofAaLevel == 0 ? s + "OFF" : s + this.ofAaLevel) : (par1EnumOptions == EnumOptions.AF_LEVEL ? (this.ofAfLevel == 1 ? s + "OFF" : s + this.ofAfLevel) : (par1EnumOptions == EnumOptions.PROFILER ? (this.ofProfiler ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.BETTER_SNOW ? (this.ofBetterSnow ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.SWAMP_COLORS ? (this.ofSwampColors ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.RANDOM_MOBS ? (this.ofRandomMobs ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.SMOOTH_BIOMES ? (this.ofSmoothBiomes ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.CUSTOM_FONTS ? (this.ofCustomFonts ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.CUSTOM_COLORS ? (this.ofCustomColors ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.SHOW_CAPES ? (this.ofShowCapes ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.NATURAL_TEXTURES ? (this.ofNaturalTextures ? s + "ON" : s + "OFF") : (par1EnumOptions == EnumOptions.FULLSCREEN_MODE ? s + this.ofFullscreenMode : (par1EnumOptions == EnumOptions.GRAPHICS ? (this.fancyGraphics ? s + stringtranslate.translateKey("options.graphics.fancy") : s + stringtranslate.translateKey("options.graphics.fast")) : s))))))))))))))))))))));
		}
	}

	public void loadOptions() {
		try {
			if(!this.optionsFile.exists()) {
				return;
			}

			BufferedReader exception = new BufferedReader(new FileReader(this.optionsFile));
			String bufferedreader = "";

			while((bufferedreader = exception.readLine()) != null) {
				try {
					String[] s = bufferedreader.split(":");
					if(s[0].equals("music")) {
						this.musicVolume = this.parseFloat(s[1]);
					}

					if(s[0].equals("sound")) {
						this.soundVolume = this.parseFloat(s[1]);
					}

					if(s[0].equals("mouseSensitivity")) {
						this.mouseSensitivity = this.parseFloat(s[1]);
					}

					if(s[0].equals("fov")) {
						this.fovSetting = this.parseFloat(s[1]);
					}

					if(s[0].equals("gamma")) {
						this.gammaSetting = this.parseFloat(s[1]);
					}

					if(s[0].equals("invertYMouse")) {
						this.invertMouse = s[1].equals("true");
					}

					if(s[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(s[1]);
						this.ofRenderDistanceFine = 32 << 3 - this.renderDistance;
					}

					if(s[0].equals("guiScale")) {
						this.guiScale = Integer.parseInt(s[1]);
					}

					if(s[0].equals("particles")) {
						this.particleSetting = Integer.parseInt(s[1]);
					}

					if(s[0].equals("bobView")) {
						this.viewBobbing = s[1].equals("true");
					}

					if(s[0].equals("anaglyph3d")) {
						this.anaglyph = s[1].equals("true");
					}

					if(s[0].equals("advancedOpengl")) {
						this.advancedOpengl = s[1].equals("true");
					}

					if(s[0].equals("fpsLimit")) {
						this.limitFramerate = Integer.parseInt(s[1]);
						Display.setVSyncEnabled(this.limitFramerate == 3);
					}

					if(s[0].equals("difficulty")) {
						this.difficulty = Integer.parseInt(s[1]);
					}

					if(s[0].equals("fancyGraphics")) {
						this.fancyGraphics = s[1].equals("true");
					}

					if(s[0].equals("ao")) {
						this.ambientOcclusion = s[1].equals("true");
						if(this.ambientOcclusion) {
							this.ofAoLevel = 1.0F;
						} else {
							this.ofAoLevel = 0.0F;
						}
					}

					if(s[0].equals("clouds")) {
						this.clouds = s[1].equals("true");
					}

					if(s[0].equals("skin")) {
						this.skin = s[1];
					}

					if(s[0].equals("lastServer") && s.length >= 2) {
						this.lastServer = s[1];
					}

					if(s[0].equals("lang") && s.length >= 2) {
						this.language = s[1];
					}

					for(int exception1 = 0; exception1 < this.keyBindings.length; ++exception1) {
						if(s[0].equals("key_" + this.keyBindings[exception1].keyDescription)) {
							this.keyBindings[exception1].keyCode = Integer.parseInt(s[1]);
						}
					}
				} catch (Exception exception7) {
					System.out.println("Skipping bad option: " + bufferedreader);
				}
			}

			KeyBinding.resetKeyBindingArrayAndHash();
			exception.close();
		} catch (Exception exception8) {
			System.out.println("Failed to load options");
			exception8.printStackTrace();
		}

		try {
			File file9 = this.optionsFileOF;
			if(!file9.exists()) {
				file9 = this.optionsFile;
			}

			if(!file9.exists()) {
				return;
			}

			BufferedReader bufferedReader10 = new BufferedReader(new FileReader(file9));
			String string11 = "";

			while((string11 = bufferedReader10.readLine()) != null) {
				try {
					String[] string12 = string11.split(":");
					if(string12[0].equals("ofRenderDistanceFine") && string12.length >= 2) {
						this.ofRenderDistanceFine = Integer.valueOf(string12[1]).intValue();
						this.ofRenderDistanceFine = Config.limit(this.ofRenderDistanceFine, 32, 512);
					}

					if(string12[0].equals("ofFogType") && string12.length >= 2) {
						this.ofFogType = Integer.valueOf(string12[1]).intValue();
						this.ofFogType = Config.limit(this.ofFogType, 1, 3);
					}

					if(string12[0].equals("ofFogStart") && string12.length >= 2) {
						this.ofFogStart = Float.valueOf(string12[1]).floatValue();
						if(this.ofFogStart < 0.2F) {
							this.ofFogStart = 0.2F;
						}

						if(this.ofFogStart > 0.81F) {
							this.ofFogStart = 0.8F;
						}
					}

					if(string12[0].equals("ofMipmapLevel") && string12.length >= 2) {
						this.ofMipmapLevel = Integer.valueOf(string12[1]).intValue();
						if(this.ofMipmapLevel < 0) {
							this.ofMipmapLevel = 0;
						}

						if(this.ofMipmapLevel > 4) {
							this.ofMipmapLevel = 4;
						}
					}

					if(string12[0].equals("ofMipmapLinear") && string12.length >= 2) {
						this.ofMipmapLinear = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofLoadFar") && string12.length >= 2) {
						this.ofLoadFar = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofPreloadedChunks") && string12.length >= 2) {
						this.ofPreloadedChunks = Integer.valueOf(string12[1]).intValue();
						if(this.ofPreloadedChunks < 0) {
							this.ofPreloadedChunks = 0;
						}

						if(this.ofPreloadedChunks > 8) {
							this.ofPreloadedChunks = 8;
						}
					}

					if(string12[0].equals("ofOcclusionFancy") && string12.length >= 2) {
						this.ofOcclusionFancy = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofSmoothFps") && string12.length >= 2) {
						this.ofSmoothFps = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofSmoothInput") && string12.length >= 2) {
						this.ofSmoothInput = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAoLevel") && string12.length >= 2) {
						this.ofAoLevel = Float.valueOf(string12[1]).floatValue();
						this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
						this.ambientOcclusion = this.ofAoLevel > 0.0F;
					}

					if(string12[0].equals("ofClouds") && string12.length >= 2) {
						this.ofClouds = Integer.valueOf(string12[1]).intValue();
						this.ofClouds = Config.limit(this.ofClouds, 0, 3);
					}

					if(string12[0].equals("ofCloudsHeight") && string12.length >= 2) {
						this.ofCloudsHeight = Float.valueOf(string12[1]).floatValue();
						this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
					}

					if(string12[0].equals("ofTrees") && string12.length >= 2) {
						this.ofTrees = Integer.valueOf(string12[1]).intValue();
						this.ofTrees = Config.limit(this.ofTrees, 0, 2);
					}

					if(string12[0].equals("ofGrass") && string12.length >= 2) {
						this.ofGrass = Integer.valueOf(string12[1]).intValue();
						this.ofGrass = Config.limit(this.ofGrass, 0, 2);
					}

					if(string12[0].equals("ofRain") && string12.length >= 2) {
						this.ofRain = Integer.valueOf(string12[1]).intValue();
						this.ofRain = Config.limit(this.ofRain, 0, 3);
					}

					if(string12[0].equals("ofWater") && string12.length >= 2) {
						this.ofWater = Integer.valueOf(string12[1]).intValue();
						this.ofWater = Config.limit(this.ofWater, 0, 3);
					}

					if(string12[0].equals("ofAnimatedWater") && string12.length >= 2) {
						this.ofAnimatedWater = Integer.valueOf(string12[1]).intValue();
						this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
					}

					if(string12[0].equals("ofAnimatedLava") && string12.length >= 2) {
						this.ofAnimatedLava = Integer.valueOf(string12[1]).intValue();
						this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
					}

					if(string12[0].equals("ofAnimatedFire") && string12.length >= 2) {
						this.ofAnimatedFire = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAnimatedPortal") && string12.length >= 2) {
						this.ofAnimatedPortal = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAnimatedRedstone") && string12.length >= 2) {
						this.ofAnimatedRedstone = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAnimatedExplosion") && string12.length >= 2) {
						this.ofAnimatedExplosion = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAnimatedFlame") && string12.length >= 2) {
						this.ofAnimatedFlame = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAnimatedSmoke") && string12.length >= 2) {
						this.ofAnimatedSmoke = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofVoidParticles") && string12.length >= 2) {
						this.ofVoidParticles = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofWaterParticles") && string12.length >= 2) {
						this.ofWaterParticles = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofPortalParticles") && string12.length >= 2) {
						this.ofPortalParticles = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofDrippingWaterLava") && string12.length >= 2) {
						this.ofDrippingWaterLava = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAnimatedTerrain") && string12.length >= 2) {
						this.ofAnimatedTerrain = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAnimatedTextures") && string12.length >= 2) {
						this.ofAnimatedTextures = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAnimatedItems") && string12.length >= 2) {
						this.ofAnimatedItems = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofRainSplash") && string12.length >= 2) {
						this.ofRainSplash = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofFastDebugInfo") && string12.length >= 2) {
						this.ofFastDebugInfo = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAutoSaveTicks") && string12.length >= 2) {
						this.ofAutoSaveTicks = Integer.valueOf(string12[1]).intValue();
						this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
					}

					if(string12[0].equals("ofBetterGrass") && string12.length >= 2) {
						this.ofBetterGrass = Integer.valueOf(string12[1]).intValue();
						this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
					}

					if(string12[0].equals("ofConnectedTextures") && string12.length >= 2) {
						this.ofConnectedTextures = Integer.valueOf(string12[1]).intValue();
						this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
					}

					if(string12[0].equals("ofWeather") && string12.length >= 2) {
						this.ofWeather = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofSky") && string12.length >= 2) {
						this.ofSky = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofStars") && string12.length >= 2) {
						this.ofStars = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofSunMoon") && string12.length >= 2) {
						this.ofSunMoon = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofChunkUpdates") && string12.length >= 2) {
						this.ofChunkUpdates = Integer.valueOf(string12[1]).intValue();
						this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
					}

					if(string12[0].equals("ofChunkLoading") && string12.length >= 2) {
						this.ofChunkLoading = Integer.valueOf(string12[1]).intValue();
						this.ofChunkLoading = Config.limit(this.ofChunkLoading, 0, 2);
						this.updateChunkLoading();
					}

					if(string12[0].equals("ofChunkUpdatesDynamic") && string12.length >= 2) {
						this.ofChunkUpdatesDynamic = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofTime") && string12.length >= 2) {
						this.ofTime = Integer.valueOf(string12[1]).intValue();
						this.ofTime = Config.limit(this.ofTime, 0, 3);
					}

					if(string12[0].equals("ofClearWater") && string12.length >= 2) {
						this.ofClearWater = Boolean.valueOf(string12[1]).booleanValue();
						this.updateWaterOpacity();
					}

					if(string12[0].equals("ofDepthFog") && string12.length >= 2) {
						this.ofDepthFog = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofAaLevel") && string12.length >= 2) {
						this.ofAaLevel = Integer.valueOf(string12[1]).intValue();
						this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
					}

					if(string12[0].equals("ofAfLevel") && string12.length >= 2) {
						this.ofAfLevel = Integer.valueOf(string12[1]).intValue();
						this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
					}

					if(string12[0].equals("ofProfiler") && string12.length >= 2) {
						this.ofProfiler = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofBetterSnow") && string12.length >= 2) {
						this.ofBetterSnow = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofSwampColors") && string12.length >= 2) {
						this.ofSwampColors = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofRandomMobs") && string12.length >= 2) {
						this.ofRandomMobs = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofSmoothBiomes") && string12.length >= 2) {
						this.ofSmoothBiomes = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofCustomFonts") && string12.length >= 2) {
						this.ofCustomFonts = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofCustomColors") && string12.length >= 2) {
						this.ofCustomColors = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofShowCapes") && string12.length >= 2) {
						this.ofShowCapes = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofNaturalTextures") && string12.length >= 2) {
						this.ofNaturalTextures = Boolean.valueOf(string12[1]).booleanValue();
					}

					if(string12[0].equals("ofFullscreenMode") && string12.length >= 2) {
						this.ofFullscreenMode = string12[1];
					}
				} catch (Exception exception5) {
					System.out.println("Skipping bad option: " + string11);
				}
			}

			KeyBinding.resetKeyBindingArrayAndHash();
			bufferedReader10.close();
		} catch (Exception exception6) {
			System.out.println("Failed to load options");
			exception6.printStackTrace();
		}

	}

	private float parseFloat(String par1Str) {
		return par1Str.equals("true") ? 1.0F : (par1Str.equals("false") ? 0.0F : Float.parseFloat(par1Str));
	}

	public void saveOptions() {
		PrintWriter exception;
		try {
			exception = new PrintWriter(new FileWriter(this.optionsFile));
			exception.println("music:" + this.musicVolume);
			exception.println("sound:" + this.soundVolume);
			exception.println("invertYMouse:" + this.invertMouse);
			exception.println("mouseSensitivity:" + this.mouseSensitivity);
			exception.println("fov:" + this.fovSetting);
			exception.println("gamma:" + this.gammaSetting);
			exception.println("viewDistance:" + this.renderDistance);
			exception.println("guiScale:" + this.guiScale);
			exception.println("particles:" + this.particleSetting);
			exception.println("bobView:" + this.viewBobbing);
			exception.println("anaglyph3d:" + this.anaglyph);
			exception.println("advancedOpengl:" + this.advancedOpengl);
			exception.println("fpsLimit:" + this.limitFramerate);
			exception.println("difficulty:" + this.difficulty);
			exception.println("fancyGraphics:" + this.fancyGraphics);
			exception.println("ao:" + this.ambientOcclusion);
			exception.println("clouds:" + this.clouds);
			exception.println("skin:" + this.skin);
			exception.println("lastServer:" + this.lastServer);
			exception.println("lang:" + this.language);

			for(int i = 0; i < this.keyBindings.length; ++i) {
				exception.println("key_" + this.keyBindings[i].keyDescription + ":" + this.keyBindings[i].keyCode);
			}

			exception.close();
		} catch (Exception exception4) {
			System.out.println("Failed to save options");
			exception4.printStackTrace();
		}

		try {
			exception = new PrintWriter(new FileWriter(this.optionsFileOF));
			exception.println("ofRenderDistanceFine:" + this.ofRenderDistanceFine);
			exception.println("ofFogType:" + this.ofFogType);
			exception.println("ofFogStart:" + this.ofFogStart);
			exception.println("ofMipmapLevel:" + this.ofMipmapLevel);
			exception.println("ofMipmapLinear:" + this.ofMipmapLinear);
			exception.println("ofLoadFar:" + this.ofLoadFar);
			exception.println("ofPreloadedChunks:" + this.ofPreloadedChunks);
			exception.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
			exception.println("ofSmoothFps:" + this.ofSmoothFps);
			exception.println("ofSmoothInput:" + this.ofSmoothInput);
			exception.println("ofAoLevel:" + this.ofAoLevel);
			exception.println("ofClouds:" + this.ofClouds);
			exception.println("ofCloudsHeight:" + this.ofCloudsHeight);
			exception.println("ofTrees:" + this.ofTrees);
			exception.println("ofGrass:" + this.ofGrass);
			exception.println("ofRain:" + this.ofRain);
			exception.println("ofWater:" + this.ofWater);
			exception.println("ofAnimatedWater:" + this.ofAnimatedWater);
			exception.println("ofAnimatedLava:" + this.ofAnimatedLava);
			exception.println("ofAnimatedFire:" + this.ofAnimatedFire);
			exception.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
			exception.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
			exception.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
			exception.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
			exception.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
			exception.println("ofVoidParticles:" + this.ofVoidParticles);
			exception.println("ofWaterParticles:" + this.ofWaterParticles);
			exception.println("ofPortalParticles:" + this.ofPortalParticles);
			exception.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
			exception.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
			exception.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
			exception.println("ofAnimatedItems:" + this.ofAnimatedItems);
			exception.println("ofRainSplash:" + this.ofRainSplash);
			exception.println("ofFastDebugInfo:" + this.ofFastDebugInfo);
			exception.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
			exception.println("ofBetterGrass:" + this.ofBetterGrass);
			exception.println("ofConnectedTextures:" + this.ofConnectedTextures);
			exception.println("ofWeather:" + this.ofWeather);
			exception.println("ofSky:" + this.ofSky);
			exception.println("ofStars:" + this.ofStars);
			exception.println("ofSunMoon:" + this.ofSunMoon);
			exception.println("ofChunkUpdates:" + this.ofChunkUpdates);
			exception.println("ofChunkLoading:" + this.ofChunkLoading);
			exception.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
			exception.println("ofTime:" + this.ofTime);
			exception.println("ofClearWater:" + this.ofClearWater);
			exception.println("ofDepthFog:" + this.ofDepthFog);
			exception.println("ofAaLevel:" + this.ofAaLevel);
			exception.println("ofAfLevel:" + this.ofAfLevel);
			exception.println("ofProfiler:" + this.ofProfiler);
			exception.println("ofBetterSnow:" + this.ofBetterSnow);
			exception.println("ofSwampColors:" + this.ofSwampColors);
			exception.println("ofRandomMobs:" + this.ofRandomMobs);
			exception.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
			exception.println("ofCustomFonts:" + this.ofCustomFonts);
			exception.println("ofCustomColors:" + this.ofCustomColors);
			exception.println("ofShowCapes:" + this.ofShowCapes);
			exception.println("ofNaturalTextures:" + this.ofNaturalTextures);
			exception.println("ofFullscreenMode:" + this.ofFullscreenMode);
			exception.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}

	}

	public void resetSettings() {
		this.renderDistance = 1;
		this.viewBobbing = true;
		this.anaglyph = false;
		this.advancedOpengl = false;
		this.limitFramerate = 0;
		this.fancyGraphics = true;
		this.ambientOcclusion = true;
		this.clouds = true;
		this.fovSetting = 0.0F;
		this.gammaSetting = 0.0F;
		this.guiScale = 0;
		this.particleSetting = 0;
		this.ofRenderDistanceFine = 32 << 3 - this.renderDistance;
		this.ofFogType = 1;
		this.ofFogStart = 0.8F;
		this.ofMipmapLevel = 0;
		this.ofMipmapLinear = false;
		this.ofLoadFar = false;
		this.ofPreloadedChunks = 0;
		this.ofOcclusionFancy = false;
		this.ofSmoothFps = false;
		this.ofSmoothInput = true;
		if(this.ambientOcclusion) {
			this.ofAoLevel = 1.0F;
		} else {
			this.ofAaLevel = 0;
		}

		this.ofAaLevel = 0;
		this.ofAfLevel = 1;
		this.ofClouds = 0;
		this.ofCloudsHeight = 0.0F;
		this.ofTrees = 0;
		this.ofGrass = 0;
		this.ofRain = 0;
		this.ofWater = 0;
		this.ofBetterGrass = 3;
		this.ofAutoSaveTicks = 4000;
		this.ofFastDebugInfo = false;
		this.ofWeather = true;
		this.ofSky = true;
		this.ofStars = true;
		this.ofSunMoon = true;
		this.ofChunkUpdates = 1;
		this.ofChunkLoading = 0;
		this.ofChunkUpdatesDynamic = false;
		this.ofTime = 0;
		this.ofClearWater = false;
		this.ofDepthFog = true;
		this.ofProfiler = false;
		this.ofBetterSnow = false;
		this.ofFullscreenMode = "Default";
		this.ofSwampColors = true;
		this.ofRandomMobs = true;
		this.ofSmoothBiomes = true;
		this.ofCustomFonts = true;
		this.ofCustomColors = true;
		this.ofShowCapes = true;
		this.ofConnectedTextures = 2;
		this.ofNaturalTextures = false;
		this.ofAnimatedWater = 0;
		this.ofAnimatedLava = 0;
		this.ofAnimatedFire = true;
		this.ofAnimatedPortal = true;
		this.ofAnimatedRedstone = true;
		this.ofAnimatedExplosion = true;
		this.ofAnimatedFlame = true;
		this.ofAnimatedSmoke = true;
		this.ofVoidParticles = true;
		this.ofWaterParticles = true;
		this.ofRainSplash = true;
		this.ofPortalParticles = true;
		this.ofDrippingWaterLava = true;
		this.ofAnimatedTerrain = true;
		this.ofAnimatedItems = true;
		this.ofAnimatedTextures = true;
		this.mc.renderGlobal.updateCapes();
		this.updateWaterOpacity();
		this.mc.renderGlobal.setAllRenderersVisible();
		this.mc.renderEngine.refreshTextures();
		this.mc.renderGlobal.loadRenderers();
		this.saveOptions();
	}

	public boolean shouldRenderClouds() {
		return this.ofRenderDistanceFine > 64 && this.clouds;
	}
}
