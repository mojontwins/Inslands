package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import java.net.URL;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import com.misc.moreresources.MoreResourcesInstaller;

import net.minecraft.client.gui.EnumOptions;
import net.minecraft.client.gui.GameSettings;
import net.minecraft.client.gui.GuiAchievement;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConflictWarning;
import net.minecraft.client.gui.GuiConnecting;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiUnused;
import net.minecraft.client.gui.LoadingScreenRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.creative.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiCreativeInventory;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.aether.ModelFlyingCow2;
import net.minecraft.client.model.aether.ModelFlyingPig2;
import net.minecraft.client.multiplayer.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromOptions;
import net.minecraft.client.player.PlayerController;
import net.minecraft.client.player.PlayerControllerTest;
import net.minecraft.client.renderer.EffectRenderer;
import net.minecraft.client.renderer.FontRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.GraphicsMode;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MouseHelper;
import net.minecraft.client.renderer.OpenGlCapsChecker;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.ptexture.TextureAnimatedFX;
import net.minecraft.client.renderer.ptexture.TextureCompassFX;
import net.minecraft.client.renderer.ptexture.TextureFlamesFX;
import net.minecraft.client.renderer.ptexture.TextureLavaFX;
import net.minecraft.client.renderer.ptexture.TextureLavaFlowFX;
import net.minecraft.client.renderer.ptexture.TexturePortalFX;
import net.minecraft.client.renderer.ptexture.TextureWatchFX;
import net.minecraft.client.renderer.ptexture.TextureWaterFX;
import net.minecraft.client.renderer.ptexture.TextureWaterFlowFX;
import net.minecraft.client.skins.TexturePackList;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.title.GuiMainMenu;
import net.minecraft.network.packet.Packet14BlockDig;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftException;
import net.minecraft.world.GlobalVars;
import net.minecraft.world.Version;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.PlayerSaveData;
import net.minecraft.world.level.PortalRegistry;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldInfo;
import net.minecraft.world.level.WorldNameGen;
import net.minecraft.world.level.WorldSettings;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.biome.BiomeGenThemeForest;
import net.minecraft.world.level.biome.BiomeGenThemeHell;
import net.minecraft.world.level.biome.BiomeGenThemeParadise;
import net.minecraft.world.level.biome.BiomeGenThemeWhiteForest;
import net.minecraft.world.level.chunk.ChunkCoordinates;
import net.minecraft.world.level.chunk.ChunkProvider;
import net.minecraft.world.level.chunk.storage.ISaveFormat;
import net.minecraft.world.level.chunk.storage.ISaveHandler;
import net.minecraft.world.level.chunk.storage.SaveConverterMcRegion;
import net.minecraft.world.level.colorizer.ColorizerFoliage;
import net.minecraft.world.level.colorizer.ColorizerGrass;
import net.minecraft.world.level.colorizer.ColorizerWater;
import net.minecraft.world.level.dimension.Teleporter;
import net.minecraft.world.level.dimension.WorldProvider;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockFire;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.EnumMovingObjectType;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.stats.AchievementList;
import net.minecraft.world.stats.StatList;

public abstract class Minecraft implements Runnable {
	public static byte[] reservedBytes = new byte[10485760];
	private static Minecraft theMinecraft;
	public PlayerController playerController;
	private boolean fullscreen = false;
	private boolean hasCrashed = false;
	public int displayWidth;
	public int displayHeight;
	private OpenGlCapsChecker glCapabilities;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public LevelRenderer renderGlobal;
	public EntityPlayerSP thePlayer;
	public EntityLiving renderViewEntity;
	public EffectRenderer effectRenderer;
	public User session = null;
	public String minecraftUri;
	public Canvas mcCanvas;
	public boolean hideQuitButton = false;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public GameRenderer entityRenderer;
	private ThreadDownloadResources downloadResourcesThread;
	private int ticksRan = 0;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	public GuiAchievement guiAchievement = new GuiAchievement(this);
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public ModelBiped playerModelBiped = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings gameSettings;
	protected MinecraftApplet mcApplet;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public TexturePackList texturePackList;
	private File mcDataDir;
	private ISaveFormat saveLoader;
	public static long[] frameTimes = new long[512];
	public static long[] tickTimes = new long[512];
	public static int numRecordedFrameTimes = 0;
	public static long hasPaidCheckTime = 0L;
	public StatFileWriter statFileWriter;
	private String serverName;
	private int serverPort;
	private TextureWaterFX textureWaterFX = new TextureWaterFX();
	private TextureLavaFX textureLavaFX = new TextureLavaFX();
	private static File minecraftDir = null;
	public volatile boolean running = true;
	public String debug = "";
	boolean isTakingScreenshot = false;
	long prevFrameTime = -1L;
	public boolean inGameHasFocus = false;
	private int mouseTicksRan = 0;
	public boolean raining = false;
	long systemTime = System.currentTimeMillis();
	private int joinPlayerCounter = 0;

	// Keep this true for the time being
	public boolean legacyOpenGL = true;
	
	public Minecraft(Component component1, Canvas canvas2, MinecraftApplet minecraftApplet3, int i4, int i5, boolean z6) {
		StatList.preInit();
		this.tempDisplayHeight = i5;
		this.fullscreen = z6;
		this.mcApplet = minecraftApplet3;
		new ThreadSleepForeverClient(this, "Timer hack thread");
		this.mcCanvas = canvas2;
		this.displayWidth = i4;
		this.displayHeight = i5;
		this.fullscreen = z6;

		this.hideQuitButton = false;

		theMinecraft = this;
	}

	public static Minecraft getMinecraft() {
		return theMinecraft;
	}

	public void onMinecraftCrash(UnexpectedThrowable unexpectedThrowable1) {
		this.hasCrashed = true;
		this.displayUnexpectedThrowable(unexpectedThrowable1);
	}

	public abstract void displayUnexpectedThrowable(UnexpectedThrowable unexpectedThrowable1);

	public void setServer(String string1, int i2) {
		this.serverName = string1;
		this.serverPort = i2;
	}

	public void startGame() throws LWJGLException {
		System.setProperty("net.java.games.input.useDefaultPlugin", "false");
		System.out.println("Java library path: " + System.getProperty("java.library.path"));
		if(this.mcCanvas != null) {
			Graphics graphics1 = this.mcCanvas.getGraphics();
			if(graphics1 != null) {
				graphics1.setColor(Color.BLACK);
				graphics1.fillRect(0, 0, this.displayWidth, this.displayHeight);
				graphics1.dispose();
			}

			Display.setParent(this.mcCanvas);
		} else if(this.fullscreen) {
			Display.setFullscreen(true);
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();
			if(this.displayWidth <= 0) {
				this.displayWidth = 1;
			}

			if(this.displayHeight <= 0) {
				this.displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
		}

		Display.setTitle("Minecraft " + Version.getVersion());

		try {
			PixelFormat pixelFormat = new PixelFormat();
			pixelFormat = pixelFormat.withDepthBits(24);
			Display.create(pixelFormat);
		} catch (LWJGLException lWJGLException6) {
			lWJGLException6.printStackTrace();

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedException5) {
			}

			Display.create();
		}

		OpenGlHelper.initializeTextures();
		this.mcDataDir = getMinecraftDir();
		this.saveLoader = new SaveConverterMcRegion(new File(this.mcDataDir, "saves"));
		this.gameSettings = new GameSettings(this, this.mcDataDir);
		this.texturePackList = new TexturePackList(this, this.mcDataDir);
		this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
		this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine);
		
		// Init ramps
		ColorizerWater.setColorRamp(this.renderEngine.getTextureContents("/misc/watercolor.png"));
		ColorizerGrass.setColorRamp(this.renderEngine.getTextureContents("/misc/grasscolor.png"));
		ColorizerFoliage.setColorRamp(this.renderEngine.getTextureContents("/misc/foliagecolor.png"));
		
		this.entityRenderer = new GameRenderer(this);
		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		this.statFileWriter = new StatFileWriter(this.session, this.mcDataDir);
		AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
		this.loadScreen();
		Keyboard.create();
		Mouse.create();
		this.mouseHelper = new MouseHelper(this.mcCanvas);

		try {
			Controllers.create();
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		this.checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.checkGLError("Startup");
		this.glCapabilities = new OpenGlCapsChecker();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.renderEngine.registerTextureFX(this.textureLavaFX);
		this.renderEngine.registerTextureFX(this.textureWaterFX);
		this.renderEngine.registerTextureFX(new TexturePortalFX());
		this.renderEngine.registerTextureFX(new TextureCompassFX(this));
		this.renderEngine.registerTextureFX(new TextureWatchFX(this));
		this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
		this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
		this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
		this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
		
		// Custom texture atlas based animated textures
		this.renderEngine.registerTextureFX(new TextureAnimatedFX(15 * 16 + 12, 0, "/animated/block_seaweed.png", 1));
		this.renderEngine.registerTextureFX(new TextureAnimatedFX(9 * 16 + 6, 0, "/animated/block_acidwater.png", 1));
		
		this.renderGlobal = new LevelRenderer(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

		try {
			this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
			this.downloadResourcesThread.start();
		} catch (Exception exception3) {
		}

		MoreResourcesInstaller moreResourcesInstaller = new MoreResourcesInstaller(this);
		moreResourcesInstaller.installResources();

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		if(this.serverName != null) {
			this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
		} else {
			this.displayGuiScreen(new GuiMainMenu());
		}

	}

	private void loadScreen() throws LWJGLException {
		ScaledResolution scaledResolution1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledResolution1.scaledWidthD, scaledResolution1.scaledHeightD, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		Tessellator tessellator2 = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
		tessellator2.startDrawingQuads();
		tessellator2.setColorOpaque_I(0xFFFFFF);
		tessellator2.addVertexWithUV(0.0D, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator2.addVertexWithUV((double)this.displayWidth, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator2.addVertexWithUV((double)this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator2.draw();
		short s3 = 256;
		short s4 = 256;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator2.setColorOpaque_I(0xFFFFFF);
		this.scaledTessellator((scaledResolution1.getScaledWidth() - s3) / 2, (scaledResolution1.getScaledHeight() - s4) / 2, 0, 0, s3, s4);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		Display.swapBuffers();
	}

	public void scaledTessellator(int i1, int i2, int i3, int i4, int i5, int i6) {
		float f7 = 0.00390625F;
		float f8 = 0.00390625F;
		Tessellator tessellator9 = Tessellator.instance;
		tessellator9.startDrawingQuads();
		tessellator9.addVertexWithUV((double)(i1 + 0), (double)(i2 + i6), 0.0D, (double)((float)(i3 + 0) * f7), (double)((float)(i4 + i6) * f8));
		tessellator9.addVertexWithUV((double)(i1 + i5), (double)(i2 + i6), 0.0D, (double)((float)(i3 + i5) * f7), (double)((float)(i4 + i6) * f8));
		tessellator9.addVertexWithUV((double)(i1 + i5), (double)(i2 + 0), 0.0D, (double)((float)(i3 + i5) * f7), (double)((float)(i4 + 0) * f8));
		tessellator9.addVertexWithUV((double)(i1 + 0), (double)(i2 + 0), 0.0D, (double)((float)(i3 + 0) * f7), (double)((float)(i4 + 0) * f8));
		tessellator9.draw();
	}

	public static File getMinecraftDir() {
		if(minecraftDir == null) {
			minecraftDir = getAppDir("minecraft");
		}

		return minecraftDir;
	}

	public static File getAppDir(String string0) {
		String string1 = System.getProperty("user.home", ".");
		File file2;
		switch(getOs()) {
		case linux:
		case solaris:
			file2 = new File(string1, '.' + string0 + '/');
			break;
		case windows:
			String string3 = System.getenv("APPDATA");
			if(string3 != null) {
				file2 = new File(string3, "." + string0 + '/');
			} else {
				file2 = new File(string1, '.' + string0 + '/');
			}
			break;
		case macos:
			file2 = new File(string1, "Library/Application Support/" + string0);
			break;
		default:
			file2 = new File(string1, string0 + '/');
		}

		if(!file2.exists() && !file2.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + file2);
		} else {
			return file2;
		}
	}

	public static EnumOS2 getOs() {
		String string0 = System.getProperty("os.name").toLowerCase();
		return string0.contains("win") ? EnumOS2.windows : (string0.contains("mac") ? EnumOS2.macos : (string0.contains("solaris") ? EnumOS2.solaris : (string0.contains("sunos") ? EnumOS2.solaris : (string0.contains("linux") ? EnumOS2.linux : (string0.contains("unix") ? EnumOS2.linux : EnumOS2.unknown)))));
	}

	public ISaveFormat getSaveLoader() {
		return this.saveLoader;
	}

	public void displayGuiScreen(GuiScreen guiScreen1) {
		if(!(this.currentScreen instanceof GuiUnused)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(guiScreen1 instanceof GuiMainMenu) {
				this.statFileWriter.func_27175_b();
			}

			this.statFileWriter.syncStats();
			if(guiScreen1 == null && this.theWorld == null) {
				guiScreen1 = new GuiMainMenu();
			} else if(guiScreen1 == null && this.thePlayer.health <= 0) {
				guiScreen1 = new GuiGameOver();
			}

			if(guiScreen1 instanceof GuiMainMenu) {
				this.ingameGUI.clearChatMessages();
			}

			this.currentScreen = (GuiScreen)guiScreen1;
			if(guiScreen1 != null) {
				this.setIngameNotInFocus();
				ScaledResolution scaledResolution2 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
				int i3 = scaledResolution2.getScaledWidth();
				int i4 = scaledResolution2.getScaledHeight();
				((GuiScreen)guiScreen1).setWorldAndResolution(this, i3, i4);
				this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}

		}
	}

	public void checkGLError(String string1) {
		int i2 = GL11.glGetError();
		if(i2 != 0) {
			String string3 = GLU.gluErrorString(i2);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + string1);
			System.out.println(i2 + ": " + string3);
		}

	}

	public void shutdownMinecraftApplet() {
		try {
			this.statFileWriter.func_27175_b();
			this.statFileWriter.syncStats();
			if(this.mcApplet != null) {
				this.mcApplet.clearApplet();
			}

			try {
				if(this.downloadResourcesThread != null) {
					this.downloadResourcesThread.closeMinecraft();
				}
			} catch (Exception exception9) {
			}

			System.out.println("Stopping!");

			try {
				this.changeWorld((World)null);
			} catch (Throwable throwable8) {
			}

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Throwable throwable7) {
			}

			this.sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Display.destroy();
			if(!this.hasCrashed) {
				System.exit(0);
			}

		}

		System.gc();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Exception exception17) {
			exception17.printStackTrace();
			this.onMinecraftCrash(new UnexpectedThrowable("Failed to start game", exception17));
			return;
		}

		try {
			long j1 = System.currentTimeMillis();
			int i3 = 0;

			while(this.running) {
				try {
					if(this.mcApplet != null && !this.mcApplet.isActive()) {
						break;
					}

					AxisAlignedBB.clearBoundingBoxPool();
					Vec3D.initialize();
					if(this.mcCanvas == null && Display.isCloseRequested()) {
						this.shutdown();
					}

					if(this.isGamePaused && this.theWorld != null) {
						float f4 = this.timer.renderPartialTicks;
						this.timer.updateTimer();
						this.timer.renderPartialTicks = f4;
					} else {
						this.timer.updateTimer();
					}

					for(int i6 = 0; i6 < this.timer.elapsedTicks; ++i6) {
						++this.ticksRan;

						try {
							this.runTick();
						} catch (MinecraftException minecraftException16) {
							this.theWorld = null;
							this.changeWorld((World)null);
							this.displayGuiScreen(new GuiConflictWarning());
						}
					}

					this.checkGLError("Pre render");
					RenderBlocks.fancyGrass = Config.isGrassFancy() && LevelThemeGlobalSettings.colorizedPlants;
					this.sndManager.func_338_a(this.thePlayer, this.timer.renderPartialTicks);
					
					GL11.glEnable(GL11.GL_TEXTURE_2D);

					if(!Keyboard.isKeyDown(Keyboard.KEY_F7)) {
						Display.update();
					}

					if(this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
						this.gameSettings.thirdPersonView = false;
					}

					if(!this.skipRenderWorld) {
						if(this.playerController != null) {
							this.playerController.setPartialTime(this.timer.renderPartialTicks);
						}

						this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
					}

					if(!Display.isActive()) {
						if(this.fullscreen) {
							this.toggleFullscreen();
						}

						Thread.sleep(10L);
					}

					if(this.gameSettings.showDebugInfo) {
						//this.displayDebugInfo(j24);
					} else {
						this.prevFrameTime = System.nanoTime();
					}

					this.guiAchievement.updateAchievementWindow();
					Thread.yield();
					if(Keyboard.isKeyDown(Keyboard.KEY_F7)) {
						Display.update();
					}

					this.screenshotListener();
					if(this.mcCanvas != null && !this.fullscreen && (this.mcCanvas.getWidth() != this.displayWidth || this.mcCanvas.getHeight() != this.displayHeight)) {
						this.displayWidth = this.mcCanvas.getWidth();
						this.displayHeight = this.mcCanvas.getHeight();
						if(this.displayWidth <= 0) {
							this.displayWidth = 1;
						}

						if(this.displayHeight <= 0) {
							this.displayHeight = 1;
						}

						this.resize(this.displayWidth, this.displayHeight);
					}

					this.checkGLError("Post render");
					++i3;

					for(this.isGamePaused = !this.isRemote() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); System.currentTimeMillis() >= j1 + 1000L; i3 = 0) {
						this.debug = i3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
						WorldRenderer.chunksUpdated = 0;
						j1 += 1000L;
					}
				} catch (MinecraftException minecraftException18) {
					this.theWorld = null;
					this.changeWorld((World)null);
					this.displayGuiScreen(new GuiConflictWarning());
				} catch (OutOfMemoryError outOfMemoryError19) {
					this.freeMemory();
					this.displayGuiScreen(new GuiErrorScreen());
					System.gc();
				}
			}
		} catch (MinecraftError minecraftError20) {
		} catch (Throwable throwable21) {
			this.freeMemory();
			throwable21.printStackTrace();
			this.onMinecraftCrash(new UnexpectedThrowable("Unexpected error", throwable21));
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	public void freeMemory() {
		try {
			reservedBytes = new byte[0];
			this.renderGlobal.deleteDisplayLists();
		} catch (Throwable throwable4) {
		}

		try {
			System.gc();
			AxisAlignedBB.clearBoundingBoxes();
			Vec3D.clearVectorList();
		} catch (Throwable throwable3) {
		}

		try {
			System.gc();
			this.changeWorld((World)null);
		} catch (Throwable throwable2) {
		}

		System.gc();
	}

	private void screenshotListener() {
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			if(!this.isTakingScreenshot) {
				this.isTakingScreenshot = true;
				this.ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, this.displayWidth, this.displayHeight));
			}
		} else {
			this.isTakingScreenshot = false;
		}

	}

	public void displayDebugInfo(long j1) {
		long j3 = 16666666L;
		if(this.prevFrameTime == -1L) {
			this.prevFrameTime = System.nanoTime();
		}

		long j5 = System.nanoTime();
		tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = j1;
		frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = j5 - this.prevFrameTime;
		this.prevFrameTime = j5;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator7 = Tessellator.instance;
		tessellator7.startDrawing(7);
		int i8 = (int)(j3 / 200000L);
		tessellator7.setColorOpaque_I(536870912);
		tessellator7.addVertex(0.0D, (double)(this.displayHeight - i8), 0.0D);
		tessellator7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		tessellator7.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		tessellator7.addVertex((double)frameTimes.length, (double)(this.displayHeight - i8), 0.0D);
		tessellator7.setColorOpaque_I(0x20200000);
		tessellator7.addVertex(0.0D, (double)(this.displayHeight - i8 * 2), 0.0D);
		tessellator7.addVertex(0.0D, (double)(this.displayHeight - i8), 0.0D);
		tessellator7.addVertex((double)frameTimes.length, (double)(this.displayHeight - i8), 0.0D);
		tessellator7.addVertex((double)frameTimes.length, (double)(this.displayHeight - i8 * 2), 0.0D);
		tessellator7.draw();
		long j9 = 0L;

		int i11;
		for(i11 = 0; i11 < frameTimes.length; ++i11) {
			j9 += frameTimes[i11];
		}

		i11 = (int)(j9 / 200000L / (long)frameTimes.length);
		tessellator7.startDrawing(7);
		tessellator7.setColorOpaque_I(0x20400000);
		tessellator7.addVertex(0.0D, (double)(this.displayHeight - i11), 0.0D);
		tessellator7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		tessellator7.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		tessellator7.addVertex((double)frameTimes.length, (double)(this.displayHeight - i11), 0.0D);
		tessellator7.draw();
		tessellator7.startDrawing(1);

		for(int i12 = 0; i12 < frameTimes.length; ++i12) {
			int i13 = (i12 - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
			int i14 = i13 * i13 / 255;
			i14 = i14 * i14 / 255;
			int i15 = i14 * i14 / 255;
			i15 = i15 * i15 / 255;
			if(frameTimes[i12] > j3) {
				tessellator7.setColorOpaque_I(0xFF000000 + i14 * 65536);
			} else {
				tessellator7.setColorOpaque_I(0xFF000000 + i14 * 256);
			}

			long j16 = frameTimes[i12] / 200000L;
			long j18 = tickTimes[i12] / 200000L;
			tessellator7.addVertex((double)((float)i12 + 0.5F), (double)((float)((long)this.displayHeight - j16) + 0.5F), 0.0D);
			tessellator7.addVertex((double)((float)i12 + 0.5F), (double)((float)this.displayHeight + 0.5F), 0.0D);
			tessellator7.setColorOpaque_I(0xFF000000 + i14 * 65536 + i14 * 256 + i14 * 1);
			tessellator7.addVertex((double)((float)i12 + 0.5F), (double)((float)((long)this.displayHeight - j16) + 0.5F), 0.0D);
			tessellator7.addVertex((double)((float)i12 + 0.5F), (double)((float)((long)this.displayHeight - (j16 - j18)) + 0.5F), 0.0D);
		}

		tessellator7.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void shutdown() {
		// Save the current world with the player's live position (and, via
		// World.saveLevel's sync, the canonical CurrentWorldId + exact Pos/Rotation
		// snapshot in the base level.dat) so quitting by any means resumes exactly.
		if(this.theWorld != null) {
			try {
				this.theWorld.saveWorldIndirectly(this.loadingScreen);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}

		this.running = false;
	}

	public void setIngameFocus() {
		if(Display.isActive()) {
			if(!this.inGameHasFocus) {
				this.inGameHasFocus = true;
				this.mouseHelper.grabMouseCursor();
				this.displayGuiScreen((GuiScreen)null);
				this.leftClickCounter = 10000;
				this.mouseTicksRan = this.ticksRan + 10000;
			}
		}
	}

	public void setIngameNotInFocus() {
		if(this.inGameHasFocus) {
			if(this.thePlayer != null) {
				this.thePlayer.resetPlayerKeyState();
			}

			this.inGameHasFocus = false;
			this.mouseHelper.ungrabMouseCursor();
		}
	}

	public void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void sendClickBlockToController(int i1, boolean z2) {
		
		if(!this.playerController.isInTestMode) {
			if(!z2) {
				this.leftClickCounter = 0;
			}

			if(i1 != 0 || this.leftClickCounter <= 0) {
				if(z2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && i1 == 0) {
					int i3 = this.objectMouseOver.blockX;
					int i4 = this.objectMouseOver.blockY;
					int i5 = this.objectMouseOver.blockZ;
					this.playerController.sendBlockRemoving(i3, i4, i5, this.objectMouseOver.sideHit);
					this.effectRenderer.addBlockHitEffects(i3, i4, i5, this.objectMouseOver.sideHit);
				} else {
					this.playerController.resetBlockRemoving();
				}

			}
		}
	}

	private void clickMouse(int i1) {
		boolean override = false;
		
		if(i1 != 0 || this.leftClickCounter <= 0) {
			if(i1 == 0) {
				this.thePlayer.swingItem();
			}

			boolean z2 = true;
			if(this.objectMouseOver == null) {
				/*
				if(i1 == 0 && !(this.playerController instanceof PlayerControllerTest)) {
					this.leftClickCounter = 10;
				}
				*/
				if (i1 == 0) {
					this.leftClickCounter = this.thePlayer.isCreative ? 3 : 10;
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
				if(i1 == 0) {
					this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}

				if(i1 == 1) {
					this.playerController.interactWithEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				int x = this.objectMouseOver.blockX;
				int y = this.objectMouseOver.blockY;
				int z = this.objectMouseOver.blockZ;
				int face = this.objectMouseOver.sideHit;

				float xWithinFace = (float) (this.objectMouseOver.hitVec.xCoord - (float)x);
				float yWithinFace = (float) (this.objectMouseOver.hitVec.yCoord - (float)y);
				float zWithinFace = (float) (this.objectMouseOver.hitVec.zCoord - (float)z);
				
				ItemStack itemStack = this.thePlayer.inventory.getCurrentItem();
				
				if(i1 == 0) {
					int blockId = this.theWorld.getBlockID(x, y, z);
					if(blockId == Block.worldPortal.blockID && this.theWorld.worldProvider != null && !this.theWorld.worldProvider.isNether && Item.isDiamondTierTool(itemStack)) {
						// Left-clicking a linked world portal with a diamond-tier tool travels to that world.
						if(this.theWorld.isRemote) {
							// Multiplayer: ask the server to perform the travel.
							((EntityClientPlayerMP)this.thePlayer).sendQueue.addToSendQueue(new Packet14BlockDig(0, x, y, z, face, itemStack, xWithinFace, yWithinFace, zWithinFace));
						} else {
							this.travelToDimension(this.theWorld.getBlockMetadata(x, y, z), false);
						}
						override = true;
						this.leftClickCounter = 20;
					} else {
						override = this.playerController.clickBlock(this.thePlayer, this.theWorld, itemStack, x, y, z, face, xWithinFace, yWithinFace, zWithinFace);
						if(override) this.leftClickCounter = 20;
					}
				} else {
					int stackSize = itemStack != null ? itemStack.stackSize : 0;
					if(this.playerController.sendPlaceBlock(this.thePlayer, this.theWorld, itemStack, x, y, z, face, xWithinFace, yWithinFace, zWithinFace)) {
						z2 = false;
						this.thePlayer.swingItem();
					}

					if(itemStack == null) {
						return ;
					}

					if(itemStack.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if(itemStack.stackSize != stackSize) {
						this.entityRenderer.itemRenderer.resetEquippedProgress();
					}
				}
			}

			if(z2 && i1 == 1) {
				ItemStack itemStack9 = this.thePlayer.inventory.getCurrentItem();
				if(itemStack9 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemStack9)) {
					this.entityRenderer.itemRenderer.resetEquippedProgress();
				}
			}

		}
		
	}

	public void toggleFullscreen() {
		try {
			this.fullscreen = !this.fullscreen;
			if(this.fullscreen) {
				//Display.setDisplayMode(Display.getDesktopDisplayMode());
				if(this.gameSettings.displayMode == null || "DEFAULT".equals(this.gameSettings.displayMode)) {
					Display.setDisplayMode(Display.getDesktopDisplayMode());
				} else {
					GraphicsMode graphicsMode = new GraphicsMode(this.gameSettings.displayMode);
					final DisplayMode[] displayModes = Display.getAvailableDisplayModes();
					boolean found = false;
					for(int i = 0; i < displayModes.length && !found; i ++) {
						DisplayMode mode = displayModes[i];
						if(mode.getWidth() == graphicsMode.w && mode.getHeight() == graphicsMode.h &&
								mode.getBitsPerPixel() == graphicsMode.d && mode.getFrequency() == graphicsMode.f) {
							System.out.println("Setting mode " + mode.getWidth() + "x" + mode.getHeight() + "x" + mode.getBitsPerPixel() + " " + mode.getFrequency() + "Hz");
							Display.setDisplayMode(mode);
							found = true;
						}
					}

					if(!found) Display.setDisplayMode(Display.getDesktopDisplayMode());
				}
				
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			} else {
				if(this.mcCanvas != null) {
					this.displayWidth = this.mcCanvas.getWidth();
					this.displayHeight = this.mcCanvas.getHeight();
				} else {
					this.displayWidth = this.tempDisplayWidth;
					this.displayHeight = this.tempDisplayHeight;
				}

				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			}

			if(this.currentScreen != null) {
				this.resize(this.displayWidth, this.displayHeight);
			}

			Display.setFullscreen(this.fullscreen);
			Display.update();
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}

	}

	private void resize(int i1, int i2) {
		if(i1 <= 0) {
			i1 = 1;
		}

		if(i2 <= 0) {
			i2 = 1;
		}

		this.displayWidth = i1;
		this.displayHeight = i2;
		if(this.currentScreen != null) {
			ScaledResolution scaledResolution3 = new ScaledResolution(this.gameSettings, i1, i2);
			int i4 = scaledResolution3.getScaledWidth();
			int i5 = scaledResolution3.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, i4, i5);
		}

	}

	private void clickMiddleMouseButton() {
		if(this.objectMouseOver != null) {
			int i1 = this.theWorld.getBlockID(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
			if(i1 == Block.grass.blockID) {
				i1 = Block.dirt.blockID;
			}

			if(i1 == Block.stairDouble.blockID) {
				i1 = Block.stairSingle.blockID;
			}

			if(i1 == Block.bedrock.blockID) {
				i1 = Block.stone.blockID;
			}

			this.thePlayer.inventory.setCurrentItem(i1, this.playerController instanceof PlayerControllerTest);
		}

	}

	/*
	private void startCheckingForTheCheque() {
		(new ThreadCheckHasPaid(this)).start();
	}
	*/

	public void runTick() {
		/*
		if(this.ticksRan == 6000) {
			this.startCheckingForTheCheque();
		}
		*/

		this.statFileWriter.func_27178_d();
		this.ingameGUI.updateTick();
		this.entityRenderer.getMouseOver(1.0F);
		int i3;

		if(!this.isGamePaused && this.theWorld != null) {
			this.playerController.updateController();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		if(!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}

		if(this.currentScreen == null && this.thePlayer != null) {
			if(this.thePlayer.health <= 0) {
				this.displayGuiScreen((GuiScreen)null);
			} else if(this.thePlayer.isPlayerSleeping() && this.theWorld != null && this.theWorld.isRemote) {
				this.displayGuiScreen(new GuiSleepMP(this));
			}
		} else if(this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen != null) {
			this.leftClickCounter = 10000;
			this.mouseTicksRan = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
			this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.guiParticles.update();
				this.currentScreen.updateScreen();
			}
		}

		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
			label301:
			while(true) {
				while(true) {
					while(true) {
						long j5;
						do {
							if(!Mouse.next()) {
								if(this.leftClickCounter > 0) {
									--this.leftClickCounter;
								}

								while(true) {
									while(true) {
										do {
											if(!Keyboard.next()) {
												// Fix to using item in pure beta codebase
												if(this.thePlayer.isUsingItem()) {
													if(!Mouse.isButtonDown(1)) {
														this.playerController.onStoppedUsingItem(this.thePlayer);
													}
												} else {
											
													if(this.currentScreen == null) {
														if(Mouse.isButtonDown(0) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
															this.clickMouse(0);
															this.mouseTicksRan = this.ticksRan;
														}
	
														if(Mouse.isButtonDown(1) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
															this.clickMouse(1);
															this.mouseTicksRan = this.ticksRan;
														}
													}
												}
												
												this.sendClickBlockToController(0, this.currentScreen == null && Mouse.isButtonDown(0) && this.inGameHasFocus);
												break label301;
											}

											this.thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState());
										} while(!Keyboard.getEventKeyState());

										if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
											this.toggleFullscreen();
										} else {
											if(this.currentScreen != null) {
												this.currentScreen.handleKeyboardInput();
											} else {
												if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
													this.displayInGameMenu();
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
													this.forceReload();
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F1) {
													this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F3) {
													this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
													this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F8) {
													this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
												}

												if(Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
													if (this.thePlayer.isCreative) {
														this.displayGuiScreen(new GuiContainerCreative(this.thePlayer));
													} else {
														this.displayGuiScreen(new GuiInventory(this.thePlayer));
													}
												}

												if(Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
													this.thePlayer.dropCurrentItem();
												}
												
												if(Keyboard.getEventKey() == this.gameSettings.keyBindCreative.keyCode && this.thePlayer.isCreative) {
													this.displayGuiScreen(new GuiCreativeInventory(this));
												}

												if((this.isRemote() || this.thePlayer.enableCheats) && Keyboard.getEventKey() == this.gameSettings.keyBindChat.keyCode) {
													this.displayGuiScreen(new GuiChat(this));
												}
											}

											for(int i6 = 0; i6 < 9; ++i6) {
												if(Keyboard.getEventKey() == Keyboard.KEY_1 + i6) {
													this.thePlayer.inventory.currentItem = i6;
												}
											}

											if(Keyboard.getEventKey() == this.gameSettings.keyBindToggleFog.keyCode) {
												this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
											}
										}
									}
								}
							}

							j5 = System.currentTimeMillis() - this.systemTime;
						} while(j5 > 200L);

						i3 = Mouse.getEventDWheel();
						if(i3 != 0) {
							this.thePlayer.inventory.changeCurrentItem(i3);
							if(this.gameSettings.field_22275_C) {
								if(i3 > 0) {
									i3 = 1;
								}

								if(i3 < 0) {
									i3 = -1;
								}

								this.gameSettings.field_22272_F += (float)i3 * 0.25F;
							}
						}

						if(this.currentScreen == null) {
							if(!this.inGameHasFocus && Mouse.getEventButtonState()) {
								this.setIngameFocus();
							} else {
								if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
									this.clickMouse(0);
									this.mouseTicksRan = this.ticksRan;
								}

								if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
									this.clickMouse(1);
									this.mouseTicksRan = this.ticksRan;
								}

								if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
									this.clickMiddleMouseButton();
								}
							}
						} else if(this.currentScreen != null) {
							this.currentScreen.handleMouseInput();
						}
					}
				}
			}
		}

		if(this.theWorld != null) {
			if(this.thePlayer != null) {
				++this.joinPlayerCounter;
				if(this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
			}

			this.theWorld.difficultySetting = this.gameSettings.difficulty;
			if(this.theWorld.isRemote) {
				this.theWorld.difficultySetting = 3;
			}

			this.theWorld.colouredAthmospherics = this.gameSettings.colouredAthmospherics;

			if(!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			if(!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			// I Swapped this block an the next
			if(!this.isGamePaused || this.isRemote()) {
				this.theWorld.setAllowedMobSpawns(this.gameSettings.difficulty > 0, true);
				this.theWorld.tick();
			}

			// Player current chunk coordinates are updated during world tick
			// And I need them to prune the entities to update list
			// theWorld may have been swapped to null mid-tick (e.g. a disconnect
			// handler running inside processReadPackets), so re-check it here.
			if(!this.isGamePaused && this.theWorld != null) {
				if(this.theWorld.lightningFlash > 0) {
					--this.theWorld.lightningFlash;
				}

				this.theWorld.updateEntities();
			}

			if(!this.isGamePaused && this.theWorld != null) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

if(!this.isGamePaused) {
this.effectRenderer.updateEffects();
		}
		}
		
		if(this.theWorld != null && this.thePlayer != null) {
			if(this.theWorld.thisSessionTicks == 40) {
				BiomeGenBase biome = this.theWorld.getBiomeGenAt((int)this.thePlayer.posX, (int)this.thePlayer.posZ);
				if(biome instanceof BiomeGenThemeHell) {
					this.thePlayer.triggerAchievement(AchievementList.themeHell);
				} else if(biome instanceof BiomeGenThemeForest) {
					this.thePlayer.triggerAchievement(AchievementList.themeForest);
				} else if(biome instanceof BiomeGenThemeParadise) {
					this.thePlayer.triggerAchievement(AchievementList.themeParadise);
				}  else if(biome instanceof BiomeGenThemeWhiteForest) {
					this.thePlayer.triggerAchievement(AchievementList.themeWForest);
				}
			}
		}

		this.systemTime = System.currentTimeMillis();
	}

	private void forceReload() {
		System.out.println("FORCING RELOAD!");
		this.sndManager = new SoundManager();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.downloadResourcesThread.reloadResources();
	}

	public boolean isRemote() {
		return this.theWorld != null && this.theWorld.isRemote;
	}

	public void startWorld(String forlderName, String worldName, WorldSettings worldSettings) {
		this.changeWorld((World)null);
		System.gc();
		
		BiomeGenBase.generateBiomeLookup();

		// §5.11 / M3. The single root player.dat decides which world to boot into
		// directly. Read it before any world is built: it holds the last session's
		// exact position/rotation/inventory plus CurrentWorldId (0 = main world,
		// 1 = the shared nether, 2..255 = a linked world). Missing, unreadable or
		// pointing at an absent world falls back to the main world below.
		PlayerSaveData.startSession();
		ISaveHandler baseHandler = null;
		File baseDir = null;
		try {
			baseHandler = this.saveLoader.getSaveLoader(forlderName, false);
			if(baseHandler != null) baseDir = baseHandler.getSaveDirectory();
		} catch(Exception e) {
			e.printStackTrace();
		}

		int resumeId = 0;
		com.mojang.nbt.NBTTagCompound resumePlayer = PlayerSaveData.readTag(baseDir);
		if(resumePlayer != null && resumePlayer.hasKey("CurrentWorldId")) {
			resumeId = resumePlayer.getInteger("CurrentWorldId");
		}
		if(resumeId < 0 || resumeId > PortalRegistry.LAST_WORLD_ID) {
			resumeId = 0;
		}

		System.gc();
		GlobalVars.initializeGameFlags();

		World world = null;
		String generating = "Generating Level";

		if(resumeId >= PortalRegistry.FIRST_WORLD_ID) {
			// Boot straight into the linked world under base/DIM-<id>.
			long baseSeed = this.readBaseWorldSeed(baseDir);
			boolean dimExists = baseDir != null && new File(baseDir, "DIM-" + resumeId + File.separator + "level.dat").isFile();
			if(dimExists && baseSeed != 0L) {
				long destSeed = WorldNameGen.deriveSeed(baseSeed, resumeId);
				String destName = WorldNameGen.getName(baseSeed, resumeId);
				ISaveHandler destHandler = this.saveLoader.getSaveLoader(forlderName + "/DIM-" + resumeId, false);
				WorldSettings destSettings = new WorldSettings(destSeed, 0, true, false, true, false, WorldType.DEFAULT);
				world = new World(destHandler, destName, destSettings, null);
				world.getWorldInfo().setDimension(resumeId);
				boolean brandNew = !world.getWorldInfo().isGenerated();
				String caption = "Entering " + destName;
				if(brandNew) {
					this.preloadWorld(world, caption, true, true);
					this.loadingScreen.displayLoadingString("Finding spawn point");
					world.worldProvider.setInitialSpawnLocation(world);
				} else {
					this.preloadWorld(world, caption, false, true);
				}
				System.out.println("Resuming in world " + resumeId);
				this.changeWorld(world, caption);
				this.thePlayer.currentWorldId = resumeId;
				this.placeResumedPlayer(world);
				return;
			}
			// The referenced linked world is gone: fall back to the main world and
			// consume the snapshot so a respawn cannot re-apply a stale position.
			System.out.println("Linked world " + resumeId + " not found; resuming in world 0");
			PlayerSaveData.markConsumed();
			resumeId = 0;
		} else if(resumeId == 1) {
			// Boot straight into the nether. It shares the base level.dat but keeps
			// its chunks under base/DIM-1, so a base-root handler + nether provider.
			long baseSeed = this.readBaseWorldSeed(baseDir);
			if(baseDir != null && new File(baseDir, "level.dat").isFile() && baseSeed != 0L) {
				String currentName = this.readBaseWorldName(baseDir);
				if(currentName != null) worldName = currentName;
				WorldSettings netherSettings = new WorldSettings(baseSeed, 0, true, false, true, false, WorldType.DEFAULT);
				world = new World(baseHandler, worldName, netherSettings, WorldProvider.createNetherProvider());
				if(world.isNewWorld) {
					world.worldProvider.setInitialSpawnLocation(world);
				}
				System.out.println("Resuming in the nether");
				this.preloadWorld(world, "Entering the Nether", false, true);
				this.changeWorld(world, "Entering the Nether");
				this.thePlayer.currentWorldId = 1;
				this.placeResumedPlayer(world);
				return;
			}
			System.out.println("Nether not found; resuming in world 0");
			PlayerSaveData.markConsumed();
			resumeId = 0;
		}

		// Main world (also the fallback when the resume target is missing).
		// Originally, vanilla Minecraft finds the spawn point at the end of this constructor:
		world = new World(baseHandler, worldName, worldSettings);
		
		// But I've removed it from there...
		boolean isNew = world.isNewWorld;
		this.preloadWorld(world, generating, isNew, false);
		
		// I need to call it once the whole world has been generated.
		if(isNew) {
			this.loadingScreen.displayLoadingString("Finding spawn point");
			world.worldProvider.setInitialSpawnLocation(world);
		}
		
		if(world.isNewWorld) {
			this.statFileWriter.readStat(StatList.createWorldStat, 1);
			this.statFileWriter.readStat(StatList.startGameStat, 1);
			this.changeWorld(world, "Generating level");
		} else {
			this.statFileWriter.readStat(StatList.loadWorldStat, 1);
			this.statFileWriter.readStat(StatList.startGameStat, 1);
			this.changeWorld(world, "Loading level");
		}
	}

	/**
	 * §5.11 / M3. After a direct boot into a linked world or the nether, place the
	 * player at the exact saved position (already hydrated from the root player.dat
	 * by changeWorld -> spawnPlayerWithLoadedChunks) and settle them onto the
	 * surface, mirroring the old resumeLastWorld post-travel dance.
	 */
	private void placeResumedPlayer(World world) {
		if(world == null || this.thePlayer == null) return;
		System.out.println("[RESUME][FIX] landed snapshot=(" + this.thePlayer.posX + "," + this.thePlayer.posY + "," + this.thePlayer.posZ + ") yaw=" + this.thePlayer.rotationYaw + " pitch=" + this.thePlayer.rotationPitch);
		this.thePlayer.setLocationAndAngles(this.thePlayer.posX, this.thePlayer.posY - (double)this.thePlayer.yOffset, this.thePlayer.posZ, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
		this.settlePlayerOnGround(world, this.thePlayer);
		world.updateEntityWithOptionalForce(this.thePlayer, false);
		world.joinEntityInSurroundings(this.thePlayer);
	}

	/**
	 * §5.11 / M3. After placing the player at the exact saved position, settle
	 * them down onto the surface immediately below within a small drop (at most
	 * 3 blocks). Without this, a resumed player briefly floats one eye-height
	 * above their saved spot for the first few ticks — the transient leaves the
	 * bounding box embedded in solid blocks (suffocation damage) and, because a
	 * quick save can capture it, permanently pollutes the stored position so
	 * every reload re-falls. Larger gaps (genuinely airborne saves) are left
	 * untouched so a mid-air quit is still honoured.
	 */
	private void settlePlayerOnGround(World world, EntityPlayer player) {
		double feet = player.posY - (double)player.yOffset;
		int x = MathHelper.floor_double(player.posX);
		int z = MathHelper.floor_double(player.posZ);
		int startY = MathHelper.floor_double(feet - 0.001D) - 1;
		for(int y = startY; y >= startY - 3; y--) {
			int id = world.getBlockID(x, y, z);
			if(id == 0) {
				continue;
			}
			Block block = Block.blocksList[id];
			if(block == null || !block.isOpaqueCube()) {
				continue;
			}
			double top = (double)(y + 1);
			double eye = top + (double)player.yOffset;
			System.out.println("[RESUME][SETTLE] floor=" + top + " drop=" + (feet - top));
			player.lastTickPosY = player.prevPosY = player.posY = eye;
			player.setPosition(player.posX, eye, player.posZ);
			player.onGround = true;
			player.fallDistance = 0.0F;
			return;
		}
	}

	public void usePortal() {
		if(this.theWorld == null || this.thePlayer == null) return;

		int worldId = this.theWorld.getWorldInfo().getWorldId();
		if(worldId == 1) {
			// Coming back out of the nether (or going into it from a linked world's
			// own nether). Use the remembered origin world, falling back to the main world.
			int target = this.thePlayer.netherReturnWorldId;
			File base = this.theWorld != null ? PortalRegistry.getBaseSaveDirectory(this.theWorld) : null;
			if(target < 2 || base == null || !new File(base, "DIM-" + target + File.separator + "level.dat").isFile()) {
				target = 0;
			}
			this.travelToDimension(target, false);
		} else {
			// Walking through a normal nether portal from a world (main or linked).
			this.thePlayer.netherReturnWorldId = worldId;
			this.travelToDimension(1, false);
		}
	}

	public void travelToDimension(int destId, boolean isResuming) {
		if(this.theWorld == null || this.thePlayer == null) return;
		if(this.isRemote()) return;

		int sourceId = this.theWorld.getWorldInfo().getWorldId();
		if(destId == sourceId) return;

		boolean toNether = destId == 1;
		boolean fromNether = sourceId == 1;

		WorldInfo sourceInfo = this.theWorld.getWorldInfo();

		// Save the source world BEFORE opening the destination save folder. Creating a
		// fresh SaveHandler writes a new session.lock; opening it while the source world
		// is still live would invalidate the source handler's lock check.
		this.theWorld.saveWorldIndirectly(this.loadingScreen);

		// Nether <-> world coordinate scaling (vanilla behaviour, kept intact).
		double d1 = this.thePlayer.posX;
		double d3 = this.thePlayer.posZ;
		if(fromNether) {
			if(WorldSize.xChunks >= 16 && WorldSize.zChunks >= 16) {
				d1 = (d1 - WorldSize.xChunks / 4) * 2;
				d3 = (d3 - WorldSize.zChunks / 4) * 2;
			}
		} else if(toNether) {
			if(WorldSize.xChunks >= 16 && WorldSize.zChunks >= 16) {
				d1 = WorldSize.xChunks / 4 + d1 / 2;
				d3 = WorldSize.zChunks / 4 + d3 / 2;
			} else {
				if(d1 == 0) d1 = 0;
				if(d1 >= WorldSize.xChunks - 1) d1 --;
				if(d3 == 0) d3 = 0;
				if(d3 >= WorldSize.zChunks - 1) d3 --;
			}
		}

		this.thePlayer.setLocationAndAngles(d1, this.thePlayer.posY, d3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
		if(this.thePlayer.isEntityAlive()) {
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
		}

		String baseName = this.getBaseWorldSaveFolderName(this.theWorld);
		World destWorld;

		if(toNether) {
			// Classic nether: nested world sharing the source handler when arriving from
			// the main world; a base-root handler when arriving from a linked world, so
			// nether chunks still live under the base folder's DIM-1.
			if(sourceId == 0) {
				destWorld = new World(this.theWorld, WorldProvider.createNetherProvider());
			} else {
				long baseSeed = PortalRegistry.getBaseSeed(this.theWorld);
				ISaveHandler baseHandler = this.saveLoader.getSaveLoader(baseName, false);
				destWorld = new World(baseHandler, WorldNameGen.getName(baseSeed, 0), new WorldSettings(baseSeed, 0, true, false, true, sourceInfo.isLayeredSand(), WorldType.DEFAULT), WorldProvider.createNetherProvider());
			}
			this.preloadWorld(destWorld, "Entering the Nether", true, !isResuming);
			if(destWorld.isNewWorld) destWorld.worldProvider.setInitialSpawnLocation(destWorld);
			this.changeWorld(destWorld, "Entering the Nether", this.thePlayer);
			this.thePlayer.worldObj = this.theWorld;
			this.thePlayer.currentWorldId = 1;
			if(this.thePlayer.isEntityAlive()) {
				this.thePlayer.setLocationAndAngles(d1, this.thePlayer.posY, d3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
				this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
				(new Teleporter()).setExitLocation(this.theWorld, this.thePlayer);
			}
			return;
		}

		// Main world (0) or a linked world (2..255), both stored as real sub-folders.
		long baseSeed = PortalRegistry.getBaseSeed(this.theWorld);
		String folder = destId == 0 ? baseName : baseName + "/DIM-" + destId;
		ISaveHandler destHandler = this.saveLoader.getSaveLoader(folder, false);
		long destSeed = WorldNameGen.deriveSeed(baseSeed, destId);
		String destName = WorldNameGen.getName(baseSeed, destId);
		if(destId == 0) {
			String currentName = this.readBaseWorldName(PortalRegistry.getBaseSaveDirectory(this.theWorld));
			if(currentName != null) destName = currentName;
		}

		String caption = fromNether ? "Leaving the Nether" : "Entering " + destName;
		destWorld = new World(destHandler, destName, new WorldSettings(destSeed, 0, true, false, true, sourceInfo.isLayeredSand(), WorldType.DEFAULT), null);
		destWorld.getWorldInfo().setDimension(destId);
		boolean brandNew = !destWorld.getWorldInfo().isGenerated();

		if(brandNew) {
			this.preloadWorld(destWorld, caption, true, true);
			this.loadingScreen.displayLoadingString("Finding spawn point");
			destWorld.worldProvider.setInitialSpawnLocation(destWorld);
		} else {
			this.preloadWorld(destWorld, caption, false, true);
		}

		this.changeWorld(destWorld, caption, this.thePlayer);
		this.thePlayer.worldObj = this.theWorld;
		this.thePlayer.currentWorldId = destId;
		if(!this.thePlayer.isEntityAlive()) return;

		if(fromNether) {
			// Arriving from the nether: find/create the vanilla nether portal in this world.
			(new Teleporter()).setExitLocation(this.theWorld, this.thePlayer);
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
		} else if(brandNew) {
			// First visit: build the return portal at the spawn point and stand the player on it.
			this.placeFirstWorldPortal(destWorld, sourceId);
		} else {
			// Re-entry: restore the remembered position, or the partner portal, if still usable.
			this.restoreLinkedWorldPosition(destWorld, sourceId);
		}
	}

	private void placeFirstWorldPortal(World world, int sourceId) {
		WorldInfo info = world.getWorldInfo();
		int sx = info.getSpawnX();
		int sy = info.getSpawnY();
		int sz = info.getSpawnZ();
		if(sy <= 1 || sy >= 127) {
			sy = 64;
		}

		// Replace the floor tile the player stands on with the return portal; keep
		// the landing tile (just above it) clear so the player can stand and act.
		world.setBlockAndMetadataWithNotify(sx, sy, sz, Block.worldPortal.blockID, sourceId);
		world.setBlockWithNotify(sx, sy + 1, sz, 0);

		info.setSpawn(sx, sy + 1, sz);
		info.setGenerated(true);
		world.saveWorldIndirectly(this.loadingScreen);

		this.thePlayer.setLocationAndAngles((double)sx + 0.5D, (double)(sy + 1), (double)sz + 0.5D, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
		this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
	}

	private void restoreLinkedWorldPosition(World world, int sourceId) {
		int[] portal = (new Teleporter()).findWorldPortalTo(world, sourceId);
		if(portal != null) {
			// Land the player on top of the partner portal (it is a floor tile).
			this.thePlayer.setLocationAndAngles((double)portal[0] + 0.5D, (double)(portal[1] + 1), (double)portal[2] + 0.5D, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
		} else {
			// No partner portal either; fall back to the world's natural spawn point.
			WorldInfo info = world.getWorldInfo();
			this.thePlayer.setLocationAndAngles((double)info.getSpawnX() + 0.5D, (double)(info.getSpawnY() + 1), (double)info.getSpawnZ() + 0.5D, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
		}
		this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
	}

	private String getBaseWorldSaveFolderName(World world) {
		if(world == null || world.getSaveHandler() == null) return null;
		File dir = world.getSaveHandler().getSaveDirectory();
		if(dir == null) return null;
		if(dir.getName().matches("DIM-\\d+")) {
			File parent = dir.getParentFile();
			return parent != null ? parent.getName() : dir.getName();
		}
		return dir.getName();
	}

	private String readBaseWorldName(File baseDir) {
		java.io.FileInputStream in = null;
		try {
			File file = new File(baseDir, "level.dat");
			if(file.isFile()) {
				in = new java.io.FileInputStream(file);
				com.mojang.nbt.NBTTagCompound root = com.mojang.nbt.CompressedStreamTools.readCompressed(in);
				if(root != null) {
					com.mojang.nbt.NBTTagCompound data = root.getCompoundTag("Data");
					if(data != null && data.hasKey("LevelName")) return data.getString("LevelName");
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if(in != null) in.close();
			} catch (java.io.IOException ignored) {
			}
		}
		return null;
	}

	private long readBaseWorldSeed(File baseDir) {
		java.io.FileInputStream in = null;
		try {
			File file = new File(baseDir, "level.dat");
			if(file.isFile()) {
				in = new java.io.FileInputStream(file);
				com.mojang.nbt.NBTTagCompound root = com.mojang.nbt.CompressedStreamTools.readCompressed(in);
				if(root != null) {
					com.mojang.nbt.NBTTagCompound data = root.getCompoundTag("Data");
					if(data != null && data.hasKey("RandomSeed")) return data.getLong("RandomSeed");
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if(in != null) in.close();
			} catch (java.io.IOException ignored) {
			}
		}
		return 0L;
	}

	public void changeWorld(World world1) {
		this.changeWorld(world1, "");
	}

	public void changeWorld(World world1, String string2) {
		this.changeWorld(world1, string2, (EntityPlayer)null);
	}

	public void changeWorld(World world, String caption, EntityPlayer entityPlayer) {
		this.statFileWriter.func_27175_b();
		this.statFileWriter.syncStats();
		this.renderViewEntity = null;
		this.loadingScreen.printText(caption);
		this.loadingScreen.displayLoadingString("");
		this.sndManager.playStreaming((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

		// If there is a current world, save it before proceeding
		if(this.theWorld != null) {
			System.out.println("Saving");
			this.theWorld.saveWorldIndirectly(this.loadingScreen);
		}

		// Release statics that pin the world being abandoned: an unfinished boss
		// fight marker and the render-pass model entities. The slider re-pins
		// currentBoss when its chunk reloads (EntitySlider.readEntityFromNBT), and
		// the model statics repopulate only when the new world renders those mobs.
		GlobalVars.currentBoss = null;
		ModelFlyingCow2.flyingcow = null;
		ModelFlyingPig2.pig = null;

		// Make new world the current world
		this.theWorld = world;

		if(world != null) {
			this.playerController.onWorldChange(world);
			
			if(!this.isRemote()) {
				if(entityPlayer == null) {
					this.thePlayer = (EntityPlayerSP)world.func_4085_a(EntityPlayerSP.class);
				}
			} else if(this.thePlayer != null) {
				this.thePlayer.preparePlayerToSpawn();
				if(world != null) {
					world.spawnEntityInWorld(this.thePlayer);
				}
			}

			if(this.thePlayer == null) {
				System.out.println("(RE)Creating player");
				this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(world);
				this.thePlayer.preparePlayerToSpawn();
				this.thePlayer.isCreative = this.gameSettings.isCreative;
				this.thePlayer.enableCheats = this.gameSettings.enableCheats;
				this.thePlayer.enableCraftingGuide = this.gameSettings.craftGuide;
				this.playerController.flipPlayer(this.thePlayer);
			}

this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);

			if(!this.isRemote() && entityPlayer != null && this.thePlayer != null && world != null) {
				// §5.11 (SP): a player handed in from another world must be registered in
				// the destination world like any spawned player. Vanilla b1.7.3 only ever
				// re-adds it to loadedEntityList (30-tick joinEntityInSurroundings drip);
				// without playerEntities membership the destination never writes its own
				// Player tag, never refreshes LastPosition, and the base-CVW canonical
				// snapshot (World.saveLevel) never runs for it.
				if(!world.playerEntities.contains(this.thePlayer)) {
					world.playerEntities.add(this.thePlayer);
					world.updateAllPlayersSleepingFlag();
				}
			}
			if(this.renderGlobal != null) {
				this.renderGlobal.changeWorld(world);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(world);
			}

this.playerController.func_6473_b(this.thePlayer);

			// §5.11: single-player reuses one EntityPlayer across all linked worlds for the
			// whole session; it already holds the live inventory/stats and is the source of
			// truth. Only a true (fresh) world load may re-hydrate the player from a
			// level.dat Player tag. Re-loading here during travel would clobber the live
			// player with the destination world's snapshot, so it is skipped when a player
			// is handed in; the landing code sets the exact position and registers the
			// player afterwards.
			if(entityPlayer == null) {
				world.spawnPlayerWithLoadedChunks(this.thePlayer);
			}

			if(world.isNewWorld) {
				System.out.println("Saving NEW world");
				world.saveWorldIndirectly(this.loadingScreen);
			}

			this.renderViewEntity = this.thePlayer;
		} else {
			this.thePlayer = null;
		}

		System.gc();
		this.systemTime = 0L;
		System.out.println("World changed");
	}

	/**
	 * Forces the whole level into memory before the game loop starts.
	 * <p>
	 * A brand-new overworld is generated and lit in one bulk pass (see
	 * {@code ChunkProvider.generateWholeWorld}). Existing worlds, and the nether dimension,
	 * keep the original chunk-by-chunk preload. The new world is saved later when
	 * {@code changeWorld} sees {@code world.isNewWorld}.
	 */
	private void preloadWorld(World world, String caption, boolean isNew, boolean isTravelling) {
		this.loadingScreen.printText(caption);
		this.loadingScreen.displayLoadingString(isNew ? "Building terrain" : "Loading terrain");

		if(isNew && (world.isNewWorld || world.worldProvider.worldType == -1 || (world.getWorldInfo() != null && !world.getWorldInfo().isGenerated()))) {
			// Client single-player always uses the modded finite-world ChunkProvider.
			// Theme-specific post generation runs at the end of generateWholeWorld.
			((ChunkProvider)world.chunkProvider).generateWholeWorld(this.loadingScreen);
		} else {
			BlockFire.dontSpread = true;
			int loadedChunks = 0;
			for(int chunkX = 0; chunkX < WorldSize.xChunks; chunkX ++) {
				for(int chunkZ = 0; chunkZ < WorldSize.zChunks; chunkZ ++) {
					this.loadingScreen.setLoadingProgress(loadedChunks++ * 100 / WorldSize.getTotalChunks());
					world.chunkProvider.prepareChunk(chunkX, chunkZ);
				}
			}
			BlockFire.dontSpread = false;
		}

		if(isTravelling) {
			this.loadingScreen.displayLoadingString("Simulating world for a bit");
			int wait = 1000 + world.rand.nextInt(1000);
			long startTime = System.currentTimeMillis();
			long endTime = startTime + wait;
			long curTime;
			while(endTime > (curTime = System.currentTimeMillis())) {
				this.loadingScreen.setLoadingProgress((int)(curTime - startTime) * 100 / wait);
			}
		}
	}

	public void installResource(String string1, File file2) {
		int i3 = string1.indexOf("/");
		String string4 = string1.substring(0, i3);
		string1 = string1.substring(i3 + 1);
		if(string4.equalsIgnoreCase("sound")) {
			this.sndManager.addSound(string1, file2);
		} else if(string4.equalsIgnoreCase("newsound")) {
			this.sndManager.addSound(string1, file2);
		} else if(string4.equalsIgnoreCase("streaming")) {
			this.sndManager.addStreaming(string1, file2);
		} else if(string4.equalsIgnoreCase("music")) {
			this.sndManager.addMusic(string1, file2);
		} else if(string4.equalsIgnoreCase("newmusic")) {
			this.sndManager.addMusic(string1, file2);
		}

	}

	public void installResourceURL(String name, URL url) {
		int i3 = name.indexOf("/");
		name = name.substring(i3 + 1);
		this.sndManager.addSoundURL(name, url);
	}

	public OpenGlCapsChecker getOpenGlCapsChecker() {
		return this.glCapabilities;
	}

	public String debugInfoRenders() {
		return this.renderGlobal.getDebugInfoRenders();
	}

	public String getEntityDebug() {
		return this.renderGlobal.getDebugInfoEntities();
	}

	public String getWorldProviderName() {
		return this.theWorld.getProviderName();
	}

	public String debugInfoEntities() {
		return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugLoadedEntities();
	}

	public void respawn(boolean z1, int i2) {
		if(!this.theWorld.isRemote && !this.theWorld.worldProvider.canRespawnHere()) {
			this.usePortal();
		}

		ChunkCoordinates chunkCoordinates3 = null;
		ChunkCoordinates chunkCoordinates4 = null;
		boolean z5 = true;
		if(this.thePlayer != null && !z1) {
			chunkCoordinates3 = this.thePlayer.getPlayerSpawnCoordinate();
			if(chunkCoordinates3 != null) {
				if(this.thePlayer.isDontCheckSpawnCoordinates()) {
					chunkCoordinates4 = chunkCoordinates3;
				} else {
					chunkCoordinates4 = EntityPlayer.verifyRespawnCoordinates(this.theWorld, chunkCoordinates3);
					if(chunkCoordinates4 == null) {
						this.thePlayer.addChatMessage("tile.bed.notValid");
					}
				}
			}
		}

		if(chunkCoordinates4 == null) {
			chunkCoordinates4 = this.theWorld.getSpawnPoint();
			z5 = false;
		}

		if(chunkCoordinates4 == null) {
			this.theWorld.setSpawnLocation();
		}
		
		this.theWorld.updateEntityList();
		int i8 = 0;
		if(this.thePlayer != null) {
			i8 = this.thePlayer.entityId;
			this.theWorld.setEntityDead(this.thePlayer);
		}

		this.renderViewEntity = null;

		// Copy permissions to the new player
		EntityPlayer oldPlayer = this.thePlayer;
		this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(this.theWorld);

		this.thePlayer.isCreative = oldPlayer.isCreative;
		this.thePlayer.enableCheats = oldPlayer.enableCheats;
		this.thePlayer.enableCraftingGuide = oldPlayer.enableCraftingGuide;

		this.thePlayer.dimension = i2;
		this.renderViewEntity = this.thePlayer;
		this.thePlayer.preparePlayerToSpawn();
		if(z5) {
			this.thePlayer.setPlayerSpawnCoordinate(chunkCoordinates3);
			this.thePlayer.setLocationAndAngles((double)((float)chunkCoordinates4.posX + 0.5F), (double)((float)chunkCoordinates4.posY + 0.1F), (double)((float)chunkCoordinates4.posZ + 0.5F), 0.0F, 0.0F);
		}

		this.playerController.flipPlayer(this.thePlayer);
		this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.entityId = i8;
		this.thePlayer.func_6420_o();
		this.playerController.func_6473_b(this.thePlayer);
		this.preloadWorld(this.theWorld, "Respawning", false, false);
		if(this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen)null);
		}

	}

	public static void startMainThread(String string0, String string1) {
		startMainThread(string0, string1, (String)null);
	}

	public static void startMainThread(String string0, String string1, String string2) {
		boolean z3 = false;
		Frame frame5 = new Frame("Minecraft");
		Canvas canvas6 = new Canvas();
		frame5.setLayout(new BorderLayout());
		frame5.add(canvas6, "Center");
		canvas6.setPreferredSize(new Dimension(852, 480));
		frame5.pack();
		frame5.setLocationRelativeTo((Component)null);
		MinecraftImpl minecraftImpl7 = new MinecraftImpl(frame5, canvas6, (MinecraftApplet)null, 854, 480, z3, frame5);
		Thread thread8 = new Thread(minecraftImpl7, "Minecraft main thread");
		thread8.setPriority(10);
		minecraftImpl7.minecraftUri = "www.minecraft.net";
		if(string0 != null && string1 != null) {
			minecraftImpl7.session = new User(string0, string1);
		} else {
			minecraftImpl7.session = new User("Player" + System.currentTimeMillis() % 1000L, "");
		}

		if(string2 != null) {
			String[] string9 = string2.split(":");
			minecraftImpl7.setServer(string9[0], Integer.parseInt(string9[1]));
		}

		frame5.setVisible(true);
		frame5.addWindowListener(new GameWindowListener(minecraftImpl7, thread8));
		thread8.start();
	}

	public NetClientHandler getSendQueue() {
		return this.thePlayer instanceof EntityClientPlayerMP ? ((EntityClientPlayerMP)this.thePlayer).sendQueue : null;
	}

	public static void main(String[] string0) {
		String string1 = null;
		String string2 = null;
		string1 = "Player" + System.currentTimeMillis() % 1000L;
		if(string0.length > 0) {
			string1 = string0[0];
		}

		string2 = "-";
		if(string0.length > 1) {
			string2 = string0[1];
		}

		startMainThread(string1, string2);
		//startLightingThread();
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	public static boolean isAmbientOcclusionEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion;
	}

	public static boolean isDebugInfoEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.showDebugInfo;
	}

	public boolean lineIsCommand(String string1) {
		if(string1.startsWith("/")) {
			;
		}

		return false;
	}

}
